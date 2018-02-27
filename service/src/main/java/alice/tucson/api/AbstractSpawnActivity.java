package alice.tucson.api;

import java.io.Serializable;
import java.util.List;

import alice.tuple.logic.LogicTuple;
import alice.tuple.logic.exceptions.InvalidLogicTupleException;
import alice.respect.core.RespectOperationDefault;
import alice.tucson.api.acc.BulkSyncACC;
import alice.tucson.api.acc.OrdinarySyncACC;
import alice.tucson.api.acc.UniformSyncACC;
import alice.tucson.api.exceptions.TucsonInvalidLogicTupleException;
import alice.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tucson.service.TucsonIdWrapper;
import alice.tucson.service.TupleCentreContainer;
import alice.tuplecentre.core.InputEvent;
import alice.tuplecentre.core.TupleCentreOpType;

/**
 * The "parallel computation" to be started with a <code>spawn</code> primitive.
 * The spawned activity should be a PURELY COMPUTATIONAL (algorithmic) process,
 * with the purpose to delegate computations to the coordination medium. For
 * this reason, a set of "constrained" Linda primitives are provided: they
 * CANNOT access a remote space. Furthermore, the programmer is strongly
 * encouraged not to put communications, locks or other potentially
 * "extra-algorithmic" features in its SpawnActivity.
 * 
 * @author ste (mailto: s.mariani@unibo.it)
 * @author (contributor) Michele Bombardi (mailto:
 *         michele.bombardi@studio.unibo.it)
 */
public abstract class AbstractSpawnActivity implements Serializable, Runnable {
    private static final long serialVersionUID = -6354837455366449916L;
    private TucsonAgentId aid;
    private TucsonTupleCentreId target;
    private TucsonTupleCentreId tcid;

    /**
     * Checks if the activity to spawn has been correctly instantiated.
     * 
     * @return true if instantiation is complete, false otherwise.
     */
    public final boolean checkInstantiation() {
        if ((this.aid != null || this.tcid != null) && this.target != null) {
            return true;
        }
        return false;
    }

    /**
     * To be overridden by user
     */
    public abstract void doActivity();

    /**
     * Both agents and the coordination medium itself can <code>spawn</code> a
     * computation, hence we need to handle both.
     * 
     * @return the "spawner" id (actually, a generic wrapper hosting either a
     *         TucsonAgentId or a TucsonTupleCentreId, accessible with method
     *         <code>getId()</code>)
     * 
     * @see alice.tucson.service.TucsonIdWrapper TucsonIdWrapper
     */
    public final TucsonIdWrapper<?> getSpawnerId() {
        if (this.aid == null) {
            return new TucsonIdWrapper<TucsonTupleCentreId>(this.tcid);
        }
        return new TucsonIdWrapper<TucsonAgentId>(this.aid);
    }

    /**
     * Gets the tuplecentre identifier hosting the spawned activity.
     * 
     * @return the identifier of the tuplecentre hosting the spawned activity.
     */
    public final TucsonTupleCentreId getTargetTC() {
        return this.target;
    }

    /**
     * Called by the ReSpecT engine.
     */
    @Override
    public final void run() {
        if (this.checkInstantiation()) {
            this.doActivity();
        }
    }

    /**
     * Linda operations used in the spawned activity are performed ON BEHALF of
     * the agent who issued the <code>spawn</code> (its "owner").
     * 
     * @param id
     *            the identifier of the agent "owner" of the spawned activity.
     */
    public final void setSpawnerId(final TucsonAgentId id) {
        this.aid = id;
        this.tcid = null;
    }

    /**
     * Linda operations used in the spawned activity are performed ON BEHALF of
     * the tuplecentre who issued the <code>spawn</code> (its "owner").
     * 
     * @param id
     *            the identifier of the tuplecentre "owner" of the spawned
     *            activity.
     */
    public final void setSpawnerId(final TucsonTupleCentreId id) {
        this.aid = null;
        this.tcid = id;
    }

    /**
     * The tuplecentre target, which will "host" the spawned computation. It is
     * automagically set by the ReSpecT engine.
     * 
     * @param id
     *            the identifier of the tuplecentre target of the spawned
     *            activity.
     */
    public final void setTargetTC(final TucsonTupleCentreId id) {
        this.target = id;
    }

