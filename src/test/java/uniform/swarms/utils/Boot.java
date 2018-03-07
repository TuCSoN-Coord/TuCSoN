/**
 *
 */
package uniform.swarms.utils;

import java.util.Properties;

import alice.tuplecentre.tucson.network.exceptions.DialogInitializationException;
import alice.tuplecentre.tucson.service.TucsonNodeService;

/**
 * @author ste
 *
 */
public class Boot {

    private final static String DEF_NETID = "localhost";
    private final static String DEF_PORTNO = "20504";
    private final static String DEF_TC_NAME = "default";
    private final static int SLEEP_TIME = 1000;

    private static String netid;
    private static String portno;
    private static String tcname;

    /**
     * @param params
     *            a Java Property object specifying name, TCP port, and IP
     *            address of the TuCSoN tuple centre to setup
     */
    public static void boot(final Properties params) {

        Boot.parseParams(params);

        final int port = Boot.checkPort(Boot.portno);

        Boot.log("Booting node on port " + port + "...");

        /* Boot TuCSoN node */
        TucsonNodeService node = null;
        node = new TucsonNodeService(port);
        new Installer(node).start();
        try {
            while (!TucsonNodeService.isInstalled(port, Boot.SLEEP_TIME)) {
                Thread.sleep(Boot.SLEEP_TIME);
            }
        } catch (final InterruptedException e) {
            Boot.err("Too early in the morning :Q___");
            System.exit(-1);
        } catch (final DialogInitializationException e) {
            Boot.err("Cannot connect to node on port " + port);
            System.exit(-1);
        }
        Boot.log("...node on port " + port + " boot");

    }

    private static void parseParams(final Properties params) {
        /* Get params (if any) */
        if (params != null) {
            Boot.netid = params.getProperty("-netid", Boot.DEF_NETID);
            Boot.portno = params.getProperty("-portno", Boot.DEF_PORTNO);
            Boot.tcname = params.getProperty("-tcname", Boot.DEF_TC_NAME);
        }
        Boot.log(Boot.netid);
        Boot.log(Boot.portno);
        Boot.log(Boot.tcname);
    }

    private static int checkPort(final String p) {
        /* Check TuCSoN node port */
        int port = 0;
        try {
            port = Integer.parseInt(p);
        } catch (final NumberFormatException e) {
            Boot.err("No valid port number given");
            System.exit(-1);
        }
        if ((port < 1) || (port > 65535)) {
            Boot.err("No valid port number given");
            System.exit(-1);
        }
        // Boot.log("" + port);
        return port;
    }

    private static void err(final String msg) {
        System.err.println(msg);
    }

    private static void log(final String msg) {
        System.out.println(msg);
    }

}
