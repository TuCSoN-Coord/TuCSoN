/*
 * ReSpecT - Copyright (C) aliCE team at deis.unibo.it
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
package alice.respect.api;

import alice.respect.api.exceptions.InvalidAgentIdException;
import alice.respect.core.AgentIdOperatorManager;
import alice.tuprolog.*;

/**
 * Agent identifier.
 * 
 * @author aricci
 */
public class AgentId implements alice.tuplecentre.api.AgentId, java.io.Serializable {

    private static AgentIdOperatorManager opManager = new AgentIdOperatorManager();
    protected Term id;
    private String localName;

    /**
     * Constructs an agent identifier
     *
     * The Agent identifier must be a ground valid logic term
     *
     * @param sid is the string representation of the identifier
     * @throws InvalidAgentIdException if it is not a valid identifier
     */
    public AgentId(String sid) throws InvalidAgentIdException {
    	localName = sid;
    	if(sid.indexOf(":")!=-1)
    		localName = sid.substring(0, sid.indexOf(":"));
        try{
            id=Term.createTerm(sid,opManager);
        } catch (InvalidTermException e){
        	e.printStackTrace();
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
    
    public String getLocalName(){
    	if(id.isCompound())
    		return ((Struct)id).getArg(0).toString();
    	else
    		return id.toString();
//    	return localName;
    }

    /**
     * Provides the logic term representation of the identifier
     *
     * @return the term representing the identifier
     */
    public Term toTerm(){
        return id;
    }

    public String toString(){
        return id.toString();
    }

    @Override
	public boolean isAgent() {
		return true;
	}

    @Override
	public boolean isTC() {
		return false;
	}
	
    @Override
	public boolean isEnv(){
		return false;
	}
	
}

