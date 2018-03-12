package alice.tuplecentre.tucson.network.messages.introspection;

import alice.tuplecentre.tucson.introspection.InspectorProtocol;

/**
 * // TODO: 12/03/2018 add documentation
 *
 * @author Enrico Siboni
 */
public interface SetProtocolMessage extends NodeMessage {

    // TODO: 12/03/2018 has something in common with NewInspectorMessage... look and refactor

    /**
     * @return the info
     */
    InspectorProtocol getInfo();

    /**
     * @param i the info to set
     */
    void setInfo(final InspectorProtocol i);
}
