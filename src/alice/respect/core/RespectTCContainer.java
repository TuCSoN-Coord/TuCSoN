package alice.respect.core;

import alice.respect.api.IBlockingContext;
import alice.respect.api.IBlockingSpecContext;
import alice.respect.api.ILinkContext;
import alice.respect.api.IManagementContext;
import alice.respect.api.INonBlockingContext;
import alice.respect.api.INonBlockingSpecContext;
import alice.respect.api.IRemoteContainerStub;
import alice.respect.api.ITCRegistry;
import alice.respect.api.InstantiationNotPossibleException;
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

	private IRemoteContainerStub stub;

	private static RespectTCContainer container;

	public static final int QUEUE_SIZE = 10;

	private RespectTCContainer()
	{
		this.registry = new RespectLocalRegistry();
		this.stub = null;
	}

	public static RespectTCContainer getRespectTCContainer()
	{
		if (RespectTCContainer.container == null)
		{
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
		try
		{
			return ((RespectTC) registry.getTC(id)).getBlockingContext();
		}
		catch (Exception e)
		{
			RespectTC tc = new RespectTC(id, this, QUEUE_SIZE);
			this.registry.addTC(tc);
			return tc.getBlockingContext();
		}
	}

	public INonBlockingContext getNonBlockingContext(TupleCentreId id)
	{
		try
		{
			return ((RespectTC) registry.getTC(id)).getNonBlockingContext();
		}
		catch (Exception e)
		{
			RespectTC tc = new RespectTC(id, this, QUEUE_SIZE);
			this.registry.addTC(tc);
			return tc.getNonBlockingContext();
		}
	}

	/**
	 * Return a LinkContext for remote/local call
	 * 
	 * @param id
	 *            the identifier of the tuple centre target (local or remote)
	 * @throws OperationNotPossibleException
	 */
	public ILinkContext getLinkContext(TupleCentreId id) throws OperationNotPossibleException
	{
		System.out.println("[ReSpecT TC CONTAINER]  Link call to tc " + id.toString());
		// TupleCentreId idNew = null;
		if (id.getNode().equals("localhost"))
		{
			try
			{
				return ((RespectTC) registry.getTC(id)).getLinkContext();

			}
			catch (InstantiationNotPossibleException e)
			{
				RespectTC tc = new RespectTC(id, this, QUEUE_SIZE);
				this.registry.addTC(tc);
				return tc.getLinkContext();
			}
		}

		if (this.stub == null)
		{
			throw new OperationNotPossibleException();
		}

		return stub.getRemoteLinkContext(id);

	}

	public void addStub(IRemoteContainerStub stub)
	{
		if (stub == null)
		{
			this.stub = stub;
		}
	}

	public INonBlockingSpecContext getNonBlockingSpecContext(TupleCentreId id) throws OperationNotPossibleException
	{
		try
		{
			return ((RespectTC) registry.getTC(id)).getNonBlockingSpecContext();
		}
		catch (Exception e)
		{
			RespectTC tc = new RespectTC(id, this, QUEUE_SIZE);
			this.registry.addTC(tc);
			return tc.getNonBlockingSpecContext();
		}
	}

	public IBlockingSpecContext getBlockingSpecContext(TupleCentreId id) throws OperationNotPossibleException
	{
		try
		{
			return ((RespectTC) registry.getTC(id)).getBlockingSpecContext();
		}
		catch (Exception e)
		{
			RespectTC tc = new RespectTC(id, this, QUEUE_SIZE);
			this.registry.addTC(tc);
			return tc.getBlockingSpecContext();
		}
	}

	public IManagementContext getManagementContext(TupleCentreId id) throws OperationNotPossibleException
	{
		try
		{
			return ((RespectTC) registry.getTC(id)).getManagementContext();
		}
		catch (Exception e)
		{
			RespectTC tc = new RespectTC(id, this, QUEUE_SIZE);
			this.registry.addTC(tc);
			return tc.getManagementContext();
		}
	}

	// ***METHOD NO LONGER USED
	// Was used by RespectFramework	
	 //public RespectTC getVM(TupleCentreId id) throws InstantiationNotPossibleException 
	 //{ 				 
		// return (RespectTC) registry.getTC(id);
	 //}	 
}
