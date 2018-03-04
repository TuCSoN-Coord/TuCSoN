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
package alice.tucson.api.actions;

import alice.tucson.api.TucsonTupleCentreId;
import alice.tucson.api.acc.EnhancedAsyncACC;
import alice.tucson.api.acc.EnhancedSyncACC;
import alice.tucson.api.TucsonOperation;
import alice.tucson.api.TucsonOperationCompletionListener;
import alice.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tucson.api.exceptions.UnreachableNodeException;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;

/**
 * Root of the hierarchy of TuCSoN actions (aka coordination operations).
 *
 * @author Luca Sangiorgi (mailto: luca.sangiorgi6@studio.unibo.it)
 * @author (contributor) Stefano Mariani (mailto: s.mariani@unibo.it)
 *
 */
public abstract class AbstractTucsonAction {

    /**
     * The Identifier of the TuCSoN tuple centre target of the operation
     */
    protected TucsonTupleCentreId tcid;
    /**
     * The name of the TuCSoN tuple centre target of the operation
     */
    protected String tupleCentreName;

    /**
     * Builds a TuCSoN action whose target is the given tuple centre
     * 
     * @param tc
     *            the Identifier of the TuCSoN tuple centre target of the operation
     */
    public AbstractTucsonAction(final TucsonTupleCentreId tc) {
        this.tcid = tc;
    }

    /**
     * Requests execution of this TuCSoN action in ASYNCHRONOUS mode, that is,
     * without blocking the caller until operation completion, regardless of the
     * operation suspensive/predicative semantics (e.g., a {@code in} without
     * matching tuples does not cause blocking the caller agent)
     * 
     * @param acc
     *            the TuCSoN ACC in charge of action execution
     * @param listener
     *            the TuCSoN listener responsible for handling completion
     *            notifications
     * @return the TuCSoN operation requested
     * @throws TucsonOperationNotPossibleException
     *             if the coordination operation request cannot be carried out
     * @throws UnreachableNodeException
     *             if the target TuCSoN node is not available on the network
     */
    public abstract TucsonOperation executeAsynch(EnhancedAsyncACC acc,
                                                  TucsonOperationCompletionListener listener)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException;

    /**
     * Requests execution of this TuCSoN action in SYNCHRONOUS mode, that is,
     * blocking the caller until operation completion (e.g., a {@code in}
     * without matching tuples does cause blocking the caller agent). This
     * method is mainly conceived for usage within TuCSoN4JADE bridge component:
     * see more at http://bitbucket.org/smariani/tucson4jade
     * 
     * @param acc
     *            the TuCSoN ACC in charge of action execution
     * @param timeout
     *            the maximum timeout the caller is willing to wait
     * @return the TuCSoN operation requested
     * @throws TucsonOperationNotPossibleException
     *             if the coordination operation request cannot be carried out
     * @throws UnreachableNodeException
     *             if the target TuCSoN node is not available on the network
     * @throws OperationTimeOutException
     *             if the chosen timeout elapses prior to completion
     *             notification
     */
    public abstract TucsonOperation executeSynch(EnhancedSyncACC acc,
                                                 Long timeout) throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException;
}
