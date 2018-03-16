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

import java.util.List;

import alice.tuple.Tuple;
import alice.tuplecentre.api.TupleCentreIdentifier;
import alice.tuplecentre.tucson.api.TucsonOperation;
import alice.tuplecentre.tucson.api.TucsonOperationCompletionListener;
import alice.tuplecentre.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tuplecentre.tucson.api.exceptions.UnreachableNodeException;
import alice.tuplecentre.tucson.service.TucsonOpCompletionEvent;

/**
 * Agent Coordination Context enabling interaction with the Ordinary Tuple Space
 * and enacting a NON-BLOCKING behavior from the agent's perspective. This means
 * that whichever is the Linda operation invoked (either suspensive or
 * predicative) the agent proxy will NOT block waiting for its completion but
 * will be asynchronously notified (by the node side).
 *
 * @author ste (mailto: s.mariani@unibo.it)
 */
public interface OrdinaryAsyncACC extends AsyncACC {
    /**
     * @return the List of the events regarding TuCSoN operations completion
     */
    List<TucsonOpCompletionEvent> getCompletionEventsList();


    /**
     * <code>get</code> TuCSoN primitive, reads (w/o removing) all the tuples in
     * the given target tuplecentre.
     * <p>
     * Semantics is NOT SUSPENSIVE: if the tuple space is empty, an empty list
     * is returned to the TuCSoN Agent exploiting this ACC.
     *
     * @param tid the target TuCSoN tuplecentre id
     *            {@link TupleCentreIdentifier tid}
     * @param l   the listener who should be notified upon operation completion
     * @return the interface to access the data about TuCSoN operations outcome.
     * @throws TucsonOperationNotPossibleException if the requested operation cannot be carried out
     * @throws UnreachableNodeException            if the target tuple centre is not reachable over the network
     * @see TupleCentreIdentifier TupleCentreIdentifier
     * @see alice.tuplecentre.tucson.api.TucsonOperationCompletionListener
     * @see TucsonOperation TucsonOperation
     */
    void get(final TupleCentreIdentifier tid, final TucsonOperationCompletionListener l)
            throws TucsonOperationNotPossibleException, UnreachableNodeException;

    /**
     * <code>in</code> Linda primitive, retrieves the specified tuple from the
     * given target tuplecentre.
     * <p>
     * Notice that the primitive semantics is still SUSPENSIVE: until no tuple
     * is found to match the given template, no success completion answer is
     * forwarded to the TuCSoN Agent exploiting this ACC, but thanks to
     * asynchronous behaviour the TuCSoN Agent could do something else instead
     * of getting stuck.
     *
     * @param tid   the target TuCSoN tuplecentre id
     *              {@link TupleCentreIdentifier tid}
     * @param tuple the tuple to be retrieved from the target tuplecentre
     * @param l     the listener who should be notified upon operation completion
     * @return the interface to access the data about TuCSoN operations outcome.
     * @throws TucsonOperationNotPossibleException if the requested operation cannot be carried out
     * @throws UnreachableNodeException            if the target tuple centre is not reachable over the network
     * @see TupleCentreIdentifier TupleCentreIdentifier
     * @see alice.tuplecentre.tucson.api.TucsonOperationCompletionListener
     * @see TucsonOperation TucsonOperation
     */
    TucsonOperation in(final TupleCentreIdentifier tid, final Tuple tuple, final TucsonOperationCompletionListener l)
            throws TucsonOperationNotPossibleException, UnreachableNodeException;

    /**
     * <code>inp</code> Linda primitive, retrieves the specified tuple in the
     * given target tuplecentre.
     * <p>
     * This time the primitive semantics is NOT SUSPENSIVE: if no tuple is found
     * to match the given template, a failure completion answer is forwarded to
     * the TuCSoN Agent exploiting this ACC.
     *
     * @param tid   the target TuCSoN tuplecentre id
     *              {@link TupleCentreIdentifier tid}
     * @param tuple the tuple to be retrieved from the target tuplecentre
     * @param l     the listener who should be notified upon operation completion
     * @return the interface to access the data about TuCSoN operations outcome.
     * @throws TucsonOperationNotPossibleException if the requested operation cannot be carried out
     * @throws UnreachableNodeException            if the target tuple centre is not reachable over the network
     * @see TupleCentreIdentifier TupleCentreIdentifier
     * @see alice.tuplecentre.tucson.api.TucsonOperationCompletionListener
     * @see TucsonOperation TucsonOperation
     */
    TucsonOperation inp(final TupleCentreIdentifier tid, final Tuple tuple, final TucsonOperationCompletionListener l)
            throws TucsonOperationNotPossibleException, UnreachableNodeException;

