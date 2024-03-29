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

import alice.tuple.Tuple;
import alice.tuplecentre.api.TupleCentreIdentifier;
import alice.tuplecentre.tucson.api.TucsonOperation;
import alice.tuplecentre.tucson.api.TucsonOperationCompletionListener;
import alice.tuplecentre.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tuplecentre.tucson.api.exceptions.UnreachableNodeException;
import alice.tuplecentre.tucson.service.TucsonOpCompletionEvent;

/**
 * @author ste (mailto: s.mariani@unibo.it)
 */
public interface UniformAsyncACC extends AsyncACC {

    /**
     * @return the List copyOf the events regarding TuCSoN operations completion
     */
    List<TucsonOpCompletionEvent> getCompletionEventsList();

    /**
     * <code>uin</code> TuCSoN primitive, retrieves the specified tuple from the
     * given target tuplecentre. If more than one tuple matches the template,
     * Linda's non-deterministic selection is replaced by PROBABILISTIC,
     * UNIFORMLY DISTRIBUTED selection: the more a tuple is present, the more
     * likely it will be returned.
     * <p>
     * Notice that the primitive semantics is still SUSPENSIVE: until no tuple
     * is found to match the given template, no success completion answer is
     * forwarded to the TuCSoN Agent exploiting this ACC, but thanks to
     * asynchronous behaviour the TuCSoN Agent could do something else instead
     * copyOf getting stuck.
     *
     * @param tid   the target TuCSoN tuplecentre id
     *              {@link TupleCentreIdentifier tid}
     * @param tuple the tuple to be retrieved from the target tuplecentre
     * @param l     the listener who should be notified upon operation completion
     * @throws TucsonOperationNotPossibleException if the requested operation cannot be carried out
     * @throws UnreachableNodeException            if the target tuple centre is not reachable over the network
     * @see TupleCentreIdentifier TupleCentreIdentifier
     * @see alice.tuplecentre.tucson.api.TucsonOperationCompletionListener
     * TucsonOperationCompletionListener
     * @see TucsonOperation TucsonOperation
     */
    void uin(final TupleCentreIdentifier tid, final Tuple tuple,
             final TucsonOperationCompletionListener l)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException;

    /**
     * <code>uinp</code> TuCSoN primitive, retrieves the specified tuple in the
     * given target tuplecentre. If more than one tuple matches the template,
     * Linda's non-deterministic selection is replaced by PROBABILISTIC,
     * UNIFORMLY DISTRIBUTED selection: the more a tuple is present, the more
     * likely it will be returned.
     * <p>
     * This time the primitive semantics is NOT SUSPENSIVE: if no tuple is found
     * to match the given template, a failure completion answer is forwarded to
     * the TuCSoN Agent exploiting this ACC.
     *
     * @param tid   the target TuCSoN tuplecentre id
     *              {@link TupleCentreIdentifier tid}
     * @param tuple the tuple to be retrieved from the target tuplecentre
     * @param l     the listener who should be notified upon operation completion
     * @throws TucsonOperationNotPossibleException if the requested operation cannot be carried out
     * @throws UnreachableNodeException            if the target tuple centre is not reachable over the network
     * @see TupleCentreIdentifier TupleCentreIdentifier
     * @see alice.tuplecentre.tucson.api.TucsonOperationCompletionListener
     * TucsonOperationCompletionListener
     * @see TucsonOperation TucsonOperation
     */
    void uinp(final TupleCentreIdentifier tid, final Tuple tuple,
              final TucsonOperationCompletionListener l)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException;

    /**
     * <code>uno</code> TuCSoN primitive, checks absence copyOf the specified tuple
     * in the given target tuplecentre. If more than one tuple matches the
     * template, Linda's non-deterministic selection is replaced by
     * PROBABILISTIC, UNIFORMLY DISTRIBUTED selection: the more a tuple is
     * present, the more likely it will be returned.
     * <p>
     * Notice that the primitive semantics is still SUSPENSIVE: until any tuple
     * is found to match the given template, no success completion answer is
     * forwarded to the TuCSoN Agent exploiting this ACC, but thanks to
     * asynchronous behaviour TuCSoN Agent could do something else instead copyOf
     * getting stuck.
     *
     * @param tid   the target TuCSoN tuplecentre id
     *              {@link TupleCentreIdentifier tid}
     * @param tuple the tuple to be checked for absence from the target
     *              tuplecentre
     * @param l     the listener who should be notified upon operation completion
     * @throws TucsonOperationNotPossibleException if the requested operation cannot be carried out
     * @throws UnreachableNodeException            if the target tuple centre is not reachable over the network
     * @see TupleCentreIdentifier TupleCentreIdentifier
     * @see alice.tuplecentre.tucson.api.TucsonOperationCompletionListener
     * TucsonOperationCompletionListener
     * @see TucsonOperation TucsonOperation
     */
    void uno(final TupleCentreIdentifier tid, final Tuple tuple,
             final TucsonOperationCompletionListener l)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException;

