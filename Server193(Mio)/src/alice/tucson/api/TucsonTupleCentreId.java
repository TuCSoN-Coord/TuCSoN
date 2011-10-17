package alice.tucson.api;

import alice.tucson.api.exception.TucsonInvalidTupleCentreIdException;

import alice.tucson.service.LanguageInfo;
import alice.tucson.service.TucsonAgentLanguageManager;
import alice.tucson.service.TucsonLanguageManager;
import alice.tucson.service.TucsonNodeLanguageManager;

import alice.tucson.types.ITCIdResolver;
//import alice.tuprolog.Struct;
import alice.tuprolog.Term;

public class TucsonTupleCentreId implements alice.tuplecentre.api.TupleCentreId, java.io.Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Object tid;
	private ITCIdResolver resolver;

	public TucsonTupleCentreId(Object id) throws TucsonInvalidTupleCentreIdException
	{		
		// Ottengo il linguaggio corrente per il nodo ...
		LanguageInfo lang = TucsonNodeLanguageManager.getCurLanguage();
		if (lang == null)
		{
			// ... o per l'agente
			lang = TucsonAgentLanguageManager.getCurLanguage();
		}
		
		try
		{
			// Ottengo il resolver relativo al linguaggio corrente
			resolver = (ITCIdResolver) TucsonLanguageManager.createObject(lang.getResolverComponentName(), new Object[0]);
			// Ottengo l'id del tuple centre corrispondente al linguaggio corrente
			tid = TucsonLanguageManager.callMethodObjPar(resolver, "tupleCentreIdConstructor", new Object[] { (Object) id });
		}
		catch (Exception e1)
		{
			e1.printStackTrace();
		}				
	}

	public Object getInternalTupleCentreId()
	{
		return tid;
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
	
	public Term toTerm()
	{
		try
		{
			return (Term) TucsonLanguageManager.callMethodObjPar(resolver, "toTerm",
					new Object[] { (Object) tid });
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
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

	public boolean isAgent()
	{
		return false;
	}

	public boolean isTC()
	{
		return false;
	}
	
	public boolean isEnv()
	{
		return false;
	}
	
	
	
	
}// class principale
