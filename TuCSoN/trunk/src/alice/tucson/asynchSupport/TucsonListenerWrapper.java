package alice.tucson.asynchSupport;

import alice.tucson.api.ITucsonOperation;
import alice.tucson.api.TucsonOperationCompletionListener;
import alice.tuplecentre.core.AbstractTupleCentreOperation;

/**
 * Class wrapping a TuCSoN listener for a correct coordination with
 * {@link alice.tucson.asynchSupport.AsynchOpsHelper} and handling queues
 * update.
 * 
 * @author Fabio Consalici, Riccardo Drudi
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 *
 */
public class TucsonListenerWrapper implements TucsonOperationCompletionListener {

    private AsynchOpsHelper helper;
    private TucsonOperationCompletionListener listener;
    private TucsonOpWrapper op;

    /**
     * Builds a wrapper to an operation listener given the actual listener and
     * the asynchronous operations handler
     * 
     * @param l
     *            the actual listener to trigger for operation handling
     * @param aoh
     *            the helper in charge of the operation
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
            e.printStackTrace();
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
    public void operationCompleted(final ITucsonOperation top) {
        /*
         * Not used atm
         */
    }

    /**
     * Sets the wrapper of the TuCSoN operation whose completion should be
     * handled
     * 
     * @param tow
     *            the wrapper of the TuCSoN operation whose completion should be
     *            handled
     */
    public final void setTucsonOpWrapper(final TucsonOpWrapper tow) {
        this.op = tow;
    }
}
