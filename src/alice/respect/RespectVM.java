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
import java.util.*;
import alice.tuplecentre.*;

import alice.respect.TupleCentreId;

import alice.tuprolog.*;
import alice.logictuple.*;
import java.io.*;

/**
 * This class defines VM core of a ReSpecT Tuple Centre 
 *
 * @see alice.tuplecentre.TupleCentreVM
 *
 *
 * @version 1.0
 */
public class RespectVM extends alice.tuplecentre.TupleCentreVM {


    /** multiset of tuples T */
	//protected TupleSet        tSet;
	protected PersistentTupleSet        tSet;

    /** multiset of pending query set  */
    protected EventSet        wSet;

    /** multiset of triggered reactions Z */
    protected TRSet           zSet;

    /** multiset of not satisfiable pending queries Wn - W/(Wn+Wp) */
    protected EventSet        outputEventSet;

    protected String        reactionSpec;

    // state (changed or not) of the tuple set after a VM cycle
    protected boolean tSetChanged;

    // engine for reactions evaluation
    Prolog core;
    // engine for reactions triggering
    Prolog trigCore;
    // current event serverd
    private Event currentEvent;

	// persistency service
	private FileWriter persistentStore;
	private String persistentStoreName;
	private BufferedWriter persistentStoreBuffer;
	private boolean isPersistent;
	
	//	-------------- timing extension
	
	private Timer trapsTimer;
	//private LinkedList trapEventQueue;
	private TrapGenSet trapGenSet;
	private long trapGenCounter;
	protected TRSet ztrapSet;
	protected boolean isReactingToTraps;
	
	//-----------------------
    
    private OutputEventListener outEventListener;
	//-----------------------
    
    
    public RespectVM(TupleCentreId tid){
        super(tid);
		
        tSet=new PersistentTupleSet();
        wSet=new EventSet();
        zSet=new TRSet();
        outputEventSet=new EventSet();
        core=new Prolog();
        alice.tuprolog.OutputListener l=new alice.tuprolog.OutputListener() {
            public void onOutput(alice.tuprolog.OutputEvent ev) {
                System.out.print(ev.getMsg());
        }};
        core.addOutputListener(l);
        try {
            ((alice.respect.Library)core.loadLibrary("alice.respect.Library")).init(this);
        } catch (Exception ex){
            ex.printStackTrace();
        }
        trigCore=new Prolog();
        try {
            trigCore.loadLibrary("alice.respect.Library");
            ((alice.respect.Library)trigCore.getLibrary("alice.respect.Library")).init(this);
        } catch (Exception ex){
            ex.printStackTrace();
        }

        reactionSpec="";
        
        //
    	isReactingToTraps = false;    
        trapsTimer = new Timer();
        trapGenSet = new TrapGenSet();
		ztrapSet=new TRSet();
        trapGenCounter = 0;
		outEventListener=null;
    }

	public RespectVM(TupleCentreId tid,OutputEventListener l){
		this(tid);
		outEventListener=l;
	}

    protected void   notifyUnknownOutputEvent(alice.tuplecentre.OutputEvent ev){
        if (outEventListener!=null){
        	outEventListener.notify(ev);
        }
    }


