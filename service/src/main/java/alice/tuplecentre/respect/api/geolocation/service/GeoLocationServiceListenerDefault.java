package alice.tuplecentre.respect.api.geolocation.service;

import alice.tuple.logic.LogicTuple;
import alice.tuple.logic.LogicTuples;
import alice.tuple.logic.exceptions.InvalidLogicTupleException;
import alice.tuplecentre.core.InputEvent;
import alice.tuplecentre.core.TupleCentreOpType;
import alice.tuplecentre.respect.api.ISpatialContext;
import alice.tuplecentre.respect.api.place.IPlace;
import alice.tuplecentre.respect.core.RespectOperationDefault;
import alice.tuplecentre.respect.core.RespectTCContainer;
import alice.tuplecentre.tucson.api.TucsonTupleCentreId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

/**
 * This class represent the listener that listens for geolocation service
 * changes and implements the behavior in response to some admissible
 * geolocation events. This class is delegated to interface with tucson, giving
 * origin to "from" and "to" events.
 *
 * @author Michele Bombardi (mailto: michele.bombardi@studio.unibo.it)
 */
public class GeoLocationServiceListenerDefault implements GeoLocationServiceListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().getClass());

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
     * @param s    the service associated
     * @param ttci the associated tuple centre identifier
     */
    public GeoLocationServiceListenerDefault(final GeoLocationService s,
                                             final TucsonTupleCentreId ttci) {
        this.service = s;
        this.tcId = ttci;
    }

    @Override
    public GeoLocationService getService() {
        return this.service;
    }

    @Override
    public GeoServiceIdentifier getServiceId() {
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
            LogicTuple tuple;
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
            LOGGER.error(e.getMessage(), e);
        }
    }
}
