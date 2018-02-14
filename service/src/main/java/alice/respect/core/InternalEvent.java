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
package alice.respect.core;

import alice.tuplecentre.core.InputEvent;

/**
 * Represents an internal event of the ReSpecT VM (stores the "connected"
 * InputEvent)
 *
 * @author Alessandro Ricci
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 * @author (contributor) Michele Bombardi (mailto:
 *         michele.bombardi@studio.unibo.it)
 */
public class InternalEvent extends alice.tuplecentre.core.AbstractEvent {

    private static final long serialVersionUID = 8362450931717138730L;
    private final InputEvent inputEvent;
    private final InternalOperation internalOperation;

    /**
     *
     * @param ev
     *            the input event direct cause of this internal event
     * @param op
     *            the ReSpecT operation generating this internal event
     */
    public InternalEvent(final InputEvent ev, final InternalOperation op) {
        super(ev.getSource(), ev.getSimpleTCEvent(), ev.getReactingTC(), ev
                .getTime(), ev.getPosition());
        this.inputEvent = ev;
        this.internalOperation = op;
    }

    /**
     *
     * @return the input event direct cause of this internal event
     */
    public InputEvent getInputEvent() {
        return this.inputEvent;
    }

    /**
     *
     * @return the ReSpecT operation generating this internal event
     */
    public InternalOperation getInternalOperation() {
        return this.internalOperation;
    }

    @Override
    public boolean isInput() {
        return false;
    }

    @Override
    public boolean isInternal() {
        return true;
    }

    @Override
    public boolean isOutput() {
        return false;
    }

    @Override
    public String toString() {
        return "[ op: " + this.internalOperation + ", ie: [ "
                + this.inputEvent.toString() + " ] ]";
    }
}
