package alice.tucson.types;


import java.io.Serializable;

import alice.respect.api.InvalidTupleCentreIdException;
import alice.respect.api.TupleCentreId;
import alice.tucson.api.exception.TucsonInvalidTupleCentreIdException;
import alice.tucson.service.LanguageInfo;
import alice.tucson.service.TucsonTCContainer;

@SuppressWarnings("serial")
public class TucsonTupleCentreId implements Serializable{
   
	private Object tid;

	public TucsonTupleCentreId(Object id) throws TucsonInvalidTupleCentreIdException{
		String lang = TucsonTCContainer.getCurLanguage().getName();
		if (lang.equals(TucsonOpType.DEFLANGUAGE)){
			if (id.getClass().getName().equals("alice.respect.api.TupleCentreId")){
	    		tid = id;
	    	} else {
	    		try {
					tid = new TupleCentreId((String)id);
				} catch (InvalidTupleCentreIdException e) {
					throw new TucsonInvalidTupleCentreIdException();
				}
	    	}
		}
	}
	
	//TODO nei metodi sotto ci andrebbe cmq il controllo (però vanno fatti dinamici)
	
	public Object getInternalTupleCentreId(){
		return tid;
	}
	
	public String toString(){
		return ((TupleCentreId)tid).toString();
	}
	
	public boolean isAgent() {
		return false;
	}

	public boolean isTC() {
		return true;
	}
	
    public String getName(){
        return ((TupleCentreId)tid).getName();
    }
    
    public String getNode(){
        return ((TupleCentreId)tid).getNode();
    }
    
    public TucsonTupleCentreId translateSyntax(LanguageInfo languageTo){
    	//TODO V. tucsonResolver
    	try {
			return new TucsonTupleCentreId(tid.toString());
		} catch (TucsonInvalidTupleCentreIdException e) {
			e.printStackTrace();
		}
		return null;
    }
    
    public boolean checkSyntax(){
    	return true;
    }
}
