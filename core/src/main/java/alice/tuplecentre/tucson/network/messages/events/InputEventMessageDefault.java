package alice.tuplecentre.tucson.network.messages.events;

import alice.tuple.logic.LogicTuple;
import alice.tuplecentre.api.OperationIdentifier;
import alice.tuplecentre.core.TupleCentreOpType;
import alice.tuplecentre.respect.api.geolocation.Position;

/**
 * //TODO add description
 *
 * @author Michele Bombardi (mailto: michele.bombardi@studio.unibo.it)
 */
public class InputEventMessageDefault extends AbstractEventMessage implements InputEventMessage {

    private final Position place;
    private final String reactingTC;
    private final String source;
    private final String target;
    private final long time;

    /**
     * @param s   the source of the events
     * @param oid the id of the operation causing this events
     * @param opt the type code of the operation causing this events
     * @param lt  the logic tuple argument of the operation causing this events
     * @param trg the id of the tuple centre target of the operation causing
     *            this events
     * @param t   the time at which this events was generated
     * @param p   the place where this events was generated
     */
    public InputEventMessageDefault(final String s, final OperationIdentifier oid, final TupleCentreOpType opt,
                                    final LogicTuple lt, final String trg, final long t,
                                    final Position p) {
        super(opt, lt, oid);
        this.source = s;
        this.target = trg;
        this.reactingTC = trg;
        this.time = t;
        this.place = p;
    }

    @Override
    public Position getPlace() {
        return this.place;
    }

    @Override
    public String getReactingTC() {
        return this.reactingTC;
    }

    @Override
    public String getSource() {
        return this.source;
    }

    @Override
    public String getTarget() {
        return this.target;
    }

    @Override
    public long getTime() {
        return this.time;
    }

    @Override
    public String toString() {
        return "[ src: " + this.getSource() + ", " + "op: " + "( " + getOpId()
                + "," + getOpType() + " ), " + "trg: " + this.getTarget()
                + ", " + "tc: " + this.getReactingTC() + " ]";
    }
}
