package alice.test.tucson;

import alice.tucson.api.*;
import alice.logictuple.*;

public class TestSend {

	public static void main(String[] args) throws Exception {
		
		TupleCentreId tid = new TupleCentreId(args[0]);
		Value dest = new Value(args[1]);
		Value msg = new Value(args[2]);
				
		TucsonContext cnt = Tucson.enterDefaultContext();
				
		cnt.out(tid,new LogicTuple("msg",dest,msg));
	}
}
