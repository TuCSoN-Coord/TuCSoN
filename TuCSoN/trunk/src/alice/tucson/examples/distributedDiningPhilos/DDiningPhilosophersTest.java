package alice.tucson.examples.distributedDiningPhilos;

import java.io.IOException;

import alice.logictuple.LogicTuple;
import alice.logictuple.exceptions.InvalidLogicTupleException;
import alice.tucson.api.AbstractTucsonAgent;
import alice.tucson.api.ITucsonOperation;
import alice.tucson.api.SynchACC;
import alice.tucson.api.TucsonTupleCentreId;
import alice.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tucson.api.exceptions.TucsonInvalidTupleCentreIdException;
import alice.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tucson.api.exceptions.UnreachableNodeException;
import alice.tucson.examples.utilities.Utils;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;
import alice.tuplecentre.core.AbstractTupleCentreOperation;

/**
 * 
 * 
 * @author ste (mailto: s.mariani@unibo.it)
 */
public class DDiningPhilosophersTest extends AbstractTucsonAgent {

    private static final int DEF_PORT = 20504;
    private static final int N_PHILOSOPHERS = 5;

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
    public void operationCompleted(final ITucsonOperation arg0) {
        /*
         * 
         */
    }

    @Override
    protected void main() {
        final SynchACC acc = this.getContext();
        try {
            final TucsonTupleCentreId[] seats =
                    new TucsonTupleCentreId[DDiningPhilosophersTest.N_PHILOSOPHERS];
            for (int i = 0; i < DDiningPhilosophersTest.N_PHILOSOPHERS; i++) {
                seats[i] =
                        new TucsonTupleCentreId(
                                "seat("
                                        + i
                                        + ","
                                        + ((i + 1) % DDiningPhilosophersTest.N_PHILOSOPHERS)
                                        + ")", this.ip,
                                String.valueOf(this.port));
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
            final TucsonTupleCentreId table =
                    new TucsonTupleCentreId("table", this.ip,
                            String.valueOf(this.port + 1));
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

  //modified by SANGIO
	@Override
	public void operationCompleted(AbstractTupleCentreOperation op) {
		// TODO Auto-generated method stub
		
	}

}
