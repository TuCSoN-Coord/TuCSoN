/*
 * ReSpecT - Copyright (C) aliCE team at deis.unibo.it
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
package alice.respect.api;
import java.util.List;

import alice.logictuple.*;
import alice.respect.core.RespectVM;
import alice.tuplecentre.api.IId;
import alice.tuplecentre.core.OperationCompletionListener;

/**
 * Basic usage interface of a  RespecT Tuple Centre
 * 
 * 
 * @author aricci
 */
public interface IRespectTC  {

	// metodo per ottenere l'ontologia relativa al tc
	
	/**
	 * Inserts a tuple in the tuple centre
	 * 
	 * @param id agent identifier
	 * @param t the tuple
	 * @return The object representing the operation
	 * @throws OperationNotPossibleException if the operation is not possible given current state of the tuple centre
	 */
	IRespectOperation out(IId id, LogicTuple t) throws OperationNotPossibleException;
   
	/**
	 * Gets the whole tuple set
	 * 
	 * @param id agent identifier
	 * @return The object representing the operation
	 * @throws OperationNotPossibleException if the operation is not possible given current state of the tuple centre
	 */
	IRespectOperation get(IId id) throws OperationNotPossibleException;
	
	/**
	 * Gets the whole tuple set
	 * 
	 * @param id agent identifier
	 * @param listener listening for operation completion
	 * @return The object representing the operation
	 * @throws OperationNotPossibleException if the operation is not possible given current state of the tuple centre
	 */
	IRespectOperation get(IId id,OperationCompletionListener l) throws OperationNotPossibleException;
	
	/**
	 * Gets the whole tuple set
	 * 
	 * @param id agent identifier
	 * @return The object representing the operation
	 * @throws OperationNotPossibleException if the operation is not possible given current state of the tuple centre
	 */
	IRespectOperation set(IId id, List<LogicTuple> tupleList) throws OperationNotPossibleException;
	
	/**
	 * Gets the whole tuple set
	 * 
	 * @param id agent identifier
	 * @param listener listening for operation completion
	 * @return The object representing the operation
	 * @throws OperationNotPossibleException if the operation is not possible given current state of the tuple centre
	 */
	IRespectOperation set(IId id, List<LogicTuple> tupleList, OperationCompletionListener l) throws OperationNotPossibleException;
	
	/**
	 * Retrieves a tuple in the tuple centre
	 * 
	 * @param id agent identifier
	 * @param t the tuple
	 * @return The object representing the operation
	 * @throws OperationNotPossibleException if the operation is not possible given current state of the tuple centre
	 */
	IRespectOperation in(IId id, LogicTuple t) throws OperationNotPossibleException;
	 
	/**
	 * Reads a tuple in the tuple centre
	 * 
	 * @param id agent identifier
	 * @param t the tuple
	 * @return The object representing the operation
	 * @throws OperationNotPossibleException if the operation is not possible given current state of the tuple centre
	 */
	IRespectOperation rd(IId id, LogicTuple t)  throws OperationNotPossibleException;
    
	/**
	 * Tries to retrieve a tuple in the tuple centre
	 * 
	 * @param id agent identifier
	 * @param t the tuple
	 * @return The object representing the operation
	 * @throws OperationNotPossibleException if the operation is not possible given current state of the tuple centre
	 */
	IRespectOperation inp(IId id, LogicTuple t)  throws OperationNotPossibleException;
    
	/**
	 * Tries to read a tuple in the tuple centre
	 * 
	 * @param id agent identifier
	 * @param t the tuple
	 * @return The object representing the operation
	 * @throws OperationNotPossibleException if the operation is not possible given current state of the tuple centre
	 */
	IRespectOperation rdp(IId id, LogicTuple t) throws OperationNotPossibleException;

	/**
	 * Inserts a tuple in the tuple centre
	 * 
	 * @param id agent identifier
	 * @param t the tuple
	 * @param listener listening for operation completion
	 * @return The object representing the operation
	 * @throws OperationNotPossibleException if the operation is not possible given current state of the tuple centre
	 */
	IRespectOperation out(IId id, LogicTuple t, OperationCompletionListener l) throws OperationNotPossibleException;
   
