/*
 * TuCSoN - Copyright (C) 2001-2002  aliCE team at deis.unibo.it
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
package alice.tucson.examples.service;

import alice.tuplecentre.core.TupleCentreOperation;

import alice.tucson.api.*;
import alice.tucson.api.exception.*;

import alice.tucson.service.TucsonLanguageManager;

class SumServiceAgent extends TucsonAgent
{
	private TucsonTupleCentreId tid;
	private boolean toStop;

	public SumServiceAgent(TucsonAgentId id, String language, TucsonTupleCentreId tid) throws TucsonException
	{
		super(id, language);
		this.tid = tid;
	}

	protected void main()
	{
		try
		{
			log("started");
			while (!toStop)
			{

			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void log(String msg)
	{
		System.out.println("[ " + this.getTucsonAgentId() + " ] " + msg);
	}

	@Override
	public void operationCompleted(TupleCentreOperation op)
	{
		// TODO Auto-generated method stub
		
	}
}

public class SumService
{
	public static void main(String args[])
	{
		try
		{
			TucsonTupleCentreId tid;
			String agentName = "mySumService";			
			
			if (args.length == 0)
			{
				tid = new TucsonTupleCentreId("default");
			}
			else if (args.length == 1)
			{
				tid = new TucsonTupleCentreId(args[0]);
			}
			else if (args.length == 2)
			{
				tid = new TucsonTupleCentreId(args[1]);
				agentName = "'" + args[0] + "'";
			}
			else
			{
				System.err.println("args: {service name} {service board tc identifier}");
				System.exit(-1);
				return;
			}

			new SumServiceAgent(new TucsonAgentId(agentName), TucsonLanguageManager.DEFLANGUAGE, tid).spawn();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
