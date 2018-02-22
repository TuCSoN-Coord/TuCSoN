package alice.tuplecentre.api;

import java.util.List;

import alice.tuplecentre.api.exceptions.OperationTimeOutException;
import alice.tuplecentre.core.OperationCompletionListener;

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
    long getId(); //TODO replace return type with TupleOperationID (to be created) (will be implemented by TucsonOpId class)

    /**
     * @return the type code of the operation
     */
    int getType(); //TODO modificare il tipo di ritorno in modo che sia l'enumerazione (da creare) che rappresenti i tipi di operazioni

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
     * @return wether this operation is a <code>get</code> operation
     */
    boolean isGet();

    /**
     * @return wether this operation is a <code>get_s</code> operation
     */
    boolean isGetS();

    /**
     * @return wether this operation is a <code>in</code> operation
     */
    boolean isIn();

    /**
     * @return wether this operation is a <code>in_all</code> operation
     */
    boolean isInAll();

    /**
     * @return wether this operation is a <code>inp</code> operation
     */
    boolean isInp();

    /**
     * @return wether this operation is a <code>inp_s</code>
     */
    boolean isInpS();

    /**
     * @return wether this operation is a <code>in_s</code>
     */
    boolean isInS();

    /**
     * @return wether this operation is a <code>no</code> operation
     */
    boolean isNo();

    /**
     * @return wether this operation is a <code>no_all</code>
     */
    boolean isNoAll();

    /**
     * @return wether this operation is a <code>nop</code> operation
     */
    boolean isNop();

    /**
     * @return wether this operation is a <code>nop_s</code>
     */
    boolean isNopS();

    /**
     * @return wether this operation is a <code>no_s</code>
     */
    boolean isNoS();

    /**
     * @return wether this operation is a <code>out</code> operation
     */
    boolean isOut();

    /**
     * @return wether this operation is a <code>out_all</code> operation
     */
    boolean isOutAll();

    /**
     * @return wether this operation is a <code>out_s</code>
     */
    boolean isOutS();

    /**
     * @return wether this operation is a <code>rd</code> operation
     */
    boolean isRd();

    /**
     * @return wether this operation is a <code>rd_all</code> operation
     */
    boolean isRdAll();

    /**
     * @return wether this operation is a <code>rdp</code> operation
     */
    boolean isRdp();

    /**
     * @return wether this operation is a <code>rdp_s</code>
     */
    boolean isRdpS();

    /**
     * @return wether this operation is a <code>rd_s</code>
     */
    boolean isRdS();

    /**
     * @return wether this operation is a <code>set</code> operation
     */
    boolean isSet();

    /**
     * @return wether this operation is a <code>set_s</code> operation
     */
    boolean isSetS();

    /**
     * @return wether this operation is a <code>spawn</code>
     */
    boolean isSpawn();

    /**
     * @return wether this operation is a <code>uin</code> operation
     */
    boolean isUin();

    /**
     * @return wether this operation is a <code>uinp</code> operation
     */
    boolean isUinp();

    /**
     * @return wether this operation is a <code>uno</code> operation
     */
    boolean isUno();

    /**
     * @return wether this operation is a <code>unop</code> operation
     */
    boolean isUnop();

    /**
     * @return wether this operation is a <code>urd</code> operation
     */
    boolean isUrd();

    /**
     * @return wether this operation is a <code>urdp</code> operation
     */
    boolean isUrdp();

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
     * Adds listener for completion Event
     *
     * @param lis the listener for operation completion to add
     */
    void setCompletionListener(final OperationCompletionListener lis);

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
