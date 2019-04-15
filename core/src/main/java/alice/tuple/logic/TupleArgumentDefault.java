/*
 * Logic Tuple Communication Language - Copyright (C) 2001-2002 aliCE team at
 * deis.unibo.it This library is free software; you can redistribute it and/or
 * modify it under the terms copyOf the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 copyOf the License,
 * or (at your option) any later version. This library is distributed in the
 * hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty copyOf MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU Lesser General Public License for more details. You should have
 * received a copy copyOf the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */
package alice.tuple.logic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import alice.tuplecentre.api.exceptions.InvalidOperationException;
import alice.tuprolog.Number;
import alice.tuprolog.Prolog;
import alice.tuprolog.Struct;
import alice.tuprolog.Term;

/**
 * Base class for tuple argument classes.
 *
 * @author Alessandro Ricci
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 * @author (contributor) Saverio Cicora
 * @see LogicTupleDefault
 * @see ValueArgument
 * @see VarArgument
 */
class TupleArgumentDefault implements Serializable, TupleArgument {

    private static final long serialVersionUID = 1L;

    /**
     * the internal representation copyOf the argument is a (tu)Prolog term
     */
    protected Term value;

    TupleArgumentDefault() {
    }

    /**
     * Contructs a tuple argument copying a tuProlog term
     *
     * @param t the Prolog term whose content is used to build the argument
     */
    TupleArgumentDefault(final Term t) {
        this.value = t;
    }

    @Override
    public double doubleValue() {
        if (this.isNumber()) {
            return ((Number) this.value).doubleValue();
        }
        throw new InvalidOperationException("The argument is not a Number");
    }

    @Override
    public float floatValue() {
        if (this.isNumber()) {
            return ((Number) this.value).floatValue();
        }
        throw new InvalidOperationException("The argument is not a Number");
    }

    @Override
    public TupleArgument getArg(final int index) {
        return new TupleArgumentDefault(((Struct) this.value.getTerm()).getTerm(index));
    }

    @Override
    public TupleArgument getArg(final String name) {
        final Struct s = ((Struct) this.value).getArg(name);
        if (s != null) {
            return new TupleArgumentDefault(s);
        }
        return null;
    }

    @Override
    public int getArity() {
        if (this.isStruct()) {
            return ((Struct) this.value).getArity();
        }
        throw new InvalidOperationException("The argument is not a Struct");
    }

    @Override
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

    @Override
    public String getPredicateIndicator() {
        if (this.isStruct()) {
            // TODO CICORA: oppure return
            // ((Struct)value).getPredicateIndicator();
            return ((alice.tuprolog.Struct) this.value).getName() + "/"
                    + ((alice.tuprolog.Struct) this.value).getArity();
        }
        throw new InvalidOperationException("The argument is not a Struct");
    }

    @Override
    public TupleArgument getVarValue(final String varName) {
        if (this.value instanceof alice.tuprolog.Var) {
            return new TupleArgumentDefault(
                    this.value.getTerm());
        } else if (!(this.value instanceof alice.tuprolog.Struct)) {
            return null;
        } else {
            final Term t = this.getVarValue(varName, (Struct) this.value);
            if (t != null) {
                return new TupleArgumentDefault(t);
            }
            return null;
        }
    }

    @Override
    public int intValue() {
        if (this.isNumber()) {
            return ((Number) this.value).intValue();
        }
        throw new InvalidOperationException("The argument is not a Number");
    }

    @Override
    public boolean isAtom() {
        return this.value.isAtom()
                && this.value instanceof alice.tuprolog.Struct;
    }

    @Override
    public boolean isAtomic() {
        return this.value.isAtomic();
    }

    @Override
    public boolean isDouble() {
        return this.value instanceof alice.tuprolog.Number
                && this.value instanceof alice.tuprolog.Double;
    }

    @Override
    public boolean isFloat() {
        return this.value instanceof alice.tuprolog.Number
                && this.value instanceof alice.tuprolog.Float;
    }

    @Override
    public boolean isInt() {
        return this.value instanceof alice.tuprolog.Number
                && this.value instanceof alice.tuprolog.Int;
    }

    @Override
    public boolean isInteger() {
        return this.value instanceof alice.tuprolog.Number
                && ((Number) this.value).isInteger();
    }

    @Override
    public boolean isNotList() {
        return !this.value.isList();
    }

    @Override
    public boolean isLong() {
        return this.value instanceof alice.tuprolog.Number
                && this.value instanceof alice.tuprolog.Long;
    }

    @Override
    public boolean isNumber() {
        return this.value instanceof alice.tuprolog.Number;
    }

    @Override
    public boolean isReal() {
        return this.value instanceof alice.tuprolog.Number
                && ((Number) this.value).isReal();
    }

    @Override
    public boolean isStruct() {
        // return this.value.isCompound(); why?
        return this.value instanceof alice.tuprolog.Struct;
    }

    @Override
    public boolean isValue() {
        return !(this.value instanceof alice.tuprolog.Var);
    }

    @Override
    public boolean isVar() {
        return this.value instanceof alice.tuprolog.Var;
    }

    @Override
    public Iterator<? extends Term> listIterator() {
        if (this.value.isList()) {
            return ((Struct) this.value).listIterator();
        }
        return null;
    }

    @Override
    public long longValue() {
        if (this.isLong()) {
            return ((Number) this.value).longValue();
        }
        throw new InvalidOperationException("The argument is not a Long");
    }

    @Override
    public boolean match(final TupleArgument t) {
        return this.value.match(t.toTerm());
    }

    @Override
    public boolean propagate(final Prolog p, final TupleArgument t) {
        return this.value.unify(p, t.toTerm());
    }

    @Override
    public TupleArgument[] toArray() {
        if (!this.isNotList()) {
            final ArrayList<Term> list = new ArrayList<>();
            final Iterator<? extends Term> it = ((Struct) this.value)
                    .listIterator();
            while (it.hasNext()) {
                list.add(it.next());
            }
            final TupleArgument[] vect = new TupleArgument[list.size()];
            for (int i = 0; i < vect.length; i++) {
                vect[i] = new TupleArgumentDefault(list.get(i));
            }
            return vect;
        }
        throw new InvalidOperationException("The argument is not a List");
    }

    @Override
    public List<Term> toList() {
        if (!this.isNotList()) {
            final LinkedList<Term> list = new LinkedList<>();
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
     * Gets the string representation copyOf the argument
     *
     * @return the string representation copyOf this argument
     */
    @Override
    public String toString() {
        return this.value.getTerm().toString();
    }

    @Override
    public Term toTerm() {
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
