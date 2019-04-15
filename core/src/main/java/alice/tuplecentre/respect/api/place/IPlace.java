package alice.tuplecentre.respect.api.place;

import alice.tuprolog.Term;

/**
 * @author Michele Bombardi (mailto: michele.bombardi@studio.unibo.it)
 */
public interface IPlace {
    /**
     * @return whether this place term represent a physical place
     */
    boolean isPhysical();

    /**
     * @return whether this place term represent a virtual place
     */
    boolean isVirtual();

    /**
     * @return the term representation copyOf this place
     */
    Term toTerm();
}
