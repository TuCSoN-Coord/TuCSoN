/*
 * Created on Dec 6, 2004 To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package alice.tuplecentre.tucson.service;

import alice.tuple.Tuple;
import alice.tuple.TupleTemplate;
import alice.tuple.logic.LogicTuple;
import alice.tuple.logic.LogicTuples;
import alice.tuple.logic.TupleArguments;
import alice.tuple.logic.exceptions.InvalidLogicTupleException;
import alice.tuplecentre.api.EmitterIdentifier;
import alice.tuplecentre.api.TupleCentreIdentifier;
import alice.tuplecentre.core.InputEvent;
import alice.tuplecentre.core.TupleCentreOpType;
import alice.tuplecentre.respect.core.RespectOperationDefault;
import alice.tuplecentre.tucson.api.TucsonAgentId;
import alice.tuplecentre.tucson.api.TucsonAgentIdDefault;
import alice.tuplecentre.tucson.api.TucsonTupleCentreId;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidLogicTupleException;
import alice.tuplecentre.tucson.api.exceptions.TucsonOperationNotPossibleException;

/**
 * @author Alessandro Ricci
 * @author (contributor) Michele Bombardi (mailto:
 * michele.bombardi@studio.unibo.it)
 */
public class ObservationService implements NodeServiceListener {
    private TucsonAgentId obsAid;
    private final TucsonTupleCentreId obsContext;

