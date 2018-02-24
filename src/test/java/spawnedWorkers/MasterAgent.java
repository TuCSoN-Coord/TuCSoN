package spawnedWorkers;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import alice.logictuple.LogicTuple;
import alice.logictuple.exceptions.InvalidLogicTupleException;
import alice.tucson.api.AbstractTucsonAgent;
import alice.tucson.api.acc.EnhancedSyncACC;
import alice.tucson.api.TucsonOperation;
import alice.tucson.api.acc.NegotiationACC;
import alice.tucson.api.TucsonMetaACC;
import alice.tucson.api.TucsonTupleCentreId;
import alice.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tucson.api.exceptions.TucsonInvalidTupleCentreIdException;
import alice.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tucson.api.exceptions.UnreachableNodeException;
import alice.tuplecentre.api.exceptions.InvalidOperationException;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;
import alice.tuplecentre.core.AbstractTupleCentreOperation;

/**
 * Master thread of a master-worker architecture. Given a list of TuCSoN Nodes
 * (hopefully up and listening), it submits jobs regarding factorial
 * computation, then collects expected results.
 *
 * @author ste (mailto: s.mariani@unibo.it)
 */
public class MasterAgent extends AbstractTucsonAgent {

    private static final int ITERS = 10;
    private static final int MAX_FACT = 10;
    private static final int SLEEP = 1000;

    /**
     * @param args
     *            no args expected.
     */
    public static void main(final String[] args) {
        final LinkedList<String> nodes = new LinkedList<String>();
        nodes.add("default@localhost:20504");
        // nodes.add("default@localhost:20505");
        try {
            // new MasterAgent("walter", nodes, 10, 20).go();
            new MasterAgent("lloyd", nodes, MasterAgent.ITERS,
                    MasterAgent.MAX_FACT).go();
        } catch (final TucsonInvalidAgentIdException e) {
            e.printStackTrace();
        }
    }

    private boolean die;
    private final int maxFactorial;
    private final int nIters;
    private final Map<Integer, Integer> pendings;
    private int reqID;
    private final List<TucsonTupleCentreId> tids;

    /**
     * @param aid
     *            agent name
     * @param nodes
     *            list of nodes where to submit jobs
     * @param iters
     *            max number of jobs per node
     * @param maxFact
     *            max number for which to calculate factorial
     *
     * @throws TucsonInvalidAgentIdException
     *             if the given String does not represent a valid TuCSoN agent
     *             identifier
     */
    public MasterAgent(final String aid, final List<String> nodes,
            final int iters, final int maxFact)
                    throws TucsonInvalidAgentIdException {
        super(aid);
        this.die = false;
        this.tids = new LinkedList<TucsonTupleCentreId>();
        try {
            for (final String node : nodes) {
                this.tids.add(new TucsonTupleCentreId(node));
            }
        } catch (final TucsonInvalidTupleCentreIdException e) {
            this.say("Invalid tid given, killing myself...");
            this.die = true;
        }
        this.nIters = iters;
        this.maxFactorial = maxFact;
        this.reqID = 0;
        this.pendings = new HashMap<Integer, Integer>();
    }

    // modified by SANGIO
    @Override
    public void operationCompleted(final AbstractTupleCentreOperation op) {
        // TODO Auto-generated method stub
    }

    @Override
    public void operationCompleted(final TucsonOperation op) {
        /*
         *
         */
    }

    private int drawRandomInt() {
        return (int) Math.round(Math.random() * this.maxFactorial);
    }

