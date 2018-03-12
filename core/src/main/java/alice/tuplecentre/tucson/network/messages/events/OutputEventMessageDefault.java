package alice.tuplecentre.tucson.network.messages.events;

import alice.tuple.logic.LogicTuple;
import alice.tuplecentre.api.OperationIdentifier;
import alice.tuplecentre.core.TupleCentreOpType;

/**
 * // TODO add documentation
 *
 * @author Michele Bombardi (mailto: michele.bombardi@studio.unibo.it)
 */
public class OutputEventMessageDefault extends AbstractEventMessage implements OutputEventMessage {

    private final boolean allowed;
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
    public OutputEventMessageDefault(final OperationIdentifier opId, final TupleCentreOpType opType, final boolean allowed,
                                     final boolean completed, final boolean succeeded) {
        super(opType, null, opId);
        this.allowed = allowed;
        this.success = completed;
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
    public OutputEventMessageDefault(final OperationIdentifier opId, final TupleCentreOpType opType, final boolean allowed,
                                     final boolean completed, final boolean succeeded, final LogicTuple req,
                                     final Object res) {
        super(opType, req, opId);
        this.success = completed;
        this.allowed = allowed;
        this.resTuple = res;
        this.resultSuccess = succeeded;
    }

    @Override
    public Object getTupleResult() {
        return this.resTuple;
    }

    @Override
    public boolean isAllowed() {
        return this.allowed;
    }

    @Override
    public boolean isResultSuccess() {
        return this.resultSuccess;
    }

    @Override
    public boolean isSuccess() {
        return this.success;
    }

    @Override
    public String toString() {
        return "[ op: " + "( " + getOpId() + "," + getOpType() + " ), "
                + "allowed: " + this.isAllowed() + ", " + "success: "
                + this.isSuccess() + ", " + "result success: "
                + this.resultSuccess + ", " + "req: " + getTuple() + ", "
                + "res: " + this.resTuple + "]";
    }
}
