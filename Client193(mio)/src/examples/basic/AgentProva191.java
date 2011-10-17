package examples.basic;

import alice.logictuple.*;
import alice.tucson.api.*;
import alice.tucson.api.exception.*;
import alice.tuplecentre.core.OperationTimeOutException;
import alice.tuplecentre.core.TupleCentreOperation;

public class AgentProva191 extends TucsonAgent {  //esempio funzionante per mobile tucson con tucsonagent  @a.pedone

	private TucsonAgentId id;
	
	protected AgentProva191(TucsonAgentId id) throws Exception {
		super(id);
		this.id=id;
	}

	
	protected void mainPlan() {
		
		TucsonTupleCentreId tid;
		TucsonContext cnt;
		
		long now = System.currentTimeMillis();
		LogicTuple tuple = new LogicTuple("msg", new Value("Hello world!"), new Value("time", new Value(now)));
		
		try {
			tid = new TucsonTupleCentreId("box@'192.168.1.68'"); //OCCHIOOOOOOOOOOO!
			
			cnt = Tucson.getContext(id, null);
			
			
			cnt.out(tid, tuple, (Long) null);
			System.out.println("Tuple inserted: " + tuple);

			LogicTuple template = new LogicTuple("msg", new Var("Msg"), new Var("Time"));
			LogicTuple msg = cnt.rd(tid, template, (Long) null);

			System.out.println("Tuple retrieved name: " + msg.getName());
			System.out.println("Msg argument: " + msg.getArg(0));
			System.out.println("Time argument: " + msg.getArg(1).getArg(0)); 
			
			
		} catch (TucsonInvalidTupleCentreIdException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  catch (ContextNotAvailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TucsonOperationNotPossibleException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnreachableNodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LanguageNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OperationTimeOutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
//		catch (InvalidTermNameException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} 
		catch (InvalidTupleOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}


	


	@Override
	public void operationCompleted(TupleCentreOperation op) {
		// TODO Auto-generated method stub
		
	}


}