    public void fetchTriggeredReactions(Event ev){
    
    	boolean trapEvent = (this.currentEvent instanceof TrapEvent);
        try {
            Term reactionTerm=null;
            if (ev instanceof InputEvent){
                if (ev instanceof InputEvent_out){
                    reactionTerm=new Struct("out",((LogicTuple)((InputEvent_out)ev).getTuple()).toTerm());
                } else if (ev instanceof InputEvent_in){
                    reactionTerm=new Struct("in",((LogicTuple)((InputEvent_in)ev).getTupleTemplate()).toTerm());
                } else if (ev instanceof InputEvent_inp){
                    reactionTerm=new Struct("inp",((LogicTuple)((InputEvent_inp)ev).getTupleTemplate()).toTerm());
                } else if (ev instanceof InputEvent_rd){
                    reactionTerm=new Struct("rd",((LogicTuple)((InputEvent_rd)ev).getTupleTemplate()).toTerm());
                } else if (ev instanceof InputEvent_rdp){
                    reactionTerm=new Struct("rdp",((LogicTuple)((InputEvent_rdp)ev).getTupleTemplate()).toTerm());
                }
            } else if (ev instanceof alice.tuplecentre.OutputEvent){
                if (ev instanceof OutputEvent_in){
                    reactionTerm=new Struct("in",((LogicTuple)((OutputEvent_in)ev).getTuple()).toTerm());
                } else if (ev instanceof OutputEvent_inp){
                    OutputEvent_inp ev_inp=(OutputEvent_inp)ev;
                    //
                    // According to the semantics of TupleCentres: if
                    // the inp was not success, the triggered reaction must
                    // take the template;  here, the fact that the tuple
                    // is not null indicates if the inp was succesful or not
                    //
                    if (ev_inp.getTuple()!=null){
                        reactionTerm=new Struct("inp",((LogicTuple)ev_inp.getTuple()).toTerm());
                    } else {
                        reactionTerm=new Struct("inp",((LogicTuple)ev_inp.getTupleTemplate()).toTerm());

                    }
                } else if (ev instanceof OutputEvent_rd){
                    reactionTerm=new Struct("rd",((LogicTuple)((OutputEvent_rd)ev).getTuple()).toTerm());
                } else if (ev instanceof OutputEvent_rdp){
                    OutputEvent_rdp ev_rdp=(OutputEvent_rdp)ev;
                    //
                    // According to the semantics of TupleCentres: if
                    // the rdp was not success, the triggered reaction must
                    // take the template;  here, the fact that the tuple
                    // is not null indicates if the rdp was succesful or not
                    //
                    if (ev_rdp.getTuple()!=null){
                        reactionTerm=new Struct("rdp",((LogicTuple)ev_rdp.getTuple()).toTerm());
                    } else {
                        reactionTerm=new Struct("rdp",((LogicTuple)ev_rdp.getTupleTemplate()).toTerm());

                    }
                }
            } else if (ev instanceof ReactionEvent){
                if (ev instanceof Event_in_r){
                    reactionTerm=new Struct("in_r",((Event_in_r)ev).getTuple().toTerm());
                } else if (ev instanceof Event_out_r){
                        reactionTerm=new Struct("out_r",((Event_out_r)ev).getTuple().toTerm());
                } else if (ev instanceof Event_rd_r){
                        reactionTerm=new Struct("rd_r",((Event_rd_r)ev).getTuple().toTerm());
                } else if (ev instanceof Event_no_r){
                        reactionTerm=new Struct("no_r",((Event_no_r)ev).getTuple().toTerm());
                }
            } else if (ev instanceof TrapEvent){
            	reactionTerm = new Struct("trap",((TrapEvent)ev).getLogicTuple().toTerm());
            	trapEvent = true;
            }
            
            
            if (reactionTerm!=null){
				if (trapEvent){
					Struct tev=new Struct("reaction",reactionTerm,new alice.tuprolog.Var("R"));
					 //String tev="reaction("+reactionTerm+",R).";

					 SolveInfo info = trigCore.solve(tev);
					 while (info.isSuccess()){
						 TriggeredReaction tr=new TriggeredReaction(ev,new LogicReaction((Struct)info.getSolution()));
						 ztrapSet.add(tr);
						 //System.out.println("new reaction triggered: \n"+ev+" -> "+tr.reaction);
						 if (isSpy()){
							 spy("new trap reaction triggered: \n"+ev+" -> "+tr.reaction);
						 }
						 if (trigCore.hasOpenAlternatives()){
							 info=trigCore.solveNext();
						 } else {
							 break;
						 }
						 trigCore.solveEnd();
					 }
				} else {
	                Struct tev=new Struct("reaction",reactionTerm,new alice.tuprolog.Var("R"));
    	            //String tev="reaction("+reactionTerm+",R).";

	                SolveInfo info = trigCore.solve(tev);
    	            while (info.isSuccess()){
        	            TriggeredReaction tr=new TriggeredReaction(ev,new LogicReaction((Struct)info.getSolution()));
           	         	zSet.add(tr);
                    	//System.out.println("new reaction triggered: \n"+ev+" -> "+tr.reaction);
                    	if (isSpy()){
                        	spy("new reaction triggered: \n"+ev+" -> "+tr.reaction);
                    	}
                    	if (trigCore.hasOpenAlternatives()){
                        	info=trigCore.solveNext();
                    	} else {
                        	break;
                    	}
                    	trigCore.solveEnd();
    	            }
                }
            }
        } catch (Exception ex){
            spy("INTERNAL ERROR: fetchTriggeredReactions "+ev);
            ex.printStackTrace();
            trigCore.solveEnd();
        }
    }


