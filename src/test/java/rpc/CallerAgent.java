package rpc;

import alice.tuple.logic.LogicTuple;
import alice.tuple.logic.TupleArgument;
import alice.tuple.logic.exceptions.InvalidLogicTupleException;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;
import alice.tuplecentre.tucson.api.AbstractTucsonAgent;
import alice.tuplecentre.tucson.api.TucsonAgentId;
import alice.tuplecentre.tucson.api.TucsonMetaACC;
import alice.tuplecentre.tucson.api.TucsonOperation;
import alice.tuplecentre.tucson.api.TucsonTupleCentreId;
import alice.tuplecentre.tucson.api.acc.NegotiationACC;
import alice.tuplecentre.tucson.api.acc.OrdinaryAndSpecificationSyncACC;
import alice.tuplecentre.tucson.api.acc.RootACC;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidTupleCentreIdException;
import alice.tuplecentre.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tuplecentre.tucson.api.exceptions.UnreachableNodeException;
import alice.tuplecentre.tucson.service.TucsonInfo;

/**
 * Caller agent in a RPC scenario.
 *
 * @author s.mariani@unibo.it
 */
public class CallerAgent extends AbstractTucsonAgent<RootACC> {

    /**
     * @param args
     *            no args expected.
     */
    public static void main(final String[] args) {
        try {
            new CallerAgent("vlad", "default@localhost:" + TucsonInfo.getDefaultPortNumber()).go();
        } catch (final TucsonInvalidAgentIdException e) {
            e.printStackTrace();
        }
    }

    private OrdinaryAndSpecificationSyncACC acc;
    private final int MAX_FACT = 20;

    private TucsonTupleCentreId tid;

    /**
     * @param aid
     *            the name copyOf the caller agent.
     * @param node
     *            the node used for RPC synchronization.
     *
     * @throws TucsonInvalidAgentIdException
     *             if the chosen Identifier is not a valid TuCSoN agent Identifier
     */
    public CallerAgent(final String aid, final String node)
            throws TucsonInvalidAgentIdException {
        super(aid);
        try {
            this.tid = TucsonTupleCentreId.of(node);
        } catch (final TucsonInvalidTupleCentreIdException e) {
            this.say("Invalid tid given, killing myself...");
        }
    }

    @Override
    protected RootACC retrieveACC(final TucsonAgentId aid, final String networkAddress, final int portNumber) {
        return null; //not used because, NegotiationACC does not extend RootACC
    }

    private int drawRandomInt() {
        return (int) Math.round(Math.random() * this.MAX_FACT);
    }

    @Override
    protected void main() {
        this.say("I'm started.");
        final NegotiationACC negAcc = TucsonMetaACC.getNegotiationContext(this
                .getTucsonAgentId());
        try {
            this.acc = negAcc.playDefaultRole();
            TucsonOperation op;
            LogicTuple reply;
            int arg;
            /*
             * Ugly but that's so...
             */
            while (true) {
                /*
                 * Invocation phase (not TuCSoN invocation!).
                 */
                arg = this.drawRandomInt();
                this.say("Calling factorial computation for " + arg + "...");
                this.acc.out(
                        this.tid,
                        LogicTuple.parse("factorial(caller(" + this.getTucsonAgentId().getLocalName()
                                + ")," + "arg(" + arg + "))"), null);
                /*
                 * Completion phase (not TuCSoN completion!).
                 */
                op = this.acc.in(
                        this.tid,
                        LogicTuple.parse("result(caller(" + this.getTucsonAgentId().getLocalName()
                                + ")," + "res(R))"), null);
                reply = op.getLogicTupleResult();
                final TupleArgument res = reply.getArg("res").getArg(0);
                if (res.isLong()) {
                    this.say("Result received is "
                            + reply.getArg("res").getArg(0).longValue());
                } else if (res.isInt()) {
                    this.say("Result received is "
                            + reply.getArg("res").getArg(0).intValue());
                } else {
                    this.say("Result received is "
                            + reply.getArg("res").getArg(0));
                }
                Thread.sleep(3000);
            }
        } catch (final InvalidLogicTupleException e) {
            this.say("ERROR: Tuple is not an admissible Prolog term!");
            e.printStackTrace();
        } catch (final TucsonOperationNotPossibleException e) {
            this.say("ERROR: Never seen this happen before *_*");
        } catch (final UnreachableNodeException e) {
            this.say("ERROR: Given TuCSoN Node is unreachable!");
            e.printStackTrace();
        } catch (final OperationTimeOutException e) {
            this.say("ERROR: Endless timeout expired!");
        } catch (final InterruptedException e) {
            this.say("ERROR: Sleep interrupted!");
        } catch (final TucsonInvalidAgentIdException e) {
            this.say("ERROR: Given Identifier is not a valid TuCSoN agent Identifier!");
        }
    }

}
