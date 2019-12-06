package timedDiningPhilos;

import java.io.IOException;

import alice.tuple.logic.LogicTuple;
import alice.tuple.logic.exceptions.InvalidLogicTupleException;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;
import alice.tuplecentre.respect.api.TupleCentreId;
import alice.tuplecentre.respect.api.exceptions.InvalidTupleCentreIdException;
import alice.tuplecentre.tucson.api.AbstractTucsonAgent;
import alice.tuplecentre.tucson.api.TucsonAgentId;
import alice.tuplecentre.tucson.api.TucsonMetaACC;
import alice.tuplecentre.tucson.api.acc.EnhancedSyncACC;
import alice.tuplecentre.tucson.api.acc.NegotiationACC;
import alice.tuplecentre.tucson.api.acc.RootACC;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tuplecentre.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tuplecentre.tucson.api.exceptions.UnreachableNodeException;
import alice.tuplecentre.tucson.utilities.Utils;

/**
 * Classic Dining Philosophers coordination problem tackled by adopting a clear
 * separation of concerns between coordinables (philosophers) and coordination
 * medium (table) thanks to TuCSoN ReSpecT tuple centres programmability.
 *
 * @author ste (mailto: s.mariani@unibo.it)
 */
public class TDiningPhilosophersTest extends AbstractTucsonAgent {

    private static final int EATING_STEP = 1000;
    /*
     * Should be exactly divisible.
     */
    private static final int EATING_TIME = 5000;
    private static final int MAX_EATING_TIME = 7000;
    /*
     * Max number of simultaneously eating philosophers should be
     * N_PHILOSOPHERS-2.
     */
    private static final int N_PHILOSOPHERS = 5;

    /**
     * @param args no args expected
     */
    public static void main(final String[] args) throws TucsonInvalidAgentIdException {
        new TDiningPhilosophersTest("boot").go();
    }

    private final String ip;
    private final String port;

    /**
     * @param aid the String representation of a valid TuCSoN agent identifier
     * @throws TucsonInvalidAgentIdException if the given String does not represent a valid TuCSoN agent
     *                                       identifier
     */
    public TDiningPhilosophersTest(final String aid)
            throws TucsonInvalidAgentIdException {
        super(aid);
        /*
         * To experiment with a distributed setting, launch the TuCSoN Node
         * hosting the 'table' tuple centre on a remote node.
         */
        this.ip = "localhost";
        this.port = "20504";
    }

    @Override
    protected RootACC retrieveACC(TucsonAgentId aid, String networkAddress, int portNumber) throws Exception {
        return null;
    }

    @Override
    protected void main() throws OperationTimeOutException, TucsonInvalidAgentIdException, UnreachableNodeException, TucsonOperationNotPossibleException, InvalidTupleCentreIdException, IOException, InvalidLogicTupleException {
        final NegotiationACC negAcc = TucsonMetaACC
                .getNegotiationContext(this.getTucsonAgentId());
        final EnhancedSyncACC acc = negAcc.playDefaultRole();
        final TupleCentreId table = new TupleCentreId("table",
                this.ip, this.port);
        this.say("Injecting 'table' ReSpecT specification in tc < "
                + table.toString() + " >...");
        /*
         * Program the tuple centre by setting a ReSpecT specification (a
         * set of ReSpecT specification tuples) in its specification space.
         */
        acc.setS(
                table,
                Utils.fileToString("timed_table.rsp"),
                null);
        /*
         * Init max eating time.
         */
        acc.out(table,
                LogicTuple.parse("max_eating_time("
                        + TDiningPhilosophersTest.MAX_EATING_TIME + ")"),
                null);
        for (int i = 0; i < TDiningPhilosophersTest.N_PHILOSOPHERS; i++) {
            /*
             * Init chopsticks required to eat.
             */
            acc.out(table, LogicTuple.parse("chop(" + i + ")"), null);
        }
        for (int i = 0; i < TDiningPhilosophersTest.N_PHILOSOPHERS - 1; i++) {
            /*
             * Start philosophers by telling them which chopsticks pair they
             * need.
             */
            new DiningPhilosopher("'philo-" + i + "'", table, i, (i + 1)
                    % TDiningPhilosophersTest.N_PHILOSOPHERS,
                    TDiningPhilosophersTest.EATING_TIME,
                    TDiningPhilosophersTest.EATING_STEP).go();
        }
        /*
         * Sloth philosopher.
         */
        new DiningPhilosopher("'philo-"
                + (TDiningPhilosophersTest.N_PHILOSOPHERS - 1) + "'",
                table, TDiningPhilosophersTest.N_PHILOSOPHERS - 1, 0,
                TDiningPhilosophersTest.EATING_TIME * 2,
                TDiningPhilosophersTest.EATING_STEP).go();
        acc.exit();
    }
}
