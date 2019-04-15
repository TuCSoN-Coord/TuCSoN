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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.invoke.MethodHandles;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import alice.tuplecentre.tucson.api.exceptions.UnreachableNodeException;
import alice.tuplecentre.tucson.introspection.InspectorContextEvent;
import alice.tuplecentre.tucson.network.exceptions.DialogAcceptException;
import alice.tuplecentre.tucson.network.exceptions.DialogCloseException;
import alice.tuplecentre.tucson.network.exceptions.DialogInitializationException;
import alice.tuplecentre.tucson.network.exceptions.DialogReceiveException;
import alice.tuplecentre.tucson.network.exceptions.DialogSendException;
import alice.tuplecentre.tucson.network.messages.TucsonMessage;
import alice.tuplecentre.tucson.network.messages.TucsonMessageReply;
import alice.tuplecentre.tucson.network.messages.TucsonMessageRequest;
import alice.tuplecentre.tucson.network.messages.introspection.NewInspectorMessage;
import alice.tuplecentre.tucson.network.messages.introspection.NodeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * TODO CICORA: e' necessario separare la classe usata server side e la classe
 * usata client side anche in vista di una separazione delle librerie agent-node
 */

/**
 * @author Alessandro Ricci
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 * @author (contributor) Saverio Cicora
 */
public class TucsonProtocolTCP extends AbstractTucsonProtocol {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * serialVersionUID
     **/
    private static final long serialVersionUID = 1L;
    private ObjectInputStream inStream;
    private ServerSocket mainSocket;
    private ObjectOutputStream outStream;
    private boolean serverSocketClosed;
    private Socket socket;

    /**
     * This constructor is typically used node side: it builds a new access
     * point to which an external agent can engage a new dialog. After the
     * creation copyOf this object usually is invoked the method acceptNewDialog()
     * <p>
     * It make a new ServerSocket binded at port specified by port parameter.
     *
     * @param port the listening port where to bind
     * @throws DialogInitializationException if something goes wrong in the udenrlying network
     */
    public TucsonProtocolTCP(final int port)
            throws DialogInitializationException {
        super();
        try {
            this.mainSocket = new ServerSocket();
            this.mainSocket.setReuseAddress(true);
            this.mainSocket.bind(new InetSocketAddress(port));
        } catch (final IOException e) {
            this.clean();
            throw new DialogInitializationException(e);
        }
    }

    /**
     * This constructor create a new dialog whit a specific host that identified
     * by host/port pair. This constructor is typically used from external agent
     * who want start a new dialogue with the node.
     * <p>
     * It make a new socket and init I/O streams. The streams are bufferized.
     *
     * @param host the host where to bound
     * @param port the listening port where to bound
     * @throws UnreachableNodeException      if the given host is unknown or no process is listening on
     *                                       the given port
     * @throws DialogInitializationException if some network problems arise
     */
    public TucsonProtocolTCP(final String host, final int port)
            throws UnreachableNodeException, DialogInitializationException {
        super();
        try {
            this.socket = new Socket(host, port);
        } catch (final UnknownHostException e) {
            throw new UnreachableNodeException("Host unknown", e);
        } catch (final ConnectException e) {
            throw new UnreachableNodeException("Connection refused", e);
        } catch (final IOException e) {
            throw new DialogInitializationException(e);
        }
        /*
         * To avoid deadlock: construct the output stream first, then flush it
         * before creating the input stream.
         */
        try {
            this.outStream = new ObjectOutputStream(new BufferedOutputStream(
                    this.socket.getOutputStream()));
            this.outStream.flush();
            this.inStream = new ObjectInputStream(new BufferedInputStream(
                    this.socket.getInputStream()));
        } catch (final IOException e) {
            throw new DialogInitializationException(e);
        }
    }

    /**
     * @param s the socket to bound
     * @throws DialogInitializationException if some network problems arise
     */
    private TucsonProtocolTCP(final Socket s)
            throws DialogInitializationException {
        super();
        this.socket = s;
        /*
         * To avoid deadlock: construct the output stream first, then flush it
         * before creating the input stream.
         */
        try {
            this.outStream = new ObjectOutputStream(new BufferedOutputStream(
                    this.socket.getOutputStream()));
            this.outStream.flush();
            this.inStream = new ObjectInputStream(new BufferedInputStream(
                    this.socket.getInputStream()));
        } catch (final IOException e) {
            throw new DialogInitializationException(e);
        }
    }

    @Override
    public TucsonProtocol acceptNewDialog()
            throws DialogAcceptException {
        try {
            return new TucsonProtocolTCP(this.mainSocket.accept());
        } catch (final IOException e) {
            // FIXME What to do here?
            if (this.serverSocketClosed) {
                LOGGER.warn("[TuCSoN protocol]: Socket "
                        + this.mainSocket.getLocalPort() + " closed");
            } else {
                LOGGER.error("Generic IO error: " + e);
            }
            throw new DialogAcceptException(e);
        } catch (final DialogInitializationException e) {
            throw new DialogAcceptException(e);
        }
    }

    @Override
    public void end() throws DialogCloseException {
        try {
            if (this.socket != null) {
                this.socket.close();
            }
            if (this.mainSocket != null) {
                this.mainSocket.close();
            }
            this.serverSocketClosed = true;
        } catch (final IOException e) {
            LOGGER.error("Generic IO error: " + e);
            throw new DialogCloseException(e);
        }
    }

    /*
     * (non-Javadoc)
     * @see alice.tuplecentre.tucson.network.AbstractTucsonProtocol#receiveInspectorEvent()
     */
    @Override
    public InspectorContextEvent receiveInspectorEvent()
            throws DialogReceiveException {
        InspectorContextEvent msg;
        try {
            msg = (InspectorContextEvent) this.inStream.readObject();
        } catch (final IOException | ClassNotFoundException e) {
            throw new DialogReceiveException(e);
        }
        return msg;
    }

