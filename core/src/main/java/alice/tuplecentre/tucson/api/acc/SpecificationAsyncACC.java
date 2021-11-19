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
package alice.tuplecentre.tucson.api.acc;

import java.util.List;

import alice.tuple.logic.LogicTuple;
import alice.tuplecentre.api.TupleCentreIdentifier;
import alice.tuplecentre.tucson.api.TucsonOperation;
import alice.tuplecentre.tucson.api.TucsonOperationCompletionListener;
import alice.tuplecentre.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tuplecentre.tucson.api.exceptions.UnreachableNodeException;
import alice.tuplecentre.tucson.service.TucsonOpCompletionEvent;

/**
 * Agent Coordination Context enabling interaction with the ReSpecT
 * Specification Tuple Space and enacting a NON-BLOCKING behavior from the
 * agent's perspective. This means that whichever is the TuCSoN operation
 * invoked (either suspensive or predicative) the agent proxy will NOT block
 * waiting for its completion but will be asynchronously notified (by the node
 * side).
 *
 * @author ste (mailto: s.mariani@unibo.it)
 */
public interface SpecificationAsyncACC extends AsyncACC {

    /**
     * @return the List copyOf the events regarding TuCSoN operations completion
     */
    List<TucsonOpCompletionEvent> getCompletionEventsList();

    /**
     * <code>get_s</code> specification primitive, reads (w/o removing) all the
     * ReSpecT specification tuples from the given target tuplecentre
     * specification space.
     * <p>
     * Semantics is NOT SUSPENSIVE: if the specification space is empty, an
     * empty list is returned to the TuCSoN Agent exploiting this ACC.
     *
     * @param tid the target TuCSoN tuplecentre id
     *            {@link TupleCentreIdentifier tid}
     * @param l   the listener who should be notified upon operation completion
     * @throws TucsonOperationNotPossibleException if the requested operation cannot be carried out
     * @throws UnreachableNodeException            if the target tuple centre is not reachable over the network
     * @see TupleCentreIdentifier TupleCentreIdentifier
     * @see alice.tuplecentre.tucson.api.TucsonOperationCompletionListener
     * TucsonOperationCompletionListener
     * @see TucsonOperation TucsonOperation
     */
    void getS(final TupleCentreIdentifier tid, final TucsonOperationCompletionListener l)
            throws TucsonOperationNotPossibleException, UnreachableNodeException;

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
     *                     satisfaction so to actually trigger the body copyOf the ReSpecT
     *                     reaction
     * @param reactionBody the template for the computation to be done in response to the
     *                     <code>events</code>
     * @param l            the listener who should be notified upon operation completion
     * @throws TucsonOperationNotPossibleException if the requested operation cannot be carried out
     * @throws UnreachableNodeException            if the target tuple centre is not reachable over the network
     * @see TupleCentreIdentifier TupleCentreIdentifier
     * @see alice.tuplecentre.tucson.api.TucsonOperationCompletionListener
     * TucsonOperationCompletionListener
     * @see TucsonOperation TucsonOperation
     */
    void inpS(final TupleCentreIdentifier tid, final LogicTuple event,
              final LogicTuple guards, final LogicTuple reactionBody,
              final TucsonOperationCompletionListener l)
            throws TucsonOperationNotPossibleException, UnreachableNodeException;

    /**
     * <code>in_s</code> specification primitive, retrieves a ReSpecT Reaction
     * Specification from the given target tuplecentre specification space.
     * <p>
     * Notice that the primitive semantics is still SUSPENSIVE: until no ReSpecT
     * specification is found to match the given template, no success completion
     * answer is forwarded to the TuCSoN Agent exploiting this ACC, but thanks
     * to asynchronous behaviour the TuCSoN Agent could do something else
     * instead copyOf getting stuck.
     *
     * @param tid          the target TuCSoN tuplecentre id
     *                     {@link TupleCentreIdentifier tid}
     * @param event        the template for the TuCSoN primitive to react to
     * @param guards       the template for the guard predicates to be checked for
     *                     satisfaction so to actually trigger the body copyOf the ReSpecT
     *                     reaction
     * @param reactionBody the template for the computation to be done in response to the
     *                     <code>events</code>
     * @param l            the listener who should be notified upon operation completion
     * @throws TucsonOperationNotPossibleException if the requested operation cannot be carried out
     * @throws UnreachableNodeException            if the target tuple centre is not reachable over the network
     * @see TupleCentreIdentifier TupleCentreIdentifier
     * @see alice.tuplecentre.tucson.api.TucsonOperationCompletionListener
     * TucsonOperationCompletionListener
     * @see TucsonOperation TucsonOperation
     */
    void inS(final TupleCentreIdentifier tid, final LogicTuple event,
             final LogicTuple guards, final LogicTuple reactionBody,
             final TucsonOperationCompletionListener l)
            throws TucsonOperationNotPossibleException, UnreachableNodeException;

