package alice.tuplecentre.tucson.network;

import java.io.Serializable;

import alice.tuplecentre.tucson.introspection.InspectorContextEvent;
import alice.tuplecentre.tucson.network.exceptions.DialogAcceptException;
import alice.tuplecentre.tucson.network.exceptions.DialogCloseException;
import alice.tuplecentre.tucson.network.exceptions.DialogReceiveException;
import alice.tuplecentre.tucson.network.exceptions.DialogSendException;
import alice.tuplecentre.tucson.network.messages.TucsonMessage;
import alice.tuplecentre.tucson.network.messages.TucsonMessageReply;
import alice.tuplecentre.tucson.network.messages.TucsonMessageRequest;
import alice.tuplecentre.tucson.network.messages.introspection.AbstractNodeMessage;
import alice.tuplecentre.tucson.network.messages.introspection.NewInspectorMsg;
import alice.tuplecentre.tucson.service.ACCDescription;

/**
 * Interface that specifies TucsonProtocol
 * <p>
 * // TODO review this documentation, written on the fly
 *
 * @author Enrico Siboni
 */
public interface TucsonProtocol extends Serializable {

    /**
     * @return the protocol to be used for interacting with TuCSoN
     * @throws DialogAcceptException if something goes wrong in the underlying network
     */
    TucsonProtocol acceptNewDialog() throws DialogAcceptException;

    /**
     * @throws DialogCloseException if something goes wrong in the underlying network
     */
    void end() throws DialogCloseException;

    /**
     * @return the ACC profile associated to this protocol
     */
    ACCDescription getContextDescription();

    /**
     * @return wether the received request is an ACC acquisition request
     */
    boolean isEnterRequest();

    /**
     * @return wether the ACC acquisition requested has been accepted or not
     */
    boolean isEnterRequestAccepted();

    /**
     * @return wether the request received last is a "NODE_ACTIVE_QUERY" query
     */
    boolean isNodeActiveQuery();

    /**
     * @throws DialogReceiveException if something goes wrong in the underlying network
     */
    void receiveEnterRequest() throws DialogReceiveException;

    /**
     * @throws DialogReceiveException if something goes wrong in the underlying network
     */
    void receiveEnterRequestAnswer() throws DialogReceiveException;

    /**
     * @throws DialogReceiveException if something goes wrong in the underlying network
     */
    void receiveFirstRequest() throws DialogReceiveException;

    /**
     * @return the Inspector event received over the network
     * @throws DialogReceiveException if something goes wrong in the underlying network
     */
    InspectorContextEvent receiveInspectorEvent() throws DialogReceiveException;

    /**
     * @return the Inspector message received over the network
     * @throws DialogReceiveException if something goes wrong in the underlying network
     */
    NewInspectorMsg receiveInspectorMsg() throws DialogReceiveException;

    /**
     * @return the TuCSoN message received over the network
     * @throws DialogReceiveException if something goes wrong in the underlying network
     */
    TucsonMessage receiveMsg() throws DialogReceiveException;

    /**
     * @return the TuCSoN message reply event received over the network
     * @throws DialogReceiveException if something goes wrong in the underlying network
     */
    TucsonMessageReply receiveMsgReply() throws DialogReceiveException;

    /**
     * @return the TuCSoN message request received over the network
     * @throws DialogReceiveException if something goes wrong in the underlying network
     */
    TucsonMessageRequest receiveMsgRequest() throws DialogReceiveException;

    /**
     * @return the node message received over the network
     * @throws DialogReceiveException if something goes wrong in the underlying network
     */
    AbstractNodeMessage receiveNodeMsg() throws DialogReceiveException;

    /**
     * @param ctx the ACC profile to be associated to this protocol
     * @throws DialogSendException if something goes wrong in the underlying network
     */
    void sendEnterRequest(final ACCDescription ctx) throws DialogSendException;

    /**
     * @throws DialogSendException if something goes wrong in the underlying network
     */
    void sendEnterRequestAccepted() throws DialogSendException;

    /**
     * @throws DialogSendException if something goes wrong in the underlying network
     */
    void sendEnterRequestRefused() throws DialogSendException;

    /**
     * @param msg the message to send over the network
     * @throws DialogSendException if something goes wrong in the underlying network
     */
    void sendInspectorEvent(final InspectorContextEvent msg) throws DialogSendException;

    /**
     * @param msg the message to send over the network
     * @throws DialogSendException if something goes wrong in the underlying network
     */
    void sendInspectorMsg(final NewInspectorMsg msg) throws DialogSendException;

    /**
     * @param msg the message to send over the network
     * @throws DialogSendException if something goes wrong in the underlying network
     */
    void sendMsg(final TucsonMessage msg) throws DialogSendException;

    /**
     * @param reply the message to send over the network
     * @throws DialogSendException if something goes wrong in the underlying network
     */
    void sendMsgReply(final TucsonMessageReply reply) throws DialogSendException;

    /**
     * @param request the message to send over the network
     * @throws DialogSendException if something goes wrong in the underlying network
     */
    void sendMsgRequest(final TucsonMessageRequest request) throws DialogSendException;

    /**
     * @throws DialogSendException if something goes wrong in the underlying network
     */
    void sendNodeActiveReply() throws DialogSendException;

    /**
     * @param msg the message to send over the network
     * @throws DialogSendException if something goes wrong in the underlying network
     */
    void sendNodeMsg(final AbstractNodeMessage msg) throws DialogSendException;
}
