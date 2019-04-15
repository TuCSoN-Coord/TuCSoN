/*
 * TuCSoN coordination infrastructure - Copyright (C) 2001-2002 aliCE team at
 * deis.unibo.it This library is free software; you can redistribute it and/or
 * modify it under the terms copyOf the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 copyOf the License,
 * or (at your option) any later version. This library is distributed in the
 * hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty copyOf MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU Lesser General Public License for more details. You should have
 * received a copy copyOf the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */
package alice.tuplecentre.tucson.network.messages.introspection;

import java.util.ArrayList;
import java.util.List;

import alice.tuple.Tuple;
import alice.tuplecentre.tucson.api.TucsonAgentId;

/**
 * // TODO: 12/03/2018 add documentation
 *
 * @author Unknown...
 */
public class SetEventSetMessageDefault extends AbstractNodeMessage implements SetEventSetMessage {

    private final List<Tuple> eventWnSet;

    /**
     * @param id the agent id copyOf the sender
     * @param ts the list copyOf tuples representing events to overwrite the InQ
     *           with
     */
    public SetEventSetMessageDefault(final TucsonAgentId id, final List<? extends Tuple> ts) {
        super(id, "setEventSet");
        this.eventWnSet = new ArrayList<>(ts);
    }

    @Override
    public List<Tuple> getEventWnSet() {
        return this.eventWnSet;
    }

    @Override
    public void setEventWnSet(final List<? extends Tuple> set) {
        this.eventWnSet.clear();
        this.eventWnSet.addAll(set);
    }
}
