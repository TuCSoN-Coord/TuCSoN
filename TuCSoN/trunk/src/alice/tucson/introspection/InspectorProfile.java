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
import alice.tucson.api.TucsonTupleCentreId;
import alice.tucson.service.ACCDescription;

public class InspectorProfile extends ACCDescription {

    private static final long serialVersionUID = 4542989407611049869L;

    public InspectorProfile(final TucsonAgentId aid,
            final TucsonTupleCentreId tid) {
        this.setProperty("context-name", "inspector");
        this.setProperty("agent-identity", aid.toString());
        this.setProperty("tuple-centre", tid.getName());
        this.setProperty("node", tid.getNode());
    }

}
