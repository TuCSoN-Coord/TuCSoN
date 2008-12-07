/*
 * TupleSetImpl.java
 *
 * Copyright 2000-2001-2002  aliCE team at deis.unibo.it
 *
 * This software is the proprietary information of deis.unibo.it
 * Use is subject to license terms.
 *
 */
package alice.respect.core;
import java.util.*;

/**
 * Pending Query Set. 
 *
 * @author aricci
 */
public class PendingQuerySet  {

    private LinkedList events;
    // to manage transactions
    private LinkedList evAdded;
    private LinkedList evRemoved;
    private boolean transaction;


    public PendingQuerySet(){
		events=new LinkedList();
		evAdded=new LinkedList();
		evRemoved=new LinkedList();
		transaction=false;
    }

    public void add(alice.tuplecentre.core.Event t){
        events.add(t);
        if (transaction){
            evAdded.add(t);
        }
    }

    public void remove(alice.tuplecentre.core.Event t){
        events.remove(t);
        if (transaction){
            evRemoved.add(t);
        }
    }

    public boolean isEmpty(){
        return events.isEmpty();
    }

    public int size(){
        return events.size();
    }

    /**
     * Begins a transaction section
     *
     * Every operation on multiset can be undone
     */
    public void beginTransaction(){
        transaction=true;
        evAdded.clear();
        evRemoved.clear();
    }

    /**
     * Ends a transaction section specifying
     * if operations must be committed or undone
     *
     * @param commit if <code>true</code> the operations are committed, else
     *               they are undone and the multiset is rolled back to the
     *               state before the <code>beginTransaction</code> invocation
     */
    public void endTransaction(boolean commit){
        if (!commit){
            //System.out.println("[ tspace ] roll back!");
            Iterator it = evAdded.listIterator();
            while (it.hasNext()) {
            	events.remove(it.next());
            }
            
			it = evRemoved.listIterator();
			while (it.hasNext()) {
                //System.out.println("[tspace] adding "+(Tuple)list[i]);
                events.add(it.next());
            }
        }
        transaction=false;
        evAdded.clear();
        evRemoved.clear();
    }

    public void    empty(){
        events.clear();
    }

    // get + remove
    public alice.tuplecentre.core.Event get(){
        alice.tuplecentre.core.Event ev= (alice.tuplecentre.core.Event) events.removeFirst();
        if (transaction){
            evRemoved.add(ev);
        }
        return ev;
    }

    public Iterator getIterator(){
        return events.listIterator();
    }

    public boolean removeEventOfOperation(long opId){
        Iterator it=events.listIterator();
        while (it.hasNext()){
        	alice.tuplecentre.core.Event ev=(alice.tuplecentre.core.Event)it.next();
            if (ev.getOperation().getId()==opId){
                it.remove();
                return true;
            }
        }
        return false;
    }

	public void removeEventsOf(alice.tuplecentre.api.AgentId id){
		Iterator it=events.listIterator();
		while (it.hasNext()){
			alice.tuplecentre.core.Event ev=(alice.tuplecentre.core.Event)it.next();
			if (ev.getId().toString().equals(id.toString())){
				it.remove();
			}
		}
	}

    public alice.tuplecentre.core.Event[] toArray(){
    	int size = events.size();
    	alice.tuplecentre.core.Event[] evArray = new alice.tuplecentre.core.Event[size];
    	for(int i = 0; i < size; i++){
    		evArray[i] = (alice.tuplecentre.core.Event) events.get(i);
    	}
    	return evArray;
    }

}

