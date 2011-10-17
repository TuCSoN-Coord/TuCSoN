/*
 * Created on Dec 6, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package alice.tucson.service;

import alice.logictuple.LogicTuple;
import alice.logictuple.TupleArgument;
import alice.logictuple.Value;
import alice.logictuple.Var;
import alice.tucson.api.TucsonAgentId;
import alice.tucson.api.TucsonTupleCentreId;
import alice.tucson.api.exception.TucsonInvalidAgentIdException;
import alice.tuplecentre.api.IId;
import alice.tuplecentre.api.Tuple;
import alice.tuplecentre.api.TupleCentreId;
import alice.tuplecentre.api.TupleTemplate;

class ObservationService implements NodeEventListener
{
	private TucsonTupleCentreId obsContext;
	private TucsonAgentId obsAid;

	public ObservationService(TucsonTupleCentreId ctx)
	{
		obsContext = ctx;
		try
		{
			obsAid = new TucsonAgentId("obs_agent");
		}
		catch (TucsonInvalidAgentIdException e)
		{
			e.printStackTrace();
		}
	}

	public void accEntered(TucsonAgentId aid)
	{
		try
		{
			TucsonTCContainer.out(obsAid, obsContext, 
					new LogicTuple("node_event", 
							new Var(), 
							new Value("new_agent", 
									new Value(aid.toString()))));			
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public void accQuit(TucsonAgentId aid)
	{
		try
		{
			TucsonTCContainer.out(obsAid, obsContext, 
					new LogicTuple("node_event", 
							new Var(), 
							new Value("exit_agent", 
									new Value(((TucsonAgentId)aid).toString()))));			
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public void tcCreated(TucsonTupleCentreId tid)
	{
		try
		{
			TucsonTCContainer.out(obsAid, obsContext, 
					new LogicTuple("node_event", 
							new Var(), 
							new Value("new_tc", 
									new TupleArgument(tid.toTerm()))));	
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public void tcDestroyed(TucsonTupleCentreId tid)
	{
		try
		{
			TucsonTCContainer.out(obsAid, obsContext, 
					new LogicTuple("node_event", 
							new Var(), 
							new Value("destoyed_tc", 
									new TupleArgument(tid.toTerm()))));	
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public void getSpec_completed(TupleCentreId tid, IId id, String spec)
	{
		try
		{			
			TucsonTCContainer.out(obsAid, obsContext, 
					new LogicTuple("node_event", 
							new Var(), 
							new Value("completed_getSpec", 
									new TupleArgument(((TucsonTupleCentreId)tid).toTerm()), 
									new Value(((TucsonAgentId)id).toString()), 
									new Value(spec))));
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public void getSpec_requested(TupleCentreId tid, IId id)
	{
		try
		{
			TucsonTCContainer.out(obsAid, obsContext, 
					new LogicTuple("node_event", 
							new Var(), 
							new Value("requested_getSpec", 
									new TupleArgument(((TucsonTupleCentreId)tid).toTerm()), 
									new Value(((TucsonAgentId)id).toString()))));		
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public void in_completed(TupleCentreId tid, IId id, Tuple t)
	{
		try
		{
			TucsonTCContainer.out(obsAid, obsContext, 
					new LogicTuple("node_event", new Var(), 
							new Value("completed_in", 
									new TupleArgument(((TucsonTupleCentreId)tid).toTerm()), 
									new Value(((TucsonAgentId)id).toString()),
									new TupleArgument(((LogicTuple)t).toTerm()))));			
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public void in_requested(TupleCentreId tid, IId id, TupleTemplate t)
	{
		try
		{
			TucsonTCContainer.out(obsAid, obsContext, 
					new LogicTuple("node_event", new Var(), 
							new Value("requested_in", 
									new TupleArgument(((TucsonTupleCentreId)tid).toTerm()), 
									new Value(((TucsonAgentId)id).toString()),
									new TupleArgument(((LogicTuple)t).toTerm()))));
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public void inp_completed(TupleCentreId tid, IId id, Tuple t)
	{
		try
		{
			if (t != null)
			{
				TucsonTCContainer.out(obsAid, obsContext, 
						new LogicTuple("node_event", new Var(), 
								new Value("completed_inp", 
										new TupleArgument(((TucsonTupleCentreId)tid).toTerm()), 
										new Value(((TucsonAgentId)id).toString()),
										new Value("succeeded", new TupleArgument(((LogicTuple)t).toTerm())))));
			}
			else
			{
				TucsonTCContainer.out(obsAid, obsContext, 
						new LogicTuple("node_event", new Var(), 
								new Value("completed_inp", 
										new TupleArgument(((TucsonTupleCentreId)tid).toTerm()), 
										new Value(((TucsonAgentId)id).toString()),
										new Value("failed"))));
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public void inp_requested(TupleCentreId tid, IId id, TupleTemplate t)
	{
		try
		{
			TucsonTCContainer.out(obsAid, obsContext, 
					new LogicTuple("node_event", new Var(), 
							new Value("requested_inp", 
									new TupleArgument(((TucsonTupleCentreId)tid).toTerm()), 
									new Value(((TucsonAgentId)id).toString()),
									new TupleArgument(((LogicTuple)t).toTerm()))));
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public void out_requested(TupleCentreId tid, IId id, Tuple t)
	{
		try
		{
			TucsonTCContainer.out(obsAid, obsContext, 
					new LogicTuple("node_event", new Var(), 
							new Value("done_out", 
									new TupleArgument(((TucsonTupleCentreId)tid).toTerm()), 
									new Value(((TucsonAgentId)id).toString()),
									new TupleArgument(((LogicTuple)t).toTerm()))));
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public void rd_completed(TupleCentreId tid, IId id, Tuple t)
	{
		try
		{
			TucsonTCContainer.out(obsAid, obsContext, 
					new LogicTuple("node_event", new Var(), 
							new Value("completed_rd", 
									new TupleArgument(((TucsonTupleCentreId)tid).toTerm()), 
									new Value(((TucsonAgentId)id).toString()),
									new TupleArgument(((LogicTuple)t).toTerm()))));
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public void rd_requested(TupleCentreId tid, IId id, TupleTemplate t)
	{
		try
		{
			TucsonTCContainer.out(obsAid, obsContext, 
					new LogicTuple("node_event", new Var(), 
							new Value("requested_rd", 
									new TupleArgument(((TucsonTupleCentreId)tid).toTerm()), 
									new Value(((TucsonAgentId)id).toString()),
									new TupleArgument(((LogicTuple)t).toTerm()))));
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public void rdp_completed(TupleCentreId tid, IId id, Tuple t)
	{
		try
		{
			if (t != null)
			{
				TucsonTCContainer.out(obsAid, obsContext, 
						new LogicTuple("node_event", new Var(), 
								new Value("completed_rdp", 
										new TupleArgument(((TucsonTupleCentreId)tid).toTerm()), 
										new Value(((TucsonAgentId)id).toString()),
										new Value("succeeded", new TupleArgument(((LogicTuple)t).toTerm())))));
			}
			else
			{
				TucsonTCContainer.out(obsAid, obsContext, 
						new LogicTuple("node_event", new Var(), 
								new Value("completed_rdp", 
										new TupleArgument(((TucsonTupleCentreId)tid).toTerm()), 
										new Value(((TucsonAgentId)id).toString()),
										new Value("failed"))));
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public void rdp_requested(TupleCentreId tid, IId id, TupleTemplate t)
	{
		try
		{
			TucsonTCContainer.out(obsAid, obsContext, 
					new LogicTuple("node_event", new Var(), 
							new Value("requested_rdp", 
									new TupleArgument(((TucsonTupleCentreId)tid).toTerm()), 
									new Value(((TucsonAgentId)id).toString()),
									new TupleArgument(((LogicTuple)t).toTerm()))));
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public void setSpec_completed(TupleCentreId tid, IId id)
	{
		try
		{
			TucsonTCContainer.out(obsAid, obsContext, 
					new LogicTuple("node_event", new Var(), 
							new Value("completed_setSpec", 
									new TupleArgument(((TucsonTupleCentreId)tid).toTerm()), 
									new Value(((TucsonAgentId)id).toString()))));
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public void setSpec_requested(TupleCentreId tid, IId id, String spec)
	{
		try
		{
			TucsonTCContainer.out(obsAid, obsContext, 
					new LogicTuple("node_event", new Var(), 
							new Value("requested_setSpec", 
									new TupleArgument(((TucsonTupleCentreId)tid).toTerm()), 
									new Value(((TucsonAgentId)id).toString()), new Value(spec))));
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
}
