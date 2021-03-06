package timedDiningPhilos;

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
 */
public class TDiningPhilosopher extends AbstractTucsonAgent<RootACC> {

    private static final int THINK_TIME = 1;//5000;
    private OrdinaryAndSpecificationSyncACC acc;
    private final int chop1, chop2;
    private final TucsonTupleCentreId myTable;
    private final int time, step;
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
     * @param eatingTime
     *            the philosopher's eating time
     * @param eatingStep
     *            the philosopher's eating step
     * @throws TucsonInvalidAgentIdException
     *             if the given String does not represent a valid TuCSoN agent
     *             identifier
     */
    public TDiningPhilosopher(final String aid, final TucsonTupleCentreId table,
                              final int left, final int right, final int eatingTime,
                              final int eatingStep, CountDownLatch latch) throws TucsonInvalidAgentIdException {
        super(aid);
        this.myTable = table;
        this.chop1 = left;
        this.chop2 = right;
        this.time = eatingTime;
        this.step = eatingStep;
        this.latch = latch;
    }

    @Override
    protected RootACC retrieveACC(final TucsonAgentId aid, final String networkAddress, final int portNumber) {
        return null; //not used because, NegotiationACC does not extend RootACC
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

    private boolean eat() {
        this.say("...gnam gnam...chomp chomp...munch munch...");
        TucsonOperation op = null;
        try {
            for (int i = 0; i < this.time / this.step; i++) {
                Thread.sleep(this.step);
                op = this.acc.rdp(
                        this.myTable,
                        LogicTuple.parse("used(" + this.chop1 + ","
                                + this.chop2 + ",_)"), null);
                if (!op.isResultSuccess()) {
                    break;
                }
            }
        } catch (final InterruptedException | OperationTimeOutException | UnreachableNodeException | TucsonOperationNotPossibleException | InvalidLogicTupleException e) {
            e.printStackTrace();
        }
        return op != null && op.isResultSuccess() && op.isResultSuccess();
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
            Thread.sleep(TDiningPhilosopher.THINK_TIME);
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void main() {
        final NegotiationACC negAcc = TucsonMetaACC.getNegotiationContext(this
                .getTucsonAgentId());
        try {
            this.acc = negAcc.playDefaultRole();
        } catch (final TucsonOperationNotPossibleException | TucsonInvalidAgentIdException | OperationTimeOutException | UnreachableNodeException e) {
            e.printStackTrace();
        }
        // Ugly but effective, pardon me...
        int numberMealEaten = 0;
        int maxMealToEat = 5;
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
                if (this.eat()) {
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
                    this.say("OMG my chopsticks disappeared!");
                }
            } else {
                this.say("I'm starving!");
            }
        }
        this.say("I've eaten all");
        latch.countDown();
    }
}
