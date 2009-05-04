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

import alice.tucson.api.exception.LanguageNotFoundException;
import alice.tucson.api.exception.OperationNotAllowedException;
import alice.tucson.api.exception.UnreachableNodeException;
import alice.tuplecentre.api.OperationTimeOutException;
import alice.logictuple.*;

/**
 * 
 * Blocking interface of any TuCSoN agent coordination context
 * 
 * An agent coordination context defines (and enables) the blocking actions that an agent
 * can do on by means of a coordination medium.
 * 
 */
public interface TucsonBlockingContext
{
	/**
	 * Inserts a tuple in a tuple centre
	 * 
	 * @param tid
	 *            is the identifier of the target tuple centre (supports
	 *            different types in relation with the used language:
	 *            TupleCentreId, String, ...)
	 * @param tuple
	 *            is the logic tuple to be inserted (supports different types in
	 *            relation with the used language: LogicTuple, String, ...)	 
	 * @throws s
	 *             OperationNotAllowedException of the operation is not allowed
	 * @throws s
	 *             UnreachableNodeException if the node specified in the tuple
	 *             centre identifier is not reachable
	 * @throws LanguageNotFoundException
	 */
	LogicTuple out(Object tid, LogicTuple tuple, Long ms) throws OperationNotAllowedException, UnreachableNodeException,
	LanguageNotFoundException, OperationTimeOutException;

	/**
	 * Gets a tuple from a tuple centre
	 * 
	 * This coordination primitive is blocking
	 * 
	 * @param tid
	 *            is the identifier of the target tuple centre (supports
	 *            different types in relation with the used language:
	 *            TupleCentreId, String, ...)
	 * @param tuple
	 *            is the logic tuple specifying the template used for the
	 *            matching (supports different types in relation with the used
	 *            language: LogicTuple, String, ...)
	 * @return the logic tuple retrieved from the tuple centre
	 * @throws s
	 *             OperationNotAllowedException of the operation is not allowed
	 * @throws s
	 *             UnreachableNodeException if the node specified in the tuple
	 *             centre identifier is not reachable
	 */	
	LogicTuple in(Object tid, LogicTuple tuple, Long ms) throws OperationNotAllowedException, UnreachableNodeException,
	LanguageNotFoundException, OperationTimeOutException;

	/**
	 * Reads a tuple from a tuple centre
	 * 
	 * This coordination primitive is blocking
	 * 
	 * @param tid
	 *            is the identifier of the target tuple centre (supports
	 *            different types in relation with the used language:
	 *            TupleCentreId, String, ...)
	 * @param tuple
	 *            is the logic tuple specifying the template used for the
	 *            matching (supports different types in relation with the used
	 *            language: LogicTuple, String, ...)
	 * @return the logic tuple matching the request
	 * @throws s
	 *             OperationNotAllowedException of the operation is not allowed
	 * @throws s
	 *             UnreachableNodeException if the node specified in the tuple
	 *             centre identifier is not reachable
	 */
	LogicTuple rd(Object tid, LogicTuple tuple, Long ms) throws OperationNotAllowedException, UnreachableNodeException,
	LanguageNotFoundException, OperationTimeOutException;
	
	/**
	 * Gets a tuple from a tuple centre, without blocking
	 * 
	 * This coordination primitive is non-blocking: if no tuple is found the
	 * primitive returns a null value.
	 * 
	 * @param tid
	 *            is the identifier of the target tuple centre (supports
	 *            different types in relation with the used language:
	 *            TupleCentreId, String, ...)
	 * @param tuple
	 *            is the logic tuple specifying the template used for the
	 *            matching (supports different types in relation with the used
	 *            language: LogicTuple, String, ...)
	 * @return the logic tuple retrieved from the tuple centre or null if no
	 *         tuple is found
	 * @throws s
	 *             OperationNotAllowedException of the operation is not allowed
	 * @throws s
	 *             UnreachableNodeException if the node specified in the tuple
	 *             centre identifier is not reachable
	 */
	LogicTuple inp(Object tid, LogicTuple tuple, Long ms) throws OperationNotAllowedException, UnreachableNodeException,
	LanguageNotFoundException, OperationTimeOutException;

