package alice.tuplecentre.tucson.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import alice.tuple.Tuple;
import alice.tuple.TupleTemplate;
import alice.tuple.java.impl.JTupleDefault;
import alice.tuple.java.impl.JTupleTemplateDefault;
import alice.tuple.java.impl.JTuplesEngine;
import alice.tuple.logic.LogicTuple;
import alice.tuplecentre.api.ITCCycleResult;
import alice.tuplecentre.api.OperationIdentifier;
import alice.tuplecentre.api.TupleCentreIdentifier;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;
import alice.tuplecentre.core.TupleCentreOpType;
import alice.tuplecentre.respect.api.geolocation.Position;
import alice.tuplecentre.tucson.api.TucsonAgentId;
import alice.tuplecentre.tucson.api.TucsonOperation;
import alice.tuplecentre.tucson.api.TucsonOperationCompletionListener;
import alice.tuplecentre.tucson.api.TucsonTupleCentreId;
import alice.tuplecentre.tucson.api.TucsonTupleCentreIdDefault;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidTupleCentreIdException;
import alice.tuplecentre.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tuplecentre.tucson.api.exceptions.UnreachableNodeException;
import alice.tuplecentre.tucson.network.TPFactory;
import alice.tuplecentre.tucson.network.TucsonProtocol;
import alice.tuplecentre.tucson.network.exceptions.DialogException;
import alice.tuplecentre.tucson.network.exceptions.DialogReceiveException;
import alice.tuplecentre.tucson.network.exceptions.DialogSendException;
import alice.tuplecentre.tucson.network.messages.TucsonMsgReply;
import alice.tuplecentre.tucson.network.messages.TucsonMsgRequest;
import alice.tuplecentre.tucson.network.messages.events.InputEventMsg;
import alice.tuplecentre.tucson.network.messages.events.InputEventMsgDefault;
import alice.tuprolog.Prolog;
import alice.tuprolog.lib.InvalidObjectIdException;

/**
 * @author ste (mailto: s.mariani@unibo.it) on 11/ago/2013
 */
public class OperationHandler {

    /**
     *
     */
    public class Controller extends Thread {

        private final TucsonProtocol dialog;
        private final Prolog p = new Prolog();
        private boolean stop;

        /**
         * @param d
         */
        Controller(final TucsonProtocol d) {
            super();
            this.dialog = d;
            this.stop = false;
            this.setDaemon(true);
            final alice.tuprolog.lib.OOLibrary jlib = (alice.tuprolog.lib.OOLibrary) this.p
                    .getLibrary("alice.tuprolog.lib.OOLibrary");
            try {
                jlib.register(new alice.tuprolog.Struct("config"), this);
            } catch (final InvalidObjectIdException e) {
                e.printStackTrace();
            }
        }

