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

/**
 * Class representing a concrete tuple argument value (integer, real, string, structure).
 *
 * @author Alessandro Ricci
 * @see TupleArgumentDefault
 * @see VarArgument
 */
class ValueArgument extends TupleArgumentDefault {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a simple double tuple argument
     *
     * @param v the double value to initialize this argument
     */
    ValueArgument(final double v) {
        super(new alice.tuprolog.Double(v));
    }

    /**
     * Constructs a simple float tuple argument
     *
     * @param v the float value to initialize this argument
     */
    ValueArgument(final float v) {
        super(new alice.tuprolog.Float(v));
    }

    /**
     * Constructs a simple integer tuple argument
     *
     * @param v the int value to initialize this argument
     */
    ValueArgument(final int v) {
        super(new alice.tuprolog.Int(v));
    }

    /**
     * Constructs a simple long tuple argument
     *
     * @param v the long value to initialize this argument
     */
    ValueArgument(final long v) {
        super(new alice.tuprolog.Long(v));
    }

    /**
     * Constructs a simple String tuple argument
     *
     * @param v the String value to initialize this argument
     */
    ValueArgument(final String v) {
        super(new alice.tuprolog.Struct(v));
    }

    /**
     * Constructs a structured (compound) argument, made of a string as a name
     * (functor) and list of arguments
     *
     * @param f       the name of the structure
     * @param argList the list of the arguments (or nothing for a simple String argument)
     */
    ValueArgument(final String f, final TupleArgument[] argList) {
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
     * @param argList the list of the arguments
     */
    ValueArgument(final TupleArgument[] argList) {
        super();
        final alice.tuprolog.Term[] list = new alice.tuprolog.Term[argList.length];
        for (int i = 0; i < list.length; i++) {
            list[i] = argList[i].toTerm();
        }
        this.value = new alice.tuprolog.Struct(list);
    }
}
