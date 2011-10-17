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

import java.io.*;
import java.util.*;

import alice.logictuple.*;

import alice.tuplecentre.api.ITupleCentreOperation;

import alice.tuplecentre.core.OperationCompletionListener;
import alice.tuplecentre.core.TupleCentreOperation;

import alice.tucson.common.*;

import alice.tucson.api.ContextDescription;
import alice.tucson.api.TucsonAgentId;
import alice.tucson.api.TucsonTupleCentreId;

import alice.tucson.service.Node;

/**
 * 
 * this is user active acceptor class receiving communication events from user
 * proxy and dispatching them to the referenced tuple centre virtual machine.
 * The interaction is bidirectional: output events from tuple centre vm are
 * dispatched to user proxy
 * 
 * an acceptor could serve more then one user proxy (because of it hasn't
 * blocking operations)
 * 
 * Relation 1-1 with ACC
 * 
 * modified by a.pedone
 */
public class AgentContextSkeleton extends ContextSkeleton implements OperationCompletionListener
{
	private boolean exit = false;
	private TucsonAgentId agentId;

	TucsonProtocol dialog;
	ObjectInputStream inStream;
	ObjectOutputStream outStream;
	alice.tucson.service.Node node;
	HashMap<Long, TucsonMsgRequest> requests;
	HashMap<Long, Long> opVsReq;
	ContextManager manager;
	int ctxId;

