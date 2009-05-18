/*
 * User.java
 *
 * Copyright 2000-2001-2002  aliCE team at deis.unibo.it
 *
 * This software is the proprietary information of deis.unibo.it
 * Use is subject to license terms.
 *
 */
package alice.tucson.service;

import alice.logictuple.*;

import alice.tucson.api.exception.TucsonInvalidAgentIdException;
import alice.tucson.api.TucsonAgentId;
import alice.tucson.api.TucsonTupleCentreId;

public class NodeManagementAgent extends Thread
{
	private boolean isLogging = true;

	private Node node;

	private TucsonAgentId nodeManAid;

	/**
	 * for local access (valid only if user is in the same java VM of daemon)
	 */
	private TucsonTupleCentreId config;

	public NodeManagementAgent(TucsonTupleCentreId config, Node node)
	{
		// agente che riceve comandi nel tuple centre $ORG
		try
		{
			nodeManAid = new TucsonAgentId("node_management_agent");
		}
		catch (TucsonInvalidAgentIdException e)
		{
			e.printStackTrace();
		}
		
		this.node = node;
		this.config = config;
		
		try
		{
			start();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public void run()
	{
		log("agent booted.");
		
		while (true)
		{
			try
			{
				LogicTuple cmd = TucsonTCContainer.in(nodeManAid, config, new LogicTuple("cmd", new Var("X")));
				
				log("received command " + cmd);
				execCmd(cmd.getArg(0));
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}
	}

	protected void execCmd(TupleArgument cmd) throws Exception
	{
		String name = cmd.getName();
		
		if (name.equals("destroy"))
		{
			String tcName = cmd.getArg(0).getName();
			boolean result = node.destroyCore(tcName);
			
			if (result)
			{
				log(tcName + " destroyed");
				
				TucsonTCContainer.out(nodeManAid, config, 
						new LogicTuple("cmd_result", new Value("destroy"), new Value("ok")));
			}
			else
			{
				log("cannot destroy " + tcName);
				
				TucsonTCContainer.out(nodeManAid, config, 
								new LogicTuple("cmd_result", new Value("destroy"), new Value("failed")));
			}
		}
		else if (name.equals("enable_persistency"))
		{
			node.enablePersistence(cmd.getArg(0));
			TucsonTCContainer.out(nodeManAid, config, 
								new LogicTuple("cmd_result", cmd, new Value("ok")));
			
			log("enabled persistency for " + cmd.getArg(0) + " tuple centres");
		}
		else if (name.equals("disable_persistency"))
		{
			node.disablePersistence(cmd.getArg(0));
			TucsonTCContainer.out(nodeManAid, config, new LogicTuple("cmd_result", new Value("disable_persistency"), new Value(
					"ok")));
			log("disabled persistency for " + cmd.getArg(0) + " tuple centres");
		}
		else if (name.equals("enable_observability"))
		{
			node.activateObservability();
			log("enable observability.");
		}
		else if (name.equals("disable_observability"))
		{
			node.deactivateObservability();
			log("disable observability.");
		}
	}

	protected void log(String s)
	{
		if (isLogging)
		{
			System.out.println("[NODE MANAGEMENT AGENT] " + s);
		}
	}
}
