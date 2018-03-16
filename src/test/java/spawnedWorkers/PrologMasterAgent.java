/**
 * PrologMasterAgent.java
 */
package spawnedWorkers;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import alice.tuplecentre.tucson.service.TucsonInfo;
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
 * @author ste (mailto: s.mariani@unibo.it) on 26/lug/2013
 *
 */
public final class PrologMasterAgent {

    /*
     * Remember the dot!
     */
    private static final String DEFAULT_GOAL = "runMasterAgent(master-agent, [default@localhost:" + TucsonInfo.getDefaultPortNumber() + "], 10, 5).";
    /*
     * The necessary Tucson2PLibrary...
     */
    private static final String DEFAULT_LIBRARY_NAME = "Tucson2PLibrary";
    /*
     * ...can be found within TuCSoN .jar
     */
    private static final String DEFAULT_LIBRARY_PATH = "./";
    /*
     * Relative path w.r.t. to running location (project root).
     */
    private static final String DEFAULT_THEORY_PATH = "spawnedWorkers/masterAgent.pl";
    private static final String ME = "PrologMasterAgent";

    /**
     * @param args
     *            theory to set eand goal to run (ptionals)
     */
    public static void main(final String[] args) {
        /*
         * 1) Get the Prolog program (theory) to run.
         */
        String sTheory = null;
        try {
            if (args.length > 0) {
                sTheory = PrologMasterAgent.fileToString(args[0]);
            } else {
                sTheory = PrologMasterAgent
                        .fileToString(PrologMasterAgent.DEFAULT_THEORY_PATH);
            }
        } catch (final IOException e) {
            Logger.getLogger(PrologMasterAgent.ME).log(Level.SEVERE,
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
            engine.loadLibrary(PrologMasterAgent.DEFAULT_LIBRARY_NAME,
                    new String[] { PrologMasterAgent.DEFAULT_LIBRARY_PATH });
        } catch (final InvalidLibraryException e) {
            Logger.getLogger(PrologMasterAgent.ME).log(Level.SEVERE,
                    "InvalidLibraryException");
            System.exit(-1);
        }
        /*
         * 3) Set the theory into the engine.
         */
        try {
            engine.setTheory(new Theory(sTheory));
        } catch (final InvalidTheoryException e) {
            Logger.getLogger(PrologMasterAgent.ME).log(Level.SEVERE,
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
                info = engine.solve(PrologMasterAgent.DEFAULT_GOAL);
            }
        } catch (final MalformedGoalException e) {
            Logger.getLogger(PrologMasterAgent.ME).log(Level.SEVERE,
                    "MalformedGoalException");
            System.exit(-1);
        }
        try {
            while (info.hasOpenAlternatives()) {
                Logger.getLogger(PrologMasterAgent.ME).log(Level.INFO,
                        info.getSolution().toString());
                engine.solveNext();
            }
        } catch (final NoSolutionException e) {
            Logger.getLogger(PrologMasterAgent.ME).log(Level.INFO,
                    "No solution found.");
        } catch (final NoMoreSolutionException e) {
            Logger.getLogger(PrologMasterAgent.ME).log(Level.INFO,
                    "No more solutions to explore.");
        }
    }

    private static String fileToString(final String path) throws IOException {
        final InputStream in = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream(path);
        if (in == null) {
            Logger.getLogger(PrologMasterAgent.ME).log(Level.SEVERE,
                    "No input stream found.");
            System.exit(-1);
        }
        try (final BufferedInputStream br = new BufferedInputStream(in);) {
            final byte[] res = new byte[br.available()];
            br.read(res);
            return new String(res);
        }
    }

    private PrologMasterAgent() {
        /*
         *
         */
    }
}
