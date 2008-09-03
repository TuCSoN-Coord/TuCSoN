/*
 * ReSpecT Framework - aliCE team at deis.unibo.it
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
package alice.respect;
import  alice.tuprolog.*;

import  alice.logictuple.*;
import  alice.tuplecentre.*;


/**
 * this tuProlog library defines the behaviour
 * of ReSpecT primitives, used inside ReSpecT sources.
 *
 */
public class Library extends alice.tuprolog.Library {

    RespectVM vm;

    public Library(){
    }

    public String getTheory(){
        return 
            ":- op(  550, xfx,  '@'). \n";
    }
    
    public void init(RespectVM m){
        vm=m;
    }

    public boolean in_r_1(Term arg0){
        //spy("in_r "+g+" BEGIN");
        LogicTuple tuArg=new LogicTuple(arg0);
        alice.tuplecentre.Tuple tuple=vm.removeMatchingTuple(tuArg);
        if (tuple!=null){
            Term term=((LogicTuple)tuple).toTerm();
            unify(arg0,getRenamedCopy(term));
            ReactionEvent ev=new Event_in_r(vm.getCurrentEvent().getId(),(alice.respect.AgentId)vm.getCurrentEvent().getAgentId(),(TupleCentreId)vm.getId(),new LogicTuple(arg0.getRenamedCopy()));
            vm.fetchTriggeredReactions(ev);
            //spy("in_r "+arg0+" success");
            return true;
        }
        else {
            return false;
        }
    }

    public boolean rd_r_1(Term arg0){
        //spy("rd_r "+g+" BEGIN");
        LogicTuple tuArg=new LogicTuple(arg0);
        //spy("rd_r:  "+tuArg);
        alice.tuplecentre.Tuple tuple=vm.readMatchingTuple(tuArg);
		//spy("rd_r:  "+tuArg+" - "+tuple);
        if (tuple!=null){
            Term term=((LogicTuple)tuple).toTerm();
            Term renamed = getRenamedCopy(term); 
            unify(arg0, renamed);
            ReactionEvent ev=new Event_rd_r(vm.getCurrentEvent().getId(),(alice.respect.AgentId)vm.getCurrentEvent().getAgentId(),(TupleCentreId)vm.getId(),new LogicTuple(arg0.getRenamedCopy()));
            vm.fetchTriggeredReactions(ev);
            //spy("rd_r "+arg0+" success");
            return true;
        } else {
            //spy("rd_r "+arg0+" fail");
            return false;
        }
    }

    public boolean no_r_1(Term arg0){
        //spy("no_r "+g+" BEGIN");
        LogicTuple tuArg=new LogicTuple(arg0);
        alice.tuplecentre.Tuple tuple=vm.readMatchingTuple(tuArg);
        if (tuple==null){
            ReactionEvent ev=new Event_in_r(vm.getCurrentEvent().getId(),(alice.respect.AgentId)vm.getCurrentEvent().getAgentId(),(TupleCentreId)vm.getId(),new LogicTuple(arg0.getRenamedCopy()));
            vm.fetchTriggeredReactions(ev);
            //spy("no_r "+arg0+" success");
            return true;
        } else {
            //spy("no_r "+arg0+" fail");
            return false;
        }
    }

    public boolean out_r_1(Term arg0){
        //spy("out_r "+g+" BEGIN");
        // get a renamed copy
        Term newArg=arg0.getRenamedCopy();

        LogicTuple tuArg=new LogicTuple(newArg);
        vm.addTuple(tuArg);
        ReactionEvent ev=new Event_out_r(vm.getCurrentEvent().getId(),(alice.respect.AgentId)vm.getCurrentEvent().getAgentId(),(TupleCentreId)vm.getId(),new LogicTuple(arg0.getRenamedCopy()));
        vm.fetchTriggeredReactions(ev);
        //spy("out_r "+arg0+" success");
        return true;
    }

