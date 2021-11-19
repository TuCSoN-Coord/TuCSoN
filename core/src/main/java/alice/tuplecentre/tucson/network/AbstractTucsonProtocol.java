/*
 * Copyright (C) 2001-2002 aliCE team at deis.unibo.it This library is free
 * software; you can redistribute it and/or modify it under the terms copyOf the GNU
 * Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 copyOf the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty copyOf MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy copyOf the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package alice.tuplecentre.tucson.network;

import java.io.IOException;
import java.util.Properties;

import alice.tuplecentre.tucson.network.exceptions.DialogReceiveException;
import alice.tuplecentre.tucson.network.exceptions.DialogSendException;
import alice.tuplecentre.tucson.service.ACCDescription;
import alice.tuplecentre.tucson.service.TucsonInfo;

/**
 * @author Alessandro Ricci
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 * @author (contributor) Saverio Cicora
 */
public abstract class AbstractTucsonProtocol implements TucsonProtocol {

    /**
     * Code for isInstalled() query
     */
    public static final int NODE_ACTIVE_QUERY = 2;
    private static final int REQ_ENTERCONTEXT = 1;
    /**
     * serialVersionUID
     **/
    private static final long serialVersionUID = 1L;
    private ACCDescription context;
    private boolean reqAllowed;
    private int reqType;

    @Override
    public ACCDescription getContextDescription() {
        return this.context;
    }

    @Override
    public boolean isEnterRequest() {
        return this.reqType == AbstractTucsonProtocol.REQ_ENTERCONTEXT;
    }

    @Override
    public boolean isEnterRequestAccepted() {
        return this.reqAllowed;
    }

    @Override
    public boolean isNodeActiveQuery() {
        return this.reqType == AbstractTucsonProtocol.NODE_ACTIVE_QUERY;
    }

    @Override
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
        } catch (final IOException e) {
            throw new DialogReceiveException(e);
        }
    }

    @Override
    public void receiveEnterRequestAnswer() throws DialogReceiveException {
        try {
            this.reqAllowed = this.receiveBoolean();
        } catch (final IOException e) {
            throw new DialogReceiveException(e);
        }
    }

    @Override
    public void receiveFirstRequest() throws DialogReceiveException {
        try {
            this.reqType = this.receiveInt();
        } catch (final IOException e) {
            throw new DialogReceiveException(e);
        }
    }

    @Override
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

    @Override
    public void sendEnterRequestAccepted() throws DialogSendException {
        try {
            this.send(true);
            this.flush();
        } catch (final IOException e) {
            throw new DialogSendException(e);
        }
    }

    @Override
    public void sendEnterRequestRefused() throws DialogSendException {
        try {
            this.send(false);
            this.flush();
        } catch (final IOException e) {
            throw new DialogSendException(e);
        }
    }

    @Override
    public void sendNodeActiveReply() throws DialogSendException {
        try {
            this.send(TucsonInfo.getVersion());
            this.flush();
        } catch (final IOException e) {
            throw new DialogSendException(e);
        }
    }

    /**
     * @throws IOException if some network problems arise
     */
    protected abstract void flush() throws IOException;

    /**
     * @return the Java boolean value received
     * @throws IOException if some network problems arise
     */
    protected abstract boolean receiveBoolean() throws IOException;

    /**
     * @return the Java int value received
     * @throws IOException if some network problems arise
     */
    protected abstract int receiveInt() throws IOException;

    /**
     * @return the Java object received
     * @throws ClassNotFoundException if the received object's class cannot be found
     * @throws IOException            if some network problems arise
     */
    protected abstract Object receiveObject() throws ClassNotFoundException,
            IOException;

    /**
     * @return the Java string received
     * @throws IOException            if some network problems arise
     */
    protected abstract String receiveString() throws
            IOException;

    /**
     * @param value the Jaba boolean value to send
     * @throws IOException if some network problems arise
     */
    protected abstract void send(final boolean value) throws IOException;

    /**
     * @param value the Java byte array to send
     * @throws IOException if some network problems arise
     */
    protected abstract void send(final byte[] value) throws IOException;

    /**
     * @param value the Java int value to send
     * @throws IOException if some network problems arise
     */
    protected abstract void send(final int value) throws IOException;

    /**
     * @param value the Java object to send
     * @throws IOException if some network problems arise
     */
    protected abstract void send(final Object value) throws IOException;

    /**
     * @param value the Java String to send
     * @throws IOException if some network problems arise
     */
    protected abstract void send(final String value) throws IOException;

}
