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
package alice.tucson.tools;

import alice.tucson.api.*;

import alice.tucson.service.TucsonAgentLanguageManager;
import alice.tucson.service.TucsonLanguageManager;

/**
 * Command Line Interpreter
 */
public class CLIAgent
{
	public static void main(String[] args) throws Exception
	{
		log("Booting TuCSoN Command Line Intepreter ...");
		
		// CLIAgent utilizza "ReSpecT 2.2"
		TucsonAgentLanguageManager.useLanguage(TucsonLanguageManager.DEFLANGUAGE);
		
		if (alice.util.Tools.isOpt(args, "-?"))
		{
			log("Argument Template: ");
			log("{-aid agent identifier} {-console} {-?}");
		}
		else
		{
			String st_aid = null;
			if (alice.util.Tools.isOpt(args, "-aid"))
			{
				st_aid = alice.util.Tools.getOpt(args, "-aid");
			}
			else
			{
				try
				{
					st_aid = "cli" + System.currentTimeMillis();
				}
				catch (Exception ex)
				{
					st_aid = "cli_" + System.currentTimeMillis();
				}
			}

			log("Agent Identifier: " + st_aid);
			TucsonContext context = null;
			
			try
			{
				context = Tucson.getContext(new TucsonAgentId(st_aid), TucsonAgentLanguageManager.getCurLanguage());

				if (alice.util.Tools.isOpt(args, "-console"))
				{
					new Thread(new ConsoleInterpreter(context)).start();
				}
				else
				{
					GUIInterpreter form = new GUIInterpreter(context);
					form.setSize(400, 200);
					form.setVisible(true);
				}
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
				System.err.println("AGENT FAILED: " + ex);
			}
		}
	}

	public static void log(String s)
	{
		System.out.println("[CLIAgent] " + s);
	}
}
