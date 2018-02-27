package alice.respect.api.geolocation.service;

import java.util.ArrayList;
import java.util.List;

import alice.respect.api.geolocation.Position;
import alice.respect.api.place.IPlace;
import alice.respect.api.place.PhPlace;
import alice.tucson.api.TucsonTupleCentreId;
import alice.tuplecentre.core.TupleCentreOpType;

/**
 * This class represent the generic geolocation service and implements some of
 * common behaviors of services. To define a geolocation service like google
 * maps, an exension of this class must be defined.
 * 
 * @author Michele Bombardi (mailto: michele.bombardi@studio.unibo.it)
 * 
 */
public abstract class AbstractGeolocationService implements GeoLocationService {
    /**
     * Service execution platform
     */
    private final int platform;
    /**
     * Specify if the geolocation service must generate spatial events (from
     * and/or to) or not
     */
    protected boolean genSpatialEvents = false;
    /**
     * List of listeners associated to the service
     */
    protected List<GeoLocationServiceListener> listeners;
    /**
     * Represent the running status of the service
     */
    protected boolean running = false;
    /**
     * Service identifier
     */
    protected GeoServiceId serviceId;
    /**
     * Identifier of the associated tuple centre
     */
    protected TucsonTupleCentreId tcId;

    /**
     * Constructs a service
     * 
     * @param p
     *            the platform code of the host platform
     * @param sid
     *            the service identifier
     * @param ttci
     *            the associated tuple centre identifier
     */
    public AbstractGeolocationService(final Integer p, final GeoServiceId sid,
            final TucsonTupleCentreId ttci) {
        this.platform = p;
        this.serviceId = sid;
        this.tcId = ttci;
        this.listeners = new ArrayList<GeoLocationServiceListener>();
    }

    @Override
    public void addListener(final GeoLocationServiceListener l) {
        this.listeners.add(l);
    }

    @Override
    public void generateSpatialEvents(final boolean generate) {
        this.genSpatialEvents = generate;
    }

    @Override
    public int getPlatform() {
        return this.platform;
    }

    @Override
    public GeoServiceId getServiceId() {
        return this.serviceId;
    }

    @Override
    public TucsonTupleCentreId getTcId() {
        return this.tcId;
    }

    @Override
    public synchronized boolean isRunning() {
        return this.running;
    }

    @Override
    public void notifyLocationChanged(final double lat, final double lng) {
        for (final GeoLocationServiceListener l : this.listeners) {
            l.locationChanged(new PhPlace("coords(" + lat + "," + lng + ")"));
        }
    }

    @Override
    public void notifyLocationChanged(final IPlace place) {
        for (final GeoLocationServiceListener l : this.listeners) {
            l.locationChanged(place);
        }
    }

    @Override
    public void notifyStartMovement(final double lat, final double lng) {
        final IPlace place = new PhPlace("coords(" + lat + "," + lng + ")");
        for (final GeoLocationServiceListener l : this.listeners) {
            l.moving(TupleCentreOpType.FROM, Position.PH, place);
        }
    }

    @Override
    public void notifyStartMovement(final String space, final IPlace place) {
        for (final GeoLocationServiceListener l : this.listeners) {
            l.moving(TupleCentreOpType.FROM, space, place);
        }
    }

    @Override
    public void notifyStopMovement(final double lat, final double lng) {
        final IPlace place = new PhPlace("coords(" + lat + "," + lng + ")");
        for (final GeoLocationServiceListener l : this.listeners) {
            l.moving(TupleCentreOpType.TO, Position.PH, place);
        }
    }

    @Override
    public void notifyStopMovement(final String space, final IPlace place) {
        for (final GeoLocationServiceListener l : this.listeners) {
            l.moving(TupleCentreOpType.TO, space, place);
        }
    }

    @Override
    public synchronized void start() {
        this.running = true;
    }

    @Override
    public synchronized void stop() {
        this.running = false;
    }
}