    /**
     * <code>no</code> TuCSoN primitive, checks absence of the specified tuple
     * in the given target tuplecentre.
     * <p>
     * Notice that the primitive semantics is still SUSPENSIVE: until any tuple
     * is found to match the given template, no success completion answer is
     * forwarded to the TuCSoN Agent exploiting this ACC, but thanks to
     * asynchronous behaviour TuCSoN Agent could do something else instead of
     * getting stuck.
     *
     * @param tid   the target TuCSoN tuplecentre id
     *              {@link TupleCentreIdentifier tid}
     * @param tuple the tuple to be checked for absence from the target
     *              tuplecentre
     * @param l     the listener who should be notified upon operation completion
     * @return the interface to access the data about TuCSoN operations outcome.
     * @throws TucsonOperationNotPossibleException if the requested operation cannot be carried out
     * @throws UnreachableNodeException            if the target tuple centre is not reachable over the network
     * @see TupleCentreIdentifier TupleCentreIdentifier
     * @see alice.tuplecentre.tucson.api.TucsonOperationCompletionListener
     * @see TucsonOperation TucsonOperation
     */
    void no(final TupleCentreIdentifier tid, final Tuple tuple, final TucsonOperationCompletionListener l)
            throws TucsonOperationNotPossibleException, UnreachableNodeException;

    /**
     * <code>nop</code> TuCSoN primitive, checks absence of the specified tuple
     * in the given target tuplecentre.
     * <p>
     * This time the primitive semantics is NOT SUSPENSIVE: if a tuple is found
     * to match the given template, a failure completion answer is forwarded to
     * the TuCSoN Agent exploiting this ACC.
     *
     * @param tid   the target TuCSoN tuplecentre id
     *              {@link TupleCentreIdentifier tid}
     * @param tuple the tuple to be checked for absence from the target
     *              tuplecentre
     * @param l     the listener who should be notified upon operation completion
     * @return the interface to access the data about TuCSoN operations outcome.
     * @throws TucsonOperationNotPossibleException if the requested operation cannot be carried out
     * @throws UnreachableNodeException            if the target tuple centre is not reachable over the network
     * @see TupleCentreIdentifier TupleCentreIdentifier
     * @see alice.tuplecentre.tucson.api.TucsonOperationCompletionListener
     * @see TucsonOperation TucsonOperation
     */
    void nop(final TupleCentreIdentifier tid, final Tuple tuple, final TucsonOperationCompletionListener l)
            throws TucsonOperationNotPossibleException, UnreachableNodeException;

    /**
     * <code>out</code> Linda primitive, inserts the specified tuple in the
     * given target tuplecentre.
     * <p>
     * Notice that TuCSoN out primitive assumes the ORDERED version of this
     * primitive, hence the tuple is SUDDENLY injected in the target space (if
     * the primitive successfully completes)
     *
     * @param tid   the target TuCSoN tuplecentre id
     *              {@link TupleCentreIdentifier tid}
     * @param tuple the tuple to be emitted in the target tuplecentre
     * @param l     the listener who should be notified upon operation completion
     * @return the interface to access the data about TuCSoN operations outcome.
     * @throws TucsonOperationNotPossibleException if the requested operation cannot be carried out
     * @throws UnreachableNodeException            if the target tuple centre is not reachable over the network
     * @see TupleCentreIdentifier TupleCentreIdentifier
     * @see alice.tuplecentre.tucson.api.TucsonOperationCompletionListener
     * @see TucsonOperation TucsonOperation
     */
    TucsonOperation out(final TupleCentreIdentifier tid, final Tuple tuple, final TucsonOperationCompletionListener l)
            throws TucsonOperationNotPossibleException, UnreachableNodeException;

    /**
     * <code>rd</code> Linda primitive, reads (w/o removing) the specified tuple
     * from the given target tuplecentre.
     * <p>
     * Notice that the primitive semantics is still SUSPENSIVE: until no tuple
     * is found to match the given template, no success completion answer is
     * forwarded to the TuCSoN Agent exploiting this ACC, but thanks to
     * asynchronous behaviour the TuCSoN Agent could do something else instead
     * of getting stuck.
     *
     * @param tid   the target TuCSoN tuplecentre id
     *              {@link TupleCentreIdentifier tid}
     * @param tuple the tuple to be read from the target tuplecentre
     * @param l     the listener who should be notified upon operation completion
     * @return the interface to access the data about TuCSoN operations outcome.
     * @throws TucsonOperationNotPossibleException if the requested operation cannot be carried out
     * @throws UnreachableNodeException            if the target tuple centre is not reachable over the network
     * @see TupleCentreIdentifier TupleCentreIdentifier
     * @see TucsonOperationCompletionListener
     * @see TucsonOperation TucsonOperation
     */
    TucsonOperation rd(final TupleCentreIdentifier tid, final Tuple tuple, final TucsonOperationCompletionListener l)
            throws TucsonOperationNotPossibleException, UnreachableNodeException;

