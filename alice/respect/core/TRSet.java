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

import alice.tuplecentre.core.TriggeredReaction;

/**
 * Triggered Reaction Set.
 * 
 * @author aricci
 */
public class TRSet {

    private LinkedList trigs;
    // to manage transactions
    private LinkedList tAdded;
    private LinkedList tRemoved;
    private boolean transaction;

    public TRSet(){
        trigs=new LinkedList();
        tRemoved=new LinkedList();
        tAdded=new LinkedList();
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
        return (TriggeredReaction)trigs.removeFirst();
    }

    public Iterator getIterator() {
        return trigs.listIterator();
    }

    public TriggeredReaction[] toArray(){
    	int size = trigs.size();
    	TriggeredReaction[] trArray = new TriggeredReaction[size];
    	for (int i = 0; i < size; i++){
    		trArray[i] = (TriggeredReaction) trigs.get(i);
    	}
    	return trArray;
    }

    public void beginTransaction(){
        transaction=true;
        tAdded.clear();
        tRemoved.clear();
    }

    public void endTransaction(boolean commit){
        if (!commit){
            //System.out.println("[ tspace ] roll back!");
            Iterator it = tAdded.listIterator();
            while (it.hasNext()) {
            	   trigs.remove(it.next());
            }
            it=tRemoved.listIterator();
			while (it.hasNext()) {
                //System.out.println("[tspace] adding "+(Tuple)list[i]);
                trigs.add(it.next());
            }
        }
        transaction=false;
        tAdded.clear();
        tRemoved.clear();
    }
}