	public AgentContextSkeleton(ContextManager man, TucsonProtocol dialog, Node node, ContextDescription p) throws Exception
	{
		try
		{
			ctxId = Integer.parseInt(p.getProperty("context-id"));
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

		String name = p.getProperty("agent-identity");
		agentId = new TucsonAgentId(name);

		this.dialog = dialog;
		inStream = dialog.getInputStream();
		outStream = dialog.getOutputStream();

		requests = new HashMap<Long, TucsonMsgRequest>();
		opVsReq = new HashMap<Long, Long>();

		this.node = node;
		manager = man;
	}

	protected void log(String st)
	{
		System.out.println("[ACC SKELETON] " + st);
	}

	/**
	 * behaviour of the acceptor: wait for communication events from a user
	 * proxy and dispatch them to the virtual machine
	 */
	@SuppressWarnings("unchecked")
	public void run()
	{
		log("ACC agent " + agentId.toString() + " booted.");

		node.addAgent(agentId);

		while (!exit)
		{
			try
			{
				// Leggo il messaggio dallo stream di input
				TucsonMsgRequest msg = TucsonMsgRequest.read(inStream);

				if(msg.getType() == TucsonOperation.exitCode())
					log("new request type = " + msg.getType() + ";");
				else
					
					log("new request identifier = " + msg.getId() +"; type = " + msg.getType() + "; destination = " + msg.getTid().toString() + "; tuple = "
						+ msg.getTuple() + ";");
					
				// Viene analizzato il tipo di operazione richiesta
				int msg_type = msg.getType();				
				
				if (msg_type == TucsonOperation.exitCode())
				{
					// Non ha senso parlare di tid per l'operazione di exit
					TucsonMsgReply reply = new TucsonMsgReply(msg.getId(), TucsonOperation.exitCode(), true, true);
					TucsonMsgReply.write(outStream, reply);
					outStream.flush();
					break;
				}
				
				//NOVITA': costruisci il centro di tuple lato nodo, con la stringa passata dal client!
				TucsonTupleCentreId tid = new TucsonTupleCentreId((Object) msg.getTid());  
				
				if (msg_type == TucsonOperation.set_specCode() || msg_type == TucsonOperation.get_specCode())
				{
					log("execute call " + msg_type);
					
					node.resolveCore(tid.getName());
					node.addTCAgent(agentId, tid);
					LogicTuple res = (LogicTuple) TucsonTCContainer.doBlockingSpecOperation(msg_type, agentId, tid, msg.getTuple());
					
					TucsonMsgReply reply = null;
					
					if (msg_type == TucsonOperation.get_specCode())
					{
						LogicTuple spec = new LogicTuple("spec", new Value(res.toString()));
						reply = new TucsonMsgReply(msg.getId(), msg_type, true, true, spec, spec);
					}
					else
					{
						reply = new TucsonMsgReply(msg.getId(), msg_type, true, true);
					}
					
					log("call executed");
					TucsonMsgReply.write(outStream, reply);
					outStream.flush();
				}
				else if (msg_type == TucsonOperation.get_Code())
				{
					log("execute call " + msg_type);
					
					node.resolveCore(tid.getName());
					node.addTCAgent(agentId, tid);
					List<LogicTuple> res = (List<LogicTuple>) TucsonTCContainer.doBlockingOperation(msg_type, agentId, tid, null);
					
					TucsonMsgReply reply = null;
										
					reply = new TucsonMsgReply(msg.getId(), msg_type, true, true, null, res);
					
					log("call executed");
					TucsonMsgReply.write(outStream, reply);
					outStream.flush();
				}
				else if (msg_type == TucsonOperation.outCode() || msg_type == TucsonOperation.inCode()
						|| msg_type == TucsonOperation.inpCode() || msg_type == TucsonOperation.rdCode()
						|| msg_type == TucsonOperation.rdpCode())
				{
					synchronized (requests)
					{
						log("execute call " + msg_type);
						
						node.resolveCore(tid.getName());
						node.addTCAgent(agentId, tid);

						// Devo ottenere un oggetto di tipo TucsonOperation
						ITupleCentreOperation op = TucsonTCContainer.doNonBlockingOperation(msg_type, agentId, tid, msg
								.getTuple(), this);			
						
						log("call executed");
						requests.put(new Long(msg.getId()), msg);
						log("put in requests: msg.getId() = " + msg.getId() + ", msg " + msg);
						opVsReq.put(new Long(op.getId()), new Long(msg.getId()));
						log("put in opVsReq: op.getId() = " + op.getId() + ", msg.getId() " + msg.getId());
					}
				}
				else if (msg_type == TucsonOperation.out_sCode()
						|| msg_type == TucsonOperation.in_sCode() || msg_type == TucsonOperation.inp_sCode()
						|| msg_type == TucsonOperation.rd_sCode() || msg_type == TucsonOperation.rdp_sCode())
				{
					synchronized (requests)
					{
						log("execute call " + msg_type);
						
						node.resolveCore(tid.getName());
						node.addTCAgent(agentId, tid);
						ITupleCentreOperation op = TucsonTCContainer.doNonBlockingSpecOperation(msg_type, agentId, tid, msg
								.getTuple(), this);
						
						log("call executed");
						requests.put(new Long(msg.getId()), msg);
						log("put in requests: msg.getId() = " + msg.getId() + ", msg " + msg);
						opVsReq.put(new Long(op.getId()), new Long(msg.getId()));
						log("put in opVsReq: op.getId() = " + op.getId() + ", msg.getId() " + msg.getId());
					}
				}
			}
			catch (Exception e)
			{
				log("agent " + agentId + " disconnected.");
				e.printStackTrace();
				break;
			}
		}

		try
		{
			dialog.end();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

		node.removeAgent(agentId);
		manager.shutdownContext(ctxId, agentId);

		log("agent " + agentId + " exit.");
	}

	synchronized public void exit()
	{
		exit = true;
		notify();
	}

	/**
	 * dispatching a virtual machine event from the tuple centre to the user
	 */
	public void operationCompleted(TupleCentreOperation op)
	{
		log("Notify operation id: " + op.getId());
		synchronized (requests)
		{
			try
			{
				// l'id della richiesta dato l'id dell'operazione
				Long reqId = opVsReq.remove(new Long(op.getId()));
				// il msg di request dato il suo id
				TucsonMsgRequest msg = requests.remove(reqId);
				
				TucsonMsgReply reply = null;
				
				log("Related request id: " + msg.getId());
				
				if (op.isOut() || op.isOut_s())
				{
					log("end Response");
					reply = new TucsonMsgReply(msg.getId(), op.getType(), true, true, msg.getTuple(), (LogicTuple) op.getResult());
				}
				else if (op.isIn() || op.isIn_s() || op.isRd() || op.isRd_s())
				{
					reply = new TucsonMsgReply(msg.getId(), op.getType(), true, true, msg.getTuple(), (LogicTuple) op.getResult());
				}
				else if (op.isInp() || op.isInp_s() || op.isRdp() || op.isRdp_s())
				{
					if (op.isResultDefined())
					{
						log("" + msg.getId() + "  " + op.getType() + "   " + msg.getTuple() + "  " + (LogicTuple) op.getResult());
						reply = new TucsonMsgReply(msg.getId(), op.getType(), true, true, msg.getTuple(), (LogicTuple) op.getResult());
					}
					else
					{
						reply = new TucsonMsgReply(msg.getId(), op.getType(), true, false);
					}
				}
				
				TucsonMsgReply.write(outStream, reply);
				outStream.flush();
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}
	}
}
