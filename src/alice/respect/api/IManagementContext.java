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
import alice.tuplecentre.api.InspectableEventListener;
import alice.tuplecentre.api.ObservableEventListener;

/**
 * Basic Management Interface for a RespecT Tuple Centre.
 * 
 * @author aricci
 */
public interface IManagementContext  {

	/**
	 * Specify the behaviour of the tuple centre
	 * 
	 * @param spec The specification in ReSpecT language
	 * 
	 * @throws InvalidSpecificationException If the specification is not correct
	 */
	void setSpec(RespectSpecification spec) throws InvalidSpecificationException;
	
	/**
	 * Get current behaviour specification 
	 * 
	 * @return the behaviour specification in ReSpecT
	 */
	RespectSpecification getSpec();    

	void reset();
	
	/**
	 * Abort a previously executed in or rd operation
	 * 
	 * The method is successful only if the operation
	 * has not completed 
	 * @param op the operation identifier
	 * @return true if the operation has been aborted
	 */
	boolean abortOperation(long opId);
	

	//
	
	/**
     * Enables/Disables the management mode
     * 
     * @param activate true to swith to management mode 
	 */	
	void setManagementMode(boolean activate);
	
	/**
	 * Stops the VM (management mode, debugging)
	 *   
	 * @throws OperationNotPossibleException if the operation is not possible
	 * according to current VM state
	 */
	void stopCommand() throws OperationNotPossibleException;

	/**
	 * Resumes VM execution (management mode)
	 * 
	 * @throws OperationNotPossibleException if the operation is not possible
	 * according to current VM state
	 */
	void goCommand() throws OperationNotPossibleException;

	/**
	 * Executes a single execution step (management mode)
	 * 
	 * @throws OperationNotPossibleException if the operation is not possible
	 * according to current VM state
	 */
	void nextStepCommand() throws OperationNotPossibleException;

	/**
	 * Enables spying information about VM state and dynamics
	 * 
	 * @param what true to enable 
	 */
	void setSpy(boolean what);
	
	//
	
	/**
	 * Gets current content of the tuple set 
	 * 
	 * @param filter tuple filtering tuples to be retrieved
	 */
	LogicTuple[] getTSet(LogicTuple filter);

	/**
	 * Gets current content of the query set in terms of logic tuples 
	 * 
	 * @param filter tuple filtering tuples to be retrieved
	 */
	LogicTuple[] getWSet(LogicTuple filter);
	
	/**
	 * Sets current content of the query set in terms of logic tuples 
	 * 
	 * @param query set in terms of logic tuples
	 */
	void setWSet(List<LogicTuple> wSet);

	/**
	 * Gets current content of the triggered reactions in terms of logic tuples 
	 * 
	 * @param filter tuple filtering tuples to be retrieved
	 */
	LogicTuple[] getTRSet(LogicTuple filter);
	
	public void addObserver(ObservableEventListener l);
	
	public void removeObserver(ObservableEventListener l);

	public boolean hasObservers();

    public void addInspector(InspectableEventListener l);

    public void removeInspector(InspectableEventListener l);

    public boolean hasInspectors();

}


