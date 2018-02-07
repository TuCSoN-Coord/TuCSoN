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

import java.util.EventObject;

/**
 * Represents observable (by inspectors) events happening inside tuple centre
 * virtual machine
 *
 * @author Alessandro Ricci
 *
 */
public class InspectableEvent extends EventObject {

    /**
     * 
     */
    public static final int TYPE_IDLESTATE = 2;
    /**
     *
     */
    public static final int TYPE_NEWSTATE = 1;
    private static final long serialVersionUID = 5564085406606810969L;
    /** virtual machine time at which the event has been observed */
    private final long time;
    /** observable event type */
    private final int type;

    /**
     *
     * @param src
     *            the source of the event
     * @param t
     *            the type code of the inspectable event
     */
    public InspectableEvent(final Object src, final int t) {
        super(src);
        this.time = System.currentTimeMillis();
        this.type = t;
    }

    /**
     * Gets the time at which the event has been observed inside the tuple
     * centre VM
     *
     * @return the time at which this inspectable event occurred
     */
    public long getTime() {
        return this.time;
    }

    /**
     * Gets observable event type
     *
     * @return the type code of this inspectable event
     */
    public int getType() {
        return this.type;
    }
}
