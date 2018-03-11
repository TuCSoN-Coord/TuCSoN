package alice.tuplecentre.tucson.network;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author Michele Bombardi (mailto: michele.bombardi@studio.unibo.it)
 */
public final class NetworkUtils {
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
        final int mask = 0xffffffff << 32 - prefix;
        // System.out.println("Prefix=" + prefix);
        // System.out.println("Address=" + ip);
        final int value = mask;
        final byte[] bytes = new byte[]{(byte) (value >>> 24),
                (byte) (value >> 16 & 0xff), (byte) (value >> 8 & 0xff),
                (byte) (value & 0xff)};
        InetAddress netAddr = null;
        try {
            netAddr = InetAddress.getByAddress(bytes);
            return netAddr.getHostAddress();
        } catch (final UnknownHostException e) {
            e.printStackTrace();
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
            e.printStackTrace();
        }
        return true;
    }

    private NetworkUtils() {
        /*
         *
         */
    }
}
