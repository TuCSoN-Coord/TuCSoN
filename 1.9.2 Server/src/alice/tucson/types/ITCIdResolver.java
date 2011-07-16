package alice.tucson.types;

import alice.tucson.api.TucsonTupleCentreId;
import alice.tucson.api.exception.TucsonInvalidTupleCentreIdException;

public interface ITCIdResolver
{
	public Object tupleCentreIdConstructor(Object id) throws TucsonInvalidTupleCentreIdException;

	public String tupleCentreIdToString(Object id);

	public String tupleCentreIdGetName(Object id);

	public String tupleCentreIdGetNode(Object id);

	public boolean tupleCentreIdCheckSyntax(Object id);
}
