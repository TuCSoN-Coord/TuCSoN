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
import alice.tuplecentre.tucson.api.acc.EnhancedSyncACC;
import alice.tuplecentre.tucson.api.actions.ordinary.In;
import alice.tuplecentre.tucson.api.actions.ordinary.Inp;
import alice.tuplecentre.tucson.api.actions.ordinary.Out;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidTupleCentreIdException;
import alice.tuplecentre.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tuplecentre.tucson.api.exceptions.UnreachableNodeException;
import alice.tuplecentre.tucson.asynchSupport.AsynchOpsHelper;
import alice.tuplecentre.tucson.asynchSupport.TucsonOpWrapper;

/**
 * Prime Calculation example master agent. The master agent delegates
 * calculations to PrimeCalculator agents through the AsynchOpsHelper, so as to
 * be free to do other activities while they compute, and getting back the
 * results without having to suspend--like in traditional tuple based
 * coordination.
 *
 * @author Fabio Consalici, Riccardo Drudi
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 */
public class MasterAgent extends AbstractTucsonAgent<EnhancedSyncACC> implements TucsonOperationCompletionListener {

    /**
     * The handler of operations completion except the last operation. This
     * handler is OPTIONAL: by remvoing it, the example still works the same
     * way. It is implemented here only to show that usual support to
     * asynchronous operation invocation -- through the listener -- and the new
     * support -- trhough the AsynchOpsHelper -- can coexist.
     *
     * @author Fabio Consalici, Riccardo Drudi
     * @author (contributor) ste (mailto: s.mariani@unibo.it)
     */
    private class CompletionHandler implements
            TucsonOperationCompletionListener {

        @Override
        public void operationCompleted(final AbstractTupleCentreOperation op) {
            if (op.isResultSuccess()) {
                try {
                    final LogicTuple tuple = (LogicTuple) op.getTupleResult();
                    final int upperBound = Integer.parseInt(tuple.getArg(0)
                            .toString());
                    final int prime = Integer.parseInt(tuple.getArg(1)
                            .toString());
                    final int nIn = MasterAgent.this.helper.getCompletedOps()
                            .getMatchingOps(In.class).getSuccessfulOps().size();
                    final int nInp = MasterAgent.this.helper.getCompletedOps()
                            .getMatchingOps(Inp.class).getSuccessfulOps()
                            .size();
                    final int nOps = nIn + nInp;
                    this.info("The prime numbers until " + upperBound + " are "
                            + prime);
                    this.info("Done " + nOps + " operations out of "
                            + MasterAgent.REQUESTS);
                } catch (NumberFormatException | InvalidOperationException e) {
                    LOGGER.error(e.getMessage(), e);
                }
            } else {
                this.severe("Operation failed!");
            }
        }

        @Override
        public void operationCompleted(final TucsonOperation op) {
            /*
             * Not used atm
             */
        }

        private void info(final String msg) {
            System.out.println("[MasterAgent.CompletionHandler]: " + msg);
        }

        private void severe(final String msg) {
            System.err.println("[MasterAgent.CompletionHandler]: " + msg);
        }
    }

