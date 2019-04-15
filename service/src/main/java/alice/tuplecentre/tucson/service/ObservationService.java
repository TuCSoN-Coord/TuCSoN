/*
 * Created on Dec 6, 2004 To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package alice.tuplecentre.tucson.service;

import alice.tuple.Tuple;
import alice.tuple.TupleTemplate;
import alice.tuple.logic.LogicTuple;
import alice.tuple.logic.TupleArgument;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

/**
 * @author Alessandro Ricci
 * @author (contributor) Michele Bombardi (mailto:
 * michele.bombardi@studio.unibo.it)
 */
public class ObservationService implements NodeServiceListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private TucsonAgentId obsAid;
    private final TucsonTupleCentreId obsContext;

    /**
     * @param ctx the identifier copyOf the tuple centre under observation
     */
    public ObservationService(final TucsonTupleCentreId ctx) {
        this.obsContext = ctx;
        try {
            this.obsAid = new TucsonAgentIdDefault("obs_agent");
        } catch (final TucsonInvalidAgentIdException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    @Override
    public void accEntered(final TucsonAgentId aid) {
        try {
            // Operation Make
            final RespectOperationDefault opRequested = RespectOperationDefault.make(
                    TupleCentreOpType.OUT,
                    LogicTuple.of("node_event", TupleArgument.var(), TupleArgument.of(
                            "new_agent", TupleArgument.of(aid.toString()))), null);
            // InputEvent Creation
            final InputEvent ev = new InputEvent(this.obsAid, opRequested,
                    this.obsContext, System.currentTimeMillis(), null);
            TupleCentreContainer.doBlockingOperation(ev);
            // TupleCentreContainer.doBlockingOperation(TupleCentreOpType.OUT,
            // this.obsAid, this.obsContext,
            // LogicTuples.fromTerm("node_event", TupleArguments.var(), TupleArguments.copyOf(
            // "new_agent", TupleArguments.copyOf(aid.toString()))));
        } catch (final TucsonInvalidLogicTupleException | InvalidLogicTupleException | TucsonOperationNotPossibleException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    @Override
    public void accQuit(final TucsonAgentId aid) {
        try {
            // Operation Make
            final RespectOperationDefault opRequested = RespectOperationDefault.make(
                    TupleCentreOpType.OUT,
                    LogicTuple.of("node_event", TupleArgument.var(), TupleArgument.of(
                            "exit_agent", TupleArgument.of(aid.toString()))), null);
            // InputEvent Creation
            final InputEvent ev = new InputEvent(this.obsAid, opRequested,
                    this.obsContext, System.currentTimeMillis(), null);
            TupleCentreContainer.doBlockingOperation(ev);
            // TupleCentreContainer.doBlockingOperation(TupleCentreOpType.OUT,
            // this.obsAid, this.obsContext,
            // LogicTuples.fromTerm("node_event", TupleArguments.var(), TupleArguments.copyOf(
            // "exit_agent", TupleArguments.copyOf(aid.toString()))));
        } catch (final TucsonInvalidLogicTupleException | InvalidLogicTupleException | TucsonOperationNotPossibleException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    @Override
    public void getSpecCompleted(final TupleCentreIdentifier tid, final EmitterIdentifier id,
                                 final String spec) {
        try {
            // Operation Make
            final RespectOperationDefault opRequested = RespectOperationDefault.make(
                    TupleCentreOpType.OUT,
                    LogicTuple.of("node_event", TupleArgument.var(), TupleArgument.of(
                            "completed_getSpec", TupleArgument.fromTerm(
                                    tid.toTerm()),
                            TupleArgument.of(id.toString()),
                            TupleArgument.of(spec))), null);
            // InputEvent Creation
            final InputEvent ev = new InputEvent(this.obsAid, opRequested,
                    this.obsContext, System.currentTimeMillis(), null);
            TupleCentreContainer.doBlockingOperation(ev);
            // TupleCentreContainer.doBlockingOperation(TupleCentreOpType.OUT,
            // this.obsAid, this.obsContext,
            // LogicTuples.fromTerm("node_event", TupleArguments.var(), TupleArguments.copyOf(
            // "completed_getSpec", TupleArguments.fromTerm(
            // ((TucsonTupleCentreId) tid).toTerm()),
            // TupleArguments.copyOf(((TucsonAgentId) id).toString()),
            // TupleArguments.copyOf(spec))));
        } catch (final TucsonInvalidLogicTupleException | InvalidLogicTupleException | TucsonOperationNotPossibleException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    @Override
    public void getSpecRequested(final TupleCentreIdentifier tid, final EmitterIdentifier id) {
        try {
            // Operation Make
            final RespectOperationDefault opRequested = RespectOperationDefault.make(
                    TupleCentreOpType.OUT,
                    LogicTuple.of("node_event", TupleArgument.var(), TupleArgument.of(
                            "requested_getSpec", TupleArgument.fromTerm(
                                    tid.toTerm()),
                            TupleArgument.of(id.toString()))), null);
            // InputEvent Creation
            final InputEvent ev = new InputEvent(this.obsAid, opRequested,
                    this.obsContext, System.currentTimeMillis(), null);
            TupleCentreContainer.doBlockingOperation(ev);
            // TupleCentreContainer.doBlockingOperation(TupleCentreOpType.OUT,
            // this.obsAid, this.obsContext,
            // LogicTuples.fromTerm("node_event", TupleArguments.var(), TupleArguments.copyOf(
            // "requested_getSpec", TupleArguments.fromTerm(
            // ((TucsonTupleCentreId) tid).toTerm()),
            // TupleArguments.copyOf(((TucsonAgentId) id).toString()))));
        } catch (final TucsonInvalidLogicTupleException | InvalidLogicTupleException | TucsonOperationNotPossibleException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    @Override
    public void inCompleted(final TupleCentreIdentifier tid, final EmitterIdentifier id, final Tuple t) {
        try {
            // Operation Make
            final RespectOperationDefault opRequested = RespectOperationDefault.make(
                    TupleCentreOpType.OUT,
                    LogicTuple.of("node_event", TupleArgument.var(), TupleArgument.of(
                            "completed_in", TupleArgument.fromTerm(
                                    tid.toTerm()),
                            TupleArgument.of(id.toString()),
                            TupleArgument.fromTerm(((LogicTuple) t).toTerm()))),
                    null);
            // InputEvent Creation
            final InputEvent ev = new InputEvent(this.obsAid, opRequested,
                    this.obsContext, System.currentTimeMillis(), null);
            TupleCentreContainer.doBlockingOperation(ev);
            // TupleCentreContainer.doBlockingOperation(TupleCentreOpType.OUT,
            // this.obsAid, this.obsContext,
            // LogicTuples.fromTerm("node_event", TupleArguments.var(), TupleArguments.copyOf(
            // "completed_in", TupleArguments.fromTerm(
            // ((TucsonTupleCentreId) tid).toTerm()),
            // TupleArguments.copyOf(((TucsonAgentId) id).toString()),
            // TupleArguments.fromTerm(((LogicTuple) t).toTerm()))));
        } catch (final TucsonInvalidLogicTupleException | InvalidLogicTupleException | TucsonOperationNotPossibleException e) {
            LOGGER.error(e.getMessage(), e);
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
                        TupleCentreOpType.OUT, LogicTuple.of("node_event", TupleArgument.var(),
                                TupleArgument.of("completed_inp", TupleArgument.fromTerm(
                                        tid.toTerm()),
                                        TupleArgument.of(id.toString()),
                                        TupleArgument.of("succeeded", TupleArgument.fromTerm(
                                                ((LogicTuple) t).toTerm())))), null);
                // TupleCentreContainer.doBlockingOperation(TucsonOperationDefault
                // .outCode(), this.obsAid, this.obsContext,
                // LogicTuples.fromTerm("node_event", TupleArguments.var(), TupleArguments.copyOf(
                // "completed_inp", TupleArguments.fromTerm(
                // ((TucsonTupleCentreId) tid).toTerm()),
                // TupleArguments.copyOf(((TucsonAgentId) id).toString()),
                // TupleArguments.copyOf("succeeded", TupleArguments.fromTerm(
                // ((LogicTuple) t).toTerm())))));
            } else {
                opRequested = RespectOperationDefault.make(
                        TupleCentreOpType.OUT, LogicTuple.of("node_event", TupleArgument.var(),
                                TupleArgument.of("completed_inp", TupleArgument.fromTerm(
                                        tid.toTerm()),
                                        TupleArgument.of(id.toString()),
                                        TupleArgument.of("failed"))), null);
                // TupleCentreContainer.doBlockingOperation(TucsonOperationDefault
                // .outCode(), this.obsAid, this.obsContext,
                // LogicTuples.fromTerm("node_event", TupleArguments.var(), TupleArguments.copyOf(
                // "completed_inp", TupleArguments.fromTerm(
                // ((TucsonTupleCentreId) tid).toTerm()),
                // TupleArguments.copyOf(((TucsonAgentId) id).toString()),
                // TupleArguments.copyOf("failed"))));
            }
            // InputEvent Creation
            final InputEvent ev = new InputEvent(this.obsAid, opRequested,
                    this.obsContext, System.currentTimeMillis(), null);
            TupleCentreContainer.doBlockingOperation(ev);
        } catch (final TucsonOperationNotPossibleException | InvalidLogicTupleException | TucsonInvalidLogicTupleException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    @Override
    public void inpRequested(final TupleCentreIdentifier tid, final EmitterIdentifier id,
                             final TupleTemplate t) {
        try {
            // Operation Make
            final RespectOperationDefault opRequested = RespectOperationDefault.make(
                    TupleCentreOpType.OUT,
                    LogicTuple.of("node_event", TupleArgument.var(), TupleArgument.of(
                            "requested_inp", TupleArgument.fromTerm(
                                    tid.toTerm()),
                            TupleArgument.of(id.toString()),
                            TupleArgument.fromTerm(((LogicTuple) t).toTerm()))),
                    null);
            // InputEvent Creation
            final InputEvent ev = new InputEvent(this.obsAid, opRequested,
                    this.obsContext, System.currentTimeMillis(), null);
            TupleCentreContainer.doBlockingOperation(ev);
            // TupleCentreContainer.doBlockingOperation(TupleCentreOpType.OUT,
            // this.obsAid, this.obsContext,
            // LogicTuples.fromTerm("node_event", TupleArguments.var(), TupleArguments.copyOf(
            // "requested_inp", TupleArguments.fromTerm(
            // ((TucsonTupleCentreId) tid).toTerm()),
            // TupleArguments.copyOf(((TucsonAgentId) id).toString()),
            // TupleArguments.fromTerm(((LogicTuple) t).toTerm()))));
        } catch (final TucsonInvalidLogicTupleException | InvalidLogicTupleException | TucsonOperationNotPossibleException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    @Override
    public void inRequested(final TupleCentreIdentifier tid, final EmitterIdentifier id,
                            final TupleTemplate t) {
        try {
            // Operation Make
            final RespectOperationDefault opRequested = RespectOperationDefault.make(
                    TupleCentreOpType.OUT,
                    LogicTuple.of("node_event", TupleArgument.var(), TupleArgument.of(
                            "requested_in", TupleArgument.fromTerm(
                                    tid.toTerm()),
                            TupleArgument.of(id.toString()),
                            TupleArgument.fromTerm(((LogicTuple) t).toTerm()))),
                    null);
            // InputEvent Creation
            final InputEvent ev = new InputEvent(this.obsAid, opRequested,
                    this.obsContext, System.currentTimeMillis(), null);
            TupleCentreContainer.doBlockingOperation(ev);
            // TupleCentreContainer.doBlockingOperation(TupleCentreOpType.OUT,
            // this.obsAid, this.obsContext,
            // LogicTuples.fromTerm("node_event", TupleArguments.var(), TupleArguments.copyOf(
            // "requested_in", TupleArguments.fromTerm(
            // ((TucsonTupleCentreId) tid).toTerm()),
            // TupleArguments.copyOf(((TucsonAgentId) id).toString()),
            // TupleArguments.fromTerm(((LogicTuple) t).toTerm()))));
        } catch (final TucsonInvalidLogicTupleException | InvalidLogicTupleException | TucsonOperationNotPossibleException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    @Override
    public void outRequested(final TupleCentreIdentifier tid, final EmitterIdentifier id,
                             final Tuple t) {
        try {
            // Operation Make
            final RespectOperationDefault opRequested = RespectOperationDefault.make(
                    TupleCentreOpType.OUT,
                    LogicTuple.of("node_event", TupleArgument.var(), TupleArgument.of(
                            "done_out", TupleArgument.fromTerm(
                                    tid.toTerm()),
                            TupleArgument.of(id.toString()),
                            TupleArgument.fromTerm(((LogicTuple) t).toTerm()))),
                    null);
            // InputEvent Creation
            final InputEvent ev = new InputEvent(this.obsAid, opRequested,
                    this.obsContext, System.currentTimeMillis(), null);
            TupleCentreContainer.doBlockingOperation(ev);
            // TupleCentreContainer.doBlockingOperation(TupleCentreOpType.OUT,
            // this.obsAid, this.obsContext,
            // LogicTuples.fromTerm("node_event", TupleArguments.var(), TupleArguments.copyOf(
            // "done_out", TupleArguments.fromTerm(
            // ((TucsonTupleCentreId) tid).toTerm()),
            // TupleArguments.copyOf(((TucsonAgentId) id).toString()),
            // TupleArguments.fromTerm(((LogicTuple) t).toTerm()))));
        } catch (final TucsonInvalidLogicTupleException | InvalidLogicTupleException | TucsonOperationNotPossibleException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    @Override
    public void rdCompleted(final TupleCentreIdentifier tid, final EmitterIdentifier id, final Tuple t) {
        try {
            // Operation Make
            final RespectOperationDefault opRequested = RespectOperationDefault.make(
                    TupleCentreOpType.OUT,
                    LogicTuple.of("node_event", TupleArgument.var(), TupleArgument.of(
                            "completed_rd", TupleArgument.fromTerm(
                                    tid.toTerm()),
                            TupleArgument.of(id.toString()),
                            TupleArgument.fromTerm(((LogicTuple) t).toTerm()))),
                    null);
            // InputEvent Creation
            final InputEvent ev = new InputEvent(this.obsAid, opRequested,
                    this.obsContext, System.currentTimeMillis(), null);
            TupleCentreContainer.doBlockingOperation(ev);
            // TupleCentreContainer.doBlockingOperation(TupleCentreOpType.OUT,
            // this.obsAid, this.obsContext,
            // LogicTuples.fromTerm("node_event", TupleArguments.var(), TupleArguments.copyOf(
            // "completed_rd", TupleArguments.fromTerm(
            // ((TucsonTupleCentreId) tid).toTerm()),
            // TupleArguments.copyOf(((TucsonAgentId) id).toString()),
            // TupleArguments.fromTerm(((LogicTuple) t).toTerm()))));
        } catch (final TucsonInvalidLogicTupleException | InvalidLogicTupleException | TucsonOperationNotPossibleException e) {
            LOGGER.error(e.getMessage(), e);
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
                        TupleCentreOpType.OUT, LogicTuple.of("node_event", TupleArgument.var(),
                                TupleArgument.of("completed_rdp", TupleArgument.fromTerm(
                                        tid.toTerm()),
                                        TupleArgument.of(id.toString()),
                                        TupleArgument.of("succeeded", TupleArgument.fromTerm(
                                                ((LogicTuple) t).toTerm())))), null);
                // TupleCentreContainer.doBlockingOperation(TucsonOperationDefault
                // .outCode(), this.obsAid, this.obsContext,
                // LogicTuples.fromTerm("node_event", TupleArguments.var(), TupleArguments.copyOf(
                // "completed_rdp", TupleArguments.fromTerm(
                // ((TucsonTupleCentreId) tid).toTerm()),
                // TupleArguments.copyOf(((TucsonAgentId) id).toString()),
                // TupleArguments.copyOf("succeeded", TupleArguments.fromTerm(
                // ((LogicTuple) t).toTerm())))));
            } else {
                opRequested = RespectOperationDefault.make(
                        TupleCentreOpType.OUT, LogicTuple.of("node_event", TupleArgument.var(),
                                TupleArgument.of("completed_rdp", TupleArgument.fromTerm(
                                        tid.toTerm()),
                                        TupleArgument.of(id.toString()),
                                        TupleArgument.of("failed"))), null);
                // TupleCentreContainer.doBlockingOperation(TucsonOperationDefault
                // .outCode(), this.obsAid, this.obsContext,
                // LogicTuples.fromTerm("node_event", TupleArguments.var(), TupleArguments.copyOf(
                // "completed_rdp", TupleArguments.fromTerm(
                // ((TucsonTupleCentreId) tid).toTerm()),
                // TupleArguments.copyOf(((TucsonAgentId) id).toString()),
                // TupleArguments.copyOf("failed"))));
            }
            // InputEvent Creation
            final InputEvent ev = new InputEvent(this.obsAid, opRequested,
                    this.obsContext, System.currentTimeMillis(), null);
            TupleCentreContainer.doBlockingOperation(ev);
        } catch (final TucsonOperationNotPossibleException | InvalidLogicTupleException | TucsonInvalidLogicTupleException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    @Override
    public void rdpRequested(final TupleCentreIdentifier tid, final EmitterIdentifier id,
                             final TupleTemplate t) {
        try {
            // Operation Make
            final RespectOperationDefault opRequested = RespectOperationDefault.make(
                    TupleCentreOpType.OUT,
                    LogicTuple.of("node_event", TupleArgument.var(), TupleArgument.of(
                            "requested_rdp", TupleArgument.fromTerm(
                                    tid.toTerm()),
                            TupleArgument.of(id.toString()),
                            TupleArgument.fromTerm(((LogicTuple) t).toTerm()))),
                    null);
            // InputEvent Creation
            final InputEvent ev = new InputEvent(this.obsAid, opRequested,
                    this.obsContext, System.currentTimeMillis(), null);
            TupleCentreContainer.doBlockingOperation(ev);
            // TupleCentreContainer.doBlockingOperation(TupleCentreOpType.OUT,
            // this.obsAid, this.obsContext,
            // LogicTuples.fromTerm("node_event", TupleArguments.var(), TupleArguments.copyOf(
            // "requested_rdp", TupleArguments.fromTerm(
            // ((TucsonTupleCentreId) tid).toTerm()),
            // TupleArguments.copyOf(((TucsonAgentId) id).toString()),
            // TupleArguments.fromTerm(((LogicTuple) t).toTerm()))));
        } catch (final TucsonInvalidLogicTupleException | InvalidLogicTupleException | TucsonOperationNotPossibleException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    @Override
    public void rdRequested(final TupleCentreIdentifier tid, final EmitterIdentifier id,
                            final TupleTemplate t) {
        try {
            // Operation Make
            final RespectOperationDefault opRequested = RespectOperationDefault.make(
                    TupleCentreOpType.OUT,
                    LogicTuple.of("node_event", TupleArgument.var(), TupleArgument.of(
                            "requested_rd", TupleArgument.fromTerm(
                                    tid.toTerm()),
                            TupleArgument.of(id.toString()),
                            TupleArgument.fromTerm(((LogicTuple) t).toTerm()))),
                    null);
            // InputEvent Creation
            final InputEvent ev = new InputEvent(this.obsAid, opRequested,
                    this.obsContext, System.currentTimeMillis(), null);
            TupleCentreContainer.doBlockingOperation(ev);
            // TupleCentreContainer.doBlockingOperation(TupleCentreOpType.OUT,
            // this.obsAid, this.obsContext,
            // LogicTuples.fromTerm("node_event", TupleArguments.var(), TupleArguments.copyOf(
            // "requested_rd", TupleArguments.fromTerm(
            // ((TucsonTupleCentreId) tid).toTerm()),
            // TupleArguments.copyOf(((TucsonAgentId) id).toString()),
            // TupleArguments.fromTerm(((LogicTuple) t).toTerm()))));
        } catch (final TucsonInvalidLogicTupleException | InvalidLogicTupleException | TucsonOperationNotPossibleException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    @Override
    public void setSpecCompleted(final TupleCentreIdentifier tid, final EmitterIdentifier id) {
        try {
            // Operation Make
            final RespectOperationDefault opRequested = RespectOperationDefault.make(
                    TupleCentreOpType.OUT,
                    LogicTuple.of("node_event", TupleArgument.var(), TupleArgument.of(
                            "completed_setSpec", TupleArgument.fromTerm(
                                    tid.toTerm()),
                            TupleArgument.of(id.toString()))), null);
            // InputEvent Creation
            final InputEvent ev = new InputEvent(this.obsAid, opRequested,
                    this.obsContext, System.currentTimeMillis(), null);
            TupleCentreContainer.doBlockingOperation(ev);
            // TupleCentreContainer.doBlockingOperation(TupleCentreOpType.OUT,
            // this.obsAid, this.obsContext,
            // LogicTuples.fromTerm("node_event", TupleArguments.var(), TupleArguments.copyOf(
            // "completed_setSpec", TupleArguments.fromTerm(
            // ((TucsonTupleCentreId) tid).toTerm()),
            // TupleArguments.copyOf(((TucsonAgentId) id).toString()))));
        } catch (final TucsonInvalidLogicTupleException | InvalidLogicTupleException | TucsonOperationNotPossibleException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    @Override
    public void setSpecRequested(final TupleCentreIdentifier tid, final EmitterIdentifier id,
                                 final String spec) {
        try {
            // Operation Make
            final RespectOperationDefault opRequested = RespectOperationDefault.make(
                    TupleCentreOpType.OUT,
                    LogicTuple.of("node_event", TupleArgument.var(), TupleArgument.of(
                            "requested_setSpec", TupleArgument.fromTerm(
                                    tid.toTerm()),
                            TupleArgument.of(id.toString()),
                            TupleArgument.of(spec))), null);
            // InputEvent Creation
            final InputEvent ev = new InputEvent(this.obsAid, opRequested,
                    this.obsContext, System.currentTimeMillis(), null);
            TupleCentreContainer.doBlockingOperation(ev);
            // TupleCentreContainer.doBlockingOperation(TupleCentreOpType.OUT,
            // this.obsAid, this.obsContext,
            // LogicTuples.fromTerm("node_event", TupleArguments.var(), TupleArguments.copyOf(
            // "requested_setSpec", TupleArguments.fromTerm(
            // ((TucsonTupleCentreId) tid).toTerm()),
            // TupleArguments.copyOf(((TucsonAgentId) id).toString()),
            // TupleArguments.copyOf(spec))));
        } catch (final TucsonInvalidLogicTupleException | InvalidLogicTupleException | TucsonOperationNotPossibleException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    @Override
    public void tcCreated(final TucsonTupleCentreId tid) {
        try {
            // Operation Make
            final RespectOperationDefault opRequested = RespectOperationDefault.make(
                    TupleCentreOpType.OUT, LogicTuple.of("node_event",
                            TupleArgument.var(), TupleArgument.of("new_tc", TupleArgument.fromTerm(
                                    tid.toTerm()))), null);
            // InputEvent Creation
            final InputEvent ev = new InputEvent(this.obsAid, opRequested,
                    this.obsContext, System.currentTimeMillis(), null);
            TupleCentreContainer.doBlockingOperation(ev);
            // TupleCentreContainer.doBlockingOperation(TupleCentreOpType.OUT,
            // this.obsAid, this.obsContext, LogicTuples.fromTerm("node_event",
            // TupleArguments.var(), TupleArguments.copyOf("new_tc", TupleArguments.fromTerm(
            // tid.toTerm()))));
        } catch (final TucsonInvalidLogicTupleException | InvalidLogicTupleException | TucsonOperationNotPossibleException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    @Override
    public void tcDestroyed(final TucsonTupleCentreId tid) {
        try {
            // Operation Make
            final RespectOperationDefault opRequested = RespectOperationDefault.make(
                    TupleCentreOpType.OUT, LogicTuple.of("node_event",
                            TupleArgument.var(), TupleArgument.of("destoyed_tc",
                                    TupleArgument.fromTerm(tid.toTerm()))), null);
            // InputEvent Creation
            final InputEvent ev = new InputEvent(this.obsAid, opRequested,
                    this.obsContext, System.currentTimeMillis(), null);
            TupleCentreContainer.doBlockingOperation(ev);
            // TupleCentreContainer.doBlockingOperation(TupleCentreOpType.OUT,
            // this.obsAid, this.obsContext, LogicTuples.fromTerm("node_event",
            // TupleArguments.var(), TupleArguments.copyOf("destoyed_tc",
            // TupleArguments.fromTerm(tid.toTerm()))));
        } catch (final TucsonInvalidLogicTupleException | InvalidLogicTupleException | TucsonOperationNotPossibleException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
