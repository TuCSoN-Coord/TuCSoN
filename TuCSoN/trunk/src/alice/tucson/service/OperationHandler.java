package alice.tucson.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import alice.logictuple.LogicTuple;
import alice.respect.api.TupleCentreId;
import alice.tucson.api.ITucsonOperation;
import alice.tucson.api.TucsonAgentId;
import alice.tucson.api.TucsonOpId;
import alice.tucson.api.TucsonOperationCompletionListener;
import alice.tucson.api.TucsonTupleCentreId;
import alice.tucson.api.exceptions.TucsonInvalidTupleCentreIdException;
import alice.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tucson.api.exceptions.UnreachableNodeException;
import alice.tucson.network.AbstractTucsonProtocol;
import alice.tucson.network.TPFactory;
import alice.tucson.network.TucsonMsgReply;
import alice.tucson.network.TucsonMsgRequest;
import alice.tucson.network.exceptions.DialogException;
import alice.tuplecentre.api.Tuple;
import alice.tuplecentre.api.TupleTemplate;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;
import alice.tuplecentre.core.TCCycleResult.Outcome;
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
         * @param in
         */
        Controller(final AbstractTucsonProtocol d) {

            super();

            this.dialog = d;
            this.stop = false;
            this.setDaemon(true);

            final alice.tuprolog.lib.JavaLibrary jlib =
                    (alice.tuprolog.lib.JavaLibrary) this.p
                            .getLibrary("alice.tuprolog.lib.JavaLibrary");
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

                for (final Long operation : OperationHandler.this.operationExpired) {
                    OperationHandler.this.operations.remove(operation);
                }

                TucsonMsgReply msg = null;
                try {
                    msg = this.dialog.receiveMsgReply();
                } catch (final DialogException e) {
                    OperationHandler.this
                            .log("TuCSoN node service unavailable, nothing I can do");
                    this.setStop();
                    break;
                }

                final boolean ok = msg.isAllowed();
                if (ok) {

                    final int type = msg.getType();
                    if ((type == TucsonOperation.uinCode())
                            || (type == TucsonOperation.uinpCode())
                            || (type == TucsonOperation.urdCode())
                            || (type == TucsonOperation.urdpCode())
                            || (type == TucsonOperation.unoCode())
                            || (type == TucsonOperation.unopCode())
                            || (type == TucsonOperation.noCode())
                            || (type == TucsonOperation.noSCode())
                            || (type == TucsonOperation.nopCode())
                            || (type == TucsonOperation.nopSCode())
                            || (type == TucsonOperation.inCode())
                            || (type == TucsonOperation.rdCode())
                            || (type == TucsonOperation.inpCode())
                            || (type == TucsonOperation.rdpCode())
                            || (type == TucsonOperation.inSCode())
                            || (type == TucsonOperation.rdSCode())
                            || (type == TucsonOperation.inpSCode())
                            || (type == TucsonOperation.rdpSCode())) {

                        final boolean succeeded = msg.isSuccess();
                        if (succeeded) {

                            final LogicTuple tupleReq = msg.getTupleRequested();
                            final LogicTuple tupleRes =
                                    (LogicTuple) msg.getTupleResult();
                            // log("tupleReq="+tupleReq+", tupleRes="+tupleRes);
                            final LogicTuple res =
                                    this.unify(tupleReq, tupleRes);
                            ev =
                                    new TucsonOpCompletionEvent(new TucsonOpId(
                                            msg.getId()), ok, true, res);

                        } else {
                            ev =
                                    new TucsonOpCompletionEvent(new TucsonOpId(
                                            msg.getId()), ok, false);
                        }

                    } else if ((type == TucsonOperation.setCode())
                            || (type == TucsonOperation.setSCode())
                            || (type == TucsonOperation.outCode())
                            || (type == TucsonOperation.outSCode())
                            || (type == TucsonOperation.outAllCode())
                            || (type == TucsonOperation.spawnCode())
                            || (type == TucsonOperation.getEnvCode())
                            || (type == TucsonOperation.setEnvCode())) {
                        ev =
                                new TucsonOpCompletionEvent(new TucsonOpId(
                                        msg.getId()), ok, msg.isSuccess());
                    } else if ((type == TucsonOperation.inAllCode())
                            || (type == TucsonOperation.rdAllCode())
                            || (type == TucsonOperation.noAllCode())
                            || (type == TucsonOperation.getCode())
                            || (type == TucsonOperation.getSCode())) {
                        final List<LogicTuple> tupleSetRes =
                                (List<LogicTuple>) msg.getTupleResult();
                        ev =
                                new TucsonOpCompletionEvent(new TucsonOpId(
                                        msg.getId()), ok, msg.isSuccess(),
                                        tupleSetRes);
                    } else if (type == TucsonOperation.exitCode()) {
                        this.setStop();
                        break;
                    }

                } else {
                    ev =
                            new TucsonOpCompletionEvent(new TucsonOpId(
                                    msg.getId()), false, false);
                }

                final TucsonOperation op =
                        OperationHandler.this.operations.remove(msg.getId());
                if (op.isNoAll() || op.isInAll() || op.isRdAll() || op.isGet()
                        || op.isSet() || op.isGetS() || op.isSetS()
                        || op.isOutAll()) {
                    op.setLogicTupleListResult((List<LogicTuple>) msg
                            .getTupleResult());
                } else {
                    op.setTupleResult((LogicTuple) msg.getTupleResult());
                }
                if (msg.isResultSuccess()) {
                    op.setOpResult(Outcome.SUCCESS);
                } else {
                    op.setOpResult(Outcome.FAILURE);
                }
                op.notifyCompletion(ev.operationSucceeded(), msg.isAllowed());
                OperationHandler.this.postEvent(ev);

            }

        }

        /**
         * 
         */
        public synchronized void setStop() {
            this.stop = true;
        }

        /**
         * 
         * @return
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
        private LogicTuple
                unify(final TupleTemplate template, final Tuple tuple) {
            final boolean res = template.propagate(this.p, tuple);
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
     * Active sessions toward different nodes
     */
    protected Map<String, ControllerSession> controllerSessions;

    /**
     * TuCSoN requests completion events (node replies events)
     */
    protected List<TucsonOpCompletionEvent> events;
    /**
     * Expired TuCSoN operations
     */
    protected List<Long> operationExpired;

    /**
     * Requested TuCSoN operations
     */
    protected Map<Long, TucsonOperation> operations;

    /**
     * Current ACC session description
     */
    protected ACCDescription profile;

    /**
     * 
     */
    public OperationHandler() {
        this.profile = new ACCDescription();
        this.events = new LinkedList<TucsonOpCompletionEvent>();
        this.controllerSessions =
                new HashMap<String, OperationHandler.ControllerSession>();
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
     *            {@link alice.tucson.api.TucsonTupleCentreId tid}
     * @param t
     *            The Logic Tuple involved in the requested operation
     * @param ms
     *            Maximum waiting time tolerated by the callee TuCSoN Agent
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
     * @see alice.tucson.api.TucsonTupleCentreId TucsonTupleCentreId
     */
    public ITucsonOperation
            doBlockingOperation(final TucsonAgentId aid, final int type,
                    final Object tid, final LogicTuple t, final Long ms)
                    throws TucsonOperationNotPossibleException,
                    UnreachableNodeException, OperationTimeOutException {

        TucsonTupleCentreId tcid = null;
        if ("alice.tucson.api.TucsonTupleCentreId".equals(tid.getClass()
                .getName())) {
            tcid = (TucsonTupleCentreId) tid;
        } else if ("alice.respect.api.TupleCentreId".equals(tid.getClass()
                .getName())) {
            tcid = new TucsonTupleCentreId((TupleCentreId) tid);
        } else if ("java.lang.String".equals(tid.getClass().getName())) {
            try {
                tcid = new TucsonTupleCentreId((String) tid);
            } catch (final TucsonInvalidTupleCentreIdException ex) {
                System.err.println("[ACCProxyAgentSide]: " + ex);
                return null;
            }
        } else {
            throw new TucsonOperationNotPossibleException();
        }

        ITucsonOperation op = null;
        try {
            op = this.doOperation(aid, tcid, type, t, null);
        } catch (final TucsonOperationNotPossibleException e) {
            throw new TucsonOperationNotPossibleException();
        } catch (final UnreachableNodeException e) {
            throw new UnreachableNodeException();
        }
        try {
            if (ms == null) {
                op.waitForOperationCompletion();
            } else {
                op.waitForOperationCompletion(ms);
            }
        } catch (final OperationTimeOutException e) {
            throw new OperationTimeOutException();
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
     *            {@link alice.tucson.api.TucsonTupleCentreId tid}
     * @param t
     *            The Logic Tuple involved in the requested operation
     * @param l
     *            The listener who should be notified upon operation completion
     * 
     * @return An object representing the primitive invocation on the TuCSoN
     *         infrastructure which will store its result
     * 
     * @throws TucsonOperationNotPossibleException
     *             if the operation requested cannot be performed
     * @throws UnreachableNodeException
     *             if the target tuple centre cannot be reached over the network
     * 
     * @see alice.tucson.api.TucsonTupleCentreId TucsonTupleCentreId
     * @see alice.tucson.api.TucsonOperationCompletionListener
     *      TucsonOperationCompletionListener
     * @see alice.tucson.api.ITucsonOperation ITucsonOperation
     */
    public ITucsonOperation doNonBlockingOperation(final TucsonAgentId aid,
            final int type, final Object tid, final LogicTuple t,
            final TucsonOperationCompletionListener l)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException {

        // log("tid.class().name() = " + tid.getClass().getName());

        TucsonTupleCentreId tcid = null;
        if ("alice.tucson.api.TucsonTupleCentreId".equals(tid.getClass()
                .getName())) {
            tcid = (TucsonTupleCentreId) tid;
        } else if ("alice.respect.api.TupleCentreId".equals(tid.getClass()
                .getName())) {
            tcid = new TucsonTupleCentreId((TupleCentreId) tid);
            // log("tcid = " + tcid);
        } else if ("java.lang.String".equals(tid.getClass().getName())) {
            try {
                tcid = new TucsonTupleCentreId((String) tid);
            } catch (final TucsonInvalidTupleCentreIdException ex) {
                System.err.println("[ACCProxyAgentSide]: " + ex);
                return null;
            }
        } else {
            throw new TucsonOperationNotPossibleException();
        }

        try {
            return this.doOperation(aid, tcid, type, t, l);
        } catch (final TucsonOperationNotPossibleException e) {
            throw new TucsonOperationNotPossibleException();
        } catch (final UnreachableNodeException e) {
            throw new UnreachableNodeException();
        }

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

    /**
     * This method is the real responsible of TuCSoN operations execution.
     * 
     * First, it takes the target tuplecentre and checks wether this proxy has
     * ever established a connection toward it: if it did, the already opened
     * connection is retrieved and used, otherwise a new connection is opened
     * and stored for later use
     * {@link alice.tucson.service.OperationHandler#getSession getSession}.
     * 
     * Then, a Tucson Operation {@link alice.tucson.service.TucsonOperation op}
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
     *            {@link alice.tucson.api.TucsonTupleCentreId tid}
     * @param type
     *            TuCSoN operation type (internal integer code)
     * @param t
     *            The Logic Tuple involved in the requested operation
     * @param l
     *            The listener who should be notified upon operation completion
     * 
     * @return An object representing the primitive invocation on the TuCSoN
     *         infrastructure which will store its result
     * 
     * @throws TucsonOperationNotPossibleException
     *             if the operation requested cannot be performed
     * @throws UnreachableNodeException
     *             if the target tuple centre cannot be reached over the network
     * 
     * @see alice.tucson.api.TucsonTupleCentreId TucsonTupleCentreId
     * @see alice.tucson.api.TucsonOperationCompletionListener
     *      TucsonOperationCompletionListener
     * @see alice.tucson.api.ITucsonOperation ITucsonOperation
     * @see alice.tucson.service.TucsonOperation TucsonOperation
     */
    protected synchronized ITucsonOperation doOperation(
            final TucsonAgentId aid, final TucsonTupleCentreId tcid,
            final int type, final LogicTuple t,
            final TucsonOperationCompletionListener l)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException {

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
                throw new UnreachableNodeException();
            }

            TucsonOperation op = null;
            if ((type == TucsonOperation.outCode())
                    || (type == TucsonOperation.outSCode())
                    || (type == TucsonOperation.setSCode())
                    || (type == TucsonOperation.setCode())
                    || (type == TucsonOperation.outAllCode())
                    || (type == TucsonOperation.spawnCode())) {
                op = new TucsonOperation(type, (Tuple) t, l, this);
            } else {
                op = new TucsonOperation(type, t, l, this);
            }
            this.operations.put(op.getId(), op);
            final TucsonMsgRequest msg =
                    new TucsonMsgRequest(op.getId(), op.getType(),
                            tcid.toString(), op.getLogicTupleArgument());
            this.log("requesting op " + msg.getType() + ", " + msg.getTuple()
                    + ", " + msg.getTid());

            try {
                session.sendMsgRequest(msg);
            } catch (final DialogException ex) {
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
     *            {@link alice.tucson.api.TucsonTupleCentreId tid}
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
    protected AbstractTucsonProtocol getSession(final TucsonTupleCentreId tid,
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
                    this.controllerSessions.get("127.0.0.1:".concat(String
                            .valueOf(p)));
        }
        // if (InetAddress.getLoopbackAddress().getHostAddress().equals(opNode))
        // {
        if ("127.0.0.1".equals(opNode)) {
            tc =
            // this.controllerSessions.get(InetAddress
            // .getLoopbackAddress().getHostName()
            // .concat(String.valueOf(p)));
                    this.controllerSessions.get("localhost:".concat(String
                            .valueOf(p)));
        }
        if (tc != null) {
            return tc.getSession();
        }

        this.profile.setProperty("agent-identity", aid.toString());
        this.profile.setProperty("agent-role", "user");

        AbstractTucsonProtocol dialog = null;
        boolean isEnterReqAcpt = false;
        try {
            dialog = TPFactory.getDialogAgentSide(tid);
            dialog.sendEnterRequest(this.profile);
            dialog.receiveEnterRequestAnswer();
            if (dialog.isEnterRequestAccepted()) {
                isEnterReqAcpt = true;
            }
        } catch (final IOException e) {
            throw new UnreachableNodeException();
        } catch (final DialogException e) {
            // TODO Auto-generated catch block
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
}
