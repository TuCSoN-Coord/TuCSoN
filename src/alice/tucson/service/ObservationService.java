/*
 * Created on Dec 6, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package alice.tucson.service;

import alice.tucson.api.*;
import alice.logictuple.*;

class ObservationService implements NodeEventListener{

	private LocalTupleCentreContext obsContext;

	public ObservationService(LocalTupleCentreContext ctx){
		obsContext = ctx;
	}
	
	public void accEntered(AgentId aid){
		try {
			obsContext.out(new LogicTuple("node_event", 
									new Var(),
									new Value("new_agent",
										new Value(aid.toString()))));
		} catch (Exception ex){
			ex.printStackTrace();
		}	
	}
	
	public void accQuit(AgentId aid){
		try {
			obsContext.out(new LogicTuple("node_event", 
									new Var(),
									new Value("exit_agent",
										new Value(aid.toString()))));
		} catch (Exception ex){
			ex.printStackTrace();
		}	
	}
	
	public void tcCreated(TupleCentreId tid){
		try {
			obsContext.out(new LogicTuple("node_event", 
									new Var(),
									new Value("new_tc",
										new TupleArgument(tid.toTerm()))));
		} catch (Exception ex){
			ex.printStackTrace();
		}	
	}

	public void tcDestroyed(TupleCentreId tid){
		try {
			obsContext.out(new LogicTuple("node_event", 
									new Var(),
									new Value("destoyed_tc",
										new TupleArgument(tid.toTerm()))));
		} catch (Exception ex){
			ex.printStackTrace();
		}	
	}
	
	public void getSpec_completed(alice.tuplecentre.TupleCentreId tid, alice.tuplecentre.AgentId id, String spec) {
		try {
			obsContext.out(new LogicTuple("node_event", 
									new Var(),
									new Value("completed_getSpec",
										new TupleArgument(((TupleCentreId)tid).toTerm()),
										new Value(((AgentId)id).toString()),
										new Value(spec))));
		} catch (Exception ex){
			ex.printStackTrace();
		}	
	}

	public void getSpec_requested(alice.tuplecentre.TupleCentreId tid, alice.tuplecentre.AgentId id) {
		try {
			obsContext.out(new LogicTuple("node_event", 
									new Var(),
									new Value("requested_getSpec",
										new TupleArgument(((TupleCentreId)tid).toTerm()),
										new Value(((AgentId)id).toString()))));
		} catch (Exception ex){
			ex.printStackTrace();
		}	
	}

	public void in_completed(alice.tuplecentre.TupleCentreId tid, alice.tuplecentre.AgentId id, alice.tuplecentre.Tuple t) {
		try {
			obsContext.out(new LogicTuple("node_event", 
									new Var(),
									new Value("completed_in",
										new TupleArgument(((TupleCentreId)tid).toTerm()),
										new Value(((AgentId)id).toString()),
										new TupleArgument(((LogicTuple)t).toTerm()))));
		} catch (Exception ex){
			ex.printStackTrace();
		}	
	}

	public void in_requested(alice.tuplecentre.TupleCentreId tid, alice.tuplecentre.AgentId id, alice.tuplecentre.TupleTemplate t) {
		try {
			obsContext.out(new LogicTuple("node_event", 
									new Var(),
									new Value("requested_in",
										new TupleArgument(((TupleCentreId)tid).toTerm()),
										new Value(((AgentId)id).toString()),
										new TupleArgument(((LogicTuple)t).toTerm()))));
		} catch (Exception ex){
			ex.printStackTrace();
		}	
	}

	public void inp_completed(alice.tuplecentre.TupleCentreId tid, alice.tuplecentre.AgentId id, alice.tuplecentre.Tuple t) {
		try {
			if (t!=null){		
				obsContext.out(new LogicTuple("node_event", 
									new Var(),
									new Value("completed_inp",
										new TupleArgument(((TupleCentreId)tid).toTerm()),
										new Value(((AgentId)id).toString()),
										new Value("succeeded",
											new TupleArgument(((LogicTuple)t).toTerm())))));
			} else {
				obsContext.out(new LogicTuple("node_event", 
									new Var(),
									new Value("completed_inp",
										new TupleArgument(((TupleCentreId)tid).toTerm()),
										new Value(((AgentId)id).toString()),
										new Value("failed"))));
			}
		} catch (Exception ex){
			ex.printStackTrace();
		}	
	}

	public void inp_requested(alice.tuplecentre.TupleCentreId tid, alice.tuplecentre.AgentId id, alice.tuplecentre.TupleTemplate t) {
		try {
			obsContext.out(new LogicTuple("node_event", 
									new Var(),
									new Value("requested_inp",
										new TupleArgument(((TupleCentreId)tid).toTerm()),
										new Value(((AgentId)id).toString()),
										new TupleArgument(((LogicTuple)t).toTerm()))));
		} catch (Exception ex){
			ex.printStackTrace();
		}	
	}

	public void out_requested(alice.tuplecentre.TupleCentreId tid, alice.tuplecentre.AgentId id, alice.tuplecentre.Tuple t) {
		try {
			obsContext.out(new LogicTuple("node_event", 
									new Var(),
									new Value("done_out",
										new TupleArgument(((TupleCentreId)tid).toTerm()),
										new Value(((AgentId)id).toString()),
										new TupleArgument(((LogicTuple)t).toTerm()))));
		} catch (Exception ex){
			ex.printStackTrace();
		}	
	}

	public void rd_completed(alice.tuplecentre.TupleCentreId tid, alice.tuplecentre.AgentId id, alice.tuplecentre.Tuple t) {
		try {
			obsContext.out(new LogicTuple("node_event", 
									new Var(),
									new Value("completed_rd",
										new TupleArgument(((TupleCentreId)tid).toTerm()),
										new Value(((AgentId)id).toString()),
										new TupleArgument(((LogicTuple)t).toTerm()))));
		} catch (Exception ex){
			ex.printStackTrace();
		}	
	}

	public void rd_requested(alice.tuplecentre.TupleCentreId tid, alice.tuplecentre.AgentId id, alice.tuplecentre.TupleTemplate t) {
		try {
			obsContext.out(new LogicTuple("node_event", 
									new Var(),
									new Value("requested_rd",
										new TupleArgument(((TupleCentreId)tid).toTerm()),
										new Value(((AgentId)id).toString()),
										new TupleArgument(((LogicTuple)t).toTerm()))));
		} catch (Exception ex){
			ex.printStackTrace();
		}	
	}

	public void rdp_completed(alice.tuplecentre.TupleCentreId tid, alice.tuplecentre.AgentId id, alice.tuplecentre.Tuple t) {
		try {
			if (t!=null){		
				obsContext.out(new LogicTuple("node_event", 
									new Var(),
									new Value("completed_rdp",
										new TupleArgument(((TupleCentreId)tid).toTerm()),
										new Value(((AgentId)id).toString()),
										new Value("succeeded",
											new TupleArgument(((LogicTuple)t).toTerm())))));
			} else {
				obsContext.out(new LogicTuple("node_event", 
									new Var(),
									new Value("completed_rdp",
										new TupleArgument(((TupleCentreId)tid).toTerm()),
										new Value(((AgentId)id).toString()),
										new Value("failed"))));
			}
		} catch (Exception ex){
			ex.printStackTrace();
		}	
	}

	public void rdp_requested(alice.tuplecentre.TupleCentreId tid, alice.tuplecentre.AgentId id, alice.tuplecentre.TupleTemplate t) {
		try {
			obsContext.out(new LogicTuple("node_event", 
									new Var(),
									new Value("requested_rdp",
										new TupleArgument(((TupleCentreId)tid).toTerm()),
										new Value(((AgentId)id).toString()),
										new TupleArgument(((LogicTuple)t).toTerm()))));
		} catch (Exception ex){
			ex.printStackTrace();
		}	
	}

	public void setSpec_completed(alice.tuplecentre.TupleCentreId tid, alice.tuplecentre.AgentId id) {
		try {
			obsContext.out(new LogicTuple("node_event", 
									new Var(),
									new Value("completed_setSpec",
										new TupleArgument(((TupleCentreId)tid).toTerm()),
										new Value(((AgentId)id).toString()))));
		} catch (Exception ex){
			ex.printStackTrace();
		}	
	}

	public void setSpec_requested(alice.tuplecentre.TupleCentreId tid, alice.tuplecentre.AgentId id, String spec) {
		try {
			obsContext.out(new LogicTuple("node_event", 
									new Var(),
									new Value("requested_setSpec",
										new TupleArgument(((TupleCentreId)tid).toTerm()),
										new Value(((AgentId)id).toString()),
										new Value(spec))));
		} catch (Exception ex){
			ex.printStackTrace();
		}	
	}
	

}
