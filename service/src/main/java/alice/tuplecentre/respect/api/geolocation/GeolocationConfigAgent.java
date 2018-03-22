package alice.tuplecentre.respect.api.geolocation;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

import alice.tuple.logic.LogicTuple;
import alice.tuple.logic.LogicTuples;
import alice.tuple.logic.TupleArgument;
import alice.tuple.logic.TupleArguments;
import alice.tuple.logic.exceptions.InvalidLogicTupleException;
import alice.tuple.logic.exceptions.InvalidVarNameException;
import alice.tuplecentre.core.InputEvent;
import alice.tuplecentre.core.TupleCentreOpType;
import alice.tuplecentre.respect.api.geolocation.service.GeoServiceId;
import alice.tuplecentre.respect.api.geolocation.service.GeoServiceIdentifier;
import alice.tuplecentre.respect.api.geolocation.service.GeolocationServiceManager;
import alice.tuplecentre.respect.core.RespectOperationDefault;
import alice.tuplecentre.tucson.api.TucsonAgentId;
import alice.tuplecentre.tucson.api.TucsonAgentIdDefault;
import alice.tuplecentre.tucson.api.TucsonTupleCentreId;
import alice.tuplecentre.tucson.api.TucsonTupleCentreIdDefault;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidLogicTupleException;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidTupleCentreIdException;
import alice.tuplecentre.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tuplecentre.tucson.service.TucsonNodeService;
import alice.tuplecentre.tucson.service.TupleCentreContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Geolocation configuration support agent. It checks for requests on
 * geolocationConfigTC and delegates them to the GeolocationServiceManager.
 *
 * @author Michele Bombardi (mailto: michele.bombardi@studio.unibo.it)
 */
public class GeolocationConfigAgent extends Thread {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * Geolocation service request's type
     **/
    private static final String CREATE_GEOLOCATION_SERVICE = "createGeolocationService";
    private static final String DESTROY_GEOLOCATION_SERVICE = "destroyGeolocationService";

    private static void log(final String s) {
        LOGGER.info("[GeolocationConfigAgent]: " + s);
    }

    private final TucsonTupleCentreId config;
    private final TucsonNodeService node;
    private TucsonAgentId nodeManAid;

    /**
     * Construct a GeolocationConfigAgent
     *
     * @param conf represents the IP address on which the associated tuple centre
     *             is running
     * @param n    represents the port number on which the associated tuple
     *             centre is listening
     */
    public GeolocationConfigAgent(final TucsonTupleCentreId conf,
                                  final TucsonNodeService n) {
        super();
        try {
            this.nodeManAid = new TucsonAgentIdDefault("geolocationConfigAgent");
        } catch (final TucsonInvalidAgentIdException e) {
            LOGGER.error(e.getMessage(), e);
        }
        this.node = n;
        this.config = conf;
        this.start();
    }

    @Override
    public void run() {
        try {
            //noinspection InfiniteLoopStatement
            while (true) {
                Object cmd;
                final RespectOperationDefault opRequested = RespectOperationDefault.make(
                        TupleCentreOpType.IN, LogicTuples.newInstance("cmd",
                                TupleArguments.newVarArgument("X")), null);
                final InputEvent ev = new InputEvent(this.nodeManAid,
                        opRequested, this.config, System.currentTimeMillis(),
                        null);
                cmd = TupleCentreContainer.doBlockingOperation(ev);
                if (cmd != null) {
                    this.execCmd(((LogicTuple) cmd).getArg(0));
                } else {
                    throw new InterruptedException();
                }
            }
        } catch (final InvalidVarNameException | InvocationTargetException | IllegalAccessException | InstantiationException | NoSuchMethodException | ClassNotFoundException | TucsonInvalidTupleCentreIdException | IllegalArgumentException | SecurityException | InvalidLogicTupleException | TucsonOperationNotPossibleException | TucsonInvalidLogicTupleException e) {
            LOGGER.error(e.getMessage(), e);
            this.node.removeNodeAgent(this);
        } catch (final InterruptedException e) {
            GeolocationConfigAgent
                    .log("Shutdown interrupt received, shutting down...");
            this.node.removeNodeAgent(this);
        }
    }

    private void execCmd(final TupleArgument cmd)
            throws
            TucsonInvalidLogicTupleException,
            TucsonOperationNotPossibleException, InvalidLogicTupleException,
            TucsonInvalidTupleCentreIdException, SecurityException,
            IllegalArgumentException, ClassNotFoundException,
            NoSuchMethodException, InstantiationException,
            IllegalAccessException, InvocationTargetException {
        final String name = cmd.getName();
        GeolocationConfigAgent.log("Executing command " + name);
        LogicTuple t;
        if (GeolocationConfigAgent.CREATE_GEOLOCATION_SERVICE.equals(name)) {
            t = LogicTuples.parse("createGeolocationService(Sid,Sclass,Stcid)");
            final RespectOperationDefault opRequested = RespectOperationDefault.make(
                    TupleCentreOpType.IN, t, null);
            final InputEvent ev = new InputEvent(this.nodeManAid, opRequested,
                    this.config, System.currentTimeMillis(), null);
            t = (LogicTuple) TupleCentreContainer.doBlockingOperation(ev);
            final GeoServiceIdentifier sId = new GeoServiceId(Objects.requireNonNull(t).getArg(0).getName());
            // Building service
            final String[] sTcId = t.getArg(2).toString().split(":"); // '@'(name,':'(node,port))
            final String tcName = sTcId[0].substring(sTcId[0].indexOf('(') + 1,
                    sTcId[0].indexOf(','));
            final String[] tcNodeAndPort = sTcId[1].substring(
                    sTcId[1].indexOf('(') + 1, sTcId[1].indexOf(')'))
                    .split(",");
            final TucsonTupleCentreId tcId = new TucsonTupleCentreIdDefault(tcName,
                    tcNodeAndPort[0], tcNodeAndPort[1]);
            final int platform = PlatformUtils.getPlatform();
            GeolocationConfigAgent
                    .log("Serving create android geolocation service request. EmitterIdentifier: "
                            + sId.getLocalName()
                            + "; TC Associated: "
                            + t.getArg(2).toString());
            GeolocationServiceManager.getGeolocationManager()
                    .createNodeService(platform, sId, t.getArg(1).toString(),
                            tcId);
        } else if (GeolocationConfigAgent.DESTROY_GEOLOCATION_SERVICE
                .equals(name)) {
            t = LogicTuples.parse("destroyGeolocationService(Sid)");
            final RespectOperationDefault opRequested = RespectOperationDefault.make(
                    TupleCentreOpType.IN, t, null);
            final InputEvent ev = new InputEvent(this.nodeManAid, opRequested,
                    this.config, System.currentTimeMillis(), null);
            t = (LogicTuple) TupleCentreContainer.doBlockingOperation(ev);
            final GeoServiceIdentifier sId = new GeoServiceId(Objects.requireNonNull(t).getArg(0).getName());
            GeolocationConfigAgent
                    .log("Serving destroy android geolocation service request. EmitterIdentifier: "
                            + sId.getLocalName());
            GeolocationServiceManager.getGeolocationManager().destroyService(
                    sId);
        }
    }
}
