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
package alice.respect.core;

import java.util.AbstractMap;
import java.util.LinkedHashMap;

import alice.respect.api.EnvId;
import alice.respect.api.TupleCentreId;

import  alice.tuprolog.*;
import alice.tuprolog.Var;

import  alice.logictuple.*;

import alice.tuplecentre.api.IId;
import alice.tuplecentre.core.*;


/**
 * TuProlog library defining the behaviour
 * of ReSpecT primitives, used inside ReSpecT VM.
 * 
 * @author aricci
 *
 */
@SuppressWarnings("serial")
public class Library extends alice.tuprolog.Library {

    RespectVMContext vm;

/*
    private static final Struct op_out = new Struct("out");
    private static final Struct op_in = new Struct("in");
    private static final Struct op_inp = new Struct("inp");
    private static final Struct op_rd = new Struct("rd");
    private static final Struct op_rdp = new Struct("rdp");
  */  
    
    public Library(){
    }

    public String getTheory(){
        return 
        ":- op(  600, xfx,  '?'). \n"+
        ":- op(  550, xfx,  '@'). \n"+
        "TC ? Op :- not(TC = X @ Y), TC@localhost ? Op, !. \n"+
        "TC ? out(T) :- out(T,TC). \n"+
        "TC ? out_s(T) :- out_s(T,TC). \n"+
        "TC ? in(T) :- in(T,TC). \n"+
        "TC ? in_s(T) :- in_s(T,TC). \n"+
        "TC ? inp(T) :- inp(T,TC). \n"+
        "TC ? inp_s(T) :- inp_s(T,TC). \n"+
        "TC ?  rd(T) :- rd(T,TC). \n"+
        "TC ? rd_s(T) :- rd_s(T,TC). \n"+
        "TC ? rdp(T) :- rdp(T,TC). \n"+
        "TC ? rdp_s(T) :- rdp_s(T,TC). \n"+
        "TC ? no(T) :- no(T,TC). \n"+
        "TC  ? set_s(T) :- set_s(T,TC). \n"+
        "TC  ? get_s(T) :- get_s(T,TC). \n"+
        
        "out(T):-out(T,this@localhost). \n" +
        "in(T):-in(T,this@localhost). \n"+
        
        "Env ? getEnv(Key,Value) :- getEnv(Env,Key,Value). \n" +
        "Env ? setEnv(Key,Value) :- setEnv(Env,Key,Value). \n" +
        "out_s(T):-out_s(T,this@localhost). \n"+
        
        "in_s(T):-in_s(T,this@localhost). \n"+
        "rd(T):-rd(T,this@localhost). \n"+
        "rd_s(T):-rd(T,this@localhost). \n"+
        "inp(T):-inp(T,this@localhost). \n"+
        "inp_s(T):-inp_s(T,this@localhost). \n"+
        "rdp(T):-rdp(T,this@localhost). \n"+
        "rdp_s(T):-rdp_s(T,this@localhost). \n"+
        "no(T):-no(T,this@localhost). \n"+
        "set_s(T):-set_s(T,this@localhost). \n"+
        "get_s(T):-get_s(T,this@localhost). \n"+
        "inp_s(T,TC) :- in_s(T,TC). \n"+
        "rdp_s(T,TC) :- rd_s(T,TC). \n"+
        //GUARD ALIASES
        "completion :- response. \n"+
        "invocation :- request. \n"+
        "between(T1,T2) :- before(T1), after(T2). \n"+
        "operation :- from_agent, to_tc. \n"+
        "link_in :- from_tc,to_tc,exo,intra. \n"+
        "link_out :- from_tc,to_tc,endo,inter. \n"+
        "internal :- from_tc, to_tc, endo, intra. \n";
    }
    
    public void init(RespectVMContext m){
        vm=m;
    }

