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

import alice.logictuple.*;

/**
 * Defines the skeleton for simple TuCSoN agent base class
 *
 */
public abstract class Agent  {

    private TucsonContext context;
    private AgentId id;

    protected Agent(AgentId id) throws TucsonException {
        this.id=id;
    }

    /**
     * Starts agent execution
     */
    final  public void spawn(){
        new AgentThread(this).start();
    }

    /**
     * Body of the agent
     */
    abstract  protected void body();

    protected TucsonContext getContext(){
        return context;
    }

    final  protected AgentId getId(){
        return id;
    }

    final protected void out(TupleCentreId tid, LogicTuple t)
        throws TucsonException, InvalidLogicTupleException {
        context.out(tid,t);
    }

    final protected LogicTuple in(TupleCentreId tid, LogicTuple t)
        throws TucsonException, InvalidLogicTupleException {
        return context.in(tid,t);
    }

    final protected LogicTuple rd(TupleCentreId tid, LogicTuple t)
        throws TucsonException, InvalidLogicTupleException {
        return context.rd(tid,t);
    }

    final protected LogicTuple inp(TupleCentreId tid, LogicTuple t)
        throws TucsonException, InvalidLogicTupleException {
        return context.inp(tid,t);
    }

    final protected LogicTuple rdp(TupleCentreId tid, LogicTuple t)
        throws TucsonException, InvalidLogicTupleException {
        return context.rdp(tid,t);
    }

    final protected void setSpec(TupleCentreId tid, String spec)
        throws TucsonException, InvalidSpecificationException{
        context.setSpec(tid,spec);
    }

    final protected String getSpec(TupleCentreId tid)  throws TucsonException {
        return context.getSpec(tid);
    }

    final class AgentThread extends Thread {
        Agent agent;
        AgentThread(Agent agent){
            this.agent=agent;
        }
        final public void run(){
            try {
                agent.context = Tucson.enterContext(new DefaultContextDescription(agent.id));
                agent.body();
                agent.context.exit();
            } catch (Exception ex){
                //ex.printStackTrace();
            }
        }
    }
}

