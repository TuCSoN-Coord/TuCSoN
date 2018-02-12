/**
 * HelloWorldJTuples.java
 */
package helloWorld;

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
import alice.tuplecentre.api.Tuple;
import alice.tuplecentre.api.exceptions.InvalidTupleException;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;
import alice.tuples.javatuples.api.IJTuple;
import alice.tuples.javatuples.api.IJTupleTemplate;
import alice.tuples.javatuples.api.JArgType;
import alice.tuples.javatuples.exceptions.InvalidJValException;
import alice.tuples.javatuples.exceptions.InvalidJVarException;
import alice.tuples.javatuples.impl.JTuple;
import alice.tuples.javatuples.impl.JTupleTemplate;
import alice.tuples.javatuples.impl.JVal;
import alice.tuples.javatuples.impl.JVar;

/**
 * @author ste (mailto: s.mariani@unibo.it) on 24/feb/2014
 *
 */
public final class HelloWorldJTuples {

    /**
     * @param args
     *            program arguments (none expected)
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
            final SynchACC acc = negAcc.playDefaultRole();

            /*
             * 3) Define the tuplecentre target of your coordination operations.
             */
            final TucsonTupleCentreId tid = new TucsonTupleCentreId("default",
                    "localhost", "20504");
            /*
             * 4) Build the tuple using the communication language.
             */
            final IJTuple tuple = new JTuple(new JVal("hello"));
            tuple.addArg(new JVal("world"));
            /*
             * 5) Perform the coordination operation using the preferred
             * coordination primitive.
             */
            ITucsonOperation op = acc.out(tid, tuple, null);
            /*
             * 6) Check requested operation success.
             */
            Tuple res = null;
            if (op.isResultSuccess()) {
                System.out.println("[" + aid.getAgentName()
                        + "]: Operation succeeded.");
                /*
                 * 7) Get requested operation result.
                 */
                res = op.getJTupleResult();
                System.out.println("[" + aid.getAgentName()
                        + "]: Operation result is " + res);
            } else {
                System.out.println("[" + aid.getAgentName()
                        + "]: Operation failed.");
            }
            /*
             * Another success test to be sure.
             */
            final IJTupleTemplate template = new JTupleTemplate(new JVal(
                    "hello"));
            template.addArg(new JVar(JArgType.LITERAL));
            op = acc.rdp(tid, template, null);
            if (op.isResultSuccess()) {
                res = op.getJTupleResult();
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
        } catch (final InvalidTupleException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (final InvalidJValException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (final InvalidJVarException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private HelloWorldJTuples() {
        /*
         *
         */
    }
}
