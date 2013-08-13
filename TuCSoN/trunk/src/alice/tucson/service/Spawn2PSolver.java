package alice.tucson.service;

import alice.tuprolog.NoMoreSolutionException;
import alice.tuprolog.NoSolutionException;
import alice.tuprolog.Prolog;
import alice.tuprolog.SolveInfo;
import alice.tuprolog.Term;
import alice.tuprolog.event.OutputEvent;
import alice.tuprolog.event.OutputListener;

/**
 * 
 * @author ste (mailto: s.mariani@unibo.it) on 17/lug/2013
 * 
 */
public class Spawn2PSolver extends Thread {

    private final Term goal;
    private final Prolog solver;

    /**
     * 
     * @param s
     *            the Prolog engine to be used
     * @param g
     *            the goal to solve
     */
    public Spawn2PSolver(final Prolog s, final Term g) {
        super();
        this.solver = s;
        this.goal = g;
    }

    @Override
    public void run() {
        this.solver.addOutputListener(new OutputListener() {

            public void onOutput(final OutputEvent arg0) {
                System.out.println("[Spawn2PSolver]: " + arg0.getMsg());
            }
        });
        // System.out.println("[Spawn2PSolver]: theory = "
        // + this.solver.getTheory());
        System.out.println("[Spawn2PSolver]: goal = " + this.goal);
        SolveInfo info;
        try {
            info = this.solver.solve(this.goal);
            System.out.println("[Spawn2PSolver]: solution = "
                    + info.getSolution().toString());
            while (this.solver.hasOpenAlternatives()) {
                info = this.solver.solveNext();
                System.out.println("[Spawn2PSolver]: solution = "
                        + info.getSolution().toString());
            }
            this.solver.solveEnd();
        } catch (final NoMoreSolutionException e) {
            System.out.println("[Spawn2PSolver]: No more solutions.");
        } catch (NoSolutionException e) {
            System.out.println("[Spawn2PSolver]: No solution found.");
        }
    }

}
