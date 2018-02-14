package alice.respect.core.tupleset;

import java.util.AbstractMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import alice.logictuple.LogicTuple;
import alice.respect.core.collection.DoubleKeyMVMap;
import alice.respect.core.collection.MVMap;
import alice.tuprolog.Var;

/**
 *
 * @author Saverio Cicora
 *
 */
public abstract class AbstractTupleSet implements ITupleSet {

    /**
     *
     * @author Saverio Cicora
     *
     */
    protected class LTEntry {

        private final String key1;
        private final String key2;
        private final LogicTuple value;

        LTEntry(final String keyOuter, final String keyInner, final LogicTuple v) {
            this.key1 = keyOuter;
            this.key2 = keyInner;
            this.value = v;
        }

        /**
         *
         * @return the String representation of the first key (K)
         */
        public String getKey1() {
            return this.key1;
        }

        /**
         *
         * @return the String representation of the second key (Q)
         */
        public String getKey2() {
            return this.key2;
        }

        /**
         *
         * @return the LogicTuple value
         */
        public LogicTuple getValue() {
            return this.value;
        }

        @Override
        public String toString() {
            return this.key1 + "\t" + this.key2 + "\t" + this.value.toString();
        }
    }

    /**
     *
     */
    protected List<LTEntry> tAdded;
    /**
     *
     */
    protected boolean transaction;
    /**
     *
     */
    protected List<LTEntry> tRemoved;
    /**
     *
     */
    protected DoubleKeyMVMap<String, String, LogicTuple> tuples;

    @Override
    public void add(final LogicTuple t) {
        final LTEntry e = this.createEntry(t);
        this.tuples.put(e.getKey1(), e.getKey2(), e.getValue());
        if (this.transaction) {
            this.tAdded.add(e);
        }
    }

    @Override
    public void beginTransaction() {
        this.transaction = true;
        this.tAdded.clear();
        this.tRemoved.clear();
    }

    @Override
    public void empty() {
        this.tuples.clear();
    }

    @Override
    public void endTransaction(final boolean commit) {
        if (!commit) {
            Iterator<LTEntry> it = this.tAdded.listIterator();
            while (it.hasNext()) {
                final LTEntry e = it.next();
                this.tuples.remove(e.getKey1(), e.getKey2(), e.getValue());
            }
            it = this.tRemoved.listIterator();
            while (it.hasNext()) {
                final LTEntry e = it.next();
                this.tuples.put(e.getKey1(), e.getKey2(), e.getValue());
            }
        }
        this.transaction = false;
        this.tAdded.clear();
        this.tRemoved.clear();
    }

    @Override
    public Iterator<LogicTuple> getIterator() {
        return this.tuples.iterator();
    }

    @Override
    public LogicTuple getMatchingTuple(final LogicTuple templ) {
        if (templ == null) {
            return null;
        }
        Iterator<LogicTuple> l;
        final String key2 = this.getTupleKey2(templ);
        if ("VAR".equals(key2)) {
            l = this.tuples.get(this.getTupleKey1(templ)).iterator();
        } else {
            final MVMap<String, LogicTuple> map = this.tuples.get(this
                    .getTupleKey1(templ));
            if (map.get("VAR").size() > 0) {
                l = map.iterator();
            } else {
                l = map.get(key2).iterator();
            }
        }
        while (l.hasNext()) {
            final LogicTuple tu = l.next();
            if (templ.match(tu)) {
                l.remove();
                if (this.transaction) {
                    this.tRemoved.add(this.createEntry(tu));
                }
                final AbstractMap<Var, Var> v = new LinkedHashMap<Var, Var>();
                return new LogicTuple(tu.toTerm().copyGoal(v, 0));
            }
        }
        return null;
    }

    @Override
    public boolean isEmpty() {
        return this.tuples.isEmpty();
    }

    @Override
    public boolean operationsPending() {
        if (this.tAdded.isEmpty() && this.tRemoved.isEmpty()) {
            return false;
        }
        return true;
    }

    @Override
    public LogicTuple readMatchingTuple(final LogicTuple templ) {
        if (templ == null) {
            return null;
        }
        Iterator<LogicTuple> l;
        final String key2 = this.getTupleKey2(templ);
        if ("VAR".equals(key2)) {
            l = this.tuples.get(this.getTupleKey1(templ)).iterator();
        } else {
            final MVMap<String, LogicTuple> map = this.tuples.get(this
                    .getTupleKey1(templ));
            if (map.get("VAR").size() > 0) {
                l = map.iterator();
            } else {
                l = map.get(key2).iterator();
            }
        }
        while (l.hasNext()) {
            final LogicTuple tu = l.next();
            if (templ.match(tu)) {
                final AbstractMap<Var, Var> v = new LinkedHashMap<Var, Var>();
                return new LogicTuple(tu.toTerm().copyGoal(v, 0));
            }
        }
        return null;
    }

    @Override
    public void remove(final LogicTuple t) {
        final LTEntry e = this.createEntry(t);
        final boolean res = this.tuples.remove(e.getKey1(), e.getKey2(),
                e.getValue());
        if (res && this.transaction) {
            this.tRemoved.add(this.createEntry(t));
        }
    }

    @Override
    public int size() {
        return this.tuples.size();
    }

    @Override
    public LogicTuple[] toArray() {
        return this.tuples.toArray(new LogicTuple[this.tuples.size()]);
    }

    @Override
    public String toString() {
        return this.tuples.toString();
    }

    private LTEntry createEntry(final LogicTuple t) {
        return new LTEntry(this.getTupleKey1(t), this.getTupleKey2(t), t);
    }

    /**
     *
     * @param t
     *            the LogicTuple whose first key should be retrieved
     * @return the String representation of the retrieved key
     */
    protected abstract String getTupleKey1(LogicTuple t);

    /**
     *
     * @param t
     *            the LogicTuple whose second key should be retrieved
     * @return the String representation of the retrieved key
     */
    protected abstract String getTupleKey2(LogicTuple t);
}
