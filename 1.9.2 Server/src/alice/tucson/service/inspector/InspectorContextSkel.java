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
package alice.tucson.service.inspector;

import alice.tuplecentre.core.*;
import alice.tuplecentre.api.*;

import alice.tucson.api.*;
import alice.tucson.api.exception.*;
import alice.tucson.service.*;
import alice.tucson.api.TucsonAgentId;
import alice.tucson.api.TucsonTupleCentreId;
import alice.tucson.common.TucsonProtocol;

import alice.logictuple.LogicTuple;

import java.util.*;
import java.io.*;
import java.lang.reflect.*;

/**
 * Inspector context skeleton
 */
public class InspectorContextSkel extends ContextSkeleton implements InspectableEventListener
{
	/** channel with remote inspector proxy */
	ObjectOutputStream outStream;
	ObjectInputStream inStream;

	TucsonProtocol dialog;

	/** reference to the observed tuple centre */
	//RespectVM core;

	/** current observation protocol */
	InspectorProtocol protocol;

	boolean nextStep;
	boolean shutdown = false;
	ContextManager manager;

	int ctxId;
	TucsonAgentId agentId;
	TucsonTupleCentreId tcId;

	public InspectorContextSkel(ContextManager man, TucsonProtocol dialog, Node node, ContextDescription p) throws Exception
	{		
		this.dialog = dialog;
		manager = man;

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
		//System.out.println(name);
		inStream = dialog.getInputStream();
		//System.out.println("PROVA");
		outStream = dialog.getOutputStream();				
		
		NewInspectorMsg msg = (NewInspectorMsg) inStream.readObject();	
		tcId = new TucsonTupleCentreId(msg.tcName);
		
		TucsonTCUsers coreInfo = node.resolveCore(msg.tcName);		
		
		if (coreInfo == null)
		{
			throw new TucsonException("Internal error: InspectorContextSkel constructor");
		}
						
		protocol = msg.info;
	}

	protected void log(String st)
	{
		System.out.println("[INSPECTOR CONTEXT] " + st);
	}