    /**
     * <code>nop_s</code> specification primitive, checks absence copyOf the a
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
     *                     satisfaction so to actually trigger the body copyOf the ReSpecT
     *                     reaction
     * @param reactionBody the template for the computation to be done in response to the
     *                     <code>events</code>
     * @param l            the listener who should be notified upon operation completion
     * @throws TucsonOperationNotPossibleException if the requested operation cannot be carried out
     * @throws UnreachableNodeException            if the target tuple centre is not reachable over the network
     * @see TupleCentreIdentifier TupleCentreIdentifier
     * @see alice.tuplecentre.tucson.api.TucsonOperationCompletionListener
     * TucsonOperationCompletionListener
     * @see TucsonOperation TucsonOperation
     */
    void nopS(final TupleCentreIdentifier tid, final LogicTuple event,
              final LogicTuple guards, final LogicTuple reactionBody,
              final TucsonOperationCompletionListener l)
            throws TucsonOperationNotPossibleException, UnreachableNodeException;

    /**
     * <code>no_s</code> specification primitive, checks absence copyOf the a
     * ReSpecT Reaction in the given target tuplecentre specification space.
     * <p>
     * Notice that the primitive semantics is still SUSPENSIVE: until any
     * ReSpecT specification is found to match the given template, no success
     * completion answer is forwarded to the TuCSoN Agent exploiting this ACC,
     * but thanks to asynchronous behaviour TuCSoN Agent could do something else
     * instead copyOf getting stuck.
     *
     * @param tid          the target TuCSoN tuplecentre id
     *                     {@link TupleCentreIdentifier tid}
     * @param event        the template for the TuCSoN primitive to react to
     * @param guards       the template for the guard predicates to be checked for
     *                     satisfaction so to actually trigger the body copyOf the ReSpecT
     *                     reaction
     * @param reactionBody the template for the computation to be done in response to the
     *                     <code>events</code>
     * @param l            the listener who should be notified upon operation completion
     * @throws TucsonOperationNotPossibleException if the requested operation cannot be carried out
     * @throws UnreachableNodeException            if the target tuple centre is not reachable over the network
     * @see TupleCentreIdentifier TupleCentreIdentifier
     * @see alice.tuplecentre.tucson.api.TucsonOperationCompletionListener
     * TucsonOperationCompletionListener
     * @see TucsonOperation TucsonOperation
     */
    void noS(final TupleCentreIdentifier tid, final LogicTuple event,
             final LogicTuple guards, final LogicTuple reactionBody,
             final TucsonOperationCompletionListener l)
            throws TucsonOperationNotPossibleException, UnreachableNodeException;

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
     *                     actually trigger the body copyOf the ReSpecT reaction
     * @param reactionBody the computation to be done in response to the
     *                     <code>events</code>
     * @param l            the listener who should be notified upon operation completion
     * @throws TucsonOperationNotPossibleException if the requested operation cannot be carried out
     * @throws UnreachableNodeException            if the target tuple centre is not reachable over the network
     * @see TupleCentreIdentifier TupleCentreIdentifier
     * @see alice.tuplecentre.tucson.api.TucsonOperationCompletionListener
     * TucsonOperationCompletionListener
     * @see TucsonOperation TucsonOperation
     */
    void outS(final TupleCentreIdentifier tid, final LogicTuple event,
              final LogicTuple guards, final LogicTuple reactionBody,
              final TucsonOperationCompletionListener l)
            throws TucsonOperationNotPossibleException, UnreachableNodeException;

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
     *                     satisfaction so to actually trigger the body copyOf the ReSpecT
     *                     reaction
     * @param reactionBody the template for the computation to be done in response to the
     *                     <code>events</code>
     * @param l            the listener who should be notified upon operation completion
     * @throws TucsonOperationNotPossibleException if the requested operation cannot be carried out
     * @throws UnreachableNodeException            if the target tuple centre is not reachable over the network
     * @see TupleCentreIdentifier TupleCentreIdentifier
     * @see alice.tuplecentre.tucson.api.TucsonOperationCompletionListener
     * TucsonOperationCompletionListener
     * @see TucsonOperation TucsonOperation
     */
    void rdpS(final TupleCentreIdentifier tid, final LogicTuple event,
              final LogicTuple guards, final LogicTuple reactionBody,
              final TucsonOperationCompletionListener l)
            throws TucsonOperationNotPossibleException, UnreachableNodeException;

