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
package alice.tucson.api;

import alice.logictuple.*;

import alice.tucson.api.exception.LanguageNotFoundException;
import alice.tucson.api.exception.OperationNotAllowedException;
import alice.tucson.api.exception.TucsonInvalidAgentIdException;
import alice.tucson.api.exception.TucsonInvalidTupleCentreIdException;
import alice.tucson.api.exception.UnreachableNodeException;

import alice.tucson.common.*;

import alice.tucson.service.LanguageInfo;
import alice.tucson.service.TucsonAgentLanguageManager;

import alice.tucson.types.*;
import alice.tucson.service.TucsonOperation;
import alice.tucson.service.TucsonOperationCompletionListener;

import alice.tuplecentre.api.OperationTimeOutException;
import alice.tuplecentre.api.Tuple;
import alice.tuplecentre.api.TupleTemplate;

import java.util.*;
import java.io.*;

/**
 * Implementation of a generic agent coordination context
 */
public class AgentContextStub implements TucsonContext
{
	private TucsonAgentId aid;
	private ContextDescription profile;
	private LinkedList<ContextEvent> events;
	private HashMap<String, ControllerSession> controllerSessions;
	private HashMap<Long, TucsonOperation> operations;
	//private int opId;
	
	public AgentContextStub(Object aid, Object language)
	{		
		// imposto il linguaggio per l'agente
		TucsonAgentLanguageManager.useLanguage((String) language);
		
		if (aid.getClass().getName().equals("alice.tucson.types.TucsonAgentId"))
		{
			this.aid = (TucsonAgentId) aid;
		}
		else
		{
			try
			{
				this.aid = new TucsonAgentId(aid);
			}
			catch (TucsonInvalidAgentIdException e)
			{
			}
		}

		profile = new ContextDescription();
		//opId = 0;
		events = new LinkedList<ContextEvent>();
		controllerSessions = new HashMap<String, ControllerSession>();
		operations = new HashMap<Long, TucsonOperation>();
	}

	// ACC Interface
	
	// operazioni non bloccanti senza linguaggio
	
	public ITucsonOperation out(Object tid, LogicTuple tuple, TucsonOperationCompletionListener l) 
	throws OperationNotAllowedException, UnreachableNodeException, LanguageNotFoundException
	{
		return doNonBlockingOperation(TucsonOperation.outCode(), tid, tuple, l, TucsonAgentLanguageManager.getCurLanguage().getName());
	}
	
	public ITucsonOperation in(Object tid, LogicTuple tuple, TucsonOperationCompletionListener l) 
	throws OperationNotAllowedException, UnreachableNodeException, LanguageNotFoundException
	{
		return doNonBlockingOperation(TucsonOperation.inCode(), tid, tuple, l, TucsonAgentLanguageManager.getCurLanguage().getName());
	}
	
	public ITucsonOperation rd(Object tid, LogicTuple tuple, TucsonOperationCompletionListener l) 
	throws OperationNotAllowedException, UnreachableNodeException, LanguageNotFoundException
	{
		return doNonBlockingOperation(TucsonOperation.rdCode(), tid, tuple, l, TucsonAgentLanguageManager.getCurLanguage().getName());
	}
	
	public ITucsonOperation inp(Object tid, LogicTuple tuple, TucsonOperationCompletionListener l) 
	throws OperationNotAllowedException, UnreachableNodeException, LanguageNotFoundException
	{
		return doNonBlockingOperation(TucsonOperation.inpCode(), tid, tuple, l, TucsonAgentLanguageManager.getCurLanguage().getName());
	}
	
	public ITucsonOperation rdp(Object tid, LogicTuple tuple, TucsonOperationCompletionListener l) 
	throws OperationNotAllowedException, UnreachableNodeException, LanguageNotFoundException
	{
		return doNonBlockingOperation(TucsonOperation.rdpCode(), tid, tuple, l, TucsonAgentLanguageManager.getCurLanguage().getName());
	}
	
	public ITucsonOperation setSpec(Object tid, String spec, TucsonOperationCompletionListener l) 
	throws OperationNotAllowedException, UnreachableNodeException, LanguageNotFoundException
	{
		LogicTuple specT = new LogicTuple("spec", new Value(spec));
		return doNonBlockingOperation(TucsonOperation.set_specCode(), tid, specT, l, TucsonAgentLanguageManager.getCurLanguage().getName());
	}
	
