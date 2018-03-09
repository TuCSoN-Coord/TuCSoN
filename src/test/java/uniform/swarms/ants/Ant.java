/**
 *
 */
package uniform.swarms.ants;

import java.util.logging.Level;
import java.util.logging.Logger;

import alice.logictuple.LogicTuple;
import alice.logictuple.exceptions.InvalidLogicTupleException;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;
import alice.tuplecentre.tucson.api.AbstractTucsonAgent;
import alice.tuplecentre.tucson.api.TucsonAgentId;
import alice.tuplecentre.tucson.api.TucsonMetaACC;
import alice.tuplecentre.tucson.api.TucsonOperation;
import alice.tuplecentre.tucson.api.TucsonTupleCentreId;
import alice.tuplecentre.tucson.api.acc.EnhancedSyncACC;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidTupleCentreIdException;
import alice.tuplecentre.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tuplecentre.tucson.api.exceptions.UnreachableNodeException;

/**
 * @author ste
 *
 */
public class Ant extends AbstractTucsonAgent<EnhancedSyncACC> {

    private final static Long TIMEOUT = 500L;

    private TucsonTupleCentreId tcid;

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
     * @see alice.tuplecentre.tucson.api.TucsonAgent#main()
     */
    @Override
    protected void main() {

        this.init();

        Logger.getAnonymousLogger().log(Level.INFO, this.getTucsonAgentId().getAgentName() + ") Hello!");

        boolean isFood = false;
        LogicTuple direction = null;

        while (!this.stopped) {
            if (!this.carryingFood) {
                isFood = this.smellFood();
                if (isFood) {
                    this.pickFood();
                } else {
                    Logger.getAnonymousLogger().log(Level.INFO,
                            this.getTucsonAgentId().getAgentName() + ") No food here :(");
                    direction = this.smellPheromone();
                    this.move(direction);
                    Logger.getAnonymousLogger().log(Level.INFO,
                            this.getTucsonAgentId().getAgentName() + ") Wandering toward " + direction);
                }
            } else {
                if (this.isAnthill()) {
                    this.dropFood();
                } else {
                    direction = this.smellAnthill();
                    this.move(direction);
                    Logger.getAnonymousLogger().log(Level.INFO,
                            this.getTucsonAgentId().getAgentName() + ") Bringing home food...");
                }
            }

            try {
                Thread.sleep(Ant.TIMEOUT);
            } catch (final InterruptedException e) {
                this.stopped = true;
            }

        }

        Logger.getAnonymousLogger().log(Level.INFO,
                this.getTucsonAgentId().getAgentName() + ") Bye bye!");

    }

    /**
     *
     */
    private void init() {
        try {
            getACC().out(this.tcid,
                    LogicTuple.parse("ant(" + this.getTucsonAgentId().getAgentName() + ")"), null);
        } catch (InvalidLogicTupleException
                | TucsonOperationNotPossibleException
                | UnreachableNodeException | OperationTimeOutException e1) {
            this.err("Error while booting!");
        }
    }

    private boolean smellFood() {
        Logger.getAnonymousLogger().log(Level.INFO,
                this.getTucsonAgentId().getAgentName() + ") Smelling food...");
        TucsonOperation op = null;
        try {
            op = getACC()
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
        TucsonOperation op = null;
        try {
            op = getACC()
                    .uinp(this.tcid, LogicTuple.parse("food"), Ant.TIMEOUT);
        } catch (InvalidLogicTupleException
                | TucsonOperationNotPossibleException
                | UnreachableNodeException | OperationTimeOutException e) {
            this.err("Error while picking food: "
                    + e.getClass().getSimpleName());
        }
        this.carryingFood = op.isResultSuccess();
        Logger.getAnonymousLogger().log(Level.INFO,
                this.getTucsonAgentId().getAgentName() + ") Food found :)");
    }

    private LogicTuple smellPheromone() {
        TucsonOperation op = null;
        try {
            op = getACC().urdp(this.tcid, LogicTuple.parse("nbr(NBR)"),
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

        TucsonOperation op = null;

        try {
            op = getACC()
                    .uinp(this.tcid,
                            LogicTuple.parse("ant(" + this.getTucsonAgentId().getAgentName() + ")"),
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
                getACC().out(this.tcid,
                        LogicTuple.parse("ant(" + this.getTucsonAgentId().getAgentName() + ")"),
                        Ant.TIMEOUT);
                if (this.carryingFood) {
                    Logger.getAnonymousLogger().log(Level.INFO,
                            this.getTucsonAgentId().getAgentName() + ") Leaving pheromone...");
                    getACC().out(this.tcid,
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
        TucsonOperation op = null;
        try {
            op = getACC().out(this.tcid, LogicTuple.parse("stored_food"),
                    Ant.TIMEOUT);
        } catch (InvalidLogicTupleException
                | TucsonOperationNotPossibleException
                | UnreachableNodeException | OperationTimeOutException e) {
            this.err("Error while dropping food: "
                    + e.getClass().getSimpleName());
        }
        this.carryingFood = !op.isResultSuccess();
        Logger.getAnonymousLogger().log(Level.INFO,
                this.getTucsonAgentId().getAgentName() + ") Job done!");
    }

    private LogicTuple smellAnthill() {
        TucsonOperation op = null;
        try {
            op = getACC().urdp(this.tcid, LogicTuple.parse("anthill(NEXT)"),
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
        System.err.println("[" + this.getTucsonAgentId().getAgentName() + "]:" + msg);
    }

    @Override
    protected EnhancedSyncACC retrieveACC(final TucsonAgentId aid, final String networkAddress, final int portNumber) {
        return TucsonMetaACC.getContext(aid, networkAddress, portNumber);
    }
}
