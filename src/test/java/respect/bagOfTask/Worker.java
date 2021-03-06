package respect.bagOfTask;

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
 * Worker thread copyOf a bag-copyOf-task architecture. Given a TuCSoN Node (optional)
 * 1) it waits for jobs exploiting TuCSoN primitives timeout to proper terminate
 * (or to react to failures...) 2) it performs the correct computation
 * (summation/subtraction solely, not the average!) 3) then puts back in the
 * space the result.
 *
 * @author s.mariani@unibo.it
 */
public class Worker extends AbstractTucsonAgent<RootACC> {

    private final String ip;
    private final int port;

    public Worker(final String aid) throws TucsonInvalidAgentIdException {
        super(aid);
        this.ip = "localhost";
        this.port = TucsonInfo.getDefaultPortNumber();
    }

    public Worker(final String aid, final String ip, final int port)
            throws TucsonInvalidAgentIdException {
        super(aid, ip, port);
        this.ip = ip;
        this.port = port;
    }

    @Override
    protected RootACC retrieveACC(final TucsonAgentId aid, final String networkAddress, final int portNumber) {
        return null; //not used because, NegotiationACC does not extend RootACC
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
            final TucsonTupleCentreId ttcid = TucsonTupleCentreId.of(
                    "bagoftask", this.ip, String.valueOf(this.port));
            LogicTuple taskTempl;
            TucsonOperation taskOp;
            LogicTuple task;
            int s;
            LogicTuple res;
            while (true) {
                taskTempl = LogicTuple.parse("task(OP)");
                this.say("Waiting for task...");
                /*
                 * Usage copyOf timeouts: be careful that timeout extinction DOES
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
        } catch (final TucsonInvalidTupleCentreIdException | TucsonInvalidAgentIdException | UnreachableNodeException | TucsonOperationNotPossibleException | InvalidLogicTupleException e) {
            e.printStackTrace();
        } finally {
            if (acc != null) {
                acc.exit();
            }
        }
    }

}