    public boolean current_agent_1(Term arg0){
    	//------
    	if (vm.getCurrentEvent() instanceof TrapEvent){
    		return false;
    	}
    	//------
        Term term=new Struct(vm.getCurrentEvent().getAgentId().toString());
        return unify(arg0,term);
    }

    public boolean current_tc_1(Term arg0){
        Term term=new Struct(vm.getId().toString());
        return unify(arg0,term);
    }

    public boolean current_op_1(Term arg0){
        return unify(arg0,new Struct(vm.getCurrentEvent().getOperationName()));
    }

    public boolean current_tuple_1(Term arg0){
        Event ev=vm.getCurrentEvent();
        Term term=null;
        if (ev instanceof InputEvent){
            if (ev instanceof InputEvent_out){
                term=((LogicTuple)((InputEvent_out)ev).getTuple()).toTerm();
            } else if (ev instanceof InputEvent_in){
                term=((LogicTuple)((InputEvent_in)ev).getTupleTemplate()).toTerm();
            } else if (ev instanceof InputEvent_inp){
                term=((LogicTuple)((InputEvent_inp)ev).getTupleTemplate()).toTerm();
            } else if (ev instanceof InputEvent_rd){
                term=((LogicTuple)((InputEvent_rd)ev).getTupleTemplate()).toTerm();
            } else if (ev instanceof InputEvent_rdp){
                term=((LogicTuple)((InputEvent_rdp)ev).getTupleTemplate()).toTerm();
            }
        } else if (ev instanceof alice.tuplecentre.OutputEvent){
            if (ev instanceof OutputEvent_in){
                term=((LogicTuple)((OutputEvent_in)ev).getTuple()).toTerm();
            } else if (ev instanceof OutputEvent_inp){
                OutputEvent_inp ev_inp=(OutputEvent_inp)ev;
                //
                // According to the semantics of TupleCentres: if
                // the inp was not success, the triggered reaction must
                // take the template;  here, the fact that the tuple
                // is not null indicates if the inp was succesful or not
                //
                if (ev_inp.getTuple()!=null){
                    term=((LogicTuple)ev_inp.getTuple()).toTerm();
                } else {
                    term=((LogicTuple)ev_inp.getTupleTemplate()).toTerm();

                }
            } else if (ev instanceof OutputEvent_rd){
                term=new Struct("rd",((LogicTuple)((OutputEvent_rd)ev).getTuple()).toTerm());
            } else if (ev instanceof OutputEvent_rdp){
                OutputEvent_rdp ev_rdp=(OutputEvent_rdp)ev;
                //
                // According to the semantics of TupleCentres: if
                // the rdp was not success, the triggered reaction must
                // take the template;  here, the fact that the tuple
                // is not null indicates if the rdp was succesful or not
                //
                if (ev_rdp.getTuple()!=null){
                    term=((LogicTuple)ev_rdp.getTuple()).toTerm();
                } else {
                    term=((LogicTuple)ev_rdp.getTupleTemplate()).toTerm();
                }
            }
        } else if (ev instanceof ReactionEvent){
            if (ev instanceof Event_in_r){
                term=((Event_in_r)ev).getTuple().toTerm();
            } else if (ev instanceof Event_out_r){
                    term=((Event_out_r)ev).getTuple().toTerm();
            } else if (ev instanceof Event_rd_r){
                    term=((Event_rd_r)ev).getTuple().toTerm();
            } else if (ev instanceof Event_no_r){
                    term=((Event_no_r)ev).getTuple().toTerm();
            }
        } else if (ev instanceof TrapEvent){
			term=((TrapEvent)ev).getLogicTuple().toTerm();
        } else {
			return false;
        }
        
        return unify(arg0,getRenamedCopy(term));
    }

    public boolean pre_0(){
        return (vm.getCurrentEvent().isToTupleCentre());
    }