        /**
         *
         */
        @Override
        public void run() {
            TucsonOpCompletionEvent ev = null;
            while (!this.isStopped()) {
                /*
                 * FIXME possibile errore di accesso concorrente a operations
                 * dal thr controller e dal addOperation usato in doOperation?
                 */
                synchronized (OperationHandler.this.operations) {
                    for (final OperationIdentifier opId : OperationHandler.this.operationExpiredIds) {
                        OperationHandler.this.operations.remove(opId);
                    }
                }
                TucsonMsgReply msg = null;
                try {
                    msg = this.dialog.receiveMsgReply();
                } catch (final DialogReceiveException e) {
                    OperationHandler.this
                            .err("TuCSoN Node disconnected unexpectedly :/");
                    // OperationHandler.this.err(e.getCause().toString());
                    this.setStop();
                    break;
                }
                final boolean ok = msg.getEventMsg().isAllowed();
                if (ok) {
                    final TupleCentreOpType type = msg.getEventMsg().getOpType();
                    if (type == TupleCentreOpType.UIN
                            || type == TupleCentreOpType.UINP
                            || type == TupleCentreOpType.URD
                            || type == TupleCentreOpType.URDP
                            || type == TupleCentreOpType.UNO
                            || type == TupleCentreOpType.UNOP
                            || type == TupleCentreOpType.NO
                            || type == TupleCentreOpType.NO_S
                            || type == TupleCentreOpType.NOP
                            || type == TupleCentreOpType.NOP_S
                            || type == TupleCentreOpType.IN
                            || type == TupleCentreOpType.RD
                            || type == TupleCentreOpType.INP
                            || type == TupleCentreOpType.RDP
                            || type == TupleCentreOpType.IN_S
                            || type == TupleCentreOpType.RD_S
                            || type == TupleCentreOpType.INP_S
                            || type == TupleCentreOpType.RDP_S) {
                        final boolean succeeded = msg.getEventMsg()
                                .isSuccess();
                        if (succeeded) {
                            final LogicTuple tupleReq = msg.getEventMsg()
                                    .getTupleRequested();
                            final LogicTuple tupleRes = (LogicTuple) msg
                                    .getEventMsg().getTupleResult();
                            // log("tupleReq="+tupleReq+", tupleRes="+tupleRes);
                            final LogicTuple res = this.unify(tupleReq,
                                    tupleRes);
                            ev = new TucsonOpCompletionEvent(msg
                                    .getEventMsg().getOpId(), ok, true, msg
                                    .getEventMsg().isResultSuccess(), res);
                        } else {
                            ev = new TucsonOpCompletionEvent(msg
                                    .getEventMsg().getOpId(), ok, false,
                                    msg.getEventMsg().isResultSuccess());
                        }
                    } else if (type == TupleCentreOpType.OUT
                            || type == TupleCentreOpType.OUT_ALL
                            || type == TupleCentreOpType.OUT_S
                            || type == TupleCentreOpType.SPAWN
                            || type == TupleCentreOpType.SET
                            || type == TupleCentreOpType.SET_S
                            || type == TupleCentreOpType.GET_ENV
                            || type == TupleCentreOpType.SET_ENV) {
                        ev = new TucsonOpCompletionEvent(msg
                                .getEventMsg().getOpId(), ok, msg
                                .getEventMsg().isSuccess(), msg
                                .getEventMsg().isResultSuccess());
                    } else if (type == TupleCentreOpType.IN_ALL
                            || type == TupleCentreOpType.RD_ALL
                            || type == TupleCentreOpType.NO_ALL
                            || type == TupleCentreOpType.GET
                            || type == TupleCentreOpType.GET_S) {
                        final List<LogicTuple> tupleSetRes = (List<LogicTuple>) msg
                                .getEventMsg().getTupleResult();
                        ev = new TucsonOpCompletionEvent(msg
                                .getEventMsg().getOpId(), ok, msg
                                .getEventMsg().isSuccess(), msg
                                .getEventMsg().isResultSuccess(),
                                tupleSetRes);
                    } else if (type == TupleCentreOpType.EXIT) {
                        this.setStop();
                        break;
                    }
                } else {
                    ev = new TucsonOpCompletionEvent(msg
                            .getEventMsg().getOpId(), false, false, msg
                            .getEventMsg().isResultSuccess());
                }
                final TucsonOperation op;
                // removing completed op from pending list
                synchronized (OperationHandler.this.operations) {
                    op = OperationHandler.this.operations.remove(msg
                            .getEventMsg().getOpId());
                }
                if (op.getType() == TupleCentreOpType.NO_ALL || op.getType() == TupleCentreOpType.IN_ALL || op.getType() == TupleCentreOpType.RD_ALL || op.getType() == TupleCentreOpType.GET
                        || op.getType() == TupleCentreOpType.SET || op.getType() == TupleCentreOpType.GET_S || op.getType() == TupleCentreOpType.SET_S
                        || op.getType() == TupleCentreOpType.OUT_ALL) {
                    op.setTupleListResult((List<LogicTuple>) msg
                            .getEventMsg().getTupleResult());
                } else {
                    op.setTupleResult((LogicTuple) msg.getEventMsg()
                            .getTupleResult());
                }
                if (msg.getEventMsg().isResultSuccess()) {
                    op.setOpResult(ITCCycleResult.Outcome.SUCCESS);
                } else {
                    op.setOpResult(ITCCycleResult.Outcome.FAILURE);
                }
                OperationHandler.this.postEvent(ev);
                op.notifyCompletion();
            }
        }

        /**
         * Stops receiving replies from the TuCSoN node.
         */
        public synchronized void setStop() {
            this.stop = true;
        }

        /**
         * Checks whether this service, listening to TuCSoN node replies, is
         * stopped
         *
         * @return {@code true} or {@code false} depending on whether this
         * listening service is stopped or not
         */
        private synchronized boolean isStopped() {
            return this.stop;
        }

