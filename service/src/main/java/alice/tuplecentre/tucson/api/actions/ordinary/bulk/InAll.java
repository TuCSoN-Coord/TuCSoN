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
package alice.tuplecentre.tucson.api.actions.ordinary.bulk;

import alice.tuple.logic.LogicTuple;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;
import alice.tuplecentre.tucson.api.TucsonOperation;
import alice.tuplecentre.tucson.api.TucsonOperationCompletionListener;
import alice.tuplecentre.tucson.api.TucsonTupleCentreId;
import alice.tuplecentre.tucson.api.acc.BulkAsyncACC;
import alice.tuplecentre.tucson.api.acc.EnhancedAsyncACC;
import alice.tuplecentre.tucson.api.acc.EnhancedSyncACC;
import alice.tuplecentre.tucson.api.actions.AbstractTucsonOrdinaryAction;
import alice.tuplecentre.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tuplecentre.tucson.api.exceptions.UnreachableNodeException;

/**
 * <code>in_all</code> TuCSoN primitive.
 * 
 * @see BulkAsyncACC
 *
 * @author Luca Sangiorgi (mailto: luca.sangiorgi6@studio.unibo.it)
 * @author (contributor) Stefano Mariani (mailto: s.mariani@unibo.it)
 *
 */
public class InAll extends AbstractTucsonOrdinaryAction {

    /**
     * Builds the TuCSoN {@code in_all} action given its target tuple centre and
     * its tuple argument
     * 
     * @param tc
     *            the Identifier of the TuCSoN tuple centre target of this coordination
     *            operation
     * @param t
     *            the logic tuple argument of this coordination operation
     */
    public InAll(final TucsonTupleCentreId tc, final LogicTuple t) {
        super(tc, t);
    }

    @Override
    public TucsonOperation executeAsynch(final EnhancedAsyncACC acc,
                                         final TucsonOperationCompletionListener listener)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException {
        return acc.inAll(this.tcid, this.tuple, listener);
    }

    @Override
    public TucsonOperation executeSynch(final EnhancedSyncACC acc,
                                        final Long timeout) throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException {
        return acc.inAll(this.tcid, this.tuple, timeout);
    }

    /*
     * (non-Javadoc)
     * @see
     * alice.tuplecentre.tucson.asynchSupport.operations.AbstractTucsonOrdinaryAction#toString
     * ()
     */
    @Override
    public String toString() {
        return "in_all" + super.toString();
    }
}
