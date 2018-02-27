package alice.tucson.introspection;

import java.io.Serializable;
import alice.tuple.logic.LogicTuple;
import alice.tuplecentre.api.IId;

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
    private IId source;
    private IId target;

    /**
     *
     * @param lt
     *            the tuple representing the event
     * @param s
     *            the identifier of the event source
     * @param t
     *            the identifier of the event target
     */
    public WSetEvent(final LogicTuple lt, final IId s, final IId t) {
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
    public IId getSource() {
        return this.source;
    }

    /**
     * @return the target
     */
    public IId getTarget() {
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
    public void setSource(final IId s) {
        this.source = s;
    }

    /**
     * @param t
     *            the target to set
     */
    public void setTarget(final IId t) {
        this.target = t;
    }
}
