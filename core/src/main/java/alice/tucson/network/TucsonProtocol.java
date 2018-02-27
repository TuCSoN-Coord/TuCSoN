package alice.tucson.network;

import java.io.Serializable;

import alice.tucson.network.exceptions.DialogAcceptException;
import alice.tucson.network.exceptions.DialogCloseException;
import alice.tucson.network.exceptions.DialogReceiveException;
import alice.tucson.network.exceptions.DialogSendException;
import alice.tucson.service.ACCDescription;

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
    TucsonMsg receiveMsg() throws DialogReceiveException;

    /**
     * @return the TuCSoN message reply event received over the network
     * @throws DialogReceiveException if something goes wrong in the underlying network
     */
    TucsonMsgReply receiveMsgReply() throws DialogReceiveException;

    /**
     * @return the TuCSoN message request received over the network
     * @throws DialogReceiveException if something goes wrong in the underlying network
     */
    TucsonMsgRequest receiveMsgRequest() throws DialogReceiveException;

    /**
     * @return the node message received over the network
     * @throws DialogReceiveException if something goes wrong in the underlying network
     */
    NodeMsg receiveNodeMsg() throws DialogReceiveException;

    /**
     * @param ctx the ACC profile to be associated to this protocol
     * @throws DialogSendException if something goes wrong in the underlying network
     */
    void sendEnterRequest(ACCDescription ctx) throws DialogSendException;

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
    void sendInspectorEvent(InspectorContextEvent msg) throws DialogSendException;

    /**
     * @param msg the message to send over the network
     * @throws DialogSendException if something goes wrong in the underlying network
     */
    void sendInspectorMsg(NewInspectorMsg msg) throws DialogSendException;

    /**
     * @param msg the message to send over the network
     * @throws DialogSendException if something goes wrong in the underlying network
     */
    void sendMsg(TucsonMsg msg) throws DialogSendException;

    /**
     * @param reply the message to send over the network
     * @throws DialogSendException if something goes wrong in the underlying network
     */
    void sendMsgReply(TucsonMsgReply reply)
            throws DialogSendException;

    /**
     * @param request the message to send over the network
     * @throws DialogSendException if something goes wrong in the underlying network
     */
    void sendMsgRequest(TucsonMsgRequest request)
            throws DialogSendException;

    /**
     * @throws DialogSendException if something goes wrong in the underlying network
     */
    void sendNodeActiveReply() throws DialogSendException;

    /**
     * @param msg the message to send over the network
     * @throws DialogSendException if something goes wrong in the underlying network
     */
    void sendNodeMsg(NodeMsg msg) throws DialogSendException;
}
