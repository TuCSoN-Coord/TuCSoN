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
import java.util.*;

import alice.respect.api.ILinkContext;
import alice.respect.api.OperationNotPossibleException;
import alice.respect.api.RespectSpecification;
import alice.respect.api.TupleCentreId;
import alice.tuplecentre.core.BehaviourSpecification;
import alice.tuplecentre.api.IId;
import alice.tuplecentre.api.InspectableEventListener;
import alice.tuplecentre.api.ObservableEventListener;
import alice.tuplecentre.api.Tuple;
import alice.tuplecentre.api.TupleTemplate;
import alice.tuplecentre.core.*;
import alice.tuprolog.*;
import alice.tuprolog.Var;
import alice.logictuple.*;
import alice.logictuple.InvalidVarNameException;

/**
 * This class defines a ReSpecT Context
 * as a specialisation of a tuple centre VM context 
 * (defining VM specific structures)
 * 
 * @see alice.tuplecentre.core.TupleCentreVM
 *
 * @author aricci
 * @version 1.0
 */
public class RespectVMContext extends alice.tuplecentre.core.TupleCentreVMContext {

	
	/**are we setting a specification from outside?*/
	private boolean isExternalSetSpec;
	
	/**Used to keep trace of theory other than reactions*/
	private Theory noReactionTh;

	/**Kist of timers scheduled for execution*/
	private List<Timer> timers;
	
    /** multiset of tuples T */
    private TupleSet        tSet;
    
    /** multiset of specification tuple Sigma */
    private TupleSet	tSpecSet;

    /** multiset of pending query set  */
    private PendingQuerySet        wSet;

    /** multiset of triggered reactions Z */
    private TRSet           zSet;
    
    /** multiset of triggered timed reactions */
    private TRSet timeSet;

    /** list of output event waiting to be served */
  // private ArrayList        outputEventList;
    
    /** list of temporary output event caused by linkability operation:
     * they are added to the output queue (outputEventList only when the
     * related reaction is successfully executed
     *  */
    private ArrayList        temporaryOutputEventList;
    
    
    
    private RespectSpecification        reactionSpec;
    
    private boolean transaction;

    private RespectVM vm;
    // engine for reactions evaluation
    private Prolog core;
    
    // engine for reactions triggering
    private Prolog trigCore;
    
    // current event serverd
    private Event currentReactionEvent;

   
    private Struct currentReactionTerm;
    
    private TransducerManager tm;
    
    // it is necessary when introducing linkability since listener for completion of
    //operations on different tuple centres can start fetching new reactions at any time
    private Object semaphore;

	private Prolog matcher = new Prolog();
    
    // Qui devo definire l'OntManager
    
    // Qui devo passare anche l'OntoManager    
    public RespectVMContext(RespectVM vm, TupleCentreId tid, int queueSize){
        super(tid, queueSize);
        
        timers = new Vector<Timer>();
        semaphore = new Object();
        tSet=new TupleSet();
        tSpecSet = new TupleSet();
        wSet=new PendingQuerySet();
        zSet=new TRSet();
        this.timeSet=new TRSet();
        this.vm = vm;
       
        // outputEventList = new ArrayList();
        temporaryOutputEventList = new ArrayList();
        
       // Qui devo assegnare l'OntManager passato come argomento
        
		try {
	        	core=new Prolog(new String[]{});
	        	/*before*/
	        	//alice.tuprolog.OutputListener
	        	/*insert by Enrico Rossignoli*/
	        	//alice.tuprolog.event.OutputListener
	        	/*end*/
	        	alice.tuprolog.event.OutputListener l=new alice.tuprolog.event.OutputListener() {
	            	public void onOutput(alice.tuprolog.event.OutputEvent ev) {
	                	System.out.print(ev.getMsg());
	        	}};
	        	core.addOutputListener(l);
			core.loadLibrary("alice.tuprolog.lib.BasicLibrary");
			core.loadLibrary("alice.tuprolog.lib.ISOLibrary");
			((alice.respect.core.Library)core.loadLibrary("alice.respect.core.Library")).init(this);
        } catch (Exception ex){
            ex.printStackTrace();
        }
		try {
			trigCore=new Prolog(new String[]{});
			trigCore.loadLibrary("alice.tuprolog.lib.BasicLibrary");
			trigCore.loadLibrary("alice.tuprolog.lib.ISOLibrary");
            trigCore.loadLibrary("alice.respect.core.Library");
            ((alice.respect.core.Library)trigCore.getLibrary("alice.respect.core.Library")).init(this);
        } catch (Exception ex){
            ex.printStackTrace();
        }

        reactionSpec=new RespectSpecification("");
        this.reset();
        
        this.isExternalSetSpec = false;

    }
    