	public void run()
	{
		log("inspector booted.");
		try
		{
			TucsonTCContainer.doManagementOperation(TucsonOperation.addInspCode(), tcId, this);
		}
		catch (TucsonOperationNotPossibleException e1)
		{
			e1.printStackTrace();
		}
		catch (TucsonInvalidLogicTupleException e1)
		{
			e1.printStackTrace();
		}
		//core.addInspector(this);
		
		while (!shutdown)
		{
			try
			{
				NodeMsg msg = (NodeMsg) inStream.readObject();
				Class cl = msg.getClass();
				//log("Elena: " + cl.getName());
				Method m = this.getClass().getMethod(msg.action, new Class[] { cl });
				//log("Elena: " + m.getName());
				m.invoke(this, new Object[] { msg });
			}
			catch (Exception e)
			{
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
		
		try
		{
			TucsonTCContainer.doManagementOperation(TucsonOperation.rmvInspCode(), tcId, this);
		}
		catch (TucsonOperationNotPossibleException e1)
		{
			e1.printStackTrace();
		}
		catch (TucsonInvalidLogicTupleException e1)
		{
			e1.printStackTrace();
		}
		
		//core.removeInspector(this);
		manager.shutdownContext(ctxId, agentId);
		log("inspector exit.");
	}

	/**
	 * notifying service invoked by the tuple centre virtual machine when a new
	 * observable event has been produced
	 */
	synchronized public void onInspectableEvent(InspectableEvent ev)
	{
		//System.out.println("Nuovo envento");
		while (protocol.tracing && !nextStep)
		{
			try
			{
				wait();
			}
			catch (Exception ex)
			{
			}
		}
		nextStep = false;
		
		try
		{
			InspectorContextEvent msg = new InspectorContextEvent();
			msg.localTime = System.currentTimeMillis();
			msg.vmTime = ev.getTime();

			if (ev.getType() == InspectableEvent.TYPE_NEWSTATE)
			{
				if (protocol.tsetObservType == InspectorProtocol.PROACTIVE_OBSERVATION)
				{
					//System.out.println("tuple");
					LogicTuple[] ltSet = (LogicTuple[]) TucsonTCContainer.doManagementOperation(TucsonOperation.getTSetCode(), tcId, protocol.tsetFilter);					
					//LogicTuple[] ltSet = core.getTSet(protocol.tsetFilter);
					//LogicTuple[] ltSet = core.getTSet(null);
					
					msg.tuples = new LinkedList();
					if(ltSet != null)
					{
						for(LogicTuple lt: ltSet)
						{
							msg.tuples.add(lt);
						}
						/*if (protocol.tsetFilter == null)
						{
							for(LogicTuple lt: ltSet)
							{
								msg.tuples.add(lt);
							}
						}
						else
						{
							for(LogicTuple lt: ltSet)
							{
								if (protocol.tsetFilter.match(lt))
								{
									msg.tuples.add(lt);
								}
							}
						}*/
					}
				}
				if (protocol.pendingQueryObservType == InspectorProtocol.PROACTIVE_OBSERVATION)
				{			
					//System.out.println("query");
					LogicTuple[] ltSet = (LogicTuple[]) TucsonTCContainer.doManagementOperation(TucsonOperation.getWSetCode(), tcId, protocol.wsetFilter);
					//LogicTuple[] ltSet = core.getWSet(protocol.tsetFilter);
					//LogicTuple[] ltSet = core.getWSet(null);
					
					msg.wnEvents = new LinkedList();					
					if(ltSet != null)
					{
						for(LogicTuple lt: ltSet)
						{
							msg.wnEvents.add(lt);
							//System.out.println(lt);
						}
					}
				}				
				//System.out.println("SEND " + msg.tuples + " " + msg.wnEvents);
				outStream.writeObject(msg);
				outStream.flush();
			}
			else if (ev.getType() == ObservableEventExt.TYPE_REACTIONOK)
			{
				if (protocol.reactionsObservType != InspectorProtocol.NO_OBSERVATION)
				{
					TriggeredReaction zCopy = new TriggeredReaction(null, ((ObservableEventReactionOK) ev).z.getReaction());
					
					//ObservableEventReactionFail evCopy = new ObservableEventReactionFail(ev.getSource(), ev.getType()); 
					msg.reactionOk = zCopy;
					
					outStream.writeObject(msg);
					outStream.flush();
				}
			}
			else if (ev.getType() == ObservableEventExt.TYPE_REACTIONFAIL)
			{
				if (protocol.reactionsObservType != InspectorProtocol.NO_OBSERVATION)
				{
					TriggeredReaction zCopy = new TriggeredReaction(null, ((ObservableEventReactionFail) ev).z.getReaction());
					
					//ObservableEventReactionFail evCopy = new ObservableEventReactionFail(ev.getSource(), ev.getType()); 
					msg.reactionFailed = zCopy;
					//System.out.println("QUIUIIIIIIIIIIIIIII" +msg.reactionFailed.getEvent().getEventProp(getName()).toString());
					outStream.writeObject(msg);
					outStream.flush();
				}
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	// service requested from remote proxy

	/** setting new observation protocol */
	synchronized public void setProtocol(SetProtocolMsg msg)
	{
		boolean wasTracing = protocol.tracing;
		protocol = msg.info;
		
		if (wasTracing)
		{
			notifyAll();
		}
		
		if (!protocol.tracing)
		{
			onInspectableEvent(new InspectableEvent(this, InspectableEvent.TYPE_NEWSTATE));
		}
	}

	/** get a tuple centre set (T set, W set,...) snapshot */
	public void getSnapshot(GetSnapshotMsg m)
	{
		InspectorContextEvent msg = new InspectorContextEvent();
		//synchronized (core)
		//{
			msg.vmTime = System.currentTimeMillis();
			msg.localTime = System.currentTimeMillis();						
			
			if (m.what == GetSnapshotMsg.TSET)
			{
				msg.tuples = new LinkedList();
				LogicTuple[] tSet = null;
				
				try
				{
					tSet = (LogicTuple[]) TucsonTCContainer.doManagementOperation(TucsonOperation.getTSetCode(), tcId, protocol.tsetFilter);
				}
				catch (TucsonOperationNotPossibleException e)
				{
					e.printStackTrace();
				}
				catch (TucsonInvalidLogicTupleException e)
				{
					e.printStackTrace();
				}
				
				for(LogicTuple lt: tSet)
				{
					msg.tuples.add(lt);
				}
			}
			else if (m.what == GetSnapshotMsg.WSET)
			{
				//System.out.println("update");
				LogicTuple[] ltSet = null;
				try
				{
					//System.out.println(protocol.wsetFilter);
					ltSet = (LogicTuple[]) TucsonTCContainer.doManagementOperation(TucsonOperation.getWSetCode(), tcId, protocol.wsetFilter);
				}
				catch (TucsonOperationNotPossibleException e)
				{
					e.printStackTrace();
				}
				catch (TucsonInvalidLogicTupleException e)
				{
					e.printStackTrace();
				}
				
				msg.wnEvents = new LinkedList();
				
				for(LogicTuple lt: ltSet)
				{
					msg.wnEvents.add(lt);
				}							
			}
			try
			{
				outStream.writeObject(msg);
				outStream.flush();
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		//}
	}

	/** reset the tuple centre VM */
	synchronized public void reset(ResetMsg msg)
	{
		//core.reset();
		try
		{
			TucsonTCContainer.doManagementOperation(TucsonOperation.reset(), tcId, null);
		}
		catch (TucsonOperationNotPossibleException e)
		{
			e.printStackTrace();
		}
		catch (TucsonInvalidLogicTupleException e)
		{
			e.printStackTrace();
		}
	}

	/** ask a new step for a tuple centre vm during tracing */
	synchronized public void nextStep(NextStepMsg msg)
	{
		if (protocol.tracing)
		{
			nextStep = true;
			notifyAll();
		}
	}

	/** set a new tuple set */
	synchronized public void setTupleSet(SetTupleSetMsg m)
	{
		try
		{
			//synchronized (core)
			//{				
				//core.removeAllTuples();
				/*Iterator it = m.tupleSet.listIterator();
				while (it.hasNext())
				{
					Tuple tuple = (Tuple) it.next();
				//	core.addTuple(tuple);
				}*/
				TucsonTCContainer.doBlockingOperation(TucsonOperation.set_Code(), agentId, tcId, m.tupleSet);
			//}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	/** set a new tuple set */
	synchronized public void setEventSet(SetEventSetMsg m)
	{
		try
		{
				TucsonTCContainer.doManagementOperation(TucsonOperation.setWSetCode(), tcId, m.eventWnSet);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public synchronized void notify(OutputEvent ev)
	{
	}

	/** set a new Wn event set */
	/*
	 * synchronized public void setEventSet(SetEventSetMsg m){ try {
	 * synchronized (core) { core.getWSet().empty(); Iterator
	 * it=m.eventWnSet.listIterator(); while (it.hasNext()){ Event
	 * event=(Event)it.next(); core.getWSet().add(event); } }
	 * System.out.println("new event set ok."); } catch (Exception ex){
	 * ex.printStackTrace(); } }
	 */

	/** shutdown the inspector */
	public void exit(ShutdownMsg msg)
	{
		exit();
	}

	public void exit()
	{
		shutdown = true;
	}

	public void operationCompleted(TupleCentreOperation op)
	{
		// TODO Auto-generated method stub		
	}
}
