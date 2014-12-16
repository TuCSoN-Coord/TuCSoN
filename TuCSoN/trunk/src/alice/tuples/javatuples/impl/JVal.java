/**
 * JArg.java
 */
package alice.tuples.javatuples.impl;

import alice.tuplecentre.api.exceptions.InvalidOperationException;
import alice.tuples.javatuples.api.IJVal;
import alice.tuples.javatuples.api.JArgType;
import alice.tuples.javatuples.exceptions.InvalidJValException;

/**
 * @author ste (mailto: s.mariani@unibo.it) on 21/feb/2014
 * 
 */
public class JVal implements IJVal {
    private final Object arg;
    private final JArgType type;

    /**
     * 
     * @param v
     *            the double value of this JVal
     */
    public JVal(final double v) {
        this.type = JArgType.DOUBLE;
        this.arg = v;
    }

    /**
     * 
     * @param v
     *            the float value of this JVal
     */
    public JVal(final float v) {
        this.type = JArgType.FLOAT;
        this.arg = v;
    }

    /**
     * 
     * @param v
     *            the int value of this JVal
     */
    public JVal(final int v) {
        this.type = JArgType.INT;
        this.arg = v;
    }

    /**
     * 
     * @param v
     *            the long value of this JVal
     */
    public JVal(final long v) {
        this.type = JArgType.LONG;
        this.arg = v;
    }

    /**
     * 
     * @param v
     *            the literal (Java String) value of this JVal
     * @throws InvalidJValException
     *             if the given literal (Java String) is invalid (e.g. null)
     */
    public JVal(final String v) throws InvalidJValException {
        if (v != null) {
            this.type = JArgType.LITERAL;
            this.arg = v;
        } else {
            throw new InvalidJValException("Null value");
        }
    }

    /*
     * (non-Javadoc)
     * @see alice.tuples.javatuples.IJVal#isDouble()
     */
    @Override
    public boolean isDouble() {
        return this.type == JArgType.DOUBLE;
    }

    /*
     * (non-Javadoc)
     * @see alice.tuples.javatuples.IJVal#isFloat()
     */
    @Override
    public boolean isFloat() {
        return this.type == JArgType.FLOAT;
    }

    /*
     * (non-Javadoc)
     * @see alice.tuples.javatuples.IJVal#isInt()
     */
    @Override
    public boolean isInt() {
        return this.type == JArgType.INT;
    }

    /*
     * (non-Javadoc)
     * @see alice.tuples.javatuples.IJVal#isLiteral()
     */
    @Override
    public boolean isLiteral() {
        return this.type == JArgType.LITERAL;
    }

    /*
     * (non-Javadoc)
     * @see alice.tuples.javatuples.IJVal#isLong()
     */
    @Override
    public boolean isLong() {
        return this.type == JArgType.LONG;
    }

    /*
     * (non-Javadoc)
     * @see alice.tuples.javatuples.IJArg#isVal()
     */
    @Override
    public boolean isVal() {
        return true;
    }

    /*
     * (non-Javadoc)
     * @see alice.tuples.javatuples.IJArg#isVar()
     */
    @Override
    public boolean isVar() {
        return false;
    }

    /*
     * (non-Javadoc)
     * @see alice.tuples.javatuples.IJVal#toDouble()
     */
    @Override
    public double toDouble() {
        if (this.type == JArgType.DOUBLE) {
            return ((Double) this.arg).doubleValue();
        }
        throw new InvalidOperationException("The JVal is not a Double");
    }

    /*
     * (non-Javadoc)
     * @see alice.tuples.javatuples.IJVal#toFloat()
     */
    @Override
    public float toFloat() {
        if (this.type == JArgType.FLOAT) {
            return ((Float) this.arg).floatValue();
        }
        throw new InvalidOperationException("The JVal is not a Float");
    }

    /*
     * (non-Javadoc)
     * @see alice.tuples.javatuples.IJVal#toInt()
     */
    @Override
    public int toInt() {
        if (this.type == JArgType.INT) {
            return ((Integer) this.arg).intValue();
        }
        throw new InvalidOperationException("The JVal is not an Int");
    }

    /*
     * (non-Javadoc)
     * @see alice.tuples.javatuples.IJVal#toLiteral()
     */
    @Override
    public String toLiteral() {
        if (this.type == JArgType.LITERAL) {
            return this.arg.toString();
        }
        throw new InvalidOperationException("The JVal is not a Literal");
    }

    /*
     * (non-Javadoc)
     * @see alice.tuples.javatuples.IJVal#toLong()
     */
    @Override
    public long toLong() {
        if (this.type == JArgType.LONG) {
            return ((Long) this.arg).longValue();
        }
        throw new InvalidOperationException("The JVal is not a Long");
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return this.arg.toString();
        // final StringBuffer sb = new StringBuffer(10);
        // switch (this.type) {
        // case DOUBLE:
        // sb.append("double(")
        // .append(String.valueOf(((Double) this.arg)
        // .doubleValue())).append(')');
        // break;
        // case FLOAT:
        // sb.append("float(")
        // .append(String.valueOf(((Float) this.arg).floatValue()))
        // .append(')');
        // break;
        // case INT:
        // sb.append("int(")
        // .append(String.valueOf(((Integer) this.arg).intValue()))
        // .append(')');
        // break;
        // case LITERAL:
        // sb.append("literal(").append(this.arg.toString()).append(')');
        // break;
        // case LONG:
        // sb.append("long(")
        // .append(String.valueOf(((Long) this.arg).longValue()))
        // .append(')');
        // break;
        // default:
        // // cannot happen
        // Logger.getLogger("JVal").log(Level.FINEST, "wtf");
        // break;
        // }
        // return sb.toString();
    }
}