    /**
     * <code>rdp</code> Linda primitive, reads (w/o removing) the specified
     * tuple in the given target tuplecentre.
     * <p>
     * This time the primitive semantics is NOT SUSPENSIVE: if no tuple is found
     * to match the given template, a failure completion answer is forwarded to
     * the TuCSoN Agent exploiting this ACC.
     *
     * @param tid   the target TuCSoN tuplecentre id
     *              {@link TupleCentreIdentifier tid}
     * @param tuple the tuple to be read from the target tuplecentre
     * @param l     the listener who should be notified upon operation completion
     * @return the interface to access the data about TuCSoN operations outcome.
     * @throws TucsonOperationNotPossibleException if the requested operation cannot be carried out
     * @throws UnreachableNodeException            if the target tuple centre is not reachable over the network
     * @see TupleCentreIdentifier TupleCentreIdentifier
     * @see alice.tuplecentre.tucson.api.TucsonOperationCompletionListener
     * @see TucsonOperation TucsonOperation
     */
    TucsonOperation rdp(final TupleCentreIdentifier tid, final Tuple tuple, final TucsonOperationCompletionListener l)
            throws TucsonOperationNotPossibleException, UnreachableNodeException;

    /**
     * <code>set</code> TuCSoN primitive, to replace all the tuples in the given
     * target tuplecentre with that specified in the given list.
     *
     * @param tid   the target TuCSoN tuplecentre id
     *              {@link TupleCentreIdentifier tid}
     * @param tuple the Prolog list of all the tuples to be injected (overwriting
     *              space)
     * @param l     the listener who should be notified upon operation completion
     * @return the interface to access the data about TuCSoN operations outcome.
     * @throws TucsonOperationNotPossibleException if the requested operation cannot be carried out
     * @throws UnreachableNodeException            if the target tuple centre is not reachable over the network
     * @see TupleCentreIdentifier TupleCentreIdentifier
     * @see alice.tuplecentre.tucson.api.TucsonOperationCompletionListener
     * @see TucsonOperation TucsonOperation
     */
    void set(final TupleCentreIdentifier tid, final Tuple tuple, final TucsonOperationCompletionListener l)
            throws TucsonOperationNotPossibleException, UnreachableNodeException;

    /**
     * <code>spawn</code> TuCSoN primitive, starts a parallel computational
     * activity within the target node.
     * <p>
     * Semantics is NOT SUSPENSIVE: as soon as the parallel activity has been
     * started by the node, the completion is returned to the TuCSoN Agent
     * exploiting this ACC.
     *
     * @param tid     the target TuCSoN tuplecentre id
     *                {@link TupleCentreIdentifier tid}
     * @param toSpawn the tuple storing the activity to spawn as a parallel
     *                computation. Must be a Prolog term with functor name
     *                <code>exec/solve</code>, storing either a Java qualified class
     *                name (dotted-list of packages and <code>.class</code>
     *                extension too) or the filepath to a valid Prolog theory and a
     *                valid Prolog goal to be checked. E.g.:
     *                <code>exec('list.of.packages.YourClass.class')</code> OR
     *                <code>solve('path/to/Prolog/Theory.pl', yourGoal)</code>
     * @param l       the listener who should be notified upon operation completion
     * @return the interface to access the data about TuCSoN operations outcome.
     * @throws TucsonOperationNotPossibleException if the requested operation cannot be carried out
     * @throws UnreachableNodeException            if the target tuple centre is not reachable over the network
     * @see TupleCentreIdentifier TupleCentreIdentifier
     * @see alice.tuplecentre.tucson.api.TucsonOperationCompletionListener
     * @see TucsonOperation TucsonOperation
     * @see alice.tuprolog.Theory Theory
     * @see alice.tuprolog.Term Term
     */
    void spawn(final TupleCentreIdentifier tid, final Tuple toSpawn, final TucsonOperationCompletionListener l)
            throws TucsonOperationNotPossibleException, UnreachableNodeException;
}
