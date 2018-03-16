/*
 * Copyright 1999-2014 Alma Mater Studiorum - Universita' di Bologna
 *
 * This file is part of TuCSoN <http://tucson.unibo.it>.
 *
 *    TuCSoN is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU Lesser General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    TuCSoN is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU Lesser General Public License for more details.
 *
 *    You should have received a copy of the GNU Lesser General Public License
 *    along with TuCSoN.  If not, see <https://www.gnu.org/licenses/lgpl.html>.
 *
 */
package alice.tuplecentre.tucson.asynchSupport;

import alice.tuplecentre.core.AbstractTupleCentreOperation;
import alice.tuplecentre.tucson.api.TucsonOperationCompletionListener;
import alice.tuplecentre.tucson.api.acc.EnhancedAsyncACC;
import alice.tuplecentre.tucson.api.actions.AbstractTucsonAction;
import alice.tuplecentre.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tuplecentre.tucson.api.exceptions.UnreachableNodeException;

/**
 * Class wrapping a {@link alice.tuplecentre.core.AbstractTupleCentreOperation}
 * to enable search based on operation type in queues.
 *
 * @author Fabio Consalici, Riccardo Drudi
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 */
public class TucsonOpWrapper {

    private final EnhancedAsyncACC acc;
    private final AbstractTucsonAction action;
    private boolean hasBeenRemoved = false;
    private final TucsonOperationCompletionListener listener;
    private AbstractTupleCentreOperation op;

    /**
     * Builds the wrapper to a TuCSoN operation.
     *
     * @param eaacc the ACC through which the operation should be executed
     * @param a     the TuCSoN operation to execute
     * @param l     the TuCSoN listener handling operation completion
     */
    public TucsonOpWrapper(final EnhancedAsyncACC eaacc,
                           final AbstractTucsonAction a,
                           final TucsonOperationCompletionListener l) {
        this.action = a;
        this.listener = l;
        this.acc = eaacc;
    }

    /**
     * Builds the wrapper to a TuCSoN operation.
     *
     * @param eaacc   the ACC through which the operation should be executed
     * @param a       the TuCSoN operation to execute
     * @param l       the TuCSoN listener handling operation completion
     * @param timeout the maximum waiting time for operation completion (not used
     *                atm)
     */
    public TucsonOpWrapper(final EnhancedAsyncACC eaacc,
                           final AbstractTucsonAction a,
                           final TucsonOperationCompletionListener l, final long timeout) {
        this.action = a;
        this.listener = l;
        this.acc = eaacc;
    }

    /**
     * Executes the operation.
     *
     * @return TucsonOperation the TuCSoN operation executed
     * @throws TucsonOperationNotPossibleException if the requested operation cannot be performed
     * @throws UnreachableNodeException            if the TuCSoN node target of the operation is not
     *                                             network-reachable
     */
    public final void execute()
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException {
        this.action.executeAsynch(this.acc, this.listener);
    }

    /**
     * Gets the TuCSoN action to execute
     *
     * @return the TuCSoN action to execute
     */
    public final AbstractTucsonAction getAction() {
        return this.action;
    }

    /**
     * Gets the TuCSoN operation to execute
     *
     * @return the TuCSoN operation to execute
     */
    public AbstractTupleCentreOperation getOp() {
        return this.op;
    }

    /**
     * Checks whether this operation has been removed from the list of pending
     * operations
     *
     * @return {@code true} or {@code false} depending on whether this operation
     * has been removed from the list of pending operations
     */
    public boolean hasBeenRemoved() {
        return this.hasBeenRemoved;
    }

    /**
     * Checks whether this operation has been removed from the list of pending
     * operations
     *
     * @param removed whether this operation has been removed from the list of
     *                pending operations
     */
    public void setHasBeenRemoved(final boolean removed) {
        this.hasBeenRemoved = removed;
    }

    /**
     * Sets the TuCSoN operation to perform
     *
     * @param a the TuCSoN operation to perform
     */
    public void setOp(final AbstractTupleCentreOperation a) {
        this.op = a;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return this.action.toString();
    }

}
