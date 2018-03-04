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
package alice.tucson.api.actions.ordinary.uniform;

import alice.logictuple.LogicTuple;
import alice.tucson.api.TucsonTupleCentreId;
import alice.tucson.api.acc.EnhancedAsyncACC;
import alice.tucson.api.acc.EnhancedSyncACC;
import alice.tucson.api.TucsonOperation;
import alice.tucson.api.TucsonOperationCompletionListener;
import alice.tucson.api.acc.UniformAsyncACC;
import alice.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tucson.api.exceptions.UnreachableNodeException;
import alice.tucson.api.actions.AbstractTucsonOrdinaryAction;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;

/**
 * <code>uinp</code> TuCSoN primitive.
 * 
 * @see UniformAsyncACC
 *
 * @author Luca Sangiorgi (mailto: luca.sangiorgi6@studio.unibo.it)
 * @author (contributor) Stefano Mariani (mailto: s.mariani@unibo.it)
 *
 */
public class Uinp extends AbstractTucsonOrdinaryAction {

    /**
     * Builds the TuCSoN {@code uin} action given its target tuple centre and
     * its tuple argument
     * 
     * @param tc
     *            the Identifier of the TuCSoN tuple centre target of this coordination
     *            operation
     * @param t
     *            the logic tuple argument of this coordination operation
     */
    public Uinp(final TucsonTupleCentreId tc, final LogicTuple t) {
        super(tc, t);
    }

    @Override
    public TucsonOperation executeAsynch(final EnhancedAsyncACC acc,
                                         final TucsonOperationCompletionListener listener)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException {
        return acc.uinp(this.tcid, this.tuple, listener);
    }

    @Override
    public TucsonOperation executeSynch(final EnhancedSyncACC acc,
                                        final Long timeout) throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException {
        return acc.uinp(this.tcid, this.tuple, timeout);
    }

    /*
     * (non-Javadoc)
     * @see
     * alice.tucson.asynchSupport.operations.AbstractTucsonOrdinaryAction#toString
     */
    @Override
    public String toString() {
        return "uinp" + super.toString();
    }
}
