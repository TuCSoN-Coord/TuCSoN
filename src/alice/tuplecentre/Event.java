/*
 * Tuple Centre media - Copyright (C) 2001-2002  aliCE team at deis.unibo.it
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
package alice.tuplecentre;

/**
 * Represents events of the tuple centre virtual machine
 *
 * According to the tuple centre model, an event is characterised by
 * the operation that caused the event,  the direction of the
 * event (from agent to the tuple centre, or viceversa),
 * the identification of the agent related to the operation.
 *
 * @see TupleCentreVM
 *
 *
 * @version 1.0
 */
abstract public class Event implements java.io.Serializable {

    /** the identifier of the agent involved in the event */
    private AgentId          idAgent;

    /** a unique id identifying the event */
    private long             id;

    /** identifier of the tuple centre target of the action */
    private TupleCentreId    tid;

    /**
     * Creates a tuple centre communication event
     *
     * @param id is the long used as identifier of the event
     * @param aid is the identifier of the agent related to the event
     */
    public Event(long id,AgentId aid,TupleCentreId tid){
        this.id=id;
        idAgent=aid;
        this.tid=tid;
    }

    /**
     * Gets the identifier of the agent
     * related to the event
     */
    public AgentId getAgentId(){
        return idAgent;
    }

    /**
     * Gets the identifier of the event
     */
    public long    getId(){
        return id;
    }

    /**
     * Gets the identifier of the target tuple centre
     */
    public TupleCentreId    getTupleCentreId(){
        return tid;
    }

    /**
     * Tests if the event is directed to the tuple centre, from an agent
     */
    public abstract boolean isToTupleCentre();

    /**
     * Tests if the event is directed to an agent, from the tuple centre
     */
    public abstract boolean isFromTupleCentre();

    /**
     * Tests if the operation related to this event succeeded
     */
    public abstract boolean isSuccess();

    /**
     * Tests if the operation related to this event failed
     */
    public abstract boolean isFailure();

    /**
     * Gets the name of the operations that generated this event
     *
     * @return the name of the operation
     */
    public abstract String      getOperationName();

    /**
     * Gets the number of arguments of the operation that generated this event
     *
     * @return the number of arguments
     */
    public abstract int         getOperationNumArguments();


    public String toString(){
        return getOperationName() + "  from agent "+idAgent;
    }
}
