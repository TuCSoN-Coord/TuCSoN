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
package alice.tucson.asynchSupport;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import alice.tucson.api.AbstractTucsonAgent;
import alice.tucson.api.acc.EnhancedAsyncACC;
import alice.tucson.api.ITucsonOperation;
import alice.tucson.api.TucsonAgentId;
import alice.tucson.api.TucsonOperationCompletionListener;
import alice.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tucson.api.exceptions.UnreachableNodeException;
import alice.tucson.api.actions.AbstractTucsonAction;
import alice.tuplecentre.core.AbstractTupleCentreOperation;

/**
 * Helper TuCSoN agent to delegate asynchronous operation to.
 *
 * @author Fabio Consalici, Riccardo Drudi
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 *
 */
public class AsynchOpsHelper extends AbstractTucsonAgent {

    /**
     * Tune very carefully, configurable should be
     */
    private static final int POLLING_TIME = 10;

    private static void log(final TucsonAgentId aid, final String msg) {
        System.out.println("....[AsynchOpsHelper (" + aid + ")]: " + msg);
    }

    private EnhancedAsyncACC acc;
    private final CompletedOpsQueue completedOpsQueue;
    private boolean isHardStopped = false;
    private boolean isSoftStopped = false;
    private final Semaphore pendingOps;
    private final SearchableOpsQueue pendingOpsQueue;
    private final Semaphore shutdownSynch;

    /**
     * Builds an helper given the delegating agent ID
     *
     * @param id
     *            the ID of the agent delegating asynchronous invocation to this
     *            helper
     * @throws TucsonInvalidAgentIdException
     *             if the given String does not represent a valid TuCSoN agent
     *             identifier
     */
    public AsynchOpsHelper(final String id)
            throws TucsonInvalidAgentIdException {
        super(id);
        this.pendingOpsQueue = new SearchableOpsQueue();
        this.completedOpsQueue = new CompletedOpsQueue();
        this.pendingOps = new Semaphore(0);
        this.shutdownSynch = new Semaphore(0);
        this.go();
    }

    /**
     * Adds an operation to the queue of pending operations, only if the
     * shutdown operation hasn't been called yet.
     *
     * @param action
     *            the TuCSoN operation to execute.
     * @param listener
     *            the TuCSoN listener in charge of handling operation completion
     *            asynchronously
     * @return {@code true} or {@code false} depending on whether the enqueue
     *         operation was successful or not
     */
    public final boolean enqueue(final AbstractTucsonAction action,
            final TucsonOperationCompletionListener listener) {
        if (this.isSoftStopped || this.isHardStopped) {
            return false;
        }
        TucsonOpWrapper op = null;
        final TucsonListenerWrapper wol = new TucsonListenerWrapper(listener,
                this);
        op = new TucsonOpWrapper(this.acc, action, wol);
        wol.setTucsonOpWrapper(op);
        try {
            return this.pendingOpsQueue.add(op);
        } catch (final IllegalStateException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Gets the queue of completed operations
     *
     * @return the queue of completed operations
     */
    public final CompletedOpsQueue getCompletedOps() {
        return this.completedOpsQueue;
    }

    /**
     * Gets the queue of pending operations
     *
     * @return the queue of pending operations
     */
    public final SearchableOpsQueue getPendingOps() {
        return this.pendingOpsQueue;
    }

    /**
     * Gets the pending operations {@link java.util.concurrent.Semaphore} object
     *
     * @return the pending oeprations {@link java.util.concurrent.Semaphore}
     */
    public final Semaphore getPendingOpsSemaphore() {
        return this.pendingOps;
    }

    /**
     * Gets the {@link java.util.concurrent.Semaphore} object for shutdown
     * synchronisation
     *
     * @return the {@link java.util.concurrent.Semaphore} for shutdown
     *         synchronisation
     */
    public final Semaphore getShutdownSemaphore() {
        return this.shutdownSynch;
    }

    /**
     * Checks whether soft shutdown has been requested
     *
     * @return {@code true} or {@code false} depending on whether soft shutdown
     *         has been requested or not
     */
    public final boolean isShutdownGraceful() {
        return this.isSoftStopped;
    }

    /**
     * Checks whether hard shutdown has been requested
     *
     * @return {@code true} or {@code false} depending on whether hard shutdown
     *         has been requested or not
     */
    public final boolean isShutdownNow() {
        return this.isHardStopped;
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

    /**
     * Requests soft shutdown of the helper, that is, shutdown happens only when
     * the queue of pending operations has been emptied
     */
    public final void shutdownGracefully() {
        this.isSoftStopped = true;
    }

    /**
     * Requests hard shutdown of the helper, that is, shutdown happens as soon
     * as the current operation in execution completes: pending operations are
     * discarded instead
     */
    public final void shutdownNow() {
        this.isHardStopped = true;
    }

    @Override
    protected final void main() {
        this.acc = this.getContext();
        TucsonOpWrapper op = null;
        AsynchOpsHelper.log(this.getTucsonAgentId(), "started");
        // this is the loop that executes the operation until user stop command
        while (!this.isHardStopped && !this.isSoftStopped) {
            try {
                op = this.pendingOpsQueue.poll(AsynchOpsHelper.POLLING_TIME,
                        TimeUnit.MILLISECONDS);
                if (op != null) {
                    this.pendingOps.release();
                    AsynchOpsHelper.log(this.getTucsonAgentId(), "doing op "
                            + op.toString());
                    op.execute();
                }
            } catch (final InterruptedException e) {
                e.printStackTrace();
            } catch (final TucsonOperationNotPossibleException e) {
                e.printStackTrace();
            } catch (final UnreachableNodeException e) {
                e.printStackTrace();
            }
        }
        // Case shutdownNow: do not execute waitingQueue ops
        if (this.isHardStopped) {
            AsynchOpsHelper.log(this.getTucsonAgentId(),
                    "shutdown now requested");
            try {
                if (this.pendingOps.availablePermits() != 0) {
                    AsynchOpsHelper.log(
                            this.getTucsonAgentId(),
                            "ops still pending: "
                                    + this.pendingOps.availablePermits());
                    this.shutdownSynch.acquire();
                } else {
                    AsynchOpsHelper.log(this.getTucsonAgentId(),
                            "no ops pending");
                }
            } catch (final InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            // case shutdownGraceful: execute all added operations
            AsynchOpsHelper.log(this.getTucsonAgentId(),
                    "shutdown gracefully requested");
            while (this.pendingOps.availablePermits() != 0
                    || !this.pendingOpsQueue.isEmpty()) {
                try {
                    op = this.pendingOpsQueue
                            .poll(AsynchOpsHelper.POLLING_TIME,
                                    TimeUnit.MILLISECONDS);
                    if (op != null) {
                        this.pendingOps.release();
                        AsynchOpsHelper.log(this.getTucsonAgentId(),
                                "doing op " + op.toString());
                        op.execute();
                    }
                } catch (final InterruptedException e) {
                    e.printStackTrace();
                } catch (final TucsonOperationNotPossibleException e) {
                    e.printStackTrace();
                } catch (final UnreachableNodeException e) {
                    e.printStackTrace();
                }
            }
        }
        AsynchOpsHelper.log(this.getTucsonAgentId(), "done");
    }
}