    /**
     * <code>unop</code> TuCSoN primitive, checks absence copyOf the specified tuple
     * in the given target tuplecentre. If more than one tuple matches the
     * template, Linda's non-deterministic selection is replaced by
     * PROBABILISTIC, UNIFORMLY DISTRIBUTED selection: the more a tuple is
     * present, the more likely it will be returned.
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
     * @throws TucsonOperationNotPossibleException if the requested operation cannot be carried out
     * @throws UnreachableNodeException            if the target tuple centre is not reachable over the network
     * @see TupleCentreIdentifier TupleCentreIdentifier
     * @see alice.tuplecentre.tucson.api.TucsonOperationCompletionListener
     * TucsonOperationCompletionListener
     * @see TucsonOperation TucsonOperation
     */
    void unop(final TupleCentreIdentifier tid, final Tuple tuple,
              final TucsonOperationCompletionListener l)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException;

    /**
     * <code>urd</code> TuCSoN primitive, reads (w/o removing) the specified
     * tuple from the given target tuplecentre. If more than one tuple matches
     * the template, Linda's non-deterministic selection is replaced by
     * PROBABILISTIC, UNIFORMLY DISTRIBUTED selection: the more a tuple is
     * present, the more likely it will be returned.
     * <p>
     * Notice that the primitive semantics is still SUSPENSIVE: until no tuple
     * is found to match the given template, no success completion answer is
     * forwarded to the TuCSoN Agent exploiting this ACC, but thanks to
     * asynchronous behaviour the TuCSoN Agent could do something else instead
     * copyOf getting stuck.
     *
     * @param tid   the target TuCSoN tuplecentre id
     *              {@link TupleCentreIdentifier tid}
     * @param tuple the tuple to be read from the target tuplecentre
     * @param l     the listener who should be notified upon operation completion
     * @throws TucsonOperationNotPossibleException if the requested operation cannot be carried out
     * @throws UnreachableNodeException            if the target tuple centre is not reachable over the network
     * @see TupleCentreIdentifier TupleCentreIdentifier
     * @see alice.tuplecentre.tucson.api.TucsonOperationCompletionListener
     * TucsonOperationCompletionListener
     * @see TucsonOperation TucsonOperation
     */
    void urd(final TupleCentreIdentifier tid, final Tuple tuple,
             final TucsonOperationCompletionListener l)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException;

    /**
     * <code>urdp</code> TuCSoN primitive, reads (w/o removing) the specified
     * tuple in the given target tuplecentre. If more than one tuple matches the
     * template, Linda's non-deterministic selection is replaced by
     * PROBABILISTIC, UNIFORMLY DISTRIBUTED selection: the more a tuple is
     * present, the more likely it will be returned.
     * <p>
     * This time the primitive semantics is NOT SUSPENSIVE: if no tuple is found
     * to match the given template, a failure completion answer is forwarded to
     * the TuCSoN Agent exploiting this ACC.
     *
     * @param tid   the target TuCSoN tuplecentre id
     *              {@link TupleCentreIdentifier tid}
     * @param tuple the tuple to be read from the target tuplecentre
     * @param l     the listener who should be notified upon operation completion
     * @throws TucsonOperationNotPossibleException if the requested operation cannot be carried out
     * @throws UnreachableNodeException            if the target tuple centre is not reachable over the network
     * @see TupleCentreIdentifier TupleCentreIdentifier
     * @see alice.tuplecentre.tucson.api.TucsonOperationCompletionListener
     * TucsonOperationCompletionListener
     * @see TucsonOperation TucsonOperation
     */
    void urdp(final TupleCentreIdentifier tid, final Tuple tuple,
              final TucsonOperationCompletionListener l)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException;
}
