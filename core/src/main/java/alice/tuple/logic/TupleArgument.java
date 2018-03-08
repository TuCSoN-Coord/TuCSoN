package alice.tuple.logic;

import java.util.Iterator;
import java.util.List;

import alice.tuprolog.Prolog;
import alice.tuprolog.Term;

/**
 * Interface for Tuple Argument
 */
public interface TupleArgument {

    /**
     * Gets the double value of this argument
     *
     * @return the double value
     */
    double doubleValue();

    /**
     * Gets the float value of this argument
     *
     * @return the float value
     */
    float floatValue();

    /**
     * Gets an argument of this argument supposed to be a compound
     *
     * @param index the index of the argument
     * @return the argument of the compound
     */
    TupleArgument getArg(final int index);

    /**
     * Gets an argument of this argument supposed to be a compound
     *
     * @param name of the argument
     * @return the argument of the compound
     */
    TupleArgument getArg(final String name);

    /**
     * Gets the number of arguments of this argument supposed to be a structure
     *
     * @return the number of arguments
     */
    int getArity();

    /**
     * Gets the name of this argument, supposed to be a structure (including
     * atoms) or a variable
     *
     * @return the name value
     */
    String getName();

    /**
     * @return the String representation of the tuProlog predicate
     */
    String getPredicateIndicator();

    /**
     * Gets the argument linked to a variable inside the tuple argument
     *
     * @param varName is the name of the variable
     * @return the value linked to the variable, in the case tha the variable
     * exits inside the argument, null otherwise
     */
    TupleArgument getVarValue(final String varName);

    /**
     * Gets the integer value of this argument
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
    boolean isList();

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
     * Gets an iterator on the elements of this structure supposed to be a list.
     *
     * @return null if the structure is not a list
     */
    Iterator<? extends Term> listIterator();

    /**
     * Gets the long value of this argument
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
     * @param p the Prolog engine in charge of propagation
     * @return <code>true</code> if the propagation was successfull,
     * <code>false</code> otherwise
     */
    boolean propagate(final Prolog p, final TupleArgument t);

    /**
     * Converts this argument (which is supposed to be a Prolog list) into an
     * array of values
     *
     * @return an array of Tuple Arguments
     */
    TupleArgument[] toArray();

    /**
     * Converts this argument (which is supposed to be a Prolog list) into a
     * list of values
     *
     * @return the list (actually a LinkedList)
     */
    List<Term> toList();

    /**
     * Gets the prolog term representation of the argument
     *
     * @return the term representation of this argument
     */
    Term toTerm();
}
