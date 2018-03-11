package alice.tuple.java.api;

/**
 * @author ste (mailto: s.mariani@unibo.it) on 21/feb/2014
 */
public interface JArg {

    /**
     * @return wether this JArg is a JVal
     */
    boolean isVal();

    /**
     * @return wether this JArg is a JVar
     */
    boolean isVar();
}