	/**
	 * Reads a tuple from a tuple centre, without blocking
	 * 
	 * This coordination primitive is non-blocking: if no tuple is found the
	 * primitive returns a null value.
	 * 
	 * @param tid
	 *            is the identifier of the target tuple centre (supports
	 *            different types in relation with the used language:
	 *            TupleCentreId, String, ...)
	 * @param tuple
	 *            is the logic tuple specifying the template used for the
	 *            matching (supports different types in relation with the used
	 *            language: LogicTuple, String, ...)
	 * @return the logic tuple retrieved from the tuple centre or null if no
	 *         tuple is found
	 * @throws s
	 *             OperationNotAllowedException of the operation is not allowed
	 * @throws s
	 *             UnreachableNodeException if the node specified in the tuple
	 *             centre identifier is not reachable
	 */	
	LogicTuple rdp(Object tid, LogicTuple tuple, Long ms) throws OperationNotAllowedException, UnreachableNodeException,
	LanguageNotFoundException, OperationTimeOutException;
	
	/**
	 * Sets the behaviour of a tuple centre
	 * 
	 * @param tid
	 *            is the identifier of the target tuple centre (supports
	 *            different types in relation with the used language:
	 *            TupleCentreId, String, ...)
	 * @param spec
	 *            is behaviour specification in the ReSpecT language (supports
	 *            different types in relation with the used language: String,
	 *            ...)
	 * @throws s
	 *             OperationNotAllowedException of the operation is not allowed
	 * @throws s
	 *             UnreachableNodeException if the node specified in the tuple
	 *             centre identifier is not reachable
	 * @throws s
	 *             InvalidSpecificationException if the specification is not
	 *             valid
	 */
	LogicTuple setSpec(Object tid, String spec, Long ms) throws OperationNotAllowedException, 
	UnreachableNodeException, LanguageNotFoundException, OperationTimeOutException;
	
	/**
	 * Gets the behaviour specification of a tuple centre
	 * 
	 * @param tid
	 *            is the identifier of the target tuple centre (supports
	 *            different types in relation with the used language:
	 *            TupleCentreId, String, ...)
	 * @return the behavious specification in the ReSpecT language
	 * @throws s
	 *             OperationNotAllowedException of the operation is not allowed
	 * @throws s
	 *             UnreachableNodeException if the node specified in the tuple
	 *             centre identifier is not reachable
	 */
	String getSpec(Object tid, Long ms) throws OperationNotAllowedException, UnreachableNodeException,
	LanguageNotFoundException, OperationTimeOutException, InvalidTupleOperationException;

	/**
	 * Inserts a tuple in a tuple centre
	 * 
	 * @param tid
	 *            is the identifier of the target tuple centre (supports
	 *            different types in relation with the used language:
	 *            TupleCentreId, String, ...)
	 * @param tuple
	 *            is the logic tuple to be inserted (supports different types in
	 *            relation with the used language: LogicTuple, String, ...)
	 * @param language
	 *            is the language for labeled the primitive (supports different
	 *            types in relation with the used language: String, ...)
	 * @throws s
	 *             OperationNotAllowedException of the operation is not allowed
	 * @throws s
	 *             UnreachableNodeException if the node specified in the tuple
	 *             centre identifier is not reachable
	 */
	LogicTuple out(Object tid, LogicTuple tuple, Long ms, Object language) throws OperationNotAllowedException, UnreachableNodeException,
	LanguageNotFoundException, OperationTimeOutException;

	/**
	 * Gets a tuple from a tuple centre
	 * 
	 * This coordination primitive is blocking
	 * 
	 * @param tid
	 *            is the identifier of the target tuple centre (supports
	 *            different types in relation with the used language:
	 *            TupleCentreId, String, ...)
	 * @param tuple
	 *            is the logic tuple specifying the template used for the
	 *            matching (supports different types in relation with the used
	 *            language: LogicTuple, String, ...)
	 * @param language
	 *            is the language for labeled the primitive (supports different
	 *            types in relation with the used language: String, ...)
	 * @return the logic tuple retrieved from the tuple centre
	 * @throws s
	 *             OperationNotAllowedException of the operation is not allowed
	 * @throws s
	 *             UnreachableNodeException if the node specified in the tuple
	 *             centre identifier is not reachable
	 */
	LogicTuple in(Object tid, LogicTuple tuple, Long ms, Object language) throws OperationNotAllowedException, UnreachableNodeException,
	LanguageNotFoundException, OperationTimeOutException;

	/**
	 * Reads a tuple from a tuple centre
	 * 
	 * This coordination primitive is blocking
	 * 
	 * @param tid
	 *            is the identifier of the target tuple centre (supports
	 *            different types in relation with the used language:
	 *            TupleCentreId, String, ...)
	 * @param tuple
	 *            is the logic tuple specifying the template used for the
	 *            matching (supports different types in relation with the used
	 *            language: LogicTuple, String, ...)
	 * @param language
	 *            is the language for labeled the primitive (supports different
	 *            types in relation with the used language: String, ...)
	 * @return the logic tuple matching the request
	 * @throws s
	 *             OperationNotAllowedException of the operation is not allowed
	 * @throws s
	 *             UnreachableNodeException if the node specified in the tuple
	 *             centre identifier is not reachable
	 */
	LogicTuple rd(Object tid, LogicTuple tuple, Long ms, Object language) throws OperationNotAllowedException, UnreachableNodeException,
	LanguageNotFoundException, OperationTimeOutException;
	
