/**
 * JVar.java
 */
package alice.tuples.javatuples;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author ste (mailto: s.mariani@unibo.it) on 21/feb/2014
 * 
 */
public class JVar implements IJVar {

    private final String arg;
    private final JArgType type;
    private IJVal val;

    public JVar(final JArgType t, final String name)
            throws InvalidJVarException {
        if ((t != null) && (name != null)) {
            this.type = t;
            this.arg = name;
            this.val = null;
        } else {
            throw new InvalidJVarException();
        }
    }

    /*
     * (non-Javadoc)
     * @see alice.tuples.javatuples.IJVar#bind(alice.tuples.javatuples.IJVal)
     */
    @Override
    public IJVal bind(final IJVal v) throws BindingNullJValException {
        if (v != null) {
            this.val = v;
            return this.val;
        }
        throw new BindingNullJValException();
    }

    /*
     * (non-Javadoc)
     * @see alice.tuples.javatuples.IJVar#getBoundVal()
     */
    @Override
    public IJVal getBoundVal() {
        return this.val;
    }

    /*
     * (non-Javadoc)
     * @see alice.tuples.javatuples.IJVar#getName()
     */
    @Override
    public String getName() {
        return this.arg;
    }

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
        StringBuffer sb = new StringBuffer(10);
        switch (this.type) {
            case ANY:
                sb.append(this.arg);
                break;
            case DOUBLE:
                sb.append("$double(").append(this.arg).append(')');
                break;
            case FLOAT:
                sb.append("$float(").append(this.arg).append(')');
                break;
            case INT:
                sb.append("$int(").append(this.arg).append(')');
                break;
            case LITERAL:
                sb.append("$literal(").append(this.arg).append(')');
                break;
            case LONG:
                sb.append("$long(").append(this.arg).append(')');
                break;
            default:
                // cannot happen
                Logger.getLogger("JVal").log(Level.FINEST, "wtf");
                break;
        }
        return sb.toString();
    }

}
