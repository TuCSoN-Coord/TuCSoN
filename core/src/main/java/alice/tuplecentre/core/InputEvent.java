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

import java.util.Map;

import alice.respect.api.geolocation.Position;
import alice.tuplecentre.api.EmitterIdentifier;
import alice.tuplecentre.api.TupleCentreId;

/**
 * Represents input events of the tuple centre virtual machine (only difference
 * w.r.t. Event is the <code>isLinking</code> field)
 *
 * @author Alessandro Ricci
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 * @author (contributor) Michele Bombardi (mailto:
 *         michele.bombardi@studio.unibo.it)
 */
public class InputEvent extends AbstractEvent {

    private static final long serialVersionUID = -6321543805920861915L;
    private boolean isLnk;

    /**
     *
     * @param source
     *            the identifier of the source of this event
     * @param op
     *            the operation which caused this event
     * @param tc
     *            the tuple centre target of this event
     * @param time
     *            the time at which this event occurred
      * @param place
     *            the place (wichever sort of) where the event was generated
     */
    public InputEvent(final EmitterIdentifier source, final AbstractTupleCentreOperation op,
                      final TupleCentreId tc, final long time, final Position place) {
        super(source, op, tc, time, place);
        this.isLnk = false;
    }

    /**
     *
     * @param source
     *            the identifier of the source of this event
     * @param op
     *            the operation which caused this event
     * @param tc
     *            the tuple centre target of this event
     * @param time
     *            the time at which this event occurred
     * @param prop
     *            some properties related to the event
     * @param place
     *            the place (wichever sort of) where the event was generated
     */
    public InputEvent(final EmitterIdentifier source, final AbstractTupleCentreOperation op,
                      final TupleCentreId tc, final long time, final Position place,
                      final Map<String, String> prop) {
        super(source, op, tc, time, place, prop);
        this.isLnk = false;
    }

    @Override
    public boolean isInput() {
        return true;
    }

    @Override
    public boolean isInternal() {
        return false;
    }

    /**
     *
     * @return wether this event is a linking event
     */
    public boolean isLinking() {
        return this.isLnk;
    }

    @Override
    public boolean isOutput() {
        return false;
    }

    /**
     *
     * @param flag
     *            wether this event is a linking event
     */
    public void setIsLinking(final boolean flag) {
        this.isLnk = flag;
    }

    @Override
    public String toString() {
        return "[ src: " + this.getSource() + ", " + "op: "
                + this.getSimpleTCEvent() + ", " + "trg: " + this.getTarget()
                + ", " + "tc: " + this.getReactingTC() + " ]";
    }
}
