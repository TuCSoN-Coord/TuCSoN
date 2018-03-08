package alice.tuple.logic;

import java.util.Collection;

import alice.tuple.logic.exceptions.InvalidTupleArgumentException;
import alice.tuple.logic.exceptions.InvalidVarNameException;
import alice.tuprolog.InvalidTermException;
import alice.tuprolog.Term;

/**
 * Factory class for Logic Tuple Arguments
 *
 * @author Enrico Siboni
 */
public final class TupleArguments {

    private TupleArguments() {
    }

    /**
     * Static method to get a Tuple Argument from a textual representation
     *
     * @param st the text representing the tuple argument
     * @return the tuple argument interpreted from the text
     * @throws InvalidTupleArgumentException if the text does not represent a valid tuple argument
     */
    public static TupleArgument parse(final String st)
            throws InvalidTupleArgumentException {
        try {
            final Term t = Term.createTerm(st);
            return new TupleArgumentDefault(t);
        } catch (final InvalidTermException ex) {
            throw new InvalidTupleArgumentException(
                    "Exception occurred while parsing the string:\"" + st
                            + "\"", ex);
        }
    }

    /**
     * Contructs a tuple argument copying a tuProlog term
     *
     * @param t the Prolog term whose content is used to build the argument
     */
    public static TupleArgument newInstance(final Term t) {
        return new TupleArgumentDefault(t);
    }

    /**
     * Constructs an anonymous variable tuple argument
     */
    public static TupleArgument newVarArgument() {
        return new VarArgument();
    }

    /**
     * Construct a variable tuple argument identified with a name
     *
     * @param name the name of the variable, which must start with an upper case
     *             letter or the underscore
     * @throws InvalidVarNameException if the text does not represent a valid Var name
     */
    public static TupleArgument newVarArgument(final String name) throws InvalidVarNameException {
        return new VarArgument(name);
    }

    /**
     * Constructs a simple double tuple argument
     *
     * @param value the double value to initialize this argument
     */
    public static TupleArgument newValueArgument(final double value) {
        return new ValueArgument(value);
    }

    /**
     * Constructs a simple float tuple argument
     *
     * @param value the float value to initialize this argument
     */
    public static TupleArgument newValueArgument(final float value) {
        return new ValueArgument(value);
    }

    /**
     * Constructs a simple integer tuple argument
     *
     * @param value the integer value to initialize this argument
     */
    public static TupleArgument newValueArgument(final int value) {
        return new ValueArgument(value);
    }

    /**
     * Constructs a simple long tuple argument
     *
     * @param value the long value to initialize this argument
     */
    public static TupleArgument newValueArgument(final long value) {
        return new ValueArgument(value);
    }

    /**
     * Constructs a simple String tuple argument
     *
     * @param value the String value to initialize this argument
     */
    public static TupleArgument newValueArgument(final String value) {
        return new ValueArgument(value);
    }

    /**
     * Constructs a structured (compound) argument, made of a string as a name
     * (functor) and list of arguments
     *
     * @param functor the name of the structure
     * @param args    the list of the arguments (or nothing)
     */
    public static TupleArgument newValueArgument(final String functor, final TupleArgument... args) {
        return new ValueArgument(functor, args);
    }

    /**
     * Constructs a structured (compound) argument, made of a string as a name
     * (functor) and list of arguments
     *
     * @param functor the name of the structure
     * @param args    the list of the arguments (or nothing)
     */
    public static <T extends Collection<? extends TupleArgument>> TupleArgument newValueArgument(final String functor, final T args) {
        return new ValueArgument(functor, args.toArray(new TupleArgument[args.size()]));
    }

    /**
     * Constructs a structured (compound) argument as a logic list
     *
     * @param argList the list of the arguments
     */
    public static TupleArgument newValueArgument(final TupleArgument... argList) {
        return new ValueArgument(argList);
    }

    /**
     * Constructs a structured (compound) argument as a logic list
     *
     * @param argList the list of the arguments
     */
    public static <T extends Collection<? extends TupleArgument>> TupleArgument newValueArgument(final T argList) {
        return new ValueArgument(argList.toArray(new TupleArgument[argList.size()]));
    }

}
