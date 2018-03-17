package alice.tuplecentre.respect.core;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

import alice.tuple.logic.LogicTuple;
import alice.tuple.logic.LogicTuples;
import alice.tuple.logic.exceptions.InvalidLogicTupleException;
import alice.tuplecentre.api.TupleCentreIdentifier;
import alice.tuplecentre.api.exceptions.InvalidOperationException;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;
import alice.tuplecentre.respect.api.TupleCentreId;
import alice.tuplecentre.respect.api.exceptions.InvalidTupleCentreIdException;
import alice.tuplecentre.respect.situatedness.ActuatorId;
import alice.tuplecentre.respect.situatedness.ISimpleProbe;
import alice.tuplecentre.respect.situatedness.ProbeIdentifier;
import alice.tuplecentre.respect.situatedness.SensorId;
import alice.tuplecentre.respect.situatedness.TransducerId;
import alice.tuplecentre.tucson.api.TucsonAgentId;
import alice.tuplecentre.tucson.api.TucsonAgentIdDefault;
import alice.tuplecentre.tucson.api.acc.OrdinaryAndSpecificationSyncACC;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tuplecentre.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tuplecentre.tucson.api.exceptions.UnreachableNodeException;
import alice.tuplecentre.tucson.service.ACCProxyAgentSide;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Environment configuration agent.
 * <p>
 * It checks for requests on '$ENV' and delegates them to the TransducerManager.
 *
 * @author Steven Maraldi
 */
