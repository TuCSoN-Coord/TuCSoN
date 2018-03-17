package respect.bagOfTask;

import java.util.Random;

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
 * Master thread of a bag-of-task architecture. Given a TuCSoN Node (optional)
 * 1) it programs the specification space so as to perform an average
 * computation; 2) it submits jobs at random regarding summation/subtraction
 * computation (to be carried out by workers); 3) then collects results of such
 * tasks and the average (computed by the tuplecentre itself thanks to ReSpecT
 * reactions).
 *
 * @author s.mariani@unibo.it
 */
public class Master extends AbstractTucsonAgent<RootACC> {

    private static final int ITERs = 10;
    private final String ip;
    private final int port;
    /*
     * To randomly choose between summation and subtraction.
     */
    private final Random r = new Random();

    public Master(final String aid) throws TucsonInvalidAgentIdException {
        super(aid);
        this.ip = "localhost";
        this.port = TucsonInfo.getDefaultPortNumber();
    }

    public Master(final String aid, final String ip, final int port)
            throws TucsonInvalidAgentIdException {
        super(aid, ip, port);
        this.ip = ip;
        this.port = port;
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
            final OrdinaryAndSpecificationSyncACC acc = negAcc.playDefaultRole();
            LogicTuple task = null;

            /*
             * Our work has to be done in a custom-defined tuplecentre.
             */
            final TucsonTupleCentreId ttcid = new TucsonTupleCentreIdDefault(
                    "bagoftask", this.ip, String.valueOf(this.port));
            this.say("Injecting ReSpecT Specification...");
            /*
             * First ReSpecT specification tuple: whenever a res(...) is
             * submitted by a worker, if no result(...) tuple exists, create it.
             */
            acc.outS(
                    ttcid,
                    LogicTuples.parse("out(res(R))"),
                    LogicTuples.parse("(completion,success)"),
                    LogicTuples.parse("(no(result(Res,Count)), in(res(R)), out(result(R,1)))"),
                    null);
            /*
             * Second ReSpecT specification tuple: whenever a res(...) is
             * submitted by a worker, if a result(...) tuple exists, update it.
             */
            acc.outS(
                    ttcid,
                    LogicTuples.parse("out(res(R))"),
                    LogicTuples.parse("(completion,success)"),
                    LogicTuples.parse("(in(result(Res,Count)), in(res(R)),"
                            + "NR is Res+R, NC is Count+1, out(result(NR,NC)))"),
                    null);
            /*
             * Start tasks submission cycle...
             */
            for (int i = 0; i < Master.ITERs; i++) {
                if (this.r.nextBoolean()) {
                    task = LogicTuples.parse("task(" + "sum("
                            + this.r.nextInt(Master.ITERs) + ","
                            + this.r.nextInt(Master.ITERs) + "))");
                } else {
                    task = LogicTuples.parse("task(" + "sub("
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
            final LogicTuple resTempl = LogicTuples.parse("result(Res,"
                    + Master.ITERs + ")");
            this.say("Waiting for result...");
            final TucsonOperation resOp = acc.in(ttcid, resTempl, null);
            final LogicTuple res = resOp.getLogicTupleResult();
            this.say("Result is: " + res.getArg(0));
            this.say("Average is: " + res.getArg(0).floatValue()
                    / res.getArg(1).floatValue());
            acc.exit();
        } catch (final InvalidLogicTupleException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (final TucsonInvalidTupleCentreIdException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (final TucsonOperationNotPossibleException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (final UnreachableNodeException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (final OperationTimeOutException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (final TucsonInvalidAgentIdException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

}