        /**
         * @param template
         * @param tuple
         * @return
         */
        private LogicTuple unify(final TupleTemplate template, final Tuple tuple) {
            final boolean res = template.propagate(tuple);
            if (res) {
                return (LogicTuple) template;
            }
            return null;
        }
    }

    /**
     *
     */
    public class ControllerSession {

        private final Controller controller;
        private final TucsonProtocol session;

        /**
         * @param c
         * @param s
         */
        ControllerSession(final Controller c, final TucsonProtocol s) {
            this.controller = c;
            this.session = s;
        }

        /**
         * @return the Controller object monitoring operation completions
         */
        public Controller getController() {
            return this.controller;
        }

        /**
         * @return the (generic) connection protocol used by this operation
         * handler
         */
        public TucsonProtocol getSession() {
            return this.session;
        }
    }

    private static final int TRIES = 3;
    /**
     * UUID of the agent using this OperationHandler
     */
    public UUID agentUUID;
    /**
     * Active sessions toward different nodes
     */
    protected Map<String, ControllerSession> controllerSessions;
    /**
     * TuCSoN requests completion events (node replies events)
     */
    public List<TucsonOpCompletionEvent> events;
    /**
     * Expired TuCSoN operations
     */
    protected List<OperationIdentifier> operationExpiredIds;
    /**
     * Requested TuCSoN operations
     */
    public Map<OperationIdentifier, TucsonOperation> operations;

    /**
     * Current ACC session description
     */
    protected ACCDescription profile;

    /**
     * @param uuid the Java UUID of the agent this handler serves.
     */
    public OperationHandler(final UUID uuid) {
        this.agentUUID = uuid;
        this.profile = new ACCDescription();
        this.events = new LinkedList<>();
        this.controllerSessions = new HashMap<>();
        this.operations = new HashMap<>();
        this.operationExpiredIds = new ArrayList<>();
    }

    /**
     * @param op the TuCSoN operation waiting to be served
     */
    public void addOperation(final TucsonOperation op) {
        this.operations.put(op.getId(), op);
    }

    /**
     * Method to track expired operations, that is operations whose completion
     * has not been received before specified timeout expiration
     *
     * @param id Unique Identifier of the expired operation
     */
    public void addOperationExpired(final OperationIdentifier id) {
        this.operationExpiredIds.add(id);
    }

    /**
     * Private method that takes in charge execution of all the Synchronous
     * primitives listed above. It simply forwards real execution to another
     * private method {@link alice.tuplecentre.tucson.api doOperation doOp} (notice that in
     * truth there is no real execution at this point: we are just packing
     * primitives invocation into TuCSoN messages, then send them to the Node
     * side)
     * <p>
     * The difference w.r.t. the previous method
     * {@link alice.tuplecentre.tucson.service.OperationHandler#doNonBlockingOperation
     * nonBlocking} is that here we explicitly wait for completion a time
     * specified in the timeout input parameter.
     *
     * @param aid      the agent identifier
     * @param type     TuCSoN operation type (internal integer code)
     * @param tid      Target TuCSoN tuplecentre id
     *                 {@link TucsonTupleCentreIdDefault tid}
     * @param t        The Logic Tuple involved in the requested operation
     * @param ms       Maximum waiting time tolerated by the callee TuCSoN Agent
     * @param position the {@link Position} of the agent invoking the operation
     * @return An object representing the primitive invocation on the TuCSoN
     * infrastructure which will store its result
     * @throws TucsonOperationNotPossibleException if the operation requested cannot be performed
     * @throws UnreachableNodeException            if the target tuple centre cannot be reached over the network
     * @throws OperationTimeOutException           if the timeout associated to the operation requested expires
     *                                             prior to operation completion
     * @see TucsonTupleCentreIdDefault TucsonTupleCentreId
     */
    public TucsonOperation doBlockingOperation(final TucsonAgentId aid,
                                               final TupleCentreOpType type, final Object tid, final Tuple t, final Long ms,
                                               final Position position)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException {
        TucsonTupleCentreId tcid = null;
        if ("alice.tuplecentre.tucson.api.TucsonTupleCentreIdDefault".equals(tid.getClass()
                .getName())) {
            tcid = (TucsonTupleCentreId) tid;
        } else if ("alice.tuplecentre.respect.api.TupleCentreId".equals(tid.getClass()
                .getName())) {
            tcid = new TucsonTupleCentreIdDefault((TupleCentreIdentifier) tid);
        } else if ("java.lang.String".equals(tid.getClass().getName())) {
            try {
                tcid = new TucsonTupleCentreIdDefault((String) tid);
            } catch (final TucsonInvalidTupleCentreIdException ex) {
                System.err.println("[ACCProxyAgentSide]: " + ex);
                return null;
            }
        } else {
            throw new TucsonOperationNotPossibleException();
        }
        TucsonOperation op = null;
        op = this.doOperation(aid, tcid, type, t, null, position);
        if (ms == null) {
            op.waitForOperationCompletion();
        } else {
            op.waitForOperationCompletion(ms);
        }
        return op;
    }

