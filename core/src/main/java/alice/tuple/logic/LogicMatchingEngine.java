/**
 * LogicMatchingEngine.java
 */
package alice.tuple.logic;

import alice.tuprolog.Prolog;
import alice.tuprolog.Term;

/**
 * Utility class for matching or unifying log tuples
 *
 * @author ste (mailto: s.mariani@unibo.it) on 07/gen/2014 ciccis
 */
public final class LogicMatchingEngine {

    private static final Prolog MATCHING_ENGINE = new Prolog();

    /**
     * Tests if the given arguments matches according to tuProlog matching rules
     * for LogicTuples
     * <p>
     * No unification (a la tuProlog) is performed
     *
     * @param tupleA the LogicTuple to match
     * @param tupleB the LogicTuple to match
     * @return wether the given LogicTuples match (according to tuProlog
     * matching rules for LogicTuples)
     */
    public static boolean match(final LogicTuple tupleA, final LogicTuple tupleB) {
        final Term a = tupleA.toTerm().getTerm();
        final Term b = tupleB.toTerm().getTerm();
        return a.match(b);
    }

    /**
     * Tries to perform unification (a la tuProlog)
     *
     * @param tupleA the LogicTuple to match
     * @param tupleB the LogicTuple to match
     * @return wether the given LogicTuples match (according to tuProlog
     * matching rules for LogicTuples)
     */
    public static boolean propagate(final LogicTuple tupleA,
                                    final LogicTuple tupleB) {
        final Term a = tupleA.toTerm().getTerm();
        final Term b = tupleB.toTerm().getTerm();
        return a.unify(LogicMatchingEngine.MATCHING_ENGINE, b);
    }

    private LogicMatchingEngine() {
    }
}
