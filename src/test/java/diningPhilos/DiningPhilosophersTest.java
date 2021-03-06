package diningPhilos;

import java.io.IOException;
import java.util.concurrent.*;

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
import org.junit.Test;

/**
 * Classic Dining Philosophers coordination problem tackled by adopting a clear
 * separation copyOf concerns between coordinables (philosophers) and coordination
 * medium (table) thanks to TuCSoN ReSpecT tuple centres programmability.
 *
 * @author ste (mailto: s.mariani@unibo.it)
 * @author (contributor) Stefano Bernagozzi (stefano.bernagozzi@studio.unibo.it)
 */
public class DiningPhilosophersTest extends AbstractTucsonAgent<RootACC> {

    /*
     * Max number copyOf simultaneously eating philosophers should be
     * N_PHILOSOPHERS-2.
     */
    private final CountDownLatch latch;
    private static final int N_PHILOSOPHERS = 5;

    /**
     *
     *            no args expected
     */

    public static void main(final String[] args){
        try {
            new DiningPhilosophersTest("boot", new CountDownLatch(N_PHILOSOPHERS)).go();
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
    public DiningPhilosophersTest(final String aid, CountDownLatch latch)
            throws TucsonInvalidAgentIdException {
        super(aid);
        /*
         * To experiment with a distributed setting, launch the TuCSoN Node
         * hosting the 'table' tuple centre on a remote node.
         */
        this.latch = latch;

        this.ip = "localhost";
        this.port = String.valueOf(TucsonInfo.getDefaultPortNumber());
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
                    Utils.fileToString("diningPhilos/table.rsp"),
                    null);
            for (int i = 0; i < DiningPhilosophersTest.N_PHILOSOPHERS; i++) {
                /*
                 * Init chopsticks required to eat.
                 */
                acc.out(table, LogicTuple.parse("chop(" + i + ")"), null);
            }
            for (int i = 0; i < DiningPhilosophersTest.N_PHILOSOPHERS; i++) {
                /*
                 * Start philosophers by telling them which chopsticks pair they
                 * need.
                 */
                new DiningPhilosopher("'philo-" + i + "'", table, i, (i + 1)
                        % DiningPhilosophersTest.N_PHILOSOPHERS, latch).go();
            }
            acc.exit();
        } catch (final TucsonInvalidTupleCentreIdException | TucsonInvalidAgentIdException | InvalidLogicTupleException | IOException | OperationTimeOutException | UnreachableNodeException | TucsonOperationNotPossibleException e) {
            e.printStackTrace();
        }
    }
}
