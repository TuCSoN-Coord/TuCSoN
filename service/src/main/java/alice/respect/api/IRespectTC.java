/*
 * ReSpecT - Copyright (C) aliCE team at deis.unibo.it This library is free
 * software; you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package alice.respect.api;

import alice.logictuple.LogicTuple;
import alice.logictuple.exceptions.InvalidLogicTupleException;
import alice.respect.api.exceptions.InvalidSpecificationException;
import alice.respect.api.exceptions.OperationNotPossibleException;
import alice.respect.core.RespectVM;
import alice.tuplecentre.core.InputEvent;

/**
 * Basic usage interface of a RespecT Tuple Centre
 * 
 * @author Alessandro Ricci
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 * @author (contributor) Michele Bombardi (mailto:
 *         michele.bombardi@studio.unibo.it)
 */
public interface IRespectTC {
    /**
     * Gets the whole tuple set
     * 
     * @param ev
     *            the event to handle
     * @return the operation requested
     * @throws OperationNotPossibleException
     *             if the requested operation cannot be carried out
     */
    IRespectOperation get(InputEvent ev) throws OperationNotPossibleException;

    /**
     * Gets the tuple centre id
     * 
     * @return the tuple centre id
     */
    TupleCentreId getId();

    /**
     * 
     * @param ev
     *            the event to handle
     * @return the operation requested
     * @throws OperationNotPossibleException
     *             if the requested operation cannot be carried out
     */
    IRespectOperation getS(InputEvent ev) throws OperationNotPossibleException;
    
    /**
     * 
     * @return this ReSpecT VM
     */
    RespectVM getVM();

    /**
     * 
     * @param ev
     *            the event to handle
     * @return the operation requested
     * @throws OperationNotPossibleException
     *             if the requested operation cannot be carried out
     */
    IRespectOperation in(InputEvent ev) throws OperationNotPossibleException;

    /**
     * 
     * @param ev
     *            the event to handle
     * @return the operation requested
     * @throws OperationNotPossibleException
     *             if the requested operation cannot be carried out
     */
    IRespectOperation inAll(InputEvent ev) throws OperationNotPossibleException;

    /**
     * 
     * @param ev
     *            the event to handle
     * @return the operation requested
     * @throws OperationNotPossibleException
     *             if the requested operation cannot be carried out
     */
    IRespectOperation inp(InputEvent ev) throws OperationNotPossibleException;

    /**
     * 
     * @param ev
     *            the event to handle
     * @return the operation requested
     * @throws OperationNotPossibleException
     *             if the requested operation cannot be carried out
     */
    IRespectOperation inpS(InputEvent ev) throws OperationNotPossibleException;

    /**
     * 
     * @param ev
     *            the event to handle
     * @return the operation requested
     * @throws OperationNotPossibleException
     *             if the requested operation cannot be carried out
     */
    IRespectOperation inS(InputEvent ev) throws OperationNotPossibleException;

    /**
     * 
     * @param ev
     *            the event to handle
     * @return the operation requested
     * @throws OperationNotPossibleException
     *             if the requested operation cannot be carried out
     */
    IRespectOperation no(InputEvent ev) throws OperationNotPossibleException;

    /**
     * 
     * @param ev
     *            the event to handle
     * @return the operation requested
     * @throws OperationNotPossibleException
     *             if the requested operation cannot be carried out
     */
    IRespectOperation noAll(InputEvent ev) throws OperationNotPossibleException;

    /**
     * 
     * @param ev
     *            the event to handle
     * @return the operation requested
     * @throws OperationNotPossibleException
     *             if the requested operation cannot be carried out
     */
    IRespectOperation nop(InputEvent ev) throws OperationNotPossibleException;

    /**
     * 
     * @param ev
     *            the event to handle
     * @return the operation requested
     * @throws OperationNotPossibleException
     *             if the requested operation cannot be carried out
     */
    IRespectOperation nopS(InputEvent ev) throws OperationNotPossibleException;

    /**
     * 
     * @param ev
     *            the event to handle
     * @return the operation requested
     * @throws OperationNotPossibleException
     *             if the requested operation cannot be carried out
     */
    IRespectOperation noS(InputEvent ev) throws OperationNotPossibleException;

    /**
     * 
     * @param ev
     *            the event to handle
     * @return the operation requested
     * @throws OperationNotPossibleException
     *             if the requested operation cannot be carried out
     */
    IRespectOperation out(InputEvent ev) throws OperationNotPossibleException;

    /**
     * 
     * @param ev
     *            the event to handle
     * @return the operation requested
     * @throws OperationNotPossibleException
     *             if the requested operation cannot be carried out
     */
    IRespectOperation outAll(InputEvent ev)
            throws OperationNotPossibleException;

    /**
     * 
     * @param ev
     *            the event to handle
     * @return the operation requested
     * @throws OperationNotPossibleException
     *             if the requested operation cannot be carried out
     */
    IRespectOperation outS(InputEvent ev) throws OperationNotPossibleException;

