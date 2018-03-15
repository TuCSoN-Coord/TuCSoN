package uniform.loadBalancing;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import alice.tuple.logic.LogicTuple;
import alice.tuple.logic.LogicTuples;
import alice.tuple.logic.exceptions.InvalidLogicTupleException;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;
import alice.tuplecentre.tucson.api.AbstractTucsonAgent;
import alice.tuplecentre.tucson.api.TucsonAgentId;
import alice.tuplecentre.tucson.api.TucsonMetaACC;
import alice.tuplecentre.tucson.api.TucsonOperation;
import alice.tuplecentre.tucson.api.TucsonTupleCentreId;
import alice.tuplecentre.tucson.api.TucsonTupleCentreIdDefault;
import alice.tuplecentre.tucson.api.acc.NegotiationACC;
import alice.tuplecentre.tucson.api.acc.OrdinaryAndSpecificationSyncACC;
import alice.tuplecentre.tucson.api.acc.RootACC;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidTupleCentreIdException;
import alice.tuplecentre.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tuplecentre.tucson.api.exceptions.UnreachableNodeException;
import alice.tuplecentre.tucson.service.TucsonInfo;

/**
 * Dummy Service Provider class to show some 'adaptive' features related to
 * usage of uniform primitives. TuCSoN Agent composed by 2 threads: main)
 * advertise its offered service and processes incoming requests taking them
 * from a private input queue; Receiver) waits for incoming requests and puts
 * them into main thread own queue.
 *
 * @author s.mariani@unibo.it
 */
public class ServiceProvider extends AbstractTucsonAgent<RootACC> {

    class Receiver extends Thread {

        @Override
        public void run() {
            LogicTuple res;
            TucsonOperation op;
            this.say("Waiting for requests...");
            try {
                final LogicTuple templ = LogicTuples.parse("req("
                        + ServiceProvider.this.service.getArg(0) + ")");
                while (!ServiceProvider.this.die) {
                    op = ServiceProvider.this.acc.in(ServiceProvider.this.tid,
                            templ, null);
                    if (op.isResultSuccess()) {
                        res = op.getLogicTupleResult();
                        this.say("Enqueuing request: " + res.toString());
                        /*
                         * We enqueue received request.
                         */
                        try {
                            ServiceProvider.this.inputQueue.add(res);
                        } catch (final IllegalStateException e) {
                            this.say("Queue is full, dropping request...");
                        }
                    }
                }
            } catch (final InvalidLogicTupleException e) {
                this.say("ERROR: Tuple is not an admissible Prolog term!");
            } catch (final TucsonOperationNotPossibleException e) {
                this.say("ERROR: Never seen this happen before *_*");
            } catch (final UnreachableNodeException e) {
                this.say("ERROR: Given TuCSoN Node is unreachable!");
            } catch (final OperationTimeOutException e) {
                this.say("ERROR: Endless timeout expired!");
            }
        }

        private void say(final String msg) {
            System.out.println("\t[Receiver]: " + msg);
        }
    }

    /**
     * @param args
     *            no args expected.
     */
    public static void main(final String[] args) {
        try {
            new ServiceProvider("provider1", "default@localhost" + TucsonInfo.getDefaultPortNumber(), 5000)
            .go();
            new ServiceProvider("provider2", "default@localhost" + TucsonInfo.getDefaultPortNumber(), 3000).go();
            new ServiceProvider("provider3", "default@localhost" + TucsonInfo.getDefaultPortNumber(), 1000)
            .go();
        } catch (final TucsonInvalidAgentIdException e) {
            e.printStackTrace();
        }
    }

    private static void serveRequest(final long time) {
        try {
            Thread.sleep(time);
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
    }

    private OrdinaryAndSpecificationSyncACC acc;
    private boolean die;
    private final LinkedBlockingQueue<LogicTuple> inputQueue;

    private LogicTuple service;

    private final long serviceTime;

    private TucsonTupleCentreId tid;

    /**
     * @param aid
     *            agent name
     * @param node
     *            node where to advertise services
     * @param cpuTime
     *            to simulate computational power
     *
     * @throws TucsonInvalidAgentIdException
     *             if the chosen Identifier is not a valid TuCSoN agent Identifier
     */
    public ServiceProvider(final String aid, final String node,
            final long cpuTime) throws TucsonInvalidAgentIdException {
        super(aid);
        this.die = false;
        try {
            this.tid = new TucsonTupleCentreIdDefault(node);
            this.service = LogicTuples.parse("ad(" + aid + ")");
            this.say("I'm started.");
        } catch (final TucsonInvalidTupleCentreIdException e) {
            this.say("Invalid tid given, killing myself...");
            this.die = true;
        } catch (final InvalidLogicTupleException e) {
            this.say("Invalid service given, killing myself...");
            this.die = true;
        }
        this.inputQueue = new LinkedBlockingQueue<LogicTuple>(10);
        this.serviceTime = cpuTime;
    }

    @Override
    protected RootACC retrieveACC(final TucsonAgentId aid, final String networkAddress, final int portNumber) {
        return null; //not used because, NegotiationACC does not extend RootACC
    }

    @Override
    protected void main() {
        try {
            final NegotiationACC negAcc = TucsonMetaACC
                    .getNegotiationContext(this.getTucsonAgentId());
            this.acc = negAcc.playDefaultRole();
            new Receiver().start();
            TucsonOperation op;
            LogicTuple req;
            final LogicTuple dieTuple = LogicTuples.parse("die(" + this.getTucsonAgentId().getLocalName()
                    + ")");
            while (!this.die) {
                this.say("Checking termination...");
                op = this.acc.inp(this.tid, dieTuple, null);
                if (op.isResultSuccess()) {
                    this.die = true;
                    continue;
                }
                /*
                 * Service advertisement phase.
                 */
                this.say("Advertising service: " + this.service.toString());
                this.acc.out(this.tid, this.service, null);
                /*
                 * Request servicing phase.
                 */
                boolean empty = true;
                try {
                    this.say("Polling queue for requests...");
                    while (empty) {
                        req = this.inputQueue.poll(1, TimeUnit.SECONDS);
                        if (req != null) {
                            empty = false;
                            this.say("Serving request: " + req.toString());
                            /*
                             * We simulate computational power of the Service
                             * Provider.
                             */
                            ServiceProvider.serveRequest(this.serviceTime);
                            /*
                             * Dummy 'positive feedback' mechanism.
                             */
                            this.say("Feedback to service: "
                                    + this.service.toString());
                            this.acc.out(this.tid, this.service, null);
                        }
                    }
                } catch (final InterruptedException e) {
                    e.printStackTrace();
                }
            }
            this.say("Someone killed me, bye!");
        } catch (final InvalidLogicTupleException e) {
            this.say("ERROR: Tuple is not an admissible Prolog term!");
        } catch (final TucsonOperationNotPossibleException e) {
            this.say("ERROR: Never seen this happen before *_*");
        } catch (final UnreachableNodeException e) {
            this.say("ERROR: Given TuCSoN Node is unreachable!");
        } catch (final OperationTimeOutException e) {
            this.say("ERROR: Endless timeout expired!");
        } catch (final TucsonInvalidAgentIdException e1) {
            this.say("ERROR: Given Identifier is not a valid agent Identifier!");
        }
    }

}
