/*
 * TRSetImpl.java
 *
 * Copyright 2000-2001-2002  aliCE team at deis.unibo.it
 *
 * This software is the proprietary information of deis.unibo.it
 * Use is subject to license terms.
 *
 */
package alice.respect;
import java.util.*;

import alice.tuplecentre.TriggeredReaction;

/**
 * An important aspect of the component implementing this interface
 * is the ability to execute transactions, that is execute sequence
 * of operations that could be commited or asked to be undone.
 *
 */
public class TRSet {

    private ArrayList trigs;
    // to manage transactions
    private ArrayList tAdded;
    private ArrayList tRemoved;
    private boolean transaction;

    private Random rand = new Random();

    public TRSet(){
        trigs=new ArrayList();
        tRemoved=new ArrayList();
        tAdded=new ArrayList();
        transaction=false;
    }

    public void add(TriggeredReaction t){
        trigs.add(t);
        if (transaction){
            tAdded.add(t);
        }
    }

    public void remove(TriggeredReaction t){
        trigs.remove(t);
        if (transaction){
            tRemoved.add(t);
        }
    }

    public boolean isEmpty(){
        return trigs.isEmpty();
    }

    public void empty(){
        trigs.clear();
    }
    // not-deterministic get + remove
    public TriggeredReaction get(){
        //return (TriggeredReaction)trigs.remove(trigs.size()-1);
        return (TriggeredReaction)trigs.remove(rand.nextInt(trigs.size()));
    }

    public Iterator getIterator() {
        return trigs.listIterator();
    }

    public TriggeredReaction[] toArray(){
        return (TriggeredReaction[])trigs.toArray();
    }

    public void beginTransaction(){
        transaction=true;
        tAdded.clear();
        tRemoved.clear();
    }

    public void endTransaction(boolean commit){
        if (!commit){
            //System.out.println("[ tspace ] roll back!");
            Object[] list=tAdded.toArray();
            for (int i=0; i<list.length; i++){
                //System.out.println("[tspace] removing "+(Tuple)list[i]);
                trigs.remove(list[i]);
            }
            list=tRemoved.toArray();
            for (int i=0; i<list.length; i++){
                //System.out.println("[tspace] adding "+(Tuple)list[i]);
                trigs.add(list[i]);
            }
        }
        transaction=false;
        tAdded.clear();
        tRemoved.clear();
    }
}

