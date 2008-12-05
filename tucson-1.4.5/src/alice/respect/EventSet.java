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

/**
 * Pending Query Set 
 *
 */
class EventSet  {

    private LinkedList events;
    // to manage transactions
    private LinkedList evAdded;
    private LinkedList evRemoved;
    private boolean transaction;


    public EventSet(){
		events=new LinkedList();
		evAdded=new LinkedList();
		evRemoved=new LinkedList();
		transaction=false;
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
    public alice.tuplecentre.Event get(){
        alice.tuplecentre.Event ev= (alice.tuplecentre.Event) events.removeFirst();
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

