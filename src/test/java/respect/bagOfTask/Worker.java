package respect.bagOfTask;

import alice.logictuple.LogicTuple;
import alice.logictuple.TupleArgument;
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
 * Worker thread of a bag-of-task architecture. Given a TuCSoN Node (optional)
 * 1) it waits for jobs exploiting TuCSoN primitives timeout to proper terminate
 * (or to react to failures...) 2) it performs the correct computation
 * (summation/subtraction solely, not the average!) 3) then puts back in the
 * space the result.
 *
 * @author s.mariani@unibo.it
 */
public class Worker extends AbstractTucsonAgent {

    private final String ip;
    private final String port;

    public Worker(final String aid) throws TucsonInvalidAgentIdException {
        super(aid);
        this.ip = "localhost";
        this.port = "20504";
    }

    public Worker(final String aid, final String ip, final int port)
            throws TucsonInvalidAgentIdException {
        super(aid, ip, port);
        this.ip = ip;
        this.port = "" + port;
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

    private int sub(final TupleArgument arg, final TupleArgument arg2) {
        this.say("sub(" + arg.intValue() + "," + arg2.intValue() + ")...");
        /*
         * "Smart" subtraction.
         */
        if (arg.intValue() > arg2.intValue()) {
            return arg.intValue() - arg2.intValue();
        }
        return arg2.intValue() - arg.intValue();
    }

    private int sum(final TupleArgument arg, final TupleArgument arg2) {
        this.say("sum(" + arg.intValue() + "," + arg2.intValue() + ")...");
        return arg.intValue() + arg2.intValue();
    }

    @Override
    protected void main() {
        OrdinaryAndSpecificationSyncACC acc = null;
        try {
            final NegotiationACC negAcc = TucsonMetaACC
                    .getNegotiationContext(this.getTucsonAgentId());
            acc = negAcc.playDefaultRole();
            final TucsonTupleCentreIdDefault ttcid = new TucsonTupleCentreIdDefault(
                    "bagoftask", this.ip, this.port);
            LogicTuple taskTempl;
            TucsonOperation taskOp;
            LogicTuple task;
            int s;
            LogicTuple res;
            while (true) {
                taskTempl = LogicTuple.parse("task(OP)");
                this.say("Waiting for task...");
                /*
                 * Usage of timeouts: be careful that timeout extinction DOES
                 * NOT IMPLY operation removal from TuCSoN Node!
                 */
                taskOp = acc.in(ttcid, taskTempl, 10000L);
                task = taskOp.getLogicTupleResult();
                /*
                 * Perform the correct computation.
                 */
                if (task.getArg(0).getName().equals("sum")) {
                    s = this.sum(task.getArg("sum").getArg(0),
                            task.getArg("sum").getArg(1));
                } else {
                    s = this.sub(task.getArg("sub").getArg(0),
                            task.getArg("sub").getArg(1));
                }
                if (s == -1) {
                    this.say("Something went wrong, don't really care");
                }
                /*
                 * Put back result.
                 */
                res = LogicTuple.parse("res(" + s + ")");
                this.say("Injecting result: " + res + "...");
                acc.out(ttcid, res, null);
                // Thread.sleep(1000);
            }
        } catch (final OperationTimeOutException e) {
            this.say("Timeout exceeded, I quit");
        } catch (final TucsonInvalidTupleCentreIdException e) {
            e.printStackTrace();
        } catch (final InvalidLogicTupleException e) {
            e.printStackTrace();
        } catch (final TucsonOperationNotPossibleException e) {
            e.printStackTrace();
        } catch (final UnreachableNodeException e) {
            e.printStackTrace();
        } catch (final TucsonInvalidAgentIdException e) {
            e.printStackTrace();
        } finally {
            if (acc != null) {
                try {
                    acc.exit();
                } catch (final TucsonOperationNotPossibleException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
