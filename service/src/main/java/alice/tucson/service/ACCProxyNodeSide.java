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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import alice.logictuple.LogicTuple;
import alice.logictuple.exceptions.InvalidLogicTupleException;
import alice.respect.core.RespectOperation;
import alice.tucson.api.TucsonAgentId;
import alice.tucson.api.TucsonTupleCentreId;
import alice.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tucson.api.exceptions.TucsonInvalidLogicTupleException;
import alice.tucson.api.exceptions.TucsonInvalidSpecificationException;
import alice.tucson.api.exceptions.TucsonInvalidTupleCentreIdException;
import alice.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tucson.api.exceptions.UnreachableNodeException;
import alice.tucson.introspection.ShutdownMsg;
import alice.tucson.network.AbstractTucsonProtocol;
import alice.tucson.network.TucsonMsgReply;
import alice.tucson.network.TucsonMsgRequest;
import alice.tucson.network.exceptions.DialogException;
import alice.tuplecentre.api.ITupleCentreOperation;
import alice.tuplecentre.api.Tuple;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;
import alice.tuplecentre.core.AbstractTupleCentreOperation;
import alice.tuplecentre.core.InputEvent;

/**
 *
 * @author Alessandro Ricci
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 * @author (contributor) Michele Bombardi (mailto:
 *         michele.bombardi@studio.unibo.it)
 *
 */
public class ACCProxyNodeSide extends AbstractACCProxyNodeSide {

    private TucsonAgentId agentId;
    private final String agentName;
    private final int ctxId;
    private final AbstractTucsonProtocol dialog;
    private boolean ex = false;
    private final ACCProvider manager;
    private final TucsonNodeService node;
    private final Map<Long, Long> opVsReq;
    private final Map<Long, TucsonMsgRequest> requests;
    private TucsonTupleCentreId tcId;

    /**
     *
     * @param man
     *            the ACC provider who created this ACC Proxy at TuCSoN node
     *            side
     * @param d
     *            the network protocol used by this ACC Proxy at TuCSoN node
     *            side
     * @param n
     *            the TuCSoN node this ACC Proxy at TuCSoN node side belongs to
     * @param p
     *            the object describing the request of entering the TuCSoN
     *            system
     * @throws TucsonInvalidTupleCentreIdException
     *             if the TupleCentreId, contained into AbstractTucsonProtocol's
     *             message, does not represent a valid TuCSoN identifier
     *
     * @throws TucsonInvalidAgentIdException
     *             if the ACCDescription's "agent-identity" property does not
     *             represent a valid TuCSoN identifier
     */
    public ACCProxyNodeSide(final ACCProvider man,
            final AbstractTucsonProtocol d, final TucsonNodeService n,
            final ACCDescription p) throws TucsonInvalidTupleCentreIdException,
            TucsonInvalidAgentIdException {
        super();
        this.ctxId = Integer.parseInt(p.getProperty("context-id"));
        String name = p.getProperty("agent-identity");
        if (name == null) {
            name = p.getProperty("tc-identity");
            this.tcId = new TucsonTupleCentreId(name);
            this.agentId = new TucsonAgentId("tcAgent", this.tcId);
        } else {
            this.agentId = new TucsonAgentId(name);
        }
        this.agentName = name;
        this.dialog = d;
        this.requests = new HashMap<Long, TucsonMsgRequest>();
        this.opVsReq = new HashMap<Long, Long>();
        this.node = n;
        this.manager = man;
    }

    @Override
    public synchronized void exit(final ShutdownMsg msg) {
        this.log("Shutdown request received from <" + msg.getAid() + ">...");
        this.ex = true;
        this.notify();
    }

