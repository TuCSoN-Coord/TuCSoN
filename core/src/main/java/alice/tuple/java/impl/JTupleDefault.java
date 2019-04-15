package alice.tuple.java.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import alice.tuple.java.api.JTuple;
import alice.tuple.java.api.JVal;
import alice.tuplecentre.api.exceptions.InvalidOperationException;
import alice.tuplecentre.api.exceptions.InvalidTupleException;

/**
 * @author ste (mailto: s.mariani@unibo.it) on 21/feb/2014
 */
public class JTupleDefault implements Iterable<JVal>, JTuple {

    private static final int AVG_CAP = 5;
    private static final int AVG_CHARS = 15;
    private List<JVal> args;

    /**
     * @param arg the JVal to add to this JTuple
     * @throws InvalidTupleException if the given JVal is invalid (e.g. null)
     */
    public JTupleDefault(final JVal arg) throws InvalidTupleException {
        if (arg != null) {
            this.args = new ArrayList<>(JTupleDefault.AVG_CAP);
            this.args.add(arg);
        } else {
            throw new InvalidTupleException("Null value");
        }
    }

    @Override
    public void addArg(final JVal arg) throws InvalidTupleException {
        if (arg != null) {
            this.args.add(arg);
        } else {
            throw new InvalidTupleException("Null value");
        }
    }

    @Override
    public JVal getArg(final int i) {
        if (i >= 0 && i < this.args.size()) {
            return this.args.get(i);
        }
        throw new InvalidOperationException(
                "Index out copyOf bounds. Value copyOf the index i: " + i);
    }

    /*
     * (non-Javadoc)
     * @see alice.tuples.javatuples.JTuple#getNArgs()
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
    public Iterator<JVal> iterator() {
        return this.args.iterator();
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(JTupleDefault.AVG_CAP
                * JTupleDefault.AVG_CHARS);
        sb.append("< ");
        for (final JVal arg : this.args) {
            sb.append(arg.toString());
            sb.append(", ");
        }
        sb.deleteCharAt(sb.length() - 2);
        sb.append(">");
        return sb.toString();
    }
}
