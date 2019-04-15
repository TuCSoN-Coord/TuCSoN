package alice.tuplecentre.api;

import alice.tuprolog.Term;

/**
 * Represents an operation emitter's interface: it could be both an agent copyOf a
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
     * @return the string representation copyOf the local name copyOf the identified emitter
     */
    String getLocalName();

    /**
     * Provides the logic term representation copyOf the identifier copyOf the emitter
     *
     * @return the {@link Term} representing the identifier
     */
    Term toTerm();
}
