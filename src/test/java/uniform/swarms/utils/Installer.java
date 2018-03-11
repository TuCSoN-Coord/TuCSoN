/**
 *
 */
package uniform.swarms.utils;

import alice.tucson.service.TucsonNodeService;

/**
 * @author ste
 *
 */
public class Installer extends Thread {

    private final TucsonNodeService tns;

    /**
     * @param node the TuCSoN service to install
     */
    public Installer(final TucsonNodeService node) {
        super("boot-thread");
        this.tns = node;
    }

    @Override
    public void run() {
        this.log("Installing node " + this.tns.getTCPPort() + "...");
        this.tns.install();
        this.log("...node " + this.tns.getTCPPort() + " installed");
    }

    private void log(final String msg) {
        System.out.println("[" + this.getName() + "]: " + msg);
    }

}