    //Used by actuator
    public RespectVMContext(RespectVM vm, TupleCentreId tid, int queueSize, TransducerManager tm){
        super(tid, queueSize);
        
        timers = new Vector<Timer>();
        semaphore = new Object();
        tSet=new TupleSet();
        tSpecSet = new TupleSet();
        wSet=new PendingQuerySet();
        zSet=new TRSet();
        this.timeSet=new TRSet();
        this.vm = vm;
        
        this.tm=tm;
        
        System.out.println("************************************************ TRANSDUCERMANAGER VMCONTEXT"+ this.tm.toString());
       
        // outputEventList = new ArrayList();
        temporaryOutputEventList = new ArrayList();
        
		try {
	        	core=new Prolog(new String[]{});
	        	alice.tuprolog.event.OutputListener l=new alice.tuprolog.event.OutputListener() {
	            	public void onOutput(alice.tuprolog.event.OutputEvent ev) {
	                	System.out.print(ev.getMsg());
	        	}};
	        	core.addOutputListener(l);
			core.loadLibrary("alice.tuprolog.lib.BasicLibrary");
			((alice.respect.core.Library)core.loadLibrary("alice.respect.core.Library")).init(this);
        } catch (Exception ex){
            ex.printStackTrace();
        }
		try {
			trigCore=new Prolog(new String[]{});
			trigCore.loadLibrary("alice.tuprolog.lib.BasicLibrary");
            trigCore.loadLibrary("alice.respect.core.Library");
            ((alice.respect.core.Library)trigCore.getLibrary("alice.respect.core.Library")).init(this);
        } catch (Exception ex){
            ex.printStackTrace();
        }

        reactionSpec=new RespectSpecification("");
        this.reset();

    }

    public boolean triggeredReaction(){
        return !zSet.isEmpty();
    }
    
