package alice.test.tucson;

import alice.tucson.api.*;
import alice.logictuple.*;

public class TestReceive {

	public static void main(String[] args) throws Exception {
		
		TupleCentreId tid = new TupleCentreId(args[0]);
		Value dest = new Value(args[1]);
				
		TucsonContext cnt = Tucson.enterDefaultContext();
				
		LogicTuple msg = cnt.in(tid,new LogicTuple("msg",dest,new Var("Msg")));
		System.out.println("New message: "+msg.getArg(1));		
	}
}
