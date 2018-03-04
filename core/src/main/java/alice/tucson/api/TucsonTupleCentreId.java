package alice.tucson.api;

import alice.tuplecentre.api.TupleCentreIdentifier;

/**
 * Interface for TuCSoN tuple centre identifier
 */
public interface TucsonTupleCentreId extends TupleCentreIdentifier {

    /**
     * @return the local tuple centre identifier
     */
    TupleCentreIdentifier getInternalTupleCentreId();
}
