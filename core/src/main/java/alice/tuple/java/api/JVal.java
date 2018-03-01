/**
 * JVal.java
 */
package alice.tuple.java.api;

/**
 * @author ste (mailto: s.mariani@unibo.it) on 21/feb/2014
 *
 */
public interface JVal extends JArg {

    /**
     *
     * @return wether the JVal is a double
     */
    boolean isDouble();

    /**
     *
     * @return wether the JVal is a float
     */
    boolean isFloat();

    /**
     *
     * @return wether the JVal is a int
     */
    boolean isInt();

    /**
     *
     * @return wether the JVal is a literal
     */
    boolean isLiteral();

    /**
     *
     * @return wether the JVal is a long
     */
    boolean isLong();

    /**
     *
     * @return the double value of the JVal
     *
     */
    double toDouble();

    /**
     *
     * @return the float value of the JVal
     *
     */
    float toFloat();

    /**
     *
     * @return the int value of the JVal
     *
     */
    int toInt();

    /**
     *
     * @return the literal (Java String) value of the JVal
     *
     */
    String toLiteral();

    /**
     *
     * @return the long value of the JVal
     *
     */
    long toLong();
}