    /*
     * (non-Javadoc)
     * @see alice.tuplecentre.tucson.network.AbstractTucsonProtocol#receiveInspectorMsg()
     */
    @Override
    public NewInspectorMessage receiveInspectorMsg() throws DialogReceiveException {
        NewInspectorMessage msg;
        try {
            msg = (NewInspectorMessage) this.inStream.readObject();
        } catch (final IOException | ClassNotFoundException e) {
            throw new DialogReceiveException(e);
        }
        return msg;
    }

    @Override
    public TucsonMessage receiveMsg() throws DialogReceiveException {
        TucsonMessage msg;
        try {
            msg = (TucsonMessage) this.inStream.readObject();
        } catch (final IOException | ClassNotFoundException e) {
            throw new DialogReceiveException(e);
        }
        return msg;
    }

    @Override
    public TucsonMessageReply receiveMsgReply() throws DialogReceiveException {
        TucsonMessageReply msg;
        try {
            msg = (TucsonMessageReply) this.inStream.readObject();
        } catch (final IOException | ClassNotFoundException e) {
            throw new DialogReceiveException(e);
        }
        return msg;
    }

    @Override
    public TucsonMessageRequest receiveMsgRequest() throws DialogReceiveException {
        TucsonMessageRequest msg;
        try {
            msg = (TucsonMessageRequest) this.inStream.readObject();
        } catch (final IOException | ClassNotFoundException e) {
            throw new DialogReceiveException(e);
        }
        return msg;
    }

    /*
     * (non-Javadoc)
     * @see alice.tuplecentre.tucson.network.AbstractTucsonProtocol#receiveNodeMsg()
     */
    @Override
    public NodeMessage receiveNodeMsg() throws DialogReceiveException {
        NodeMessage msg;
        try {
            msg = (NodeMessage) this.inStream.readObject();
        } catch (final IOException | ClassNotFoundException e) {
            throw new DialogReceiveException(e);
        }
        return msg;
    }

    /*
     * (non-Javadoc)
     * @see
     * alice.tuplecentre.tucson.network.AbstractTucsonProtocol#sendInspectorEvent(alice.
     * tucson.introspection.InspectorContextEventDefault)
     */
    @Override
    public void sendInspectorEvent(final InspectorContextEvent msg)
            throws DialogSendException {
        try {
            this.outStream.writeObject(msg);
            this.outStream.flush();
        } catch (final IOException e) {
            throw new DialogSendException(e);
        }
    }

    /*
     * (non-Javadoc)
     * @see
     * alice.tuplecentre.tucson.network.AbstractTucsonProtocol#sendInspectorMsg(alice.tuplecentre.tucson
     * .introspection.NewInspectorMessageDefault)
     */
    @Override
    public void sendInspectorMsg(final NewInspectorMessage msg)
            throws DialogSendException {
        try {
            this.outStream.writeObject(msg);
            this.outStream.flush();
        } catch (final IOException e) {
            throw new DialogSendException(e);
        }
    }

    @Override
    public void sendMsg(final TucsonMessage msg) throws DialogSendException {
        try {
            this.outStream.writeObject(msg);
            this.outStream.flush();
        } catch (final IOException e) {
            throw new DialogSendException(e);
        }
    }

    @Override
    public void sendMsgReply(final TucsonMessageReply reply)
            throws DialogSendException {
        try {
            this.outStream.writeObject(reply);
            this.outStream.flush();
        } catch (final IOException e) {
            throw new DialogSendException(e);
        }
    }

    @Override
    public void sendMsgRequest(final TucsonMessageRequest request)
            throws DialogSendException {
        try {
            this.outStream.writeObject(request);
            this.outStream.flush();
        } catch (final IOException e) {
            throw new DialogSendException(e);
        }
    }

    /*
     * (non-Javadoc)
     * @see
     * alice.tuplecentre.tucson.network.AbstractTucsonProtocol#sendNodeMsg(alice.tuplecentre.tucson.
     * introspection.AbstractNodeMessage)
     */
    @Override
    public void sendNodeMsg(final NodeMessage msg) throws DialogSendException {
        try {
            this.outStream.writeObject(msg);
            this.outStream.flush();
        } catch (final IOException e) {
            throw new DialogSendException(e);
        }
    }

    /**
     *
     */
    private void clean() {
        try {
            this.mainSocket.close();
        } catch (final IOException e) {
            // TODO Auto-generated catch block
            LOGGER.error(e.getMessage(), e);
        }
    }

    @Override
    protected void flush() throws IOException {
        this.outStream.flush();
    }

    @Override
    protected boolean receiveBoolean() throws IOException {
        return this.inStream.readBoolean();
    }

    @Override
    protected int receiveInt() throws IOException {
        return this.inStream.readInt();
    }

    @Override
    protected Object receiveObject() throws ClassNotFoundException, IOException {
        return this.inStream.readObject();
    }

    @Override
    protected String receiveString() throws IOException {
        return this.inStream.readUTF();
    }

    @Override
    protected void send(final boolean value) throws IOException {
        this.outStream.writeBoolean(value);
    }

    @Override
    protected void send(final byte[] value) throws IOException {
        this.outStream.write(value);
    }

    @Override
    protected void send(final int value) throws IOException {
        this.outStream.writeInt(value);
    }

    @Override
    protected void send(final Object value) throws IOException {
        this.outStream.writeObject(value);
    }

    @Override
    protected void send(final String value) throws IOException {
        this.outStream.writeUTF(value);
    }
}
