/*
 * Copyright 1999-2019 Alma Mater Studiorum - Universita' di Bologna
 *
 * This file is part of MoK <http://mok.apice.unibo.it>.
 *
 *    MoK is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU Lesser General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    MoK is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU Lesser General Public License for more details.
 *
 *    You should have received a copy of the GNU Lesser General Public License
 *    along with MoK.  If not, see <https://www.gnu.org/licenses/lgpl.html>.
 *
 */
package uniform.dice;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import alice.logictuple.LogicTuple;
import alice.logictuple.Value;
import alice.logictuple.Var;
import alice.logictuple.exceptions.InvalidLogicTupleException;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;
import alice.tuplecentre.core.AbstractTupleCentreOperation;
import alice.tuplecentre.tucson.api.AbstractTucsonAgent;
import alice.tuplecentre.tucson.api.TucsonAgentId;
import alice.tuplecentre.tucson.api.TucsonMetaACC;
import alice.tuplecentre.tucson.api.TucsonOperation;
import alice.tuplecentre.tucson.api.TucsonTupleCentreId;
import alice.tuplecentre.tucson.api.acc.EnhancedSyncACC;
import alice.tuplecentre.tucson.api.acc.NegotiationACC;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidTupleCentreIdException;
import alice.tuplecentre.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tuplecentre.tucson.api.exceptions.UnreachableNodeException;
import alice.tuplecentre.tucson.network.exceptions.DialogInitializationException;
import alice.tuplecentre.tucson.service.TucsonNodeService;

/**
 * @author Stefano Mariani (mailto: s [dot] mariani [at] unibo [dot] it)
 *
 */
public final class DicePlayer extends AbstractTucsonAgent {

    /**
     * @param args
     *            no args expected.
     */
    public static void main(final String[] args) {
        try {
            node = new TucsonNodeService();
            node.install();
            while (!TucsonNodeService.isInstalled(1000)) {
                Logger.getAnonymousLogger().log(Level.INFO,
                        "Waiting for TuCSoN Node to boot...");
            }
            Logger.getAnonymousLogger().log(Level.INFO,
                    "...boot done, now configuring space...");
            final NegotiationACC negAcc = TucsonMetaACC
                    .getNegotiationContext(new TucsonAgentId("god"));
            final EnhancedSyncACC acc = negAcc.playDefaultRole();
            acc.outAll(
                    new TucsonTupleCentreId("dice", "localhost", "20504"),
                    LogicTuple
                            .parse("[face(1),face(2),face(3),face(4),face(5),face(6)]"),
                    Long.MAX_VALUE);
            Logger.getAnonymousLogger().log(Level.INFO,
                    "...configuration done, now starting agent...");
            new DicePlayer("roller").go();
        } catch (TucsonOperationNotPossibleException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnreachableNodeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (OperationTimeOutException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TucsonInvalidAgentIdException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidLogicTupleException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TucsonInvalidTupleCentreIdException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (DialogInitializationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private boolean stop;
    private TucsonTupleCentreId tcid;
    private Map<Integer, Integer> outcomes;
    private static TucsonNodeService node;

    /**
     * @param aid
     *            the TuCSoN agent identifier
     * @throws TucsonInvalidAgentIdException
     *             if the given String does not represent a valid TuCSoN agent
     *             identifier
     */
    public DicePlayer(String aid) throws TucsonInvalidAgentIdException {
        super(aid);
        this.stop = false;
        try {
            this.tcid = new TucsonTupleCentreId("dice", "localhost", "20504");
        } catch (TucsonInvalidTupleCentreIdException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        this.outcomes = new HashMap<>();
    }

    /*
     * (non-Javadoc)
     * @see alice.tuplecentre.tucson.api.AbstractTucsonAgent#main()
     */
    @Override
    protected void main() {
        final NegotiationACC negAcc = TucsonMetaACC.getNegotiationContext(this
                .getTucsonAgentId());
        try {
            final EnhancedSyncACC acc = negAcc.playDefaultRole();
            TucsonOperation op;
            LogicTuple template;
            final LogicTuple dieTuple = new LogicTuple("stahp", new Value(
                    this.myName()));
            int face;
            Integer nTimes = 1;
            while (!this.stop) {
                this.say("Checking termination...");
                op = acc.inp(this.tcid, dieTuple, null);
                if (op.isResultSuccess()) {
                    this.stop = true;
                    continue;
                }
                this.say("Rolling dice...");
                template = new LogicTuple("face", new Var());
                // op = acc.rd(this.tcid, template, Long.MAX_VALUE);
                op = acc.urd(this.tcid, template, Long.MAX_VALUE);
                if (op.isResultSuccess()) {
                    face = op.getLogicTupleResult().getArg(0).intValue();
                    this.say("...they see me rollin', they hatin': " + face);
                    nTimes = this.outcomes.get(face);
                    if (nTimes == null) {
                        this.outcomes.put(face, 1);
                    } else {
                        this.outcomes.put(face, ++nTimes);
                    }
                }
                printStats();
                Thread.sleep(500);
            }
        } catch (TucsonOperationNotPossibleException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnreachableNodeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (OperationTimeOutException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TucsonInvalidAgentIdException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        this.say("Someone killed me, bye!");
        printFinalStats();
        node.shutdown();
    }

    /**
     * 
     */
    private void printFinalStats() {
        this.say("Outcomes 'till now:");
        Integer t;
        Integer sum = 0;
        for (Integer v : this.outcomes.values()) {
            sum += v;
        }
        // Integer sum = this.outcomes.entrySet().parallelStream()
        // .mapToInt(i -> i.getValue()).sum();
        for (Integer i : this.outcomes.keySet()) {
            t = this.outcomes.get(i);
            this.say("\t Face " + i + " drawn " + t + " times (ratio: "
                    + String.format("%.2f", t.floatValue() / sum.floatValue())
                    + "%)");
        }
    }

    /**
     * 
     */
    private void printStats() {
        this.say("Outcomes 'till now:");
        for (Integer i : this.outcomes.keySet()) {
            this.say("\t Face " + i + " drawn " + this.outcomes.get(i)
                    + " times");
        }
    }

    /*
     * (non-Javadoc)
     * @see
     * alice.tuplecentre.tucson.api.AbstractTucsonAgent#operationCompleted(alice.tuplecentre
     * .core.AbstractTupleCentreOperation)
     */
    @Override
    public void operationCompleted(AbstractTupleCentreOperation arg0) {
        /*
         * not used atm
         */
    }

    /*
     * (non-Javadoc)
     * @see
     * alice.tuplecentre.tucson.api.AbstractTucsonAgent#operationCompleted(alice.tuplecentre.tucson.
     * api.TucsonOperation)
     */
    @Override
    public void operationCompleted(TucsonOperation arg0) {
        /*
         * not used atm
         */
    }

}
