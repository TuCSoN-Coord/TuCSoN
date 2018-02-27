/*
 * Created on Dec 6, 2004 To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package alice.tucson.service;

import alice.tuple.logic.LogicTuple;
import alice.tuple.logic.LogicTupleDefault;
import alice.tuple.logic.TupleArgumentDefault;
import alice.tuple.logic.Value;
import alice.tuple.logic.Var;
import alice.tuple.logic.exceptions.InvalidLogicTupleException;
import alice.respect.core.RespectOperationDefault;
import alice.tucson.api.TucsonAgentId;
import alice.tucson.api.TucsonTupleCentreId;
import alice.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tucson.api.exceptions.TucsonInvalidLogicTupleException;
import alice.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tuplecentre.api.IId;
import alice.tuple.Tuple;
import alice.tuplecentre.api.TupleCentreId;
import alice.tuple.TupleTemplate;
import alice.tuplecentre.core.InputEvent;
import alice.tuplecentre.core.TupleCentreOpType;

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
            this.obsAid = new TucsonAgentId("obs_agent");
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
                    new LogicTupleDefault("node_event", new Var(), new Value(
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
                    new LogicTupleDefault("node_event", new Var(), new Value(
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
    public void getSpecCompleted(final TupleCentreId tid, final IId id,
            final String spec) {
        try {
            // Operation Make
            final RespectOperationDefault opRequested = RespectOperationDefault.make(
                    TupleCentreOpType.OUT,
                    new LogicTupleDefault("node_event", new Var(), new Value(
                            "completed_getSpec", new TupleArgumentDefault(
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
            // "completed_getSpec", new TupleArgumentDefault(
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
    public void getSpecRequested(final TupleCentreId tid, final IId id) {
        try {
            // Operation Make
            final RespectOperationDefault opRequested = RespectOperationDefault.make(
                    TupleCentreOpType.OUT,
                    new LogicTupleDefault("node_event", new Var(), new Value(
                            "requested_getSpec", new TupleArgumentDefault(
                                    ((TucsonTupleCentreId) tid).toTerm()),
                            new Value(((TucsonAgentId) id).toString()))), null);
            // InputEvent Creation
            final InputEvent ev = new InputEvent(this.obsAid, opRequested,
                    this.obsContext, System.currentTimeMillis(), null);
            TupleCentreContainer.doBlockingOperation(ev);
            // TupleCentreContainer.doBlockingOperation(TupleCentreOpType.OUT,
            // this.obsAid, this.obsContext,
            // new LogicTuple("node_event", new Var(), new Value(
            // "requested_getSpec", new TupleArgumentDefault(
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
    public void inCompleted(final TupleCentreId tid, final IId id, final Tuple t) {
        try {
            // Operation Make
            final RespectOperationDefault opRequested = RespectOperationDefault.make(
                    TupleCentreOpType.OUT,
                    new LogicTupleDefault("node_event", new Var(), new Value(
                            "completed_in", new TupleArgumentDefault(
                                    ((TucsonTupleCentreId) tid).toTerm()),
                            new Value(((TucsonAgentId) id).toString()),
                            new TupleArgumentDefault(((LogicTuple) t).toTerm()))),
                    null);
            // InputEvent Creation
            final InputEvent ev = new InputEvent(this.obsAid, opRequested,
                    this.obsContext, System.currentTimeMillis(), null);
            TupleCentreContainer.doBlockingOperation(ev);
            // TupleCentreContainer.doBlockingOperation(TupleCentreOpType.OUT,
            // this.obsAid, this.obsContext,
            // new LogicTuple("node_event", new Var(), new Value(
            // "completed_in", new TupleArgumentDefault(
            // ((TucsonTupleCentreId) tid).toTerm()),
            // new Value(((TucsonAgentId) id).toString()),
            // new TupleArgumentDefault(((LogicTuple) t).toTerm()))));
        } catch (final TucsonInvalidLogicTupleException e) {
            e.printStackTrace();
        } catch (final TucsonOperationNotPossibleException e) {
            e.printStackTrace();
        } catch (final InvalidLogicTupleException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void inpCompleted(final TupleCentreId tid, final IId id,
            final Tuple t) {
        try {
            // Operation Make
            RespectOperationDefault opRequested = null;
            if (t != null) {
                opRequested = RespectOperationDefault.make(
TupleCentreOpType.OUT, new LogicTupleDefault("node_event", new Var(),
                        new Value("completed_inp", new TupleArgumentDefault(
                                ((TucsonTupleCentreId) tid).toTerm()),
                                new Value(((TucsonAgentId) id).toString()),
                                new Value("succeeded", new TupleArgumentDefault(
                                        ((LogicTuple) t).toTerm())))), null);
                // TupleCentreContainer.doBlockingOperation(TucsonOperationDefault
                // .outCode(), this.obsAid, this.obsContext,
                // new LogicTuple("node_event", new Var(), new Value(
                // "completed_inp", new TupleArgumentDefault(
                // ((TucsonTupleCentreId) tid).toTerm()),
                // new Value(((TucsonAgentId) id).toString()),
                // new Value("succeeded", new TupleArgumentDefault(
                // ((LogicTuple) t).toTerm())))));
            } else {
                opRequested = RespectOperationDefault.make(
TupleCentreOpType.OUT, new LogicTupleDefault("node_event", new Var(),
                        new Value("completed_inp", new TupleArgumentDefault(
                                ((TucsonTupleCentreId) tid).toTerm()),
                                new Value(((TucsonAgentId) id).toString()),
                                new Value("failed"))), null);
                // TupleCentreContainer.doBlockingOperation(TucsonOperationDefault
                // .outCode(), this.obsAid, this.obsContext,
                // new LogicTuple("node_event", new Var(), new Value(
                // "completed_inp", new TupleArgumentDefault(
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
    public void inpRequested(final TupleCentreId tid, final IId id,
            final TupleTemplate t) {
        try {
            // Operation Make
            final RespectOperationDefault opRequested = RespectOperationDefault.make(
                    TupleCentreOpType.OUT,
                    new LogicTupleDefault("node_event", new Var(), new Value(
                            "requested_inp", new TupleArgumentDefault(
                                    ((TucsonTupleCentreId) tid).toTerm()),
                            new Value(((TucsonAgentId) id).toString()),
                            new TupleArgumentDefault(((LogicTuple) t).toTerm()))),
                    null);
            // InputEvent Creation
            final InputEvent ev = new InputEvent(this.obsAid, opRequested,
                    this.obsContext, System.currentTimeMillis(), null);
            TupleCentreContainer.doBlockingOperation(ev);
            // TupleCentreContainer.doBlockingOperation(TupleCentreOpType.OUT,
            // this.obsAid, this.obsContext,
            // new LogicTuple("node_event", new Var(), new Value(
            // "requested_inp", new TupleArgumentDefault(
            // ((TucsonTupleCentreId) tid).toTerm()),
            // new Value(((TucsonAgentId) id).toString()),
            // new TupleArgumentDefault(((LogicTuple) t).toTerm()))));
        } catch (final TucsonInvalidLogicTupleException e) {
            e.printStackTrace();
        } catch (final TucsonOperationNotPossibleException e) {
            e.printStackTrace();
        } catch (final InvalidLogicTupleException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void inRequested(final TupleCentreId tid, final IId id,
            final TupleTemplate t) {
        try {
            // Operation Make
            final RespectOperationDefault opRequested = RespectOperationDefault.make(
                    TupleCentreOpType.OUT,
                    new LogicTupleDefault("node_event", new Var(), new Value(
                            "requested_in", new TupleArgumentDefault(
                                    ((TucsonTupleCentreId) tid).toTerm()),
                            new Value(((TucsonAgentId) id).toString()),
                            new TupleArgumentDefault(((LogicTuple) t).toTerm()))),
                    null);
            // InputEvent Creation
            final InputEvent ev = new InputEvent(this.obsAid, opRequested,
                    this.obsContext, System.currentTimeMillis(), null);
            TupleCentreContainer.doBlockingOperation(ev);
            // TupleCentreContainer.doBlockingOperation(TupleCentreOpType.OUT,
            // this.obsAid, this.obsContext,
            // new LogicTuple("node_event", new Var(), new Value(
            // "requested_in", new TupleArgumentDefault(
            // ((TucsonTupleCentreId) tid).toTerm()),
            // new Value(((TucsonAgentId) id).toString()),
            // new TupleArgumentDefault(((LogicTuple) t).toTerm()))));
        } catch (final TucsonInvalidLogicTupleException e) {
            e.printStackTrace();
        } catch (final TucsonOperationNotPossibleException e) {
            e.printStackTrace();
        } catch (final InvalidLogicTupleException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void outRequested(final TupleCentreId tid, final IId id,
            final Tuple t) {
        try {
            // Operation Make
            final RespectOperationDefault opRequested = RespectOperationDefault.make(
                    TupleCentreOpType.OUT,
                    new LogicTupleDefault("node_event", new Var(), new Value(
                            "done_out", new TupleArgumentDefault(
                                    ((TucsonTupleCentreId) tid).toTerm()),
                            new Value(((TucsonAgentId) id).toString()),
                            new TupleArgumentDefault(((LogicTuple) t).toTerm()))),
                    null);
            // InputEvent Creation
            final InputEvent ev = new InputEvent(this.obsAid, opRequested,
                    this.obsContext, System.currentTimeMillis(), null);
            TupleCentreContainer.doBlockingOperation(ev);
            // TupleCentreContainer.doBlockingOperation(TupleCentreOpType.OUT,
            // this.obsAid, this.obsContext,
            // new LogicTuple("node_event", new Var(), new Value(
            // "done_out", new TupleArgumentDefault(
            // ((TucsonTupleCentreId) tid).toTerm()),
            // new Value(((TucsonAgentId) id).toString()),
            // new TupleArgumentDefault(((LogicTuple) t).toTerm()))));
        } catch (final TucsonInvalidLogicTupleException e) {
            e.printStackTrace();
        } catch (final TucsonOperationNotPossibleException e) {
            e.printStackTrace();
        } catch (final InvalidLogicTupleException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void rdCompleted(final TupleCentreId tid, final IId id, final Tuple t) {
        try {
            // Operation Make
            final RespectOperationDefault opRequested = RespectOperationDefault.make(
                    TupleCentreOpType.OUT,
                    new LogicTupleDefault("node_event", new Var(), new Value(
                            "completed_rd", new TupleArgumentDefault(
                                    ((TucsonTupleCentreId) tid).toTerm()),
                            new Value(((TucsonAgentId) id).toString()),
                            new TupleArgumentDefault(((LogicTuple) t).toTerm()))),
                    null);
            // InputEvent Creation
            final InputEvent ev = new InputEvent(this.obsAid, opRequested,
                    this.obsContext, System.currentTimeMillis(), null);
            TupleCentreContainer.doBlockingOperation(ev);
            // TupleCentreContainer.doBlockingOperation(TupleCentreOpType.OUT,
            // this.obsAid, this.obsContext,
            // new LogicTuple("node_event", new Var(), new Value(
            // "completed_rd", new TupleArgumentDefault(
            // ((TucsonTupleCentreId) tid).toTerm()),
            // new Value(((TucsonAgentId) id).toString()),
            // new TupleArgumentDefault(((LogicTuple) t).toTerm()))));
        } catch (final TucsonInvalidLogicTupleException e) {
            e.printStackTrace();
        } catch (final TucsonOperationNotPossibleException e) {
            e.printStackTrace();
        } catch (final InvalidLogicTupleException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void rdpCompleted(final TupleCentreId tid, final IId id,
            final Tuple t) {
        try {
            // Operation Make
            RespectOperationDefault opRequested = null;
            if (t != null) {
                opRequested = RespectOperationDefault.make(
TupleCentreOpType.OUT, new LogicTupleDefault("node_event", new Var(),
                        new Value("completed_rdp", new TupleArgumentDefault(
                                ((TucsonTupleCentreId) tid).toTerm()),
                                new Value(((TucsonAgentId) id).toString()),
                                new Value("succeeded", new TupleArgumentDefault(
                                        ((LogicTuple) t).toTerm())))), null);
                // TupleCentreContainer.doBlockingOperation(TucsonOperationDefault
                // .outCode(), this.obsAid, this.obsContext,
                // new LogicTuple("node_event", new Var(), new Value(
                // "completed_rdp", new TupleArgumentDefault(
                // ((TucsonTupleCentreId) tid).toTerm()),
                // new Value(((TucsonAgentId) id).toString()),
                // new Value("succeeded", new TupleArgumentDefault(
                // ((LogicTuple) t).toTerm())))));
            } else {
                opRequested = RespectOperationDefault.make(
TupleCentreOpType.OUT, new LogicTupleDefault("node_event", new Var(),
                        new Value("completed_rdp", new TupleArgumentDefault(
                                ((TucsonTupleCentreId) tid).toTerm()),
                                new Value(((TucsonAgentId) id).toString()),
                                new Value("failed"))), null);
                // TupleCentreContainer.doBlockingOperation(TucsonOperationDefault
                // .outCode(), this.obsAid, this.obsContext,
                // new LogicTuple("node_event", new Var(), new Value(
                // "completed_rdp", new TupleArgumentDefault(
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
    public void rdpRequested(final TupleCentreId tid, final IId id,
            final TupleTemplate t) {
        try {
            // Operation Make
            final RespectOperationDefault opRequested = RespectOperationDefault.make(
                    TupleCentreOpType.OUT,
                    new LogicTupleDefault("node_event", new Var(), new Value(
                            "requested_rdp", new TupleArgumentDefault(
                                    ((TucsonTupleCentreId) tid).toTerm()),
                            new Value(((TucsonAgentId) id).toString()),
                            new TupleArgumentDefault(((LogicTuple) t).toTerm()))),
                    null);
            // InputEvent Creation
            final InputEvent ev = new InputEvent(this.obsAid, opRequested,
                    this.obsContext, System.currentTimeMillis(), null);
            TupleCentreContainer.doBlockingOperation(ev);
            // TupleCentreContainer.doBlockingOperation(TupleCentreOpType.OUT,
            // this.obsAid, this.obsContext,
            // new LogicTuple("node_event", new Var(), new Value(
            // "requested_rdp", new TupleArgumentDefault(
            // ((TucsonTupleCentreId) tid).toTerm()),
            // new Value(((TucsonAgentId) id).toString()),
            // new TupleArgumentDefault(((LogicTuple) t).toTerm()))));
        } catch (final TucsonInvalidLogicTupleException e) {
            e.printStackTrace();
        } catch (final TucsonOperationNotPossibleException e) {
            e.printStackTrace();
        } catch (final InvalidLogicTupleException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void rdRequested(final TupleCentreId tid, final IId id,
            final TupleTemplate t) {
        try {
            // Operation Make
            final RespectOperationDefault opRequested = RespectOperationDefault.make(
                    TupleCentreOpType.OUT,
                    new LogicTupleDefault("node_event", new Var(), new Value(
                            "requested_rd", new TupleArgumentDefault(
                                    ((TucsonTupleCentreId) tid).toTerm()),
                            new Value(((TucsonAgentId) id).toString()),
                            new TupleArgumentDefault(((LogicTuple) t).toTerm()))),
                    null);
            // InputEvent Creation
            final InputEvent ev = new InputEvent(this.obsAid, opRequested,
                    this.obsContext, System.currentTimeMillis(), null);
            TupleCentreContainer.doBlockingOperation(ev);
            // TupleCentreContainer.doBlockingOperation(TupleCentreOpType.OUT,
            // this.obsAid, this.obsContext,
            // new LogicTuple("node_event", new Var(), new Value(
            // "requested_rd", new TupleArgumentDefault(
            // ((TucsonTupleCentreId) tid).toTerm()),
            // new Value(((TucsonAgentId) id).toString()),
            // new TupleArgumentDefault(((LogicTuple) t).toTerm()))));
        } catch (final TucsonInvalidLogicTupleException e) {
            e.printStackTrace();
        } catch (final TucsonOperationNotPossibleException e) {
            e.printStackTrace();
        } catch (final InvalidLogicTupleException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setSpecCompleted(final TupleCentreId tid, final IId id) {
        try {
            // Operation Make
            final RespectOperationDefault opRequested = RespectOperationDefault.make(
                    TupleCentreOpType.OUT,
                    new LogicTupleDefault("node_event", new Var(), new Value(
                            "completed_setSpec", new TupleArgumentDefault(
                                    ((TucsonTupleCentreId) tid).toTerm()),
                            new Value(((TucsonAgentId) id).toString()))), null);
            // InputEvent Creation
            final InputEvent ev = new InputEvent(this.obsAid, opRequested,
                    this.obsContext, System.currentTimeMillis(), null);
            TupleCentreContainer.doBlockingOperation(ev);
            // TupleCentreContainer.doBlockingOperation(TupleCentreOpType.OUT,
            // this.obsAid, this.obsContext,
            // new LogicTuple("node_event", new Var(), new Value(
            // "completed_setSpec", new TupleArgumentDefault(
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
    public void setSpecRequested(final TupleCentreId tid, final IId id,
            final String spec) {
        try {
            // Operation Make
            final RespectOperationDefault opRequested = RespectOperationDefault.make(
                    TupleCentreOpType.OUT,
                    new LogicTupleDefault("node_event", new Var(), new Value(
                            "requested_setSpec", new TupleArgumentDefault(
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
            // "requested_setSpec", new TupleArgumentDefault(
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
                    TupleCentreOpType.OUT, new LogicTupleDefault("node_event",
                            new Var(), new Value("new_tc", new TupleArgumentDefault(
                                    tid.toTerm()))), null);
            // InputEvent Creation
            final InputEvent ev = new InputEvent(this.obsAid, opRequested,
                    this.obsContext, System.currentTimeMillis(), null);
            TupleCentreContainer.doBlockingOperation(ev);
            // TupleCentreContainer.doBlockingOperation(TupleCentreOpType.OUT,
            // this.obsAid, this.obsContext, new LogicTuple("node_event",
            // new Var(), new Value("new_tc", new TupleArgumentDefault(
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
                    TupleCentreOpType.OUT, new LogicTupleDefault("node_event",
                            new Var(), new Value("destoyed_tc",
                                    new TupleArgumentDefault(tid.toTerm()))), null);
            // InputEvent Creation
            final InputEvent ev = new InputEvent(this.obsAid, opRequested,
                    this.obsContext, System.currentTimeMillis(), null);
            TupleCentreContainer.doBlockingOperation(ev);
            // TupleCentreContainer.doBlockingOperation(TupleCentreOpType.OUT,
            // this.obsAid, this.obsContext, new LogicTuple("node_event",
            // new Var(), new Value("destoyed_tc",
            // new TupleArgumentDefault(tid.toTerm()))));
        } catch (final TucsonInvalidLogicTupleException e) {
            e.printStackTrace();
        } catch (final TucsonOperationNotPossibleException e) {
            e.printStackTrace();
        } catch (final InvalidLogicTupleException e) {
            e.printStackTrace();
        }
    }
}
