package masterWorkers;

import java.math.BigInteger;

import alice.logictuple.LogicTuple;
import alice.logictuple.TupleArgument;
import alice.logictuple.exceptions.InvalidLogicTupleException;
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

/**
 * Worker thread of a master-worker architecture. Given a TuCSoN Node (hopefully
 * up and listening), it waits for submitted jobs regarding factorial computation,
 * then outputs computed results.
 *
 * @author s.mariani@unibo.it
 */
public class WorkerAgent extends AbstractTucsonAgent {

    /**
     * @param args
     *            no args expected.
     */
    public static void main(final String[] args) {
        try {
            new WorkerAgent("hank", "default@localhost:20504").go();
            new WorkerAgent("rob", "default@localhost:20504").go();
            new WorkerAgent("peter", "default@localhost:20505").go();
            new WorkerAgent("carl", "default@localhost:20505").go();
        } catch (final TucsonInvalidAgentIdException e) {
            e.printStackTrace();
        }
    }

    private OrdinaryAndSpecificationSyncACC acc;
    private boolean die;

    private TucsonTupleCentreId tid;

    /**
     * @param aid
     *            agent name
     * @param node
     *            node to contact for retrieving jobs
     *
     * @throws TucsonInvalidAgentIdException
     *             if the chosen ID is not a valid TuCSoN agent ID
     */
    public WorkerAgent(final String aid, final String node)
            throws TucsonInvalidAgentIdException {
        super(aid);
        this.die = false;
        try {
            this.tid = new TucsonTupleCentreId(node);
        } catch (final TucsonInvalidTupleCentreIdException e) {
            this.say("Invalid tid given, killing myself...");
            this.die = true;
        }
    }

    @Override
    protected RootACC retrieveACC(final TucsonAgentId aid, final String networkAddress, final int portNumber) {
        return null; //not used because, NegotiationACC does not extend RootACC
    }

    private BigInteger computeFactorial(final TupleArgument varValue) {
        final int num = varValue.intValue();
        this.say("Computing factorial for: " + num + "...");
        return this.factorial(num);
    }

    private BigInteger factorial(final int num) {
        if (num == 0) {
            return BigInteger.ONE;
        }
        return new BigInteger("" + num).multiply(this.factorial(num - 1));
    }

    @Override
    protected void main() {
        this.say("I'm started.");
        try {
            final NegotiationACC negAcc = TucsonMetaACC
                    .getNegotiationContext(this.getTucsonAgentId());
            this.acc = negAcc.playDefaultRole();
            TucsonOperation op;
            LogicTuple job;
            LogicTuple templ;
            LogicTuple res;
            BigInteger bigNum;
            while (!this.die) {
                this.say("Checking termination...");
                op = this.acc.inp(this.tid,
                        LogicTuple.parse("die(" + this.getTucsonAgentId().getAgentName() + ")"), null);
                /*
                 * Only upon success the searched tuple was found.
                 */
                if (op.isResultSuccess()) {
                    this.die = true;
                    continue;
                }
                /*
                 * Jobs collection phase.
                 */
                templ = LogicTuple.parse("fact(master(M),num(N),reqID(R))");
                this.say("Waiting for jobs...");
                /*
                 * Watch out: it's a suspensive primitive! If no jobs are
                 * available we are stuck and we can't even terminate using
                 * tuple <die()>!
                 */
                op = this.acc.in(this.tid, templ, null);
                job = op.getLogicTupleResult();
                this.say("Found job: " + job.toString());
                /*
                 * Computation phase.
                 */
                bigNum = this.computeFactorial(job.getArg("num").getArg(0));
                /*
                 * Result submission phase.
                 */
                res = LogicTuple.parse("res(" + "master("
                        + job.getArg("master").getArg(0) + ")," + "fact("
                        + bigNum.toString() + ")," + "reqID("
                        + job.getArg("reqID").getArg(0) + ")" + ")");
                this.say("Putting result: " + res.toString());
                this.acc.out(this.tid, res, null);
                /*
                 * Just to have time to read outputs on console.
                 */
                Thread.sleep(3000);
            }
            this.say("Someone killed me, bye!");
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
