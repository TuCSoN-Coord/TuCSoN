/*
 * Tuple Centre media - Copyright (C) 2001-2002  aliCE team at deis.unibo.it
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
package alice.tuplecentre.core;
import java.util.*;

import alice.tuplecentre.api.AgentId;
import alice.tuplecentre.api.IId;
import alice.tuplecentre.api.ITupleCentre;
import alice.tuplecentre.api.ITupleCentreManagement;
import alice.tuplecentre.api.InspectableEventListener;
import alice.tuplecentre.api.ObservableEventListener;
import alice.tuplecentre.api.Tuple;
import alice.tuplecentre.api.TupleCentreId;
import alice.tuplecentre.api.TupleTemplate;

/**
 * Defines the core abstract behaviour of a tuple centre virtual machine.
 *
 * The class is abstract because the specific implementation
 * of the reacting behaviour and of the set management 
 * is left to the derived classes.
 *
 * This class implements - by means of the state pattern -
 * the behaviour described formally in the article 
 * "From Tuple Space to Tuple Centre" (Omicini, Denti) -
 * Science of Computer Programming 2001,
 *
 *
 * @author aricci
 */
public abstract class TupleCentreVMContext implements ITupleCentreManagement, ITupleCentre {

    private long bootTime;
    
    private LinkedList inputEvents;
    private LinkedList inputEnvEvents;
    
    private TupleCentreVMState currentState;
    
    private InputEvent currentEvent;
    
    private int maxPendingInputEventNumber; 
    
    private TupleCentreId tid;

    private boolean isSpy = false;
    private boolean isDebugging = false;
    
    private boolean management;
    private boolean stop;
    private boolean do_step;
    
    private HashMap states;
    
    /**
     * Creates a new tuple centre virtual machine core
     *
     * @param tid is the tuple centre identifier
     * @param ieSize is the size of the input event queue
     */
    public TupleCentreVMContext(TupleCentreId tid, int ieSize){
        
       // bootTime=System.currentTimeMillis();        
    	management = false;
    	    
        inputEvents=new LinkedList();
        inputEnvEvents=new LinkedList();
        
        this.tid=tid;
        maxPendingInputEventNumber = ieSize;
        
        TupleCentreVMState resetState = new ResetState(this);
        TupleCentreVMState idleState = new IdleState(this);
		TupleCentreVMState listeningState = new ListeningState(this);
        TupleCentreVMState fetchState = new FetchState(this);
        TupleCentreVMState fetchEnvState = new FetchEnvState(this);
        TupleCentreVMState reactingState = new ReactingState(this);
        TupleCentreVMState speakingState = new SpeakingState(this);
        
        states = new HashMap();
        states.put("ResetState", resetState);
        states.put("IdleState", idleState);
		states.put("ListeningState", listeningState);
        states.put("FetchState", fetchState);
        states.put("FetchEnvState", fetchEnvState);
        states.put("ReactingState", reactingState);
        states.put("SpeakingState", speakingState);
        
        Iterator it = states.values().iterator();
        while (it.hasNext()){
            ((TupleCentreVMState)it.next()).resolveLinks();
        }
        
        currentState = resetState;
    }

    //-------------------------------------------------------------------------
    
	/**
	 * Executes a new Operation.
	 */
	public void doOperation(IId who, TupleCentreOperation op) throws OperationNotPossibleException  {
		//
		// the operation generates an input 
		InputEvent ev = new InputEvent(who,op,this.tid,this.getCurrentTime());
		synchronized(inputEvents){
			if (inputEvents.size()>this.maxPendingInputEventNumber){
				throw new OperationNotPossibleException();
			}
			//spy("Accepting new event " + ev);
			inputEvents.add(ev);
		}
	}	
	public void addEnvInputEvent(InputEvent in){
		synchronized(inputEvents){
			inputEnvEvents.add(in);
		}
	} 
	public void addInputEvent(InputEvent in){
		
		synchronized(inputEvents){
			inputEvents.add(in);
		}
	}
	
    /**
     * Executes a virtual machine behaviour cycle
     */
	public void execute() {

		try {
			if (management){
				if (stop){
					if (!do_step) {
						spy("VM is stopped");
						return;
					} else {
						do_step = false;
					}
				}
			}
			
			spy("Starting the execution cycle.");

			while (!currentState.isIdle()) {

				spy("Executing: " + currentState.getClass().getName());
				currentState.execute();
				currentState = currentState.getNextState();

				//System.out.println("==> "+management+" "+stop);
				if (management){
					if (stop){
						if (!do_step) {
							spy("VM is stopped");
							break;
						} else {
							do_step = false;
						}
					}
				}
			}

			spy("Ending the execution cycle.");
			spyState();
			
		} catch (Exception ex) {
			notifyException(ex);
		}

	}

