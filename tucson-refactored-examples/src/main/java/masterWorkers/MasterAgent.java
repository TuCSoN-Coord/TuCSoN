package masterWorkers;

import java.util.HashMap;
import java.util.LinkedList;

import alice.tuple.logic.LogicTuple;
import alice.tuple.logic.exceptions.InvalidLogicTupleException;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;
import alice.tuplecentre.respect.api.TupleCentreId;
import alice.tuplecentre.respect.api.exceptions.InvalidTupleCentreIdException;
import alice.tuplecentre.tucson.api.AbstractTucsonAgent;
import alice.tuplecentre.tucson.api.TucsonAgentId;
import alice.tuplecentre.tucson.api.TucsonMetaACC;
import alice.tuplecentre.tucson.api.TucsonOperation;
import alice.tuplecentre.tucson.api.acc.NegotiationACC;
import alice.tuplecentre.tucson.api.acc.OrdinarySyncACC;
import alice.tuplecentre.tucson.api.acc.RootACC;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tuplecentre.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tuplecentre.tucson.api.exceptions.UnreachableNodeException;

/**
 * Master thread of a master-worker architecture. Given a list of TuCSoN Nodes
 * (hopefully up and listening), it submits jobs regarding factorial computation,
 * then collects expected results.
 *
 * @author s.mariani@unibo.it
 */
public class MasterAgent extends AbstractTucsonAgent {

    /**
     * @param args no args expected.
     */
    public static void main(final String[] args) throws TucsonInvalidAgentIdException, InvalidTupleCentreIdException {
        final LinkedList<String> nodes = new LinkedList<String>();
        nodes.add("default@localhost:20504");
        nodes.add("default@localhost:20505");
        new MasterAgent("walter", nodes, 10, 10).go();
        new MasterAgent("lloyd", nodes, 10, 10).go();
    }

    private OrdinarySyncACC acc;
    private boolean die;
    private final int ITERs;
    private final int MAX_FACT;
    private final HashMap<Integer, Integer> pendings;
    private int reqID;

    private final LinkedList<TupleCentreId> tids;

    /**
     * @param aid     agent name
     * @param nodes   list of nodes where to submit jobs
     * @param iters   max number of jobs per node
     * @param maxFact max number for which to calculate factorial
     * @throws TucsonInvalidAgentIdException if the chosen ID is not a valid TuCSoN agent ID
     */
    public MasterAgent(final String aid, final LinkedList<String> nodes,
                       final int iters, final int maxFact)
            throws TucsonInvalidAgentIdException, InvalidTupleCentreIdException {
        super(aid);
        this.die = false;
        this.tids = new LinkedList<TupleCentreId>();
        for (final String node : nodes) {
            this.tids.add(new TupleCentreId(node));
        }
        this.ITERs = iters;
        this.MAX_FACT = maxFact;
        this.reqID = 0;
        this.pendings = new HashMap<Integer, Integer>();
    }

    private int drawRandomInt() {
        return (int) Math.round(Math.random() * this.MAX_FACT);
    }

    @Override
    protected RootACC retrieveACC(TucsonAgentId aid, String networkAddress, int portNumber) throws Exception {
        return null;
    }

    @Override
    protected void main() throws OperationTimeOutException, TucsonInvalidAgentIdException, UnreachableNodeException, TucsonOperationNotPossibleException, InvalidLogicTupleException {
        this.say("I'm started.");
        final NegotiationACC negAcc = TucsonMetaACC
                .getNegotiationContext(this.getTucsonAgentId());
        this.acc = negAcc.playDefaultRole();
        TucsonOperation op;
        TupleCentreId next;
        LogicTuple job;
        LogicTuple templ;
        LogicTuple res;
        int num;
        while (!this.die) {
            this.say("Checking termination...");
            for (int i = 0; i < this.tids.size(); i++) {
                op = this.acc.inp(this.tids.get(i),
                        LogicTuple.parse("die(" + this.getTucsonAgentId().getLocalName() + ")"),
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
            for (int i = 0; i < this.tids.size(); i++) {
                /*
                 * We iterate nodes in a round-robin fashion...
                 */
                next = this.tids.get(i);
                this.say("Putting jobs in: " + next.toString());
                for (int j = 0; j < this.ITERs; j++) {
                    /*
                     * ...to put in each <ITERs> jobs.
                     */
                    num = this.drawRandomInt();
                    job = LogicTuple.parse("fact(" + "master("
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
            for (int i = 0; i < this.tids.size(); i++) {
                /*
                 * Again we iterate nodes in a round-robin fashion...
                 */
                next = this.tids.get(i);
                this.say("Collecting results from: " + next.toString());
                for (int j = 0; j < this.ITERs; j++) {
                    /*
                     * ...this time to retrieve factorial results.
                     */
                    templ = LogicTuple.parse("res(" + "master("
                            + this.getTucsonAgentId().getLocalName() + ")," + "fact(F),"
                            + "reqID(N)" + ")");
                    /*
                     * Watch out: it's a suspensive primitive! To not get
                     * stuck we need all submitted jobs' results (order
                     * doesn't matter though).
                     */
                    op = this.acc.in(next, templ, null);
                    /*
                     * We could even skip this check due to suspensive
                     * semantics.
                     */
                    if (op.isResultSuccess()) {
                        res = op.getLogicTupleResult();
                        this.say("Collected result: " + res.toString());
                        /*
                         * We remove corresponding pending job.
                         */
                        num = this.pendings.remove(res.getArg("reqID")
                                .getArg(0).intValue());
                        this.say("Factorial of " + num + " is "
                                + res.getArg("fact").getArg(0));
                    }
                }
            }
            if (this.tids.isEmpty()) {
                this.say("No nodes given to contact, killing myself...");
                this.die = true;
            }
        }
        this.say("Someone killed me, bye!");
    }

}
