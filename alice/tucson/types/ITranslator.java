package alice.tucson.types;

public interface ITranslator
{
	public String[] getInfo();

	public TucsonTupleCentreId tupleCentreIdTranslateSyntax(Object languageTo, Object id);

	public TucsonAgentId agentIdTranslateSyntax(Object languageTo, Object id);

	//public TucsonTuple tupleTranslateSyntax(Object languageTo, Object id);
}
