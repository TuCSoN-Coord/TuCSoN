package alice.tucson.test;

import alice.tucson.api.*;
import alice.logictuple.*;

public class TestTCName {

	public static void main(String[] args) throws Exception {
		
		TupleCentreId tid = new TupleCentreId("'test-tc'");
				
		TucsonContext cnt = Tucson.getContext(new AgentId("pippo"));
				
		cnt.out(tid,new LogicTuple("done_out",new Value("p", new Value(1))));
	}
}
