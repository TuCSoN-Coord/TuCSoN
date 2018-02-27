/**
 * JTupleTemplate.java
 */
package alice.tuple.java.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import alice.tuple.Tuple;
import alice.tuplecentre.api.exceptions.InvalidOperationException;
import alice.tuplecentre.api.exceptions.InvalidTupleException;
import alice.tuple.java.api.IJArg;
import alice.tuple.java.api.IJTuple;
import alice.tuple.java.api.IJTupleTemplate;
import alice.tuple.java.api.IJVal;

/**
 * @author ste (mailto: s.mariani@unibo.it) on 21/feb/2014
 *
 */
public class JTupleTemplate implements Iterable<IJArg>, IJTupleTemplate {

    private static final int AVG_CAP = 5;
    private static final int AVG_CHARS = 1;
    private List<IJArg> args;

    /**
     *
     * @param arg
     *            the JArg to add to this JTupleTemplate
     * @throws InvalidTupleException
     *             if the given JArg is invalid (e.g. null)
     */
    public JTupleTemplate(final IJArg arg) throws InvalidTupleException {
        if (arg != null) {
            this.args = new ArrayList<IJArg>(JTupleTemplate.AVG_CAP);
            this.args.add(arg);
        } else {
            throw new InvalidTupleException("Null value");
        }
    }

    /*
     * (non-Javadoc)
     * @see
     * alice.tuples.javatuples.IJTupleTemplate#addArg(alice.tuples.javatuples
     * .JVar)
     */
    @Override
    public void addArg(final IJArg arg) throws InvalidTupleException {
        if (arg != null) {
            this.args.add(arg);
        } else {
            throw new InvalidTupleException("Null value");
        }
    }

    /*
     * (non-Javadoc)
     * @see alice.tuples.javatuples.IJTupleTemplate#getArg(int)
     */
    @Override
    public IJArg getArg(final int i) {
        if (i >= 0 && i < this.args.size()) {
            return this.args.get(i);
        }
        throw new InvalidOperationException(
                "Index out of bounds. Value of the index i: " + i);
    }

    /*
     * (non-Javadoc)
     * @see alice.tuples.javatuples.IJTupleTemplate#getNArgs()
     */
    @Override
    public int getNArgs() {
        return this.args.size();
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator<IJArg> iterator() {
        return this.args.iterator();
    }

    /*
     * (non-Javadoc)
     * @see
     * alice.tuple.TupleTemplate#match(alice.tuple.Tuple)
     */
    @Override
    public boolean match(final Tuple t) {
        if (t instanceof IJTuple) {
            return JTuplesEngine.match(this, (IJTuple) t);
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * @see
     * alice.tuple.TupleTemplate#propagate(alice.tuple.Tuple
     * )
     */
    @Override
    public boolean propagate(final Tuple t) {
        if (t instanceof JTuple) {
            final JTuple jt = (JTuple) t;
            if (JTuplesEngine.propagate(this, jt)) {
                this.args.clear();
                for (final IJVal val : jt) {
                    this.args.add(val);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer(JTupleTemplate.AVG_CAP
                * JTupleTemplate.AVG_CHARS);
        sb.append("< ");
        for (final IJArg arg : this.args) {
            sb.append(arg.toString());
            sb.append(", ");
        }
        sb.deleteCharAt(sb.length() - 2);
        sb.append(">");
        return sb.toString();
    }
}
