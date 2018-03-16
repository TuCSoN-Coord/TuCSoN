package alice.tuple.java.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import alice.tuple.Tuple;
import alice.tuple.java.api.JArg;
import alice.tuple.java.api.JTuple;
import alice.tuple.java.api.JTupleTemplate;
import alice.tuple.java.api.JVal;
import alice.tuplecentre.api.exceptions.InvalidOperationException;
import alice.tuplecentre.api.exceptions.InvalidTupleException;

/**
 * @author ste (mailto: s.mariani@unibo.it) on 21/feb/2014
 */
public class JTupleTemplateDefault implements Iterable<JArg>, JTupleTemplate {

    private static final int AVG_CAP = 5;
    private static final int AVG_CHARS = 1;
    private List<JArg> args;

    /**
     * @param arg the JArg to add to this JTupleTemplate
     * @throws InvalidTupleException if the given JArg is invalid (e.g. null)
     */
    public JTupleTemplateDefault(final JArg arg) throws InvalidTupleException {
        if (arg != null) {
            this.args = new ArrayList<>(JTupleTemplateDefault.AVG_CAP);
            this.args.add(arg);
        } else {
            throw new InvalidTupleException("Null value");
        }
    }

    /*
     * (non-Javadoc)
     * @see
     * alice.tuples.javatuples.JTupleTemplate#addArg(alice.tuples.javatuples
     * .JVar)
     */
    @Override
    public void addArg(final JArg arg) throws InvalidTupleException {
        if (arg != null) {
            this.args.add(arg);
        } else {
            throw new InvalidTupleException("Null value");
        }
    }

    /*
     * (non-Javadoc)
     * @see alice.tuples.javatuples.JTupleTemplate#getArg(int)
     */
    @Override
    public JArg getArg(final int i) {
        if (i >= 0 && i < this.args.size()) {
            return this.args.get(i);
        }
        throw new InvalidOperationException(
                "Index out of bounds. Value of the index i: " + i);
    }

    /*
     * (non-Javadoc)
     * @see alice.tuples.javatuples.JTupleTemplate#getNArgs()
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
    public Iterator<JArg> iterator() {
        return this.args.iterator();
    }

    /*
     * (non-Javadoc)
     * @see
     * alice.tuple.TupleTemplate#match(alice.tuple.Tuple)
     */
    @Override
    public boolean match(final Tuple t) {
        return t instanceof JTuple && JTuplesEngine.match(this, (JTuple) t);
    }

    /*
     * (non-Javadoc)
     * @see
     * alice.tuple.TupleTemplate#propagate(alice.tuple.Tuple
     * )
     */
    @Override
    public boolean propagate(final Tuple t) {
        if (t instanceof JTupleDefault) {
            final JTupleDefault jt = (JTupleDefault) t;
            if (JTuplesEngine.propagate(this, jt)) {
                this.args.clear();
                for (final JVal val : jt) {
                    this.args.add(val);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(JTupleTemplateDefault.AVG_CAP
                * JTupleTemplateDefault.AVG_CHARS);
        sb.append("< ");
        for (final JArg arg : this.args) {
            sb.append(arg.toString());
            sb.append(", ");
        }
        sb.deleteCharAt(sb.length() - 2);
        sb.append(">");
        return sb.toString();
    }
}
