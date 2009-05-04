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

import alice.tucson.api.*;
import alice.tucson.common.TucsonProtocol;
import alice.tucson.common.TucsonProtocolTCP;
import java.net.*;

/**
 * 
 * 
 */
public class WelcomeAgent extends Thread
{
	ContextManager contextManager;
	Node node;

	int port;

	public WelcomeAgent(int port, Node node, ContextManager cm)
	{
		contextManager = cm;
		this.port = port;
		this.node = node;
		start();
	}

	private void log(String st)
	{
		System.out.println("[WELCOME AGENT] " + st);
	}

	public void run()
	{
		try
		{
			TucsonProtocol mainDialog = new TucsonProtocolTCP(new ServerSocket(port));

			while (true)
			{
				TucsonProtocol dialog = null;
				try
				{
					log("welcome agent for TCP/IP booted. Listening on port " + port + "...");
					dialog = mainDialog.acceptNewDialog();
					dialog.receiveFirstRequest();
					
					if (dialog.isEnterRequest())
					{
						log("New request received");
						dialog.receiveEnterRequest();
						ContextDescription desc = dialog.getContextDescription();
						contextManager.processContextRequest(desc, dialog);
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
					try
					{
						dialog.end();
					}
					catch (Exception ex)
					{
					}
				}
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
}
