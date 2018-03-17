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

import java.lang.invoke.MethodHandles;
import java.util.*;

import alice.tuple.Tuple;
import alice.tuple.TupleTemplate;
import alice.tuple.logic.LogicTuple;
import alice.tuplecentre.api.ITCCycleResult;
import alice.tuplecentre.api.OperationIdentifier;
import alice.tuplecentre.api.TupleCentreIdentifier;
import alice.tuplecentre.core.AbstractTupleCentreOperation;
import alice.tuplecentre.core.OperationCompletionListener;
import alice.tuplecentre.core.TupleCentreOpType;
import alice.tuplecentre.respect.api.geolocation.Position;
import alice.tuplecentre.tucson.api.TucsonOpId;
import alice.tuplecentre.tucson.api.TucsonTupleCentreId;
import alice.tuplecentre.tucson.api.TucsonTupleCentreIdDefault;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidTupleCentreIdException;
import alice.tuplecentre.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tuplecentre.tucson.api.exceptions.UnreachableNodeException;
import alice.tuplecentre.tucson.network.TucsonProtocol;
import alice.tuplecentre.tucson.network.TucsonProtocolTCP;
import alice.tuplecentre.tucson.network.exceptions.DialogException;
import alice.tuplecentre.tucson.network.exceptions.DialogInitializationException;
import alice.tuplecentre.tucson.network.messages.TucsonMessageReply;
import alice.tuplecentre.tucson.network.messages.TucsonMessageRequest;
import alice.tuplecentre.tucson.network.messages.events.InputEventMessageDefault;
import alice.tuplecentre.tucson.network.messages.events.OutputEventMessage;
import alice.tuprolog.Prolog;
import alice.tuprolog.lib.InvalidObjectIdException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ste (mailto: s.mariani@unibo.it)
 */
