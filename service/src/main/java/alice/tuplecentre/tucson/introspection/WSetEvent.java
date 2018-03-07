package alice.tuplecentre.tucson.introspection;

import java.io.Serializable;

import alice.logictuple.LogicTuple;
import alice.tuplecentre.api.EmitterIdentifier;

/**
 *
 * @author Unknown...
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 *
 */
public class WSetEvent implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 9193318251500885501L;
    private LogicTuple op;
    private EmitterIdentifier source;
    private EmitterIdentifier target;

    /**
     *
     * @param lt
     *            the tuple representing the event
     * @param s
     *            the identifier of the event source
     * @param t
     *            the identifier of the event target
     */
    public WSetEvent(final LogicTuple lt, final EmitterIdentifier s, final EmitterIdentifier t) {
        this.op = lt;
        this.source = s;
        this.target = t;
    }

    /**
     * @return the op
     */
    public LogicTuple getOp() {
        return this.op;
    }

    /**
     * @return the source
     */
    public EmitterIdentifier getSource() {
        return this.source;
    }

    /**
     * @return the target
     */
    public EmitterIdentifier getTarget() {
        return this.target;
    }

    /**
     * @param o
     *            the op to set
     */
    public void setOp(final LogicTuple o) {
        this.op = o;
    }

    /**
     * @param s
     *            the source to set
     */
    public void setSource(final EmitterIdentifier s) {
        this.source = s;
    }

    /**
     * @param t
     *            the target to set
     */
    public void setTarget(final EmitterIdentifier t) {
        this.target = t;
    }
}
