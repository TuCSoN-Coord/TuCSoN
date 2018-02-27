/**
 * JVar.java
 */
package alice.tuple.java.impl;

import alice.tuple.java.api.IJVar;
import alice.tuple.java.api.JArgType;
import alice.tuple.java.exceptions.InvalidJVarException;

/**
 * @author ste (mailto: s.mariani@unibo.it) on 21/feb/2014
 *
 */
public class JVar implements IJVar {

    // private final String arg;
    private final JArgType type;

    // private IJVal val;
    /**
     *
     * @param t
     *            the JArgType of this JVar
     * @throws InvalidJVarException
     *             if the given type or name are invalid (e.g. null)
     */
    public JVar(final JArgType t) throws InvalidJVarException {
        if (t != null) {
            this.type = t;
            // this.arg = name;
            // this.val = null;
        } else {
            throw new InvalidJVarException("Null value");
        }
    }

    /*
     * (non-Javadoc)
     * @see alice.tuples.javatuples.IJVar#bind(alice.tuples.javatuples.IJVal)
     */
    // public IJVal bind(final IJVal v) throws BindingNullJValException {
    // if (v != null) {
    // this.val = v;
    // return this.val;
    // }
    // throw new BindingNullJValException();
    // }
    /*
     * (non-Javadoc)
     * @see alice.tuples.javatuples.IJVar#getBoundVal()
     */
    // public IJVal getBoundVal() {
    // return this.val;
    // }
    /*
     * (non-Javadoc)
     * @see alice.tuples.javatuples.IJVar#getName()
     */
    // public String getName() {
    // return this.arg;
    // }
    /*
     * (non-Javadoc)
     * @see alice.tuples.javatuples.IJVar#getType()
     */
    @Override
    public JArgType getType() {
        return this.type;
    }

    /*
     * (non-Javadoc)
     * @see alice.tuples.javatuples.IJArg#isVal()
     */
    @Override
    public boolean isVal() {
        return false;
    }

    /*
     * (non-Javadoc)
     * @see alice.tuples.javatuples.IJArg#isVar()
     */
    @Override
    public boolean isVar() {
        return true;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return this.type.toString();
        // switch (this.type) {
        // case ANY:
        // return "ANY";
        // case DOUBLE:
        // return "DOUBLE";
        // case FLOAT:
        // return "FLOAT";
        // case INT:
        // return "INT";
        // case LITERAL:
        // return "LITERAL";
        // case LONG:
        // return "LONG";
        // default:
        // // cannot happen
        // Logger.getLogger("JVar").log(Level.FINEST, "wtf");
        // break;
        // }
        // return null;
    }
}
