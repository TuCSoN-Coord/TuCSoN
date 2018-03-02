package alice.tuplecentre.api;

import java.io.Serializable;

/**
 * Interface that represents the operation ID in the tuple centre.
 *
 * @author Nicola Piscaglia
 * @author (contributor) Enrico Siboni
 */
public interface TupleOperationID extends Serializable {

    /**
     * @return the progressive, unique identifier of TuCSoN operations
     */
    long getId();
}
