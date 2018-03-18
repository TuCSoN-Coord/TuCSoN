/*
 * Copyright 1999-2014 Alma Mater Studiorum - Universita' di Bologna
 *
 * This file is part of TuCSoN <http://tucson.unibo.it>.
 *
 *    TuCSoN is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU Lesser General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    TuCSoN is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU Lesser General Public License for more details.
 *
 *    You should have received a copy of the GNU Lesser General Public License
 *    along with TuCSoN.  If not, see <https://www.gnu.org/licenses/lgpl.html>.
 *
 */
package asynchAPI;

import alice.tuple.logic.LogicTuple;
import alice.tuple.logic.LogicTuples;
import alice.tuple.logic.exceptions.InvalidLogicTupleException;
import alice.tuplecentre.api.exceptions.InvalidOperationException;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;
import alice.tuplecentre.core.AbstractTupleCentreOperation;
import alice.tuplecentre.tucson.api.AbstractTucsonAgent;
import alice.tuplecentre.tucson.api.TucsonAgentId;
import alice.tuplecentre.tucson.api.TucsonMetaACC;
import alice.tuplecentre.tucson.api.TucsonOperation;
import alice.tuplecentre.tucson.api.TucsonOperationCompletionListener;
import alice.tuplecentre.tucson.api.TucsonTupleCentreId;
import alice.tuplecentre.tucson.api.TucsonTupleCentreIdDefault;
import alice.tuplecentre.tucson.api.acc.EnhancedACC;
import alice.tuplecentre.tucson.api.acc.EnhancedAsyncACC;
import alice.tuplecentre.tucson.api.acc.EnhancedSyncACC;
import alice.tuplecentre.tucson.api.actions.ordinary.In;
import alice.tuplecentre.tucson.api.actions.ordinary.Inp;
import alice.tuplecentre.tucson.api.actions.ordinary.Out;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidTupleCentreIdException;
import alice.tuplecentre.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tuplecentre.tucson.api.exceptions.UnreachableNodeException;
import alice.tuplecentre.tucson.asynchSupport.AsynchOpsHelper;

/**
 * Prime Calculation example worker agent. The Prime Calculator agent
 * asynchronously retrieves calculation requests from the Master agent through a
 * mediating TuCSoN tuple centre, performs the calculation, then puts the
 * results back in the space.
 *
 * @author Fabio Consalici, Riccardo Drudi
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 *
 */
public class PrimeCalculator extends AbstractTucsonAgent<EnhancedACC> {

    /**
     * 
     * This handler is MANDATORY only due to the way in which this example is
     * implemented -- that is, with the Prime Calculation agent serving requests
     * as soon as they appear. By remvoing it and coordinating in another way,
     * the example still works the same way. It is implemented here only to show
     * that usual support to asynchronous operation invocation -- through the
     * listener -- and the new support -- trhough the AsynchOpsHelper -- can
     * coexist.
     *
     * @author Fabio Consalici, Riccardo Drudi
     * @author (contributor) ste (mailto: s.mariani@unibo.it)
     *
     */
    private class InpHandler implements TucsonOperationCompletionListener {

        private final EnhancedAsyncACC eaacc;
        private final AsynchOpsHelper help;
        private final TucsonTupleCentreId ttcid;

        public InpHandler(final EnhancedAsyncACC acc,
                          final TucsonTupleCentreId tid, final AsynchOpsHelper aqm) {
            this.eaacc = acc;
            this.ttcid = tid;
            this.help = aqm;
        }

