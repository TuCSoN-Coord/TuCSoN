package alice.tucson.api;

import alice.tucson.api.exception.TucsonInvalidTupleCentreIdException;
import alice.tuprolog.Term;
import alice.tuprolog.Struct;


/**
 * modified by a.pedone
 */

public class TucsonTupleCentreId implements alice.tuplecentre.api.TupleCentreId, java.io.Serializable
{
	
	private static final long serialVersionUID = 1L;
	private Object tid;
	protected alice.tuprolog.Term id;
	private  TupleCentreIdOperatorManager opManager = new TupleCentreIdOperatorManager();

	public TucsonTupleCentreId(Object tid) throws TucsonInvalidTupleCentreIdException
	{		
		this.tid=tid;
		try
		{
			CheckSyntaxCentreClientSide((String)tid);
			
		}
		catch (Exception e1)
		{
			System.err.println("InvalidTupleCentreIdException");
			e1.printStackTrace();
		}				
	}

	//CONTROLLO LA SINTASSI DEL NOME DEL CENTRO DI TUPLE FORNITO
	public void CheckSyntaxCentreClientSide(String name) throws Exception
	{
		if(name.indexOf("@")<0){
			name +="@localhost";
		}
		
		try
		{
			/*before*/
            //id=Term.parse(name,opManager);
            /*insert by Enrico Rossignoli*/
            id=Term.createTerm(name,opManager);
            /*end insert*/
		}
		catch (Exception ex)
		{
			throw new Exception();
		}
		if (!id.isGround())
		{
			throw new Exception();
		}
	}
	
	public Object getInternalTupleCentreId()
	{
		return tid;
	}
	
		
	public String toString()
	{
		return tid.toString();
	}
	
	public Term toTerm()
	{
		return id;
	}

	public String getName()
	{
		
		return id.toString();
	}

	public String getNode()
	{
		/*before*/
		//if (id.isStruct())
		/*insert by Enrico Rossignoli*/
		if (id instanceof Struct)
		/*end insert*/
		{
			Struct sid = (Struct) id;
			if (sid.getArity() == 2 && sid.getName().equals("@"))
			{
				return sid.getArg(1).getTerm().toString();
			}
		}
		return "localhost";
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
	
	public Object callMethod(String method, Object[] args)
	{
		
		return null;
	}		
	
	public TucsonTupleCentreId translateSyntax(LanguageInfo languageTo)
	{
		
		return null;
	}

	
	
}//classe

/*class TupleCentreIdOperatorManager extends alice.tuprolog.OperatorManager {

	 
	private static final long serialVersionUID = 1L;

	public TupleCentreIdOperatorManager(){
	        opNew("@","xfx",550);
	    }

	}*/
	