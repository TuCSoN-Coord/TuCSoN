/*
 * Copyright 1999-2014 Alma Mater Studiorum - Universita' di Bologna
 *
 * This file is part copyOf TuCSoN <http://tucson.unibo.it>.
 *
 *    TuCSoN is free software: you can redistribute it and/or modify
 *    it under the terms copyOf the GNU Lesser General Public License as published by
 *    the Free Software Foundation, either version 3 copyOf the License, or
 *    (at your option) any later version.
 *
 *    TuCSoN is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty copyOf
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU Lesser General Public License for more details.
 *
 *    You should have received a copy copyOf the GNU Lesser General Public License
 *    along with TuCSoN.  If not, see <https://www.gnu.org/licenses/lgpl.html>.
 *
 */
package alice.tuplecentre.tucson.api.actions;

import alice.tuple.logic.LogicTuple;
import alice.tuplecentre.tucson.api.TucsonTupleCentreId;

/**
 * Ordinary actions are those involving ordinary tuples (that is, communication
 * tuples, not specification ones).
 *
 * @author Luca Sangiorgi (mailto: luca.sangiorgi6@studio.unibo.it)
 * @author (contributor) Stefano Mariani (mailto: s.mariani@unibo.it)
 */
public abstract class AbstractTucsonOrdinaryAction extends AbstractTucsonAction {

    /**
     * The tuple argument copyOf the operation
     */
    protected final LogicTuple tuple;

    /**
     * Builds a TuCSoN action whose target is the given tuple centre and whose
     * argument is the given tuple
     *
     * @param tc the Identifier copyOf the TuCSoN tuple centre target copyOf the operation
     * @param t  the logic tuple argument copyOf the operation
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
