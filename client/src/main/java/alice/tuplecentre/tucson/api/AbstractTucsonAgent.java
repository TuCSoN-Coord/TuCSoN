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

import alice.tuplecentre.tucson.api.acc.RootACC;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tuplecentre.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tuplecentre.tucson.service.TucsonInfo;

/**
 * Base class to extend to implement TuCSoN Agents. Once created, the method
 * {@link alice.tuplecentre.tucson.api.AbstractTucsonAgent#go go()} gets TuCSoN Default ACC
 * (the most comprehensive at the moment) and trigger Agent's main execution
 * cycle, that is the method {@link alice.tuplecentre.tucson.api.AbstractTucsonAgent#main
 * main}.
 *
 * @param <T> type parameter to specify wich ACC will be used
 * @author Alessandro Ricci
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 */
public abstract class AbstractTucsonAgent<T extends RootACC> {

    private final TucsonAgentId aid;

    private T context;
    private final String nodeIp;
    private final int portNumber;

    /**
     * Most complete constructor, allows to specify the ip address where the
     * TuCSoN node to whom ask for an ACC resides and its listening port.
     *
     * @param id         The TucsonAgent Identifier
     * @param networkId  The ip address of the TuCSoN Node to contact
     * @param portNumber The listening port of the TuCSoN Node to contact
     */
    private AbstractTucsonAgent(final TucsonAgentId id, final String networkId, final int portNumber) {
        this.aid = id;
        this.nodeIp = networkId;
        this.portNumber = portNumber;
    }

    /**
     * Same as first one, but takes a String in place of a TucsonAgentId that is
     * created from scratch using such string.
     *
     * @param id         The String to use to build the TucsonAgentIdentifier
     * @param networkId  The ip address of the TuCSoN Node to contact
     * @param portNumber The listening port of the TuCSoN Node to contact
     * @throws TucsonInvalidAgentIdException if the String given is not a valid representation of a TuCSoN
     *                                       agent identifier
     */
    public AbstractTucsonAgent(final String id, final String networkId, final int portNumber)
            throws TucsonInvalidAgentIdException {
        this(new TucsonAgentId(id), networkId, portNumber);
    }

    /**
     * Again we assume {@link TucsonInfo#getDefaultPortNumber() default port }, so we skip that parameter
     * (String aid version).
     *
     * @param id        The String to use to build the TucsonAgentIdentifier
     * @param networkId The ip address of the TuCSoN Node to contact
     * @throws TucsonInvalidAgentIdException if the String given is not a valid representation of a TuCSoN
     *                                       agent identifier
     */
    public AbstractTucsonAgent(final String id, final String networkId)
            throws TucsonInvalidAgentIdException {
        this(new TucsonAgentId(id), networkId, TucsonInfo.getDefaultPortNumber());
    }

    /**
     * Same as before, this time using the passed String to create the
     * TucsonAgentId from scratch
     *
     * @param id The String to use to build the TucsonAgentIdentifier
     * @throws TucsonInvalidAgentIdException if the String given is not a valid representation of a TuCSoN
     *                                       agent identifier
     */
    public AbstractTucsonAgent(final String id)
            throws TucsonInvalidAgentIdException {
        this(new TucsonAgentId(id), "localhost",
                TucsonInfo.getDefaultPortNumber());
    }

    /**
     * Getter for the TucsonAgent identifier
     *
     * @return The TucsonAgentId for this agent
     */
    public final TucsonAgentId getTucsonAgentId() {
        return this.aid;
    }

    /**
     * Returns agent default node.
     *
     * @return The default node of the agent
     */
    public final String myNode() {
        return this.nodeIp;
    }

    /**
     * Returns agent default port
     *
     * @return The default port of the agent
     */
    public final int myPort() {
        return this.portNumber;
    }

    /**
     * Starts main execution cycle
     * {@link alice.tuplecentre.tucson.api.AbstractTucsonAgent#main main}
     */
    public final void go() {
        new AgentThread(this).start();
    }

    /**
     * Getter for the ACC. At the moment the TucsonAgent base class always ask
     * for the most comprehensive ACC (that is the DefaultACC): it's up to the
     * user agent wether to use a more restrictive one (properly declaring its
     * reference)
     *
     * @return The DefaultACC
     */
    protected final T getACC() {
        return this.context;
    }

    /**
     * Method that client classes must implement to gain the ACC, then available on {@link #getACC()}
     * <p>
     * <p>It's called before {@link #main()}</p>
     *
     * @param aid            the agentId that should be used to create ACC
     * @param networkAddress the node address thata should be used to create ACC
     * @param portNumber     the portNumber relative to that address that should be used
     * @return the ACC retrieved by the Node specified at construction time
     */
    protected abstract T retrieveACC(final TucsonAgentId aid, final String networkAddress, final int portNumber);

    /**
     * Main execution cycle, user-defined.
     */
    protected abstract void main();

    /**
     * Utility method to print on standard output the user agent activity.
     *
     * @param msg The message to print
     */
    protected void say(final String msg) {
        //TODO use logging
        System.out.println("[" + this.aid.getAgentName() + "]: "
                + msg);
    }


    /**
     * Internal Thread responsible for ACC acquisition and main cycle execution.
     * Notice that the ACC is demanded to the TuCSoN Node Service hosted at the
     * construction-time defined ip address and listening on the
     * construction-time defined port.
     */
    private final class AgentThread extends Thread {

        private final AbstractTucsonAgent agent;

        AgentThread(final AbstractTucsonAgent a) {
            super();
            this.agent = a;
        }

        @Override
        public void run() {
            try {
                this.agent.context = retrieveACC(agent.aid, agent.nodeIp, agent.portNumber);
                this.agent.main();
                this.agent.getACC().exit();
            } catch (final TucsonOperationNotPossibleException e) {
                e.printStackTrace();
            }
        }
    }
}









