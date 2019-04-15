package alice.tuplecentre.tucson.api;

import alice.tuplecentre.api.TupleCentreIdentifier;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidTupleCentreIdException;

/**
 * Interface for TuCSoN tuple centre identifier
 */
public interface TucsonTupleCentreId extends TupleCentreIdentifier {

    /**
     * @return the local tuple centre identifier
     */
    TupleCentreIdentifier getInternalTupleCentreId();

    static TucsonTupleCentreId of(final String tcName, final String netid, final String portno) throws TucsonInvalidTupleCentreIdException {
        return new TucsonTupleCentreIdDefault(tcName, netid, portno);
    }

    static TucsonTupleCentreIdDefault of(final TupleCentreIdentifier id) {
        return new TucsonTupleCentreIdDefault(id);
    }

    static TucsonTupleCentreIdDefault of(final String id) throws TucsonInvalidTupleCentreIdException {
        return new TucsonTupleCentreIdDefault(id);
    }
}
