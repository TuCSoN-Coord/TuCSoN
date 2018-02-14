package diningPhilos;

import java.io.IOException;
import alice.logictuple.LogicTuple;
import alice.logictuple.exceptions.InvalidLogicTupleException;
import alice.tucson.api.AbstractTucsonAgent;
import alice.tucson.api.ITucsonOperation;
import alice.tucson.api.NegotiationACC;
import alice.tucson.api.SynchACC;
import alice.tucson.api.TucsonMetaACC;
import alice.tucson.api.TucsonTupleCentreId;
import alice.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tucson.api.exceptions.TucsonInvalidTupleCentreIdException;
import alice.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tucson.api.exceptions.UnreachableNodeException;
import alice.tucson.utilities.Utils;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;
import alice.tuplecentre.core.AbstractTupleCentreOperation;

/**
 * Classic Dining Philosophers coordination problem tackled by adopting a clear
 * separation of concerns between coordinables (philosophers) and coordination
 * medium (table) thanks to TuCSoN ReSpecT tuple centres programmability.
 *
 * @author ste (mailto: s.mariani@unibo.it)
 */
public class DiningPhilosophersTest extends AbstractTucsonAgent {

    /*
     * Max number of simultaneously eating philosophers should be
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
            new DiningPhilosophersTest("boot").go();
        } catch (final TucsonInvalidAgentIdException e) {
            e.printStackTrace();
        }
    }

    private final String ip;
    private final String port;

    /**
     *
     * @param aid
     *            the String representation of a valid TuCSoN agent identifier
     * @throws TucsonInvalidAgentIdException
     *             if the given String does not represent a valid TuCSoN agent
     *             identifier
     */
    public DiningPhilosophersTest(final String aid)
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
    public void operationCompleted(final AbstractTupleCentreOperation op) {
        /*
         * not used atm
         */
    }

    @Override
    public void operationCompleted(final ITucsonOperation arg0) {
        /*
         * not used atm
         */
    }

    @Override
    protected void main() {
        try {
            final NegotiationACC negAcc = TucsonMetaACC
                    .getNegotiationContext(this.getTucsonAgentId());
            final SynchACC acc = negAcc.playDefaultRole();

            final TucsonTupleCentreId table = new TucsonTupleCentreId("table",
                    this.ip, this.port);
            this.say("Injecting 'table' ReSpecT specification in tc < "
                    + table.toString() + " >...");
            /*
             * Program the tuple centre by setting a ReSpecT specification (a
             * set of ReSpecT specification tuples) in its specification space.
             */
            acc.setS(
                    table,
                    Utils.fileToString("alice/tucson/examples/diningPhilos/table.rsp"),
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
                        % DiningPhilosophersTest.N_PHILOSOPHERS).go();
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
