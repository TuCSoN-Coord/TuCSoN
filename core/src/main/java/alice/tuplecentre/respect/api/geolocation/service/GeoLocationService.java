package alice.tuplecentre.respect.api.geolocation.service;

import alice.tuplecentre.respect.api.place.IPlace;
import alice.tuplecentre.tucson.api.TucsonTupleCentreId;
import alice.tuprolog.Term;

/**
 * Generic GeoLocation service interface.
 *
 * @author Michele Bombardi (mailto: michele.bombardi@studio.unibo.it)
 */
public interface GeoLocationService {

    /**
     * Adds a listener to the list
     *
     * @param l an GeoLocationServiceListener that listens for changes
     */
    void addListener(final GeoLocationServiceListener l);

    /**
     * Specify if the geolocation service must generate spatial events (from
     * and/or to) or not
     *
     * @param generate represent the geolocation service behavior: specify
     *                 <code>true</code> if spatial events have to be generated,
     *                 <code>false</code> otherwise.
     */
    void generateSpatialEvents(final boolean generate);

    /**
     * Makes a request to the geolocation service to translate a textual address
     * in latitude and longitude coordinates
     *
     * @param address the textual address to translate
     * @return a string representing coordinates of the address (coords(Lat,
     * Lng))
     */
    Term geoCode(final String address);

    /**
     * Gets the service execution platform
     *
     * @return the service platform
     */
    int getPlatform();

    /**
     * Gets the service identifier
     *
     * @return service identifier
     */
    GeoServiceIdentifier getServiceId();

    /**
     * Gets the tuple centre identifier associated with this service
     *
     * @return tuple centre identifier
     */
    TucsonTupleCentreId getTcId();

    /**
     * Return the running status of the service
     *
     * @return <code>true</code> if the service is running
     */
    boolean isRunning();

    /**
     * Notifies to listeners that the physical absolute position is changed
     *
     * @param lat latitude coordinate
     * @param lng longitude coordinate
     */
    void notifyLocationChanged(final double lat, final double lng);

    /**
     * Notifies to listeners that the position is changed
     *
     * @param place the new position
     */
    void notifyLocationChanged(final IPlace place);

    /**
     * Notifies to listeners that the movement is started (from events)
     *
     * @param lat latitude coordinate
     * @param lng longitude coordinate
     */
    void notifyStartMovement(final double lat, final double lng);

    /**
     * Notifies to listeners that the movement is started (from events)
     *
     * @param space type of node position. It can be specified as either its
     *              absolute physical position (S=ph), its IP number (S=ip), its
     *              domain name (S=dns), its geographical location (S=map), or its
     *              organisational position (S=org).
     * @param place the start position
     */
    void notifyStartMovement(final String space, final IPlace place);

    /**
     * Notifies to listeners that the movement is terminated (to events)
     *
     * @param lat latitude coordinate
     * @param lng longitude coordinate
     */
    void notifyStopMovement(final double lat, final double lng);

    /**
     * Notifies to listeners that the movement is terminated (to events)
     *
     * @param space type of node position. It can be specified as either its
     *              absolute physical position (S=ph), its IP number (S=ip), its
     *              domain name (S=dns), its geographical location (S=map), or its
     *              organisational position (S=org).
     * @param place the final position
     */
    void notifyStopMovement(final String space, final IPlace place);

    /**
     * Starts the geolocation service
     */
    void start();

    /**
     * Stops the geolocation service
     */
    void stop();
}
