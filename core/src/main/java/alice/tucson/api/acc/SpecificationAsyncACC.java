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

import java.util.List;

import alice.logictuple.LogicTuple;
import alice.tucson.api.ITucsonOperation;
import alice.tucson.api.TucsonOperationCompletionListener;
import alice.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tucson.api.exceptions.UnreachableNodeException;
import alice.tucson.service.TucsonOpCompletionEvent;
import alice.tuplecentre.api.TupleCentreId;

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
public interface SpecificationAsyncACC extends RootACC {

    /**
     * @return the List of the events regarding TuCSoN operations completion
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
     *            {@link alice.tucson.api.TucsonTupleCentreId tid}
     * @param l   the listener who should be notified upon operation completion
     * @return the interface to access the data about TuCSoN operations outcome.
     * @throws TucsonOperationNotPossibleException if the requested operation cannot be carried out
     * @throws UnreachableNodeException            if the target tuple centre is not reachable over the network
     * @see alice.tuplecentre.api.TupleCentreId TupleCentreId
     * @see alice.tucson.api.TucsonOperationCompletionListener
     * TucsonOperationCompletionListener
     * @see alice.tucson.api.ITucsonOperation ITucsonOperation
     */
    ITucsonOperation getS(final TupleCentreId tid, final TucsonOperationCompletionListener l)
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
     *                     {@link alice.tucson.api.TucsonTupleCentreId tid}
     * @param event        the template for the TuCSoN primitive to react to
     * @param guards       the template for the guard predicates to be checked for
     *                     satisfaction so to actually trigger the body of the ReSpecT
     *                     reaction
     * @param reactionBody the template for the computation to be done in response to the
     *                     <code>event</code>
     * @param l            the listener who should be notified upon operation completion
     * @return the interface to access the data about TuCSoN operations outcome.
     * @throws TucsonOperationNotPossibleException if the requested operation cannot be carried out
     * @throws UnreachableNodeException            if the target tuple centre is not reachable over the network
     * @see alice.tuplecentre.api.TupleCentreId TupleCentreId
     * @see alice.tucson.api.TucsonOperationCompletionListener
     * TucsonOperationCompletionListener
     * @see alice.tucson.api.ITucsonOperation ITucsonOperation
     */
    ITucsonOperation inpS(final TupleCentreId tid, final LogicTuple event,
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
     * instead of getting stuck.
     *
     * @param tid          the target TuCSoN tuplecentre id
     *                     {@link alice.tucson.api.TucsonTupleCentreId tid}
     * @param event        the template for the TuCSoN primitive to react to
     * @param guards       the template for the guard predicates to be checked for
     *                     satisfaction so to actually trigger the body of the ReSpecT
     *                     reaction
     * @param reactionBody the template for the computation to be done in response to the
     *                     <code>event</code>
     * @param l            the listener who should be notified upon operation completion
     * @return the interface to access the data about TuCSoN operations outcome.
     * @throws TucsonOperationNotPossibleException if the requested operation cannot be carried out
     * @throws UnreachableNodeException            if the target tuple centre is not reachable over the network
     * @see alice.tuplecentre.api.TupleCentreId TupleCentreId
     * @see alice.tucson.api.TucsonOperationCompletionListener
     * TucsonOperationCompletionListener
     * @see alice.tucson.api.ITucsonOperation ITucsonOperation
     */
    ITucsonOperation inS(final TupleCentreId tid, final LogicTuple event,
                         final LogicTuple guards, final LogicTuple reactionBody,
                         final TucsonOperationCompletionListener l)
            throws TucsonOperationNotPossibleException, UnreachableNodeException;

    /**
     * <code>nop_s</code> specification primitive, checks absence of the a
     * ReSpecT Reaction in the given target tuplecentre specification space.
     * <p>
     * This time the primitive semantics is NOT SUSPENSIVE: if any ReSpecT
     * specification is found to match the given template, a failure completion
     * answer is forwarded to the TuCSoN Agent exploiting this ACC.
     *
     * @param tid          the target TuCSoN tuplecentre id
     *                     {@link alice.tucson.api.TucsonTupleCentreId tid}
     * @param event        the template for the TuCSoN primitive to react to
     * @param guards       the template for the guard predicates to be checked for
     *                     satisfaction so to actually trigger the body of the ReSpecT
     *                     reaction
     * @param reactionBody the template for the computation to be done in response to the
     *                     <code>event</code>
     * @param l            the listener who should be notified upon operation completion
     * @return the interface to access the data about TuCSoN operations outcome.
     * @throws TucsonOperationNotPossibleException if the requested operation cannot be carried out
     * @throws UnreachableNodeException            if the target tuple centre is not reachable over the network
     * @see alice.tuplecentre.api.TupleCentreId TupleCentreId
     * @see alice.tucson.api.TucsonOperationCompletionListener
     * TucsonOperationCompletionListener
     * @see alice.tucson.api.ITucsonOperation ITucsonOperation
     */
    ITucsonOperation nopS(final TupleCentreId tid, final LogicTuple event,
                          final LogicTuple guards, final LogicTuple reactionBody,
                          final TucsonOperationCompletionListener l)
            throws TucsonOperationNotPossibleException, UnreachableNodeException;

    /**
     * <code>no_s</code> specification primitive, checks absence of the a
     * ReSpecT Reaction in the given target tuplecentre specification space.
     * <p>
     * Notice that the primitive semantics is still SUSPENSIVE: until any
     * ReSpecT specification is found to match the given template, no success
     * completion answer is forwarded to the TuCSoN Agent exploiting this ACC,
     * but thanks to asynchronous behaviour TuCSoN Agent could do something else
     * instead of getting stuck.
     *
     * @param tid          the target TuCSoN tuplecentre id
     *                     {@link alice.tucson.api.TucsonTupleCentreId tid}
     * @param event        the template for the TuCSoN primitive to react to
     * @param guards       the template for the guard predicates to be checked for
     *                     satisfaction so to actually trigger the body of the ReSpecT
     *                     reaction
     * @param reactionBody the template for the computation to be done in response to the
     *                     <code>event</code>
     * @param l            the listener who should be notified upon operation completion
     * @return the interface to access the data about TuCSoN operations outcome.
     * @throws TucsonOperationNotPossibleException if the requested operation cannot be carried out
     * @throws UnreachableNodeException            if the target tuple centre is not reachable over the network
     * @see alice.tuplecentre.api.TupleCentreId TupleCentreId
     * @see alice.tucson.api.TucsonOperationCompletionListener
     * TucsonOperationCompletionListener
     * @see alice.tucson.api.ITucsonOperation ITucsonOperation
     */
    ITucsonOperation noS(final TupleCentreId tid, final LogicTuple event,
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
     *                     {@link alice.tucson.api.TucsonTupleCentreId tid}
     * @param event        the TuCSoN primitive to react to
     * @param guards       the guard predicates to be checked for satisfaction so to
     *                     actually trigger the body of the ReSpecT reaction
     * @param reactionBody the computation to be done in response to the
     *                     <code>event</code>
     * @param l            the listener who should be notified upon operation completion
     * @return the interface to access the data about TuCSoN operations outcome.
     * @throws TucsonOperationNotPossibleException if the requested operation cannot be carried out
     * @throws UnreachableNodeException            if the target tuple centre is not reachable over the network
     * @see alice.tuplecentre.api.TupleCentreId TupleCentreId
     * @see alice.tucson.api.TucsonOperationCompletionListener
     * TucsonOperationCompletionListener
     * @see alice.tucson.api.ITucsonOperation ITucsonOperation
     */
    ITucsonOperation outS(final TupleCentreId tid, final LogicTuple event,
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
     *                     {@link alice.tucson.api.TucsonTupleCentreId tid}
     * @param event        the template for the TuCSoN primitive to react to
     * @param guards       the template for the guard predicates to be checked for
     *                     satisfaction so to actually trigger the body of the ReSpecT
     *                     reaction
     * @param reactionBody the template for the computation to be done in response to the
     *                     <code>event</code>
     * @param l            the listener who should be notified upon operation completion
     * @return the interface to access the data about TuCSoN operations outcome.
     * @throws TucsonOperationNotPossibleException if the requested operation cannot be carried out
     * @throws UnreachableNodeException            if the target tuple centre is not reachable over the network
     * @see alice.tuplecentre.api.TupleCentreId TupleCentreId
     * @see alice.tucson.api.TucsonOperationCompletionListener
     * TucsonOperationCompletionListener
     * @see alice.tucson.api.ITucsonOperation ITucsonOperation
     */
    ITucsonOperation rdpS(final TupleCentreId tid, final LogicTuple event,
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
     * instead of getting stuck.
     *
     * @param tid          the target TuCSoN tuplecentre id
     *                     {@link alice.tucson.api.TucsonTupleCentreId tid}
     * @param event        the template for the TuCSoN primitive to react to
     * @param guards       the template for the guard predicates to be checked for
     *                     satisfaction so to actually trigger the body of the ReSpecT
     *                     reaction
     * @param reactionBody the template for the computation to be done in response to the
     *                     <code>event</code>
     * @param l            the listener who should be notified upon operation completion
     * @return the interface to access the data about TuCSoN operations outcome.
     * @throws TucsonOperationNotPossibleException if the requested operation cannot be carried out
     * @throws UnreachableNodeException            if the target tuple centre is not reachable over the network
     * @see alice.tuplecentre.api.TupleCentreId TupleCentreId
     * @see alice.tucson.api.TucsonOperationCompletionListener
     * TucsonOperationCompletionListener
     * @see alice.tucson.api.ITucsonOperation ITucsonOperation
     */
    ITucsonOperation rdS(final TupleCentreId tid, final LogicTuple event,
                         final LogicTuple guards, final LogicTuple reactionBody,
                         final TucsonOperationCompletionListener l)
            throws TucsonOperationNotPossibleException, UnreachableNodeException;

    /**
     * <code>set_s</code> specification primitive, to replace all the ReSpecT
     * specification tuples in the given target tuplecentre specification space
     * with that specified in the given tuple. The ReSpecT specification tuple
     * should be formatted as a Prolog list of the kind [(E1,G1,R1), ...,
     * (En,Gn,Rn)] where <code>E = events</code>, <code>G = guards</code>,
     * <code>R = reactionBody</code>.
     *
     * @param tid  the target TuCSoN tuplecentre id
     *             {@link alice.tucson.api.TucsonTupleCentreId tid}
     * @param spec the new ReSpecT specification to replace the current
     *             specification space
     * @param l    the listener who should be notified upon operation completion
     * @return the interface to access the data about TuCSoN operations outcome.
     * @throws TucsonOperationNotPossibleException if the requested operation cannot be carried out
     * @throws UnreachableNodeException            if the target tuple centre is not reachable over the network
     * @see alice.tuplecentre.api.TupleCentreId TupleCentreId
     * @see alice.tucson.api.ITucsonOperation ITucsonOperation
     */
    ITucsonOperation setS(final TupleCentreId tid, final LogicTuple spec,
                          final TucsonOperationCompletionListener l)
            throws TucsonOperationNotPossibleException, UnreachableNodeException;

    /**
     * <code>set_s</code> specification primitive, to replace all the ReSpecT
     * specification tuples in the given target tuplecentre specification space
     * with that specified in the given String. The ReSpecT specification string
     * should be formatted according to Prolog theory syntax.
     *
     * @param tid  the target TuCSoN tuplecentre id
     *             {@link alice.tucson.api.TucsonTupleCentreId tid}
     * @param spec the new ReSpecT specification to replace the current
     *             specification space
     * @param l    the listener who should be notified upon operation completion
     * @return the interface to access the data about TuCSoN operations outcome.
     * @throws TucsonOperationNotPossibleException if the requested operation cannot be carried out
     * @throws UnreachableNodeException            if the target tuple centre is not reachable over the network
     * @see alice.tuplecentre.api.TupleCentreId TupleCentreId
     * @see alice.tucson.api.TucsonOperationCompletionListener
     * TucsonOperationCompletionListener
     * @see alice.tucson.api.ITucsonOperation ITucsonOperation
     * @see alice.tuprolog.Theory Theory
     */
    ITucsonOperation setS(final TupleCentreId tid, final String spec,
                          final TucsonOperationCompletionListener l)
            throws TucsonOperationNotPossibleException, UnreachableNodeException;
}