    /**
     * @param op
     *            the operation just completed
     */
    @Override
    public void operationCompleted(final AbstractTupleCentreOperation op) {
        Long reqId;
        TucsonMsgRequest msg;
        synchronized (this.requests) {
            reqId = this.opVsReq.remove(Long.valueOf(op.getId()));
            msg = this.requests.remove(reqId);
        }
        final InputEventMsg evMsg = msg.getInputEventMsg();
        TucsonMsgReply reply = null;
        if (op.isInAll() || op.isRdAll() || op.isNoAll() || op.isOutAll()) {
            if (op.getTupleListResult() == null) {
                op.setTupleListResult(new LinkedList<Tuple>());
            }
            if (op.isResultSuccess()) {
                reply = new TucsonMsgReply(new OutputEventMsg(evMsg.getOpId(),
                        evMsg.getOpType(), true, true, true, evMsg.getTuple(),
                        op.getTupleListResult()));
            } else {
                reply = new TucsonMsgReply(new OutputEventMsg(evMsg.getOpId(),
                        evMsg.getOpType(), true, true, false, evMsg.getTuple(),
                        op.getTupleListResult()));
            }
        } else {
            if (op.isResultSuccess()) {
                reply = new TucsonMsgReply(new OutputEventMsg(evMsg.getOpId(),
                        evMsg.getOpType(), true, true, true, evMsg.getTuple(),
                        op.getTupleResult()));
            } else {
                reply = new TucsonMsgReply(new OutputEventMsg(evMsg.getOpId(),
                        evMsg.getOpType(), true, true, false, evMsg.getTuple(),
                        op.getTupleResult()));
            }
        }
        try {
            this.dialog.sendMsgReply(reply);
        } catch (final DialogException e) {
            e.printStackTrace();
        }
    }

