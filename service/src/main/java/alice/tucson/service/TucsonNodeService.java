/*
 * TuCSoN coordination infrastructure - Copyright (C) 2001-2002 aliCE team at
 * deis.unibo.it This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of the License,
 * or (at your option) any later version. This library is distributed in the
 * hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU Lesser General Public License for more details. You should have
 * received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */
package alice.tucson.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import alice.tuple.logic.LogicMatchingEngine;
import alice.tuple.logic.LogicTuple;
import alice.tuple.logic.LogicTupleDefault;
import alice.tuple.logic.TupleArgument;
import alice.tuple.logic.TupleArgumentDefault;
import alice.tuple.logic.ValueArgument;
import alice.tuple.logic.exceptions.InvalidLogicTupleException;
import alice.tuple.logic.exceptions.InvalidTupleArgumentException;
import alice.respect.api.exceptions.InvalidTupleCentreIdException;
import alice.respect.api.geolocation.GeolocationConfigAgent;
import alice.respect.core.EnvConfigAgent;
import alice.respect.core.RespectOperationDefault;
import alice.respect.core.RespectTC;
import alice.tucson.api.TucsonAgentId;
import alice.tucson.api.TucsonMetaACC;
import alice.tucson.api.TucsonTupleCentreId;
import alice.tucson.api.exceptions.InvalidConfigException;
import alice.tucson.api.exceptions.TucsonGenericException;
import alice.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tucson.api.exceptions.TucsonInvalidLogicTupleException;
import alice.tucson.api.exceptions.TucsonInvalidSpecificationException;
import alice.tucson.api.exceptions.TucsonInvalidTupleCentreIdException;
import alice.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tucson.api.exceptions.UnreachableNodeException;
import alice.tucson.introspection.InspectorContextSkel;
import alice.tucson.network.AbstractTucsonProtocol;
import alice.tucson.network.TPConfig;
import alice.tucson.network.exceptions.DialogInitializationException;
import alice.tucson.service.tools.TucsonACCTool;
import alice.tucson.utilities.Utils;
import alice.tuple.Tuple;
import alice.tuplecentre.core.InputEvent;
import alice.tuplecentre.core.TupleCentreOpType;
import alice.tuprolog.InvalidTheoryException;
import alice.tuprolog.MalformedGoalException;
import alice.tuprolog.Prolog;
import alice.tuprolog.Theory;
import alice.tuprolog.lib.InvalidObjectIdException;

/**
 *
 * @author Alessandro Ricci
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 * @author (contributor) Saverio Cicora
 *
 */
public class TucsonNodeService {

    private static final String TUCSON_NODE_SERVICE_RESOURCES_FOLDER = "tucson/service/config/";

    private static final String BOOT_SETUP_THEORY = TUCSON_NODE_SERVICE_RESOURCES_FOLDER + "boot.pl";
    private static final String DEFAULT_BOOT_SPEC_FILE = TUCSON_NODE_SERVICE_RESOURCES_FOLDER + "boot_spec.rsp";
    private static final String DEFAULT_ENVCONFIG_SPEC_FILE = TUCSON_NODE_SERVICE_RESOURCES_FOLDER + "env_spec.rsp";
    private static final String DEFAULT_GEOLOCATION_SPEC_FILE = TUCSON_NODE_SERVICE_RESOURCES_FOLDER + "geolocation_spec.rsp";
    private static final String DEFAULT_OBS_SPEC_FILE = TUCSON_NODE_SERVICE_RESOURCES_FOLDER + "obs_spec.rsp";

    private static final int DEFAULT_TCP_PORT = 20504;
    // how to set a "proper" number?
    private static final int MAX_EVENT_QUEUE_SIZE = 1000;
    private static final int MAX_UNBOUND_PORT = 64000;
    private static final Map<Integer, TucsonNodeService> NODES = new HashMap<Integer, TucsonNodeService>();

    private static final String PERSISTENCY_PATH = "persistent/";

    public static final synchronized TucsonNodeService getNode(final int port) {
        return TucsonNodeService.NODES.get(port);
    }

    /**
     *
     * @return the String representation of the TuCSoN version
     */
    public static String getVersion() {
        return TucsonMetaACC.getVersion();
    }

