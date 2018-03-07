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
package alice.tuplecentre.tucson.api.acc;

import alice.tuplecentre.api.Tuple;
import alice.tuplecentre.api.TupleCentreIdentifier;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;
import alice.tuplecentre.tucson.api.TucsonOperation;
import alice.tuplecentre.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tuplecentre.tucson.api.exceptions.UnreachableNodeException;

/**
 * @author ste (mailto: s.mariani@unibo.it)
 */
public interface UniformSyncACC extends SyncACC {

    /**
     * <code>uin</code> TuCSoN primitive, retrieves the specified tuple from the
     * given target tuplecentre. If more than one tuple matches the template,
     * Linda's non-deterministic selection is replaced by PROBABILISTIC,
     * UNIFORMLY DISTRIBUTED selection: the more a tuple is present, the more
     * likely it will be returned.
     * <p>
     * Notice that the primitive semantics is SUSPENSIVE: until no tuple is
     * found to match the given template, no success completion answer is
     * forwarded to the TuCSoN Agent exploiting this ACC, which then is blocked
     * waiting.
     *
     * @param tid     the target TuCSoN tuplecentre id
     *                {@link TupleCentreIdentifier tid}
     * @param tuple   the tuple to be retrieved from the target tuplecentre
     * @param timeout the maximum waiting time for completion tolerated by the
     *                TuCSoN agent behind this ACC. Notice that reaching the timeout
     *                just unblocks the agent, but the request IS NOT REMOVED from
     *                TuCSoN node pending requests (will still be served at sometime
     *                in the future).
     * @return the interface to access the data about TuCSoN operations outcome.
     * @throws TucsonOperationNotPossibleException if the requested operation cannot be carried out
     * @throws UnreachableNodeException            if the target tuple centre is not reachable over the network
     * @throws OperationTimeOutException           if the operation timeout expired prior to operation
     *                                             completion
     * @see TupleCentreIdentifier TupleCentreIdentifier
     * @see TucsonOperation TucsonOperation
     */
    TucsonOperation uin(final TupleCentreIdentifier tid, final Tuple tuple, final Long timeout)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException;

    /**
     * <code>uinp</code> TuCSoN primitive, retrieves the specified tuple from
     * the given target tuplecentre. If more than one tuple matches the
     * template, Linda's non-deterministic selection is replaced by
     * PROBABILISTIC, UNIFORMLY DISTRIBUTED selection: the more a tuple is
     * present, the more likely it will be returned.
     * <p>
     * This time the primitive semantics is NOT SUSPENSIVE: if no tuple is found
     * to match the given template, a failure completion answer is forwarded to
     * the TuCSoN Agent exploiting this ACC.
     *
     * @param tid     the target TuCSoN tuplecentre id
     *                {@link TupleCentreIdentifier tid}
     * @param tuple   the tuple to be retrieved from the target tuplecentre
     * @param timeout the maximum waiting time for completion tolerated by the
     *                TuCSoN agent behind this ACC. Notice that reaching the timeout
     *                just unblocks the agent, but the request IS NOT REMOVED from
     *                TuCSoN node pending requests (will still be served at sometime
     *                in the future).
     * @return the interface to access the data about TuCSoN operations outcome.
     * @throws TucsonOperationNotPossibleException if the requested operation cannot be carried out
     * @throws UnreachableNodeException            if the target tuple centre is not reachable over the network
     * @throws OperationTimeOutException           if the operation timeout expired prior to operation
     *                                             completion
     * @see TupleCentreIdentifier TupleCentreIdentifier
     * @see TucsonOperation TucsonOperation
     */
    TucsonOperation uinp(final TupleCentreIdentifier tid, final Tuple tuple, final Long timeout)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException;

    /**
     * <code>uno</code> TuCSoN primitive, checks absence of the specified tuple
     * in the given target tuplecentre. If more than one tuple matches the
     * template, Linda's non-deterministic selection is replaced by
     * PROBABILISTIC, UNIFORMLY DISTRIBUTED selection: the more a tuple is
     * present, the more likely it will be returned.
     * <p>
     * Notice that the primitive semantics is SUSPENSIVE: until any tuple is
     * found to match the given template, no success completion answer is
     * forwarded to the TuCSoN Agent exploiting this ACC, which then is blocked
     * waiting.
     *
     * @param tid     the target TuCSoN tuplecentre id
     *                {@link TupleCentreIdentifier tid}
     * @param tuple   the tuple to be checked for absence from the target
     *                tuplecentre
     * @param timeout the maximum waiting time for completion tolerated by the
     *                TuCSoN agent behind this ACC. Notice that reaching the timeout
     *                just unblocks the agent, but the request IS NOT REMOVED from
     *                TuCSoN node pending requests (will still be served at sometime
     *                in the future).
     * @return the interface to access the data about TuCSoN operations outcome.
     * @throws TucsonOperationNotPossibleException if the requested operation cannot be carried out
     * @throws UnreachableNodeException            if the target tuple centre is not reachable over the network
     * @throws OperationTimeOutException           if the operation timeout expired prior to operation
     *                                             completion
     * @see TupleCentreIdentifier TupleCentreIdentifier
     * @see TucsonOperation TucsonOperation
     */
    TucsonOperation uno(final TupleCentreIdentifier tid, final Tuple tuple, final Long timeout)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException;

