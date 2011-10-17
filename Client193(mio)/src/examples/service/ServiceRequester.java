package examples.service;

import alice.tucson.api.*;
import alice.logictuple.*;

public class ServiceRequester {
	public static void main (String[] args) throws Exception {
		
		TucsonContext ctx = Tucson.getContext(new TucsonAgentId("requester"), null);
		TucsonTupleCentreId tid = new TucsonTupleCentreId("services@'192.168.1.68'");  ///NEEEEEEw
		
		Value v0 = new Value(13);
		Value v1 = new Value(17);
		
		System.out.println("Agent  requesting the sum of "+v0+" and "+v1+"...");
		
		ctx.out(tid, new LogicTuple("request", v0, v1), (Long) null);
		while(true) {
		LogicTuple res = ctx.in(tid, new LogicTuple("result", new Var("R")), (Long) null);
		System.out.println("The result is "+res.getArg(0));
		}
		
	}

}

