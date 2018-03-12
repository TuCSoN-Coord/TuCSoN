package alice.tuplecentre.tucson.network.messages.introspection;

import alice.tuplecentre.tucson.api.TucsonAgentId;
import alice.tuplecentre.tucson.network.messages.Message;

/**
 * TODO add documentation and change the class name to a more evocative
 *
 * @author Enrico Siboni
 */
public interface NodeMessage extends Message {

    /**
     * @return the action
     */
    String getAction();

    /**
     * @param a the action to set
     */
    void setAction(final String a);

    /**
     * @return the AgentIdentifier
     */
    TucsonAgentId getAgentIdentifier();

    /**
     * @param id the aid to set
     */
    void setAgentIdentifier(final TucsonAgentId id);
}
