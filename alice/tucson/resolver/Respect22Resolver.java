package alice.tucson.resolver;

import java.io.Serializable;

import alice.respect.api.AgentId;
import alice.respect.api.InvalidAgentIdException;
import alice.respect.api.InvalidTupleCentreIdException;
import alice.respect.api.TupleCentreId;
import alice.tucson.api.exception.TucsonInvalidAgentIdException;
import alice.tucson.api.exception.TucsonInvalidTupleCentreIdException;
import alice.tucson.service.LanguageInfo;
import alice.tucson.service.TucsonAgentLanguageManager;
import alice.tucson.service.TucsonLanguageManager;
import alice.tucson.service.TucsonNodeLanguageManager;
import alice.tucson.types.IAgentIdResolver;
import alice.tucson.types.IResolver;
import alice.tucson.types.ITCIdResolver;
import alice.tucson.types.ITranslator;
import alice.tucson.types.TucsonAgentId;
import alice.tucson.types.TucsonConstant;
import alice.tucson.types.TucsonTupleCentreId;

@SuppressWarnings("serial")
public class Respect22Resolver implements ITCIdResolver, IAgentIdResolver, IResolver, Serializable
{
	private static LanguageInfo lInfo;

	public Respect22Resolver()
	{
	}

	public LanguageInfo getInfo()
	{
		lInfo = new LanguageInfo(TucsonConstant.RESPECT22);
		lInfo.setResolverComponentName(TucsonConstant.RESOLVERPATH + "Respect22Resolver");
		lInfo.setCompatibleLanguage(TucsonConstant.RESPECT21);
		lInfo.setCompatibleLanguage(TucsonConstant.RESPECT20);
		return lInfo;
	}

	public Object tupleCentreIdConstructor(Object id) throws TucsonInvalidTupleCentreIdException
	{
		if (id.getClass().getName().equals("alice.respect.api.TupleCentreId"))
		{
			return id;
		}
		else
		{
			try
			{
				return new TupleCentreId((String) id);
			}
			catch (InvalidTupleCentreIdException e)
			{
				throw new TucsonInvalidTupleCentreIdException();
			}
		}
	}

	public boolean tupleCentreIdCheckSyntax(Object id)
	{
		return true;
	}

	public String tupleCentreIdGetNode(Object id)
	{
		return ((TupleCentreId) id).getNode();
	}

	public String tupleCentreIdGetName(Object id)
	{
		return ((TupleCentreId) id).getName();
	}

	public String tupleCentreIdToString(Object id)
	{
		return ((TupleCentreId) id).toString();
	}

	public TucsonTupleCentreId tupleCentreIdTranslateSyntax(Object languageTo, Object id)
	{
		// Check the availability of the required translator
		LanguageInfo lang = TucsonNodeLanguageManager.getCurLanguage();
		if (lang == null)
		{
			lang = TucsonAgentLanguageManager.getCurLanguage();
		}
		
		// If destination language and local language are the same
		if (((LanguageInfo) languageTo).getName().equals(lang.getName()))
		{
			try
			{
				return new TucsonTupleCentreId(((TupleCentreId) id).toString());
			}
			catch (TucsonInvalidTupleCentreIdException e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			String translator = lang.getTranslatorComponentName(((LanguageInfo) languageTo).getName());
			
			if (translator != null)
			{
				try
				{
					ITranslator transl = (ITranslator) TucsonLanguageManager.createObject(translator, new Object[0]);
					return (TucsonTupleCentreId) TucsonLanguageManager.callMethodObjPar(transl, "tupleCentreIdTranslateSyntax",
							new Object[] { (Object) languageTo, (Object) id });
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public Object agentIdConstructor(Object id) throws TucsonInvalidAgentIdException
	{
		if (id.getClass().getName().equals("alice.respect.api.AgentId"))
		{
			return id;
		}
		else
		{
			try
			{
				return new AgentId((String) id);
			}
			catch (InvalidAgentIdException e)
			{
				throw new TucsonInvalidAgentIdException();
			}
		}
	}

	public boolean agentIdCheckSyntax(Object id)
	{
		return true;
	}

	public String agentIdToString(Object id)
	{
		return ((AgentId) id).toString();
	}

	public TucsonAgentId agentIdTranslateSyntax(Object languageTo, Object id)
	{
		// Check the availability of the required translator
		LanguageInfo lang = TucsonNodeLanguageManager.getCurLanguage();
		if (lang == null)
		{
			lang = TucsonAgentLanguageManager.getCurLanguage();
		}
		// If destination language and local language are the same
		if (((LanguageInfo) languageTo).getName().equals(lang.getName()))
		{
			try
			{
				return new TucsonAgentId(id.toString());
			}
			catch (TucsonInvalidAgentIdException e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			String translator = lang.getTranslatorComponentName(((LanguageInfo) languageTo).getName());
			if (translator != null)
			{
				try
				{
					ITranslator transl = (ITranslator) TucsonLanguageManager.createObject(translator, new Object[0]);
					return (TucsonAgentId) TucsonLanguageManager.callMethodObjPar(transl, "agentIdTranslateSyntax", new Object[] {
							(Object) languageTo, (Object) id });
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		return null;
	}

}
