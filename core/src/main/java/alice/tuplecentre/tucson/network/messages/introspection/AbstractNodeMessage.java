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
import alice.tuplecentre.tucson.network.messages.Message;


/**
 * // TODO: 12/03/2018 add documentation... and maybe change class name
 *
 * @author Unknown...
 */
public abstract class AbstractNodeMessage implements Message {

    private static final long serialVersionUID = -3499870079832457223L;
    private String action;
    private TucsonAgentId aid;

    /**
     * @param id the agent id of the sender
     */
    public AbstractNodeMessage(final TucsonAgentId id) {
        this.aid = id;
    }

    /**
     * @param id  the agent id of the sender
     * @param act the action to perform
     */
    public AbstractNodeMessage(final TucsonAgentId id, final String act) {
        this.aid = id;
        this.action = act;
    }

    /**
     *
     */
    protected AbstractNodeMessage() {
        super();
    }

    /**
     * @return the action
     */
    public String getAction() {
        return this.action;
    }

    /**
     * @return the aid
     */
    public TucsonAgentId getAid() {
        return this.aid;
    }

    /**
     * @param a the action to set
     */
    public void setAction(final String a) {
        this.action = a;
    }

    /**
     * @param id the aid to set
     */
    public void setAid(final TucsonAgentId id) {
        this.aid = id;
    }
}
