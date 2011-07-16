package alice.tucson.api;

import java.io.Serializable;

import alice.tucson.api.exception.TucsonInvalidAgentIdException;
import alice.tuprolog.Term;
import alice.tuprolog.OperatorManager;

/**
 * modified by a.pedone
 */

public class TucsonAgentId implements Serializable
{
	
	private static final long serialVersionUID = 1L;
	private Object aid;
	protected Term id;
	private  OperatorManager opManager = new OperatorManager();

	public TucsonAgentId(Object id) throws TucsonInvalidAgentIdException
	{
		this.aid=id;
		try
		{
			CheckSyntaxAgentClientSide((String) id);
		}
		catch (Exception e1)
		{
			System.err.println("InvalidAgentIdException");
			e1.printStackTrace();
		}
	}
	
	//CONTROLLO LA SINTASSI DEL NOME DELL'AGENTE FORNITO
	public void CheckSyntaxAgentClientSide(String sid) throws Exception {
        try{
            id=Term.parse(sid,opManager);
        } catch (Exception ex){
            throw new Exception();
        }
        if (!id.isGround()){
            throw new Exception();
        }
    }

	public Object getInternalAgentId()
	{
		return aid;
	}

	public String toString()
	{
		return aid.toString();
	}

	public boolean isAgent()
	{
		return true;
	}

	public boolean isTC()
	{
		return false;
	}

	

	public boolean checkSyntax()
	{
		try
		{
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	public TucsonAgentId translateSyntax(LanguageInfo languageTo)
	{
		
		return null;
	}


	
}