    //-------------------------
    
    /**
     * Tests if there are pending input events
     *
     * The method tests in there are input events to be processed
     * (or rather if the input event queue is not empty)
     */
    public boolean pendingEvents(){
        try {
            synchronized(inputEvents){
                return inputEvents.size()>0;
            }
        }catch (Exception ex){
            notifyException(ex);
            return false;
        }
    }
    public boolean pendingEnvEvents(){
        try {
            synchronized(inputEnvEvents){
                return inputEnvEvents.size()>0;
            }
        }catch (Exception ex){
            notifyException(ex);
            return false;
        }
    }
    
    /**
     * Fetches a new pending input event.
     *
     * The first pending input event is fetched from the queue
     * as current event subject of VM process.
     *
     */
    public void fetchPendingEvent(){
        try {
            synchronized(inputEvents){
                currentEvent = (InputEvent)(inputEvents.removeFirst());
            }
        }catch (Exception ex){
            notifyException(ex);
        }
    }
    public void fetchPendingEnvEvent(){
    	if (pendingEnvEvents()){
    		try {
	    		synchronized(inputEnvEvents){
	                currentEvent = (InputEvent)(inputEnvEvents.removeFirst());
	            }
			}catch (Exception ex){
		           notifyException(ex);
		    }        
    	}
    }

    /**
     * Resets the tuple centre vm core.
     */
    public abstract void reset();
    
    
    //-------------------------
    
    public abstract void linkOperation(OutputEvent out);
    
    
    /**
     * Specifies how to notify an output event.
     */
    protected void notifyOutputEvent(OutputEvent ev){
		spy("notify event "+ev);
    	ev.getOperation().notifyCompletion();
    	
    }
    
    //-------------------------

    /*
     * Interface related to tuple centre reactive behaviour
     */

    /**
     * Evaluates a triggered reaction, changing the state
     * of the VM accordingly.
     *
     * @param z the triggered reaction to be evaluated
     */
    public abstract void      evalReaction(TriggeredReaction z);

    /**
     * Collects the reactions that are triggered by an event
     *
     * @param ev the event triggering reactions
     */
    public abstract void            fetchTriggeredReactions(Event ev);

    /**
     * Collects the time-triggered reactions
     *
     * @param ev the event triggering reactions
     */
    public abstract void            fetchTimedReactions(Event ev);
    
    /**
     * Removes a triggered reaction, previously fetched
     *
     * @return the triggered reaction
     */
    public abstract TriggeredReaction   removeTriggeredReaction();

    /**
     * Removes a time-triggered reaction, previously fetched
     *
     * @return the time-triggered reaction
     */
    public abstract TriggeredReaction   removeTimeTriggeredReaction();
    
    /**
     * Gets an iterator over the set of triggered reactions
     *
     * @return the iterator
     */
    public abstract Iterator            getTriggeredReactionSetIterator();

    
    public abstract boolean  triggeredReaction();
    
    public abstract boolean  time_triggeredReaction();
    
   
    //----------------------
    

    /*
     * Interface related to tuple set (T) management
     */

    /**
     *  Adds a tuple to the tuple set (T)
     *
     *  @param t the tuple to be addedd
     */
    public abstract void        addTuple(Tuple t);

    /**
     * Removes (not deterministically) from the tuple set  a tuple
     * that matches with the provided tuple template
     *
     * @param t the tuple template that must be matched by the tuple
     * @return a tuple matching the tuple template
     */
    public abstract Tuple       removeMatchingTuple(TupleTemplate t);

    /**
     * Gets all the tuples of the tuple centre
     *
     * @param 
     * @return the whole tuple set
     */
    public abstract List<Tuple>       getAllTuples();
    
    /**
     * Gets all the tuples of the tuple centre
     *
     * @param 
     * @return the whole tuple set
     */
    public abstract void       setAllTuples(List<Tuple> tupleList);
    
    /**
     * Gets (not deterministically) without removing from the tuple set  a tuple
     * that matches with the provided tuple template
     *
     * @param t the tuple template that must be matched by the tuple
     * @return a tuple matching the tuple template
     */
    public abstract Tuple       readMatchingTuple(TupleTemplate t);
    
    /**
     *  Adds a tuple to the specification tuple set 
     *
     *  @param t the tuple to be added
     */
    public abstract void        addSpecTuple(Tuple t);

