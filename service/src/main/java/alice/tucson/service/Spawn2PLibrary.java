package alice.tucson.service;

import java.util.Iterator;
import java.util.List;

import alice.tuple.logic.LogicTuple;
import alice.tuple.logic.exceptions.InvalidLogicTupleException;
import alice.respect.core.RespectOperationDefault;
import alice.tucson.api.TucsonAgentId;
import alice.tucson.api.TucsonTupleCentreId;
import alice.tucson.api.exceptions.TucsonInvalidLogicTupleException;
import alice.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tuple.Tuple;
import alice.tuplecentre.api.TupleCentreOperation;
import alice.tuplecentre.core.InputEvent;
import alice.tuplecentre.core.TupleCentreOpType;
import alice.tuprolog.Library;
import alice.tuprolog.Struct;
import alice.tuprolog.Term;

/**
 *
 *
 * @author ste (mailto: s.mariani@unibo.it)
 * @author (contributor) Michele Bombardi (mailto:
 *         michele.bombardi@studio.unibo.it)
 */
public class Spawn2PLibrary extends Library {

    private static final long serialVersionUID = 3019036120338145017L;

    /**
     * Utility to convert a list of tuple into a tuple list of tuples
     *
     * @param list
     *            the list of tuples to convert
     *
     * @return the tuple list of tuples result of the conversion
     */
    private static Term list2tuple(final List<?extends Tuple> list) {
        final Term[] termArray = new Term[list.size()];
        final Iterator<?extends Tuple> it = list.iterator();
        int i = 0;
        while (it.hasNext()) {
            termArray[i] = ((LogicTuple) it.next()).toTerm();
            i++;
        }
        return new Struct(termArray);
    }

    private TucsonAgentId aid;
    private TucsonTupleCentreId target;
    private TucsonTupleCentreId tcid;

    /**
     * Both agents and the coordination medium itself can spawn() a computation,
     * hence we need to handle both.
     *
     * @return the "spawner" id (actually, a generic wrapper hosting either a
     *         TucsonAgentId or a TucsonTupleCentreId, accessible with method
     *         <code> getId() </code>)
     */
    public final TucsonIdWrapper<?> getSpawnerId() {
        if (this.aid == null) {
            return new TucsonIdWrapper<TucsonTupleCentreId>(this.tcid);
        }
        return new TucsonIdWrapper<TucsonAgentId>(this.aid);
    }

    /**
     *
     * @return the tuple centre identifier of the target tuple centre
     */
    public final TucsonTupleCentreId getTargetTC() {
        return this.target;
    }

    /**
     * @return the String representation of the theory available to 2p agents
     */
    @Override
    public String getTheory() {
        return "out(T). \n" + "in(T). \n" + "inp(T). \n" + "rd(T). \n"
                + "rdp(T). \n" + "no(T). \n" + "nop(T). \n" + "out_all(L). \n"
                + "in_all(T,L). \n" + "rd_all(T,L). \n" + "no_all(T,L). \n"
                + "uin(T). \n" + "uinp(T). \n" + "urd(T). \n" + "urdp(T). \n"
                + "uno(T). \n" + "unop(T). \n";
    }