    public boolean in_2(Term arg0, Term arg1){
        
    	spy("ENTERING READ");
    	String tcName = null;
    	TupleCentreId tid = null;
    	
    	try{
    		tid=new TupleCentreId(arg1);
    		
    	}catch(Exception e){
    		
    		e.printStackTrace();
    		spy("Invalid tuple centre identifier");
    		return false;
    	}
    	
    	tcName = tid.getName();
    	
        //spy("out_r "+g+" BEGIN");
    	LogicTuple tuArg=new LogicTuple(arg0);
    	/*insert by Enrico Rossignoli*/
    	AbstractMap<Var,Var> v = new LinkedHashMap<Var,Var>();
    	/*end insert*/
    	
    	if(tcName.equals("this")){
	    	//spy("rd_r "+g+" BEGIN");
	        
	        alice.tuplecentre.api.Tuple tuple=vm.removeMatchingTuple(tuArg);
	        if (tuple!=null){
	            Term term=((LogicTuple)tuple).toTerm();
	            /*Before*/
	            //unify(arg0,getRenamedCopy(term));
	            
	            /*insert by Enrico Rossignoli*/
	            v.clear();
	            unify(arg0,term.copyGoal(v,0));
	            /*end insert*/
	            InputEvent ce=vm.getCurrentEvent();
	            /*Before*/
				//InternalEvent ev=new InternalEvent(ce,InternalOperation.makeInR(new LogicTuple(arg0.getRenamedCopy())));
				
				/*Insert by Enrico Rossignoli*/
	            //AbstractMap<Var,Var> v2 = new LinkedHashMap<Var,Var>();
				v.clear();
				InternalEvent ev=new InternalEvent(ce,InternalOperation.makeInR(new LogicTuple(arg0.copyGoal(v,0))));
				/*end insert*/
				ev.setSource(ce.getReactingTC());
	            ev.setTarget(ce.getReactingTC());
				vm.fetchTriggeredReactions(ev);
	            //spy("rd_r "+arg0+" success");
	            return true;
	        } else {
	            //spy("rd_r "+arg0+" fail");
	            return false;
	            	
	            	
	        }
	    }else{
	    	spy("IT IS NOT THIS TC");	
	    	spy("TC NAME:"+tid);
	    	spy("IT IS NOT THIS TC");
	    	InputEvent ce=vm.getCurrentEvent();
		/*	InternalEvent evt = new InternalEvent(ce,InternalOperation.makeRdR(new LogicTuple(arg0.getRenamedCopy())));
			evt.setTarget(tid);*/
			
	    	/*Before*/
			//InputEvent out_ev = new InputEvent(ce.getReactingTC(),RespectOperation.makeIn(new LogicTuple(arg0.getRenamedCopy()),null),tid,vm.getCurrentTime());
			/*inseert by Enrico Rossignoli*/
	    	v.clear();
	    	InputEvent out_ev = new InputEvent(ce.getReactingTC(),RespectOperation.makeIn(getProlog(), new LogicTuple(arg0.copyGoal(v,0)),null),tid,vm.getCurrentTime());
			/*end insert*/
			out_ev.setIsLinking(true);
			out_ev.setTarget(tid);
			vm.addTemporaryOutputEvent(out_ev);
			spy("TC NAME AGAIN:"+out_ev.getTarget());
			spy("GET OPERATION: "+out_ev.getOperation());
			//vm.fetchTriggeredReactions(evt);
			
            
            return true;
	    	
	    }
    }

    
public boolean inp_2(Term arg0, Term arg1){
        
    	spy("ENTERING READ");
    	String tcName = null;
    	TupleCentreId tid = null;
    	
    	try{
    		tid=new TupleCentreId(arg1);
    		
    	}catch(Exception e){
    		
    		e.printStackTrace();
    		spy("Invalid tuple centre identifier");
    		return false;
    	}
    	
    	tcName = tid.getName();
    	
        //spy("out_r "+g+" BEGIN");
    	LogicTuple tuArg=new LogicTuple(arg0);
    	/*insert by Enrico Rossignoli*/
    	AbstractMap<Var,Var> v = new LinkedHashMap<Var,Var>();
    	/*end insert*/
    	
    	if(tcName.equals("this")){
	    	//spy("rd_r "+g+" BEGIN");
	        
	        alice.tuplecentre.api.Tuple tuple=vm.removeMatchingTuple(tuArg);
	        if (tuple!=null){
	            Term term=((LogicTuple)tuple).toTerm();
	            /*Before*/
	            //unify(arg0,getRenamedCopy(term));
	            
	            /*insert by Enrico Rossignoli*/
	            v.clear();
	            unify(arg0,term.copyGoal(v,0));
	            /*end insert*/
	            
	            
	            InputEvent ce=vm.getCurrentEvent();
	            /*Before*/
				//InternalEvent ev=new InternalEvent(ce,InternalOperation.makeInR(new LogicTuple(arg0.getRenamedCopy())));
				
				/*Insert by Enrico Rossignoli*/
	            //AbstractMap<Var,Var> v2 = new LinkedHashMap<Var,Var>();
				v.clear();
				InternalEvent ev=new InternalEvent(ce,InternalOperation.makeInR(new LogicTuple(arg0.copyGoal(v,0))));
				/*end insert*/
				
				ev.setSource(ce.getReactingTC());
	            ev.setTarget(ce.getReactingTC());
				vm.fetchTriggeredReactions(ev);
	            //spy("rd_r "+arg0+" success");
	            return true;
	        } else {
	            //spy("rd_r "+arg0+" fail");
	            return false;
	            	
	            	
	        }
	    }else{
	    	spy("IT IS NOT THIS TC");	
	    	spy("TC NAME:"+tid);
	    	spy("IT IS NOT THIS TC");
	    	InputEvent ce=vm.getCurrentEvent();
		/*	InternalEvent evt = new InternalEvent(ce,InternalOperation.makeRdR(new LogicTuple(arg0.getRenamedCopy())));
			evt.setTarget(tid);*/
			
	    	/*Before*/
			//InputEvent out_ev = new InputEvent(ce.getReactingTC(),RespectOperation.makeInp(new LogicTuple(arg0.getRenamedCopy()),null),tid,vm.getCurrentTime());
			/*inseert by Enrico Rossignoli*/
	    	v.clear();
	    	InputEvent out_ev = new InputEvent(ce.getReactingTC(),RespectOperation.makeInp(getProlog(), new LogicTuple(arg0.copyGoal(v,0)),null),tid,vm.getCurrentTime());
			/*end insert*/
			out_ev.setIsLinking(true);
			out_ev.setTarget(tid);
			vm.addTemporaryOutputEvent(out_ev);
			spy("TC NAME AGAIN:"+out_ev.getTarget());
			spy("GET OPERATION: "+out_ev.getOperation());
			//vm.fetchTriggeredReactions(evt);
			
            
            return true;
	    	
	    }
    }

    
    public boolean rd_2(Term arg0, Term arg1){
        
    	spy("ENTERING READ");
    	String tcName = null;
    	TupleCentreId tid = null;
    	
    	try{
    		tid=new TupleCentreId(arg1);
    		
    	}catch(Exception e){
    		
    		e.printStackTrace();
    		spy("Invalid tuple centre identifier");
    		return false;
    	}
    	
    	tcName = tid.getName();
    	
        //spy("out_r "+g+" BEGIN");
    	LogicTuple tuArg=new LogicTuple(arg0);
    	/*insert by Enrico Rossignoli*/
    	AbstractMap<Var,Var> v = new LinkedHashMap<Var,Var>();
    	/*end insert*/
    	
    	if(tcName.equals("this")){
	    	//spy("rd_r "+g+" BEGIN");
	        
	        alice.tuplecentre.api.Tuple tuple=vm.readMatchingTuple(tuArg);
	        if (tuple!=null){
	            Term term=((LogicTuple)tuple).toTerm();
	            /*Before*/
	            //unify(arg0,getRenamedCopy(term));
	            
	            /*insert by Enrico Rossignoli*/
	            v.clear();
	            unify(arg0,term.copyGoal(v,0));
	            /*end insert*/
	                        
	            InputEvent ce=vm.getCurrentEvent();
	            /*Before*/
				//InternalEvent ev=new InternalEvent(ce,InternalOperation.makeRdR(new LogicTuple(arg0.getRenamedCopy())));
				/*insert by Enrico Rossignoli*/
				v.clear();
				InternalEvent ev=new InternalEvent(ce,InternalOperation.makeRdR(new LogicTuple(arg0.copyGoal(v, 0))));
				/*end insert*/
				ev.setSource(ce.getReactingTC());
	            ev.setTarget(ce.getReactingTC());
				vm.fetchTriggeredReactions(ev);
	            //spy("rd_r "+arg0+" success");
	            return true;
	        } else {
	            //spy("rd_r "+arg0+" fail");
	            return false;
	            	
	            	
	        }
	    }else{
	    	spy("IT IS NOT THIS TC");	
	    	
	    	spy("TC TARGET NAME:"+tid);
	    	spy("IT IS NOT THIS TC");
	    	InputEvent ce=vm.getCurrentEvent();
	    	spy("TC SOURCE NAME:"+ce.getReactingTC());
		/*	InternalEvent evt = new InternalEvent(ce,InternalOperation.makeRdR(new LogicTuple(arg0.getRenamedCopy())));
			evt.setTarget(tid);*/
	    	/*Before*/
			//InputEvent out_ev = new InputEvent(ce.getReactingTC(),RespectOperation.makeRd(new LogicTuple(arg0.getRenamedCopy()),null),tid,vm.getCurrentTime());
			/*insert by Enrico Rossignoli*/
	    	v.clear();
			InputEvent out_ev = new InputEvent(ce.getReactingTC(),RespectOperation.makeRd(getProlog(), new LogicTuple(arg0.copyGoal(v, 0)),null),tid,vm.getCurrentTime());
			/*end insert*/
			out_ev.setIsLinking(true);
			out_ev.setTarget(tid);
			vm.addTemporaryOutputEvent(out_ev);
			spy("TC NAME AGAIN:"+out_ev.getTarget());
			spy("GET OPERATION: "+out_ev.getOperation());
			//vm.fetchTriggeredReactions(evt);
			
            
            return true;
	    	
	    }
    	
    }
    
    	
    public boolean rdp_2(Term arg0, Term arg1){
        
    	spy("ENTERING READ");
    	String tcName = null;
    	TupleCentreId tid = null;
    	
    	try{
    		tid=new TupleCentreId(arg1);
    		
    	}catch(Exception e){
    		
    		e.printStackTrace();
    		spy("Invalid tuple centre identifier");
    		return false;
    	}
    	
    	tcName = tid.getName();
    	
        //spy("out_r "+g+" BEGIN");
    	LogicTuple tuArg=new LogicTuple(arg0);
    	/*insert by Enrico Rossignoli*/
    	AbstractMap<Var,Var> v = new LinkedHashMap<Var,Var>();
    	/*end insert*/
    	
    	if(tcName.equals("this")){
	    	//spy("rd_r "+g+" BEGIN");
	        
	        alice.tuplecentre.api.Tuple tuple=vm.readMatchingTuple(tuArg);
	        if (tuple!=null){
	            Term term=((LogicTuple)tuple).toTerm();
	            /*Before*/
	            //unify(arg0,getRenamedCopy(term));
	            
	            /*insert by Enrico Rossignoli*/
	            v.clear();
	            unify(arg0,term.copyGoal(v,0));
	            /*end insert*/
	            
	            InputEvent ce=vm.getCurrentEvent();
	            /*Before*/
				//InternalEvent ev=new InternalEvent(ce,InternalOperation.makeRdR(new LogicTuple(arg0.getRenamedCopy())));
				/*insert by Enrico Rossignoli*/
	            v.clear();
				InternalEvent ev=new InternalEvent(ce,InternalOperation.makeRdR(new LogicTuple(arg0.copyGoal(v, 0))));
				/*end insert*/
				ev.setSource(ce.getReactingTC());
	            ev.setTarget(ce.getReactingTC());
				vm.fetchTriggeredReactions(ev);
	            //spy("rd_r "+arg0+" success");
	            return true;
	        } else {
	            //spy("rd_r "+arg0+" fail");
	            return false;
	            	
	            	
	        }
	    }else{
	    	spy("IT IS NOT THIS TC");	
	    	spy("TC NAME:"+tid);
	    	spy("IT IS NOT THIS TC");
	    	InputEvent ce=vm.getCurrentEvent();
		/*	InternalEvent evt = new InternalEvent(ce,InternalOperation.makeRdR(new LogicTuple(arg0.getRenamedCopy())));
			evt.setTarget(tid);*/
			/*Before*/
			//InputEvent out_ev = new InputEvent(ce.getReactingTC(),RespectOperation.makeRdp(new LogicTuple(arg0.getRenamedCopy()),null),tid,vm.getCurrentTime());
			/*insert by Enrico Rossignoli*/
	    	v.clear();
			InputEvent out_ev = new InputEvent(ce.getReactingTC(),RespectOperation.makeRdp(getProlog(), new LogicTuple(arg0.copyGoal(v, 0)),null),tid,vm.getCurrentTime());
			/*end insert*/
			out_ev.setIsLinking(true);
			out_ev.setTarget(tid);
			vm.addTemporaryOutputEvent(out_ev);
			spy("TC NAME AGAIN:"+out_ev.getTarget());
			spy("GET OPERATION: "+out_ev.getOperation());
			//vm.fetchTriggeredReactions(evt);
			
            
            return true;
	    	
	    }
    }
    
    
    
    
    
