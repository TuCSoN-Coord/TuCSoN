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
package alice.tucson.examples.asynchAPI;

import alice.logictuple.LogicTuple;
import alice.logictuple.exceptions.InvalidLogicTupleException;
import alice.tucson.api.AbstractTucsonAgent;
import alice.tucson.api.EnhancedAsynchACC;
import alice.tucson.api.EnhancedSynchACC;
import alice.tucson.api.ITucsonOperation;
import alice.tucson.api.TucsonAgentId;
import alice.tucson.api.TucsonOperationCompletionListener;
import alice.tucson.api.TucsonTupleCentreId;
import alice.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tucson.api.exceptions.TucsonInvalidTupleCentreIdException;
import alice.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tucson.api.exceptions.UnreachableNodeException;
import alice.tucson.asynchSupport.AsynchOpsHelper;
import alice.tucson.asynchSupport.operations.ordinary.In;
import alice.tucson.asynchSupport.operations.ordinary.Inp;
import alice.tucson.asynchSupport.operations.ordinary.Out;
import alice.tuplecentre.api.exceptions.InvalidOperationException;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;
import alice.tuplecentre.core.AbstractTupleCentreOperation;

/**
 * Prime Calculation example worker agent. The Prime Calculator agent ...
 *
 * @author Fabio Consalici, Riccardo Drudi
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 *
 */
public class PrimeCalculator extends AbstractTucsonAgent {

    /**
     *
     * @author Fabio Consalici, Riccardo Drudi
     * @author (contributor) ste (mailto: s.mariani@unibo.it)
     *
     */
    private class InpHandler implements TucsonOperationCompletionListener {

        private final EnhancedAsynchACC acc;
        private final AsynchOpsHelper help;
        private final TucsonTupleCentreId ttcid;

        public InpHandler(final EnhancedAsynchACC acc,
                final TucsonTupleCentreId tid, final AsynchOpsHelper aqm) {
            this.acc = acc;
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
                    this.info("Preime numbers up to " + upperBound + " are "
                            + primeNumbers);
                    tupleRes = LogicTuple.parse("prime(" + upperBound + ","
                            + primeNumbers + ")");
                    final Out out = new Out(this.ttcid, tupleRes);
                    this.help.enqueue(out, null);
                    if (!PrimeCalculator.this.stop) {
                        final LogicTuple tuple = LogicTuple
                                .parse("calcprime(X)");
                        final Inp inp = new Inp(this.ttcid, tuple);
                        this.help.enqueue(inp, new InpHandler(this.acc,
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
                        final LogicTuple tuple = LogicTuple
                                .parse("calcprime(X)");
                        final Inp inp = new Inp(this.ttcid, tuple);
                        this.help.enqueue(inp, new InpHandler(this.acc,
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
        public void operationCompleted(final ITucsonOperation op) {
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

        TucsonAgentId agentId;

        public StopHandler(final TucsonAgentId tucsonAgentId) {
            this.agentId = tucsonAgentId;
        }

        @Override
        public void operationCompleted(final AbstractTupleCentreOperation op) {
            if (op.isResultSuccess()) {
                PrimeCalculator.this.stop = true;
                this.info("Received stop request");
            }
        }

        @Override
        public void operationCompleted(final ITucsonOperation op) {
            /*
             * Not used atm
             */
        }

        private void info(final String msg) {
            System.out.println("[PrimeCalculator.StopHandler]: " + msg);
        }
    }

    private final static int SLEEP = 50;

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
    public void operationCompleted(final ITucsonOperation op) {
        /*
         * Not used atm
         */
    }

    @Override
    protected void main() {
        try {
            super.say("Started");
            final EnhancedAsynchACC acc = this.getContext();
            final TucsonTupleCentreId tid = new TucsonTupleCentreId("default",
                    "localhost", "20504");
            final AsynchOpsHelper helper = new AsynchOpsHelper("helper4"
                    + this.getTucsonAgentId());
            final LogicTuple tuple = LogicTuple.parse("calcprime(X)");
            final Inp inp = new Inp(tid, tuple);
            helper.enqueue(inp, new InpHandler(acc, tid, helper));
            final EnhancedSynchACC accSynch = this.getContext();
            final LogicTuple stopTuple = LogicTuple.parse("stop(primecalc)");
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
        primi[0] = primi[1] = false;
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
