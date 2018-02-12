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
package alice.tucson.api;

import java.util.List;
import alice.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tucson.api.exceptions.UnreachableNodeException;
import alice.tucson.service.TucsonOpCompletionEvent;
import alice.tuplecentre.api.Tuple;
import alice.tuplecentre.api.TupleCentreId;

/**
 *
 * @author ste (mailto: s.mariani@unibo.it)
 *
 */
public interface UniformAsynchACC extends RootACC {

    /**
     *
     * @return the List of the events regarding TuCSoN operations completion
     */
    List<TucsonOpCompletionEvent> getCompletionEventsList();

    /**
     * <code>uin</code> TuCSoN primitive, retrieves the specified tuple from the
     * given target tuplecentre. If more than one tuple matches the template,
     * Linda's non-deterministic selection is replaced by PROBABILISTIC,
     * UNIFORMLY DISTRIBUTED selection: the more a tuple is present, the more
     * likely it will be returned.
     *
     * Notice that the primitive semantics is still SUSPENSIVE: until no tuple
     * is found to match the given template, no success completion answer is
     * forwarded to the TuCSoN Agent exploiting this ACC, but thanks to
     * asynchronous behaviour the TuCSoN Agent could do something else instead
     * of getting stuck.
     *
     * @param tid
     *            the target TuCSoN tuplecentre id
     *            {@link alice.tucson.api.TucsonTupleCentreId tid}
     * @param tuple
     *            the tuple to be retrieved from the target tuplecentre
     * @param l
     *            the listener who should be notified upon operation completion
     *
     * @return the interface to access the data about TuCSoN operations outcome.
     *
     * @throws TucsonOperationNotPossibleException
     *             if the requested operation cannot be carried out
     * @throws UnreachableNodeException
     *             if the target tuple centre is not reachable over the network
     *
     * @see alice.tuplecentre.api.TupleCentreId TupleCentreId
     * @see alice.tucson.api.TucsonOperationCompletionListener
     *      TucsonOperationCompletionListener
     * @see alice.tucson.api.ITucsonOperation ITucsonOperation
     */
    ITucsonOperation uin(TupleCentreId tid, Tuple tuple,
            TucsonOperationCompletionListener l)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException;

    /**
     * <code>uinp</code> TuCSoN primitive, retrieves the specified tuple in the
     * given target tuplecentre. If more than one tuple matches the template,
     * Linda's non-deterministic selection is replaced by PROBABILISTIC,
     * UNIFORMLY DISTRIBUTED selection: the more a tuple is present, the more
     * likely it will be returned.
     *
     * This time the primitive semantics is NOT SUSPENSIVE: if no tuple is found
     * to match the given template, a failure completion answer is forwarded to
     * the TuCSoN Agent exploiting this ACC.
     *
     * @param tid
     *            the target TuCSoN tuplecentre id
     *            {@link alice.tucson.api.TucsonTupleCentreId tid}
     * @param tuple
     *            the tuple to be retrieved from the target tuplecentre
     * @param l
     *            the listener who should be notified upon operation completion
     *
     * @return the interface to access the data about TuCSoN operations outcome.
     *
     * @throws TucsonOperationNotPossibleException
     *             if the requested operation cannot be carried out
     * @throws UnreachableNodeException
     *             if the target tuple centre is not reachable over the network
     *
     * @see alice.tuplecentre.api.TupleCentreId TupleCentreId
     * @see alice.tucson.api.TucsonOperationCompletionListener
     *      TucsonOperationCompletionListener
     * @see alice.tucson.api.ITucsonOperation ITucsonOperation
     */
    ITucsonOperation uinp(TupleCentreId tid, Tuple tuple,
            TucsonOperationCompletionListener l)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException;

    /**
     * <code>uno</code> TuCSoN primitive, checks absence of the specified tuple
     * in the given target tuplecentre. If more than one tuple matches the
     * template, Linda's non-deterministic selection is replaced by
     * PROBABILISTIC, UNIFORMLY DISTRIBUTED selection: the more a tuple is
     * present, the more likely it will be returned.
     *
     * Notice that the primitive semantics is still SUSPENSIVE: until any tuple
     * is found to match the given template, no success completion answer is
     * forwarded to the TuCSoN Agent exploiting this ACC, but thanks to
     * asynchronous behaviour TuCSoN Agent could do something else instead of
     * getting stuck.
     *
     * @param tid
     *            the target TuCSoN tuplecentre id
     *            {@link alice.tucson.api.TucsonTupleCentreId tid}
     * @param tuple
     *            the tuple to be checked for absence from the target
     *            tuplecentre
     * @param l
     *            the listener who should be notified upon operation completion
     *
     * @return the interface to access the data about TuCSoN operations outcome.
     *
     * @throws TucsonOperationNotPossibleException
     *             if the requested operation cannot be carried out
     * @throws UnreachableNodeException
     *             if the target tuple centre is not reachable over the network
     *
     * @see alice.tuplecentre.api.TupleCentreId TupleCentreId
     * @see alice.tucson.api.TucsonOperationCompletionListener
     *      TucsonOperationCompletionListener
     * @see alice.tucson.api.ITucsonOperation ITucsonOperation
     */
    ITucsonOperation uno(TupleCentreId tid, Tuple tuple,
            TucsonOperationCompletionListener l)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException;