    public boolean no_2(Term arg0, Term arg1){
    	String tcName = null;
    	TupleCentreId tid = null;
    	try{
    		tid=new TupleCentreId(arg1);
    		
    	}catch(Exception e){
    		
    		e.printStackTrace();
    		spy("Invalid tuple centre identifier");
    		return false;
    	}
    	
    	tcName = tid.getName();
    	/*insert by Enrico Rossignoli*/
    	AbstractMap<Var,Var> v = new LinkedHashMap<Var,Var>();
    	/*end insert*/
        //spy("out_r "+g+" BEGIN");

    	if(tcName.equals("this")){
	        LogicTuple tuArg=new LogicTuple(arg0);
	        alice.tuplecentre.api.Tuple tuple=vm.readMatchingTuple(tuArg);
	        if (tuple==null){
	        	InputEvent ce=vm.getCurrentEvent();
	        	/*Before*/
				//InternalEvent ev=new InternalEvent(ce,InternalOperation.makeNoR(new LogicTuple(arg0.getRenamedCopy())));
				/*inseert by Enrico Rossignoli*/
	        	v.clear();
				InternalEvent ev=new InternalEvent(ce,InternalOperation.makeNoR(new LogicTuple(arg0.copyGoal(v, 0))));
				/*end insert*/
				ev.setSource(ce.getReactingTC());
	            ev.setTarget(ce.getReactingTC());
				vm.fetchTriggeredReactions(ev);
	            //spy("no_r "+arg0+" success");
	            return true;
	        } else {
	            //spy("no_r "+arg0+" fail");
	            return false;
	        }
    	}else{
	    	spy("IT IS NOT THIS TC");	
	    	spy("TC NAME:"+tid);
	    	spy("IT IS NOT THIS TC");
	    	InputEvent ce=vm.getCurrentEvent();
		/*	InternalEvent evt = new InternalEvent(ce,InternalOperation.makeRdR(new LogicTuple(arg0.getRenamedCopy())));
			evt.setTarget(tid);*/
			/*Before*/
			//InputEvent out_ev = new InputEvent(ce.getReactingTC(),RespectOperation.makeNo(new LogicTuple(arg0.getRenamedCopy()),null),tid,vm.getCurrentTime());
			/*Insert by Enrico Rossignoli*/
	    	v.clear();
			InputEvent out_ev = new InputEvent(ce.getReactingTC(),RespectOperation.makeNo(getProlog(), new LogicTuple(arg0.copyGoal(v, 0)),null),tid,vm.getCurrentTime());
			/*end insert*/
			out_ev.setIsLinking(true);
			out_ev.setTarget(tid);
			vm.addTemporaryOutputEvent(out_ev);
			spy("TC NAME AGAIN:"+out_ev.getTarget());
			spy("GET OPERATION: "+out_ev.getOperation());
			//vm.fetchTriggeredReactions(evt);
			
            
            return true;
	    	
	    }
    }