	public ITucsonOperation getSpec(Object tid, TucsonOperationCompletionListener l) 
	throws OperationNotAllowedException, UnreachableNodeException, LanguageNotFoundException
	{		
		LogicTuple spec = new LogicTuple("spec");
		return doNonBlockingOperation(TucsonOperation.get_specCode(), tid, spec, l, TucsonAgentLanguageManager.getCurLanguage().getName());
	}
	
	public ITucsonOperation out_s(Object tid, LogicTuple tuple, TucsonOperationCompletionListener l)
	throws OperationNotAllowedException, UnreachableNodeException, LanguageNotFoundException
	{
		return doNonBlockingOperation(TucsonOperation.out_sCode(), tid, tuple, l, TucsonAgentLanguageManager.getCurLanguage().getName());
	}
	
	public ITucsonOperation in_s(Object tid, LogicTuple tuple, TucsonOperationCompletionListener l)
	throws OperationNotAllowedException, UnreachableNodeException, LanguageNotFoundException
	{
		return doNonBlockingOperation(TucsonOperation.in_sCode(), tid, tuple, l, TucsonAgentLanguageManager.getCurLanguage().getName());
	}
	
	public ITucsonOperation rd_s(Object tid, LogicTuple tuple, TucsonOperationCompletionListener l)
	throws OperationNotAllowedException, UnreachableNodeException, LanguageNotFoundException
	{
		return doNonBlockingOperation(TucsonOperation.rd_sCode(), tid, tuple, l, TucsonAgentLanguageManager.getCurLanguage().getName());
	}
	
	public ITucsonOperation inp_s(Object tid, LogicTuple tuple, TucsonOperationCompletionListener l)
	throws OperationNotAllowedException, UnreachableNodeException, LanguageNotFoundException
	{
		return doNonBlockingOperation(TucsonOperation.inp_sCode(), tid, tuple, l, TucsonAgentLanguageManager.getCurLanguage().getName());
	}
	
	public ITucsonOperation rdp_s(Object tid, LogicTuple tuple, TucsonOperationCompletionListener l)
	throws OperationNotAllowedException, UnreachableNodeException, LanguageNotFoundException
	{
		return doNonBlockingOperation(TucsonOperation.rdp_sCode(), tid, tuple, l, TucsonAgentLanguageManager.getCurLanguage().getName());
	}
	
	// operazioni non bloccanti con linguaggio
	
	public ITucsonOperation out(Object tid, LogicTuple tuple, TucsonOperationCompletionListener l, Object language) 
	throws OperationNotAllowedException, UnreachableNodeException, LanguageNotFoundException
	{
		return doNonBlockingOperation(TucsonOperation.outCode(), tid, tuple, l, (String) language);
	}
	
	public ITucsonOperation in(Object tid, LogicTuple tuple, TucsonOperationCompletionListener l, Object language) 
	throws OperationNotAllowedException, UnreachableNodeException, LanguageNotFoundException
	{
		return doNonBlockingOperation(TucsonOperation.inCode(), tid, tuple, l, (String) language);
	}
	
	public ITucsonOperation rd(Object tid, LogicTuple tuple, TucsonOperationCompletionListener l, Object language) 
	throws OperationNotAllowedException, UnreachableNodeException, LanguageNotFoundException
	{
		return doNonBlockingOperation(TucsonOperation.rdCode(), tid, tuple, l, (String) language);
	}
	
	public ITucsonOperation inp(Object tid, LogicTuple tuple, TucsonOperationCompletionListener l, Object language) 
	throws OperationNotAllowedException, UnreachableNodeException, LanguageNotFoundException
	{
		return doNonBlockingOperation(TucsonOperation.inpCode(), tid, tuple, l, (String) language);
	}
	
	public ITucsonOperation rdp(Object tid, LogicTuple tuple, TucsonOperationCompletionListener l, Object language) 
	throws OperationNotAllowedException, UnreachableNodeException, LanguageNotFoundException
	{
		return doNonBlockingOperation(TucsonOperation.rdpCode(), tid, tuple, l, (String) language);
	}
	
	public ITucsonOperation setSpec(Object tid, String spec, TucsonOperationCompletionListener l, Object language) 
	throws OperationNotAllowedException, UnreachableNodeException, LanguageNotFoundException
	{
		LogicTuple specT = new LogicTuple("spec", new Value(spec));
		return doNonBlockingOperation(TucsonOperation.set_specCode(), tid, specT, l, (String) language);
	}
	
