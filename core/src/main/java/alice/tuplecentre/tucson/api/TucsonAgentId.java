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
package alice.tuplecentre.tucson.api;

import java.io.Serializable;
import java.util.UUID;

import alice.tuplecentre.respect.api.AgentId;
import alice.tuplecentre.respect.api.exceptions.InvalidAgentIdException;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidAgentIdException;

/**
 *
 * @author Alessandro Ricci
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 *
 */
public class TucsonAgentId implements alice.tuplecentre.api.AgentId,
        Serializable {

    private static final long serialVersionUID = -5788843633820003843L;

    private static String dropMinus(final UUID uuid) {
        final String uuids = uuid.toString();
        String res = "";
        int j = 0;
        for (int i = 0; i < uuids.length(); i++) {
            if (uuids.charAt(i) == '-') {
                res = res.concat(uuids.substring(j, i));
                j = i + 1;
            }
        }
        res = res.concat(uuids.substring(j, uuids.length()));
        return res;
    }

    private AgentId aid;
    private UUID uuid;

    /**
     *
     * @param id
     *            the String representation of this TuCSoN agent identifier
     * @throws TucsonInvalidAgentIdException
     *             if the given String does not represent a valid TuCSoN
     *             identifier
     */
    public TucsonAgentId(final String id) throws TucsonInvalidAgentIdException {
        try {
            this.aid = new AgentId(id);
            this.uuid = null;
        } catch (final InvalidAgentIdException e) {
            throw new TucsonInvalidAgentIdException();
        }
    }

    /**
     *
     * @param name
     *            the String representation of this TuCSoN agent identifier
     * @param tcId
     *            the identifier of the tuple centre the agent behind this
     *            identifier represents
     */
    public TucsonAgentId(final String name, final TucsonTupleCentreId tcId) {
        this.aid = new AgentId(name, tcId);
    }

    /**
     *
     * @return wether a UUID has been succesfully assigned to this agent
     *         identifier
     */
    public boolean assignUUID() {
        if (this.uuid == null) {
            this.uuid = UUID.randomUUID();
            try {
                this.aid = new AgentId(this.aid + ":uuid"
                        + TucsonAgentId.dropMinus(this.uuid));
            } catch (final InvalidAgentIdException e) {
                // Cannot happen because it's specified here
                e.printStackTrace();
            }
        }
        return true;
    }

    /**
     *
     * @return the local agent identifier part of the full TuCSoN agent
     *         identifier
     */
    public AgentId getAgentId() {
        return this.aid;
    }

    /**
     *
     * @return the local name given to the agent by the programmer
     */
    public String getAgentName() {
        return this.aid.getLocalName();
    }

    /**
     *
     * @return the UUID assigned to the agent identifier by TuCSoN to globally,
     *         univocally identify agents
     */
    public String getAgentUUID() {
        return this.uuid.toString();
    }

    @Override
    public boolean isAgent() {
        return true;
    }

    @Override
    public boolean isEnv() {
        return false;
    }
    
    @Override
    public boolean isGeo() {
        return false;
    }

    @Override
    public boolean isTC() {
        return false;
    }

    @Override
    public String toString() {
        return this.aid.toString();
    }
}
