package timedDiningPhilos;

import alice.logictuple.LogicTuple;
import alice.logictuple.exceptions.InvalidLogicTupleException;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;
import alice.tuplecentre.core.AbstractTupleCentreOperation;
import alice.tuplecentre.tucson.api.AbstractTucsonAgent;
import alice.tuplecentre.tucson.api.TucsonMetaACC;
import alice.tuplecentre.tucson.api.TucsonOperation;
import alice.tuplecentre.tucson.api.TucsonTupleCentreId;
import alice.tuplecentre.tucson.api.acc.NegotiationACC;
import alice.tuplecentre.tucson.api.acc.OrdinaryAndSpecificationSyncACC;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tuplecentre.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tuplecentre.tucson.api.exceptions.UnreachableNodeException;

/**
 * A Dining Philosopher: thinks and eats in an endless loop.
 *
 * @author ste (mailto: s.mariani@unibo.it)
 */
public class DiningPhilosopher extends AbstractTucsonAgent {

    private static final int THINK_TIME = 5000;
    private OrdinaryAndSpecificationSyncACC acc;
    private final int chop1, chop2;
    private final TucsonTupleCentreId myTable;
    private final int time, step;

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
     * @param eatingTime
     *            the philosopher's eating time
     * @param eatingStep
     *            the philosopher's eating step
     * @throws TucsonInvalidAgentIdException
     *             if the given String does not represent a valid TuCSoN agent
     *             identifier
     */
    public DiningPhilosopher(final String aid, final TucsonTupleCentreId table,
            final int left, final int right, final int eatingTime,
            final int eatingStep) throws TucsonInvalidAgentIdException {
        super(aid);
        this.myTable = table;
        this.chop1 = left;
        this.chop2 = right;
        this.time = eatingTime;
        this.step = eatingStep;
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
        } catch (final InterruptedException e) {
            e.printStackTrace();
        } catch (final InvalidLogicTupleException e) {
            e.printStackTrace();
        } catch (final TucsonOperationNotPossibleException e) {
            e.printStackTrace();
        } catch (final UnreachableNodeException e) {
            e.printStackTrace();
        } catch (final OperationTimeOutException e) {
            e.printStackTrace();
        }
        if (op != null && op.isResultSuccess()) {
            return op.isResultSuccess();
        }
        return false;
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
            Thread.sleep(DiningPhilosopher.THINK_TIME);
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
        } catch (final TucsonOperationNotPossibleException e) {
            e.printStackTrace();
        } catch (final UnreachableNodeException e) {
            e.printStackTrace();
        } catch (final OperationTimeOutException e) {
            e.printStackTrace();
        } catch (final TucsonInvalidAgentIdException e) {
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
                if (this.eat()) {
                    this.say("I'm done, wonderful meal :)");
                    /*
                     * Then release chops.
                     */
                    this.releaseChops();
                } else {
                    this.say("OMG my chopsticks disappeared!");
                }
            } else {
                this.say("I'm starving!");
            }
        }
    }
}