    public boolean out_2(Term arg0,Term arg1){
    	String tcName = null;
    	TupleCentreId tid = null;
    	try{
    		tid=new TupleCentreId(arg1);
    		
    	}catch(Exception e){
    		
    		e.printStackTrace();
    		spy("Invalid tuple centre identifier");
    		return false;
    	}
    	
    	tcName = tid.getName();
    	
        spy("tc name: "+tcName);
        /*insert by Enrico Rossignoli*/
    	AbstractMap<Var,Var> v = new LinkedHashMap<Var,Var>();
    	/*end insert*/

    	if(tcName.equals("this")){
	        // get a renamed copy
    		/*Before*/
//	        Term newArg=arg0.getRenamedCopy();
	        /*insert by Enrico Rossignoli*/
    		v.clear();
    		Term newArg=arg0.copyGoal(v, 0);
	        /*end insert*/
	
	        LogicTuple tuArg=new LogicTuple(newArg);
	        vm.addTuple(tuArg);
	        InputEvent ce=vm.getCurrentEvent();
	        /*Before*/
//			InternalEvent ev=new InternalEvent(ce,InternalOperation.makeOutR(new LogicTuple(arg0.getRenamedCopy())));
			/*insert by Enrico Rossignoli*/
	        v.clear();
	        InternalEvent ev=new InternalEvent(ce,InternalOperation.makeOutR(new LogicTuple(arg0.copyGoal(v, 0))));
			/*end insert*/
			ev.setSource(ce.getReactingTC());
	        ev.setTarget(ce.getReactingTC());
			vm.fetchTriggeredReactions(ev);
	        //spy("out_r "+arg0+" success");
	        return true;
    	}else{
	    	spy("IT IS NOT THIS TC");	
	    	spy("TC NAME:"+tid);
	    	spy("IT IS NOT THIS TC");
	    	InputEvent ce=vm.getCurrentEvent();
		/*	InternalEvent evt = new InternalEvent(ce,InternalOperation.makeRdR(new LogicTuple(arg0.getRenamedCopy())));
			evt.setTarget(tid);*/
			/*Before*/
//			InputEvent out_ev = new InputEvent(ce.getReactingTC(),RespectOperation.makeOut(new LogicTuple(arg0.getRenamedCopy()),null),tid,vm.getCurrentTime());
			/*insert by Enrico Rossignoli*/
	    	v.clear();
	    	InputEvent out_ev = new InputEvent(ce.getReactingTC(),RespectOperation.makeOut(getProlog(), new LogicTuple(arg0.copyGoal(v, 0)),null),tid,vm.getCurrentTime());
			/*end insert*/
			out_ev.setIsLinking(true);
			out_ev.setTarget(tid);
			vm.addTemporaryOutputEvent(out_ev);
			spy("TC NAME AGAIN:"+out_ev.getTarget());
			spy("GET OPERATION: "+out_ev.getOperation());
			//vm.fetchTriggeredReactions(evt);
			
            
            return true;
	    	
	    }
    }

