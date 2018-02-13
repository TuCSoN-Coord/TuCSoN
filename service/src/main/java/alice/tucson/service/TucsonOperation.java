package alice.tucson.service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import alice.logictuple.LogicTuple;
import alice.respect.core.RespectOperation;
import alice.tucson.api.ITucsonOperation;
import alice.tuplecentre.api.Tuple;
import alice.tuplecentre.api.TupleTemplate;
import alice.tuplecentre.api.exceptions.InvalidTupleException;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;
import alice.tuplecentre.core.AbstractTupleCentreOperation;
import alice.tuplecentre.core.OperationCompletionListener;
import alice.tuples.javatuples.impl.JTuplesEngine;

/**
 *
 * @author Alessandro Ricci
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 *
 */
public class TucsonOperation extends AbstractTupleCentreOperation implements
        ITucsonOperation {

    private static final int OPTYPE_EXIT = 310;

    /**
     *
     * @return the Integer representing the type code of
     *         <code>abort_operation</code> operation
     */
    public static int abortOpCode() {
        return AbstractTupleCentreOperation.OPTYPE_ABORT_OP;
    }

    /**
     *
     * @return the Integer representing the type code of
     *         <code>add_inspector</code> operation
     */
    public static int addInspCode() {
        return AbstractTupleCentreOperation.OPTYPE_ADD_INSP;
    }

    /**
     *
     * @return the Integer representing the type code of
     *         <code>add_observer</code> operation
     */
    public static int addObsCode() {
        return AbstractTupleCentreOperation.OPTYPE_ADD_OBS;
    }

    /**
     *
     * @return the type code for an environmental operation
     */
    public static int envCode() {
        return RespectOperation.OPTYPE_ENV;
    }

    /**
     *
     * @return the Integer representing the type code of <code>exit</code>
     *         operation
     */
    public static int exitCode() {
        return TucsonOperation.OPTYPE_EXIT;
    }

    /**
     *
     * @return the Integer representing the type code of <code>get</code>
     *         operation
     */
    public static int getCode() {
        return AbstractTupleCentreOperation.OPTYPE_GET;
    }

    /**
     *
     * @return the type code of the <code>get_env</code> environmental getter
     *         operation
     */
    public static int getEnvCode() {
        return RespectOperation.OPTYPE_GET_ENV;
    }

    public static int getInspectorsCode() {
        return AbstractTupleCentreOperation.OPTYPE_GET_INSPS;
    }

    /**
     *
     * @return the Integer representing the type code of <code>get_s</code>
     *         operation
     */
    public static int getSCode() {
        return AbstractTupleCentreOperation.OPTYPE_GET_S;
    }

    /**
     *
     * @return the Integer representing the type code of the operation to
     *         retrieve the triggered reactions set
     */
    public static int getTRSetCode() {
        return AbstractTupleCentreOperation.OPTYPE_GET_TRSET;
    }

    /**
     *
     * @return the Integer representing the type code of the operation to
     *         retrieve the tuples set
     */
    public static int getTSetCode() {
        return AbstractTupleCentreOperation.OPTYPE_GET_TSET;
    }

    /**
     *
     * @return the Integer representing the type code of the operation to
     *         retrieve the input events set
     */
    public static int getWSetCode() {
        return AbstractTupleCentreOperation.OPTYPE_GET_WSET;
    }

    /**
     *
     * @return the Integer representing the type code of <code>go_cmd</code>
     *         operation
     */
    public static int goCmdCode() {
        return AbstractTupleCentreOperation.OPTYPE_GO_CMD;
    }

    /**
     *
     * @return the Integer representing the type code of
     *         <code>has_inspectors</code> operation
     */
    public static int hasInspCode() {
        return AbstractTupleCentreOperation.OPTYPE_HAS_INSP;
    }

    /**
     *
     * @return the Integer representing the type code of
     *         <code>has_observers</code> operation
     */
    public static int hasObsCode() {
        return AbstractTupleCentreOperation.OPTYPE_HAS_OBS;
    }

    /**
     *
     * @return the Integer representing the type code of <code>in_all</code>
     *         operation
     */
    public static int inAllCode() {
        return AbstractTupleCentreOperation.OPTYPE_IN_ALL;
    }

    /**
     *
     * @return the Integer representing the type code of <code>in</code>
     *         operation
     */
    public static int inCode() {
        return AbstractTupleCentreOperation.OPTYPE_IN;
    }

    /**
     *
     * @return the Integer representing the type code of <code>inp</code>
     *         operation
     */
    public static int inpCode() {
        return AbstractTupleCentreOperation.OPTYPE_INP;
    }

    /**
     *
     * @return the Integer representing the type code of <code>inp_s</code>
     *         operation
     */
    public static int inpSCode() {
        return AbstractTupleCentreOperation.OPTYPE_INP_S;
    }

    /**
     *
     * @return the Integer representing the type code of <code>in_s</code>
     *         operation
     */
    public static int inSCode() {
        return AbstractTupleCentreOperation.OPTYPE_IN_S;
    }

    /**
     *
     * @return the Integer representing the type code of <code>step_mode</code>
     *         operation
     */
    public static int isStepModeCode() {
        return AbstractTupleCentreOperation.OPTYPE_IS_STEP_MODE;
    }

    /**
     *
     * @return the Integer representing the type code of <code>next_step</code>
     *         operation
     */
    public static int nextStepCode() {
        return AbstractTupleCentreOperation.OPTYPE_NEXT_STEP;
    }

    /**
     *
     * @return the Integer representing the type code of <code>no_all</code>
     *         operation
     */
    public static int noAllCode() {
        return AbstractTupleCentreOperation.OPTYPE_NO_ALL;
    }

    /**
     *
     * @return the Integer representing the type code of <code>no</code>
     *         operation
     */
    public static int noCode() {
        return AbstractTupleCentreOperation.OPTYPE_NO;
    }

    /**
     *
     * @return the Integer representing the type code of <code>nop</code>
     *         operation
     */
    public static int nopCode() {
        return AbstractTupleCentreOperation.OPTYPE_NOP;
    }

    /**
     *
     * @return the Integer representing the type code of <code>nop_s</code>
     *         operation
     */
    public static int nopSCode() {
        return AbstractTupleCentreOperation.OPTYPE_NOP_S;
    }

    /**
     *
     * @return the Integer representing the type code of <code>no_s</code>
     *         operation
     */
    public static int noSCode() {
        return AbstractTupleCentreOperation.OPTYPE_NO_S;
    }

    /**
     *
     * @return the Integer representing the type code of <code>out_all</code>
     *         operation
     */
    public static int outAllCode() {
        return AbstractTupleCentreOperation.OPTYPE_OUT_ALL;
    }

    /**
     *
     * @return the Integer representing the type code of <code>out</code>
     *         operation
     */
    public static int outCode() {
        return AbstractTupleCentreOperation.OPTYPE_OUT;
    }

    /**
     *
     * @return the Integer representing the type code of <code>out_s</code>
     *         operation
     */
    public static int outSCode() {
        return AbstractTupleCentreOperation.OPTYPE_OUT_S;
    }

    /**
     *
     * @return the Integer representing the type code of <code>rd_all</code>
     *         operation
     */
    public static int rdAllCode() {
        return AbstractTupleCentreOperation.OPTYPE_RD_ALL;
    }

    /**
     *
     * @return the Integer representing the type code of <code>rd</code>
     *         operation
     */
    public static int rdCode() {
        return AbstractTupleCentreOperation.OPTYPE_RD;
    }

    /**
     *
     * @return the Integer representing the type code of <code>rdp</code>
     *         operation
     */
    public static int rdpCode() {
        return AbstractTupleCentreOperation.OPTYPE_RDP;
    }

    /**
     *
     * @return the Integer representing the type code of <code>rdp_s</code>
     *         operation
     */
    public static int rdpSCode() {
        return AbstractTupleCentreOperation.OPTYPE_RDP_S;
    }

    /**
     *
     * @return the Integer representing the type code of <code>rd_s</code>
     *         operation
     */
    public static int rdSCode() {
        return AbstractTupleCentreOperation.OPTYPE_RD_S;
    }

    /**
     *
     * @return the Integer representing the type code of <code>reset</code>
     *         operation
     */
    public static int reset() {
        return AbstractTupleCentreOperation.RESET;
    }

    /**
     *
     * @return the Integer representing the type code of
     *         <code>rmv_inspector</code> operation
     */
    public static int rmvInspCode() {
        return AbstractTupleCentreOperation.OPTYPE_RMV_INSP;
    }

    /**
     *
     * @return the Integer representing the type code of
     *         <code>rmv_observer</code> operation
     */
    public static int rmvObsCode() {
        return AbstractTupleCentreOperation.OPTYPE_RMV_OBS;
    }

    /**
     *
     * @return the Integer representing the type code of <code>set</code>
     *         operation
     */
    public static int setCode() {
        return AbstractTupleCentreOperation.OPTYPE_SET;
    }

    /**
     *
     * @return the type code of the <code>set_env</code> environmental setter
     *         operation
     */
    public static int setEnvCode() {
        return RespectOperation.OPTYPE_SET_ENV;
    }

    /**
     *
     * @return the Integer representing the type code of
     *         <code>set_mgm_mode</code> operation
     */
    /*
     * TODO must be delete... public static int setMngModeCode() { return
     * AbstractTupleCentreOperation.OPTYPE_SET_MNG_MODE; }
     */
    /**
     *
     * @return the Integer representing the type code of <code>set_s</code>
     *         operation
     */
    public static int setSCode() {
        return AbstractTupleCentreOperation.OPTYPE_SET_S;
    }

    /**
     *
     * @return the Integer representing the type code of <code>set_spy</code>
     *         operation
     */
    public static int setSpyCode() {
        return AbstractTupleCentreOperation.OPTYPE_SET_SPY;
    }

    /**
     *
     * @return the Integer representing the type code of the operation to set
     *         the input events set
     */
    public static int setWSetCode() {
        return AbstractTupleCentreOperation.OPTYPE_SET_WSET;
    }

    /**
     *
     * @return the Integer representing the type code of <code>spawn</code>
     *         operation
     */
    public static int spawnCode() {
        return AbstractTupleCentreOperation.OPTYPE_SPAWN;
    }

    /**
     *
     * @return the Integer representing the type code of <code>step_mode</code>
     *         operation
     */
    public static int stepModeCode() {
        return AbstractTupleCentreOperation.OPTYPE_STEP_MODE;
    }

    /**
     *
     * @return the Integer representing the type code of <code>stop_cmd</code>
     *         operation
     */
    public static int stopCmdCode() {
        return AbstractTupleCentreOperation.OPTYPE_STOP_CMD;
    }

    /**
     *
     * @return the Integer representing the type code of <code>time</code>
     *         operation
     */
    public static int timeCode() {
        return RespectOperation.OPTYPE_TIME;
    }

    /**
     *
     * @return the Integer representing the type code of <code>uin</code>
     *         operation
     */
    public static int uinCode() {
        return AbstractTupleCentreOperation.OPTYPE_UIN;
    }

    /**
     *
     * @return the Integer representing the type code of <code>uinp</code>
     *         operation
     */
    public static int uinpCode() {
        return AbstractTupleCentreOperation.OPTYPE_UINP;
    }

    /**
     *
     * @return the Integer representing the type code of <code>uno</code>
     *         operation
     */
    public static int unoCode() {
        return AbstractTupleCentreOperation.OPTYPE_UNO;
    }

    /**
     *
     * @return the Integer representing the type code of <code>unop</code>
     *         operation
     */
    public static int unopCode() {
        return AbstractTupleCentreOperation.OPTYPE_UNOP;
    }

    /**
     *
     * @return the Integer representing the type code of <code>urd</code>
     *         operation
     */
    public static int urdCode() {
        return AbstractTupleCentreOperation.OPTYPE_URD;
    }

    /**
     *
     * @return the Integer representing the type code of <code>urdp</code>
     *         operation
     */
    public static int urdpCode() {
        return AbstractTupleCentreOperation.OPTYPE_URDP;
    }

    private OperationHandler context = null;

    /**
     *
     * @param type
     *            the type code of the operation
     * @param t
     *            the tuple argument of the operation
     * @param l
     *            the listener for operation completion
     * @param ctx
     *            the ACC requesting the operation
     */
    public TucsonOperation(final int type, final Tuple t,
            final OperationCompletionListener l, final OperationHandler ctx) {
        super(type, t, l);
        this.context = ctx;
    }

    /**
     *
     * @param type
     *            the type code of the operation
     * @param t
     *            the tuple template argument of the operation
     * @param l
     *            the listener for operation completion
     * @param ctx
     *            the ACC requesting the operation
     */
    public TucsonOperation(final int type, final TupleTemplate t,
            final OperationCompletionListener l, final OperationHandler ctx) {
        super(type, t, l);
        this.context = ctx;
    }

    /*
     * (non-Javadoc)
     * @see alice.tucson.api.ITucsonOperation#getJTupleArgument()
     */
    @Override
    public Tuple getJTupleArgument() {
        final LogicTuple lt = this.getLogicTupleArgument();
        try {
            if (JTuplesEngine.isTemplate(lt)) {
                return JTuplesEngine.toJavaTupleTemplate(lt);
            }
            return JTuplesEngine.toJavaTuple(lt);
        } catch (final InvalidTupleException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
     * (non-Javadoc)
     * @see alice.tucson.api.ITucsonOperation#getJTupleListResult()
     */
    @Override
    public List<Tuple> getJTupleListResult() {
        final List<LogicTuple> lts = this.getLogicTupleListResult();
        final List<Tuple> jts = new ArrayList<Tuple>(lts.size());
        try {
            for (final LogicTuple t : lts) {
                if (JTuplesEngine.isTemplate(t)) {
                    jts.add(JTuplesEngine.toJavaTupleTemplate(t));
                } else {
                    jts.add(JTuplesEngine.toJavaTuple(t));
                }
            }
        } catch (final InvalidTupleException e) {
            e.printStackTrace();
            return null;
        }
        return jts;
    }

    /*
     * (non-Javadoc)
     * @see alice.tucson.api.ITucsonOperation#getJTupleResult()
     */
    @Override
    public Tuple getJTupleResult() {
        final LogicTuple lt = this.getLogicTupleArgument();
        try {
            if (JTuplesEngine.isTemplate(lt)) {
                return JTuplesEngine.toJavaTupleTemplate(lt);
            }
            return JTuplesEngine.toJavaTuple(lt);
        } catch (final InvalidTupleException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     *
     * @return the listener for operation completion
     */
    public OperationCompletionListener getListener() {
        return this.listener;
    }

    @Override
    public LogicTuple getLogicTupleArgument() {
        if (this.isOut() || this.isOutS() || this.isSetS() || this.isSet()
                || this.isOutAll() || this.isSpawn()) {
            return (LogicTuple) this.getTupleArgument();
        }
        return (LogicTuple) this.getTemplateArgument();
    }

    @Override
    public List<LogicTuple> getLogicTupleListResult() {
        final List<Tuple> tl = this.getTupleListResult();
        final List<LogicTuple> tll = new LinkedList<LogicTuple>();
        for (final Tuple t : tl) {
            tll.add((LogicTuple) t);
        }
        return tll;
    }

    @Override
    public LogicTuple getLogicTupleResult() {
        return (LogicTuple) this.getTupleResult();
    }

    /**
     *
     * @param s
     *            wether the operation succeeded
     * @param a
     *            wether the operation was allowed
     */
    public void notifyCompletion(final boolean s, final boolean a) {
        if (this.listener != null) {
            // System.out.println("......[TucsonOperation]: listener is "
            // + this.listener.getClass().getSimpleName());
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
     * @param tl
     *            the list of tuples result of the operation
     */
    public void setLogicTupleListResult(final List<LogicTuple> tl) {
        final List<Tuple> t = new LinkedList<Tuple>();
        for (final LogicTuple logicTuple : tl) {
            t.add(logicTuple);
        }
        this.setTupleListResult(t);
    }

    @Override
    public void waitForOperationCompletion(final long ms)
            throws OperationTimeOutException {
        synchronized (this.token) {
            if (!this.operationCompleted) {
                try {
                    this.token.wait(ms);
                } catch (final InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (!this.operationCompleted) {
                this.context.addOperationExpired(this.getId());
                throw new OperationTimeOutException();
            }
        }
    }
}
