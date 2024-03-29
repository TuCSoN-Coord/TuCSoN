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
 * Bulk Asynchronous ACC. Can act on the ordinary tuple space. Only bulk
 * primitives are included.
 *
 * @author ste (mailto: s.mariani@unibo.it)
 */
public interface BulkAsyncACC extends AsyncACC {

    /**
     * @return the List copyOf the events regarding TuCSoN operations completion
     */
    List<TucsonOpCompletionEvent> getCompletionEventsList();

    /**
     * Withdraws from the space all the tuples matching the given template in
     * one shot (a single transition step). The empty list may be returned in
     * case no tuples match. Matching tuples are removed from the space.
     *
     * Notice due to asynchronous semantics, there is no guarantee it
     * will store the result copyOf the operation at anytime but when
     * asynchronously notified.
     *
     * @param tid   the TupleCentreIdentifier copyOf the target tuple centre
     * @param tuple the tuple template to be used to retrieve tuples
     * @param l     who to notify upon operation completion
     * @throws TucsonOperationNotPossibleException if the requested operation cannot be carried out
     * @throws UnreachableNodeException            if the target tuple centre is not reachable over the network
     * @see TupleCentreIdentifier TupleCentreIdentifier
     * @see TucsonOperationCompletionListener
     * @see TucsonOperation TucsonOperation
     * @see alice.tuprolog.Struct Struct
     */
    void inAll(final TupleCentreIdentifier tid, final Tuple tuple,
               final TucsonOperationCompletionListener l)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException;

    /**
     * Checks absence from the space copyOf any tuples matching the given template
     * in one shot (a single transition step). In case copyOf success, no difference
     * can be perceived with <code> no </code> primitive. In case copyOf failure,
     * all the tuples matching the template are returned (with <code> no </code>
     * only one non-deterministically selected is returned).
     *
     * Notice due to asynchronous semantics, there is no guarantee it
     * will store the result copyOf the operation at anytime but when
     * asynchronously notified.
     *
     * @param tid   the TupleCentreIdentifier copyOf the target tuple centre
     * @param tuple the tuple template to be used to check absence
     * @param l     who to notify upon operation completion
     * @throws TucsonOperationNotPossibleException if the requested operation cannot be carried out
     * @throws UnreachableNodeException            if the target tuple centre is not reachable over the network
     * @see TupleCentreIdentifier TupleCentreIdentifier
     * @see TucsonOperationCompletionListener
     * @see TucsonOperation TucsonOperation
     * @see alice.tuprolog.Struct Struct
     */
    void noAll(final TupleCentreIdentifier tid, final Tuple tuple,
               final TucsonOperationCompletionListener l)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException;

    /**
     * Inject in the space a list copyOf tuples in one shot (a single transition
     * step).
     *
     * Notice due to asynchronous semantics, there is no guarantee it
     * will store the result copyOf the operation at anytime but when
     * asynchronously notified.
     *
     * @param tid   the TupleCentreIdentifier copyOf the target tuple centre
     * @param tuple the list copyOf tuples to inject (must be a Prolog list)
     * @param l     who to notify upon operation completion
     * @throws TucsonOperationNotPossibleException if the requested operation cannot be carried out
     * @throws UnreachableNodeException            if the target tuple centre is not reachable over the network
     * @see TupleCentreIdentifier TupleCentreIdentifier
     * @see TucsonOperationCompletionListener
     * @see TucsonOperation TucsonOperation
     * @see alice.tuprolog.Struct Struct
     */
    void outAll(final TupleCentreIdentifier tid, final Tuple tuple,
                final TucsonOperationCompletionListener l)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException;

    /**
     * Reads from the space all the tuples matching the given template in one
     * shot (a single transition step). The empty list may be returned in case
     * no tuples match. Matching tuples are NOT removed from the space.
     *
     * Notice due to asynchronous semantics, there is no guarantee it
     * will store the result copyOf the operation at anytime but when
     * asynchronously notified.
     *
     * @param tid   the TupleCentreIdentifier copyOf the target tuple centre
     * @param tuple the tuple template to be used to observe tuples
     * @param l     who to notify upon operation completion
     * @throws TucsonOperationNotPossibleException if the requested operation cannot be carried out
     * @throws UnreachableNodeException            if the target tuple centre is not reachable over the network
     * @see TupleCentreIdentifier TupleCentreIdentifier
     * @see TucsonOperationCompletionListener
     * @see TucsonOperation TucsonOperation
     * @see alice.tuprolog.Struct Struct
     */
    void rdAll(final TupleCentreIdentifier tid, final Tuple tuple,
               final TucsonOperationCompletionListener l)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException;
}