    public void fetchTriggeredReactions(Event ev){
       
    	synchronized(semaphore){
    	
    		try {
            
	            currentReactionTerm=null;
	            
	            if (ev.isInput()){
	                InputEvent ie = (InputEvent)ev;
					RespectOperation op=(RespectOperation)ev.getOperation();
	                if (op.isOut()){
	                	//System.out.println(""+op.getLogicTupleArgument().toTerm());
	                    currentReactionTerm=new Struct("out",op.getLogicTupleArgument().toTerm());
	                } else if (op.isIn()){
		                currentReactionTerm=new Struct("in",op.getLogicTupleArgument().toTerm());
					} else if (op.isRd()){
						currentReactionTerm=new Struct("rd",op.getLogicTupleArgument().toTerm());
	                } else if (op.isInp()){
	                    currentReactionTerm=new Struct("inp",op.getLogicTupleArgument().toTerm());
	                } else if (op.isRdp()){
	                    currentReactionTerm=new Struct("rdp",op.getLogicTupleArgument().toTerm());
	                } else if (op.isOut_s()){
	                	currentReactionTerm = new Struct("out_s", op.getLogicTupleArgument().toTerm());
	                } else if (op.isRd_s()){
	                	currentReactionTerm = new Struct("rd_s", op.getLogicTupleArgument().toTerm());
	                } else if (op.isIn_s()){
	                	currentReactionTerm = new Struct("in_s", op.getLogicTupleArgument().toTerm());
	                } else if (op.isRdp_s()){
	                	currentReactionTerm = new Struct("rdp_s", op.getLogicTupleArgument().toTerm());
	                } else if (op.isInp_s()){
	                	currentReactionTerm = new Struct("inp_s", op.getLogicTupleArgument().toTerm());
		            } else if (op.isGetEnv()){
	                	currentReactionTerm = new Struct("getEnv", op.getLogicTupleArgument().getArg(0).toTerm(),op.getLogicTupleArgument().getArg(1).toTerm());
	                }else if (op.isSetEnv()){
	                	currentReactionTerm = new Struct("setEnv", op.getLogicTupleArgument().getArg(0).toTerm(),op.getLogicTupleArgument().getArg(1).toTerm());
	                }else if (op.isTime()){
	                	currentReactionTerm = new Struct("time", op.getLogicTupleArgument().toTerm());
	                }
	            }else if (ev.isOutput()){
	                alice.tuplecentre.core.OutputEvent oe = (alice.tuplecentre.core.OutputEvent)ev;
					RespectOperation op=(RespectOperation)ev.getOperation();
	                spy("IS LINKING"+((OutputEvent)ev).isLinking());
				
					if(((OutputEvent)ev).isLinking()){
						if (op.isOut()){
		                	//System.out.println(""+op.getLogicTupleArgument().toTerm());
		                    currentReactionTerm=new Struct("out",op.getLogicTupleArgument().toTerm());
		                } else if (op.isIn()){
			                currentReactionTerm=new Struct("in",op.getLogicTupleArgument().toTerm());
						} else if (op.isRd()){
							currentReactionTerm=new Struct("rd",op.getLogicTupleArgument().toTerm());
		                } else if (op.isInp()){
		                    currentReactionTerm=new Struct("inp",op.getLogicTupleArgument().toTerm());
		                } else if (op.isRdp()){
		                    currentReactionTerm=new Struct("rdp",op.getLogicTupleArgument().toTerm());
		                } else if (op.isOut_s()){
		                	currentReactionTerm = new Struct("out_s", op.getLogicTupleArgument().toTerm());
		                } else if (op.isIn_s()){
			                currentReactionTerm=new Struct("in_s",op.getLogicTupleArgument().toTerm());
						} else if (op.isRd_s()){
							currentReactionTerm=new Struct("rd_s",op.getLogicTupleArgument().toTerm());
		                } else if (op.isInp_s()){
		                    currentReactionTerm=new Struct("inp_s",op.getLogicTupleArgument().toTerm());
		                } else if (op.isRdp_s()){
		                    currentReactionTerm=new Struct("rdp_s",op.getLogicTupleArgument().toTerm());
		                } 
					}else{
		                if (op.isIn()){
		                    currentReactionTerm=new Struct("in",op.getLogicTupleResult().toTerm());
		                } else if (op.isRd()){
							currentReactionTerm=new Struct("rd",op.getLogicTupleResult().toTerm());
						} else if (op.isInp()){
							LogicTuple result = op.getLogicTupleResult();
							if (result!=null){
								currentReactionTerm=new Struct("inp",result.toTerm());
							} else {
								currentReactionTerm=new Struct("inp",op.getLogicTupleArgument().toTerm());
							}
		                } else if (op.isRdp()){
							LogicTuple result = op.getLogicTupleResult();
							if (result!=null){
								currentReactionTerm=new Struct("rdp",result.toTerm());
							} else {
								currentReactionTerm=new Struct("rdp",op.getLogicTupleArgument().toTerm());
							}
		                } else  if (op.isOut()){
		                    currentReactionTerm=new Struct("out",op.getLogicTupleResult().toTerm());
		                } else if (op.isIn_s()){
		                    currentReactionTerm=new Struct("in_s",op.getLogicTupleResult().toTerm());
		                } else if (op.isRd_s()){
							currentReactionTerm=new Struct("rd_s",op.getLogicTupleResult().toTerm());
						} else if (op.isInp_s()){
							LogicTuple result = op.getLogicTupleResult();
							if (result!=null){
								currentReactionTerm=new Struct("inp_s",result.toTerm());
							} else {
								currentReactionTerm=new Struct("inp_s",op.getLogicTupleArgument().toTerm());
							}
		                } else if (op.isRdp_s()){
							LogicTuple result = op.getLogicTupleResult();
							if (result!=null){
								currentReactionTerm=new Struct("rdp_s",result.toTerm());
							} else {
								currentReactionTerm=new Struct("rdp_s",op.getLogicTupleArgument().toTerm());
							}
		                } else if (op.isOut_s()){
		                	currentReactionTerm=new Struct("out_s",op.getLogicTupleResult().toTerm());
		                }
					}
	            } else if (ev.isInternal()){
	                InternalEvent ev1=(InternalEvent)ev;
	                InternalOperation rop=ev1.getInternalOperation();
	                if (rop.isInR()){
	                    currentReactionTerm=new Struct("in",rop.getArgument().toTerm());
	                } else if (rop.isOutR()){
	                    currentReactionTerm=new Struct("out",rop.getArgument().toTerm());
	                } else if (rop.isRdR()){
	                    currentReactionTerm=new Struct("rd",rop.getArgument().toTerm());
	                } else if (rop.isNoR()){
	                    currentReactionTerm=new Struct("no",rop.getArgument().toTerm());
	                } else if (rop.isIn_sR()){
	                	currentReactionTerm = new Struct("in_s", rop.getArgument().toTerm());
	                } else if (rop.isOut_sR()){
	                	currentReactionTerm = new Struct("out_s", rop.getArgument().toTerm());
	                } else if (rop.isRd_sR()){
	                	currentReactionTerm = new Struct("rd_s", rop.getArgument().toTerm());
	                }else if (rop.isGetEnv()){
	                	currentReactionTerm = new Struct("getEnv", rop.getArgument().getArg(0).toTerm(),rop.getArgument().getArg(1).toTerm());
	                }else if (rop.isSetEnv()){
	                	currentReactionTerm = new Struct("setEnv", rop.getArgument().getArg(0).toTerm(),rop.getArgument().getArg(1).toTerm());
	                }
	            }
	            
	            if (currentReactionTerm!=null){
	            	/*Before*/
//	            	currentReactionTerm = (Struct)currentReactionTerm.getRenamedCopy();
	            	/*insert by Enrico Rossignoli*/
	            	AbstractMap<Var,Var> v = new LinkedHashMap<Var,Var>();
	            	currentReactionTerm = (Struct)currentReactionTerm.copyGoal(v, 0);
	            	/*end insert*/
	    
	            	Struct tev=new Struct("reaction",currentReactionTerm,new alice.tuprolog.Var("G"),new alice.tuprolog.Var("R"));
	                //String tev="reaction("+currentReactionTerm+",R).";
	                SolveInfo info = trigCore.solve(tev);
	                
	                alice.tuprolog.Term guard=null;
	                
	                while (info.isSuccess()){
	                	//System.out.println("Sol: "+info.getSolution());
	                	guard = info.getVarValue("G");
	                	spy("Guard to evaluate: "+guard);
	                	
	                	//we have to set the current reaction event because the respect library (loaded into tuProlog Engine)
	                	//uses this event to resolve the guard predicates
	                	this.currentReactionEvent = ev;
	                	
	                	if( this.evalGuard(guard)){
	                		
	                		Struct trigReaction = new Struct("reaction",currentReactionTerm,info.getVarValue("R"));
	                		TriggeredReaction tr=new TriggeredReaction(ev,new LogicReaction(trigReaction));
	                		zSet.add(tr);
	                		spy("New reaction triggered: \n"+ev+" -> "+tr.getReaction());
	                	}
	                	
	                    if (trigCore.hasOpenAlternatives()){
	                        info=trigCore.solveNext();
	                    } else {
	                        break;
	                    }
	                    trigCore.solveEnd();
	                }
	            }
	        } catch (Exception ex){
	            notifyException("INTERNAL ERROR: fetchTriggeredReactions "+ev);
				ex.printStackTrace();
	            trigCore.solveEnd();
	        }
        
    	}//end of synchronization
    }


