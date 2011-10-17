package alice.tucson.api;

import java.io.Serializable;
import alice.tucson.api.exception.TucsonInvalidAgentIdException;
import alice.tucson.service.LanguageInfo;
import alice.tucson.service.TucsonAgentLanguageManager;
import alice.tucson.service.TucsonLanguageManager;
import alice.tucson.service.TucsonNodeLanguageManager;
import alice.tucson.types.IAgentIdResolver;
//import alice.tuprolog.OperatorManager;
//import alice.tuprolog.Term;

@SuppressWarnings("serial")
public class TucsonAgentId implements Serializable
{
	private Object aid;
	private IAgentIdResolver resolver;

	public TucsonAgentId(Object id) throws TucsonInvalidAgentIdException
	{
		LanguageInfo lang = TucsonNodeLanguageManager.getCurLanguage();
		
		if (lang == null)
		{
			lang = TucsonAgentLanguageManager.getCurLanguage();
		}
		try
		{
			//System.out.println((String)id);
			resolver = (IAgentIdResolver) TucsonLanguageManager.createObject(lang.getResolverComponentName(), new Object[0]);			
			aid = TucsonLanguageManager.callMethodObjPar(resolver, "agentIdConstructor", new Object[] { (Object) id });
		}
		catch (Exception e1)
		{
			e1.printStackTrace();
		}
	}

	public Object getInternalAgentId()
	{
		return aid;
	}

	public String toString()
	{
		try
		{
			return (String) TucsonLanguageManager.callMethodObjPar(resolver, "agentIdToString", new Object[] { (Object) aid });
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public boolean isAgent()
	{
		return true;
	}

	public boolean isTC()
	{
		return false;
	}

	public TucsonAgentId translateSyntax(LanguageInfo languageTo)
	{
		try
		{
			return (TucsonAgentId) TucsonLanguageManager.callMethodObjPar(resolver, "agentIdTranslateSyntax", new Object[] {
					(Object) languageTo, (Object) aid });
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public boolean checkSyntax()
	{
		try
		{
			return (Boolean) TucsonLanguageManager
					.callMethodObjPar(resolver, "agentIdCheckSyntax", new Object[] { (Object) aid });
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	
	
	
}// classe principale
