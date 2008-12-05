/*
 * ReSpecT Framework - aliCE team at deis.unibo.it
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
package alice.respect;

import java.lang.reflect.*;

/**
 * This abstract class represents a template
 * for building Agents. An agent has not 
 * public method, apart those necessary
 * for its booting.
 * 
 * The method mainTask must be overriden,
 * specifying the main activity of the agent.
 * 
 * @author aricci
 *
 */
public abstract class Agent  {

    private AgentId id;
	private static final Class[] ARGS_CLASS = new Class[]{}; 
	private static final Object[] ARGS = new Object[]{}; 
        
	protected Agent(String sid) throws Exception {
		this.id=new AgentId(sid);
	}
	protected Agent(AgentId id) {
		this.id=id;
	}

    /**
     * Start agent execution
     */
    final  public void spawn() throws Exception {
        execTask("main");
    }

    /**
     * Body of the agent
     */
    abstract  protected void main();

    /**
     * Gets the agent identifier
     * 
     * @return agent identifier
     */
    final  protected AgentId getId(){
        return id;
    }

	//
	
	/**
	 * Executes a new task, whose behaviour
	 * is defined in a method with the specified name
	 * 
	 * @param name method name containing the task behaviour
	 */
	final protected void execTask(String name) throws Exception {
		Method m = this.getClass().getDeclaredMethod(name,ARGS_CLASS);
		m.setAccessible(true);
		new AgentTask(this,m).start();
	}
	
	//
    final class AgentTask extends Thread {
        private Agent agent;
        private Method activity;
        AgentTask(Agent agent, Method m) throws Exception {
            this.agent=agent;
            activity=m;
        }
        final public void run(){
            try {
                activity.invoke(agent,ARGS);
            } catch (Exception ex){
                //ex.printStackTrace();
            }
        }
    }
}

