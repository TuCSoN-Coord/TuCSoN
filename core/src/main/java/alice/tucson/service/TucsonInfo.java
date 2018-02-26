package alice.tucson.service;

/**
 * Class that contains information about TuCSoN Coordination Infrastructure
 *
 * @author Enrico Siboni
 */
public final class TucsonInfo {
    // if having a class for such information seems an overkill, feel free to move those information to a better place

    /**
     * Field containing the TuCSoN version stringified
     */
    private static final String VERSION = "TuCSoN-1.13.0.0301-beta";
    /**
     * Field containing TuCSoN default port number
     */
    private static final int DEFAULT_PORT = 20504;

    private TucsonInfo() {
    }

    /**
     * Gets the actual version of TuCSoN Coordination Infrastructure
     *
     * @return a string containing the version of TuCSoN Coordination Infrastructure
     */
    public static String getVersion() {
        return VERSION;
    }

    /**
     * @return the TuCSoN default port number
     */
    public static int getDefaultPortNumber() {
        return DEFAULT_PORT;
    }
}
