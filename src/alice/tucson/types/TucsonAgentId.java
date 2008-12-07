package alice.tucson.types;

import java.io.Serializable;

import alice.respect.api.AgentId;
import alice.respect.api.InvalidAgentIdException;
import alice.respect.api.InvalidTupleCentreIdException;
import alice.respect.api.TupleCentreId;
import alice.tucson.api.exception.TucsonInvalidAgentIdException;
import alice.tucson.api.exception.TucsonInvalidTupleCentreIdException;
import alice.tucson.service.LanguageInfo;
import alice.tucson.service.TucsonTCContainer;

@SuppressWarnings("serial")
public class TucsonAgentId implements Serializable{

	private Object aid;

	public TucsonAgentId(Object id) throws TucsonInvalidAgentIdException{
		String lang = TucsonTCContainer.getCurLanguage().getName();
		if (lang.equals(TucsonOpType.DEFLANGUAGE)){
			if (id.getClass().getName().equals("alice.respect.api.TupleCentreId")){
				aid = id;
			} else {
				try {
					aid = new AgentId((String)id);
				} catch (InvalidAgentIdException e) {
					throw new TucsonInvalidAgentIdException();
				}
			}	
		}
	}

	//TODO nei metodi sotto ci andrebbe cmq il controllo (però vanno fatti dinamici)
	
	public Object getInternalAgentId(){
		return aid;
	}
	
	public String toString(){
		return ((AgentId)aid).toString();
	}
	
	public boolean isAgent() {
		return true;
	}

	public boolean isTC() {
		return false;
	}
	
	public TucsonAgentId translateSyntax(LanguageInfo languageTo){
		//TODO V. tucsonResolver
		try {
			return new TucsonAgentId(aid.toString());
		} catch (TucsonInvalidAgentIdException e) {
			e.printStackTrace();
		}

		return null;
    }
	
	public boolean checkSyntax(){
		return true;
	}
}
