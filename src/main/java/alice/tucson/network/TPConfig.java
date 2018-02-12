package alice.tucson.network;

import alice.tucson.network.exceptions.IllegalPortNumberException;

/**
 * <p>
 * TPConfig
 * </p>
 * <p>
 * A singleton class to manage configuration of TucsonProtocol
 * </p>
 *
 * @author Saverio Cicora
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 *
 */
public final class TPConfig {

    // TCP configuration ----------------------------------
    private static final int DEFAULT_TCP_PORT = 20504;
    private static final int MAX_UNBOUND_PORT = 64000;

    /**
     * Return the default TCP port number
     *
     * @return a valid TCP port number
     */
    public static int getDefaultTcpPort() {
        return TPConfig.DEFAULT_TCP_PORT;
    }

    // Generic configuration ------------------------------
    private final int defProtType = TPFactory.DIALOG_TYPE_TCP;
    private int tcpPort = -1;

    /**
     * Return the default Protocol type
     *
     * @return the protocol type codified as an integer
     */
    public int getDefaultProtocolType() {
        return this.defProtType;
    }

    /**
     * Return the TCP port number
     *
     * @return a valid TCP port number
     */
    public int getNodeTcpPort() {
        if (this.tcpPort < 0) {
            return TPConfig.DEFAULT_TCP_PORT;
        }
        return this.tcpPort;
    }

    /**
     * TODO CICORA: Set the TCP port: only one set is permitted, the second one
     * will be ignored
     *
     * @param portNumber
     *            the TCP listening port
     */
    public synchronized void setTcpPort(final int portNumber) {
        if (portNumber < 1 || portNumber > TPConfig.MAX_UNBOUND_PORT) {
            throw new IllegalPortNumberException(
                    "Port number out of bounds. Port number: " + portNumber);
        }
        this.tcpPort = portNumber;
    }
}