    /**
     * <code>unop</code> TuCSoN primitive, checks absence of the specified tuple
     * in the given target tuplecentre. If more than one tuple matches the
     * template, Linda's non-deterministic selection is replaced by
     * PROBABILISTIC, UNIFORMLY DISTRIBUTED selection: the more a tuple is
     * present, the more likely it will be returned.
     * <p>
     * This time the primitive semantics is NOT SUSPENSIVE: if any tuple is
     * found to match the given template, a failure completion answer is
     * forwarded to the TuCSoN Agent exploiting this ACC.
     *
     * @param tid     the target TuCSoN tuplecentre id
     *                {@link TupleCentreIdentifier tid}
     * @param tuple   the tuple to be checked for absence from the target
     *                tuplecentre
     * @param timeout the maximum waiting time for completion tolerated by the
     *                TuCSoN agent behind this ACC. Notice that reaching the timeout
     *                just unblocks the agent, but the request IS NOT REMOVED from
     *                TuCSoN node pending requests (will still be served at sometime
     *                in the future).
     * @return the interface to access the data about TuCSoN operations outcome.
     * @throws TucsonOperationNotPossibleException if the requested operation cannot be carried out
     * @throws UnreachableNodeException            if the target tuple centre is not reachable over the network
     * @throws OperationTimeOutException           if the operation timeout expired prior to operation
     *                                             completion
     * @see TupleCentreIdentifier TupleCentreIdentifier
     * @see TucsonOperation TucsonOperation
     */
    TucsonOperation unop(final TupleCentreIdentifier tid, final Tuple tuple, final Long timeout)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException;

    /**
     * <code>urd</code> TuCSoN primitive, reads (w/o removing) the specified
     * tuple from the given target tuplecentre. If more than one tuple matches
     * the template, Linda's non-deterministic selection is replaced by
     * PROBABILISTIC, UNIFORMLY DISTRIBUTED selection: the more a tuple is
     * present, the more likely it will be returned.
     * <p>
     * Notice that the primitive semantics is SUSPENSIVE: until no tuple is
     * found to match the given template, no success completion answer is
     * forwarded to the TuCSoN Agent exploiting this ACC, which then is blocked
     * waiting.
     *
     * @param tid     the target TuCSoN tuplecentre id
     *                {@link TupleCentreIdentifier tid}
     * @param tuple   the tuple to be read from the target tuplecentre
     * @param timeout the maximum waiting time for completion tolerated by the
     *                TuCSoN agent behind this ACC. Notice that reaching the timeout
     *                just unblocks the agent, but the request IS NOT REMOVED from
     *                TuCSoN node pending requests (will still be served at sometime
     *                in the future).
     * @return the interface to access the data about TuCSoN operations outcome.
     * @throws TucsonOperationNotPossibleException if the requested operation cannot be carried out
     * @throws UnreachableNodeException            if the target tuple centre is not reachable over the network
     * @throws OperationTimeOutException           if the operation timeout expired prior to operation
     *                                             completion
     * @see TupleCentreIdentifier TupleCentreIdentifier
     * @see TucsonOperation TucsonOperation
     */
    TucsonOperation urd(final TupleCentreIdentifier tid, final Tuple tuple, final Long timeout)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException;

    /**
     * <code>urdp</code> TuCSoN primitive, reads (w/o removing) the specified
     * tuple from the given target tuplecentre. If more than one tuple matches
     * the template, Linda's non-deterministic selection is replaced by
     * PROBABILISTIC, UNIFORMLY DISTRIBUTED selection: the more a tuple is
     * present, the more likely it will be returned.
     * <p>
     * This time the primitive semantics is NOT SUSPENSIVE: if no tuple is found
     * to match the given template, a failure completion answer is forwarded to
     * the TuCSoN Agent exploiting this ACC.
     *
     * @param tid     the target TuCSoN tuplecentre id
     *                {@link TupleCentreIdentifier tid}
     * @param tuple   the tuple to be read from the target tuplecentre
     * @param timeout the maximum waiting time for completion tolerated by the
     *                TuCSoN agent behind this ACC. Notice that reaching the timeout
     *                just unblocks the agent, but the request IS NOT REMOVED from
     *                TuCSoN node pending requests (will still be served at sometime
     *                in the future).
     * @return the interface to access the data about TuCSoN operations outcome.
     * @throws TucsonOperationNotPossibleException if the requested operation cannot be carried out
     * @throws UnreachableNodeException            if the target tuple centre is not reachable over the network
     * @throws OperationTimeOutException           if the operation timeout expired prior to operation
     *                                             completion
     * @see TupleCentreIdentifier TupleCentreIdentifier
     * @see TucsonOperation TucsonOperation
     */
    TucsonOperation urdp(final TupleCentreIdentifier tid, final Tuple tuple, final Long timeout)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException;
}
