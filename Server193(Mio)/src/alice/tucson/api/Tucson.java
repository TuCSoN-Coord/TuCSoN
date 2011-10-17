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

import java.net.*;
import java.io.*;

import alice.tucson.api.exception.ContextNotAvailableException;
import alice.tucson.service.LanguageInfo;

/**
 * Tucson Meta Agent Coordiation Context, which can be used to get an agent
 * coordination context to access TuCSoN services
 */
public class Tucson
{
	protected Tucson()
	{
	}

	/**
	 * Gets a context, starting a session.
	 * 
	 * @param id
	 *            agent id (supports different types in relation with the used
	 *            language: AgentId (default language), String (default
	 *            language), ...)
	 * @param lang
	 *            is the language for labeled the primitive (supports different
	 *            types in relation with the used language: String, ...)
	 * @return a context.
	 * @throws ContextNotAvailableException
	 */
	public static TucsonContext getContext(Object agentId, LanguageInfo chosenLanguage) throws ContextNotAvailableException
	{
		try
		{
			return new AgentContextStub(agentId, chosenLanguage);
		}
		catch (Exception ex)
		{
			throw new ContextNotAvailableException();
		}
	}

	/**
	 * Gets the TuCSoN version number
	 * 
	 * @return the version number
	 */
	public static String getVersion()
	{
		return "1.9.3";
	}

	public static boolean isActive(InetAddress node)
	{
		try
		{
			Socket s = new Socket(node, 20504);
			StringBuffer buf = new StringBuffer();
			BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			
			try
			{
				String line = br.readLine();
				while (line != null)
				{
					buf.append(line);
					line = br.readLine();
				}
			}
			catch (Exception ex)
			{
			}
			finally
			{
				try
				{
					br.close();
				}
				catch (Exception ex)
				{
				}
			}

			if (buf.toString().indexOf("TuCSoN Infrastructure") != -1)
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
			return false;
		}
	}

}
