/**
 *
 */
package uniform.swarms.ants;

import java.util.logging.Level;
import java.util.logging.Logger;
import alice.logictuple.LogicTuple;
import alice.logictuple.exceptions.InvalidLogicTupleException;
import alice.tucson.api.AbstractTucsonAgent;
import alice.tucson.api.acc.EnhancedSyncACC;
import alice.tucson.api.ITucsonOperation;
import alice.tucson.api.TucsonTupleCentreId;
import alice.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tucson.api.exceptions.TucsonInvalidTupleCentreIdException;
import alice.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tucson.api.exceptions.UnreachableNodeException;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;
import alice.tuplecentre.core.AbstractTupleCentreOperation;

/**
 * @author ste
 *
 */
public class Ant extends AbstractTucsonAgent {

    private final static Long TIMEOUT = 500L;

    private TucsonTupleCentreId tcid;
    private EnhancedSyncACC acc;

    private boolean stopped;
    private boolean carryingFood;

    /**
     * @param aid
     *            the TuCSoN agent identifier
     * @param netid
     *            the IP address of the TuCSoN node to interact with
     * @param port
     *            the TCP address of the TuCSoN node to interact with
     * @param tcName
     *            the name of the tuple centre to interact with
     * @throws TucsonInvalidAgentIdException
     *             if the given String does not represent a valid TuCSoN agent
     *             identifier
     */
    public Ant(final String aid, final String netid, final int port,
            final String tcName) throws TucsonInvalidAgentIdException {
        super(aid, netid, port);
        this.acc = null;
        try {
            this.tcid = new TucsonTupleCentreId(tcName, netid, "" + port);
        } catch (final TucsonInvalidTupleCentreIdException e) {
            this.err(e.getClass().getSimpleName() + ":" + tcName + ", " + netid
                    + ", " + port);
        }
        this.stopped = false;
        this.carryingFood = false;
    }

    /*
     * (non-Javadoc)
     * @see alice.tucson.api.TucsonAgent#main()
     */
    @Override
    protected void main() {

        this.acc = this.getContext();

        this.init();

        Logger.getAnonymousLogger().log(Level.INFO, this.myName() + ") Hello!");

        boolean isFood = false;
        LogicTuple direction = null;

        while (!this.stopped) {
            if (!this.carryingFood) {
                isFood = this.smellFood();
                if (isFood) {
                    this.pickFood();
                } else {
                    Logger.getAnonymousLogger().log(Level.INFO,
                            this.myName() + ") No food here :(");
                    direction = this.smellPheromone();
                    this.move(direction);
                    Logger.getAnonymousLogger().log(Level.INFO,
                            this.myName() + ") Wandering toward " + direction);
                }
            } else {
                if (this.isAnthill()) {
                    this.dropFood();
                } else {
                    direction = this.smellAnthill();
                    this.move(direction);
                    Logger.getAnonymousLogger().log(Level.INFO,
                            this.myName() + ") Bringing home food...");
                }
            }

            try {
                Thread.sleep(Ant.TIMEOUT);
            } catch (final InterruptedException e) {
                this.stopped = true;
            }

        }

        Logger.getAnonymousLogger().log(Level.INFO,
                this.myName() + ") Bye bye!");

    }

    /**
     *
     */
    private void init() {
        try {
            this.acc.out(this.tcid,
                    LogicTuple.parse("ant(" + this.myName() + ")"), null);
        } catch (InvalidLogicTupleException
                | TucsonOperationNotPossibleException
                | UnreachableNodeException | OperationTimeOutException e1) {
            this.err("Error while booting!");
        }
    }

    private boolean smellFood() {
        Logger.getAnonymousLogger().log(Level.INFO,
                this.myName() + ") Smelling food...");
        ITucsonOperation op = null;
        try {
            op = this.acc
                    .urdp(this.tcid, LogicTuple.parse("food"), Ant.TIMEOUT);
        } catch (InvalidLogicTupleException
                | TucsonOperationNotPossibleException
                | UnreachableNodeException | OperationTimeOutException e) {
            this.err("Error while smelling food: "
                    + e.getClass().getSimpleName());
        }
        return op.isResultSuccess();
    }

    private void pickFood() {
        ITucsonOperation op = null;
        try {
            op = this.acc
                    .uinp(this.tcid, LogicTuple.parse("food"), Ant.TIMEOUT);
        } catch (InvalidLogicTupleException
                | TucsonOperationNotPossibleException
                | UnreachableNodeException | OperationTimeOutException e) {
            this.err("Error while picking food: "
                    + e.getClass().getSimpleName());
        }
        this.carryingFood = op.isResultSuccess();
        Logger.getAnonymousLogger().log(Level.INFO,
                this.myName() + ") Food found :)");
    }

