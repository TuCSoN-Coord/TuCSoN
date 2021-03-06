package timedDiningPhilos;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import alice.tuple.logic.LogicTuple;
import alice.tuple.logic.exceptions.InvalidLogicTupleException;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;
import alice.tuplecentre.tucson.api.AbstractTucsonAgent;
import alice.tuplecentre.tucson.api.TucsonAgentId;
import alice.tuplecentre.tucson.api.TucsonMetaACC;
import alice.tuplecentre.tucson.api.TucsonTupleCentreId;
import alice.tuplecentre.tucson.api.acc.NegotiationACC;
import alice.tuplecentre.tucson.api.acc.OrdinaryAndSpecificationSyncACC;
import alice.tuplecentre.tucson.api.acc.RootACC;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidTupleCentreIdException;
import alice.tuplecentre.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tuplecentre.tucson.api.exceptions.UnreachableNodeException;
import alice.tuplecentre.tucson.service.TucsonInfo;
import alice.tuplecentre.tucson.utilities.Utils;

/**
 * Classic Dining Philosophers coordination problem tackled by adopting a clear
 * separation copyOf concerns between coordinables (philosophers) and coordination
 * medium (table) thanks to TuCSoN ReSpecT tuple centres programmability.
 *
 * @author ste (mailto: s.mariani@unibo.it)
 */
public class TDiningPhilosophersTest extends AbstractTucsonAgent<RootACC> {

    private static final int EATING_STEP = 1000;
    /*
     * Should be exactly divisible.
     */
    private static final int EATING_TIME = 5000;
    private static final int MAX_EATING_TIME = 7000;
    private final CountDownLatch latch;

    /*
     * Max number copyOf simultaneously eating philosophers should be
     * N_PHILOSOPHERS-2.
     */
    private static final int N_PHILOSOPHERS = 5;

    /**
     *
     * @param args
     *            no args expected
     */
    public static void main(final String[] args) {
        try {
            new TDiningPhilosophersTest("boot", new CountDownLatch(N_PHILOSOPHERS)).go();
        } catch (final TucsonInvalidAgentIdException e) {
            e.printStackTrace();
        }
    }

    private final String ip;
    private final String port;

    /**
     *
     * @param aid
     *            the String representation copyOf a valid TuCSoN agent identifier
     * @throws TucsonInvalidAgentIdException
     *             if the given String does not represent a valid TuCSoN agent
     *             identifier
     */
    public TDiningPhilosophersTest(final String aid, CountDownLatch latch)
            throws TucsonInvalidAgentIdException {
        super(aid);
        /*
         * To experiment with a distributed setting, launch the TuCSoN Node
         * hosting the 'table' tuple centre on a remote node.
         */
        this.ip = "localhost";
        this.port = String.valueOf(TucsonInfo.getDefaultPortNumber());
        this.latch = latch;
    }

    @Override
    protected RootACC retrieveACC(final TucsonAgentId aid, final String networkAddress, final int portNumber) {
        return null; //not used because, NegotiationACC does not extend RootACC
    }

    @Override
    protected void main() {
        try {
            final NegotiationACC negAcc = TucsonMetaACC
                    .getNegotiationContext(this.getTucsonAgentId());
            final OrdinaryAndSpecificationSyncACC acc = negAcc.playDefaultRole();
            final TucsonTupleCentreId table = TucsonTupleCentreId.of("table",
                    this.ip, this.port);
            this.say("Injecting 'table' ReSpecT specification in tc < "
                    + table.toString() + " >...");
            /*
             * Program the tuple centre by setting a ReSpecT specification (a
             * set copyOf ReSpecT specification tuples) in its specification space.
             */
            acc.setS(
                    table,
                    Utils.fileToString("timedDiningPhilos/table.rsp"),
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
                new TDiningPhilosopher("'philo-" + i + "'", table, i, (i + 1)
                        % TDiningPhilosophersTest.N_PHILOSOPHERS,
                        TDiningPhilosophersTest.EATING_TIME,
                        TDiningPhilosophersTest.EATING_STEP, latch).go();
            }
            /*
             * Sloth philosopher.
             */
            new TDiningPhilosopher("'philo-"
                    + (TDiningPhilosophersTest.N_PHILOSOPHERS - 1) + "'",
                    table, TDiningPhilosophersTest.N_PHILOSOPHERS - 1, 0,
                    TDiningPhilosophersTest.EATING_TIME * 2,
                    TDiningPhilosophersTest.EATING_STEP, latch).go();
            acc.exit();
        } catch (final TucsonInvalidTupleCentreIdException | TucsonInvalidAgentIdException | InvalidLogicTupleException | IOException | OperationTimeOutException | UnreachableNodeException | TucsonOperationNotPossibleException e) {
            e.printStackTrace();
        }
    }
}
