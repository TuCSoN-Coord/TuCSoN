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
package alice.tucson.api.actions.specification;

import alice.tucson.api.EnhancedAsynchACC;
import alice.tucson.api.EnhancedSynchACC;
import alice.tucson.api.ITucsonOperation;
import alice.tucson.api.TucsonOperationCompletionListener;
import alice.tucson.api.TucsonTupleCentreId;
import alice.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tucson.api.exceptions.UnreachableNodeException;
import alice.tucson.api.actions.AbstractTucsonSpecificationAction;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;

/**
 * <code>get_s</code> TuCSoN primitive.
 *
 * @see alice.tucson.api.SpecificationAsynchACC
 *
 * @author Luca Sangiorgi (mailto: luca.sangiorgi6@studio.unibo.it)
 * @author (contributor) Stefano Mariani (mailto: s.mariani@unibo.it)
 *
 */
public class GetS extends AbstractTucsonSpecificationAction {

    /**
     * Builds the TuCSoN {@code get_s} action given its target tuple centre
     * 
     * @param tc
     *            the ID of the TuCSoN tuple centre target of this coordination
     *            operation
     */
    public GetS(final TucsonTupleCentreId tc) {
        super(tc, null, null, null);
    }

    @Override
    public ITucsonOperation executeAsynch(final EnhancedAsynchACC acc,
            final TucsonOperationCompletionListener listener)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException {
        return acc.getS(this.tcid, listener);
    }

    @Override
    public ITucsonOperation executeSynch(final EnhancedSynchACC acc,
            final Long timeout) throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException {
        return acc.getS(this.tcid, timeout);
    }

    /*
     * (non-Javadoc)
     * @see
     * alice.tucson.asynchSupport.operations.AbstractTucsonOrdinaryAction#toString
     */
    @Override
    public String toString() {
        return "get_s" + super.toString();
    }
}