    /**
     * @param tuple
     *            the tuple argument of the operation
     * @return the tuple result of the operation
     * @see OrdinarySyncACC OrdinarySyncACC
     */
    protected final LogicTuple in(final LogicTuple tuple) {
        if (this.aid != null) {
            try {
                // Operation Make
                final RespectOperationDefault opRequested = RespectOperationDefault.make(
                        TupleCentreOpType.IN, tuple, null);
                // InputEvent Creation
                final InputEvent ev = new InputEvent(this.aid, opRequested,
                        this.target, System.currentTimeMillis(), null);
                return (LogicTuple) TupleCentreContainer
                        .doBlockingOperation(ev);
                // return (LogicTuple) TupleCentreContainer.doBlockingOperation(
                // TupleCentreOpType.IN, this.aid, this.target, tuple);
            } catch (final TucsonInvalidLogicTupleException e) {
                e.printStackTrace();
            } catch (final TucsonOperationNotPossibleException e) {
                e.printStackTrace();
            } catch (final InvalidLogicTupleException e) {
                e.printStackTrace();
            }
        } else {
            try {
                // Operation Make
                final RespectOperationDefault opRequested = RespectOperationDefault.make(
                        TupleCentreOpType.IN, tuple, null);
                // InputEvent Creation
                final InputEvent ev = new InputEvent(this.tcid, opRequested,
                        this.target, System.currentTimeMillis(), null);
                return (LogicTuple) TupleCentreContainer
                        .doBlockingOperation(ev);
                // return (LogicTuple) TupleCentreContainer
                // .doBlockingOperation(TupleCentreOpType.IN,
                // this.tcid, this.target, tuple);
            } catch (final TucsonInvalidLogicTupleException e) {
                e.printStackTrace();
            } catch (final TucsonOperationNotPossibleException e) {
                e.printStackTrace();
            } catch (final InvalidLogicTupleException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * @param tuple
     *            the tuple argument of the operation
     * @return the list of tuples result of the operation
     * @see BulkSyncACC BulkSyncACC
     */
    protected final List<LogicTuple> inAll(final LogicTuple tuple) {
        if (this.aid != null) {
            try {
                // Operation Make
                final RespectOperationDefault opRequested = RespectOperationDefault.make(
                        TupleCentreOpType.IN_ALL, tuple, null);
                // InputEvent Creation
                final InputEvent ev = new InputEvent(this.aid, opRequested,
                        this.target, System.currentTimeMillis(), null);
                return (List<LogicTuple>) TupleCentreContainer
                        .doBlockingOperation(ev);
                // return (List<LogicTuple>) TupleCentreContainer
                // .doBlockingOperation(TupleCentreOpType.IN_ALL,
                // this.aid, this.target, tuple);
            } catch (final TucsonInvalidLogicTupleException e) {
                e.printStackTrace();
            } catch (final TucsonOperationNotPossibleException e) {
                e.printStackTrace();
            } catch (final InvalidLogicTupleException e) {
                e.printStackTrace();
            }
        } else {
            try {
                // Operation Make
                final RespectOperationDefault opRequested = RespectOperationDefault.make(
                        TupleCentreOpType.IN_ALL, tuple, null);
                // InputEvent Creation
                final InputEvent ev = new InputEvent(this.tcid, opRequested,
                        this.target, System.currentTimeMillis(), null);
                return (List<LogicTuple>) TupleCentreContainer
                        .doBlockingOperation(ev);
                // return (List<LogicTuple>) TupleCentreContainer
                // .doBlockingOperation(TupleCentreOpType.IN_ALL,
                // this.tcid, this.target, tuple);
            } catch (final TucsonInvalidLogicTupleException e) {
                e.printStackTrace();
            } catch (final TucsonOperationNotPossibleException e) {
                e.printStackTrace();
            } catch (final InvalidLogicTupleException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * @param tuple
     *            the tuple argument of the operation
     * @return the tuple result of the operation
     * @see OrdinarySyncACC OrdinarySyncACC
     */
    protected final LogicTuple inp(final LogicTuple tuple) {
        if (this.aid != null) {
            try {
                // Operation Make
                final RespectOperationDefault opRequested = RespectOperationDefault.make(
                        TupleCentreOpType.INP, tuple, null);
                // InputEvent Creation
                final InputEvent ev = new InputEvent(this.aid, opRequested,
                        this.target, System.currentTimeMillis(), null);
                return (LogicTuple) TupleCentreContainer
                        .doBlockingOperation(ev);
                // return (LogicTuple) TupleCentreContainer
                // .doBlockingOperation(TupleCentreOpType.INP,
                // this.aid, this.target, tuple);
            } catch (final TucsonInvalidLogicTupleException e) {
                e.printStackTrace();
            } catch (final TucsonOperationNotPossibleException e) {
                e.printStackTrace();
            } catch (final InvalidLogicTupleException e) {
                e.printStackTrace();
            }
        } else {
            try {
                // Operation Make
                final RespectOperationDefault opRequested = RespectOperationDefault.make(
                        TupleCentreOpType.INP, tuple, null);
                // InputEvent Creation
                final InputEvent ev = new InputEvent(this.tcid, opRequested,
                        this.target, System.currentTimeMillis(), null);
                return (LogicTuple) TupleCentreContainer
                        .doBlockingOperation(ev);
                // return (LogicTuple) TupleCentreContainer.doBlockingOperation(
                // TupleCentreOpType.INP, this.tcid, this.target,
                // tuple);
            } catch (final TucsonInvalidLogicTupleException e) {
                e.printStackTrace();
            } catch (final TucsonOperationNotPossibleException e) {
                e.printStackTrace();
            } catch (final InvalidLogicTupleException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Standard output log utility.
     * 
     * @param msg
     *            the message to log on standard output.
     */
    protected void log(final String msg) {
        if (this.aid != null) {
            System.out.println("[" + this.aid.getAgentName() + "-spawned]: "
                    + msg);
        } else {
            System.out
                    .println("[" + this.tcid.toString() + "-spawned]: " + msg);
        }
    }

    /**
     * @param tuple
     *            the tuple argument of the operation
     * @return the tuple result of the operation
     * @see OrdinarySyncACC OrdinarySyncACC
     */
    protected final LogicTuple no(final LogicTuple tuple) {
        if (this.aid != null) {
            try {
                // Operation Make
                final RespectOperationDefault opRequested = RespectOperationDefault.make(
                        TupleCentreOpType.NO, tuple, null);
                // InputEvent Creation
                final InputEvent ev = new InputEvent(this.aid, opRequested,
                        this.target, System.currentTimeMillis(), null);
                return (LogicTuple) TupleCentreContainer
                        .doBlockingOperation(ev);
                // return (LogicTuple) TupleCentreContainer.doBlockingOperation(
                // TupleCentreOpType.NO, this.aid, this.target, tuple);
            } catch (final TucsonInvalidLogicTupleException e) {
                e.printStackTrace();
            } catch (final TucsonOperationNotPossibleException e) {
                e.printStackTrace();
            } catch (final InvalidLogicTupleException e) {
                e.printStackTrace();
            }
        } else {
            try {
                // Operation Make
                final RespectOperationDefault opRequested = RespectOperationDefault.make(
                        TupleCentreOpType.NO, tuple, null);
                // InputEvent Creation
                final InputEvent ev = new InputEvent(this.tcid, opRequested,
                        this.target, System.currentTimeMillis(), null);
                return (LogicTuple) TupleCentreContainer
                        .doBlockingOperation(ev);
                // return (LogicTuple) TupleCentreContainer
                // .doBlockingOperation(TupleCentreOpType.NO,
                // this.tcid, this.target, tuple);
            } catch (final TucsonInvalidLogicTupleException e) {
                e.printStackTrace();
            } catch (final TucsonOperationNotPossibleException e) {
                e.printStackTrace();
            } catch (final InvalidLogicTupleException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * @param tuple
     *            the tuple argument of the operation
     * @return the list of tuples result of the operation
     * @see BulkSyncACC BulkSyncACC
     */
    protected final List<LogicTuple> noAll(final LogicTuple tuple) {
        if (this.aid != null) {
            try {
                // Operation Make
                final RespectOperationDefault opRequested = RespectOperationDefault.make(
                        TupleCentreOpType.NO_ALL, tuple, null);
                // InputEvent Creation
                final InputEvent ev = new InputEvent(this.aid, opRequested,
                        this.target, System.currentTimeMillis(), null);
                return (List<LogicTuple>) TupleCentreContainer
                        .doBlockingOperation(ev);
                // return (List<LogicTuple>) TupleCentreContainer
                // .doBlockingOperation(TupleCentreOpType.NO_ALL,
                // this.aid, this.target, tuple);
            } catch (final TucsonInvalidLogicTupleException e) {
                e.printStackTrace();
            } catch (final TucsonOperationNotPossibleException e) {
                e.printStackTrace();
            } catch (final InvalidLogicTupleException e) {
                e.printStackTrace();
            }
        } else {
            try {
                // Operation Make
                final RespectOperationDefault opRequested = RespectOperationDefault.make(
                        TupleCentreOpType.NO_ALL, tuple, null);
                // InputEvent Creation
                final InputEvent ev = new InputEvent(this.tcid, opRequested,
                        this.target, System.currentTimeMillis(), null);
                return (List<LogicTuple>) TupleCentreContainer
                        .doBlockingOperation(ev);
                // return (List<LogicTuple>) TupleCentreContainer
                // .doBlockingOperation(TupleCentreOpType.NO_ALL,
                // this.tcid, this.target, tuple);
            } catch (final TucsonInvalidLogicTupleException e) {
                e.printStackTrace();
            } catch (final TucsonOperationNotPossibleException e) {
                e.printStackTrace();
            } catch (final InvalidLogicTupleException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * @param tuple
     *            the tuple argument of the operation
     * @return the tuple result of the operation
     * @see OrdinarySyncACC OrdinarySyncACC
     */
    protected final LogicTuple nop(final LogicTuple tuple) {
        if (this.aid != null) {
            try {
                // Operation Make
                final RespectOperationDefault opRequested = RespectOperationDefault.make(
                        TupleCentreOpType.NOP, tuple, null);
                // InputEvent Creation
                final InputEvent ev = new InputEvent(this.aid, opRequested,
                        this.target, System.currentTimeMillis(), null);
                return (LogicTuple) TupleCentreContainer
                        .doBlockingOperation(ev);
                // return (LogicTuple) TupleCentreContainer
                // .doBlockingOperation(TupleCentreOpType.NOP,
                // this.aid, this.target, tuple);
            } catch (final TucsonInvalidLogicTupleException e) {
                e.printStackTrace();
            } catch (final TucsonOperationNotPossibleException e) {
                e.printStackTrace();
            } catch (final InvalidLogicTupleException e) {
                e.printStackTrace();
            }
        } else {
            try {
                // Operation Make
                final RespectOperationDefault opRequested = RespectOperationDefault.make(
                        TupleCentreOpType.NOP, tuple, null);
                // InputEvent Creation
                final InputEvent ev = new InputEvent(this.tcid, opRequested,
                        this.target, System.currentTimeMillis(), null);
                return (LogicTuple) TupleCentreContainer
                        .doBlockingOperation(ev);
                // return (LogicTuple) TupleCentreContainer.doBlockingOperation(
                // TupleCentreOpType.NOP, this.tcid, this.target,
                // tuple);
            } catch (final TucsonInvalidLogicTupleException e) {
                e.printStackTrace();
            } catch (final TucsonOperationNotPossibleException e) {
                e.printStackTrace();
            } catch (final InvalidLogicTupleException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * @param tuple
     *            the tuple argument of the operation
     * @return the tuple result of the operation
     * @see OrdinarySyncACC OrdinarySyncACC
     */
    protected final LogicTuple out(final LogicTuple tuple) {
        if (this.aid != null) {
            try {
                // Operation Make
                final RespectOperationDefault opRequested = RespectOperationDefault.make(
                        TupleCentreOpType.OUT, tuple, null);
                // InputEvent Creation
                final InputEvent ev = new InputEvent(this.aid, opRequested,
                        this.target, System.currentTimeMillis(), null);
                return (LogicTuple) TupleCentreContainer
                        .doBlockingOperation(ev);
                // return (LogicTuple) TupleCentreContainer
                // .doBlockingOperation(TupleCentreOpType.OUT,
                // this.aid, this.target, tuple);
            } catch (final TucsonInvalidLogicTupleException e) {
                e.printStackTrace();
            } catch (final TucsonOperationNotPossibleException e) {
                e.printStackTrace();
            } catch (final InvalidLogicTupleException e) {
                e.printStackTrace();
            }
        } else {
            try {
                // Operation Make
                final RespectOperationDefault opRequested = RespectOperationDefault.make(
                        TupleCentreOpType.OUT, tuple, null);
                // InputEvent Creation
                final InputEvent ev = new InputEvent(this.tcid, opRequested,
                        this.target, System.currentTimeMillis(), null);
                return (LogicTuple) TupleCentreContainer
                        .doBlockingOperation(ev);
                // return (LogicTuple) TupleCentreContainer.doBlockingOperation(
                // TupleCentreOpType.OUT, this.tcid, this.target,
                // tuple);
            } catch (final TucsonInvalidLogicTupleException e) {
                e.printStackTrace();
            } catch (final TucsonOperationNotPossibleException e) {
                e.printStackTrace();
            } catch (final InvalidLogicTupleException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * @param tuple
     *            the tuple argument of the operation
     * @return the list of tuples result of the operation
     * @see BulkSyncACC BulkSyncACC
     */
    protected final List<LogicTuple> outAll(final LogicTuple tuple) {
        if (this.aid != null) {
            try {
                // Operation Make
                final RespectOperationDefault opRequested = RespectOperationDefault.make(
                        TupleCentreOpType.OUT_ALL, tuple, null);
                // InputEvent Creation
                final InputEvent ev = new InputEvent(this.aid, opRequested,
                        this.target, System.currentTimeMillis(), null);
                return (List<LogicTuple>) TupleCentreContainer
                        .doBlockingOperation(ev);
                // return (List<LogicTuple>) TupleCentreContainer
                // .doBlockingOperation(TupleCentreOpType.OUT_ALL,
                // this.aid, this.target, tuple);
            } catch (final TucsonInvalidLogicTupleException e) {
                e.printStackTrace();
            } catch (final TucsonOperationNotPossibleException e) {
                e.printStackTrace();
            } catch (final InvalidLogicTupleException e) {
                e.printStackTrace();
            }
        } else {
            try {
                // Operation Make
                final RespectOperationDefault opRequested = RespectOperationDefault.make(
                        TupleCentreOpType.OUT_ALL, tuple, null);
                // InputEvent Creation
                final InputEvent ev = new InputEvent(this.tcid, opRequested,
                        this.target, System.currentTimeMillis(), null);
                return (List<LogicTuple>) TupleCentreContainer
                        .doBlockingOperation(ev);
                // return (List<LogicTuple>) TupleCentreContainer
                // .doBlockingOperation(TupleCentreOpType.OUT_ALL,
                // this.tcid, this.target, tuple);
            } catch (final TucsonInvalidLogicTupleException e) {
                e.printStackTrace();
            } catch (final TucsonOperationNotPossibleException e) {
                e.printStackTrace();
            } catch (final InvalidLogicTupleException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * @param tuple
     *            the tuple argument of the operation
     * @return the tuple result of the operation
     * @see OrdinarySyncACC OrdinarySyncACC
     */
    protected final LogicTuple rd(final LogicTuple tuple) {
        if (this.aid != null) {
            try {
                // Operation Make
                final RespectOperationDefault opRequested = RespectOperationDefault.make(
                        TupleCentreOpType.RD, tuple, null);
                // InputEvent Creation
                final InputEvent ev = new InputEvent(this.aid, opRequested,
                        this.target, System.currentTimeMillis(), null);
                return (LogicTuple) TupleCentreContainer
                        .doBlockingOperation(ev);
                // return (LogicTuple) TupleCentreContainer.doBlockingOperation(
                // TupleCentreOpType.RD, this.aid, this.target, tuple);
            } catch (final TucsonInvalidLogicTupleException e) {
                e.printStackTrace();
            } catch (final TucsonOperationNotPossibleException e) {
                e.printStackTrace();
            } catch (final InvalidLogicTupleException e) {
                e.printStackTrace();
            }
        } else {
            try {
                // Operation Make
                final RespectOperationDefault opRequested = RespectOperationDefault.make(
                        TupleCentreOpType.RD, tuple, null);
                // InputEvent Creation
                final InputEvent ev = new InputEvent(this.tcid, opRequested,
                        this.target, System.currentTimeMillis(), null);
                return (LogicTuple) TupleCentreContainer
                        .doBlockingOperation(ev);
                // return (LogicTuple) TupleCentreContainer
                // .doBlockingOperation(TupleCentreOpType.RD,
                // this.tcid, this.target, tuple);
            } catch (final TucsonInvalidLogicTupleException e) {
                e.printStackTrace();
            } catch (final TucsonOperationNotPossibleException e) {
                e.printStackTrace();
            } catch (final InvalidLogicTupleException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * @param tuple
     *            the tuple argument of the operation
     * @return the list of tuples result of the operation
     * @see BulkSyncACC BulkSyncACC
     */
    protected final List<LogicTuple> rdAll(final LogicTuple tuple) {
        if (this.aid != null) {
            try {
                // Operation Make
                final RespectOperationDefault opRequested = RespectOperationDefault.make(
                        TupleCentreOpType.RD_ALL, tuple, null);
                // InputEvent Creation
                final InputEvent ev = new InputEvent(this.aid, opRequested,
                        this.target, System.currentTimeMillis(), null);
                return (List<LogicTuple>) TupleCentreContainer
                        .doBlockingOperation(ev);
                // return (List<LogicTuple>) TupleCentreContainer
                // .doBlockingOperation(TupleCentreOpType.RD_ALL,
                // this.aid, this.target, tuple);
            } catch (final TucsonInvalidLogicTupleException e) {
                e.printStackTrace();
            } catch (final TucsonOperationNotPossibleException e) {
                e.printStackTrace();
            } catch (final InvalidLogicTupleException e) {
                e.printStackTrace();
            }
        } else {
            try {
                // Operation Make
                final RespectOperationDefault opRequested = RespectOperationDefault.make(
                        TupleCentreOpType.RD_ALL, tuple, null);
                // InputEvent Creation
                final InputEvent ev = new InputEvent(this.tcid, opRequested,
                        this.target, System.currentTimeMillis(), null);
                return (List<LogicTuple>) TupleCentreContainer
                        .doBlockingOperation(ev);
                // return (List<LogicTuple>) TupleCentreContainer
                // .doBlockingOperation(TupleCentreOpType.RD_ALL,
                // this.tcid, this.target, tuple);
            } catch (final TucsonInvalidLogicTupleException e) {
                e.printStackTrace();
            } catch (final TucsonOperationNotPossibleException e) {
                e.printStackTrace();
            } catch (final InvalidLogicTupleException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * @param tuple
     *            the tuple argument of the operation
     * @return the tuple result of the operation
     * @see OrdinarySyncACC OrdinarySyncACC
     */
    protected final LogicTuple rdp(final LogicTuple tuple) {
        if (this.aid != null) {
            try {
                // Operation Make
                final RespectOperationDefault opRequested = RespectOperationDefault.make(
                        TupleCentreOpType.RDP, tuple, null);
                // InputEvent Creation
                final InputEvent ev = new InputEvent(this.aid, opRequested,
                        this.target, System.currentTimeMillis(), null);
                return (LogicTuple) TupleCentreContainer
                        .doBlockingOperation(ev);
                // return (LogicTuple) TupleCentreContainer
                // .doBlockingOperation(TupleCentreOpType.RDP,
                // this.aid, this.target, tuple);
            } catch (final TucsonInvalidLogicTupleException e) {
                e.printStackTrace();
            } catch (final TucsonOperationNotPossibleException e) {
                e.printStackTrace();
            } catch (final InvalidLogicTupleException e) {
                e.printStackTrace();
            }
        } else {
            try {
                // Operation Make
                final RespectOperationDefault opRequested = RespectOperationDefault.make(
                        TupleCentreOpType.RDP, tuple, null);
                // InputEvent Creation
                final InputEvent ev = new InputEvent(this.tcid, opRequested,
                        this.target, System.currentTimeMillis(), null);
                return (LogicTuple) TupleCentreContainer
                        .doBlockingOperation(ev);
                // return (LogicTuple) TupleCentreContainer.doBlockingOperation(
                // TupleCentreOpType.RDP, this.tcid, this.target,
                // tuple);
            } catch (final TucsonInvalidLogicTupleException e) {
                e.printStackTrace();
            } catch (final TucsonOperationNotPossibleException e) {
                e.printStackTrace();
            } catch (final InvalidLogicTupleException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * @param tuple
     *            the tuple argument of the operation
     * @return the tuple result of the operation
     * @see UniformSyncACC UniformSyncACC
     */
    protected final LogicTuple uin(final LogicTuple tuple) {
        if (this.aid != null) {
            try {
                // Operation Make
                final RespectOperationDefault opRequested = RespectOperationDefault.make(
                        TupleCentreOpType.UIN, tuple, null);
                // InputEvent Creation
                final InputEvent ev = new InputEvent(this.aid, opRequested,
                        this.target, System.currentTimeMillis(), null);
                return (LogicTuple) TupleCentreContainer
                        .doBlockingOperation(ev);
                // return (LogicTuple) TupleCentreContainer
                // .doBlockingOperation(TupleCentreOpType.UIN,
                // this.aid, this.target, tuple);
            } catch (final TucsonInvalidLogicTupleException e) {
                e.printStackTrace();
            } catch (final TucsonOperationNotPossibleException e) {
                e.printStackTrace();
            } catch (final InvalidLogicTupleException e) {
                e.printStackTrace();
            }
        } else {
            try {
                // Operation Make
                final RespectOperationDefault opRequested = RespectOperationDefault.make(
                        TupleCentreOpType.UIN, tuple, null);
                // InputEvent Creation
                final InputEvent ev = new InputEvent(this.tcid, opRequested,
                        this.target, System.currentTimeMillis(), null);
                return (LogicTuple) TupleCentreContainer
                        .doBlockingOperation(ev);
                // return (LogicTuple) TupleCentreContainer.doBlockingOperation(
                // TupleCentreOpType.UIN, this.tcid, this.target,
                // tuple);
            } catch (final TucsonInvalidLogicTupleException e) {
                e.printStackTrace();
            } catch (final TucsonOperationNotPossibleException e) {
                e.printStackTrace();
            } catch (final InvalidLogicTupleException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * @param tuple
     *            the tuple argument of the operation
     * @return the tuple result of the operation
     * @see UniformSyncACC UniformSyncACC
     */
    protected final LogicTuple uinp(final LogicTuple tuple) {
        if (this.aid != null) {
            try {
                // Operation Make
                final RespectOperationDefault opRequested = RespectOperationDefault.make(
                        TupleCentreOpType.UINP, tuple, null);
                // InputEvent Creation
                final InputEvent ev = new InputEvent(this.aid, opRequested,
                        this.target, System.currentTimeMillis(), null);
                return (LogicTuple) TupleCentreContainer
                        .doBlockingOperation(ev);
                // return (LogicTuple) TupleCentreContainer.doBlockingOperation(
                // TupleCentreOpType.UINP, this.aid, this.target,
                // tuple);
            } catch (final TucsonInvalidLogicTupleException e) {
                e.printStackTrace();
            } catch (final TucsonOperationNotPossibleException e) {
                e.printStackTrace();
            } catch (final InvalidLogicTupleException e) {
                e.printStackTrace();
            }
        } else {
            try {
                // Operation Make
                final RespectOperationDefault opRequested = RespectOperationDefault.make(
                        TupleCentreOpType.UINP, tuple, null);
                // InputEvent Creation
                final InputEvent ev = new InputEvent(this.tcid, opRequested,
                        this.target, System.currentTimeMillis(), null);
                return (LogicTuple) TupleCentreContainer
                        .doBlockingOperation(ev);
                // return (LogicTuple) TupleCentreContainer.doBlockingOperation(
                // TupleCentreOpType.UINP, this.tcid, this.target,
                // tuple);
            } catch (final TucsonInvalidLogicTupleException e) {
                e.printStackTrace();
            } catch (final TucsonOperationNotPossibleException e) {
                e.printStackTrace();
            } catch (final InvalidLogicTupleException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * @param tuple
     *            the tuple argument of the operation
     * @return the tuple result of the operation
     * @see UniformSyncACC UniformSyncACC
     */
    protected final LogicTuple uno(final LogicTuple tuple) {
        if (this.aid != null) {
            try {
                // Operation Make
                final RespectOperationDefault opRequested = RespectOperationDefault.make(
                        TupleCentreOpType.UNO, tuple, null);
                // InputEvent Creation
                final InputEvent ev = new InputEvent(this.aid, opRequested,
                        this.target, System.currentTimeMillis(), null);
                return (LogicTuple) TupleCentreContainer
                        .doBlockingOperation(ev);
                // return (LogicTuple) TupleCentreContainer
                // .doBlockingOperation(TupleCentreOpType.UNO,
                // this.aid, this.target, tuple);
            } catch (final TucsonInvalidLogicTupleException e) {
                e.printStackTrace();
            } catch (final TucsonOperationNotPossibleException e) {
                e.printStackTrace();
            } catch (final InvalidLogicTupleException e) {
                e.printStackTrace();
            }
        } else {
            try {
                // Operation Make
                final RespectOperationDefault opRequested = RespectOperationDefault.make(
                        TupleCentreOpType.UNO, tuple, null);
                // InputEvent Creation
                final InputEvent ev = new InputEvent(this.tcid, opRequested,
                        this.target, System.currentTimeMillis(), null);
                return (LogicTuple) TupleCentreContainer
                        .doBlockingOperation(ev);
                // return (LogicTuple) TupleCentreContainer.doBlockingOperation(
                // TupleCentreOpType.UNO, this.tcid, this.target,
                // tuple);
            } catch (final TucsonInvalidLogicTupleException e) {
                e.printStackTrace();
            } catch (final TucsonOperationNotPossibleException e) {
                e.printStackTrace();
            } catch (final InvalidLogicTupleException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * @param tuple
     *            the tuple argument of the operation
     * @return the tuple result of the operation
     * @see UniformSyncACC UniformSyncACC
     */
    protected final LogicTuple unop(final LogicTuple tuple) {
        if (this.aid != null) {
            try {
                // Operation Make
                final RespectOperationDefault opRequested = RespectOperationDefault.make(
                        TupleCentreOpType.UNOP, tuple, null);
                // InputEvent Creation
                final InputEvent ev = new InputEvent(this.aid, opRequested,
                        this.target, System.currentTimeMillis(), null);
                return (LogicTuple) TupleCentreContainer
                        .doBlockingOperation(ev);
                // return (LogicTuple) TupleCentreContainer.doBlockingOperation(
                // TupleCentreOpType.UNOP, this.aid, this.target,
                // tuple);
            } catch (final TucsonInvalidLogicTupleException e) {
                e.printStackTrace();
            } catch (final TucsonOperationNotPossibleException e) {
                e.printStackTrace();
            } catch (final InvalidLogicTupleException e) {
                e.printStackTrace();
            }
        } else {
            try {
                // Operation Make
                final RespectOperationDefault opRequested = RespectOperationDefault.make(
                        TupleCentreOpType.UNOP, tuple, null);
                // InputEvent Creation
                final InputEvent ev = new InputEvent(this.tcid, opRequested,
                        this.target, System.currentTimeMillis(), null);
                return (LogicTuple) TupleCentreContainer
                        .doBlockingOperation(ev);
                // return (LogicTuple) TupleCentreContainer.doBlockingOperation(
                // TupleCentreOpType.UNOP, this.tcid, this.target,
                // tuple);
            } catch (final TucsonInvalidLogicTupleException e) {
                e.printStackTrace();
            } catch (final TucsonOperationNotPossibleException e) {
                e.printStackTrace();
            } catch (final InvalidLogicTupleException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * @param tuple
     *            the tuple argument of the operation
     * @return the tuple result of the operation
     * @see UniformSyncACC UniformSyncACC
     */
    protected final LogicTuple urd(final LogicTuple tuple) {
        if (this.aid != null) {
            try {
                // Operation Make
                final RespectOperationDefault opRequested = RespectOperationDefault.make(
                        TupleCentreOpType.URD, tuple, null);
                // InputEvent Creation
                final InputEvent ev = new InputEvent(this.aid, opRequested,
                        this.target, System.currentTimeMillis(), null);
                return (LogicTuple) TupleCentreContainer
                        .doBlockingOperation(ev);
                // return (LogicTuple) TupleCentreContainer
                // .doBlockingOperation(TupleCentreOpType.URD,
                // this.aid, this.target, tuple);
            } catch (final TucsonInvalidLogicTupleException e) {
                e.printStackTrace();
            } catch (final TucsonOperationNotPossibleException e) {
                e.printStackTrace();
            } catch (final InvalidLogicTupleException e) {
                e.printStackTrace();
            }
        } else {
            try {
                // Operation Make
                final RespectOperationDefault opRequested = RespectOperationDefault.make(
                        TupleCentreOpType.URD, tuple, null);
                // InputEvent Creation
                final InputEvent ev = new InputEvent(this.tcid, opRequested,
                        this.target, System.currentTimeMillis(), null);
                return (LogicTuple) TupleCentreContainer
                        .doBlockingOperation(ev);
                // return (LogicTuple) TupleCentreContainer.doBlockingOperation(
                // TupleCentreOpType.URD, this.tcid, this.target,
                // tuple);
            } catch (final TucsonInvalidLogicTupleException e) {
                e.printStackTrace();
            } catch (final TucsonOperationNotPossibleException e) {
                e.printStackTrace();
            } catch (final InvalidLogicTupleException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * @param tuple
     *            the tuple argument of the operation
     * @return the tuple result of the operation
     * @see UniformSyncACC UniformSyncACC
     */
    protected final LogicTuple urdp(final LogicTuple tuple) {
        if (this.aid != null) {
            try {
                // Operation Make
                final RespectOperationDefault opRequested = RespectOperationDefault.make(
                        TupleCentreOpType.URDP, tuple, null);
                // InputEvent Creation
                final InputEvent ev = new InputEvent(this.aid, opRequested,
                        this.target, System.currentTimeMillis(), null);
                return (LogicTuple) TupleCentreContainer
                        .doBlockingOperation(ev);
                // return (LogicTuple) TupleCentreContainer.doBlockingOperation(
                // TupleCentreOpType.URDP, this.aid, this.target,
                // tuple);
            } catch (final TucsonInvalidLogicTupleException e) {
                e.printStackTrace();
            } catch (final TucsonOperationNotPossibleException e) {
                e.printStackTrace();
            } catch (final InvalidLogicTupleException e) {
                e.printStackTrace();
            }
        } else {
            try {
                // Operation Make
                final RespectOperationDefault opRequested = RespectOperationDefault.make(
                        TupleCentreOpType.URDP, tuple, null);
                // InputEvent Creation
                final InputEvent ev = new InputEvent(this.tcid, opRequested,
                        this.target, System.currentTimeMillis(), null);
                return (LogicTuple) TupleCentreContainer
                        .doBlockingOperation(ev);
                // return (LogicTuple) TupleCentreContainer.doBlockingOperation(
                // TupleCentreOpType.URDP, this.tcid, this.target,
                // tuple);
            } catch (final TucsonInvalidLogicTupleException e) {
                e.printStackTrace();
            } catch (final TucsonOperationNotPossibleException e) {
                e.printStackTrace();
            } catch (final InvalidLogicTupleException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
