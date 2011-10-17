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

import alice.tucson.api.exception.*;

import alice.tucson.api.*;

public class Inspector extends Thread implements InspectorContextListener
{
	protected InspectorContext context;
	protected boolean quit;

	public Inspector(TucsonAgentId id, TucsonTupleCentreId tid) throws TucsonException
	{		
		context = new InspectorContextStub(id, tid);		
		context.addInspectorContextListener(this);
		quit = false;		
	}

	public Inspector(TucsonAgentId id, TucsonTupleCentreId tid, int port) throws TucsonException
	{
		context = new InspectorContextStub(id, tid);
		context.addInspectorContextListener(this);
		quit = false;
	}

	/**
	 * behaviour: wait a new observable event and take some actions, according
	 * to specific instance of inspector
	 */
	public synchronized void run()
	{
		System.out.println("[ Inspector ] start.");
		while (!quit)
		{
			try
			{
				context.acceptVMEvent();
			}
			catch (Exception e)
			{
				//e.printStackTrace();
				break;
			}
		}
		System.out.println("[ Inspector ] shutdown.");
	}

	public InspectorContext getContext()
	{
		return context;
	}

	public void onContextEvent(InspectorContextEvent ev)
	{
	}

	public void quit()
	{
		try
		{
			quit = true;
			context.exit();
			this.interrupt();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
}
