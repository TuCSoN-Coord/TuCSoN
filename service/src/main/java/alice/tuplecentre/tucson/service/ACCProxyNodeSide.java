/*
 * TuCSoN coordination infrastructure - Copyright (C) 2001-2002 aliCE team at
 * deis.unibo.it This library is free software; you can redistribute it and/or
 * modify it under the terms copyOf the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 copyOf the License,
 * or (at your option) any later version. This library is distributed in the
 * hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty copyOf MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU Lesser General Public License for more details. You should have
 * received a copy copyOf the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */
package alice.tuplecentre.tucson.service;

import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import alice.tuple.logic.LogicTuple;
import alice.tuple.logic.exceptions.InvalidLogicTupleException;
import alice.tuplecentre.api.OperationIdentifier;
import alice.tuplecentre.api.TupleCentreOperation;
import alice.tuplecentre.core.AbstractTupleCentreOperation;
import alice.tuplecentre.core.InputEvent;
import alice.tuplecentre.core.TupleCentreOpType;
import alice.tuplecentre.respect.core.RespectOperationDefault;
import alice.tuplecentre.tucson.api.TucsonAgentId;
import alice.tuplecentre.tucson.api.TucsonAgentIdDefault;
import alice.tuplecentre.tucson.api.TucsonTupleCentreId;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidLogicTupleException;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidSpecificationException;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidTupleCentreIdException;
import alice.tuplecentre.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tuplecentre.tucson.network.TucsonProtocol;
import alice.tuplecentre.tucson.network.exceptions.DialogException;
import alice.tuplecentre.tucson.network.messages.TucsonMessageReply;
import alice.tuplecentre.tucson.network.messages.TucsonMessageRequest;
import alice.tuplecentre.tucson.network.messages.events.InputEventMessage;
import alice.tuplecentre.tucson.network.messages.events.OutputEventMessageDefault;
import alice.tuplecentre.tucson.network.messages.introspection.ShutdownMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Alessandro Ricci
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 * @author (contributor) Michele Bombardi (mailto:
 * michele.bombardi@studio.unibo.it)
 */
public class ACCProxyNodeSide extends AbstractACCProxyNodeSide {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private TucsonAgentId agentId;
    private final String agentName;
    private final int ctxId;
    private final TucsonProtocol dialog;
    private boolean ex = false;
    private final ACCProvider manager;
    private final TucsonNodeService node;
    private final Map<OperationIdentifier, OperationIdentifier> opVsReq;
    private final Map<OperationIdentifier, TucsonMessageRequest> requests;
    private TucsonTupleCentreId tcId;

    /**
     * @param man the ACC provider who created this ACC Proxy at TuCSoN node
     *            side
     * @param d   the network protocol used by this ACC Proxy at TuCSoN node
     *            side
     * @param n   the TuCSoN node this ACC Proxy at TuCSoN node side belongs to
     * @param p   the object describing the request copyOf entering the TuCSoN
     *            system
     * @throws TucsonInvalidTupleCentreIdException if the TupleCentreIdentifier, contained into AbstractTucsonProtocol's
     *                                             message, does not represent a valid TuCSoN identifier
     * @throws TucsonInvalidAgentIdException       if the ACCDescription's "agent-identity" property does not
     *                                             represent a valid TuCSoN identifier
     */
    public ACCProxyNodeSide(final ACCProvider man,
                            final TucsonProtocol d, final TucsonNodeService n,
                            final ACCDescription p) throws TucsonInvalidTupleCentreIdException,
            TucsonInvalidAgentIdException {
        super();
        this.ctxId = Integer.parseInt(p.getProperty("context-id"));
        String name = p.getProperty("agent-identity");
        if (name == null) {
            name = p.getProperty("tc-identity");
            this.tcId = TucsonTupleCentreId.of(name);
            this.agentId = new TucsonAgentIdDefault("tcAgent", this.tcId);
        } else {
            this.agentId = new TucsonAgentIdDefault(name);
        }
        this.agentName = name;
        this.dialog = d;
        this.requests = new HashMap<>();
        this.opVsReq = new HashMap<>();
        this.node = n;
        this.manager = man;
    }

    @Override
    public synchronized void exit(final ShutdownMessage msg) {
        this.log("Shutdown request received from <" + msg.getAgentIdentifier() + ">...");
        this.ex = true;
        this.notify();
    }