	public ITucsonOperation getSpec(Object tid, TucsonOperationCompletionListener l, Object language) 
	throws OperationNotAllowedException, UnreachableNodeException, LanguageNotFoundException
	{
		LogicTuple spec = new LogicTuple("spec");
		return doNonBlockingOperation(TucsonOperation.get_specCode(), tid, spec, l, (String) language);
	}
	
	public ITucsonOperation out_s(Object tid, LogicTuple tuple, TucsonOperationCompletionListener l, Object language) 
	throws OperationNotAllowedException, UnreachableNodeException, LanguageNotFoundException
	{
		return doNonBlockingOperation(TucsonOperation.out_sCode(), tid, tuple, l, (String) language);
	}
	
	public ITucsonOperation in_s(Object tid, LogicTuple tuple, TucsonOperationCompletionListener l, Object language) 
	throws OperationNotAllowedException, UnreachableNodeException, LanguageNotFoundException
	{
		return doNonBlockingOperation(TucsonOperation.in_sCode(), tid, tuple, l, (String) language);
	}
	
	public ITucsonOperation rd_s(Object tid, LogicTuple tuple, TucsonOperationCompletionListener l, Object language) 
	throws OperationNotAllowedException, UnreachableNodeException, LanguageNotFoundException
	{
		return doNonBlockingOperation(TucsonOperation.rd_sCode(), tid, tuple, l, (String) language);
	}
	
	public ITucsonOperation inp_s(Object tid, LogicTuple tuple, TucsonOperationCompletionListener l, Object language) 
	throws OperationNotAllowedException, UnreachableNodeException, LanguageNotFoundException
	{
		return doNonBlockingOperation(TucsonOperation.inp_sCode(), tid, tuple, l, (String) language);
	}
	
	public ITucsonOperation rdp_s(Object tid, LogicTuple tuple, TucsonOperationCompletionListener l, Object language) 
	throws OperationNotAllowedException, UnreachableNodeException, LanguageNotFoundException
	{
		return doNonBlockingOperation(TucsonOperation.rdp_sCode(), tid, tuple, l, (String) language);
	}
	
	public ITucsonOperation doNonBlockingOperation(int type, Object tid, LogicTuple t, 
			TucsonOperationCompletionListener l, String language)			 
	throws OperationNotAllowedException, UnreachableNodeException, LanguageNotFoundException
	{
		TucsonTupleCentreId tcid = null;
		
		if (tid.getClass().getName().equals("alice.tucson.types.TucsonTupleCentreId"))
		{
			tcid = (TucsonTupleCentreId) tid;
		}
		else
		{
			try
			{
				tcid = new TucsonTupleCentreId(tid);
			}
			catch (TucsonInvalidTupleCentreIdException e)
			{
			}
		}
		
		try
		{
			return doOperation(tcid, type, t, l, language);
		}
		catch (OperationNotAllowedException e)
		{
			throw new OperationNotAllowedException();
		}
		catch (UnreachableNodeException e)
		{
			throw new UnreachableNodeException();
		}
		catch (LanguageNotFoundException e)
		{
			throw new LanguageNotFoundException();
		}		
	}
	
	// operazioni bloccanti senza linguaggio
	
	public LogicTuple out(Object tid, LogicTuple tuple, Long ms) throws OperationNotAllowedException, UnreachableNodeException,
	LanguageNotFoundException, OperationTimeOutException
	{
		return doBlockingOperation(TucsonOperation.outCode(), tid, tuple, null, TucsonAgentLanguageManager.getCurLanguage().getName());
	}
	
	public LogicTuple in(Object tid, LogicTuple tuple, Long ms) throws OperationNotAllowedException, UnreachableNodeException,
	LanguageNotFoundException, OperationTimeOutException
	{
		return doBlockingOperation(TucsonOperation.inCode(), tid, tuple, null, TucsonAgentLanguageManager.getCurLanguage().getName());
	}
	
	public LogicTuple rd(Object tid, LogicTuple tuple, Long ms) throws OperationNotAllowedException, UnreachableNodeException,
	LanguageNotFoundException, OperationTimeOutException
	{
		return doBlockingOperation(TucsonOperation.rdCode(), tid, tuple, null, TucsonAgentLanguageManager.getCurLanguage().getName());
	}
	
