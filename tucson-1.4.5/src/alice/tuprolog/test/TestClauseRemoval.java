package alice.tuprolog.test;

import alice.tuprolog.*;

public class TestClauseRemoval {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		Prolog engine = new Prolog();
		
		Theory theory = new Theory(
				"a(X,Y):-b(X),c(Y).\n"+
				"b(1).\n"+
				"c(3).\n"+
				"c(2).\n");
		
		engine.setTheory(theory);
		
		System.out.println(engine.getTheory());

		SolveInfo info = engine.solve("a(X,Y).");
		
		System.out.println(info.isSuccess());
		
		engine.removeClauses("c(_).");
		
		info = engine.solve("a(X,Y).");
		
		System.out.println(info.isSuccess());
		
		System.out.println(engine.getTheory());
		
		
		engine.addClause("c(4).");

		info = engine.solve("a(X,Y).");
		
		System.out.println(info.isSuccess());

		System.out.println(engine.getTheory());

		engine.removeClauses("a(X,Y):-b(X),c(Y).");
		
		info = engine.solve("a(X,Y).");
		
		System.out.println(info.isSuccess());
		
		System.out.println(engine.getTheory());
		
		
		
	}

}
