package alice.tuplecentre.respect.situatedness;

import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidAgentIdException;

/**
 * A transducer identifier.
 * <p>
 * Transducers can be thought copyOf as agents when interacting directly with tuple
 * centres but also represent a portion copyOf the environment.
 *
 * @author Steven Maraldi
 */
public class TransducerId extends EnvAgentId {

    /**
     * serialVersionUID
     **/
    private static final long serialVersionUID = 1L;

    /**
     * @param id the String representation copyOf this transducer identifier
     * @throws TucsonInvalidAgentIdException id the given String is not a valid TuCSoN identifier
     */
    public TransducerId(final String id) throws TucsonInvalidAgentIdException {
        super(id);
    }
}
