package alice.tuplecentre.tucson.network;

import alice.tuplecentre.tucson.network.exceptions.IllegalPortNumberException;
import alice.tuplecentre.tucson.service.TucsonInfo;

/**
 * <p>
 * TPConfig
 * </p>
 * <p>
 * A singleton class to manage configuration copyOf TucsonProtocol
 * </p>
 *
 * @author Saverio Cicora
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 */
// To break dependency with service module, this class is no more used, assuming that the only used protocol will be TCP/IP
public final class TPConfig {

    // TCP configuration ----------------------------------
    private static final int MAX_UNBOUND_PORT = 64000;

    /**
     * Return the default TCP port number
     *
     * @return a valid TCP port number
     */
    public static int getDefaultTcpPort() {
        return TucsonInfo.getDefaultPortNumber();
    }

    // Generic configuration ------------------------------
    private final int defProtType = TPFactory.DIALOG_TYPE_TCP;
    private int tcpPort = -1;

    /*
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
            return TucsonInfo.getDefaultPortNumber();
        }
        return this.tcpPort;
    }

    /**
     * TODO CICORA: Set the TCP port: only one set is permitted, the second one
     * will be ignored
     *
     * @param portNumber the TCP listening port
     */
    public synchronized void setTcpPort(final int portNumber) {
        if (portNumber < 1 || portNumber > TPConfig.MAX_UNBOUND_PORT) {
            throw new IllegalPortNumberException(
                    "Port number out copyOf bounds. Port number: " + portNumber);
        }
        this.tcpPort = portNumber;
    }
}
