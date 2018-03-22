/**
 * Crated by ste on 07/giu/2014
 */
package persistency;

import java.io.IOException;

import alice.tuple.logic.LogicTuples;
import alice.tuple.logic.TupleArguments;
import alice.tuple.logic.exceptions.InvalidLogicTupleException;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;
import alice.tuplecentre.tucson.api.TucsonAgentId;
import alice.tuplecentre.tucson.api.TucsonAgentIdDefault;
import alice.tuplecentre.tucson.api.TucsonMetaACC;
import alice.tuplecentre.tucson.api.TucsonTupleCentreId;
import alice.tuplecentre.tucson.api.TucsonTupleCentreIdDefault;
import alice.tuplecentre.tucson.api.acc.EnhancedACC;
import alice.tuplecentre.tucson.api.acc.NegotiationACC;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidTupleCentreIdException;
import alice.tuplecentre.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tuplecentre.tucson.api.exceptions.UnreachableNodeException;
import alice.tuplecentre.tucson.network.exceptions.DialogInitializationException;
import alice.tuplecentre.tucson.service.TucsonInfo;
import alice.tuplecentre.tucson.service.TucsonNodeService;
import alice.tuplecentre.tucson.utilities.Utils;

/**
 * @author ste
 */
public final class PersistencyTester {

    /**
     * @param args not used atm
     */
    public static void main(final String[] args) {
        try {
            final TucsonNodeService tns = new TucsonNodeService(TucsonInfo.getDefaultPortNumber());
            tns.install();
            try {
                while (!TucsonNodeService.isInstalled(TucsonInfo.getDefaultPortNumber(), 5000)) {
                    Thread.sleep(1000);
                }
            } catch (final InterruptedException | DialogInitializationException e) {
                e.printStackTrace();
            }
            final TucsonTupleCentreId ttcid = new TucsonTupleCentreIdDefault(
                    "def(1)@localhost:" + TucsonInfo.getDefaultPortNumber());
            final TucsonTupleCentreId ttcidOrg = new TucsonTupleCentreIdDefault(
                    "'$ORG'@localhost:" + TucsonInfo.getDefaultPortNumber());
            final TucsonAgentId aid = new TucsonAgentIdDefault("'PersistencyTester'");
            final NegotiationACC negAcc = TucsonMetaACC
                    .getNegotiationContext(aid);
            final EnhancedACC acc = negAcc.playDefaultRole();
            // spec addition
            String spec = Utils
                    .fileToString("persistency/aggregation.rsp");
            acc.setS(ttcid, spec, Long.MAX_VALUE);
            // tuples addition
            int i = 0;
            for (; i < 1000; i++) {
                acc.out(ttcid, LogicTuples.newInstance("t", TupleArguments.newValueArgument(i)),
                        Long.MAX_VALUE);
            }
            // snapshot test
            acc.out(ttcidOrg, LogicTuples.newInstance("cmd", TupleArguments.newValueArgument(
                    "enable_persistency", TupleArguments.newValueArgument("def", TupleArguments.newValueArgument(1)))),
                    Long.MAX_VALUE);
            // spec addition
            spec = Utils
                    .fileToString("persistency/repulsion.rsp");
            acc.setS(ttcid, spec, Long.MAX_VALUE);
            // tuples addition
            for (; i < 2000; i++) {
                acc.out(ttcid, LogicTuples.newInstance("t", TupleArguments.newValueArgument(i)),
                        Long.MAX_VALUE);
            }
            // tuples deletion
            for (i--; i > 1500; i--) {
                acc.in(ttcid, LogicTuples.newInstance("t", TupleArguments.newValueArgument(i)), Long.MAX_VALUE);
            }
            acc.inS(ttcid,
                    LogicTuples.newInstance("out", TupleArguments.newValueArgument("repulse",
                            TupleArguments.newValueArgument("INFO"))),
                    LogicTuples.newInstance("completion"),
                    LogicTuples.parse("(rd_all(neighbour(_), NBRS),multiread(NBRS, repulse(INFO)))"),
                    Long.MAX_VALUE);
            // disable persistency test
            acc.out(ttcidOrg, LogicTuples.newInstance("cmd", TupleArguments.newValueArgument(
                    "disable_persistency", TupleArguments.newValueArgument("def", TupleArguments.newValueArgument(1)))),
                    Long.MAX_VALUE);
            // tuples addition
            for (; i < 2000; i++) {
                acc.out(ttcid, LogicTuples.newInstance("ttt", TupleArguments.newValueArgument(i)),
                        Long.MAX_VALUE);
            }
            // snapshot test n. 2
            acc.out(ttcidOrg, LogicTuples.newInstance("cmd", TupleArguments.newValueArgument(
                    "enable_persistency", TupleArguments.newValueArgument("def", TupleArguments.newVarArgument()))),
                    Long.MAX_VALUE);
            // tuples addition
            for (; i < 3000; i++) {
                acc.out(ttcid, LogicTuples.newInstance("ttt", TupleArguments.newValueArgument(i)),
                        Long.MAX_VALUE);
            }
            acc.exit();
            // give node time to close persistency file
            Thread.sleep(3000);
            tns.shutdown();
        } catch (final TucsonInvalidTupleCentreIdException | InterruptedException | InvalidLogicTupleException | IOException | OperationTimeOutException | UnreachableNodeException | TucsonOperationNotPossibleException | TucsonInvalidAgentIdException e) {
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
