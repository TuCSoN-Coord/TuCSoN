package alice.tuplecentre.api;

import java.util.List;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;

/**
 * Basic interface for tuple centre operations.
 *
 * @author Alessandro Ricci
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 */
public interface ITupleCentreOperation {

    /**
     * Get operation identifier
     *
     * @return Operation identifier
     */
    long getId();

    /**
     *
     * @return the tuple template argument of this operation
     */
    TupleTemplate getTemplateArgument();

    /**
     *
     * @return the tuple argument of this operation
     */
    Tuple getTupleArgument();

    /**
     *
     * @return the list of tuples argument of this operation
     */
    List<Tuple> getTupleListArgument();

    /**
     *
     * @return the list of tuples result of this operation
     */
    List<Tuple> getTupleListResult();

    /**
     *
     * @return the tuple result of this operation
     */
    Tuple getTupleResult();

    /**
     *
     * @return wether this operation is a <code>get</code> operation
     */
    boolean isGet();

    /**
     *
     * @return wether this operation is a <code>get_s</code> operation
     */
    boolean isGetS();

    /**
     *
     * @return wether this operation is a <code>in</code> operation
     */
    boolean isIn();

    /**
     *
     * @return wether this operation is a <code>in_all</code> operation
     */
    boolean isInAll();

    /**
     *
     * @return wether this operation is a <code>inp</code> operation
     */
    boolean isInp();

    /**
     *
     * @return wether this operation is a <code>no</code> operation
     */
    boolean isNo();

    /**
     *
     * @return wether this operation is a <code>noall</code> operation
     */
    boolean isNoAll();

    /**
     *
     * @return wether this operation is a <code>nop</code> operation
     */
    boolean isNop();

    /**
     * Tests if the operation is completed
     *
     * @return true if the operation is completed
     */
    boolean isOperationCompleted();

    /**
     *
     * @return wether this operation is a <code>out</code> operation
     */
    boolean isOut();

    /**
     *
     * @return wether this operation is a <code>out_all</code> operation
     */
    boolean isOutAll();

    /**
     *
     * @return wether this operation is a <code>rd</code> operation
     */
    boolean isRd();

    /**
     *
     * @return wether this operation is a <code>rd_all</code> operation
     */
    boolean isRdAll();

    /**
     *
     * @return wether this operation is a <code>rdp</code> operation
     */
    boolean isRdp();

    /**
     *
     * @return wether this operation failed
     */
    boolean isResultFailure();

    /**
     *
     * @return wether this operation succeeded
     */
    boolean isResultSuccess();

    /**
     *
     * @return wether this operation is a <code>set</code> operation
     */
    boolean isSet();

    /**
     *
     * @return wether this operation is a <code>set_s</code> operation
     */
    boolean isSetS();

    /**
     *
     * @return wether this operation is a <code>uin</code> operation
     */
    boolean isUin();

    /**
     *
     * @return wether this operation is a <code>uinp</code> operation
     */
    boolean isUinp();

    /**
     *
     * @return wether this operation is a <code>urd</code> operation
     */
    boolean isUrd();

    /**
     *
     * @return wether this operation is a <code>urdp</code> operation
     */
    boolean isUrdp();

    /**
     * Wait for operation completion
     *
     * Current execution flow is blocked until the operation is completed
     */
    void waitForOperationCompletion();

    /**
     * Wait for operation completion, with time out
     *
     * Current execution flow is blocked until the operation is completed or a
     * maximum waiting time is elapsed
     *
     * @param ms
     *            maximum waiting time
     * @throws OperationTimeOutException
     *             if the given timeout expires prior to operation completion
     */
    void waitForOperationCompletion(final long ms) throws OperationTimeOutException;
}
