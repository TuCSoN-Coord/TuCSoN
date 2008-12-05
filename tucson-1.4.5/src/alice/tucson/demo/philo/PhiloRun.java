/*
 * tuProlog - Copyright (C) 2001-2002  aliCE team at deis.unibo.it
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
 */package alice.tucson.demo.philo;

import alice.logictuple.*;
import alice.tucson.api.*;


/**
 * the java philosopher
 */
class JavaPhilo extends TucsonAgent {
    TupleCentreId tid;
    int c1,c2;

    public JavaPhilo(AgentId id,TupleCentreId tid_) {
        super(id);
        tid=tid_;
    }

    protected void main(){
        System.out.println("[ "+getId()+" ] run");
        try {
            LogicTuple tc = synch_rd(tid,new LogicTuple("philo",new Value(getId().toString()),new Var("C1"),new Var("C2")));
            
            c1=(int)tc.getArg(1).intValue();
            c2=(int)tc.getArg(2).intValue();
            while (true){
                think();
                synch_in(tid,new LogicTuple("chops",new Value(c1),new Value(c2)));
                eat();
                synch_out(tid,new LogicTuple("chops",new Value(c1),new Value(c2)));
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
    PrologPhilo(AgentId id, TupleCentreId tid){
        String theory=makeTheory(tid,id);
        new alice.tuprolog.Agent(theory).spawn();
    }

    String makeTheory(TupleCentreId tid,AgentId id){
        String  name=id.toString();
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
        "              TCName @ TCAddress ? in(chops(C1,C2)),                                       \n"+
        "              eat(Name,C1,C2),                                                             \n"+
        "              TCName @ TCAddress ? out(chops(C1,C2)),                                      \n"+
        "              philo(TCName,TCAddress,Name,C1,C2).                                          \n"+
        "go:- enter_context("+name+"), "+tid.getName()+" @ '"+tid.getNode()+
              "' ? rd(philo("+name+",C1,C2)),                         \n"+
        "     philo("+tid.getName()+",'"+tid.getNode()+"',"+name+",C1,C2).                                 \n";
    }
}


/**
 * install proper reactions to avoid deadlock and
 * make chops resources avaiable to philosophers
 */
class MainResource extends TucsonAgent {

    TupleCentreId tid;
    
    MainResource(TupleCentreId tid_) throws TucsonException {
        super(new AgentId("main"));
        tid=tid_;
    }

    protected void main() {
        System.out.println("[ main ] run");
        
        try {
            setSpec(tid,"reaction(in(chops(C1,C2)), (pre, out_r(required(C1,C2)))).\n"+
                "reaction(in(chops(C1,C2)), (post,in_r(required(C1,C2)))).\n"+
                "reaction(out_r(required(C1,C2)),(in_r(chop(C1)),in_r(chop(C2)),out_r(chops(C1,C2)))).\n"+
                "reaction(out(chops(C1,C2)), ( in_r(chops(C1,C2)),out_r(chop(C1)),out_r(chop(C2)))).\n"+
                "reaction(out_r(chop(C1)), (rd_r(required(C1,C)), in_r(chop(C1)),in_r(chop(C)),out_r(chops(C1,C)))).\n"+
                "reaction(out_r(chop(C2)), (rd_r(required(C,C2)), in_r(chop(C)),in_r(chop(C2)),out_r(chops(C,C2)))).\n");

            out(tid,new LogicTuple("chop",new Value(0)));
            out(tid,new LogicTuple("chop",new Value(1)));
            out(tid,new LogicTuple("chop",new Value(2)));
            out(tid,new LogicTuple("chop",new Value(3)));
            out(tid,new LogicTuple("chop",new Value(4)));
            // we make philosophers known what chops they must use
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
public class PhiloRun {
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
            new JavaPhilo(new AgentId("arnaldo"),tid).spawn();
            new JavaPhilo(new AgentId("etles"),tid).spawn();
            new JavaPhilo(new AgentId("beppo"),tid).spawn();
            new JavaPhilo(new AgentId("cledes"),tid).spawn();
            new JavaPhilo(new AgentId("rosa"),tid).spawn();
            
            //new PrologPhilo(new AgentId("beppo"),tid);
            //new PrologPhilo(new AgentId("cledes"),tid);
            //new PrologPhilo(new AgentId("rosa"),tid);
        } catch (Exception e ) {
            e.printStackTrace();
        }
    }
}
