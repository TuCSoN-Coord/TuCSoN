package masterWorkers.bulk;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

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
import alice.tuplecentre.tucson.api.acc.EnhancedSyncACC;
import alice.tuplecentre.tucson.api.acc.NegotiationACC;
import alice.tuplecentre.tucson.api.acc.RootACC;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidTupleCentreIdException;
import alice.tuplecentre.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tuplecentre.tucson.api.exceptions.UnreachableNodeException;

/**
 * Master thread of a master-worker architecture. Given a list of TuCSoN Nodes
 * (hopefully up and listening), it submits jobs regarding factorial computation,
 * then collects expected results.
 *
 * @author s.mariani@unibo.it
 */
public class MasterAgent extends AbstractTucsonAgent<RootACC> {

    /**
     * @param args
     *            no args expected.
     */
    public static void main(final String[] args) {
        final LinkedList<String> nodes = new LinkedList<>();
        nodes.add("default@localhost:20504");
        // nodes.add("default@localhost:20505");
        try {
            // new MasterAgent("walter", nodes, 10, 20).go();
            new MasterAgent("lloyd", nodes, 10, 10).go();
        } catch (final TucsonInvalidAgentIdException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    private EnhancedSyncACC acc;
    private boolean die;
    private final int ITERs;
    private final int MAX_FACT;
    private final HashMap<Integer, Integer> pendings;
    private int reqID;

    private final LinkedList<TucsonTupleCentreId> tids;

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
     *             if the chosen Identifier is not a valid TuCSoN agent Identifier
     */
    public MasterAgent(final String aid, final LinkedList<String> nodes,
            final int iters, final int maxFact)
                    throws TucsonInvalidAgentIdException {
        super(aid);
        this.die = false;
        this.tids = new LinkedList<>();
        try {
            for (final String node : nodes) {
                this.tids.add(new TucsonTupleCentreIdDefault(node));
            }
        } catch (final TucsonInvalidTupleCentreIdException e) {
            this.say("Invalid tid given, killing myself...");
            this.die = true;
        }
        this.ITERs = iters;
        this.MAX_FACT = maxFact;
        this.reqID = 0;
        this.pendings = new HashMap<>();
    }

    @Override
    protected RootACC retrieveACC(final TucsonAgentId aid, final String networkAddress, final int portNumber) {
        return null; //not used because, NegotiationACC does not extend RootACC
    }

    private int drawRandomInt() {
        return (int) Math.round(Math.random() * this.MAX_FACT);
    }

    @Override
    protected void main() {
        this.say("I'm started.");
        try {
            final NegotiationACC negAcc = TucsonMetaACC
                    .getNegotiationContext(this.getTucsonAgentId());
            this.acc = negAcc.playDefaultRole();
            TucsonOperation op;
            TucsonTupleCentreId next;
            LogicTuple job;
            LogicTuple templ;
            List<LogicTuple> res;
            int num;
            while (!this.die) {
                this.say("Checking termination...");
                for (TucsonTupleCentreId tid2 : this.tids) {
                    op = this.acc.inp(tid2,
                            LogicTuples.parse("die(" + this.getTucsonAgentId().getLocalName() + ")"),
                            null);
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
                for (TucsonTupleCentreId tid1 : this.tids) {
                    /*
                     * We iterate nodes in a round-robin fashion...
                     */
                    next = tid1;
                    this.say("Putting jobs in: " + next.toString());
                    for (int j = 0; j < this.ITERs; j++) {
                        /*
                         * ...to put in each <ITERs> jobs.
                         */
                        num = this.drawRandomInt();
                        job = LogicTuples.parse("fact(" + "master("
                                + this.getTucsonAgentId().getLocalName() + ")," + "num(" + num + "),"
                                + "reqID(" + this.reqID + ")" + ")");
                        this.say("Putting job: " + job.toString());
                        /*
                         * Only non-reachability of target tuplecentre may cause
                         * <out> to fail, which raises a Java Exception.
                         */
                        this.acc.out(next, job, null);
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
                for (TucsonTupleCentreId tid : this.tids) {
                    /*
                     * Again we iterate nodes in a round-robin fashion...
                     */
                    next = tid;
                    this.say("Collecting results from: " + next.toString());
                    for (int j = 0; j < this.ITERs; j++) {
                        Thread.sleep(3000);
                        /*
                         * ...this time to retrieve factorial results.
                         */
                        templ = LogicTuples.parse("res(" + "master("
                                + this.getTucsonAgentId().getLocalName() + ")," + "fact(F),"
                                + "reqID(N)" + ")");
                        /*
                         * No longer a suspensive primitive. We need to keep
                         * track of collected results.
                         */
                        op = this.acc.inAll(next, templ, null);
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
            LOGGER.error(e.getMessage(), e);
        } catch (final TucsonOperationNotPossibleException e) {
            this.say("ERROR: Never seen this happen before *_*");
        } catch (final UnreachableNodeException e) {
            this.say("ERROR: Given TuCSoN Node is unreachable!");
            LOGGER.error(e.getMessage(), e);
        } catch (final OperationTimeOutException e) {
            this.say("ERROR: Endless timeout expired!");
        } catch (final InterruptedException e) {
            this.say("ERROR: Sleep interrupted!");
        } catch (final TucsonInvalidAgentIdException e) {
            this.say("ERROR: Given Identifier is not a valid TuCSoN agent Identifier!");
        }
    }

}