    public boolean in_s_2(Term arg0, Term arg1){
        
    	spy("ENTERING READ");
    	String tcName = null;
    	TupleCentreId tid = null;
    	
    	try{
    		tid=new TupleCentreId(arg1);
    		
    	}catch(Exception e){
    		
    		e.printStackTrace();
    		spy("Invalid tuple centre identifier");
    		return false;
    	}
    	
    	tcName = tid.getName();
    	/*insert by Enrico Rossignoli*/
    	AbstractMap<Var,Var> v = new LinkedHashMap<Var,Var>();
    	/*end insert*/
    	
        //spy("out_r "+g+" BEGIN");
    	LogicTuple tuArg=new LogicTuple(arg0);
    	
    	if(tcName.equals("this")){
	    	//spy("rd_r "+g+" BEGIN");
	        
	        alice.tuplecentre.api.Tuple tuple=vm.removeMatchingSpecTuple(tuArg);
	        if (tuple!=null){
	            Term term=((LogicTuple)tuple).toTerm();
	            /*Before*/
//	            unify(arg0,getRenamedCopy(term));
	            /*insert by Enrico Rossignoli*/
	            v.clear();
	            unify(arg0,term.copyGoal(v, 0));
				/*end insert*/
	            InputEvent ce=vm.getCurrentEvent();
	            /*Before*/
//				InternalEvent ev=new InternalEvent(ce,InternalOperation.makeIn_sR(new LogicTuple(arg0.getRenamedCopy())));
				/*insert by Enrico Rossignoli*/
	            v.clear();
	            InternalEvent ev=new InternalEvent(ce,InternalOperation.makeIn_sR(new LogicTuple(arg0.copyGoal(v, 0))));
				/*end insert*/
				ev.setSource(ce.getReactingTC());
	            ev.setTarget(ce.getReactingTC());
				vm.fetchTriggeredReactions(ev);
	            //spy("rd_r "+arg0+" success");
	            return true;
	        } else {
	            //spy("rd_r "+arg0+" fail");
	            return false;
	            	
	            	
	        }
	    }else{
	    	spy("IT IS NOT THIS TC");	
	    	spy("TC NAME:"+tid);
	    	spy("IT IS NOT THIS TC");
	    	InputEvent ce=vm.getCurrentEvent();
		/*	InternalEvent evt = new InternalEvent(ce,InternalOperation.makeRdR(new LogicTuple(arg0.getRenamedCopy())));
			evt.setTarget(tid);*/
	    	/*Before*/
//			InputEvent out_ev = new InputEvent(ce.getReactingTC(),RespectOperation.makeInp(new LogicTuple(arg0.getRenamedCopy()),null),tid,vm.getCurrentTime());
			/*insert by Enrico Rossignoli*/
	    	v.clear();
			InputEvent out_ev = new InputEvent(ce.getReactingTC(),RespectOperation.makeInp(getProlog(), new LogicTuple(arg0.copyGoal(v, 0)),null),tid,vm.getCurrentTime());
			/*end insert*/
			out_ev.setIsLinking(true);
			out_ev.setTarget(tid);
			vm.addTemporaryOutputEvent(out_ev);
			spy("TC NAME AGAIN:"+out_ev.getTarget());
			spy("GET OPERATION: "+out_ev.getOperation());
			//vm.fetchTriggeredReactions(evt);
			
            
            return true;
	    	
	    }
    }
    
