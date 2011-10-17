/**
 * 5 Philosophers example using a logic tuple centre service as coordination model
 * with 3 prolog philosophers and 2 java philosophers
 *
 * this is a concrete example of coordination among etherogenous activities
 *
 * the prolog source is executed on  tuProlog virtual machine
 */

package alice.respect.test;

import alice.respect.api.*;
import alice.respect.core.RespectTCContainer;
import alice.logictuple.*;

/**
 * the java philosopher
 */
class Philo extends Thread {
    private AgentId id;
    private int c1,c2;
    private IBlockingContext medium;
    
    public Philo(RespectTC core, String n) throws Exception {
        id=new AgentId(n);
        medium=core.getBlockingContext();
        start();
    }
    
    public void run(){
        System.out.println("[ "+id+" ] run");
        try {
            LogicTuple tc=medium.rd(id,new LogicTuple("philo",new Value(id.toString()),new Var("C1"),new Var("C2")));
            c1=(int)tc.getArg(1).intValue();
            c2=(int)tc.getArg(2).intValue();
            while (true){
                think();
                medium.in(id,new LogicTuple("chops",new Value(c1),new Value(c2)));
                eat();
                medium.out(id,new LogicTuple("chops",new Value(c1),new Value(c2)));
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    
    void think(){
        System.out.println("[ "+id+" ] thinking");
    }
    
    void eat(){
        System.out.println("[ "+id+" ] eating "+c1+" "+c2);
    }
}




/**
 * the prolog philosopher running on the tuprolog virtual machine
 */
/*
class PrologPhilo implements Runnable {
    alice.tuprolog.Prolog core;
 
    PrologPhilo(TupleCentre medium, String name) throws Exception {
        core=new alice.tuprolog.Prolog();
        alice.tuprolog.lib.RespectTCLibrary lib=(alice.tuprolog.lib.RespectTCLibrary)core.loadLibrary("alice.tuprolog.lib.RespectTCLibrary");
        lib.setMedium(medium);
        core.setTheory(new alice.tuprolog.Theory(new ByteArrayInputStream(makeTheory(name).getBytes())));
        core.addOutputListener(new alice.tuprolog.OutputListener() {
                                       public void onOutput(alice.tuprolog.OutputEvent ev) {
                                                System.out.print(ev.getMsg());
                                       }});
        new Thread(this).start();
    }
 
    public void run() {
        try {
            alice.tuprolog.SolveInfo info=core.solve("go.");
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
 
    private String makeTheory(String name){
        return
            "think(Name):-                                                                              \n"+
            "              atom_chars(N1, [ '[ ',Name,' ] thinking']),                                  \n"+
            "              write(N1),nl.                                                                \n"+
            "eat(Name,C1,C2):-                                                                          \n"+
            "              num_atom(C1,NC1), num_atom(C2,NC2),                                          \n"+
            "              atom_chars(N1, [ '[ ',Name,' ] eating ',NC1,' ',NC2,' ']),                   \n"+
            "              write(N1),nl.                                                                \n"+
            "philo(Name,C1,C2):-                                                                        \n"+
            "              think(Name),                                                                 \n"+
            "              in(chops(C1,C2)),                                                            \n"+
            "              eat(Name,C1,C2),                                                             \n"+
            "              out(chops(C1,C2)),                                                           \n"+
            "              philo(Name,C1,C2).                                                           \n"+
            "go:- rd(philo("+name+",C1,C2)), philo("+name+",C1,C2).                                     \n";
    }
}
 **/
/**
 * install proper reactions to avoid deadlock and
 * make chops resources avaiable to philosophers
 */

public class TestDebugging {
    public static void main(String args[]) {
        try {
            /*Before*/
        	//RespectTC core=new RespectTC(new TupleCentreId("test"),100);
        	/*insert by Enrico Rossignoli*/
            RespectTC core=new RespectTC(new TupleCentreId("test"),RespectTCContainer.getRespectTCContainer(),100);
            /*end insert*/
            AgentId aid = new AgentId("main");
            
            core.setSpec(aid,new RespectSpecification(
            "reaction(in(chops(C1,C2)), (pre, out_r(required(C1,C2)))).\n"+
            "reaction(in(chops(C1,C2)), (post,in_r(required(C1,C2)))).\n"+
            "reaction(out_r(required(C1,C2)),(in_r(chop(C1)),in_r(chop(C2)),out_r(chops(C1,C2)))).\n"+
            "reaction(out(chops(C1,C2)), ( in_r(chops(C1,C2)),out_r(chop(C1)),out_r(chop(C2)))).\n"+
            "reaction(out_r(chop(C1)), (rd_r(required(C1,C)), in_r(chop(C1)),in_r(chop(C)),out_r(chops(C1,C)))).\n"+
            "reaction(out_r(chop(C2)), (rd_r(required(C,C2)), in_r(chop(C)),in_r(chop(C2)),out_r(chops(C,C2)))).\n"+
            //
			"reaction(out(bastarda),( out_r(bast) )).\n"+
			"reaction(out_r(bast),(in_r(bast), out_r(bast))).\n"));
            
            
            IBlockingContext medium = core.getBlockingContext();
            
            medium.out(aid,new LogicTuple("chop",new Value(0)));
			medium.out(aid,new LogicTuple("chop",new Value(1)));
			medium.out(aid,new LogicTuple("chop",new Value(2)));
            medium.out(aid,new LogicTuple("philo",new Value("arnaldo"),new Value(0),new Value(1)));
            medium.out(aid,new LogicTuple("philo",new Value("etles"),new Value(1),new Value(2)));
            medium.out(aid,new LogicTuple("philo",new Value("beppo"),new Value(2),new Value(0)));
            
            new Philo(core,"arnaldo");
			new Philo(core,"etles");
			new Philo(core,"beppo");
            
			Thread.sleep(2000);
            core.out(aid,new LogicTuple("bastarda"));
			Thread.sleep(1000);
            
			System.out.println("CI SONO");

			IManagementContext mc = core.getManagementContext();
            mc.setManagementMode(true);
            mc.stopCommand();
			Thread.sleep(1000);
			
			//core.setSpec(aid,new RespectSpecification(""));
			//core.goCommand();
			//System.out.println(medium.rdp(aid,new LogicTuple("philo",new Var(), new Var(),new Var())));
			
			
			mc.setSpy(true);
			
			for (int i=0; i<10; i++){
				System.out.println("STEP "+i);
				mc.nextStepCommand();
				Thread.sleep(200);
			}
			
			System.exit(0);
			
			
			
            
        } catch (Exception e ) {
            e.printStackTrace();
        }
    }
}

