/**
 * JTuple.java
 */
package alice.tuple.java.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import alice.tuplecentre.api.exceptions.InvalidOperationException;
import alice.tuplecentre.api.exceptions.InvalidTupleException;
import alice.tuple.java.api.IJTuple;
import alice.tuple.java.api.IJVal;

/**
 * @author ste (mailto: s.mariani@unibo.it) on 21/feb/2014
 *
 */
public class JTuple implements Iterable<IJVal>, IJTuple {

    private static final int AVG_CAP = 5;
    private static final int AVG_CHARS = 15;
    private List<IJVal> args;

    /**
     *
     * @param arg
     *            the JVal to add to this JTuple
     * @throws InvalidTupleException
     *             if the given JVal is invalid (e.g. null)
     */
    public JTuple(final IJVal arg) throws InvalidTupleException {
        if (arg != null) {
            this.args = new ArrayList<IJVal>(JTuple.AVG_CAP);
            this.args.add(arg);
        } else {
            throw new InvalidTupleException("Null value");
        }
    }

    @Override
    public void addArg(final IJVal arg) throws InvalidTupleException {
        if (arg != null) {
            this.args.add(arg);
        } else {
            throw new InvalidTupleException("Null value");
        }
    }

    @Override
    public IJVal getArg(final int i) {
        if (i >= 0 && i < this.args.size()) {
            return this.args.get(i);
        }
        throw new InvalidOperationException(
                "Index out of bounds. Value of the index i: " + i);
    }

    /*
     * (non-Javadoc)
     * @see alice.tuples.javatuples.IJTuple#getNArgs()
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
    public Iterator<IJVal> iterator() {
        return this.args.iterator();
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer(JTuple.AVG_CAP
                * JTuple.AVG_CHARS);
        sb.append("< ");
        for (final IJVal arg : this.args) {
            sb.append(arg.toString());
            sb.append(", ");
        }
        sb.deleteCharAt(sb.length() - 2);
        sb.append(">");
        return sb.toString();
    }
}
