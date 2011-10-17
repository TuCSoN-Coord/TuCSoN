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

import java.util.*;

import java.io.*;

import java.util.List;

import alice.tuplecentre.api.Tuple;
import alice.tuplecentre.api.TupleTemplate;
import alice.tuplecentre.core.OperationTimeOutException;
import alice.tuprolog.Prolog;
import alice.tuprolog.lib.InvalidObjectIdException;

import alice.tucson.api.exception.TucsonOperationNotPossibleException;
import alice.tucson.api.exception.TucsonInvalidAgentIdException;
import alice.tucson.api.exception.TucsonInvalidTupleCentreIdException;
import alice.tucson.api.exception.UnreachableNodeException;

import alice.tucson.common.*;

import alice.tucson.service.LanguageInfo;
import alice.tucson.service.TucsonOperation;
import alice.tucson.service.TucsonOperationCompletionListener;

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
	private ArrayList<Long> operationExpired;
	@SuppressWarnings("unused")
	private LanguageInfo chosenLanguage;
	
	public AgentContextStub(Object aid, LanguageInfo chosenLanguage)
	{		
		// Imposto il linguaggio per l'agente
		//TucsonAgentLanguageManager.useLanguage((String) language);
		this.chosenLanguage = chosenLanguage; 
		//System.out.println(aid.getClass().getName());
		if (aid.getClass().getName().equals("alice.tucson.api.TucsonAgentId"))
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
		events = new LinkedList<ContextEvent>();
		controllerSessions = new HashMap<String, ControllerSession>();
		operations = new HashMap<Long, TucsonOperation>();
		operationExpired = new ArrayList<Long>();
	}

	// ACC Interface
	
	// operazioni non bloccanti
	
	public ITucsonOperation out(Object tid, LogicTuple tuple, TucsonOperationCompletionListener l) 
	throws TucsonOperationNotPossibleException, UnreachableNodeException
	{
		return doNonBlockingOperation(TucsonOperation.outCode(), tid, tuple, l);
	}
	
	public ITucsonOperation in(Object tid, LogicTuple tuple, TucsonOperationCompletionListener l) 
	throws TucsonOperationNotPossibleException, UnreachableNodeException
	{
		return doNonBlockingOperation(TucsonOperation.inCode(), tid, tuple, l);
	}
	
	public ITucsonOperation rd(Object tid, LogicTuple tuple, TucsonOperationCompletionListener l) 
	throws TucsonOperationNotPossibleException, UnreachableNodeException
	{
		return doNonBlockingOperation(TucsonOperation.rdCode(), tid, tuple, l);
	}
	
	public ITucsonOperation inp(Object tid, LogicTuple tuple, TucsonOperationCompletionListener l) 
	throws TucsonOperationNotPossibleException, UnreachableNodeException
	{
		return doNonBlockingOperation(TucsonOperation.inpCode(), tid, tuple, l);
	}
	
	public ITucsonOperation rdp(Object tid, LogicTuple tuple, TucsonOperationCompletionListener l) 
	throws TucsonOperationNotPossibleException, UnreachableNodeException
	{
		return doNonBlockingOperation(TucsonOperation.rdpCode(), tid, tuple, l);
	}		
	
	public ITucsonOperation out_s(Object tid, LogicTuple tuple, TucsonOperationCompletionListener l)
	throws TucsonOperationNotPossibleException, UnreachableNodeException
	{
		return doNonBlockingOperation(TucsonOperation.out_sCode(), tid, tuple, l);
	}
	
	public ITucsonOperation in_s(Object tid, LogicTuple tuple, TucsonOperationCompletionListener l)
	throws TucsonOperationNotPossibleException, UnreachableNodeException
	{
		return doNonBlockingOperation(TucsonOperation.in_sCode(), tid, tuple, l);
	}
	
	public ITucsonOperation rd_s(Object tid, LogicTuple tuple, TucsonOperationCompletionListener l)
	throws TucsonOperationNotPossibleException, UnreachableNodeException
	{
		return doNonBlockingOperation(TucsonOperation.rd_sCode(), tid, tuple, l);
	}
	
	public ITucsonOperation inp_s(Object tid, LogicTuple tuple, TucsonOperationCompletionListener l)
	throws TucsonOperationNotPossibleException, UnreachableNodeException
	{
		return doNonBlockingOperation(TucsonOperation.inp_sCode(), tid, tuple, l);
	}
	
	public ITucsonOperation rdp_s(Object tid, LogicTuple tuple, TucsonOperationCompletionListener l)
	throws TucsonOperationNotPossibleException, UnreachableNodeException
	{
		return doNonBlockingOperation(TucsonOperation.rdp_sCode(), tid, tuple, l);
	}
	
	public ITucsonOperation doNonBlockingOperation(int type, Object tid, LogicTuple t, 
			TucsonOperationCompletionListener l)			 
	throws TucsonOperationNotPossibleException, UnreachableNodeException
	{
		TucsonTupleCentreId tcid = null;
		
		if (tid.getClass().getName().equals("alice.tucson.api.TucsonTupleCentreId"))
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
				e.printStackTrace();
				return null;
			}
		}
		
		try
		{
			return doOperation(tcid, type, t, l);
		}
		catch (TucsonOperationNotPossibleException e)
		{
			throw new TucsonOperationNotPossibleException();
		}
		catch (UnreachableNodeException e)
		{
			throw new UnreachableNodeException();
		}	
	}
	
	// operazioni bloccanti
	
	public LogicTuple out(Object tid, LogicTuple tuple, Long ms) throws TucsonOperationNotPossibleException, UnreachableNodeException,
	OperationTimeOutException
	{
		
		LogicTuple res = (LogicTuple) doBlockingOperation(TucsonOperation.outCode(), tid, tuple, ms);		
		return res;
	}
	
	public LogicTuple in(Object tid, LogicTuple tuple, Long ms) throws TucsonOperationNotPossibleException, UnreachableNodeException,
	OperationTimeOutException
	{
		return (LogicTuple) doBlockingOperation(TucsonOperation.inCode(), tid, tuple, ms);
	}
	
	public LogicTuple rd(Object tid, LogicTuple tuple, Long ms) throws TucsonOperationNotPossibleException, UnreachableNodeException,
	OperationTimeOutException
	{
		return (LogicTuple) doBlockingOperation(TucsonOperation.rdCode(), tid, tuple, ms);
	}
	
	public LogicTuple inp(Object tid, LogicTuple tuple, Long ms) throws TucsonOperationNotPossibleException, UnreachableNodeException,
	OperationTimeOutException
	{
		return (LogicTuple) doBlockingOperation(TucsonOperation.inpCode(), tid, tuple, ms);
	}
	
	public LogicTuple rdp(Object tid, LogicTuple tuple, Long ms) throws TucsonOperationNotPossibleException, UnreachableNodeException,
	OperationTimeOutException
	{
		return (LogicTuple) doBlockingOperation(TucsonOperation.rdpCode(), tid, tuple, ms);
	}	
	
	@SuppressWarnings("unchecked")
	public List<LogicTuple> get(Object tid, Long ms) throws TucsonOperationNotPossibleException, UnreachableNodeException,
	OperationTimeOutException
	{
		return (List<LogicTuple>) doBlockingOperation(TucsonOperation.get_Code(), tid, null, ms);
	}

	public LogicTuple out_s(Object tid, LogicTuple tuple, Long ms) throws TucsonOperationNotPossibleException, UnreachableNodeException,
	OperationTimeOutException
	{
		return (LogicTuple) doBlockingOperation(TucsonOperation.out_sCode(), tid, tuple, ms);
	}
	
	public LogicTuple in_s(Object tid, LogicTuple tuple, Long ms) throws TucsonOperationNotPossibleException, UnreachableNodeException,
	OperationTimeOutException
	{
		return (LogicTuple) doBlockingOperation(TucsonOperation.in_sCode(), tid, tuple, ms);
	}
	
	public LogicTuple rd_s(Object tid, LogicTuple tuple, Long ms) throws TucsonOperationNotPossibleException, UnreachableNodeException,
	OperationTimeOutException
	{
		return (LogicTuple) doBlockingOperation(TucsonOperation.rd_sCode(), tid, tuple, ms);
	}
	
	public LogicTuple inp_s(Object tid, LogicTuple tuple, Long ms) throws TucsonOperationNotPossibleException, UnreachableNodeException,
	OperationTimeOutException
	{
		return (LogicTuple) doBlockingOperation(TucsonOperation.inp_sCode(), tid, tuple, ms);
	}
	
	public LogicTuple rdp_s(Object tid, LogicTuple tuple, Long ms) throws TucsonOperationNotPossibleException, UnreachableNodeException,
	OperationTimeOutException
	{
		return (LogicTuple) doBlockingOperation(TucsonOperation.rdp_sCode(), tid, tuple, ms);
	}
	
	public LogicTuple setSpec(Object tid, String spec, Long ms) throws TucsonOperationNotPossibleException,
	UnreachableNodeException, OperationTimeOutException
	{		
		LogicTuple specT = new LogicTuple("spec", new Value(spec));
		return (LogicTuple) doBlockingOperation(TucsonOperation.set_specCode(), tid, specT, ms);
	}
	
	public String getSpec(Object tid, Long ms) throws TucsonOperationNotPossibleException, UnreachableNodeException,
	OperationTimeOutException, InvalidTupleOperationException
	{		
		LogicTuple spec = new LogicTuple("spec");		
		LogicTuple tupleRes = (LogicTuple) doBlockingOperation(TucsonOperation.get_specCode(), tid, spec, ms);
		return tupleRes.getArg(0).getName().replace('\'', ' ').trim();		
	}
	
	public Object doBlockingOperation(int type, Object tid, LogicTuple t, Long ms)
	throws TucsonOperationNotPossibleException, UnreachableNodeException, OperationTimeOutException
	{		
		TucsonTupleCentreId tcid = null;
		
		if (tid.getClass().getName().equals("alice.tucson.api.TucsonTupleCentreId"))
		{
			tcid = (TucsonTupleCentreId) tid;
			System.out.println("TC_ID" + tcid);
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
		
		ITucsonOperation op = null;
		
		try
		{			
			op = doOperation(tcid, type, t, null);

			if(ms == null)
				op.waitForOperationCompletion();
			else
				op.waitForOperationCompletion(ms);
			
			if(op.isGet())
				return op.getTupleResultList();
			
			return op.getLogicTupleResult();
		}
		catch (TucsonOperationNotPossibleException e)
		{
			e.printStackTrace();
			throw new TucsonOperationNotPossibleException();
		}
		catch (UnreachableNodeException e)
		{
			e.printStackTrace();
			throw new UnreachableNodeException();
		}
		catch (OperationTimeOutException e)
		{
			e.printStackTrace();
			throw new OperationTimeOutException();
		}
	}
	
	// Da rivedere ...
	public void addOperationExpired(long id)
	{
		this.operationExpired.add(id);
	}
	
	public synchronized ITucsonOperation doOperation(TucsonTupleCentreId tcid, int type, LogicTuple t, TucsonOperationCompletionListener l)
			throws TucsonOperationNotPossibleException, UnreachableNodeException
	{				
		// Imposto il linguaggio per l'agente
		//TucsonAgentLanguageManager.useLanguage(language);
				
		// First phase: syntax check before going on remote node
		//log("Syntax check");
		
		//if (!tcid.checkSyntax() || !aid.checkSyntax())
		//{
		//	throw new TucsonOperationNotPossibleException();
		//}

		int nTry = 1;
		do
		{
			try
			{
				// Second phase: negotiation (method getSession)
				log("Negotiation with " + tcid.toString());
				
				TucsonProtocol session = getSession(tcid); //qui!
				ObjectOutputStream outStream = session.getOutputStream();		
									
				// creo un'operazione TuCSoN quindi l'id delle operazioni viene d'ora in poi viene aggiornato
				TucsonOperation op = null;					
					
				if((type == TucsonOperation.outCode()) | (type == TucsonOperation.out_sCode())
						 | (type == TucsonOperation.set_specCode()))
					op = new TucsonOperation(type, (Tuple) t, l, this);			
				else
					op = new TucsonOperation(type, (TupleTemplate) t, l, this);
				
				//System.out.println(op);
				operations.put(op.getId(), op);
				
				//log("Remote call & eventually translation");
				log("Remote call");
				
				TucsonMsgRequest msg = new TucsonMsgRequest(op.getId(), op.getType(), 
						tcid.toString(), op.getLogicTupleArgument());
				
				
				TucsonMsgRequest.write(outStream, msg);
				outStream.flush();
				
				log("Request done: type = " + op.getType() + ", logic tuple argument = " + op.getLogicTupleArgument() + ", operation identifier " + op.getId());
				
				return op;
			}
			catch (IOException e)
			{
				e.printStackTrace();				
			}
			catch (TucsonOperationNotPossibleException e1)
			{
				e1.printStackTrace();	
				throw new TucsonOperationNotPossibleException();
			}
			nTry++;
		}
		while (nTry < 3);
		throw new UnreachableNodeException();
	}

	public synchronized void exit() throws TucsonOperationNotPossibleException
	{
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
				
				TucsonOperation op = new TucsonOperation(TucsonOperation.exitCode(), (TupleTemplate) null, null, this);				
				operations.put(op.getId(), op);
				
				TucsonMsgRequest exit = new TucsonMsgRequest((int) op.getId(), op.getType(), null,
						op.getLogicTupleArgument());
				TucsonMsgRequest.write(outStream, exit);
				outStream.flush();
			}
			catch (Exception e)
			{
				e.printStackTrace();
				throw new TucsonOperationNotPossibleException();
			}
		}
	}

	/*public HashMap<String, LanguageInfo> getInfo()
	{
		return TucsonAgentLanguageManager.getAvailableLanguages();
	}*/

	private TucsonProtocol getSession(TucsonTupleCentreId tid) throws TucsonOperationNotPossibleException,
			UnreachableNodeException
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
						port = Integer.parseInt(node.substring(idx+1));		//MODIFIED by LINZ UNIVERSITY
						node = node.substring(0, idx);					//MODIFIED by LINZ UNIVERSITY
					}
				}
				catch (Exception ex)
				{
					throw new UnreachableNodeException();
				}
				
				TucsonProtocol dialog = new TucsonProtocolTCP(node, port);
				dialog.sendEnterRequest(profile);
				dialog.receiveEnterRequestAnswer();								

				System.out.println("richiesta accettata " + dialog.isEnterRequestAccepted());
				
				if (dialog.isEnterRequestAccepted())
				{
					//System.out.println("richiesta accettata");
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
				ex2.printStackTrace();
				throw new TucsonOperationNotPossibleException();
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
		/*inser by Enrico Rossignoli*/
		private final Prolog p = new Prolog();
		
		Controller(ObjectInputStream in)
		{
			this.in = in;
			stop = false;
			this.setDaemon(true);
			/*insert by Enrico Rossignoli*/
			/*insert a prolog engine to propagate*/
			alice.tuprolog.lib.JavaLibrary jlib = (alice.tuprolog.lib.JavaLibrary) p.getLibrary("alice.tuprolog.lib.JavaLibrary");
			try {
				jlib.register(new alice.tuprolog.Struct("config"), this);
			} catch (InvalidObjectIdException e) {
				e.printStackTrace();
			}
			/*end insert*/
		}

		@SuppressWarnings("unchecked")
		public void run()
		{
			log("Controller started.");
			ContextEvent ev = null;
			while (!isStopped())
			{
				try
				{
					// Elimino tutte le operazioni di cui non si � pi� interessati
					for(Long operation : operationExpired)
					{
						operations.remove(operation);
					}
					
					TucsonMsgReply msg = TucsonMsgReply.read(in);
					log("received msg reply:  operation identifier = " + msg.getId() + ", type = " + msg.getType());

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
								LogicTuple tupleRes = (LogicTuple) msg.getTupleResult();
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
							LogicTuple tupleRes = (LogicTuple)  msg.getTupleResult();
							String spec = tupleRes.getArg(0).getName();
							//System.out.println(spec);
							ev = new ContextEvent(new TucsonOpId(msg.getId()), ok, msg.isSuccess(), spec);
						}
						else if (type == TucsonOperation.set_specCode() || type == TucsonOperation.outCode()
								|| type == TucsonOperation.out_sCode())
						{
							ev = new ContextEvent(new TucsonOpId(msg.getId()), ok, msg.isSuccess());
						}
						else if (type == TucsonOperation.get_Code())
						{
							List<LogicTuple> tupleSetRes = (List<LogicTuple>) msg.getTupleResult();
							//System.out.println(spec);
							ev = new ContextEvent(new TucsonOpId(msg.getId()), ok, msg.isSuccess(), tupleSetRes);
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
					
					//log("posting event " + ev.getOpId().getId());
					
					TucsonOperation op = operations.remove(msg.getId());
					
					if(op.isGet())
					{
						op.setTupleResultList((List<LogicTuple>) msg.getTupleResult());
					}
					else
						op.setResult((LogicTuple) msg.getTupleResult());	
					
					//System.out.println("RES = " + msg.getTupleResult());
					op.notifyCompletion(ev.operationSucceeded(), msg.isAllowed());
					
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

		LogicTuple unify(TupleTemplate template, Tuple tuple)
		{
			boolean res = template.propagate(p, tuple);
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