    /**
     * @param op the operation just completed
     */
    @Override
    public void operationCompleted(final AbstractTupleCentreOperation op) {
        OperationIdentifier reqId;
        TucsonMessageRequest msg;
        synchronized (this.requests) {
            reqId = this.opVsReq.remove(op.getId());
            msg = this.requests.remove(reqId);
        }
        final InputEventMessage evMsg = msg.getEventMsg();
        TucsonMessageReply reply;
        if (op.getType() == TupleCentreOpType.IN_ALL || op.getType() == TupleCentreOpType.RD_ALL || op.getType() == TupleCentreOpType.NO_ALL || op.getType() == TupleCentreOpType.OUT_ALL) {
            if (op.getTupleListResult() == null) {
                op.setTupleListResult(new LinkedList<>());
            }
            if (op.isResultSuccess()) {
                reply = new TucsonMessageReply(new OutputEventMessageDefault(evMsg.getOpId(),
                        evMsg.getOpType(), true, true, true, evMsg.getTuple(),
                        op.getTupleListResult()));
            } else {
                reply = new TucsonMessageReply(new OutputEventMessageDefault(evMsg.getOpId(),
                        evMsg.getOpType(), true, true, false, evMsg.getTuple(),
                        op.getTupleListResult()));
            }
        } else {
            if (op.isResultSuccess()) {
                reply = new TucsonMessageReply(new OutputEventMessageDefault(evMsg.getOpId(),
                        evMsg.getOpType(), true, true, true, evMsg.getTuple(),
                        op.getTupleResult()));
            } else {
                reply = new TucsonMessageReply(new OutputEventMessageDefault(evMsg.getOpId(),
                        evMsg.getOpType(), true, true, false, evMsg.getTuple(),
                        op.getTupleResult()));
            }
        }
        try {
            this.dialog.sendMsgReply(reply);
        } catch (final DialogException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    /**
     *
     */
    @Override
    public void run() {
        this.node.addAgent(this.agentId);
        TucsonMessageRequest msg;
        TucsonMessageReply reply;
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
            final InputEventMessage evMsg = msg.getEventMsg();
            final TupleCentreOpType msgType = evMsg.getOpType();
            if (msgType == TupleCentreOpType.EXIT) {
                reply = new TucsonMessageReply(new OutputEventMessageDefault(evMsg.getOpId(),
                        TupleCentreOpType.EXIT, true, true, true));
                try {
                    this.dialog.sendMsgReply(reply);
                    break;
                } catch (final DialogException e) {
                    LOGGER.error(e.getMessage(), e);
                    break;
                }
            }
            try {
                tid = TucsonTupleCentreId.of(evMsg.getReactingTC());
            } catch (final TucsonInvalidTupleCentreIdException e) {
                LOGGER.error(e.getMessage(), e);
                break;
            }
            // Prolog p = null;
            // try {
            // p = ( (RespectTC)
            // RespectTCContainer.getRespectTCContainer().getRegistry().getTC(tid.getInternalTupleCentreId())).getProlog();
            // } catch (InstantiationNotPossibleException e) {
            // LOGGER.error(e.getMessage(), e);
            // }

            // Operation Make
            final RespectOperationDefault opRequested = this.makeOperation(
                    evMsg.getOpType(), evMsg.getTuple());
            // InputEvent Creation
            InputEvent ev;
            if (this.tcId != null) {
                ev = new InputEvent(this.tcId, opRequested, tid,
                        evMsg.getTime(), evMsg.getPlace());
            } else {
                ev = new InputEvent(this.agentId, opRequested, tid,
                        evMsg.getTime(), evMsg.getPlace());
            }
            final AbstractTupleCentreOperation evOp = ev.getSimpleTCEvent();
            this.log("Serving TucsonOperationDefault request < id=" + evOp.getId()
                    + ", type=" + evOp.getType() + ", tuple="
                    + evMsg.getTuple() + " >...");
            switch (msgType) {
                case SET_S:
                    this.node.resolveCore(tid.getLocalName());
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
                    } catch (final TucsonOperationNotPossibleException | TucsonInvalidSpecificationException e) {
                        LOGGER.error(e.getMessage(), e);
                        break;
                    }
                    reply = new TucsonMessageReply(new OutputEventMessageDefault(evMsg.getOpId(),
                            msgType, true, true, true, evMsg.getTuple(), resList));
                    try {
                        this.dialog.sendMsgReply(reply);
                    } catch (final DialogException e) {
                        LOGGER.error(e.getMessage(), e);
                        break;
                    }
                    break;
                case SET:
                    this.node.resolveCore(tid.getLocalName());
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
                    } catch (final TucsonOperationNotPossibleException | TucsonInvalidLogicTupleException e) {
                        LOGGER.error(e.getMessage(), e);
                        break;
                    }
                    reply = new TucsonMessageReply(new OutputEventMessageDefault(evMsg.getOpId(),
                            msgType, true, true, true, res, resList));
                    try {
                        this.dialog.sendMsgReply(reply);
                    } catch (final DialogException e) {
                        LOGGER.error(e.getMessage(), e);
                        break;
                    }
                    break;
                case GET:
                    this.node.resolveCore(tid.getLocalName());
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
                    } catch (final TucsonOperationNotPossibleException | TucsonInvalidLogicTupleException e) {
                        LOGGER.error(e.getMessage(), e);
                        break;
                    }
                    reply = new TucsonMessageReply(new OutputEventMessageDefault(evMsg.getOpId(),
                            msgType, true, true, true, null, resList));
                    try {
                        this.dialog.sendMsgReply(reply);
                    } catch (final DialogException e) {
                        LOGGER.error(e.getMessage(), e);
                        break;
                    }
                    break;
                case GET_S:
                    this.node.resolveCore(tid.getLocalName());
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
                            resList = new LinkedList<>();
                        }
                    } catch (final TucsonOperationNotPossibleException | TucsonInvalidSpecificationException e) {
                        LOGGER.error(e.getMessage(), e);
                        break;
                    }
                    reply = new TucsonMessageReply(new OutputEventMessageDefault(evMsg.getOpId(),
                            msgType, true, true, true, null, resList));
                    try {
                        this.dialog.sendMsgReply(reply);
                    } catch (final DialogException e) {
                        LOGGER.error(e.getMessage(), e);
                        break;
                    }
                    break;
                case NO:
                case NOP:
                case OUT:
                case OUT_ALL:
                case IN:
                case INP:
                case RD:
                case RDP:
                case UIN:
                case UINP:
                case URD:
                case URDP:
                case UNO:
                case UNOP:
                case IN_ALL:
                case RD_ALL:
                case NO_ALL:
                case SPAWN: {
                    this.node.resolveCore(tid.getLocalName());
                    this.node.addTCAgent(this.agentId, tid);
                    TupleCentreOperation op;
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
                        } catch (final TucsonOperationNotPossibleException | TucsonInvalidLogicTupleException e) {
                            LOGGER.error(e.getMessage(), e);
                            break;
                        }
                        this.requests.put(evOp.getId(), msg);
                        this.opVsReq.put(op.getId(),
                                evOp.getId());
                    }
                    break;
                }
                case NO_S:
                case NOP_S:
                case OUT_S:
                case IN_S:
                case INP_S:
                case RD_S:
                case RDP_S: {
                    this.node.resolveCore(tid.getLocalName());
                    this.node.addTCAgent(this.agentId, tid);
                    TupleCentreOperation op;
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
                        } catch (final TucsonOperationNotPossibleException | TucsonInvalidLogicTupleException e) {
                            LOGGER.error(e.getMessage(), e);
                            break;
                        }
                        this.requests.put(evOp.getId(), msg);
                        this.opVsReq.put(op.getId(), evOp.getId());
                    }
                    break;
                }
                case GET_ENV:
                case SET_ENV: {
                    this.node.resolveCore(tid.getLocalName());
                    this.node.addTCAgent(this.agentId, tid);
                    TupleCentreOperation op;
                    synchronized (this.requests) {
                            if (this.tcId == null) {
                                op = TupleCentreContainer.doEnvironmentalOperation(
                                        msgType, this.agentId, tid, msg.getEventMsg().getTuple(),
                                        this);
                            } else {
                                op = TupleCentreContainer.doEnvironmentalOperation(
                                        msgType, this.tcId, tid, msg.getEventMsg().getTuple(),
                                        this);
                            }
                    }
                    this.requests.put(msg.getEventMsg().getOpId(), msg);
                    this.opVsReq.put(op.getId(), msg.getEventMsg().getOpId());
                    break;
                }
            }
        }
        try {
            this.dialog.end();
        } catch (final DialogException e) {
            LOGGER.error(e.getMessage(), e);
        }
        this.log("Releasing ACC < " + this.ctxId + " > held by TuCSoN agent < "
                + this.agentId.toString() + " >");
        this.node.removeAgent(this.agentId);
        this.manager.shutdownContext(this.ctxId, this.agentId);
        this.node.removeNodeAgent(this);
    }

    private void err(final String st) {
        LOGGER.error("..[ACCProxyNodeSide (" + this.node.getTCPPort()
                + ", " + this.ctxId + ", " + this.agentName + ")]: " + st);
    }

    private void log(final String st) {
        LOGGER.info("..[ACCProxyNodeSide (" + this.node.getTCPPort()
                + ", " + this.ctxId + ", " + this.agentName + ")]: " + st);
    }

    /**
     * @param opType
     * @param tuple
     * @return
     */
    private RespectOperationDefault makeOperation(final TupleCentreOpType opType,
                                                  final LogicTuple tuple) {
        RespectOperationDefault op = null;
        try {
            if (opType == TupleCentreOpType.GET
                    || opType == TupleCentreOpType.GET_S
                    || opType == TupleCentreOpType.SET
                    || opType == TupleCentreOpType.SET_S) {
                op = RespectOperationDefault.make(opType, tuple, null); // blocking
                // operation,
                // no
                // need
                // for
                // operation
                // completion
                // listener
            } else {
                op = RespectOperationDefault.make(opType, tuple, this); // non
                // blocking
                // operation,
                // need
                // for
                // operation
                // completion
                // listener
            }
        } catch (final InvalidLogicTupleException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return op;
    }
}
