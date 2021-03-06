package alice.tuplecentre.respect.api;

import alice.tuple.logic.LogicTuple;
import alice.tuplecentre.api.TupleCentreOperation;

/**
 * ReSpecT Operation Interface.
 *
 * @author Alessandro Ricci
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 */
public interface RespectOperation extends TupleCentreOperation {

    /**
     * @return the logic tuple representation copyOf this operation
     */
    LogicTuple toTuple();
}
