package alice.tucson.types;

import java.io.Serializable;
import alice.tucson.api.exception.TucsonInvalidTupleCentreIdException;
import alice.tucson.service.LanguageInfo;
import alice.tucson.service.TucsonAgentLanguageManager;
import alice.tucson.service.TucsonLanguageManager;
import alice.tucson.service.TucsonNodeLanguageManager;
import alice.tuplecentre.api.*;

@SuppressWarnings("serial")
public class TucsonTupleCentreId implements Serializable, TupleCentreId
{
	private Object tid;
	private ITCIdResolver resolver;

	public TucsonTupleCentreId(Object id) throws TucsonInvalidTupleCentreIdException
	{
		// se chi fa la richiesta è il nodo, prendo il linguaggio corrente del nodo
		LanguageInfo lang = TucsonNodeLanguageManager.getCurLanguage();
		
		if (lang == null)
		{
			// se chi fa la richiesta è l'agente, prendo il linguaggio corrente dell'agente
			lang = TucsonAgentLanguageManager.getCurLanguage();
		}

		try
		{			
			resolver = (ITCIdResolver) TucsonLanguageManager.createObject(lang.getResolverComponentName(), new Object[0]);
			tid = TucsonLanguageManager.callMethodObjPar(resolver, "tupleCentreIdConstructor", new Object[] { (Object) id });
		}
		catch (Exception e1)
		{
			e1.printStackTrace();
		}
	}

	public Object callMethod(String method, Object[] args)
	{
		try
		{
			return TucsonLanguageManager.callMethodObjPar(getInternalTupleCentreId(), method, args);			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public Object getInternalTupleCentreId()
	{
		return tid;
	}

	public String toString()
	{
		try
		{
			return (String) TucsonLanguageManager.callMethodObjPar(resolver, "tupleCentreIdToString",
					new Object[] { (Object) tid });
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public boolean isAgent()
	{
		return false;
	}

	public boolean isTC()
	{
		return true;
	}

	public String getName()
	{
		try
		{
			return (String) TucsonLanguageManager.callMethodObjPar(resolver, "tupleCentreIdGetName",
					new Object[] { (Object) tid });
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public String getNode()
	{
		try
		{
			return (String) TucsonLanguageManager.callMethodObjPar(resolver, "tupleCentreIdGetNode",
					new Object[] { (Object) tid });
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public TucsonTupleCentreId translateSyntax(LanguageInfo languageTo)
	{
		try
		{
			return (TucsonTupleCentreId) TucsonLanguageManager.callMethodObjPar(resolver, "tupleCentreIdTranslateSyntax",
					new Object[] { (Object) languageTo, (Object) tid });
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
			return (Boolean) TucsonLanguageManager.callMethodObjPar(resolver, "tupleCentreIdCheckSyntax",
					new Object[] { (Object) tid });
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}

	public boolean isEnv() {
		// TODO Auto-generated method stub
		return false;
	}
}
