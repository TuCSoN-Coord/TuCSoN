package alice.tucson.examples.distributedDiningPhilos;

import alice.logictuple.LogicTuple;
import alice.logictuple.exceptions.InvalidLogicTupleException;
import alice.tucson.api.AbstractTucsonAgent;
import alice.tucson.api.ITucsonOperation;
import alice.tucson.api.NegotiationACC;
import alice.tucson.api.SynchACC;
import alice.tucson.api.TucsonMetaACC;
import alice.tucson.api.TucsonTupleCentreId;
import alice.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tucson.api.exceptions.UnreachableNodeException;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;
import alice.tuplecentre.core.AbstractTupleCentreOperation;

/**
 *
 * @author ste (mailto: s.mariani@unibo.it)
 *
 */
public class DiningPhilosopher extends AbstractTucsonAgent {

    private static final int EATING_TIME = 5000;
    private static final int THINKING_TIME = 5000;
    private final TucsonTupleCentreId mySeat;

    /**
     *
     * @param aid
     *            the String representation of this philosopher's TuCSoN agent
     *            identifier
     * @param seat
     *            the identifier of the TuCSoN tuple centre representing the
     *            philosopher's seat
     * @throws TucsonInvalidAgentIdException
     *             if the given String does not represent a valid TuCSoN agent
     *             identifier
     */
    public DiningPhilosopher(final String aid, final TucsonTupleCentreId seat)
            throws TucsonInvalidAgentIdException {
        super(aid);
        this.mySeat = seat;
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

    private void eating() {
        this.say("...gnam gnam...chomp chomp...munch munch...");
        try {
            Thread.sleep(DiningPhilosopher.EATING_TIME);
        } catch (final InterruptedException e) {
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
    protected void main() {
        final NegotiationACC negAcc = TucsonMetaACC.getNegotiationContext(this
                .getTucsonAgentId());
        SynchACC acc = null;
        try {
            acc = negAcc.playDefaultRole();
        } catch (final TucsonOperationNotPossibleException e) {
            e.printStackTrace();
        } catch (final UnreachableNodeException e) {
            e.printStackTrace();
        } catch (final OperationTimeOutException e) {
            e.printStackTrace();
        } catch (final TucsonInvalidAgentIdException e) {
            e.printStackTrace();
        }
        // final SynchACC acc = this.getContext();
        ITucsonOperation op;
        // Ugly but effective, pardon me...
        while (true) {
            try {
                op = acc.rd(this.mySeat,
                        LogicTuple.parse("philosopher(thinking)"), null);
                if (op.isResultSuccess()) {
                    this.say("Now thinking...");
                    this.think();
                } else {
                    this.say("I'm exploding!");
                }
                this.say("I'm hungry, let's try to eat something...");
                acc.out(this.mySeat, LogicTuple.parse("wanna_eat"), null);
                op = acc.rd(this.mySeat,
                        LogicTuple.parse("philosopher(eating)"), null);
                if (op.isResultSuccess()) {
                    this.eating();
                    this.say("I'm done, wonderful meal :)");
                    acc.out(this.mySeat, LogicTuple.parse("wanna_think"), null);
                } else {
                    this.say("I'm starving!");
                }
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
    }
}
