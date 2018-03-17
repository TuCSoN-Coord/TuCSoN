/**
 * ActualSensor.java
 */
package situatedness;

import alice.tuple.logic.LogicTuple;
import alice.tuple.logic.LogicTuples;
import alice.tuple.logic.exceptions.InvalidLogicTupleException;
import alice.tuplecentre.api.exceptions.InvalidOperationException;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;
import alice.tuplecentre.respect.core.TransducersManager;
import alice.tuplecentre.respect.situatedness.AbstractTransducer;
import alice.tuplecentre.respect.situatedness.ISimpleProbe;
import alice.tuplecentre.respect.situatedness.ProbeIdentifier;
import alice.tuplecentre.respect.situatedness.TransducerId;
import alice.tuplecentre.respect.situatedness.TransducerStandardInterface;
import alice.tuplecentre.tucson.api.TucsonAgentId;
import alice.tuplecentre.tucson.api.TucsonAgentIdDefault;
import alice.tuplecentre.tucson.api.TucsonMetaACC;
import alice.tuplecentre.tucson.api.TucsonOperation;
import alice.tuplecentre.tucson.api.TucsonTupleCentreId;
import alice.tuplecentre.tucson.api.TucsonTupleCentreIdDefault;
import alice.tuplecentre.tucson.api.acc.EnhancedSyncACC;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidTupleCentreIdException;
import alice.tuplecentre.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tuplecentre.tucson.api.exceptions.UnreachableNodeException;
import alice.tuplecentre.tucson.service.TucsonInfo;

/**
 * The 'actual' sensor probe deployed in this scenario. Although in this toy
 * example it is only simulated, here is where you would place your code to
 * interface with a real-world probe.
 *
 * @author ste (mailto: s.mariani@unibo.it) on 05/nov/2013
 *
 */
public class ActualSensor implements ISimpleProbe {

    private static final String DEFAULT_HOST = "localhost";
    private EnhancedSyncACC acc;
    private final ProbeIdentifier pid;
    private TucsonTupleCentreId tempTc;
    private TransducerId tid;
    private TransducerStandardInterface transducer;

    public ActualSensor(final ProbeIdentifier i) {
        this.pid = i;
        try {
            final TucsonAgentId aid = new TucsonAgentIdDefault("sensor");
            this.acc = TucsonMetaACC.getContext(aid, ActualSensor.DEFAULT_HOST,
                    TucsonInfo.getDefaultPortNumber());
            this.tempTc = new TucsonTupleCentreIdDefault("tempTc",
                    ActualSensor.DEFAULT_HOST, String.valueOf(TucsonInfo.getDefaultPortNumber()));
        } catch (final TucsonInvalidTupleCentreIdException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (final TucsonInvalidAgentIdException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    /*
     * (non-Javadoc)
     * @see alice.tuplecentre.respect.situatedness.ISimpleProbe#getIdentifier()
     */
    @Override
    public ProbeIdentifier getIdentifier() {
        return this.pid;
    }

    /*
     * (non-Javadoc)
     * @see alice.tuplecentre.respect.situatedness.ISimpleProbe#getTransducer()
     */
    @Override
    public TransducerId getTransducer() {
        return this.tid;
    }

    /*
     * (non-Javadoc)
     * @see alice.tuplecentre.respect.situatedness.ISimpleProbe#readValue(java.lang.String)
     */
    @Override
    public boolean readValue(final String key) {
        if (!"temp".equals(key)) {
            System.err.println("[" + this.pid + "]: Unknown property " + key);
            return false;
        }
        if (this.tid == null) {
            System.err.println("[" + this.pid
                    + "]: Don't have any transducer associated yet!");
            return false;
        }
        if (this.transducer == null) {
            this.transducer = TransducersManager.INSTANCE
                    .getTransducer(this.tid.getLocalName());
            if (this.transducer == null) {
                System.err.println("[" + this.pid
                        + "]: Can't retrieve my transducer!");
                return false;
            }
        }
        try {
            final LogicTuple template = LogicTuples.parse("temp(_)");
            final TucsonOperation op = this.acc
                    .rd(this.tempTc, template, null);
            if (op.isResultSuccess()) {
                final int temp = op.getLogicTupleResult().getArg(0).intValue();
                System.out.println("[" + this.pid + "]: temp is " + temp);
                this.transducer.notifyEnvEvent(key, temp,
                        AbstractTransducer.GET_MODE);
            }
            return true;
        } catch (final TucsonOperationNotPossibleException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (final UnreachableNodeException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (final OperationTimeOutException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (final InvalidOperationException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (final InvalidLogicTupleException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * @see alice.tuplecentre.respect.situatedness.ISimpleProbe#setTransducer(alice.tuplecentre.respect.
     * situatedness.TransducerId)
     */
    @Override
    public void setTransducer(final TransducerId t) {
        this.tid = t;
    }

    /*
     * (non-Javadoc)
     * @see alice.tuplecentre.respect.situatedness.ISimpleProbe#writeValue(java.lang.String,
     * int)
     */
    @Override
    public boolean writeValue(final String key, final int value) {
        System.err.println("[" + this.pid
                + "]: I'm a sensor, I can't set values!");
        return false;
    }
}
