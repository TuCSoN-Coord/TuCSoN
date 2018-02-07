package alice.respect.api.geolocation.service;

import alice.logictuple.LogicTuple;
import alice.logictuple.exceptions.InvalidLogicTupleException;
import alice.respect.api.ISpatialContext;
import alice.respect.api.place.IPlace;
import alice.respect.core.RespectOperation;
import alice.respect.core.RespectTCContainer;
import alice.tucson.api.TucsonTupleCentreId;
import alice.tuplecentre.core.InputEvent;

/**
 * This class represent the listener that listens for geolocation service
 * changes and implements the behavior in response to some admissible
 * geolocation events. This class is delegated to interface with tucson, giving
 * origin to "from" and "to" events.
 * 
 * @author Michele Bombardi (mailto: michele.bombardi@studio.unibo.it)
 * 
 */
public class GeolocationServiceListener implements IGeolocationServiceListener {
    /**
     * Listener identifier
     */
    private final AbstractGeolocationService service;
    /**
     * Identifier of the associated tuple centre
     */
    private final TucsonTupleCentreId tcId;

    /**
     * Constructs a listener
     * 
     * @param s
     *            the service associated
     * @param ttci
     *            the associated tuple centre identifier
     */
    public GeolocationServiceListener(final AbstractGeolocationService s,
            final TucsonTupleCentreId ttci) {
        this.service = s;
        this.tcId = ttci;
    }

    @Override
    public AbstractGeolocationService getService() {
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
        final ISpatialContext context = RespectTCContainer
                .getRespectTCContainer().getSpatialContext(
                        this.tcId.getInternalTupleCentreId());
        context.setPosition(place);
    }

    @Override
    public void moving(final int type, final String space, final IPlace place) {
        try {
            final ISpatialContext context = RespectTCContainer
                    .getRespectTCContainer().getSpatialContext(
                            this.tcId.getInternalTupleCentreId());
            LogicTuple tuple = null;
            RespectOperation op = null;
            if (type == RespectOperation.OPTYPE_FROM) {
                tuple = LogicTuple.parse("from(" + space + "," + place.toTerm()
                        + ")");
                op = RespectOperation.makeFrom(tuple, null);
            } else if (type == RespectOperation.OPTYPE_TO) {
                tuple = LogicTuple.parse("to(" + space + "," + place.toTerm()
                        + ")");
                op = RespectOperation.makeTo(tuple, null);
            }
            final InputEvent ev = new InputEvent(this.service.getServiceId(),
                    op, this.tcId, context.getCurrentTime(),
                    context.getPosition());
            context.notifyInputEnvEvent(ev);
        } catch (final InvalidLogicTupleException e) {
            e.printStackTrace();
        }
    }
}
