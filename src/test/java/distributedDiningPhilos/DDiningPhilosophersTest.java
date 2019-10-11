package distributedDiningPhilos;

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
 * TODO add documentation
 *
 * @author ste (mailto: s.mariani@unibo.it)
 * @author (contributor) Stefano Bernagozzi (stefano.bernagozzi@studio.unibo.it)
 */
public class DDiningPhilosophersTest extends AbstractTucsonAgent<RootACC> {

    private static final int N_PHILOSOPHERS = 10;
    private final CountDownLatch latch;

    /**
     *
     * @param args
     *            no args expected
     */
    public static void main(final String[] args) {
        try {
            new DDiningPhilosophersTest("boot", new CountDownLatch(N_PHILOSOPHERS)).go();
        } catch (final TucsonInvalidAgentIdException e) {
            e.printStackTrace();
        }
    }

    private final String ip;
    private final int port;

    /**
     *
     * @param aid
     *            the String representation copyOf a valid TuCSoN agent identifier
     * @throws TucsonInvalidAgentIdException
     *             if the given String does not represent a valid TuCSoN agent
     *             identifier
     */
    public DDiningPhilosophersTest(final String aid, CountDownLatch latch)
            throws TucsonInvalidAgentIdException {
        super(aid);
        this.latch = latch;
        this.ip = "localhost";
        this.port = TucsonInfo.getDefaultPortNumber();
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
            final TucsonTupleCentreId[] seats = new TucsonTupleCentreId[DDiningPhilosophersTest.N_PHILOSOPHERS];
            for (int i = 0; i < DDiningPhilosophersTest.N_PHILOSOPHERS; i++) {
                seats[i] = TucsonTupleCentreId.of("seat(" + i + "," + (i + 1)
                        % DDiningPhilosophersTest.N_PHILOSOPHERS + ")",
                        this.ip, String.valueOf(this.port));
                this.say("Injecting 'seat' ReSpecT specification in tc < "
                        + seats[i].toString() + " >...");
                acc.setS(
                        seats[i],
                        Utils.fileToString("distributedDiningPhilos/seat.rsp"),
                        null);
                acc.out(seats[i], LogicTuple.parse("philosopher(thinking)"),
                        null);
            }
            /* MOD: begin */
            final TucsonTupleCentreId table = TucsonTupleCentreId.of("table",
                    this.ip, String.valueOf(this.port + 1));
            /* MOD: end */
            this.say("Injecting 'table' ReSpecT specification in tc < "
                    + table.toString() + " >...");
            acc.setS(
                    table,
                    Utils.fileToString("distributedDiningPhilos/table.rsp"),
                    null);
            for (int i = 0; i < DDiningPhilosophersTest.N_PHILOSOPHERS; i++) {
                acc.out(table, LogicTuple.parse("chop(" + i + ")"), null);
            }
            for (int i = 0; i < DDiningPhilosophersTest.N_PHILOSOPHERS; i++) {
                new DDiningPhilosopher("'philo-" + i + "'", seats[i], latch).go();
            }
            acc.exit();
        } catch (final TucsonInvalidTupleCentreIdException | TucsonInvalidAgentIdException | InvalidLogicTupleException | IOException | OperationTimeOutException | UnreachableNodeException | TucsonOperationNotPossibleException e) {
            e.printStackTrace();
        }
    }
}
