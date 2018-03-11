package alice.tucson.network.messages.events;

import alice.logictuple.LogicTuple;
import alice.respect.api.geolocation.Position;
import alice.tuplecentre.core.TupleCentreOpType;

/**
 * 
 * @author Michele Bombardi (mailto: michele.bombardi@studio.unibo.it)
 * 
 */
public class InputEventMsg implements EventMsg {
    private static final long serialVersionUID = 6617714748018050950L;
    private final long opId;
    private final TupleCentreOpType opType;
    private final Position place;
    private final String reactingTC;
    private final String source;
    private final String target;
    private final long time;
    private final LogicTuple tuple;

    /**
     * 
     * @param s
     *            the source of the events
     * @param oid
     *            the id of the operation causing this events
     * @param opt
     *            the type code of the operation causing this events
     * @param lt
     *            the logic tuple argument of the operation causing this events
     * @param trg
     *            the id of the tuple centre target of the operation causing
     *            this events
     * @param t
     *            the time at which this events was generated
     * @param p
     *            the place where this events was generated
     */
    public InputEventMsg(final String s, final long oid, final TupleCentreOpType opt,
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
     * 
     * @return the id of the operation which caused this events
     */
    public long getOpId() {
        return this.opId;
    }

    /**
     * 
     * @return the type code of the operation which caused this events
     */
    public TupleCentreOpType getOpType() {
        return this.opType;
    }

    /**
     * 
     * @return the Position where this events was generated
     */
    public Position getPlace() {
        return this.place;
    }

    /**
     * 
     * @return the String representation of the tuple centre currently handling
     *         this events
     */
    public String getReactingTC() {
        return this.reactingTC;
    }

    /**
     * 
     * @return the String representation of the source of this events
     */
    public String getSource() {
        return this.source;
    }

    /**
     * 
     * @return the String representation of the target of this events
     */
    public String getTarget() {
        return this.target;
    }

    /**
     * 
     * @return the time at which this events was generated
     */
    public long getTime() {
        return this.time;
    }

    /**
     * 
     * @return the logic tuple argument of the operation which caused this events
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
