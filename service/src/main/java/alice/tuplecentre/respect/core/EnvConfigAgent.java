package alice.tuplecentre.respect.core;

import java.lang.reflect.InvocationTargetException;

import alice.logictuple.LogicTuple;
import alice.logictuple.exceptions.InvalidLogicTupleException;
import alice.tuplecentre.api.exceptions.InvalidOperationException;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;
import alice.tuplecentre.respect.api.TupleCentreId;
import alice.tuplecentre.respect.api.exceptions.InvalidTupleCentreIdException;
import alice.tuplecentre.respect.situatedness.AbstractProbeId;
import alice.tuplecentre.respect.situatedness.ActuatorId;
import alice.tuplecentre.respect.situatedness.ISimpleProbe;
import alice.tuplecentre.respect.situatedness.SensorId;
import alice.tuplecentre.respect.situatedness.TransducerId;
import alice.tuplecentre.tucson.api.TucsonAgentId;
import alice.tuplecentre.tucson.api.acc.OrdinaryAndSpecificationSyncACC;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tuplecentre.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tuplecentre.tucson.api.exceptions.UnreachableNodeException;
import alice.tuplecentre.tucson.service.ACCProxyAgentSide;

/**
 *
 * Environment configuration agent.
 *
 * It checks for requests on '$ENV' and delegates them to the TransducerManager.
 *
 * @author Steven Maraldi
 *
 */
public class EnvConfigAgent {

    /** Add an actuator request's type **/
    private static final String ADD_ACTUATOR = "addActuator";
    /** Add a sensor request's type **/
    private static final String ADD_SENSOR = "addSensor";
    /** Change the transducer associated to a resource request's type **/
    private static final String CHANGE_TRANSDUCER = "changeTransducer";
    /** Create transducer actuator request's type **/
    private static final String CREATE_TRANSDUCER_ACTUATOR = "createTransducerActuator";
    /** Create transducer sensor request's type **/
    private static final String CREATE_TRANSDUCER_SENSOR = "createTransducerSensor";
    /** Remove a resource request's type **/
    private static final String REMOVE_RESOURCE = "removeResource";
    /** The tuple centre used for environment configuration **/
    private TupleCentreId idEnvTC;
    private boolean iteraction = true;


    private final TucsonAgentId agentId;
    private final String nodeToConfigIpAddress;
    private final int nodeToConfigPortNumber;

