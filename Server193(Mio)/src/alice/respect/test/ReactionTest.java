/*

+* ReactionTest.java
 *
 * Created on 4 dicembre 2000, 3.57
 */

package alice.respect.test;
import alice.logictuple.*;
import alice.respect.api.*;
import alice.respect.core.RespectTCContainer;


/**
 *
 * @author  Administrator
 * @version
 */


/*
class agent0 extends Thread {

    TupleCentre tc;
    int counter=0;

    public agent0(TupleCentre tu){
        tc=tu;
    }

    public void run(){
        try {
            while(true){
                tc.out(new LogicTuple("po",new Value(counter++)));
                tc.in(new LogicTuple("q",new Var("X")));
                try{
                    Thread.currentThread().sleep(100);
                } catch (Exception ex){
                }
            }
        } catch (Exception ex){
        }
    }
}

class agent1 extends Thread {

    TupleCentre tc;

    public agent1(TupleCentre tu){
        tc=tu;
    }

    public void run(){
        try {
            while(true){
                LogicTuple tuple= tc.in(new LogicTuple("p",new Var("X")));
                System.out.println(tuple);
                tc.out(new LogicTuple("q",new Value(tuple.getInt(0)+1)));
            }
        } catch (Exception ex){
        }
    }
}

             */
public class ReactionTest extends Object {

    /** Creates new ReactionTest */
    public ReactionTest() {
    }

    /**
    * @param args the command line arguments
    */
    public static void main (String args[]) throws Exception {

  /*
        Tuple tu0=new Tuple("copy",new Var("X"),new Struct("p",new Var("X")));
        Tuple tu1=new Tuple("copy",new Term(1),new Var("Y"));

        System.out.println(tu0.match_logic(tu1));

        tu0.unify(tu1);
        Tuple tcopy=(Tuple)tu1.copy();
        System.out.println(tcopy);

        System.exit(0);
    */

        /*
        Prolog prolog=new Prolog();
        prolog.solve("assert(reaction(in(chops(X,Y)),(pre,out_r(required(X,Y))))).");
        Struct c=new Struct("reaction",new Struct("in",new Struct("chops",new Term(1),new Term(2))),new Var("R"));
        //SolveInfo info=prolog.solve("reaction(in(chops(1,2)),R).");
        SolveInfo info=prolog.solve(c);
        Term term=info.getSolution();
        Term tcloned=(Term)term.clone();
        System.out.println(term);
        System.out.println(tcloned);
        System.exit(0);
        */

 /*
        Term term=Term.parse("reaction(in(service(X,Y)),( in_r(pippo(X,Z1)))).");
        Term termClone=(Term)term.clone();

        Term templateA=Term.parse("reaction(in(service(13,17)),Reaction).");
        Term templateB=Term.parse("reaction(in(service(13,17)),Reaction).");

        //Term templateC1=Term.parse("reaction(in(service(13,17)),Reaction).");
        //Term templateC2=Term.parse("reaction(in(service(13,17)),Reaction).");
        //Term templateC3=Term.parse("reaction(in(service(13,17)),Reaction).");
        //Term templateC4=Term.parse("reaction(in(service(13,17)),Reaction).");

        term.unify(templateA,0);
        termClone.unify(templateB,0);

        System.out.println(templateA);
        System.out.println(templateB);
        System.out.println(term);
        System.out.println(termClone);

        //templateA.unify(templateC1,0);
        //templateB.unify(templateC2,0);
        //term.unify(templateC3,0);
        //termClone.unify(templateC4,0);

        //System.out.println(templateA);
        //System.out.println(templateB);
        //System.out.println(term);
        //System.out.println(termClone);

        System.exit(0);
*/

        AgentId id = new AgentId("pippuzzo");
        /*Before*/
    	//RespectTC core=new RespectTC(new TupleCentreId("test"),100);
    	/*insert by Enrico Rossignoli*/
        RespectTC medium=new RespectTC(new TupleCentreId("test"),RespectTCContainer.getRespectTCContainer(),100);
        /*end insert*/
        /*
        medium.setSpec("reaction(out(p(X)),( out_r(q(X)))).");

        medium.out(new LogicTuple("p",new Value(1)));
        medium.out(new LogicTuple("p",new Value(2)));
        */
        medium.setSpec(id,new RespectSpecification(
                "reaction( out(p(T)),("+
                            "in_r(p(T)),"+
                            "out_r(q(T)))).\n"+
                "reaction( out_r(q(T)),("+
                            "in_r(q(T)))).\n"+
                "reaction( out_r(q(T)),("+
                            "out_r(bak(T)))).\n"
                            ));

		IBlockingContext ctx = medium.getBlockingContext();

		System.out.println("doing out...");
		
        ctx.out(id, new LogicTuple("p",new Value("test",new Var(), new Value(1))));
		
		System.out.println("doing in...");
		
		LogicTuple t = ctx.in(id,new LogicTuple("bak",new Var()));
		
		System.out.println("BBBBBBBBBBBBBBBBBBBBBB"+t);
		
		ITimedContext tc = medium.getTimedContext();
		
		tc.in(id,new LogicTuple("qq"),1000);

        System.exit(0);

    }


}



