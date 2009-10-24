package alice.tucson.service.stubs;

import java.util.HashMap;

import alice.respect.api.ILinkContext;
import alice.respect.api.IRemoteContainerStub;
import alice.tuplecentre.api.TupleCentreId;

public class RespectRemoteContainerStub implements IRemoteContainerStub
{
	private static HashMap<String, RespectInterTupleCentreContextStub> remoteRegistry;

	public RespectRemoteContainerStub()
	{
		remoteRegistry = new HashMap<String, RespectInterTupleCentreContextStub>();
	}

	public ILinkContext getRemoteLinkContext(TupleCentreId id)
	{
		RespectInterTupleCentreContextStub helper = remoteRegistry.get(id.toString());
		if (helper == null)
		{
			helper = new RespectInterTupleCentreContextStub(id);
			remoteRegistry.put(id.toString(), helper);
		}
		return helper;
	}
}