    public void evalReaction(TriggeredReaction z){
    	transaction = true;
        tSet.beginTransaction();
        tSpecSet.beginTransaction();
        wSet.beginTransaction();
        zSet.beginTransaction();
        timeSet.beginTransaction();

		temporaryOutputEventList.clear();

        Term goalList=((LogicReaction)z.getReaction()).getStructReaction().getTerm(1);
        //System.out.println("Triggered Evaluation: "+goalList.toString());
        currentReactionEvent=z.getEvent();

        //System.out.println("CURRENT EVENT: "+currentEvent);
        spy("EVAL REACTION: "+goalList);
        SolveInfo info=core.solve(goalList);
        core.solveEnd();
        if (info.isSuccess()){
            // add to the total set of activities to spawn
            //activitiesToSpawn.addAll(activitiesToSpawn_singleReaction);
            spy("Reaction evaluation succeeded: "+goalList);

            System.out.println("EVAL REACTION OK");
            if (vm.hasInspectors())
            	vm.notifyInspectableEvent(new ObservableEventReactionOK(this,z));            
			
			// notifies possible output events
			int n = temporaryOutputEventList.size();
			for (int i=0; i<n; i++){
				InputEvent curr = (alice.tuplecentre.core.InputEvent)temporaryOutputEventList.get(i);
				//this.notifyOutputEvent((alice.tuplecentre.core.OutputEvent)outputEventList.get(i));            
				this.addPendingQueryEvent(curr);
				//this.fetchTriggeredReactions(curr);
			}
			
            
        } else {
            
            spy("Reaction evaluation failed: "+goalList);
            
            System.out.println("EVAL REACTION FAIL");
            if (vm.hasInspectors())
            	vm.notifyInspectableEvent(new ObservableEventReactionFail(this,z));            
        }
        boolean success=info.isSuccess();
        boolean specModified = tSpecSet.operationsPending();
        temporaryOutputEventList.clear();
        zSet.endTransaction(success);
        wSet.endTransaction(success);
        tSet.endTransaction(success);
        tSpecSet.endTransaction(success);
        timeSet.endTransaction(success);
        transaction = false;
        if(specModified){
        	setReactionSpecHelper(new RespectSpecification(
        			tSpecSet.toString()));
        }
    }
    
    public void addTemporaryOutputEvent(InputEvent out){
    	
    	synchronized(this.temporaryOutputEventList){
    		
    		this.temporaryOutputEventList.add(out);
    		spy("Added a temporary outputEvent, operation: "+out.getOperation());
    		
    		
    	}
    	
    }
    
    private boolean evalGuard(Term g){
     
        //System.out.println("CURRENT EVENT: "+currentEvent);
        spy("EVAL GUARD: "+g);
        SolveInfo info=core.solve(g);
        core.solveEnd();
        spy("GUARD EVALUATION: "+info.isSuccess());
        return info.isSuccess();
    }
    
    public void removeReactionSpec(){
    	this.core.clearTheory();
    	this.trigCore.clearTheory();
    	this.tSpecSet.empty();
    }
   
    protected boolean setReactionSpecHelper(BehaviourSpecification spec){
    	if (transaction){
    		return false;
    	}
        try {
        	this.timers.clear();
            //System.out.println("setting spec: \n"+spec);
            Struct co=new Struct(spec.toString());
            if (co.isAtom()){
                alice.tuprolog.Theory thspec=new alice.tuprolog.Theory(co.getName());
                core.setTheory(thspec);
                trigCore.setTheory(thspec);
            } else if (co.isList()){
                alice.tuprolog.Theory thspec=new alice.tuprolog.Theory(co);
                core.setTheory(thspec);
                trigCore.setTheory(thspec);
            } else {
                notifyException("Invalid reaction spec:\n"+co);
                return false;
            }

            
            if((noReactionTh != null) && !this.isExternalSetSpec){
            	core.addTheory(noReactionTh);
            	trigCore.addTheory(noReactionTh);
            	spy("Added non-reaction theory");
            }
            spy("New behaviour set.");
            spy("TH1: "+core.getTheory());
            spy("TH2: "+trigCore.getTheory());
            
            reactionSpec=(RespectSpecification)spec;
            
            Iterator<Term> it;
            it = this.foundTimeReactions();
            while(it.hasNext()){
            	
            	Term current = it.next();
            	
            	spy("time "+current);
            	
            	Timer currTimer = new Timer();
            	long timeValue = ((alice.tuprolog.Number)current).longValue();
            	long currLocalTime = this.getCurrentTime();
            	spy("Current ReSpecT VM local time: "+currLocalTime);
            	
            	long delay;
            	
            	if(timeValue > currLocalTime) delay = timeValue - currLocalTime;
            	else delay = 0;
            	
            	currTimer.schedule(new RespectTimerTask(this,RespectOperation.makeTime(getPrologCore(), new LogicTuple("time",new TupleArgument(current)), null)),delay);
            	//this.timers.add(currTimer);
            	spy("scheduled "+RespectOperation.makeTime(getPrologCore(), new LogicTuple("time",new TupleArgument(current)),null));
            	
            	
            }
            
            
            return true;
        } catch (alice.tuprolog.InvalidTheoryException ex){
        	ex.printStackTrace();
            notifyException("Invalid reaction spec. "+ex.line+" "+ex.pos);
            notifyException(spec.toString());
            //ex.printStackTrace();
            return false;
        } catch (Exception ex){
            notifyException("Invalid reaction spec.");
            return false;
        }          
    }
    