    /**
     * 
     * @param ev
     *            the event to handle
     * @return the operation requested
     * @throws OperationNotPossibleException
     *             if the requested operation cannot be carried out
     */
    IRespectOperation rd(InputEvent ev) throws OperationNotPossibleException;

    /**
     * Retrieves all tuples in the tuple centre matching the template without
     * remove them
     * 
     * @param ev
     *            the event to handle
     * @return the operation requested
     * @throws OperationNotPossibleException
     *             if the requested operation cannot be carried out
     */
    IRespectOperation rdAll(InputEvent ev) throws OperationNotPossibleException;

    /**
     * 
     * @param ev
     *            the event to handle
     * @return the operation requested
     * @throws OperationNotPossibleException
     *             if the requested operation cannot be carried out
     */
    IRespectOperation rdp(InputEvent ev) throws OperationNotPossibleException;

    /**
     * 
     * @param ev
     *            the event to handle
     * @return the operation requested
     * @throws OperationNotPossibleException
     *             if the requested operation cannot be carried out
     */
    IRespectOperation rdpS(InputEvent ev) throws OperationNotPossibleException;

    /**
     * 
     * @param ev
     *            the event to handle
     * @return the operation requested
     * @throws OperationNotPossibleException
     *             if the requested operation cannot be carried out
     */
    IRespectOperation rdS(InputEvent ev) throws OperationNotPossibleException;

    /**
     * 
     * @param ev
     *            the event to handle
     * @return the operation requested
     * @throws OperationNotPossibleException
     *             if the requested operation cannot be carried out
     * @throws InvalidLogicTupleException
     *             if the given argument is not a valid Prolog tuple
     */
    IRespectOperation set(InputEvent ev) throws OperationNotPossibleException,
            InvalidLogicTupleException;

    /**
     * @param t
     *            the logic tuple representing the ReSpecT specification to set
     * @param ev
     *            the event to handle
     * @return the operation requested
     * @throws OperationNotPossibleException
     *             if the requested operation cannot be carried out
     */
    IRespectOperation setS(final LogicTuple t, final InputEvent ev)
            throws OperationNotPossibleException;

    /**
     * 
     * @param ev
     *            the event to handle
     * @param spec
     *            the ReSpecT specification to set
     * @return the operation requested
     * @throws OperationNotPossibleException
     *             if the requested operation cannot be carried out
     */
    IRespectOperation setSasynch(final InputEvent ev,
            final RespectSpecification spec)
            throws OperationNotPossibleException;

    /**
     * 
     * @param ev
     *            the event to handle
     * @param spec
     *            the ReSpecT specification to set
     * @return the operation requested
     * @throws OperationNotPossibleException
     *             if the requested operation cannot be carried out
     * @throws InvalidSpecificationException
     *             if the given ReSpecT specification has syntactical errors
     */
    IRespectOperation setSsynch(final InputEvent ev,
            final RespectSpecification spec)
            throws OperationNotPossibleException, InvalidSpecificationException;

    /**
     * 
     * @param ev
     *            the event to handle
     * @return the operation requested
     * @throws OperationNotPossibleException
     *             if the requested operation cannot be carried out
     */
    IRespectOperation spawn(InputEvent ev) throws OperationNotPossibleException;

    /**
     * Retrieves all tuples in the tuple centre matching the template
     * 
     * @param ev
     *            the event to handle
     * @return the operation requested
     * @throws OperationNotPossibleException
     *             if the requested operation cannot be carried out
     */
    IRespectOperation uin(InputEvent ev) throws OperationNotPossibleException;

    /**
     * Retrieves all tuples in the tuple centre matching the template
     * 
     * @param ev
     *            the event to handle
     * @return the operation requested
     * @throws OperationNotPossibleException
     *             if the requested operation cannot be carried out
     */
    IRespectOperation uinp(InputEvent ev) throws OperationNotPossibleException;

    /**
     * 
     * @param ev
     *            the event to handle
     * @return the operation requested
     * @throws OperationNotPossibleException
     *             if the requested operation cannot be carried out
     */
    IRespectOperation uno(InputEvent ev) throws OperationNotPossibleException;

    /**
     * 
     * @param ev
     *            the event to handle
     * @return the operation requested
     * @throws OperationNotPossibleException
     *             if the requested operation cannot be carried out
     */
    IRespectOperation unop(InputEvent ev) throws OperationNotPossibleException;

    /**
     * Retrieves all tuples in the tuple centre matching the template
     * 
     * @param ev
     *            the event to handle
     * @return the operation requested
     * @throws OperationNotPossibleException
     *             if the requested operation cannot be carried out
     */
    IRespectOperation urd(InputEvent ev) throws OperationNotPossibleException;

    /**
     * Retrieves all tuples in the tuple centre matching the template
     * 
     * @param ev
     *            the event to handle
     * @return the operation requested
     * @throws OperationNotPossibleException
     *             if the requested operation cannot be carried out
     */
    IRespectOperation urdp(InputEvent ev) throws OperationNotPossibleException;
}
