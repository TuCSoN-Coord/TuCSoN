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

import alice.tuple.logic.LogicTuple;
import alice.tuplecentre.api.TupleCentreIdentifier;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;
import alice.tuplecentre.tucson.api.TucsonOperation;
import alice.tuplecentre.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tuplecentre.tucson.api.exceptions.UnreachableNodeException;

/**
 * Agent Coordination Context enabling interaction with the ReSpecT
 * Specification Tuple Space and enacting a BLOCKING behavior from the agent's
 * perspective. This means that whichever is the TuCSoN operation invoked
 * (either suspensive or predicative) the agent proxy WILL block waiting for its
 * completion (either success or failure).
 *
 * @author ste (mailto: s.mariani@unibo.it)
 */
public interface SpecificationSyncACC extends SyncACC {

    /**
     * <code>get_s</code> specification primitive, reads (w/o removing) all the
     * ReSpecT specification tuples from the given target tuplecentre
     * specification space.
     * <p>
     * Semantics is NOT SUSPENSIVE: if the specification space is empty, an
     * empty list is returned to the TuCSoN Agent exploiting this ACC.
     *
     * @param tid     the target TuCSoN tuplecentre id
     *                {@link TupleCentreIdentifier tid}
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
    TucsonOperation getS(final TupleCentreIdentifier tid, final Long timeout)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException;

    /**
     * <code>inp_s</code> specification primitive, retrieves a ReSpecT Reaction
     * Specification from the given target tuplecentre specification space.
     * <p>
     * This time the primitive semantics is NOT SUSPENSIVE: if no ReSpecT
     * specification is found to match the given template, a failure completion
     * answer is forwarded to the TuCSoN Agent exploiting this ACC.
     *
     * @param tid          the target TuCSoN tuplecentre id
     *                     {@link TupleCentreIdentifier tid}
     * @param event        the template for the TuCSoN primitive to react to
     * @param guards       the template for the guard predicates to be checked for
     *                     satisfaction so to actually trigger the body of the ReSpecT
     *                     reaction
     * @param reactionBody the template for the computation to be done in response to the
     *                     <code>events</code>
     * @param timeout      the maximum waiting time for completion tolerated by the
     *                     TuCSoN agent behind this ACC. Notice that reaching the timeout
     *                     just unblocks the agent, but the request IS NOT REMOVED from
     *                     TuCSoN node pending requests (will still be served at sometime
     *                     in the future).
     * @return the interface to access the data about TuCSoN operations outcome.
     * @throws TucsonOperationNotPossibleException if the requested operation cannot be carried out
     * @throws UnreachableNodeException            if the target tuple centre is not reachable over the network
     * @throws OperationTimeOutException           if the operation timeout expired prior to operation
     *                                             completion
     * @see TupleCentreIdentifier TupleCentreIdentifier
     * @see TucsonOperation TucsonOperation
     */
    TucsonOperation inpS(final TupleCentreIdentifier tid, final LogicTuple event,
                         final LogicTuple guards, final LogicTuple reactionBody, final Long timeout)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException;

    /**
     * <code>in_s</code> specification primitive, retrieves a ReSpecT Reaction
     * Specification from the given target tuplecentre specification space.
     * <p>
     * Notice that the primitive semantics is SUSPENSIVE: until no ReSpecT
     * specification is found to match the given template, no success completion
     * answer is forwarded to the TuCSoN Agent exploiting this ACC, which then
     * is blocked waiting.
     *
     * @param tid          the target TuCSoN tuplecentre id
     *                     {@link TupleCentreIdentifier tid}
     * @param event        the template for the TuCSoN primitive to react to
     * @param guards       the template for the guard predicates to be checked for
     *                     satisfaction so to actually trigger the body of the ReSpecT
     *                     reaction
     * @param reactionBody the template for the computation to be done in response to the
     *                     <code>events</code>
     * @param timeout      the maximum waiting time for completion tolerated by the
     *                     TuCSoN agent behind this ACC. Notice that reaching the timeout
     *                     just unblocks the agent, but the request IS NOT REMOVED from
     *                     TuCSoN node pending requests (will still be served at sometime
     *                     in the future).
     * @return the interface to access the data about TuCSoN operations outcome.
     * @throws TucsonOperationNotPossibleException if the requested operation cannot be carried out
     * @throws UnreachableNodeException            if the target tuple centre is not reachable over the network
     * @throws OperationTimeOutException           if the operation timeout expired prior to operation
     *                                             completion
     * @see TupleCentreIdentifier TupleCentreIdentifier
     * @see TucsonOperation TucsonOperation
     */
    TucsonOperation inS(final TupleCentreIdentifier tid, final LogicTuple event,
                        final LogicTuple guards, final LogicTuple reactionBody, final Long timeout)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException;

