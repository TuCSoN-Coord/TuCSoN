package alice.tuplecentre.api;

/**
 * Represents an operation emitter's interface: it could be both an agent of a
 * tuple centre
 *
 * @author Matteo Casadei
 *
 */
public interface EmitterIdentifier {

    /**
     *
     * @return wether this identifier is an agent identifier
     */
    boolean isAgent();

    /**
     *
     * @return wether this identifier is an environmental resource identifier
     */
    boolean isEnv();
    
    /**
     * 
     * @return wether this identifier is a geolocation service identifier
     */
    boolean isGeo();

    /**
     *
     * @return wether this identifier is a tuple centre identifier
     */
    boolean isTC();
}
