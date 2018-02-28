package alice.tuplecentre.api;

import java.io.Serializable;

/**
 Interface that represents the operation ID in the tuple centre.
 */
public interface TupleOperationID extends Serializable{
    void increase();
}
