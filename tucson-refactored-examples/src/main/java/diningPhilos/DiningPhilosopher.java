package diningPhilos;

import alice.tuple.logic.LogicTuple;
import alice.tuple.logic.exceptions.InvalidLogicTupleException;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;
import alice.tuplecentre.respect.api.TupleCentreId;
import alice.tuplecentre.tucson.api.*;
import alice.tuplecentre.tucson.api.acc.NegotiationACC;
import alice.tuplecentre.tucson.api.acc.OrdinarySyncACC;
import alice.tuplecentre.tucson.api.acc.RootACC;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tuplecentre.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tuplecentre.tucson.api.exceptions.UnreachableNodeException;

/**
 * A Dining Philosopher: thinks and eats in an endless loop.
 *
 * @author ste (mailto: s.mariani@unibo.it)
 */
public class DiningPhilosopher extends AbstractTucsonAgent {

    private static final int EATING_TIME = 5000;
    private static final int THINKING_TIME = 5000;
    private OrdinarySyncACC acc;
    private final int chop1, chop2;
    private final TupleCentreId myTable;

    /**
     *
     * @param aid
     *            the String representation of this philosopher's TuCSoN agent
     *            identifier
     * @param table
     *            the identifier of the TuCSoN tuple centre representing the
     *            table
     * @param left
     *            an integer representing the left fork
     * @param right
     *            an integer representing the right fork
     * @throws TucsonInvalidAgentIdException
     *             if the given String does not represent a valid TuCSoN agent
     *             identifier
     */
    public DiningPhilosopher(final String aid, final TupleCentreId table,
            final int left, final int right)
            throws TucsonInvalidAgentIdException {
        super(aid);
        this.myTable = table;
        this.chop1 = left;
        this.chop2 = right;
    }

    private boolean acquireChops() {
        TucsonOperation op = null;
        try {
            /*
             * NB: The 2 needed chopsticks are "perceived" as a single item by
             * the philosophers, while the coordination medium correctly handle
             * them separately.
             */
            op = this.acc.in(
                    this.myTable,
                    LogicTuple.parse("chops(" + this.chop1 + "," + this.chop2
                            + ")"), null);
        } catch (final InvalidLogicTupleException e) {
            e.printStackTrace();
        } catch (final TucsonOperationNotPossibleException e) {
            e.printStackTrace();
        } catch (final UnreachableNodeException e) {
            e.printStackTrace();
        } catch (final OperationTimeOutException e) {
            e.printStackTrace();
        }
        if (op != null) {
            return op.isResultSuccess();
        }
        return false;
    }

    private void eat() {
        this.say("...gnam gnam...chomp chomp...munch munch...");
        try {
            Thread.sleep(DiningPhilosopher.EATING_TIME);
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void releaseChops() {
        try {
            this.acc.out(
                    this.myTable,
                    LogicTuple.parse("chops(" + this.chop1 + "," + this.chop2
                            + ")"), null);
        } catch (final InvalidLogicTupleException e) {
            e.printStackTrace();
        } catch (final TucsonOperationNotPossibleException e) {
            e.printStackTrace();
        } catch (final UnreachableNodeException e) {
            e.printStackTrace();
        } catch (final OperationTimeOutException e) {
            e.printStackTrace();
        }
    }

    private void think() {
        this.say("...mumble mumble...rat rat...mumble mumble...");
        try {
            Thread.sleep(DiningPhilosopher.THINKING_TIME);
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected RootACC retrieveACC(TucsonAgentId aid, String networkAddress, int portNumber) throws Exception {
        return null;
    }

    @Override
    protected void main() {
        try {
            final NegotiationACC negAcc = TucsonMetaACC
                    .getNegotiationContext(this.getTucsonAgentId());
            this.acc = negAcc.playDefaultRole();
        } catch (final OperationTimeOutException e) {
            e.printStackTrace();
        } catch (final TucsonInvalidAgentIdException e) {
            e.printStackTrace();
        } catch (final TucsonOperationNotPossibleException e) {
            e.printStackTrace();
        } catch (final UnreachableNodeException e) {
            e.printStackTrace();
        }
        // Ugly but effective, pardon me...
        while (true) {
            this.say("Now thinking...");
            this.think();
            this.say("I'm hungry, let's try to eat something...");
            /*
             * Try to get needed chopsticks.
             */
            if (this.acquireChops()) {
                /*
                 * If successful eat.
                 */
                this.eat();
                this.say("I'm done, wonderful meal :)");
                /*
                 * Then release chops.
                 */
                this.releaseChops();
            } else {
                this.say("I'm starving!");
            }
        }
    }
}
