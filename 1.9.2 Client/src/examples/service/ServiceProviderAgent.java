package examples.service;

import alice.tucson.api.*;
import alice.tuplecentre.core.TupleCentreOperation;
import alice.logictuple.*;

public class ServiceProviderAgent extends TucsonAgent {
	
	private TucsonAgentId id;
	TucsonTupleCentreId tid;
	TucsonContext ctx;
	LogicTuple req;
	
	protected ServiceProviderAgent(TucsonAgentId id) throws Exception {
		super(id);
		this.id=id;
	}
	
	protected void mainPlan() {
		try {
			ctx = Tucson.getContext(id, null);
			tid  = new TucsonTupleCentreId("services@'192.168.1.68'");
			System.out.println("Service Provider Agent started!");
			while (true) {
				req = ctx.in(tid, new LogicTuple("request", new Var("X"), new Var("Y")), (Long) null);

				System.out.println("A new request arrived: "+req);
				
				try {
					int x = req.getArg(0).intValue();
					int y = req.getArg(1).intValue();
					int res = x+y;
					ctx.out(tid, new LogicTuple("result", new Value(res)), (Long) null);
				} catch (Exception ex) {
					ex.printStackTrace();
					System.err.println("Error in processing the request!");
				}
				
			}
		}catch (Exception ex) {
			System.err.println("Problems in accessing Tucson!");
		}
		
	
	}//run

	public void operationCompleted(TupleCentreOperation op) {
		// TODO Auto-generated method stub
		
	}

}//class