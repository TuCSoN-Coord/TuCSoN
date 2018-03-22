package helloWorld;

import alice.tuple.logic.LogicTuple;
import alice.tuple.logic.LogicTuples;
import alice.tuple.logic.TupleArguments;
import alice.tuple.logic.exceptions.InvalidVarNameException;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;
import alice.tuplecentre.tucson.api.TucsonAgentId;
import alice.tuplecentre.tucson.api.TucsonAgentIdDefault;
import alice.tuplecentre.tucson.api.TucsonMetaACC;
import alice.tuplecentre.tucson.api.TucsonOperation;
import alice.tuplecentre.tucson.api.TucsonTupleCentreId;
import alice.tuplecentre.tucson.api.TucsonTupleCentreIdDefault;
import alice.tuplecentre.tucson.api.acc.NegotiationACC;
import alice.tuplecentre.tucson.api.acc.OrdinaryAndSpecificationSyncACC;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidTupleCentreIdException;
import alice.tuplecentre.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tuplecentre.tucson.api.exceptions.UnreachableNodeException;
import alice.tuplecentre.tucson.service.TucsonInfo;

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
        TucsonAgentId aid;
        try {
            if (args.length == 1) {
                aid = new TucsonAgentIdDefault(args[0]);
            } else {
                aid = new TucsonAgentIdDefault("helloWorldMain");
            }
            /*
             * 2) Get a TuCSoN ACC to enable interaction with the TuCSoN system.
             */
            final NegotiationACC negAcc = TucsonMetaACC
                    .getNegotiationContext(aid);
            final OrdinaryAndSpecificationSyncACC acc = negAcc.playDefaultRole();
            /*
             * 3) Define the tuplecentre target of your coordination operations.
             */
            final TucsonTupleCentreId tid = new TucsonTupleCentreIdDefault("default",
                    "localhost", String.valueOf(TucsonInfo.getDefaultPortNumber()));
            /*
             * 4) Build the tuple using the communication language.
             */
            final LogicTuple tuple = LogicTuples.newInstance("hello", TupleArguments.newValueArgument("world"));
            /*
             * 5) Perform the coordination operation using the preferred
             * coordination primitive.
             */
            TucsonOperation op = acc.out(tid, tuple, null);
            /*
             * 6) Check requested operation success.
             */
            LogicTuple res;
            if (op.isResultSuccess()) {
                System.out.println("[" + aid.getLocalName()
                        + "]: Operation succeeded.");
                /*
                 * 7) Get requested operation result.
                 */
                res = op.getLogicTupleResult();
                System.out.println("[" + aid.getLocalName()
                        + "]: Operation result is " + res);
            } else {
                System.out.println("[" + aid.getLocalName()
                        + "]: Operation failed.");
            }
            /*
             * Another success test to be sure.
             */
            final LogicTuple template = LogicTuples.newInstance("hello", TupleArguments.newVarArgument("Who"));
            op = acc.rdp(tid, template, null);
            if (op.isResultSuccess()) {
                res = op.getLogicTupleResult();
                System.out.println("[" + aid.getLocalName()
                        + "]: Operation result is " + res);
            } else {
                System.out.println("[" + aid.getLocalName()
                        + "]: Operation failed.");
            }
            /*
             * Release any TuCSoN ACC held when done.
             */
            acc.exit();
        } catch (final TucsonInvalidAgentIdException | InvalidVarNameException | OperationTimeOutException | UnreachableNodeException | TucsonOperationNotPossibleException | TucsonInvalidTupleCentreIdException e) {
            /*
             * The chosen TuCSoN Agent Identifier is not admissible.
             */
            e.printStackTrace();
        }
    }

    private HelloWorld() {
        /*
         *
         */
    }
}