    public boolean rd_s_2(Term arg0, Term arg1){
        
    	spy("ENTERING READ");
    	String tcName = null;
    	TupleCentreId tid = null;
    	
    	try{
    		tid=new TupleCentreId(arg1);
    		
    	}catch(Exception e){
    		
    		e.printStackTrace();
    		spy("Invalid tuple centre identifier");
    		return false;
    	}
    	
    	tcName = tid.getName();
    	
        //spy("out_r "+g+" BEGIN");
    	LogicTuple tuArg=new LogicTuple(arg0);
    	/*insert by Enrico Rossignoli*/
    	AbstractMap<Var,Var> v = new LinkedHashMap<Var,Var>();
    	/*end insert*/
    	
    	if(tcName.equals("this")){
	    	//spy("rd_r "+g+" BEGIN");
	        
	        alice.tuplecentre.api.Tuple tuple=vm.readMatchingSpecTuple(tuArg);
	        if (tuple!=null){
	            Term term=((LogicTuple)tuple).toTerm();
	            /*Before*/
//	            unify(arg0,getRenamedCopy(term));
	            /*insert by Enrico Rossignoli*/
	            v.clear();
	            unify(arg0,term.copyGoal(v, 0));
				/*end insert*/
	            InputEvent ce=vm.getCurrentEvent();
	            /*Before*/
//				InternalEvent ev=new InternalEvent(ce,InternalOperation.makeRd_sR(new LogicTuple(arg0.getRenamedCopy()))); 
				/*insert by Enrico Rossignoli*/
	            v.clear();
	            InternalEvent ev=new InternalEvent(ce,InternalOperation.makeRd_sR(new LogicTuple(arg0.copyGoal(v, 0))));
				/*end insert*/
				ev.setSource(ce.getReactingTC());
	            ev.setTarget(ce.getReactingTC());
				vm.fetchTriggeredReactions(ev);
	            //spy("rd_r "+arg0+" success");
	            return true;
	        } else {
	            //spy("rd_r "+arg0+" fail");
	            return false;
	            	
	            	
	        }
	    }else{
	    	spy("IT IS NOT THIS TC");	
	    	spy("TC NAME:"+tid);
	    	spy("IT IS NOT THIS TC");
	    	InputEvent ce=vm.getCurrentEvent();
		/*	InternalEvent evt = new InternalEvent(ce,InternalOperation.makeRdR(new LogicTuple(arg0.getRenamedCopy())));
			evt.setTarget(tid);*/
	    	/*Before*/
//			InputEvent out_ev = new InputEvent(ce.getReactingTC(),RespectOperation.makeRdp_s(new LogicTuple(arg0.getRenamedCopy()),null),tid,vm.getCurrentTime());
			/*insert by Enrico Rossignoli*/
			v.clear();
			InputEvent out_ev = new InputEvent(ce.getReactingTC(),RespectOperation.makeRdp_s(getProlog(), new LogicTuple(arg0.copyGoal(v, 0)),null),tid,vm.getCurrentTime());
			/*end insert*/
			out_ev.setIsLinking(true);
			out_ev.setTarget(tid);
			vm.addTemporaryOutputEvent(out_ev);
			spy("TC NAME AGAIN:"+out_ev.getTarget());
			spy("GET OPERATION: "+out_ev.getOperation());
			//vm.fetchTriggeredReactions(evt);
			
            
            return true;
	    	
	    }
    }
    public boolean out_s_2(Term arg0,Term arg1){
    	String tcName = null;
    	TupleCentreId tid = null;
    	try{
    		tid=new TupleCentreId(arg1);
    		
    	}catch(Exception e){
    		
    		e.printStackTrace();
    		spy("Invalid tuple centre identifier");
    		return false;
    	}
    	
    	tcName = tid.getName();
    	
        spy("tc name: "+tcName);
        /*insert by Enrico Rossignoli*/
    	AbstractMap<Var,Var> v = new LinkedHashMap<Var,Var>();
    	/*end insert*/

    	if(tcName.equals("this")){
	        // get a renamed copy
    		/*Before*/
//	        Term newArg=arg0.getRenamedCopy();
	        /*insert by Enrico Rossignoli*/
	        v.clear();
	        Term newArg=arg0.copyGoal(v, 0);
			/*end insert*/
	        LogicTuple tuArg=new LogicTuple(newArg);
	        vm.addSpecTuple(tuArg);
	        InputEvent ce=vm.getCurrentEvent();
	        /*Before*/
//			InternalEvent ev=new InternalEvent(ce,InternalOperation.makeOut_sR(new LogicTuple(arg0.getRenamedCopy()))); 
			/*insert by Enrico Rossignoli*/
			v.clear();
			InternalEvent ev=new InternalEvent(ce,InternalOperation.makeOut_sR(new LogicTuple(arg0.copyGoal(v, 0))));
			/*end insert*/
			ev.setSource(ce.getReactingTC());
	        ev.setTarget(ce.getReactingTC());
			vm.fetchTriggeredReactions(ev);
	        //spy("out_r "+arg0+" success");
	        return true;
    	}else{
	    	spy("IT IS NOT THIS TC");	
	    	spy("TC NAME:"+tid);
	    	spy("IT IS NOT THIS TC");
	    	InputEvent ce=vm.getCurrentEvent();
		/*	InternalEvent evt = new InternalEvent(ce,InternalOperation.makeRdR(new LogicTuple(arg0.getRenamedCopy())));
			evt.setTarget(tid);*/
	    	/*Before*/
//			InputEvent out_ev = new InputEvent(ce.getReactingTC(),RespectOperation.makeOut_s(new LogicTuple(arg0.getRenamedCopy()),null),tid,vm.getCurrentTime());
			/*insert by Enrico Rossignoli*/
			v.clear();
			InputEvent out_ev = new InputEvent(ce.getReactingTC(),RespectOperation.makeOut_s(getProlog(), new LogicTuple(arg0.copyGoal(v, 0)),null),tid,vm.getCurrentTime());
			/*end insert*/
			out_ev.setIsLinking(true);
			out_ev.setTarget(tid);
			vm.addTemporaryOutputEvent(out_ev);
			spy("TC NAME AGAIN:"+out_ev.getTarget());
			spy("GET OPERATION: "+out_ev.getOperation());
			//vm.fetchTriggeredReactions(evt);
			
            
            return true;
	    	
	    }
    }

    public boolean current_agent_1(Term arg0){
        Term term=new Struct(vm.getCurrentReactionEvent().getId().toString());
        return unify(arg0,term);
    }

    public boolean current_tc_1(Term arg0){
        Term term=new Struct(vm.getId().toString());
        return unify(arg0,term);
    }

    public boolean current_operation_1(Term arg0){
        return unify(arg0,vm.getCurrentReactionTerm());
    }

    public boolean current_source_1(Term arg0){
        return unify(arg0,new Struct(""+vm.getCurrentReactionEvent().getReactingTC()));
    }
    
    public boolean current_target_1(Term arg0){
        return unify(arg0,new Struct(""+vm.getCurrentReactionEvent().getReactingTC()));
    }
    
    public boolean current_tuple_1(Term arg0){
    	/*Before*/
//        return unify(arg0,getRenamedCopy(vm.getCurrentReactionTerm().getArg(0)));
        /*insert by Enrico Rossignoli*/
        AbstractMap<Var,Var> v = new LinkedHashMap<Var,Var>();
        return unify(arg0,vm.getCurrentReactionTerm().getArg(0).copyGoal(v, 0));
		/*end insert*/
    }

    public boolean event_operation_1(Term arg0){
        return unify(arg0,new Struct(""+vm.getCurrentReactionEvent().getOperation()));
    }
    
    public boolean event_tuple_1(Term arg0){
        return unify(arg0,new Struct(""+((RespectOperation)vm.getCurrentReactionEvent().getOperation()).getLogicTupleArgument()));
    }
    
    public boolean event_time_1(Term arg0){
        return unify(arg0,new alice.tuprolog.Long(vm.getCurrentReactionEvent().getTime()));
    }
    
    public boolean event_source_1(Term arg0){
        return unify(arg0,new Struct(""+vm.getCurrentReactionEvent().getSource()));
    }
    
