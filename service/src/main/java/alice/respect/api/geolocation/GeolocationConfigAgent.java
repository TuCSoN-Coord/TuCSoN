package alice.respect.api.geolocation;

import java.lang.reflect.InvocationTargetException;

import alice.respect.api.geolocation.service.GeoServiceId;
import alice.respect.api.geolocation.service.GeolocationServiceManager;
import alice.respect.core.RespectOperationDefault;
import alice.tucson.api.TucsonAgentId;
import alice.tucson.api.TucsonTupleCentreId;
import alice.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tucson.api.exceptions.TucsonInvalidLogicTupleException;
import alice.tucson.api.exceptions.TucsonInvalidTupleCentreIdException;
import alice.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tucson.service.TucsonNodeService;
import alice.tucson.service.TupleCentreContainer;
import alice.tuple.logic.LogicTuple;
import alice.tuple.logic.LogicTuples;
import alice.tuple.logic.TupleArgument;
import alice.tuple.logic.VarArgument;
import alice.tuple.logic.exceptions.InvalidLogicTupleException;
import alice.tuple.logic.exceptions.InvalidLogicTupleOperationException;
import alice.tuple.logic.exceptions.InvalidVarNameException;
import alice.tuplecentre.core.InputEvent;
import alice.tuplecentre.core.TupleCentreOpType;
import alice.tuprolog.InvalidTermException;

/**
 * Geolocation configuration support agent. It checks for requests on
 * geolocationConfigTC and delegates them to the GeolocationServiceManager.
 * 
 * @author Michele Bombardi (mailto: michele.bombardi@studio.unibo.it)
 * 
 */
public class GeolocationConfigAgent extends Thread {
    /** Geolocation service request's type **/
    private static final String CREATE_GEOLOCATION_SERVICE = "createGeolocationService";
    private static final String DESTROY_GEOLOCATION_SERVICE = "destroyGeolocationService";

    private static void log(final String s) {
        System.out.println("[GeolocationConfigAgent]: " + s);
    }

    private final TucsonTupleCentreId config;
    private final TucsonNodeService node;
    private TucsonAgentId nodeManAid;

    /**
     * Construct a GeolocationConfigAgent
     * 
     * @param conf
     *            represents the IP address on which the associated tuple centre
     *            is running
     * @param n
     *            represents the port number on which the associated tuple
     *            centre is listening
     */
    public GeolocationConfigAgent(final TucsonTupleCentreId conf,
            final TucsonNodeService n) {
        super();
        try {
            this.nodeManAid = new TucsonAgentId("geolocationConfigAgent");
        } catch (final TucsonInvalidAgentIdException e) {
            e.printStackTrace();
        }
        this.node = n;
        this.config = conf;
        this.start();
    }

    @Override
    public void run() {
        try {
            while (true) {
                Object cmd;
                final RespectOperationDefault opRequested = RespectOperationDefault.make(
                        TupleCentreOpType.IN, LogicTuples.newInstance("cmd",
                                new VarArgument("X")), null);
                final InputEvent ev = new InputEvent(this.nodeManAid,
                        opRequested, this.config, System.currentTimeMillis(),
                        null);
                cmd = TupleCentreContainer.doBlockingOperation(ev);
                if (cmd != null) {
                    this.execCmd(((LogicTuple)cmd).getArg(0));
                } else {
                    throw new InterruptedException();
                }
            }
        } catch (final InvalidTermException e) {
            e.printStackTrace();
            this.node.removeNodeAgent(this);
        } catch (final InterruptedException e) {
            GeolocationConfigAgent
                    .log("Shutdown interrupt received, shutting down...");
            this.node.removeNodeAgent(this);
        } catch (final TucsonInvalidLogicTupleException e) {
            e.printStackTrace();
            this.node.removeNodeAgent(this);
        } catch (final TucsonOperationNotPossibleException e) {
            e.printStackTrace();
            this.node.removeNodeAgent(this);
        } catch (final InvalidLogicTupleException e) {
            e.printStackTrace();
            this.node.removeNodeAgent(this);
        } catch (final SecurityException e) {
            e.printStackTrace();
            this.node.removeNodeAgent(this);
        } catch (final IllegalArgumentException e) {
            e.printStackTrace();
            this.node.removeNodeAgent(this);
        } catch (final TucsonInvalidTupleCentreIdException e) {
            e.printStackTrace();
            this.node.removeNodeAgent(this);
        } catch (final ClassNotFoundException e) {
            e.printStackTrace();
            this.node.removeNodeAgent(this);
        } catch (final NoSuchMethodException e) {
            e.printStackTrace();
            this.node.removeNodeAgent(this);
        } catch (final InstantiationException e) {
            e.printStackTrace();
            this.node.removeNodeAgent(this);
        } catch (final IllegalAccessException e) {
            e.printStackTrace();
            this.node.removeNodeAgent(this);
        } catch (final InvocationTargetException e) {
            e.printStackTrace();
            this.node.removeNodeAgent(this);
        } catch (InvalidVarNameException e) {
			e.printStackTrace();
			this.node.removeNodeAgent(this);
		} catch (InvalidLogicTupleOperationException e) {
			e.printStackTrace();
			this.node.removeNodeAgent(this);
		}
    }

    private void execCmd(final TupleArgument cmd)
            throws InvalidLogicTupleOperationException,
            TucsonInvalidLogicTupleException,
            TucsonOperationNotPossibleException, InvalidLogicTupleException,
            TucsonInvalidTupleCentreIdException, SecurityException,
            IllegalArgumentException, ClassNotFoundException,
            NoSuchMethodException, InstantiationException,
            IllegalAccessException, InvocationTargetException {
        final String name = cmd.getName();
        GeolocationConfigAgent.log("Executing command " + name);
        LogicTuple t = null;
        if (GeolocationConfigAgent.CREATE_GEOLOCATION_SERVICE.equals(name)) {
            t = LogicTuples.parse("createGeolocationService(Sid,Sclass,Stcid)");
            final RespectOperationDefault opRequested = RespectOperationDefault.make(
                    TupleCentreOpType.IN, t, null);
            final InputEvent ev = new InputEvent(this.nodeManAid, opRequested,
                    this.config, System.currentTimeMillis(), null);
            t = (LogicTuple) TupleCentreContainer.doBlockingOperation(ev);
            final GeoServiceId sId = new GeoServiceId(t.getArg(0).getName());
            // Building service
            final String[] sTcId = t.getArg(2).toString().split(":"); // '@'(name,':'(node,port))
            final String tcName = sTcId[0].substring(sTcId[0].indexOf('(') + 1,
                    sTcId[0].indexOf(','));
            final String[] tcNodeAndPort = sTcId[1].substring(
                    sTcId[1].indexOf('(') + 1, sTcId[1].indexOf(')'))
                    .split(",");
            final TucsonTupleCentreId tcId = new TucsonTupleCentreId(tcName,
                    tcNodeAndPort[0], tcNodeAndPort[1]);
            final int platform = PlatformUtils.getPlatform();
            GeolocationConfigAgent
                    .log("Serving create android geolocation service request. Id: "
                            + sId.getName()
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
            final GeoServiceId sId = new GeoServiceId(t.getArg(0).getName());
            GeolocationConfigAgent
                    .log("Serving destroy android geolocation service request. Id: "
                            + sId.getName());
            GeolocationServiceManager.getGeolocationManager().destroyService(
                    sId);
        }
    }
}