    /**
     * <code>unop</code> TuCSoN primitive, checks absence of the specified tuple
     * in the given target tuplecentre. If more than one tuple matches the
     * template, Linda's non-deterministic selection is replaced by
     * PROBABILISTIC, UNIFORMLY DISTRIBUTED selection: the more a tuple is
     * present, the more likely it will be returned.
     *
     * This time the primitive semantics is NOT SUSPENSIVE: if a tuple is found
     * to match the given template, a failure completion answer is forwarded to
     * the TuCSoN Agent exploiting this ACC.
     *
     * @param tid
     *            the target TuCSoN tuplecentre id
     *            {@link alice.tucson.api.TucsonTupleCentreId tid}
     * @param tuple
     *            the tuple to be checked for absence from the target
     *            tuplecentre
     * @param l
     *            the listener who should be notified upon operation completion
     *
     * @return the interface to access the data about TuCSoN operations outcome.
     *
     * @throws TucsonOperationNotPossibleException
     *             if the requested operation cannot be carried out
     * @throws UnreachableNodeException
     *             if the target tuple centre is not reachable over the network
     *
     * @see alice.tuplecentre.api.TupleCentreId TupleCentreId
     * @see alice.tucson.api.TucsonOperationCompletionListener
     *      TucsonOperationCompletionListener
     * @see alice.tucson.api.ITucsonOperation ITucsonOperation
     */
    ITucsonOperation unop(TupleCentreId tid, Tuple tuple,
            TucsonOperationCompletionListener l)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException;

    /**
     * <code>urd</code> TuCSoN primitive, reads (w/o removing) the specified
     * tuple from the given target tuplecentre. If more than one tuple matches
     * the template, Linda's non-deterministic selection is replaced by
     * PROBABILISTIC, UNIFORMLY DISTRIBUTED selection: the more a tuple is
     * present, the more likely it will be returned.
     *
     * Notice that the primitive semantics is still SUSPENSIVE: until no tuple
     * is found to match the given template, no success completion answer is
     * forwarded to the TuCSoN Agent exploiting this ACC, but thanks to
     * asynchronous behaviour the TuCSoN Agent could do something else instead
     * of getting stuck.
     *
     * @param tid
     *            the target TuCSoN tuplecentre id
     *            {@link alice.tucson.api.TucsonTupleCentreId tid}
     * @param tuple
     *            the tuple to be read from the target tuplecentre
     * @param l
     *            the listener who should be notified upon operation completion
     *
     * @return the interface to access the data about TuCSoN operations outcome.
     *
     * @throws TucsonOperationNotPossibleException
     *             if the requested operation cannot be carried out
     * @throws UnreachableNodeException
     *             if the target tuple centre is not reachable over the network
     *
     * @see alice.tuplecentre.api.TupleCentreId TupleCentreId
     * @see alice.tucson.api.TucsonOperationCompletionListener
     *      TucsonOperationCompletionListener
     * @see alice.tucson.api.ITucsonOperation ITucsonOperation
     */
    ITucsonOperation urd(TupleCentreId tid, Tuple tuple,
            TucsonOperationCompletionListener l)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException;

    /**
     * <code>urdp</code> TuCSoN primitive, reads (w/o removing) the specified
     * tuple in the given target tuplecentre. If more than one tuple matches the
     * template, Linda's non-deterministic selection is replaced by
     * PROBABILISTIC, UNIFORMLY DISTRIBUTED selection: the more a tuple is
     * present, the more likely it will be returned.
     *
     * This time the primitive semantics is NOT SUSPENSIVE: if no tuple is found
     * to match the given template, a failure completion answer is forwarded to
     * the TuCSoN Agent exploiting this ACC.
     *
     * @param tid
     *            the target TuCSoN tuplecentre id
     *            {@link alice.tucson.api.TucsonTupleCentreId tid}
     * @param tuple
     *            the tuple to be read from the target tuplecentre
     * @param l
     *            the listener who should be notified upon operation completion
     *
     * @return the interface to access the data about TuCSoN operations outcome.
     *
     * @throws TucsonOperationNotPossibleException
     *             if the requested operation cannot be carried out
     * @throws UnreachableNodeException
     *             if the target tuple centre is not reachable over the network
     *
     * @see alice.tuplecentre.api.TupleCentreId TupleCentreId
     * @see alice.tucson.api.TucsonOperationCompletionListener
     *      TucsonOperationCompletionListener
     * @see alice.tucson.api.ITucsonOperation ITucsonOperation
     */
    ITucsonOperation urdp(TupleCentreId tid, Tuple tuple,
            TucsonOperationCompletionListener l)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException;
}