	/**
	 * Retrieves a tuple in the tuple centre
	 * 
	 * @param id agent identifier
	 * @param t the tuple
	 * @param listener listening for operation completion
	 * @return The object representing the operation
	 * @throws OperationNotPossibleException if the operation is not possible given current state of the tuple centre
	 */
	IRespectOperation in(IId id, LogicTuple t, OperationCompletionListener l) throws OperationNotPossibleException;
	 
	/**
	 * Reads a tuple in the tuple centre
	 * 
	 * @param id agent identifier
	 * @param t the tuple
	 * @param listener listening for operation completion
	 * @return The object representing the operation
	 * @throws OperationNotPossibleException if the operation is not possible given current state of the tuple centre
	 */
	IRespectOperation rd(IId id, LogicTuple t, OperationCompletionListener l)  throws OperationNotPossibleException;
    
	/**
	 * Tries to retrieve a tuple in the tuple centre
	 * 
	 * @param id agent identifier
	 * @param t the tuple
	 * @param listener listening for operation completion
	 * @return The object representing the operation
	 * @throws OperationNotPossibleException if the operation is not possible given current state of the tuple centre
	 */
	IRespectOperation inp(IId id, LogicTuple t, OperationCompletionListener l)  throws OperationNotPossibleException;
    
	/**
	 * Tries to read a tuple in the tuple centre
	 * 
	 * @param id agent identifier
	 * @param t the tuple
	 * @param listener listening for operation completion
	 * @return The object representing the operation
	 * @throws OperationNotPossibleException if the operation is not possible given current state of the tuple centre
	 */
	IRespectOperation rdp(IId id, LogicTuple t, OperationCompletionListener l) throws OperationNotPossibleException;
	
	/**
	 * Inserts a tuple in the tuple centre
	 * 
	 * @param id agent identifier
	 * @param t the tuple
	 * @param listener listening for operation completion
	 * @return The object representing the operation
	 * @throws OperationNotPossibleException if the operation is not possible given current state of the tuple centre
	 */
	IRespectOperation out_s(IId id, LogicTuple t, OperationCompletionListener l) throws OperationNotPossibleException;

	IRespectOperation in_s(IId id, LogicTuple t) throws OperationNotPossibleException;
	
	IRespectOperation rd_s(IId id, LogicTuple t)  throws OperationNotPossibleException;

	IRespectOperation inp_s(IId id, LogicTuple t) throws OperationNotPossibleException;
	
	IRespectOperation rdp_s(IId id, LogicTuple t)  throws OperationNotPossibleException;
	
	/**
	 * Specify the behaviour of the tuple centre
	 * 
	 * @param spec The specification in ReSpecT language
	 * 
	 * @throws InvalidSpecificationException If the specification is not correct
	 */
	void setSpec(IId id, RespectSpecification spec) throws InvalidSpecificationException;
	
	/**
	 * Get current behaviour specification 
	 * 
	 * @return the behaviour specification in ReSpecT
	 */
	RespectSpecification getSpec(IId id);    

	/**
	 * Gets the tuple centre id
	 * 
	 * @return the id
	 */
	TupleCentreId getId();

	/**
	 * Add the reaction specification to the tuple centre behaviour 
	 * 
	 * @param id agent identifier
	 * @param t the tuple representing the reaction specification in ReSpecT language
	 * @return the object representing the operation
	 */
	IRespectOperation out_s(IId id, LogicTuple t) throws OperationNotPossibleException;
	
	IRespectOperation in_s(IId id, LogicTuple t, OperationCompletionListener l) throws OperationNotPossibleException;

	IRespectOperation rd_s(IId id, LogicTuple t, OperationCompletionListener l)  throws OperationNotPossibleException;	

	IRespectOperation inp_s(IId id, LogicTuple t, OperationCompletionListener l) throws OperationNotPossibleException;

	IRespectOperation rdp_s(IId id, LogicTuple t, OperationCompletionListener l)  throws OperationNotPossibleException;	
	
	public RespectVM getVM();

}

