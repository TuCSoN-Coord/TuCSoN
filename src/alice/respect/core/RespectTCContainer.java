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
import alice.respect.api.InvalidTupleCentreIdException;
import alice.respect.api.OperationNotPossibleException;
import alice.respect.api.RespectTC;
import alice.respect.api.TupleCentreId;

/**
 * A Container for ReSpecT tuple centres
 * 
 * @author matteo casadei v 2.1.1
 * 
 */

public class RespectTCContainer
{
	private ITCRegistry registry;

	// private static boolean isTucsonPresent = false;
	// private static HashMap<String, RespectInterTupleCentreContextStub>
	// remoteRegistry;
	
	private static RespectTCContainer container;
	
	public static final int QUEUE_SIZE = 10;

	private RespectTCContainer(){
		this.registry= new RespectLocalRegistry();
	}
	
	public static RespectTCContainer getRespectTCContainer()
	{
		if(RespectTCContainer.container==null){
			RespectTCContainer.container = new RespectTCContainer();
		}
		
		return RespectTCContainer.container;
		
	}
	

	public boolean createRespectTC(TupleCentreId id, Integer q) throws InstantiationNotPossibleException
	{
		registry.addTC(new RespectTC(id, this, q));
		return true;
	}

	public IBlockingContext getBlockingContext(TupleCentreId id) throws OperationNotPossibleException
	{
		try{
			return ((RespectTC) registry.getTC(id)).getBlockingContext();
		}catch(Exception e){
			RespectTC tc = new RespectTC(id,this,QUEUE_SIZE);
			this.registry.addTC(tc);
			return tc.getBlockingContext();
		}
	}

	public INonBlockingContext getNonBlockingContext(TupleCentreId id){
		try{
			return ((RespectTC) registry.getTC(id)).getNonBlockingContext();
		}catch(Exception e){
			RespectTC tc = new RespectTC(id,this,QUEUE_SIZE);
			this.registry.addTC(tc);
			return tc.getNonBlockingContext();
		}	
	}

	/**
	 * Allow to set the Tucson presence
	 * 
	 * @param id
	 *            the identifier of the agent whose events must be removed
	 */
	/*
	 * public static void setTucson() { // Create the remote registry only the
	 * first time if (remoteRegistry == null) { remoteRegistry = new
	 * HashMap<String, RespectInterTupleCentreContextStub>(); }
	 * 
	 * isTucsonPresent = true; }
	 */

	/**
	 * Return a LinkContext for remote/local call
	 * 
	 * @param id
	 *            the identifier of the tuple centre target (local or remote)
	 */
	public ILinkContext getLinkContext(TupleCentreId id)
	{
		System.out.println("[ReSpecT TC CONTAINER]  Link call to tc " + id.toString());
		//TupleCentreId idNew = null;
		try
		{
			return ((RespectTC) registry.getTC(id)).getLinkContext();

		}
		catch(InstantiationNotPossibleException e){
			RespectTC tc = new RespectTC(id,this,QUEUE_SIZE);
			this.registry.addTC(tc);
			return tc.getLinkContext();
		}
		
	}

	public INonBlockingSpecContext getNonBlockingSpecContext(TupleCentreId id) throws OperationNotPossibleException
	{
		try{
			return ((RespectTC) registry.getTC(id)).getNonBlockingSpecContext();
		}catch(Exception e){
			RespectTC tc = new RespectTC(id,this,QUEUE_SIZE);
			this.registry.addTC(tc);
			return tc.getNonBlockingSpecContext();
		}
	}

	public IBlockingSpecContext getBlockingSpecContext(TupleCentreId id) throws OperationNotPossibleException
	{
		try{
			return ((RespectTC) registry.getTC(id)).getBlockingSpecContext();
		}catch(Exception e){
			RespectTC tc = new RespectTC(id,this,QUEUE_SIZE);
			this.registry.addTC(tc);
			return tc.getBlockingSpecContext();
		}
	}

	public IManagementContext getManagementContext(TupleCentreId id) throws OperationNotPossibleException
	{
		try{
			return ((RespectTC) registry.getTC(id)).getManagementContext();
		}catch(Exception e){
			RespectTC tc = new RespectTC(id,this,QUEUE_SIZE);
			this.registry.addTC(tc);
			return tc.getManagementContext();
		}
	}

	//***METHOD NO LONGER USED
	// Was used by RespectFramework
	/*public RespectTC getVM(TupleCentreId id)
	{
		return (RespectTC) registry.getTC(id);
	}*/

}