    protected boolean addReactionSpecHelper(BehaviourSpecification spec){
    	if (transaction){
    		return false;
    	}
        try {
        	this.timers.clear();
            //System.out.println("setting spec: \n"+spec);
            Struct co=new Struct(spec.toString());
            if (co.isAtom()){
                alice.tuprolog.Theory thspec=new alice.tuprolog.Theory(co.getName());
                core.addTheory(thspec);
                trigCore.addTheory(thspec);
            } else if (co.isList()){
                alice.tuprolog.Theory thspec=new alice.tuprolog.Theory(co);
                core.addTheory(thspec);
                trigCore.addTheory(thspec);
            } else {
                notifyException("Invalid reaction spec:\n"+co);
                return false;
            }

            spy("New behaviour set.");
            
            reactionSpec=(RespectSpecification)spec;
            
            Iterator<Term> it;
            it = this.foundTimeReactions();
            while(it.hasNext()){
            	
            	Term current = it.next();
            	
            	spy("time "+current);
            	
            	Timer currTimer = new Timer();
            	long timeValue = ((alice.tuprolog.Number)current).longValue();
            	long currLocalTime = this.getCurrentTime();
            	spy("Current ReSpecT VM local time: "+currLocalTime);
            	
            	long delay;
            	
            	if(timeValue > currLocalTime) delay = timeValue - currLocalTime;
            	else delay = 0;
            	
            	currTimer.schedule(new RespectTimerTask(this,RespectOperation.makeTime(getPrologCore(), new LogicTuple("time",new TupleArgument(current)), null)),delay);
            	//this.timers.add(currTimer);
            	spy("scheduled "+RespectOperation.makeTime(getPrologCore(), new LogicTuple("time",new TupleArgument(current)),null));
            	
            	
            }
            
            
            return true;
        } catch (alice.tuprolog.InvalidTheoryException ex){
            notifyException("Invalid reaction spec. "+ex.line+" "+ex.pos);
            notifyException(spec.toString());
            //ex.printStackTrace();
            return false;
        } catch (Exception ex){
            notifyException("Invalid reaction spec.");
            return false;
        }          
    }
    
