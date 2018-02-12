/**
 * ActuatorTransducer.java
 */
package situatedness;

import alice.respect.situatedness.AbstractTransducer;
import alice.respect.situatedness.ISimpleProbe;
import alice.respect.situatedness.TransducerId;
import alice.tucson.api.ITucsonOperation;
import alice.tuplecentre.api.TupleCentreId;
import alice.tuplecentre.core.AbstractTupleCentreOperation;

/**
 * The transducer mediating interactions to/from the actuator probe. As such,
 * only the 'setEnv' method is implemented (furthermore, a synchronous behaviour
 * is expected, hence no asynchronous facility is implemented).
 *
 * @author ste (mailto: s.mariani@unibo.it) on 05/nov/2013
 *
 */
public class ActuatorTransducer extends AbstractTransducer {

    /**
     * @param i
     *            the transducer id
     * @param tc
     *            the tuple centre id
     */
    public ActuatorTransducer(final TransducerId i, final TupleCentreId tc) {
        super(i, tc);
    }

    /*
     * (non-Javadoc)
     * @see
     * alice.respect.situatedness.AbstractTransducer#getEnv(java.lang.String)
     */
    @Override
    public boolean getEnv(final String key) {
        this.speakErr("[" + this.id
                + "]: I'm an actuator transducer, I can't sense values!");
        return false;
    }

    /*
     * (non-Javadoc)
     * @see
     * alice.tuplecentre.core.OperationCompletionListener#operationCompleted
     * (alice.tuplecentre.core.AbstractTupleCentreOperation)
     */
    @Override
    public void operationCompleted(final AbstractTupleCentreOperation op) {
        /* not used */
    }

    /*
     * (non-Javadoc)
     * @see
     * alice.tucson.api.TucsonOperationCompletionListener#operationCompleted
     * (alice.tucson.api.ITucsonOperation)
     */
    @Override
    public void operationCompleted(final ITucsonOperation op) {
        /* not used */
    }

    /*
     * (non-Javadoc)
     * @see
     * alice.respect.situatedness.AbstractTransducer#setEnv(java.lang.String,
     * int)
     */
    @Override
    public boolean setEnv(final String key, final int value) {
        this.speak("[" + this.id + "]: Writing...");
        boolean success = true;
        final Object[] keySet = this.probes.keySet().toArray();
        /*
         * for each probe this transducer models, stimulate it to act on its
         * environment
         */
        for (final Object element : keySet) {
            if (!((ISimpleProbe) this.probes.get(element)).writeValue(key,
                    value)) {
                this.speakErr("[" + this.id + "]: Write failure!");
                success = false;
                break;
            }
        }
        return success;
    }
}
