/*
 * ReSpecT - Copyright (C) aliCE team at deis.unibo.it This library is free
 * software; you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package alice.respect.api;

import java.io.Serializable;

import alice.respect.api.exceptions.InvalidAgentIdException;
import alice.respect.core.AgentIdOperatorManager;
import alice.tucson.api.TucsonTupleCentreIdDefault;
import alice.tuplecentre.api.AgentIdentifier;
import alice.tuprolog.InvalidTermException;
import alice.tuprolog.Struct;
import alice.tuprolog.Term;

/**
 * Agent identifier.
 *
 * @author Alessandro Ricci
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 */
public class AgentId implements AgentIdentifier, Serializable {

    private Term id;

    private static AgentIdOperatorManager opManager = new AgentIdOperatorManager();

    private static final long serialVersionUID = 1L;

    /**
     * Constructs an agent identifier
     * <p>
     * The Agent identifier must be a ground valid logic term
     *
     * @param sid is the string representation of the identifier
     * @throws InvalidAgentIdException if it is not a valid identifier
     */
    public AgentId(final String sid) throws InvalidAgentIdException {
        String newSid;
        if (sid.indexOf(':') != -1) {
            newSid = sid.substring(0, sid.indexOf(':'));
        } else {
            newSid = sid;
        }
        try {
            this.id = Term.createTerm(newSid, AgentId.opManager);
        } catch (final InvalidTermException e) {
            e.printStackTrace();
            throw new InvalidAgentIdException("The identifier '" + newSid
                    + "' is not a valid term");
        }
        if (!this.id.isGround()) {
            throw new InvalidAgentIdException("The identifier '"
                    + this.id.toString() + "' is not ground");
        }
    }

    /**
     * @param name the string representation of this identifier
     * @param tcId the tuple centre identifier this agent operates on
     */
    public AgentId(final String name, final TucsonTupleCentreIdDefault tcId) {
        this.id = new Struct(name, tcId.toTerm());
    }

    /**
     * Constructs an agent identifier
     * <p>
     * The Agent identifier must be a ground logic term
     *
     * @param tid the identifier as tuProlog term
     * @throws InvalidAgentIdException if it is not a valid identifier
     */
    public AgentId(final Term tid) throws InvalidAgentIdException {
        this.id = tid.getTerm();
        if (!this.id.isGround()) {
            throw new InvalidAgentIdException("The identifier '"
                    + this.id.toString() + "' is not ground");
        }
    }

    @Override
    public String getLocalName() {
        if (this.id.isCompound()) {
            return ((Struct) this.id).getArg(0).toString();
        }
        return this.id.toString();
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
    public Term toTerm() {
        return id;
    }

    @Override
    public String toString() {
        return this.id.toString();
    }
}
