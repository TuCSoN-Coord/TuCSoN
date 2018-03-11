/*
 * TuCSoN coordination infrastructure - Copyright (C) 2001-2002 aliCE team at
 * deis.unibo.it This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of the License,
 * or (at your option) any later version. This library is distributed in the
 * hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU Lesser General Public License for more details. You should have
 * received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */
package alice.tuplecentre.tucson.network.messages.inspection;

import java.util.List;

import alice.tuplecentre.api.Tuple;
import alice.tuplecentre.tucson.api.TucsonAgentId;

/**
 *
 * @author Unknown...
 *
 */
public class SetEventSetMsg extends NodeMsg {

    private static final long serialVersionUID = -3946179149619833984L;
    private java.util.List<? extends Tuple> eventWnSet;

    /**
     *
     * @param id
     *            the agent id of the sender
     * @param ts
     *            the list of tuples representing events to overwrite the InQ
     *            with
     */
    public SetEventSetMsg(final TucsonAgentId id, final List<? extends Tuple> ts) {
        super(id, "setEventSet");
        this.eventWnSet = ts;
    }

    /**
     * @return the eventWnSet
     */
    public java.util.List<? extends Tuple> getEventWnSet() {
        return this.eventWnSet;
    }

    /**
     * @param set
     *            the eventWnSet to set
     */
    public void setEventWnSet(final java.util.List<? extends Tuple> set) {
        this.eventWnSet = set;
    }
}
