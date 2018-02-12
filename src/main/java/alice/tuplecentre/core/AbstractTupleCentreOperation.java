/*
 * Tuple Centre media - Copyright (C) 2001-2002 aliCE team at deis.unibo.it This
 * library is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version. This library is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser
 * General Public License for more details. You should have received a copy of
 * the GNU Lesser General Public License along with this library; if not, write
 * to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
 * 02111-1307 USA
 */
package alice.tuplecentre.core;

import java.util.List;
import alice.logictuple.LogicTuple;
import alice.logictuple.exceptions.InvalidLogicTupleException;
import alice.tuplecentre.api.ITupleCentreOperation;
import alice.tuplecentre.api.Tuple;
import alice.tuplecentre.api.TupleTemplate;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;
import alice.tuplecentre.core.TCCycleResult.Outcome;

/**
 * This class represents an Operation on a tuple centre.
 *
 * @author Alessandro Ricci
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 *
 */
public abstract class AbstractTupleCentreOperation implements
        ITupleCentreOperation {

    /**  */
    public static final int OPTYPE_IS_STEP_MODE = 57;
    /**
     * shared id counter
     */
    private static long idCounter = 0;
    /**  */
    protected static final int OPTYPE_ABORT_OP = 58;
    /**  */
    protected static final int OPTYPE_ADD_INSP = 70;
    /**  */
    protected static final int OPTYPE_ADD_OBS = 67;
    /**  */
    protected static final int OPTYPE_GET = 8;
    /**  */
    protected static final int OPTYPE_GET_INSPS = 29;
    /**  */
    protected static final int OPTYPE_GET_S = 28;
    /**  */
    protected static final int OPTYPE_GET_TRSET = 66;
    /**  */
    protected static final int OPTYPE_GET_TSET = 64;
    /**  */
    protected static final int OPTYPE_GET_WSET = 65;
    /**  */
    protected static final int OPTYPE_GO_CMD = 61;
    /**  */
    protected static final int OPTYPE_HAS_INSP = 72;
    /**  */
    protected static final int OPTYPE_HAS_OBS = 69;
    /**  */
    protected static final int OPTYPE_IN = 2;
    /**  */
    protected static final int OPTYPE_IN_ALL = 11;
    /**  */
    protected static final int OPTYPE_IN_S = 21;
    /**  */
    protected static final int OPTYPE_INP = 4;
    /**  */
    protected static final int OPTYPE_INP_S = 23;
    /**  */
    protected static final int OPTYPE_NEXT_STEP = 62;
    /**  */
    protected static final int OPTYPE_NO = 6;
    /**  */
    protected static final int OPTYPE_NO_ALL = 13;
    /**  */
    protected static final int OPTYPE_NO_S = 25;
    /**  */
    protected static final int OPTYPE_NOP = 7;
    /**  */
    protected static final int OPTYPE_NOP_S = 26;
    /**  */
    protected static final int OPTYPE_OUT = 1;
    /**  */
    protected static final int OPTYPE_OUT_ALL = 10;
    /**  */
    protected static final int OPTYPE_OUT_S = 20;
    /**  */
    protected static final int OPTYPE_RD = 3;
    /**  */
    protected static final int OPTYPE_RD_ALL = 12;
    /**  */
    protected static final int OPTYPE_RD_S = 22;
    /**  */
    protected static final int OPTYPE_RDP = 5;
    /**  */
    protected static final int OPTYPE_RDP_S = 24;
    /**  */
    protected static final int OPTYPE_RMV_INSP = 71;
    /**  */
    protected static final int OPTYPE_RMV_OBS = 68;
    /**  */
    protected static final int OPTYPE_SET = 9;
    /**  */
    // TODO must be delete.. protected static final int OPTYPE_SET_MNG_MODE =
    // 59;
    /**  */
    protected static final int OPTYPE_SET_S = 27;
    /**  */
    protected static final int OPTYPE_SET_SPY = 63;
    /**  */
    protected static final int OPTYPE_SET_WSET = 73;
    /**  */
    protected static final int OPTYPE_SPAWN = 666;
    /**  */
    protected static final int OPTYPE_STEP_MODE = 59;
    /**  */
    protected static final int OPTYPE_STOP_CMD = 60;
    /**  */
    protected static final int OPTYPE_UIN = 15;
    /**  */
    protected static final int OPTYPE_UINP = 18;
    /**  */
    protected static final int OPTYPE_UNO = 16;
    /**  */
    protected static final int OPTYPE_UNOP = 19;
    /**  */
    protected static final int OPTYPE_URD = 14;
    /**  */
    protected static final int OPTYPE_URDP = 17;
    /**  */
    protected static final int RESET = 74;
    /**
     * internal identifier of the operation
     */
    private final long id;
    private final TCCycleResult result;
    private TupleTemplate templateArgument;
    private Tuple tupleArgument;
    private List<Tuple> tupleListArgument;
    private final int type;
    /**
     *
     */
    protected OperationCompletionListener listener;
    /**
     *
     */
    protected boolean operationCompleted;
    /**
     * used for possible synchronisation
     */
    protected Object token;

    private AbstractTupleCentreOperation(final int ty) {
        this.operationCompleted = false;
        this.result = new TCCycleResult();
        this.type = ty;
        this.token = new Object();
        this.id = AbstractTupleCentreOperation.idCounter;
        AbstractTupleCentreOperation.idCounter++;
    }

    /**
     *
     * @param t
     *            the type code of the operation
     * @param tupleList
     *            the list of tuples argument of the operation
     */
    protected AbstractTupleCentreOperation(final int t,
            final List<Tuple> tupleList) {
        this(t);
        this.listener = null;
        this.tupleListArgument = tupleList;
    }

    /**
     *
     * @param t
     *            the type code of the operation
     * @param tupleList
     *            the list of tuples argument of the operation
     * @param l
     *            the listener for operation completion
     */
    protected AbstractTupleCentreOperation(final int t,
            final List<Tuple> tupleList, final OperationCompletionListener l) {
        this(t, tupleList);
        this.listener = l;
    }

    /**
     *
     * @param ty
     *            the type code of the operation
     * @param t
     *            the tuple argument of the operation
     */
    protected AbstractTupleCentreOperation(final int ty, final Tuple t) {
        this(ty);
        this.listener = null;
        this.tupleArgument = t;
    }

    /**
     *
     * @param ty
     *            the type code of the operation
     * @param t
     *            the tuple argument of the operation
     * @param l
     *            the listener for operation completion
     */
    protected AbstractTupleCentreOperation(final int ty, final Tuple t,
            final OperationCompletionListener l) {
        this(ty, t);
        this.listener = l;
    }

    /**
     *
     * @param ty
     *            the type code of the operation
     * @param t
     *            the tuple template argument of the operation
     */
    protected AbstractTupleCentreOperation(final int ty, final TupleTemplate t) {
        this(ty);
        this.listener = null;
        this.templateArgument = t;
    }

    /**
     *
     * @param ty
     *            the type code of the operation
     * @param t
     *            the tuple template argument of the operation
     * @param l
     *            the listener for operation completion
     */
    protected AbstractTupleCentreOperation(final int ty, final TupleTemplate t,
            final OperationCompletionListener l) {
        this(ty, t);
        this.listener = l;
    }

    /**
     *
     * @param lis
     *            the listener for operation completion to add
     */
    public void addListener(final OperationCompletionListener lis) {
        this.listener = lis;
    }

    /**
     * Get operation identifier
     *
     * @return Operation identifier
     */
    @Override
    public long getId() {
        return this.id;
    }

    /**
     *
     * @return the tuple representing the whole invocation predicate (primitive
     *         + tuple argument)
     */
    public Tuple getPredicate() {
        final StringBuffer pred = new StringBuffer();
        try {
            if (this.isOut() || this.isOutS() || this.isOutAll()
                    || this.isSpawn() || this.isSet() || this.isSetS()) {
                pred.append(this.getPrimitive().toString()).append('(')
                        .append(this.tupleArgument).append(')');
                return LogicTuple.parse(pred.toString());
            }
            pred.append(this.getPrimitive().toString()).append('(')
                    .append(this.templateArgument).append(')');
            return LogicTuple.parse(pred.toString());
        } catch (final InvalidLogicTupleException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     *
     * @return the tuple representing the primitive invoked
     */
    public Tuple getPrimitive() {
        switch (this.type) {
            case OPTYPE_OUT:
                return new LogicTuple("out");
            case OPTYPE_IN:
                return new LogicTuple("in");
            case OPTYPE_RD:
                return new LogicTuple("rd");
            case OPTYPE_INP:
                return new LogicTuple("inp");
            case OPTYPE_RDP:
                return new LogicTuple("rdp");
            case OPTYPE_NO:
                return new LogicTuple("no");
            case OPTYPE_NOP:
                return new LogicTuple("nop");
            case OPTYPE_GET:
                return new LogicTuple("get");
            case OPTYPE_SET:
                return new LogicTuple("set");
            case OPTYPE_SPAWN:
                return new LogicTuple("spawn");
            case OPTYPE_OUT_ALL:
                return new LogicTuple("out_all");
            case OPTYPE_IN_ALL:
                return new LogicTuple("in_all");
            case OPTYPE_RD_ALL:
                return new LogicTuple("rd_all");
            case OPTYPE_NO_ALL:
                return new LogicTuple("no_all");
            case OPTYPE_URD:
                return new LogicTuple("urd");
            case OPTYPE_UIN:
                return new LogicTuple("uin");
            case OPTYPE_UNO:
                return new LogicTuple("uno");
            case OPTYPE_URDP:
                return new LogicTuple("urdp");
            case OPTYPE_UINP:
                return new LogicTuple("uinp");
            case OPTYPE_UNOP:
                return new LogicTuple("unop");
            case OPTYPE_OUT_S:
                return new LogicTuple("out_s");
            case OPTYPE_IN_S:
                return new LogicTuple("in_s");
            case OPTYPE_RD_S:
                return new LogicTuple("rd_s");
            case OPTYPE_INP_S:
                return new LogicTuple("inp_s");
            case OPTYPE_RDP_S:
                return new LogicTuple("rdp_s");
            case OPTYPE_NO_S:
                return new LogicTuple("no_s");
            case OPTYPE_NOP_S:
                return new LogicTuple("nop_s");
            case OPTYPE_SET_S:
                return new LogicTuple("set_s");
            case OPTYPE_GET_S:
                return new LogicTuple("get_s");
            default:
                return null;
        }
    }

    @Override
    public TupleTemplate getTemplateArgument() {
        return this.templateArgument;
    }

    @Override
    public Tuple getTupleArgument() {
        return this.tupleArgument;
    }

    @Override
    public List<Tuple> getTupleListArgument() {
        return this.tupleListArgument;
    }

    @Override
    public List<Tuple> getTupleListResult() {
        return this.result.getTupleListResult();
    }

    @Override
    public Tuple getTupleResult() {
        return this.result.getTupleResult();
    }

    /**
     *
     * @return the type code of the operation
     */
    public int getType() {
        return this.type;
    }

    @Override
    public boolean isGet() {
        return this.type == AbstractTupleCentreOperation.OPTYPE_GET;
    }

    @Override
    public boolean isGetS() {
        return this.type == AbstractTupleCentreOperation.OPTYPE_GET_S;
    }

    @Override
    public boolean isIn() {
        return this.type == AbstractTupleCentreOperation.OPTYPE_IN;
    }

    @Override
    public boolean isInAll() {
        return this.type == AbstractTupleCentreOperation.OPTYPE_IN_ALL;
    }

    @Override
    public boolean isInp() {
        return this.type == AbstractTupleCentreOperation.OPTYPE_INP;
    }

    /**
     *
     * @return wether this operation is a <code>inp_s</code>
     */
    public boolean isInpS() {
        return this.type == AbstractTupleCentreOperation.OPTYPE_INP_S;
    }

    /**
     *
     * @return wether this operation is a <code>in_s</code>
     */
    public boolean isInS() {
        return this.type == AbstractTupleCentreOperation.OPTYPE_IN_S;
    }

    @Override
    public boolean isNo() {
        return this.type == AbstractTupleCentreOperation.OPTYPE_NO;
    }

    /**
     *
     * @return wether this operation is a <code>no_all</code>
     */
    @Override
    public boolean isNoAll() {
        return this.type == AbstractTupleCentreOperation.OPTYPE_NO_ALL;
    }

    @Override
    public boolean isNop() {
        return this.type == AbstractTupleCentreOperation.OPTYPE_NOP;
    }

    /**
     *
     * @return wether this operation is a <code>nop_s</code>
     */
    public boolean isNopS() {
        return this.type == AbstractTupleCentreOperation.OPTYPE_NOP_S;
    }

    /**
     *
     * @return wether this operation is a <code>no_s</code>
     */
    public boolean isNoS() {
        return this.type == AbstractTupleCentreOperation.OPTYPE_NO_S;
    }

    @Override
    public boolean isOperationCompleted() {
        return this.operationCompleted;
    }

    @Override
    public boolean isOut() {
        return this.type == AbstractTupleCentreOperation.OPTYPE_OUT;
    }

    @Override
    public boolean isOutAll() {
        return this.type == AbstractTupleCentreOperation.OPTYPE_OUT_ALL;
    }

    /**
     *
     * @return wether this operation is a <code>out_s</code>
     */
    public boolean isOutS() {
        return this.type == AbstractTupleCentreOperation.OPTYPE_OUT_S;
    }

    @Override
    public boolean isRd() {
        return this.type == AbstractTupleCentreOperation.OPTYPE_RD;
    }

    @Override
    public boolean isRdAll() {
        return this.type == AbstractTupleCentreOperation.OPTYPE_RD_ALL;
    }

    @Override
    public boolean isRdp() {
        return this.type == AbstractTupleCentreOperation.OPTYPE_RDP;
    }

    /**
     *
     * @return wether this operation is a <code>rdp_s</code>
     */
    public boolean isRdpS() {
        return this.type == AbstractTupleCentreOperation.OPTYPE_RDP_S;
    }

    /**
     *
     * @return wether this operation is a <code>rd_s</code>
     */
    public boolean isRdS() {
        return this.type == AbstractTupleCentreOperation.OPTYPE_RD_S;
    }

    /**
     * Tests if the result is defined
     *
     * @return true if the result is defined
     */
    public boolean isResultDefined() {
        return this.result.isResultDefined();
    }

    @Override
    public boolean isResultFailure() {
        return this.result.isResultFailure();
    }

    @Override
    public boolean isResultSuccess() {
        return this.result.isResultSuccess();
    }

    @Override
    public boolean isSet() {
        return this.type == AbstractTupleCentreOperation.OPTYPE_SET;
    }

    @Override
    public boolean isSetS() {
        return this.type == AbstractTupleCentreOperation.OPTYPE_SET_S;
    }

    /**
     *
     * @return wether this operation is a <code>spawn</code>
     */
    public boolean isSpawn() {
        return this.type == AbstractTupleCentreOperation.OPTYPE_SPAWN;
    }

    @Override
    public boolean isUin() {
        return this.type == AbstractTupleCentreOperation.OPTYPE_UIN;
    }

    @Override
    public boolean isUinp() {
        return this.type == AbstractTupleCentreOperation.OPTYPE_UINP;
    }

    public boolean isUno() {
        return this.type == AbstractTupleCentreOperation.OPTYPE_UNO;
    }

    public boolean isUnop() {
        return this.type == AbstractTupleCentreOperation.OPTYPE_UNOP;
    }

    @Override
    public boolean isUrd() {
        return this.type == AbstractTupleCentreOperation.OPTYPE_URD;
    }

    @Override
    public boolean isUrdp() {
        return this.type == AbstractTupleCentreOperation.OPTYPE_URDP;
    }

    /**
     * Changes the state of the operation to complete.
     *
     */
    public void notifyCompletion() {
        if (this.listener != null) {
            this.operationCompleted = true;
            this.listener.operationCompleted(this);
        } else {
            synchronized (this.token) {
                this.operationCompleted = true;
                this.token.notifyAll();
            }
        }
    }

    /**
     *
     */
    public void removeListener() {
        this.listener = null;
    }

    /**
     *
     * @param l
     *            the listener for operation completion
     */
    public void setListener(final OperationCompletionListener l) {
        this.listener = l;
    }

    /**
     *
     * @param o
     *            the outcome of the operation
     */
    public void setOpResult(final Outcome o) {
        this.result.setOpResult(o);
    }

    /**
     *
     * @param t
     *            the list of tuples result of the operation
     */
    public void setTupleListResult(final List<Tuple> t) {
        this.result.setTupleListResult(t);
        this.result.setEndTime(System.currentTimeMillis());
    }

    /**
     *
     * @param t
     *            the tuple result of the operation
     */
    public void setTupleResult(final Tuple t) {
        this.result.setTupleResult(t);
        if (this.templateArgument != null) {
            this.templateArgument.propagate(t);
        }
        this.tupleArgument = t;
        this.result.setEndTime(System.currentTimeMillis());
    }

    @Override
    public void waitForOperationCompletion() {
        try {
            synchronized (this.token) {
                while (!this.operationCompleted) {
                    this.token.wait();
                }
            }
        } catch (final InterruptedException e) {
            // do nothing here, ususally happens when shutting down nodes
        }
    }

    @Override
    public void waitForOperationCompletion(final long ms)
            throws OperationTimeOutException {
        synchronized (this.token) {
            if (!this.operationCompleted) {
                try {
                    this.token.wait(ms);
                } catch (final InterruptedException e) {
                    // do nothing here, usually happens when shutting down
                    // nodes
                }
            }
            if (!this.operationCompleted) {
                throw new OperationTimeOutException(ms);
            }
        }
    }
}
