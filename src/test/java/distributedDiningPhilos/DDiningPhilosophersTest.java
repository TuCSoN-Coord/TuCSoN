package distributedDiningPhilos;

import java.io.IOException;

import alice.tuple.logic.LogicTuples;
import alice.tuple.logic.exceptions.InvalidLogicTupleException;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;
import alice.tuplecentre.tucson.api.AbstractTucsonAgent;
import alice.tuplecentre.tucson.api.TucsonAgentId;
import alice.tuplecentre.tucson.api.TucsonMetaACC;
import alice.tuplecentre.tucson.api.TucsonTupleCentreId;
import alice.tuplecentre.tucson.api.TucsonTupleCentreIdDefault;
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
 */
public class DDiningPhilosophersTest extends AbstractTucsonAgent<RootACC> {

    private static final int N_PHILOSOPHERS = 10;

    /**
     *
     * @param args
     *            no args expected
     */
    public static void main(final String[] args) {
        try {
            new DDiningPhilosophersTest("boot").go();
        } catch (final TucsonInvalidAgentIdException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    private final String ip;
    private final int port;

    /**
     *
     * @param aid
     *            the String representation of a valid TuCSoN agent identifier
     * @throws TucsonInvalidAgentIdException
     *             if the given String does not represent a valid TuCSoN agent
     *             identifier
     */
    public DDiningPhilosophersTest(final String aid)
            throws TucsonInvalidAgentIdException {
        super(aid);
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
                seats[i] = new TucsonTupleCentreIdDefault("seat(" + i + "," + (i + 1)
                        % DDiningPhilosophersTest.N_PHILOSOPHERS + ")",
                        this.ip, String.valueOf(this.port));
                this.say("Injecting 'seat' ReSpecT specification in tc < "
                        + seats[i].toString() + " >...");
                acc.setS(
                        seats[i],
                        Utils.fileToString("distributedDiningPhilos/seat.rsp"),
                        null);
                acc.out(seats[i], LogicTuples.parse("philosopher(thinking)"),
                        null);
            }
            /* MOD: begin */
            final TucsonTupleCentreId table = new TucsonTupleCentreIdDefault("table",
                    this.ip, String.valueOf(this.port + 1));
            /* MOD: end */
            this.say("Injecting 'table' ReSpecT specification in tc < "
                    + table.toString() + " >...");
            acc.setS(
                    table,
                    Utils.fileToString("distributedDiningPhilos/table.rsp"),
                    null);
            for (int i = 0; i < DDiningPhilosophersTest.N_PHILOSOPHERS; i++) {
                acc.out(table, LogicTuples.parse("chop(" + i + ")"), null);
            }
            for (int i = 0; i < DDiningPhilosophersTest.N_PHILOSOPHERS; i++) {
                new DiningPhilosopher("'philo-" + i + "'", seats[i]).go();
            }
            acc.exit();
        } catch (final TucsonInvalidTupleCentreIdException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (final TucsonOperationNotPossibleException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (final UnreachableNodeException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (final OperationTimeOutException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (final IOException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (final InvalidLogicTupleException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (final TucsonInvalidAgentIdException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
