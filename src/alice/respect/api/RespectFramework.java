/*
 * Created on Jul 2, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package alice.respect.api;

import java.util.*;

import alice.respect.api.RespectTC;
import alice.respect.api.TupleCentreId;
import alice.respect.core.RespectTCContainer;

/**
 * Framework class managing set of tuple centres.
 * 
 * @author aricci
 * 
 */
public class RespectFramework
{
	private static HashMap map = new HashMap();

	public static synchronized RespectTC getTC(TupleCentreId tid)
	{
		// LS for use Interface
		/*
		 * String key = tid.toString(); RespectTC tc = (RespectTC) map.get(key);
		 * if (tc == null){ tc = new RespectTC(tid,1000); map.put(key,tc); }
		 */

		return (RespectTC) RespectTCContainer.getVM(tid);
	}
}
