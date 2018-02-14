package alice.tucson.service;

import java.io.Serializable;
import alice.logictuple.LogicTuple;

/**
 * 
 * @author Michele Bombardi (mailto: michele.bombardi@studio.unibo.it)
 * 
 */
public class OutputEventMsg implements Serializable {
    private static final long serialVersionUID = 6617714748018050950L;
    private final boolean allowed;
    private final long opId;
    private final int opType;
    private final LogicTuple reqTuple;
    private final Object resTuple;
    private final boolean resultSuccess;
    private final boolean success;

    /**
     * 
     * @param i
     *            the operation id
     * @param t
     *            the operation type code
     * @param a
     *            wether the operation is allowed
     * @param s
     *            wether the operation completed
     * @param ok
     *            wether the operation succeeded
     */
    public OutputEventMsg(final long i, final int t, final boolean a,
            final boolean s, final boolean ok) {
        this.opId = i;
        this.opType = t;
        this.allowed = a;
        this.success = s;
        this.reqTuple = null;
        this.resTuple = null;
        this.resultSuccess = ok;
    }

    /**
     * 
     * @param i
     *            the operation id
     * @param t
     *            the operation type code
     * @param a
     *            wether the operation is allowed
     * @param s
     *            wether the operation completed
     * @param ok
     *            wether the operation succeeded
     * @param req
     *            the tuple argument of the operation
     * @param res
     *            the object result of the operation (can be a tuple or a list
     *            of tuples)
     */
    public OutputEventMsg(final long i, final int t, final boolean a,
            final boolean s, final boolean ok, final LogicTuple req,
            final Object res) {
        this.opId = i;
        this.opType = t;
        this.success = s;
        this.allowed = a;
        this.reqTuple = req;
        this.resTuple = res;
        this.resultSuccess = ok;
    }

    /**
     * 
     * @return the id of the operation which caused the event
     */
    public long getOpId() {
        return this.opId;
    }

    /**
     * 
     * @return the type code of the operation which caused the event
     */
    public int getOpType() {
        return this.opType;
    }

    /**
     * 
     * @return the logic tuple argument of the operation which caused the event
     */
    public LogicTuple getTupleRequested() {
        return this.reqTuple;
    }

    /**
     * 
     * @return the effect of the event
     */
    public Object getTupleResult() {
        return this.resTuple;
    }

    /**
     * 
     * @return wether the event was allowed
     */
    public boolean isAllowed() {
        return this.allowed;
    }

    /**
     * 
     * @return wether the effect has been applied succesfully
     */
    public boolean isResultSuccess() {
        return this.resultSuccess;
    }

    /**
     * 
     * @return wether the event has been handled succesfully
     */
    public boolean isSuccess() {
        return this.success;
    }

    @Override
    public String toString() {
        return "[ op: " + "( " + this.opId + "," + this.opType + " ), "
                + "allowed: " + this.isAllowed() + ", " + "success: "
                + this.isSuccess() + ", " + "result success: "
                + this.resultSuccess + ", " + "req: " + this.reqTuple + ", "
                + "res: " + this.resTuple + "]";
    }
}
