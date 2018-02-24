package respect.bagOfTask;

import java.util.Random;
import alice.logictuple.LogicTuple;
import alice.logictuple.exceptions.InvalidLogicTupleException;
import alice.tucson.api.AbstractTucsonAgent;
import alice.tucson.api.TucsonOperation;
import alice.tucson.api.acc.NegotiationACC;
import alice.tucson.api.acc.SyncACC;
import alice.tucson.api.TucsonMetaACC;
import alice.tucson.api.TucsonTupleCentreId;
import alice.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tucson.api.exceptions.TucsonInvalidTupleCentreIdException;
import alice.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tucson.api.exceptions.UnreachableNodeException;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;
import alice.tuplecentre.core.AbstractTupleCentreOperation;

/**
 * Master thread of a bag-of-task architecture. Given a TuCSoN Node (optional)
 * 1) it programs the specification space so as to perform an average
 * computation; 2) it submits jobs at random regarding summation/subtraction
 * computation (to be carried out by workers); 3) then collects results of such
 * tasks and the average (computed by the tuplecentre itself thanks to ReSpecT
 * reactions).
 *
 * @author s.mariani@unibo.it
 */
public class Master extends AbstractTucsonAgent {

    private static final int ITERs = 10;
    private final String ip;
    private final String port;
    /*
     * To randomly choose between summation and subtraction.
     */
    private final Random r = new Random();

    public Master(final String aid) throws TucsonInvalidAgentIdException {
        super(aid);
        this.ip = "localhost";
        this.port = "20504";
    }

    public Master(final String aid, final String ip, final int port)
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

    @Override
    protected void main() {
        try {
            final NegotiationACC negAcc = TucsonMetaACC
                    .getNegotiationContext(this.getTucsonAgentId());
            final SyncACC acc = negAcc.playDefaultRole();
            LogicTuple task = null;

            /*
             * Our work has to be done in a custom-defined tuplecentre.
             */
            final TucsonTupleCentreId ttcid = new TucsonTupleCentreId(
                    "bagoftask", this.ip, this.port);
            this.say("Injecting ReSpecT Specification...");
            /*
             * First ReSpecT specification tuple: whenever a res(...) is
             * submitted by a worker, if no result(...) tuple exists, create it.
             */
            acc.outS(
                    ttcid,
                    LogicTuple.parse("out(res(R))"),
                    LogicTuple.parse("(completion,success)"),
                    LogicTuple
                    .parse("(no(result(Res,Count)), in(res(R)), out(result(R,1)))"),
                    null);
            /*
             * Second ReSpecT specification tuple: whenever a res(...) is
             * submitted by a worker, if a result(...) tuple exists, update it.
             */
            acc.outS(
                    ttcid,
                    LogicTuple.parse("out(res(R))"),
                    LogicTuple.parse("(completion,success)"),
                    LogicTuple
                    .parse("(in(result(Res,Count)), in(res(R)),"
                            + "NR is Res+R, NC is Count+1, out(result(NR,NC)))"),
                            null);
            /*
             * Start tasks submission cycle...
             */
            for (int i = 0; i < Master.ITERs; i++) {
                if (this.r.nextBoolean()) {
                    task = LogicTuple.parse("task(" + "sum("
                            + this.r.nextInt(Master.ITERs) + ","
                            + this.r.nextInt(Master.ITERs) + "))");
                } else {
                    task = LogicTuple.parse("task(" + "sub("
                            + this.r.nextInt(Master.ITERs) + ","
                            + this.r.nextInt(Master.ITERs) + "))");
                }
                this.say("Injecting task: " + task + "...");
                acc.out(ttcid, task, null);
                // Thread.sleep(1000);
            }
            /*
             * ...then wait the result to be computed by ReSpecT reaction
             * chaining.
             */
            final LogicTuple resTempl = LogicTuple.parse("result(Res,"
                    + Master.ITERs + ")");
            this.say("Waiting for result...");
            final TucsonOperation resOp = acc.in(ttcid, resTempl, null);
            final LogicTuple res = resOp.getLogicTupleResult();
            this.say("Result is: " + res.getArg(0));
            this.say("Average is: " + res.getArg(0).floatValue()
                    / res.getArg(1).floatValue());
            acc.exit();
        } catch (final InvalidLogicTupleException e) {
            e.printStackTrace();
        } catch (final TucsonInvalidTupleCentreIdException e) {
            e.printStackTrace();
        } catch (final TucsonOperationNotPossibleException e) {
            e.printStackTrace();
        } catch (final UnreachableNodeException e) {
            e.printStackTrace();
        } catch (final OperationTimeOutException e) {
            e.printStackTrace();
        } catch (final TucsonInvalidAgentIdException e) {
            e.printStackTrace();
        }
    }

}
