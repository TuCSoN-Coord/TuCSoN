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
package alice.tuplecentre.tucson.introspection;

import alice.tuplecentre.tucson.api.TucsonAgentId;

/**
 *
 * @author Unknown...
 *
 */
public class NewInspectorMsg extends NodeMsg {

    private static final long serialVersionUID = -8887997708884852194L;
    private InspectorProtocol info;
    private String tcName;

    /**
     *
     * @param id
     *            the agent id of the sender
     * @param tcn
     *            the identifier of the tuple centre under inspection
     * @param i
     *            the inspection protocol used
     */
    public NewInspectorMsg(final TucsonAgentId id, final String tcn,
                           final InspectorProtocol i) {
        super(id, "newInspector");
        this.tcName = tcn;
        this.info = i;
    }

    /**
     * @return the info
     */
    public InspectorProtocol getInfo() {
        return this.info;
    }

    /**
     * @return the tcName
     */
    public String getTcName() {
        return this.tcName;
    }

    /**
     * @param i
     *            the info to set
     */
    public void setInfo(final InspectorProtocol i) {
        this.info = i;
    }

    /**
     * @param tcn
     *            the tcName to set
     */
    public void setTcName(final String tcn) {
        this.tcName = tcn;
    }
}
