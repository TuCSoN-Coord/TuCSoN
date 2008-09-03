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
package alice.respect.tools;
import alice.tuplecentre.*;
import alice.respect.*;
import java.util.*;

class InspectorCore implements InspectableEventListener {

	/* listeners registrated for virtual machine output events */
	private Vector contextListeners = new Vector();

    /** reference to the observed tuple centre */
    RespectVM core;
	TupleCentre tc;
		
    /** current observation protocol */
    InspectorProtocol protocol;
	
    boolean nextStep;

    public InspectorCore(TupleCentre tc)
            throws Exception {
        //
        this.tc=tc;
        this.core=tc.getCore();
        protocol=new InspectorProtocol(); //msg.info;
		core.addInspector(this);
    }

    protected void log(String st){
        System.out.println("[INSPECTOR] "+st);
    }
    
    /**
     * notifying service invoked by the tuple centre virtual machine
     * when a new observable event has been produced
     */
    synchronized public void onInspectableEvent(InspectableEvent ev) {
        while (protocol.tracing && !nextStep){
            try {
                wait();
            } catch (Exception ex){
            }
        }
        nextStep=false;
        try {
            InspectorContextEvent msg=new InspectorContextEvent();
            //msg.localTime=System.currentTimeMillis();
            msg.vmTime=ev.getTime();

            if (ev.getType()==InspectableEvent.TYPE_NEWSTATE){
                int i=0;
                if (protocol.tsetObservType==InspectorProtocol.PROACTIVE_OBSERVATION){

                    Iterator it=core.getTupleSetIterator();
                    msg.tuples=new LinkedList();
                    if (protocol.tsetFilter==null){
                        while (it.hasNext()){
                            msg.tuples.add((Tuple)it.next());
                        }
                    } else {
                        while (it.hasNext()){
                            Tuple tu=(Tuple)it.next();
                            if (protocol.tsetFilter.match(tu)){
                                msg.tuples.add(tu);
                            }
                        }
                    }
                }
                if (protocol.pendingQueryObservType==InspectorProtocol.PROACTIVE_OBSERVATION){
                    Iterator it=core.getPendingQuerySetIterator();
                    msg.wnEvents=new LinkedList();
                    while (it.hasNext()){
                        msg.wnEvents.add((Event)it.next());
                    }
                }
                //System.out.println("SEND "+msg.tuples+" "+msg.wnEvents);
                dispatch(msg);
            }
            else if (ev.getType()==ObservableEventExt.TYPE_REACTIONOK){
                if (protocol.reactionsObservType!=InspectorProtocol.NO_OBSERVATION){
                    msg.reactionOk=((ObservableEventReactionOK)ev).z;
					dispatch(msg);
                }
            } else if (ev.getType()==ObservableEventExt.TYPE_REACTIONFAIL){
                if (protocol.reactionsObservType!=InspectorProtocol.NO_OBSERVATION){
                    msg.reactionFailed=((ObservableEventReactionFail)ev).z;
					dispatch(msg);
                }
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }


    // service requested from remote proxy
	/** setting a new observation protocol */
	public void updateProtocol()  {
		try {
	        boolean wasTracing=protocol.tracing;
	        if (wasTracing){
	            notifyAll();
	        }
	        if (!protocol.tracing){
	            onInspectableEvent(new alice.tuplecentre.InspectableEvent(this,InspectableEvent.TYPE_NEWSTATE));
	        }
		} catch (Exception ex){
			ex.printStackTrace();
		} 
    }

    /** get a tuple centre set (T set, W set,...) snapshot */
    public void getSnapshot(){
        InspectorContextEvent msg=new InspectorContextEvent();
        synchronized (core) {
            msg.vmTime=System.currentTimeMillis();
            msg.localTime=System.currentTimeMillis();
            //if (m.what==GetSnapshotMsg.TSET){
                msg.tuples=new LinkedList();
                Iterator it=core.getTupleSetIterator();
                if (protocol.tsetFilter==null){
                    while (it.hasNext()){
                        msg.tuples.add(it.next());
                    }
                } else {
                    while (it.hasNext()){
                        Tuple tu=(Tuple)it.next();
                        if (protocol.tsetFilter.match(tu)){
                            msg.tuples.add(tu);
                        }
                    }
                }
            //}
            /* else if (m.what==GetSnapshotMsg.WSET){
                msg.wnEvents=new LinkedList();
                Iterator it=core.getPendingQuerySetIterator();
                while (it.hasNext()){
                    msg.wnEvents.add(it.next());
                }
            }
            try {
                outStream.writeObject(msg);
                outStream.flush();
            } catch (Exception ex){
                ex.printStackTrace();
            }*/
            try {
				dispatch(msg);
            } catch(Exception ex){
            	ex.printStackTrace();
            }
        }
    }

    /** reset the tuple centre VM */
    synchronized public void reset(){
        core.reset();
    }

    /** ask a new step for a tuple centre vm  during tracing */
    synchronized public void nextStep(){
        if (protocol.tracing){
            nextStep=true;
            notifyAll();
        }
    }

    /** set a new tuple set */
    synchronized public void setTupleSet(ArrayList tset){
        try {
            synchronized (core) {
                core.removeAllTuples();
                Iterator it=tset.listIterator();
                while (it.hasNext()){
                    Tuple tuple=(Tuple)it.next();
                    core.addTuple(tuple);
                }
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }


    //public synchronized void notify(OutputEvent ev) {
    //}

    /** set a new Wn event set */
    /*
    synchronized public void setEventSet(SetEventSetMsg m){
        try {
            synchronized (core) {
                core.getWSet().empty();
                Iterator it=m.eventWnSet.listIterator();
                while (it.hasNext()){
                    Event event=(Event)it.next();
                    core.getWSet().add(event);
                }
            }
            System.out.println("new event set ok.");
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
     */

    public void exit(){
		core.removeInspector(this);
    }
    
	/**
	  * Adds a listener to Inspector Events
	  *
	  * @param l the listener
	  */
	 public void addInspectorContextListener(InspectorContextListener l){
		 contextListeners.addElement(l);
	 }
	
	 /**
	  * Removes a listener to Inspector Events
	  */
	 public void removeInspectorContextListener(InspectorContextListener l){
		 contextListeners.removeElement(l);
	 }
	
	 private void dispatch(InspectorContextEvent msg) throws Exception {
		for (int i=0; i<contextListeners.size(); i++){
			((InspectorContextListener)contextListeners.elementAt(i)).onContextEvent(msg);
		}
	 }
}
