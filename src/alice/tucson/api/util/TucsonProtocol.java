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
import alice.logictuple.LogicTuple;
import java.util.*;
import java.io.*;

/**
 *
 */
public abstract class TucsonProtocol implements java.io.Serializable {

    abstract public DataInputStream getInputStream();
    abstract public DataOutputStream getOutputStream();

    abstract public TucsonProtocol acceptNewDialog() throws Exception;
    abstract public void end() throws Exception;

    abstract protected String receiveString() throws Exception;
    abstract protected int receiveInt() throws Exception;
    abstract protected boolean receiveBoolean() throws Exception;

    abstract protected void send(int value) throws Exception;
    abstract protected void send(boolean value) throws Exception;
    abstract protected void send(String value) throws Exception;
    abstract protected void send(byte[] value) throws Exception;

    abstract protected void flush() throws Exception;

    // -----------------------------------------------

    public void sendEnterRequest(ContextDescription context) throws Exception {

        send(REQ_ENTERCONTEXT);

        String contextName=context.getProperty("context-name");
        if (contextName==null){
            contextName="default-context";
        }
        send(contextName);

        //agent name
        String agentName=context.getProperty("agent-identity");
        if (agentName==null){
            agentName="anonymous";
        }
        send(agentName);

        // agent profile
        String agentProfile=context.getProperty("agent-profile");
        if (agentProfile==null){
            agentProfile="default";
        }
        send(agentProfile);

        //tc name
        String tcName=context.getProperty("tuple-centre");
        if (tcName==null){
            tcName="_";
        }
        send(tcName);
        flush();
    }

    public void receiveEnterRequestAnswer() throws Exception {
        request_allowed = receiveBoolean();
    }

    public boolean isEnterRequestAccepted() throws Exception {
        return request_allowed;
    }

    public void sendTelnetRequestAnswer(String info) throws Exception {
        send(info.getBytes());
        flush();
    }
    // ----------------------------------------------


    public void receiveFirstRequest() throws Exception {
        // in order to see if it's a telnet interaction
        // we test some times if there are not
        // avaiable bytes (telnet case)
        int nchecks=0;
        boolean telnet_req=false;
        while (getInputStream().available()==0){
            Thread.currentThread().sleep(50);
            if (++nchecks==4){
                telnet_req = true;
                break;
            }
        }
        if (telnet_req){
            request_type = REQ_TELNET;
        } else {
            request_type = receiveInt();
        }
    }

    public void receiveEnterRequest() throws Exception {
        // context name
        String contextName=receiveString();
        // agent name
        String agentName=receiveString();
        // agent profile
        String agentProfile=receiveString();
        // tuple centre name
        String tcName=receiveString();
        //
        Properties profile = new Properties();
        profile.setProperty("agent-identity",agentName);
        profile.setProperty("context-name",contextName);
        profile.setProperty("agent-profile",agentProfile);
        if (!tcName.equals("_")){
            profile.setProperty("tuple-centre",tcName);
        }
        context= new ContextDescription(profile);
    }

    public void sendEnterRequestRefused(ContextDescription context) throws Exception {
        send(false);
        flush();
    }

    public void sendEnterRequestAccepted(ContextDescription context)throws Exception {
        send(true);
        flush();
    }

    public ContextDescription getContextDescription() {
        return context;
    }

    public boolean isTelnetRequest(){
        return request_type == REQ_TELNET;
    }

    public boolean isEnterRequest(){
        return request_type == REQ_ENTERCONTEXT;
    }

    // -------------------------------

    private int request_type;
    private boolean request_allowed;

    private static final int TUCSON_MARK = 303;
    private static final int REQ_TELNET = 0;
    private static final int REQ_ENTERCONTEXT = 1;
    private ContextDescription context;

}