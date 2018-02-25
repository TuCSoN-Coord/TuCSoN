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
package alice.tucson.api;

import java.util.List;

import alice.tucson.api.acc.EnhancedACC;
import alice.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tucson.service.TucsonOpCompletionEvent;
import alice.tuplecentre.core.AbstractTupleCentreOperation;

/**
 * Base class to extend to implement TuCSoN Agents. Once created, the method
 * {@link alice.tucson.api.AbstractTucsonAgent#go go()} gets TuCSoN Default ACC
 * (the most comprehensive at the moment) and trigger Agent's main execution
 * cyle, that is the method {@link alice.tucson.api.AbstractTucsonAgent#main
 * main}.
 *
 * @author Alessandro Ricci
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 *
 */
public abstract class AbstractTucsonAgent implements
        TucsonOperationCompletionListener {

    /**
     * Internal Thread responsible for ACC acquisition and main cycle execution.
     * Notice that the ACC is demanded to the TuCSoN Node Service hosted at the
     * construction-time defined ip address and listening on the
     * construction-time defined port.
     */
    final class AgentThread extends Thread {

        private final AbstractTucsonAgent agent;

        AgentThread(final AbstractTucsonAgent a) {
            super();
            this.agent = a;
        }

        @Override
        public void run() {
            try {
                this.agent.setContext(TucsonMetaACC.getContext(
                        this.agent.getTucsonAgentId(),
                        AbstractTucsonAgent.this.node,
                        AbstractTucsonAgent.this.port));
                this.agent.main();
                this.agent.getContext().exit();
            } catch (final TucsonOperationNotPossibleException e) {
                e.printStackTrace();
            }
        }
    }

    private static final int DEFAULT_PORT = 20504;
    private final TucsonAgentId aid;
    private EnhancedACC context;
    private final String node;
    private final int port;
    /**
     *
     */
    protected List<TucsonOpCompletionEvent> completionEvents;

    /**
     * Same as before, this time using the passed String to create the
     * TucsonAgentId from scratch
     *
     * @param id
     *            The String to use to build the TucsonAgentIdentifier
     *
     * @throws TucsonInvalidAgentIdException
     *             if the String given is not a valid representation of a TuCSoN
     *             agent identifier
     */
    public AbstractTucsonAgent(final String id)
            throws TucsonInvalidAgentIdException {
        this(new TucsonAgentId(id), "localhost",
                AbstractTucsonAgent.DEFAULT_PORT);
    }

    /**
     * Again we assume default port (which is 20504), so we skip that parameter
     * (String aid version).
     *
     * @param id
     *            The String to use to build the TucsonAgentIdentifier
     * @param netid
     *            The ip address of the TuCSoN Node to contact
     *
     * @throws TucsonInvalidAgentIdException
     *             if the String given is not a valid representation of a TuCSoN
     *             agent identifier
     */
    public AbstractTucsonAgent(final String id, final String netid)
            throws TucsonInvalidAgentIdException {
        this(new TucsonAgentId(id), netid, AbstractTucsonAgent.DEFAULT_PORT);
    }

    /**
     * Same as first one, but takes a String in place of a TucsonAgentId that is
     * created from scratch using such string.
     *
     * @param id
     *            The String to use to build the TucsonAgentIdentifier
     * @param netid
     *            The ip address of the TuCSoN Node to contact
     * @param p
     *            The listening port of the TuCSoN Node to contact
     *
     * @throws TucsonInvalidAgentIdException
     *             if the String given is not a valid representation of a TuCSoN
     *             agent identifier
     */
    public AbstractTucsonAgent(final String id, final String netid, final int p)
            throws TucsonInvalidAgentIdException {
        this(new TucsonAgentId(id), netid, p);
    }

    /**
     * Most complete constructor, allows to specify the ip address where the
     * TuCSoN node to whom ask for an ACC resides and its listening port.
     *
     * @param id
     *            The TucsonAgent Identifier
     * @param netid
     *            The ip address of the TuCSoN Node to contact
     * @param p
     *            The listening port of the TuCSoN Node to contact
     */
    private AbstractTucsonAgent(final TucsonAgentId id, final String netid,
            final int p) {
        this.aid = id;
        this.node = netid;
        this.port = p;
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
     * Starts main execution cycle
     * {@link alice.tucson.api.AbstractTucsonAgent#main main}
     */
    public final void go() {
        new AgentThread(this).start();
    }

    /**
     * Returns local agent name
     *
     * @return The String name of the agent
     */
    public final String myName() {
        return this.aid.getAgentName();
    }

    /**
     * Returns agent default node.
     *
     * @return The default node of the agent
     */
    public final String myNode() {
        return this.node;
    }

    /**
     * Returns agent default port
     *
     * @return The default port of the agent
     */
    public final int myport() {
        return this.port;
    }

    @Override
    public abstract void operationCompleted(AbstractTupleCentreOperation op);

    @Override
    public abstract void operationCompleted(TucsonOperation op);

    /**
     * Getter for the ACC. At the moment the TucsonAgent base class always ask
     * for the most comprehensive ACC (that is the DefaultACC): it's up to the
     * user agent wether to use a more restrictive one (properly declaring its
     * reference)
     *
     * @return The DefaultACC
     */
    protected EnhancedACC getContext() {
        return this.context;
    }

    /**
     * Main execution cycle, user-defined.
     */
    protected abstract void main();

    /**
     * Utility method to print on standard output the user agent activity.
     *
     * @param msg
     *            The message to print
     */
    protected void say(final String msg) {
        System.out.println("[" + this.getTucsonAgentId().getAgentName() + "]: "
                + msg);
    }

    /**
     * Setter for the ACC. Takes the most comprehensive one, hence even a
     * less-powerful can be passed.
     *
     * @param ctx
     *            The ACC to use
     */
    protected void setContext(final EnhancedACC ctx) {
        this.context = ctx;
    }
}
