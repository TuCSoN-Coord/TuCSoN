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
import alice.tucson.api.TucsonOpId;
import alice.tucson.api.TucsonTupleCentreId;
import alice.tucson.api.exceptions.TucsonInvalidTupleCentreIdException;
import alice.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tucson.api.exceptions.UnreachableNodeException;
import alice.tucson.network.AbstractTucsonProtocol;
import alice.tucson.network.TucsonMsgReply;
import alice.tucson.network.TucsonMsgRequest;
import alice.tucson.network.TucsonProtocolTCP;
import alice.tucson.network.exceptions.DialogException;
import alice.tucson.network.exceptions.DialogInitializationException;
import alice.tucson.network.exceptions.DialogReceiveException;
import alice.tucson.network.exceptions.DialogSendException;
import alice.tuplecentre.api.Tuple;
import alice.tuplecentre.api.TupleTemplate;
import alice.tuplecentre.core.AbstractTupleCentreOperation;
import alice.tuplecentre.core.OperationCompletionListener;
import alice.tuplecentre.core.TCCycleResult.Outcome;
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
         * @param input
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
                } catch (final DialogReceiveException e) {
                    InterTupleCentreACCProxy.this
                    .err("TuCSoN Node disconnected unexpectedly :/");
                    this.setStop();
                    break;
                }
                final boolean ok = msg.isAllowed();
                if (ok) {
                    final int type = msg.getType();
                    if (type == TucsonOperation.noCode()
                            || type == TucsonOperation.noSCode()
                            || type == TucsonOperation.nopCode()
                            || type == TucsonOperation.nopSCode()
                            || type == TucsonOperation.inCode()
                            || type == TucsonOperation.rdCode()
                            || type == TucsonOperation.inpCode()
                            || type == TucsonOperation.rdpCode()
                            || type == TucsonOperation.uinCode()
                            || type == TucsonOperation.urdCode()
                            || type == TucsonOperation.uinpCode()
                            || type == TucsonOperation.urdpCode()
                            || type == TucsonOperation.unoCode()
                            || type == TucsonOperation.unopCode()
                            || type == TucsonOperation.inSCode()
                            || type == TucsonOperation.rdSCode()
                            || type == TucsonOperation.inpSCode()
                            || type == TucsonOperation.rdpSCode()) {
                        final boolean succeeded = msg.isSuccess();
                        if (succeeded) {
                            final LogicTuple tupleReq = msg.getTupleRequested();
                            final LogicTuple tupleRes = (LogicTuple) msg
                                    .getTupleResult();
                            final LogicTuple res = this.unify(tupleReq,
                                    tupleRes);
                            ev = new TucsonOpCompletionEvent(new TucsonOpId(
                                    msg.getId()), ok, true,
                                    msg.isResultSuccess(), res);
                        } else {
                            ev = new TucsonOpCompletionEvent(new TucsonOpId(
                                    msg.getId()), ok, false,
                                    msg.isResultSuccess());
                        }
                    } else if (type == TucsonOperation.setCode()
                            || type == TucsonOperation.setSCode()
                            || type == TucsonOperation.outCode()
                            || type == TucsonOperation.outSCode()
                            || type == TucsonOperation.outAllCode()
                            || type == TucsonOperation.spawnCode()) {
                        ev = new TucsonOpCompletionEvent(new TucsonOpId(
                                msg.getId()), ok, msg.isSuccess(),
                                msg.isResultSuccess());
                    } else if (type == TucsonOperation.inAllCode()
                            || type == TucsonOperation.rdAllCode()
                            || type == TucsonOperation.noAllCode()
                            || type == TucsonOperation.getCode()
                            || type == TucsonOperation.getSCode()) {
                        final List<LogicTuple> tupleSetRes = (List<LogicTuple>) msg
                                .getTupleResult();
                        ev = new TucsonOpCompletionEvent(new TucsonOpId(
                                msg.getId()), ok, msg.isSuccess(),
                                msg.isResultSuccess(), tupleSetRes);
                    } else if (type == TucsonOperation.exitCode()) {
                        this.setStop();
                        break;
                    }
                } else {
                    ev = new TucsonOpCompletionEvent(
                            new TucsonOpId(msg.getId()), false, false,
                            msg.isResultSuccess());
                }
                final AbstractTupleCentreOperation op = InterTupleCentreACCProxy.this.operations
                        .remove(msg.getId());
                if (op.isNoAll() || op.isInAll() || op.isRdAll() || op.isGet()
                        || op.isSet() || op.isGetS() || op.isSetS()
                        || op.isOutAll()) {
                    InterTupleCentreACCProxy.this
                    .log("received completion msg " + msg.getId()
                            + ", op " + op.getType() + ", "
                            + op.getTupleListResult());
                    op.setTupleListResult((List<Tuple>) msg.getTupleResult());
                } else {
                    InterTupleCentreACCProxy.this
                    .log("received completion msg " + msg.getId()
                            + ", op " + op.getType() + ", "
                            + op.getTupleResult());
                    op.setTupleResult((LogicTuple) msg.getTupleResult());
                }
                if (msg.isResultSuccess()) {
                    op.setOpResult(Outcome.SUCCESS);
                } else {
                    op.setOpResult(Outcome.FAILURE);
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
        this.events = new LinkedList<TucsonOpCompletionEvent>();
        this.controllerSessions = new HashMap<String, ControllerSession>();
        this.operations = new HashMap<Long, AbstractTupleCentreOperation>();
        this.opId = -1;
    }

    @Override
    public synchronized TucsonOpId doOperation(final Object tid,
            final AbstractTupleCentreOperation op)
                    throws TucsonOperationNotPossibleException,
                    UnreachableNodeException, TucsonInvalidTupleCentreIdException {
        TucsonTupleCentreId tcid = null;
        if ("alice.respect.api.TupleCentreId".equals(tid.getClass().getName())) {
            final TupleCentreId id = (TupleCentreId) tid;
            tcid = new TucsonTupleCentreId(id.getName(), id.getNode(),
                    String.valueOf(id.getPort()));
        } else if ("alice.tucson.api.TucsonTupleCentreId".equals(tid.getClass()
                .getName())) {
            tcid = (TucsonTupleCentreId) tid;
        } else if ("java.lang.String".equals(tid.getClass().getName())) {
            tcid = new TucsonTupleCentreId((String) tid);
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
            } catch (final DialogInitializationException e) {
                exception = true;
                e.printStackTrace();
            } catch (final UnreachableNodeException e) {
                exception = true;
                throw new UnreachableNodeException(e);
            }
            this.operations.put(this.opId, op);
            final int type = op.getType();
            TucsonMsgRequest msg;
            if (type == TucsonOperation.outCode()
                    || type == TucsonOperation.outSCode()
                    || type == TucsonOperation.setSCode()
                    || type == TucsonOperation.setCode()
                    || type == TucsonOperation.outAllCode()
                    || type == TucsonOperation.spawnCode()) {
                msg = new TucsonMsgRequest(this.opId, type, tcid.toString(),
                        (LogicTuple) op.getTupleArgument());
            } else {
                msg = new TucsonMsgRequest(this.opId, type, tcid.toString(),
                        (LogicTuple) op.getTemplateArgument());
            }
            this.log("sending msg " + msg.getId() + ", op = " + msg.getType()
                    + ", " + msg.getTuple() + ", " + msg.getTid());
            try {
                if (session != null) {
                    session.sendMsgRequest(msg);
                }
            } catch (final DialogSendException e) {
                exception = true;
                e.printStackTrace();
            }
            if (!exception) {
                return new TucsonOpId(this.opId);
            }
        } while (nTry < InterTupleCentreACCProxy.TRIES);
        throw new UnreachableNodeException();
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

    private void log(final String msg) {
        System.out.println("..[InterTupleCentreACCProxy ("
                + this.profile.getProperty("tc-identity") + ")]: " + msg);
    }

    private void postEvent(final TucsonOpCompletionEvent ev) {
        synchronized (this.events) {
            this.events.add(this.events.size(), ev);
            this.events.notifyAll();
        }
    }
}
