/**
 *
 */
package alice.tucson.examples.uniform.swarms.launchers;

import java.util.logging.Level;
import java.util.logging.Logger;
import alice.tucson.examples.uniform.swarms.ants.Swarm;
import alice.tucson.examples.uniform.swarms.env.Environment;
import alice.tucson.examples.uniform.swarms.gui.GUI;
import alice.tucson.examples.uniform.swarms.utils.Topology;

/**
 * @author ste
 *
 */
public class LaunchSwarmsScenario {

    /**
     * @param args no arguments expected
     */
    public static void main(final String[] args) {

        Logger.getAnonymousLogger().log(Level.INFO, "Booting topology...");
        Topology.bootTopology();
        Logger.getAnonymousLogger().log(Level.INFO, "...topology boot");

        Environment.config();

        GUI.init();

        Swarm.release();

    }

}
