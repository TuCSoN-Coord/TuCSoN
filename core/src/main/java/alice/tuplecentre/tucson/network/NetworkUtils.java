package alice.tuplecentre.tucson.network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author Michele Bombardi (mailto: michele.bombardi@studio.unibo.it)
 */
public final class NetworkUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().getClass());

    /**
     * Gets the decimal representation of the netmask of the address specified
     * as input
     *
     * @param addr the complete address, specified in the form IP/Netmask
     * @return the decimal representation of the netmask of the address
     * specified as input
     */
    public static String getDecimalNetmask(final String addr) {
        final String[] parts = addr.split("/");
        int prefix;
        if (parts.length < 2) {
            prefix = 0;
        } else {
            prefix = Integer.parseInt(parts[1]);
        }
        // System.out.println("Prefix=" + prefix);
        // System.out.println("Address=" + ip);
        final byte[] bytes = new byte[]{(byte) (0xffffffff << 32 - prefix >>> 24),
                (byte) (0xffffffff << 32 - prefix >> 16 & 0xff), (byte) (0xffffffff << 32 - prefix >> 8 & 0xff),
                (byte) (0xffffffff << 32 - prefix & 0xff)};
        InetAddress netAddr;
        try {
            netAddr = InetAddress.getByAddress(bytes);
            return netAddr.getHostAddress();
        } catch (final UnknownHostException e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
        // System.out.println("Mask=" + netAddr.getHostAddress());
    }

    /**
     * Gets the IP address part of the address specified as input
     *
     * @param addr the complete address, specified in the form IP/Netmask
     * @return the IP address part of the address specified as input
     */
    public static String getIp(final String addr) {
        final String[] parts = addr.split("/");
        return parts[0];
    }

    /**
     * Gets the Netmask address part of the address specified as input
     *
     * @param addr the complete address, specified in the form IP/Netmask
     * @return the Netmask address part of the address specified as input
     */
    public static int getNetmask(final String addr) {
        final String[] parts = addr.split("/");
        return Integer.parseInt(parts[1]);
    }

    /**
     * Check if two IP addresses are in the same network
     *
     * @param ip1  the first IP address
     * @param ip2  the second IP address
     * @param mask the network netmask
     * @return <code>true</code> if two IP addresses are in the same network
     * according to the specified netmask
     */
    public static boolean sameNetwork(final String ip1, final String ip2,
                                      final String mask) {
        try {
            final byte[] a1 = InetAddress.getByName(ip1).getAddress();
            final byte[] a2 = InetAddress.getByName(ip2).getAddress();
            final byte[] m = InetAddress.getByName(mask).getAddress();
            for (int i = 0; i < a1.length; i++) {
                if ((a1[i] & m[i]) != (a2[i] & m[i])) {
                    return false;
                }
            }
        } catch (final UnknownHostException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return true;
    }

    private NetworkUtils() {
        /*
         *
         */
    }
}
