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
package alice.tucson.runtime.contexts;
import alice.tucson.api.*;
import alice.tucson.api.util.TucsonProtocol;
import alice.tucson.api.util.TupleSerialiser;
import alice.tucson.api.contexts.DefaultContextRawProtocol;
import alice.tucson.runtime.*;
import alice.logictuple.*;

import alice.tuplecentre.OutputEvent;
import alice.tuplecentre.OutputEvent_in;
import alice.tuplecentre.OutputEvent_inp;
import alice.tuplecentre.OutputEvent_rd;
import alice.tuplecentre.OutputEvent_rdp;
import alice.tuplecentre.OutputEvent_setSpec;
import alice.tuplecentre.OutputEvent_getSpec;
import alice.tuplecentre.OutputEvent;
import alice.tuplecentre.OutputEventListener;
import alice.tuplecentre.Event;

import java.util.*;
import java.io.*;

/**
 *
 * this is user active acceptor class receiving communication
 * events from user proxy and dispatching them to the
 * referenced tuple centre virtual machine.
 * The interaction is bidirectional:  output events from
 * tuple centre vm are dispatched to user proxy
 *
 * an acceptor could serve more then one user proxy
 * (because of it hasn't blocking operations)
 */
public class DefaultContextSkel extends ContextSkeleton {


    /** tuple centre virtual machine reachable from this acceptor*/
    TucsonVM core;
    TucsonVMUsers coreInfo;

    private boolean exit =false;

    private DefaultContextProfile profile;

    private AgentId agentId;

    TucsonProtocol      dialog;
    DataInputStream  inStream;
    DataOutputStream outStream;

    public DefaultContextSkel(TucsonProtocol dialog, Node node, ContextDescription p, LogicTuple contextDescr) throws Exception {
        //
        String name = p.getProperty("agent-identity");
        String tcName = p.getProperty("tuple-centre");
        agentId = new AgentId(name);

        this.dialog = dialog;
        inStream = dialog.getInputStream();
        outStream = dialog.getOutputStream();

        coreInfo=node.resolveCore(tcName);
        core=coreInfo.getVirtualMachine();

        profile= new DefaultContextProfile(contextDescr);
        start();
    }

    protected void log(String st){
        System.out.println("[DEFAULT CONTEXT] "+st);
    }

    /**
     * behaviour of the acceptor:
     * wait for communication events from a user proxy and
     * dispatch them to the virtual machine
     */
    public void run() {
        LogicTuple logicTuple=null;
        log("agent "+agentId+" booted.");
        coreInfo.addUser(this);
        while (!exit){
            try {
                int msg_type=inStream.readInt();

                if (!profile.isTemporalConstraintValid()){
                    outStream.writeBoolean(false);
                    outStream.flush();
                    break;
                }

                if (msg_type==DefaultContextRawProtocol.TYPE_OUT){
                    logicTuple=TupleSerialiser.readTuple(inStream);

                    if (!profile.isAllowed_out(logicTuple)){
                        outStream.writeBoolean(false);
                        outStream.flush();
                    } else {
                        // unordered semantic
                        core.out(logicTuple, agentId, this);
                        //
                        outStream.writeBoolean(true);
                        outStream.flush();
                    }
                } else if (msg_type==DefaultContextRawProtocol.TYPE_IN){
                    logicTuple=TupleSerialiser.readTuple(inStream);

                    if (!profile.isAllowed_in(logicTuple)){
                        outStream.writeBoolean(false);
                        outStream.flush();
                    } else {
                        core.in(logicTuple, agentId, this);
                    }
                } else if (msg_type==DefaultContextRawProtocol.TYPE_RD){
                    logicTuple=TupleSerialiser.readTuple(inStream);

                    if (!profile.isAllowed_rd(logicTuple)){
                        outStream.writeBoolean(false);
                        outStream.flush();
                    } else {
                        core.rd(logicTuple, agentId, this);
                    }
                } else if (msg_type==DefaultContextRawProtocol.TYPE_INP){
                    logicTuple=TupleSerialiser.readTuple(inStream);

                    if (!profile.isAllowed_inp(logicTuple)){
                        outStream.writeBoolean(false);
                        outStream.flush();
                    } else {
                        core.inp(logicTuple, agentId, this);
                    }
                } else if (msg_type==DefaultContextRawProtocol.TYPE_RDP){
                    logicTuple=TupleSerialiser.readTuple(inStream);

                    if (!profile.isAllowed_rdp(logicTuple)){
                        outStream.writeBoolean(false);
                        outStream.flush();
                    } else {
                        core.rdp(logicTuple, agentId, this);
                    }
                } else if (msg_type==DefaultContextRawProtocol.TYPE_SETSPEC){
                    String spec=inStream.readUTF();
                    core.setSpec(spec, agentId, this);
                } else if (msg_type==DefaultContextRawProtocol.TYPE_GETSPEC){
                    core.getSpec(agentId, this);
                } else if (msg_type==DefaultContextRawProtocol.TYPE_EXIT){
                    break;
                }
            } catch (Exception e){
                log("agent "+agentId+" disconnected.");
                //e.printStackTrace();
                break;
            }
        }

        try {
            dialog.end();
        } catch (Exception ex){
            ex.printStackTrace();
        }

        if (coreInfo!=null){
            coreInfo.removeUser(this);
        }

        core.removeAgentEvents(agentId);
        log("agent "+agentId+" exit.");
    }


    /**
     * dispatching a virtual machine event
     * from the tuple centre to the user
     */
    public synchronized void notify(OutputEvent ev) {
        try {
            if (ev instanceof OutputEvent_inp){
                outStream.writeBoolean(true);
                outStream.writeBoolean(ev.isSuccess());
                TupleSerialiser.writeTuple(outStream,(LogicTuple)((OutputEvent_inp)ev).getTuple());

            } else if (ev instanceof OutputEvent_rdp){
                outStream.writeBoolean(true);
                outStream.writeBoolean(ev.isSuccess());
                TupleSerialiser.writeTuple(outStream,(LogicTuple)((OutputEvent_rdp)ev).getTuple());
            } else if (ev instanceof OutputEvent_in){
                outStream.writeBoolean(true);
                TupleSerialiser.writeTuple(outStream,(LogicTuple)((OutputEvent_in)ev).getTuple());
            } else if (ev instanceof OutputEvent_rd){
                outStream.writeBoolean(true);
                TupleSerialiser.writeTuple(outStream,(LogicTuple)((OutputEvent_rd)ev).getTuple());
            } else if (ev instanceof OutputEvent_setSpec){
                outStream.writeBoolean(true);
                outStream.writeBoolean(ev.isSuccess());
            } else if (ev instanceof OutputEvent_getSpec){
                outStream.writeBoolean(true);
                outStream.writeUTF(((OutputEvent_getSpec)ev).getSpec());
            }
            outStream.flush();
        } catch (Exception ex){
        }
    }

    synchronized public void exit(){
        exit=true;
        notify();
    }
}

