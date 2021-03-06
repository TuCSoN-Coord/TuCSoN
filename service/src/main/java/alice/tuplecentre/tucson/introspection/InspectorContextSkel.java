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
package alice.tuplecentre.tucson.introspection;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;

import alice.tuple.logic.LogicTuple;
import alice.tuple.logic.exceptions.InvalidLogicTupleException;
import alice.tuplecentre.api.InspectableEventListener;
import alice.tuplecentre.core.AbstractTupleCentreOperation;
import alice.tuplecentre.core.InputEvent;
import alice.tuplecentre.core.InspectableEvent;
import alice.tuplecentre.core.ObservableEventExt;
import alice.tuplecentre.core.ObservableEventReactionFail;
import alice.tuplecentre.core.ObservableEventReactionOK;
import alice.tuplecentre.core.TriggeredReaction;
import alice.tuplecentre.core.TupleCentreOpType;
import alice.tuplecentre.respect.core.LogicReaction;
import alice.tuplecentre.respect.core.RespectOperationDefault;
import alice.tuplecentre.tucson.api.TucsonAgentId;
import alice.tuplecentre.tucson.api.TucsonAgentIdDefault;
import alice.tuplecentre.tucson.api.TucsonTupleCentreId;
import alice.tuplecentre.tucson.api.exceptions.TucsonGenericException;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidLogicTupleException;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidTupleCentreIdException;
import alice.tuplecentre.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tuplecentre.tucson.network.TucsonProtocol;
import alice.tuplecentre.tucson.network.exceptions.DialogException;
import alice.tuplecentre.tucson.network.exceptions.DialogReceiveException;
import alice.tuplecentre.tucson.network.exceptions.DialogSendException;
import alice.tuplecentre.tucson.network.messages.introspection.GetSnapshotMessage;
import alice.tuplecentre.tucson.network.messages.introspection.IsActiveStepModeMessage;
import alice.tuplecentre.tucson.network.messages.introspection.NewInspectorMessage;
import alice.tuplecentre.tucson.network.messages.introspection.NextStepMessage;
import alice.tuplecentre.tucson.network.messages.introspection.NodeMessage;
import alice.tuplecentre.tucson.network.messages.introspection.SetEventSetMessage;
import alice.tuplecentre.tucson.network.messages.introspection.SetProtocolMessageDefault;
import alice.tuplecentre.tucson.network.messages.introspection.SetTupleSetMessage;
import alice.tuplecentre.tucson.network.messages.introspection.ShutdownMessage;
import alice.tuplecentre.tucson.network.messages.introspection.StepModeMessage;
import alice.tuplecentre.tucson.service.ACCDescription;
import alice.tuplecentre.tucson.service.ACCProvider;
import alice.tuplecentre.tucson.service.AbstractACCProxyNodeSide;
import alice.tuplecentre.tucson.service.TucsonNodeService;
import alice.tuplecentre.tucson.service.TucsonTCUsers;
import alice.tuplecentre.tucson.service.TupleCentreContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Unknown...
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 * @author (contributor) Michele Bombardi (mailto:
 * michele.bombardi@studio.unibo.it)
 */