    /**
     * Gets an iterator over the tuple set (T)
     *
     * @return the iterator
     */
    public abstract Iterator    getTupleSetIterator();
    
    public abstract Iterator getSpecTupleSetIterator();

    /**
     * Removes all tuples
     */
    public abstract void emptyTupleSet();

    // ---------------------------

    /*
     * Interface related to pending query set (W) management
     */

    /**
     * Adds a query to the pending query set (W) of the tuple centre
     *
     * @param w the pending query to be added
     */
    public abstract void        addPendingQueryEvent(InputEvent w);

    /**
     * Gets an iterator over the pending query set (W)
     *
     * @return the iterator
     */
    public abstract Iterator    getPendingQuerySetIterator();

	/**
	 * Removes the pending queries related to an agent
	 * 
	 * @param id is the agent identifies
	 */    
    public abstract void        removePendingQueryEventsOf(AgentId id);

	public abstract Tuple removeMatchingSpecTuple(TupleTemplate templateArgument);
	
	public abstract Tuple readMatchingSpecTuple(TupleTemplate templateArgument);
    
    // ------------------------------

    /**
     * Gets the boot time of the Tuple Centre VM
     *
     * The time is expressed in millisecond, according
     * to the standard Java measurement of time.
     */
    public long getBootTime(){
        return bootTime;
    }
    
    protected void setBootTime(){
    	this.bootTime = System.currentTimeMillis();
    	spy("Current ReSpecT VM time is: "+this.getCurrentTime());
    }
    
    /**
     * Gets current time of the Tuple Centre VM
     *
     * The time is expressed in millisecond, according
     * to the standard Java measurement of time.
     */
    public long getCurrentTime(){
        return System.currentTimeMillis()-bootTime;
    }


    /** 
     * Gets the identifier of this tuple centre
     *  
     */
    public TupleCentreId getId(){
        return tid;
    }
    
    /**
     * Gets the event currently processed by the virtual machine 
     * @return
     */
    public InputEvent getCurrentEvent(){
        return currentEvent;
    }
    
    /**
     * Gets a state of tuple centre virtual machine.
     * 
     * @param stateName name of the state
     * @return the state
     */
    public TupleCentreVMState getState(String stateName){
        return (TupleCentreVMState)(states.get(stateName));
    }
    
    
    // --------------------------------------------------------------------------

	/**
	 * Activates the production of spy information
	 */
    public void setSpy(boolean spy){
        isSpy=spy;
    }

	/**
	 * Tests if the spy information are active
	 * @return
	 */
    public boolean isSpy(){
        return isSpy;
    }

    public void spy(String m){
        if (isSpy){
            System.out.println("[TCVM-"+this.getId()+"]["+currentState.getClass().getName()+"]"+m);
        }
    }


    public void spyState(){
        if (isSpy){
            spy("-----------STATE--------------------");
            spy("input events");
            Iterator l=inputEvents.listIterator();
            while (l.hasNext())
                spy("[ev]"+(Event)l.next());
            spy("TSET");
            l=getTupleSetIterator();
            while (l.hasNext())
                spy("[tuple]"+(Tuple)l.next());
            spy("WSET");
            l=getPendingQuerySetIterator();
            while (l.hasNext())
                spy("[event]"+(Event)l.next());
            spy("ZSET");
            l=getTriggeredReactionSetIterator();
            while (l.hasNext()){
                TriggeredReaction tr=(TriggeredReaction)l.next();
                spy("[reaction]"+tr+" "+tr.getReaction());
            }
            spy("SIGMASET");
            l=getSpecTupleSetIterator();
            while (l.hasNext())
                spy("[tuple]"+(Tuple)l.next());
            spy("Behaviour Specification");
            spy(getReactionSpec().toString());
            spy("------------------------------------");
        }
    }

    public void notifyException(Exception ex){
        ex.printStackTrace();
    }

    public void notifyException(String ex){
        System.err.println(ex);
    }

	
	//-------------------------
	
	// MAnagement / Debugging COMMANDS
	
	public void setManagementMode(boolean activate){
		management = activate;
	}
	
	public void stopCommand() throws OperationNotPossibleException {
		if (!management){
			throw new OperationNotPossibleException();
		}
		stop = true;
	}

	public void goCommand() throws OperationNotPossibleException {
		if (!management){
			throw new OperationNotPossibleException();
		}
		stop = false;
	}

	public void nextStepCommand() throws OperationNotPossibleException {
		if (!management){
			throw new OperationNotPossibleException();
		}
		do_step = true;
	}

	public abstract void updateSpecAfterTimedReaction(TriggeredReaction tr);

}


