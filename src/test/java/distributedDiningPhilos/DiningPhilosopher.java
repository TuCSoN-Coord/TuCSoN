package distributedDiningPhilos;

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

import java.util.Objects;

/**
 *
 * @author ste (mailto: s.mariani@unibo.it)
 *
 */
public class DiningPhilosopher extends AbstractTucsonAgent<RootACC> {

    private static final int EATING_TIME = 5000;
    private static final int THINKING_TIME = 5000;
    private final TucsonTupleCentreId mySeat;

    /**
     *
     * @param aid
     *            the String representation copyOf this philosopher's TuCSoN agent
     *            identifier
     * @param seat
     *            the identifier copyOf the TuCSoN tuple centre representing the
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
    protected RootACC retrieveACC(final TucsonAgentId aid, final String networkAddress, final int portNumber) {
        return null; //not used because, NegotiationACC does not extend RootACC
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
        OrdinaryAndSpecificationSyncACC acc = null;
        try {
            acc = negAcc.playDefaultRole();
        } catch (final TucsonOperationNotPossibleException | TucsonInvalidAgentIdException | OperationTimeOutException | UnreachableNodeException e) {
            e.printStackTrace();
        }
        // final OrdinaryAndSpecificationSyncACC acc = this.getACC();
        TucsonOperation op;
        // Ugly but effective, pardon me...
        while (true) {
            try {
                op = Objects.requireNonNull(acc).rd(this.mySeat,
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
            } catch (final InvalidLogicTupleException | OperationTimeOutException | UnreachableNodeException | TucsonOperationNotPossibleException e) {
                e.printStackTrace();
            }
        }
    }
}
