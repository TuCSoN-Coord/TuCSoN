package diningPhilos;

import alice.tuple.logic.LogicTuple;
import alice.tuple.logic.exceptions.InvalidLogicTupleException;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;
import alice.tuplecentre.tucson.api.AbstractTucsonAgent;
import alice.tuplecentre.tucson.api.TucsonAgentId;
import alice.tuplecentre.tucson.api.TucsonMetaACC;
import alice.tuplecentre.tucson.api.TucsonOperation;
import alice.tuplecentre.tucson.api.TucsonTupleCentreId;
import alice.tuplecentre.tucson.api.acc.NegotiationACC;
import alice.tuplecentre.tucson.api.acc.OrdinaryAndSpecificationSyncACC;
import alice.tuplecentre.tucson.api.acc.RootACC;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tuplecentre.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tuplecentre.tucson.api.exceptions.UnreachableNodeException;

import java.util.concurrent.CountDownLatch;

/**
 * A Dining Philosopher: thinks and eats in an endless loop.
 *
 * @author ste (mailto: s.mariani@unibo.it)
 * @author (contributor) Stefano Bernagozzi (stefano.bernagozzi@studio.unibo.it)
 */
public class DiningPhilosopher extends AbstractTucsonAgent<RootACC> {

    private static final int EATING_TIME = 1;//5000;
    private static final int THINKING_TIME = 1;//5000;
    private OrdinaryAndSpecificationSyncACC acc;
    private final int chop1, chop2;
    private final TucsonTupleCentreId myTable;
    private final CountDownLatch latch;

    /**
     *
     * @param aid
     *            the String representation copyOf this philosopher's TuCSoN agent
     *            identifier
     * @param table
     *            the identifier copyOf the TuCSoN tuple centre representing the
     *            table
     * @param left
     *            an integer representing the left fork
     * @param right
     *            an integer representing the right fork
     * @throws TucsonInvalidAgentIdException
     *             if the given String does not represent a valid TuCSoN agent
     *             identifier
     */
    public DiningPhilosopher(final String aid, final TucsonTupleCentreId table,
                             final int left, final int right, final CountDownLatch latch)
            throws TucsonInvalidAgentIdException {
        super(aid);
        this.myTable = table;
        this.chop1 = left;
        this.chop2 = right;
        this.latch = latch;
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
        } catch (final InvalidLogicTupleException | OperationTimeOutException | UnreachableNodeException | TucsonOperationNotPossibleException e) {
            e.printStackTrace();
        }
        return op != null && op.isResultSuccess();
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
        } catch (final InvalidLogicTupleException | OperationTimeOutException | UnreachableNodeException | TucsonOperationNotPossibleException e) {
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
    protected RootACC retrieveACC(final TucsonAgentId aid, final String networkAddress, final int portNumber) {
        return null; //not used because, NegotiationACC does not extend RootACC
    }

    @Override
    protected void main() {
        try {
            final NegotiationACC negAcc = TucsonMetaACC
                    .getNegotiationContext(this.getTucsonAgentId());
            this.acc = negAcc.playDefaultRole();
        } catch (final OperationTimeOutException | UnreachableNodeException | TucsonOperationNotPossibleException | TucsonInvalidAgentIdException e) {
            e.printStackTrace();
        }
        // this.acc = this.getACC();
        // Ugly but effective, pardon me...
        int numberMealEaten = 0;
        int maxMealToEat = 5;
        while(true){
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
                numberMealEaten ++;
                /*
                 * Then release chops.
                 */
                this.releaseChops();
                if(numberMealEaten == maxMealToEat) {
                    break;
                }
            } else {
                this.say("I'm starving!");
            }
        }
        this.say("I've eaten all");
        latch.countDown();
    }
}
