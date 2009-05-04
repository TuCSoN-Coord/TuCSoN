/*
 * TuCSoN - Copyright (C) 2001-2002  aliCE team at deis.unibo.it
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
package alice.tucson.examples.philo;

import alice.logictuple.*;
import alice.tucson.api.*;


/**
 * the java philosopher
 */
class JavaPhilo extends alice.tucson.api.Agent {
    TupleCentreId tid;
    int c1,c2;

    public JavaPhilo(String id,TupleCentreId tid_) throws TucsonException {
        super(id);
        tid=tid_;
    }

    protected void body(){
        System.out.println("[ "+getId()+" ] run");
        try {
            LogicTuple tc=rd(tid,new LogicTuple("philo",new Value(getId().toString()),new Var("C1"),new Var("C2")));
            c1=(int)tc.getArg(1).intValue();
            c2=(int)tc.getArg(2).intValue();
            while (true){
                think();
                in(tid,new LogicTuple("forks",new Value(c1),new Value(c2)));
                eat();
                out(tid,new LogicTuple("forks",new Value(c1),new Value(c2)));
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void think(){
        System.out.println("[ "+getId()+" ] thinking");
    }

    private void eat(){
        System.out.println("[ "+getId()+" ] eating "+c1+" "+c2);
    }
}

/**
 * the prolog philosopher running on the tuprolog virtual machine
 */
class PrologPhilo {
    PrologPhilo(String aid, TupleCentreId tid){
        String theory=makeTheory(tid,aid);
        new alice.tuprolog.Agent(theory).spawn();
    }

    String makeTheory(TupleCentreId tid,String name){
        return
        ":-load_library('alice.tucson.api.Tucson2PLibrary').                                              \n"+
        ":-solve(go).                                                                               \n"+
        "think(Name):-                                                                              \n"+
        "              atom_chars(N1, [ '[ ',Name,' ] thinking']),                                  \n"+
        "              write(N1),nl.                                                                \n"+
        "eat(Name,C1,C2):-                                                                          \n"+
        "              num_atom(C1,NC1), num_atom(C2,NC2),                                          \n"+
        "              atom_chars(N1, [ '[ ',Name,' ] eating ',NC1,' ',NC2,' ']),                   \n"+
        "              write(N1),nl.                                                                \n"+
        "philo(TCName,TCAddress,Name,C1,C2):-                                                       \n"+
        "              think(Name),                                                                 \n"+
        "              TCName @ TCAddress ? in(forks(C1,C2)),                                       \n"+
        "              eat(Name,C1,C2),                                                             \n"+
        "              TCName @ TCAddress ? out(forks(C1,C2)),                                      \n"+
        "              philo(TCName,TCAddress,Name,C1,C2).                                          \n"+
        "go:- enter_context('"+name+"'), "+tid.getName()+" @ "+tid.getNode()+
              " ? rd(philo('"+name+"',C1,C2)),                         \n"+
        "     philo("+tid.getName()+","+tid.getNode()+","+name+",C1,C2).                                 \n";
    }
}


/**
 * install proper reactions to avoid deadlock and
 * make forks resources avaiable to philosophers
 */
class MainResource extends alice.tucson.api.Agent {

    TupleCentreId tid;
    
    MainResource(TupleCentreId tid_) throws TucsonException {
        super("table_manager");
        tid=tid_;
    }

    protected void body() {
        System.out.println("[ main ] run");
        
        try {
            setSpec(tid,"reaction(in(forks(C1,C2)), (pre, out_r(required(C1,C2)))).\n"+
                "reaction(in(forks(C1,C2)), (post,in_r(required(C1,C2)))).\n"+
                "reaction(out_r(required(C1,C2)),(in_r(fork(C1)),in_r(fork(C2)),out_r(forks(C1,C2)))).\n"+
                "reaction(out(forks(C1,C2)), ( in_r(forks(C1,C2)),out_r(fork(C1)),out_r(fork(C2)))).\n"+
                "reaction(out_r(fork(C1)), (rd_r(required(C1,C)), in_r(fork(C1)),in_r(fork(C)),out_r(forks(C1,C)))).\n"+
                "reaction(out_r(fork(C2)), (rd_r(required(C,C2)), in_r(fork(C)),in_r(fork(C2)),out_r(forks(C,C2)))).\n");

            out(tid,new LogicTuple("fork",new Value(0)));
            out(tid,new LogicTuple("fork",new Value(1)));
            out(tid,new LogicTuple("fork",new Value(2)));
            out(tid,new LogicTuple("fork",new Value(3)));
            out(tid,new LogicTuple("fork",new Value(4)));
            // we make philosophers known what forks they must use
            out(tid,new LogicTuple("philo",new Value("arnaldo"),new Value(0),new Value(1)));
            out(tid,new LogicTuple("philo",new Value("etles"),new Value(1),new Value(2)));
            out(tid,new LogicTuple("philo",new Value("beppo"),new Value(2),new Value(3)));
            out(tid,new LogicTuple("philo",new Value("cledes"),new Value(3),new Value(4)));
            out(tid,new LogicTuple("philo",new Value("rosa"),new Value(4),new Value(0)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

/**
 * 5 Philosophers example using Tucson Service as coordination model
 * with 3 prolog philosophers and 2 java philosophers
 *
 */
public class MixedPhiloRun {
    public static void main (String args[]) {
        try {
            TupleCentreId tid;
            if (args.length==0){
                tid=new TupleCentreId("table");
            } else if (args.length==1) {
                tid=new TupleCentreId(args[0]);
            } else {
                System.err.println("args: {tuple centre identifier}");
                System.exit(-1);
                return;
            }
            new MainResource(tid).spawn();
            new Philosopher("arnaldo",tid).spawn();
            new Philosopher("etles",tid).spawn();
            new PrologPhilo("beppo",tid);
            new PrologPhilo("cledes",tid);
            new PrologPhilo("rosa",tid);
        } catch (Exception e ) {
            e.printStackTrace();
        }
    }
}