public class InterTupleCentreACCProxy implements InterTupleCentreACC, OperationCompletionListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().getClass());

    /**
     *
     */
    class Controller extends Thread {

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
                // Cannot happen, the object name it's specified here
                LOGGER.error(e.getMessage(), e);
            }
        }

        @Override
        public void run() {
            TucsonOpCompletionEvent ev = null;
            label:
            while (!this.isStopped()) {
                TucsonMessageReply msg;
                try {
                    msg = this.dialog.receiveMsgReply();
                } catch (final DialogException e) {
                    InterTupleCentreACCProxy
                            .log("TuCSoN node service unavailable, nothing I can do");
                    this.setStop();
                    break;
                }
                final OutputEventMessage oEv = msg.getEventMsg();
                final boolean ok = oEv.isAllowed();
                if (ok) {
                    final TupleCentreOpType type = oEv.getOpType();
                    switch (type) {
                        case NO:
                        case NO_S:
                        case NOP:
                        case NOP_S:
                        case IN:
                        case RD:
                        case INP:
                        case RDP:
                        case UIN:
                        case URD:
                        case UINP:
                        case URDP:
                        case UNO:
                        case UNOP:
                        case IN_S:
                        case RD_S:
                        case INP_S:
                        case RDP_S:
                            final boolean succeeded = oEv.isSuccess();
                            if (succeeded) {
                                final LogicTuple tupleReq = oEv.getTuple();
                                final LogicTuple tupleRes = (LogicTuple) oEv
                                        .getTupleResult();
                                final LogicTuple res = this.unify(tupleReq,
                                        tupleRes);
                                ev = new TucsonOpCompletionEvent(
                                        oEv.getOpId(), true, true, oEv.isResultSuccess(), res);
                            } else {
                                ev = new TucsonOpCompletionEvent(
                                        oEv.getOpId(), true, false, oEv.isResultSuccess());
                            }
                            break;
                        case SET:
                        case SET_S:
                        case OUT:
                        case OUT_S:
                        case OUT_ALL:
                        case SPAWN:
                            ev = new TucsonOpCompletionEvent(
                                    oEv.getOpId(), true, oEv.isSuccess(), oEv.isResultSuccess());
                            break;
                        case IN_ALL:
                        case RD_ALL:
                        case NO_ALL:
                        case GET:
                        case GET_S:
                            final List<LogicTuple> tupleSetRes = (List<LogicTuple>) oEv
                                    .getTupleResult();
                            ev = new TucsonOpCompletionEvent(
                                    oEv.getOpId(), true, oEv.isSuccess(), oEv.isResultSuccess(),
                                    tupleSetRes);
                            break;
                        case EXIT:
                            this.setStop();
                            break label;
                    }
                } else {
                    ev = new TucsonOpCompletionEvent(
                            oEv.getOpId(), false, false, oEv.isResultSuccess());
                }
                final AbstractTupleCentreOperation op = InterTupleCentreACCProxy.this.operations
                        .remove(oEv.getOpId());
                if (op.getType() == TupleCentreOpType.NO_ALL || op.getType() == TupleCentreOpType.IN_ALL || op.getType() == TupleCentreOpType.RD_ALL || op.getType() == TupleCentreOpType.GET
                        || op.getType() == TupleCentreOpType.SET || op.getType() == TupleCentreOpType.GET_S || op.getType() == TupleCentreOpType.SET_S
                        || op.getType() == TupleCentreOpType.OUT_ALL) {
                    InterTupleCentreACCProxy.log("received completion msg "
                            + oEv.getOpId() + ", op " + op.getType() + ", "
                            + op.getTupleListResult());
                    op.setTupleListResult((List<Tuple>) oEv.getTupleResult());
                } else {
                    InterTupleCentreACCProxy.log("received completion msg "
                            + oEv.getOpId() + ", op " + op.getType() + ", "
                            + op.getTupleResult());
                    op.setTupleResult((LogicTuple) oEv.getTupleResult());
                }
                if (oEv.isResultSuccess()) {
                    op.setOpResult(ITCCycleResult.Outcome.SUCCESS);
                } else {
                    op.setOpResult(ITCCycleResult.Outcome.FAILURE);
                }
                op.notifyCompletion();
                InterTupleCentreACCProxy.this.postEvent(ev);
            }
        }

        private synchronized boolean isStopped() {
            return this.stop;
        }

        private synchronized void setStop() {
            this.stop = true;
        }

        private LogicTuple unify(final TupleTemplate template, final Tuple tuple) {
            final boolean res = template.propagate(tuple);
            if (res) {
                return (LogicTuple) template;
            }
            return null;
        }
    }

    class ControllerSession {

        private final Controller controller;
        private final TucsonProtocol session;

        ControllerSession(final Controller c, final TucsonProtocol s) {
            this.controller = c;
            this.session = s;
        }

        public Controller getController() {
            return this.controller;
        }

        public TucsonProtocol getSession() {
            return this.session;
        }
    }

    private static final int TRIES = 3;
    // aid is the source tuple centre Identifier
    private TucsonTupleCentreId aid;
    private final Map<String, ControllerSession> controllerSessions;
    private final List<TucsonOpCompletionEvent> events;
    private final Map<OperationIdentifier, AbstractTupleCentreOperation> operations;
    private long opId;
    private final ACCDescription profile;
    /**
     * Current ACC position
     */
    protected Position place;

    /**
     * @param id tuplecentre source
     * @throws TucsonInvalidTupleCentreIdException if the given Object is not a valid identifier of a tuple
     *                                             centre
     */
    public InterTupleCentreACCProxy(final Object id)
            throws TucsonInvalidTupleCentreIdException {
        switch (id.getClass()
                .getName()) {
            case "alice.tuplecentre.tucson.api.TucsonTupleCentreIdDefault":
                this.aid = (TucsonTupleCentreId) id;
                break;
            case "java.lang.String":
                this.aid = new TucsonTupleCentreIdDefault((String) id);
                break;
            default:
                throw new TucsonInvalidTupleCentreIdException();
        }
        this.profile = new ACCDescription();
        this.events = new LinkedList<>();
        this.controllerSessions = new HashMap<>();
        this.operations = new HashMap<>();
        this.opId = -1;
        this.setPosition();
    }

    @Override
    public synchronized void doOperation(final Object tid,
                                         final AbstractTupleCentreOperation op)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException {
        TucsonTupleCentreId tcid = null;
        switch (tid.getClass().getName()) {
            case "alice.tuplecentre.respect.api.TupleCentreId":
                final TupleCentreIdentifier id = (TupleCentreIdentifier) tid;
                try {
                    tcid = new TucsonTupleCentreIdDefault(id.getLocalName(), id.getNode(),
                            String.valueOf(id.getPort()));
                } catch (final TucsonInvalidTupleCentreIdException e) {
                    LOGGER.error(e.getMessage(), e);
                }
                break;
            case "alice.tuplecentre.tucson.api.TucsonTupleCentreIdDefault":
                tcid = (TucsonTupleCentreId) tid;
                break;
            case "java.lang.String":
                try {
                    tcid = new TucsonTupleCentreIdDefault((String) tid);
                } catch (final TucsonInvalidTupleCentreIdException e) {
                    throw new TucsonOperationNotPossibleException();
                }
                break;
            default:
                // DEBUG
                LOGGER.debug("Invalid Class: " + tid.getClass().getName());
                throw new TucsonOperationNotPossibleException();
        }
        int nTry = 0;
        boolean exception;
        do {
            this.opId++;
            nTry++;
            exception = false;
            TucsonProtocol session = null;
            try {
                session = this.getSession(Objects.requireNonNull(tcid));
            } catch (final UnreachableNodeException ex2) {
                throw new UnreachableNodeException();
            } catch (DialogInitializationException e) {
                LOGGER.error(e.getMessage(), e);
            }
            final OperationIdentifier tucsonOpId = new TucsonOpId(this.opId);
            this.operations.put(tucsonOpId, op);
            final TupleCentreOpType type = op.getType();
            TucsonMessageRequest msg;
            if (type == TupleCentreOpType.OUT
                    || type == TupleCentreOpType.OUT_S
                    || type == TupleCentreOpType.SET_S
                    || type == TupleCentreOpType.SET
                    || type == TupleCentreOpType.OUT_ALL
                    || type == TupleCentreOpType.SPAWN) {
                msg = new TucsonMessageRequest(new InputEventMessageDefault(
                        this.aid.toString(), tucsonOpId, type,
                        (LogicTuple) op.getTupleArgument(), tcid.toString(),
                        System.currentTimeMillis(), this.getPosition()));
                // new TucsonMessageRequest(this.opId, type, tcid.toString(),
                // (LogicTuple) op.getTupleArgument());
            } else {
                msg = new TucsonMessageRequest(new InputEventMessageDefault(
                        this.aid.toString(), tucsonOpId, type,
                        (LogicTuple) op.getTemplateArgument(), tcid.toString(),
                        System.currentTimeMillis(), this.getPosition()));
                // new TucsonMessageRequest(this.opId, type, tcid.toString(),
                // (LogicTuple) op.getTemplateArgument());
            }
            InterTupleCentreACCProxy.log("sending msg "
                    + msg.getEventMsg().getOpId() + ", op = "
                    + msg.getEventMsg().getOpType() + ", "
                    + msg.getEventMsg().getTuple() + ", "
                    + msg.getEventMsg().getTarget());
            try {
                Objects.requireNonNull(session).sendMsgRequest(msg);
            } catch (final DialogException e) {
                exception = true;
                LOGGER.error(e.getMessage(), e);
            }
            if (!exception) {
                return;
            }
        } while (nTry < InterTupleCentreACCProxy.TRIES);
        throw new UnreachableNodeException();
    }

    /**
     * @return the Position of the tuple centre behind this proxy
     */
    public Position getPosition() {
        return this.place;
    }

    @Override
    public void operationCompleted(final AbstractTupleCentreOperation op) {
        // FIXME What to do here?
    }

    @Override
    public TucsonOpCompletionEvent waitForCompletion(final OperationIdentifier id) {
        try {
            synchronized (this.events) {
                TucsonOpCompletionEvent ev = this.findEvent(id);
                while (ev == null) {
                    this.events.wait();
                    ev = this.findEvent(id);
                }
                return ev;
            }
        } catch (final InterruptedException ex) {
            return null;
        }
    }

    @Override
    public TucsonOpCompletionEvent waitForCompletion(final OperationIdentifier id,
                                                     final int timeout) {
        try {
            final long startTime = System.currentTimeMillis();
            synchronized (this.events) {
                long dt = System.currentTimeMillis() - startTime;
                TucsonOpCompletionEvent ev = this.findEvent(id);
                while (ev == null && dt < timeout) {
                    this.events.wait(timeout - dt);
                    ev = this.findEvent(id);
                    dt = System.currentTimeMillis() - startTime;
                }
                return ev;
            }
        } catch (final InterruptedException e) {
            return null;
        }
    }

    private void err(final String msg) {
        LOGGER.error("..[InterTupleCentreACCProxy ("
                + this.profile.getProperty("tc-identity") + ")]: " + msg);
    }

    private TucsonOpCompletionEvent findEvent(final OperationIdentifier id) {
        final Iterator<TucsonOpCompletionEvent> it = this.events.iterator();
        while (it.hasNext()) {
            final TucsonOpCompletionEvent ev = it.next();
            if (ev.getOpId().equals(id)) {
                it.remove();
                return ev;
            }
        }
        return null;
    }

    private TucsonProtocol getSession(final TucsonTupleCentreId tid)
            throws UnreachableNodeException, DialogInitializationException {
        final String opNode = alice.util.Tools.removeApices(tid.getNode());
        final int port = tid.getPort();
        ControllerSession tc = this.controllerSessions.get(opNode + ":" + port);
        if (tc != null) {
            return tc.getSession();
        }
        // if (InetAddress.getLoopbackAddress().getHostName().equals(opNode)) {
        if ("localhost".equals(opNode)) {
            tc =
                    // this.controllerSessions.get(InetAddress
                    // .getLoopbackAddress().getHostAddress()
                    // .concat(String.valueOf(p)));
                    this.controllerSessions
                            .get("127.0.0.1".concat(String.valueOf(port)));
        }
        // if (InetAddress.getLoopbackAddress().getHostAddress().equals(opNode))
        // {
        if ("127.0.0.1".equals(opNode)) {
            tc =
                    // this.controllerSessions.get(InetAddress
                    // .getLoopbackAddress().getHostName()
                    // .concat(String.valueOf(p)));
                    this.controllerSessions
                            .get("localhost".concat(String.valueOf(port)));
        }
        if (tc != null) {
            return tc.getSession();
        }
        this.profile.setProperty("tc-identity", this.aid.toString());
        this.profile.setProperty("agent-role", "user");
        TucsonProtocol dialog;
        boolean isEnterReqAcpt = false;
        dialog = new TucsonProtocolTCP(opNode, port);
        try {
            dialog.sendEnterRequest(this.profile);
            dialog.receiveEnterRequestAnswer();
        } catch (final DialogException e) {
            LOGGER.error(e.getMessage(), e);
        }
        if (dialog.isEnterRequestAccepted()) {
            isEnterReqAcpt = true;
        }
        if (isEnterReqAcpt) {
            final Controller contr = new Controller(dialog);
            final ControllerSession cs = new ControllerSession(contr, dialog);
            this.controllerSessions.put(opNode + ":" + port, cs);
            contr.start();
            return dialog;
        }
        return null;
    }

    private static void log(final String msg) {
        LOGGER.info("[InterTupleCentreACCProxy]: " + msg);
    }

    private void postEvent(final TucsonOpCompletionEvent ev) {
        synchronized (this.events) {
            this.events.add(this.events.size(), ev);
            this.events.notifyAll();
        }
    }

    private void setPosition() {
        this.place = new Position();
    }
}
