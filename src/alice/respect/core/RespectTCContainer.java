package alice.respect.core;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;

import alice.respect.api.IBlockingContext;
import alice.respect.api.IBlockingSpecContext;
import alice.respect.api.ILinkContext;
import alice.respect.api.IManagementContext;
import alice.respect.api.INonBlockingContext;
import alice.respect.api.INonBlockingSpecContext;
import alice.respect.api.ITCRegistry;
import alice.respect.api.InstantiationNotPossibleException;
import alice.respect.api.OperationNotPossibleException;
import alice.respect.api.RespectTC;
import alice.respect.api.TupleCentreId;
import alice.tucson.service.LanguageInfo;
import alice.tucson.service.TucsonNodeLanguageManager;

/**
 * A Container for ReSpecT tuple centres
 * 
 * @author matteo casadei v 2.1.1
 * 
 */

public class RespectTCContainer
{
	private static ITCRegistry registry = new RespectLocalRegistry();
	private static boolean isTucsonPresent = false;
	private static HashMap<String, RespectInterTupleCentreContextStub> remoteRegistry;

	public static LanguageInfo getLanguage()
	{
		LanguageInfo lInfo = new LanguageInfo("ReSpecT 2.2");
		lInfo.setCompatibleLanguage("ReSpecT 2.1");
		lInfo.setCompatibleLanguage("ReSpecT 2.0");
		return lInfo;
	}

	public static boolean createRespectTC(TupleCentreId id, Integer q) throws InstantiationNotPossibleException
	{
		registry.addTC(new RespectTC(id, q));
		return true;
	}

	public static IBlockingContext getBlockingContext(TupleCentreId id) throws OperationNotPossibleException
	{
		return ((RespectTC) registry.getTC(id)).getBlockingContext();
	}

	public static INonBlockingContext getNonBlockingContext(TupleCentreId id) throws OperationNotPossibleException
	{
		return ((RespectTC) registry.getTC(id)).getNonBlockingContext();
	}

	/**
	 * Allow to set the Tucson presence
	 * 
	 * @param id
	 *            the identifier of the agent whose events must be removed
	 */
	public static void setTucson()
	{
		// Create the remote registry only the first time
		if (remoteRegistry == null)
		{
			remoteRegistry = new HashMap<String, RespectInterTupleCentreContextStub>();
		}
		
		isTucsonPresent = true;
	}

	/**
	 * Return a LinkContext for remote/local call
	 * 
	 * @param id
	 *            the identifier of the tuple centre target (local or remote)
	 */
	public static ILinkContext getLinkContext(TupleCentreId id) throws OperationNotPossibleException
	{
		System.out.println("[ReSpecT TC CONTAINER]  Link call to tc " + id.toString());
		if (!isTucsonPresent)
		{
			// TuCSoN not set
			try
			{
				if (id.getNode().equals("'" + InetAddress.getLocalHost().getHostAddress() + "'")
						|| id.getNode().equals("localhost"))
				{
					return ((RespectTC) registry.getTC(id)).getLinkContext();
				}
			}
			catch (UnknownHostException e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			try
			{
				// Local call (check if is created a not existent tuple centre)
				if (id.getNode().equals("'" + InetAddress.getLocalHost().getHostAddress() + "'")
						|| id.getNode().equals("localhost"))
				{
					return ((RespectTC) registry.getTC(id)).getLinkContext();
				}
				else
				{
					// Remote call with TuCSoN set
					System.out.println("[ReSpecT TC CONTAINER] Link Context request");
					RespectInterTupleCentreContextStub helper = remoteRegistry.get(id.toString());
					if (helper == null)
					{
						helper = new RespectInterTupleCentreContextStub(id, TucsonNodeLanguageManager.getCurLanguage().getName());
						remoteRegistry.put(id.toString(), helper);
					}
					return helper;
				}
			}
			catch (UnknownHostException e)
			{
				e.printStackTrace();
			}
		}
		throw new OperationNotPossibleException();
	}

	public static INonBlockingSpecContext getNonBlockingSpecContext(TupleCentreId id) throws OperationNotPossibleException
	{
		return ((RespectTC) registry.getTC(id)).getNonBlockingSpecContext();
	}

	public static IBlockingSpecContext getBlockingSpecContext(TupleCentreId id) throws OperationNotPossibleException
	{
		return ((RespectTC) registry.getTC(id)).getBlockingSpecContext();
	}

	public static IManagementContext getManagementContext(TupleCentreId id) throws OperationNotPossibleException
	{
		return ((RespectTC) registry.getTC(id)).getManagementContext();
	}

	// For use RespectFramework
	public static RespectTC getVM(TupleCentreId id)
	{
		return (RespectTC) registry.getTC(id);
	}

}