    /**
     * Private method that takes in charge execution of all the Asynchronous
     * primitives listed above. It simply forwards real execution to another
     * private method {@link alice.tuplecentre.tucson.api doOperation doOp} (notice that in
     * truth there is no real execution at this point: we are just packing
     * primitives invocation into TuCSoN messages, then send them to the Node
     * side)
     *
     * @param aid      the agent identifier
     * @param type     TuCSoN operation type (internal integer code)
     * @param tid      Target TuCSoN tuplecentre id
     *                 {@link TucsonTupleCentreIdDefault tid}
     * @param t        The Logic Tuple involved in the requested operation
     * @param l        The listener who should be notified upon operation completion
     * @param position the {@link Position} of the agent invoking the operation
     * @return An object representing the primitive invocation on the TuCSoN
     * infrastructure which will store its result
     * @throws TucsonOperationNotPossibleException if the operation requested cannot be performed
     * @throws UnreachableNodeException            if the target tuple centre cannot be reached over the network
     * @see TucsonTupleCentreIdDefault TucsonTupleCentreId
     * @see alice.tuplecentre.tucson.api.TucsonOperationCompletionListener
     * TucsonOperationCompletionListener
     * @see TucsonOperation TucsonOperation
     */
    public TucsonOperation doNonBlockingOperation(final TucsonAgentId aid,
                                                  final TupleCentreOpType type, final Object tid, final Tuple t,
                                                  final TucsonOperationCompletionListener l, Position position)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException {
        // log("tid.class().name() = " + tid.getClass().getName());
        TucsonTupleCentreId tcid = null;
        if ("alice.tuplecentre.tucson.api.TucsonTupleCentreIdDefault".equals(tid.getClass()
                .getName())) {
            tcid = (TucsonTupleCentreId) tid;
        } else if ("alice.tuplecentre.respect.api.TupleCentreId".equals(tid.getClass()
                .getName())) {
            tcid = new TucsonTupleCentreIdDefault((TupleCentreIdentifier) tid);
            // log("tcid = " + tcid);
        } else if ("java.lang.String".equals(tid.getClass().getName())) {
            try {
                tcid = new TucsonTupleCentreIdDefault((String) tid);
            } catch (final TucsonInvalidTupleCentreIdException ex) {
                System.err.println("[ACCProxyAgentSide]: " + ex);
                return null;
            }
        } else {
            throw new TucsonOperationNotPossibleException();
        }
        return this.doOperation(aid, tcid, type, t, l, position);
    }

    /**
     * @return the Map associations between the String representation of a
     * TuCSoN node network address and the TuCSoN protocol session
     * currently active toward those nodes
     */
    public Map<String, ControllerSession> getControllerSessions() {
        return this.controllerSessions;
    }

    private void err(final String msg) {
        System.err.println("....[OperationHandler ("
                + this.profile.getProperty("agent-identity") + ")]: " + msg);
    }

    /**
     * Method internally used to log proxy activity (could be used for debug)
     *
     * @param msg String to display on the standard output
     */
    private void log(final String msg) {
        System.out.println("....[OperationHandler ("
                + this.profile.getProperty("agent-identity") + ")]: " + msg);
    }

