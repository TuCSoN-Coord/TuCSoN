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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import alice.logictuple.LogicTuple;
import alice.logictuple.Value;
import alice.logictuple.Var;
import alice.logictuple.TupleArgument;

import alice.tucson.service.inspector.*;

import alice.tucson.common.TucsonProtocol;

import alice.tucson.api.*;

/**
 * 
 * Node class provides the basic daemon of a TuCSoN infrastructure node.
 * 
 */
public class ContextManager
{
	protected final static int threadsNumber = 5;
	protected TucsonTupleCentreId config;
	protected TucsonAgentId aid;
	protected Node node;
	protected ExecutorService exec;

	public ContextManager(Node node, TucsonTupleCentreId tid)
	{
		try
		{
			aid = new TucsonAgentId("context_manager");
		}
		catch (Exception ex)
		{
		}
		this.node = node;
		this.config = tid;

		exec = Executors.newFixedThreadPool(threadsNumber);
	}

	private void log(String st)
	{
		System.out.println("[CONTEXT MANAGER] " + st);
	}

	public synchronized boolean processContextRequest(ContextDescription profile, TucsonProtocol dialog)
	{
		try
		{
			log("creating context ...");

			String agentName = profile.getProperty("agent-identity");
			log("agent-identity: " + agentName);
			
			LogicTuple req = new LogicTuple("context_request", new Value(agentName), new Var("CtxId"));
			log("context_request: " + req.toString());
			
			LogicTuple result = TucsonTCContainer.inp(aid, config, req);
			log("Context creation result: " + result);
			
			if (result == null)
			{
				profile.setProperty("failure", "context not available");
				dialog.sendEnterRequestRefused(profile);
				return false;
			}
			
			TupleArgument res = result.getArg(1);
			
			if (res.getName().equals("failed"))
			{
				profile.setProperty("failure", res.getArg(0).getName());
				dialog.sendEnterRequestRefused(profile);
				return false;
			}

			TupleArgument ctxId = res.getArg(0);
			profile.setProperty("context-id", ctxId.toString());
			//profile.setLanguageInfo(TucsonNodeLanguageManager.getCurLanguage());

			dialog.sendEnterRequestAccepted(profile);

			String agentRole = profile.getProperty("agent-role");
			
			if (agentRole.equals("$inspector"))
			{
				log("creating inspector skeleton");
				ContextSkeleton skel = new InspectorContextSkel(this, dialog, node, profile);
				skel.start();
			}
			else
			{
				log("creating agent skeleton");
				ContextSkeleton skel = new AgentContextSkeleton(this, dialog, node, profile);
				exec.execute(skel);
			}		
			return true;
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			profile.setProperty("failure", "generic");
			return false;
		}
	}

	public synchronized boolean shutdownContext(int ctxId, TucsonAgentId aid)
	{
		try
		{
			LogicTuple req = new LogicTuple("context_shutdown", new Value(ctxId), new Value(aid.toString()), new Var("CtxId"));
			LogicTuple result = TucsonTCContainer.inp(this.aid, config, req);
			
			log("shutdown context: " + result);
			
			if (result.getArg(2).getName().equals("ok"))
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return false;
		}
	}
}
