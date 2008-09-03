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
class TrapGenSet  {

    private LinkedList generators;
    // to manage transactions
    private LinkedList genAdded;
    private LinkedList genRemoved;
    private boolean transaction;


    public TrapGenSet(){
		generators=new LinkedList();
		genAdded=new LinkedList();
		genRemoved=new LinkedList();
		transaction=false;
    }

    public void add(TrapGenerator t){
        generators.add(t);
        if (transaction){
            genAdded.add(t);
        }
    }

    public boolean remove(long id){
    	Iterator it = generators.iterator();
		TrapGenerator found = null;
    	while (it.hasNext()){
    		TrapGenerator gen = ((TrapGenerator) it.next());
    		if (gen.getId()==id){
    			found = gen;
    			it.remove();
    			break;
    		}
    	}
    	if (found==null){
    		return false;
    	}
        if (transaction){
            genRemoved.add(found);
        }
        return true;
    }

    public boolean isEmpty(){
        return generators.isEmpty();
    }

    public int size(){
        return generators.size();
    }

    /**
     * Begins a transaction section
     *
     * Every operation on multiset can be undone
     */
    public void beginTransaction(){
        transaction=true;
        genAdded.clear();
        genRemoved.clear();
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
            Iterator it = genAdded.listIterator();
            while (it.hasNext()) {
            	generators.remove(it.next());
            }
            
			it = genRemoved.listIterator();
			while (it.hasNext()) {
                //System.out.println("[tspace] adding "+(Tuple)list[i]);
                generators.add(it.next());
            }
        } else {
			Iterator it = genAdded.listIterator();
			while (it.hasNext()) {
				((TrapGenerator)(it.next())).install();
			}
			it = genRemoved.listIterator();
			while (it.hasNext()) {
				((TrapGenerator)(it.next())).kill();
			}
        }
        transaction=false;
        genAdded.clear();
        genRemoved.clear();
    }

    public void    empty(){
        generators.clear();
    }

    public Iterator getIterator(){
        return generators.listIterator();
    }
}

