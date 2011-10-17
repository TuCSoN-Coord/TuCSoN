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

import java.io.*;
import java.util.*;

import alice.tucson.api.*;
import alice.tucson.api.exception.ContextNotAvailableException;
import alice.tucson.api.exception.TucsonInvalidAgentIdException;
import alice.tucson.service.TucsonAgentLanguageManager;
import alice.logictuple.*;

public class ConsoleInterpreter extends alice.util.Automaton
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
	private TucsonContext context;

	public ConsoleInterpreter(TucsonContext context)
	{
		//neew: bugs fix
		String st_aid = "cli" + System.currentTimeMillis();  
		try {
			context = Tucson.getContext(new TucsonAgentId(st_aid), TucsonAgentLanguageManager.getCurLanguage());
		} catch (ContextNotAvailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TucsonInvalidAgentIdException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 											// fin qui: new
		this.context = context;
	}

	public void boot()
	{
		System.out.println("\n\nTuCSoN Command Line Interpreter - Version " + alice.tucson.api.Tucson.getVersion() + "\n"
				+ new Date() + "\n");
		become("goalRequest");
	}

	public void goalRequest()
	{
		String st = "";
		try
		{
			while (st.equals(""))
			{
				System.out.print("\n?- ");
				st = stdin.readLine();
			}
		}
		catch (Exception ex)
		{
		}

		String hostName = "localhost";
		String tcName = "default";
		try
		{
			int index3 = st.indexOf('?');
			int index4 = st.indexOf('@');

			String cmd = st.substring(index3 + 1, st.length());
			if (cmd.equals("quit"))
			{
				context.exit();
				become("end");
			}
			else
			{
				int index1 = cmd.indexOf('(');
				int index2 = cmd.lastIndexOf(')');

				if (index4 != -1 && index3 != -1)
				{
					hostName = st.substring(index4 + 1, index3);
				}

				tcName = "default";   
				if (index3 != -1)
				{
					if (index4 != -1)
					{
						tcName = st.substring(0, index4);
					}
					else
					{
						tcName = st.substring(0, index3);
					}
				}

				hostName = removeSpaces(hostName);
				tcName = removeSpaces(tcName);
				TucsonTupleCentreId tid = new TucsonTupleCentreId(tcName + "@" + hostName);

				if (index1 != -1 && index2 != -1)
				{
					String methodName = removeSpaces(cmd.substring(0, index1));
					String tuple = cmd.substring(index1 + 1, index2);
					if (methodName.equals("out"))
					{
						LogicTuple t = LogicTuple.parse(tuple);
						context.out(tid, t, (Long) null);
						System.out.println("ok.");
						
					}
					else if (methodName.equals("in"))
					{
						LogicTuple templ = LogicTuple.parse(tuple);

						LogicTuple t = (LogicTuple) context.in(tid, templ, (Long) null);
						if (t == null)
						{
							System.out.println("answer: no");
						}
						else
						{
							System.out.println("answer: " + t);
						}
					}
					else if (methodName.equals("rd"))
					{
						LogicTuple templ = LogicTuple.parse(tuple);
						LogicTuple t = (LogicTuple) context.rd(tid, templ, (Long) null);
						if (t == null)
						{
							System.out.println("answer: no");
						}
						else
						{
							System.out.println("answer: " + t);
						}
					}
					else if (methodName.equals("inp"))
					{
						LogicTuple templ = LogicTuple.parse(tuple);
						LogicTuple t = (LogicTuple) context.inp(tid, templ, (Long) null);
						if (t == null)
						{
							System.out.println("answer: no");
						}
						else
						{
							System.out.println("answer: " + t);
						}
					}
					else if (methodName.equals("rdp"))
					{
						LogicTuple templ = LogicTuple.parse(tuple);
						LogicTuple t = (LogicTuple) context.rdp(tid, templ, (Long) null);
						if (t == null)
						{
							System.out.println("answer: no");
						}
						else
						{
							System.out.println("answer: " + t);
						}
					}
					else if (methodName.equals("set_spec"))
					{
						if (tuple.startsWith("'"))
						{
							tuple = tuple.substring(1, tuple.length() - 1);
						}
						context.setSpec(tid, tuple, (Long) null);
						System.out.println("ok");
					}
					else if (methodName.equals("get_spec"))
					{
						String spec = (String) context.getSpec(tid, (Long) null);
						System.out.println("answer: " + spec);
					}
					else
					{
						System.out.println("unknown communication act: " + methodName);
					}
				}
				else
				{
					System.out.println("syntax error in " + st);
				}
			}
		}
		catch (InvalidLogicTupleException ex)
		{
			// output.append(ex.toString()+"\n");
			System.out.println("syntax error in logic tuple description.");
		}
		catch (Exception ex)
		{
			// output.append(ex.toString()+"\n");
			ex.printStackTrace();
			System.out.println("error.");
		}
		become("goalRequest");
	}

	private String removeSpaces(String st)
	{
		int i = 0;
		int j = st.length() - 1;
		while (st.charAt(i) == ' ')
			i++;
		while (st.charAt(j) == ' ')
			j--;
		return st.substring(i, j + 1);
	}

	static String getOpt(String[] args, String prefix)
	{
		for (int i = 0; i < args.length; i++)
			if (args[i].equals(prefix))
			{
				return args[i + 1];
			}
		return null;
	}

}
