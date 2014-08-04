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

import java.util.List;
import alice.logictuple.LogicTuple;
import alice.respect.api.exceptions.InvalidSpecificationException;
import alice.respect.api.exceptions.OperationNotPossibleException;
import alice.tucson.api.TucsonTupleCentreId;
import alice.tucson.introspection.WSetEvent;
import alice.tuplecentre.api.InspectableEventListener;
import alice.tuplecentre.api.ObservableEventListener;

/**
 * Basic Management Interface for a RespecT Tuple Centre.
 * 
 * @author Alessandro Ricci
 */
public interface IManagementContext {
    /**
     * Abort a previously executed in or rd operation
     * 
     * The method is successful only if the operation has not completed
     * 
     * @param opId
     *            the operation identifier
     * @return true if the operation has been aborted
     */
    boolean abortOperation(long opId);

    /**
     * 
     * @param l
     *            the listener of inspectable events
     */
    void addInspector(InspectableEventListener l);

    /**
     * 
     * @param l
     *            the listener of observable events
     */
    void addObserver(ObservableEventListener l);

    void disablePersistency(String path, TucsonTupleCentreId ttcid);

    void enablePersistency(String path, TucsonTupleCentreId ttcid);

    /**
     * Get current behaviour specification
     * 
     * @return the behaviour specification in ReSpecT
     */
    RespectSpecification getSpec();

    /**
     * Gets current content of the triggered reactions in terms of logic tuples
     * 
     * @param filter
     *            tuple filtering tuples to be retrieved
     * @return the array of tuples representing the triggered reactions
     */
    LogicTuple[] getTRSet(LogicTuple filter);

    /**
     * Gets current content of the tuple set
     * 
     * @param filter
     *            tuple filtering tuples to be retrieved
     * @return the array of tuples stored in the tuple centre
     */
    LogicTuple[] getTSet(LogicTuple filter);

    /**
     * Gets current content of the query set in terms of logic tuples
     * 
     * @param filter
     *            tuple filtering tuples to be retrieved
     * @return the array of tuples representing the pending operations
     */
    WSetEvent[] getWSet(LogicTuple filter);

    /**
     * Resumes VM execution (management mode)
     * 
     * @throws OperationNotPossibleException
     *             if the operation is not possible according to current VM
     *             state
     */
    void goCommand() throws OperationNotPossibleException;

    /**
     * 
     * @return <code>true</code> if the tuple centre has some inspector
     *         listening
     */
    boolean hasInspectors();

    /**
     * 
     * @return <code>true</code> if the tuple centre has some observers
     *         listening
     */
    boolean hasObservers();
    
    /**
     * enable/disable VM step mode
     */
    void stepModeCommand();

    /**
     * Executes a single execution step (step mode)
     * @throws alice.tuplecentre.api.exceptions.OperationNotPossibleException 
     */
    void nextStepCommand() throws alice.tuplecentre.api.exceptions.OperationNotPossibleException;

    void recoveryPersistent(String path, String file, TucsonTupleCentreId ttcid);

    /**
     * 
     * @param l
     *            the listener of inspectable events
     */
    void removeInspector(InspectableEventListener l);

    /**
     * 
     * @param l
     *            the listener of observable events
     */
    void removeObserver(ObservableEventListener l);

    /**
     * 
     */
    void reset();

    /**
     * Specify the behaviour of the tuple centre
     * 
     * @param spec
     *            The specification in ReSpecT language
     * 
     * @throws InvalidSpecificationException
     *             If the specification is not correct
     */
    void setSpec(RespectSpecification spec)
            throws InvalidSpecificationException;

    /**
     * Sets current content of the query set in terms of logic tuples
     * 
     * @param wSet
     *            set in terms of logic tuples
     */
    void setWSet(List<LogicTuple> wSet);

    /**
     * Stops the VM (management mode, debugging)
     * 
     * @throws OperationNotPossibleException
     *             if the operation is not possible according to current VM
     *             state
     */
    void stopCommand() throws OperationNotPossibleException;

	void setManagementMode(boolean activate);
}
