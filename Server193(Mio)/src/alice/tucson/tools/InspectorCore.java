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

import alice.tucson.api.TucsonAgentId;
import alice.tucson.api.TucsonTupleCentreId;

import alice.logictuple.LogicTuple;

import alice.tuplecentre.core.TriggeredReaction;

public class InspectorCore extends alice.tucson.service.inspector.Inspector
{
	Inspector form;

	boolean loggingTuples = false;
	String logTupleFilename;
	FileWriter logTupleWriter;
	LogicTuple logTupleFilter;

	boolean loggingQueries = false;
	String logQueryFilename;
	FileWriter logQueryWriter;
	// LogicTuple logQueryFilter;

	boolean loggingReactions = false;
	String logReactionFilename;
	FileWriter logReactionWriter;

	// LogicTuple logQueryFilter;

	public InspectorCore(Inspector f, TucsonAgentId id_, TucsonTupleCentreId tid_) throws Exception
	{
		super(id_, tid_);
		form = f;
		logTupleFilename = "inspector-tuples.log";
		logQueryFilename = "inspector-queries.log";
		logReactionFilename = "inspector-reactions.log";		
		try
		{
			logTupleWriter = new FileWriter(logTupleFilename, true);
			logQueryWriter = new FileWriter(logQueryFilename, true);
			logReactionWriter = new FileWriter(logReactionFilename, true);
		}
		catch (Exception ex)
		{
		}
	}

	public void onContextEvent(alice.tucson.service.inspector.InspectorContextEvent msg)
	{
		//System.out.println("Eventi" + msg.wnEvents);
		//System.out.println("Eventi" + msg.tuples);
		if (msg.tuples != null)
		{
			TupleViewer viewer = form.getTupleForm();
			viewer.setVMTime(msg.vmTime);
			msg.localTime = System.currentTimeMillis();
			viewer.setLocalTime(msg.localTime);
			String st = "";

			Iterator it = msg.tuples.iterator();
			int n = 0;
			
			while (it.hasNext())
			{
				st = st + it.next().toString() + "\n";
				// log("context event: " + st);
				n++;
			}
			viewer.setNItems(n);
			viewer.setText(st);
			
			if (loggingTuples)
			{
				try
				{
					st = "";
					logTupleWriter.write("snapshot( \n" + "    time_vm(" + msg.vmTime + "),\n" + "    time_local("
							+ msg.localTime + "),\n" + "    tuple_filter(" + form.protocol.tsetFilter + "),\n"
							+ "    tuple_log_filter(" + logTupleFilter + "),\n" + "    tuple_list([ \n");

					it = msg.tuples.iterator();
					if (logTupleFilter == null)
					{
						if (it.hasNext())
						{
							st = st + "        " + it.next().toString();
							while (it.hasNext())
							{
								st = st + ",\n        " + it.next().toString() + "\n";
							}
						}
					}
					else
					{
						if (it.hasNext())
						{
							LogicTuple tuple = (LogicTuple) it.next();

							if (logTupleFilter.match(tuple))
							{
								st = st + "        " + tuple.toString();
							}
							while (it.hasNext())
							{
								tuple = (LogicTuple) it.next();
								if (logTupleFilter.match(tuple))
								{
									st = st + ",\n        " + tuple.toString();
								}
							}
						}
					}
					logTupleWriter.write(st + "])).\n");
					logTupleWriter.flush();
				}
				catch (Exception ex)
				{
				}
			}
		}
		if (msg.wnEvents != null)
		{			
			EventViewer viewer = form.getQueryForm();
			msg.localTime = System.currentTimeMillis();
			viewer.setVMTime(msg.vmTime);
			viewer.setLocalTime(msg.localTime);
			String st = "";
			Iterator it = msg.wnEvents.iterator();
			int n = 0;
			//System.out.println("Eventi" + it.hasNext());
			while (it.hasNext())
			{
				//System.out.println("while" + it.next());
				// tupleEvent+=ev.getOperationName()+"(";
				// if (ev.tuple!=null){
				// tupleEvent+=ev.tuple+") from agent "+ev.idAgent;
				// } else {
				// tupleEvent+=ev.template+") from agent "+ev.idAgent;
				// }
				//System.out.println(it.next());
				st = st + it.next() + "\n";				
				n++;
			}
			viewer.setNItems(n);
			viewer.setText(st);
			if (loggingQueries)
			{
				try
				{
					st = "";
					logQueryWriter.write("snapshot( \n" + "    time_vm(" + msg.vmTime + "),\n" + "    time_local("
							+ msg.localTime + "),\n" +
							 //"query_filter(" + form.protocol.queryFilter + "),\n"+
							 //" query_log_filter(" + logQueryFilter + "),\n"+
							"    query_list([ \n");

					it = msg.wnEvents.iterator();
					if (it.hasNext())
					{
						st = st + "        " + it.next();
						while (it.hasNext())
						{
							st = st + ",\n        " + it.next();
						}
					}

					logQueryWriter.write(st + "])).\n");
					logQueryWriter.flush();
				}
				catch (Exception ex)
				{
				}
			}
		}

		if (msg.reactionOk != null)
		{
			ReactionViewer viewer = form.getReactionForm();
			TriggeredReaction tr = msg.reactionOk;
			viewer.appendText("time: " + msg.vmTime + "\n" + tr.getReaction() + " OK\n");
			if (loggingReactions)
			{
				//System.out.println("LOG OK");
				try
				{
					logReactionWriter.write("succeed-reaction( time(" + msg.vmTime + "), " + tr.getReaction() + ").\n");
					logReactionWriter.flush();
				}
				catch (Exception ex)
				{
				}
			}

		}
		else if (msg.reactionFailed != null)
		{
			ReactionViewer viewer = form.getReactionForm();
			TriggeredReaction tr = msg.reactionFailed;
			viewer.appendText("time: " + msg.vmTime + "\n" + tr.getReaction() + " FAILED\n");
			if (loggingReactions)
			{
				//System.out.println("LOG FAILED");
				try
				{
					logReactionWriter.write("failed-reaction( time(" + msg.vmTime + "), " + tr.getReaction() + ").\n");
					logReactionWriter.flush();
				}
				catch (Exception ex)
				{
				}
			}
		}
	}

	void changeLogTupleFile(String name)
	{
		logTupleFilename = name;
		if (logTupleWriter != null)
		{
			try
			{
				logTupleWriter.close();
			}
			catch (Exception ex)
			{
			}
		}
		try
		{
			logTupleWriter = new FileWriter(logTupleFilename, true);
		}
		catch (Exception ex)
		{
		}
	}

	void changeLogQueryFile(String name)
	{
		logQueryFilename = name;
		if (logQueryWriter != null)
		{
			try
			{
				logQueryWriter.close();
			}
			catch (Exception ex)
			{
			}
		}
		try
		{
			logQueryWriter = new FileWriter(logQueryFilename, true);
		}
		catch (Exception ex)
		{
		}
	}

	void changeLogReactionFile(String name)
	{
		logReactionFilename = name;
		if (logReactionWriter != null)
		{
			try
			{
				logReactionWriter.close();
			}
			catch (Exception ex)
			{
			}
		}
		try
		{
			logReactionWriter = new FileWriter(logReactionFilename, true);
		}
		catch (Exception ex)
		{
		}
	}

	private void log(String st)
	{
		System.out.println("InspectorCore: " + st);
	}
}
