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


import alice.tuplecentre.tucson.api.TucsonAgentId;

/**
 * @author Unknown...
 */
public class GetSnapshotMsg extends AbstractNodeMessage {

    /**
     *
     */
    public static final byte TSET = 1;
    /**
     *
     */
    public static final byte WSET = 2;
    private static final long serialVersionUID = -7715943663646624722L;
    private byte what;

    /**
     * @param id the agent identifier
     * @param w  the set to retrieve
     */
    public GetSnapshotMsg(final TucsonAgentId id, final byte w) {
        super(id, "getSnapshot");
        this.what = w;
    }

    /**
     * @return the what
     */
    public byte getWhat() {
        return this.what;
    }

    /**
     * @param w the what to set
     */
    public void setWhat(final byte w) {
        this.what = w;
    }
}