	public LogicTuple inp(Object tid, LogicTuple tuple, Long ms) throws OperationNotAllowedException, UnreachableNodeException,
	LanguageNotFoundException, OperationTimeOutException
	{
		return doBlockingOperation(TucsonOperation.inpCode(), tid, tuple, null, TucsonAgentLanguageManager.getCurLanguage().getName());
	}
	
	public LogicTuple rdp(Object tid, LogicTuple tuple, Long ms) throws OperationNotAllowedException, UnreachableNodeException,
	LanguageNotFoundException, OperationTimeOutException
	{
		return doBlockingOperation(TucsonOperation.rdpCode(), tid, tuple, null, TucsonAgentLanguageManager.getCurLanguage().getName());
	}
	
	public LogicTuple setSpec(Object tid, String spec, Long ms) throws OperationNotAllowedException,
	UnreachableNodeException, LanguageNotFoundException, OperationTimeOutException
	{		
		LogicTuple specT = new LogicTuple("spec", new Value(spec));
		return doBlockingOperation(TucsonOperation.set_specCode(), tid, specT, null, TucsonAgentLanguageManager.getCurLanguage().getName());
	}
	
	public String getSpec(Object tid, Long ms) throws OperationNotAllowedException, UnreachableNodeException,
	LanguageNotFoundException, OperationTimeOutException, InvalidTupleOperationException
	{		
		LogicTuple spec = new LogicTuple("spec");		
		LogicTuple tupleRes = doBlockingOperation(TucsonOperation.get_specCode(), tid, spec, null, TucsonAgentLanguageManager.getCurLanguage().getName());
		//System.out.println(tupleRes);
		return tupleRes.getArg(0).getName();		
	}

	public LogicTuple out_s(Object tid, LogicTuple tuple, Long ms) throws OperationNotAllowedException, UnreachableNodeException,
	LanguageNotFoundException, OperationTimeOutException
	{
		return doBlockingOperation(TucsonOperation.out_sCode(), tid, tuple, null, TucsonAgentLanguageManager.getCurLanguage().getName());
	}
	
	public LogicTuple in_s(Object tid, LogicTuple tuple, Long ms) throws OperationNotAllowedException, UnreachableNodeException,
	LanguageNotFoundException, OperationTimeOutException
	{
		return doBlockingOperation(TucsonOperation.in_sCode(), tid, tuple, null, TucsonAgentLanguageManager.getCurLanguage().getName());
	}
	
	public LogicTuple rd_s(Object tid, LogicTuple tuple, Long ms) throws OperationNotAllowedException, UnreachableNodeException,
	LanguageNotFoundException, OperationTimeOutException
	{
		return doBlockingOperation(TucsonOperation.rd_sCode(), tid, tuple, null, TucsonAgentLanguageManager.getCurLanguage().getName());
	}
	
	public LogicTuple inp_s(Object tid, LogicTuple tuple, Long ms) throws OperationNotAllowedException, UnreachableNodeException,
	LanguageNotFoundException, OperationTimeOutException
	{
		return doBlockingOperation(TucsonOperation.inp_sCode(), tid, tuple, null, TucsonAgentLanguageManager.getCurLanguage().getName());
	}
	
	public LogicTuple rdp_s(Object tid, LogicTuple tuple, Long ms) throws OperationNotAllowedException, UnreachableNodeException,
	LanguageNotFoundException, OperationTimeOutException
	{
		return doBlockingOperation(TucsonOperation.rdp_sCode(), tid, tuple, null, TucsonAgentLanguageManager.getCurLanguage().getName());
	}
	
	// operazioni bloccanti con linguaggio
	
	public LogicTuple out(Object tid, LogicTuple tuple, Long ms, Object language) throws OperationNotAllowedException, UnreachableNodeException,
	LanguageNotFoundException, OperationTimeOutException
	{
		return doBlockingOperation(TucsonOperation.outCode(), tid, tuple, null, (String) language);
	}
	
	public LogicTuple in(Object tid, LogicTuple tuple, Long ms, Object language) throws OperationNotAllowedException, UnreachableNodeException,
	LanguageNotFoundException, OperationTimeOutException
	{
		return doBlockingOperation(TucsonOperation.inCode(), tid, tuple, null, (String) language);
	}
	
