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
package alice.respect;

import java.util.*;
import java.io.*;
import alice.logictuple.LogicTuple;
/**
 *
 * An important aspect of the component implementing this interface
 * is the ability to execute transactions, that is execute sequence
 * of operations that could be commited or asked to be undone.
 *
 */
public class PersistentTupleSet  {

    private LinkedList tuples;
    // to manage transactions
    private LinkedList tAdded;
    private LinkedList tRemoved;
    private boolean transaction;

	private boolean isPersistent;
	//private FileWriter actionStore; 
	private BufferedWriter actionStore; 
	
    public PersistentTupleSet() {
		tuples=new LinkedList();
		tAdded=new LinkedList();
		tRemoved=new LinkedList();
		transaction=false;
		//
		isPersistent = false;
		actionStore = null;
    }

    public void add(LogicTuple t){
        if (transaction){
            tAdded.add(t);
			tuples.add(t);
        } else {
			tuples.add(t);
			if (isPersistent){
				try {
					actionStore.write("+"+t.toString()+"\n");
					actionStore.flush();
				} catch (Exception ex){
					ex.printStackTrace();
				}	
			}
        }
    }

    public void remove(LogicTuple t){
        if (transaction){
        	tuples.remove(t);
            tRemoved.add(t);
        } else {
        	tuples.remove(t);
			if (isPersistent){
				try {
					actionStore.write("-"+t.toString()+"\n");
					actionStore.flush();
				} catch (Exception ex){
					ex.printStackTrace();
				}	
			}

        }
    }

    public boolean isEmpty(){
        return tuples.isEmpty();
    }

    public int size(){
        return tuples.size();
    }

    /**
     * Begins a transaction section
     *
     * Every operation on multiset can be undone
     */
    public void beginTransaction(){
        transaction=true;
        tAdded.clear();
        tRemoved.clear();
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
            Iterator it = tAdded.listIterator();
            while (it.hasNext()) {
            	//System.out.println("[tspace] removing "+(LogicTuple)list[i]);
                tuples.remove(it.next());
            }
            it=tRemoved.listIterator();
			while (it.hasNext()) {
                //System.out.println("[tspace] adding "+(LogicTuple)list[i]);
                tuples.add(it.next());
            }
        } else {
        	if (isPersistent){
				//System.out.println("[ tspace ] roll back!");
				try {
					Iterator it = tAdded.listIterator();
					while (it.hasNext()) {
						//System.out.println("[tspace] removing "+(LogicTuple)list[i]);
						actionStore.write("+"+it.next().toString()+"\n");
					}
					it=tRemoved.listIterator();
					while (it.hasNext()) {
						//System.out.println("[tspace] removing "+(LogicTuple)list[i]);
						actionStore.write("-"+it.next().toString()+"\n");
					}
					actionStore.flush();
				} catch (Exception ex){
					ex.printStackTrace();
				}	
        	}
        }
        transaction=false;
        tAdded.clear();
        tRemoved.clear();
    }

    public void    empty(){
    	tuples.clear();
    	if (isPersistent){
    		try {
				actionStore.write("e\n");
			} catch (Exception ex){
				ex.printStackTrace();
			}
    	}	
    }

    public LogicTuple getMatchingTuple(LogicTuple templ){
        if (templ==null)
            return null;
        //System.out.println("TSET: getMatchingTuple "+templ);
        ListIterator l=tuples.listIterator();
        while (l.hasNext()){
            LogicTuple tu=(LogicTuple)l.next();
            //System.out.println("-- "+tu);
            if (templ.match(tu)){
                //System.out.println(">> FOUND"+tu);
                l.remove();
                if (transaction){
                    tRemoved.add(tu);
                } else {
                	if (isPersistent){
						try {
							actionStore.write("-"+tu.toString()+"\n");
							actionStore.flush();
						} catch (Exception ex){
							ex.printStackTrace();
						}	
                	}
                }
                
                return tu;
            }
        }
        return null;
    }

    public LogicTuple readMatchingTuple(LogicTuple templ){
        if (templ==null)
            return null;
        //System.out.println("TSET: readMatchingTuple "+templ);
        ListIterator l=tuples.listIterator();
        while (l.hasNext()){
            LogicTuple tu=(LogicTuple)l.next();
            //System.out.println("-- "+tu);
            if (templ.match(tu)){
                //System.out.println(">> FOUND"+tu);
                return tu;
            }
        }
        return null;
    }

    public Iterator getIterator(){
        return tuples.listIterator();
    }

    public LogicTuple[] toArray() {
        return (LogicTuple[])tuples.toArray();
    }
    
    //
    
  
    public void enablePersistence(BufferedWriter out) throws IOException {
		isPersistent = true;
		actionStore = out;
		actionStore.write("<snapshot>\n");
		ListIterator l=tuples.listIterator();
		while (l.hasNext()){
			LogicTuple tu=(LogicTuple)l.next();
			actionStore.write(tu+"\n");
		}
		actionStore.write("</snapshot>\n");
		actionStore.write("<updates>\n");
		actionStore.flush();
    }
    
    public void disablePersistence(){
    	isPersistent = false;
    }
    
    public boolean isPersistenceOn() {
    	return isPersistent;
    } 
    
    public void update(BufferedReader in) throws Exception {
		String snap = in.readLine();
		if (!snap.equals("<snapshot>")){
			throw new Exception();
		}
		while (in.ready()){
			String tuple = in.readLine();
			if (tuple.equals("</snapshot>")){
				break;
			} else {
				LogicTuple t = LogicTuple.parse(tuple);
				tuples.add(t);
			}
		}
		
		String updates = in.readLine();
		if (!updates.equals("<updates>")){
			throw new Exception();
		}
		while (in.ready()){
			String cmd = in.readLine();
			if (cmd!=null){
				char ch = cmd.charAt(0);
				if (ch=='+'){
					LogicTuple t = LogicTuple.parse(cmd.substring(1));
					tuples.add(t);	
				} else if (ch=='-') {
					String st = cmd.substring(1);
					LogicTuple t = LogicTuple.parse(st);
					boolean found = false;
					Iterator it = tuples.iterator();
					while (it.hasNext()){
						if (it.next().toString().equals(st)){
							it.remove();
							found = true;
							break;
						}
					}	
					if (!found){
						throw new Exception();
					}
				} else if (ch=='e') {
					tuples.clear();
				} else {
					throw new Exception();
				}
			}
		}    		
    }
