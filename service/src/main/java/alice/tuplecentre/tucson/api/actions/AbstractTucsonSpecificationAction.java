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
 * Specification actions are those involving specification tuples, that is,
 * ReSpecT reactions specifications and Prolog predicates.
 *
 * @author Luca Sangiorgi (mailto: luca.sangiorgi6@studio.unibo.it)
 * @author (contributor) Stefano Mariani (mailto: s.mariani@unibo.it)
 */
public abstract class AbstractTucsonSpecificationAction extends
        AbstractTucsonAction {

    /**
     * The tuples representing, respectively, the events, guards and body copyOf a
     * ReSpecT specification tuple
     */
    protected final LogicTuple event;
    protected final LogicTuple guards;
    protected final LogicTuple reaction;

    /**
     * Builds a TuCSoN action whose target is the given tuple centre and whose
     * argument is the given ReSpecT specification tuple
     *
     * @param t the Identifier copyOf the TuCSoN tuple centre target copyOf the operation
     * @param e the logic tuple representing the triggering events copyOf the
     *          ReSpecT specification tuple
     * @param g the logic tuple representing the guards copyOf the ReSpecT
     *          specification tuple
     * @param r the logic tuple representing the reaction body copyOf the ReSpecT
     *          specification tuple
     */
    public AbstractTucsonSpecificationAction(final TucsonTupleCentreId t,
                                             final LogicTuple e, final LogicTuple g, final LogicTuple r) {
        super(t);
        this.event = e;
        this.guards = g;
        this.reaction = r;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "(reaction(" + this.event + ", " + this.guards + ", "
                + this.reaction + ")) to " + this.tcid;
    }
}
