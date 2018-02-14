package alice.respect.api;

import java.util.List;
import alice.logictuple.LogicTuple;

/**
 * ReSpecT Operation Interface.
 *
 * @author Alessandro Ricci
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 */
public interface IRespectOperation extends
        alice.tuplecentre.api.ITupleCentreOperation {

    /**
     * Gets the argument of the operation
     *
     * @return the argument of this operation
     */
    LogicTuple getLogicTupleArgument();

    /**
     * Gets the results of a get operation
     *
     * @return the result of this operation
     */
    List<LogicTuple> getLogicTupleListResult();

    /**
     * Gets the result of the operation,
     *
     * @return the result of this operation
     */
    LogicTuple getLogicTupleResult();

    /**
     *
     * @return <code>true</code> if this is an env operation
     */
    boolean isEnv();

    /**
     *
     * @return <code>true</code> if this is a getEnv operation
     */
    boolean isGetEnv();

    /**
     *
     * @return <code>true</code> if this is a setEnv operation
     */
    boolean isSetEnv();

    /**
     *
     * @return <code>true</code> if this is a time operation
     */
    boolean isTime();
}
