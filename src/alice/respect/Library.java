/*LogicLojjjLoososo
 * Library.java
 *
 * Copyright 2000-2001-2002  aliCE team at deis.unibo.it
 *
 * This software is the proprietary information of deis.unibo.it
 * Use is subject to license terms.
 *
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

    public boolean in_r_1(Struct g){
        //spy("in_r "+g+" BEGIN");
        Term arg0=g.getTerm(0);
        LogicTuple tuArg=new LogicTuple(arg0);
        alice.tuplecentre.Tuple tuple=vm.removeMatchingTuple(tuArg);
        if (tuple!=null){
            Term term=((LogicTuple)tuple).toTerm();
            unify(arg0,getRenamedCopy(term));
            ReactionEvent ev=new Event_in_r(vm.currentEvent.getId(),(alice.respect.AgentId)vm.currentEvent.getAgentId(),(TupleCentreId)vm.getId(),tuArg);
            vm.fetchTriggeredReactions(ev);
            //spy("in_r "+arg0+" success");
            return true;
        }
        else {
            return false;
        }
    }

    public boolean rd_r_1(Struct g){
        //spy("rd_r "+g+" BEGIN");
        Term arg0=g.getTerm(0);
        LogicTuple tuArg=new LogicTuple(arg0);
        alice.tuplecentre.Tuple tuple=vm.readMatchingTuple(tuArg);
        if (tuple!=null){
            Term term=((LogicTuple)tuple).toTerm();
            unify(arg0,getRenamedCopy(term));
            ReactionEvent ev=new Event_rd_r(vm.currentEvent.getId(),(alice.respect.AgentId)vm.currentEvent.getAgentId(),(TupleCentreId)vm.getId(),tuArg);
            vm.fetchTriggeredReactions(ev);
            //spy("rd_r "+arg0+" success");
            return true;
        } else {
            //spy("rd_r "+arg0+" fail");
            return false;
        }
    }

    public boolean no_r_1(Struct g){
        //spy("no_r "+g+" BEGIN");
        Term arg0=g.getTerm(0);
        LogicTuple tuArg=new LogicTuple(arg0);
        alice.tuplecentre.Tuple tuple=vm.readMatchingTuple(tuArg);
        if (tuple==null){
            ReactionEvent ev=new Event_in_r(vm.currentEvent.getId(),(alice.respect.AgentId)vm.currentEvent.getAgentId(),(TupleCentreId)vm.getId(),tuArg);
            vm.fetchTriggeredReactions(ev);
            //spy("no_r "+arg0+" success");
            return true;
        } else {
            //spy("no_r "+arg0+" fail");
            return false;
        }
    }

    public boolean out_r_1(Struct g){
        //spy("out_r "+g+" BEGIN");
        Term arg0=g.getTerm(0);

        // get a renamed copy
        Term newArg=arg0.getRenamedCopy();

        LogicTuple tuArg=new LogicTuple(newArg);
        vm.addTuple(tuArg);
        ReactionEvent ev=new Event_out_r(vm.currentEvent.getId(),(alice.respect.AgentId)vm.currentEvent.getAgentId(),(TupleCentreId)vm.getId(),tuArg);
        vm.fetchTriggeredReactions(ev);
        //spy("out_r "+arg0+" success");
        return true;
    }

    public boolean current_agent_1(Struct g){
        Term arg0=g.getTerm(0);
        Term term=new Struct(vm.currentEvent.getAgentId().toString());
        return unify(arg0,term);
    }

    public boolean current_tc_1(Struct g){
        Term arg0=g.getTerm(0);
        Term term=new Struct(vm.getId().toString());
        return unify(arg0,term);
    }

    public boolean current_op_1(Struct g){
        Term arg0=g.getTerm(0);
        return unify(arg0,new Struct(vm.currentEvent.getOperationName()));
    }

    public boolean current_tuple_1(Struct g){
        Term arg0=g.getTerm(0);
        Event ev=vm.currentEvent;
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
        }

        return unify(arg0,getRenamedCopy(term));
    }

    public boolean pre_0(Struct g){
        return (vm.currentEvent.isToTupleCentre());
    }

    public boolean post_0(Struct g){
        return (vm.currentEvent.isFromTupleCentre());
    }

    public boolean success_0(Struct g){
        return (vm.currentEvent.isSuccess());
    }

    public boolean failure_0(Struct g){
        return (vm.currentEvent.isFailure());
    }


    public boolean spawn_3(Struct g){
        Term agentId = g.getTerm(0);
        Term agentType = g.getTerm(1);
        if (g.getTerm(2).isList()){
            Struct args = (Struct)g.getTerm(2);
            alice.tuplecentre.OutputEvent ev=
                       new OutputEvent_spawn((alice.respect.AgentId)vm.currentEvent.getAgentId(),
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

    public boolean current_time_1(Struct g){
        Term arg0=g.getTerm(0);
        long time=vm.getCurrentTime();
        return unify(arg0,new alice.tuprolog.Long(time));
    }

    public boolean out_tc_2(Struct g){
        LogicTuple tid=new LogicTuple((Struct)g.getTerm(0));
        Term tuple=g.getTerm(1);
        // get a renamed copy
        Term newTuple=tuple.getRenamedCopy();

        LogicTuple tuArg=new LogicTuple(newTuple);
        alice.tuplecentre.OutputEvent ev=new OutputEvent_out_tc((alice.respect.AgentId)vm.currentEvent.getAgentId(),(TupleCentreId)vm.getId(),tid,tuArg);
        vm.addOutputEvent(ev);
        return true;
    }

    //

    protected void spy(String msg){
        //System.out.println("[ react ] "+msg);
    }
}