    /**
     * This method is the real responsible of TuCSoN operations execution.
     * <p>
     * First, it takes the target tuplecentre and checks wether this proxy has
     * ever established a connection toward it: if it did, the already opened
     * connection is retrieved and used, otherwise a new connection is opened
     * and stored for later use
     * {@link alice.tuplecentre.tucson.service.OperationHandler#getSession getSession}.
     * <p>
     * Then, a Tucson Operation {@link TucsonOperationDefault op}
     * storing any useful information about the TuCSoN primitive invocation is
     * created and packed into a Tucson Message Request
     * {@link TucsonMsgRequest} to be possibly sent over
     * the wire toward the target tuplecentre.
     * <p>
     * Notice that a listener is needed, who is the proxy itself, wichever was
     * the requested operation (inp, in, etc.) and despite its (a-)synchronous
     * behavior. This is because of the distributed very nature of TuCSoN: we
     * couldn't expect to block on a socket waiting for a reply. Instead,
     * requested operations should be dispatched toward the TuCSoN Node Service,
     * which in turn will take them in charge and notify the requestor upon
     * completion.
     *
     * @param aid      the agent identifier
     * @param tcid     Target TuCSoN tuplecentre id
     *                 {@link TucsonTupleCentreIdDefault tid}
     * @param type     TuCSoN operation type (internal integer code)
     * @param t        The Logic Tuple involved in the requested operation
     * @param l        The listener who should be notified upon operation completion
     * @param position the {@link Position} of the agent invoking the operation
     * @return An object representing the primitive invocation on the TuCSoN
     * infrastructure which will store its result
     * @throws UnreachableNodeException if the target tuple centre cannot be reached over the network
     * @see TucsonTupleCentreIdDefault TucsonTupleCentreId
     * @see alice.tuplecentre.tucson.api.TucsonOperationCompletionListener
     * TucsonOperationCompletionListener
     * @see TucsonOperation TucsonOperation
     * @see TucsonOperationDefault TucsonOperationDefault
     */
    protected synchronized TucsonOperation doOperation(
            final TucsonAgentId aid, final TucsonTupleCentreId tcid,
            final TupleCentreOpType type, final Tuple t,
            final TucsonOperationCompletionListener l, final Position position)
            throws UnreachableNodeException {
        // this.log("t = " + t);
        Tuple tupl = null;
        if (t instanceof LogicTuple) {
            tupl = t;
        } else if (t instanceof JTupleDefault) {
            tupl = JTuplesEngine.toLogicTuple((JTupleDefault) t);
        } else if (t instanceof JTupleTemplateDefault) {
            tupl = JTuplesEngine.toLogicTuple((JTupleTemplateDefault) t);
        }
        // this.log("tupl = " + tupl);
        int nTry = 0;
        boolean exception;
        do {
            nTry++;
            exception = false;
            TucsonProtocol session = null;
            try {
                session = this.getSession(tcid, aid);
            } catch (final UnreachableNodeException ex2) {
                exception = true;
                throw new UnreachableNodeException(ex2);
            }
            TucsonOperationDefault op = null;
            if (type == TupleCentreOpType.OUT
                    || type == TupleCentreOpType.OUT_S
                    || type == TupleCentreOpType.SET_S
                    || type == TupleCentreOpType.SET
                    || type == TupleCentreOpType.OUT_ALL
                    || type == TupleCentreOpType.SPAWN) {
                // maybe tupl should be TupleTemplate, thus here cast to Tuple
                op = new TucsonOperationDefault(type, tupl, l, this);
            } else {
                op = new TucsonOperationDefault(type, (TupleTemplate) tupl, l, this);
            }
            // put invoked ops in pending list
            synchronized (this.operations) {
                this.operations.put(op.getId(), op);
            }
            // TODO: 02/03/2018  isn't that operation added twice ????????
            this.addOperation(op);
            // TODO: 02/03/2018 CHECK
            final InputEventMsg ev = new InputEventMsgDefault(aid.toString(),
                    op.getId(), op.getType(), op.getLogicTupleArgument(),
                    tcid.toString(), System.currentTimeMillis(), position);

            final TucsonMsgRequest msg = new TucsonMsgRequest(ev);

            /*
             * final TucsonMsgRequest msg = new TucsonMsgRequest(op.getId(),
             * op.getType(), tcid.toString(), op.getLogicTupleArgument());
             */
            this.log("requesting op " + msg.getEventMsg().getOpType()
                    + ", " + msg.getEventMsg().getTuple() + ", "
                    + msg.getEventMsg().getTarget());
            try {
                session.sendMsgRequest(msg);
            } catch (final DialogSendException ex) {
                exception = true;
                System.err.println("[ACCProxyAgentSide]: " + ex);
            }
            if (!exception) {
                return op;
            }
        } while (nTry < OperationHandler.TRIES);
        throw new UnreachableNodeException();
    }

