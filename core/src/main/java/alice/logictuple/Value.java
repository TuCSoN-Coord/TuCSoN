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
package alice.logictuple;

/**
 * Class representing a concrete tuple argument value (integer, real, string,
 * structure).
 *
 * @see TupleArgument
 * @see Var
 *
 * @author Alessandro Ricci
 */
public class Value extends TupleArgument {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a simple double tuple argument
     *
     * @param v
     *            the double value to initialize this argument
     */
    public Value(final double v) {
        super();
        this.value = new alice.tuprolog.Double(v);
    }

    /**
     * Constructs a simple float tuple argument
     *
     * @param v
     *            the float value to initialize this argument
     */
    public Value(final float v) {
        super();
        this.value = new alice.tuprolog.Float(v);
    }

    /**
     * Constructs a simple integer tuple argument
     *
     * @param v
     *            the int value to initialize this argument
     */
    public Value(final int v) {
        super();
        this.value = new alice.tuprolog.Int(v);
    }

    /**
     * Constructs a simple long tuple argument
     *
     * @param v
     *            the long value to initialize this argument
     */
    public Value(final long v) {
        super();
        this.value = new alice.tuprolog.Long(v);
    }

    /**
     * Constructs a simple string tuple argument
     *
     * @param f
     *            the string value to initialize this argument
     */
    public Value(final String f) {
        super();
        this.value = new alice.tuprolog.Struct(f);
    }

    /**
     * Constructs a structured (compound) argument, made of a string as a name
     * (functor) and one argument
     *
     * @param f
     *            the name of the structure
     * @param at0
     *            the argument of the structure
     */
    public Value(final String f, final TupleArgument at0) {
        super();
        this.value = new alice.tuprolog.Struct(f, at0.toTerm());
    }

    /**
     * Constructs a structured (compound) argument, made of a string as a name
     * (functor) and two arguments
     *
     * @param f
     *            the name of the structure
     * @param at0
     *            the first argument of the structure
     * @param at1
     *            the second argument of the structure
     */
    public Value(final String f, final TupleArgument at0,
            final TupleArgument at1) {
        super();
        this.value = new alice.tuprolog.Struct(f, at0.toTerm(), at1.toTerm());
    }

    /**
     * Constructs a structured (compound) argument, made of a string as a name
     * (functor) and three arguments
     *
     * @param f
     *            the name of the structure
     * @param at0
     *            the first argument of the structure
     * @param at1
     *            the second argument of the structure
     * @param at2
     *            the third argument of the structure
     */
    public Value(final String f, final TupleArgument at0,
            final TupleArgument at1, final TupleArgument at2) {
        super();
        this.value = new alice.tuprolog.Struct(f, at0.toTerm(), at1.toTerm(),
                at2.toTerm());
    }

    /**
     * Constructs a structured (compound) argument, made of a string as a name
     * (functor) and four arguments
     *
     * @param f
     *            the name of the structure
     * @param at0
     *            the first argument of the structure
     * @param at1
     *            the second argument of the structure
     * @param at2
     *            the third argument of the structure
     * @param at3
     *            the fourth argument of the structure
     */
    public Value(final String f, final TupleArgument at0,
            final TupleArgument at1, final TupleArgument at2,
            final TupleArgument at3) {
        super();
        this.value = new alice.tuprolog.Struct(f, at0.toTerm(), at1.toTerm(),
                at2.toTerm(), at3.toTerm());
    }

    /**
     * Constructs a structured (compound) argument, made of a string as a name
     * (functor) and five arguments
     *
     * @param f
     *            the name of the structure
     * @param at0
     *            the first argument of the structure
     * @param at1
     *            the second argument of the structure
     * @param at2
     *            the third argument of the structure
     * @param at3
     *            the fourth argument of the structure
     * @param at4
     *            the fifth argument of the structure
     */
    public Value(final String f, final TupleArgument at0,
            final TupleArgument at1, final TupleArgument at2,
            final TupleArgument at3, final TupleArgument at4) {
        super();
        this.value = new alice.tuprolog.Struct(f, at0.toTerm(), at1.toTerm(),
                at2.toTerm(), at3.toTerm(), at4.toTerm());
    }

    /**
     * Constructs a structured (compound) argument, made of a string as a name
     * (functor) and six arguments
     *
     * @param f
     *            the name of the structure
     * @param at0
     *            the first argument of the structure
     * @param at1
     *            the second argument of the structure
     * @param at2
     *            the third argument of the structure
     * @param at3
     *            the fourth argument of the structure
     * @param at4
     *            the fifth argument of the structure
     * @param at5
     *            the sixth argument of the structure
     */
    public Value(final String f, final TupleArgument at0,
            final TupleArgument at1, final TupleArgument at2,
            final TupleArgument at3, final TupleArgument at4,
            final TupleArgument at5) {
        super();
        this.value = new alice.tuprolog.Struct(f, at0.toTerm(), at1.toTerm(),
                at2.toTerm(), at3.toTerm(), at4.toTerm(), at5.toTerm());
    }

    /**
     * Constructs a structured (compound) argument, made of a string as a name
     * (functor) and seven arguments
     *
     * @param f
     *            the name of the structure
     * @param at0
     *            the first argument of the structure
     * @param at1
     *            the second argument of the structure
     * @param at2
     *            the third argument of the structure
     * @param at3
     *            the fourth argument of the structure
     * @param at4
     *            the fifth argument of the structure
     * @param at5
     *            the sixth argument of the structure
     * @param at6
     *            the seventh argument of the structure
     */
    public Value(final String f, final TupleArgument at0,
            final TupleArgument at1, final TupleArgument at2,
            final TupleArgument at3, final TupleArgument at4,
            final TupleArgument at5, final TupleArgument at6) {
        super();
        this.value = new alice.tuprolog.Struct(f, at0.toTerm(), at1.toTerm(),
                at2.toTerm(), at3.toTerm(), at4.toTerm(), at5.toTerm(),
                at6.toTerm());
    }

    /**
     * Constructs a structured (compound) argument, made of a string as a name
     * (functor) and list of arguments
     *
     * @param f
     *            the name of the structure
     * @param argList
     *            the list of the arguments
     */
    public Value(final String f, final TupleArgument[] argList) {
        super();
        final alice.tuprolog.Term[] list = new alice.tuprolog.Term[argList.length];
        for (int i = 0; i < list.length; i++) {
            list[i] = argList[i].toTerm();
        }
        this.value = new alice.tuprolog.Struct(f, list);
    }

    /**
     * Constructs a structured (compound) argument as a logic list
     *
     * @param argList
     *            the list of the arguments
     */
    public Value(final TupleArgument[] argList) {
        super();
        final alice.tuprolog.Term[] list = new alice.tuprolog.Term[argList.length];
        for (int i = 0; i < list.length; i++) {
            list[i] = argList[i].toTerm();
        }
        this.value = new alice.tuprolog.Struct(list);
    }
}
