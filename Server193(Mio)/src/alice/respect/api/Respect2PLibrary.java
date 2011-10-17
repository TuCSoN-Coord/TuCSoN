/*
 * ReSpecT - Copyright (C) aliCE team at deis.unibo.it
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
package alice.respect.api;

import alice.respect.core.BlockingContext;
import alice.respect.core.RespectOperation;
import alice.respect.core.TimedContext;
import alice.tuprolog.*;
import alice.util.Tools;
import alice.logictuple.*;

import java.util.*;

/**
 * 2P Library to exploit ReSpecT inside 2P.
 * 
 * @author aricci
 */
public class Respect2PLibrary extends alice.tuprolog.Library {
	
	
	private AgentId aid;
	private HashMap ops;
	private HashMap ctxs;
	private HashMap timed_ctxs;
	
	private int count;
	
	public Respect2PLibrary(){
		ops = new HashMap();
		ctxs = new HashMap();
		timed_ctxs = new HashMap();
		
		count = 0;
		try {
			long t = System.currentTimeMillis();
			aid = new AgentId("agent_"+t);
		} catch (Exception ex){		
			System.err.println("Invalid agent identity.");
		}
		//System.out.println("Library loaded.");
	}	
	
	public boolean set_agent_id_1(Struct id){
		try {
			aid = new AgentId(id.toString());
			return true;
		} catch (Exception ex){
			return false;
		}
	}
	
	public boolean agent_id_1(alice.tuprolog.Term var){
		return unify(var,new Struct(alice.util.Tools.removeApices(aid.toString())));
	}
	
	public boolean out_3(Struct tcName, Struct tuple, alice.tuprolog.Var op){
		try {
			RespectTC tc = RespectFramework.getTC(new TupleCentreId(tcName.toString()));
			IRespectOperation oper = tc.out(aid,new LogicTuple(tuple));
			String key = "$op"+count++;
			ops.put(key,oper);
		 	return unify(op,new Struct(key));
		} catch (Exception ex){
			return false;
		}
	}
	
	public boolean in_3(Struct tcName, Struct tuple, alice.tuprolog.Var op){
		try {
			RespectTC tc = RespectFramework.getTC(new TupleCentreId(tcName.toString()));
			IRespectOperation oper = tc.in(aid,new LogicTuple(tuple));
			String key = "$op"+count++;
			ops.put(key,oper);
			return unify(op,new Struct(key));
		} catch (Exception ex){
			return false;
		}
	}

	public boolean rd_3(Struct tcName, Struct tuple, alice.tuprolog.Var op){
		try {
			RespectTC tc = RespectFramework.getTC(new TupleCentreId(tcName.toString()));
			IRespectOperation oper = tc.rd(aid,new LogicTuple(tuple));
			String key = "$op"+count++;
			ops.put(key,oper);
			return unify(op,new Struct(key));
		} catch (Exception ex){
			return false;
		}
	}

	public boolean inp_3(Struct tcName, Struct tuple, alice.tuprolog.Var op){
		try {
			RespectTC tc = RespectFramework.getTC(new TupleCentreId(tcName.toString()));
			IRespectOperation oper = tc.inp(aid,new LogicTuple(tuple));
			String key = "$op"+count++;
			ops.put(key,oper);
			return unify(op,new Struct(key));
		} catch (Exception ex){
			return false;
		}
	}

	public boolean rdp_3(Struct tcName, Struct tuple, alice.tuprolog.Var op){
		try {
			RespectTC tc = RespectFramework.getTC(new TupleCentreId(tcName.toString()));
			IRespectOperation oper = tc.rdp(aid,new LogicTuple(tuple));
			String key = "$op"+count++;
			ops.put(key,oper);
			return unify(op,new Struct(key));
		} catch (Exception ex){
			return false;
		}
	}


	public boolean set_spec_2(Struct tcName, Struct spec){
		try {
			RespectTC tc = RespectFramework.getTC(new TupleCentreId(tcName.toString()));
			String sp = alice.util.Tools.removeApices(spec.toString());
			tc.setSpec(aid,new RespectSpecification(sp));
			return true;
		} catch (Exception ex){
			return false;
		}
	}

	public boolean get_spec_2(Struct tcName, Term spec){
		try {
			RespectTC tc = RespectFramework.getTC(new TupleCentreId(tcName.toString()));
			String sp = tc.getSpec(aid).toString();
			return unify(spec,new Struct(sp));
		} catch (Exception ex){
			return false;
		}
	}


	public boolean result_2(Struct op,Term tuple){
		try {
			IRespectOperation oper = (IRespectOperation)ops.get(Tools.removeApices(op.toString()));
			if (op==null){
				return false;
			}
			LogicTuple t = oper.getLogicTupleResult();
			if (t==null){
				return false;
			} else {
				return unify(tuple,t.toTerm());
			}
		} catch (Exception ex){
			return false;
		}
	}
	

