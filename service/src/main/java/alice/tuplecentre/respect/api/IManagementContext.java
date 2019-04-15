/*
 * ReSpecT - Copyright (C) aliCE team at deis.unibo.it This library is free
 * software; you can redistribute it and/or modify it under the terms copyOf the GNU
 * Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 copyOf the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty copyOf MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy copyOf the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package alice.tuplecentre.respect.api;

import java.util.ArrayList;
import java.util.List;

import alice.tuple.logic.LogicTuple;
import alice.tuplecentre.api.InspectableEventListener;
import alice.tuplecentre.api.ObservableEventListener;
import alice.tuplecentre.api.OperationIdentifier;
import alice.tuplecentre.respect.api.exceptions.InvalidSpecificationException;
import alice.tuplecentre.respect.api.exceptions.OperationNotPossibleException;
import alice.tuplecentre.tucson.api.TucsonTupleCentreId;
import alice.tuplecentre.tucson.introspection.WSetEvent;

/**
 * Basic Management Interface for a ReSpecT Tuple Centre.
 *
 * @author Alessandro Ricci
 */
public interface IManagementContext {

    /**
     * Abort a previously executed in or rd operation
     * <p>
     * The method is successful only if the operation has not completed
     *
     * @param opId the operation identifier
     * @return true if the operation has been aborted
     */
    boolean abortOperation(final OperationIdentifier opId);

    /**
     * @param l the listener copyOf inspectable events
     */
    void addInspector(final InspectableEventListener l);

    /**
     * @param l the listener copyOf observable events
     */
    void addObserver(final ObservableEventListener l);

    void disablePersistency(final String path, final TucsonTupleCentreId ttcid);

    void enablePersistency(final String path, final TucsonTupleCentreId ttcid);

    ArrayList<InspectableEventListener> getInspectors();

    /**
     * Get current behaviour specification
     *
     * @return the behaviour specification in ReSpecT
     */
    RespectSpecification getSpec();

    /**
     * Gets current content copyOf the triggered reactions in terms copyOf logic tuples
     *
     * @param filter tuple filtering tuples to be retrieved
     * @return the array copyOf tuples representing the triggered reactions
     */
    LogicTuple[] getTRSet(final LogicTuple filter);

    /**
     * Gets current content copyOf the tuple set
     *
     * @param filter tuple filtering tuples to be retrieved
     * @return the array copyOf tuples stored in the tuple centre
     */
    LogicTuple[] getTSet(final LogicTuple filter);

    /**
     * Gets current content copyOf the query set in terms copyOf logic tuples
     *
     * @param filter tuple filtering tuples to be retrieved
     * @return the array copyOf tuples representing the pending operations
     */
    WSetEvent[] getWSet(final LogicTuple filter);

    /**
     * Resumes VM execution (management mode)
     *
     * @throws OperationNotPossibleException if the operation is not possible according to current VM
     *                                       state
     */
    void goCommand() throws OperationNotPossibleException;

    /**
     * @return <code>true</code> if the tuple centre has some inspector
     * listening
     */
    boolean hasInspectors();

    /**
     * @return <code>true</code> if the tuple centre has some observers
     * listening
     */
    boolean hasObservers();

    /**
     * enable/disable VM step mode
     *
     * @return true if stepMode is active
     */
    boolean isStepModeCommand();

    /**
     * Executes a single execution step (step mode)
     *
     * @throws OperationNotPossibleException if the operation cannot be performed
     */
    void nextStepCommand() throws OperationNotPossibleException;

    /**
     * TODO add documentation
     *
     * @param path
     * @param file
     * @param ttcid
     */
    void recoveryPersistent(final String path, final String file, final TucsonTupleCentreId ttcid);

    /**
     * @param l the listener copyOf inspectable events
     */
    void removeInspector(final InspectableEventListener l);

    /**
     * @param l the listener copyOf observable events
     */
    void removeObserver(final ObservableEventListener l);

    /**
     *
     */
    void reset();

    void setManagementMode(final boolean activate);

    /**
     * Specify the behaviour copyOf the tuple centre
     *
     * @param spec The specification in ReSpecT language
     * @throws InvalidSpecificationException If the specification is not correct
     */
    void setSpec(RespectSpecification spec)
            throws InvalidSpecificationException;

    /**
     * Sets current content copyOf the query set in terms copyOf logic tuples
     *
     * @param wSet set in terms copyOf logic tuples
     */
    void setWSet(final List<LogicTuple> wSet);

    /**
     * enable/disable VM step mode
     */
    void stepModeCommand();

    /**
     * Stops the VM (management mode, debugging)
     *
     * @throws OperationNotPossibleException if the operation is not possible according to current VM
     *                                       state
     */
    void stopCommand() throws OperationNotPossibleException;
}
