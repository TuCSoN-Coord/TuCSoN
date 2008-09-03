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
public abstract class TucsonAgent  {

    private TucsonContext context;
    private AgentId id;

    protected TucsonAgent(AgentId id) {
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
    abstract  protected void main();

    protected TucsonContext getContext(){
        return context;
    }

    final  protected AgentId getId(){
        return id;
    }

    final protected TucsonOpId out(TupleCentreId tid, LogicTuple t)
        throws TucsonException, InvalidLogicTupleException {
        return context.out(tid,t);
    }

    final protected TucsonOpId in(TupleCentreId tid, LogicTuple t)
        throws TucsonException, InvalidLogicTupleException {
        return context.in(tid,t);
    }

    final protected TucsonOpId rd(TupleCentreId tid, LogicTuple t)
        throws TucsonException, InvalidLogicTupleException {
        return context.rd(tid,t);
    }

    final protected TucsonOpId inp(TupleCentreId tid, LogicTuple t)
        throws TucsonException, InvalidLogicTupleException {
        return context.inp(tid,t);
    }

    final protected TucsonOpId rdp(TupleCentreId tid, LogicTuple t)
        throws TucsonException, InvalidLogicTupleException {
        return context.rdp(tid,t);
    }

    final protected TucsonOpId setSpec(TupleCentreId tid, String spec)
        throws TucsonException, InvalidSpecificationException{
        return context.setSpec(tid,spec);
    }

    final protected TucsonOpId getSpec(TupleCentreId tid)  throws TucsonException {
        return context.getSpec(tid);
    }
    
    final protected void synch_out(TupleCentreId tid, LogicTuple t) throws TucsonException, InvalidLogicTupleException {
    		TucsonOpId opId = context.out(tid,t);
    		context.waitForCompletion(opId);
    }	

	final protected LogicTuple synch_in(TupleCentreId tid, LogicTuple t) throws TucsonException, InvalidLogicTupleException {
		TucsonOpId opId = context.in(tid,t);
		ContextEvent ev = context.waitForCompletion(opId);
		return ev.getTuple();
	}
	
	final protected LogicTuple synch_rd(TupleCentreId tid, LogicTuple t) throws TucsonException, InvalidLogicTupleException {
		TucsonOpId opId = context.rd(tid,t);
		ContextEvent ev = context.waitForCompletion(opId);
		return ev.getTuple();
	}
	
	final protected LogicTuple synch_inp(TupleCentreId tid, LogicTuple t) throws TucsonException, InvalidLogicTupleException {
		TucsonOpId opId = context.inp(tid,t);
		ContextEvent ev = context.waitForCompletion(opId);
		return ev.getTuple();
	}
	
	final protected LogicTuple synch_rdp(TupleCentreId tid, LogicTuple t) throws TucsonException, InvalidLogicTupleException {
		TucsonOpId opId = context.rdp(tid,t);
		ContextEvent ev = context.waitForCompletion(opId);
		return ev.getTuple();
	}
	
	final protected void synch_setSpec(TupleCentreId tid, String spec) throws TucsonException, InvalidSpecificationException{
		TucsonOpId opId = context.setSpec(tid,spec);
		ContextEvent ev = context.waitForCompletion(opId);
		if (!ev.operationSucceeded()){
			throw new InvalidSpecificationException();
		}
	}
	
	final protected String synch_getSpec(TupleCentreId tid)  throws TucsonException {
		TucsonOpId opId = context.getSpec(tid);
		ContextEvent ev = context.waitForCompletion(opId);
		return ev.getSpec();
	}
    
    final protected ContextEvent waitForCompletion(TucsonOpId id, int timeout) {
    		return context.waitForCompletion(id,timeout);
    }
    
    final protected ContextEvent waitForCompletion(TucsonOpId id) {
		return context.waitForCompletion(id);
    }  
    

   
    final class AgentThread extends Thread {
        TucsonAgent agent;
        AgentThread(TucsonAgent agent){
            this.agent=agent;
        }
        final public void run(){
            try {
                agent.context = Tucson.getContext(agent.id);
                agent.main();
                agent.context.exit();
            } catch (Exception ex){
                //ex.printStackTrace();
            }
        }
    }
}

