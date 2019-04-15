package alice.tuple.logic;

import alice.tuple.TupleTemplate;
import alice.tuple.logic.exceptions.InvalidLogicTupleException;
import alice.tuprolog.InvalidTermException;
import alice.tuprolog.Term;

import java.util.Collection;

/**
 * Interface for Logic Tuples
 *
 * @author Enrico Siboni
 */
public interface LogicTuple extends TupleTemplate {
    /**
     * Static method to get a LogicTuple from a textual representation
     *
     * @param tupleString the text representing the tuple
     * @return the logic tuple interpreted from the text
     * @throws InvalidLogicTupleException if the text does not represent a valid logic tuple
     */
    static LogicTuple parse(final String tupleString) throws InvalidLogicTupleException {
        try {
            final Term t = Term.createTerm(tupleString,
                    new LogicTupleOpManager());
            return of(TupleArgument.fromTerm(t));
        } catch (final InvalidTermException ex) {
            throw new InvalidLogicTupleException(
                    "Exception occurred while parsing the string: \"" + tupleString
                            + "\"", ex);
        }
    }

    /**
     * Static method for copying a LogicTuple
     *
     * @param logicTuple the logicTuple to copy
     * @return the copy
     */
    static LogicTuple copyOf(final LogicTuple logicTuple) {
        try {
            return parse(logicTuple.toString());
        } catch (final InvalidLogicTupleException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Constructs a logic tuple providing the tuple name and argument list (or nothing for a tuple without arguments)
     *
     * @param name the name copyOf the tuple (the functor)
     * @param args the list copyOf tuple arguments
     * @return the newly created LogicTuple
     */
    static LogicTuple of(final String name, final TupleArgument... args) {
        return new LogicTupleDefault(name, args);
    }

    /**
     * Constructs a logic tuple providing the tuple name and argument list
     *
     * @param name the name copyOf the tuple (the functor)
     * @param args the list copyOf tuple arguments
     * @return the newly created LogicTuple
     */
    static <T extends Collection<? extends TupleArgument>> LogicTuple of(final String name, final T args) {
        return new LogicTupleDefault(name, args.toArray(new TupleArgument[args.size()]));
    }

    /**
     * Constructs the logic tuple from a tuprolog term
     *
     * @param term the tuprolog term
     */
    static LogicTuple fromTerm(final Term term) {
        return new LogicTupleDefault(term);
    }

    /**
     * Constructs the logic tuple from a tuple argument (free form copyOf
     * construction)
     *
     * @param tupleArg the tuple argument
     */
    static LogicTuple of(final TupleArgument tupleArg) {
        return new LogicTupleDefault(tupleArg);
    }

    /**
     * Gets a argument inside the logic tuple
     *
     * @param index the position (index) copyOf the argument
     * @return the tuple argument if it exists, <code>null</code> otherwise
     */
    TupleArgument getArg(final int index);

    /**
     * Gets an argument (typically a structured value) given its name
     *
     * @param name name copyOf the argument
     * @return the argument (a structured Value) or null if not present
     */
    TupleArgument getArg(final String name);

    /**
     * Gets the number copyOf arguments copyOf this argument supposed to be a structure
     *
     * @return the number copyOf arguments
     */
    int getArity();

    /**
     * Gets the name copyOf the logic tuple
     *
     * @return the name copyOf the logic tuple
     */
    String getName();

    /**
     * Return a string that is the name and arity copyOf the logic tuple in the
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
     * resolved, that is the tuple has been subject copyOf matching or unification
     * with an other tuple: typically this method is used with a tuple used in
     * or retrieved by a coordination primitive.
     *
     * @param varName is the name copyOf the variable
     * @return the value linked to the variable, in the case that the variable
     * exits inside the tuple, null otherwise
     */
    TupleArgument getVarValue(final String varName);

    /**
     * Gets the Term representation copyOf the logic tuple
     *
     * @return the logictuple as a term
     */
    Term toTerm();
}
