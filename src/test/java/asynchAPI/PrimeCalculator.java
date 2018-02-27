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

import alice.tucson.api.AbstractTucsonAgent;
import alice.tucson.api.TucsonOperation;
import alice.tucson.api.TucsonOperationCompletionListener;
import alice.tucson.api.TucsonTupleCentreId;
import alice.tucson.api.acc.EnhancedAsyncACC;
import alice.tucson.api.acc.EnhancedSyncACC;
import alice.tucson.api.actions.ordinary.In;
import alice.tucson.api.actions.ordinary.Inp;
import alice.tucson.api.actions.ordinary.Out;
import alice.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tucson.api.exceptions.TucsonInvalidTupleCentreIdException;
import alice.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tucson.api.exceptions.UnreachableNodeException;
import alice.tucson.asynchSupport.AsynchOpsHelper;
import alice.tuple.logic.LogicTuple;
import alice.tuple.logic.LogicTuples;
import alice.tuple.logic.exceptions.InvalidLogicTupleException;
import alice.tuplecentre.api.exceptions.InvalidOperationException;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;
import alice.tuplecentre.core.AbstractTupleCentreOperation;

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
public class PrimeCalculator extends AbstractTucsonAgent {

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
                    LogicTuple res = null;
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
                } catch (final InvalidLogicTupleException e) {
                    e.printStackTrace();
                } catch (final NumberFormatException e) {
                    e.printStackTrace();
                } catch (final InvalidOperationException e) {
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
                } catch (final InterruptedException e) {
                    e.printStackTrace();
                } catch (final InvalidLogicTupleException e) {
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
     * Builds a Prime Calculator Agent given its TuCSoN agent ID
     *
     * @param id
     *            the TuCSoN agent ID
     * @throws TucsonInvalidAgentIdException
     *             if the given String does not represent a valid TuCSoN agent
     *             ID
     */
    public PrimeCalculator(final String id)
            throws TucsonInvalidAgentIdException {
        super(id);
        this.stop = false;
    }

    @Override
    public void operationCompleted(final AbstractTupleCentreOperation op) {
        /*
         * Not used atm
         */
    }

    @Override
    public void operationCompleted(final TucsonOperation op) {
        /*
         * Not used atm
         */
    }

    @Override
    protected void main() {
        try {
            super.say("Started");
            final EnhancedAsyncACC acc = this.getContext();
            final TucsonTupleCentreId tid = new TucsonTupleCentreId("default",
                    "localhost", "20504");
            final AsynchOpsHelper helper = new AsynchOpsHelper("'helper4"
                    + this.getTucsonAgentId() + "'");
            final LogicTuple tuple = LogicTuples.parse("calcprime(X)");
            final Inp inp = new Inp(tid, tuple);
            helper.enqueue(inp, new InpHandler(acc, tid, helper));
            final EnhancedSyncACC accSynch = this.getContext();
            final LogicTuple stopTuple = LogicTuples.parse("stop(primecalc)");
            final In inStop = new In(tid, stopTuple);
            inStop.executeSynch(accSynch, null);
            this.stop = true;
            super.say("Stopping TuCSoN Asynch Helper now");
            helper.shutdownNow();
            super.say("I'm done");
        } catch (final TucsonInvalidTupleCentreIdException e) {
            e.printStackTrace();
        } catch (final InvalidLogicTupleException e) {
            e.printStackTrace();
        } catch (final NumberFormatException e) {
            e.printStackTrace();
        } catch (final TucsonInvalidAgentIdException e) {
            e.printStackTrace();
        } catch (final TucsonOperationNotPossibleException e) {
            e.printStackTrace();
        } catch (final UnreachableNodeException e) {
            e.printStackTrace();
        } catch (final OperationTimeOutException e) {
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
