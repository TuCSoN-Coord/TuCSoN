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
 * Represents a reaction which has been triggered inside a tuple centre by a
 * specific event
 * 
 * @see Reaction
 * @see Event
 * 
 * @author aricci
 */
public class TriggeredReaction implements java.io.Serializable {

    private static final long serialVersionUID = -2906772411975529805L;
    /** the event triggering the reaction */
    private final Event event;
    /** the reaction triggered */
    private final Reaction reaction;

    public TriggeredReaction(final Event ev, final Reaction re) {
        this.event = ev;
        this.reaction = re;
    }

    public Event getEvent() {
        return this.event;
    }

    public Reaction getReaction() {
        return this.reaction;
    }

}
