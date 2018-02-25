/*
 * Copyright (C) 2001-2002 aliCE team at deis.unibo.it This library is free
 * software; you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package alice.tucson.network;

import java.io.IOException;
import java.util.Properties;

import alice.tucson.introspection.InspectorContextEvent;
import alice.tucson.introspection.NewInspectorMsg;
import alice.tucson.introspection.NodeMsg;
import alice.tucson.network.exceptions.DialogAcceptException;
import alice.tucson.network.exceptions.DialogCloseException;
import alice.tucson.network.exceptions.DialogReceiveException;
import alice.tucson.network.exceptions.DialogSendException;
import alice.tucson.service.ACCDescription;
import alice.tucson.service.TucsonInfo;

/**
 *
 * @author Alessandro Ricci
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 * @author (contributor) Saverio Cicora
 *
 */
public abstract class AbstractTucsonProtocol implements java.io.Serializable {

    /** Code for isInstalled() query */
    public static final int NODE_ACTIVE_QUERY = 2;
    private static final int REQ_ENTERCONTEXT = 1;
    /** serialVersionUID **/
    private static final long serialVersionUID = 1L;
    private ACCDescription context;
    private boolean reqAllowed;
    private int reqType;

    /**
     *
     * @return the protocol to be used for interacting with TuCSoN
     *
     * @throws DialogAcceptException
     *             if something goes wrong in the underlying network
     */
    public abstract AbstractTucsonProtocol acceptNewDialog()
            throws DialogAcceptException;

    /**
     * @throws DialogCloseException
     *             if something goes wrong in the underlying network
     */
    public abstract void end() throws DialogCloseException;

    /**
     *
     * @return the ACC profile associated to this protocol
     */
    public ACCDescription getContextDescription() {
        return this.context;
    }

    /*
     * public abstract ObjectInputStream getInputStream(); public abstract
     * ObjectOutputStream getOutputStream();
     */
    /**
     *
     * @return wether the received request is an ACC acquisition request
     */
    public boolean isEnterRequest() {
        return this.reqType == AbstractTucsonProtocol.REQ_ENTERCONTEXT;
    }

    /**
     *
     * @return wether the ACC acquisition requested has been accepted or not
     */
    public boolean isEnterRequestAccepted() {
        return this.reqAllowed;
    }

    /**
     * @return wether the request received last is a "NODE_ACTIVE_QUERY" query
     */
    public boolean isNodeActiveQuery() {
        return this.reqType == AbstractTucsonProtocol.NODE_ACTIVE_QUERY;
    }

    /**
     * @throws DialogReceiveException
     *             if something goes wrong in the underlying network
     */
    public void receiveEnterRequest() throws DialogReceiveException {
        try {
            final String agentName = this.receiveString();
            final String agentRole = this.receiveString();
            final String agentUUID = this.receiveString(); // BUCCELLI
            final String tcName = this.receiveString();
            final Properties profile = new Properties();
            if (agentName.startsWith("'@'")) {
                profile.setProperty("tc-identity", agentName);
            } else {
                profile.setProperty("agent-identity", agentName);
            }
            profile.setProperty("agent-role", agentRole);
            profile.setProperty("agent-uuid", agentUUID);
            profile.setProperty("tuple-centre", tcName);
            this.context = new ACCDescription(profile);
        } catch (final ClassNotFoundException e) {
            throw new DialogReceiveException(e);
        } catch (final IOException e) {
            throw new DialogReceiveException(e);
        }
    }

    /**
     *
     * @throws DialogReceiveException
     *             if something goes wrong in the underlying network
     */
    public void receiveEnterRequestAnswer() throws DialogReceiveException {
        try {
            this.reqAllowed = this.receiveBoolean();
        } catch (final IOException e) {
            throw new DialogReceiveException(e);
        }
    }

    /**
     *
     * @throws DialogReceiveException
     *             if something goes wrong in the underlying network
     */
    public void receiveFirstRequest() throws DialogReceiveException {
        try {
            this.reqType = this.receiveInt();
        } catch (final IOException e) {
            throw new DialogReceiveException(e);
        }
    }

    /**
     *
     * @return the Inspector event received over the network
     * @throws DialogReceiveException
     *             if something goes wrong in the underlying network
     */
    public abstract InspectorContextEvent receiveInspectorEvent()
            throws DialogReceiveException;

    /**
     *
     * @return the Inspector message received over the network
     * @throws DialogReceiveException
     *             if something goes wrong in the underlying network
     */
    public abstract NewInspectorMsg receiveInspectorMsg()
            throws DialogReceiveException;

    /**
     *
     * @return the TuCSoN message received over the network
     * @throws DialogReceiveException
     *             if something goes wrong in the underlying network
     */
    public abstract TucsonMsg receiveMsg() throws DialogReceiveException;

    /**
     *
     * @return the TuCSoN message reply event received over the network
     * @throws DialogReceiveException
     *             if something goes wrong in the underlying network
     */
    public abstract TucsonMsgReply receiveMsgReply()
            throws DialogReceiveException;

    /**
     *
     * @return the TuCSoN message request received over the network
     * @throws DialogReceiveException
     *             if something goes wrong in the underlying network
     */
    public abstract TucsonMsgRequest receiveMsgRequest()
            throws DialogReceiveException;

