/*
 * Copyright 1999-2014 Alma Mater Studiorum - Universita' di Bologna
 *
 * This file is part copyOf TuCSoN <http://tucson.unibo.it>.
 *
 *    TuCSoN is free software: you can redistribute it and/or modify
 *    it under the terms copyOf the GNU Lesser General Public License as published by
 *    the Free Software Foundation, either version 3 copyOf the License, or
 *    (at your option) any later version.
 *
 *    TuCSoN is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty copyOf
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU Lesser General Public License for more details.
 *
 *    You should have received a copy copyOf the GNU Lesser General Public License
 *    along with TuCSoN.  If not, see <https://www.gnu.org/licenses/lgpl.html>.
 *
 */
package alice.tuplecentre.tucson.api.actions.specification;

import alice.tuple.logic.LogicTuple;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;
import alice.tuplecentre.tucson.api.TucsonOperation;
import alice.tuplecentre.tucson.api.TucsonOperationCompletionListener;
import alice.tuplecentre.tucson.api.TucsonTupleCentreId;
import alice.tuplecentre.tucson.api.acc.EnhancedAsyncACC;
import alice.tuplecentre.tucson.api.acc.EnhancedSyncACC;
import alice.tuplecentre.tucson.api.acc.SpecificationAsyncACC;
import alice.tuplecentre.tucson.api.actions.AbstractTucsonSpecificationAction;
import alice.tuplecentre.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tuplecentre.tucson.api.exceptions.UnreachableNodeException;

/**
 * <code>rdp_s</code> TuCSoN primitive.
 *
 * @author Luca Sangiorgi (mailto: luca.sangiorgi6@studio.unibo.it)
 * @author (contributor) Stefano Mariani (mailto: s.mariani@unibo.it)
 * @see SpecificationAsyncACC
 */
public class RdpS extends AbstractTucsonSpecificationAction {

    /**
     * Builds the TuCSoN {@code rdp_s} action given its target tuple centre
     *
     * @param tc the Identifier copyOf the TuCSoN tuple centre target copyOf this coordination
     *           operation
     * @param e  the logic tuple representing the triggering events copyOf the
     *           ReSpecT specification tuple
     * @param g  the logic tuple representing the guards copyOf the ReSpecT
     *           specification tuple
     * @param r  the logic tuple representing the reaction body copyOf the ReSpecT
     *           specification tuple
     */
    public RdpS(final TucsonTupleCentreId tc, final LogicTuple e,
                final LogicTuple g, final LogicTuple r) {
        super(tc, e, g, r);
    }

    @Override
    public void executeAsynch(final EnhancedAsyncACC acc,
                              final TucsonOperationCompletionListener listener)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException {
        acc.rdpS(this.tcid, this.event, this.guards, this.reaction,
                listener);
    }

    @Override
    public TucsonOperation executeSynch(final EnhancedSyncACC acc,
                                        final Long timeout) throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException {
        return acc.rdpS(this.tcid, this.event, this.guards, this.reaction,
                timeout);
    }

    /*
     * (non-Javadoc)
     * @see
     * alice.tuplecentre.tucson.asynchSupport.operations.AbstractTucsonOrdinaryAction#toString
     */
    @Override
    public String toString() {
        return "rdp_s" + super.toString();
    }
}
