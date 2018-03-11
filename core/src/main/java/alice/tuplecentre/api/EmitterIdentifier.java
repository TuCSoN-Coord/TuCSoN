package alice.tuplecentre.api;

import alice.tuprolog.Term;

/**
 * Represents an operation emitter's interface: it could be both an agent of a
 * tuple centre
 *
 * @author Matteo Casadei
 */
public interface EmitterIdentifier extends Identifier {

    /**
     * @return wether this identifier is an agent identifier
     */
    boolean isAgent();

    /**
     * @return wether this identifier is an environmental resource identifier
     */
    boolean isEnv();

    /**
     * @return wether this identifier is a geolocation service identifier
     */
    boolean isGeo();

    /**
     * @return wether this identifier is a tuple centre identifier
     */
    boolean isTC();

    /**
     * @return the string representation of the local name of the identified emitter
     */
    String getLocalName();

    /**
     * Provides the logic term representation of the identifier of the emitter
     *
     * @return the {@link Term} representing the identifier
     */
    Term toTerm();
}
