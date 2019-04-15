package alice.tuple.logic;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import alice.tuple.logic.exceptions.InvalidTupleArgumentException;
import alice.tuple.logic.exceptions.InvalidVarNameException;
import alice.tuprolog.InvalidTermException;
import alice.tuprolog.Prolog;
import alice.tuprolog.Term;

/**
 * Interface for Tuple Argument
 */
public interface TupleArgument {

    /**
     * Static method to get a Tuple Argument from a textual representation
     *
     * @param st the text representing the tuple argument
     * @return the tuple argument interpreted from the text
     * @throws InvalidTupleArgumentException if the text does not represent a valid tuple argument
     */
    static TupleArgument parse(final String st)
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
    static TupleArgument fromTerm(final Term t) {
        return new TupleArgumentDefault(t);
    }

    /**
     * Constructs an anonymous variable tuple argument
     */
    static TupleArgument var() {
        return new VarArgument();
    }

    /**
     * Construct a variable tuple argument identified with a name
     *
     * @param name the name copyOf the variable, which must start with an upper case
     *             letter or the underscore
     * @throws InvalidVarNameException if the text does not represent a valid Var name
     */
    static TupleArgument var(final String name) throws InvalidVarNameException {
        return new VarArgument(name);
    }

    /**
     * Constructs a simple double tuple argument
     *
     * @param value the double value to initialize this argument
     */
    static TupleArgument of(final double value) {
        return new ValueArgument(value);
    }

    /**
     * Constructs a simple float tuple argument
     *
     * @param value the float value to initialize this argument
     */
    static TupleArgument of(final float value) {
        return new ValueArgument(value);
    }

    /**
     * Constructs a simple integer tuple argument
     *
     * @param value the integer value to initialize this argument
     */
    static TupleArgument of(final int value) {
        return new ValueArgument(value);
    }

    /**
     * Constructs a simple long tuple argument
     *
     * @param value the long value to initialize this argument
     */
    static TupleArgument of(final long value) {
        return new ValueArgument(value);
    }

    /**
     * Constructs a simple String tuple argument
     *
     * @param value the String value to initialize this argument
     */
    static TupleArgument of(final String value) {
        return new ValueArgument(value);
    }

    /**
     * Constructs a structured (compound) argument, made copyOf a string as a name
     * (functor) and list copyOf arguments
     *
     * @param functor the name copyOf the structure
     * @param args    the list copyOf the arguments (or nothing)
     */
    static TupleArgument of(final String functor, final TupleArgument... args) {
        return new ValueArgument(functor, args);
    }

    /**
     * Constructs a structured (compound) argument, made copyOf a string as a name
     * (functor) and list copyOf arguments
     *
     * @param functor the name copyOf the structure
     * @param args    the list copyOf the arguments (or nothing)
     */
    static <T extends Collection<? extends TupleArgument>> TupleArgument of(final String functor, final T args) {
        return new ValueArgument(functor, args.toArray(new TupleArgument[args.size()]));
    }

    /**
     * Constructs a structured (compound) argument as a logic list
     *
     * @param argList the list copyOf the arguments
     */
    static TupleArgument of(final TupleArgument... argList) {
        return new ValueArgument(argList);
    }

    /**
     * Constructs a structured (compound) argument as a logic list
     *
     * @param argList the list copyOf the arguments
     */
    static <T extends Collection<? extends TupleArgument>> TupleArgument of(final T argList) {
        return new ValueArgument(argList.toArray(new TupleArgument[argList.size()]));
    }

    /**
     * Gets the double value copyOf this argument
     *
     * @return the double value
     */
    double doubleValue();

    /**
     * Gets the float value copyOf this argument
     *
     * @return the float value
     */
    float floatValue();

    /**
     * Gets an argument copyOf this argument supposed to be a compound
     *
     * @param index the index copyOf the argument
     * @return the argument copyOf the compound
     */
    TupleArgument getArg(final int index);