    public boolean from_env_0(){
    	return vm.getCurrentReactionEvent().getSource().isEnv(); 
    }
    public boolean to_env_0(){
    	return vm.getCurrentReactionEvent().getTarget().isEnv(); 
    }
    public boolean event_target_1(Term arg0){
        return unify(arg0,new Struct(""+vm.getCurrentReactionEvent().getTarget()));
    }
    public boolean env_2(Term key, Term value){
    	/*Before*/
//    	if (value.isVar()){
//    		String res=vm.getCurrentReactionEvent().getEventProp(key.getTerm().toString());
//    		//The specified value doesn't exists.
//    		if (res==null)return false;
//    		return unify(value, new Struct(res));
//    	}
//    	return false;
    	/*insert by Enrico Rossignoli*/
    	if (value instanceof alice.tuprolog.Var)
    	{
    		String res=vm.getCurrentReactionEvent().getEventProp(key.getTerm().toString());
    		//The specified value doesn't exists.
    		if (res==null)return false;
    		return unify(value, new Struct(res));
    	}
    	return false;
    	/*end insert*/
    }
    public boolean pre_0(){
        Event ev = vm.getCurrentReactionEvent();
        if (ev.isInput()){
            return true;
        } else if (ev.isOutput()){
            return false;
        } else {
        	InternalOperation op= ((InternalEvent)ev).getInternalOperation();
        	if (op.isOutR()){
        		return true;
        	} else {
        		return false;
        	}
        }
    }

    public boolean post_0(){
        Event ev = vm.getCurrentReactionEvent();
		if (ev.isOutput()){
			return true;
		} else if (ev.isInput()){
			return false;
		} else {
			InternalOperation op= ((InternalEvent)ev).getInternalOperation();
			if (op.isOutR()){
				return false;
			} else {
				return true;
			}
		}
    }
    
    
    public boolean request_0(){
       
    	Event ev = vm.getCurrentReactionEvent();
        this.spy("CurrentReactionEvent: "+ev);
        TupleCentreOperation op = ev.getOperation();
        
        if(!op.isResultDefined()){
        	
        	return true;
        }
        else return false;
        
    }

    
    public boolean before_1(Term time){
    	
    	Event ev = vm.getCurrentReactionEvent();
    	long evtTime = ev.getTime();
    	long compareTime;
    	try{
    		
    		compareTime = ((alice.tuprolog.Number)time).longValue();
    		
    	}catch(Exception e){
    		
    		this.spy(e.getMessage());
    		return false;
    		
    	}
    	
    	if(evtTime<=compareTime){
    		
    		return true;
    		
    	}
    	
        return false;
    	
   
    }
    
    
    public boolean after_1(Term time){
    	
    	return !before_1(time);
    	
   
    }
    
    
    public boolean response_0(){
    	
    	Event ev = vm.getCurrentReactionEvent();
        
        TupleCentreOperation op = ev.getOperation();
        
        if(op.isResultDefined()){
        	spy("IS RES DEF: "+op+" "+op.isResultDefined());
        	if(ev instanceof OutputEvent){
        		spy("IS RES DEF: OUTPUT");
        		boolean isLinking = ((OutputEvent)ev).isLinking();
        		if(isLinking) return false;
        		else return true;
        	}else{
        		return true;
        	}
        }
        
        return false;
        //return op.isResultDefined();
        
    }
    
    public boolean intra_0(){
    	
    	Event ev = vm.getCurrentReactionEvent();
        
    	IId id = ev.getTarget();
    	
    	IId c = this.vm.getId();
    	
    	if(c.toString().equals(id.toString())){
    		
    		return true;
    		
    	}else return false;
    }
    
    public boolean inter_0(){
    	
    	/*Event ev = vm.getCurrentReactionEvent();
        
    	IId id = ev.getTarget();
    	
    	IId c = ev.getReactingTC();
    	
    	if(!c.toString().equals(id.toString())){
    		
    		return true;
    		
    	}else return false;*/
    	return !intra_0();
    }
    
    public boolean endo_0(){
    	
    	/*Event ev = vm.getCurrentReactionEvent();
        
    	IId id = ev.getId();
    	
    	IId c = ev.getReactingTC();
    	
    	if(c.toString().equals(id.toString())){
    		
    		return true;
    		
    	}else return false;*/
    	return !exo_0();
    }
    
    
  
    
    public boolean exo_0(){
    	
    	Event ev = vm.getCurrentReactionEvent();
    	
    	vm.spy("EVALUATING EXO source: "+ev.getSource()+" c: "+ev.getReactingTC());
        
    	IId id = ev.getId();
    	
    	IId c = this.vm.getId();
    	//vm.spy("EVALUATING EXO current"+);
    	
    	if(!c.toString().equals(id.toString())){
    		
    		return true;
    		
    	}else return false;
    }
    
    
    
    
    public boolean from_agent_0(){
    	
    	Event ev = vm.getCurrentReactionEvent();
        
    	IId id = ev.getId();
    	
    	if(id.isAgent()){
    		
    		return true;
    		
    	}else return false;
    }
    
    public boolean to_agent_0(){
    	
    	Event ev = vm.getCurrentReactionEvent();
        
    	IId id = ev.getTarget();
    	
    	if(id.isAgent()){
    		
    		return true;
    		
    	}else return false;
    }
    
    public boolean from_tc_0(){
    	
    	Event ev = vm.getCurrentReactionEvent();
        
    	IId id = ev.getId();
    	
    	if(id.isTC()){
    		spy("IS FROM TC");
    		return true;
    		
    	}else return false;
    }
    
    
    public boolean to_tc_0(){
    	
    	Event ev = vm.getCurrentReactionEvent();
        spy("EVENT++++>"+ev+" T: "+ev.getTarget());
    	IId id = ev.getTarget();
    	
    	if(id.isTC()){
    		
    		return true;
    		
    	}else return false;
    }

