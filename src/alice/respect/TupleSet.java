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

import alice.logictuple.LogicTuple;
/**
 *
 * An important aspect of the component implementing this interface
 * is the ability to execute transactions, that is execute sequence
 * of operations that could be commited or asked to be undone.
 *
 */
public class TupleSet  {

    private ArrayList tuples=new ArrayList();
    // to manage transactions
    private ArrayList tAdded=new ArrayList();
    private ArrayList tRemoved=new ArrayList();
    private boolean transaction=false;

    private Random rand=new Random();

    public TupleSet(){
    }

    public void add(LogicTuple t){

        int size=tuples.size();
        if (size>0){
            tuples.add(rand.nextInt(size),t);
        } else {
            tuples.add(t);
        }

        //tuples.add(t);
        if (transaction){
            tAdded.add(t);
        }
    }

    public void remove(LogicTuple t){
        tuples.remove(t);
        if (transaction){
            tRemoved.add(t);
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
            Object[] list=tAdded.toArray();
            for (int i=0; i<list.length; i++){
                //System.out.println("[tspace] removing "+(LogicTuple)list[i]);
                tuples.remove(list[i]);
            }
            list=tRemoved.toArray();
            for (int i=0; i<list.length; i++){
                //System.out.println("[tspace] adding "+(LogicTuple)list[i]);
                tuples.add(list[i]);
            }
        }
        transaction=false;
        tAdded.clear();
        tRemoved.clear();
    }

    public void    empty(){
        tuples.clear();
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

