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
package alice.tuplecentre.respect.api;

import alice.logictuple.LogicTuple;
import alice.logictuple.exceptions.InvalidLogicTupleException;
import alice.tuplecentre.api.TupleCentreIdentifier;
import alice.tuplecentre.core.InputEvent;
import alice.tuplecentre.respect.api.exceptions.InvalidSpecificationException;
import alice.tuplecentre.respect.api.exceptions.OperationNotPossibleException;
import alice.tuplecentre.respect.core.RespectVM;

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
    RespectOperation get(InputEvent ev) throws OperationNotPossibleException;

    /**
     * Gets the tuple centre id
     * 
     * @return the tuple centre id
     */
    TupleCentreIdentifier getId();

    /**
     * 
     * @param ev
     *            the event to handle
     * @return the operation requested
     * @throws OperationNotPossibleException
     *             if the requested operation cannot be carried out
     */
    RespectOperation getS(InputEvent ev) throws OperationNotPossibleException;
    
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
    RespectOperation in(InputEvent ev) throws OperationNotPossibleException;

    /**
     * 
     * @param ev
     *            the event to handle
     * @return the operation requested
     * @throws OperationNotPossibleException
     *             if the requested operation cannot be carried out
     */
    RespectOperation inAll(InputEvent ev) throws OperationNotPossibleException;

    /**
     * 
     * @param ev
     *            the event to handle
     * @return the operation requested
     * @throws OperationNotPossibleException
     *             if the requested operation cannot be carried out
     */
    RespectOperation inp(InputEvent ev) throws OperationNotPossibleException;

    /**
     * 
     * @param ev
     *            the event to handle
     * @return the operation requested
     * @throws OperationNotPossibleException
     *             if the requested operation cannot be carried out
     */
    RespectOperation inpS(InputEvent ev) throws OperationNotPossibleException;

    /**
     * 
     * @param ev
     *            the event to handle
     * @return the operation requested
     * @throws OperationNotPossibleException
     *             if the requested operation cannot be carried out
     */
    RespectOperation inS(InputEvent ev) throws OperationNotPossibleException;

    /**
     * 
     * @param ev
     *            the event to handle
     * @return the operation requested
     * @throws OperationNotPossibleException
     *             if the requested operation cannot be carried out
     */
    RespectOperation no(InputEvent ev) throws OperationNotPossibleException;

    /**
     * 
     * @param ev
     *            the event to handle
     * @return the operation requested
     * @throws OperationNotPossibleException
     *             if the requested operation cannot be carried out
     */
    RespectOperation noAll(InputEvent ev) throws OperationNotPossibleException;

    /**
     * 
     * @param ev
     *            the event to handle
     * @return the operation requested
     * @throws OperationNotPossibleException
     *             if the requested operation cannot be carried out
     */
    RespectOperation nop(InputEvent ev) throws OperationNotPossibleException;

    /**
     * 
     * @param ev
     *            the event to handle
     * @return the operation requested
     * @throws OperationNotPossibleException
     *             if the requested operation cannot be carried out
     */
    RespectOperation nopS(InputEvent ev) throws OperationNotPossibleException;

    /**
     * 
     * @param ev
     *            the event to handle
     * @return the operation requested
     * @throws OperationNotPossibleException
     *             if the requested operation cannot be carried out
     */
    RespectOperation noS(InputEvent ev) throws OperationNotPossibleException;

    /**
     * 
     * @param ev
     *            the event to handle
     * @return the operation requested
     * @throws OperationNotPossibleException
     *             if the requested operation cannot be carried out
     */
    RespectOperation out(InputEvent ev) throws OperationNotPossibleException;

    /**
     * 
     * @param ev
     *            the event to handle
     * @return the operation requested
     * @throws OperationNotPossibleException
     *             if the requested operation cannot be carried out
     */
    RespectOperation outAll(InputEvent ev)
            throws OperationNotPossibleException;

    /**
     * 
     * @param ev
     *            the event to handle
     * @return the operation requested
     * @throws OperationNotPossibleException
     *             if the requested operation cannot be carried out
     */
    RespectOperation outS(InputEvent ev) throws OperationNotPossibleException;

    /**
     * 
     * @param ev
     *            the event to handle
     * @return the operation requested
     * @throws OperationNotPossibleException
     *             if the requested operation cannot be carried out
     */
    RespectOperation rd(InputEvent ev) throws OperationNotPossibleException;

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
    RespectOperation rdAll(InputEvent ev) throws OperationNotPossibleException;

    /**
     * 
     * @param ev
     *            the event to handle
     * @return the operation requested
     * @throws OperationNotPossibleException
     *             if the requested operation cannot be carried out
     */
    RespectOperation rdp(InputEvent ev) throws OperationNotPossibleException;

    /**
     * 
     * @param ev
     *            the event to handle
     * @return the operation requested
     * @throws OperationNotPossibleException
     *             if the requested operation cannot be carried out
     */
    RespectOperation rdpS(InputEvent ev) throws OperationNotPossibleException;

    /**
     * 
     * @param ev
     *            the event to handle
     * @return the operation requested
     * @throws OperationNotPossibleException
     *             if the requested operation cannot be carried out
     */
    RespectOperation rdS(InputEvent ev) throws OperationNotPossibleException;

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
    RespectOperation set(InputEvent ev) throws OperationNotPossibleException,
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
    RespectOperation setS(final LogicTuple t, final InputEvent ev)
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
    RespectOperation setSasynch(final InputEvent ev,
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
    RespectOperation setSsynch(final InputEvent ev,
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
    RespectOperation spawn(InputEvent ev) throws OperationNotPossibleException;

    /**
     * Retrieves all tuples in the tuple centre matching the template
     * 
     * @param ev
     *            the event to handle
     * @return the operation requested
     * @throws OperationNotPossibleException
     *             if the requested operation cannot be carried out
     */
    RespectOperation uin(InputEvent ev) throws OperationNotPossibleException;

    /**
     * Retrieves all tuples in the tuple centre matching the template
     * 
     * @param ev
     *            the event to handle
     * @return the operation requested
     * @throws OperationNotPossibleException
     *             if the requested operation cannot be carried out
     */
    RespectOperation uinp(InputEvent ev) throws OperationNotPossibleException;

    /**
     * 
     * @param ev
     *            the event to handle
     * @return the operation requested
     * @throws OperationNotPossibleException
     *             if the requested operation cannot be carried out
     */
    RespectOperation uno(InputEvent ev) throws OperationNotPossibleException;

    /**
     * 
     * @param ev
     *            the event to handle
     * @return the operation requested
     * @throws OperationNotPossibleException
     *             if the requested operation cannot be carried out
     */
    RespectOperation unop(InputEvent ev) throws OperationNotPossibleException;

    /**
     * Retrieves all tuples in the tuple centre matching the template
     * 
     * @param ev
     *            the event to handle
     * @return the operation requested
     * @throws OperationNotPossibleException
     *             if the requested operation cannot be carried out
     */
    RespectOperation urd(InputEvent ev) throws OperationNotPossibleException;

    /**
     * Retrieves all tuples in the tuple centre matching the template
     * 
     * @param ev
     *            the event to handle
     * @return the operation requested
     * @throws OperationNotPossibleException
     *             if the requested operation cannot be carried out
     */
    RespectOperation urdp(InputEvent ev) throws OperationNotPossibleException;
}
