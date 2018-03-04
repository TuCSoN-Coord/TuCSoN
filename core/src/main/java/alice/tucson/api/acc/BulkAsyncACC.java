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

import alice.tucson.api.TucsonOperation;
import alice.tucson.api.TucsonOperationCompletionListener;
import alice.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tucson.api.exceptions.UnreachableNodeException;
import alice.tucson.service.TucsonOpCompletionEvent;
import alice.tuplecentre.api.Tuple;
import alice.tuplecentre.api.TupleCentreIdentifier;

/**
 * Bulk Asynchronous ACC. Can act on the ordinary tuple space. Only bulk
 * primitives are included.
 *
 * @author ste (mailto: s.mariani@unibo.it)
 */
public interface BulkAsyncACC extends AsyncACC {

    /**
     * @return the List of the events regarding TuCSoN operations completion
     */
    List<TucsonOpCompletionEvent> getCompletionEventsList();

    /**
     * Withdraws from the space all the tuples matching the given template in
     * one shot (a single transition step). The empty list may be returned in
     * case no tuples match. Matching tuples are removed from the space.
     *
     * @param tid   the TupleCentreIdentifier of the target tuple centre
     * @param tuple the tuple template to be used to retrieve tuples
     * @param l     who to notify upon operation completion
     * @return the TucsonOperation object storing the outcome of the execution.
     * Notice due to asynchronous semantics, there is no guarantee it
     * will store the result of the operation at anytime but when
     * asynchronously notified.
     * @throws TucsonOperationNotPossibleException if the requested operation cannot be carried out
     * @throws UnreachableNodeException            if the target tuple centre is not reachable over the network
     * @see TupleCentreIdentifier TupleCentreIdentifier
     * @see alice.tucson.api.TucsonOperationCompletionListener
     * @see TucsonOperation TucsonOperation
     * @see alice.tuprolog.Struct Struct
     */
    TucsonOperation inAll(final TupleCentreIdentifier tid, final Tuple tuple,
                          final TucsonOperationCompletionListener l)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException;

    /**
     * Checks absence from the space of any tuples matching the given template
     * in one shot (a single transition step). In case of success, no difference
     * can be perceived with <code> no </code> primitive. In case of failure,
     * all the tuples matching the template are returned (with <code> no </code>
     * only one non-deterministically selected is returned).
     *
     * @param tid   the TupleCentreIdentifier of the target tuple centre
     * @param tuple the tuple template to be used to check absence
     * @param l     who to notify upon operation completion
     * @return the TucsonOperation object storing the outcome of the execution.
     * Notice due to asynchronous semantics, there is no guarantee it
     * will store the result of the operation at anytime but when
     * asynchronously notified.
     * @throws TucsonOperationNotPossibleException if the requested operation cannot be carried out
     * @throws UnreachableNodeException            if the target tuple centre is not reachable over the network
     * @see TupleCentreIdentifier TupleCentreIdentifier
     * @see alice.tucson.api.TucsonOperationCompletionListener
     * @see TucsonOperation TucsonOperation
     * @see alice.tuprolog.Struct Struct
     */
    TucsonOperation noAll(final TupleCentreIdentifier tid, final Tuple tuple,
                          final TucsonOperationCompletionListener l)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException;

    /**
     * Inject in the space a list of tuples in one shot (a single transition
     * step).
     *
     * @param tid   the TupleCentreIdentifier of the target tuple centre
     * @param tuple the list of tuples to inject (must be a Prolog list)
     * @param l     who to notify upon operation completion
     * @return the TucsonOperation object storing the outcome of the execution.
     * Notice due to asynchronous semantics, there is no guarantee it
     * will store the result of the operation at anytime but when
     * asynchronously notified.
     * @throws TucsonOperationNotPossibleException if the requested operation cannot be carried out
     * @throws UnreachableNodeException            if the target tuple centre is not reachable over the network
     * @see TupleCentreIdentifier TupleCentreIdentifier
     * @see alice.tucson.api.TucsonOperationCompletionListener
     * @see TucsonOperation TucsonOperation
     * @see alice.tuprolog.Struct Struct
     */
    TucsonOperation outAll(final TupleCentreIdentifier tid, final Tuple tuple,
                           final TucsonOperationCompletionListener l)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException;

    /**
     * Reads from the space all the tuples matching the given template in one
     * shot (a single transition step). The empty list may be returned in case
     * no tuples match. Matching tuples are NOT removed from the space.
     *
     * @param tid   the TupleCentreIdentifier of the target tuple centre
     * @param tuple the tuple template to be used to observe tuples
     * @param l     who to notify upon operation completion
     * @return the TucsonOperation object storing the outcome of the execution.
     * Notice due to asynchronous semantics, there is no guarantee it
     * will store the result of the operation at anytime but when
     * asynchronously notified.
     * @throws TucsonOperationNotPossibleException if the requested operation cannot be carried out
     * @throws UnreachableNodeException            if the target tuple centre is not reachable over the network
     * @see TupleCentreIdentifier TupleCentreIdentifier
     * @see alice.tucson.api.TucsonOperationCompletionListener
     * @see TucsonOperation TucsonOperation
     * @see alice.tuprolog.Struct Struct
     */
    TucsonOperation rdAll(final TupleCentreIdentifier tid, final Tuple tuple,
                          final TucsonOperationCompletionListener l)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException;
}
