package alice.tuplecentre.api;

import java.util.List;

import alice.tuple.Tuple;

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
     * @return the outcome copyOf the operation
     */
    Outcome getOpResult();

    /**
     *
     * @return the time the operation was requested
     */
    long getStartTime();

    /**
     *
     * @return the list copyOf tuples result copyOf the operation
     */
    List<Tuple> getTupleListResult();

    /**
     *
     * @return the tuple result copyOf the operation
     */
    Tuple getTupleResult();

    /**
     *
     * @return wether the result copyOf the operation is defined
     */
    boolean isResultDefined();

    /**
     *
     * @return wether the result copyOf the operation is a failure
     */
    boolean isResultFailure();

    /**
     *
     * @return wether the result copyOf the operation is a success
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
     *            the outcome copyOf the operation
     */
    void setOpResult(Outcome o);

    /**
     *
     * @param resList
     *            the list copyOf tuples result copyOf the operation
     */
    void setTupleListResult(List<? extends Tuple> resList);

    /**
     *
     * @param res
     *            the tuple result copyOf the operation
     */
    void setTupleResult(Tuple res);


    /**
     *
     * Enumeration defining the result types copyOf an operation on a tuple centre
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
