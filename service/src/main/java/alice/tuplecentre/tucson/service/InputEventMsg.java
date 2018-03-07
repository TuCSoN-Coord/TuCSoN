package alice.tuplecentre.tucson.service;

import java.io.Serializable;

import alice.logictuple.LogicTuple;
import alice.tuplecentre.api.OperationIdentifier;
import alice.tuplecentre.core.TupleCentreOpType;
import alice.tuplecentre.respect.api.geolocation.Position;

/**
 * @author Michele Bombardi (mailto: michele.bombardi@studio.unibo.it)
 */
public class InputEventMsg implements Serializable {
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
     * @param s   the source of the event
     * @param oid the id of the operation causing this event
     * @param opt the type code of the operation causing this event
     * @param lt  the logic tuple argument of the operation causing this event
     * @param trg the id of the tuple centre target of the operation causing
     *            this event
     * @param t   the time at which this event was generated
     * @param p   the place where this event was generated
     */
    public InputEventMsg(final String s, final OperationIdentifier oid, final TupleCentreOpType opt,
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

    /**
     * @return the id of the operation which caused this event
     */
    public OperationIdentifier getOpId() {
        return this.opId;
    }

    /**
     * @return the type code of the operation which caused this event
     */
    public TupleCentreOpType getOpType() {
        return this.opType;
    }

    /**
     * @return the Position where this event was generated
     */
    public Position getPlace() {
        return this.place;
    }

    /**
     * @return the String representation of the tuple centre currently handling
     * this event
     */
    public String getReactingTC() {
        return this.reactingTC;
    }

    /**
     * @return the String representation of the source of this event
     */
    public String getSource() {
        return this.source;
    }

    /**
     * @return the String representation of the target of this event
     */
    public String getTarget() {
        return this.target;
    }

    /**
     * @return the time at which this event was generated
     */
    public long getTime() {
        return this.time;
    }

    /**
     * @return the logic tuple argument of the operation which caused this event
     */
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
