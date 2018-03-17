/**
 * HelloWorldJTuples.java
 */
package helloWorld;

import alice.tuple.Tuple;
import alice.tuple.java.api.JArgType;
import alice.tuple.java.api.JTuple;
import alice.tuple.java.api.JTupleTemplate;
import alice.tuple.java.exceptions.InvalidJValException;
import alice.tuple.java.exceptions.InvalidJVarException;
import alice.tuple.java.impl.JTupleDefault;
import alice.tuple.java.impl.JTupleTemplateDefault;
import alice.tuple.java.impl.JValDefault;
import alice.tuple.java.impl.JVarDefault;
import alice.tuplecentre.api.exceptions.InvalidTupleException;
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
            final JTuple tuple = new JTupleDefault(new JValDefault("hello"));
            tuple.addArg(new JValDefault("world"));
            /*
             * 5) Perform the coordination operation using the preferred
             * coordination primitive.
             */
            TucsonOperation op = acc.out(tid, tuple, null);
            /*
             * 6) Check requested operation success.
             */
            Tuple res = null;
            if (op.isResultSuccess()) {
                System.out.println("[" + aid.getLocalName()
                        + "]: Operation succeeded.");
                /*
                 * 7) Get requested operation result.
                 */
                res = op.getJTupleResult();
                System.out.println("[" + aid.getLocalName()
                        + "]: Operation result is " + res);
            } else {
                System.out.println("[" + aid.getLocalName()
                        + "]: Operation failed.");
            }
            /*
             * Another success test to be sure.
             */
            final JTupleTemplate template = new JTupleTemplateDefault(new JValDefault(
                    "hello"));
            template.addArg(new JVarDefault(JArgType.LITERAL));
            op = acc.rdp(tid, template, null);
            if (op.isResultSuccess()) {
                res = op.getJTupleResult();
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
        } catch (final TucsonInvalidAgentIdException e) {
            /*
             * The chosen TuCSoN Agent Identifier is not admissible.
             */
            LOGGER.error(e.getMessage(), e);
        } catch (final TucsonInvalidTupleCentreIdException e) {
            /*
             * The chosen target tuple centre is not admissible.
             */
            LOGGER.error(e.getMessage(), e);
        } catch (final TucsonOperationNotPossibleException e) {
            /*
             * The requested TuCSoN operation cannot be performed.
             */
            LOGGER.error(e.getMessage(), e);
        } catch (final UnreachableNodeException e) {
            /*
             * The chosen target tuple centre is not reachable.
             */
            LOGGER.error(e.getMessage(), e);
        } catch (final OperationTimeOutException e) {
            /*
             * Operation timeout expired.
             */
            LOGGER.error(e.getMessage(), e);
        } catch (final InvalidTupleException e) {
            // TODO Auto-generated catch block
            LOGGER.error(e.getMessage(), e);
        } catch (final InvalidJValException e) {
            // TODO Auto-generated catch block
            LOGGER.error(e.getMessage(), e);
        } catch (final InvalidJVarException e) {
            // TODO Auto-generated catch block
            LOGGER.error(e.getMessage(), e);
        }
    }

    private HelloWorldJTuples() {
        /*
         *
         */
    }
}
