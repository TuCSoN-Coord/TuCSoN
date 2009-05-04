/*
 
+* ReactionTest.java
 *
 * Created on 4 dicembre 2000, 3.57
 */

package alice.respect.test;
import alice.logictuple.*;
import alice.respect.api.*;

/**
 *
 * @author  Administrator
 * @version
 */

class Agent0 extends Thread {
    
    private RespectTC tc;
    private int counter=0;
    private AgentId id;
    
    public Agent0(RespectTC tu) throws Exception {
        tc=tu;
        id = new AgentId("agent0");
        start();
    }
    
    public void run(){
        try {
            while(true){
                tc.out(id, new LogicTuple("p",new Value(counter++)));
                try{
                    Thread.sleep(100);
                } catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
}

class Agent1 extends Thread {
    
    private IBlockingContext tc;
    private AgentId id;
    
    public Agent1(RespectTC tu) throws Exception {
        tc=tu.getBlockingContext();
        id = new AgentId("agent1");
        start();
    }
    
    public void run(){
        try {
            while(true){
                LogicTuple tuple= tc.in(id, new LogicTuple("q",new Var("X")));
                System.out.println(tuple);
                tc.out(id, new LogicTuple("t",new Value(tuple.getArg(0).intValue()+1)));
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
}


public class ReactionTest2 extends Object {
    
    /** Creates new ReactionTest */
    public ReactionTest2() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) throws Exception {
        
        
        AgentId id = new AgentId("main");
        RespectTC medium=new RespectTC(new TupleCentreId("test"),100);
        
        
        medium.setSpec(id,new RespectSpecification("reaction(out(p(X)),( in_r(p(X)), out_r(q(X)) ))."));
        
        /*
        medium.out(new LogicTuple("p",new Value(1)));
        medium.out(new LogicTuple("p",new Value(2)));
         */
        /*
        medium.setSpec(id,
        "reaction( out(p(X)),("+
        "in_r(p(X)),"+
        "out_tc(test,p(X)))).\n"+
        "reaction( out(p(X)),("+
        "in_r(q(X)),"+
        "out_tc(test,q(X)))).\n");
        */
        
        new Agent0(medium);
        new Agent1(medium);
        
        
    }
    
    
}



