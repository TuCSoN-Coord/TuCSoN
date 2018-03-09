package helloWorld;

import alice.logictuple.LogicTuple;
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

/*
 * 1) Extend alice.tuplecentre.tucson.api.AbstractTucsonAgent class.
 */
/**
 * Java TuCSoN Agent extending alice.tuplecentre.tucson.api.TucsonAgent base class.
 *
 * @author ste (mailto: s.mariani@unibo.it)
 */
public class HelloWorldAgent extends AbstractTucsonAgent<RootACC> {

    /**
     * @param args
     *            the name of the TuCSoN coordinable (optional).
     */
    public static void main(final String[] args) {
        String aid = null;
        if (args.length == 1) {
            aid = args[0];
        } else {
            aid = "helloWorldAgent";
        }
        /*
         * 10) Instantiate your agent and 11) start executing its 'main()' using
         * method 'go()'.
         */
        try {
            new HelloWorldAgent(aid).go();
        } catch (final TucsonInvalidAgentIdException e) {
            e.printStackTrace();
        }
    }

    /*
     * 2) Choose one of the given constructors.
     */
    /**
     *
     * @param aid
     *            the String representation of a valid TuCSoN agent identifier
     * @throws TucsonInvalidAgentIdException
     *             if the given String does not represent a valid TuCSoN agent
     *             identifier
     */
    public HelloWorldAgent(final String aid)
            throws TucsonInvalidAgentIdException {
        super(aid);
    }

    @Override
    protected RootACC retrieveACC(final TucsonAgentId aid, final String networkAddress, final int portNumber) {
        return null; //not used because, NegotiationACC does not extend RootACC
    }

    /*
     * 3) To be overridden by TuCSoN programmers with their agent business
     * logic.
     */
    @Override
    protected void main() {

        try {
            /*
             * 4) Get your ACC.
             */
            final NegotiationACC negAcc = TucsonMetaACC
                    .getNegotiationContext(this.getTucsonAgentId());
            final OrdinaryAndSpecificationSyncACC acc = negAcc.playDefaultRole();
            /*
             * 5) Define the tuplecentre target of your coordination operations.
             */
            final TucsonTupleCentreId tid = new TucsonTupleCentreId("default",
                    "localhost", "20504");
            /*
             * 6) Build the tuple e.g. using TuCSoN parsing facilities.
             */
            final LogicTuple tuple = LogicTuple.parse("hello(world)");
            /*
             * 7) Perform the coordination operation using the preferred
             * coordination primitive.
             */
            TucsonOperation op = acc.out(tid, tuple, null);
            /*
             * 8) Check requested operation success.
             */
            LogicTuple res = null;
            if (op.isResultSuccess()) {
                this.say("Operation succeeded.");
                /*
                 * 9) Get requested operation result.
                 */
                res = op.getLogicTupleResult();
                this.say("Operation result is " + res);
            } else {
                this.say("Operation failed.");
            }
            /*
             * Another success test to be sure.
             */
            final LogicTuple template = LogicTuple.parse("hello(Who)");
            op = acc.rdp(tid, template, null);
            if (op.isResultSuccess()) {
                res = op.getLogicTupleResult();
                this.say("Operation result is " + res);
            } else {
                this.say("Operation failed.");
            }
            /*
             * ACC release is automatically done by the TucsonAgent base class.
             */
        } catch (final TucsonInvalidTupleCentreIdException e) {
            e.printStackTrace();
        } catch (final InvalidLogicTupleException e) {
            /*
             * String to be parsed is not in a valid Prolog syntax.
             */
            e.printStackTrace();
        } catch (final TucsonOperationNotPossibleException e) {
            e.printStackTrace();
        } catch (final UnreachableNodeException e) {
            e.printStackTrace();
        } catch (final OperationTimeOutException e) {
            e.printStackTrace();
        } catch (final TucsonInvalidAgentIdException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