    /**
     * The handler of last operation completion. This handler is MANDATORY only
     * due to the way in which this example is implemented -- that is, with the
     * Master agent deliberatively waiting for the tuple here produced. By
     * remvoing it and coordinating in another way, the example still works the
     * same way. It is implemented here only to show that usual support to
     * asynchronous operation invocation -- through the listener -- and the new
     * support -- trhough the AsynchOpsHelper -- can coexist.
     *
     * @author Fabio Consalici, Riccardo Drudi
     * @author (contributor) ste (mailto: s.mariani@unibo.it)
     */
    private class LastCompletionHandler implements
            TucsonOperationCompletionListener {

        private final AsynchOpsHelper help;
        private final TucsonTupleCentreId ttcid;

        public LastCompletionHandler(final AsynchOpsHelper aoh,
                                     final TucsonTupleCentreId tid) {
            this.help = aoh;
            this.ttcid = tid;
        }

        @Override
        public void operationCompleted(final AbstractTupleCentreOperation op) {
            LogicTuple tuple;
            try {
                tuple = LogicTuples.parse("firstloop");
                final Out out = new Out(this.ttcid, tuple);
                this.help.enqueue(out, null);
                this.info("First loop done");
            } catch (final InvalidLogicTupleException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }

        @Override
        public void operationCompleted(final TucsonOperation op) {
            /*
             * Not used atm
             */
        }

        private void info(final String msg) {
            System.out.println("[MasterAgent.LastCompletionHandler]: " + msg);
        }

    }

    private static final int LOOPS = 3;
    private static final int REQUESTS = 50;
    private static final int SEED = 1000;

    private static final int SLEEP = 1000;

    private static final int STEP = 1000;

    /**
     * @param args no args expected
     */
    public static void main(final String[] args) {
        try {
            /*
             * LOOPS is the number of "firstloops" to be done
             */
            new MasterAgent("master", MasterAgent.LOOPS).go();
        } catch (final TucsonInvalidAgentIdException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    private final AsynchOpsHelper helper;
    private int nInpSucceeded;
    private final int nPrimeCalc;

    /**
     * Builds a Master Agent given its TuCSoN agent Identifier and the number of
     * calculations to perform
     *
     * @param id
    the TuCSoN agent Identifier
     * @param nCalcs
    the number of calculations to perform
     * @throws TucsonInvalidAgentIdException
    if the given String does not represent a valid TuCSoN agent
     *             Identifier
     */
    public MasterAgent(final String id, final int nCalcs)
            throws TucsonInvalidAgentIdException {
        super(id);
        this.nInpSucceeded = 0;
        this.nPrimeCalc = nCalcs;
        /*
         * 1 - Instantiate the helper
         */
        this.helper = new AsynchOpsHelper("'helper4" + this.getTucsonAgentId()
                + "'");
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
    protected EnhancedSyncACC retrieveACC(final TucsonAgentId aid, final String networkAddress, final int portNumber) {
        return TucsonMetaACC.getContext(aid, networkAddress, portNumber);
    }

    @Override
    protected void main() {
        try {
            super.say("Started");
            Out out;
            LogicTuple tuple;
            final TucsonTupleCentreId tid = new TucsonTupleCentreIdDefault("default",
                    "localhost", "20504");
            int number = MasterAgent.SEED;
            for (int i = 0; i < MasterAgent.REQUESTS; i++) {
                tuple = LogicTuples.parse("calcprime(" + number + ")");
                super.say("Enqueuing prime numbers calculation up to " + number);
                /*
                 * 2 - Build the TuCSoN action whose asynchronous invocation
                 * should be delegated
                 */
                out = new Out(tid, tuple);
                /*
                 * 3 - Delegate asynchronous invocation to the helper
                 */
                this.helper.enqueue(out, null);
                number += MasterAgent.STEP;
            }
            super.say("Sent "
                    + MasterAgent.REQUESTS
                    + " requests to Prime Calculator agent, now registering handlers...");
            Inp inp;
            for (int i = 0; i < MasterAgent.REQUESTS; i++) {
                tuple = LogicTuples.parse("prime(X,Y)");
                inp = new Inp(tid, tuple);
                if (i == MasterAgent.REQUESTS - 1) {
                    final LastCompletionHandler lch = new LastCompletionHandler(
                            this.helper, tid);
                    this.helper.enqueue(inp, lch);
                } else {
                    final CompletionHandler ch = new CompletionHandler();
                    this.helper.enqueue(inp, ch);
                }
            }
            super.say("Handlers registered, now I suspend myself until first loop completes...");
            final EnhancedSyncACC accSynch = this.getACC();
            final LogicTuple firstLoopTuple = LogicTuples.parse("firstloop");
            final In firstLoopIn = new In(tid, firstLoopTuple);
            /*
             * - You can also directly execute synchronous invocation the usual
             * TuCSoN way or by using the novel TuCSoN action object (as done
             * here)
             */
            firstLoopIn.executeSynch(accSynch, null);
            /*
             * 4 - If and When you want, query the helper for operation state,
             * incrementally filtering pending/completed queues as you please
             */
            this.nInpSucceeded = this.helper.getCompletedOps()
                    .getMatchingOps(Inp.class).getSuccessfulOps().size();
            super.say("First loop completed, received " + this.nInpSucceeded
                    + " inp successful completions out of "
                    + MasterAgent.REQUESTS
                    + ", now registering handlers for remaining (if any)...");
            In in;
            /*
             * 5 - Remember to remove the operations you already managed
             */
            this.helper.getCompletedOps().removeSuccessfulOps();
            for (int i = 0; i < MasterAgent.REQUESTS - this.nInpSucceeded; i++) {
                tuple = LogicTuples.parse("prime(X,Y)");
                in = new In(tid, tuple);
                final CompletionHandler ch = new CompletionHandler();
                this.helper.enqueue(in, ch);
            }
            int nInSucceeded;
            boolean stop = false;
            while (!stop) {
                Thread.sleep(MasterAgent.SLEEP);
                nInSucceeded = this.helper.getCompletedOps()
                        .getMatchingOps(In.class).getSuccessfulOps().size();
                super.say("Handlers registered, received " + nInSucceeded
                        + " in successful completions, " + this.nInpSucceeded
                        + " inp  successful completions, total of "
                        + (nInSucceeded + this.nInpSucceeded));
                if (nInSucceeded + this.nInpSucceeded == MasterAgent.REQUESTS) {
                    stop = true;
                }
            }
            super.say("All requests completed:");
            /*
             * - You can freely iterate over (filtered) queues to retrieve each
             * single operation
             */
            for (TucsonOpWrapper op : this.helper.getCompletedOps()
                    .getMatchingOps(In.class).getSuccessfulOps()) {
                super.say(op.toString());
            }
            super.say("Stopping Prime Calculator agent");
            for (int i = 0; i < this.nPrimeCalc; i++) {
                tuple = LogicTuples.parse("stop(primecalc)");
                out = new Out(tid, tuple);
                this.helper.enqueue(out, null);
            }
            super.say("Stopping TuCSoN Asynch Helper gracefully");
            /*
             * 6 - When done, shutdown the helper as you please
             */
            this.helper.shutdownGracefully();
            Thread.sleep(MasterAgent.SLEEP);
            super.say("I'm done");
        } catch (final TucsonInvalidTupleCentreIdException | OperationTimeOutException | UnreachableNodeException | TucsonOperationNotPossibleException | InterruptedException | InvalidLogicTupleException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
