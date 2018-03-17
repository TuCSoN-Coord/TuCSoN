package alice.tuplecentre.tucson.service;

import alice.tuprolog.NoMoreSolutionException;
import alice.tuprolog.Prolog;
import alice.tuprolog.Term;
import alice.tuprolog.event.OutputEvent;
import alice.tuprolog.event.OutputListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

/**
 * @author ste (mailto: s.mariani@unibo.it)
 */
public class Spawn2PSolver extends Thread {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().getClass());

    private final Term goal;
    private final Prolog solver;

    /**
     * @param s the Prolog engine to be used
     * @param g the goal to solve
     */
    public Spawn2PSolver(final Prolog s, final Term g) {
        super();
        this.solver = s;
        this.goal = g;
    }

    @Override
    public void run() {
        this.solver.addOutputListener(new OutputListener() {

            @Override
            public void onOutput(final OutputEvent arg0) {
                LOGGER.info("[Spawn2PSolver]: " + arg0.getMsg());
            }
        });
        // System.out.println("[Spawn2PSolver]: theory = "
        // + this.solver.getTheory());
        LOGGER.info("[Spawn2PSolver]: goal = " + this.goal);
        try {
            this.solver.solve(this.goal);
            // System.out.println("[Spawn2PSolver]: solution = "
            // + info.getSolution().toString());
            while (this.solver.hasOpenAlternatives()) {
                this.solver.solveNext();
            }
            this.solver.solveEnd();
        } catch (final NoMoreSolutionException e) {
            LOGGER.info("[Spawn2PSolver]: No more solutions.");
        }
    }
}
