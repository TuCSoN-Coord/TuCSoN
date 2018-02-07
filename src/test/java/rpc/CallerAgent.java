package rpc;

import alice.logictuple.LogicTuple;
import alice.logictuple.TupleArgument;
import alice.logictuple.exceptions.InvalidLogicTupleException;
import alice.tucson.api.AbstractTucsonAgent;
import alice.tucson.api.ITucsonOperation;
import alice.tucson.api.NegotiationACC;
import alice.tucson.api.SynchACC;
import alice.tucson.api.TucsonMetaACC;
import alice.tucson.api.TucsonTupleCentreId;
import alice.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tucson.api.exceptions.TucsonInvalidTupleCentreIdException;
import alice.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tucson.api.exceptions.UnreachableNodeException;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;
import alice.tuplecentre.core.AbstractTupleCentreOperation;

/**
 * Caller agent in a RPC scenario.
 *
 * @author s.mariani@unibo.it
 */
public class CallerAgent extends AbstractTucsonAgent {

    /**
     * @param args
     *            no args expected.
     */
    public static void main(final String[] args) {
        try {
            new CallerAgent("vlad", "default@localhost:20504").go();
        } catch (final TucsonInvalidAgentIdException e) {
            e.printStackTrace();
        }
    }

    private SynchACC acc;
    private final int MAX_FACT = 20;

    private TucsonTupleCentreId tid;

    /**
     * @param aid
     *            the name of the caller agent.
     * @param node
     *            the node used for RPC synchronization.
     *
     * @throws TucsonInvalidAgentIdException
     *             if the chosen ID is not a valid TuCSoN agent ID
     */
    public CallerAgent(final String aid, final String node)
            throws TucsonInvalidAgentIdException {
        super(aid);
        try {
            this.tid = new TucsonTupleCentreId(node);
        } catch (final TucsonInvalidTupleCentreIdException e) {
            this.say("Invalid tid given, killing myself...");
        }
    }

    /*
     * (non-Javadoc)
     * @see
     * alice.tucson.api.AbstractTucsonAgent#operationCompleted(alice.tuplecentre
     * .core.AbstractTupleCentreOperation)
     */
    @Override
    public void operationCompleted(final AbstractTupleCentreOperation arg0) {
        /*
         * not used atm
         */
    }

    @Override
    public void operationCompleted(final ITucsonOperation arg0) {
        /*
         * not used atm
         */
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
            ITucsonOperation op;
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
                        LogicTuple.parse("factorial(caller(" + this.myName()
                                + ")," + "arg(" + arg + "))"), null);
                /*
                 * Completion phase (not TuCSoN completion!).
                 */
                op = this.acc.in(
                        this.tid,
                        LogicTuple.parse("result(caller(" + this.myName()
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
            this.say("ERROR: Given ID is not a valid TuCSoN agent ID!");
        }
    }

}
