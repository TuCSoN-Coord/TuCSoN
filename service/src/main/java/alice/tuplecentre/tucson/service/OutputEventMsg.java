package alice.tuplecentre.tucson.service;

import java.io.Serializable;

import alice.tuple.logic.LogicTuple;
import alice.tuplecentre.api.OperationIdentifier;
import alice.tuplecentre.core.TupleCentreOpType;

/**
 * @author Michele Bombardi (mailto: michele.bombardi@studio.unibo.it)
 */
public class OutputEventMsg implements Serializable {
    private static final long serialVersionUID = 6617714748018050950L;
    private final boolean allowed;
    private final OperationIdentifier opId;
    private final TupleCentreOpType opType;
    private final LogicTuple reqTuple;
    private final Object resTuple;
    private final boolean resultSuccess;
    private final boolean success;

    /**
     * @param opId      the operation id
     * @param opType    the operation type code
     * @param allowed   wether the operation is allowed
     * @param completed wether the operation completed
     * @param succeeded wether the operation succeeded
     */
    public OutputEventMsg(final OperationIdentifier opId, final TupleCentreOpType opType, final boolean allowed,
                          final boolean completed, final boolean succeeded) {
        this.opId = opId;
        this.opType = opType;
        this.allowed = allowed;
        this.success = completed;
        this.reqTuple = null;
        this.resTuple = null;
        this.resultSuccess = succeeded;
    }

    /**
     * @param opId      the operation id
     * @param opType    the operation type code
     * @param allowed   wether the operation is allowed
     * @param completed wether the operation completed
     * @param succeeded wether the operation succeeded
     * @param req       the tuple argument of the operation
     * @param res       the object result of the operation (can be a tuple or a list
     *                  of tuples)
     */
    public OutputEventMsg(final OperationIdentifier opId, final TupleCentreOpType opType, final boolean allowed,
                          final boolean completed, final boolean succeeded, final LogicTuple req,
                          final Object res) {
        this.opId = opId;
        this.opType = opType;
        this.success = completed;
        this.allowed = allowed;
        this.reqTuple = req;
        this.resTuple = res;
        this.resultSuccess = succeeded;
    }

    /**
     * @return the id of the operation which caused the event
     */
    public OperationIdentifier getOpId() {
        return this.opId;
    }

    /**
     * @return the type code of the operation which caused the event
     */
    public TupleCentreOpType getOpType() {
        return this.opType;
    }

    /**
     * @return the logic tuple argument of the operation which caused the event
     */
    public LogicTuple getTupleRequested() {
        return this.reqTuple;
    }

    /**
     * @return the effect of the event
     */
    public Object getTupleResult() {
        return this.resTuple;
    }

    /**
     * @return wether the event was allowed
     */
    public boolean isAllowed() {
        return this.allowed;
    }

    /**
     * @return wether the effect has been applied succesfully
     */
    public boolean isResultSuccess() {
        return this.resultSuccess;
    }

    /**
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