    /**
     *
     * @param ipAddress
     *            the netid of the TuCSoN Node this environment configuration
     *            agent works with
     * @param portno
     *            the listening port of the TuCSoN Node this environment
     *            configuration agent works with
     * @throws TucsonInvalidAgentIdException
     *             this cannot actually happen, since this agent identifier is
     *             given, then well-formed
     */
    public EnvConfigAgent(final String ipAddress, final int portno)
            throws TucsonInvalidAgentIdException {
        this.agentId = new TucsonAgentId("'$EnvAgent'");
        this.nodeToConfigIpAddress = ipAddress;
        this.nodeToConfigPortNumber = portno;
        try {
            this.idEnvTC = new TupleCentreId("'$ENV'", ipAddress,
                    String.valueOf(portno));
        } catch (final InvalidTupleCentreIdException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        this.go();
    }

    /**
     * Starts main execution cycle
     */
    public void go() {
        new Thread(){
            @Override
            public void run() {
                OrdinaryAndSpecificationSyncACC acc=null; //TODO how to gain ACC??
                try {
                    // TODO Temporary solution!!! ACCProxyAgentSide will be moved soon to "client" sub-project
                    acc = new ACCProxyAgentSide(agentId,nodeToConfigIpAddress,nodeToConfigPortNumber);
                } catch (TucsonInvalidAgentIdException e) {
                    e.printStackTrace();
                }
                while (iteraction) {
                    try {
                        // Gets the command from the tuple space
                        LogicTuple t = LogicTuple.parse("cmd(Type)");
                        t = acc.in(idEnvTC, t, null).getLogicTupleResult();
                        if (EnvConfigAgent.CREATE_TRANSDUCER_SENSOR.equals(t.getArg(0)
                                .toString())) {
                            t = LogicTuple
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
                            final TupleCentreId tcId = new TupleCentreId(tcName,
                                    tcNodeAndPort[0], tcNodeAndPort[1]);
                            speak("Serving 'createTransducer' request < TransducerId="
                                    + tId
                                    + ", associated TC="
                                    + t.getArg(0).toString()
                                    + " >...");
                            // Obtaining resource
                            final AbstractProbeId pId = new SensorId(t.getArg(4)
                                    .getName());
                            ProbesManager.INSTANCE.createProbe(t.getArg(3).toString(),
                                    pId);
                            final TransducersManager tm = TransducersManager.INSTANCE;
                            tm.createTransducer(t.getArg(1).toString(), tId, tcId, pId);
                        } else if (EnvConfigAgent.CREATE_TRANSDUCER_ACTUATOR.equals(t
                                .getArg(0).toString())) {
                            t = LogicTuple
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
                            final TupleCentreId tcId = new TupleCentreId(tcName,
                                    tcNodeAndPort[0], tcNodeAndPort[1]);
                            speak("Serving 'createTransducer' request < TransducerId="
                                    + tId
                                    + ", associated TC="
                                    + t.getArg(0).toString()
                                    + " >...");
                            // Obtaining resource
                            final AbstractProbeId pId = new ActuatorId(t.getArg(4)
                                    .getName());
                            ProbesManager.INSTANCE.createProbe(t.getArg(3).toString(),
                                    pId);
                            final TransducersManager tm = TransducersManager.INSTANCE;
                            // Building transducer
                            tm.createTransducer(t.getArg(1).toString(), tId, tcId, pId);
                        } else if (EnvConfigAgent.ADD_SENSOR.equals(t.getArg(0)
                                .toString())) {
                            t = LogicTuple.parse("addSensor(Class,Pid,Tid)");
                            t = acc.in(idEnvTC, t, null).getLogicTupleResult();
                            // Creating resource
                            final AbstractProbeId pId = new SensorId(t.getArg(1)
                                    .getName());
                            final ProbesManager rm = ProbesManager.INSTANCE;
                            rm.createProbe(t.getArg(0).toString(), pId);
                            final ISimpleProbe probe = rm.getProbe(pId);
                            final TransducersManager tm = TransducersManager.INSTANCE;
                            final TransducerId tId = tm.getTransducer(
                                    t.getArg(2).getName()).getIdentifier();
                            speak("Serving 'addSensor' request < ProbeId=" + pId
                                    + ", associated transducer=" + tId + " >...");
                            tm.addProbe(probe.getIdentifier(), tId, probe);
                        } else if (EnvConfigAgent.ADD_ACTUATOR.equals(t.getArg(0)
                                .toString())) {
                            t = LogicTuple.parse("addActuator(Class,Pid,Tid)");
                            t = acc.in(idEnvTC, t, null).getLogicTupleResult();
                            // Creating resource
                            final AbstractProbeId pId = new ActuatorId(t.getArg(1)
                                    .getName());
                            final ProbesManager rm = ProbesManager.INSTANCE;
                            rm.createProbe(t.getArg(0).toString(), pId);
                            final ISimpleProbe probe = rm.getProbe(pId);
                            final TransducersManager tm = TransducersManager.INSTANCE;
                            final TransducerId tId = tm.getTransducer(
                                    t.getArg(2).getName()).getIdentifier();
                            speak("Serving 'addActuator' request < ProbeId=" + pId
                                    + ", associated transducer=" + tId + " >...");
                            tm.addProbe(probe.getIdentifier(), tId, probe);
                        } else if (EnvConfigAgent.REMOVE_RESOURCE.equals(t.getArg(0)
                                .toString())) {
                            speak("Serving 'removeResource' request < ProbeId="
                                    + t.getArg(0).getName() + " >...");
                            t = LogicTuple.parse("removeResource(Pid)");
                            t = acc.in(idEnvTC, t, null).getLogicTupleResult();
                            final ProbesManager rm = ProbesManager.INSTANCE;
                            final ISimpleProbe probe = rm.getProbeByName(t.getArg(0)
                                    .getName());
                            rm.removeProbe(probe.getIdentifier());
                        } else if (EnvConfigAgent.CHANGE_TRANSDUCER.equals(t.getArg(0)
                                .toString())) {
                            t = LogicTuple.parse("changeTransducer(Pid,Tid)");
                            t = acc.in(idEnvTC, t, null).getLogicTupleResult();
                            final ProbesManager rm = ProbesManager.INSTANCE;
                            final AbstractProbeId pId = rm.getProbeByName(
                                    t.getArg(0).getName()).getIdentifier();
                            final TransducersManager tm = TransducersManager.INSTANCE;
                            final TransducerId tId = tm.getTransducer(
                                    t.getArg(1).getName()).getIdentifier();
                            speak("Serving 'changeTransducer' request < ProbeId="
                                    + pId + ", new transducer=" + tId + " >...");
                            rm.setTransducer(pId, tId);
                        }
                        acc.exit();
                    } catch (final InvalidLogicTupleException e) {
                        e.printStackTrace();
                    } catch (final TucsonOperationNotPossibleException e) {
                        e.printStackTrace();
                    } catch (final UnreachableNodeException e) {
                        e.printStackTrace();
                    } catch (final OperationTimeOutException e) {
                        e.printStackTrace();
                    } catch (final InvalidOperationException e) {
                        e.printStackTrace();
                    } catch (final TucsonInvalidAgentIdException e) {
                        e.printStackTrace();
                    } catch (final InvalidTupleCentreIdException e) {
                        e.printStackTrace();
                    } catch (final ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (final NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (final InstantiationException e) {
                        e.printStackTrace();
                    } catch (final IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (final InvocationTargetException e) {
                        e.printStackTrace();
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
        System.out.println("..[$EnvAgent (" + this.idEnvTC + ")]: " + msg);
    }
}