	public boolean completed_1(Struct op){
		try {
			RespectOperation oper = (RespectOperation)ops.get(Tools.removeApices(op.toString()));
			if (oper==null){
				return false;
			}
			if (oper.isOperationCompleted()){
				return true;
			} else {
				return false;
			}
		} catch (Exception ex){
			return false;
		}
	}
	
	public boolean arg_2(Struct op,Term tuple){
		try {
			IRespectOperation oper = (IRespectOperation)ops.get(Tools.removeApices(op.toString()));
			if (op==null){
				return false;
			}
			LogicTuple t = oper.getLogicTupleArgument();
			if (t==null){
				return false;
			} else {
				return unify(tuple,t.toTerm());
			}
		} catch (Exception ex){
			return false;
		}
	}
	
	//
	
	public boolean b_out_2(Struct tcName, Term tuple){
		try {
			BlockingContext ctx = getBlockingContext(tcName);
			if (ctx==null){
				return false;
			}
			ctx.out(aid,new LogicTuple(tuple));
			return true;
		} catch (Exception ex){
			return false;
		}
	}

	public boolean b_in_2(Struct tcName, Term tuple){
		try {
			BlockingContext ctx = getBlockingContext(tcName);
			if (ctx==null){
				return false;
			}
			LogicTuple t = ctx.in(aid,new LogicTuple(tuple));
			return unify(tuple,t.toTerm());
		} catch (Exception ex){
			return false;
		}
	}

	public boolean b_rd_2(Struct tcName, Term tuple){
		try {
			BlockingContext ctx = getBlockingContext(tcName);
			if (ctx==null){
				return false;
			}
			LogicTuple t = ctx.rd(aid,new LogicTuple(tuple));
			return unify(tuple,t.toTerm());
		} catch (Exception ex){
			return false;
		}
	}
	
	public boolean b_inp_2(Struct tcName, Term tuple){
		try {
			BlockingContext ctx = getBlockingContext(tcName);
			if (ctx==null){
				return false;
			}			
			LogicTuple t = ctx.inp(aid,new LogicTuple(tuple));
			return unify(tuple,t.toTerm());
		} catch (Exception ex){
			return false;
		}
	}

	public boolean b_rdp_2(Struct tcName, Term tuple){
		try {
			BlockingContext ctx = getBlockingContext(tcName);
			if (ctx==null){
				return false;
			}
			LogicTuple t = ctx.rdp(aid,new LogicTuple(tuple));
			return unify(tuple,t.toTerm());
		} catch (Exception ex){
			return false;
		}
	}
	
		
	private synchronized BlockingContext getBlockingContext(Struct tcName){
		try {
			String key = tcName.toString();
			BlockingContext bc = (BlockingContext)ctxs.get(key);
			if (bc==null){
				RespectTC tc = RespectFramework.getTC(new TupleCentreId(tcName.toString()));
				bc = new BlockingContext(tc);
				ctxs.put(key,bc);
			}
			return bc;
		} catch (Exception ex){
			return null;
		}
	}
		
	//
	
	public boolean t_in_4(Struct tcName, Term tuple,alice.tuprolog.Number millis, alice.tuprolog.Var op){
		try {
			TimedContext ctx = getTimedContext(tcName);
			if (ctx==null){
				return false;
			}
			LogicTuple t = ctx.in(aid,new LogicTuple(tuple),millis.longValue());
			return unify(tuple,t.toTerm());
		} catch (Exception ex){
			return false;
		}
	}

	public boolean t_rd_4(Struct tcName, Term tuple,alice.tuprolog.Number millis, alice.tuprolog.Var op){
		try {
			TimedContext ctx = getTimedContext(tcName);
			if (ctx==null){
				return false;
			}
			LogicTuple t = ctx.rd(aid,new LogicTuple(tuple),millis.longValue());
			return unify(tuple,t.toTerm());
		} catch (Exception ex){
			return false;
		}
	}


	private synchronized TimedContext getTimedContext(Struct tcName){
		try {
			String key = tcName.toString();
			TimedContext bc = (TimedContext)timed_ctxs.get(key);
			if (bc==null){
				RespectTC tc = RespectFramework.getTC(new TupleCentreId(tcName.toString()));
				bc = new TimedContext(tc);
				timed_ctxs.put(key,bc);
			}
			return bc;
		} catch (Exception ex){
			return null;
		}
	}
	
	//

	public String getTheory(){
		return 
		":- op(  600, xfx,  '?'). \n"+
		"TC ? out(T) :- b_out(TC,T). \n"+
		"TC ? in(T)  :- b_in(TC,T). \n"+
		"TC ? inp(T) :- b_inp(TC,T). \n"+
		"TC ? rd(T)  :- b_rd(TC,T). \n"+
		"TC ? rdp(T) :- b_rdp(TC,T). \n"+
		"TC ? set_spec(T)  :- set_spec(TC,T). \n"+
		"TC ? get_spec(T) :- get_spec(TC,T). \n"+
		// timed interface
		"TC ? in(T,Ms,Ops) :- t_in(TC,T,Ms,Ops). \n"+
		"TC ? rd(T,Ms,Ops) :- t_rd(TC,T,Ms,Ops). \n";
	}
}
