package alice.tucson.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import alice.logictuple.LogicTuple;
import alice.respect.api.TupleCentreId;
import alice.respect.api.geolocation.Position;
import alice.tucson.api.TucsonAgentIdDefault;
import alice.tucson.api.TucsonOpId;
import alice.tucson.api.TucsonOperation;
import alice.tucson.api.TucsonOperationCompletionListener;
import alice.tucson.api.TucsonTupleCentreIdDefault;
import alice.tucson.api.exceptions.TucsonInvalidTupleCentreIdException;
import alice.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tucson.api.exceptions.UnreachableNodeException;
import alice.tucson.network.AbstractTucsonProtocol;
import alice.tucson.network.TPFactory;
import alice.tucson.network.TucsonMsgReply;
import alice.tucson.network.TucsonMsgRequest;
import alice.tucson.network.exceptions.DialogException;
import alice.tucson.network.exceptions.DialogReceiveException;
import alice.tucson.network.exceptions.DialogSendException;
import alice.tuplecentre.api.ITCCycleResult;
import alice.tuplecentre.api.Tuple;
import alice.tuplecentre.api.TupleTemplate;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;
import alice.tuplecentre.core.TupleCentreOpType;
import alice.tuples.javatuples.impl.JTuple;
import alice.tuples.javatuples.impl.JTupleTemplate;
import alice.tuples.javatuples.impl.JTuplesEngine;
import alice.tuprolog.Prolog;
import alice.tuprolog.lib.InvalidObjectIdException;

/**
 *
 * @author ste (mailto: s.mariani@unibo.it) on 11/ago/2013
 *
 */
public class OperationHandler {

    /**
     *
     */
    public class Controller extends Thread {

        private final AbstractTucsonProtocol dialog;
        private final Prolog p = new Prolog();
        private boolean stop;

