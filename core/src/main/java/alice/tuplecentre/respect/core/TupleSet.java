/*
 * ReSpecT - Copyright (C) aliCE team at deis.unibo.it This library is free
 * software; you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package alice.tuplecentre.respect.core;

import java.util.AbstractMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import alice.logictuple.LogicTuple;
import alice.tuprolog.Var;

/**
 * Class representing a Tuple Set.
 *
 * @author Alessandro Ricci
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 * @author (contributor) Saverio Cicora
 *
 */
public class TupleSet {

    private final List<LogicTuple> tAdded;
    private boolean transaction;
    private final List<LogicTuple> tRemoved;
    private final List<LogicTuple> tuples;

    /**
     *
     */
    public TupleSet() {
        this.tuples = new LinkedList<LogicTuple>();
        this.tAdded = new LinkedList<LogicTuple>();
        this.tRemoved = new LinkedList<LogicTuple>();
        this.transaction = false;
    }

    /**
     *
     * @param t
     *            the tuple to add to this tuple list
     */
    public void add(final LogicTuple t) {
        this.tuples.add(t);
        if (this.transaction) {
            this.tAdded.add(t);
        }
    }

    /**
     * Begins a transaction section
     *
     * Every operation on multiset can be undone
     */
    public void beginTransaction() {
        this.transaction = true;
        this.tAdded.clear();
        this.tRemoved.clear();
    }

    /**
     *
     */
    public void empty() {
        this.tuples.clear();
    }

    /**
     * Ends a transaction section specifying if operations must be committed or
     * undone
     *
     * @param commit
     *            if <code>true</code> the operations are committed, else they
     *            are undone and the multiset is rolled back to the state before
     *            the <code>beginTransaction</code> invocation
     */
    public void endTransaction(final boolean commit) {
        if (!commit) {
            Iterator<LogicTuple> it = this.tAdded.listIterator();
            while (it.hasNext()) {
                this.tuples.remove(it.next());
            }
            it = this.tRemoved.listIterator();
            while (it.hasNext()) {
                this.tuples.add(it.next());
            }
        }
        this.transaction = false;
        this.tAdded.clear();
        this.tRemoved.clear();
    }

    /**
     *
     * @return a Java iterator through this tuples list
     */
    public Iterator<LogicTuple> getIterator() {
        return this.tuples.listIterator();
    }

    /**
     *
     * @param templ
     *            the tuple template to be used to retrieve matching tuples from
     *            this set
     * @return the tuple non deterministically selected as a result of the
     *         matching process (which is consumed)
     */
    public LogicTuple getMatchingTuple(final LogicTuple templ) {
        if (templ == null) {
            return null;
        }
        final ListIterator<LogicTuple> l = this.tuples.listIterator();
        while (l.hasNext()) {
            final LogicTuple tu = l.next();
            if (templ.match(tu)) {
                l.remove();
                if (this.transaction) {
                    this.tRemoved.add(tu);
                }
                final AbstractMap<Var, Var> v = new LinkedHashMap<Var, Var>();
                return new LogicTuple(tu.toTerm().copyGoal(v, 0));
            }
        }
        return null;
    }

    /**
     *
     * @return wether this tuples set is empty or not
     */
    public boolean isEmpty() {
        return this.tuples.isEmpty();
    }

    /**
     * Tells whether there are changes in the tuple multi-set during a
     * transaction
     *
     * @return true if the ongoing transaction made any changes to the tuple
     *         multi-set
     */
    public boolean operationsPending() {
        if (this.tAdded.isEmpty() && this.tRemoved.isEmpty()) {
            return false;
        }
        return true;
    }

    /**
     *
     * @param templ
     *            the tuple template to be used to retrieve matching tuples from
     *            this set
     * @return the tuple non deterministically selected as a result of the
     *         matching process (which is NOT consumed)
     */
    public LogicTuple readMatchingTuple(final LogicTuple templ) {
        if (templ == null) {
            return null;
        }
        final ListIterator<LogicTuple> l = this.tuples.listIterator();
        while (l.hasNext()) {
            final LogicTuple tu = l.next();
            if (templ.match(tu)) {
                final AbstractMap<Var, Var> v = new LinkedHashMap<Var, Var>();
                return new LogicTuple(tu.toTerm().copyGoal(v, 0));
            }
        }
        return null;
    }

    /**
     *
     * @return the length of this tuple set
     */
    public int size() {
        return this.tuples.size();
    }

    /**
     *
     * @return the Java array representation of this tuple set
     */
    public LogicTuple[] toArray() {
        return this.tuples.toArray(new LogicTuple[this.tuples.size()]);
    }

    /**
     * Provides a representation of the tuple multi-set in the form of a String
     * containing a prolog theory.
     *
     * @return a textual representation in the form of a prolog theory.
     */
    @Override
    public String toString() {
        String str = "";
        for (final LogicTuple t : this.tuples) {
            str = str.concat(t.toString()).concat(".\n");
        }
        return str;
    }
}
