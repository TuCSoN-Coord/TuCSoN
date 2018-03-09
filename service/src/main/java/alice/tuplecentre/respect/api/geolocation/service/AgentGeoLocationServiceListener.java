package alice.tuplecentre.respect.api.geolocation.service;

import alice.tuplecentre.core.TupleCentreOpType;
import alice.tuplecentre.respect.api.place.IPlace;
import alice.tuplecentre.tucson.api.TucsonTupleCentreId;
import alice.tuplecentre.tucson.service.ACCProxyAgentSide;

/**
 * This class represent the listener that listens for geolocation service
 * changes and implements the behavior in response to some admissible
 * geolocation events. This class is delegated to interface with tucson, giving
 * origin to "from" and "to" events.
 *
 * @author Michele Bombardi (mailto: michele.bombardi@studio.unibo.it)
 */
public class AgentGeoLocationServiceListener implements GeoLocationServiceListener {
    /**
     * The acc context of the agent associated with this listener
     */
    private final ACCProxyAgentSide acc;
    /**
     * Listener identifier
     */
    private final GeoLocationService service;
    /**
     * Identifier of the associated tuple centre
     */
    private final TucsonTupleCentreId tcId;

    /**
     * Constructs a listener
     *
     * @param accProxyAgentSide the acc context of the agent associated with this listener
     * @param s                 the service associated
     * @param ttci              the associated tuple centre identifier
     */
    public AgentGeoLocationServiceListener(
            final ACCProxyAgentSide accProxyAgentSide,
            final GeoLocationService s, final TucsonTupleCentreId ttci) {
        this.acc = accProxyAgentSide;
        this.service = s;
        this.tcId = ttci;
    }

    /**
     * @return the ACC for which this Geolocation Listener is responsible for.
     */
    public ACCProxyAgentSide getACC() {
        return this.acc;
    }

    @Override
    public GeoLocationService getService() {
        return this.service;
    }

    @Override
    public GeoServiceId getServiceId() {
        return this.service.getServiceId();
    }

    @Override
    public TucsonTupleCentreId getTcId() {
        return this.tcId;
    }

    @Override
    public void locationChanged(final IPlace place) {
        this.acc.setPosition(place);
    }

    @Override
    public void moving(final TupleCentreOpType type, final String space, final IPlace place) {
        /*
         * For future usage.
         */
    }
}
