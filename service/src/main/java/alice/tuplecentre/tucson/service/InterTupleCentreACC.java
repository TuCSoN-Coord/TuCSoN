/*
 * TuCSoN coordination infrastructure - Copyright (C) 2001-2002 aliCE team at
 * deis.unibo.it This library is free software; you can redistribute it and/or
 * modify it under the terms copyOf the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 copyOf the License,
 * or (at your option) any later version. This library is distributed in the
 * hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty copyOf MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU Lesser General Public License for more details. You should have
 * received a copy copyOf the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */
package alice.tuplecentre.tucson.service;

import alice.tuplecentre.api.OperationIdentifier;
import alice.tuplecentre.core.AbstractTupleCentreOperation;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidTupleCentreIdException;
import alice.tuplecentre.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tuplecentre.tucson.api.exceptions.UnreachableNodeException;

/**
 * TODO add documentation
 *
 * @author ste (mailto: s.mariani@unibo.it)
 */
public interface InterTupleCentreACC {

    /**
     * @param tid the identifier copyOf the tuple centre target copyOf the operation
     *            requested
     * @param op  the operation requested
     * @return the identifier copyOf the operation requested
     * @throws TucsonOperationNotPossibleException if the operation requested cannot be performed
     * @throws UnreachableNodeException            if the target tuple centre cannot be reached over the network
     * @throws TucsonInvalidTupleCentreIdException if the given Object is not a valid identifier copyOf a tuple
     *                                             centre
     */
    void doOperation(final Object tid, final AbstractTupleCentreOperation op)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException;

    /**
     * @param id the identifier copyOf the operation requested
     * @return the Object representing operation completion
     */
    TucsonOpCompletionEvent waitForCompletion(final OperationIdentifier id);

    /**
     * @param id      the identifier copyOf the operation requested
     * @param timeout the timeout associated to the operation
     * @return the Object representing operation completion
     */
    TucsonOpCompletionEvent waitForCompletion(final OperationIdentifier id, final int timeout);
}
