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

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import alice.logictuple.LogicTuple;
import alice.respect.api.TupleCentreId;
import alice.respect.api.geolocation.Position;
import alice.tucson.api.TucsonOpId;
import alice.tucson.api.TucsonTupleCentreId;
import alice.tucson.api.exceptions.TucsonInvalidTupleCentreIdException;
import alice.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tucson.api.exceptions.UnreachableNodeException;
import alice.tucson.network.AbstractTucsonProtocol;
import alice.tucson.network.messages.events.InputEventMsg;
import alice.tucson.network.messages.events.OutputEventMsg;
import alice.tucson.network.messages.TucsonMsgReply;
import alice.tucson.network.messages.TucsonMsgRequest;
import alice.tucson.network.TucsonProtocolTCP;
import alice.tucson.network.exceptions.DialogException;
import alice.tucson.network.exceptions.DialogInitializationException;
import alice.tuplecentre.api.ITCCycleResult;
import alice.tuplecentre.api.Tuple;
import alice.tuplecentre.api.TupleTemplate;
import alice.tuplecentre.core.AbstractTupleCentreOperation;
import alice.tuplecentre.core.OperationCompletionListener;
import alice.tuplecentre.core.TupleCentreOpType;
import alice.tuprolog.Prolog;
import alice.tuprolog.lib.InvalidObjectIdException;

/**
 *
 * @author ste (mailto: s.mariani@unibo.it)
 *
 */
