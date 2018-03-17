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
package alice.tuplecentre.tucson.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.invoke.MethodHandles;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.*;

import alice.tuple.Tuple;
import alice.tuple.logic.LogicMatchingEngine;
import alice.tuple.logic.LogicTuple;
import alice.tuple.logic.LogicTuples;
import alice.tuple.logic.TupleArgument;
import alice.tuple.logic.TupleArguments;
import alice.tuple.logic.exceptions.InvalidLogicTupleException;
import alice.tuple.logic.exceptions.InvalidTupleArgumentException;
import alice.tuplecentre.core.InputEvent;
import alice.tuplecentre.core.TupleCentreOpType;
import alice.tuplecentre.respect.api.exceptions.InvalidTupleCentreIdException;
import alice.tuplecentre.respect.api.geolocation.GeolocationConfigAgent;
import alice.tuplecentre.respect.core.EnvConfigAgent;
import alice.tuplecentre.respect.core.RespectOperationDefault;
import alice.tuplecentre.respect.core.RespectTC;
import alice.tuplecentre.tucson.api.TucsonAgentId;
import alice.tuplecentre.tucson.api.TucsonAgentIdDefault;
import alice.tuplecentre.tucson.api.TucsonTupleCentreId;
import alice.tuplecentre.tucson.api.TucsonTupleCentreIdDefault;
import alice.tuplecentre.tucson.api.exceptions.InvalidConfigException;
import alice.tuplecentre.tucson.api.exceptions.TucsonGenericException;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidLogicTupleException;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidSpecificationException;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidTupleCentreIdException;
import alice.tuplecentre.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tuplecentre.tucson.api.exceptions.UnreachableNodeException;
import alice.tuplecentre.tucson.introspection.InspectorContextSkel;
import alice.tuplecentre.tucson.network.AbstractTucsonProtocol;
import alice.tuplecentre.tucson.network.exceptions.DialogInitializationException;
import alice.tuplecentre.tucson.service.tools.TucsonACCTool;
import alice.tuplecentre.tucson.utilities.Utils;
import alice.tuprolog.InvalidTheoryException;
import alice.tuprolog.MalformedGoalException;
import alice.tuprolog.Prolog;
import alice.tuprolog.Theory;
import alice.tuprolog.lib.InvalidObjectIdException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Alessandro Ricci
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 * @author (contributor) Saverio Cicora
 */
