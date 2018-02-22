package alice.tuplecentre.api;

import java.util.List;

/**
 *
 * @author Alessandro Ricci
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 *
 */
public interface ITCCycleResult {

    /**
     *
     * @return the time the operation was completed
     */
    long getEndTime();

    /**
     *
     * @return the outcome of the operation
     */
    Outcome getOpResult();

    /**
     *
     * @return the time the operation was requested
     */
    long getStartTime();

    /**
     *
     * @return the list of tuples result of the operation
     */
    List<?extends Tuple> getTupleListResult();

    /**
     *
     * @return the tuple result of the operation
     */
    Tuple getTupleResult();

    /**
     *
     * @return wether the result of the operation is defined
     */
    boolean isResultDefined();

    /**
     *
     * @return wether the result of the operation is a failure
     */
    boolean isResultFailure();

    /**
     *
     * @return wether the result of the operation is a success
     */
    boolean isResultSuccess();

    /**
     *
     * @param time
     *            the time at which the operation completed
     */
    void setEndTime(long time);

    /**
     *
     * @param o
     *            the outcome of the operation
     */
    void setOpResult(Outcome o);

    /**
     *
     * @param resList
     *            the list of tuples result of the operation
     */
    void setTupleListResult(List<?extends Tuple> resList);

    /**
     *
     * @param res
     *            the tuple result of the operation
     */
    void setTupleResult(Tuple res);


    /**
     *
     * Enumeration defining the result types of an operation on a tuple centre
     *
     * @author ste (mailto: s.mariani@unibo.it) on 17/lug/2013
     *
     */
    enum Outcome {
        /**
         *
         */
        FAILURE,
        /**
         *
         */
        SUCCESS,
        /**
         *
         */
        UNDEFINED
    }
}