    private LogicTuple smellPheromone() {
        ITucsonOperation op = null;
        try {
            op = this.acc.urdp(this.tcid, LogicTuple.parse("nbr(NBR)"),
                    Ant.TIMEOUT);
        } catch (InvalidLogicTupleException
                | TucsonOperationNotPossibleException
                | UnreachableNodeException | OperationTimeOutException e) {
            this.err("Error while smelling pheromone: "
                    + e.getClass().getSimpleName());
        }
        if (op.isResultSuccess()) {
            return op.getLogicTupleResult();
        }
        this.err("Error while smelling pheromone: no nbrs found!");
        return null;
    }

    private void move(final LogicTuple direction) {

        ITucsonOperation op = null;

        try {
            op = this.acc
                    .uinp(this.tcid,
                            LogicTuple.parse("ant(" + this.myName() + ")"),
                            Ant.TIMEOUT);
        } catch (InvalidLogicTupleException
                | TucsonOperationNotPossibleException
                | UnreachableNodeException | OperationTimeOutException e) {
            this.err("Error while moving (1): " + e.getClass().getSimpleName());
        }

        if (op.isResultSuccess()) {

            TucsonTupleCentreId oldTcid = null;

            try {
                if (this.carryingFood) {
                    oldTcid = new TucsonTupleCentreId(this.tcid.getName(),
                            this.tcid.getNode(), "" + this.tcid.getPort());
                }
                this.tcid = new TucsonTupleCentreId(direction.getArg(0)
                        .getArg(0).toString(), direction.getArg(0).getArg(1)
                        .getArg(0).toString(), direction.getArg(0).getArg(1)
                        .getArg(1).toString());
            } catch (final TucsonInvalidTupleCentreIdException e) {
                this.err("Error while moving (2): "
                        + e.getClass().getSimpleName());
            }

            try {
                this.acc.out(this.tcid,
                        LogicTuple.parse("ant(" + this.myName() + ")"),
                        Ant.TIMEOUT);
                if (this.carryingFood) {
                    Logger.getAnonymousLogger().log(Level.INFO,
                            this.myName() + ") Leaving pheromone...");
                    this.acc.out(this.tcid,
                            LogicTuple.parse("nbr(" + oldTcid + ")"),
                            Ant.TIMEOUT);
                }
            } catch (InvalidLogicTupleException
                    | TucsonOperationNotPossibleException
                    | UnreachableNodeException | OperationTimeOutException e) {
                this.err("Error while moving (3): "
                        + e.getClass().getSimpleName());
            }

        } else {
            this.err("Error while moving: cannot find myself!");
        }

    }

    private boolean isAnthill() {
        return ("anthill".equals(this.tcid.getName()));
    }

    private void dropFood() {
        ITucsonOperation op = null;
        try {
            op = this.acc.out(this.tcid, LogicTuple.parse("stored_food"),
                    Ant.TIMEOUT);
        } catch (InvalidLogicTupleException
                | TucsonOperationNotPossibleException
                | UnreachableNodeException | OperationTimeOutException e) {
            this.err("Error while dropping food: "
                    + e.getClass().getSimpleName());
        }
        this.carryingFood = !op.isResultSuccess();
        Logger.getAnonymousLogger().log(Level.INFO,
                this.myName() + ") Job done!");
    }

    private LogicTuple smellAnthill() {
        ITucsonOperation op = null;
        try {
            op = this.acc.urdp(this.tcid, LogicTuple.parse("anthill(NEXT)"),
                    Ant.TIMEOUT);
        } catch (InvalidLogicTupleException
                | TucsonOperationNotPossibleException
                | UnreachableNodeException | OperationTimeOutException e) {
            this.err("Error while smelling anthill: "
                    + e.getClass().getSimpleName());
        }
        if (op.isResultSuccess()) {
            return op.getLogicTupleResult();
        }
        this.err("Error while smelling anthill: no anthill found!");
        return null;
    }

    private void err(final String msg) {
        System.err.println("[" + this.myName() + "]:" + msg);
    }

    /*
     * (non-Javadoc)
     * @see alice.tucson.api.TucsonAgent#operationCompleted(alice.tucson.api.
     * ITucsonOperation)
     */
    @Override
    public void operationCompleted(final ITucsonOperation op) {
        // Not used atm
    }

    /*
     * (non-Javadoc)
     * @see
     * alice.tucson.api.AbstractTucsonAgent#operationCompleted(alice.tuplecentre
     * .core.AbstractTupleCentreOperation)
     */
    @Override
    public void operationCompleted(final AbstractTupleCentreOperation arg0) {
        // Not used atm
    }

}