	public LogicTuple rd(Object tid, LogicTuple tuple, Long ms, Object language) throws OperationNotAllowedException, UnreachableNodeException,
	LanguageNotFoundException, OperationTimeOutException
	{
		return doBlockingOperation(TucsonOperation.rdCode(), tid, tuple, null, (String) language);
	}
	
	public LogicTuple inp(Object tid, LogicTuple tuple, Long ms, Object language) throws OperationNotAllowedException, UnreachableNodeException,
	LanguageNotFoundException, OperationTimeOutException
	{
		return doBlockingOperation(TucsonOperation.inpCode(), tid, tuple, null, (String) language);
	}
	
	public LogicTuple rdp(Object tid, LogicTuple tuple, Long ms, Object language) throws OperationNotAllowedException, UnreachableNodeException,
	LanguageNotFoundException, OperationTimeOutException
	{
		return doBlockingOperation(TucsonOperation.rdpCode(), tid, tuple, null, (String) language);
	}
	
	public LogicTuple setSpec(Object tid, String spec, Long ms, Object language) throws OperationNotAllowedException, UnreachableNodeException,
	LanguageNotFoundException, OperationTimeOutException
	{
		LogicTuple specT = new LogicTuple("spec", new Value(spec));
		return doBlockingOperation(TucsonOperation.set_specCode(), tid, specT, null, (String) language);
	}
	
	public String getSpec(Object tid, Long ms, Object language) throws OperationNotAllowedException, UnreachableNodeException,
	LanguageNotFoundException, OperationTimeOutException, InvalidTupleOperationException
	{
		LogicTuple spec = new LogicTuple("spec");		
		LogicTuple tupleRes = doBlockingOperation(TucsonOperation.get_specCode(), tid, spec, null, (String) language);
		return tupleRes.getArg(0).getName();	
	}
	
	public LogicTuple out_s(Object tid, LogicTuple tuple, Long ms, Object language) throws OperationNotAllowedException, UnreachableNodeException,
	LanguageNotFoundException, OperationTimeOutException
	{
		return doBlockingOperation(TucsonOperation.out_sCode(), tid, tuple, null, (String) language);
	}
	
	public LogicTuple in_s(Object tid, LogicTuple tuple, Long ms, Object language) throws OperationNotAllowedException, UnreachableNodeException,
	LanguageNotFoundException, OperationTimeOutException
	{
		return doBlockingOperation(TucsonOperation.in_sCode(), tid, tuple, null, (String) language);
	}
	
	public LogicTuple rd_s(Object tid, LogicTuple tuple, Long ms, Object language) throws OperationNotAllowedException, UnreachableNodeException,
	LanguageNotFoundException, OperationTimeOutException
	{
		return doBlockingOperation(TucsonOperation.rd_sCode(), tid, tuple, null, (String) language);
	}
	
	public LogicTuple inp_s(Object tid, LogicTuple tuple, Long ms, Object language) throws OperationNotAllowedException, UnreachableNodeException,
	LanguageNotFoundException, OperationTimeOutException
	{
		return doBlockingOperation(TucsonOperation.inp_sCode(), tid, tuple, null, (String) language);
	}
	
	public LogicTuple rdp_s(Object tid, LogicTuple tuple, Long ms, Object language) throws OperationNotAllowedException, UnreachableNodeException,
	LanguageNotFoundException, OperationTimeOutException
	{
		return doBlockingOperation(TucsonOperation.rdp_sCode(), tid, tuple, null, (String) language);
	}
	
	public LogicTuple doBlockingOperation(int type, Object tid, LogicTuple t, Long ms, String language)
	throws OperationNotAllowedException, UnreachableNodeException, LanguageNotFoundException, OperationTimeOutException
	{		
		TucsonTupleCentreId tcid = null;
		
		if (tid.getClass().getName().equals("alice.tucson.types.TucsonTupleCentreId"))
		{
			tcid = (TucsonTupleCentreId) tid;
		}
		else
		{
			try
			{
				tcid = new TucsonTupleCentreId(tid);
			}
			catch (TucsonInvalidTupleCentreIdException e)
			{
			}
		}
		
		try
		{			
			ITucsonOperation op = doOperation(tcid, type, t, null, (String) language);
			
			if(ms == null)
				op.waitForOperationCompletion();
			else
				op.waitForOperationCompletion(ms);
			
			return op.getLogicTupleResult();
		}
		catch (OperationNotAllowedException e)
		{
			throw new OperationNotAllowedException();
		}
		catch (UnreachableNodeException e)
		{
			throw new UnreachableNodeException();
		}
		catch (LanguageNotFoundException e)
		{
			throw new LanguageNotFoundException();
		}
	}
	