public class TucsonNodeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().getClass());

    private static final String TUCSON_NODE_SERVICE_RESOURCES_FOLDER = "tucson/service/config/";

    private static final String BOOT_SETUP_THEORY = TUCSON_NODE_SERVICE_RESOURCES_FOLDER + "boot.pl";
    private static final String DEFAULT_BOOT_SPEC_FILE = TUCSON_NODE_SERVICE_RESOURCES_FOLDER + "boot_spec.rsp";
    private static final String DEFAULT_ENVCONFIG_SPEC_FILE = TUCSON_NODE_SERVICE_RESOURCES_FOLDER + "env_spec.rsp";
    private static final String DEFAULT_GEOLOCATION_SPEC_FILE = TUCSON_NODE_SERVICE_RESOURCES_FOLDER + "geolocation_spec.rsp";
    private static final String DEFAULT_OBS_SPEC_FILE = TUCSON_NODE_SERVICE_RESOURCES_FOLDER + "obs_spec.rsp";


    private static final int MAX_EVENT_QUEUE_SIZE = 1000;
    private static final int MAX_UNBOUND_PORT = 64000;
    private static final Map<Integer, TucsonNodeService> NODES = new HashMap<>();

    private static final String PERSISTENCY_PATH = "persistent/";

    public static synchronized TucsonNodeService getNode(final int port) {
        return NODES.get(port);
    }

    public static boolean isInstalled(final int timeout)
            throws DialogInitializationException {
        try {
            return isInstalled("localhost", TucsonInfo.getDefaultPortNumber(), timeout);
        } catch (final UnreachableNodeException e) {
            LOGGER.error(e.getMessage(), e);
            return false;
        }
    }

    public static boolean isInstalled(final int port, final int timeout)
            throws DialogInitializationException {
        try {
            return isInstalled("localhost", port, timeout);
        } catch (final UnreachableNodeException e) {
            LOGGER.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * @param netid   the IP address where to test if a TuCSoN node is up and
     *                running
     * @param port    the listening port where to test if a TuCSoN node is up and
     *                running
     * @param timeout the maximum waiting time the caller agent can afford to wait
     *                for a response
     * @return whether a TuCSoN node is up and active on the given port
     * @throws UnreachableNodeException      if the given host is unknown
     * @throws DialogInitializationException if some network problems arise
     */
    public static boolean isInstalled(final String netid, final int port,
                                      final int timeout) throws UnreachableNodeException,
            DialogInitializationException {
        String reply;
        try (final Socket test = new Socket(netid, port)) {
            test.setReuseAddress(true);
            test.setSoTimeout(timeout);
            final ObjectInputStream ois = new ObjectInputStream(
                    new BufferedInputStream(test.getInputStream()));
            final ObjectOutputStream oos = new ObjectOutputStream(
                    new BufferedOutputStream(test.getOutputStream()));
            oos.writeInt(AbstractTucsonProtocol.NODE_ACTIVE_QUERY);
            oos.flush();
            reply = ois.readUTF();
        } catch (final ConnectException | SocketTimeoutException e) {
            reply = "";
        } catch (final UnknownHostException e) {
            throw new UnreachableNodeException("Host unknown", e);
        } catch (final IOException e) {
            throw new DialogInitializationException(e);
        }
        return reply.startsWith("TuCSoN");
    }

    /**
     * @param args the arguments to start the TuCSoN node with
     */
    public static void main(final String[] args) {
        if (alice.util.Tools.isOpt(args, "-help")
                || alice.util.Tools.isOpt(args, "-?")) {
            LOGGER.info("Arguments: -portno {portNumber} {-? | -help}");
        } else {
            final String portInfo = alice.util.Tools.getOpt(args, "-portno");
            final String configInfo = alice.util.Tools.getOpt(args, "-config");
            final String persistencyInfo = alice.util.Tools.getOpt(args,
                    "-persistency");
            int portNumber = TucsonInfo.getDefaultPortNumber();
            if (portInfo != null) {
                try {
                    portNumber = Integer.parseInt(portInfo);
                    if (portNumber < 0
                            || portNumber > MAX_UNBOUND_PORT) {
                        LOGGER.error("Invalid port number");
                        System.exit(-1);
                    }
                } catch (final NumberFormatException e) {
                    LOGGER.error("Invalid port number");
                    System.exit(-1);
                }
            }
            // at least check if the config file exists...you don't say?
            Tuple template = null;
            if (persistencyInfo != null) {
                try {
                    template = LogicTuples.parse(persistencyInfo);
                } catch (final InvalidLogicTupleException e) {
                    LOGGER.error("Invalid persistency template");
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
    private int tcpPort = TucsonInfo.getDefaultPortNumber();
    private final List<RespectTC> tcs;
    //private final TPConfig tpConfig;
    private WelcomeAgent welcome;

    /**
     *
     */
    public TucsonNodeService() {
        this(null, TucsonInfo.getDefaultPortNumber(), null);
    }

    /**
     * @param portno the default listening port of this TuCSoN node
     */
    public TucsonNodeService(final int portno) {
        this(null, portno, null);
    }

    /**
     * @param conf         the configuration file to load
     * @param portNumber   the default listening port of this TuCSoN node
     * @param persistTempl the persistency template to be used to permanently store
     *                     tuples
     */
    public TucsonNodeService(final String conf, final int portNumber,
                             final Tuple persistTempl) {
        this.configFile = conf;
        this.tcpPort = portNumber;
        this.persistencyTemplate = persistTempl;
        try {
            this.nodeAid = new TucsonAgentIdDefault("'$TucsonNodeService-Agent'");
            this.idConfigTC = new TucsonTupleCentreIdDefault("'$ORG'", "localhost",
                    String.valueOf(this.tcpPort));
            this.idObsTC = new TucsonTupleCentreIdDefault("'$OBS'", "localhost",
                    String.valueOf(this.tcpPort));
            this.idEnvTC = new TucsonTupleCentreIdDefault("'$ENV'", "localhost",
                    String.valueOf(this.tcpPort));
            this.idGeolocationTC = new TucsonTupleCentreIdDefault(
                    "geolocationConfigTC", "localhost",
                    String.valueOf(this.tcpPort));
        } catch (final TucsonInvalidAgentIdException | TucsonInvalidTupleCentreIdException e) {
            // Cannot happen
            LOGGER.error(e.getMessage(), e);
        }
        this.observed = false;
        this.agents = new ArrayList<>();
        this.nodeAgents = new ArrayList<>();
        this.inspectorAgents = new ArrayList<>();
        this.tcs = new ArrayList<>();
        /*this.tpConfig = new TPConfig();
        this.tpConfig.setTcpPort(this.tcpPort);*/
        synchronized (NODES) {
            NODES.put(this.tcpPort, this);
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
        for (TucsonTCUsers tc : this.cores.values()) {
            TupleCentreContainer.doManagementOperation(
                    TupleCentreOpType.ADD_OBS, tc.getTucsonTupleCentreId(),
                    this.obsService);
        }
    }

    /**
     * @param aid the identifier of the agent to add to this TuCSoN node
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
     * @param i the inspector agent to add
     */
    public void addInspectorAgent(final InspectorContextSkel i) {
        this.inspectorAgents.add(i);
    }

    /**
     * @param t the identifier of the internal management agent to add to this
     *          TuCSoN node
     */
    public synchronized void addNodeAgent(final Thread t) {
        this.nodeAgents.add(t);
    }

    /**
     * @param agentId the identifier of the tuple centre agent to add to this TuCSoN
     *                node
     * @param tid     the identifier of the tuple centre whose agent has to be added
     */
    // why another slightly different method to add an agent? is this for
    // inter-tc agents?
    public void addTCAgent(final TucsonAgentId agentId,
                           final TucsonTupleCentreId tid) {
        this.cores.get(tid.getLocalName()).addUser(agentId);
    }

    /**
     *
     */
    public synchronized void deactivateObservability() {
        this.observed = false;
        for (TucsonTCUsers tc : this.cores.values()) {
            TupleCentreContainer.doManagementOperation(
                    TupleCentreOpType.RMV_OBS, tc.getTucsonTupleCentreId(),
                    this.obsService);
        }
    }

    /**
     * @param tcn the String representing the tuple centre identifier to destroy
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
            tid = new TucsonTupleCentreIdDefault(tcName.toString());
        } catch (final TucsonInvalidTupleCentreIdException e) {
            LOGGER.error(e.getMessage(), e);
            return false;
        }
        final String realName = tcName.toString();
        final TucsonTupleCentreId core = this.cores.get(realName)
                .getTucsonTupleCentreId();
        if (core != null) {
            LOGGER.info("Destroying tuple centre < " + realName
                    + " >...");
            if (this.observed) {
                this.obsService.tcDestroyed(tid);
            }
            try {
                final TupleArgument tcArg = TupleArguments.parse(realName);
                // Operation Make
                final RespectOperationDefault opRequested = RespectOperationDefault.make(
                        TupleCentreOpType.INP, LogicTuples.newInstance(
                                "tuple_centre", tcArg), null);
                // InputEvent Creation
                final InputEvent ev = new InputEvent(this.nodeAid, opRequested,
                        this.idConfigTC, System.currentTimeMillis(), null);
                TupleCentreContainer.doBlockingOperation(ev);
                // TupleCentreContainer.doBlockingOperation(
                // TupleCentreOpType.INP, this.nodeAid,
                // this.idConfigTC, LogicTuples.newInstance("tuple_centre", tcArg));
                final RespectOperationDefault opRequested2 = RespectOperationDefault.make(
                        TupleCentreOpType.INP, LogicTuples.newInstance(
                                "is_persistent", TupleArguments.newValueArgument(realName)), null);
                final InputEvent ev2 = new InputEvent(this.nodeAid,
                        opRequested2, this.idConfigTC,
                        System.currentTimeMillis(), null);
                TupleCentreContainer.doBlockingOperation(ev2);
                // TupleCentreContainer.doBlockingOperation(TucsonOperationDefault
                // .inpCode(), this.nodeAid, this.idConfigTC,
                // LogicTuples.newInstance("is_persistent", TupleArguments.newValueArgument(realName)));
            } catch (final TucsonOperationNotPossibleException | InvalidLogicTupleException | TucsonInvalidLogicTupleException | InvalidTupleArgumentException e) {
                LOGGER.error(e.getMessage(), e);
            }
            this.cores.remove(realName);
            return true;
        }
        return false;
    }

    /**
     * @param tc the identifier of the tuple centre whose persistency service
     *           should be disabled
     * @return wether persistency has been succesfully disabled
     */
    public synchronized boolean disablePersistency(final String tc) {
        final TucsonTCUsers tar = this.cores.get(tc);
        try {
            // Operation Make
            final RespectOperationDefault opRequested = RespectOperationDefault.make(
                    TupleCentreOpType.IN, LogicTuples.newInstance("is_persistent",
                            TupleArguments.newValueArgument(tar.getTucsonTupleCentreId().getLocalName())),
                    null);
            // InputEvent Creation
            final InputEvent ev = new InputEvent(this.nodeAid, opRequested,
                    tar.getTucsonTupleCentreId(), System.currentTimeMillis(),
                    null);

            TupleCentreContainer.doBlockingOperation(ev);
            return true;

            // TupleCentreContainer.doBlockingOperation(TupleCentreOpType.IN,
            // this.nodeAid, tar.getTucsonTupleCentreId(), LogicTuples.newInstance(
            // "is_persistent", TupleArguments.newValueArgument(tar
            // .getTucsonTupleCentreId().getName())));
        } catch (final TucsonOperationNotPossibleException | InvalidLogicTupleException | TucsonInvalidLogicTupleException e) {
            LOGGER.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * @param template the tuple template to be used in filtering tuple centre
     *                 identifiers whose persistency service should be disabled
     */
    public synchronized void disablePersistency(final Tuple template) {
        if (this.persistencyTemplate != null) {
            for (TucsonTCUsers tc : this.cores.values()) {
                try {
                    final TucsonTupleCentreId ttcid = tc
                            .getTucsonTupleCentreId();
                    final Tuple tid = LogicTuples.parse(ttcid.getLocalName());
                    LOGGER.info(">>> Found tid: " + tid);
                    if (LogicMatchingEngine.match((LogicTuple) template,
                            (LogicTuple) tid)) {
                        LOGGER.info(">>> It matches: disabling persistency...");
                        // Operation Make
                        final RespectOperationDefault opRequested = RespectOperationDefault
                                .make(TupleCentreOpType.IN, LogicTuples.newInstance(
                                        "is_persistent", TupleArguments.newValueArgument(tc
                                                .getTucsonTupleCentreId()
                                                .getLocalName())), null);
                        // InputEvent Creation
                        final InputEvent ev = new InputEvent(this.nodeAid,
                                opRequested, tc.getTucsonTupleCentreId(),
                                System.currentTimeMillis(), null);
                        TupleCentreContainer.doBlockingOperation(ev);
                        // TupleCentreContainer.doBlockingOperation(
                        // TupleCentreOpType.IN, this.nodeAid, tc
                        // .getTucsonTupleCentreId(),
                        // LogicTuples.newInstance("is_persistent", TupleArguments.newValueArgument(tc
                        // .getTucsonTupleCentreId().getName())));
                        LOGGER.info(">>> persistency disabled.");
                    }
                } catch (final TucsonOperationNotPossibleException | InvalidLogicTupleException | TucsonInvalidLogicTupleException e) {
                    LOGGER.error(e.getMessage(), e);
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
     * @param tc the identifier of the tuple centre whose persistency service
     *           should be enabled
     * @return wether persistency has been succesfully enabled
     */
    public synchronized boolean enablePersistency(final String tc) {
        final TucsonTCUsers tar = this.cores.get(tc);
        try {
            // Operation Make
            final RespectOperationDefault opRequested = RespectOperationDefault.make(
                    TupleCentreOpType.OUT, LogicTuples.newInstance("is_persistent",
                            TupleArguments.newValueArgument(tar.getTucsonTupleCentreId().getLocalName())),
                    null);
            // InputEvent Creation
            final InputEvent ev = new InputEvent(this.nodeAid, opRequested,
                    tar.getTucsonTupleCentreId(), System.currentTimeMillis(),
                    null);
            TupleCentreContainer.doBlockingOperation(ev);
            // TupleCentreContainer.doBlockingOperation(TupleCentreOpType.OUT,
            // this.nodeAid, tar.getTucsonTupleCentreId(), LogicTuples.newInstance(
            // "is_persistent", TupleArguments.newValueArgument(tar
            // .getTucsonTupleCentreId().getName())));
            return true;
        } catch (final TucsonOperationNotPossibleException | InvalidLogicTupleException | TucsonInvalidLogicTupleException e) {
            LOGGER.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * @param template the tuple template to be used in filtering tuple centre
     *                 identifiers whose persistency service should be enabled
     */
    public synchronized void enablePersistency(final Tuple template) {
        this.persistencyTemplate = template;
        LOGGER.info(">>> Looking for " + this.persistencyTemplate);
        for (TucsonTCUsers tc : this.cores.values()) {
            try {
                final TucsonTupleCentreId ttcid = tc.getTucsonTupleCentreId();
                final Tuple tid = LogicTuples.parse(ttcid.getLocalName());
                LOGGER.info(">>> Found tid: " + tid);
                if (LogicMatchingEngine.match((LogicTuple) template,
                        (LogicTuple) tid)) {
                    LOGGER.info(">>> It matches: enabling persistency...");
                    TupleCentreContainer.enablePersistency(ttcid,
                            PERSISTENCY_PATH);
                    // Operation Make
                    final RespectOperationDefault opRequested = RespectOperationDefault.make(
                            TupleCentreOpType.OUT, LogicTuples.newInstance(
                                    "is_persistent",
                                    TupleArguments.newValueArgument(tc.getTucsonTupleCentreId()
                                            .getLocalName())), null);
                    // InputEvent Creation
                    final InputEvent ev = new InputEvent(this.nodeAid,
                            opRequested, tc.getTucsonTupleCentreId(),
                            System.currentTimeMillis(), null);
                    TupleCentreContainer.doBlockingOperation(ev);
                    // TupleCentreContainer.doBlockingOperation(TucsonOperationDefault
                    // .outCode(), this.nodeAid, tc
                    // .getTucsonTupleCentreId(), LogicTuples.newInstance(
                    // "is_persistent", TupleArguments.newValueArgument(tc
                    // .getTucsonTupleCentreId().getName())));
                    LOGGER.info(">>> persistency enabled.");
                }
            } catch (final TucsonOperationNotPossibleException | InvalidLogicTupleException | TucsonInvalidLogicTupleException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }

    /**
     * @return a Map storing associations between String representations of
     * tuple centres along with the list of their users
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
     * @return the date when the TuCSoN node was installed
     */
    public Date getInstallationDate() {
        return this.installationDate;
    }

    /**
     * @return the observer of the TuCSoN node, if any
     */
    public NodeServiceListener getListener() {
        return this.obsService;
    }

    /**
     * @return the listening port this TuCSoN node is bound to
     */
    public int getTCPPort() {
        return this.tcpPort;
    }

    /*public final TPConfig getTPConfig() {
        return this.tpConfig;
    }*/

    /**
     *
     */
    public synchronized void install() {
        log("--------------------------------------------------------------------------------");
        try {
            final StringTokenizer st = new StringTokenizer(
                    Utils.fileToString(TUCSON_NODE_SERVICE_RESOURCES_FOLDER + "tucsonCLIlogo3.txt"),
                    "\n");
            while (st.hasMoreTokens()) {
                log(st.nextToken());
            }
        } catch (final IOException e) {
            // should not happen
            LOGGER.error(e.getMessage(), e);
        }
        log("--------------------------------------------------------------------------------");
        LOGGER.info("Welcome to the TuCSoN infrastructure :)");
        LOGGER.info("  Version " + TucsonInfo.getVersion());
        log("--------------------------------------------------------------------------------");
        LOGGER.info(new Date().toString());
        LOGGER.info("Beginning TuCSoN Node Service installation...");
        this.configManager = new Prolog();
        this.cores = new HashMap<>();
        LOGGER.info("Configuring TuCSoN Node Service...");
        try {
            this.setupConfiguration(this.configFile);
        } catch (final TucsonGenericException | InvalidConfigException e) {
            LOGGER.error(e.getMessage(), e);
        }
        LOGGER.info("Setting up Observation Service...");
        this.setupObsTupleCentre();
        LOGGER.info("Setting up Management Service...");
        this.setupConfigTupleCentre();
        this.checkPersistentTupleCentres(PERSISTENCY_PATH);
        LOGGER.info("Setting up Environment Configuration Service...");
        this.setupEnvConfigTupleCentre();
        LOGGER.info("Setting up Geolocation Configuration Service...");
        this.setupGeolocationConfigTupleCentre();
        this.installationDate = new Date();
        LOGGER.info("Spawning management agents...");
        this.bootManagementAgents();
    }

    /**
     * @param aid the identifier of the TuCSoN agent to be removed from users
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
        for (TucsonTCUsers tucsonTCUsers : this.cores.values()) {
            tucsonTCUsers.removeUser(aid);
        }
    }

    /**
     * @param i the InspectorContextSkel to eliminate
     */
    public void removeInspectorAgent(final InspectorContextSkel i) {
        this.inspectorAgents.remove(i);
    }

    /**
     * @param t the Thread object executing the internal management agent to
     *          be removed
     */
    public synchronized void removeNodeAgent(final Thread t) {
        this.nodeAgents.remove(t);
    }

    /**
     * @param tcn the String representation of the tuple centre whose usage
     *            associations should be retrieved
     * @return the Object representing associations between agents and tuple
     * centre they're using
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
            tid = new TucsonTupleCentreIdDefault(tcName.toString());
        } catch (final TucsonInvalidTupleCentreIdException e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
        final String realName = tid.getLocalName();
        TucsonTCUsers core = this.cores.get(realName);
        if (core == null) {
            LOGGER.info("Booting new tuple centre < " + realName
                    + " >...");
            try {
                core = this.bootTupleCentre(realName);
            } catch (final TucsonInvalidTupleCentreIdException e) {
                LOGGER.error(e.getMessage(), e);
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
        LOGGER.info("Node is shutting down management agents and proxies...");
        for (final Thread t : this.nodeAgents) {
            if (t.isAlive()) {
                LOGGER.info("  ...shutting down <" + t.getName()
                        + ">");
                t.interrupt();
                // boolean b = t.interrupted();
            } else {
                LOGGER.info("  ...<" + t.getName()
                        + "> is already dead");
            }
        }
        this.welcome.shutdown();
        try {
            this.ctxman.shutdown();
        } catch (final InterruptedException e) {
            LOGGER.warn("ACCProvider may still have tasks executing...");
        }
        this.envAgent.stopIteraction();
        LOGGER.info("Node is shutting down ReSpecT VMs...");
        for (final RespectTC tc : this.tcs) {
            final Thread t = tc.getVMThread();
            if (t.isAlive()) {
                LOGGER.info("  ...shutting down <" + tc.getId() + ">");
                t.interrupt();
            } else {
                LOGGER.info("  ...<" + tc.getId()
                        + "> is already dead");
            }
        }
        LOGGER.info("TuCSoN Node shutdown completed, see you :)");
    }

    /**
     *
     */
    private void bootManagementAgents() {
        LOGGER.info("Spawning Node Management Agent...");
        this.nodeAgents.add(new NodeManagementAgent(this.idConfigTC, this));
        log("--------------------------------------------------------------------------------");
        LOGGER.info("Spawning Geolocation Config Agent...");
        // GeolocationConfigAgent geolocationConfigAgent = new
        // GeolocationConfigAgent( "localhost", tcpPort );
        this.nodeAgents.add(new GeolocationConfigAgent(this.idGeolocationTC,
                this));
        log("--------------------------------------------------------------------------------");
        LOGGER.info("Spawning ACC Provider Agent...");
        this.ctxman = new ACCProvider(this, this.idConfigTC);
        LOGGER.info("Spawning Welcome Agent...");
        this.welcome = new WelcomeAgent(this, this.ctxman);
        LOGGER.info("Spawning Environmental Agent...");
        try {
            this.envAgent = new EnvConfigAgent("localhost", this.tcpPort);
        } catch (final TucsonInvalidAgentIdException e) {
            // Cannot happen
            LOGGER.error(e.getMessage(), e);
        }
    }

    /**
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
        final TucsonTupleCentreId id = new TucsonTupleCentreIdDefault(name.toString());
        try {
            final RespectTC rtc = TupleCentreContainer.createTC(id,
                    MAX_EVENT_QUEUE_SIZE, this.tcpPort);
            this.tcs.add(rtc);
        } catch (final InvalidTupleCentreIdException e) {
            LOGGER.error("TupleCentreContainer.createTC(...) error");
        }
        if (this.observed) {
            TupleCentreContainer.doManagementOperation(
                    TupleCentreOpType.ADD_OBS, id, this.obsService);
            this.obsService.tcCreated(id);
        }
        final TucsonTCUsers tcUsers = new TucsonTCUsers(id);
        this.cores.put(id.getLocalName(), tcUsers);
        return tcUsers;
    }

    /**
     * @param dirName
     */
    private void checkPersistentTupleCentres(final String dirName) {
        final File dir = new File(dirName);
        if (dir.exists() && dir.isDirectory()) {
            final String[] files = dir.list();
            for (final String file : Objects.requireNonNull(files)) {
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
                        LOGGER.info(">>> Persistent tc found: "
                                + fullTcName);
                        try {
                            this.bootTupleCentre(tcName);
                        } catch (final TucsonInvalidTupleCentreIdException e) {
                            LOGGER.error(e.getMessage(), e);
                        }
                        LOGGER.info(">>> Recovering persistent tc < "
                                + fullTcName + " >...");
                        final TucsonTupleCentreId ttcid = this.cores
                                .get(tcName).getTucsonTupleCentreId();
                        TupleCentreContainer.recoveryPersistent(ttcid,
                                PERSISTENCY_PATH, file);
                        // TupleCentreContainer.enablePersistency(
                        // this.cores.get(tcName).getTucsonTupleCentreId(),
                        // TucsonNodeService.PERSISTENCY_PATH);
                        try {
                            // Operation Make
                            final RespectOperationDefault opRequested = RespectOperationDefault
                                    .make(TupleCentreOpType.OUT,
                                            LogicTuples.newInstance("is_persistent",
                                                    TupleArguments.newValueArgument(tcName)), null);
                            // InputEvent Creation
                            final InputEvent ev = new InputEvent(this.nodeAid,
                                    opRequested, this.cores.get(tcName)
                                    .getTucsonTupleCentreId(),
                                    System.currentTimeMillis(), null);
                            TupleCentreContainer.doBlockingOperation(ev);
                            LOGGER.info(">>> ...persistent tc < "
                                    + fullTcName + " > recovered.");
                        } catch (final TucsonOperationNotPossibleException | InvalidLogicTupleException | TucsonInvalidLogicTupleException e) {
                            LOGGER.error(e.getMessage(), e);
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
            this.bootTupleCentre(this.idConfigTC.getLocalName());
            final InputStream is = Thread
                    .currentThread()
                    .getContextClassLoader()
                    .getResourceAsStream(
                            DEFAULT_BOOT_SPEC_FILE);
            final String spec = alice.util.Tools
                    .loadText(new BufferedInputStream(is));
            final LogicTuple specTuple = LogicTuples.newInstance("spec", TupleArguments.newValueArgument(spec));
            final RespectOperationDefault opRequested = RespectOperationDefault.make(
                    TupleCentreOpType.SET_S, specTuple, null);

            // InputEvent Creation
            final InputEvent ev = new InputEvent(this.nodeAid, opRequested,
                    this.idConfigTC, System.currentTimeMillis(), null);
            TupleCentreContainer.doBlockingSpecOperation(ev, specTuple);
            final RespectOperationDefault opRequested2 = RespectOperationDefault.make(
                    TupleCentreOpType.OUT, LogicTuples.newInstance("boot"), null);
            final InputEvent ev2 = new InputEvent(this.nodeAid, opRequested2,
                    this.idConfigTC, System.currentTimeMillis(), null);
            TupleCentreContainer.doNonBlockingOperation(ev2);

            // Set default agent class
            final RespectOperationDefault opRequested3 = RespectOperationDefault.make(
                    TupleCentreOpType.OUT,
                    LogicTuples.newInstance("basic_agent_class", TupleArguments.newValueArgument(
                            this.baseAgentClass)), null);
            final InputEvent ev3 = new InputEvent(this.nodeAid, opRequested3,
                    this.idConfigTC, System.currentTimeMillis(), null);
            TupleCentreContainer.doBlockingOperation(ev3);

            // Set login required
            final RespectOperationDefault opRequested4 = RespectOperationDefault.make(
                    TupleCentreOpType.OUT, LogicTuples.newInstance(
                            "is_login_required", TupleArguments.newValueArgument(
                                    this.loginRequired ? "yes" : "no")), null);
            final InputEvent ev4 = new InputEvent(this.nodeAid, opRequested4,
                    this.idConfigTC, System.currentTimeMillis(), null);
            TupleCentreContainer.doBlockingOperation(ev4);

            // Allow or not list of all roles
            final RespectOperationDefault opRequested5 = RespectOperationDefault.make(
                    TupleCentreOpType.OUT, LogicTuples.newInstance("list_all_roles",
                            TupleArguments.newValueArgument(this.listAllRoles ? "yes" : "no")), null);
            final InputEvent ev5 = new InputEvent(this.nodeAid, opRequested5,
                    this.idConfigTC, System.currentTimeMillis(), null);
            TupleCentreContainer.doBlockingOperation(ev5);

            final RespectOperationDefault opRequested6 = RespectOperationDefault.make(
                    TupleCentreOpType.OUT, LogicTuples.newInstance(
                            "allow_inspection", TupleArguments.newValueArgument(
                                    this.inspectorsAuthorised ? "yes" : "no")),
                    null);
            final InputEvent ev6 = new InputEvent(this.nodeAid, opRequested6,
                    this.idConfigTC, System.currentTimeMillis(), null);
            TupleCentreContainer.doBlockingOperation(ev6);

            // TupleCentreContainer.doBlockingOperation(TupleCentreOpType.OUT,
            // this.nodeAid, this.idConfigTC, LogicTuples.newInstance("role", new
            // Value("admin_role"), TupleArguments.newValueArgument("admin role"), TupleArguments.newValueArgument("0")));
            /*
             * if(!authForAdmin){
             * TupleCentreContainer.doBlockingOperation(TucsonOperationDefault
             * .outCode(), this.nodeAid, this.idConfigTC, new
             * LogicTuple("role_credentials", TupleArguments.newValueArgument("admin_role"), new
             * Value("_"))); } else if(adminUsername!=null &&
             * !adminUsername.equalsIgnoreCase("") && adminPassword!=null &&
             * !adminPassword.equalsIgnoreCase("")) {
             * TupleCentreContainer.doBlockingOperation
             * (TupleCentreOpType.OUT, nodeAid, idConfigTC, new
             * LogicTuple("role_credentials", TupleArguments.newValueArgument("admin_role"), new
             * Value(adminUsername+":"+TucsonACCTool.encrypt(adminPassword))));
             * } else {
             * TupleCentreContainer.doBlockingOperation(TucsonOperationDefault.
             * outCode(), this.nodeAid, this.idConfigTC, new
             * LogicTuple("role_credentials", TupleArguments.newValueArgument("admin_role"), new
             * Value("_"))); }
             */
            if (this.adminUsername != null
                    && !this.adminUsername.equalsIgnoreCase("")
                    && this.adminPassword != null
                    && !this.adminPassword.equalsIgnoreCase("")) {

                final RespectOperationDefault opRequested7 = RespectOperationDefault.make(
                        TupleCentreOpType.OUT,
                        LogicTuples.newInstance("admin_credentials", TupleArguments.newValueArgument(
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
        } catch (final TucsonInvalidTupleCentreIdException | InvalidLogicTupleException | TucsonInvalidSpecificationException | TucsonInvalidLogicTupleException | TucsonOperationNotPossibleException | IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    /**
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
            LOGGER.info("Configuration file not supported atm!");
            try {
                final InputStream is = Thread
                        .currentThread()
                        .getContextClassLoader()
                        .getResourceAsStream(
                                BOOT_SETUP_THEORY);
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
            this.bootTupleCentre(this.idEnvTC.getLocalName());
            final InputStream is = Thread
                    .currentThread()
                    .getContextClassLoader()
                    .getResourceAsStream(
                            DEFAULT_ENVCONFIG_SPEC_FILE);
            final String spec = alice.util.Tools
                    .loadText(new BufferedInputStream(is));
            final LogicTuple specTuple = LogicTuples.newInstance("spec", TupleArguments.newValueArgument(spec));
            // Operation Make
            final RespectOperationDefault opRequested = RespectOperationDefault.make(
                    TupleCentreOpType.SET_S, specTuple, null);
            // InputEvent Creation
            final InputEvent ev = new InputEvent(this.nodeAid, opRequested,
                    this.idEnvTC, System.currentTimeMillis(), null);
            TupleCentreContainer.doBlockingSpecOperation(ev, specTuple);
        } catch (final TucsonOperationNotPossibleException | InvalidLogicTupleException | TucsonInvalidTupleCentreIdException | IOException | TucsonInvalidSpecificationException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    /*
     * Setting up the environment configuration tuple centre
     */
    private void setupGeolocationConfigTupleCentre() {
        try {
            this.bootTupleCentre(this.idGeolocationTC.getLocalName());
            final InputStream is = Thread
                    .currentThread()
                    .getContextClassLoader()
                    .getResourceAsStream(
                            DEFAULT_GEOLOCATION_SPEC_FILE);
            final String spec = alice.util.Tools
                    .loadText(new BufferedInputStream(is));
            final LogicTuple specTuple = LogicTuples.newInstance("spec", TupleArguments.newValueArgument(spec));
            // Operation Make
            final RespectOperationDefault opRequested = RespectOperationDefault.make(
                    TupleCentreOpType.SET_S, specTuple, null);
            // InputEvent Creation
            final InputEvent ev = new InputEvent(this.nodeAid, opRequested,
                    this.idGeolocationTC, System.currentTimeMillis(), null);
            TupleCentreContainer.doBlockingSpecOperation(ev, specTuple);
            final RespectOperationDefault opRequested2 = RespectOperationDefault.make(
                    TupleCentreOpType.OUT, LogicTuples.newInstance("boot"), null);
            final InputEvent ev2 = new InputEvent(this.nodeAid, opRequested2,
                    this.idGeolocationTC, System.currentTimeMillis(), null);
            TupleCentreContainer.doNonBlockingOperation(ev2);
        } catch (final TucsonInvalidTupleCentreIdException | InvalidLogicTupleException | IOException | TucsonInvalidSpecificationException | TucsonOperationNotPossibleException | TucsonInvalidLogicTupleException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    /**
     *
     */
    private void setupObsTupleCentre() {
        try {
            this.bootTupleCentre(this.idObsTC.getLocalName());
            final InputStream is = Thread
                    .currentThread()
                    .getContextClassLoader()
                    .getResourceAsStream(
                            DEFAULT_OBS_SPEC_FILE);
            final String spec = alice.util.Tools
                    .loadText(new BufferedInputStream(is));
            final LogicTuple specTuple = LogicTuples.newInstance("spec", TupleArguments.newValueArgument(spec));
            // Operation Make
            final RespectOperationDefault opRequested = RespectOperationDefault.make(
                    TupleCentreOpType.SET_S, specTuple, null);
            // InputEvent Creation
            final InputEvent ev = new InputEvent(this.nodeAid, opRequested,
                    this.idObsTC, System.currentTimeMillis(), null);
            TupleCentreContainer.doBlockingSpecOperation(ev, specTuple);
            final RespectOperationDefault opRequested2 = RespectOperationDefault.make(
                    TupleCentreOpType.OUT, LogicTuples.newInstance("boot"), null);
            final InputEvent ev2 = new InputEvent(this.nodeAid, opRequested2,
                    this.idObsTC, System.currentTimeMillis(), null);
            TupleCentreContainer.doNonBlockingOperation(ev2);
            this.obsService = new ObservationService(this.idObsTC);
        } catch (final TucsonInvalidTupleCentreIdException | InvalidLogicTupleException | TucsonInvalidSpecificationException | TucsonInvalidLogicTupleException | TucsonOperationNotPossibleException | IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
