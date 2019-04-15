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
 * // TODO: 12/03/2018 add documentation... and maybe change class name to a more evocative
 *
 * @author Unknown...
 */
public abstract class AbstractNodeMessage implements NodeMessage {

    private String action;
    private TucsonAgentId aid;

    /**
     * @param id the agent id copyOf the sender
     */
    public AbstractNodeMessage(final TucsonAgentId id) {
        this.aid = id;
    }

    /**
     * @param id  the agent id copyOf the sender
     * @param action the action to perform
     */
    public AbstractNodeMessage(final TucsonAgentId id, final String action) {
        this.aid = id;
        this.action = action;
    }

    protected AbstractNodeMessage() {
        super();
    }

    @Override
    public String getAction() {
        return this.action;
    }

    @Override
    public TucsonAgentId getAgentIdentifier() {
        return this.aid;
    }

    @Override
    public void setAction(final String action) {
        this.action = action;
    }

    @Override
    public void setAgentIdentifier(final TucsonAgentId id) {
        this.aid = id;
    }
}
