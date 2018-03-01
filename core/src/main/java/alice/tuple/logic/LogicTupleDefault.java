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

import java.io.Serializable;

import alice.tuple.Tuple;
import alice.tuple.TupleTemplate;
import alice.tuprolog.Term;

/**
 * Class representing a logic tuple.
 *
 * @author Alessandro Ricci
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 * @author (contributor) Saverio Cicora
 * @see TupleArgumentDefault
 * @see Tuple
 * @see TupleTemplate
 */
class LogicTupleDefault implements LogicTuple, Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * the information content of logic tuple
     */
    private TupleArgument info;

    /**
     * Constructs a logic tuple providing the tuple name and argument list
     *
     * @param name the name of the tuple (the functor)
     * @param list the list of tuple argument
     */
    LogicTupleDefault(final String name, final TupleArgument[] list) {
        this.info = TupleArguments.newValueArgument(name, list);
    }

    /**
     * Constructs the logic tuple from a tuprolog term
     *
     * @param term the tuprolog term
     */
    LogicTupleDefault(final Term term) {
        this.info = TupleArguments.newInstance(term);
    }

    /**
     * Constructs the logic tuple from a tuple argument (free form of
     * construction)
     *
     * @param t the tuple argument
     */
    LogicTupleDefault(final TupleArgument t) {
        this.info = t;
    }

    @Override
    public TupleArgument getArg(final int index) {
        return this.info.getArg(index);
    }

    @Override
    public TupleArgument getArg(final String name) {
        return this.info.getArg(name);
    }

    @Override
    public int getArity() {
        return this.info.getArity();
    }

    @Override
    public String getName() {
        return this.info.getName();
    }

    @Override
    public String getPredicateIndicator() {
        return this.info.getPredicateIndicator();
    }

    @Override
    public TupleArgument getVarValue(final String varName) {
        return this.info.getVarValue(varName);
    }

    @Override
    public boolean match(final Tuple t) {
        final LogicTuple tu = (LogicTuple) t;
        return LogicMatchingEngine.match(this, tu);
    }

    @Override
    public boolean propagate(final Tuple t) {
        final LogicTuple tu = (LogicTuple) t;
        return LogicMatchingEngine.propagate(this, tu);
    }

    /**
     * Gets the string representation of the logic tuple
     *
     * @return the string representing the logic tuple
     */
    @Override
    public String toString() {
        return this.info.toString();
    }

    @Override
    public Term toTerm() {
        return this.info.toTerm();
    }
}
