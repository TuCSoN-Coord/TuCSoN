package alice.tucson.test;

import alice.tucson.api.*;
import alice.logictuple.*;


class AgentA extends TucsonAgent {
	AgentA(AgentId aid) throws Exception{
		super(aid);
	}	
	protected void main(){
		try {
			String spec = "reaction(out(p(X)),( in_r(p(X)),out_r(pp(X)))).\n";
			TupleCentreId tid = new TupleCentreId("agora");

			TucsonOpId id = setSpec(tid,spec);
			
			ContextEvent ev = waitForCompletion(id,10000);
			if (ev == null){
				System.out.println("timeout in setting spec.");
			} else {
				System.out.println(ev.operationSucceeded());
			}
			
			id = getSpec(tid);
			ev = waitForCompletion(id,10000);
			if (ev == null){
				System.out.println("timeout in getting spec.");
			} else {
				System.out.println(ev.operationSucceeded());
				System.out.println(ev.getSpec());
			}
			
			out(tid,new LogicTuple("ready"));
			
		} catch (Exception ex){
			ex.printStackTrace();
		}
		
	}
}

class AgentB extends TucsonAgent {
	AgentB(AgentId aid) {
		super(aid);
	}	
	protected void main(){
		try {
			TupleCentreId tid = new TupleCentreId("agora");

			TucsonOpId id = rd(tid,new LogicTuple("ready"));
			waitForCompletion(id);
			
			for (int i=0; i<100; i++){
				out(tid, new LogicTuple("p",new Value(i)));
			}
			
		} catch (Exception ex){
			ex.printStackTrace();
		}
	}
	
	private void log(String msg){
		System.out.println("["+getId()+"] "+msg);
	}
}

class AgentC extends TucsonAgent {
	AgentC(AgentId aid) {
		super(aid);
	}	
	protected void main(){
		try {
			TupleCentreId tid = new TupleCentreId("agora");

			TucsonOpId id = rd(tid,new LogicTuple("ready"));
			waitForCompletion(id);
			
			for (int i=0; i<100; i++){
				id = in(tid, new LogicTuple("pp",new Var("X")));
				ContextEvent ev = waitForCompletion(id,2000);
				if (ev!=null){
					log(" "+ev.getTuple());
				} else {
					log("timeout");
					break;
				}
			}
			
		} catch (Exception ex){
			ex.printStackTrace();
		}
	}
	
	private void log(String msg){
		System.out.println("["+getId()+"] "+msg);
	}
}

public class Test {

	public static void main(String[] args) throws Exception {
		
		TucsonContext ctx = Tucson.getContext(new AgentId("main"));
		
		TupleCentreId tid = new TupleCentreId("agora");
		
		ctx.out(tid,new LogicTuple("p",new Value(1)));		
		System.out.println("out done.");
		
		TucsonOpId opId = ctx.rd(tid,new LogicTuple("p", new Var("X")));
		System.out.println("rd done.");
		
		ContextEvent ev = ctx.waitForCompletion(opId);		
		System.out.println(ev.getTuple());
		
		opId = ctx.in(tid,new LogicTuple("p", new Var("X")));
		System.out.println("in done.");
		
		ev = ctx.waitForCompletion(opId);		
		System.out.println(ev.getTuple());
		
//
		/*
		TucsonContext ctx1 = Tucson.enterDefaultContext();
		
		TupleCentreId tid = new TupleCentreId("agora");
		
		ctx1.setSpec(tid,"reaction(out(p(X)),( in_r(p(X)),out_r(pp(X)))).\n");
		
		ctx1.out(tid,new LogicTuple("p",new Value(1)));		
		System.out.println("out done.");
		
		String spec = ctx1.getSpec(tid);
		System.out.println("spec: "+spec);
		
		LogicTuple t = ctx1.rd(tid,new LogicTuple("p", new Var("X")));
		System.out.println(t);
		
		t = ctx1.in(tid,new LogicTuple("p", new Var("X")));
		System.out.println(t);
		*/
		
		
		
		
		/*
		TupleCentreId tid = new TupleCentreId("test");
				
		TucsonContextAsync cnt = Tucson.enterDefaultContextAsync();
		
		cnt.out(tid,new LogicTuple("p",new Value(1)));
		
		OpId id = cnt.in(tid,new LogicTuple("p",new Var("X")));
		
		ContextEvent ev = cnt.waitForEvent(id,10000);
		System.out.println(ev.getResultLogicTuple());
		
		id = cnt.rd(tid, new LogicTuple("q",new Var("X")));
		
		ev = cnt.waitForEvent(id,1000);
		if (ev == null){
			System.out.println("not present after 1000 ms.");
		}
		
		cnt.out(tid, new LogicTuple("q",new Value(2)));
		
		ev = cnt.waitForEvent(id,5000);
		if (ev == null){
			System.out.println("not present after 1000 ms.");
		} else {
			System.out.println(ev.getResultLogicTuple());
		}*/
		
		new AgentA(new AgentId("pippo")).spawn();
		new AgentB(new AgentId("pluto")).spawn();
		new AgentC(new AgentId("paperino")).spawn();
		

		
	}
}
