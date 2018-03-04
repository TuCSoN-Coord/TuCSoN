package alice.respect.api;


import alice.tuplecentre.api.EmitterIdentifier;
import alice.tuprolog.Term;

/**
 * Environmental resource identifier
 *
 * @author Nicola Piscaglia
 */
public interface EnvironmentIdentifier extends EmitterIdentifier {
    /**
     * @return the String representation of the local name of an environmental
     * resource
     */
    String getLocalName();

    /**
     * @return the term representation of this identifier
     */
    Term toTerm();
}
