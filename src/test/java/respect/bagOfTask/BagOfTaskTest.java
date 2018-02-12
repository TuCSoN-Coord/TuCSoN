package respect.bagOfTask;

import alice.tucson.api.exceptions.TucsonInvalidAgentIdException;

/**
 * NOT configured to run multiple masters. How to do so? Does it run
 * INDEPENDENTLY of master/workers launch ORDER?
 *
 * @author s.mariani@unibo.it
 */
public class BagOfTaskTest {

    /**
     * @param args
     *            no args expected.
     */
    public static void main(final String[] args) {
        try {
            new Master("master").go();
            new Worker("worker1").go();
            new Worker("worker2").go();
        } catch (final TucsonInvalidAgentIdException e) {
            e.printStackTrace();
        }
    }

}