	public synchronized ITucsonOperation doOperation(TucsonTupleCentreId tcid, int type, LogicTuple t, TucsonOperationCompletionListener l, String language)
			throws OperationNotAllowedException, UnreachableNodeException, LanguageNotFoundException // ??? controllare
																									 // le eccezioni
	{				
		// imposto il linguaggio per l'agente
		TucsonAgentLanguageManager.useLanguage(language);
				
		// First phase: syntax check before going on remote node
		log("Syntax check");
		
		if (!tcid.checkSyntax() || !aid.checkSyntax())
		{
			throw new OperationNotAllowedException();
		}

		int nTry = 1;
		do
		{
			// Second phase: negotiation (method getSession)
			log("Negotiation with " + tcid.toString());
			
			TucsonProtocol session = getSession(tcid, language);
			ObjectOutputStream outStream = session.getOutputStream();
			
			try
			{						
				// creo un'operazione TuCSoN
				TucsonOperation op = null;					
					
				if((type == TucsonOperation.outCode()) | (type == TucsonOperation.out_sCode())
						 | (type == TucsonOperation.set_specCode()))
					op = new TucsonOperation(type, (Tuple) t, l);
				else
					op = new TucsonOperation(type, (TupleTemplate) t, l);
				
				//System.out.println("ELENA " + op.getLogicTupleArgument());
				//opId++;	
				operations.put(op.getId(), op);
				
				log("Remote call & eventually translation");
				
				TucsonMsgRequest msg = new TucsonMsgRequest(op.getId(), op.getType(), 
						tcid.translateSyntax(session.getLanguageInfo()), op.getLogicTupleArgument());
				
				TucsonMsgRequest.write(outStream, msg);
				outStream.flush();
				
				log("Request done: " + op.getType() + " " + op.getLogicTupleArgument() + " " + op.getId());
				//log("Operation executed succesfully");
				
				return op;
				//return new TucsonOpId(opId);
			}
			catch (IOException ex)
			{
				ex.printStackTrace();
			}
			nTry++;
		}
		while (nTry < 3);
		throw new UnreachableNodeException();
	}

