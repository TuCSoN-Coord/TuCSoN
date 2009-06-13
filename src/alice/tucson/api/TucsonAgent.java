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

import java.util.*;

import alice.tucson.api.exception.TucsonInvalidAgentIdException;

import alice.tucson.service.TucsonAgentLanguageManager;
import alice.tucson.service.TucsonLanguageManager;
import alice.tucson.service.TucsonOperationCompletionListener;

/**
 * Defines the skeleton for simple TuCSoN agent base class
 * 
 */
public abstract class TucsonAgent implements TucsonOperationCompletionListener
{
	private TucsonContext context;
	private TucsonAgentId aid;
	@SuppressWarnings("unused")
	private HashMap <TucsonOpId, ContextEvent> events = null;

	protected TucsonAgent(String aid, String language) throws TucsonInvalidAgentIdException
	{
		if(language == null && TucsonAgentLanguageManager.getCurLanguage() == null)
			TucsonAgentLanguageManager.useLanguage(TucsonLanguageManager.DEFLANGUAGE);
		else
			TucsonAgentLanguageManager.useLanguage((String)language);
		
		this.aid = new TucsonAgentId(aid);
		
		events = new HashMap <TucsonOpId, ContextEvent>();
	}
	
	protected TucsonAgent(TucsonAgentId aid, String language) throws TucsonInvalidAgentIdException
	{
		if(language == null && TucsonAgentLanguageManager.getCurLanguage() == null)
			TucsonAgentLanguageManager.useLanguage(TucsonLanguageManager.DEFLANGUAGE);
		else
			TucsonAgentLanguageManager.useLanguage((String)language);
		
		this.aid = aid;
		
		events = new HashMap <TucsonOpId, ContextEvent>();
	}
	
	/**
	 * Starts agent execution
	 */
	final public void spawn()
	{
		new AgentThread(this).start();
	}

	/**
	 * Body of the agent
	 */
	abstract protected void main();

	public TucsonAgentId getTucsonAgentId()
	{
		return aid;
	}
	
	protected TucsonContext getContext()
	{
		return context;
	}
	
	protected void setContext(TucsonContext context)
	{
		this.context = context;
	}

	final class AgentThread extends Thread
	{
		TucsonAgent agent;

		AgentThread(TucsonAgent agent)
		{
			this.agent = agent;
		}

		final public void run()
		{
			try
			{				
				agent.setContext(Tucson.getContext(agent.getTucsonAgentId(), TucsonAgentLanguageManager.getCurLanguage()));
				agent.main();
				agent.getContext().exit();
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}
	}
}
