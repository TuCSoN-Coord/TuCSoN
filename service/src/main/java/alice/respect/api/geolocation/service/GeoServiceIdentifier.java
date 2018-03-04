package alice.respect.api.geolocation.service;

import alice.tuplecentre.api.EmitterIdentifier;
import alice.tuprolog.Term;

/**
 * Interface for GeoLocation service identifier
 *
 * @author Nicola Piscaglia
 */
public interface GeoServiceIdentifier extends EmitterIdentifier {


    /**
     * @return the name given to the Geolocation Service
     */
    String getLocalName();

    /**
     * @return the tuProlog Term representation of this Geolocation Service id
     */
    Term toTerm();
}
