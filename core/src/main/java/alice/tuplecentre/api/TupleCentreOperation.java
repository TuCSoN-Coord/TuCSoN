package alice.tuplecentre.api;

import java.util.List;

import alice.logictuple.LogicTuple;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;
import alice.tuplecentre.core.OperationCompletionListener;
import alice.tuplecentre.core.TupleCentreOpType;

/**
 * Basic interface for tuple centre operations.
 *
 * @author Alessandro Ricci
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 */
public interface TupleCentreOperation {

    /**
     * Get operation identifier
     *
     * @return Operation identifier
     */
    TupleOperationID getId();

    /**
     * @return the type code of the operation
     */
    TupleCentreOpType getType();

    /**
     * @return the tuple template argument of this operation
     */
    TupleTemplate getTemplateArgument();

    /**
     * Gets the tuple argument used in the operation.
     *
     * @return the tuple argument used in the operation.
     */
    Tuple getTupleArgument();

    /**
     * @return the list of tuples argument of this operation
     */
    List<Tuple> getTupleListArgument();

    /**
     * @return the tuple representing the primitive invoked
     */
    Tuple getPrimitive();

    /**
     * @return the Tuple representing the whole invocation predicate (primitive + tuple argument)
     */
    Tuple getPredicate();

    /**
     * Sets the tuple list as result for this operation
     *
     * @param t the list of tuples, result of the operation
     */
    void setTupleListResult(final List<? extends Tuple> t);

    /**
     * Gets the list of tuples returned as the result of the requested
     * operation.
     *
     * @return the list of tuples result of the requested operation.
     */
    List<Tuple> getTupleListResult();

    /**
     * Sets the tuple as result for this operation
     *
     * @param t the tuple result of the operation
     */
    void setTupleResult(final Tuple t);

    /**
     * Gets the tuple returned as the result of the requested operation.
     *
     * @return the tuple result of the requested operation.
     */
    Tuple getTupleResult();

    /**
     * Sets the operation result {@link ITCCycleResult.Outcome}
     *
     * @param o the outcome of the operation
     */
    void setOpResult(final ITCCycleResult.Outcome o);

    /**
     * Gets the tuple argument used in the operation.
     *
     * @return the tuple argument used in the operation.
     */
    LogicTuple getLogicTupleArgument();
    //TODO if and when LogicTuple public methods will be moved to an upper Interface...
    // TODO here and in implementing methods, would be better to set return type to such interface

    /**
     * Gets the list of tuples returned as the result of the requested
     * operation.
     *
     * @return the list of tuples result of the requested operation.
     */
    List<LogicTuple> getLogicTupleListResult();

    /**
     * Gets the tuple returned as the result of the requested operation.
     *
     * @return the tuple result of the requested operation.
     */
    LogicTuple getLogicTupleResult();

    /**
     * Tests if the result is defined
     *
     * @return true if the result is defined
     */
    boolean isResultDefined();

    /**
     * @return wether this operation failed
     */
    boolean isResultFailure();

    /**
     * Checks success of operation execution.
     *
     * @return <code>true</code> if the operation succeeded, <code>false</code>
     * otherwise (failure or undefined)
     */
    boolean isResultSuccess();

    /**
     * Adds listener for completion Events
     *
     * @param lis the listener for operation completion to add
     */
    void setCompletionListener(final OperationCompletionListener lis);

    /**
     * Gets the listener for completion Events
     *
     * @return the listener for operation completion
     */
    OperationCompletionListener getCompletionListener();

    /**
     * Removes listener for completion Event
     */
    void removeCompletionListener();

    /**
     * Tests if the operation is completed
     *
     * @return true if the operation is completed
     */
    boolean isOperationCompleted();

    /**
     * Wait for operation completion
     * <p>
     * Current execution flow is blocked until the operation is completed
     */
    void waitForOperationCompletion();

    /**
     * Wait for operation completion, with time out
     * <p>
     * Current execution flow is blocked until the operation is completed or a
     * maximum waiting time is elapsed
     *
     * @param ms maximum waiting time
     * @throws OperationTimeOutException if the given timeout expires prior to operation completion
     */
    void waitForOperationCompletion(final long ms) throws OperationTimeOutException;

    /**
     * Changes the state of the operation to complete, and notifies the attached listener if present.
     */
    void notifyCompletion();
}
