/*
 * TuCSoN coordination infrastructure - Copyright (C) 2001-2002  aliCE team at deis.unibo.it
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package alice.tucson.api;

import alice.logictuple.*;

/**
 *
 * Interface of any TuCSoN agent coordination context
 *
 * An agent coordination  context defines (and enables) the actions that an agent
 * can do on by means of a coordination medium.
 *
 */
public interface TucsonContext {

    /**
     * Inserts a tuple in a tuple centre
     *
     * @param tid is the identifier of the target tuple centre
     * @param t is the logic tuple to be inserted
     * @throws s OperationNotAllowedException of the operation is not allowed
     * @throws s UnreachableNodeException if the node specified in the tuple centre
     *         identifier is not reachable
     */
	TucsonOpId out(TupleCentreId tid, LogicTuple t)
            throws  OperationNotAllowedException,
                    UnreachableNodeException;

    /**
     * Gets a tuple from a tuple centre
     *
     * This coordination primitive is blocking
     *
     * @param tid is the identifier of the target tuple centre
     * @param t is the logic tuple specifying the template used for the matching
     * @return the logic tuple retrieved from the tuple centre
     * @throws s OperationNotAllowedException of the operation is not allowed
     * @throws s UnreachableNodeException if the node specified in the tuple centre
     *         identifier is not reachable
     */
    TucsonOpId in(TupleCentreId tid, LogicTuple t)
            throws  OperationNotAllowedException,
                    UnreachableNodeException;

    /**
     * Reads a tuple from a tuple centre
     *
     * This coordination primitive is blocking
     *
     * @param tid is the identifier of the target tuple centre
     * @param t is the logic tuple specifying the template used for the matching
     * @return the logic tuple matching the request
     * @throws s OperationNotAllowedException of the operation is not allowed
     * @throws s UnreachableNodeException if the node specified in the tuple centre
     *         identifier is not reachable
     */
    TucsonOpId rd(TupleCentreId tid, LogicTuple t)
            throws  OperationNotAllowedException,
                    UnreachableNodeException;

    /**
     * Gets a tuple from a tuple centre, without blocking
     *
     * This coordination primitive is non-blocking: if no
     * tuple is found the primitive returns a null value.
     *
     * @param tid is the identifier of the target tuple centre
     * @param t is the logic tuple specifying the template used for the matching
     * @return the logic tuple retrieved from the tuple centre or null if no tuple is found
     * @throws s OperationNotAllowedException of the operation is not allowed
     * @throws s UnreachableNodeException if the node specified in the tuple centre
     *         identifier is not reachable
     */
    TucsonOpId inp(TupleCentreId tid, LogicTuple t)
            throws  OperationNotAllowedException,
                    UnreachableNodeException;

    /**
     * Reads a tuple from a tuple centre, without blocking
     *
     * This coordination primitive is non-blocking: if no
     * tuple is found the primitive returns a null value.
     *
     * @param tid is the identifier of the target tuple centre
     * @param t is the logic tuple specifying the template used for the matching
     * @return the logic tuple retrieved from the tuple centre or null if no tuple is found
     * @throws s OperationNotAllowedException of the operation is not allowed
     * @throws s UnreachableNodeException if the node specified in the tuple centre
     *         identifier is not reachable
     */
    TucsonOpId rdp(TupleCentreId tid, LogicTuple t)
            throws  OperationNotAllowedException,
                    UnreachableNodeException;


    /**
     * Sets the behaviour of a tuple centre
     *
     * @param tid is the identifier of the target tuple centre
     * @param spec is behaviour specification in the ReSpecT language
     * @throws s OperationNotAllowedException of the operation is not allowed
     * @throws s UnreachableNodeException if the node specified in the tuple centre
     *         identifier is not reachable
     * @throws s InvalidSpecificationException if the specification is not valid
     */
    TucsonOpId setSpec(TupleCentreId tid, String spec)
            throws  OperationNotAllowedException,
                    UnreachableNodeException,
                    InvalidSpecificationException;

    /**
     * Gets the behaviour specification of a tuple centre
     *
     * @param tid is the identifier of the target tuple centre
     * @return the behavious specification in the ReSpecT language
     * @throws s OperationNotAllowedException of the operation is not allowed
     * @throws s UnreachableNodeException if the node specified in the tuple centre
     *         identifier is not reachable
     */
    TucsonOpId getSpec(TupleCentreId tid)
        throws  OperationNotAllowedException,
                UnreachableNodeException;

    ContextEvent waitForCompletion(TucsonOpId id, int timeout);

    ContextEvent waitForCompletion(TucsonOpId id);
    
    /**
     * Exits from the coordination context
     *
     * @throws s OperationNotAllowedException of the operation is not allowed
     */
    void exit()
            throws  OperationNotAllowedException;
}