    /**
     * 
     */
    @Override
    public void run() {
        this.node.addAgent(this.agentId);
        TucsonMsgRequest msg;
        TucsonMsgReply reply;
        TucsonTupleCentreId tid;
        final LogicTuple res = null;
        List<LogicTuple> resList;
        while (!this.ex) {
            this.log("Listening to incoming TuCSoN agents/nodes requests...");
            try {
                msg = this.dialog.receiveMsgRequest();
            } catch (final DialogException e) {
                this.log("Agent " + this.agentId + " quit");
                break;
            }
            // final int msgType = msg.getType();
            final InputEventMsg evMsg = msg.getInputEventMsg();
            final int msgType = evMsg.getOpType();
            if (msgType == TucsonOperation.exitCode()) {
                reply = new TucsonMsgReply(new OutputEventMsg(evMsg.getOpId(),
                        TucsonOperation.exitCode(), true, true, true));
                try {
                    this.dialog.sendMsgReply(reply);
                    break;
                } catch (final DialogException e) {
                    e.printStackTrace();
                    break;
                }
            }
            try {
                tid = new TucsonTupleCentreId(evMsg.getReactingTC());
            } catch (final TucsonInvalidTupleCentreIdException e) {
                e.printStackTrace();
                break;
            }
            // Prolog p = null;
            // try {
            // p = ( (RespectTC)
            // RespectTCContainer.getRespectTCContainer().getRegistry().getTC(tid.getInternalTupleCentreId())).getProlog();
            // } catch (InstantiationNotPossibleException e) {
            // e.printStackTrace();
            // }
            
            // Operation Make
            final RespectOperation opRequested = this.makeOperation(
                    evMsg.getOpType(), evMsg.getTuple());
            // InputEvent Creation
            InputEvent ev = null;
            if (this.tcId != null) {
                ev = new InputEvent(this.tcId, opRequested, tid,
                        evMsg.getTime(), evMsg.getPlace());
            } else {
                ev = new InputEvent(this.agentId, opRequested, tid,
                        evMsg.getTime(), evMsg.getPlace());
            }
            final AbstractTupleCentreOperation evOp = ev.getSimpleTCEvent();
            this.log("Serving TucsonOperation request < id=" + evOp.getId()
                    + ", type=" + evOp.getType() + ", tuple="
                    + evMsg.getTuple() + " >...");
            if (msgType == TucsonOperation.setSCode()) {
                this.node.resolveCore(tid.getName());
                this.node.addTCAgent(this.agentId, tid);
                try {
                    resList = (List<LogicTuple>) TupleCentreContainer
                            .doBlockingSpecOperation(ev, evMsg.getTuple());
                    // if (this.tcId == null) {
                    // resList =
                    // (List<LogicTuple>) TupleCentreContainer
                    // .doBlockingSpecOperation(msgType,
                    // this.agentId, tid,
                    // (LogicTuple)ev.getSimpleTCEvent().getTupleArgument(),
                    // ev);
                    // } else {
                    // resList =
                    // (List<LogicTuple>) TupleCentreContainer
                    // .doBlockingSpecOperation(msgType,
                    // this.tcId, tid,
                    // (LogicTuple)ev.getSimpleTCEvent().getTupleArgument(),
                    // ev);
                    // }
                } catch (final TucsonOperationNotPossibleException e) {
                    e.printStackTrace();
                    break;
                } catch (final TucsonInvalidSpecificationException e) {
                    e.printStackTrace();
                    break;
                }
                reply = new TucsonMsgReply(new OutputEventMsg(evMsg.getOpId(),
                        msgType, true, true, true, evMsg.getTuple(), resList));
                try {
                    this.dialog.sendMsgReply(reply);
                } catch (final DialogException e) {
                    e.printStackTrace();
                    break;
                }
            } else if (msgType == TucsonOperation.setCode()) {
                this.node.resolveCore(tid.getName());
                this.node.addTCAgent(this.agentId, tid);
                try {
                    resList = (List<LogicTuple>) TupleCentreContainer
                            .doBlockingOperation(ev);
                    // if (this.tcId == null) {
                    // resList =
                    // (List<LogicTuple>) TupleCentreContainer
                    // .doBlockingOperation(msgType,
                    // this.agentId, tid,
                    // (LogicTuple)ev.getSimpleTCEvent().getTupleArgument(),
                    // ev);
                    // resList =
                    // (List<LogicTuple>) TupleCentreContainer
                    // .doBlockingOperation(msgType,
                    // this.tcId, tid,
                    // (LogicTuple)ev.getSimpleTCEvent().getTupleArgument(),
                    // ev);
                    // }
                } catch (final TucsonOperationNotPossibleException e) {
                    e.printStackTrace();
                    break;
                } catch (final TucsonInvalidLogicTupleException e) {
                    e.printStackTrace();
                    break;
                }
                reply = new TucsonMsgReply(new OutputEventMsg(evMsg.getOpId(),
                        msgType, true, true, true, res, resList));
                try {
                    this.dialog.sendMsgReply(reply);
                } catch (final DialogException e) {
                    e.printStackTrace();
                    break;
                }
            } else if (msgType == TucsonOperation.getCode()) {
                this.node.resolveCore(tid.getName());
                this.node.addTCAgent(this.agentId, tid);
                try {
                    resList = (List<LogicTuple>) TupleCentreContainer
                            .doBlockingOperation(ev);
                    // if (this.tcId == null) {
                    // resList =
                    // (List<LogicTuple>) TupleCentreContainer
                    // .doBlockingOperation(msgType,
                    // this.agentId, tid, null, ev);
                    // } else {
                    // resList =
                    // (List<LogicTuple>) TupleCentreContainer
                    // .doBlockingOperation(msgType,
                    // this.tcId, tid, null, ev);
                    // }
                } catch (final TucsonOperationNotPossibleException e) {
                    e.printStackTrace();
                    break;
                } catch (final TucsonInvalidLogicTupleException e) {
                    e.printStackTrace();
                    break;
                }
                reply = new TucsonMsgReply(new OutputEventMsg(evMsg.getOpId(),
                        msgType, true, true, true, null, resList));
                try {
                    this.dialog.sendMsgReply(reply);
                } catch (final DialogException e) {
                    e.printStackTrace();
                    break;
                }
            } else if (msgType == TucsonOperation.getSCode()) {
                this.node.resolveCore(tid.getName());
                this.node.addTCAgent(this.agentId, tid);
                try {
                    resList = (List<LogicTuple>) TupleCentreContainer
                            .doBlockingSpecOperation(ev, evMsg.getTuple());
                    // if (this.tcId == null) {
                    // resList =
                    // (List<LogicTuple>) TupleCentreContainer
                    // .doBlockingSpecOperation(msgType,
                    // this.agentId, tid, null, ev);
                    // } else {
                    // resList =
                    // (List<LogicTuple>) TupleCentreContainer
                    // .doBlockingSpecOperation(msgType,
                    // this.tcId, tid, null, ev);
                    // }
                    if (resList == null) {
                        resList = new LinkedList<LogicTuple>();
                    }
                } catch (final TucsonOperationNotPossibleException e) {
                    e.printStackTrace();
                    break;
                } catch (final TucsonInvalidSpecificationException e) {
                    e.printStackTrace();
                    break;
                }
                reply = new TucsonMsgReply(new OutputEventMsg(evMsg.getOpId(),
                        msgType, true, true, true, null, resList));
                try {
                    this.dialog.sendMsgReply(reply);
                } catch (final DialogException e) {
                    e.printStackTrace();
                    break;
                }
            } else if (msgType == TucsonOperation.noCode()
                    || msgType == TucsonOperation.nopCode()
                    || msgType == TucsonOperation.outCode()
                    || msgType == TucsonOperation.outAllCode()
                    || msgType == TucsonOperation.inCode()
                    || msgType == TucsonOperation.inpCode()
                    || msgType == TucsonOperation.rdCode()
                    || msgType == TucsonOperation.rdpCode()
                    || msgType == TucsonOperation.uinCode()
                    || msgType == TucsonOperation.uinpCode()
                    || msgType == TucsonOperation.urdCode()
                    || msgType == TucsonOperation.urdpCode()
                    || msgType == TucsonOperation.unoCode()
                    || msgType == TucsonOperation.unopCode()
                    || msgType == TucsonOperation.inAllCode()
                    || msgType == TucsonOperation.rdAllCode()
                    || msgType == TucsonOperation.noAllCode()
                    || msgType == TucsonOperation.spawnCode()) {
                this.node.resolveCore(tid.getName());
                this.node.addTCAgent(this.agentId, tid);
                ITupleCentreOperation op;
                synchronized (this.requests) {
                    try {
                        op = TupleCentreContainer.doNonBlockingOperation(ev);
                        // if (this.tcId == null) {
                        // op =
                        // TupleCentreContainer
                        // .doNonBlockingOperation(msgType,
                        // this.agentId, tid,
                        // (LogicTuple)ev.getSimpleTCEvent().getTupleArgument(),
                        // this, ev);
                        // } else {
                        // op =
                        // TupleCentreContainer
                        // .doNonBlockingOperation(msgType,
                        // this.tcId, tid,
                        // (LogicTuple)ev.getSimpleTCEvent().getTupleArgument(),
                        // this, ev);
                        // }
                    } catch (final TucsonOperationNotPossibleException e) {
                        e.printStackTrace();
                        break;
                    } catch (final TucsonInvalidLogicTupleException e) {
                        e.printStackTrace();
                        break;
                    }
                    this.requests.put(Long.valueOf(evOp.getId()), msg);
                    this.opVsReq.put(Long.valueOf(op.getId()),
                            Long.valueOf(evOp.getId()));
                }
            } else if (msgType == TucsonOperation.noSCode()
                    || msgType == TucsonOperation.nopSCode()
                    || msgType == TucsonOperation.outSCode()
                    || msgType == TucsonOperation.inSCode()
                    || msgType == TucsonOperation.inpSCode()
                    || msgType == TucsonOperation.rdSCode()
                    || msgType == TucsonOperation.rdpSCode()) {
                this.node.resolveCore(tid.getName());
                this.node.addTCAgent(this.agentId, tid);
                ITupleCentreOperation op;
                synchronized (this.requests) {
                    try {
                        op = TupleCentreContainer
                                .doNonBlockingSpecOperation(ev);
                        // if (this.tcId == null) {
                        // op =
                        // TupleCentreContainer
                        // .doNonBlockingSpecOperation(
                        // msgType, this.agentId, tid,
                        // (LogicTuple)ev.getSimpleTCEvent().getTupleArgument(),
                        // this, ev);
                        // } else {
                        // op =
                        // TupleCentreContainer
                        // .doNonBlockingSpecOperation(
                        // msgType, this.tcId, tid,
                        // (LogicTuple)ev.getSimpleTCEvent().getTupleArgument(),
                        // this, ev);
                        // }
                    } catch (final TucsonOperationNotPossibleException e) {
                        e.printStackTrace();
                        break;
                    } catch (final TucsonInvalidLogicTupleException e) {
                        e.printStackTrace();
                        break;
                    }
                    this.requests.put(Long.valueOf(evOp.getId()), msg);
                    this.opVsReq.put(Long.valueOf(op.getId()),
                            Long.valueOf(evOp.getId()));
                }
            } else if (msgType == TucsonOperation.getEnvCode()
                    || msgType == TucsonOperation.setEnvCode()) {
                this.node.resolveCore(tid.getName());
                this.node.addTCAgent(this.agentId, tid);
                ITupleCentreOperation op = null;
                synchronized (this.requests) {
                    try {
                        if (this.tcId == null) {
                            op = TupleCentreContainer.doEnvironmentalOperation(
                                    msgType, this.agentId, tid, msg.getInputEventMsg().getTuple(),
                                    this);
                        } else {
                            op = TupleCentreContainer.doEnvironmentalOperation(
                                    msgType, this.tcId, tid, msg.getInputEventMsg().getTuple(),
                                    this);
                        }
                    } catch (final TucsonOperationNotPossibleException e) {
                        System.err.println("[ACCProxyNodeSide]: " + e);
                        break;
                    } catch (final OperationTimeOutException e) {
                        System.err.println("[ACCProxyNodeSide]: " + e);
                        break;
                    } catch (final UnreachableNodeException e) {
                        System.err.println("[ACCProxyNodeSide]: " + e);
                        break;
                    }
                }
                this.requests.put(Long.valueOf(msg.getInputEventMsg().getOpId()), msg);
                this.opVsReq.put(Long.valueOf(op.getId()),
                        Long.valueOf(msg.getInputEventMsg().getOpId()));
            }
        }
        try {
            this.dialog.end();
        } catch (final DialogException e) {
            e.printStackTrace();
        }
        this.log("Releasing ACC < " + this.ctxId + " > held by TuCSoN agent < "
                + this.agentId.toString() + " >");
        this.node.removeAgent(this.agentId);
        this.manager.shutdownContext(this.ctxId, this.agentId);
        this.node.removeNodeAgent(this);
    }

