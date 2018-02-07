package uniform.swarms.env;

import java.io.IOException;
import alice.logictuple.LogicTuple;
import alice.logictuple.exceptions.InvalidLogicTupleException;
import alice.tucson.api.EnhancedSynchACC;
import alice.tucson.api.NegotiationACC;
import alice.tucson.api.TucsonAgentId;
import alice.tucson.api.TucsonMetaACC;
import alice.tucson.api.TucsonTupleCentreId;
import alice.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tucson.api.exceptions.TucsonInvalidTupleCentreIdException;
import alice.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tucson.api.exceptions.UnreachableNodeException;
import alice.tucson.utilities.Utils;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;

/**
 * @author Stefano Mariani (mailto: s [dot] mariani [at] unibo [dot] it)
 *
 */
public final class Environment {

    private static final String PATH = "./alice/tucson/examples/uniform/swarms/env/evaporation.rsp";

    private static TucsonAgentId me;
    private static NegotiationACC negAcc;
    private static EnhancedSynchACC acc;

    /**
     *
     */
    public static void config() {

        Environment.acquireACC();

        final String spec = Environment.parseSpec();

        Environment.log("Configuring environment...");

        Environment.initAnthill(spec);

        Environment.initShortPath(spec);

        Environment.initLongPath(spec);

        Environment.initFoodSource();

        Environment.log("...environment configured");

    }

    /**
     *
     */
    private static void initFoodSource() {
        TucsonTupleCentreId tcid;
        try {
            Environment.log("Configuring <food>...");
            tcid = new TucsonTupleCentreId("food", "localhost", "20504");
            Environment.acc
                    .outAll(tcid,
                            LogicTuple
                                    .parse("[nbr(short@localhost:20506),nbr(long2@localhost:20505)]"),
                            null);
            Environment.acc
                    .outAll(tcid,
                            LogicTuple
                                    .parse("[anthill(short@localhost:20506),anthill(short@localhost:20506),anthill(long2@localhost:20505)]"),
                            null);
            for (int i = 0; i < 1000; i++) {
                Environment.acc.out(tcid, LogicTuple.parse("food"), null);
            }
        } catch (final InvalidLogicTupleException e) {
            Environment.err("Invalid tuple given: " + e.getCause());
            System.exit(-1);
        } catch (final TucsonInvalidTupleCentreIdException e) {
            Environment.err("Invalid tc id given: " + e.getCause());
            System.exit(-1);
        } catch (final TucsonOperationNotPossibleException e) {
            Environment.err("Operation forbidden: " + e.getCause());
            System.exit(-1);
        } catch (final UnreachableNodeException e) {
            Environment.err("Cannot reach node: " + e.getCause());
            System.exit(-1);
        } catch (final OperationTimeOutException e) {
            Environment.err("Timeout exceeded: " + e.getCause());
            System.exit(-1);
        }
        Environment.log("...<food> configured");
    }

    /**
     * @param spec
     */
    private static void initLongPath(final String spec) {
        TucsonTupleCentreId tcid;
        try {
            Environment.log("Configuring <long1>...");
            tcid = new TucsonTupleCentreId("long1", "localhost", "20507");
            Environment.acc
                    .outAll(tcid,
                            LogicTuple
                                    .parse("[nbr(anthill@localhost:20508),nbr(long2@localhost:20505)]"),
                            null);
            Environment.acc.out(tcid,
                    LogicTuple.parse("anthill(anthill@localhost:20508)"), null);
            Environment.acc.setS(tcid, spec, null);
            Environment.acc.out(tcid, LogicTuple.parse("'$start_evaporation'"),
                    null);
        } catch (final InvalidLogicTupleException e) {
            Environment.err("Invalid tuple given: " + e.getCause());
            System.exit(-1);
        } catch (final TucsonInvalidTupleCentreIdException e) {
            Environment.err("Invalid tc id given: " + e.getCause());
            System.exit(-1);
        } catch (final TucsonOperationNotPossibleException e) {
            Environment.err("Operation forbidden: " + e.getCause());
            System.exit(-1);
        } catch (final UnreachableNodeException e) {
            Environment.err("Cannot reach node: " + e.getCause());
            System.exit(-1);
        } catch (final OperationTimeOutException e) {
            Environment.err("Timeout exceeded: " + e.getCause());
            System.exit(-1);
        }
        Environment.log("...<long1> configured");

        try {
            Environment.log("Configuring <long2>...");
            tcid = new TucsonTupleCentreId("long2", "localhost", "20505");
            Environment.acc
                    .outAll(tcid,
                            LogicTuple
                                    .parse("[nbr(long1@localhost:20507),nbr(food@localhost:20504)]"),
                            null);
            Environment.acc.out(tcid,
                    LogicTuple.parse("anthill(long1@localhost:20507)"), null);
            Environment.acc.setS(tcid, spec, null);
            Environment.acc.out(tcid, LogicTuple.parse("'$start_evaporation'"),
                    null);
        } catch (final InvalidLogicTupleException e) {
            Environment.err("Invalid tuple given: " + e.getCause());
            System.exit(-1);
        } catch (final TucsonInvalidTupleCentreIdException e) {
            Environment.err("Invalid tc id given: " + e.getCause());
            System.exit(-1);
        } catch (final TucsonOperationNotPossibleException e) {
            Environment.err("Operation forbidden: " + e.getCause());
            System.exit(-1);
        } catch (final UnreachableNodeException e) {
            Environment.err("Cannot reach node: " + e.getCause());
            System.exit(-1);
        } catch (final OperationTimeOutException e) {
            Environment.err("Timeout exceeded: " + e.getCause());
            System.exit(-1);
        }
        Environment.log("...<long2> configured");
    }