    public boolean setReactionSpec(BehaviourSpecification spec){
    	
    	this.isExternalSetSpec = true;
    	noReactionTh = null;
    	Prolog engine = new Prolog();
    	try{
    		Theory th = new Theory(spec.toString());
    		engine.setTheory(th);
    		engine.solve("retractall(reaction(X,Y,Z)).");
    		engine.solveEnd();
    		spy("INITIAL SET: "+engine.getTheory());
    		noReactionTh = engine.getTheory();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    		
    	boolean result = setReactionSpecHelper(spec);
    	if (result){
    		tSpecSet.empty();
    		try {
    			alice.tuprolog.SolveInfo info = core.solve("reaction(X,Y,Z).");
    				while (true){
    					alice.tuprolog.Term solution = info.getSolution();
    					tSpecSet.add(new LogicTuple(solution));
    					info = core.solveNext();
    				}
    		} catch (alice.tuprolog.NoMoreSolutionException ex) {
    			spy("tSpecSet update on Beahaviour Specification change completed.");
    		} catch (alice.tuprolog.NoSolutionException ex){
    			spy("tSpecSet update on Beahaviour Specification change completed.");
    		} catch(MalformedGoalException ex) {
    			ex.printStackTrace();
    		}

    	}
    	this.isExternalSetSpec = false;
		return result;
    }
 

    public BehaviourSpecification getReactionSpec(){
        return reactionSpec;
    }

    /**
     *  Static services that checks if a source text
     *  contains a valid ReSpecT specification
     *
     *  @return a logic tuple that provides information about
     *          the check: valid is the specification is OK, or
     *          invalid(L) if there are errors (at line L).
     *
     */
    static public LogicTuple checkReactionSpec(String spec){
        Prolog core=new Prolog();
        try {
            Struct co=new Struct(spec);
            if (co.isAtom()){
                alice.tuprolog.Theory thspec=new alice.tuprolog.Theory(co.getName());
                core.setTheory(thspec);
            } else if (co.isList()){
                alice.tuprolog.Theory thspec=new alice.tuprolog.Theory(co);
                core.setTheory(thspec);
            } else {
                core=null;
                return new LogicTuple("invalid",new alice.logictuple.Var());
            }
            core=null;
            return new LogicTuple("valid");
        } catch (alice.tuprolog.InvalidTheoryException ex){
            core=null;
            return new LogicTuple("invalid",new Value(ex.line));
        }
    }

    // ----------------------------
    
    public Struct getCurrentReactionTerm(){
        return currentReactionTerm;
    }
    
    public Event getCurrentReactionEvent(){
        return currentReactionEvent;
    }

    // ----------------------------


    /**
     * resets the virtual machine to boot state
     */
    public  void reset(){
        tSet.empty();
        wSet.empty();
        zSet.empty();
        timeSet.empty();
        setBootTime();
    }


    // ----------------------------------------------------------------

    public void        addTuple(Tuple t){
        tSet.add((alice.logictuple.LogicTuple)t);
    }

    public  Tuple       removeMatchingTuple(TupleTemplate t){
        Tuple tuple=tSet.getMatchingTuple((alice.logictuple.LogicTuple)t);
        return tuple;

    }

    public Tuple       readMatchingTuple(TupleTemplate t){
        return  tSet.readMatchingTuple((alice.logictuple.LogicTuple)t);
    }
    
    public void addSpecTuple(Tuple t){
    	tSpecSet.add((LogicTuple)t);
    		this.setReactionSpecHelper(
   				new alice.respect.api.RespectSpecification(
    					tSpecSet.toString())
				);
    }
    
    public Tuple readMatchingSpecTuple(TupleTemplate t){
    	return tSpecSet.readMatchingTuple((alice.logictuple.LogicTuple)t);
    }

    public Tuple removeMatchingSpecTuple(TupleTemplate t){
    	Tuple tuple = tSpecSet.getMatchingTuple((alice.logictuple.LogicTuple)t);
    	if(tuple != null)
    		this.setReactionSpecHelper(
    				new alice.respect.api.RespectSpecification(
    						tSpecSet.toString())
					);
    	return tuple;
    }
    
    public Iterator    getTupleSetIterator(){
        return tSet.getIterator();
    }
    
    public Iterator getSpecTupleSetIterator(){
    	return tSpecSet.getIterator();
    }

    public void emptyTupleSet(){
        tSet.empty();
    }
    // ---------------------------------------------------------------

    public void        addPendingQueryEvent(InputEvent w){
        wSet.add(w);
    }

    public Iterator    getPendingQuerySetIterator(){
        return wSet.getIterator();
    }

	/**
	 * Removes all events of specified agent
	 *
	 * @param id the identifier of the agent whose events must be removed
	 */
	public void removePendingQueryEventsOf(alice.tuplecentre.api.AgentId id){
		wSet.removeEventsOf(id);
	}
    
	/**
	 * Removes the event related to a specific executed operation  
	 *
	 * @param id identifier of the operation
	 */
	public boolean removePendingQueryEvent(long operationId){
		return wSet.removeEventOfOperation(operationId);
	}
    
    // ----------------------------------------------------------------

    public TriggeredReaction removeTriggeredReaction(){
        if (zSet.isEmpty()){
            return null;
        } else {
            return zSet.get();
        }
    }

    public Iterator            getTriggeredReactionSetIterator(){
        return zSet.getIterator();
    }
    public void notifyNewInputEvent() {
        this.vm.notifyNewInputEvent();
    }
    
    public void notifyInputEnvEvent(InputEvent in){
    	addEnvInputEvent(in);
    	spy("Accepting new event " + in);
    	vm.notifyNewInputEvent();
    }
    
    public void notifyInputEvent(InputEvent in){ 	
    	this.addInputEvent(in); 	
    	//because the vm could be waiting for an input event if there are no input event in the queue
    	this.notifyNewInputEvent();
    }
    
   //---------------------------------------------------------------
		
  /*  public void  addOutputEvent(alice.tuplecentre.core.OutputEvent ev){
        outputEventList.add(ev);
    }*/

	// 
	
    // Aggiunta la gestione del filtro t
	public LogicTuple[] getTSet(LogicTuple filter){
		LogicTuple[] ltSet = tSet.toArray();
		
		ArrayList<LogicTuple> supportList = new ArrayList<LogicTuple>();
		
		if (filter == null)
			return ltSet;
		
		for(LogicTuple tuple : ltSet)
			if (filter.match(tuple))
				supportList.add(tuple);		
		
		return supportList.toArray(new LogicTuple[0]);
	}
	
	public LogicTuple[] getWSet(LogicTuple filter){
		LogicTuple[] tuples = new LogicTuple[this.wSet.size()];
		
		ArrayList<LogicTuple> supportList = new ArrayList<LogicTuple>();				
		
		Event[] ev = wSet.toArray();
		//System.out.println("context.getWSet(filter):");
		for(int i=0;i<tuples.length;i++){			
			tuples[i] = ((RespectOperation)ev[i].getOperation()).toTuple();
			//System.out.println(tuples[i]);
		}
		if (filter == null)
			return tuples;
		
		for(LogicTuple tuple : tuples)
			if (filter.match(tuple))
				supportList.add(tuple);		
		
		return supportList.toArray(new LogicTuple[0]);		
	}
	
	public void setWSet(List<LogicTuple> wSet){
		
		this.wSet.empty();
		
		for(LogicTuple t : wSet)
		{
			//System.out.println(t);
			String operation = t.toString();
			String opKind = operation.substring(0,2);
			//System.out.println(opKind);						
			
			if(opKind.equals("rd"))				
			{		
				String tupla = operation.substring(3,operation.length()-1);
				//System.out.println(tupla);
				LogicTuple logicTuple = null;
				try
				{
					logicTuple = LogicTuple.parse(tupla);
				}
				catch (InvalidLogicTupleException e1)
				{
					e1.printStackTrace();
				}
				
				RespectOperation op = RespectOperation.makeRd(getPrologCore(), logicTuple, null);
				try
				{
					vm.doOperation(null,op);
				}
				catch (OperationNotPossibleException e)
				{
					e.printStackTrace();
				}
			}
			else if(opKind.equals("in"))				
			{
				String tupla = operation.substring(3,operation.length()-1);
				//System.out.println(tupla);
				LogicTuple logicTuple = null;
				try
				{
					logicTuple = LogicTuple.parse(tupla);
				}
				catch (InvalidLogicTupleException e1)
				{
					e1.printStackTrace();
				}
				
				RespectOperation op = RespectOperation.makeIn(getPrologCore(), logicTuple,null);
				try
				{
					vm.doOperation(null,op);
				}
				catch (OperationNotPossibleException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	public LogicTuple[] getTRSet(LogicTuple t){
		TriggeredReaction[] trig = this.zSet.toArray();
		LogicTuple[] tuples = new LogicTuple[trig.length];
		for(int i=0;i<tuples.length;i++){
			Term term = ((LogicReaction)trig[i].getReaction()).getStructReaction().getTerm();
			tuples[i] = new LogicTuple(term);
		}
		return tuples;
	}

	
	public Iterator<Term> foundTimeReactions() {
		
		Vector<Term> foundReactions = new Vector<Term>();
		try {
	            
					
	         		//boolean timedReaction = false;
	            	
	            	Struct timed = new Struct("time",new alice.tuprolog.Var("Time"));
	    
	            	Struct tev=new Struct("reaction",timed,new alice.tuprolog.Var("G"),new alice.tuprolog.Var("R"));
	                //String tev="reaction("+currentReactionTerm+",R).";
	                
	            	//long currentTime = this.getCurrentTime();
	            	//spy("CURRENT TIME: \n"+ev+" -> "+currentTime);
	            	SolveInfo info = trigCore.solve(tev);
	                
	                alice.tuprolog.Term guard=null;
	                
	                while (info.isSuccess()){
	                	//System.out.println("Sol: "+info.getSolution());
	                	
	                	
	                	//we have to set the current reaction event because the respect library (loaded into tuProlog Engine)
	                	//uses this event to resolve the guard predicates
	                //	this.currentReactionEvent = ev;
	                	
	                	
	                		
	                	//long reactionTime = ((alice.tuprolog.Int)info.getVarValue("Time")).longValue();
	                	foundReactions.add(info.getVarValue("Time"));
	                		
	                		
	                	
	                	
	                    if (trigCore.hasOpenAlternatives()){
	                        info=trigCore.solveNext();
	                    } else {
	                        break;
	                    }
	                    trigCore.solveEnd();
	                    
	                    
	                    
	                    //spy("THEORY: "+trigCore.getTheory());
	                }
	                
	                	            
	        }catch (Exception ex){
	            notifyException("INTERNAL ERROR: fetchTimedReactions ");
				ex.printStackTrace();
	            trigCore.solveEnd();
	        }/**finally{
	        	
	        	trigCore.solveEnd();
	        	//trigCore.setTheory(core.getTheory());
	        	
	        } */
	        
	        return foundReactions.iterator();
			
		}
	
	
	public void fetchTimedReactions(Event ev) {
	try {
            
         		boolean timedReaction = false;
            	
            	Term timed = (((RespectOperation)ev.getOperation()).getLogicTupleArgument().toTerm());
            	spy("TIMED? "+timed);
            	Struct tev=new Struct("reaction",timed,new alice.tuprolog.Var("G"),new alice.tuprolog.Var("R"));
                //String tev="reaction("+currentReactionTerm+",R).";
                
            	//long currentTime = this.getCurrentTime();
            	//spy("CURRENT TIME: \n"+ev+" -> "+currentTime);
            	SolveInfo info = trigCore.solve(tev);
                
                alice.tuprolog.Term guard=null;
                
                while (info.isSuccess()){
                	//System.out.println("Sol: "+info.getSolution());
                	guard = info.getVarValue("G");
                	spy("Guard to evaluate: "+guard);
                	
                	//we have to set the current reaction event because the respect library (loaded into tuProlog Engine)
                	//uses this event to resolve the guard predicates
                	this.currentReactionEvent = ev;
                	
                	if( this.evalGuard(guard)){
                		
                    	
                		//long reactionTime = ((alice.tuprolog.Int)info.getVarValue("Time")).longValue();
                		
                		//if(reactionTime<=currentTime){
                		Term reactions = info.getVarValue("R");
                		Struct trigReaction = new Struct("reaction",timed,reactions);
                		TriggeredReaction tr=new TriggeredReaction(ev,new LogicReaction(trigReaction));
                		this.timeSet.add(tr);
                		timedReaction = true;
                		//SolveInfo removeInfo = this.trigCore.solve("retract(reaction( time("+reactionTime+"), (G),("+reactions+"))) .");
                		//spy("Remove time reaction - 1: \n"+ev+" -> "+removeInfo.isSuccess());
                		
                		spy("TO BE REMOVED: "+timed+" local time is: "+this.getCurrentTime());
                		SolveInfo removeInfo = this.core.solve("retract(reaction( "+timed+", (G),("+reactions+"))) .");
                		spy("CORE: Remove time reaction - 2: \n"+ev+" -> "+removeInfo.isSuccess());
                		this.core.solveEnd();
                		
                		spy("New time-triggered reaction: \n"+ev+" -> "+tr.getReaction());
                		//}
                	}
                	
                    if (trigCore.hasOpenAlternatives()){
                        info=trigCore.solveNext();
                    } else {
                        break;
                    }
                    trigCore.solveEnd();
                    SolveInfo removeInfo = this.trigCore.solve("retract(reaction( "+timed+", (G),("+info.getVarValue("R")+"))) .");
            		spy("TRIGCORE: Remove time reaction - 2: \n"+ev+" -> "+removeInfo.isSuccess());
            		trigCore.solveEnd();
                    
                    
                    //spy("THEORY: "+trigCore.getTheory());
                }
                
                
                //if(timedReaction) trigCore.setTheory(core.getTheory());
            
        }catch (Exception ex){
            notifyException("INTERNAL ERROR: fetchTimedReactions "+ev);
			//ex.printStackTrace();
            trigCore.solveEnd();
        }/**finally{
        	
        	trigCore.solveEnd();
        	//trigCore.setTheory(core.getTheory());
        	
        } */
		
	}

	public boolean time_triggeredReaction() {
		return !timeSet.isEmpty();
	}

	
	public TriggeredReaction removeTimeTriggeredReaction() {
		 if (timeSet.isEmpty()){
	            return null;
	        } else {
	        	spy("GOT a TIME REACTION");
	            return timeSet.get();
	        }
	}
	
	@Override
	public void updateSpecAfterTimedReaction(TriggeredReaction tr) {
		LogicReaction lr = (LogicReaction)tr.getReaction();
		Struct rStruct = lr.getStructReaction();
		Struct rg = new Struct(rStruct.getName(),
				rStruct.getArg(0),
				new Var(),
				rStruct.getArg(1)
				);
		this.removeMatchingSpecTuple(new LogicTuple(rg));
	}

	public void linkOperation(OutputEvent arg0) {
		TupleCentreId target = (TupleCentreId)arg0.getTarget();
		try{
			
			TupleCentreOperation op = arg0.getOperation();
			op.addListener(new CompletionListener(arg0,target));
			spy("target tc "+target+" arg0.getSource(): "+arg0.getSource());
			ILinkContext link = RespectTCContainer.getRespectTCContainer().getLinkContext(target);
			
			link.doOperation((TupleCentreId)arg0.getSource(), op);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	
	class CompletionListener implements OperationCompletionListener{

    	private OutputEvent inter;
    	private TupleCentreId currentTC;
    	
    	public CompletionListener(OutputEvent inter,TupleCentreId source){
    		this.inter = inter;
    		this.currentTC = source;
    		/*inter.setSource(inter.getTarget());*/
    	//	this.in = in;
    	}
    	
		public void operationCompleted(TupleCentreOperation arg0) {
			
			//RespectOperation rArg0 = (RespectOperation)arg0;
			//InternalOperation intOp;
			arg0.removeListener();
			
			InputEvent res = new InputEvent(inter.getTarget(),arg0,currentTC,getCurrentTime());
			
			
			notifyInputEvent(res);
			/*if(rArg0.isIn() || rArg0.isInp()){
				intOp = InternalOperation.makeInR(rArg0.getLogicTupleResult());
			}else if(rArg0.isRd() || rArg0.isRdp() ){
				intOp = InternalOperation.makeRdR(rArg0.getLogicTupleResult());
			}else{
				intOp = InternalOperation.makeOutR(rArg0.getLogicTupleResult());
			}
			
			InternalEvent ev=new InternalEvent(in,intOp); 
            ev.setSource(in.getSource());
            ev.setTarget(in.getTarget());
            vm.fetchTriggeredReactions(ev);*/
		}
    	
    	
    }


//	public TransducerManager getTransducerManager() {
//		return tm;
//	}

	public List<Tuple> getAllTuples() {
		List<Tuple> tl = new LinkedList<Tuple>();
		Iterator<LogicTuple> it = this.tSet.getIterator();
		while(it.hasNext()){
			tl.add((Tuple)it.next());
		}
		return tl;
	}
	
	// Metodo necessario per realizzare la set 
	public void setAllTuples(List<Tuple> tupleList) {		
		
		this.tSet.empty();

		for(Tuple t : tupleList)
			this.addTuple(t);
	}
	
	public RespectVM getRespectVM()
	{
		return vm;
	}
	
	
	/*insert by Enrico Rossignoli*/
	// this is a new prolog engine only for match tuples
	public Prolog getPrologCore()
	{
		return matcher ;
	}

}