    private void err(final String st) {
        System.err.println("..[ACCProxyNodeSide (" + this.node.getTCPPort()
                + ", " + this.ctxId + ", " + this.agentName + ")]: " + st);
    }

    private void log(final String st) {
        System.out.println("..[ACCProxyNodeSide (" + this.node.getTCPPort()
                + ", " + this.ctxId + ", " + this.agentName + ")]: " + st);
    }
    
    /**
     *
     * @param opType
     * @param tuple
     * @return
     */
    private RespectOperation makeOperation(final int opType,
            final LogicTuple tuple) {
        RespectOperation op = null;
        try {
            if (opType == TucsonOperation.getCode()
                    || opType == TucsonOperation.getSCode()
                    || opType == TucsonOperation.setCode()
                    || opType == TucsonOperation.setSCode()) {
                op = RespectOperation.make(opType, tuple, null); // blocking
                                                                       // operation,
                                                                       // no
                                                                       // need
                                                                       // for
                                                                       // operation
                                                                       // completion
                                                                       // listener
            } else {
                op = RespectOperation.make(opType, tuple, this); // non
                                                                       // blocking
                                                                       // operation,
                                                                       // need
                                                                       // for
                                                                       // operation
                                                                       // completion
                                                                       // listener
            }
        } catch (final InvalidLogicTupleException e) {
            e.printStackTrace();
        }
        return op;
    }
}
