/**
 * PrologAgentTest.java
 */
package helloWorld;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import alice.tuprolog.InvalidLibraryException;
import alice.tuprolog.InvalidTheoryException;
import alice.tuprolog.MalformedGoalException;
import alice.tuprolog.NoMoreSolutionException;
import alice.tuprolog.NoSolutionException;
import alice.tuprolog.Prolog;
import alice.tuprolog.SolveInfo;
import alice.tuprolog.Theory;
import alice.tuprolog.event.OutputEvent;
import alice.tuprolog.event.OutputListener;

/**
 * @author ste (mailto: s.mariani@unibo.it)
 *
 */
public final class PrologHelloWorld {

    /*
     * Remember the dot!
     */
    private static final String DEFAULT_GOAL = "runHelloWorld(agent-test, default@localhost:20504).";
    /*
     * The necessary Tucson2PLibrary...
     */
    private static final String DEFAULT_LIBRARY_NAME = "Tucson2PLibrary";
    /*
     * ...can be found within TuCSoN .jar, hence: "path/to/jar.jar" (here we
     * have direct access).
     */
    private static final String DEFAULT_LIBRARY_PATH = "./";
    /*
     * Relative path w.r.t. to running location (project root).
     */
    private static final String DEFAULT_THEORY_PATH = "alice/tucson/examples/helloWorld/helloWorld.pl";
    private static final String ME = "PrologHelloWorld";

    /**
     * @param args
     *            argument 0 can be the theory path, then argument 1 can be the
     *            goal to be solved
     */
    public static void main(final String[] args) {
        /*
         * 1) Get the Prolog program (theory) to run.
         */
        String sTheory = null;
        try {
            if (args.length > 0) {
                sTheory = PrologHelloWorld.fileToString(args[0]);
            } else {
                sTheory = PrologHelloWorld
                        .fileToString(PrologHelloWorld.DEFAULT_THEORY_PATH);
            }
        } catch (final IOException e) {
            Logger.getLogger(PrologHelloWorld.ME).log(Level.SEVERE,
                    "IOException");
            e.printStackTrace();
            System.exit(-1);
        }
        /*
         * 2) Boot a tuProlog engine with required libraries (Tucson2PLibrary)
         * loaded.
         */
        final Prolog engine = new Prolog();
        try {
            engine.loadLibrary(PrologHelloWorld.DEFAULT_LIBRARY_NAME,
                    new String[] { PrologHelloWorld.DEFAULT_LIBRARY_PATH });
        } catch (final InvalidLibraryException e) {
            Logger.getLogger(PrologHelloWorld.ME).log(Level.SEVERE,
                    "InvalidLibraryException");
            System.exit(-1);
        }
        /*
         * 3) Set the theory into the engine.
         */
        try {
            engine.setTheory(new Theory(sTheory));
        } catch (final InvalidTheoryException e) {
            Logger.getLogger(PrologHelloWorld.ME).log(Level.SEVERE,
                    "InvalidTheoryException");
            System.exit(-1);
        }
        /*
         * 4) [OPTIONAL] Capture tuProlog output and redirect it to Java Logger.
         */
        engine.addOutputListener(new OutputListener() {

            @Override
            public void onOutput(final OutputEvent arg0) {
                System.out.println(arg0.getMsg());
            }
        });
        /*
         * 5) Solve a given goal using the engine on the theory.
         */
        SolveInfo info = null;
        try {
            if (args.length > 1) {
                info = engine.solve(args[1]);
            } else {
                info = engine.solve(PrologHelloWorld.DEFAULT_GOAL);
            }
        } catch (final MalformedGoalException e) {
            Logger.getLogger(PrologHelloWorld.ME).log(Level.SEVERE,
                    "MalformedGoalException");
            System.exit(-1);
        }
        try {
            while (info.hasOpenAlternatives()) {
                Logger.getLogger(PrologHelloWorld.ME).log(Level.INFO,
                        info.getSolution().toString());
                engine.solveNext();
            }
        } catch (final NoSolutionException e) {
            Logger.getLogger(PrologHelloWorld.ME).log(Level.INFO,
                    "Unsatisfiable goal.");
        } catch (final NoMoreSolutionException e) {
            Logger.getLogger(PrologHelloWorld.ME).log(Level.INFO,
                    "No more solutions to explore.");
        }
    }

    private static String fileToString(final String path) throws IOException {
        final InputStream in = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream(path);
        if (in == null) {
            Logger.getLogger(PrologHelloWorld.ME).log(Level.SEVERE,
                    "No input stream found.");
            System.exit(-1);
        }
        try (final BufferedInputStream br = new BufferedInputStream(in);) {
            final byte[] res = new byte[br.available()];
            br.read(res);
            return new String(res);
        }
    }

    private PrologHelloWorld() {
        super();
    }
}
