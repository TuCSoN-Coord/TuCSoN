/*
 * Copyright (C) 2001-2002  aliCE team at deis.unibo.it
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package alice.tucson.api.util;

import alice.tucson.api.*;
import alice.tucson.api.util.TucsonProtocol;
import alice.logictuple.LogicTuple;
import java.util.*;
import java.io.*;
import java.net.*;

/**
 *
 */
public class TucsonProtocolTCP extends TucsonProtocol {

    private DataInputStream inStream;
    private DataOutputStream outStream;
    private Socket socket;
    private ServerSocket mainSocket;

    public TucsonProtocolTCP(String host,int port) throws Exception {
        this.socket=new Socket(host,port);
        inStream=new DataInputStream(socket.getInputStream());
        outStream=new DataOutputStream(socket.getOutputStream());
    }

    public TucsonProtocolTCP(ServerSocket socket){
        this.mainSocket=socket;
    }

    private TucsonProtocolTCP(Socket socket) throws Exception {
        this.socket=socket;
        inStream=new DataInputStream(socket.getInputStream());
        outStream=new DataOutputStream(socket.getOutputStream());
    }

    public DataInputStream getInputStream() {
        return inStream;
    }

    public DataOutputStream getOutputStream(){
        return outStream;
    }

    public TucsonProtocol acceptNewDialog() throws Exception {
        return new TucsonProtocolTCP(mainSocket.accept());
    }

    public void end() throws Exception {
        socket.close();
    }


    protected String receiveString() throws Exception {
        return inStream.readUTF();
    }

    protected int receiveInt() throws Exception {
        return inStream.readInt();
    }

    protected boolean receiveBoolean() throws Exception {
        return inStream.readBoolean();
    }

    protected void send(int value) throws Exception {
        outStream.writeInt(value);
    }

    protected void send(boolean value) throws Exception {
        outStream.writeBoolean(value);
    }

    protected void send(String value) throws Exception {
        outStream.writeUTF(value);
    }

    protected void flush() throws Exception {
        outStream.flush();
    }

    protected void send(byte[] value) throws Exception {
        outStream.write(value);
    }
}