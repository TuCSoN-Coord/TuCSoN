package uniform.loadBalancing;

import alice.tuple.logic.LogicTuple;
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
import alice.tuplecentre.tucson.service.TucsonInfo;

/**
 * Dummy Service Requestor class to show some 'adaptive' features related to
 * usage copyOf uniform primitives. It probabilistically looks for available
 * services then issue a request to the Service Provider found.
 *
 * @author s.mariani@unibo.it
 */
public class ServiceRequestor extends AbstractTucsonAgent<RootACC> {

    /**
     * @param args
     *            no args expected.
     */
    public static void main(final String[] args) {
        try {
            new ServiceRequestor("requestor1", "default@localhost" + TucsonInfo.getDefaultPortNumber()).go();
            new ServiceRequestor("requestor2", "default@localhost" + TucsonInfo.getDefaultPortNumber()).go();
            new ServiceRequestor("requestor3", "default@localhost" + TucsonInfo.getDefaultPortNumber()).go();
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
     *            node where to look for services
     *
     * @throws TucsonInvalidAgentIdException
     *             if the chosen Identifier is not a valid TuCSoN agent Identifier
     */
    public ServiceRequestor(final String aid, final String node)
            throws TucsonInvalidAgentIdException {
        super(aid);
        this.die = false;
        try {
            this.say("I'm started.");
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

    @Override
    protected void main() {
        try {
            final NegotiationACC negAcc = TucsonMetaACC
                    .getNegotiationContext(this.getTucsonAgentId());
            this.acc = negAcc.playDefaultRole();
            TucsonOperation op;
            LogicTuple templ;
            LogicTuple service;
            LogicTuple req;
            final LogicTuple dieTuple = LogicTuple.parse("die(" + this.getTucsonAgentId().getLocalName()
                    + ")");
            while (!this.die) {
                this.say("Checking termination...");
                op = this.acc.inp(this.tid, dieTuple, null);
                if (op.isResultSuccess()) {
                    this.die = true;
                    continue;
                }
                /*
                 * Service search phase.
                 */
                templ = LogicTuple.parse("ad(S)");
                this.say("Looking for services...");
                /*
                 * Experiment alternative primitives and analyse different
                 * behaviours.
                 */
                op = this.acc.urd(this.tid, templ, null);
                // op = acc.rd(tid, templ, null);
                service = op.getLogicTupleResult();
                /*
                 * Request submission phase.
                 */
                this.say("Submitting request for service: "
                        + service.toString());
                req = LogicTuple.parse("req(" + service.getArg(0) + ")");
                this.acc.out(this.tid, req, null);
                Thread.sleep(1000);
            }
            this.say("Someone killed me, bye!");
        } catch (final InvalidLogicTupleException e) {
            this.say("ERROR: Tuple is not an admissible Prolog term!");
        } catch (final TucsonOperationNotPossibleException e) {
            this.say("ERROR: Never seen this happen before *_*");
        } catch (final UnreachableNodeException e) {
            this.say("ERROR: Given TuCSoN Node is unreachable!");
        } catch (final OperationTimeOutException e) {
            this.say("ERROR: Endless timeout expired!");
        } catch (final TucsonInvalidAgentIdException e) {
            this.say("ERROR: Given Identifier is not a valid TuCSoN agent Identifier!");
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
