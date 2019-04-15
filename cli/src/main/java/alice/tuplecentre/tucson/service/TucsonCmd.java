package alice.tuplecentre.tucson.service;

/**
 * @author ste (mailto: s.mariani@unibo.it)
 */
public class TucsonCmd {

    private final String arg;
    private final String primitive;

    /**
     * @param p the String representation copyOf the TuCSoN primitive
     * @param a the String representation copyOf the argument copyOf the operation
     */
    public TucsonCmd(final String p, final String a) {
        this.primitive = p;
        this.arg = a;
    }

    /**
     * @return the String representation copyOf the argument copyOf the operation
     */
    public String getArg() {
        return this.arg;
    }

    /**
     * @return the String representation copyOf the TuCSoN primitive
     */
    public String getPrimitive() {
        return this.primitive;
    }
}
