package alice.tuple.logic;

import alice.tuple.TupleTemplate;
import alice.tuprolog.Term;

/**
 * Interface for Logic Tuples
 *
 * @author Enrico Siboni
 */
public interface LogicTuple extends TupleTemplate {
    /**
     * Gets a argument inside the logic tuple
     *
     * @param index the position (index) of the argument
     * @return the tuple argument if it exists, <code>null</code> otherwise
     */
    TupleArgument getArg(final int index);

    /**
     * Gets an argument (typically a structured value) given its name
     *
     * @param name name of the argument
     * @return the argument (a structured Value) or null if not present
     */
    TupleArgument getArg(final String name);

    /**
     * Gets the number of arguments of this argument supposed to be a structure
     *
     * @return the number of arguments
     */
    int getArity();

    /**
     * Gets the name of the logic tuple
     *
     * @return the name of the logic tuple
     */
    String getName();

    /**
     * Return a string that is the name and arity of the logic tuple in the
     * following format: {@code name/arity}. The method is applicable only if
     * this term is a structure.
     *
     * @return a {@code String} in the form nome/arity.
     */
    String getPredicateIndicator();

    /**
     * Gets the argument linked to a variable inside the tuple.
     * <p>
     * Note that this method works only after that the logic tuple has been
     * resolved, that is the tuple has been subject of matching or unification
     * with an other tuple: typically this method is used with a tuple used in
     * or retrieved by a coordination primitive.
     *
     * @param varName is the name of the variable
     * @return the value linked to the variable, in the case that the variable
     * exits inside the tuple, null otherwise
     */
    TupleArgument getVarValue(final String varName);

    /**
     * Gets the Term representation of the logic tuple
     *
     * @return the logictuple as a term
     */
    Term toTerm();
}
