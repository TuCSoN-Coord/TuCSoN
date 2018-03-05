/*
 * Created on Dec 6, 2004 To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package alice.tuplecentre.tucson.service;

import alice.logictuple.LogicTuple;
import alice.logictuple.TupleArgument;
import alice.logictuple.Value;
import alice.logictuple.Var;
import alice.logictuple.exceptions.InvalidLogicTupleException;
import alice.tuplecentre.api.EmitterIdentifier;
import alice.tuplecentre.api.Tuple;
import alice.tuplecentre.api.TupleCentreIdentifier;
import alice.tuplecentre.api.TupleTemplate;
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
 * 
 * @author Alessandro Ricci
 * @author (contributor) Michele Bombardi (mailto:
 *         michele.bombardi@studio.unibo.it)
 * 
 */
public class ObservationService implements NodeServiceListener {
    private TucsonAgentId obsAid;
    private final TucsonTupleCentreId obsContext;

    /**
     * 
     * @param ctx
     *            the identifier of the tuple centre under observation
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
                    new LogicTuple("node_event", new Var(), new Value(
                            "new_agent", new Value(aid.toString()))), null);
            // InputEvent Creation
            final InputEvent ev = new InputEvent(this.obsAid, opRequested,
                    this.obsContext, System.currentTimeMillis(), null);
            TupleCentreContainer.doBlockingOperation(ev);
            // TupleCentreContainer.doBlockingOperation(TupleCentreOpType.OUT,
            // this.obsAid, this.obsContext,
            // new LogicTuple("node_event", new Var(), new Value(
            // "new_agent", new Value(aid.toString()))));
        } catch (final TucsonInvalidLogicTupleException e) {
            e.printStackTrace();
        } catch (final TucsonOperationNotPossibleException e) {
            e.printStackTrace();
        } catch (final InvalidLogicTupleException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void accQuit(final TucsonAgentId aid) {
        try {
            // Operation Make
            final RespectOperationDefault opRequested = RespectOperationDefault.make(
                    TupleCentreOpType.OUT,
                    new LogicTuple("node_event", new Var(), new Value(
                            "exit_agent", new Value(aid.toString()))), null);
            // InputEvent Creation
            final InputEvent ev = new InputEvent(this.obsAid, opRequested,
                    this.obsContext, System.currentTimeMillis(), null);
            TupleCentreContainer.doBlockingOperation(ev);
            // TupleCentreContainer.doBlockingOperation(TupleCentreOpType.OUT,
            // this.obsAid, this.obsContext,
            // new LogicTuple("node_event", new Var(), new Value(
            // "exit_agent", new Value(aid.toString()))));
        } catch (final TucsonInvalidLogicTupleException e) {
            e.printStackTrace();
        } catch (final TucsonOperationNotPossibleException e) {
            e.printStackTrace();
        } catch (final InvalidLogicTupleException e) {
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
                    new LogicTuple("node_event", new Var(), new Value(
                            "completed_getSpec", new TupleArgument(
                                    ((TucsonTupleCentreId) tid).toTerm()),
                            new Value(((TucsonAgentId) id).toString()),
                            new Value(spec))), null);
            // InputEvent Creation
            final InputEvent ev = new InputEvent(this.obsAid, opRequested,
                    this.obsContext, System.currentTimeMillis(), null);
            TupleCentreContainer.doBlockingOperation(ev);
            // TupleCentreContainer.doBlockingOperation(TupleCentreOpType.OUT,
            // this.obsAid, this.obsContext,
            // new LogicTuple("node_event", new Var(), new Value(
            // "completed_getSpec", new TupleArgument(
            // ((TucsonTupleCentreId) tid).toTerm()),
            // new Value(((TucsonAgentId) id).toString()),
            // new Value(spec))));
        } catch (final TucsonInvalidLogicTupleException e) {
            e.printStackTrace();
        } catch (final TucsonOperationNotPossibleException e) {
            e.printStackTrace();
        } catch (final InvalidLogicTupleException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getSpecRequested(final TupleCentreIdentifier tid, final EmitterIdentifier id) {
        try {
            // Operation Make
            final RespectOperationDefault opRequested = RespectOperationDefault.make(
                    TupleCentreOpType.OUT,
                    new LogicTuple("node_event", new Var(), new Value(
                            "requested_getSpec", new TupleArgument(
                                    ((TucsonTupleCentreId) tid).toTerm()),
                            new Value(((TucsonAgentId) id).toString()))), null);
            // InputEvent Creation
            final InputEvent ev = new InputEvent(this.obsAid, opRequested,
                    this.obsContext, System.currentTimeMillis(), null);
            TupleCentreContainer.doBlockingOperation(ev);
            // TupleCentreContainer.doBlockingOperation(TupleCentreOpType.OUT,
            // this.obsAid, this.obsContext,
            // new LogicTuple("node_event", new Var(), new Value(
            // "requested_getSpec", new TupleArgument(
            // ((TucsonTupleCentreId) tid).toTerm()),
            // new Value(((TucsonAgentId) id).toString()))));
        } catch (final TucsonInvalidLogicTupleException e) {
            e.printStackTrace();
        } catch (final TucsonOperationNotPossibleException e) {
            e.printStackTrace();
        } catch (final InvalidLogicTupleException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void inCompleted(final TupleCentreIdentifier tid, final EmitterIdentifier id, final Tuple t) {
        try {
            // Operation Make
            final RespectOperationDefault opRequested = RespectOperationDefault.make(
                    TupleCentreOpType.OUT,
                    new LogicTuple("node_event", new Var(), new Value(
                            "completed_in", new TupleArgument(
                                    ((TucsonTupleCentreId) tid).toTerm()),
                            new Value(((TucsonAgentId) id).toString()),
                            new TupleArgument(((LogicTuple) t).toTerm()))),
                    null);
            // InputEvent Creation
            final InputEvent ev = new InputEvent(this.obsAid, opRequested,
                    this.obsContext, System.currentTimeMillis(), null);
            TupleCentreContainer.doBlockingOperation(ev);
            // TupleCentreContainer.doBlockingOperation(TupleCentreOpType.OUT,
            // this.obsAid, this.obsContext,
            // new LogicTuple("node_event", new Var(), new Value(
            // "completed_in", new TupleArgument(
            // ((TucsonTupleCentreId) tid).toTerm()),
            // new Value(((TucsonAgentId) id).toString()),
            // new TupleArgument(((LogicTuple) t).toTerm()))));
        } catch (final TucsonInvalidLogicTupleException e) {
            e.printStackTrace();
        } catch (final TucsonOperationNotPossibleException e) {
            e.printStackTrace();
        } catch (final InvalidLogicTupleException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void inpCompleted(final TupleCentreIdentifier tid, final EmitterIdentifier id,
                             final Tuple t) {
        try {
            // Operation Make
            RespectOperationDefault opRequested = null;
            if (t != null) {
                opRequested = RespectOperationDefault.make(
TupleCentreOpType.OUT, new LogicTuple("node_event", new Var(),
                        new Value("completed_inp", new TupleArgument(
                                ((TucsonTupleCentreId) tid).toTerm()),
                                new Value(((TucsonAgentId) id).toString()),
                                new Value("succeeded", new TupleArgument(
                                        ((LogicTuple) t).toTerm())))), null);
                // TupleCentreContainer.doBlockingOperation(TucsonOperationDefault
                // .outCode(), this.obsAid, this.obsContext,
                // new LogicTuple("node_event", new Var(), new Value(
                // "completed_inp", new TupleArgument(
                // ((TucsonTupleCentreId) tid).toTerm()),
                // new Value(((TucsonAgentId) id).toString()),
                // new Value("succeeded", new TupleArgument(
                // ((LogicTuple) t).toTerm())))));
            } else {
                opRequested = RespectOperationDefault.make(
TupleCentreOpType.OUT, new LogicTuple("node_event", new Var(),
                        new Value("completed_inp", new TupleArgument(
                                ((TucsonTupleCentreId) tid).toTerm()),
                                new Value(((TucsonAgentId) id).toString()),
                                new Value("failed"))), null);
                // TupleCentreContainer.doBlockingOperation(TucsonOperationDefault
                // .outCode(), this.obsAid, this.obsContext,
                // new LogicTuple("node_event", new Var(), new Value(
                // "completed_inp", new TupleArgument(
                // ((TucsonTupleCentreId) tid).toTerm()),
                // new Value(((TucsonAgentId) id).toString()),
                // new Value("failed"))));
            }
            // InputEvent Creation
            final InputEvent ev = new InputEvent(this.obsAid, opRequested,
                    this.obsContext, System.currentTimeMillis(), null);
            TupleCentreContainer.doBlockingOperation(ev);
        } catch (final TucsonOperationNotPossibleException e) {
            e.printStackTrace();
        } catch (final TucsonInvalidLogicTupleException e) {
            e.printStackTrace();
        } catch (final InvalidLogicTupleException e) {
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
                    new LogicTuple("node_event", new Var(), new Value(
                            "requested_inp", new TupleArgument(
                                    ((TucsonTupleCentreId) tid).toTerm()),
                            new Value(((TucsonAgentId) id).toString()),
                            new TupleArgument(((LogicTuple) t).toTerm()))),
                    null);
            // InputEvent Creation
            final InputEvent ev = new InputEvent(this.obsAid, opRequested,
                    this.obsContext, System.currentTimeMillis(), null);
            TupleCentreContainer.doBlockingOperation(ev);
            // TupleCentreContainer.doBlockingOperation(TupleCentreOpType.OUT,
            // this.obsAid, this.obsContext,
            // new LogicTuple("node_event", new Var(), new Value(
            // "requested_inp", new TupleArgument(
            // ((TucsonTupleCentreId) tid).toTerm()),
            // new Value(((TucsonAgentId) id).toString()),
            // new TupleArgument(((LogicTuple) t).toTerm()))));
        } catch (final TucsonInvalidLogicTupleException e) {
            e.printStackTrace();
        } catch (final TucsonOperationNotPossibleException e) {
            e.printStackTrace();
        } catch (final InvalidLogicTupleException e) {
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
                    new LogicTuple("node_event", new Var(), new Value(
                            "requested_in", new TupleArgument(
                                    ((TucsonTupleCentreId) tid).toTerm()),
                            new Value(((TucsonAgentId) id).toString()),
                            new TupleArgument(((LogicTuple) t).toTerm()))),
                    null);
            // InputEvent Creation
            final InputEvent ev = new InputEvent(this.obsAid, opRequested,
                    this.obsContext, System.currentTimeMillis(), null);
            TupleCentreContainer.doBlockingOperation(ev);
            // TupleCentreContainer.doBlockingOperation(TupleCentreOpType.OUT,
            // this.obsAid, this.obsContext,
            // new LogicTuple("node_event", new Var(), new Value(
            // "requested_in", new TupleArgument(
            // ((TucsonTupleCentreId) tid).toTerm()),
            // new Value(((TucsonAgentId) id).toString()),
            // new TupleArgument(((LogicTuple) t).toTerm()))));
        } catch (final TucsonInvalidLogicTupleException e) {
            e.printStackTrace();
        } catch (final TucsonOperationNotPossibleException e) {
            e.printStackTrace();
        } catch (final InvalidLogicTupleException e) {
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
                    new LogicTuple("node_event", new Var(), new Value(
                            "done_out", new TupleArgument(
                                    ((TucsonTupleCentreId) tid).toTerm()),
                            new Value(((TucsonAgentId) id).toString()),
                            new TupleArgument(((LogicTuple) t).toTerm()))),
                    null);
            // InputEvent Creation
            final InputEvent ev = new InputEvent(this.obsAid, opRequested,
                    this.obsContext, System.currentTimeMillis(), null);
            TupleCentreContainer.doBlockingOperation(ev);
            // TupleCentreContainer.doBlockingOperation(TupleCentreOpType.OUT,
            // this.obsAid, this.obsContext,
            // new LogicTuple("node_event", new Var(), new Value(
            // "done_out", new TupleArgument(
            // ((TucsonTupleCentreId) tid).toTerm()),
            // new Value(((TucsonAgentId) id).toString()),
            // new TupleArgument(((LogicTuple) t).toTerm()))));
        } catch (final TucsonInvalidLogicTupleException e) {
            e.printStackTrace();
        } catch (final TucsonOperationNotPossibleException e) {
            e.printStackTrace();
        } catch (final InvalidLogicTupleException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void rdCompleted(final TupleCentreIdentifier tid, final EmitterIdentifier id, final Tuple t) {
        try {
            // Operation Make
            final RespectOperationDefault opRequested = RespectOperationDefault.make(
                    TupleCentreOpType.OUT,
                    new LogicTuple("node_event", new Var(), new Value(
                            "completed_rd", new TupleArgument(
                                    ((TucsonTupleCentreId) tid).toTerm()),
                            new Value(((TucsonAgentId) id).toString()),
                            new TupleArgument(((LogicTuple) t).toTerm()))),
                    null);
            // InputEvent Creation
            final InputEvent ev = new InputEvent(this.obsAid, opRequested,
                    this.obsContext, System.currentTimeMillis(), null);
            TupleCentreContainer.doBlockingOperation(ev);
            // TupleCentreContainer.doBlockingOperation(TupleCentreOpType.OUT,
            // this.obsAid, this.obsContext,
            // new LogicTuple("node_event", new Var(), new Value(
            // "completed_rd", new TupleArgument(
            // ((TucsonTupleCentreId) tid).toTerm()),
            // new Value(((TucsonAgentId) id).toString()),
            // new TupleArgument(((LogicTuple) t).toTerm()))));
        } catch (final TucsonInvalidLogicTupleException e) {
            e.printStackTrace();
        } catch (final TucsonOperationNotPossibleException e) {
            e.printStackTrace();
        } catch (final InvalidLogicTupleException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void rdpCompleted(final TupleCentreIdentifier tid, final EmitterIdentifier id,
                             final Tuple t) {
        try {
            // Operation Make
            RespectOperationDefault opRequested = null;
            if (t != null) {
                opRequested = RespectOperationDefault.make(
TupleCentreOpType.OUT, new LogicTuple("node_event", new Var(),
                        new Value("completed_rdp", new TupleArgument(
                                ((TucsonTupleCentreId) tid).toTerm()),
                                new Value(((TucsonAgentId) id).toString()),
                                new Value("succeeded", new TupleArgument(
                                        ((LogicTuple) t).toTerm())))), null);
                // TupleCentreContainer.doBlockingOperation(TucsonOperationDefault
                // .outCode(), this.obsAid, this.obsContext,
                // new LogicTuple("node_event", new Var(), new Value(
                // "completed_rdp", new TupleArgument(
                // ((TucsonTupleCentreId) tid).toTerm()),
                // new Value(((TucsonAgentId) id).toString()),
                // new Value("succeeded", new TupleArgument(
                // ((LogicTuple) t).toTerm())))));
            } else {
                opRequested = RespectOperationDefault.make(
TupleCentreOpType.OUT, new LogicTuple("node_event", new Var(),
                        new Value("completed_rdp", new TupleArgument(
                                ((TucsonTupleCentreId) tid).toTerm()),
                                new Value(((TucsonAgentId) id).toString()),
                                new Value("failed"))), null);
                // TupleCentreContainer.doBlockingOperation(TucsonOperationDefault
                // .outCode(), this.obsAid, this.obsContext,
                // new LogicTuple("node_event", new Var(), new Value(
                // "completed_rdp", new TupleArgument(
                // ((TucsonTupleCentreId) tid).toTerm()),
                // new Value(((TucsonAgentId) id).toString()),
                // new Value("failed"))));
            }
            // InputEvent Creation
            final InputEvent ev = new InputEvent(this.obsAid, opRequested,
                    this.obsContext, System.currentTimeMillis(), null);
            TupleCentreContainer.doBlockingOperation(ev);
        } catch (final TucsonOperationNotPossibleException e) {
            e.printStackTrace();
        } catch (final TucsonInvalidLogicTupleException e) {
            e.printStackTrace();
        } catch (final InvalidLogicTupleException e) {
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
                    new LogicTuple("node_event", new Var(), new Value(
                            "requested_rdp", new TupleArgument(
                                    ((TucsonTupleCentreId) tid).toTerm()),
                            new Value(((TucsonAgentId) id).toString()),
                            new TupleArgument(((LogicTuple) t).toTerm()))),
                    null);
            // InputEvent Creation
            final InputEvent ev = new InputEvent(this.obsAid, opRequested,
                    this.obsContext, System.currentTimeMillis(), null);
            TupleCentreContainer.doBlockingOperation(ev);
            // TupleCentreContainer.doBlockingOperation(TupleCentreOpType.OUT,
            // this.obsAid, this.obsContext,
            // new LogicTuple("node_event", new Var(), new Value(
            // "requested_rdp", new TupleArgument(
            // ((TucsonTupleCentreId) tid).toTerm()),
            // new Value(((TucsonAgentId) id).toString()),
            // new TupleArgument(((LogicTuple) t).toTerm()))));
        } catch (final TucsonInvalidLogicTupleException e) {
            e.printStackTrace();
        } catch (final TucsonOperationNotPossibleException e) {
            e.printStackTrace();
        } catch (final InvalidLogicTupleException e) {
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
                    new LogicTuple("node_event", new Var(), new Value(
                            "requested_rd", new TupleArgument(
                                    ((TucsonTupleCentreId) tid).toTerm()),
                            new Value(((TucsonAgentId) id).toString()),
                            new TupleArgument(((LogicTuple) t).toTerm()))),
                    null);
            // InputEvent Creation
            final InputEvent ev = new InputEvent(this.obsAid, opRequested,
                    this.obsContext, System.currentTimeMillis(), null);
            TupleCentreContainer.doBlockingOperation(ev);
            // TupleCentreContainer.doBlockingOperation(TupleCentreOpType.OUT,
            // this.obsAid, this.obsContext,
            // new LogicTuple("node_event", new Var(), new Value(
            // "requested_rd", new TupleArgument(
            // ((TucsonTupleCentreId) tid).toTerm()),
            // new Value(((TucsonAgentId) id).toString()),
            // new TupleArgument(((LogicTuple) t).toTerm()))));
        } catch (final TucsonInvalidLogicTupleException e) {
            e.printStackTrace();
        } catch (final TucsonOperationNotPossibleException e) {
            e.printStackTrace();
        } catch (final InvalidLogicTupleException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setSpecCompleted(final TupleCentreIdentifier tid, final EmitterIdentifier id) {
        try {
            // Operation Make
            final RespectOperationDefault opRequested = RespectOperationDefault.make(
                    TupleCentreOpType.OUT,
                    new LogicTuple("node_event", new Var(), new Value(
                            "completed_setSpec", new TupleArgument(
                                    ((TucsonTupleCentreId) tid).toTerm()),
                            new Value(((TucsonAgentId) id).toString()))), null);
            // InputEvent Creation
            final InputEvent ev = new InputEvent(this.obsAid, opRequested,
                    this.obsContext, System.currentTimeMillis(), null);
            TupleCentreContainer.doBlockingOperation(ev);
            // TupleCentreContainer.doBlockingOperation(TupleCentreOpType.OUT,
            // this.obsAid, this.obsContext,
            // new LogicTuple("node_event", new Var(), new Value(
            // "completed_setSpec", new TupleArgument(
            // ((TucsonTupleCentreId) tid).toTerm()),
            // new Value(((TucsonAgentId) id).toString()))));
        } catch (final TucsonInvalidLogicTupleException e) {
            e.printStackTrace();
        } catch (final TucsonOperationNotPossibleException e) {
            e.printStackTrace();
        } catch (final InvalidLogicTupleException e) {
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
                    new LogicTuple("node_event", new Var(), new Value(
                            "requested_setSpec", new TupleArgument(
                                    ((TucsonTupleCentreId) tid).toTerm()),
                            new Value(((TucsonAgentId) id).toString()),
                            new Value(spec))), null);
            // InputEvent Creation
            final InputEvent ev = new InputEvent(this.obsAid, opRequested,
                    this.obsContext, System.currentTimeMillis(), null);
            TupleCentreContainer.doBlockingOperation(ev);
            // TupleCentreContainer.doBlockingOperation(TupleCentreOpType.OUT,
            // this.obsAid, this.obsContext,
            // new LogicTuple("node_event", new Var(), new Value(
            // "requested_setSpec", new TupleArgument(
            // ((TucsonTupleCentreId) tid).toTerm()),
            // new Value(((TucsonAgentId) id).toString()),
            // new Value(spec))));
        } catch (final TucsonInvalidLogicTupleException e) {
            e.printStackTrace();
        } catch (final TucsonOperationNotPossibleException e) {
            e.printStackTrace();
        } catch (final InvalidLogicTupleException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void tcCreated(final TucsonTupleCentreId tid) {
        try {
            // Operation Make
            final RespectOperationDefault opRequested = RespectOperationDefault.make(
                    TupleCentreOpType.OUT, new LogicTuple("node_event",
                            new Var(), new Value("new_tc", new TupleArgument(
                                    tid.toTerm()))), null);
            // InputEvent Creation
            final InputEvent ev = new InputEvent(this.obsAid, opRequested,
                    this.obsContext, System.currentTimeMillis(), null);
            TupleCentreContainer.doBlockingOperation(ev);
            // TupleCentreContainer.doBlockingOperation(TupleCentreOpType.OUT,
            // this.obsAid, this.obsContext, new LogicTuple("node_event",
            // new Var(), new Value("new_tc", new TupleArgument(
            // tid.toTerm()))));
        } catch (final TucsonInvalidLogicTupleException e) {
            e.printStackTrace();
        } catch (final TucsonOperationNotPossibleException e) {
            e.printStackTrace();
        } catch (final InvalidLogicTupleException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void tcDestroyed(final TucsonTupleCentreId tid) {
        try {
            // Operation Make
            final RespectOperationDefault opRequested = RespectOperationDefault.make(
                    TupleCentreOpType.OUT, new LogicTuple("node_event",
                            new Var(), new Value("destoyed_tc",
                                    new TupleArgument(tid.toTerm()))), null);
            // InputEvent Creation
            final InputEvent ev = new InputEvent(this.obsAid, opRequested,
                    this.obsContext, System.currentTimeMillis(), null);
            TupleCentreContainer.doBlockingOperation(ev);
            // TupleCentreContainer.doBlockingOperation(TupleCentreOpType.OUT,
            // this.obsAid, this.obsContext, new LogicTuple("node_event",
            // new Var(), new Value("destoyed_tc",
            // new TupleArgument(tid.toTerm()))));
        } catch (final TucsonInvalidLogicTupleException e) {
            e.printStackTrace();
        } catch (final TucsonOperationNotPossibleException e) {
            e.printStackTrace();
        } catch (final InvalidLogicTupleException e) {
            e.printStackTrace();
        }
    }
}
