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

import java.util.*;
import alice.tuplecentre.*;
import alice.respect.tools.*;

/**
 * This class provides static services to create and access to tuple centres
 * and related tools (Inspector and CLIAgent).
 *
 * @author aricci
 *
 */
public class RSPFramework {
	
	private static OutputEventListener listener = new RSPFrameworkOutputEventListener(); 
	private static HashMap map = new HashMap();
	
	private RSPFramework(){	
	}
	
	/**
	 * Gets the reference to a tuple centre.
	 * 
	 * If the tuple centre does not exist,
	 * it is created.
	 * 
	 * @param tid tuple centre string identifier
	 * @return a reference to the tuple centre
	 */
	public static synchronized ITupleCentre getTC(String tid) throws InvalidTupleCentreIdException {
		ITupleCentre tc = (ITupleCentre) map.get(tid);
		if (tc == null){
			tc = new TupleCentre(new TupleCentreId(tid),listener);
			map.put(tid,tc);
		}		
		return tc;
	}
	
	/**
	 * Gets a new Inspector agent.
	 * 
	 * @param name Inspector name
	 * @param tid name of the tuple centre to be inspected 
	 * @return a reference to the Inspector agent
	 * @throws InvalidTupleCentreIdException if an invalid tuple centre name is provided
	 * @throws InvalidAgentIdException if an invalid agent name is specified
	 */
	public static synchronized Agent getInspector(String name, String tid) throws InvalidTupleCentreIdException, InvalidAgentIdException  {
		TupleCentre tc = (TupleCentre)RSPFramework.getTC(tid);
		try {
			return new Inspector(name,tc);
		} catch(InvalidAgentIdException ex){
			throw new InvalidAgentIdException();
		} catch (Exception ex){
			ex.printStackTrace();
			return null;
		}
	} 

	/**
	 * Gets a new Command Line Interpreted agent.
	 * 
	 * @param name CLIAgent name
	 * @param tid name of the tuple centre target of the agent commands
	 * @return a reference to the CLIAgent agent
	 * @throws InvalidTupleCentreIdException if an invalid tuple centre name is provided
	 * @throws InvalidAgentIdException if an invalid agent name is specified
	 */
	public static synchronized Agent getCLIAgent(String name, String tid) throws InvalidTupleCentreIdException, InvalidAgentIdException  {
		TupleCentre tc = (TupleCentre)RSPFramework.getTC(tid);
		try {
			return new CLIAgent(name,tc);
		} catch(InvalidAgentIdException ex){
			throw new InvalidAgentIdException();
		} catch (Exception ex){
			ex.printStackTrace();
			return null;
		}
	} 

}
