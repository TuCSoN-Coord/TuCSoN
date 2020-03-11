package loadBalancing;

import alice.tuple.logic.LogicTuple;
import alice.tuple.logic.exceptions.InvalidLogicTupleException;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;
import alice.tuplecentre.respect.api.TupleCentreId;
import alice.tuplecentre.respect.api.exceptions.InvalidTupleCentreIdException;
import alice.tuplecentre.tucson.api.*;
import alice.tuplecentre.tucson.api.acc.EnhancedSyncACC;
import alice.tuplecentre.tucson.api.acc.NegotiationACC;
import alice.tuplecentre.tucson.api.acc.RootACC;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tuplecentre.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tuplecentre.tucson.api.exceptions.UnreachableNodeException;

/**
 * Dummy Service Requestor class to show some 'adaptive' features related to
 * usage of uniform primitives. It probabilistically looks for available
 * services then issue a request to the Service Provider found.
 *
 * @author s.mariani@unibo.it
 */
public class ServiceRequestor extends AbstractTucsonAgent {

    /**
     * @param args no args expected.
     */
    public static void main(final String[] args) throws TucsonInvalidAgentIdException, InvalidTupleCentreIdException {
        new ServiceRequestor("requestor1", "default@localhost:20504").go();
        new ServiceRequestor("requestor2", "default@localhost:20504").go();
        new ServiceRequestor("requestor3", "default@localhost:20504").go();
    }

    private EnhancedSyncACC acc;
    private boolean die;

    private TupleCentreId tid;

    /**
     * @param aid  agent name
     * @param node node where to look for services
     * @throws TucsonInvalidAgentIdException if the chosen ID is not a valid TuCSoN agent ID
     */
    public ServiceRequestor(final String aid, final String node)
            throws TucsonInvalidAgentIdException, InvalidTupleCentreIdException {
        super(aid);
        this.die = false;
        this.say("I'm started.");
        this.tid = new TupleCentreId(node);
    }

    @Override
    protected RootACC retrieveACC(TucsonAgentId aid, String networkAddress, int portNumber) throws Exception {
        return null;
    }

    @Override
    protected void main() throws OperationTimeOutException, TucsonInvalidAgentIdException, UnreachableNodeException, TucsonOperationNotPossibleException, InvalidLogicTupleException, InterruptedException {
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
    }

}
