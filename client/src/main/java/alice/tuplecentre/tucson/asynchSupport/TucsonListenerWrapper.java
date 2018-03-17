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
package alice.tuplecentre.tucson.asynchSupport;

import alice.tuplecentre.core.AbstractTupleCentreOperation;
import alice.tuplecentre.tucson.api.TucsonOperation;
import alice.tuplecentre.tucson.api.TucsonOperationCompletionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

/**
 * Class wrapping a TuCSoN listener for a correct coordination with
 * {@link AsynchOpsHelper} and handling queues
 * update.
 *
 * @author Fabio Consalici, Riccardo Drudi
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 */
public class TucsonListenerWrapper implements TucsonOperationCompletionListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().getClass());

    private final AsynchOpsHelper helper;
    private final TucsonOperationCompletionListener listener;
    private TucsonOpWrapper op;

    /**
     * Builds a wrapper to an operation listener given the actual listener and
     * the asynchronous operations handler
     *
     * @param l   the actual listener to trigger for operation handling
     * @param aoh the helper in charge of the operation
     */
    public TucsonListenerWrapper(final TucsonOperationCompletionListener l,
                                 final AsynchOpsHelper aoh) {
        this.helper = aoh;
        this.listener = l;
    }

    /**
     * Gets the associated helper
     *
     * @return the associated helper
     */
    public AsynchOpsHelper getOpHelper() {
        return this.helper;
    }

    /**
     * Gets the associated TuCSoN operation completion listener
     *
     * @return the associated TuCSoN operation completion listener
     */
    public TucsonOperationCompletionListener getActualListener() {
        return this.listener;
    }

    /**
     * Gets the operation whose completion is handled by the wrapped listener
     *
     * @return the operation whose completion is handled by the wrapped listener
     */
    public TucsonOpWrapper getOp() {
        return this.op;
    }

    @Override
    public void operationCompleted(final AbstractTupleCentreOperation atco) {
        try {
            this.helper.getPendingOpsSemaphore().acquire();
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        }
        if (this.helper.isShutdownNow()
                && this.helper.getPendingOpsSemaphore().availablePermits() == 0) {
            this.helper.getShutdownSemaphore().release();
        }
        if (!this.helper.isShutdownNow()) {
            this.op.setOp(atco);
            this.helper.getCompletedOps().add(this.op);
            if (this.listener != null) {
                this.listener.operationCompleted(atco);
            }
        }
    }

    @Override
    public void operationCompleted(final TucsonOperation top) {
        /*
         * Not used atm
         */
    }

    /**
     * Sets the wrapper of the TuCSoN operation whose completion should be
     * handled
     *
     * @param tow the wrapper of the TuCSoN operation whose completion should be
     *            handled
     */
    public final void setTucsonOpWrapper(final TucsonOpWrapper tow) {
        this.op = tow;
    }
}
