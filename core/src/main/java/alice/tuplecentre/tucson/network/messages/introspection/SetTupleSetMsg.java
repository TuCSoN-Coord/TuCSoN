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
package alice.tuplecentre.tucson.network.messages.introspection;


import alice.tuple.Tuple;
import alice.tuplecentre.tucson.api.TucsonAgentId;

/**
 * @author Unknown...
 */
public class SetTupleSetMsg extends AbstractNodeMessage {

    private static final long serialVersionUID = 3683932175338169242L;
    private java.util.List<? extends Tuple> tupleSet;

    /**
     * @param id the agent id of the sender
     * @param ts the list of tuples to overwrite the tuple set with
     */
    public SetTupleSetMsg(final TucsonAgentId id,
                          final java.util.List<? extends Tuple> ts) {
        super(id, "setTupleSet");
        this.tupleSet = ts;
    }

    /**
     * @return the tupleSet
     */
    public java.util.List<? extends Tuple> getTupleSet() {
        return this.tupleSet;
    }

    /**
     * @param set the tupleSet to set
     */
    public void setTupleSet(final java.util.List<? extends Tuple> set) {
        this.tupleSet = set;
    }
}
