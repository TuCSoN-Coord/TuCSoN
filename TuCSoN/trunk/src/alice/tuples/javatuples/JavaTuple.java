/**
 * JavaTuple.java
 */
package alice.tuples.javatuples;

import java.util.ArrayList;
import java.util.List;

import alice.tuplecentre.api.Tuple;
import alice.tuplecentre.api.exceptions.InvalidTupleException;

/**
 * @author ste (mailto: s.mariani@unibo.it) on 09/gen/2014
 * 
 */
public class JavaTuple implements IJavaTuple {

    private final static int AVG_ARG_LENGTH = 10;
    private final List<IJavaTuple> args;
    private final String name;

    public JavaTuple(final String tupleName, final IJavaTuple arg)
            throws InvalidTupleException {
        if (!tupleName.matches("[a-zA-z]+")) {
            throw new InvalidTupleException();
        }
        this.name = tupleName.toLowerCase();
        this.args = new ArrayList<IJavaTuple>(1);
        this.args.add(arg);
    }

    /*
     * (non-Javadoc)
     * @see
     * alice.tuples.javatuples.IJavaTuple#addArg(alice.tuplecentre.api.Tuple)
     */
    @Override
    public void addArg(final IJavaTuple t) {
        this.args.add(t);
    }

    /*
     * (non-Javadoc)
     * @see alice.tuples.javatuples.IJavaTuple#getArg(int)
     */
    @Override
    public IJavaTuple getArg(final int i) {
        return this.args.get(i);
    }

    /*
     * (non-Javadoc)
     * @see alice.tuples.javatuples.IJavaTuple#getName()
     */
    @Override
    public String getName() {
        return this.name;
    }

    /*
     * (non-Javadoc)
     * @see alice.tuples.javatuples.IJavaTuple#isComposite()
     */
    @Override
    public boolean isComposite() {
        return true;
    }

    /*
     * (non-Javadoc)
     * @see alice.tuples.javatuples.IJavaTuple#isValue()
     */
    @Override
    public boolean isValue() {
        return false;
    }

    /*
     * (non-Javadoc)
     * @see alice.tuples.javatuples.IJavaTuple#isVar()
     */
    @Override
    public boolean isVar() {
        return false;
    }

    /*
     * (non-Javadoc)
     * @see
     * alice.tuplecentre.api.TupleTemplate#match(alice.tuplecentre.api.Tuple)
     */
    @Override
    public boolean match(final Tuple t) {
        // TODO Auto-generated method stub
        return false;
    }

    /*
     * (non-Javadoc)
     * @see
     * alice.tuplecentre.api.TupleTemplate#propagate(alice.tuplecentre.api.Tuple
     * )
     */
    @Override
    public boolean propagate(final Tuple t) {
        // TODO Auto-generated method stub
        return false;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        final StringBuffer sb =
                new StringBuffer(this.name.length()
                        + (this.args.size() * JavaTuple.AVG_ARG_LENGTH));
        sb.append(this.name);
        sb.append('(');
        for (final IJavaTuple arg : this.args) {
            sb.append(arg.toString());
            sb.append(',');
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(')');
        return sb.toString();
    }

}
