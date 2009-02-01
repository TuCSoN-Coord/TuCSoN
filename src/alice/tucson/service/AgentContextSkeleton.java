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
import alice.respect.core.RespectOperation;
import alice.tucson.api.*;
import alice.tucson.common.*;
import alice.tucson.types.TucsonAgentId;
import alice.tucson.types.TucsonOpType;
import alice.tucson.types.TucsonTupleCentreId;
import alice.logictuple.*;

import alice.tuplecentre.api.ITupleCentreOperation;
import alice.tuplecentre.core.OperationCompletionListener;
import alice.tuplecentre.core.TupleCentreOperation;

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
 * 
 * Relation 1-1 with ACC
 */
public class AgentContextSkeleton extends ContextSkeleton implements OperationCompletionListener{

    private boolean exit =false;
    private TucsonAgentId agentId; 

    TucsonProtocol      dialog;
    ObjectInputStream  inStream;
    ObjectOutputStream outStream;
    alice.tucson.service.Node 			 node;
    HashMap 			cores;
    HashMap<Long, TucsonMsgRequest> pendingOps;
    ContextManager   manager;
    int ctxId;
    
    public AgentContextSkeleton(ContextManager man, TucsonProtocol dialog, alice.tucson.service.Node node, ContextDescription p) throws Exception {
        
        try {
        		ctxId = Integer.parseInt(p.getProperty("context-id"));
        } catch (Exception ex){
        		ex.printStackTrace();
        }
        
        String name = p.getProperty("agent-identity");
        agentId = new TucsonAgentId(name);

        this.dialog = dialog;
        inStream = dialog.getInputStream();
        outStream = dialog.getOutputStream();
  
        cores = new HashMap();
        pendingOps = new HashMap<Long, TucsonMsgRequest>();
        
        this.node = node;
        manager = man;
    }

    protected void log(String st){
        System.out.println("[ACC SKELETON] "+st);
    }

    /**
     * behaviour of the acceptor:
     * wait for communication events from a user proxy and
     * dispatch them to the virtual machine
     */
    public void run() {
    	log("ACC agent "+agentId.toString()+" booted.");

    	node.addAgent(agentId);

    	while (!exit){
    		try {

    			TucsonMsgRequest msg = TucsonMsgRequest.read(inStream);
    			log("new request type = "+msg.getType()+"; destination = "+msg.getTid().toString()+"; tuple = "+msg.getTuple()+";");

    			int msg_type=msg.getType();
    			//Have no sense speak of tid for exit operation
    			if (msg_type ==  TucsonOpType.OPTYPE_EXIT){
    				TucsonMsgReply reply = new TucsonMsgReply(msg.getId(),TucsonOpType.OPTYPE_EXIT,true,true);
    				TucsonMsgReply.write(outStream,reply);
    				outStream.flush();
    				break;
    			}
    			TucsonTupleCentreId tid = msg.getTid();
    			if (msg_type == TucsonOpType.OPTYPE_SET_SPEC || msg_type == TucsonOpType.OPTYPE_GET_SPEC){
    				//setSpec and getSpec are managed in a different way because of their synchronous nature
    				node.resolveCore(tid.getName());
    				node.addTCAgent(agentId,tid);
    				LogicTuple res = TucsonTCContainer.doBlockingOperation(msg_type, agentId, tid, msg.getTuple());
    				TucsonMsgReply reply = null;
    				if (msg_type == TucsonOpType.OPTYPE_GET_SPEC){
    					LogicTuple spec = new LogicTuple("spec",new Value(res.toString()));
    					reply = new TucsonMsgReply(msg.getId(),msg_type,true,true,spec,spec);
    				}else {
    					reply = new TucsonMsgReply(msg.getId(),msg_type,true,true);
    				}
    				TucsonMsgReply.write(outStream,reply);
        			outStream.flush();
    			} else if (msg_type == TucsonOpType.OPTYPE_OUT || msg_type == TucsonOpType.OPTYPE_IN || msg_type == TucsonOpType.OPTYPE_INP || msg_type == TucsonOpType.OPTYPE_RD || msg_type == TucsonOpType.OPTYPE_RDP 
    					|| msg_type == TucsonOpType.OPTYPE_OUT_S || msg_type == TucsonOpType.OPTYPE_IN_S || msg_type == TucsonOpType.OPTYPE_INP_S || msg_type == TucsonOpType.OPTYPE_RD_S || msg_type == TucsonOpType.OPTYPE_RDP_S){
    				synchronized (pendingOps){
    					log("execute call "+msg_type);
    					node.resolveCore(tid.getName());
    					node.addTCAgent(agentId, tid);
    					ITupleCentreOperation op = TucsonTCContainer.doNonBlockingOperation(msg_type, agentId, tid, msg.getTuple(), this);
    					log("call executed");
    					pendingOps.put(new Long(op.getId()),msg);
    				}
    			}
    			
    		} catch (Exception e){
    			log("agent "+agentId+" disconnected.");
    			e.printStackTrace();
    			break;
    		}
    	}

    	try {
    		dialog.end();
    	} catch (Exception ex){
    		ex.printStackTrace();
    	}

    	node.removeAgent(agentId);
    	
    	manager.shutdownContext(ctxId,agentId);

    	log("agent "+agentId+" exit.");
    }

    synchronized public void exit(){
    	exit=true;
    	notify();
    }

    /**
     * dispatching a virtual machine event
     * from the tuple centre to the user
     */
	public void operationCompleted(TupleCentreOperation op) {
		//log("PRE-NOTIFY "+op.getId());
    	synchronized (pendingOps){
    		try {
    			TucsonMsgRequest msg = pendingOps.remove(new Long(op.getId()));
    			TucsonMsgReply reply = null;
    			//log("Co-related request: "+msg.getId());
    			if (op.isOut() || op.isOut_s()){
    				log("end Response");
    				reply = new TucsonMsgReply(msg.getId(),op.getType(),true,true);
    			} else if (op.isIn() || op.isIn_s() || op.isRd() || op.isRd_s()){
    				reply = new TucsonMsgReply(msg.getId(),op.getType(),true,true,msg.getTuple(), ((RespectOperation)op).getLogicTupleResult());
    			} else if (op.isInp() || op.isInp_s() || op.isRdp() || op.isRdp_s()){
    				if (op.isResultDefined()){
    					log(""+msg.getId()+"  "+op.getType()+"   "+msg.getTuple()+"  "+((RespectOperation)op).getLogicTupleResult());
    					reply = new TucsonMsgReply(msg.getId(),op.getType(),true,true,msg.getTuple(), ((RespectOperation)op).getLogicTupleResult());
    				} else {
    					reply = new TucsonMsgReply(msg.getId(),op.getType(),true,false);
    				}
    			}
    			TucsonMsgReply.write(outStream,reply);
    			outStream.flush();
    		} catch (Exception ex){
    			ex.printStackTrace();
    		}
    	}		
	}
	
}