	public synchronized void exit() throws OperationNotAllowedException
	{
		//Object tid = new Object();
		//LogicTuple t = new LogicTuple();
		Iterator<ControllerSession> it = controllerSessions.values().iterator();
		
		while (it.hasNext())
		{
			try
			{
				ControllerSession cs = (ControllerSession) it.next();
				TucsonProtocol info = cs.getSession();
				Controller contr = cs.getController();
				contr.setStop();
				ObjectOutputStream outStream = info.getOutputStream();
				
				TucsonOperation op = new TucsonOperation(TucsonOperation.exitCode(), (TupleTemplate) null, null);				
				operations.put(op.getId(), op);
				
				//opId++;
				TucsonMsgRequest exit = new TucsonMsgRequest((int) op.getId(), op.getType(), null,
						op.getLogicTupleArgument());
				TucsonMsgRequest.write(outStream, exit);
				outStream.flush();
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}
	}

	public HashMap<String, LanguageInfo> getInfo()
	{
		return TucsonAgentLanguageManager.getAvailableLanguages();
	}

	private TucsonProtocol getSession(TucsonTupleCentreId tid, String language) throws OperationNotAllowedException,
			UnreachableNodeException, LanguageNotFoundException
	{				
		String node = alice.util.Tools.removeApices(tid.getNode());
		// Discriminate between remote node
		ControllerSession tc = controllerSessions.get(node);
		if (tc != null)
		{			
			return tc.getSession();
		}
		else
		{
			try
			{
				profile.setProperty("agent-identity", aid.toString());
				profile.setProperty("agent-role", "user");

				int idx = node.indexOf(':');
				int port = 20504;
				try
				{
					if (idx != -1)
					{
						port = Integer.parseInt(node.substring(idx));
						node = node.substring(0, idx - 1);
					}
				}
				catch (Exception ex)
				{
					throw new UnreachableNodeException();
				}
				TucsonProtocol dialog = new TucsonProtocolTCP(node, port);
				dialog.sendEnterRequest(profile);
				dialog.receiveEnterRequestAnswer();
				if (dialog.isEnterRequestAccepted())
				{
					ObjectInputStream din = dialog.getInputStream();
					Controller contr = new Controller(din);
					ControllerSession cs = new ControllerSession(contr, dialog);
					controllerSessions.put(node, cs);
					contr.start();
					return dialog;
				}
			}
			catch (UnreachableNodeException ex)
			{
				throw new UnreachableNodeException();
			}
			catch (Exception ex2)
			{
				throw new OperationNotAllowedException();
			}
		}
		return null;
	}

	private void postEvent(ContextEvent ev)
	{
		synchronized (events)
		{
			events.addLast(ev);
			// log("notified ev " + ev.getTuple() + " - op id: " + ev.getOpId().getId());
			//events.notifyAll();
		}
	}

	private void log(String msg)
	{
		System.out.println("[ACC STUB] " + msg);
	}

	public class Controller extends Thread
	{
		private boolean stop;
		private ObjectInputStream in;

		Controller(ObjectInputStream in)
		{
			this.in = in;
			stop = false;
		}

		public void run()
		{
			// log("Controller started.");
			ContextEvent ev = null;
			while (!isStopped())
			{
				try
				{
					TucsonMsgReply msg = TucsonMsgReply.read(in);
					// log("received msg "+msg);
					boolean ok = msg.isAllowed();
					
					if (ok)
					{
						int type = msg.getType();
						if (type == TucsonOperation.inCode() || type == TucsonOperation.rdCode() || type == TucsonOperation.inpCode()
								|| type == TucsonOperation.rdpCode() || type == TucsonOperation.in_sCode()
								|| type == TucsonOperation.rd_sCode() || type == TucsonOperation.inp_sCode()
								|| type == TucsonOperation.rdp_sCode())
						{

							boolean succeeded = msg.isSuccess();
							if (succeeded)
							{
								LogicTuple tupleReq = msg.getTupleRequested();
								LogicTuple tupleRes = msg.getTupleResult();
								LogicTuple res = unify(tupleReq, tupleRes);
								ev = new ContextEvent(new TucsonOpId(msg.getId()), ok, true, res);
							}
							else
							{
								ev = new ContextEvent(new TucsonOpId(msg.getId()), ok, false);
							}
						}
						else if (type == TucsonOperation.get_specCode())
						{
							LogicTuple tupleRes = msg.getTupleResult();
							String spec = tupleRes.getArg(0).getName();
							//System.out.println(spec);
							ev = new ContextEvent(new TucsonOpId(msg.getId()), ok, msg.isSuccess(), spec);
						}
						else if (type == TucsonOperation.set_specCode() || type == TucsonOperation.outCode()
								|| type == TucsonOperation.out_sCode())
						{
							ev = new ContextEvent(new TucsonOpId(msg.getId()), ok, msg.isSuccess());
						}
						else if (type == TucsonOperation.exitCode())
						{
							setStop();
							break;
						}
					}
					else
					{
						ev = new ContextEvent(new TucsonOpId(msg.getId()), false, false);
					}
					
					log("posting event " + ev.getOpId().getId());
					TucsonOperation op = operations.remove(msg.getId());
					
					op.setResult(msg.getTupleResult());	
					//System.out.println(ev.getTuple());
					op.notifyCompletion(ev.operationSucceeded());
					
					postEvent(ev);
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
					setStop();
				}
			}
		}

		synchronized boolean isStopped()
		{
			return stop;
		}

		synchronized void setStop()
		{
			stop = true;
		}

		LogicTuple unify(alice.tuplecentre.api.TupleTemplate template, alice.tuplecentre.api.Tuple tuple)
		{
			boolean res = template.propagate(tuple);
			if (res)
			{
				return (LogicTuple) template;
			}
			else
			{
				return null;
			}
		}
	}

	public class ControllerSession
	{
		private Controller controller;
		private TucsonProtocol session;

		ControllerSession(Controller c, TucsonProtocol s)
		{
			controller = c;
			session = s;
		}

		public Controller getController()
		{
			return controller;
		}

		public TucsonProtocol getSession()
		{
			return session;
		}
	}
}
