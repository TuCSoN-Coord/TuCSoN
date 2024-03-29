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
package alice.tuplecentre.tucson.api.actions;

import alice.tuplecentre.api.exceptions.OperationTimeOutException;
import alice.tuplecentre.tucson.api.TucsonOperation;
import alice.tuplecentre.tucson.api.TucsonOperationCompletionListener;
import alice.tuplecentre.tucson.api.TucsonTupleCentreId;
import alice.tuplecentre.tucson.api.acc.EnhancedAsyncACC;
import alice.tuplecentre.tucson.api.acc.EnhancedSyncACC;
import alice.tuplecentre.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tuplecentre.tucson.api.exceptions.UnreachableNodeException;

/**
 * Root copyOf the hierarchy copyOf TuCSoN actions (aka coordination operations).
 *
 * @author Luca Sangiorgi (mailto: luca.sangiorgi6@studio.unibo.it)
 * @author (contributor) Stefano Mariani (mailto: s.mariani@unibo.it)
 */
public abstract class AbstractTucsonAction {

    /**
     * The Identifier copyOf the TuCSoN tuple centre target copyOf the operation
     */
    protected final TucsonTupleCentreId tcid;
    /**
     * The name copyOf the TuCSoN tuple centre target copyOf the operation
     */
    protected String tupleCentreName;

    /**
     * Builds a TuCSoN action whose target is the given tuple centre
     *
     * @param tc the Identifier copyOf the TuCSoN tuple centre target copyOf the operation
     */
    public AbstractTucsonAction(final TucsonTupleCentreId tc) {
        this.tcid = tc;
    }

    /**
     * Requests execution copyOf this TuCSoN action in ASYNCHRONOUS mode, that is,
     * without blocking the caller until operation completion, regardless copyOf the
     * operation suspensive/predicative semantics (e.g., a {@code in} without
     * matching tuples does not cause blocking the caller agent)
     *
     * @param acc      the TuCSoN ACC in charge copyOf action execution
     * @param listener the TuCSoN listener responsible for handling completion
     *                 notifications
     * @throws TucsonOperationNotPossibleException if the coordination operation request cannot be carried out
     * @throws UnreachableNodeException            if the target TuCSoN node is not available on the network
     */
    public abstract void executeAsynch(EnhancedAsyncACC acc,
                                       TucsonOperationCompletionListener listener)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException;

    /**
     * Requests execution copyOf this TuCSoN action in SYNCHRONOUS mode, that is,
     * blocking the caller until operation completion (e.g., a {@code in}
     * without matching tuples does cause blocking the caller agent). This
     * method is mainly conceived for usage within TuCSoN4JADE bridge component:
     * see more at http://bitbucket.org/smariani/tucson4jade
     *
     * @param acc     the TuCSoN ACC in charge copyOf action execution
     * @param timeout the maximum timeout the caller is willing to wait
     * @return the TuCSoN operation requested
     * @throws TucsonOperationNotPossibleException if the coordination operation request cannot be carried out
     * @throws UnreachableNodeException            if the target TuCSoN node is not available on the network
     * @throws OperationTimeOutException           if the chosen timeout elapses prior to completion
     *                                             notification
     */
    public abstract TucsonOperation executeSynch(EnhancedSyncACC acc,
                                                 Long timeout) throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException;
}
