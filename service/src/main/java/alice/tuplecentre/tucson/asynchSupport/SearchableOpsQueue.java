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

import java.util.concurrent.LinkedBlockingQueue;

import alice.tuplecentre.tucson.api.actions.AbstractTucsonAction;

/**
 * Queue storing pending operations delegated to
 * {@link alice.tuplecentre.tucson.asynchSupport.AsynchOpsHelper}.
 *
 * @author Fabio Consalici, Riccardo Drudi
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 *
 */
public class SearchableOpsQueue extends LinkedBlockingQueue<TucsonOpWrapper> {

    private static final long serialVersionUID = 1L;

    /**
     * Gets all the operations whose type matches the given type
     * 
     * @param optype
     *            the operation type to look for
     *
     * @return the queue of operations of the same input type
     */
    public SearchableOpsQueue getMatchingOps(
            final Class<? extends AbstractTucsonAction> optype) {
        final SearchableOpsQueue matching = new SearchableOpsQueue();
        for (TucsonOpWrapper tow : this) {
            if (tow.getAction().getClass().equals(optype)) {
                matching.add(tow);
            }
        }
        return matching;
    }

    /**
     * Removes the specific action from the queue
     *
     * @param action
     *            the TuCSoN action to be removed
     * @return {@code true} or {@code false} depending on whether removal was
     *         successful or not
     */
    public boolean removeOp(final AbstractTucsonAction action) {
        for (TucsonOpWrapper tow : this) {
            if (tow.getAction().equals(action)) {
                tow.setHasBeenRemoved(true);
                return this.remove(tow);
            }
        }
        return false;
    }

    /**
     * Removse all the operations whose type matches the given type
     *
     * @param optype
     *            the operation type whose operations should be removed
     */
    public void removeMatchingOps(final Class<?> optype) {
        for (TucsonOpWrapper tow : this) {
            if (tow.getAction().getClass().equals(optype)) {
                tow.setHasBeenRemoved(true);
                this.remove(tow);
            }
        }
    }

    /**
     * Removes the operation identified by the given Identifier
     *
     * @param id
     *            the Identifier of the operation to remove
     * @return {@code true} or {@code false} depending on whether removal was
     *         successful or not
     */
    public boolean removeOpById(final long id) {
        for (TucsonOpWrapper tow : this) {
            if (tow.getOp().getId() == id) {
                tow.setHasBeenRemoved(true);
                return this.remove(tow);
            }
        }
        return false;
    }

}
