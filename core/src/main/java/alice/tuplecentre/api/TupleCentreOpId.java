package alice.tuplecentre.api;

import java.io.Serializable;

/**
 * Interface that represents the operation ID on a tuple centre.
 *
 * @author Nicola Piscaglia
 * @author (contributor) Enrico Siboni
 */
public interface TupleCentreOpId extends Serializable {

    /**
     * @return the progressive, unique identifier of operations
     */
    long getId();
}
