package examples.basic;
//import alice.respect.api.AgentId;
import alice.tucson.api.TucsonAgentId;

public class TestAgentProva191 {
	
	
	public static void main(String[] args) throws Exception {
		TucsonAgentId aid = new TucsonAgentId("pluto2");
		new AgentProva191(aid).spawn();
	}


}
