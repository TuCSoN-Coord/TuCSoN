/*
 * TupleSetImpl.java
 *
 * Copyright 2000-2001-2002  aliCE team at deis.unibo.it
 *
 * This software is the proprietary information of deis.unibo.it
 * Use is subject to license terms.
 *
 */
package alice.respect;
import java.util.*;

/**
 *
 * An important aspect of the component implementing this interface
 * is the ability to execute transactions, that is execute sequence
 * of operations that could be commited or asked to be undone.
 *
 */
public class EventSet  {

    private List events=new LinkedList();
    // to manage transactions
    private List evAdded=new LinkedList();
    private List evRemoved=new LinkedList();
    private boolean transaction=false;

    private Random rand=new Random();

    public EventSet(){
    }

    public void add(alice.tuplecentre.Event t){
        events.add(t);
        if (transaction){
            evAdded.add(t);
        }
    }

    public void remove(alice.tuplecentre.Event t){
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
            Object[] list=evAdded.toArray();
            for (int i=0; i<list.length; i++){
                //System.out.println("[tspace] removing "+(Tuple)list[i]);
                events.remove(list[i]);
            }
            list=evRemoved.toArray();
            for (int i=0; i<list.length; i++){
                //System.out.println("[tspace] adding "+(Tuple)list[i]);
                events.add(list[i]);
            }
        }
        transaction=false;
        evAdded.clear();
        evRemoved.clear();
    }

    public void    empty(){
        events.clear();
    }

    // not-deterministic get + remove
    public alice.tuplecentre.Event get(){
        alice.tuplecentre.Event ev=(alice.tuplecentre.Event)events.remove(rand.nextInt(events.size()));
        if (transaction){
            evRemoved.add(ev);
        }
        return ev;
    }

    public Iterator getIterator(){
        return events.listIterator();
    }

    public void removeEventsOf(alice.tuplecentre.AgentId id){
        Iterator it=events.listIterator();
        while (it.hasNext()){
            alice.tuplecentre.Event ev=(alice.tuplecentre.Event)it.next();
            if (ev.getAgentId().toString().equals(id.toString())){
                it.remove();
            }
        }
    }

    public alice.tuplecentre.Event[] toArray(){
        return (alice.tuplecentre.Event[])events.toArray();
    }



}

