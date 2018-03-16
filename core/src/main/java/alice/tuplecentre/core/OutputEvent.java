/*
 * Tuple Centre media - Copyright (C) 2001-2002 aliCE team at deis.unibo.it This
 * library is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version. This library is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser
 * General Public License for more details. You should have received a copy of
 * the GNU Lesser General Public License along with this library; if not, write
 * to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
 * 02111-1307 USA
 */
package alice.tuplecentre.core;

/**
 * Represents output events of the tuple centre virtual machine (stores the
 * "connected" InputEvent)
 *
 * @author Alessandro Ricci
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 * @author (contributor) Michele Bombardi (mailto:
 *         michele.bombardi@studio.unibo.it)
 */
public class OutputEvent extends AbstractEvent {

    private static final long serialVersionUID = -5521129200850527503L;
    private boolean isLnk;
    /** the input events this output events is consequence of */
    protected final InputEvent inputEvent;

    /**
     *
     * @param ev
     *            the input events this output events refers to
     */
    public OutputEvent(final InputEvent ev) {
        super(ev.getSource(), ev.getSimpleTCEvent(), ev.getReactingTC(), ev
                .getTime(), ev.getPosition());
        this.inputEvent = ev;
        this.isLnk = ev.isLinking();
    }

    /**
     *
     * @return the input events this output events refers to
     */
    public InputEvent getInputEvent() {
        return this.inputEvent;
    }

    @Override
    public boolean isInput() {
        return false;
    }

    @Override
    public boolean isInternal() {
        return false;
    }

    /**
     *
     * @return wether this events is a linking events
     */
    public boolean isLinking() {
        return this.isLnk;
    }

    @Override
    public boolean isOutput() {
        return true;
    }

    /**
     *
     * @param flag
     *            wether this events is a linking events
     */
    public void setIsLinking(final boolean flag) {
        this.isLnk = flag;
    }

    @Override
    public String toString() {
        return "[ op: "
                + (this.getSimpleTCEvent().isResultDefined() ? this
                        .getSimpleTCEvent().getTupleResult() : this
                        .getSimpleTCEvent()) + ", ie: [ "
                + this.inputEvent.toString() + " ] ]";
    }
}