    /**
     * <code>nop_s</code> specification primitive, checks absence of the a
     * ReSpecT Reaction in the given target tuplecentre specification space.
     * <p>
     * This time the primitive semantics is NOT SUSPENSIVE: if any ReSpecT
     * specification is found to match the given template, a failure completion
     * answer is forwarded to the TuCSoN Agent exploiting this ACC.
     *
     * @param tid          the target TuCSoN tuplecentre id
     *                     {@link TupleCentreIdentifier tid}
     * @param event        the template for the TuCSoN primitive to react to
     * @param guards       the template for the guard predicates to be checked for
     *                     satisfaction so to actually trigger the body of the ReSpecT
     *                     reaction
     * @param reactionBody the template for the computation to be done in response to the
     *                     <code>events</code>
     * @param timeout      the maximum waiting time for completion tolerated by the
     *                     TuCSoN agent behind this ACC. Notice that reaching the timeout
     *                     just unblocks the agent, but the request IS NOT REMOVED from
     *                     TuCSoN node pending requests (will still be served at sometime
     *                     in the future).
     * @return the interface to access the data about TuCSoN operations outcome.
     * @throws TucsonOperationNotPossibleException if the requested operation cannot be carried out
     * @throws UnreachableNodeException            if the target tuple centre is not reachable over the network
     * @throws OperationTimeOutException           if the operation timeout expired prior to operation
     *                                             completion
     * @see TupleCentreIdentifier TupleCentreIdentifier
     * @see TucsonOperation TucsonOperation
     */
    TucsonOperation nopS(final TupleCentreIdentifier tid, final LogicTuple event,
                         final LogicTuple guards, final LogicTuple reactionBody, final Long timeout)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException;

    /**
     * <code>no_s</code> specification primitive, checks absence of the a
     * ReSpecT Reaction in the given target tuplecentre specification space.
     * <p>
     * Notice that the primitive semantics is SUSPENSIVE: until any ReSpecT
     * specification is found to match the given template, no success completion
     * answer is forwarded to the TuCSoN Agent exploiting this ACC, which then
     * is blocked waiting.
     *
     * @param tid          the target TuCSoN tuplecentre id
     *                     {@link TupleCentreIdentifier tid}
     * @param event        the template for the TuCSoN primitive to react to
     * @param guards       the template for the guard predicates to be checked for
     *                     satisfaction so to actually trigger the body of the ReSpecT
     *                     reaction
     * @param reactionBody the template for the computation to be done in response to the
     *                     <code>events</code>
     * @param timeout      the maximum waiting time for completion tolerated by the
     *                     TuCSoN agent behind this ACC. Notice that reaching the timeout
     *                     just unblocks the agent, but the request IS NOT REMOVED from
     *                     TuCSoN node pending requests (will still be served at sometime
     *                     in the future).
     * @return the interface to access the data about TuCSoN operations outcome.
     * @throws TucsonOperationNotPossibleException if the requested operation cannot be carried out
     * @throws UnreachableNodeException            if the target tuple centre is not reachable over the network
     * @throws OperationTimeOutException           if the operation timeout expired prior to operation
     *                                             completion
     * @see TupleCentreIdentifier TupleCentreIdentifier
     * @see TucsonOperation TucsonOperation
     */
    TucsonOperation noS(final TupleCentreIdentifier tid, final LogicTuple event,
                        final LogicTuple guards, final LogicTuple reactionBody, final Long timeout)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException;

    /**
     * <code>out_s</code> specification primitive, adds the ReSpecT Reaction
     * Specification in the given target tuplecentre specification space.
     * <p>
     * This TuCSoN <code>out_s</code> primitive assumes the ORDERED semantics,
     * hence the reaction specification is SUDDENLY injected in the target space
     * (if the primitive successfully completes).
     *
     * @param tid          the target TuCSoN tuplecentre id
     *                     {@link TupleCentreIdentifier tid}
     * @param event        the TuCSoN primitive to react to
     * @param guards       the guard predicates to be checked for satisfaction so to
     *                     actually trigger the body of the ReSpecT reaction
     * @param reactionBody the computation to be done in response to the
     *                     <code>events</code>
     * @param timeout      the maximum waiting time for completion tolerated by the
     *                     TuCSoN agent behind this ACC. Notice that reaching the timeout
     *                     just unblocks the agent, but the request IS NOT REMOVED from
     *                     TuCSoN node pending requests (will still be served at sometime
     *                     in the future).
     * @return the interface to access the data about TuCSoN operations outcome.
     * @throws TucsonOperationNotPossibleException if the requested operation cannot be carried out
     * @throws UnreachableNodeException            if the target tuple centre is not reachable over the network
     * @throws OperationTimeOutException           if the operation timeout expired prior to operation
     *                                             completion
     * @see TupleCentreIdentifier TupleCentreIdentifier
     * @see TucsonOperation TucsonOperation
     */
    TucsonOperation outS(final TupleCentreIdentifier tid, final LogicTuple event,
                         final LogicTuple guards, final LogicTuple reactionBody, final Long timeout)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException;