        @Override
        public void operationCompleted(final AbstractTupleCentreOperation op) {
            if (op.isResultSuccess()) {
                try {
                    LogicTuple res;
                    LogicTuple tupleRes;
                    res = (LogicTuple) op.getTupleResult();
                    final int upperBound = Integer.parseInt(res.getArg(0)
                            .toString());
                    this.info("Got request to calculate prime numbers up to "
                            + upperBound);
                    final long primeNumbers = PrimeCalculator.this
                            .getPrimeNumbers(upperBound);
                    this.info("Prime numbers up to " + upperBound + " are "
                            + primeNumbers);
                    tupleRes = LogicTuples.parse("prime(" + upperBound + ","
                            + primeNumbers + ")");
                    final Out out = new Out(this.ttcid, tupleRes);
                    this.help.enqueue(out, null);
                    if (!PrimeCalculator.this.stop) {
                        final LogicTuple tuple = LogicTuples
                                .parse("calcprime(X)");
                        final Inp inp = new Inp(this.ttcid, tuple);
                        this.help.enqueue(inp, new InpHandler(this.eaacc,
                                this.ttcid, this.help));
                    }
                } catch (final InvalidLogicTupleException | InvalidOperationException | NumberFormatException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    Thread.sleep(PrimeCalculator.SLEEP);
                    if (!PrimeCalculator.this.stop) {
                        final LogicTuple tuple = LogicTuples
                                .parse("calcprime(X)");
                        final Inp inp = new Inp(this.ttcid, tuple);
                        this.help.enqueue(inp, new InpHandler(this.eaacc,
                                this.ttcid, this.help));
                    }
                } catch (final InterruptedException | InvalidLogicTupleException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void operationCompleted(final TucsonOperation op) {
            /*
             * Not used atm
             */
        }

        private void info(final String msg) {
            System.out.println("[PrimeCalculator.InpHandler]: " + msg);
        }
    }

    /**
     *
     * @author Fabio Consalici, Riccardo Drudi
     * @author (contributor) ste (mailto: s.mariani@unibo.it)
     *
     */
    class StopHandler implements TucsonOperationCompletionListener {

        @Override
        public void operationCompleted(final AbstractTupleCentreOperation op) {
            if (op.isResultSuccess()) {
                PrimeCalculator.this.stop = true;
                this.info("Received stop request");
            }
        }

        @Override
        public void operationCompleted(final TucsonOperation op) {
            /*
             * Not used atm
             */
        }

        private void info(final String msg) {
            System.out.println("[PrimeCalculator.StopHandler]: " + msg);
        }
    }

    private static final int SLEEP = 50;

    private boolean stop;

    /**
     * Builds a Prime Calculator Agent given its TuCSoN agent Identifier
     *
     * @param id
     *            the TuCSoN agent Identifier
     * @throws TucsonInvalidAgentIdException
     *             if the given String does not represent a valid TuCSoN agent
     *             Identifier
     */
    public PrimeCalculator(final String id)
            throws TucsonInvalidAgentIdException {
        super(id);
        this.stop = false;
    }

    @Override
    protected EnhancedACC retrieveACC(final TucsonAgentId aid, final String networkAddress, final int portNumber) {
        return TucsonMetaACC.getContext(aid, networkAddress, portNumber);
    }

    @Override
    protected void main() {
        try {
            super.say("Started");
            final EnhancedAsyncACC acc = this.getACC();
            final TucsonTupleCentreId tid = new TucsonTupleCentreIdDefault("default",
                    "localhost", "20504");
            final AsynchOpsHelper helper = new AsynchOpsHelper("'helper4"
                    + this.getTucsonAgentId() + "'");
            final LogicTuple tuple = LogicTuples.parse("calcprime(X)");
            final Inp inp = new Inp(tid, tuple);
            helper.enqueue(inp, new InpHandler(acc, tid, helper));
            final EnhancedSyncACC accSynch = this.getACC();
            final LogicTuple stopTuple = LogicTuples.parse("stop(primecalc)");
            final In inStop = new In(tid, stopTuple);
            inStop.executeSynch(accSynch, null);
            this.stop = true;
            super.say("Stopping TuCSoN Asynch Helper now");
            helper.shutdownNow();
            super.say("I'm done");
        } catch (final TucsonInvalidTupleCentreIdException | OperationTimeOutException | UnreachableNodeException | TucsonOperationNotPossibleException | TucsonInvalidAgentIdException | NumberFormatException | InvalidLogicTupleException e) {
            e.printStackTrace();
        }
    }

    int getPrimeNumbers(final int n) {
        final boolean[] primi = new boolean[n];
        primi[0] = false;
        primi[1] = false;
        for (int i = 2; i < n; i++) {
            primi[i] = true;
            for (int j = 2; j < i; j++) {
                if (i % j == 0) {
                    primi[i] = false;
                    break;
                }
            }
        }
        int p = 0;
        for (int j = 2; j < n; j++) {
            if (primi[j]) {
                p++;
            }
        }
        return p;
    }
}
