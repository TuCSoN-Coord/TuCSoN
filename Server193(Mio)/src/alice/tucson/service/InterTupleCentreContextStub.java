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

import java.util.*;
import java.io.*;

import java.util.List;

import alice.logictuple.*;

import alice.tucson.api.*;

import alice.tucson.api.exception.LanguageNotFoundException;
import alice.tucson.api.exception.TucsonOperationNotPossibleException;
import alice.tucson.api.exception.TucsonInvalidTupleCentreIdException;
import alice.tucson.api.exception.UnreachableNodeException;

import alice.tucson.common.TucsonMsgReply;
import alice.tucson.common.TucsonMsgRequest;
import alice.tucson.common.TucsonProtocol;
import alice.tucson.common.TucsonProtocolTCP;

import alice.tucson.api.TucsonTupleCentreId;

/**
 * Inter tuple centre context stub
 * modified by a.pedone
 */
public class InterTupleCentreContextStub implements InterTupleCentreContext
{
	private TucsonTupleCentreId aid;
	private ContextDescription profile;

	private LinkedList<ContextEvent> events;
	private HashMap<String, ControllerSession> controllerSessions;

	private int opId;

	public InterTupleCentreContextStub(Object id, Object language)
	{
		TucsonNodeLanguageManager.useLanguage((String) language);
		if (id.getClass().getName().equals("alice.tucson.types.TucsonTupleCentreId"))
		{
			this.aid = (TucsonTupleCentreId) id;
		}
		else
		{
			try
			{
				this.aid = new TucsonTupleCentreId(id);
			}
			catch (TucsonInvalidTupleCentreIdException e)
			{
				e.printStackTrace();
			}
		}

		profile = new ContextDescription();
		opId = 0;
		events = new LinkedList<ContextEvent>();
		controllerSessions = new HashMap<String, ControllerSession>();

	}

	public synchronized TucsonOpId doOperation(Object tid, int type, Object t, String language)
			throws TucsonOperationNotPossibleException, UnreachableNodeException, LanguageNotFoundException
	{
		TucsonNodeLanguageManager.useLanguage(language);
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
				throw new TucsonOperationNotPossibleException();
			}
		}
		// First phase: syntax check before going on remote node
		log("Syntax check");
		if (!tcid.checkSyntax() || !aid.checkSyntax())
		{
			throw new TucsonOperationNotPossibleException();
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
				opId++;
				log("Remote call & eventually translation");
				TucsonMsgRequest msg = new TucsonMsgRequest(opId, type, tcid.toString(), (LogicTuple) t);
				TucsonMsgRequest.write(outStream, msg);
				outStream.flush();
				// System.out.println("Request done: "+type+" "+t+" "+opId);
				log("Operation executed succesfully");
				return new TucsonOpId(opId);
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

	private ContextEvent findEvent(TucsonOpId id)
	{
		Iterator<ContextEvent> it = events.iterator();
		while (it.hasNext())
		{
			ContextEvent ev = (ContextEvent) it.next();
			if (ev.getOpId().equals(id))
			{
				it.remove();
				return ev;
			}
		}
		return null;
	}

	public ContextEvent waitForCompletion(TucsonOpId id, int timeout)
	{
		try
		{
			long startTime = System.currentTimeMillis();
			synchronized (events)
			{
				log("Waiting for result...");
				long dt = System.currentTimeMillis() - startTime;
				ContextEvent ev = findEvent(id);
				while (ev == null && dt < timeout)
				{
					// log("waiting for "+id.getId());
					events.wait(timeout - dt);
					ev = findEvent(id);
					dt = System.currentTimeMillis() - startTime;
				}
				log("Result received...");
				return ev;
			}
		}
		catch (Exception ex)
		{
			return null;
		}
	}

	public ContextEvent waitForCompletion(TucsonOpId id)
	{
		try
		{
			synchronized (events)
			{
				log("Waiting for result...");
				ContextEvent ev = findEvent(id);
				while (ev == null)
				{
					// log("waiting for "+id.getId());
					events.wait();
					ev = findEvent(id);
				}
				log("Result received...");
				return ev;
			}
		}
		catch (Exception ex)
		{
			return null;
		}
	}

	private TucsonProtocol getSession(TucsonTupleCentreId tid, String language) throws TucsonOperationNotPossibleException,
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
			// log("notified ev "+ev.getTuple()+" - op id:
			// "+ev.getOpId().getId());
			events.notifyAll();
		}
	}

	private void log(String msg)
	{
		System.out.println("[ITCC STUB] " + msg);
	}

	class Controller extends Thread
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
							LogicTuple tupleRes = (LogicTuple) msg.getTupleResult();
							String spec = tupleRes.getArg(0).getName();
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
					// log("posting event "+ev.getOpId().getId());
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
			boolean res = template.propagate(Node.getEngineProlog(), tuple);
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

	class ControllerSession
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