    /**
     * <code>rdp_s</code> specification primitive, reads (w/o removing) a
     * ReSpecT Reaction Specification from the given target tuplecentre
     * specification space.
     * <p>
     * This time the primitive semantics is NOT SUSPENSIVE: if no ReSpecT
     * specification is found to match the given template, a failure completion
     * answer is forwarded to the TuCSoN Agent exploiting this ACC.
     *
     * @param tid          the target TuCSoN tuplecentre id
     *                     {@link TupleCentreIdentifier tid}
     * @param event        the template for the TuCSoN primitive to react to
     * @param guards       the template for the guard predicates to be checked for
     *                     satisfaction so to actually trigger the body of the ReSpecT
     *                     reaction
     * @param reactionBody the template for the computation to be done in response to the
     *                     <code>events</code>
     * @param timeout      the maximum waiting time for completion tolerated by the
     *                     TuCSoN agent behind this ACC. Notice that reaching the timeout
     *                     just unblocks the agent, but the request IS NOT REMOVED from
     *                     TuCSoN node pending requests (will still be served at sometime
     *                     in the future).
     * @return the interface to access the data about TuCSoN operations outcome.
     * @throws TucsonOperationNotPossibleException if the requested operation cannot be carried out
     * @throws UnreachableNodeException            if the target tuple centre is not reachable over the network
     * @throws OperationTimeOutException           if the operation timeout expired prior to operation
     *                                             completion
     * @see TupleCentreIdentifier TupleCentreIdentifier
     * @see TucsonOperation TucsonOperation
     */
    TucsonOperation rdpS(final TupleCentreIdentifier tid, final LogicTuple event,
                         final LogicTuple guards, final LogicTuple reactionBody, final Long timeout)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException;

    /**
     * <code>in_s</code> specification primitive, reads (w/o removing) a ReSpecT
     * Reaction Specification from the given target tuplecentre specification
     * space.
     * <p>
     * Notice that the primitive semantics is SUSPENSIVE: until no ReSpecT
     * specification is found to match the given template, no success completion
     * answer is forwarded to the TuCSoN Agent exploiting this ACC, which then
     * is blocked waiting.
     *
     * @param tid          the target TuCSoN tuplecentre id
     *                     {@link TupleCentreIdentifier tid}
     * @param event        the template for the TuCSoN primitive to react to
     * @param guards       the template for the guard predicates to be checked for
     *                     satisfaction so to actually trigger the body of the ReSpecT
     *                     reaction
     * @param reactionBody the template for the computation to be done in response to the
     *                     <code>events</code>
     * @param timeout      the maximum waiting time for completion tolerated by the
     *                     TuCSoN agent behind this ACC. Notice that reaching the timeout
     *                     just unblocks the agent, but the request IS NOT REMOVED from
     *                     TuCSoN node pending requests (will still be served at sometime
     *                     in the future).
     * @return the interface to access the data about TuCSoN operations outcome.
     * @throws TucsonOperationNotPossibleException if the requested operation cannot be carried out
     * @throws UnreachableNodeException            if the target tuple centre is not reachable over the network
     * @throws OperationTimeOutException           if the operation timeout expired prior to operation
     *                                             completion
     * @see TupleCentreIdentifier TupleCentreIdentifier
     * @see TucsonOperation TucsonOperation
     */
    TucsonOperation rdS(final TupleCentreIdentifier tid, final LogicTuple event,
                        final LogicTuple guards, final LogicTuple reactionBody, final Long timeout)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException;

    /**
     * <code>set_s</code> specification primitive, to replace all the ReSpecT
     * specification tuples in the given target tuplecentre specification space
     * with that specified in the given tuple. The ReSpecT specification tuple
     * should be formatted as a Prolog list of the kind [(E1,G1,R1), ...,
     * (En,Gn,Rn)] where <code>E = events</code>, <code>G = guards</code>,
     * <code>R = reactionBody</code>.
     *
     * @param tid     the target TuCSoN tuplecentre id
     *                {@link TupleCentreIdentifier tid}
     * @param spec    the new ReSpecT specification to replace the current
     *                specification space
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
    TucsonOperation setS(final TupleCentreIdentifier tid, final LogicTuple spec, final Long timeout)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException;

    /**
     * <code>set_s</code> specification primitive, to replace all the ReSpecT
     * specification tuples in the given target tuplecentre specification space
     * with that specified in the given String. The ReSpecT specification string
     * should be formatted according to Prolog theory syntax.
     *
     * @param tid     the target TuCSoN tuplecentre id
     *                {@link TupleCentreIdentifier tid}
     * @param spec    the new ReSpecT specification to replace the current
     *                specification space
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
     * @see alice.tuprolog.Theory Theory
     */
    TucsonOperation setS(final TupleCentreIdentifier tid, final String spec, final Long timeout)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException;
}
