/**
 * SensorTransducer.java
 */
package situatedness;

import alice.tuplecentre.api.TupleCentreIdentifier;
import alice.tuplecentre.core.AbstractTupleCentreOperation;
import alice.tuplecentre.respect.situatedness.AbstractTransducer;
import alice.tuplecentre.respect.situatedness.Probe;
import alice.tuplecentre.respect.situatedness.TransducerId;
import alice.tuplecentre.tucson.api.TucsonOperation;

/**
 * The transducer mediating interactions to/from the sensor probe. As such, only
 * the 'getEnv' method is implemented (furthermore, a synchronous behaviour is
 * expected, hence no asynchronous facility is implemented).
 *
 * @author ste (mailto: s.mariani@unibo.it) on 05/nov/2013
 *
 */
public class SensorTransducer extends AbstractTransducer {

    /**
     * @param i
     *            the transducer identifier
     * @param tc
     *            the tuple centre identifier
     */
    public SensorTransducer(final TransducerId i, final TupleCentreIdentifier tc) {
        super(i, tc);
    }

    /*
     * (non-Javadoc)
     * @see
     * alice.tuplecentre.respect.situatedness.AbstractTransducer#getEnv(java.lang.String)
     */
    @Override
    public boolean getEnv(final String key) {
        this.speak("[" + this.id + "]: Reading...");
        boolean success = true;
        final Object[] keySet = this.probes.keySet().toArray();
        /*
         * for each probe this transducer models, stimulate it to sense its
         * environment
         */
        for (final Object element : keySet) {
            if (!((Probe) this.probes.get(element)).readValue(key)) {
                this.speakErr("[" + this.id + "]: Read failure!");
                success = false;
                break;
            }
        }
        return success;
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
     * alice.tuplecentre.tucson.api.TucsonOperationCompletionListener#operationCompleted
     * (TucsonOperation)
     */
    @Override
    public void operationCompleted(final TucsonOperation op) {
        /* not used */
    }

    /*
     * (non-Javadoc)
     * @see
     * alice.tuplecentre.respect.situatedness.AbstractTransducer#setEnv(java.lang.String,
     * int)
     */
    @Override
    public boolean setEnv(final String key, final int value) {
        this.speakErr("[" + this.id
                + "]: I'm a sensor transducer, I can't set values!");
        return false;
    }
}
