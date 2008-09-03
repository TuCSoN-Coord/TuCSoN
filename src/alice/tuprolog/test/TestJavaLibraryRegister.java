package alice.tuprolog.test;

import alice.tuprolog.*;
import alice.tuprolog.lib.*;

public class TestJavaLibraryRegister {
	
	public static void main(String[] args) throws Exception {
	
		Prolog engine = new Prolog();
		engine.setSpy(true);
		
		String theory =
			"create:-			\n"+
			"   java_object('java.util.Random',[],T),\n"+
			"   register(T,rand). \n"+
			"test(X):-				\n"+
			"	rand <- nextFloat returns X.\n"+
			"dispose:-				\n"+
			"	unregister(rand).	\n";
					
		engine.setTheory(new Theory(theory));

		SolveInfo sol = engine.solve(new Struct("create"));
		System.out.println(sol);
		
		sol = engine.solve(new Struct("test",new Var("X")));
		System.out.println(sol);
		
		JavaLibrary lib = (JavaLibrary) engine.getLibrary("alice.tuprolog.lib.JavaLibrary");
		java.util.Random rand = (java.util.Random)lib.getRegisteredObject(new Struct("rand"));
		System.out.println("Object registered in the library: "+rand);
		
		System.out.println("Testing unregister...");
		
		sol = engine.solve(new Struct("dispose"));
		System.out.println(sol);
		
		/*
		 * The next query should succeeds, but with
		 * the variable bound 
		 */
		sol = engine.solve(new Struct("test",new Var("X")));
		System.out.println(sol);
	}
}
