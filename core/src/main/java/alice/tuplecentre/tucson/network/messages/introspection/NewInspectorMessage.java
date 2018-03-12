package alice.tuplecentre.tucson.network.messages.introspection;

import alice.tuplecentre.tucson.introspection.InspectorProtocol;

/**
 * // TODO: 12/03/2018 add documentation
 *
 * @author Enrico Siboni
 */
public interface NewInspectorMessage extends NodeMessage {

    /**
     * @return the info
     */
    InspectorProtocol getInfo();

    /**
     * @param i the info to set
     */
    void setInfo(final InspectorProtocol i);

    /**
     * @return the tcName
     */
    String getTcName();

    /**
     * @param tcn the tcName to set
     */
    void setTcName(final String tcn);
}
