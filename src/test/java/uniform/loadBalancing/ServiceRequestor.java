package uniform.loadBalancing;

import alice.logictuple.LogicTuple;
import alice.logictuple.exceptions.InvalidLogicTupleException;
import alice.tucson.api.AbstractTucsonAgent;
import alice.tucson.api.TucsonOperation;
import alice.tucson.api.TucsonTupleCentreId;
import alice.tucson.api.TucsonTupleCentreIdDefault;
import alice.tucson.api.acc.EnhancedSyncACC;
import alice.tucson.api.acc.NegotiationACC;
import alice.tucson.api.TucsonMetaACC;
import alice.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tucson.api.exceptions.TucsonInvalidTupleCentreIdException;
import alice.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tucson.api.exceptions.UnreachableNodeException;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;
import alice.tuplecentre.core.AbstractTupleCentreOperation;

/**
 * Dummy Service Requestor class to show some 'adaptive' features related to
 * usage of uniform primitives. It probabilistically looks for available
 * services then issue a request to the Service Provider found.
 *
 * @author s.mariani@unibo.it
 */
public class ServiceRequestor extends AbstractTucsonAgent {

    /**
     * @param args
     *            no args expected.
     */
    public static void main(final String[] args) {
        try {
            new ServiceRequestor("requestor1", "default@localhost:20504").go();
            new ServiceRequestor("requestor2", "default@localhost:20504").go();
            new ServiceRequestor("requestor3", "default@localhost:20504").go();
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
            this.tid = new TucsonTupleCentreIdDefault(node);
        } catch (final TucsonInvalidTupleCentreIdException e) {
            this.say("Invalid tid given, killing myself...");
            this.die = true;
        }
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
    public void operationCompleted(final TucsonOperation op) {
        /*
         * not used atm
         */
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
            final LogicTuple dieTuple = LogicTuple.parse("die(" + this.myName()
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
