package masterWorkers.bulk;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

import alice.tuple.logic.LogicTuple;
import alice.tuple.logic.TupleArgument;
import alice.tuple.logic.exceptions.InvalidLogicTupleException;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;
import alice.tuplecentre.tucson.api.AbstractTucsonAgent;
import alice.tuplecentre.tucson.api.TucsonAgentId;
import alice.tuplecentre.tucson.api.TucsonMetaACC;
import alice.tuplecentre.tucson.api.TucsonOperation;
import alice.tuplecentre.tucson.api.TucsonTupleCentreId;
import alice.tuplecentre.tucson.api.acc.EnhancedSyncACC;
import alice.tuplecentre.tucson.api.acc.NegotiationACC;
import alice.tuplecentre.tucson.api.acc.RootACC;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidTupleCentreIdException;
import alice.tuplecentre.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tuplecentre.tucson.api.exceptions.UnreachableNodeException;

/**
 * Worker thread copyOf a master-worker architecture. Given a TuCSoN Node (hopefully
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
            // new WorkerAgent("peter", "default@localhost:20505").go();
            // new WorkerAgent("carl", "default@localhost:20505").go();
        } catch (final TucsonInvalidAgentIdException e) {
            e.printStackTrace();
        }
    }

    private EnhancedSyncACC acc;
    private boolean die;

    private TucsonTupleCentreId tid;

    /**
     * @param aid
     *            agent name
     * @param node
     *            node to contact for retrieving jobs
     *
     * @throws TucsonInvalidAgentIdException
     *             if the chose Identifier is not a valid TuCSoN agent Identifier
     */
    public WorkerAgent(final String aid, final String node)
            throws TucsonInvalidAgentIdException {
        super(aid);
        this.die = false;
        try {
            this.tid = TucsonTupleCentreId.of(node);
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
            List<LogicTuple> job;
            LogicTuple templ;
            final List<LogicTuple> res = new LinkedList<>();
            BigInteger bigNum;
            while (!this.die) {
                this.say("Checking termination...");
                op = this.acc.inp(this.tid,
                        LogicTuple.parse("die(" + this.getTucsonAgentId().getLocalName() + ")"), null);
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
                 * No longer a suspensive primitive: in case no jobs have been
                 * submitted we get an empty list.
                 */
                op = this.acc.inAll(this.tid, templ, null);
                job = op.getLogicTupleListResult();
                if (!job.isEmpty()) {
                    this.say("Found job: " + job.toString());
                    /*
                     * Computation phase.
                     */
                    for (final LogicTuple lt : job) {
                        bigNum = this.computeFactorial(lt.getArg("num").getArg(
                                0));
                        res.add(LogicTuple.parse("res(" + "master("
                                + lt.getArg("master").getArg(0) + "),"
                                + "fact(" + bigNum.toString() + ")," + "reqID("
                                + lt.getArg("reqID").getArg(0) + ")" + ")"));
                    }
                    /*
                     * Result submission phase.
                     */
                    this.say("Putting results: " + res.toString());
                    this.acc.outAll(this.tid, LogicTuple.parse(res.toString()),
                            null);
                    /*
                     * Empty data stores for next iteration.
                     */
                    job.clear();
                    res.clear();
                }
                /*
                 * Just to have time to read outputs on console.
                 */
                Thread.sleep(1000);
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
            this.say("ERROR: Given Identifier is not a valid TuCSoN agent Identifier!");
        }
    }

}
