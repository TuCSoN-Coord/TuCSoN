/*
 * TuCSoN coordination infrastructure - Copyright (C) 2001-2002 aliCE team at
 * deis.unibo.it This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of the License,
 * or (at your option) any later version. This library is distributed in the
 * hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU Lesser General Public License for more details. You should have
 * received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */
package alice.tucson.api.acc;

import alice.tucson.api.TucsonOperation;
import alice.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tucson.api.exceptions.UnreachableNodeException;
import alice.tuplecentre.api.Tuple;
import alice.tuplecentre.api.TupleCentreId;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;

/**
 * Bulk Synchronous ACC. Can act on the ordinary tuple space. Only bulk
 * primitives are included.
 *
 * @author ste (mailto: s.mariani@unibo.it)
 */
public interface BulkSyncACC extends SyncACC {

    /**
     * Withdraws from the space all the tuples matching the given template in
     * one shot (a single transition step). The empty list may be returned in
     * case no tuples match. Matching tuples are removed from the space.
     *
     * @param tid     the TupleCentreId of the target tuple centre
     * @param tuple   the tuple template to be used to retrieve tuples
     * @param timeout the maximum waiting time for completion tolerated by the
     *                TuCSoN agent behind this ACC. Notice that reaching the timeout
     *                just unblocks the agent, but the request IS NOT REMOVED from
     *                TuCSoN node pending requests (will still be served at sometime
     *                in the future).
     * @return the TucsonOperation object storing the outcome of the execution.
     * Notice due to synchronous semantics, it is guaranteed to store
     * the result of the operation.
     * @throws TucsonOperationNotPossibleException if the requested operation cannot be carried out
     * @throws UnreachableNodeException            if the target tuple centre is not reachable over the network
     * @throws OperationTimeOutException           if the operation timeout expired prior to operation
     *                                             completion
     * @see alice.tuplecentre.api.TupleCentreId TupleCentreId
     * @see alice.tucson.api.TucsonOperationCompletionListener
     * @see TucsonOperation TucsonOperation
     * @see alice.tuprolog.Struct Struct
     */
    TucsonOperation inAll(final TupleCentreId tid, final Tuple tuple, final Long timeout)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException;

    /**
     * Checks absence from the space of any tuples matching the given template
     * in one shot (a single transition step). In case of success, no difference
     * can be perceived with <code> no </code> primitive. In case of failure,
     * all the tuples matching the template are returned (with <code> no </code>
     * only one non-deterministically selected is returned).
     *
     * @param tid     the TupleCentreId of the target tuple centre
     * @param tuple   the tuple template to be used to check absence
     * @param timeout the maximum waiting time for completion tolerated by the
     *                TuCSoN agent behind this ACC. Notice that reaching the timeout
     *                just unblocks the agent, but the request IS NOT REMOVED from
     *                TuCSoN node pending requests (will still be served at sometime
     *                in the future).
     * @return the TucsonOperation object storing the outcome of the execution.
     * Notice due to synchronous semantics, it is guaranteed to store
     * the result of the operation.
     * @throws TucsonOperationNotPossibleException if the requested operation cannot be carried out
     * @throws UnreachableNodeException            if the target tuple centre is not reachable over the network
     * @throws OperationTimeOutException           if the operation timeout expired prior to operation
     *                                             completion
     * @see alice.tuplecentre.api.TupleCentreId TupleCentreId
     * @see alice.tucson.api.TucsonOperationCompletionListener
     * @see TucsonOperation TucsonOperation
     * @see alice.tuprolog.Struct Struct
     */
    TucsonOperation noAll(final TupleCentreId tid, final Tuple tuple, final Long timeout)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException;

    /**
     * Inject in the space a list of tuples in one shot (a single transition
     * step).
     *
     * @param tid     the TupleCentreId of the target tuple centre
     * @param tuple   the list of tuples to inject (must be a Prolog list)
     * @param timeout the maximum waiting time for completion tolerated by the
     *                TuCSoN agent behind this ACC. Notice that reaching the timeout
     *                just unblocks the agent, but the request IS NOT REMOVED from
     *                TuCSoN node pending requests (will still be served at sometime
     *                in the future).
     * @return the TucsonOperation object storing the outcome of the execution.
     * Notice due to synchronous semantics, it is guaranteed to store
     * the result of the operation.
     * @throws TucsonOperationNotPossibleException if the requested operation cannot be carried out
     * @throws UnreachableNodeException            if the target tuple centre is not reachable over the network
     * @throws OperationTimeOutException           if the operation timeout expired prior to operation
     *                                             completion
     * @see alice.tuplecentre.api.TupleCentreId TupleCentreId
     * @see alice.tucson.api.TucsonOperationCompletionListener
     * @see TucsonOperation TucsonOperation
     * @see alice.tuprolog.Struct Struct
     */
    TucsonOperation outAll(final TupleCentreId tid, final Tuple tuple, final Long timeout)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException;

    /**
     * Reads from the space all the tuples matching the given template in one
     * shot (a single transition step). The empty list may be returned in case
     * no tuples match. Matching tuples are NOT removed from the space.
     *
     * @param tid     the TupleCentreId of the target tuple centre
     * @param tuple   the tuple template to be used to observe tuples
     * @param timeout the maximum waiting time for completion tolerated by the
     *                TuCSoN agent behind this ACC. Notice that reaching the timeout
     *                just unblocks the agent, but the request IS NOT REMOVED from
     *                TuCSoN node pending requests (will still be served at sometime
     *                in the future).
     * @return the TucsonOperation object storing the outcome of the execution.
     * Notice due to synchronous semantics, it is guaranteed to store
     * the result of the operation.
     * @throws TucsonOperationNotPossibleException if the requested operation cannot be carried out
     * @throws UnreachableNodeException            if the target tuple centre is not reachable over the network
     * @throws OperationTimeOutException           if the operation timeout expired prior to operation
     *                                             completion
     * @see alice.tuplecentre.api.TupleCentreId TupleCentreId
     * @see alice.tucson.api.TucsonOperationCompletionListener
     * @see TucsonOperation TucsonOperation
     * @see alice.tuprolog.Struct Struct
     */
    TucsonOperation rdAll(final TupleCentreId tid, final Tuple tuple, final Long timeout)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException;
}
