/*
 * AgentId.java
 *
 * Copyright 2000-2001-2002  aliCE team at deis.unibo.it
 *
 * This software is the proprietary information of deis.unibo.it
 * Use is subject to license terms.
 *
 */
package alice.respect;
import alice.tuprolog.*;
/**
 * agent identifier for logic tuple centres, described in term
 * of string name.  As logic tuple, it is represented by the
 * name itself.
 */
public class AgentId implements alice.tuplecentre.AgentId, java.io.Serializable {

    private static AgentIdOperatorManager opManager = new AgentIdOperatorManager();

    protected Term id;

    public AgentId(){
    }

    /**
     * Constructs an agent identifier
     *
     * The Agent identifier must be a ground valid logic term
     *
     * @param sid is the string representation of the identifier
     * @throws InvalidAgentIdException if it is not a valid identifier
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
}

