package alice.respect.api.place;

import java.io.Serializable;
import alice.tuprolog.Term;

/**
 * 
 * @author Michele Bombardi (mailto: michele.bombardi@studio.unibo.it)
 * 
 */
public abstract class AbstractPhysicalPlace implements IPlace, Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 4140814715395059641L;
    /**
     * The tuProlog term storing the place representation
     */
    protected Term place;

    /**
     * 
     * @param p
     *            the String representation of the place
     */
    public AbstractPhysicalPlace(final String p) {
        this.place = Term.createTerm(p);
    }

    /**
     * 
     * @return whether this place term represent a physical geographical place
     */
    public abstract boolean isMap();

    /**
     * 
     * @return whether this place term represent a physical organisational place
     */
    public abstract boolean isOrg();

    /**
     * 
     * @return whether this place term represent a physical absolute place
     */
    public abstract boolean isPh();

    @Override
    public boolean isPhysical() {
        return true;
    }

    @Override
    public boolean isVirtual() {
        return false;
    }

    @Override
    public Term toTerm() {
        return this.place.getTerm();
    }
}