    /**
     * @param ctx the identifier of the tuple centre under observation
     */
    public ObservationService(final TucsonTupleCentreId ctx) {
        this.obsContext = ctx;
        try {
            this.obsAid = new TucsonAgentIdDefault("obs_agent");
        } catch (final TucsonInvalidAgentIdException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void accEntered(final TucsonAgentId aid) {
        try {
            // Operation Make
            final RespectOperationDefault opRequested = RespectOperationDefault.make(
                    TupleCentreOpType.OUT,
                    LogicTuples.newInstance("node_event", TupleArguments.newVarArgument(), TupleArguments.newValueArgument(
                            "new_agent", TupleArguments.newValueArgument(aid.toString()))), null);
            // InputEvent Creation
            final InputEvent ev = new InputEvent(this.obsAid, opRequested,
                    this.obsContext, System.currentTimeMillis(), null);
            TupleCentreContainer.doBlockingOperation(ev);
            // TupleCentreContainer.doBlockingOperation(TupleCentreOpType.OUT,
            // this.obsAid, this.obsContext,
            // LogicTuples.newInstance("node_event", TupleArguments.newVarArgument(), TupleArguments.newValueArgument(
            // "new_agent", TupleArguments.newValueArgument(aid.toString()))));
        } catch (final TucsonInvalidLogicTupleException | InvalidLogicTupleException | TucsonOperationNotPossibleException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void accQuit(final TucsonAgentId aid) {
        try {
            // Operation Make
            final RespectOperationDefault opRequested = RespectOperationDefault.make(
                    TupleCentreOpType.OUT,
                    LogicTuples.newInstance("node_event", TupleArguments.newVarArgument(), TupleArguments.newValueArgument(
                            "exit_agent", TupleArguments.newValueArgument(aid.toString()))), null);
            // InputEvent Creation
            final InputEvent ev = new InputEvent(this.obsAid, opRequested,
                    this.obsContext, System.currentTimeMillis(), null);
            TupleCentreContainer.doBlockingOperation(ev);
            // TupleCentreContainer.doBlockingOperation(TupleCentreOpType.OUT,
            // this.obsAid, this.obsContext,
            // LogicTuples.newInstance("node_event", TupleArguments.newVarArgument(), TupleArguments.newValueArgument(
            // "exit_agent", TupleArguments.newValueArgument(aid.toString()))));
        } catch (final TucsonInvalidLogicTupleException | InvalidLogicTupleException | TucsonOperationNotPossibleException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getSpecCompleted(final TupleCentreIdentifier tid, final EmitterIdentifier id,
                                 final String spec) {
        try {
            // Operation Make
            final RespectOperationDefault opRequested = RespectOperationDefault.make(
                    TupleCentreOpType.OUT,
                    LogicTuples.newInstance("node_event", TupleArguments.newVarArgument(), TupleArguments.newValueArgument(
                            "completed_getSpec", TupleArguments.newInstance(
                                    tid.toTerm()),
                            TupleArguments.newValueArgument(id.toString()),
                            TupleArguments.newValueArgument(spec))), null);
            // InputEvent Creation
            final InputEvent ev = new InputEvent(this.obsAid, opRequested,
                    this.obsContext, System.currentTimeMillis(), null);
            TupleCentreContainer.doBlockingOperation(ev);
            // TupleCentreContainer.doBlockingOperation(TupleCentreOpType.OUT,
            // this.obsAid, this.obsContext,
            // LogicTuples.newInstance("node_event", TupleArguments.newVarArgument(), TupleArguments.newValueArgument(
            // "completed_getSpec", TupleArguments.newInstance(
            // ((TucsonTupleCentreId) tid).toTerm()),
            // TupleArguments.newValueArgument(((TucsonAgentId) id).toString()),
            // TupleArguments.newValueArgument(spec))));
        } catch (final TucsonInvalidLogicTupleException | InvalidLogicTupleException | TucsonOperationNotPossibleException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getSpecRequested(final TupleCentreIdentifier tid, final EmitterIdentifier id) {
        try {
            // Operation Make
            final RespectOperationDefault opRequested = RespectOperationDefault.make(
                    TupleCentreOpType.OUT,
                    LogicTuples.newInstance("node_event", TupleArguments.newVarArgument(), TupleArguments.newValueArgument(
                            "requested_getSpec", TupleArguments.newInstance(
                                    tid.toTerm()),
                            TupleArguments.newValueArgument(id.toString()))), null);
            // InputEvent Creation
            final InputEvent ev = new InputEvent(this.obsAid, opRequested,
                    this.obsContext, System.currentTimeMillis(), null);
            TupleCentreContainer.doBlockingOperation(ev);
            // TupleCentreContainer.doBlockingOperation(TupleCentreOpType.OUT,
            // this.obsAid, this.obsContext,
            // LogicTuples.newInstance("node_event", TupleArguments.newVarArgument(), TupleArguments.newValueArgument(
            // "requested_getSpec", TupleArguments.newInstance(
            // ((TucsonTupleCentreId) tid).toTerm()),
            // TupleArguments.newValueArgument(((TucsonAgentId) id).toString()))));
        } catch (final TucsonInvalidLogicTupleException | InvalidLogicTupleException | TucsonOperationNotPossibleException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void inCompleted(final TupleCentreIdentifier tid, final EmitterIdentifier id, final Tuple t) {
        try {
            // Operation Make
            final RespectOperationDefault opRequested = RespectOperationDefault.make(
                    TupleCentreOpType.OUT,
                    LogicTuples.newInstance("node_event", TupleArguments.newVarArgument(), TupleArguments.newValueArgument(
                            "completed_in", TupleArguments.newInstance(
                                    tid.toTerm()),
                            TupleArguments.newValueArgument(id.toString()),
                            TupleArguments.newInstance(((LogicTuple) t).toTerm()))),
                    null);
            // InputEvent Creation
            final InputEvent ev = new InputEvent(this.obsAid, opRequested,
                    this.obsContext, System.currentTimeMillis(), null);
            TupleCentreContainer.doBlockingOperation(ev);
            // TupleCentreContainer.doBlockingOperation(TupleCentreOpType.OUT,
            // this.obsAid, this.obsContext,
            // LogicTuples.newInstance("node_event", TupleArguments.newVarArgument(), TupleArguments.newValueArgument(
            // "completed_in", TupleArguments.newInstance(
            // ((TucsonTupleCentreId) tid).toTerm()),
            // TupleArguments.newValueArgument(((TucsonAgentId) id).toString()),
            // TupleArguments.newInstance(((LogicTuple) t).toTerm()))));
        } catch (final TucsonInvalidLogicTupleException | InvalidLogicTupleException | TucsonOperationNotPossibleException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void inpCompleted(final TupleCentreIdentifier tid, final EmitterIdentifier id,
                             final Tuple t) {
        try {
            // Operation Make
            RespectOperationDefault opRequested;
            if (t != null) {
                opRequested = RespectOperationDefault.make(
                        TupleCentreOpType.OUT, LogicTuples.newInstance("node_event", TupleArguments.newVarArgument(),
                                TupleArguments.newValueArgument("completed_inp", TupleArguments.newInstance(
                                        tid.toTerm()),
                                        TupleArguments.newValueArgument(id.toString()),
                                        TupleArguments.newValueArgument("succeeded", TupleArguments.newInstance(
                                                ((LogicTuple) t).toTerm())))), null);
                // TupleCentreContainer.doBlockingOperation(TucsonOperationDefault
                // .outCode(), this.obsAid, this.obsContext,
                // LogicTuples.newInstance("node_event", TupleArguments.newVarArgument(), TupleArguments.newValueArgument(
                // "completed_inp", TupleArguments.newInstance(
                // ((TucsonTupleCentreId) tid).toTerm()),
                // TupleArguments.newValueArgument(((TucsonAgentId) id).toString()),
                // TupleArguments.newValueArgument("succeeded", TupleArguments.newInstance(
                // ((LogicTuple) t).toTerm())))));
            } else {
                opRequested = RespectOperationDefault.make(
                        TupleCentreOpType.OUT, LogicTuples.newInstance("node_event", TupleArguments.newVarArgument(),
                                TupleArguments.newValueArgument("completed_inp", TupleArguments.newInstance(
                                        tid.toTerm()),
                                        TupleArguments.newValueArgument(id.toString()),
                                        TupleArguments.newValueArgument("failed"))), null);
                // TupleCentreContainer.doBlockingOperation(TucsonOperationDefault
                // .outCode(), this.obsAid, this.obsContext,
                // LogicTuples.newInstance("node_event", TupleArguments.newVarArgument(), TupleArguments.newValueArgument(
                // "completed_inp", TupleArguments.newInstance(
                // ((TucsonTupleCentreId) tid).toTerm()),
                // TupleArguments.newValueArgument(((TucsonAgentId) id).toString()),
                // TupleArguments.newValueArgument("failed"))));
            }
            // InputEvent Creation
            final InputEvent ev = new InputEvent(this.obsAid, opRequested,
                    this.obsContext, System.currentTimeMillis(), null);
            TupleCentreContainer.doBlockingOperation(ev);
        } catch (final TucsonOperationNotPossibleException | InvalidLogicTupleException | TucsonInvalidLogicTupleException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void inpRequested(final TupleCentreIdentifier tid, final EmitterIdentifier id,
                             final TupleTemplate t) {
        try {
            // Operation Make
            final RespectOperationDefault opRequested = RespectOperationDefault.make(
                    TupleCentreOpType.OUT,
                    LogicTuples.newInstance("node_event", TupleArguments.newVarArgument(), TupleArguments.newValueArgument(
                            "requested_inp", TupleArguments.newInstance(
                                    tid.toTerm()),
                            TupleArguments.newValueArgument(id.toString()),
                            TupleArguments.newInstance(((LogicTuple) t).toTerm()))),
                    null);
            // InputEvent Creation
            final InputEvent ev = new InputEvent(this.obsAid, opRequested,
                    this.obsContext, System.currentTimeMillis(), null);
            TupleCentreContainer.doBlockingOperation(ev);
            // TupleCentreContainer.doBlockingOperation(TupleCentreOpType.OUT,
            // this.obsAid, this.obsContext,
            // LogicTuples.newInstance("node_event", TupleArguments.newVarArgument(), TupleArguments.newValueArgument(
            // "requested_inp", TupleArguments.newInstance(
            // ((TucsonTupleCentreId) tid).toTerm()),
            // TupleArguments.newValueArgument(((TucsonAgentId) id).toString()),
            // TupleArguments.newInstance(((LogicTuple) t).toTerm()))));
        } catch (final TucsonInvalidLogicTupleException | InvalidLogicTupleException | TucsonOperationNotPossibleException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void inRequested(final TupleCentreIdentifier tid, final EmitterIdentifier id,
                            final TupleTemplate t) {
        try {
            // Operation Make
            final RespectOperationDefault opRequested = RespectOperationDefault.make(
                    TupleCentreOpType.OUT,
                    LogicTuples.newInstance("node_event", TupleArguments.newVarArgument(), TupleArguments.newValueArgument(
                            "requested_in", TupleArguments.newInstance(
                                    tid.toTerm()),
                            TupleArguments.newValueArgument(id.toString()),
                            TupleArguments.newInstance(((LogicTuple) t).toTerm()))),
                    null);
            // InputEvent Creation
            final InputEvent ev = new InputEvent(this.obsAid, opRequested,
                    this.obsContext, System.currentTimeMillis(), null);
            TupleCentreContainer.doBlockingOperation(ev);
            // TupleCentreContainer.doBlockingOperation(TupleCentreOpType.OUT,
            // this.obsAid, this.obsContext,
            // LogicTuples.newInstance("node_event", TupleArguments.newVarArgument(), TupleArguments.newValueArgument(
            // "requested_in", TupleArguments.newInstance(
            // ((TucsonTupleCentreId) tid).toTerm()),
            // TupleArguments.newValueArgument(((TucsonAgentId) id).toString()),
            // TupleArguments.newInstance(((LogicTuple) t).toTerm()))));
        } catch (final TucsonInvalidLogicTupleException | InvalidLogicTupleException | TucsonOperationNotPossibleException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void outRequested(final TupleCentreIdentifier tid, final EmitterIdentifier id,
                             final Tuple t) {
        try {
            // Operation Make
            final RespectOperationDefault opRequested = RespectOperationDefault.make(
                    TupleCentreOpType.OUT,
                    LogicTuples.newInstance("node_event", TupleArguments.newVarArgument(), TupleArguments.newValueArgument(
                            "done_out", TupleArguments.newInstance(
                                    tid.toTerm()),
                            TupleArguments.newValueArgument(id.toString()),
                            TupleArguments.newInstance(((LogicTuple) t).toTerm()))),
                    null);
            // InputEvent Creation
            final InputEvent ev = new InputEvent(this.obsAid, opRequested,
                    this.obsContext, System.currentTimeMillis(), null);
            TupleCentreContainer.doBlockingOperation(ev);
            // TupleCentreContainer.doBlockingOperation(TupleCentreOpType.OUT,
            // this.obsAid, this.obsContext,
            // LogicTuples.newInstance("node_event", TupleArguments.newVarArgument(), TupleArguments.newValueArgument(
            // "done_out", TupleArguments.newInstance(
            // ((TucsonTupleCentreId) tid).toTerm()),
            // TupleArguments.newValueArgument(((TucsonAgentId) id).toString()),
            // TupleArguments.newInstance(((LogicTuple) t).toTerm()))));
        } catch (final TucsonInvalidLogicTupleException | InvalidLogicTupleException | TucsonOperationNotPossibleException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void rdCompleted(final TupleCentreIdentifier tid, final EmitterIdentifier id, final Tuple t) {
        try {
            // Operation Make
            final RespectOperationDefault opRequested = RespectOperationDefault.make(
                    TupleCentreOpType.OUT,
                    LogicTuples.newInstance("node_event", TupleArguments.newVarArgument(), TupleArguments.newValueArgument(
                            "completed_rd", TupleArguments.newInstance(
                                    tid.toTerm()),
                            TupleArguments.newValueArgument(id.toString()),
                            TupleArguments.newInstance(((LogicTuple) t).toTerm()))),
                    null);
            // InputEvent Creation
            final InputEvent ev = new InputEvent(this.obsAid, opRequested,
                    this.obsContext, System.currentTimeMillis(), null);
            TupleCentreContainer.doBlockingOperation(ev);
            // TupleCentreContainer.doBlockingOperation(TupleCentreOpType.OUT,
            // this.obsAid, this.obsContext,
            // LogicTuples.newInstance("node_event", TupleArguments.newVarArgument(), TupleArguments.newValueArgument(
            // "completed_rd", TupleArguments.newInstance(
            // ((TucsonTupleCentreId) tid).toTerm()),
            // TupleArguments.newValueArgument(((TucsonAgentId) id).toString()),
            // TupleArguments.newInstance(((LogicTuple) t).toTerm()))));
        } catch (final TucsonInvalidLogicTupleException | InvalidLogicTupleException | TucsonOperationNotPossibleException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void rdpCompleted(final TupleCentreIdentifier tid, final EmitterIdentifier id,
                             final Tuple t) {
        try {
            // Operation Make
            RespectOperationDefault opRequested;
            if (t != null) {
                opRequested = RespectOperationDefault.make(
                        TupleCentreOpType.OUT, LogicTuples.newInstance("node_event", TupleArguments.newVarArgument(),
                                TupleArguments.newValueArgument("completed_rdp", TupleArguments.newInstance(
                                        tid.toTerm()),
                                        TupleArguments.newValueArgument(id.toString()),
                                        TupleArguments.newValueArgument("succeeded", TupleArguments.newInstance(
                                                ((LogicTuple) t).toTerm())))), null);
                // TupleCentreContainer.doBlockingOperation(TucsonOperationDefault
                // .outCode(), this.obsAid, this.obsContext,
                // LogicTuples.newInstance("node_event", TupleArguments.newVarArgument(), TupleArguments.newValueArgument(
                // "completed_rdp", TupleArguments.newInstance(
                // ((TucsonTupleCentreId) tid).toTerm()),
                // TupleArguments.newValueArgument(((TucsonAgentId) id).toString()),
                // TupleArguments.newValueArgument("succeeded", TupleArguments.newInstance(
                // ((LogicTuple) t).toTerm())))));
            } else {
                opRequested = RespectOperationDefault.make(
                        TupleCentreOpType.OUT, LogicTuples.newInstance("node_event", TupleArguments.newVarArgument(),
                                TupleArguments.newValueArgument("completed_rdp", TupleArguments.newInstance(
                                        tid.toTerm()),
                                        TupleArguments.newValueArgument(id.toString()),
                                        TupleArguments.newValueArgument("failed"))), null);
                // TupleCentreContainer.doBlockingOperation(TucsonOperationDefault
                // .outCode(), this.obsAid, this.obsContext,
                // LogicTuples.newInstance("node_event", TupleArguments.newVarArgument(), TupleArguments.newValueArgument(
                // "completed_rdp", TupleArguments.newInstance(
                // ((TucsonTupleCentreId) tid).toTerm()),
                // TupleArguments.newValueArgument(((TucsonAgentId) id).toString()),
                // TupleArguments.newValueArgument("failed"))));
            }
            // InputEvent Creation
            final InputEvent ev = new InputEvent(this.obsAid, opRequested,
                    this.obsContext, System.currentTimeMillis(), null);
            TupleCentreContainer.doBlockingOperation(ev);
        } catch (final TucsonOperationNotPossibleException | InvalidLogicTupleException | TucsonInvalidLogicTupleException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void rdpRequested(final TupleCentreIdentifier tid, final EmitterIdentifier id,
                             final TupleTemplate t) {
        try {
            // Operation Make
            final RespectOperationDefault opRequested = RespectOperationDefault.make(
                    TupleCentreOpType.OUT,
                    LogicTuples.newInstance("node_event", TupleArguments.newVarArgument(), TupleArguments.newValueArgument(
                            "requested_rdp", TupleArguments.newInstance(
                                    tid.toTerm()),
                            TupleArguments.newValueArgument(id.toString()),
                            TupleArguments.newInstance(((LogicTuple) t).toTerm()))),
                    null);
            // InputEvent Creation
            final InputEvent ev = new InputEvent(this.obsAid, opRequested,
                    this.obsContext, System.currentTimeMillis(), null);
            TupleCentreContainer.doBlockingOperation(ev);
            // TupleCentreContainer.doBlockingOperation(TupleCentreOpType.OUT,
            // this.obsAid, this.obsContext,
            // LogicTuples.newInstance("node_event", TupleArguments.newVarArgument(), TupleArguments.newValueArgument(
            // "requested_rdp", TupleArguments.newInstance(
            // ((TucsonTupleCentreId) tid).toTerm()),
            // TupleArguments.newValueArgument(((TucsonAgentId) id).toString()),
            // TupleArguments.newInstance(((LogicTuple) t).toTerm()))));
        } catch (final TucsonInvalidLogicTupleException | InvalidLogicTupleException | TucsonOperationNotPossibleException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void rdRequested(final TupleCentreIdentifier tid, final EmitterIdentifier id,
                            final TupleTemplate t) {
        try {
            // Operation Make
            final RespectOperationDefault opRequested = RespectOperationDefault.make(
                    TupleCentreOpType.OUT,
                    LogicTuples.newInstance("node_event", TupleArguments.newVarArgument(), TupleArguments.newValueArgument(
                            "requested_rd", TupleArguments.newInstance(
                                    tid.toTerm()),
                            TupleArguments.newValueArgument(id.toString()),
                            TupleArguments.newInstance(((LogicTuple) t).toTerm()))),
                    null);
            // InputEvent Creation
            final InputEvent ev = new InputEvent(this.obsAid, opRequested,
                    this.obsContext, System.currentTimeMillis(), null);
            TupleCentreContainer.doBlockingOperation(ev);
            // TupleCentreContainer.doBlockingOperation(TupleCentreOpType.OUT,
            // this.obsAid, this.obsContext,
            // LogicTuples.newInstance("node_event", TupleArguments.newVarArgument(), TupleArguments.newValueArgument(
            // "requested_rd", TupleArguments.newInstance(
            // ((TucsonTupleCentreId) tid).toTerm()),
            // TupleArguments.newValueArgument(((TucsonAgentId) id).toString()),
            // TupleArguments.newInstance(((LogicTuple) t).toTerm()))));
        } catch (final TucsonInvalidLogicTupleException | InvalidLogicTupleException | TucsonOperationNotPossibleException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setSpecCompleted(final TupleCentreIdentifier tid, final EmitterIdentifier id) {
        try {
            // Operation Make
            final RespectOperationDefault opRequested = RespectOperationDefault.make(
                    TupleCentreOpType.OUT,
                    LogicTuples.newInstance("node_event", TupleArguments.newVarArgument(), TupleArguments.newValueArgument(
                            "completed_setSpec", TupleArguments.newInstance(
                                    tid.toTerm()),
                            TupleArguments.newValueArgument(id.toString()))), null);
            // InputEvent Creation
            final InputEvent ev = new InputEvent(this.obsAid, opRequested,
                    this.obsContext, System.currentTimeMillis(), null);
            TupleCentreContainer.doBlockingOperation(ev);
            // TupleCentreContainer.doBlockingOperation(TupleCentreOpType.OUT,
            // this.obsAid, this.obsContext,
            // LogicTuples.newInstance("node_event", TupleArguments.newVarArgument(), TupleArguments.newValueArgument(
            // "completed_setSpec", TupleArguments.newInstance(
            // ((TucsonTupleCentreId) tid).toTerm()),
            // TupleArguments.newValueArgument(((TucsonAgentId) id).toString()))));
        } catch (final TucsonInvalidLogicTupleException | InvalidLogicTupleException | TucsonOperationNotPossibleException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setSpecRequested(final TupleCentreIdentifier tid, final EmitterIdentifier id,
                                 final String spec) {
        try {
            // Operation Make
            final RespectOperationDefault opRequested = RespectOperationDefault.make(
                    TupleCentreOpType.OUT,
                    LogicTuples.newInstance("node_event", TupleArguments.newVarArgument(), TupleArguments.newValueArgument(
                            "requested_setSpec", TupleArguments.newInstance(
                                    tid.toTerm()),
                            TupleArguments.newValueArgument(id.toString()),
                            TupleArguments.newValueArgument(spec))), null);
            // InputEvent Creation
            final InputEvent ev = new InputEvent(this.obsAid, opRequested,
                    this.obsContext, System.currentTimeMillis(), null);
            TupleCentreContainer.doBlockingOperation(ev);
            // TupleCentreContainer.doBlockingOperation(TupleCentreOpType.OUT,
            // this.obsAid, this.obsContext,
            // LogicTuples.newInstance("node_event", TupleArguments.newVarArgument(), TupleArguments.newValueArgument(
            // "requested_setSpec", TupleArguments.newInstance(
            // ((TucsonTupleCentreId) tid).toTerm()),
            // TupleArguments.newValueArgument(((TucsonAgentId) id).toString()),
            // TupleArguments.newValueArgument(spec))));
        } catch (final TucsonInvalidLogicTupleException | InvalidLogicTupleException | TucsonOperationNotPossibleException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void tcCreated(final TucsonTupleCentreId tid) {
        try {
            // Operation Make
            final RespectOperationDefault opRequested = RespectOperationDefault.make(
                    TupleCentreOpType.OUT, LogicTuples.newInstance("node_event",
                            TupleArguments.newVarArgument(), TupleArguments.newValueArgument("new_tc", TupleArguments.newInstance(
                                    tid.toTerm()))), null);
            // InputEvent Creation
            final InputEvent ev = new InputEvent(this.obsAid, opRequested,
                    this.obsContext, System.currentTimeMillis(), null);
            TupleCentreContainer.doBlockingOperation(ev);
            // TupleCentreContainer.doBlockingOperation(TupleCentreOpType.OUT,
            // this.obsAid, this.obsContext, LogicTuples.newInstance("node_event",
            // TupleArguments.newVarArgument(), TupleArguments.newValueArgument("new_tc", TupleArguments.newInstance(
            // tid.toTerm()))));
        } catch (final TucsonInvalidLogicTupleException | InvalidLogicTupleException | TucsonOperationNotPossibleException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void tcDestroyed(final TucsonTupleCentreId tid) {
        try {
            // Operation Make
            final RespectOperationDefault opRequested = RespectOperationDefault.make(
                    TupleCentreOpType.OUT, LogicTuples.newInstance("node_event",
                            TupleArguments.newVarArgument(), TupleArguments.newValueArgument("destoyed_tc",
                                    TupleArguments.newInstance(tid.toTerm()))), null);
            // InputEvent Creation
            final InputEvent ev = new InputEvent(this.obsAid, opRequested,
                    this.obsContext, System.currentTimeMillis(), null);
            TupleCentreContainer.doBlockingOperation(ev);
            // TupleCentreContainer.doBlockingOperation(TupleCentreOpType.OUT,
            // this.obsAid, this.obsContext, LogicTuples.newInstance("node_event",
            // TupleArguments.newVarArgument(), TupleArguments.newValueArgument("destoyed_tc",
            // TupleArguments.newInstance(tid.toTerm()))));
        } catch (final TucsonInvalidLogicTupleException | InvalidLogicTupleException | TucsonOperationNotPossibleException e) {
            e.printStackTrace();
        }
    }
}