    public static boolean isInstalled(final int timeout)
            throws DialogInitializationException {
        try {
            return TucsonNodeService.isInstalled("localhost",
                    TucsonNodeService.DEFAULT_TCP_PORT, timeout);
        } catch (final UnreachableNodeException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isInstalled(final int port, final int timeout)
            throws DialogInitializationException {
        try {
            return TucsonNodeService.isInstalled("localhost", port, timeout);
        } catch (final UnreachableNodeException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @param netid
     *            the IP address where to test if a TuCSoN node is up and
     *            running
     * @param port
     *            the listening port where to test if a TuCSoN node is up and
     *            running
     * @param timeout
     *            the maximum waiting time the caller agent can afford to wait
     *            for a response
     * @return whether a TuCSoN node is up and active on the given port
     * @throws UnreachableNodeException
     *             if the given host is unknown
     * @throws DialogInitializationException
     *             if some network problems arise
     */
    public static boolean isInstalled(final String netid, final int port,
            final int timeout) throws UnreachableNodeException,
            DialogInitializationException {
        String reply = "";
        try (final Socket test = new Socket(netid, port);) {
            test.setReuseAddress(true);
            test.setSoTimeout(timeout);
            final ObjectInputStream ois = new ObjectInputStream(
                    new BufferedInputStream(test.getInputStream()));
            final ObjectOutputStream oos = new ObjectOutputStream(
                    new BufferedOutputStream(test.getOutputStream()));
            oos.writeInt(AbstractTucsonProtocol.NODE_ACTIVE_QUERY);
            oos.flush();
            reply = ois.readUTF();
        } catch (final ConnectException e) {
            reply = "";
        } catch (final UnknownHostException e) {
            throw new UnreachableNodeException("Host unknown", e);
        } catch (SocketTimeoutException e) {
            reply = "";
        } catch (final IOException e) {
            throw new DialogInitializationException(e);
        }
        return reply.startsWith("TuCSoN");
    }

    /**
     *
     * @param args
     *            the arguments to start the TuCSoN node with
     */
    public static void main(final String[] args) {
        if (alice.util.Tools.isOpt(args, "-help")
                || alice.util.Tools.isOpt(args, "-?")) {
            TucsonNodeService
                    .log("Arguments: -portno {portNumber} {-? | -help}");
        } else {
            final String portInfo = alice.util.Tools.getOpt(args, "-portno");
            final String configInfo = alice.util.Tools.getOpt(args, "-config");
            final String persistencyInfo = alice.util.Tools.getOpt(args,
                    "-persistency");
            int portNumber = TucsonNodeService.DEFAULT_TCP_PORT;
            if (portInfo != null) {
                try {
                    portNumber = Integer.parseInt(portInfo);
                    if (portNumber < 0
                            || portNumber > TucsonNodeService.MAX_UNBOUND_PORT) {
                        System.err.println("Invalid port number");
                        System.exit(-1);
                    }
                } catch (final NumberFormatException e) {
                    System.err.println("Invalid port number");
                    System.exit(-1);
                }
            }
            // at least check if the config file exists...you don't say?
            Tuple template = null;
            if (persistencyInfo != null) {
                try {
                    template = LogicTupleDefault.parse(persistencyInfo);
                } catch (final InvalidLogicTupleException e) {
                    System.err.println("Invalid persistency template");
                    System.exit(-1);
                }
            }
            /*
             * TODO CICORA: Only for compatibility, this code should be removed
             * TPConfigNodeSide is a singleton
             */
            new TucsonNodeService(configInfo, portNumber, template).install();
        }
    }

    private static void log(final String m) {
        System.out.println("[TuCSoN Node Service]: " + m);
    }

    private String adminPassword;

    private String adminUsername;

    private final List<TucsonAgentId> agents;

    // RBAC
    private String baseAgentClass;

    private final String configFile;

    private Prolog configManager;

    private Map<String, TucsonTCUsers> cores;
    private ACCProvider ctxman;
    private EnvConfigAgent envAgent;
    private TucsonTupleCentreId idConfigTC;
    private TucsonTupleCentreId idEnvTC; // Tuple centre for environment
    // configuration
    private TucsonTupleCentreId idGeolocationTC; // Tuple centre for geolocation
    // configuration
    private TucsonTupleCentreId idObsTC;
    private final ArrayList<InspectorContextSkel> inspectorAgents;
    private boolean inspectorsAuthorised;
    private Date installationDate;
    private boolean listAllRoles;
    private boolean loginRequired;
    private final List<Thread> nodeAgents;
    private TucsonAgentId nodeAid;
    private boolean observed;
    private ObservationService obsService;
    private Tuple persistencyTemplate;
    private int tcpPort = TucsonNodeService.DEFAULT_TCP_PORT;
    private final List<RespectTC> tcs;
    private final TPConfig tpConfig;
    private WelcomeAgent welcome;

    /**
     *
     */
    public TucsonNodeService() {
        this(null, TucsonNodeService.DEFAULT_TCP_PORT, null);
    }

    /**
     *
     * @param portno
     *            the default listening port of this TuCSoN node
     */
    public TucsonNodeService(final int portno) {
        this(null, portno, null);
    }

    /**
     *
     * @param conf
     *            the configuration file to load
     * @param portNumber
     *            the default listening port of this TuCSoN node
     * @param persistTempl
     *            the persistency template to be used to permanently store
     *            tuples
     */
    public TucsonNodeService(final String conf, final int portNumber,
            final Tuple persistTempl) {
        this.configFile = conf;
        this.tcpPort = portNumber;
        this.persistencyTemplate = persistTempl;
        try {
            this.nodeAid = new TucsonAgentId("'$TucsonNodeService-Agent'");
            this.idConfigTC = new TucsonTupleCentreId("'$ORG'", "localhost",
                    String.valueOf(this.tcpPort));
            this.idObsTC = new TucsonTupleCentreId("'$OBS'", "localhost",
                    String.valueOf(this.tcpPort));
            this.idEnvTC = new TucsonTupleCentreId("'$ENV'", "localhost",
                    String.valueOf(this.tcpPort));
            this.idGeolocationTC = new TucsonTupleCentreId(
                    "geolocationConfigTC", "localhost",
                    String.valueOf(this.tcpPort));
        } catch (final TucsonInvalidAgentIdException e) {
            // Cannot happen
            e.printStackTrace();
        } catch (final TucsonInvalidTupleCentreIdException e) {
            // Cannot happen
            e.printStackTrace();
        }
        this.observed = false;
        this.agents = new ArrayList<TucsonAgentId>();
        this.nodeAgents = new ArrayList<Thread>();
        this.inspectorAgents = new ArrayList<InspectorContextSkel>();
        this.tcs = new ArrayList<RespectTC>();
        this.tpConfig = new TPConfig();
        this.tpConfig.setTcpPort(this.tcpPort);
        synchronized (TucsonNodeService.NODES) {
            TucsonNodeService.NODES.put(this.tcpPort, this);
        }
        // Set rbac properties
        this.baseAgentClass = "basicAgentClass";
        this.loginRequired = false;
        this.listAllRoles = true;
    }

    /**
     *
     */
    public synchronized void activateObservability() {
        this.observed = true;
        final Iterator<TucsonTCUsers> it = this.cores.values().iterator();
        while (it.hasNext()) {
            final TucsonTCUsers tc = it.next();
            TupleCentreContainer.doManagementOperation(
                    TupleCentreOpType.ADD_OBS, tc.getTucsonTupleCentreId(),
                    this.obsService);
        }
    }

    /**
     *
     * @param aid
     *            the identifier of the agent to add to this TuCSoN node
     */
    public synchronized void addAgent(final TucsonAgentId aid) {
        boolean present = false;
        if (!this.agents.contains(aid)) {
            this.agents.add(aid);
        } else {
            present = true;
        }
        if (this.observed && !present) {
            this.obsService.accEntered(aid);
        }
    }

    /**
     * @param i
     *            the inspector agent to add
     */
    public void addInspectorAgent(final InspectorContextSkel i) {
        this.inspectorAgents.add(i);
    }

    /**
     *
     * @param t
     *            the identifier of the internal management agent to add to this
     *            TuCSoN node
     */
    public synchronized void addNodeAgent(final Thread t) {
        this.nodeAgents.add(t);
    }

    /**
     *
     * @param agentId
     *            the identifier of the tuple centre agent to add to this TuCSoN
     *            node
     * @param tid
     *            the identifier of the tuple centre whose agent has to be added
     */
    // why another slightly different method to add an agent? is this for
    // inter-tc agents?
    public void addTCAgent(final TucsonAgentId agentId,
            final TucsonTupleCentreId tid) {
        this.cores.get(tid.getName()).addUser(agentId);
    }

    /**
     *
     */
    public synchronized void deactivateObservability() {
        this.observed = false;
        final Iterator<TucsonTCUsers> it = this.cores.values().iterator();
        while (it.hasNext()) {
            final TucsonTCUsers tc = it.next();
            TupleCentreContainer.doManagementOperation(
                    TupleCentreOpType.RMV_OBS, tc.getTucsonTupleCentreId(),
                    this.obsService);
        }
    }

    /**
     *
     * @param tcn
     *            the String representing the tuple centre identifier to destroy
     * @return wether the operation has been succesfully carried out or not
     */
    public synchronized boolean destroyCore(final String tcn) {
        final StringBuilder tcName = new StringBuilder(tcn);
        if (tcn.indexOf('@') < 0) {
            tcName.append("@localhost");
        }
        if (tcn.indexOf(':') < 0) {
            tcName.append(this.tcpPort);
        }
        TucsonTupleCentreId tid;
        try {
            tid = new TucsonTupleCentreId(tcName.toString());
        } catch (final TucsonInvalidTupleCentreIdException e) {
            e.printStackTrace();
            return false;
        }
        final String realName = tcName.toString();
        final TucsonTupleCentreId core = this.cores.get(realName)
                .getTucsonTupleCentreId();
        if (core != null) {
            TucsonNodeService.log("Destroying tuple centre < " + realName
                    + " >...");
            TupleCentreContainer.destroyTC();
            if (this.observed) {
                this.obsService.tcDestroyed(tid);
            }
            try {
                final TupleArgument tcArg = TupleArgumentDefault.parse(realName);
                // Operation Make
                final RespectOperationDefault opRequested = RespectOperationDefault.make(
                        TupleCentreOpType.INP, new LogicTupleDefault(
                                "tuple_centre", tcArg), null);
                // InputEvent Creation
                final InputEvent ev = new InputEvent(this.nodeAid, opRequested,
                        this.idConfigTC, System.currentTimeMillis(), null);
                TupleCentreContainer.doBlockingOperation(ev);
                // TupleCentreContainer.doBlockingOperation(
                // TupleCentreOpType.INP, this.nodeAid,
                // this.idConfigTC, new LogicTuple("tuple_centre", tcArg));
                final RespectOperationDefault opRequested2 = RespectOperationDefault.make(
                        TupleCentreOpType.INP, new LogicTupleDefault(
                                "is_persistent", new ValueArgument(realName)), null);
                final InputEvent ev2 = new InputEvent(this.nodeAid,
                        opRequested2, this.idConfigTC,
                        System.currentTimeMillis(), null);
                TupleCentreContainer.doBlockingOperation(ev2);
                // TupleCentreContainer.doBlockingOperation(TucsonOperationDefault
                // .inpCode(), this.nodeAid, this.idConfigTC,
                // new LogicTuple("is_persistent", new Value(realName)));
            } catch (final TucsonOperationNotPossibleException e) {
                e.printStackTrace();
            } catch (final InvalidTupleArgumentException e) {
                e.printStackTrace();
            } catch (final TucsonInvalidLogicTupleException e) {
                e.printStackTrace();
            } catch (final InvalidLogicTupleException e) {
                e.printStackTrace();
            }
            this.cores.remove(realName);
            return true;
        }
        return false;
    }

    /**
     *
     * @param tc
     *            the identifier of the tuple centre whose persistency service
     *            should be disabled
     * @return wether persistency has been succesfully disabled
     */
    public synchronized boolean disablePersistency(final String tc) {
        final TucsonTCUsers tar = this.cores.get(tc);
        TupleCentreContainer.disablePersistence();
        try {
            // Operation Make
            final RespectOperationDefault opRequested = RespectOperationDefault.make(
                    TupleCentreOpType.IN, new LogicTupleDefault("is_persistent",
                            new ValueArgument(tar.getTucsonTupleCentreId().getName())),
                    null);
            // InputEvent Creation
            final InputEvent ev = new InputEvent(this.nodeAid, opRequested,
                    tar.getTucsonTupleCentreId(), System.currentTimeMillis(),
                    null);

            TupleCentreContainer.doBlockingOperation(ev);
            return true;

            // TupleCentreContainer.doBlockingOperation(TupleCentreOpType.IN,
            // this.nodeAid, tar.getTucsonTupleCentreId(), new LogicTuple(
            // "is_persistent", new Value(tar
            // .getTucsonTupleCentreId().getName())));
        } catch (final TucsonOperationNotPossibleException e) {
            e.printStackTrace();
            return false;
        } catch (final TucsonInvalidLogicTupleException e) {
            e.printStackTrace();
            return false;
        } catch (final InvalidLogicTupleException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @param template
     *            the tuple template to be used in filtering tuple centre
     *            identifiers whose persistency service should be disabled
     */
    public synchronized void disablePersistency(final Tuple template) {
        if (this.persistencyTemplate != null) {
            final Iterator<TucsonTCUsers> it = this.cores.values().iterator();
            while (it.hasNext()) {
                final TucsonTCUsers tc = it.next();
                try {
                    final TucsonTupleCentreId ttcid = tc
                            .getTucsonTupleCentreId();
                    final Tuple tid = LogicTupleDefault.parse(ttcid.getName());
                    TucsonNodeService.log(">>> Found tid: " + tid);
                    if (LogicMatchingEngine.match((LogicTuple) template,
                            (LogicTuple) tid)) {
                        TucsonNodeService
                                .log(">>> It matches: disabling persistency...");
                        TupleCentreContainer.disablePersistence();
                        // Operation Make
                        final RespectOperationDefault opRequested = RespectOperationDefault
                                .make(TupleCentreOpType.IN, new LogicTupleDefault(
                                        "is_persistent", new ValueArgument(tc
                                                .getTucsonTupleCentreId()
                                                .getName())), null);
                        // InputEvent Creation
                        final InputEvent ev = new InputEvent(this.nodeAid,
                                opRequested, tc.getTucsonTupleCentreId(),
                                System.currentTimeMillis(), null);
                        TupleCentreContainer.doBlockingOperation(ev);
                        // TupleCentreContainer.doBlockingOperation(
                        // TupleCentreOpType.IN, this.nodeAid, tc
                        // .getTucsonTupleCentreId(),
                        // new LogicTuple("is_persistent", new Value(tc
                        // .getTucsonTupleCentreId().getName())));
                        TucsonNodeService.log(">>> persistency disabled.");
                    }
                } catch (final TucsonOperationNotPossibleException e) {
                    e.printStackTrace();
                } catch (final TucsonInvalidLogicTupleException e) {
                    e.printStackTrace();
                } catch (final InvalidLogicTupleException e) {
                    e.printStackTrace();
                }
            }
            if (LogicMatchingEngine.match(
                    (LogicTuple) this.persistencyTemplate,
                    (LogicTuple) template)) {
                this.persistencyTemplate = null;
            }
        }
    }

    /**
     * UNUSED ATM
     * 
     * @param tc
     *            the identifier of the tuple centre whose persistency service
     *            should be enabled
     * @return wether persistency has been succesfully enabled
     */
    public synchronized boolean enablePersistency(final String tc) {
        final TucsonTCUsers tar = this.cores.get(tc);
        TupleCentreContainer.enablePersistence();
        try {
            // Operation Make
            final RespectOperationDefault opRequested = RespectOperationDefault.make(
                    TupleCentreOpType.OUT, new LogicTupleDefault("is_persistent",
                            new ValueArgument(tar.getTucsonTupleCentreId().getName())),
                    null);
            // InputEvent Creation
            final InputEvent ev = new InputEvent(this.nodeAid, opRequested,
                    tar.getTucsonTupleCentreId(), System.currentTimeMillis(),
                    null);
            TupleCentreContainer.doBlockingOperation(ev);
            // TupleCentreContainer.doBlockingOperation(TupleCentreOpType.OUT,
            // this.nodeAid, tar.getTucsonTupleCentreId(), new LogicTuple(
            // "is_persistent", new Value(tar
            // .getTucsonTupleCentreId().getName())));
            return true;
        } catch (final TucsonOperationNotPossibleException e) {
            e.printStackTrace();
            return false;
        } catch (final TucsonInvalidLogicTupleException e) {
            e.printStackTrace();
            return false;
        } catch (final InvalidLogicTupleException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @param template
     *            the tuple template to be used in filtering tuple centre
     *            identifiers whose persistency service should be enabled
     */
    public synchronized void enablePersistency(final Tuple template) {
        this.persistencyTemplate = template;
        TucsonNodeService.log(">>> Looking for " + this.persistencyTemplate);
        final Iterator<TucsonTCUsers> it = this.cores.values().iterator();
        while (it.hasNext()) {
            final TucsonTCUsers tc = it.next();
            try {
                final TucsonTupleCentreId ttcid = tc.getTucsonTupleCentreId();
                final Tuple tid = LogicTupleDefault.parse(ttcid.getName());
                TucsonNodeService.log(">>> Found tid: " + tid);
                if (LogicMatchingEngine.match((LogicTuple) template,
                        (LogicTuple) tid)) {
                    TucsonNodeService
                            .log(">>> It matches: enabling persistency...");
                    TupleCentreContainer.enablePersistency(ttcid,
                            TucsonNodeService.PERSISTENCY_PATH);
                    // Operation Make
                    final RespectOperationDefault opRequested = RespectOperationDefault.make(
                            TupleCentreOpType.OUT, new LogicTupleDefault(
                                    "is_persistent",
                                    new ValueArgument(tc.getTucsonTupleCentreId()
                                            .getName())), null);
                    // InputEvent Creation
                    final InputEvent ev = new InputEvent(this.nodeAid,
                            opRequested, tc.getTucsonTupleCentreId(),
                            System.currentTimeMillis(), null);
                    TupleCentreContainer.doBlockingOperation(ev);
                    // TupleCentreContainer.doBlockingOperation(TucsonOperationDefault
                    // .outCode(), this.nodeAid, tc
                    // .getTucsonTupleCentreId(), new LogicTuple(
                    // "is_persistent", new Value(tc
                    // .getTucsonTupleCentreId().getName())));
                    TucsonNodeService.log(">>> persistency enabled.");
                }
            } catch (final TucsonOperationNotPossibleException e) {
                e.printStackTrace();
            } catch (final TucsonInvalidLogicTupleException e) {
                e.printStackTrace();
            } catch (final InvalidLogicTupleException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @return a Map storing associations between String representations of
     *         tuple centres along with the list of their users
     */
    public Map<String, TucsonTCUsers> getCores() {
        return this.cores;
    }

    /**
     * @return node agents list
     */
    public ArrayList<InspectorContextSkel> getInspectorAgents() {
        return this.inspectorAgents;
    }

    /**
     *
     * @return the date when the TuCSoN node was installed
     */
    public Date getInstallationDate() {
        return this.installationDate;
    }

    /**
     *
     * @return the observer of the TuCSoN node, if any
     */
    public NodeServiceListener getListener() {
        return this.obsService;
    }

    /**
     *
     * @return the listening port this TuCSoN node is bound to
     */
    public int getTCPPort() {
        return this.tcpPort;
    }

    public final TPConfig getTPConfig() {
        return this.tpConfig;
    }

    /**
     *
     */
    public synchronized void install() {
        TucsonNodeService
                .log("--------------------------------------------------------------------------------");
        try {
            final StringTokenizer st = new StringTokenizer(
                    Utils.fileToString(TUCSON_NODE_SERVICE_RESOURCES_FOLDER + "tucsonCLIlogo3.txt"),
                    "\n");
            while (st.hasMoreTokens()) {
                TucsonNodeService.log(st.nextToken());
            }
        } catch (final IOException e) {
            // should not happen
            e.printStackTrace();
        }
        TucsonNodeService
                .log("--------------------------------------------------------------------------------");
        TucsonNodeService.log("Welcome to the TuCSoN infrastructure :)");
        TucsonNodeService.log("  Version " + TucsonNodeService.getVersion());
        TucsonNodeService
                .log("--------------------------------------------------------------------------------");
        TucsonNodeService.log(new Date().toString());
        TucsonNodeService.log("Beginning TuCSoN Node Service installation...");
        this.configManager = new Prolog();
        this.cores = new HashMap<String, TucsonTCUsers>();
        TucsonNodeService.log("Configuring TuCSoN Node Service...");
        try {
            this.setupConfiguration(this.configFile);
        } catch (final TucsonGenericException e) {
            e.printStackTrace();
        } catch (final InvalidConfigException e) {
            e.printStackTrace();
        }
        TucsonNodeService.log("Setting up Observation Service...");
        this.setupObsTupleCentre();
        TucsonNodeService.log("Setting up Management Service...");
        this.setupConfigTupleCentre();
        this.checkPersistentTupleCentres(TucsonNodeService.PERSISTENCY_PATH);
        TucsonNodeService
                .log("Setting up Environment Configuration Service...");
        this.setupEnvConfigTupleCentre();
        TucsonNodeService
                .log("Setting up Geolocation Configuration Service...");
        this.setupGeolocationConfigTupleCentre();
        this.installationDate = new Date();
        TucsonNodeService.log("Spawning management agents...");
        this.bootManagementAgents();
    }

    /**
     *
     * @param aid
     *            the identifier of the TuCSoN agent to be removed from users
     */
    public synchronized void removeAgent(final TucsonAgentId aid) {
        boolean present = true;
        if (this.agents.contains(aid)) {
            this.agents.remove(aid);
        } else {
            present = false;
        }
        if (this.observed && present) {
            this.obsService.accQuit(aid);
        }
        final Iterator<TucsonTCUsers> it = this.cores.values().iterator();
        while (it.hasNext()) {
            it.next().removeUser(aid);
        }
    }

    /**
     * @param i
     *            the InspectorContextSkel to eliminate
     */
    public void removeInspectorAgent(final InspectorContextSkel i) {
        this.inspectorAgents.remove(i);
    }

    /**
     *
     * @param t
     *            the Thread object executing the internal management agent to
     *            be removed
     */
    public synchronized void removeNodeAgent(final Thread t) {
        this.nodeAgents.remove(t);
    }

    /**
     * @param tcn
     *            the String representation of the tuple centre whose usage
     *            associations should be retrieved
     * @return the Object representing associations between agents and tuple
     *         centre they're using
     */
    public synchronized TucsonTCUsers resolveCore(final String tcn) {
        final StringBuilder tcName = new StringBuilder(tcn);
        if (tcn.indexOf('@') < 0) {
            tcName.append("@localhost");
        }
        if (tcn.indexOf(':') < 0) {
            tcName.append(":").append(this.tcpPort);
        }
        TucsonTupleCentreId tid;
        try {
            tid = new TucsonTupleCentreId(tcName.toString());
        } catch (final TucsonInvalidTupleCentreIdException e) {
            e.printStackTrace();
            return null;
        }
        final String realName = tid.getName();
        TucsonTCUsers core = this.cores.get(realName);
        if (core == null) {
            TucsonNodeService.log("Booting new tuple centre < " + realName
                    + " >...");
            try {
                core = this.bootTupleCentre(realName);
            } catch (final TucsonInvalidTupleCentreIdException e) {
                e.printStackTrace();
                return null;
            }
        }
        return core;
    }

    public void setAdminPassword(final String password) {
        this.adminPassword = password;
    }

    public void setAdminUsername(final String username) {
        this.adminUsername = username;
    }

    public void setBasicAgentClass(final String agentClass) {
        this.baseAgentClass = agentClass;
    }

    public void setInspectorsAuthorised(final boolean inspectorsAuth) {
        this.inspectorsAuthorised = inspectorsAuth;
    }

    public void setListAllRolesAllowed(final boolean listRoles) {
        this.listAllRoles = listRoles;
    }

    public void setLoginRequired(final boolean loginReq) {
        this.loginRequired = loginReq;
    }

    /**
     *
     */
    public void shutdown() {
        TucsonNodeService
                .log("Node is shutting down management agents and proxies...");
        for (final Thread t : this.nodeAgents) {
            if (t.isAlive()) {
                TucsonNodeService.log("  ...shutting down <" + t.getName()
                        + ">");
                t.interrupt();
                // boolean b = t.interrupted();
            } else {
                TucsonNodeService.log("  ...<" + t.getName()
                        + "> is already dead");
            }
        }
        this.welcome.shutdown();
        try {
            this.ctxman.shutdown();
        } catch (final InterruptedException e) {
            TucsonNodeService
                    .log("ACCProvider may still have tasks executing...");
        }
        this.envAgent.stopIteraction();
        TucsonNodeService.log("Node is shutting down ReSpecT VMs...");
        for (final RespectTC tc : this.tcs) {
            final Thread t = tc.getVMThread();
            if (t.isAlive()) {
                TucsonNodeService
                        .log("  ...shutting down <" + tc.getId() + ">");
                t.interrupt();
            } else {
                TucsonNodeService.log("  ...<" + tc.getId()
                        + "> is already dead");
            }
        }
        TucsonNodeService.log("TuCSoN Node shutdown completed, see you :)");
    }

    /**
     *
     */
    private void bootManagementAgents() {
        TucsonNodeService.log("Spawning Node Management Agent...");
        this.nodeAgents.add(new NodeManagementAgent(this.idConfigTC, this));
        TucsonNodeService
                .log("--------------------------------------------------------------------------------");
        TucsonNodeService.log("Spawning Geolocation Config Agent...");
        // GeolocationConfigAgent geolocationConfigAgent = new
        // GeolocationConfigAgent( "localhost", tcpPort );
        this.nodeAgents.add(new GeolocationConfigAgent(this.idGeolocationTC,
                this));
        TucsonNodeService
                .log("--------------------------------------------------------------------------------");
        TucsonNodeService.log("Spawning ACC Provider Agent...");
        this.ctxman = new ACCProvider(this, this.idConfigTC);
        TucsonNodeService.log("Spawning Welcome Agent...");
        this.welcome = new WelcomeAgent(this, this.ctxman);
        TucsonNodeService.log("Spawning Environmental Agent...");
        try {
            this.envAgent = new EnvConfigAgent("localhost", this.tcpPort);
        } catch (final TucsonInvalidAgentIdException e) {
            // Cannot happen
            e.printStackTrace();
        }
    }

    /**
     *
     * @param n
     * @return
     * @throws TucsonInvalidTupleCentreIdException
     * @throws InvalidTupleCentreIdException
     */
    private TucsonTCUsers bootTupleCentre(final String n)
            throws TucsonInvalidTupleCentreIdException {
        final StringBuilder name = new StringBuilder(n);
        if (n.indexOf('@') < 0) {
            name.append("@localhost");
        }
        if (n.indexOf(':') < 0) {
            name.append(':').append("'").append(this.tcpPort).append("'");
        }
        final TucsonTupleCentreId id = new TucsonTupleCentreId(name.toString());
        try {
            final RespectTC rtc = TupleCentreContainer.createTC(id,
                    TucsonNodeService.MAX_EVENT_QUEUE_SIZE, this.tcpPort);
            this.tcs.add(rtc);
        } catch (final InvalidTupleCentreIdException e) {
            TucsonNodeService.log("TupleCentreContainer.createTC(...) error");
            e.printStackTrace();
        }
        if (this.observed) {
            TupleCentreContainer.doManagementOperation(
                    TupleCentreOpType.ADD_OBS, id, this.obsService);
            this.obsService.tcCreated(id);
        }
        final TucsonTCUsers tcUsers = new TucsonTCUsers(id);
        this.cores.put(id.getName(), tcUsers);
        return tcUsers;
    }

    /**
     *
     * @param dirName
     */
    private void checkPersistentTupleCentres(final String dirName) {
        final File dir = new File(dirName);
        if (dir.exists() && dir.isDirectory()) {
            final String[] files = dir.list();
            for (final String file : files) {
                // if (file.startsWith("tc_") && file.endsWith(".dat")) {
                if (file.startsWith("tc_") && file.endsWith(".xml")) {
                    final int start = file.indexOf("_");
                    int end = file.lastIndexOf("_");
                    String toParse = file.substring(start + 1, end);
                    end = toParse.lastIndexOf("_");
                    toParse = toParse.substring(0, end);
                    final String[] split = toParse.split("_at_");
                    if (Integer.parseInt(split[2]) == this.tcpPort) {
                        final String tcName = split[0];
                        final String fullTcName = split[0] + "@" + split[1]
                                + ":" + split[2];
                        TucsonNodeService.log(">>> Persistent tc found: "
                                + fullTcName);
                        try {
                            this.bootTupleCentre(tcName);
                        } catch (final TucsonInvalidTupleCentreIdException e) {
                            e.printStackTrace();
                        }
                        TucsonNodeService.log(">>> Recovering persistent tc < "
                                + fullTcName + " >...");
                        final TucsonTupleCentreId ttcid = this.cores
                                .get(tcName).getTucsonTupleCentreId();
                        TupleCentreContainer.recoveryPersistent(ttcid,
                                TucsonNodeService.PERSISTENCY_PATH, file);
                        // TupleCentreContainer.enablePersistency(
                        // this.cores.get(tcName).getTucsonTupleCentreId(),
                        // TucsonNodeService.PERSISTENCY_PATH);
                        try {
                            // Operation Make
                            final RespectOperationDefault opRequested = RespectOperationDefault
                                    .make(TupleCentreOpType.OUT,
                                            new LogicTupleDefault("is_persistent",
                                                    new ValueArgument(tcName)), null);
                            // InputEvent Creation
                            final InputEvent ev = new InputEvent(this.nodeAid,
                                    opRequested, this.cores.get(tcName)
                                            .getTucsonTupleCentreId(),
                                    System.currentTimeMillis(), null);
                            TupleCentreContainer.doBlockingOperation(ev);
                            TucsonNodeService.log(">>> ...persistent tc < "
                                    + fullTcName + " > recovered.");
                        } catch (final TucsonOperationNotPossibleException e) {
                            e.printStackTrace();
                        } catch (final TucsonInvalidLogicTupleException e) {
                            e.printStackTrace();
                        } catch (InvalidLogicTupleException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
            }
        } else {
            dir.mkdir();
        }
    }

    /**
     *
     */
    private void setupConfigTupleCentre() {
        try {
            this.bootTupleCentre(this.idConfigTC.getName());
            final InputStream is = Thread
                    .currentThread()
                    .getContextClassLoader()
                    .getResourceAsStream(
                            TucsonNodeService.DEFAULT_BOOT_SPEC_FILE);
            final String spec = alice.util.Tools
                    .loadText(new BufferedInputStream(is));
            final LogicTuple specTuple = new LogicTupleDefault("spec", new ValueArgument(spec));
            final RespectOperationDefault opRequested = RespectOperationDefault.make(
                    TupleCentreOpType.SET_S, specTuple, null);

            // InputEvent Creation
            final InputEvent ev = new InputEvent(this.nodeAid, opRequested,
                    this.idConfigTC, System.currentTimeMillis(), null);
            TupleCentreContainer.doBlockingSpecOperation(ev, specTuple);
            final RespectOperationDefault opRequested2 = RespectOperationDefault.make(
                    TupleCentreOpType.OUT, new LogicTupleDefault("boot"), null);
            final InputEvent ev2 = new InputEvent(this.nodeAid, opRequested2,
                    this.idConfigTC, System.currentTimeMillis(), null);
            TupleCentreContainer.doNonBlockingOperation(ev2);

            // Set default agent class
            final RespectOperationDefault opRequested3 = RespectOperationDefault.make(
                    TupleCentreOpType.OUT,
                    new LogicTupleDefault("basic_agent_class", new ValueArgument(
                            this.baseAgentClass)), null);
            final InputEvent ev3 = new InputEvent(this.nodeAid, opRequested3,
                    this.idConfigTC, System.currentTimeMillis(), null);
            TupleCentreContainer.doBlockingOperation(ev3);

            // Set login required
            final RespectOperationDefault opRequested4 = RespectOperationDefault.make(
                    TupleCentreOpType.OUT, new LogicTupleDefault(
                            "is_login_required", new ValueArgument(
                                    this.loginRequired ? "yes" : "no")), null);
            final InputEvent ev4 = new InputEvent(this.nodeAid, opRequested4,
                    this.idConfigTC, System.currentTimeMillis(), null);
            TupleCentreContainer.doBlockingOperation(ev4);

            // Allow or not list of all roles
            final RespectOperationDefault opRequested5 = RespectOperationDefault.make(
                    TupleCentreOpType.OUT, new LogicTupleDefault("list_all_roles",
                            new ValueArgument(this.listAllRoles ? "yes" : "no")), null);
            final InputEvent ev5 = new InputEvent(this.nodeAid, opRequested5,
                    this.idConfigTC, System.currentTimeMillis(), null);
            TupleCentreContainer.doBlockingOperation(ev5);

            final RespectOperationDefault opRequested6 = RespectOperationDefault.make(
                    TupleCentreOpType.OUT, new LogicTupleDefault(
                            "allow_inspection", new ValueArgument(
                                    this.inspectorsAuthorised ? "yes" : "no")),
                    null);
            final InputEvent ev6 = new InputEvent(this.nodeAid, opRequested6,
                    this.idConfigTC, System.currentTimeMillis(), null);
            TupleCentreContainer.doBlockingOperation(ev6);

            // TupleCentreContainer.doBlockingOperation(TupleCentreOpType.OUT,
            // this.nodeAid, this.idConfigTC, new LogicTuple("role", new
            // Value("admin_role"), new Value("admin role"), new Value("0")));
            /*
             * if(!authForAdmin){
             * TupleCentreContainer.doBlockingOperation(TucsonOperationDefault
             * .outCode(), this.nodeAid, this.idConfigTC, new
             * LogicTuple("role_credentials", new Value("admin_role"), new
             * Value("_"))); } else if(adminUsername!=null &&
             * !adminUsername.equalsIgnoreCase("") && adminPassword!=null &&
             * !adminPassword.equalsIgnoreCase("")) {
             * TupleCentreContainer.doBlockingOperation
             * (TupleCentreOpType.OUT, nodeAid, idConfigTC, new
             * LogicTuple("role_credentials", new Value("admin_role"), new
             * Value(adminUsername+":"+TucsonACCTool.encrypt(adminPassword))));
             * } else {
             * TupleCentreContainer.doBlockingOperation(TucsonOperationDefault.
             * outCode(), this.nodeAid, this.idConfigTC, new
             * LogicTuple("role_credentials", new Value("admin_role"), new
             * Value("_"))); }
             */
            if (this.adminUsername != null
                    && !this.adminUsername.equalsIgnoreCase("")
                    && this.adminPassword != null
                    && !this.adminPassword.equalsIgnoreCase("")) {

                final RespectOperationDefault opRequested7 = RespectOperationDefault.make(
                        TupleCentreOpType.OUT,
                        new LogicTupleDefault("admin_credentials", new ValueArgument(
                                this.adminUsername
                                        + ":"
                                        + TucsonACCTool
                                                .encrypt(this.adminPassword))),
                        null);
                final InputEvent ev7 = new InputEvent(this.nodeAid,
                        opRequested7, this.idConfigTC,
                        System.currentTimeMillis(), null);
                TupleCentreContainer.doBlockingOperation(ev7);
            }

            this.addAgent(this.nodeAid);
        } catch (final TucsonInvalidTupleCentreIdException e) {
            e.printStackTrace();
        } catch (final IOException e) {
            e.printStackTrace();
        } catch (final TucsonOperationNotPossibleException e) {
            e.printStackTrace();
        } catch (final TucsonInvalidLogicTupleException e) {
            e.printStackTrace();
        } catch (final TucsonInvalidSpecificationException e) {
            e.printStackTrace();
        } catch (InvalidLogicTupleException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     *
     * @param conf
     * @throws TucsonGenericException
     * @throws InvalidConfigException
     */
    // exception handling is a mess, review it...
    private void setupConfiguration(final String conf)
            throws TucsonGenericException, InvalidConfigException {
        final alice.tuprolog.lib.OOLibrary jlib = (alice.tuprolog.lib.OOLibrary) this.configManager
                .getLibrary("alice.tuprolog.lib.OOLibrary");
        try {
            jlib.register(new alice.tuprolog.Struct("config"), this);
        } catch (final InvalidObjectIdException e) {
            throw new TucsonGenericException(
                    "Internal Failure: loading JavaLibrary in Prolog Configuration Engine failed.");
        }
        if (conf != null) {
            TucsonNodeService.log("Configuration file not supported atm!");
            try {
                final InputStream is = Thread
                        .currentThread()
                        .getContextClassLoader()
                        .getResourceAsStream(
                                TucsonNodeService.BOOT_SETUP_THEORY);
                final Theory setupTh = new Theory(new BufferedInputStream(is));
                this.configManager.setTheory(setupTh);
            } catch (final alice.tuprolog.InvalidTheoryException ex) {
                throw new TucsonGenericException(
                        "Internal Failure: invalid Prolog Setup Engine theory.");
            } catch (final IOException ex) {
                throw new TucsonGenericException(
                        "Internal Failure: loading Prolog Setup Engine theory failed.");
            }
            Theory cfgTh;
            alice.tuprolog.SolveInfo info;
            try {
                cfgTh = new Theory(new FileInputStream(conf));
                this.configManager.addTheory(cfgTh);
                info = this.configManager.solve("setup.");
            } catch (final FileNotFoundException e) {
                throw new TucsonGenericException(
                        "Internal Failure: Prolog Configuration Engine theory not found.");
            } catch (final IOException e) {
                throw new TucsonGenericException(
                        "Internal Failure: loading Prolog Configuration Engine theory failed.");
            } catch (final InvalidTheoryException e) {
                throw new InvalidConfigException();
            } catch (final MalformedGoalException e) {
                throw new TucsonGenericException(
                        "Internal Failure: solving Prolog Configuration Engine theory failed.");
            }
            if (!info.isSuccess()) {
                throw new InvalidConfigException();
            }
        }
    }

    /*
     * Setting up the environment configuration tuple centre
     */
    private void setupEnvConfigTupleCentre() {
        try {
            this.bootTupleCentre(this.idEnvTC.getName());
            final InputStream is = Thread
                    .currentThread()
                    .getContextClassLoader()
                    .getResourceAsStream(
                            TucsonNodeService.DEFAULT_ENVCONFIG_SPEC_FILE);
            final String spec = alice.util.Tools
                    .loadText(new BufferedInputStream(is));
            final LogicTuple specTuple = new LogicTupleDefault("spec", new ValueArgument(spec));
            // Operation Make
            final RespectOperationDefault opRequested = RespectOperationDefault.make(
                    TupleCentreOpType.SET_S, specTuple, null);
            // InputEvent Creation
            final InputEvent ev = new InputEvent(this.nodeAid, opRequested,
                    this.idEnvTC, System.currentTimeMillis(), null);
            TupleCentreContainer.doBlockingSpecOperation(ev, specTuple);
        } catch (final TucsonOperationNotPossibleException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (final TucsonInvalidSpecificationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (final TucsonInvalidTupleCentreIdException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidLogicTupleException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /*
     * Setting up the environment configuration tuple centre
     */
    private void setupGeolocationConfigTupleCentre() {
        try {
            this.bootTupleCentre(this.idGeolocationTC.getName());
            final InputStream is = Thread
                    .currentThread()
                    .getContextClassLoader()
                    .getResourceAsStream(
                            TucsonNodeService.DEFAULT_GEOLOCATION_SPEC_FILE);
            final String spec = alice.util.Tools
                    .loadText(new BufferedInputStream(is));
            final LogicTuple specTuple = new LogicTupleDefault("spec", new ValueArgument(spec));
            // Operation Make
            final RespectOperationDefault opRequested = RespectOperationDefault.make(
                    TupleCentreOpType.SET_S, specTuple, null);
            // InputEvent Creation
            final InputEvent ev = new InputEvent(this.nodeAid, opRequested,
                    this.idGeolocationTC, System.currentTimeMillis(), null);
            TupleCentreContainer.doBlockingSpecOperation(ev, specTuple);
            final RespectOperationDefault opRequested2 = RespectOperationDefault.make(
                    TupleCentreOpType.OUT, new LogicTupleDefault("boot"), null);
            final InputEvent ev2 = new InputEvent(this.nodeAid, opRequested2,
                    this.idGeolocationTC, System.currentTimeMillis(), null);
            TupleCentreContainer.doNonBlockingOperation(ev2);
        } catch (final TucsonInvalidTupleCentreIdException e) {
            e.printStackTrace();
        } catch (final TucsonInvalidLogicTupleException e) {
            e.printStackTrace();
        } catch (final TucsonOperationNotPossibleException e) {
            e.printStackTrace();
        } catch (final TucsonInvalidSpecificationException e) {
            e.printStackTrace();
        } catch (final IOException e) {
            e.printStackTrace();
        } catch (final InvalidLogicTupleException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     */
    private void setupObsTupleCentre() {
        try {
            this.bootTupleCentre(this.idObsTC.getName());
            final InputStream is = Thread
                    .currentThread()
                    .getContextClassLoader()
                    .getResourceAsStream(
                            TucsonNodeService.DEFAULT_OBS_SPEC_FILE);
            final String spec = alice.util.Tools
                    .loadText(new BufferedInputStream(is));
            final LogicTuple specTuple = new LogicTupleDefault("spec", new ValueArgument(spec));
            // Operation Make
            final RespectOperationDefault opRequested = RespectOperationDefault.make(
                    TupleCentreOpType.SET_S, specTuple, null);
            // InputEvent Creation
            final InputEvent ev = new InputEvent(this.nodeAid, opRequested,
                    this.idObsTC, System.currentTimeMillis(), null);
            TupleCentreContainer.doBlockingSpecOperation(ev, specTuple);
            final RespectOperationDefault opRequested2 = RespectOperationDefault.make(
                    TupleCentreOpType.OUT, new LogicTupleDefault("boot"), null);
            final InputEvent ev2 = new InputEvent(this.nodeAid, opRequested2,
                    this.idObsTC, System.currentTimeMillis(), null);
            TupleCentreContainer.doNonBlockingOperation(ev2);
            this.obsService = new ObservationService(this.idObsTC);
        } catch (final TucsonInvalidTupleCentreIdException e) {
            e.printStackTrace();
        } catch (final IOException e) {
            e.printStackTrace();
        } catch (final TucsonOperationNotPossibleException e) {
            e.printStackTrace();
        } catch (final TucsonInvalidLogicTupleException e) {
            e.printStackTrace();
        } catch (final TucsonInvalidSpecificationException e) {
            e.printStackTrace();
        } catch (InvalidLogicTupleException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
