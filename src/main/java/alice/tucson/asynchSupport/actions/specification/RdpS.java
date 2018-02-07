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
package alice.tucson.asynchSupport.actions.specification;

import alice.logictuple.LogicTuple;
import alice.tucson.api.EnhancedAsynchACC;
import alice.tucson.api.EnhancedSynchACC;
import alice.tucson.api.ITucsonOperation;
import alice.tucson.api.TucsonOperationCompletionListener;
import alice.tucson.api.TucsonTupleCentreId;
import alice.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tucson.api.exceptions.UnreachableNodeException;
import alice.tucson.asynchSupport.actions.AbstractTucsonSpecificationAction;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;

/**
 * <code>rdp_s</code> TuCSoN primitive.
 * 
 * @see alice.tucson.api.SpecificationAsynchACC
 *
 * @author Luca Sangiorgi (mailto: luca.sangiorgi6@studio.unibo.it)
 * @author (contributor) Stefano Mariani (mailto: s.mariani@unibo.it)
 *
 */
public class RdpS extends AbstractTucsonSpecificationAction {

    /**
     * Builds the TuCSoN {@code rdp_s} action given its target tuple centre
     * 
     * @param tc
     *            the ID of the TuCSoN tuple centre target of this coordination
     *            operation
     * @param e
     *            the logic tuple representing the triggering event of the
     *            ReSpecT specification tuple
     * @param g
     *            the logic tuple representing the guards of the ReSpecT
     *            specification tuple
     * @param r
     *            the logic tuple representing the reaction body of the ReSpecT
     *            specification tuple
     */
    public RdpS(final TucsonTupleCentreId tc, final LogicTuple e,
            final LogicTuple g, final LogicTuple r) {
        super(tc, e, g, r);
    }

    @Override
    public ITucsonOperation executeAsynch(final EnhancedAsynchACC acc,
            final TucsonOperationCompletionListener listener)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException {
        return acc.rdpS(this.tcid, this.event, this.guards, this.reaction,
                listener);
    }

    @Override
    public ITucsonOperation executeSynch(final EnhancedSynchACC acc,
            final Long timeout) throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException {
        return acc.rdpS(this.tcid, this.event, this.guards, this.reaction,
                timeout);
    }

    /*
     * (non-Javadoc)
     * @see
     * alice.tucson.asynchSupport.operations.AbstractTucsonOrdinaryAction#toString
     */
    @Override
    public String toString() {
        return "rdp_s" + super.toString();
    }
}
