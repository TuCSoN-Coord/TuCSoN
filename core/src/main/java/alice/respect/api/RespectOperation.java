package alice.respect.api;

import alice.logictuple.LogicTuple;
import alice.tuplecentre.api.TupleCentreOperation;

/**
 * ReSpecT Operation Interface.
 *
 * @author Alessandro Ricci
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 */
public interface RespectOperation extends TupleCentreOperation {

    /**
     * @return the logic tuple representation of this operation
     */
    LogicTuple toTuple();
}