    protected void evalReaction(TriggeredReaction z){
        tSet.beginTransaction();
        wSet.beginTransaction();
        zSet.beginTransaction();
        outputEventSet.beginTransaction();
		//
		trapGenSet.beginTransaction();
		ztrapSet.beginTransaction();
		//
        Term goalList=((LogicReaction)z.reaction).getStructReaction().getTerm(1);
        currentEvent=z.event;

        //System.out.println("CURRENT EVENT: "+currentEvent);
        //System.out.println("EVAL REACTION: "+goalList);
        SolveInfo info=core.solve(goalList);
        core.solveEnd();
        if (info.isSuccess()){
            // add to the total set of activities to spawn
            //activitiesToSpawn.addAll(activitiesToSpawn_singleReaction);
            if (isSpy()){
                spy("reaction evaluation ok: "+goalList);
            }
            //System.out.println("EVAL REACTION OK");
            if (hasInspectors()){
                notifyInspectableEvent(new ObservableEventReactionOK(this,z));
            }
        } else {
            if (isSpy()){
                spy("reaction evaluation fail: "+goalList);
            }
            //System.out.println("EVAL REACTION FAIL");
            if (hasInspectors()){
                notifyInspectableEvent(new ObservableEventReactionFail(this,z));
            }
        }
		
		ztrapSet.endTransaction(info.isSuccess());
		trapGenSet.endTransaction(info.isSuccess());
		//
        outputEventSet.endTransaction(info.isSuccess());
        zSet.endTransaction(info.isSuccess());
        wSet.endTransaction(info.isSuccess());
        tSet.endTransaction(info.isSuccess());
    }


    public boolean setReactionSpec(String spec){
        try {
            //System.out.println("setting spec: \n"+spec);
            Struct co=new Struct(spec);
            if (co.isAtom()){
                alice.tuprolog.Theory thspec=new alice.tuprolog.Theory(co.getName());
                core.setTheory(thspec);
                trigCore.setTheory(thspec);
            } else if (co.isList()){
                alice.tuprolog.Theory thspec=new alice.tuprolog.Theory(co);
                core.setTheory(thspec);
                trigCore.setTheory(thspec);
            } else {
                System.err.println("Invalid reaction spec:\n"+co);
                return false;
            }
            if (isSpy()){
                spy("new behaviour set.");
            }
            reactionSpec=spec;
            //
            // added for persistency
            //
            if (isPersistent){
				disablePersistence();
				enablePersistence(persistentStoreName);
            }	            
            //
            return true;
        } catch (alice.tuprolog.InvalidTheoryException ex){
            System.err.println("Invalid reaction spec. "+ex.line+" "+ex.pos);
            //ex.printStackTrace();
            return false;
        } catch (Exception ex){
            System.err.println("Invalid reaction spec.");
            ex.printStackTrace();
            return false;
        }
    }

    public String getReactionSpec(){
        return reactionSpec;
    }

