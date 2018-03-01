package alice.respect.api.geolocation.service;

import alice.respect.api.place.IPlace;
import alice.tucson.api.TucsonTupleCentreId;
import alice.tuplecentre.core.TupleCentreOpType;

/**
 * Generic geolocation service listener interface.
 * 
 * @author Michele Bombardi (mailto: michele.bombardi@studio.unibo.it)
 * 
 */
public interface IGeolocationServiceListener {
    /**
     * Gets the associated geolocation service
     * 
     * @return the associated geolocation service
     */
    AbstractGeolocationService getService();

    /**
     * Gets the associated geolocation service identifier
     * 
     * @return service identifier
     */
    GeoServiceId getServiceId();

    /**
     * Gets the tuple centre identifier associated with this listener and
     * related service
     * 
     * @return tuple centre identifier
     */
    TucsonTupleCentreId getTcId();

    /**
     * Called by the related service when the location is changed
     * 
     * @param place
     *            the new position
     */
    void locationChanged(final IPlace place);

    /**
     * Called by the related service when the device starts or stops moving
     * 
     * @param type
     *            the type of the events (from or to)
     * @param space
     *            type of node position. It can be specified as either its
     *            absolute physical position (S=ph), its IP number (S=ip), its
     *            domain name (S=dns), its geographical location (S=map), or its
     *            organisational position (S=org).
     * @param place
     *            the start/end position
     */
    void moving(final TupleCentreOpType type, final String space, final IPlace place);
}
