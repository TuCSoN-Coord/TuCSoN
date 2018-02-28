package alice.tucson.service;

import alice.tucson.api.TucsonOperation;
import alice.tuplecentre.api.TupleCentreOperation;
import alice.tuplecentre.api.TupleOperationID;

import java.util.HashSet;
import java.util.Set;

public abstract class OperationDoer {

    /**
     * Requested TuCSoN operations
     */
    protected final Set<TucsonOperation> operations = new HashSet<>();

    /**
     *
     * @param opID
     * @return the tucson operation associated with the given ID
     */
    protected TupleCentreOperation findOpById(TupleOperationID opID) {
        for (TupleCentreOperation op: this.operations) {
            if (opID.equals(op.getId())){
                return op;
            }
        }
        return null;
    }
}
