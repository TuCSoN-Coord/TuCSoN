/*
 * TuCSoN coordination infrastructure - Copyright (C) 2001-2002  aliCE team at deis.unibo.it
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
package alice.tucson.service;
import alice.tucson.api.*;
import alice.tucson.common.TucsonProtocol;
import alice.tucson.common.TucsonProtocolTCP;
import alice.tucson.common.TupleSerialiser;
import alice.logictuple.*;


import java.util.*;
import java.io.*;

/**
 * Inter tuple centre context stub
 *
 */
public class InterTupleCentreContextStub implements InterTupleCentreContext{

    private static final int  TYPE_OUT = 0;

    /** current tuple centres accessed*/
    private HashMap tupleCentreConnections = new HashMap();

    private ContextDescription profile;
    private Node node;

    public InterTupleCentreContextStub(Node node) {
        this.node=node;
        profile = new ContextDescription();
        profile.setProperty("context-name","default");
    }

    public synchronized void out_tc(AgentId id, TupleCentreId tid, LogicTuple t)
        throws TucsonException, InvalidLogicTupleException {

            if (t==null){
                throw new InvalidLogicTupleException();
            }
            int     nTry=1;
            do {
                InterTCConnectionInfo info=getTupleCentreInfo(id,tid);
                if (!info.local){
                    try {
                        DataOutputStream outStream=info.dialog.getOutputStream();
                        DataInputStream inStream=info.dialog.getInputStream();

                        outStream.writeInt(TYPE_OUT);
                        TupleSerialiser.writeTuple(outStream,t);
                        outStream.flush();
                        //
                        boolean allowed=inStream.readBoolean();
                        if (!allowed){
                            throw new OperationNotAllowedException();
                        }
                        return;
                    } catch (IOException ex){
                        resetTupleCentreInfo(tid);
                    }
                } else {
                    info.core.out(t,id,null);
                    return;
                }
                nTry++;
            } while (nTry<3);
            
            throw new TucsonException("TuCSoN I/O Exception");
    }


    /**
     * creates or retrieve the connection to a tuple centre vm.
     * if the tuple centre is new, a new connection to target
     * daemon providing the tuple centre is created,
     * otherwise returns the already established connection
     */
    private InterTCConnectionInfo getTupleCentreInfo(AgentId id,TupleCentreId tid) throws TucsonException  {
        try {
            String stid=tid.toString();
            Object tc=tupleCentreConnections.get(stid);
            if (tc!=null){
                return (InterTCConnectionInfo)tc;
            } else {
                InterTCConnectionInfo info=null;
                info=new InterTCConnectionInfo();
                info.core=null;
                TucsonVMUsers coreInfo=null;

                coreInfo=node.getCore(tid);

                if (coreInfo!=null){
                    info.local=true;
                    info.core=coreInfo.getVirtualMachine();
                    tupleCentreConnections.put(tid.toString(),info);
                    return info;
                } else {
                    info.local=false;

                    profile.setProperty("node",tid.getNode().toString());
                    profile.setProperty("tuple-centre",tid.getName().toString());
                    if (id==null){
                       id = new AgentId("timer("+tid.getName().toString()+")");
                    }
                    profile.setProperty("agent-identity",id.toString());
                    try {
                        String node= alice.util.Tools.removeApices(tid.getNode());
                        TucsonProtocol dialog = new TucsonProtocolTCP(node,20504);
                        dialog.sendEnterRequest(profile);
                        dialog.receiveEnterRequestAnswer();
                        if (dialog.isEnterRequestAccepted()){
                            info.local=false;
                            info.dialog=dialog;
                            tupleCentreConnections.put(tid.toString(),info);
                            return info;
                        }
                    } catch (Exception ex){
                        throw new TucsonException("TuCSoN I/O error");
                    }
                }

            }
        } catch (Exception ex){
            ex.printStackTrace();
            throw new TucsonException();
        }
        throw new TucsonException("TuCSoN I/O error");
    }


    /**
     * reset information about an established connection
     */
    private boolean resetTupleCentreInfo(TupleCentreId tid){
        String stid=tid.toString();
        Object tc=tupleCentreConnections.remove(stid);
        if (tc!=null){
            TucsonProtocol info=(TucsonProtocol)tc;
            try {
                info.end();
            } catch (Exception ex){
            }
            return true;
        }
        return false;
    }


    /**
     * information about an established connection
     * with a remote tuple centre virtual machine
     */
    class InterTCConnectionInfo {
        TucsonProtocol dialog;
        boolean local;
        TucsonVM core;
    }
}





