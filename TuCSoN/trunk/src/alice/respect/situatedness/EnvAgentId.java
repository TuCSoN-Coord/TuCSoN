package alice.respect.situatedness;

import alice.tucson.api.TucsonAgentId;
import alice.tucson.api.exceptions.TucsonInvalidAgentIdException;

/**
 * Environmental Agent Identifier. This particular agent is used internally by
 * the TuCSoN machinery to dynamically handle transducers and resources
 * (de)registration. As such, it is both an agent as well as an environmental
 * resource.
 * 
 * @author Steven Maraldi
 * 
 */
public class EnvAgentId extends TucsonAgentId {

    /** serialVersionUID **/
    private static final long serialVersionUID = 1L;

    /**
     * 
     * @param aid
     *            the String representation of the environmental agent
     *            identifier
     * @throws TucsonInvalidAgentIdException
     *             if the given String does not represent a valid TuCSoN agent
     *             identifier
     */
    public EnvAgentId(final String aid) throws TucsonInvalidAgentIdException {
        super(aid);
    }

    @Override
    public boolean isAgent() {
        return true;
    }

    @Override
    public boolean isEnv() {
        return true;
    }

    @Override
    public boolean isTC() {
        return false;
    }

}
