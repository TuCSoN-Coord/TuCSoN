package alice.respect.api;

import alice.tuplecentre.api.TupleCentreOperation;

/**
 * ReSpecT Operation Interface.
 *
 * @author Alessandro Ricci
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 */
public interface RespectOperation extends
        TupleCentreOperation {

    /**
     * @return <code>true</code> if this is an env operation
     */
    boolean isEnv();

    /**
     * @return <code>true</code> if this is a getEnv operation
     */
    boolean isGetEnv();

    /**
     * @return <code>true</code> if this is a setEnv operation
     */
    boolean isSetEnv();

    /**
     * @return <code>true</code> if this is a time operation
     */
    boolean isTime();
}
