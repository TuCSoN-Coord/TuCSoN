/**
 * ActualActuator.java
 */
package situatedness;

import alice.tuple.logic.LogicTuple;
import alice.tuple.logic.exceptions.InvalidLogicTupleException;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;
import alice.tuplecentre.respect.core.TransducersManager;
import alice.tuplecentre.respect.situatedness.*;
import alice.tuplecentre.tucson.api.*;
import alice.tuplecentre.tucson.api.acc.EnhancedSyncACC;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidTupleCentreIdException;
import alice.tuplecentre.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tuplecentre.tucson.api.exceptions.UnreachableNodeException;
import alice.tuplecentre.tucson.service.TucsonInfo;

/**
 * The 'actual' actuator probe deployed in this scenario. Although in this toy
 * example it is only simulated, here is where you would place your code to
 * interface with a real-world probe.
 *
 * @author ste (mailto: s.mariani@unibo.it) on 06/nov/2013
 */
public class ActualActuator implements Probe {

    private static final String DEFAULT_HOST = "localhost";
    private EnhancedSyncACC acc;
    private final ProbeIdentifier pid;
    private TucsonTupleCentreId tempTc;
    private TransducerId tid;
    private TransducerStandardInterface transducer;

    public ActualActuator(final ProbeIdentifier i) {
        this.pid = i;
        try {
            final TucsonAgentId aid = new TucsonAgentIdDefault("actuator");
            this.acc = TucsonMetaACC.getContext(aid,
                    ActualActuator.DEFAULT_HOST,
                    TucsonInfo.getDefaultPortNumber());
            this.tempTc = TucsonTupleCentreId.of("tempTc",
                    ActualActuator.DEFAULT_HOST, String.valueOf(TucsonInfo.getDefaultPortNumber()));
        } catch (final TucsonInvalidTupleCentreIdException | TucsonInvalidAgentIdException e) {
            e.printStackTrace();
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
        System.err.println("[" + this.pid
                + "]: I'm an actuator, I can't sense values!");
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
            //TODO remove
            System.out.println("setting temperature");
            final LogicTuple template = LogicTuple.parse("temp(_)");
            final TucsonOperation op = this.acc.inAll(this.tempTc, template,
                    null);
            if (op.isResultSuccess()) {
                final LogicTuple tempTuple = LogicTuple.parse("temp(" + value
                        + ")");
                this.acc.out(this.tempTc, tempTuple, null);
                System.out.println("[" + this.pid + "]: temp set to " + value);
                this.transducer.notifyEnvEvent(key, value,
                        AbstractTransducer.SET_MODE);
                return true;
            }
        } catch (final TucsonOperationNotPossibleException | InvalidLogicTupleException | OperationTimeOutException | UnreachableNodeException e) {
            e.printStackTrace();

        }
        return false;
    }
}
