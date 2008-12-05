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
import alice.tucson.common.*;
import alice.tucson.service.*;
import alice.logictuple.*;

import alice.tuplecentre.InputEvent;
import alice.tuplecentre.OutputEvent;
import alice.tuplecentre.OutputEvent_in;
import alice.tuplecentre.OutputEvent_inp;
import alice.tuplecentre.OutputEvent_rd;
import alice.tuplecentre.OutputEvent_rdp;
import alice.tuplecentre.OutputEvent_setSpec;
import alice.tuplecentre.OutputEvent_getSpec;
import java.io.*;
import java.util.*;

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
public class AgentContextSkeleton extends ContextSkeleton {

    private static final int  TYPE_OUT = 0;
    private static final int  TYPE_IN = 1;
    private static final int  TYPE_RD = 2;
    private static final int  TYPE_INP = 3;
    private static final int  TYPE_RDP = 4;
    private static final int  TYPE_SETSPEC = 5;
    private static final int  TYPE_GETSPEC = 6;
    private static final int  TYPE_EXIT = 7;

    private boolean exit =false;
    private AgentId agentId;

    TucsonProtocol      dialog;
    DataInputStream  inStream;
    DataOutputStream outStream;
    alice.tucson.service.Node 			 node;
    HashMap 			cores;
    HashMap			pendingOps;
    ContextManager   manager;
    int ctxId;
    
    public AgentContextSkeleton(ContextManager man, TucsonProtocol dialog, alice.tucson.service.Node node, ContextDescription p) throws Exception {
        
        try {
        		ctxId = Integer.parseInt(p.getProperty("context-id"));
        } catch (Exception ex){
        		ex.printStackTrace();
        }
        
        String name = p.getProperty("agent-identity");
        agentId = new AgentId(name);

        this.dialog = dialog;
        inStream = dialog.getInputStream();
        outStream = dialog.getOutputStream();
  
        cores = new HashMap();
        pendingOps = new HashMap();
        
        this.node = node;
        manager = man;
    }

    protected void log(String st){
        System.out.println("["+agentId+" CONTEXT] "+st);
    }

