package alice.tucson.examples.helloWorld;

import alice.logictuple.LogicTuple;
import alice.logictuple.Value;
import alice.logictuple.Var;
import alice.logictuple.exceptions.InvalidVarNameException;
import alice.tucson.api.ITucsonOperation;
import alice.tucson.api.NegotiationACC;
import alice.tucson.api.SynchACC;
import alice.tucson.api.TucsonAgentId;
import alice.tucson.api.TucsonMetaACC;
import alice.tucson.api.TucsonTupleCentreId;
import alice.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tucson.api.exceptions.TucsonInvalidTupleCentreIdException;
import alice.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tucson.api.exceptions.UnreachableNodeException;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;

/**
 * Plain Java class exploiting TuCSoN library.
 *
 * @author ste (mailto: s.mariani@unibo.it)
 */
public final class HelloWorld {

    /**
     * @param args
     *            the name of the TuCSoN coordinable (optional).
     */
    public static void main(final String[] args) {
        /*
         * 1) Build a TuCSoN Agent identifier to contact the TuCSoN system.
         */
        TucsonAgentId aid = null;
        try {
            if (args.length == 1) {
                aid = new TucsonAgentId(args[0]);
            } else {
                aid = new TucsonAgentId("helloWorldMain");
            }
            /*
             * 2) Get a TuCSoN ACC to enable interaction with the TuCSoN system.
             */
            final NegotiationACC negAcc = TucsonMetaACC
                    .getNegotiationContext(aid);
            final SynchACC acc = negAcc.activateDefaultRole();
            /*
             * 3) Define the tuplecentre target of your coordination operations.
             */
            final TucsonTupleCentreId tid = new TucsonTupleCentreId("default",
                    "localhost", "20504");
            /*
             * 4) Build the tuple using the communication language.
             */
            final LogicTuple tuple = new LogicTuple("hello", new Value("world"));
            /*
             * 5) Perform the coordination operation using the preferred
             * coordination primitive.
             */
            ITucsonOperation op = acc.out(tid, tuple, null);
            /*
             * 6) Check requested operation success.
             */
            LogicTuple res = null;
            if (op.isResultSuccess()) {
                System.out.println("[" + aid.getAgentName()
                        + "]: Operation succeeded.");
                /*
                 * 7) Get requested operation result.
                 */
                res = op.getLogicTupleResult();
                System.out.println("[" + aid.getAgentName()
                        + "]: Operation result is " + res);
            } else {
                System.out.println("[" + aid.getAgentName()
                        + "]: Operation failed.");
            }
            /*
             * Another success test to be sure.
             */
            final LogicTuple template = new LogicTuple("hello", new Var("Who"));
            op = acc.rdp(tid, template, null);
            if (op.isResultSuccess()) {
                res = op.getLogicTupleResult();
                System.out.println("[" + aid.getAgentName()
                        + "]: Operation result is " + res);
            } else {
                System.out.println("[" + aid.getAgentName()
                        + "]: Operation failed.");
            }
            /*
             * Release any TuCSoN ACC held when done.
             */
            acc.exit();
        } catch (final TucsonInvalidAgentIdException e) {
            /*
             * The chosen TuCSoN Agent ID is not admissible.
             */
            e.printStackTrace();
        } catch (final TucsonInvalidTupleCentreIdException e) {
            /*
             * The chosen target tuple centre is not admissible.
             */
            e.printStackTrace();
        } catch (final TucsonOperationNotPossibleException e) {
            /*
             * The requested TuCSoN operation cannot be performed.
             */
            e.printStackTrace();
        } catch (final UnreachableNodeException e) {
            /*
             * The chosen target tuple centre is not reachable.
             */
            e.printStackTrace();
        } catch (final OperationTimeOutException e) {
            /*
             * Operation timeout expired.
             */
            e.printStackTrace();
        } catch (final InvalidVarNameException e) {
            e.printStackTrace();
        }
    }

    private HelloWorld() {
        /*
         *
         */
    }
}
