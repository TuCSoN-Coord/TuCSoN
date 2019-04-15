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


import alice.tuplecentre.tucson.api.TucsonAgentId;

/**
 * // TODO: 12/03/2018 add documentation
 *
 * @author Unknown...
 */
public class GetSnapshotMessageDefault extends AbstractNodeMessage implements GetSnapshotMessage {

    private SetType what;

    /**
     * @param id      the agent identifier
     * @param setType the set to retrieve
     */
    public GetSnapshotMessageDefault(final TucsonAgentId id, final SetType setType) {
        super(id, "getSnapshot");
        this.what = setType;
    }

    @Override
    public SetType getWhat() {
        return this.what;
    }

    @Override
    public void setWhat(final SetType setType) {
        this.what = setType;
    }
}
