/**
 * Crated by ste on 07/giu/2014
 */
package persistency;

import java.io.IOException;
import alice.logictuple.LogicTuple;
import alice.logictuple.Value;
import alice.logictuple.Var;
import alice.logictuple.exceptions.InvalidLogicTupleException;
import alice.tucson.api.TucsonTupleCentreIdDefault;
import alice.tucson.api.acc.EnhancedACC;
import alice.tucson.api.acc.NegotiationACC;
import alice.tucson.api.TucsonAgentIdDefault;
import alice.tucson.api.TucsonMetaACC;
import alice.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tucson.api.exceptions.TucsonInvalidTupleCentreIdException;
import alice.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tucson.api.exceptions.UnreachableNodeException;
import alice.tucson.network.exceptions.DialogInitializationException;
import alice.tucson.service.TucsonNodeService;
import alice.tucson.utilities.Utils;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;

/**
 * @author ste
 *
 */
public final class PersistencyTester {

    /**
     * @param args
     *            not used atm
     */
    public static void main(final String[] args) {
        try {
            final TucsonNodeService tns = new TucsonNodeService(20504);
            tns.install();
            try {
                while (!TucsonNodeService.isInstalled(20504, 5000)) {
                    Thread.sleep(1000);
                }
            } catch (final InterruptedException e) {
                e.printStackTrace();
            } catch (final DialogInitializationException e) {
                e.printStackTrace();
            }
            final TucsonTupleCentreIdDefault ttcid = new TucsonTupleCentreIdDefault(
                    "def(1)@localhost:20504");
            final TucsonTupleCentreIdDefault ttcidOrg = new TucsonTupleCentreIdDefault(
                    "'$ORG'@localhost:20504");
            final TucsonAgentIdDefault aid = new TucsonAgentIdDefault("'PersistencyTester'");
            final NegotiationACC negAcc = TucsonMetaACC
                    .getNegotiationContext(aid);
            final EnhancedACC acc = negAcc.playDefaultRole();
            // spec addition
            String spec = Utils
                    .fileToString("alice/tucson/examples/persistency/aggregation.rsp");
            acc.setS(ttcid, spec, Long.MAX_VALUE);
            // tuples addition
            int i = 0;
            for (; i < 1000; i++) {
                acc.out(ttcid, new LogicTuple("t", new Value(i)),
                        Long.MAX_VALUE);
            }
            // snapshot test
            acc.out(ttcidOrg, new LogicTuple("cmd", new Value(
                    "enable_persistency", new Value("def", new Value(1)))),
                    Long.MAX_VALUE);
            // spec addition
            spec = Utils
                    .fileToString("alice/tucson/examples/persistency/repulsion.rsp");
            acc.setS(ttcid, spec, Long.MAX_VALUE);
            // tuples addition
            for (; i < 2000; i++) {
                acc.out(ttcid, new LogicTuple("t", new Value(i)),
                        Long.MAX_VALUE);
            }
            // tuples deletion
            for (i--; i > 1500; i--) {
                acc.in(ttcid, new LogicTuple("t", new Value(i)), Long.MAX_VALUE);
            }
            acc.inS(ttcid,
                    new LogicTuple("out", new Value("repulse",
                            new Value("INFO"))),
                    new LogicTuple("completion"),
                    LogicTuple
                            .parse("(rd_all(neighbour(_), NBRS),multiread(NBRS, repulse(INFO)))"),
                    Long.MAX_VALUE);
            // disable persistency test
            acc.out(ttcidOrg, new LogicTuple("cmd", new Value(
                    "disable_persistency", new Value("def", new Value(1)))),
                    Long.MAX_VALUE);
            // tuples addition
            for (; i < 2000; i++) {
                acc.out(ttcid, new LogicTuple("ttt", new Value(i)),
                        Long.MAX_VALUE);
            }
            // snapshot test n. 2
            acc.out(ttcidOrg, new LogicTuple("cmd", new Value(
                    "enable_persistency", new Value("def", new Var()))),
                    Long.MAX_VALUE);
            // tuples addition
            for (; i < 3000; i++) {
                acc.out(ttcid, new LogicTuple("ttt", new Value(i)),
                        Long.MAX_VALUE);
            }
            acc.exit();
            // give node time to close persistency file
            Thread.sleep(3000);
            tns.shutdown();
        } catch (final TucsonInvalidTupleCentreIdException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (final TucsonInvalidAgentIdException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (final TucsonOperationNotPossibleException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (final UnreachableNodeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (final OperationTimeOutException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (final InvalidLogicTupleException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (final InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private PersistencyTester() {
        /*
         * avoid instantiability
         */
    }
}