public class InterTupleCentreACCProxy implements InterTupleCentreACC,
OperationCompletionListener {

    /**
     *
     */
    class Controller extends Thread {

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
                // Cannot happen, the object name it's specified here
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            TucsonOpCompletionEvent ev = null;
            while (!this.isStopped()) {
                TucsonMsgReply msg = null;
                try {
                    msg = this.dialog.receiveMsgReply();
                } catch (final DialogException e) {
                    InterTupleCentreACCProxy
                            .log("TuCSoN node service unavailable, nothing I can do");
                    this.setStop();
                    break;
                }
                final OutputEventMsg oEv = msg.getEventMsg();
                final boolean ok = oEv.isAllowed();
                if (ok) {
                    final TupleCentreOpType type = oEv.getOpType();
                    if (type == TupleCentreOpType.NO
                            || type == TupleCentreOpType.NO_S
                            || type == TupleCentreOpType.NOP
                            || type == TupleCentreOpType.NOP_S
                            || type == TupleCentreOpType.IN
                            || type == TupleCentreOpType.RD
                            || type == TupleCentreOpType.INP
                            || type == TupleCentreOpType.RDP
                            || type == TupleCentreOpType.UIN
                            || type == TupleCentreOpType.URD
                            || type == TupleCentreOpType.UINP
                            || type == TupleCentreOpType.URDP
                            || type == TupleCentreOpType.UNO
                            || type == TupleCentreOpType.UNOP
                            || type == TupleCentreOpType.IN_S
                            || type == TupleCentreOpType.RD_S
                            || type == TupleCentreOpType.INP_S
                            || type == TupleCentreOpType.RDP_S) {
                        final boolean succeeded = oEv.isSuccess();
                        if (succeeded) {
                            final LogicTuple tupleReq = oEv.getTupleRequested();
                            final LogicTuple tupleRes = (LogicTuple) oEv
                                    .getTupleResult();
                            final LogicTuple res = this.unify(tupleReq,
                                    tupleRes);
                            ev = new TucsonOpCompletionEvent(new TucsonOpId(
                                    oEv.getOpId()), ok, true, oEv.isResultSuccess(), res);
                        } else {
                            ev = new TucsonOpCompletionEvent(new TucsonOpId(
                                    oEv.getOpId()), ok, false, oEv.isResultSuccess());
                        }
                    } else if (type == TupleCentreOpType.SET
                            || type == TupleCentreOpType.SET_S
                            || type == TupleCentreOpType.OUT
                            || type == TupleCentreOpType.OUT_S
                            || type == TupleCentreOpType.OUT_ALL
                            || type == TupleCentreOpType.SPAWN) {
                        ev = new TucsonOpCompletionEvent(new TucsonOpId(
                                oEv.getOpId()), ok, oEv.isSuccess(), oEv.isResultSuccess());
                    } else if (type == TupleCentreOpType.IN_ALL
                            || type == TupleCentreOpType.RD_ALL
                            || type == TupleCentreOpType.NO_ALL
                            || type == TupleCentreOpType.GET
                            || type == TupleCentreOpType.GET_S) {
                        final List<LogicTuple> tupleSetRes = (List<LogicTuple>) oEv
                                .getTupleResult();
                        ev = new TucsonOpCompletionEvent(new TucsonOpId(
                                oEv.getOpId()), ok, oEv.isSuccess(), oEv.isResultSuccess(),
                                tupleSetRes);
                    } else if (type == TupleCentreOpType.EXIT) {
                        this.setStop();
                        break;
                    }
                } else {
                    ev = new TucsonOpCompletionEvent(new TucsonOpId(
                            oEv.getOpId()), false, false, oEv.isResultSuccess());
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
        private final AbstractTucsonProtocol session;

        ControllerSession(final Controller c, final AbstractTucsonProtocol s) {
            this.controller = c;
            this.session = s;
        }

        public Controller getController() {
            return this.controller;
        }

        public AbstractTucsonProtocol getSession() {
            return this.session;
        }
    }

    private static final int TRIES = 3;
    // aid is the source tuple centre ID
    private TucsonTupleCentreId aid;
    private final Map<String, ControllerSession> controllerSessions;
    private final List<TucsonOpCompletionEvent> events;
    private final Map<Long, AbstractTupleCentreOperation> operations;
    private long opId;
    private final ACCDescription profile;
    /**
     * Current ACC position
     */
    protected Position place;

    /**
     *
     * @param id
     *            tuplecentre source
     * @throws TucsonInvalidTupleCentreIdException
     *             if the given Object is not a valid identifier of a tuple
     *             centre
     */
    public InterTupleCentreACCProxy(final Object id)
            throws TucsonInvalidTupleCentreIdException {
        if ("alice.tucson.api.TucsonTupleCentreId".equals(id.getClass()
                .getName())) {
            this.aid = (TucsonTupleCentreId) id;
        } else if ("java.lang.String".equals(id.getClass().getName())) {
            this.aid = new TucsonTupleCentreId((String) id);
        } else {
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
    public synchronized TucsonOpId doOperation(final Object tid,
            final AbstractTupleCentreOperation op)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException {
        TucsonTupleCentreId tcid = null;
        if ("alice.respect.api.TupleCentreId".equals(tid.getClass().getName())) {
            final TupleCentreId id = (TupleCentreId) tid;
            try {
                tcid = new TucsonTupleCentreId(id.getName(), id.getNode(),
                        String.valueOf(id.getPort()));
            } catch (final TucsonInvalidTupleCentreIdException e) {
                e.printStackTrace();
            }
        } else if ("alice.tucson.api.TucsonTupleCentreId".equals(tid.getClass()
                .getName())) {
            tcid = (TucsonTupleCentreId) tid;
        } else if ("java.lang.String".equals(tid.getClass().getName())) {
            try {
                tcid = new TucsonTupleCentreId((String) tid);
            } catch (final TucsonInvalidTupleCentreIdException e) {
                throw new TucsonOperationNotPossibleException();
            }
        } else {
            // DEBUG
            System.err.println("Invalid Class: " + tid.getClass().getName());
            throw new TucsonOperationNotPossibleException();
        }
        int nTry = 0;
        boolean exception;
        do {
            this.opId++;
            nTry++;
            exception = false;
            AbstractTucsonProtocol session = null;
            try {
                session = this.getSession(tcid);
            } catch (final UnreachableNodeException ex2) {
                exception = true;
                throw new UnreachableNodeException();
            } catch (DialogInitializationException e) {
				e.printStackTrace();
			}
            this.operations.put(this.opId, op);
            final TupleCentreOpType type = op.getType();
            TucsonMsgRequest msg;
            if (type == TupleCentreOpType.OUT
                    || type == TupleCentreOpType.OUT_S
                    || type == TupleCentreOpType.SET_S
                    || type == TupleCentreOpType.SET
                    || type == TupleCentreOpType.OUT_ALL
                    || type == TupleCentreOpType.SPAWN) {
                msg = new TucsonMsgRequest(new InputEventMsg(
                        this.aid.toString(), this.opId, type,
                        (LogicTuple) op.getTupleArgument(), tcid.toString(),
                        System.currentTimeMillis(), this.getPosition()));
                // new TucsonMsgRequest(this.opId, type, tcid.toString(),
                // (LogicTuple) op.getTupleArgument());
            } else {
                msg = new TucsonMsgRequest(new InputEventMsg(
                        this.aid.toString(), this.opId, type,
                        (LogicTuple) op.getTemplateArgument(), tcid.toString(),
                        System.currentTimeMillis(), this.getPosition()));
                // new TucsonMsgRequest(this.opId, type, tcid.toString(),
                // (LogicTuple) op.getTemplateArgument());
            }
            InterTupleCentreACCProxy.log("sending msg "
                    + msg.getEventMsg().getOpId() + ", op = "
                    + msg.getEventMsg().getOpType() + ", "
                    + msg.getEventMsg().getTuple() + ", "
                    + msg.getEventMsg().getTarget());
            try {
                session.sendMsgRequest(msg);
            } catch (final DialogException e) {
                exception = true;
                e.printStackTrace();
            }
            if (!exception) {
                return new TucsonOpId(this.opId);
            }
        } while (nTry < InterTupleCentreACCProxy.TRIES);
        throw new UnreachableNodeException();
    }
    
    /**
     * 
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
    public TucsonOpCompletionEvent waitForCompletion(final TucsonOpId id) {
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
    public TucsonOpCompletionEvent waitForCompletion(final TucsonOpId id,
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
        System.err.println("..[InterTupleCentreACCProxy ("
                + this.profile.getProperty("tc-identity") + ")]: " + msg);
    }

    private TucsonOpCompletionEvent findEvent(final TucsonOpId id) {
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

    private AbstractTucsonProtocol getSession(final TucsonTupleCentreId tid)
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
        AbstractTucsonProtocol dialog = null;
        boolean isEnterReqAcpt = false;
        dialog = new TucsonProtocolTCP(opNode, port);
        try {
            dialog.sendEnterRequest(this.profile);
            dialog.receiveEnterRequestAnswer();
        } catch (final DialogException e) {
            e.printStackTrace();
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
        System.out.println("[InterTupleCentreACCProxy]: " + msg);
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