	public Event getCurrentEvent(){
		return currentEvent;
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


    /**
     * resets the virtual machine to boot state
     */
    public synchronized void reset(){
        tSet.empty();
        wSet.empty();
        zSet.empty();
        outputEventSet.empty();
        super.reset();
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

    public Iterator    getTupleSetIterator(){
        return tSet.getIterator();
    }

    public void removeAllTuples(){
        tSet.empty();
    }

    // ext
    
    public  List       removeMatchingTuples(TupleTemplate t){
        List tuples=tSet.getMatchingTuples((alice.logictuple.LogicTuple)t);
        return tuples;

    }

    public List       readMatchingTuples(TupleTemplate t){
        return  tSet.readMatchingTuples((alice.logictuple.LogicTuple)t);
    }

    
    // ---------------------------------------------------------------

    public void        addPendingQueryEvent(PendingQueryEvent w){
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
    public void removePendingQueryEventsOf(alice.tuplecentre.AgentId aid){
        wSet.removeEventsOf(aid);
    }

    // ---------------------------------------------------------------

    public void        addOutputEvent(alice.tuplecentre.OutputEvent ev){
        outputEventSet.add(ev);
    }

    public Iterator    getOutputEventSetIterator(){
        return outputEventSet.getIterator();
    }

    public boolean isOutputEventSetEmpty(){
        return outputEventSet.isEmpty();
    }
    // ----------------------------------------------------------------

    public TriggeredReaction removeTriggeredReaction(){
    	if (!ztrapSet.isEmpty()){
    		return ztrapSet.get();
    	} else if (zSet.isEmpty()){
            return null;
        } else {
            return zSet.get();
        }
    }

    public Iterator            getTriggeredReactionSetIterator(){
        return zSet.getIterator();
    }

	// ---------------------------------------------------------------------------------
	
	public void enablePersistence(String fileName) throws Exception {
		isPersistent = true;
		persistentStoreName = fileName;
		persistentStore = new FileWriter(fileName);
		persistentStoreBuffer = new BufferedWriter(persistentStore);
		persistentStoreBuffer.write("<tcinfo Tuple Centre "+this.getId()+" persistent information - "+new Date()+"/>\n"); 
		persistentStoreBuffer.write("<spec>\n");
		persistentStoreBuffer.write(this.reactionSpec);
		persistentStoreBuffer.write("\n</spec>\n");
		tSet.enablePersistence(persistentStoreBuffer);
	}
	
	public void loadPersistentInformation(String fileName) throws Exception {
		FileReader fr = new FileReader(fileName);
		BufferedReader in = new BufferedReader(fr);
		String prolog = in.readLine();
		if (in.readLine().equals("<spec>")){
			StringBuffer spec=new StringBuffer();
			while (in.ready()){
				String spLine=in.readLine();
				if (spLine==null){
					throw new Exception();
				}
				if (spLine.equals("</spec>")){
					break;
				} else {
					spec.append(spLine);
					spec.append("\n");
				}
			}
			boolean ok = setReactionSpec(spec.toString());
			if (!ok){
				throw new Exception();
			}
		} else {
			throw new Exception();
		}
		tSet.empty();
		tSet.update(in);
		in.close();
	}
	
	public void disablePersistence(){
		isPersistent = false;
		tSet.disablePersistence();
		if (persistentStoreBuffer!=null){
			try {
				persistentStoreBuffer.close();
			} catch (Exception ex){
				ex.printStackTrace();
			}
		}
	}
	
	// ----------------
	
	public TrapGenerator addTrapGenerator(long time, LogicTuple t){
		// check che non ci sia giˆ e che time < tc
		trapGenCounter++;
		TrapGenerator gen = new TrapGenerator(trapGenCounter,time,t,this);
		trapGenSet.add(gen);
		return gen;		
	}
	
	public void addTrapEvent(TrapEvent ev){
		acceptTrapEvent(ev);
	}
	
	public boolean removeTrapGenerator(long aid){
		return trapGenSet.remove(aid);
	}
	
	public void installTrapTimerTask(TrapTimerTask t){
		spy("Schedule trap for "+t.getScheduledTime()+" (now: "+System.currentTimeMillis()+" )");
		trapsTimer.schedule(t,t.getScheduledTime());
	}
}