    /**
     *
     * @param arg0
     *            the tuple template argument of the operation
     * @return wether the operation has been succesfully completed or not
     */
    public boolean in_1(final Term arg0) {
        TupleCentreOperation op = null;
        final LogicTuple arg = new LogicTuple(arg0);
        if (this.aid != null) {
            try {
            	// Operation Make
                final RespectOperationDefault opRequested = RespectOperationDefault.make(
                        TupleCentreOpType.IN, arg, null);
                // InputEvent Creation
                final InputEvent ev = new InputEvent(this.aid, opRequested,
                        this.target, System.currentTimeMillis(), null);
                op = TupleCentreContainer.doNonBlockingOperation(ev);
            } catch (final TucsonInvalidLogicTupleException e) {
                e.printStackTrace();
                return false;
            } catch (final TucsonOperationNotPossibleException e) {
                e.printStackTrace();
                return false;
            } catch (InvalidLogicTupleException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        } else {
            try {
            	// Operation Make
                final RespectOperationDefault opRequested = RespectOperationDefault.make(
                        TupleCentreOpType.IN, arg, null);
                // InputEvent Creation
                final InputEvent ev = new InputEvent(this.tcid, opRequested,
                        this.target, System.currentTimeMillis(), null);
                op = TupleCentreContainer.doNonBlockingOperation(ev);
            } catch (final TucsonInvalidLogicTupleException e) {
                e.printStackTrace();
                return false;
            } catch (final TucsonOperationNotPossibleException e) {
                e.printStackTrace();
                return false;
            } catch (InvalidLogicTupleException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        op.waitForOperationCompletion();
        if (op.isResultSuccess()) {
            final Term term = ((LogicTuple) op.getTupleResult()).toTerm();
            return this.unify(arg0, term);
        }
        return false;
    }

    /**
     *
     * @param arg0
     *            the tuple template argument of the operation
     * @param arg1
     *            the tuple result of the operation
     * @return wether the operation has been succesfully completed or not
     */
    public boolean in_all_2(final Term arg0, final Term arg1) {
        TupleCentreOperation op = null;
        final LogicTuple arg = new LogicTuple(arg0);
        if (this.aid != null) {
            try {
            	// Operation Make
                final RespectOperationDefault opRequested = RespectOperationDefault.make(
                        TupleCentreOpType.IN_ALL, arg, null);
                // InputEvent Creation
                final InputEvent ev = new InputEvent(this.aid, opRequested,
                        this.target, System.currentTimeMillis(), null);
                op = TupleCentreContainer.doNonBlockingOperation(ev);
            } catch (final TucsonInvalidLogicTupleException e) {
                e.printStackTrace();
                return false;
            } catch (final TucsonOperationNotPossibleException e) {
                e.printStackTrace();
                return false;
            } catch (InvalidLogicTupleException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        } else {
            try {
            	// Operation Make
                final RespectOperationDefault opRequested = RespectOperationDefault.make(
                        TupleCentreOpType.IN_ALL, arg, null);
                // InputEvent Creation
                final InputEvent ev = new InputEvent(this.tcid, opRequested,
                        this.target, System.currentTimeMillis(), null);
                op = TupleCentreContainer.doNonBlockingOperation(ev);
            } catch (final TucsonInvalidLogicTupleException e) {
                e.printStackTrace();
                return false;
            } catch (final TucsonOperationNotPossibleException e) {
                e.printStackTrace();
                return false;
            } catch (InvalidLogicTupleException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        op.waitForOperationCompletion();
        if (op.isResultSuccess()) {
            return this.unify(arg1,
                    Spawn2PLibrary.list2tuple(op.getTupleListResult()));
        }
        return false;
    }

    /**
     *
     * @param arg0
     *            the tuple template argument of the operation
     * @return wether the operation has been succesfully completed or not
     */
    public boolean inp_1(final Term arg0) {
        TupleCentreOperation op = null;
        final LogicTuple arg = new LogicTuple(arg0);
        if (this.aid != null) {
            try {
            	// Operation Make
                final RespectOperationDefault opRequested = RespectOperationDefault.make(
                        TupleCentreOpType.INP, arg, null);
                // InputEvent Creation
                final InputEvent ev = new InputEvent(this.aid, opRequested,
                        this.target, System.currentTimeMillis(), null);
                op = TupleCentreContainer.doNonBlockingOperation(ev);
            } catch (final TucsonInvalidLogicTupleException e) {
                e.printStackTrace();
                return false;
            } catch (final TucsonOperationNotPossibleException e) {
                e.printStackTrace();
                return false;
            } catch (InvalidLogicTupleException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        } else {
            try {
            	// Operation Make
                final RespectOperationDefault opRequested = RespectOperationDefault.make(
                        TupleCentreOpType.INP, arg, null);
                // InputEvent Creation
                final InputEvent ev = new InputEvent(this.tcid, opRequested,
                        this.target, System.currentTimeMillis(), null);
                op = TupleCentreContainer.doNonBlockingOperation(ev);
            } catch (final TucsonInvalidLogicTupleException e) {
                e.printStackTrace();
                return false;
            } catch (final TucsonOperationNotPossibleException e) {
                e.printStackTrace();
                return false;
            } catch (InvalidLogicTupleException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        op.waitForOperationCompletion();
        if (op.isResultSuccess()) {
            final Term term = ((LogicTuple) op.getTupleResult()).toTerm();
            return this.unify(arg0, term);
        }
        return false;
    }

    /**
     *
     * @param arg0
     *            the tuple template argument of the operation
     * @return wether the operation has been succesfully completed or not
     */
    public boolean no_1(final Term arg0) {
        TupleCentreOperation op = null;
        final LogicTuple arg = new LogicTuple(arg0);
        if (this.aid != null) {
            try {
            	// Operation Make
                final RespectOperationDefault opRequested = RespectOperationDefault.make(
                        TupleCentreOpType.NO, arg, null);
                // InputEvent Creation
                final InputEvent ev = new InputEvent(this.aid, opRequested,
                        this.target, System.currentTimeMillis(), null);
                op = TupleCentreContainer.doNonBlockingOperation(ev);
            } catch (final TucsonInvalidLogicTupleException e) {
                e.printStackTrace();
                return false;
            } catch (final TucsonOperationNotPossibleException e) {
                e.printStackTrace();
                return false;
            } catch (InvalidLogicTupleException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        } else {
            try {
            	// Operation Make
                final RespectOperationDefault opRequested = RespectOperationDefault.make(
                        TupleCentreOpType.NO, arg, null);
                // InputEvent Creation
                final InputEvent ev = new InputEvent(this.tcid, opRequested,
                        this.target, System.currentTimeMillis(), null);
                op = TupleCentreContainer.doNonBlockingOperation(ev);
            } catch (final TucsonInvalidLogicTupleException e) {
                e.printStackTrace();
                return false;
            } catch (final TucsonOperationNotPossibleException e) {
                e.printStackTrace();
                return false;
            } catch (InvalidLogicTupleException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        op.waitForOperationCompletion();
        if (op.isResultSuccess()) {
            return true;
        }
        final Term term = ((LogicTuple) op.getTupleResult()).toTerm();
        this.unify(arg0, term);
        return false;
    }

    /**
     *
     * @param arg0
     *            the tuple template argument of the operation
     * @param arg1
     *            the tuple result of the operation
     * @return wether the operation has been succesfully completed or not
     */
    public boolean no_all_2(final Term arg0, final Term arg1) {
        TupleCentreOperation op = null;
        final LogicTuple arg = new LogicTuple(arg0);
        if (this.aid != null) {
            try {
            	// Operation Make
                final RespectOperationDefault opRequested = RespectOperationDefault.make(
                        TupleCentreOpType.NO_ALL, arg, null);
                // InputEvent Creation
                final InputEvent ev = new InputEvent(this.aid, opRequested,
                        this.target, System.currentTimeMillis(), null);
                op = TupleCentreContainer.doNonBlockingOperation(ev);
            } catch (final TucsonInvalidLogicTupleException e) {
                e.printStackTrace();
                return false;
            } catch (final TucsonOperationNotPossibleException e) {
                e.printStackTrace();
                return false;
            } catch (InvalidLogicTupleException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        } else {
            try {
            	// Operation Make
                final RespectOperationDefault opRequested = RespectOperationDefault.make(
                        TupleCentreOpType.NO_ALL, arg, null);
                // InputEvent Creation
                final InputEvent ev = new InputEvent(this.tcid, opRequested,
                        this.target, System.currentTimeMillis(), null);
                op = TupleCentreContainer.doNonBlockingOperation(ev);
            } catch (final TucsonInvalidLogicTupleException e) {
                e.printStackTrace();
                return false;
            } catch (final TucsonOperationNotPossibleException e) {
                e.printStackTrace();
                return false;
            } catch (InvalidLogicTupleException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        op.waitForOperationCompletion();
        if (op.isResultSuccess()) {
            return this.unify(arg1,
                    Spawn2PLibrary.list2tuple(op.getTupleListResult()));
        }
        return false;
    }

    /**
     *
     * @param arg0
     *            the tuple template argument of the operation
     * @return wether the operation has been succesfully completed or not
     */
    public boolean nop_1(final Term arg0) {
        TupleCentreOperation op = null;
        final LogicTuple arg = new LogicTuple(arg0);
        if (this.aid != null) {
            try {
            	// Operation Make
                final RespectOperationDefault opRequested = RespectOperationDefault.make(
                        TupleCentreOpType.NOP, arg, null);
                // InputEvent Creation
                final InputEvent ev = new InputEvent(this.aid, opRequested,
                        this.target, System.currentTimeMillis(), null);
                op = TupleCentreContainer.doNonBlockingOperation(ev);
            } catch (final TucsonInvalidLogicTupleException e) {
                e.printStackTrace();
                return false;
            } catch (final TucsonOperationNotPossibleException e) {
                e.printStackTrace();
                return false;
            } catch (InvalidLogicTupleException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        } else {
            try {
            	// Operation Make
                final RespectOperationDefault opRequested = RespectOperationDefault.make(
                        TupleCentreOpType.NOP, arg, null);
                // InputEvent Creation
                final InputEvent ev = new InputEvent(this.tcid, opRequested,
                        this.target, System.currentTimeMillis(), null);
                op = TupleCentreContainer.doNonBlockingOperation(ev);
            } catch (final TucsonInvalidLogicTupleException e) {
                e.printStackTrace();
                return false;
            } catch (final TucsonOperationNotPossibleException e) {
                e.printStackTrace();
                return false;
            } catch (InvalidLogicTupleException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        op.waitForOperationCompletion();
        if (op.isResultSuccess()) {
            return true;
        }
        final Term term = ((LogicTuple) op.getTupleResult()).toTerm();
        this.unify(arg0, term);
        return false;
    }

    /**
     *
     * @param arg0
     *            the tuple argument of the operation
     * @return wether the operation has been succesfully completed or not
     */
    public boolean out_1(final Term arg0) {
        TupleCentreOperation op = null;
        final LogicTuple arg = new LogicTuple(arg0);
        if (this.aid != null) {
            try {
            	// Operation Make
                final RespectOperationDefault opRequested = RespectOperationDefault.make(
                        TupleCentreOpType.OUT, arg, null);
                // InputEvent Creation
                final InputEvent ev = new InputEvent(this.aid, opRequested,
                        this.target, System.currentTimeMillis(), null);
                op = TupleCentreContainer.doNonBlockingOperation(ev);
            } catch (final TucsonInvalidLogicTupleException e) {
                e.printStackTrace();
                return false;
            } catch (final TucsonOperationNotPossibleException e) {
                e.printStackTrace();
                return false;
            } catch (InvalidLogicTupleException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        } else {
            try {
            	final RespectOperationDefault opRequested = RespectOperationDefault.make(
                        TupleCentreOpType.OUT, arg, null);
                // InputEvent Creation
                final InputEvent ev = new InputEvent(this.tcid, opRequested,
                        this.target, System.currentTimeMillis(), null);
                op = TupleCentreContainer.doNonBlockingOperation(ev);
            } catch (final TucsonInvalidLogicTupleException e) {
                e.printStackTrace();
                return false;
            } catch (final TucsonOperationNotPossibleException e) {
                e.printStackTrace();
                return false;
            } catch (InvalidLogicTupleException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        op.waitForOperationCompletion();
        return true;
    }

    /**
     * 
     * @param arg0
     *            the tuple argument of the operation
     * @return wether the operation has been succesfully completed or not
     */
    public boolean out_all_1(final Term arg0) {
        TupleCentreOperation op = null;
        final LogicTuple arg = new LogicTuple(arg0);
        if (this.aid != null) {
            try {
                // Operation Make
                final RespectOperationDefault opRequested = RespectOperationDefault.make(
                        TupleCentreOpType.OUT_ALL, arg, null);
                // InputEvent Creation
                final InputEvent ev = new InputEvent(this.aid, opRequested,
                        this.target, System.currentTimeMillis(), null);
                op = TupleCentreContainer.doNonBlockingOperation(ev);
                // op =
                // TupleCentreContainer.doNonBlockingOperation(
                // TupleCentreOpType.OUT_ALL, this.aid,
                // this.target, arg, null);
            } catch (final TucsonInvalidLogicTupleException e) {
                e.printStackTrace();
                return false;
            } catch (final TucsonOperationNotPossibleException e) {
                e.printStackTrace();
                return false;
            } catch (final InvalidLogicTupleException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            try {
                // Operation Make
                final RespectOperationDefault opRequested = RespectOperationDefault.make(
                        TupleCentreOpType.OUT_ALL, arg, null);
                // InputEvent Creation
                final InputEvent ev = new InputEvent(this.tcid, opRequested,
                        this.target, System.currentTimeMillis(), null);
                op = TupleCentreContainer.doNonBlockingOperation(ev);
                // op =
                // TupleCentreContainer.doNonBlockingOperation(
                // TupleCentreOpType.OUT_ALL, this.tcid,
                // this.target, arg, null);
            } catch (final TucsonInvalidLogicTupleException e) {
                e.printStackTrace();
                return false;
            } catch (final TucsonOperationNotPossibleException e) {
                e.printStackTrace();
                return false;
            } catch (final InvalidLogicTupleException e) {
                e.printStackTrace();
                return false;
            }
        }
        op.waitForOperationCompletion();
        return true;
    }

    /**
     * 
     * @param arg0
     *            the tuple template argument of the operation
     * @return wether the operation has been succesfully completed or not
     */
    public boolean rd_1(final Term arg0) {
        TupleCentreOperation op = null;
        final LogicTuple arg = new LogicTuple(arg0);
        if (this.aid != null) {
            try {
                // Operation Make
                final RespectOperationDefault opRequested = RespectOperationDefault.make(
                        TupleCentreOpType.RD, arg, null);
                // InputEvent Creation
                final InputEvent ev = new InputEvent(this.aid, opRequested,
                        this.target, System.currentTimeMillis(), null);
                op = TupleCentreContainer.doNonBlockingOperation(ev);
                // op =
                // TupleCentreContainer.doNonBlockingOperation(
                // TupleCentreOpType.RD, this.aid,
                // this.target, arg, null);
            } catch (final TucsonInvalidLogicTupleException e) {
                e.printStackTrace();
                return false;
            } catch (final TucsonOperationNotPossibleException e) {
                e.printStackTrace();
                return false;
            } catch (final InvalidLogicTupleException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            try {
                // Operation Make
                final RespectOperationDefault opRequested = RespectOperationDefault.make(
                        TupleCentreOpType.RD, arg, null);
                // InputEvent Creation
                final InputEvent ev = new InputEvent(this.tcid, opRequested,
                        this.target, System.currentTimeMillis(), null);
                op = TupleCentreContainer.doNonBlockingOperation(ev);
                // op =
                // TupleCentreContainer.doNonBlockingOperation(
                // TupleCentreOpType.RD, this.tcid,
                // this.target, arg, null);
            } catch (final TucsonInvalidLogicTupleException e) {
                e.printStackTrace();
                return false;
            } catch (final TucsonOperationNotPossibleException e) {
                e.printStackTrace();
                return false;
            } catch (final InvalidLogicTupleException e) {
                e.printStackTrace();
                return false;
            }
        }
        op.waitForOperationCompletion();
        if (op.isResultSuccess()) {
            final Term term = ((LogicTuple) op.getTupleResult()).toTerm();
            return this.unify(arg0, term);
        }
        return false;
    }

    /**
     * 
     * @param arg0
     *            the tuple template argument of the operation
     * @param arg1
     *            the tuple result of the operation
     * @return wether the operation has been succesfully completed or not
     */
    public boolean rd_all_2(final Term arg0, final Term arg1) {
        TupleCentreOperation op = null;
        final LogicTuple arg = new LogicTuple(arg0);
        if (this.aid != null) {
            try {
                // Operation Make
                final RespectOperationDefault opRequested = RespectOperationDefault.make(
                        TupleCentreOpType.RD_ALL, arg, null);
                // InputEvent Creation
                final InputEvent ev = new InputEvent(this.aid, opRequested,
                        this.target, System.currentTimeMillis(), null);
                op = TupleCentreContainer.doNonBlockingOperation(ev);
                // op =
                // TupleCentreContainer.doNonBlockingOperation(
                // TupleCentreOpType.RD_ALL, this.aid,
                // this.target, arg, null);
            } catch (final TucsonInvalidLogicTupleException e) {
                e.printStackTrace();
                return false;
            } catch (final TucsonOperationNotPossibleException e) {
                e.printStackTrace();
                return false;
            } catch (final InvalidLogicTupleException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            try {
                // Operation Make
                final RespectOperationDefault opRequested = RespectOperationDefault.make(
                        TupleCentreOpType.RD_ALL, arg, null);
                // InputEvent Creation
                final InputEvent ev = new InputEvent(this.tcid, opRequested,
                        this.target, System.currentTimeMillis(), null);
                op = TupleCentreContainer.doNonBlockingOperation(ev);
                // op =
                // TupleCentreContainer.doNonBlockingOperation(
                // TupleCentreOpType.RD_ALL, this.tcid,
                // this.target, arg, null);
            } catch (final TucsonInvalidLogicTupleException e) {
                e.printStackTrace();
                return false;
            } catch (final TucsonOperationNotPossibleException e) {
                e.printStackTrace();
                return false;
            } catch (final InvalidLogicTupleException e) {
                e.printStackTrace();
                return false;
            }
        }
        op.waitForOperationCompletion();
        if (op.isResultSuccess()) {
            return this.unify(arg1,
                    Spawn2PLibrary.list2tuple(op.getTupleListResult()));
        }
        return false;
    }

    /**
     * 
     * @param arg0
     *            the tuple template argument of the operation
     * @return wether the operation has been succesfully completed or not
     */
    public boolean rdp_1(final Term arg0) {
        TupleCentreOperation op = null;
        final LogicTuple arg = new LogicTuple(arg0);
        if (this.aid != null) {
            try {
                // Operation Make
                final RespectOperationDefault opRequested = RespectOperationDefault.make(
                        TupleCentreOpType.RDP, arg, null);
                // InputEvent Creation
                final InputEvent ev = new InputEvent(this.aid, opRequested,
                        this.target, System.currentTimeMillis(), null);
                op = TupleCentreContainer.doNonBlockingOperation(ev);
                // op =
                // TupleCentreContainer.doNonBlockingOperation(
                // TupleCentreOpType.RDP, this.aid,
                // this.target, arg, null);
            } catch (final TucsonInvalidLogicTupleException e) {
                e.printStackTrace();
                return false;
            } catch (final TucsonOperationNotPossibleException e) {
                e.printStackTrace();
                return false;
            } catch (final InvalidLogicTupleException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            try {
                // Operation Make
                final RespectOperationDefault opRequested = RespectOperationDefault.make(
                        TupleCentreOpType.RDP, arg, null);
                // InputEvent Creation
                final InputEvent ev = new InputEvent(this.tcid, opRequested,
                        this.target, System.currentTimeMillis(), null);
                op = TupleCentreContainer.doNonBlockingOperation(ev);
                // op =
                // TupleCentreContainer.doNonBlockingOperation(
                // TupleCentreOpType.RDP, this.tcid,
                // this.target, arg, null);
            } catch (final TucsonInvalidLogicTupleException e) {
                e.printStackTrace();
                return false;
            } catch (final TucsonOperationNotPossibleException e) {
                e.printStackTrace();
                return false;
            } catch (final InvalidLogicTupleException e) {
                e.printStackTrace();
                return false;
            }
        }
        op.waitForOperationCompletion();
        if (op.isResultSuccess()) {
            final Term term = ((LogicTuple) op.getTupleResult()).toTerm();
            return this.unify(arg0, term);
        }
        return false;
    }

    /**
     *
     * @param id
     *            the identifier of the agent whose behalf this spawn behaves on
     */
    public final void setSpawnerId(final TucsonAgentId id) {
        this.aid = id;
        this.tcid = null;
    }

    /**
     *
     * @param id
     *            the identifier of the tuple centre whose behalf this spawn
     *            behaves on
     */
    public final void setSpawnerId(final TucsonTupleCentreId id) {
        this.aid = null;
        this.tcid = id;
    }

    /**
     *
     * @param id
     *            the identifier of the tuple centre this spawn is operating on
     */
    public final void setTargetTC(final TucsonTupleCentreId id) {
        this.target = id;
    }

    /**
     * 
     * @param arg0
     *            the tuple template argument of the operation
     * @return wether the operation has been succesfully completed or not
     */
    public boolean uin_1(final Term arg0) {
        TupleCentreOperation op = null;
        final LogicTuple arg = new LogicTuple(arg0);
        if (this.aid != null) {
            try {
                // Operation Make
                final RespectOperationDefault opRequested = RespectOperationDefault.make(
                        TupleCentreOpType.UIN, arg, null);
                // InputEvent Creation
                final InputEvent ev = new InputEvent(this.aid, opRequested,
                        this.target, System.currentTimeMillis(), null);
                op = TupleCentreContainer.doNonBlockingOperation(ev);
                // op =
                // TupleCentreContainer.doNonBlockingOperation(
                // TupleCentreOpType.UIN, this.aid,
                // this.target, arg, null);
            } catch (final TucsonInvalidLogicTupleException e) {
                e.printStackTrace();
                return false;
            } catch (final TucsonOperationNotPossibleException e) {
                e.printStackTrace();
                return false;
            } catch (final InvalidLogicTupleException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            try {
                // Operation Make
                final RespectOperationDefault opRequested = RespectOperationDefault.make(
                        TupleCentreOpType.UIN, arg, null);
                // InputEvent Creation
                final InputEvent ev = new InputEvent(this.tcid, opRequested,
                        this.target, System.currentTimeMillis(), null);
                op = TupleCentreContainer.doNonBlockingOperation(ev);
                // op =
                // TupleCentreContainer.doNonBlockingOperation(
                // TupleCentreOpType.UIN, this.tcid,
                // this.target, arg, null);
            } catch (final TucsonInvalidLogicTupleException e) {
                e.printStackTrace();
                return false;
            } catch (final TucsonOperationNotPossibleException e) {
                e.printStackTrace();
                return false;
            } catch (final InvalidLogicTupleException e) {
                e.printStackTrace();
                return false;
            }
        }
        op.waitForOperationCompletion();
        if (op.isResultSuccess()) {
            final Term term = ((LogicTuple) op.getTupleResult()).toTerm();
            return this.unify(arg0, term);
        }
        return false;
    }

    /**
     * 
     * @param arg0
     *            the tuple template argument of the operation
     * @return wether the operation has been succesfully completed or not
     */
    public boolean uinp_1(final Term arg0) {
        TupleCentreOperation op = null;
        final LogicTuple arg = new LogicTuple(arg0);
        if (this.aid != null) {
            try {
                // Operation Make
                final RespectOperationDefault opRequested = RespectOperationDefault.make(
                        TupleCentreOpType.UINP, arg, null);
                // InputEvent Creation
                final InputEvent ev = new InputEvent(this.aid, opRequested,
                        this.target, System.currentTimeMillis(), null);
                op = TupleCentreContainer.doNonBlockingOperation(ev);
                // op =
                // TupleCentreContainer.doNonBlockingOperation(
                // TupleCentreOpType.UINP, this.aid,
                // this.target, arg, null);
            } catch (final TucsonInvalidLogicTupleException e) {
                e.printStackTrace();
                return false;
            } catch (final TucsonOperationNotPossibleException e) {
                e.printStackTrace();
                return false;
            } catch (final InvalidLogicTupleException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            try {
                // Operation Make
                final RespectOperationDefault opRequested = RespectOperationDefault.make(
                        TupleCentreOpType.UINP, arg, null);
                // InputEvent Creation
                final InputEvent ev = new InputEvent(this.tcid, opRequested,
                        this.target, System.currentTimeMillis(), null);
                op = TupleCentreContainer.doNonBlockingOperation(ev);
                // op =
                // TupleCentreContainer.doNonBlockingOperation(
                // TupleCentreOpType.UINP, this.tcid,
                // this.target, arg, null);
            } catch (final TucsonInvalidLogicTupleException e) {
                e.printStackTrace();
                return false;
            } catch (final TucsonOperationNotPossibleException e) {
                e.printStackTrace();
                return false;
            } catch (final InvalidLogicTupleException e) {
                e.printStackTrace();
                return false;
            }
        }
        op.waitForOperationCompletion();
        if (op.isResultSuccess()) {
            final Term term = ((LogicTuple) op.getTupleResult()).toTerm();
            return this.unify(arg0, term);
        }
        return false;
    }

    /**
     * 
     * @param arg0
     *            the tuple template argument of the operation
     * @return wether the operation has been succesfully completed or not
     */
    public boolean uno_1(final Term arg0) {
        TupleCentreOperation op = null;
        final LogicTuple arg = new LogicTuple(arg0);
        if (this.aid != null) {
            try {
                // Operation Make
                final RespectOperationDefault opRequested = RespectOperationDefault.make(
                        TupleCentreOpType.UNO, arg, null);
                // InputEvent Creation
                final InputEvent ev = new InputEvent(this.aid, opRequested,
                        this.target, System.currentTimeMillis(), null);
                op = TupleCentreContainer.doNonBlockingOperation(ev);
                // op =
                // TupleCentreContainer.doNonBlockingOperation(
                // TupleCentreOpType.UNO, this.aid,
                // this.target, arg, null);
            } catch (final TucsonInvalidLogicTupleException e) {
                e.printStackTrace();
                return false;
            } catch (final TucsonOperationNotPossibleException e) {
                e.printStackTrace();
                return false;
            } catch (final InvalidLogicTupleException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            try {
                // Operation Make
                final RespectOperationDefault opRequested = RespectOperationDefault.make(
                        TupleCentreOpType.UNO, arg, null);
                // InputEvent Creation
                final InputEvent ev = new InputEvent(this.tcid, opRequested,
                        this.target, System.currentTimeMillis(), null);
                op = TupleCentreContainer.doNonBlockingOperation(ev);
                // op =
                // TupleCentreContainer.doNonBlockingOperation(
                // TupleCentreOpType.UNO, this.tcid,
                // this.target, arg, null);
            } catch (final TucsonInvalidLogicTupleException e) {
                e.printStackTrace();
                return false;
            } catch (final TucsonOperationNotPossibleException e) {
                e.printStackTrace();
                return false;
            } catch (final InvalidLogicTupleException e) {
                e.printStackTrace();
                return false;
            }
        }
        op.waitForOperationCompletion();
        if (op.isResultSuccess()) {
            return true;
        }
        final Term term = ((LogicTuple) op.getTupleResult()).toTerm();
        this.unify(arg0, term);
        return false;
    }

    /**
     * 
     * @param arg0
     *            the tuple template argument of the operation
     * @return wether the operation has been succesfully completed or not
     */
    public boolean unop_1(final Term arg0) {
        TupleCentreOperation op = null;
        final LogicTuple arg = new LogicTuple(arg0);
        if (this.aid != null) {
            try {
                // Operation Make
                final RespectOperationDefault opRequested = RespectOperationDefault.make(
                        TupleCentreOpType.UNOP, arg, null);
                // InputEvent Creation
                final InputEvent ev = new InputEvent(this.aid, opRequested,
                        this.target, System.currentTimeMillis(), null);
                op = TupleCentreContainer.doNonBlockingOperation(ev);
                // op =
                // TupleCentreContainer.doNonBlockingOperation(
                // TupleCentreOpType.UNOP, this.aid,
                // this.target, arg, null);
            } catch (final TucsonInvalidLogicTupleException e) {
                e.printStackTrace();
                return false;
            } catch (final TucsonOperationNotPossibleException e) {
                e.printStackTrace();
                return false;
            } catch (final InvalidLogicTupleException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            try {
                // Operation Make
                final RespectOperationDefault opRequested = RespectOperationDefault.make(
                        TupleCentreOpType.UNOP, arg, null);
                // InputEvent Creation
                final InputEvent ev = new InputEvent(this.tcid, opRequested,
                        this.target, System.currentTimeMillis(), null);
                op = TupleCentreContainer.doNonBlockingOperation(ev);
                // op =
                // TupleCentreContainer.doNonBlockingOperation(
                // TupleCentreOpType.UNOP, this.tcid,
                // this.target, arg, null);
            } catch (final TucsonInvalidLogicTupleException e) {
                e.printStackTrace();
                return false;
            } catch (final TucsonOperationNotPossibleException e) {
                e.printStackTrace();
                return false;
            } catch (final InvalidLogicTupleException e) {
                e.printStackTrace();
                return false;
            }
        }
        op.waitForOperationCompletion();
        if (op.isResultSuccess()) {
            return true;
        }
        final Term term = ((LogicTuple) op.getTupleResult()).toTerm();
        this.unify(arg0, term);
        return false;
    }

    /**
     * 
     * @param arg0
     *            the tuple template argument of the operation
     * @return wether the operation has been succesfully completed or not
     */
    public boolean urd_1(final Term arg0) {
        TupleCentreOperation op = null;
        final LogicTuple arg = new LogicTuple(arg0);
        if (this.aid != null) {
            try {
                // Operation Make
                final RespectOperationDefault opRequested = RespectOperationDefault.make(
                        TupleCentreOpType.URD, arg, null);
                // InputEvent Creation
                final InputEvent ev = new InputEvent(this.aid, opRequested,
                        this.target, System.currentTimeMillis(), null);
                op = TupleCentreContainer.doNonBlockingOperation(ev);
                // op =
                // TupleCentreContainer.doNonBlockingOperation(
                // TupleCentreOpType.URD, this.aid,
                // this.target, arg, null);
            } catch (final TucsonInvalidLogicTupleException e) {
                e.printStackTrace();
                return false;
            } catch (final TucsonOperationNotPossibleException e) {
                e.printStackTrace();
                return false;
            } catch (final InvalidLogicTupleException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            try {
                // Operation Make
                final RespectOperationDefault opRequested = RespectOperationDefault.make(
                        TupleCentreOpType.URD, arg, null);
                // InputEvent Creation
                final InputEvent ev = new InputEvent(this.tcid, opRequested,
                        this.target, System.currentTimeMillis(), null);
                op = TupleCentreContainer.doNonBlockingOperation(ev);
                // op =
                // TupleCentreContainer.doNonBlockingOperation(
                // TupleCentreOpType.URD, this.tcid,
                // this.target, arg, null);
            } catch (final TucsonInvalidLogicTupleException e) {
                e.printStackTrace();
                return false;
            } catch (final TucsonOperationNotPossibleException e) {
                e.printStackTrace();
                return false;
            } catch (final InvalidLogicTupleException e) {
                e.printStackTrace();
                return false;
            }
        }
        op.waitForOperationCompletion();
        if (op.isResultSuccess()) {
            final Term term = ((LogicTuple) op.getTupleResult()).toTerm();
            return this.unify(arg0, term);
        }
        return false;
    }

    /**
     * 
     * @param arg0
     *            the tuple template argument of the operation
     * @return wether the operation has been succesfully completed or not
     */
    public boolean urdp_1(final Term arg0) {
        TupleCentreOperation op = null;
        final LogicTuple arg = new LogicTuple(arg0);
        if (this.aid != null) {
            try {
                // Operation Make
                final RespectOperationDefault opRequested = RespectOperationDefault.make(
                        TupleCentreOpType.URDP, arg, null);
                // InputEvent Creation
                final InputEvent ev = new InputEvent(this.aid, opRequested,
                        this.target, System.currentTimeMillis(), null);
                op = TupleCentreContainer.doNonBlockingOperation(ev);
                // op =
                // TupleCentreContainer.doNonBlockingOperation(
                // TupleCentreOpType.URDP, this.aid,
                // this.target, arg, null);
            } catch (final TucsonInvalidLogicTupleException e) {
                e.printStackTrace();
                return false;
            } catch (final TucsonOperationNotPossibleException e) {
                e.printStackTrace();
                return false;
            } catch (final InvalidLogicTupleException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            try {
                // Operation Make
                final RespectOperationDefault opRequested = RespectOperationDefault.make(
                        TupleCentreOpType.URDP, arg, null);
                // InputEvent Creation
                final InputEvent ev = new InputEvent(this.tcid, opRequested,
                        this.target, System.currentTimeMillis(), null);
                op = TupleCentreContainer.doNonBlockingOperation(ev);
                // op =
                // TupleCentreContainer.doNonBlockingOperation(
                // TupleCentreOpType.URDP, this.tcid,
                // this.target, arg, null);
            } catch (final TucsonInvalidLogicTupleException e) {
                e.printStackTrace();
                return false;
            } catch (final TucsonOperationNotPossibleException e) {
                e.printStackTrace();
                return false;
            } catch (final InvalidLogicTupleException e) {
                e.printStackTrace();
                return false;
            }
        }
        op.waitForOperationCompletion();
        if (op.isResultSuccess()) {
            final Term term = ((LogicTuple) op.getTupleResult()).toTerm();
            return this.unify(arg0, term);
        }
        return false;
    }
}
