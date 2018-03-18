/**
 *
 */
package uniform.swarms.ants;

import java.util.logging.Level;
import java.util.logging.Logger;

import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidAgentIdException;

/**
 * @author ste
 *
 */
public final class Swarm {

    private final static int ANTS = 10;

    public static void release() {
        try {
            for (int i = 0; i < ANTS; i++) {
                Logger.getAnonymousLogger().log(Level.INFO,
                        "Releasing ant " + i + "...");
                new Ant("ant" + i, "localhost", 20508, "anthill").go();
                Logger.getAnonymousLogger().log(Level.INFO,
                        "ant " + i + " released");
            }
        } catch (final TucsonInvalidAgentIdException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