    /**
     * behaviour of the acceptor:
     * wait for communication events from a user proxy and
     * dispatch them to the virtual machine
     */
    public void run() {
        log("ACC agent "+agentId+" booted.");
		
        node.addAgent(agentId);
        
        while (!exit){
            try {
            	
            	   TucsonMsgRequest msg = TucsonMsgRequest.read(inStream);
            	   
                int msg_type=msg.getType();
                TupleCentreId tid = msg.getTid();

         	   //log("New request "+msg.getId()+"  type: "+msg_type);
                
                TucsonVM core = getCore(tid);
                
                if (msg_type==TYPE_OUT){
                	//log("PRE-OUT "+msg.getTuple());
                	synchronized (pendingOps){
                		InputEvent ev = core.out(msg.getTuple(), agentId, this);
                		pendingOps.put(new Long(ev.getId()),msg);
                		//
                		TucsonMsgReply reply = new TucsonMsgReply(msg.getId(),TYPE_OUT,true,true);
                		TucsonMsgReply.write(outStream,reply);
                		outStream.flush();	            		
                	}
                		//log("POST-OUT "+msg.getTuple());
                } else if (msg_type==TYPE_IN){
            			//log("PRE-IN "+msg.getTuple());
                		synchronized (pendingOps){
                			InputEvent ev = core.in(msg.getTuple(), agentId, this);
                			pendingOps.put(new Long(ev.getId()),msg);
                		}
                		//log("POST-IN "+msg.getTuple());
                } else if (msg_type==TYPE_RD){
            			//log("POST-RD "+msg.getTuple());
	            		synchronized (pendingOps){
	            			InputEvent ev = core.rd(msg.getTuple(), agentId, this);
	            			//log("PRE-RD id "+ev.getId());
	            			pendingOps.put(new Long(ev.getId()),msg);
	            		}
                		//log("POST-RD "+msg.getTuple());
                } else if (msg_type==TYPE_INP){
	            		synchronized (pendingOps){
	            			InputEvent ev = core.inp(msg.getTuple(), agentId, this);
	            			//log("PRE-INP id "+ev.getId());
	            			pendingOps.put(new Long(ev.getId()),msg);
	            		}
                } else if (msg_type==TYPE_RDP){
	            		synchronized (pendingOps){
	            			InputEvent ev = core.rdp(msg.getTuple(), agentId, this);
	            			pendingOps.put(new Long(ev.getId()),msg);
	            		}
                } else if (msg_type==TYPE_SETSPEC){
                     String spec=msg.getTuple().getArg(0).getName();
             		//log("PRE-SETSPEC "+spec);
	            		synchronized (pendingOps){
	            			InputEvent ev = core.setSpec(spec, agentId, this);
	            			pendingOps.put(new Long(ev.getId()),msg);
	            		}
            			//log("POST-SETSPEC "+spec);
                } else if (msg_type==TYPE_GETSPEC){
              		//log("PRE-GET_SPEC ");
	            		synchronized (pendingOps){
	            			InputEvent ev = core.getSpec(agentId, this);
	            			pendingOps.put(new Long(ev.getId()),msg);
	            		}
                		//log("POST-GET_SPEC ");
                } else if (msg_type==TYPE_EXIT){
                	TucsonMsgReply reply = new TucsonMsgReply(msg.getId(),TYPE_EXIT,true,true);
                	TucsonMsgReply.write(outStream,reply);
                	outStream.flush();
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

        node.removeAgent(agentId);
		synchronized (cores){
			Iterator it = cores.values().iterator();
			while (it.hasNext()){
				TucsonVMUsers info = (TucsonVMUsers)it.next();
				info.removeUser(this);
				info.getVirtualMachine().removeAgentEvents(agentId);
			}
		}
        
		manager.shutdownContext(ctxId,agentId);
		
        log("agent "+agentId+" exit.");
    }

	private TucsonVM getCore(TupleCentreId tid){
		synchronized (cores){
			TucsonVMUsers coreInfo = (TucsonVMUsers)cores.get(tid.getName()); 
			if (coreInfo==null){	
				coreInfo = node.resolveCore(tid.getName());
				coreInfo.addUser(this);
				cores.put(tid.getName(),coreInfo);
			}
			TucsonVM core=coreInfo.getVirtualMachine();
			return core;
		}
	}

    /**
     * dispatching a virtual machine event
     * from the tuple centre to the user
     */
    public void notify(OutputEvent ev) {
   	        //log("PRE-NOTIFY "+ev.getId());
    		synchronized (pendingOps){
	    		try {
	    			
	    		   TucsonMsgRequest msg = (TucsonMsgRequest) pendingOps.remove(new Long(ev.getId()));
	    		   TucsonMsgReply reply = null;

         	       //log("Co-related request: "+msg.getId());

	    		    if (ev instanceof OutputEvent_inp){
	            	   if (ev.isSuccess()){
	            		   reply = new TucsonMsgReply(msg.getId(),TYPE_INP,true,true,msg.getTuple(),(LogicTuple)((OutputEvent_inp)ev).getTuple());
	            	   } else {
	            		   reply = new TucsonMsgReply(msg.getId(),TYPE_INP,true,false);
	            	   }            		   
	            } else if (ev instanceof OutputEvent_rdp){
	            	   if (ev.isSuccess()){
	            		   reply = new TucsonMsgReply(msg.getId(),TYPE_RDP,true,true,msg.getTuple(),(LogicTuple)((OutputEvent_rdp)ev).getTuple());
	            	   } else {
	            		   reply = new TucsonMsgReply(msg.getId(),TYPE_RDP,true,false);
	            	   }            		   
	            } else if (ev instanceof OutputEvent_in){
         		   	reply = new TucsonMsgReply(msg.getId(),TYPE_IN,true,true,msg.getTuple(),(LogicTuple)((OutputEvent_in)ev).getTuple());
	            } else if (ev instanceof OutputEvent_rd){
         		   reply = new TucsonMsgReply(msg.getId(),TYPE_RD,true,true,msg.getTuple(),(LogicTuple)((OutputEvent_rd)ev).getTuple());
	            } else if (ev instanceof OutputEvent_setSpec){
	            		if (ev.isSuccess()){
	            			reply = new TucsonMsgReply(msg.getId(),TYPE_SETSPEC,true,true);
	            		} else {
	            			reply = new TucsonMsgReply(msg.getId(),TYPE_SETSPEC,true,false);
	            		}
	            } else if (ev instanceof OutputEvent_getSpec){
	            		LogicTuple spec = new LogicTuple("spec",new Value(((OutputEvent_getSpec)ev).getSpec()));
	            		reply = new TucsonMsgReply(msg.getId(),TYPE_GETSPEC,true,true,spec,spec);
	            }
	            
	            TucsonMsgReply.write(outStream,reply);
	            outStream.flush();
	            
	    		} catch (Exception ex){
	    			ex.printStackTrace();
	    		}
    		}
    }

    synchronized public void exit(){
        exit=true;
        notify();
    }
}