public class EnvConfigAgent {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().getClass());

    /**
     * Add an actuator request's type
     **/
    private static final String ADD_ACTUATOR = "addActuator";
    /**
     * Add a sensor request's type
     **/
    private static final String ADD_SENSOR = "addSensor";
    /**
     * Change the transducer associated to a resource request's type
     **/
    private static final String CHANGE_TRANSDUCER = "changeTransducer";
    /**
     * Create transducer actuator request's type
     **/
    private static final String CREATE_TRANSDUCER_ACTUATOR = "createTransducerActuator";
    /**
     * Create transducer sensor request's type
     **/
    private static final String CREATE_TRANSDUCER_SENSOR = "createTransducerSensor";
    /**
     * Remove a resource request's type
     **/
    private static final String REMOVE_RESOURCE = "removeResource";
    /**
     * The tuple centre used for environment configuration
     **/
    private TupleCentreIdentifier idEnvTC;
    private boolean iteraction = true;


    private final TucsonAgentId agentId;
    private final String nodeToConfigIpAddress;
    private final int nodeToConfigPortNumber;

    /**
     * @param ipAddress the netid of the TuCSoN Node this environment configuration
     *                  agent works with
     * @param portno    the listening port of the TuCSoN Node this environment
     *                  configuration agent works with
     * @throws TucsonInvalidAgentIdException this cannot actually happen, since this agent identifier is
     *                                       given, then well-formed
     */
    public EnvConfigAgent(final String ipAddress, final int portno)
            throws TucsonInvalidAgentIdException {
        this.agentId = new TucsonAgentIdDefault("'$EnvAgent'");
        this.nodeToConfigIpAddress = ipAddress;
        this.nodeToConfigPortNumber = portno;
        try {
            this.idEnvTC = new TupleCentreId("'$ENV'", ipAddress,
                    String.valueOf(portno));
        } catch (final InvalidTupleCentreIdException e) {
            // TODO Auto-generated catch block
            LOGGER.error(e.getMessage(), e);
        }
        this.go();
    }

    /**
     * Starts main execution cycle
     */
    public void go() {
        new Thread() {
            @Override
            public void run() {
                OrdinaryAndSpecificationSyncACC acc = null; //TODO how to gain ACC??
                try {
                    // TODO Temporary solution!!! ACCProxyAgentSide will be moved soon to "client" sub-project
                    acc = new ACCProxyAgentSide(agentId, nodeToConfigIpAddress, nodeToConfigPortNumber);
                } catch (TucsonInvalidAgentIdException e) {
                    LOGGER.error(e.getMessage(), e);
                }
                while (iteraction) {
                    try {
                        // Gets the command from the tuple space
                        LogicTuple t = LogicTuples.parse("cmd(Type)");
                        t = Objects.requireNonNull(acc).in(idEnvTC, t, null).getLogicTupleResult();
                        switch (t.getArg(0)
                                .toString()) {
                            case EnvConfigAgent.CREATE_TRANSDUCER_SENSOR: {
                                t = LogicTuples
                                        .parse("createTransducerSensor(Tcid,Tclass,Tid,Pclass,Pid)");
                                t = acc.in(idEnvTC, t, null).getLogicTupleResult();
                                // Obtaining transducer
                                final TransducerId tId = new TransducerId(t.getArg(2)
                                        .getName());
                                // Obtaining tuple centre and its properties
                                final String[] sTcId = t.getArg(0).toString().split(":"); // '@'(name,':'(node,port))

                                final String tcName = sTcId[0].substring(
                                        sTcId[0].indexOf('(') + 1, sTcId[0].indexOf(','));
                                final String[] tcNodeAndPort = sTcId[1].substring(
                                        sTcId[1].indexOf('(') + 1, sTcId[1].indexOf(')'))
                                        .split(",");
                                final TupleCentreIdentifier tcId = new TupleCentreId(tcName,
                                        tcNodeAndPort[0], tcNodeAndPort[1]);
                                speak("Serving 'createTransducer' request < TransducerId="
                                        + tId
                                        + ", associated TC="
                                        + t.getArg(0).toString()
                                        + " >...");
                                // Obtaining resource
                                final ProbeIdentifier pId = new SensorId(t.getArg(4)
                                        .getName());
                                ProbesManager.INSTANCE.createProbe(t.getArg(3).toString(),
                                        pId);
                                final TransducersManager tm = TransducersManager.INSTANCE;
                                tm.createTransducer(t.getArg(1).toString(), tId, tcId, pId);
                                break;
                            }
                            case EnvConfigAgent.CREATE_TRANSDUCER_ACTUATOR: {
                                t = LogicTuples
                                        .parse("createTransducerActuator(Tcid,Tclass,Tid,Pclass,Pid)");
                                t = acc.in(idEnvTC, t, null).getLogicTupleResult();
                                // Obtaining transducer
                                final TransducerId tId = new TransducerId(t.getArg(2)
                                        .getName());
                                // Obtaining tuple centre and its properties
                                final String[] sTcId = t.getArg(0).toString().split(":"); // '@'(name,':'(node,port))

                                final String tcName = sTcId[0].substring(
                                        sTcId[0].indexOf('(') + 1, sTcId[0].indexOf(','));
                                final String[] tcNodeAndPort = sTcId[1].substring(
                                        sTcId[1].indexOf('(') + 1, sTcId[1].indexOf(')'))
                                        .split(",");
                                final TupleCentreIdentifier tcId = new TupleCentreId(tcName,
                                        tcNodeAndPort[0], tcNodeAndPort[1]);
                                speak("Serving 'createTransducer' request < TransducerId="
                                        + tId
                                        + ", associated TC="
                                        + t.getArg(0).toString()
                                        + " >...");
                                // Obtaining resource
                                final ProbeIdentifier pId = new ActuatorId(t.getArg(4)
                                        .getName());
                                ProbesManager.INSTANCE.createProbe(t.getArg(3).toString(),
                                        pId);
                                final TransducersManager tm = TransducersManager.INSTANCE;
                                // Building transducer
                                tm.createTransducer(t.getArg(1).toString(), tId, tcId, pId);
                                break;
                            }
                            case EnvConfigAgent.ADD_SENSOR: {
                                t = LogicTuples.parse("addSensor(Class,Pid,Tid)");
                                t = acc.in(idEnvTC, t, null).getLogicTupleResult();
                                // Creating resource
                                final ProbeIdentifier pId = new SensorId(t.getArg(1)
                                        .getName());
                                final ProbesManager rm = ProbesManager.INSTANCE;
                                rm.createProbe(t.getArg(0).toString(), pId);
                                final ISimpleProbe probe = rm.getProbe(pId);
                                final TransducersManager tm = TransducersManager.INSTANCE;
                                final TransducerId tId = Objects.requireNonNull(tm.getTransducer(
                                        t.getArg(2).getName())).getIdentifier();
                                speak("Serving 'addSensor' request < ProbeId=" + pId
                                        + ", associated transducer=" + tId + " >...");
                                tm.addProbe(Objects.requireNonNull(probe).getIdentifier(), tId, probe);
                                break;
                            }
                            case EnvConfigAgent.ADD_ACTUATOR: {
                                t = LogicTuples.parse("addActuator(Class,Pid,Tid)");
                                t = acc.in(idEnvTC, t, null).getLogicTupleResult();
                                // Creating resource
                                final ProbeIdentifier pId = new ActuatorId(t.getArg(1)
                                        .getName());
                                final ProbesManager rm = ProbesManager.INSTANCE;
                                rm.createProbe(t.getArg(0).toString(), pId);
                                final ISimpleProbe probe = rm.getProbe(pId);
                                final TransducersManager tm = TransducersManager.INSTANCE;
                                final TransducerId tId = Objects.requireNonNull(tm.getTransducer(
                                        t.getArg(2).getName())).getIdentifier();
                                speak("Serving 'addActuator' request < ProbeId=" + pId
                                        + ", associated transducer=" + tId + " >...");
                                tm.addProbe(Objects.requireNonNull(probe).getIdentifier(), tId, probe);
                                break;
                            }
                            case EnvConfigAgent.REMOVE_RESOURCE: {
                                speak("Serving 'removeResource' request < ProbeId="
                                        + t.getArg(0).getName() + " >...");
                                t = LogicTuples.parse("removeResource(Pid)");
                                t = acc.in(idEnvTC, t, null).getLogicTupleResult();
                                final ProbesManager rm = ProbesManager.INSTANCE;
                                final ISimpleProbe probe = rm.getProbeByName(t.getArg(0)
                                        .getName());
                                rm.removeProbe(Objects.requireNonNull(probe).getIdentifier());
                                break;
                            }
                            case EnvConfigAgent.CHANGE_TRANSDUCER: {
                                t = LogicTuples.parse("changeTransducer(Pid,Tid)");
                                t = acc.in(idEnvTC, t, null).getLogicTupleResult();
                                final ProbesManager rm = ProbesManager.INSTANCE;
                                final ProbeIdentifier pId = Objects.requireNonNull(rm.getProbeByName(
                                        t.getArg(0).getName())).getIdentifier();
                                final TransducersManager tm = TransducersManager.INSTANCE;
                                final TransducerId tId = Objects.requireNonNull(tm.getTransducer(
                                        t.getArg(1).getName())).getIdentifier();
                                speak("Serving 'changeTransducer' request < ProbeId="
                                        + pId + ", new transducer=" + tId + " >...");
                                rm.setTransducer(pId, tId);
                                break;
                            }
                        }
                        acc.exit();
                    } catch (final InvalidLogicTupleException | InvocationTargetException | IllegalAccessException | InstantiationException | NoSuchMethodException | ClassNotFoundException | InvalidTupleCentreIdException | TucsonInvalidAgentIdException | InvalidOperationException | OperationTimeOutException | UnreachableNodeException | TucsonOperationNotPossibleException e) {
                        LOGGER.error(e.getMessage(), e);
                    }
                }
            }
        }.start();
    }

    /**
     *
     */
    public void stopIteraction() {
        this.iteraction = false;
    }

    private void speak(final Object msg) {
        LOGGER.info("..[$EnvAgent (" + this.idEnvTC + ")]: " + msg);
    }
}
