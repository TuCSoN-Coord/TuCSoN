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
package alice.tucson.introspection;

import alice.tucson.api.TucsonAgentId;

/**
 * 
 * @author Unknown...
 * 
 */
public class SetProtocolMsg extends NodeMsg {
    private static final long serialVersionUID = 7284025970406889712L;
    private InspectorProtocol info;

    /**
     * 
     * @param id
     *            the agent id of the sender
     * @param p
     *            the inspection protocol to be used
     */
    public SetProtocolMsg(final TucsonAgentId id, final InspectorProtocol p) {
        super(id, "setProtocol");
        this.info = p;
    }

    /**
     * @return the info
     */
    public InspectorProtocol getInfo() {
        return this.info;
    }

    /**
     * @param i
     *            the info to set
     */
    public void setInfo(final InspectorProtocol i) {
        this.info = i;
    }
}