        /**
         *
         * @param d
         */
        Controller(final AbstractTucsonProtocol d) {
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
                    for (final Long operation : OperationHandler.this.operationExpired) {
                        OperationHandler.this.operations.remove(operation);
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
                final boolean ok = msg.getOutputEvent().isAllowed();
                if (ok) {
                    final TupleCentreOpType type = msg.getOutputEvent().getOpType();
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
                        final boolean succeeded = msg.getOutputEvent()
                                .isSuccess();
                        if (succeeded) {
                            final LogicTuple tupleReq = msg.getOutputEvent()
                                    .getTupleRequested();
                            final LogicTuple tupleRes = (LogicTuple) msg
                                    .getOutputEvent().getTupleResult();
                            // log("tupleReq="+tupleReq+", tupleRes="+tupleRes);
                            final LogicTuple res = this.unify(tupleReq,
                                    tupleRes);
                            ev = new TucsonOpCompletionEvent(new TucsonOpId(msg
                                    .getOutputEvent().getOpId()), ok, true, msg
                                    .getOutputEvent().isResultSuccess(), res);
                        } else {
                            ev = new TucsonOpCompletionEvent(new TucsonOpId(msg
                                    .getOutputEvent().getOpId()), ok, false,
                                    msg.getOutputEvent().isResultSuccess());
                        }
                    } else if (type == TupleCentreOpType.OUT
                            || type == TupleCentreOpType.OUT_ALL
                            || type == TupleCentreOpType.OUT_S
                            || type == TupleCentreOpType.SPAWN
                            || type == TupleCentreOpType.SET
                            || type == TupleCentreOpType.SET_S
                            || type == TupleCentreOpType.GET_ENV
                            || type == TupleCentreOpType.SET_ENV) {
                        ev = new TucsonOpCompletionEvent(new TucsonOpId(msg
                                .getOutputEvent().getOpId()), ok, msg
                                .getOutputEvent().isSuccess(), msg
                                .getOutputEvent().isResultSuccess());
                    } else if (type == TupleCentreOpType.IN_ALL
                            || type == TupleCentreOpType.RD_ALL
                            || type == TupleCentreOpType.NO_ALL
                            || type == TupleCentreOpType.GET
                            || type == TupleCentreOpType.GET_S) {
                        final List<LogicTuple> tupleSetRes = (List<LogicTuple>) msg
                                .getOutputEvent().getTupleResult();
                        ev = new TucsonOpCompletionEvent(new TucsonOpId(msg
                                .getOutputEvent().getOpId()), ok, msg
                                .getOutputEvent().isSuccess(), msg
                                .getOutputEvent().isResultSuccess(),
                                tupleSetRes);
                    } else if (type == TupleCentreOpType.EXIT) {
                        this.setStop();
                        break;
                    }
                } else {
                    ev = new TucsonOpCompletionEvent(new TucsonOpId(msg
                            .getOutputEvent().getOpId()), false, false, msg
                            .getOutputEvent().isResultSuccess());
                }
                final TucsonOperation op;
                // removing completed op from pending list
                synchronized (OperationHandler.this.operations) {
                    op = OperationHandler.this.operations.remove(msg
                            .getOutputEvent().getOpId());
                }
                if (op.getType() == TupleCentreOpType.NO_ALL || op.getType() == TupleCentreOpType.IN_ALL || op.getType() == TupleCentreOpType.RD_ALL || op.getType() == TupleCentreOpType.GET
                        || op.getType() == TupleCentreOpType.SET || op.getType() == TupleCentreOpType.GET_S || op.getType() == TupleCentreOpType.SET_S
                        || op.getType() == TupleCentreOpType.OUT_ALL) {
                    op.setTupleListResult((List<LogicTuple>) msg
                            .getOutputEvent().getTupleResult());
                } else {
                    op.setTupleResult((LogicTuple) msg.getOutputEvent()
                            .getTupleResult());
                }
                if (msg.getOutputEvent().isResultSuccess()) {
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
         *         listening service is stopped or not
         */
        private synchronized boolean isStopped() {
            return this.stop;
        }

        /**
         *
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
        private final AbstractTucsonProtocol session;

        /**
         *
         * @param c
         * @param s
         */
        ControllerSession(final Controller c, final AbstractTucsonProtocol s) {
            this.controller = c;
            this.session = s;
        }

        /**
         *
         * @return the Controller object monitoring operation completions
         */
        public Controller getController() {
            return this.controller;
        }

        /**
         *
         * @return the (generic) connection protocol used by this operation
         *         handler
         */
        public AbstractTucsonProtocol getSession() {
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
    protected List<Long> operationExpired;
    /**
     * Requested TuCSoN operations
     */
    public Map<Long, TucsonOperation> operations;

    /**
     * Current ACC session description
     */
    protected ACCDescription profile;

    /**
     * @param uuid
     *            the Java UUID of the agent this handler serves.
     *
     */
    public OperationHandler(final UUID uuid) {
        this.agentUUID = uuid;
        this.profile = new ACCDescription();
        this.events = new LinkedList<TucsonOpCompletionEvent>();
        this.controllerSessions = new HashMap<String, OperationHandler.ControllerSession>();
        this.operations = new HashMap<Long, TucsonOperation>();
        this.operationExpired = new ArrayList<Long>();
    }

    /**
     *
     * @param id
     *            the Long identifier of the pending operation just requested
     * @param op
     *            the TuCSoN operation waiting to be served
     */
    public void addOperation(final Long id, final TucsonOperation op) {
        this.operations.put(id, op);
    }

    /**
     * Method to track expired operations, that is operations whose completion
     * has not been received before specified timeout expiration
     *
     * @param id
     *            Unique Identifier of the expired operation
     */
    public void addOperationExpired(final long id) {
        this.operationExpired.add(id);
    }

    /**
     * Private method that takes in charge execution of all the Synchronous
     * primitives listed above. It simply forwards real execution to another
     * private method {@link alice.tucson.api doOperation doOp} (notice that in
     * truth there is no real execution at this point: we are just packing
     * primitives invocation into TuCSoN messages, then send them to the Node
     * side)
     *
     * The difference w.r.t. the previous method
     * {@link alice.tucson.service.OperationHandler#doNonBlockingOperation
     * nonBlocking} is that here we explicitly wait for completion a time
     * specified in the timeout input parameter.
     *
     * @param aid
     *            the agent identifier
     * @param type
     *            TuCSoN operation type (internal integer code)
     * @param tid
     *            Target TuCSoN tuplecentre id
     *            {@link TucsonTupleCentreIdDefault tid}
     * @param t
     *            The Logic Tuple involved in the requested operation
     * @param ms
     *            Maximum waiting time tolerated by the callee TuCSoN Agent
     * @param position
     *            the {@link Position} of the agent invoking the operation
     *
     * @return An object representing the primitive invocation on the TuCSoN
     *         infrastructure which will store its result
     *
     * @throws TucsonOperationNotPossibleException
     *             if the operation requested cannot be performed
     * @throws UnreachableNodeException
     *             if the target tuple centre cannot be reached over the network
     * @throws OperationTimeOutException
     *             if the timeout associated to the operation requested expires
     *             prior to operation completion
     *
     * @see TucsonTupleCentreIdDefault TucsonTupleCentreId
     */
    public TucsonOperation doBlockingOperation(final TucsonAgentIdDefault aid,
                                               final TupleCentreOpType type, final Object tid, final Tuple t, final Long ms,
                                               final Position position)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException {
        TucsonTupleCentreIdDefault tcid = null;
        if ("alice.tucson.api.TucsonTupleCentreIdDefault".equals(tid.getClass()
                .getName())) {
            tcid = (TucsonTupleCentreIdDefault) tid;
        } else if ("alice.respect.api.TupleCentreId".equals(tid.getClass()
                .getName())) {
            tcid = new TucsonTupleCentreIdDefault((TupleCentreId) tid);
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
     * private method {@link alice.tucson.api doOperation doOp} (notice that in
     * truth there is no real execution at this point: we are just packing
     * primitives invocation into TuCSoN messages, then send them to the Node
     * side)
     *
     * @param aid
     *            the agent identifier
     * @param type
     *            TuCSoN operation type (internal integer code)
     * @param tid
     *            Target TuCSoN tuplecentre id
     *            {@link TucsonTupleCentreIdDefault tid}
     * @param t
     *            The Logic Tuple involved in the requested operation
     * @param l
     *            The listener who should be notified upon operation completion
     * @param position
     *            the {@link Position} of the agent invoking the operation
     *
     * @return An object representing the primitive invocation on the TuCSoN
     *         infrastructure which will store its result
     *
     * @throws TucsonOperationNotPossibleException
     *             if the operation requested cannot be performed
     * @throws UnreachableNodeException
     *             if the target tuple centre cannot be reached over the network
     *
     * @see TucsonTupleCentreIdDefault TucsonTupleCentreId
     * @see alice.tucson.api.TucsonOperationCompletionListener
     *      TucsonOperationCompletionListener
     * @see TucsonOperation TucsonOperation
     */
    public TucsonOperation doNonBlockingOperation(final TucsonAgentIdDefault aid,
                                                  final TupleCentreOpType type, final Object tid, final Tuple t,
                                                  final TucsonOperationCompletionListener l, Position position)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException {
        // log("tid.class().name() = " + tid.getClass().getName());
        TucsonTupleCentreIdDefault tcid = null;
        if ("alice.tucson.api.TucsonTupleCentreIdDefault".equals(tid.getClass()
                .getName())) {
            tcid = (TucsonTupleCentreIdDefault) tid;
        } else if ("alice.respect.api.TupleCentreId".equals(tid.getClass()
                .getName())) {
            tcid = new TucsonTupleCentreIdDefault((TupleCentreId) tid);
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
     *
     * @return the Map associations between the String representation of a
     *         TuCSoN node network address and the TuCSoN protocol session
     *         currently active toward those nodes
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
     * @param msg
     *            String to display on the standard output
     */
    private void log(final String msg) {
        System.out.println("....[OperationHandler ("
                + this.profile.getProperty("agent-identity") + ")]: " + msg);
    }

    /**
     * This method is the real responsible of TuCSoN operations execution.
     *
     * First, it takes the target tuplecentre and checks wether this proxy has
     * ever established a connection toward it: if it did, the already opened
     * connection is retrieved and used, otherwise a new connection is opened
     * and stored for later use
     * {@link alice.tucson.service.OperationHandler#getSession getSession}.
     *
     * Then, a Tucson Operation {@link TucsonOperationDefault op}
     * storing any useful information about the TuCSoN primitive invocation is
     * created and packed into a Tucson Message Request
     * {@link alice.tucson.network.TucsonMsgRequest} to be possibly sent over
     * the wire toward the target tuplecentre.
     *
     * Notice that a listener is needed, who is the proxy itself, wichever was
     * the requested operation (inp, in, etc.) and despite its (a-)synchronous
     * behavior. This is because of the distributed very nature of TuCSoN: we
     * couldn't expect to block on a socket waiting for a reply. Instead,
     * requested operations should be dispatched toward the TuCSoN Node Service,
     * which in turn will take them in charge and notify the requestor upon
     * completion.
     *
     * @param aid
     *            the agent identifier
     * @param tcid
     *            Target TuCSoN tuplecentre id
     *            {@link TucsonTupleCentreIdDefault tid}
     * @param type
     *            TuCSoN operation type (internal integer code)
     * @param t
     *            The Logic Tuple involved in the requested operation
     * @param l
     *            The listener who should be notified upon operation completion
     * @param position
     *            the {@link Position} of the agent invoking the operation
     *
     * @return An object representing the primitive invocation on the TuCSoN
     *         infrastructure which will store its result
     *
     * @throws UnreachableNodeException
     *             if the target tuple centre cannot be reached over the network
     *
     * @see TucsonTupleCentreIdDefault TucsonTupleCentreId
     * @see alice.tucson.api.TucsonOperationCompletionListener
     *      TucsonOperationCompletionListener
     * @see TucsonOperation TucsonOperation
     * @see TucsonOperationDefault TucsonOperationDefault
     */
    protected synchronized TucsonOperation doOperation(
            final TucsonAgentIdDefault aid, final TucsonTupleCentreIdDefault tcid,
            final TupleCentreOpType type, final Tuple t,
            final TucsonOperationCompletionListener l, final Position position)
            throws UnreachableNodeException {
        // this.log("t = " + t);
        Tuple tupl = null;
        if (t instanceof LogicTuple) {
            tupl = t;
        } else if (t instanceof JTuple) {
            tupl = JTuplesEngine.toLogicTuple((JTuple) t);
        } else if (t instanceof JTupleTemplate) {
            tupl = JTuplesEngine.toLogicTuple((JTupleTemplate) t);
        }
        // this.log("tupl = " + tupl);
        int nTry = 0;
        boolean exception;
        do {
            nTry++;
            exception = false;
            AbstractTucsonProtocol session = null;
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

            this.addOperation(op.getId(), op);
            final InputEventMsg ev = new InputEventMsg(aid.toString(),
                    op.getId(), op.getType(), op.getLogicTupleArgument(),
                    tcid.toString(), System.currentTimeMillis(), position);

            final TucsonMsgRequest msg = new TucsonMsgRequest(ev);

            /*
             * final TucsonMsgRequest msg = new TucsonMsgRequest(op.getId(),
             * op.getType(), tcid.toString(), op.getLogicTupleArgument());
             */
            this.log("requesting op " + msg.getInputEventMsg().getOpType()
                    + ", " + msg.getInputEventMsg().getTuple() + ", "
                    + msg.getInputEventMsg().getTarget());
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
     *
     * If a connection toward the given target tuplecentre already exists, it is
     * retrieved and used. If not, the new connection is setup then stored for
     * later use.
     *
     * It is worth noting a couple of things. Why don't we setup connections
     * once and for all as soon as the TuCSoN Agent is booted? The reason is
     * that new tuplecentres can be created at run-time as TuCSoN Agents please,
     * thus for every TuCSoN Operation request we should check wether a new
     * tuplecentre has to be created and booted. If a new tuplecentre has to be
     * booted the correspondant proxy node side is dinamically triggered and
     * booted {@link alice.tucson.service.ACCProxyNodeSide nodeProxy}
     *
     * @param tid
     *            Target TuCSoN tuplecentre id
     *            {@link TucsonTupleCentreIdDefault tid}
     * @param aid
     *            the agent identifier
     *
     * @return The open session toward the given target tuplecentre
     *
     * @throws UnreachableNodeException
     *             if the target tuple centre cannot be reached over the network
     *
     * @see alice.tucson.network.AbstractTucsonProtocol TucsonProtocol
     * @see alice.tucson.service.ACCProxyNodeSide ACCProxyNodeSide
     */
    public AbstractTucsonProtocol getSession(final TucsonTupleCentreIdDefault tid,
                                             final TucsonAgentIdDefault aid) throws UnreachableNodeException {
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
        AbstractTucsonProtocol dialog = null;
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
     * {@link alice.tucson.service.TucsonOpCompletionEvent event} to the
     * internal queue of pending completion event to process
     *
     * @param ev
     *            Completion Event to be added to pending queue
     *
     * @see alice.tucson.service.TucsonOpCompletionEvent TucsonOpCompletionEvent
     */
    protected void postEvent(final TucsonOpCompletionEvent ev) {
        // FIXME Check correctness
        synchronized (this.events) {
            this.events.add(this.events.size(), ev);
        }
    }
}
