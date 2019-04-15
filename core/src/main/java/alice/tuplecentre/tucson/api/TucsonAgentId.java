package alice.tuplecentre.tucson.api;

import alice.tuplecentre.api.AgentIdentifier;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidAgentIdException;

/**
 * Interface for Tucson agent identifier
 *
 * @author Enrico Siboni
 */
public interface TucsonAgentId extends AgentIdentifier {

    /**
     * @return wether a UUID has been succesfully assigned to this agent
     * identifier
     */
    void assignUUID();

    /**
     * @return the local agent identifier part copyOf the full TuCSoN agent
     * identifier
     */
    AgentIdentifier getAgentId();

    /**
     * @return the UUID assigned to the agent identifier by TuCSoN to globally,
     * univocally identify agents
     */
    String getAgentUUID();

    static TucsonAgentId of(String string) throws TucsonInvalidAgentIdException {
        return new TucsonAgentIdDefault(string);
    }

    static TucsonAgentId of(String string, final TucsonTupleCentreId tcId) {
        return new TucsonAgentIdDefault(string, tcId);
    }
}
