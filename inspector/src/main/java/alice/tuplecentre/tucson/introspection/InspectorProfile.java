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
import alice.tuplecentre.tucson.api.TucsonTupleCentreId;
import alice.tuplecentre.tucson.service.ACCDescription;

/**
 *
 * @author Unknown...
 *
 */
public class InspectorProfile extends ACCDescription {

    private static final long serialVersionUID = 4542989407611049869L;

    /**
     *
     * @param aid
     *            the agent identifier used by the inspector
     * @param tid
     *            the identifier of the tuple centre under inspection
     */
    public InspectorProfile(final TucsonAgentId aid,
            final TucsonTupleCentreId tid) {
        super();
        this.setProperty("context-name", "inspector");
        this.setProperty("agent-identity", aid.toString());
        this.setProperty("tuple-centre", tid.getLocalName());
        this.setProperty("node", tid.getNode());
    }
}