    /**
     *
     * @return the node message received over the network
     * @throws DialogReceiveException
     *             if something goes wrong in the underlying network
     */
    public abstract NodeMsg receiveNodeMsg() throws DialogReceiveException;

    /**
     *
     * @param ctx
     *            the ACC profile to be associated to this protocol
     * @throws DialogSendException
     *             if something goes wrong in the underlying network
     */
    public void sendEnterRequest(final ACCDescription ctx)
            throws DialogSendException {
        try {
            this.send(AbstractTucsonProtocol.REQ_ENTERCONTEXT);
            String agentName = ctx.getProperty("agent-identity");
            if (agentName == null) {
                agentName = ctx.getProperty("tc-identity");
                if (agentName == null) {
                    agentName = "anonymous";
                }
            }
            this.send(agentName);
            String agentProfile = ctx.getProperty("agent-role");
            if (agentProfile == null) {
                agentProfile = "default";
            }
            this.send(agentProfile);
            String agentUUID = ctx.getProperty("agent-uuid");
            if (agentUUID == null) {
                agentUUID = "defaultUUID";
            }
            this.send(agentUUID);
            String tcName = ctx.getProperty("tuple-centre");
            if (tcName == null) {
                tcName = "_";
            }
            this.send(tcName);
            this.flush();
        } catch (final IOException e) {
            throw new DialogSendException(e);
        }
    }

    /**
     *
     * @throws DialogSendException
     *             if something goes wrong in the underlying network
     */
    public void sendEnterRequestAccepted() throws DialogSendException {
        try {
            this.send(true);
            this.flush();
        } catch (final IOException e) {
            throw new DialogSendException(e);
        }
    }

    /**
     *
     * @throws DialogSendException
     *             if something goes wrong in the underlying network
     */
    public void sendEnterRequestRefused() throws DialogSendException {
        try {
            this.send(false);
            this.flush();
        } catch (final IOException e) {
            throw new DialogSendException(e);
        }
    }

    /**
     *
     * @param msg
     *            the message to send over the network
     * @throws DialogSendException
     *             if something goes wrong in the underlying network
     */
    public abstract void sendInspectorEvent(InspectorContextEvent msg)
            throws DialogSendException;

    /**
     *
     * @param msg
     *            the message to send over the network
     * @throws DialogSendException
     *             if something goes wrong in the underlying network
     */
    public abstract void sendInspectorMsg(NewInspectorMsg msg)
            throws DialogSendException;

    /**
     *
     * @param msg
     *            the message to send over the network
     * @throws DialogSendException
     *             if something goes wrong in the underlying network
     */
    public abstract void sendMsg(TucsonMsg msg) throws DialogSendException;

    /**
     *
     * @param reply
     *            the message to send over the network
     * @throws DialogSendException
     *             if something goes wrong in the underlying network
     */
    public abstract void sendMsgReply(TucsonMsgReply reply)
            throws DialogSendException;

    /**
     *
     * @param request
     *            the message to send over the network
     * @throws DialogSendException
     *             if something goes wrong in the underlying network
     */
    public abstract void sendMsgRequest(TucsonMsgRequest request)
            throws DialogSendException;

    /**
     * @throws DialogSendException
     *             if something goes wrong in the underlying network
     */
    public void sendNodeActiveReply() throws DialogSendException {
        try {
            this.send(TucsonInfo.getVersion());
            this.flush();
        } catch (final IOException e) {
            throw new DialogSendException(e);
        }
    }

    /**
     *
     * @param msg
     *            the message to send over the network
     * @throws DialogSendException
     *             if something goes wrong in the underlying network
     */
    public abstract void sendNodeMsg(NodeMsg msg) throws DialogSendException;

    /**
     *
     * @throws IOException
     *             if some network problems arise
     */
    protected abstract void flush() throws IOException;

    /**
     *
     * @return the Java boolean value received
     * @throws IOException
     *             if some network problems arise
     */
    protected abstract boolean receiveBoolean() throws IOException;

    /**
     *
     * @return the Java int value received
     * @throws IOException
     *             if some network problems arise
     */
    protected abstract int receiveInt() throws IOException;

    /**
     *
     * @return the Java object received
     * @throws ClassNotFoundException
     *             if the received object's class cannot be found
     * @throws IOException
     *             if some network problems arise
     */
    protected abstract Object receiveObject() throws ClassNotFoundException,
    IOException;

    /**
     *
     * @return the Java string received
     * @throws ClassNotFoundException
     *             if the received object's class cannot be found
     * @throws IOException
     *             if some network problems arise
     */
    protected abstract String receiveString() throws ClassNotFoundException,
    IOException;

    /**
     *
     * @param value
     *            the Jaba boolean value to send
     * @throws IOException
     *             if some network problems arise
     */
    protected abstract void send(boolean value) throws IOException;

    /**
     *
     * @param value
     *            the Java byte array to send
     * @throws IOException
     *             if some network problems arise
     */
    protected abstract void send(byte[] value) throws IOException;

    /**
     *
     * @param value
     *            the Java int value to send
     * @throws IOException
     *             if some network problems arise
     */
    protected abstract void send(int value) throws IOException;

    /**
     *
     * @param value
     *            the Java object to send
     * @throws IOException
     *             if some network problems arise
     */
    protected abstract void send(Object value) throws IOException;

    /**
     *
     * @param value
     *            the Java String to send
     * @throws IOException
     *             if some network problems arise
     */
    protected abstract void send(String value) throws IOException;

}