/*
    // used for testing performance

    static public void main(String[] args) throws Exception {
        TupleSet tset=new TupleSetImpl();

        LogicTuple[] out_tuples=new LogicTuple[]{
            LogicTuple.parse("p(1)"),
            LogicTuple.parse("p(2)"),
            LogicTuple.parse("p(3)"),
            LogicTuple.parse("p(ciccio,2)"),
            LogicTuple.parse("p(pluto,Y)"),
            LogicTuple.parse("p(X,15)"),
            LogicTuple.parse("q(303,p(X))"),
            LogicTuple.parse("q(_,_)"),
            LogicTuple.parse("q(13,_)"),
            LogicTuple.parse("q(13)"),
            LogicTuple.parse("q(test)")};

        LogicTuple[] in_tuples=new LogicTuple[]{
            LogicTuple.parse("p(X)"),
            LogicTuple.parse("p(Y,2)"),
            LogicTuple.parse("p(pluto,1)"),
            LogicTuple.parse("q(Z,K)"),
            LogicTuple.parse("q(_,p(1))"),
            LogicTuple.parse("q(13,1)"),
            LogicTuple.parse("q(_)"),
            LogicTuple.parse("q(X)")};


            Random rand=new Random();

            rand.setSeed(100);

            long time=System.currentTimeMillis();

            for (int i=0; i<100000; i++){
                int irand=rand.nextInt(out_tuples.length);
                tset.add(out_tuples[irand]);
                //System.out.println("out "+out_tuples[irand]+"   -  "+tset.size());
            }

            for (int i=0; i<100000; i++){
                int irand=rand.nextInt(in_tuples.length);
                LogicTuple tuple=tset.getMatchingTuple(in_tuples[irand]);
                //System.out.println("in "+in_tuples[irand]+" ->  "+tuple+"  -   "+ tset.size());
            }

            System.out.println("total ms: "+(System.currentTimeMillis()-time));
    }
    */

}

