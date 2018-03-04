package rpc;

import alice.logictuple.LogicTuple;
import alice.logictuple.exceptions.InvalidLogicTupleException;
import alice.tucson.api.AbstractTucsonAgent;
import alice.tucson.api.TucsonOperation;
import alice.tucson.api.TucsonTupleCentreIdDefault;
import alice.tucson.api.acc.NegotiationACC;
import alice.tucson.api.acc.OrdinaryAndSpecificationSyncACC;
import alice.tucson.api.TucsonMetaACC;
import alice.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tucson.api.exceptions.TucsonInvalidTupleCentreIdException;
import alice.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tucson.api.exceptions.UnreachableNodeException;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;
import alice.tuplecentre.core.AbstractTupleCentreOperation;

/**
 * Callee agent in a RPC scenario.
 *
 * @author s.mariani@unibo.it
 */
public class CalleeAgent extends AbstractTucsonAgent {

    /**
     * @param args
     *            no args expected.
     */
    public static void main(final String[] args) {
        try {
            new CalleeAgent("boris", "default@localhost:20504").go();
        } catch (final TucsonInvalidAgentIdException e) {
            e.printStackTrace();
        }
    }

    private OrdinaryAndSpecificationSyncACC acc;

    private TucsonTupleCentreIdDefault tid;

    /**
     * @param aid
     *            the name of the callee agent.
     * @param node
     *            the node used for RPC synchronization.
     *
     * @throws TucsonInvalidAgentIdException
     *             if the chosen Identifier is not a valid TuCSoN agent Identifier
     */
    public CalleeAgent(final String aid, final String node)
            throws TucsonInvalidAgentIdException {
        super(aid);
        try {
            this.tid = new TucsonTupleCentreIdDefault(node);
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
    public void operationCompleted(final TucsonOperation arg0) {
        /*
         * not used atm
         */
    }

    private long computeFactorial(final int arg) {
        if (arg == 0) {
            return 1;
        }
        return arg * this.computeFactorial(arg - 1);
    }

    @Override
    protected void main() {
        this.say("I'm started.");
        final NegotiationACC negAcc = TucsonMetaACC.getNegotiationContext(this
                .getTucsonAgentId());
        try {
            this.acc = negAcc.playDefaultRole();
            TucsonOperation op;
            LogicTuple req;
            int arg;
            Long result;
            /*
             * Ugly but that's so...
             */
            while (true) {
                /*
                 * Invocation phase (not TuCSoN invocation!).
                 */
                this.say("Waiting for remote calls...");
                op = this.acc.in(this.tid, LogicTuple
                        .parse("factorial(caller(Who)," + "arg(N))"), null);
                req = op.getLogicTupleResult();
                this.say("Call received from " + req.getArg("caller").getArg(0));
                arg = req.getArg("arg").getArg(0).intValue();
                /*
                 * Computation phase.
                 */
                this.say("Computing factorial...");
                result = this.computeFactorial(arg);
                /*
                 * Completion phase (not TuCSoN completion!).
                 */
                this.say("Call returns to " + req.getArg("caller").getArg(0));
                this.acc.out(
                        this.tid,
                        LogicTuple.parse("result(" + "caller("
                                + req.getArg("caller").getArg(0) + "),"
                                + "res(" + result + "))"), null);
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
        } catch (final TucsonInvalidAgentIdException e) {
            this.say("ERROR: Given Identifier is not a valid TuCSoN agent Identifier!");
        }
    }

}
