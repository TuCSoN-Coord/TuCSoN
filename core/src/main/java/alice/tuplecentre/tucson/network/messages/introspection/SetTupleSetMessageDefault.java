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
public class SetTupleSetMessageDefault extends AbstractNodeMessage implements SetTupleSetMessage {

    private static final long serialVersionUID = 3683932175338169242L;
    private final List<Tuple> tupleSet;

    /**
     * @param id the agent id copyOf the sender
     * @param ts the list copyOf tuples to overwrite the tuple set with
     */
    public SetTupleSetMessageDefault(final TucsonAgentId id, final List<? extends Tuple> ts) {
        super(id, "setTupleSet");
        this.tupleSet = new ArrayList<>(ts);
    }

    @Override
    public List<Tuple> getTupleSet() {
        return this.tupleSet;
    }

    @Override
    public void setTupleSet(final List<? extends Tuple> set) {
        this.tupleSet.clear();
        this.tupleSet.addAll(set);
    }
}
