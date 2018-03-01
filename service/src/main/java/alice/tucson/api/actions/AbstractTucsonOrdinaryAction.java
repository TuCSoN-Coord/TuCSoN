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
package alice.tucson.api.actions;

import alice.logictuple.LogicTuple;
import alice.tucson.api.TucsonTupleCentreId;

/**
 * Ordinary actions are those involving ordinary tuples (that is, communication
 * tuples, not specification ones).
 *
 * @author Luca Sangiorgi (mailto: luca.sangiorgi6@studio.unibo.it)
 * @author (contributor) Stefano Mariani (mailto: s.mariani@unibo.it)
 *
 */
public abstract class AbstractTucsonOrdinaryAction extends AbstractTucsonAction {

    /**
     * The tuple argument of the operation
     */
    protected LogicTuple tuple;

    /**
     * Builds a TuCSoN action whose target is the given tuple centre and whose
     * argument is the given tuple
     * 
     * @param tc
     *            the Identifier of the TuCSoN tuple centre target of the operation
     * @param t
     *            the logic tuple argument of the operation
     */
    public AbstractTucsonOrdinaryAction(final TucsonTupleCentreId tc,
            final LogicTuple t) {
        super(tc);
        this.tuple = t;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "(" + this.tuple + ") to " + this.tcid;
    }
}
