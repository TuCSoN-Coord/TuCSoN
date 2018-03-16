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

import alice.tuplecentre.tucson.api.actions.AbstractTucsonAction;

/**
 * {@link SearchableOpsQueue} specialised for
 * handling completed operations.
 *
 * @author Fabio Consalici, Riccardo Drudi
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 */
public class CompletedOpsQueue extends SearchableOpsQueue {

    private static final long serialVersionUID = 1L;

    /**
     * Gets all successfully completed operations
     *
     * @return the queue of successfully completed operations
     */
    public CompletedOpsQueue getSuccessfulOps() {
        final CompletedOpsQueue successful = new CompletedOpsQueue();
        for (TucsonOpWrapper tow : this) {
            if (tow.getOp().isResultSuccess()) {
                successful.add(tow);
            }
        }
        return successful;
    }

    @Override
    public CompletedOpsQueue getMatchingOps(
            final Class<? extends AbstractTucsonAction> optype) {
        final CompletedOpsQueue matching = new CompletedOpsQueue();
        for (TucsonOpWrapper tow : this) {
            if (tow.getAction().getClass().equals(optype)) {
                matching.add(tow);
            }
        }
        return matching;
    }

    /**
     * Gets all failed operations
     *
     * @return the queue of failed operations
     */
    public CompletedOpsQueue getFailedOps() {
        final CompletedOpsQueue failed = new CompletedOpsQueue();
        for (TucsonOpWrapper tow : this) {
            if (!tow.getOp().isResultSuccess()) {
                failed.add(tow);
            }
        }
        return failed;
    }

    /**
     * Removes all successfully completed operations
     */
    public void removeSuccessfulOps() {
        for (TucsonOpWrapper tow : this) {
            if (tow.getOp().isResultSuccess()) {
                this.remove(tow);
            }
        }
    }

    /**
     * Removes all failed operations
     */
    public void removeFailedOps() {
        for (TucsonOpWrapper tow : this) {
            if (!tow.getOp().isResultSuccess()) {
                this.remove(tow);
            }
        }
    }

}
