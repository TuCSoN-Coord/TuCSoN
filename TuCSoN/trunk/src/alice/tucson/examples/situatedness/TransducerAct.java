package alice.tucson.examples.situatedness;

import alice.respect.situatedness.AbstractTransducer;
import alice.respect.situatedness.ISimpleProbe;
import alice.respect.situatedness.TransducerId;
import alice.tucson.api.ITucsonOperation;
import alice.tuplecentre.api.TupleCentreId;
import alice.tuplecentre.core.AbstractTupleCentreOperation;

public class TransducerAct extends AbstractTransducer {

    public TransducerAct(final TransducerId i, final TupleCentreId t) {
        super(i, t);
    }

    @Override
    public boolean getEnv(final String key) {
        try {
            boolean allReadsOk = false;
            final Object[] keySet = this.probes.keySet().toArray();
            for (final Object element : keySet) {
                allReadsOk =
                        ((ISimpleProbe) this.probes.get(element))
                                .readValue(key);
            }

            return allReadsOk;
        } catch (final Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public void operationCompleted(final AbstractTupleCentreOperation op) {
        /*
         * 
         */
    }

    public void operationCompleted(final ITucsonOperation op) {
        /*
         * 
         */
    }

    @Override
    public boolean setEnv(final String key, final int value) {
        try {
            boolean resultIsOk = false;
            final Object[] keySet = this.probes.keySet().toArray();
            for (final Object element : keySet) {
                resultIsOk =
                        ((ISimpleProbe) this.probes.get(element)).writeValue(
                                key, value);
            }
            return resultIsOk;
        } catch (final Exception ex) {
            // ex.printStackTrace();
            return false;
        }
    }

}