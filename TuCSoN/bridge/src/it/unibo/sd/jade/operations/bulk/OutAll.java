package it.unibo.sd.jade.operations.bulk;

import it.unibo.sd.jade.operations.AbstractTucsonOrdinaryAction;
import alice.logictuple.LogicTuple;
import alice.tucson.api.EnhancedAsynchACC;
import alice.tucson.api.EnhancedSynchACC;
import alice.tucson.api.ITucsonOperation;
import alice.tucson.api.TucsonOperationCompletionListener;
import alice.tucson.api.TucsonTupleCentreId;
import alice.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tucson.api.exceptions.UnreachableNodeException;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;

/**
 * <code>out_all</code> TuCSoN primitive.
 * 
 * @author Luca Sangiorgi (mailto: luca.sangiorgi6@studio.unibo.it)
 * @author (contributor) Stefano Mariani (mailto: s.mariani@unibo.it)
 * 
 */
public class OutAll extends AbstractTucsonOrdinaryAction {
    /**
     * 
     * @param tc
     *            the TuCSoN tuple centre id target of the coordination
     *            operation
     * @param t
     *            the logic tuple argument of the coordination operation
     */
    public OutAll(final TucsonTupleCentreId tc, final LogicTuple t) {
        super(tc, t);
    }

    @Override
    public ITucsonOperation executeAsynch(final EnhancedAsynchACC acc,
            final TucsonOperationCompletionListener listener)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException {
        return acc.outAll(this.tcid, this.tuple, listener);
    }

    @Override
    public ITucsonOperation executeSynch(final EnhancedSynchACC acc,
            final Long timeout) throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException {
        return acc.outAll(this.tcid, this.tuple, timeout);
    }

    /*
     * (non-Javadoc)
     * @see it.unibo.sd.jade.operations.AbstractTucsonOrdinaryAction#toString()
     */
    @Override
    public String toString() {
        return "out_all" + super.toString();
    }
}