    @Override
    protected void main() {
        this.say("I'm started.");

        // final EnhancedSyncACC acc = this.getContext();
        TucsonOperation op;
        TucsonTupleCentreId next;
        LogicTuple job;
        LogicTuple templ;
        List<LogicTuple> res;
        int num;
        try {
            final NegotiationACC negAcc = TucsonMetaACC
                    .getNegotiationContext(this.getTucsonAgentId());
            final EnhancedSyncACC acc = negAcc.playDefaultRole();
            while (!this.die) {
                this.say("Checking termination...");
                for (int i = 0; i < this.tids.size(); i++) {
                    op = acc.inp(this.tids.get(i),
                            LogicTuple.parse("die(" + this.myName() + ")"),
                            (Long) null);
                    /*
                     * Only upon success the searched tuple was found. NB: we do
                     * not <break> cycle to consume all termination tuples if
                     * multiple exist.
                     */
                    if (op.isResultSuccess()) {
                        this.die = true;
                    }
                }
                if (this.die) {
                    continue;
                }
                /*
                 * Jobs submission phase.
                 */
                for (int i = 0; i < this.tids.size(); i++) {
                    /*
                     * We iterate nodes in a round-robin fashion...
                     */
                    next = this.tids.get(i);
                    this.say("Putting jobs in: " + next.toString());
                    for (int j = 0; j < this.nIters; j++) {
                        /*
                         * ...to put in each <ITERs> jobs.
                         */
                        num = this.drawRandomInt();
                        job = LogicTuple.parse("fact(" + "master("
                                + this.myName() + ")," + "num(" + num + "),"
                                + "reqID(" + this.reqID + ")" + ")");
                        this.say("Putting job: " + job.toString());
                        /*
                         * Only non-reachability of target tuplecentre may cause
                         * <out> to fail, which raises a Java Exception.
                         */
                        acc.out(next, job, (Long) null);
                        /*
                         * We keep track of pending computations.
                         */
                        this.pendings.put(this.reqID, num);
                        this.reqID++;
                    }
                }
                /*
                 * Result collection phase.
                 */
                for (int i = 0; i < this.tids.size(); i++) {
                    /*
                     * Again we iterate nodes in a round-robin fashion...
                     */
                    next = this.tids.get(i);
                    this.say("Collecting results from: " + next.toString());
                    for (int j = 0; j < this.nIters; j++) {
                        acc.spawn(
                                next,
                                LogicTuple
                                .parse("exec('alice.tucson.examples.spawnedWorkers.SpawnedWorkingActivity.class')"),
                                null);
                        /*
                         * Just to let you view something on the console.
                         */
                        Thread.sleep(MasterAgent.SLEEP);
                        /*
                         * ...this time to retrieve factorial results.
                         */
                        templ = LogicTuple.parse("res(" + "master("
                                + this.myName() + ")," + "fact(F),"
                                + "reqID(N)" + ")");
                        /*
                         * No longer a suspensive primitive. We need to keep
                         * track of collected results.
                         */
                        op = acc.inAll(next, templ, (Long) null);
                        /*
                         * Check needed due to suspensive semantics.
                         */
                        if (op.isResultSuccess()) {
                            res = op.getLogicTupleListResult();
                            if (!res.isEmpty()) {
                                this.say("Collected results:");
                                for (final LogicTuple lt : res) {
                                    /*
                                     * We remove corresponding pending job.
                                     */
                                    num = this.pendings.remove(lt
                                            .getArg("reqID").getArg(0)
                                            .intValue());
                                    this.say("\tFactorial of " + num + " is "
                                            + lt.getArg("fact").getArg(0));
                                    j++;
                                }
                            }
                        }
                        j--;
                    }
                }
                if (this.tids.isEmpty()) {
                    this.say("No nodes given to contact, killing myself...");
                    this.die = true;
                }
            }
            this.say("Someone killed me, bye!");
        } catch (final InvalidLogicTupleException e) {
            this.say("ERROR: Tuple is not an admissible Prolog term!");
            e.printStackTrace();
        } catch (final TucsonOperationNotPossibleException e) {
            this.say("ERROR: Never seen this happen before *_*");
            e.printStackTrace();
        } catch (final UnreachableNodeException e) {
            this.say("ERROR: Given TuCSoN Node is unreachable!");
            e.printStackTrace();
        } catch (final OperationTimeOutException e) {
            this.say("ERROR: Endless timeout expired!");
            e.printStackTrace();
        } catch (final InvalidOperationException e) {
            this.say("ERROR: No tuple arguments to retrieve!");
            e.printStackTrace();
        } catch (final InterruptedException e) {
            this.say("ERROR: Sleep interrupted!");
            e.printStackTrace();
        } catch (final TucsonInvalidAgentIdException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
