package alice.respect.api.geolocation.service;

import alice.respect.api.ISpatialContext;
import alice.respect.api.place.IPlace;
import alice.respect.core.RespectOperationDefault;
import alice.respect.core.RespectTCContainer;
import alice.tucson.api.TucsonTupleCentreId;
import alice.tuple.logic.LogicTuple;
import alice.tuple.logic.LogicTuples;
import alice.tuple.logic.exceptions.InvalidLogicTupleException;
import alice.tuplecentre.core.InputEvent;
import alice.tuplecentre.core.TupleCentreOpType;

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
    public void moving(final TupleCentreOpType type, final String space, final IPlace place) {
        try {
            final ISpatialContext context = RespectTCContainer
                    .getRespectTCContainer().getSpatialContext(
                            this.tcId.getInternalTupleCentreId());
            LogicTuple tuple = null;
            RespectOperationDefault op = null;
            if (type == TupleCentreOpType.FROM) {
                tuple = LogicTuples.parse("from(" + space + "," + place.toTerm()
                        + ")");
                op = RespectOperationDefault.makeFrom(tuple, null);
            } else if (type == TupleCentreOpType.TO) {
                tuple = LogicTuples.parse("to(" + space + "," + place.toTerm()
                        + ")");
                op = RespectOperationDefault.makeTo(tuple, null);
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
