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

import alice.tuprolog.*;
import alice.logictuple.*;

public class RSPFrameworkOutputEventListener implements alice.tuplecentre.OutputEventListener {

		static Class[] signature = new Class[]{ AgentId.class, LogicTuple.class};
	
		public void   notify(alice.tuplecentre.OutputEvent ev){
			if (ev instanceof alice.respect.OutputEvent_out_tc){
				System.out.println("out_tc "+ev);
				OutputEvent_out_tc evout=(OutputEvent_out_tc)ev;
				TupleCentreId tid=null;
				try {
					ITupleCentre tc = RSPFramework.getTC(evout.getTargetTupleCentreId().toString());
					tc.out(evout.getTuple());
				} catch (Exception ex){
					ex.printStackTrace();
					return;
				}
			} else if (ev instanceof alice.respect.OutputEvent_spawn){
				OutputEvent_spawn evspawn=(OutputEvent_spawn)ev;
				Term agentId = evspawn.getSpawnedAgentId();
				Term agentType = evspawn.getSpawnedAgentType();
				Struct agentArgs = evspawn.getSpawnedAgentArguments();
	
				try {
					AgentId aid = new AgentId(alice.util.Tools.removeApices(agentId.toString()));
					spawnAgent(aid,agentType,agentArgs);
				} catch (Exception ex){
					ex.printStackTrace();
				}
			}
		}
		

	protected void spawnAgent(AgentId id, Term agentType, Struct argsList) throws RSPException {

		if (agentType.isStruct()){
			Struct at = (Struct)agentType;
			if (at.getName().equals("java") && at.getArity()==1){
				/*
				* Support for Java agents
				*/
				Term className = at.getArg(0);
				try {
					/*
					 * prepare arguments:
					 * from a list to a structure,
					 * for easy access to Java Agents
					 */
					int nargs=argsList.listSize();
					Term[] arg_list = new Term[nargs];
					java.util.Iterator it=argsList.listIterator();
					int count = 0;
					while (it.hasNext()){
						arg_list[count++]=(Term)it.next();
					}
					LogicTuple argStruct = new LogicTuple(new Struct("args",arg_list));

					String flatName= alice.util.Tools.removeApices(className.toString());
					//System.out.println("class: "+flatName+" args: "+args+"  id: "+id);

					ClassLoader cloader = new RespectClassLoader();
					Class cl=cloader.loadClass(flatName);

					Agent agent =
							   (Agent)(cl.getConstructor(signature).newInstance(new Object[]{id,argStruct}));
					agent.spawn();
					cloader = null;
				} catch (Exception ex){
					//ex.printStackTrace();
					throw new RSPException("Invalid agent.");
				}
			} else if (at.getName().equals("prolog") && at.getArity()==2){
					/*
					* Support for Logic agents
					*/
					Term theoryFileName = at.getArg(0);
					String goal = at.getArg(1).toString();
					try {
						String fileName= alice.util.Tools.removeApices(theoryFileName.toString());
						alice.tuprolog.Agent agent = new alice.tuprolog.Agent(fileName,goal);
						agent.spawn();
					} catch (Exception ex){
						throw new RSPException("Invalid agent "+ex.toString());
					}
			} else {
				throw new RSPException("unknown agent type "+at);
			}
		}
	}

}



class RespectClassLoader extends ClassLoader{
}