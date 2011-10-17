package alice.respect.api;

import alice.tuplecentre.api.IId;
import alice.tuprolog.Struct;
import alice.tuprolog.Term;

public class EnvId implements IId{

	private Struct id;
	
	public EnvId(Struct id){
		this.id=id;
	}
	public boolean isAgent() {
		return false;
	}
	public boolean isEnv() {
		return true;
	}
	public boolean isTC() {
		return false;
	}
	public Term toTerm(){
		//Bypassing @localhost
		if (id.getName().equals("@")){
			return id.getArg(0).getTerm();
		}
		return id.getTerm();
	}
}