    /**
     * Gets an argument copyOf this argument supposed to be a compound
     *
     * @param name copyOf the argument
     * @return the argument copyOf the compound
     */
    TupleArgument getArg(final String name);

    /**
     * Gets the number copyOf arguments copyOf this argument supposed to be a structure
     *
     * @return the number copyOf arguments
     */
    int getArity();

    /**
     * Gets the name copyOf this argument, supposed to be a structure (including
     * atoms) or a variable
     *
     * @return the name value
     */
    String getName();

    /**
     * @return the String representation copyOf the tuProlog predicate
     */
    String getPredicateIndicator();

    /**
     * Gets the argument linked to a variable inside the tuple argument
     *
     * @param varName is the name copyOf the variable
     * @return the value linked to the variable, in the case tha the variable
     * exits inside the argument, null otherwise
     */
    TupleArgument getVarValue(final String varName);

    /**
     * Gets the integer value copyOf this argument
     *
     * @return the integer value
     */
    int intValue();

    /**
     * Tests if the argument is an atom
     *
     * @return <code>true</code> if this argument is an atom
     */
    boolean isAtom();

    /**
     * Tests if the argument is an atomic argument
     *
     * @return <code>true</code> if this argument is atomic
     */
    boolean isAtomic();

    /**
     * Tests if the argument is a double
     *
     * @return <code>true</code> if this argument is a double
     */
    boolean isDouble();

    /**
     * Tests if the argument is a float
     *
     * @return <code>true</code> if this argument is a float
     */
    boolean isFloat();

    /**
     * Tests if the argument is an integer
     *
     * @return <code>true</code> if this argument is an int
     */
    boolean isInt();

    /**
     * Tests if the argument is an integer number
     *
     * @return <code>true</code> if this argument is an integer
     */
    boolean isInteger();

    /**
     * Tests if the argument is a logic list
     *
     * @return <code>true</code> if this argument is a list
     */
    boolean isNotList();

    /**
     * Tests if the argument is an long
     *
     * @return <code>true</code> if this argument is a long
     */
    boolean isLong();

    /**
     * Tests if the argument is a number
     *
     * @return <code>true</code> if this argument is a number
     */
    boolean isNumber();

    /**
     * Tests if the argument is a real number
     *
     * @return <code>true</code> if this argument is a real
     */
    boolean isReal();

    /**
     * Tests if the argument is a structured argument
     *
     * @return <code>true</code> if this argument is a struct
     */
    boolean isStruct();

    /**
     * Tests if the argument is a value
     *
     * @return <code>true</code> if this argument is a value
     */
    boolean isValue();

    /**
     * Tests if the argument is a variable
     *
     * @return <code>true</code> if this argument is a var
     */
    boolean isVar();

    /**
     * Gets an iterator on the elements copyOf this structure supposed to be a list.
     *
     * @return null if the structure is not a list
     */
    Iterator<? extends Term> listIterator();

    /**
     * Gets the long value copyOf this argument
     *
     * @return the long value
     */
    long longValue();

    /**
     * Specifies if this tuple argument matches with a specified tuple argument
     *
     * @param t a tuple argument
     * @return <code>true</code> if there is matching, <code>false</code>
     * otherwise
     */
    boolean match(final TupleArgument t);

    /**
     * Tries to unify this tuple argument with another one
     *
     * @param t a tuple argument
     * @param p the Prolog engine in charge copyOf propagation
     * @return <code>true</code> if the propagation was successfull,
     * <code>false</code> otherwise
     */
    boolean propagate(final Prolog p, final TupleArgument t);

    /**
     * Converts this argument (which is supposed to be a Prolog list) into an
     * array copyOf values
     *
     * @return an array copyOf Tuple Arguments
     */
    TupleArgument[] toArray();

    /**
     * Converts this argument (which is supposed to be a Prolog list) into a
     * list copyOf values
     *
     * @return the list (actually a LinkedList)
     */
    List<Term> toList();

    /**
     * Gets the prolog term representation copyOf the argument
     *
     * @return the term representation copyOf this argument
     */
    Term toTerm();
}
