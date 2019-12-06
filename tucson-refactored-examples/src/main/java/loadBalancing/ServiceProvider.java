package loadBalancing;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import alice.tuple.logic.LogicTuple;
import alice.tuple.logic.exceptions.InvalidLogicTupleException;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;
import alice.tuplecentre.core.AbstractTupleCentreOperation;
import alice.tuplecentre.respect.api.TupleCentreId;
import alice.tuplecentre.respect.api.exceptions.InvalidTupleCentreIdException;
import alice.tuplecentre.tucson.api.AbstractTucsonAgent;
import alice.tuplecentre.tucson.api.TucsonAgentId;
import alice.tuplecentre.tucson.api.TucsonMetaACC;
import alice.tuplecentre.tucson.api.TucsonOperation;
import alice.tuplecentre.tucson.api.acc.NegotiationACC;
import alice.tuplecentre.tucson.api.acc.OrdinarySyncACC;
import alice.tuplecentre.tucson.api.acc.RootACC;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tuplecentre.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tuplecentre.tucson.api.exceptions.UnreachableNodeException;

/**
 * Dummy Service Provider class to show some 'adaptive' features related to
 * usage of uniform primitives. TuCSoN Agent composed by 2 threads: main)
 * advertise its offered service and processes incoming requests taking them
 * from a private input queue; Receiver) waits for incoming requests and puts
 * them into main thread own queue.
 *
 * @author s.mariani@unibo.it
 */
public class ServiceProvider extends AbstractTucsonAgent {

    class Receiver extends Thread {

        @Override
        public void run() {
            LogicTuple res;
            TucsonOperation op;
            this.say("Waiting for requests...");
            final LogicTuple templ;
            try {
                templ = LogicTuple.parse("req("
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
            } catch (InvalidLogicTupleException e) {
                e.printStackTrace();
            } catch (UnreachableNodeException e) {
                e.printStackTrace();
            } catch (TucsonOperationNotPossibleException e) {
                e.printStackTrace();
            } catch (OperationTimeOutException e) {
                e.printStackTrace();
            }

        }

        private void say(final String msg) {
            System.out.println("\t[Receiver]: " + msg);
        }
    }

    /**
     * @param args no args expected.
     */
    public static void main(final String[] args) throws InvalidLogicTupleException, TucsonInvalidAgentIdException, InvalidTupleCentreIdException {
        new ServiceProvider("provider1", "default@localhost:20504", 5000)
                .go();
        new ServiceProvider("provider2", "default@localhost:20504", 3000).go();
        new ServiceProvider("provider3", "default@localhost:20504", 1000)
                .go();
    }

    private static void serveRequest(final long time) throws InterruptedException {
        Thread.sleep(time);
    }

    private OrdinarySyncACC acc;
    private boolean die;
    private final LinkedBlockingQueue<LogicTuple> inputQueue;

    private LogicTuple service;

    private final long serviceTime;

    private TupleCentreId tid;

    /**
     * @param aid     agent name
     * @param node    node where to advertise services
     * @param cpuTime to simulate computational power
     * @throws TucsonInvalidAgentIdException if the chosen ID is not a valid TuCSoN agent ID
     */
    public ServiceProvider(final String aid, final String node,
                           final long cpuTime) throws TucsonInvalidAgentIdException, InvalidTupleCentreIdException, InvalidLogicTupleException {
        super(aid);
        this.die = false;
        this.tid = new TupleCentreId(node);
        this.service = LogicTuple.parse("ad(" + aid + ")");
        this.say("I'm started.");
        this.inputQueue = new LinkedBlockingQueue<LogicTuple>(10);
        this.serviceTime = cpuTime;
    }

    @Override
    protected RootACC retrieveACC(TucsonAgentId aid, String networkAddress, int portNumber) throws Exception {
        return null;
    }

    @Override
    protected void main() throws OperationTimeOutException, TucsonInvalidAgentIdException, UnreachableNodeException, TucsonOperationNotPossibleException, InvalidLogicTupleException {
        final NegotiationACC negAcc = TucsonMetaACC
                .getNegotiationContext(this.getTucsonAgentId());
        this.acc = negAcc.playDefaultRole();
        new Receiver().start();
        TucsonOperation op;
        LogicTuple req;
        final LogicTuple dieTuple = LogicTuple.parse("die(" + this.getTucsonAgentId().getLocalName()
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
    }

}
