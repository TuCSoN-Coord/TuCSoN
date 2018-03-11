package alice.tuplecentre.tucson.network.messages.events;

import alice.tuple.logic.LogicTuple;
import alice.tuplecentre.api.OperationIdentifier;
import alice.tuplecentre.core.TupleCentreOpType;
import alice.tuplecentre.respect.api.geolocation.Position;
import alice.tuplecentre.tucson.service.InputEventMsg;

/**
 * //TODO add description
 *
 * @author Michele Bombardi (mailto: michele.bombardi@studio.unibo.it)
 */
public class InputEventMsgDefault implements InputEventMsg , EventMsg {
    private static final long serialVersionUID = 6617714748018050950L;
    private final OperationIdentifier opId;
    private final TupleCentreOpType opType;
    private final Position place;
    private final String reactingTC;
    private final String source;
    private final String target;
    private final long time;
    private final LogicTuple tuple;

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
    public InputEventMsgDefault(final String s, final OperationIdentifier oid, final TupleCentreOpType opt,
                                final LogicTuple lt, final String trg, final long t,
                                final Position p) {
        this.source = s;
        this.opId = oid;
        this.opType = opt;
        this.tuple = lt;
        this.target = trg;
        this.reactingTC = trg;
        this.time = t;
        this.place = p;
    }

    @Override
    public OperationIdentifier getOpId() {
        return this.opId;
    }

    @Override
    public TupleCentreOpType getOpType() {
        return this.opType;
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
    public LogicTuple getTuple() {
        return this.tuple;
    }

    @Override
    public String toString() {
        return "[ src: " + this.getSource() + ", " + "op: " + "( " + this.opId
                + "," + this.opType + " ), " + "trg: " + this.getTarget()
                + ", " + "tc: " + this.getReactingTC() + " ]";
    }
}