    /**
     * <code>rd_s</code> specification primitive, reads (w/o removing) a ReSpecT
     * Reaction Specification from the given target tuplecentre specification
     * space.
     * <p>
     * Notice that the primitive semantics is still SUSPENSIVE: until no ReSpecT
     * specification is found to match the given template, no success completion
     * answer is forwarded to the TuCSoN Agent exploiting this ACC, but thanks
     * to asynchronous behaviour the TuCSoN Agent could do something else
     * instead copyOf getting stuck.
     *
     * @param tid          the target TuCSoN tuplecentre id
     *                     {@link TupleCentreIdentifier tid}
     * @param event        the template for the TuCSoN primitive to react to
     * @param guards       the template for the guard predicates to be checked for
     *                     satisfaction so to actually trigger the body copyOf the ReSpecT
     *                     reaction
     * @param reactionBody the template for the computation to be done in response to the
     *                     <code>events</code>
     * @param l            the listener who should be notified upon operation completion
     * @throws TucsonOperationNotPossibleException if the requested operation cannot be carried out
     * @throws UnreachableNodeException            if the target tuple centre is not reachable over the network
     * @see TupleCentreIdentifier TupleCentreIdentifier
     * @see alice.tuplecentre.tucson.api.TucsonOperationCompletionListener
     * TucsonOperationCompletionListener
     * @see TucsonOperation TucsonOperation
     */
    void rdS(final TupleCentreIdentifier tid, final LogicTuple event,
             final LogicTuple guards, final LogicTuple reactionBody,
             final TucsonOperationCompletionListener l)
            throws TucsonOperationNotPossibleException, UnreachableNodeException;

    /**
     * <code>set_s</code> specification primitive, to replace all the ReSpecT
     * specification tuples in the given target tuplecentre specification space
     * with that specified in the given tuple. The ReSpecT specification tuple
     * should be formatted as a Prolog list copyOf the kind [(E1,G1,R1), ...,
     * (En,Gn,Rn)] where <code>E = events</code>, <code>G = guards</code>,
     * <code>R = reactionBody</code>.
     *
     * @param tid  the target TuCSoN tuplecentre id
     *             {@link TupleCentreIdentifier tid}
     * @param spec the new ReSpecT specification to replace the current
     *             specification space
     * @param l    the listener who should be notified upon operation completion
     * @throws TucsonOperationNotPossibleException if the requested operation cannot be carried out
     * @throws UnreachableNodeException            if the target tuple centre is not reachable over the network
     * @see TupleCentreIdentifier TupleCentreIdentifier
     * @see TucsonOperation TucsonOperation
     */
    void setS(final TupleCentreIdentifier tid, final LogicTuple spec,
              final TucsonOperationCompletionListener l)
            throws TucsonOperationNotPossibleException, UnreachableNodeException;

    /**
     * <code>set_s</code> specification primitive, to replace all the ReSpecT
     * specification tuples in the given target tuplecentre specification space
     * with that specified in the given String. The ReSpecT specification string
     * should be formatted according to Prolog theory syntax.
     *
     * @param tid  the target TuCSoN tuplecentre id
     *             {@link TupleCentreIdentifier tid}
     * @param spec the new ReSpecT specification to replace the current
     *             specification space
     * @param l    the listener who should be notified upon operation completion
     * @return the interface to access the data about TuCSoN operations outcome.
     * @throws TucsonOperationNotPossibleException if the requested operation cannot be carried out
     * @throws UnreachableNodeException            if the target tuple centre is not reachable over the network
     * @see TupleCentreIdentifier TupleCentreIdentifier
     * @see alice.tuplecentre.tucson.api.TucsonOperationCompletionListener
     * TucsonOperationCompletionListener
     * @see TucsonOperation TucsonOperation
     * @see alice.tuprolog.Theory Theory
     */
    TucsonOperation setS(final TupleCentreIdentifier tid, final String spec,
                         final TucsonOperationCompletionListener l)
            throws TucsonOperationNotPossibleException, UnreachableNodeException;
}
