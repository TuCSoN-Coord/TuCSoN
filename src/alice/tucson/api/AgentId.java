/*
 * TuCSoN coordination infrastructure - Copyright (C) 2001-2002  aliCE team at deis.unibo.it
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package alice.tucson.api;

import alice.tuprolog.Term;

/**
 * Defines the identifier of an agent interacting with the TuCSoN
 * infrastructure
 *
 */
public class AgentId extends alice.respect.AgentId {

    private static AgentIdOperatorManager opManager = new AgentIdOperatorManager();

    public AgentId(){
    }

    /**
     * Constructs an agent identifier
     *
     * The Agent identifier must be a ground valid logic term
     *
     * @param sid is the string representation of the identifier
     * @throws alice.tucson.api.InvalidAgentIdException if it is not a valid identifier
     */
    public AgentId(String sid) throws InvalidAgentIdException {
        try{
            id=Term.parse(sid,opManager);
        } catch (Exception ex){
            throw new InvalidAgentIdException();
        }
        if (!id.isGround()){
            throw new InvalidAgentIdException();
        }
    }

    /**
     * Constructs an agent identifier
     *
     * The Agent identifier must be a ground logic term
     *
     * @param tid the identifier as tuProlog term
     * @throws InvalidAgentIdException if it is not a valid identifier
     */
    public AgentId(Term tid) throws InvalidAgentIdException {
        try{
            id=tid.getTerm();
        } catch (Exception ex){
            throw new InvalidAgentIdException();
        }
        if (!id.isGround()){
            throw new InvalidAgentIdException();
        }
    }
}
