/**
 * ActualSensor.java
 */
package alice.tucson.examples.situatedness;

import alice.logictuple.LogicTuple;
import alice.logictuple.exceptions.InvalidLogicTupleException;
import alice.respect.core.TransducersManager;
import alice.respect.situatedness.AbstractProbeId;
import alice.respect.situatedness.AbstractTransducer;
import alice.respect.situatedness.ISimpleProbe;
import alice.respect.situatedness.TransducerId;
import alice.respect.situatedness.TransducerStandardInterface;
import alice.tucson.api.EnhancedSynchACC;
import alice.tucson.api.ITucsonOperation;
import alice.tucson.api.TucsonAgentId;
import alice.tucson.api.TucsonMetaACC;
import alice.tucson.api.TucsonTupleCentreId;
import alice.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tucson.api.exceptions.TucsonInvalidTupleCentreIdException;
import alice.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tucson.api.exceptions.UnreachableNodeException;
import alice.tuplecentre.api.exceptions.InvalidOperationException;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;

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
    private static final String DEFAULT_PORT = "20504";
    private EnhancedSynchACC acc;
    private final AbstractProbeId pid;
    private TucsonTupleCentreId tempTc;
    private TransducerId tid;
    private TransducerStandardInterface transducer;

    public ActualSensor(final AbstractProbeId i) {
        this.pid = i;
        try {
            final TucsonAgentId aid = new TucsonAgentId("sensor");
            this.acc = TucsonMetaACC.getContext(aid, ActualSensor.DEFAULT_HOST,
                    Integer.valueOf(ActualSensor.DEFAULT_PORT));
            this.tempTc = new TucsonTupleCentreId("tempTc",
                    ActualSensor.DEFAULT_HOST, ActualSensor.DEFAULT_PORT);
        } catch (final TucsonInvalidTupleCentreIdException e) {
            e.printStackTrace();
        } catch (final TucsonInvalidAgentIdException e) {
            e.printStackTrace();
        }
    }

    /*
     * (non-Javadoc)
     * @see alice.respect.situatedness.ISimpleProbe#getIdentifier()
     */
    @Override
    public AbstractProbeId getIdentifier() {
        return this.pid;
    }

    /*
     * (non-Javadoc)
     * @see alice.respect.situatedness.ISimpleProbe#getTransducer()
     */
    @Override
    public TransducerId getTransducer() {
        return this.tid;
    }

    /*
     * (non-Javadoc)
     * @see alice.respect.situatedness.ISimpleProbe#readValue(java.lang.String)
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
                    .getTransducer(this.tid.getAgentName());
            if (this.transducer == null) {
                System.err.println("[" + this.pid
                        + "]: Can't retrieve my transducer!");
                return false;
            }
        }
        try {
            final LogicTuple template = LogicTuple.parse("temp(_)");
            final ITucsonOperation op = this.acc
                    .rd(this.tempTc, template, null);
            if (op.isResultSuccess()) {
                final int temp = op.getLogicTupleResult().getArg(0).intValue();
                System.out.println("[" + this.pid + "]: temp is " + temp);
                this.transducer.notifyEnvEvent(key, temp,
                        AbstractTransducer.GET_MODE);
            }
            return true;
        } catch (final TucsonOperationNotPossibleException e) {
            e.printStackTrace();
        } catch (final UnreachableNodeException e) {
            e.printStackTrace();
        } catch (final OperationTimeOutException e) {
            e.printStackTrace();
        } catch (final InvalidOperationException e) {
            e.printStackTrace();
        } catch (final InvalidLogicTupleException e) {
            e.printStackTrace();
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * @see alice.respect.situatedness.ISimpleProbe#setTransducer(alice.respect.
     * situatedness.TransducerId)
     */
    @Override
    public void setTransducer(final TransducerId t) {
        this.tid = t;
    }

    /*
     * (non-Javadoc)
     * @see alice.respect.situatedness.ISimpleProbe#writeValue(java.lang.String,
     * int)
     */
    @Override
    public boolean writeValue(final String key, final int value) {
        System.err.println("[" + this.pid
                + "]: I'm a sensor, I can't set values!");
        return false;
    }
}