    public boolean post_0(){
        return (vm.getCurrentEvent().isFromTupleCentre());
    }

    public boolean success_0(){
        return (vm.getCurrentEvent().isSuccess());
    }

    public boolean failure_0(){
        return (vm.getCurrentEvent().isFailure());
    }


    public boolean spawn_3(Term agentId, Term agentType, Term argList){
        if (argList.isList()){
            Struct args = (Struct)argList;
            alice.tuplecentre.OutputEvent ev=
                       new OutputEvent_spawn((alice.respect.AgentId)vm.getCurrentEvent().getAgentId(),
                                             (TupleCentreId)vm.getId(),
                                             agentId.getRenamedCopy(),
                                             agentType.getRenamedCopy(),
                                             (Struct)args.getRenamedCopy());
            vm.addOutputEvent(ev);
            return true;
        } else {
            return false;
        }

    }

    public boolean current_time_1(Term arg0){
        long time=vm.getCurrentTime();
        return unify(arg0,new alice.tuprolog.Long(time));
    }

    public boolean out_tc_2(Struct id, Term tuple){
        LogicTuple tid=new LogicTuple(id);
        // get a renamed copy
        Term newTuple=tuple.getRenamedCopy();

        LogicTuple tuArg=new LogicTuple(newTuple);
        alice.tuplecentre.OutputEvent ev=new OutputEvent_out_tc((alice.respect.AgentId)vm.getCurrentEvent().getAgentId(),(TupleCentreId)vm.getId(),tid,tuArg);
        vm.addOutputEvent(ev);
        return true;
    }

	// timed extension
	
	public boolean new_trap_3(alice.tuprolog.Var trapId, alice.tuprolog.Number time, Term tuple){
		try {
			Term newTuple=tuple.getRenamedCopy();
			LogicTuple tuArg=new LogicTuple(newTuple);
			TrapGenerator gen = vm.addTrapGenerator(time.longValue(),tuArg);
			if (gen!=null){
				return unify(trapId,new alice.tuprolog.Long(gen.getId()));
			} else {
				return false;
			}
		} catch (Exception ex){
			return false;
		}
	}
	
	public boolean kill_trap_1(alice.tuprolog.Term id){
		try {
			long idGen = ((alice.tuprolog.Number)id).longValue();
			return vm.removeTrapGenerator(idGen);
		} catch (Exception ex){
			return false;
		}
	}
	
	// LIB extension 

	public boolean in_all_r_2(Term arg0, Term arg1) {
        LogicTuple tuArg=new LogicTuple(arg0);
        java.util.List tuples=vm.removeMatchingTuples(tuArg);
        java.util.Iterator it = tuples.listIterator();
        Term[] terms = new Term[tuples.size()];
        int index = 0;
        while (it.hasNext()){
        		terms[index++]=getRenamedCopy(((LogicTuple)it.next()).toTerm());
        }
        return unify(arg1,new Struct(terms));
	}
	
	public boolean rd_all_r_2(Term arg0, Term arg1) {
        LogicTuple tuArg=new LogicTuple(arg0);
        java.util.List tuples=vm.readMatchingTuples(tuArg);
        java.util.Iterator it = tuples.listIterator();
        Term[] terms = new Term[tuples.size()];
        int index = 0;
        while (it.hasNext()){
        		terms[index++]=getRenamedCopy(((LogicTuple)it.next()).toTerm());
        }
        return unify(arg1,new Struct(terms));
	}

	public boolean out_all_r_1(Term arg0) {
        if (!arg0.isList()){
            return false;
        }
        java.util.Iterator it = ((Struct)arg0).listIterator();
        while (it.hasNext()){
            Term t = ((Term)it.next()).getRenamedCopy();	
            vm.addTuple(new LogicTuple(t));
        }
        return true;
	}
	
    //

    protected void spy(String msg){
        //System.out.println("[ react ] "+msg);
    }
}
