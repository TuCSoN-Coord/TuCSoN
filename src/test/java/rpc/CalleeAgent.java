package rpc;

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
 * Callee agent in a RPC scenario.
 *
 * @author s.mariani@unibo.it
 */
public class CalleeAgent extends AbstractTucsonAgent<RootACC> {

    /**
     * @param args no args expected.
     */
    public static void main(final String[] args) {
        try {
            new CalleeAgent("boris", "default@localhost:" + TucsonInfo.getDefaultPortNumber()).go();
        } catch (final TucsonInvalidAgentIdException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    private OrdinaryAndSpecificationSyncACC acc;

    private TucsonTupleCentreId tid;

    /**
     * @param aid  the name of the callee agent.
     * @param node the node used for RPC synchronization.
     * @throws TucsonInvalidAgentIdException if the chosen Identifier is not a valid TuCSoN agent Identifier
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

    @Override
    protected RootACC retrieveACC(final TucsonAgentId aid, final String networkAddress, final int portNumber) {
        return null; //not used because, NegotiationACC does not extend RootACC
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
                op = this.acc.in(this.tid, LogicTuples.parse("factorial(caller(Who)," + "arg(N))"), null);
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
                        LogicTuples.parse("result(" + "caller("
                                + req.getArg("caller").getArg(0) + "),"
                                + "res(" + result + "))"), null);
            }
        } catch (final InvalidLogicTupleException e) {
            this.say("ERROR: Tuple is not an admissible Prolog term!");
            LOGGER.error(e.getMessage(), e);
        } catch (final TucsonOperationNotPossibleException e) {
            this.say("ERROR: Never seen this happen before *_*");
        } catch (final UnreachableNodeException e) {
            this.say("ERROR: Given TuCSoN Node is unreachable!");
            LOGGER.error(e.getMessage(), e);
        } catch (final OperationTimeOutException e) {
            this.say("ERROR: Endless timeout expired!");
        } catch (final TucsonInvalidAgentIdException e) {
            this.say("ERROR: Given Identifier is not a valid TuCSoN agent Identifier!");
        }
    }

}
