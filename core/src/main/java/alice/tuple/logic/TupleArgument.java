/*
 * Logic Tuple Communication Language - Copyright (C) 2001-2002 aliCE team at
 * deis.unibo.it This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of the License,
 * or (at your option) any later version. This library is distributed in the
 * hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU Lesser General Public License for more details. You should have
 * received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */
package alice.tuple.logic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import alice.tuple.logic.exceptions.InvalidTupleArgumentException;
import alice.tuplecentre.api.exceptions.InvalidOperationException;
import alice.tuprolog.InvalidTermException;
import alice.tuprolog.Number;
import alice.tuprolog.Prolog;
import alice.tuprolog.Struct;
import alice.tuprolog.Term;

/**
 * Base class for tuple argument classes.
 *
 * @see LogicTupleDefault
 * @see Value
 * @see Var
 *
 * @author Alessandro Ricci
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 * @author (contributor) Saverio Cicora
 */
public class TupleArgument implements java.io.Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * Static service to get a Tuple Argument from a textual representation
     *
     * @param st
     *            the text representing the tuple argument
     * @return the tuple argument interpreted from the text
     * @exception InvalidTupleArgumentException
     *                if the text does not represent a valid tuple argument
     */
    public static TupleArgument parse(final String st)
            throws InvalidTupleArgumentException {
        try {
            final Term t = alice.tuprolog.Term.createTerm(st);
            return new TupleArgument(t);
        } catch (final InvalidTermException ex) {
            throw new InvalidTupleArgumentException(
                    "Exception occurred while parsing the string:\"" + st
                            + "\"", ex);
        }
    }

    /** the internal representation of the argument is a (tu)Prolog term */
    protected Term value;

    /**
     *
     */
    public TupleArgument() {
        /*
         *
         */
    }

    /**
     * Contructs a tuple argument copying a tuProlog term
     *
     * @param t
     *            the Prolog term whose content is used to build the argument
     */
    public TupleArgument(final Term t) {
        this.value = t;
    }

    /**
     * Gets the double value of this argument
     *
     * @return the double value
     */
    public double doubleValue() {
        if (this.isNumber()) {
            return ((Number) this.value).doubleValue();
        }
        throw new InvalidOperationException("The argument is not a Number");
    }

    /**
     * Gets the float value of this argument
     *
     * @return the float value
     */
    public float floatValue() {
        if (this.isNumber()) {
            return ((Number) this.value).floatValue();
        }
        throw new InvalidOperationException("The argument is not a Number");
    }

    /**
     * Gets an argument of this argument supposed to be a compound
     *
     * @param index
     *            the index of the argument
     * @return the argument of the compound
     */
    public TupleArgument getArg(final int index) {
    		return new TupleArgument(((Struct) this.value.getTerm()).getTerm(index));	
    }

    /**
     * Gets an argument of this argument supposed to be a compound
     *
     * @param name
     *            of the argument
     * @return the argument of the compound
     */
    public TupleArgument getArg(final String name) {
        final Struct s = ((Struct) this.value).getArg(name);
        if (s != null) {
            return new TupleArgument(s);
        }
        return null;
    }

    /**
     * Gets the number of arguments of this argument supposed to be a structure
     *
     * @return the number of arguments
     *
     */
    public int getArity() {
        if (this.isStruct()) {
            return ((Struct) this.value).getArity();
        }
        throw new InvalidOperationException("The argument is not a Struct");
    }

    /**
     * Gets the name of this argument, supposed to be a structure (including
     * atoms) or a variable
     *
     * @return the name value
     *
     */
    public String getName() {
        if (this.isStruct()) {
            return ((Struct) this.value).getName();
        } else if (this.isVar()) {
            return ((alice.tuprolog.Var) this.value).getName();
        } else {
            throw new InvalidOperationException(
                    "The argument is not a Struct or a Var");
        }
    }

    /**
     *
     * @return the String representation of the tuProlog predicate
     */
    public String getPredicateIndicator() {
        if (this.isStruct()) {
            // TODO CICORA: oppure return
            // ((Struct)value).getPredicateIndicator();
            return ((alice.tuprolog.Struct) this.value).getName() + "/"
                    + ((alice.tuprolog.Struct) this.value).getArity();
        }
        throw new InvalidOperationException("The argument is not a Struct");
    }

    /**
     * Gets the argument linked to a variable inside the tuple argument
     *
     * @param varName
     *            is the name of the variable
     * @return the value linked to the variable, in the case tha the variable
     *         exits inside the argument, null otherwise
     */
    public TupleArgument getVarValue(final String varName) {
        if (this.value instanceof alice.tuprolog.Var) {
            return new TupleArgument(
                    ((alice.tuprolog.Var) this.value).getTerm());
        } else if (!(this.value instanceof alice.tuprolog.Struct)) {
            return null;
        } else {
            final Term t = this.getVarValue(varName, (Struct) this.value);
            if (t != null) {
                return new TupleArgument(t);
            }
            return null;
        }
    }

    /**
     * Gets the integer value of this argument
     *
     * @return the integer value
     */
    public int intValue() {
        if (this.isNumber()) {
            return ((Number) this.value).intValue();
        }
        throw new InvalidOperationException("The argument is not a Number");
    }

    /**
     * Tests if the argument is an atom
     *
     * @return <code>true</code> if this argument is an atom
     */
    public boolean isAtom() {
        return this.value.isAtom()
                && this.value instanceof alice.tuprolog.Struct;
    }

    /**
     * Tests if the argument is an atomic argument
     *
     * @return <code>true</code> if this argument is atomic
     */
    public boolean isAtomic() {
        return this.value.isAtomic();
    }

    /**
     * Tests if the argument is a double
     *
     * @return <code>true</code> if this argument is a double
     */
    public boolean isDouble() {
        return this.value instanceof alice.tuprolog.Number
                && (Number) this.value instanceof alice.tuprolog.Double;
    }

    /**
     * Tests if the argument is a float
     *
     * @return <code>true</code> if this argument is a float
     */
    public boolean isFloat() {
        return this.value instanceof alice.tuprolog.Number
                && (Number) this.value instanceof alice.tuprolog.Float;
    }

    /**
     * Tests if the argument is an integer
     *
     * @return <code>true</code> if this argument is an int
     */
    public boolean isInt() {
        return this.value instanceof alice.tuprolog.Number
                && (Number) this.value instanceof alice.tuprolog.Int;
    }

    /**
     * Tests if the argument is an integer number
     *
     * @return <code>true</code> if this argument is an integer
     */
    public boolean isInteger() {
        return this.value instanceof alice.tuprolog.Number
                && ((Number) this.value).isInteger();
    }

    /**
     * Tests if the argument is a logic list
     *
     * @return <code>true</code> if this argument is a list
     */
    public boolean isList() {
        return this.value.isList();
    }

    /**
     * Tests if the argument is an long
     *
     * @return <code>true</code> if this argument is a long
     */
    public boolean isLong() {
        return this.value instanceof alice.tuprolog.Number
                && (Number) this.value instanceof alice.tuprolog.Long;
    }

    /**
     * Tests if the argument is a number
     *
     * @return <code>true</code> if this argument is a number
     */
    public boolean isNumber() {
        return this.value instanceof alice.tuprolog.Number;
    }

    /**
     * Tests if the argument is a real number
     *
     * @return <code>true</code> if this argument is a real
     */
    public boolean isReal() {
        return this.value instanceof alice.tuprolog.Number
                && ((Number) this.value).isReal();
    }

    /**
     * Tests if the argument is a structured argument
     *
     * @return <code>true</code> if this argument is a struct
     */
    public boolean isStruct() {
        // return this.value.isCompound(); why?
        return this.value instanceof alice.tuprolog.Struct;
    }

    /**
     * Tests if the argument is a value
     *
     * @return <code>true</code> if this argument is a value
     */
    public boolean isValue() {
        return !(this.value instanceof alice.tuprolog.Var);
    }

    /**
     * Tests if the argument is a variable
     *
     * @return <code>true</code> if this argument is a var
     */
    public boolean isVar() {
        return this.value instanceof alice.tuprolog.Var;
    }

    /**
     * Gets an iterator on the elements of this structure supposed to be a list.
     *
     * @return null if the structure is not a list
     */
    public java.util.Iterator<? extends Term> listIterator() {
        if (this.value.isList()) {
            return ((Struct) this.value).listIterator();
        }
        return null;
    }

    /**
     * Gets the long value of this argument
     *
     * @return the long value
     */
    public long longValue() {
        if (this.isLong()) {
            return ((Number) this.value).longValue();
        }
        throw new InvalidOperationException("The argument is not a Long");
    }

    /**
     * Specifies if this tuple argument matches with a specified tuple argument
     *
     * @param t
     *            a tuple argument
     * @return <code>true</code> if there is matching, <code>false</code>
     *         otherwise
     */
    public boolean match(final TupleArgument t) {
        return this.value.match(t.value);
    }

    /**
     * Tries to unify this tuple argument with another one
     *
     * @param t
     *            a tuple argument
     * @param p
     *            the Prolog engine in charge of propagation
     * @return <code>true</code> if the propagation was successfull,
     *         <code>false</code> otherwise
     */
    public boolean propagate(final Prolog p, final TupleArgument t) {
        return this.value.unify(p, t.value);
    }

    /**
     * Converts this argument (which is supposed to be a Prolog list) into an
     * array of values
     *
     * @return an array of Tuple Arguments
     */
    public TupleArgument[] toArray() {
        if (this.isList()) {
            final ArrayList<Term> list = new ArrayList<Term>();
            final Iterator<? extends Term> it = ((Struct) this.value)
                    .listIterator();
            while (it.hasNext()) {
                list.add(it.next());
            }
            final TupleArgument[] vect = new TupleArgument[list.size()];
            for (int i = 0; i < vect.length; i++) {
                vect[i] = new TupleArgument(list.get(i));
            }
            return vect;
        }
        throw new InvalidOperationException("The argument is not a List");
    }

    /**
     * Converts this argument (which is supposed to be a Prolog list) into a
     * list of values
     *
     * @return the list (actually a LinkedList)
     *
     */
    public List<Term> toList() {
        if (this.isList()) {
            final LinkedList<Term> list = new LinkedList<Term>();
            final Iterator<? extends Term> it = ((Struct) this.value)
                    .listIterator();
            while (it.hasNext()) {
                list.add(it.next());
            }
            return list;
        }
        throw new InvalidOperationException("The argument is not a List");
    }

    /**
     * Gets the string representation of the argument
     *
     * @return the string representation of this argument
     */
    @Override
    public String toString() {
        return this.value.getTerm().toString();
    }

    /**
     * Gets the prolog term representation of the argument
     *
     * @return the term representation of this argument
     */
    public alice.tuprolog.Term toTerm() {
        return this.value;
    }

    private Term getVarValue(final String name, final Struct st) {
        final int len = st.getArity();
        for (int i = 0; i < len; i++) {
            final Term arg = st.getArg(i);
            if (arg instanceof alice.tuprolog.Var) {
                final alice.tuprolog.Var v = (alice.tuprolog.Var) arg;
                if (v.getName().equals(name)) {
                    return v.getTerm();
                }
            } else if (arg instanceof alice.tuprolog.Struct) {
                final Term t = this.getVarValue(name, (Struct) arg);
                if (t != null) {
                    return t;
                }
            }
        }
        return null;
    }
}
