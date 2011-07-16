package alice.tucson.types;

import alice.tucson.api.TucsonAgentId;
import alice.tucson.api.exception.TucsonInvalidAgentIdException;

public interface IAgentIdResolver
{
	public Object agentIdConstructor(Object id) throws TucsonInvalidAgentIdException;

	public String agentIdToString(Object id);

	public boolean agentIdCheckSyntax(Object id);
}