    /**
     * This method is responsible to setup, store and retrieve connections
     * toward all the tuplecentres ever contacted by the TuCSoN Agent behind
     * this proxy.
     * <p>
     * If a connection toward the given target tuplecentre already exists, it is
     * retrieved and used. If not, the new connection is setup then stored for
     * later use.
     * <p>
     * It is worth noting a couple of things. Why don't we setup connections
     * once and for all as soon as the TuCSoN Agent is booted? The reason is
     * that new tuplecentres can be created at run-time as TuCSoN Agents please,
     * thus for every TuCSoN Operation request we should check wether a new
     * tuplecentre has to be created and booted. If a new tuplecentre has to be
     * booted the correspondant proxy node side is dinamically triggered and
     * booted
     *
     * @param tid Target TuCSoN tuplecentre id
     *            {@link TucsonTupleCentreIdDefault tid}
     * @param aid the agent identifier
     * @return The open session toward the given target tuplecentre
     * @throws UnreachableNodeException if the target tuple centre cannot be reached over the network
     * @see alice.tuplecentre.tucson.network.AbstractTucsonProtocol TucsonProtocol
     */
    public TucsonProtocol getSession(final TucsonTupleCentreId tid,
                                     final TucsonAgentId aid) throws UnreachableNodeException {
        final String opNode = alice.util.Tools.removeApices(tid.getNode());
        final int p = tid.getPort();
        ControllerSession tc = this.controllerSessions.get(opNode + ":" + p);
        if (tc != null) {
            return tc.getSession();
        }
        // if (InetAddress.getLoopbackAddress().getHostName().equals(opNode)) {
        if ("localhost".equals(opNode)) {
            tc =
                    // this.controllerSessions.get(InetAddress
                    // .getLoopbackAddress().getHostAddress()
                    // .concat(String.valueOf(p)));
                    this.controllerSessions.get("127.0.0.1:".concat(String.valueOf(p)));
        }
        // if (InetAddress.getLoopbackAddress().getHostAddress().equals(opNode))
        // {
        if ("127.0.0.1".equals(opNode)) {
            tc =
                    // this.controllerSessions.get(InetAddress
                    // .getLoopbackAddress().getHostName()
                    // .concat(String.valueOf(p)));
                    this.controllerSessions.get("localhost:".concat(String.valueOf(p)));
        }
        if (tc != null) {
            return tc.getSession();
        }
        this.profile.setProperty("agent-identity", aid.toString());
        this.profile.setProperty("agent-role", "user");
        this.profile.setProperty("agent-uuid", this.agentUUID.toString());
        // this.profile.setProperty("agent-class", value);
        TucsonProtocol dialog = null;
        boolean isEnterReqAcpt = false;
        try {
            dialog = TPFactory.getDialogAgentSide(tid);
            dialog.sendEnterRequest(this.profile);
            dialog.receiveEnterRequestAnswer();
            if (dialog.isEnterRequestAccepted()) {
                isEnterReqAcpt = true;
            }
        } catch (final DialogException e) {
            e.printStackTrace();
        }
        if (isEnterReqAcpt) {
            final Controller contr = new Controller(dialog);
            final ControllerSession cs = new ControllerSession(contr, dialog);
            this.controllerSessions.put(opNode + ":" + p, cs);
            contr.start();
            return dialog;
        }
        return null;
    }

    /**
     * Method to add a TuCSoN Operation Completion Event
     * {@link alice.tuplecentre.tucson.service.TucsonOpCompletionEvent events} to the
     * internal queue of pending completion events to process
     *
     * @param ev Completion Event to be added to pending queue
     * @see alice.tuplecentre.tucson.service.TucsonOpCompletionEvent TucsonOpCompletionEvent
     */
    protected void postEvent(final TucsonOpCompletionEvent ev) {
        // FIXME Check correctness
        synchronized (this.events) {
            this.events.add(this.events.size(), ev);
        }
    }
}