   /** public boolean success_0(){
        Event ev = vm.getCurrentReactionEvent();
        if (!ev.isOutput()){
            return true;
        } else {
        	RespectOperation op = (RespectOperation)ev.getOperation();
			if (op.isInp() && op.getLogicTupleResult()==null){
				return false;
			} else if (op.isRdp() && op.getLogicTupleResult()==null){
				return false;
			} else {
				return true;
			}
        }
    }

    public boolean failure_0(){
        Event ev = vm.getCurrentReactionEvent();
		if (!ev.isOutput()){
			return false;
		} else {
			RespectOperation op = (RespectOperation)ev.getOperation();
			if (op.isInp() && op.getLogicTupleResult()==null){
				return true;
			} else if (op.isRdp() && op.getLogicTupleResult()==null){
				return true;
			} else {
				return false;
			}
		}
    }


    public boolean spawn_3(Term agentId, Term agentType, Term arg){
        if (arg.isList()){
            Struct args = (Struct)arg;
            Struct sp_arg = new Struct("spawn",agentId.getRenamedCopy(),
											 agentType.getRenamedCopy(),
											(Struct)args.getRenamedCopy());
			
            InternalOperation spop = InternalOperation.makeSpawn(new LogicTuple(sp_arg));
            alice.tuplecentre.core.OutputEvent ev= new RespectOutputEvent(vm.getCurrentEvent(),spop);
            vm.addOutputEvent(ev);
            return true;
        } else {
            return false;
        }

    }*/
    
    public boolean success_0(){
        Event ev = vm.getCurrentReactionEvent();
        RespectOperation op = (RespectOperation)ev.getOperation();
       
        if(op.isResultDefined()){
        	Term res = op.getLogicTupleResult().toTerm();
        	/*Before*/
        	//if(!res.isNull()){
    		/*insert by Enrico Rossignoli*/
    		if(res != null){
    		/*end insert*/
        		
        		return true;
        		
        	}else return false;
        }
        
        return false;

    }
    
    public boolean failure_0(){
        
        return !(success_0());

    }

    public boolean current_time_1(Term arg0){
        long time=vm.getCurrentTime();
        return unify(arg0,new alice.tuprolog.Long(time));
    }

    public boolean out_tc_2(Struct stid, Term tuple){
        LogicTuple tid=new LogicTuple(stid);
        /*insert by Enrico Rossignoli*/
    	AbstractMap<Var,Var> v = new LinkedHashMap<Var,Var>();
    	/*end insert*/
        // get a renamed copy
        /*Before*/
//        Term newTuple=tuple.getRenamedCopy();
        /*insert by Enrico Rossignoli*/
        Term newTuple=tuple.copyGoal(v, 0);
        /*end insert*/
		Struct outTc = new Struct("out_tc",tid.toTerm(),newTuple);
        InternalOperation outTCop = InternalOperation.makeOutTC(new LogicTuple(outTc));
        													
        alice.tuplecentre.core.OutputEvent ev=new RespectOutputEvent(vm.getCurrentEvent(), outTCop);
      //  vm.addOutputEvent(ev);
        return true;
    }

    public boolean getEnv_3(Term env, Term key, Term val){
    	/*insert by Enrico Rossignoli*/
    	AbstractMap<Var,Var> v = new LinkedHashMap<Var,Var>();
    	AbstractMap<Var,Var> v1 = new LinkedHashMap<Var,Var>();
    	/*end insert*/
    	/*Before*/
//    	LogicTuple lt=new LogicTuple("getEnv",new TupleArgument(key.getRenamedCopy()),new TupleArgument(val.getRenamedCopy()));
    	/*insert by Enrico Rossignoli*/
    	LogicTuple lt=new LogicTuple("getEnv",new TupleArgument(key.copyGoal(v, 0)),new TupleArgument(val.copyGoal(v1, 0)));
		/*end insert*/
    	InputEvent ev=vm.getCurrentEvent();
    	InternalEvent ie=new InternalEvent(ev,InternalOperation.makeGetEnv(lt));
    	ie.setTarget(new EnvId((Struct)env));
    	ie.setSource(vm.getId());
    	
//    	if (vm.getTransducerManager().notifyOutputEnv(ie)){
//    		vm.fetchTriggeredReactions(ie);
//    		return true;
//    	}
    	return false;
    }
    
    public boolean setEnv_3(Term env, Term key, Term val){
    	/*insert by Enrico Rossignoli*/
    	AbstractMap<Var,Var> v = new LinkedHashMap<Var,Var>();
    	AbstractMap<Var,Var> v1 = new LinkedHashMap<Var,Var>();
    	/*end insert*/
    	/*Before*/
//    	LogicTuple lt=new LogicTuple("setEnv",new TupleArgument(key.getRenamedCopy()),new TupleArgument(val.getRenamedCopy()));
    	/*insert by Enrico Rossignoli*/
    	LogicTuple lt=new LogicTuple("setEnv",new TupleArgument(key.copyGoal(v, 0)),new TupleArgument(val.copyGoal(v1, 0)));
		/*end insert*/
    	InputEvent ev=vm.getCurrentEvent();
    	InternalEvent ie=new InternalEvent(ev,InternalOperation.makeSetEnv(lt));
    	ie.setTarget(new EnvId((Struct)env));
    	ie.setSource(vm.getId());
    	
//    	if (vm.getTransducerManager().notifyOutputEnv(ie)){
//    		vm.fetchTriggeredReactions(ie);
//    		return true;
//    	}
    	return false;
    }
    //

    protected void spy(String msg){
        //System.out.println("[ react ] "+msg);
    }
    
    /*insert by Enrico Rossignoli*/    
    private Prolog getProlog(){
    	return vm.getPrologCore();
    }
    
    
    
}