public class InspectorContextSkel extends AbstractACCProxyNodeSide implements
        InspectableEventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final TucsonAgentId agentId;
    private final int ctxId;
    protected final TucsonProtocol dialog;
    private final ACCProvider manager;
    private boolean nStep;
    /**
     * current observation protocol
     */
    protected InspectorProtocol protocol;
    private boolean shutdown = false;
    protected final TucsonTupleCentreId tcId;

    /**
     * @param man  the ACC provider distributing ACCs
     * @param d    the TuCSoN protocol to be used
     * @param node the TuCSoN node service to communicate with
     * @param p    the ACC properties descriptor
     * @throws TucsonGenericException              if the tuple centre to inspect cannot be resolved
     * @throws TucsonInvalidAgentIdException       if the ACCDescription's "agent-identity" property does not
     *                                             represent a valid TuCSoN identifier
     * @throws TucsonInvalidTupleCentreIdException if the TupleCentreIdentifier, contained into AbstractTucsonProtocol's
     *                                             message, does not represent a valid TuCSoN identifier
     * @throws DialogReceiveException              if something goes wrong in the underlying network
     */
    public InspectorContextSkel(final ACCProvider man,
                                final TucsonProtocol d, final TucsonNodeService node,
                                final ACCDescription p) throws TucsonGenericException,
            TucsonInvalidAgentIdException, DialogReceiveException,
            TucsonInvalidTupleCentreIdException {
        super();
        this.dialog = d;
        this.manager = man;
        NewInspectorMessage msg;
        this.ctxId = Integer.parseInt(p.getProperty("context-id"));
        final String name = p.getProperty("agent-identity");
        this.agentId = TucsonAgentId.of(name);
        msg = this.dialog.receiveInspectorMsg();
        this.tcId = TucsonTupleCentreId.of(msg.getTcName());
        final TucsonTCUsers coreInfo = node.resolveCore(msg.getTcName());
        if (coreInfo == null) {
            throw new TucsonGenericException(
                    "Internal error: InspectorContextSkel constructor");
        }
        this.protocol = msg.getInfo();
    }

    @Override
    public void exit(final ShutdownMessage msg) {
        this.log("Shutdown request received from <" + msg.getAgentIdentifier() + ">...");
        this.shutdown = true;
    }

    /**
     * get a tuple centre set (T set, W set,...) snapshot
     *
     * @param m the snapshot message
     */
    public void getSnapshot(final GetSnapshotMessage m) {
        final InspectorContextEventDefault msg = new InspectorContextEventDefault();
        msg.setVmTime(System.currentTimeMillis());
        msg.setLocalTime(System.currentTimeMillis());
        if (m.getWhat() == GetSnapshotMessage.SetType.TSET) {
            msg.setTuples(new LinkedList<>());
            LogicTuple[] tSet;
            tSet = (LogicTuple[]) TupleCentreContainer.doManagementOperation(
                    TupleCentreOpType.GET_TSET, this.tcId,
                    this.protocol.getTsetFilter());
            if (tSet != null) {
                for (final LogicTuple lt : tSet) {
                    msg.getTuples().add(lt);
                }
            }
        } else if (m.getWhat() == GetSnapshotMessage.SetType.WSET) {
            WSetEvent[] ltSet;
            ltSet = (WSetEvent[]) TupleCentreContainer.doManagementOperation(
                    TupleCentreOpType.GET_WSET, this.tcId,
                    this.protocol.getWsetFilter());
            msg.setWnEvents(new LinkedList<>());
            if (ltSet != null) {
                for (final WSetEvent lt : ltSet) {
                    msg.getWnEvents().add(lt);
                }
            }
        }
        try {
            this.dialog.sendInspectorEvent(msg);
        } catch (final DialogSendException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    /**
     * verify if VM step mode is already active
     *
     * @param m the IsActiveStepModeMessage
     */
    public void isStepMode(final IsActiveStepModeMessage m) {
        final boolean isActive = (Boolean) TupleCentreContainer
                .doManagementOperation(TupleCentreOpType.IS_STEP_MODE,
                        this.tcId, null);
        final InspectorContextEvent msg = new InspectorContextEventDefault();
        msg.setStepMode(isActive);
        try {
            this.dialog.sendInspectorEvent(msg);
        } catch (final DialogException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    /**
     * ask a new step for a tuple centre vm during step mode
     *
     * @param m the NxtStepMsg
     */
    public void nextStep(final NextStepMessage m) {
        TupleCentreContainer.doManagementOperation(
                TupleCentreOpType.NEXT_STEP, this.tcId, null);
    }

    @Override
    public synchronized void onInspectableEvent(final InspectableEvent ev) {
        try {
            while (this.protocol.isTracing() && !this.nStep) {
                this.wait();
            }
            this.nStep = false;
            final InspectorContextEventDefault msg = new InspectorContextEventDefault();
            msg.setLocalTime(System.currentTimeMillis());
            msg.setVmTime(ev.getTime());
            if (ev.getType() == InspectableEvent.TYPE_IDLESTATE
                    && this.protocol.getStepModeObservType() == InspectorProtocol.ObsType.STEP_MODE_AGENT) {
                if (this.protocol.getTsetObservType() == InspectorProtocol.ObsType.PROACTIVE) {
                    final LogicTuple[] ltSet = (LogicTuple[]) TupleCentreContainer
                            .doManagementOperation(
                                    TupleCentreOpType.GET_TSET, this.tcId,
                                    this.protocol.getTsetFilter());
                    msg.setTuples(new LinkedList<>());
                    if (ltSet != null) {
                        for (final LogicTuple lt : ltSet) {
                            msg.getTuples().add(lt);
                        }
                    }
                }
                if (this.protocol.getPendingQueryObservType() == InspectorProtocol.ObsType.PROACTIVE) {
                    final WSetEvent[] ltSet = (WSetEvent[]) TupleCentreContainer
                            .doManagementOperation(
                                    TupleCentreOpType.GET_WSET, this.tcId,
                                    this.protocol.getWsetFilter());
                    msg.setWnEvents(new LinkedList<>());
                    if (ltSet != null) {
                        for (final WSetEvent lt : ltSet) {
                            msg.getWnEvents().add(lt);
                        }
                    }
                }
                this.dialog.sendInspectorEvent(msg);
            }
            if (ev.getType() == InspectableEvent.TYPE_NEWSTATE
                    && this.protocol.getStepModeObservType() == InspectorProtocol.ObsType.STEP_MODE_TUPLE_SPACE) {
                if (this.protocol.getTsetObservType() == InspectorProtocol.ObsType.PROACTIVE) {
                    final LogicTuple[] ltSet = (LogicTuple[]) TupleCentreContainer
                            .doManagementOperation(
                                    TupleCentreOpType.GET_TSET, this.tcId,
                                    this.protocol.getTsetFilter());
                    msg.setTuples(new LinkedList<>());
                    if (ltSet != null) {
                        for (final LogicTuple lt : ltSet) {
                            msg.getTuples().add(lt);
                        }
                    }
                }
                if (this.protocol.getPendingQueryObservType() == InspectorProtocol.ObsType.PROACTIVE) {
                    final WSetEvent[] ltSet = (WSetEvent[]) TupleCentreContainer
                            .doManagementOperation(
                                    TupleCentreOpType.GET_WSET, this.tcId,
                                    this.protocol.getWsetFilter());
                    msg.setWnEvents(new LinkedList<>());
                    if (ltSet != null) {
                        for (final WSetEvent lt : ltSet) {
                            msg.getWnEvents().add(lt);
                        }
                    }
                }
                this.dialog.sendInspectorEvent(msg);
            } else if (ev.getType() == ObservableEventExt.TYPE_REACTIONOK) {
                if (this.protocol.getReactionsObservType() != InspectorProtocol.ObsType.DISABLED) {
                    final TriggeredReaction zCopy = new TriggeredReaction(null,
                            ((ObservableEventReactionOK) ev).getZ()
                                    .getReaction());
                    /* Dradi */
                    if (zCopy.getReaction() instanceof LogicReaction) {
                        ((LogicReaction) zCopy.getReaction())
                                .getStructReaction().resolveTerm();
                    }
                    /* Dradi */
                    msg.setReactionOk(zCopy);
                    this.dialog.sendInspectorEvent(msg);
                }
            } else if (ev.getType() == ObservableEventExt.TYPE_REACTIONFAIL
                    && this.protocol.getReactionsObservType() != InspectorProtocol.ObsType.DISABLED) {
                final TriggeredReaction zCopy = new TriggeredReaction(null,
                        ((ObservableEventReactionFail) ev).getZ().getReaction());
                msg.setReactionFailed(zCopy);
                this.dialog.sendInspectorEvent(msg);
            }
        } catch (final InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (final DialogSendException e) {
            this.log("Inspector quit");
            LOGGER.error(e.getMessage(), e);
        }
    }

    @Override
    public void operationCompleted(final AbstractTupleCentreOperation op) {
        // FIXME What to do here?
    }

    /**
     * reset the tuple centre VM
     */
    public synchronized void reset() {
        TupleCentreContainer.doManagementOperation(TupleCentreOpType.RESET,
                this.tcId, null);
    }

    @Override
    public void run() {
        try {
            TupleCentreContainer.doManagementOperation(
                    TupleCentreOpType.ADD_INSP, this.tcId, this);
            while (!this.shutdown) {
                final NodeMessage msg = this.dialog.receiveNodeMsg();
                final Class<?> cl = msg.getClass();
                final Method m = this.getClass().getMethod(msg.getAction(),
                        cl);
                m.invoke(this, msg);
            }
            this.dialog.end();
            TupleCentreContainer.doManagementOperation(
                    TupleCentreOpType.RMV_INSP, this.tcId, this);
        } catch (final NoSuchMethodException | DialogException | InvocationTargetException | IllegalArgumentException | IllegalAccessException | SecurityException e) {
            LOGGER.error(e.getMessage(), e);
        }
        this.manager.shutdownContext(this.ctxId, this.agentId);
    }

    /**
     * set a new tuple set
     *
     * @param m the set InQ message
     */
    public synchronized void setEventSet(final SetEventSetMessage m) {
        TupleCentreContainer.doManagementOperation(
                TupleCentreOpType.SET_WSET, this.tcId, m.getEventWnSet());
    }

    /**
     * setting new observation protocol
     *
     * @param msg the set protocol message
     */
    public synchronized void setProtocol(final SetProtocolMessageDefault msg) {
        final boolean wasTracing = this.protocol.isTracing();
        this.protocol = msg.getInfo();
        if (wasTracing) {
            this.notifyAll();
        }
        if (!this.protocol.isTracing()) {
            this.onInspectableEvent(new InspectableEvent(this,
                    InspectableEvent.TYPE_NEWSTATE));
        }
    }

    /**
     * set a new tuple set
     *
     * @param m the set tuples message
     */
    public synchronized void setTupleSet(final SetTupleSetMessage m) {
        try {
            // Operation Make
            final RespectOperationDefault opRequested = RespectOperationDefault.make(
                    TupleCentreOpType.SET_S, (LogicTuple) m.getTupleSet(),
                    null);
            // InputEvent Creation
            final InputEvent ev = new InputEvent(this.agentId, opRequested,
                    this.tcId, System.currentTimeMillis(), null);
            TupleCentreContainer.doBlockingOperation(ev);
            // TupleCentreContainer.doBlockingOperation(TupleCentreOpType.SET,
            // this.agentId, this.tcId, m.getTupleSet());
        } catch (final TucsonInvalidLogicTupleException | InvalidLogicTupleException | TucsonOperationNotPossibleException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    /**
     * enable/disable VM step Mode
     *
     * @param m the step mode message
     */
    public void stepMode(final StepModeMessage m) {
        TupleCentreContainer.doManagementOperation(
                TupleCentreOpType.STEP_MODE, this.tcId, null);
        final ArrayList<InspectableEventListener> inspectors = (ArrayList<InspectableEventListener>) TupleCentreContainer
                .doManagementOperation(TupleCentreOpType.GET_INSPS,
                        this.tcId, null);
        for (final InspectableEventListener insp : Objects.requireNonNull(inspectors)) {
            final InspectorContextSkel skel = (InspectorContextSkel) insp;
            if (skel.getId() == this.getId()) {
                continue;
            }
            final InspectorContextEvent msg = new InspectorContextEventDefault();
            msg.setModeChanged(true);
            try {
                skel.getDialog().sendInspectorEvent(msg);
            } catch (final DialogException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }

    /**
     * @return InspectorContextSke dialog
     */
    private TucsonProtocol getDialog() {
        return this.dialog;
    }

    /**
     * @param st the String to log
     */
    protected void log(final String st) {
        LOGGER.info("[InspectorContextSkel]: " + st);
    }
}