	/**
	 * Gets a tuple from a tuple centre, without blocking
	 * 
	 * This coordination primitive is non-blocking: if no tuple is found the
	 * primitive returns a null value.
	 * 
	 * @param tid
	 *            is the identifier of the target tuple centre (supports
	 *            different types in relation with the used language:
	 *            TupleCentreId, String, ...)
	 * @param tuple
	 *            is the logic tuple specifying the template used for the
	 *            matching (supports different types in relation with the used
	 *            language: LogicTuple, String, ...)
	 * @param language
	 *            is the language for labeled the primitive (supports different
	 *            types in relation with the used language: String, ...)
	 * @return the logic tuple retrieved from the tuple centre or null if no
	 *         tuple is found
	 * @throws s
	 *             OperationNotAllowedException of the operation is not allowed
	 * @throws s
	 *             UnreachableNodeException if the node specified in the tuple
	 *             centre identifier is not reachable
	 */
	LogicTuple inp(Object tid, LogicTuple tuple, Long ms, Object language) throws OperationNotAllowedException, UnreachableNodeException,
	LanguageNotFoundException, OperationTimeOutException;
	
	/**
	 * Reads a tuple from a tuple centre, without blocking
	 * 
	 * This coordination primitive is non-blocking: if no tuple is found the
	 * primitive returns a null value.
	 * 
	 * @param tid
	 *            is the identifier of the target tuple centre (supports
	 *            different types in relation with the used language:
	 *            TupleCentreId, String, ...)
	 * @param tuple
	 *            is the logic tuple specifying the template used for the
	 *            matching (supports different types in relation with the used
	 *            language: LogicTuple, String, ...)
	 * @param language
	 *            is the language for labeled the primitive (supports different
	 *            types in relation with the used language: String, ...)
	 * @return the logic tuple retrieved from the tuple centre or null if no
	 *         tuple is found
	 * @throws s
	 *             OperationNotAllowedException of the operation is not allowed
	 * @throws s
	 *             UnreachableNodeException if the node specified in the tuple
	 *             centre identifier is not reachable
	 */	
	LogicTuple rdp(Object tid, LogicTuple tuple, Long ms, Object language) throws OperationNotAllowedException, UnreachableNodeException,
	LanguageNotFoundException, OperationTimeOutException;	
	
	/**
	 * Sets the behaviour of a tuple centre
	 * 
	 * @param tid
	 *            is the identifier of the target tuple centre (supports
	 *            different types in relation with the used language:
	 *            TupleCentreId, String, ...)
	 * @param spec
	 *            is behaviour specification in the ReSpecT language (supports
	 *            different types in relation with the used language: String,
	 *            ...)
	 * @param language
	 *            is the language for labeled the primitive (supports different
	 *            types in relation with the used language: String, ...)
	 * @throws s
	 *             OperationNotAllowedException of the operation is not allowed
	 * @throws s
	 *             UnreachableNodeException if the node specified in the tuple
	 *             centre identifier is not reachable
	 * @throws s
	 *             InvalidSpecificationException if the specification is not
	 *             valid
	 */
	LogicTuple setSpec(Object tid, String spec, Long ms, Object language) throws OperationNotAllowedException, UnreachableNodeException, LanguageNotFoundException, OperationTimeOutException;

	/**
	 * Gets the behaviour specification of a tuple centre
	 * 
	 * @param tid
	 *            is the identifier of the target tuple centre (supports
	 *            different types in relation with the used language:
	 *            TupleCentreId, String, ...)
	 * @param language
	 *            is the language for labeled the primitive (supports different
	 *            types in relation with the used language: String, ...)
	 * @return the behavious specification in the ReSpecT language
	 * @throws s
	 *             OperationNotAllowedException of the operation is not allowed
	 * @throws s
	 *             UnreachableNodeException if the node specified in the tuple
	 *             centre identifier is not reachable
	 */
	String getSpec(Object tid, Long ms, Object language) throws OperationNotAllowedException, UnreachableNodeException,
	LanguageNotFoundException, OperationTimeOutException, InvalidTupleOperationException;
}
