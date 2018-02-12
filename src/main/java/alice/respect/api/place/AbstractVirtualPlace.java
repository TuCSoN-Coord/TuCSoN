package alice.respect.api.place;

import java.io.Serializable;
import alice.tuprolog.Term;

/**
 * 
 * @author Michele Bombardi (mailto: michele.bombardi@studio.unibo.it)
 * 
 */
public abstract class AbstractVirtualPlace implements IPlace, Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -749526964492926104L;
    /**
     * The tuProlog Term representing a place of any sort
     */
    protected Term place;

    /**
     * 
     * @param p
     *            the String representation of the place
     */
    public AbstractVirtualPlace(final String p) {
        this.place = Term.createTerm(p);
    }

    /**
     * 
     * @return whether this place term represent a virtual relative place
     */
    public abstract boolean isDns();

    /**
     * 
     * @return whether this place term represent a virtual absolute place
     */
    public abstract boolean isIp();

    @Override
    public boolean isPhysical() {
        return false;
    }

    @Override
    public boolean isVirtual() {
        return true;
    }

    @Override
    public Term toTerm() {
        return this.place.getTerm();
    }
}
