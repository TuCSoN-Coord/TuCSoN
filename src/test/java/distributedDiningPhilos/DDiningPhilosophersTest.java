package distributedDiningPhilos;

import java.io.IOException;

import alice.logictuple.LogicTuple;
import alice.logictuple.exceptions.InvalidLogicTupleException;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;
import alice.tuplecentre.core.AbstractTupleCentreOperation;
import alice.tuplecentre.tucson.api.AbstractTucsonAgent;
import alice.tuplecentre.tucson.api.TucsonMetaACC;
import alice.tuplecentre.tucson.api.TucsonOperation;
import alice.tuplecentre.tucson.api.TucsonTupleCentreId;
import alice.tuplecentre.tucson.api.TucsonTupleCentreIdDefault;
import alice.tuplecentre.tucson.api.acc.NegotiationACC;
import alice.tuplecentre.tucson.api.acc.OrdinaryAndSpecificationSyncACC;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidTupleCentreIdException;
import alice.tuplecentre.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tuplecentre.tucson.api.exceptions.UnreachableNodeException;
import alice.tuplecentre.tucson.utilities.Utils;

/**
 *
 *
 * @author ste (mailto: s.mariani@unibo.it)
 */
public class DDiningPhilosophersTest extends AbstractTucsonAgent {

    private static final int DEF_PORT = 20504;
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
            e.printStackTrace();
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
        this.port = DDiningPhilosophersTest.DEF_PORT;
    }

    @Override
    public void operationCompleted(final AbstractTupleCentreOperation op) {
        /*
         * not used atm
         */
    }

    @Override
    public void operationCompleted(final TucsonOperation arg0) {
        /*
         * not used atm
         */
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
                        Utils.fileToString("alice/tucson/examples/distributedDiningPhilos/seat.rsp"),
                        null);
                acc.out(seats[i], LogicTuple.parse("philosopher(thinking)"),
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
                    Utils.fileToString("alice/tucson/examples/distributedDiningPhilos/table.rsp"),
                    null);
            for (int i = 0; i < DDiningPhilosophersTest.N_PHILOSOPHERS; i++) {
                acc.out(table, LogicTuple.parse("chop(" + i + ")"), null);
            }
            for (int i = 0; i < DDiningPhilosophersTest.N_PHILOSOPHERS; i++) {
                new DiningPhilosopher("'philo-" + i + "'", seats[i]).go();
            }
            acc.exit();
        } catch (final TucsonInvalidTupleCentreIdException e) {
            e.printStackTrace();
        } catch (final TucsonOperationNotPossibleException e) {
            e.printStackTrace();
        } catch (final UnreachableNodeException e) {
            e.printStackTrace();
        } catch (final OperationTimeOutException e) {
            e.printStackTrace();
        } catch (final IOException e) {
            e.printStackTrace();
        } catch (final InvalidLogicTupleException e) {
            e.printStackTrace();
        } catch (final TucsonInvalidAgentIdException e) {
            e.printStackTrace();
        }
    }
}
