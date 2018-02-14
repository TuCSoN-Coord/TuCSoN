package alice.respect.situatedness;

import alice.tucson.api.exceptions.TucsonInvalidAgentIdException;

/**
 * A transducer identifier.
 *
 * Transducers can be thought of as agents when interacting directly with tuple
 * centres but also represent a portion of the environment.
 *
 * @author Steven Maraldi
 *
 */
public class TransducerId extends EnvAgentId {

    /** serialVersionUID **/
    private static final long serialVersionUID = 1L;

    /**
     *
     * @param id
     *            the String representation of this transducer identifier
     * @throws TucsonInvalidAgentIdException
     *             id the given String is not a valid TuCSoN identifier
     */
    public TransducerId(final String id) throws TucsonInvalidAgentIdException {
        super(id);
    }
}