    /**
     * @param spec
     */
    private static void initShortPath(final String spec) {
        TucsonTupleCentreId tcid;
        try {
            Environment.log("Configuring <short>...");
            tcid = new TucsonTupleCentreId("short", "localhost", "20506");
            Environment.acc
                    .outAll(tcid,
                            LogicTuple
                                    .parse("[nbr(anthill@localhost:20508),nbr(food@localhost:20504)]"),
                            null);
            Environment.acc.out(tcid,
                    LogicTuple.parse("anthill(anthill@localhost:20508)"), null);
            Environment.acc.setS(tcid, spec, null);
            Environment.acc.out(tcid, LogicTuple.parse("'$start_evaporation'"),
                    null);
        } catch (final InvalidLogicTupleException e) {
            Environment.err("Invalid tuple given: " + e.getCause());
            System.exit(-1);
        } catch (final TucsonInvalidTupleCentreIdException e) {
            Environment.err("Invalid tc id given: " + e.getCause());
            System.exit(-1);
        } catch (final TucsonOperationNotPossibleException e) {
            Environment.err("Operation forbidden: " + e.getCause());
            System.exit(-1);
        } catch (final UnreachableNodeException e) {
            Environment.err("Cannot reach node: " + e.getCause());
            System.exit(-1);
        } catch (final OperationTimeOutException e) {
            Environment.err("Timeout exceeded: " + e.getCause());
            System.exit(-1);
        }
        Environment.log("...<short> configured");
    }

    /**
     * @param spec
     */
    private static void initAnthill(final String spec) {
        TucsonTupleCentreId tcid;
        try {
            Environment.log("Configuring <anthill>...");
            tcid = new TucsonTupleCentreId("anthill", "localhost", "20508");
            Environment.acc
                    .outAll(tcid,
                            LogicTuple
                                    .parse("[nbr(short@localhost:20506),nbr(long1@localhost:20507)]"),
                            null);
            Environment.acc.setS(tcid, spec, null);
            Environment.acc.out(tcid, LogicTuple.parse("'$start_evaporation'"),
                    null);
        } catch (final InvalidLogicTupleException e) {
            Environment.err("Invalid tuple given: " + e.getCause());
            System.exit(-1);
        } catch (final TucsonInvalidTupleCentreIdException e) {
            Environment.err("Invalid tc id given: " + e.getCause());
            System.exit(-1);
        } catch (final TucsonOperationNotPossibleException e) {
            Environment.err("Operation forbidden: " + e.getCause());
            System.exit(-1);
        } catch (final UnreachableNodeException e) {
            Environment.err("Cannot reach node: " + e.getCause());
            System.exit(-1);
        } catch (final OperationTimeOutException e) {
            Environment.err("Timeout exceeded: " + e.getCause());
            System.exit(-1);
        }
        Environment.log("...<anthill> configured");
    }

    /**
     * @return
     */
    private static String parseSpec() {
        String spec = null;
        try {
            spec = Utils.fileToString(Environment.PATH);
        } catch (final IOException e) {
            Environment.err("Cannot read spec from " + Environment.PATH);
            System.exit(-1);
        }
        return spec;
    }

    /**
     *
     */
    private static void acquireACC() {
        try {
            Environment.me = new TucsonAgentId("env");
            Environment.negAcc = TucsonMetaACC
                    .getNegotiationContext(Environment.me);
            Environment.acc = Environment.negAcc.playDefaultRole();
        } catch (final TucsonInvalidAgentIdException e) {
            Environment.err("Invalid agent id given: " + e.getCause());
            System.exit(-1);
        } catch (final TucsonOperationNotPossibleException e) {
            Environment.err("Operation forbidden: " + e.getCause());
            System.exit(-1);
        } catch (final UnreachableNodeException e) {
            Environment.err("Cannot reach node: " + e.getCause());
            System.exit(-1);
        } catch (final OperationTimeOutException e) {
            Environment.err("Timeout exceeded: " + e.getCause());
            System.exit(-1);
        }
    }

    private static void log(final String msg) {
        System.out.println("[ENV]: " + msg);
    }

    private static void err(final String msg) {
        System.err.println("[ENV]: " + msg);
    }

}
