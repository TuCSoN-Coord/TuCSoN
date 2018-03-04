package alice.tucson.api;

import alice.respect.api.TupleCentreId;
import alice.tuplecentre.api.TupleCentreIdentifier;

/**
 * Interface for TuCSoN tuple centre identifier
 */
public interface TucsonTupleCentreId extends TupleCentreIdentifier {

    /**
     * @return the local tuple centre identifier
     */
    TupleCentreId getInternalTupleCentreId();
}
