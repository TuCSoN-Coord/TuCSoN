package aaa.myAgent191;

import alice.logictuple.InvalidTupleOperationException;
import alice.logictuple.InvalidVarNameException;
import alice.logictuple.LogicTuple;
import alice.logictuple.Value;
import alice.logictuple.Var;
import alice.respect.api.Agent;
import alice.respect.api.AgentId;
import alice.respect.api.RespectException;
import alice.tucson.api.Tucson;
import alice.tucson.api.TucsonAgentId;
import alice.tucson.api.TucsonContext;
import alice.tucson.api.TucsonTupleCentreId;
import alice.tucson.api.exception.ContextNotAvailableException;
import alice.tucson.api.exception.LanguageNotFoundException;
import alice.tucson.api.exception.TucsonInvalidTupleCentreIdException;
import alice.tucson.api.exception.TucsonOperationNotPossibleException;
import alice.tucson.api.exception.UnreachableNodeException;
import alice.tucson.service.TucsonAgentLanguageManager;
import alice.tucson.service.TucsonLanguageManager;
import alice.tuplecentre.core.OperationTimeOutException;

public class AgentProva191 extends Agent {

	private AgentId id;
	
	protected AgentProva191(AgentId id) throws RespectException {
		super(id);
		this.id=id;
	}

	
	protected void mainPlan() {
		
		TucsonAgentLanguageManager.useLanguage(TucsonLanguageManager.DEFLANGUAGE);
				
		TucsonTupleCentreId tid;
		TucsonContext cnt;
		
		long now = System.currentTimeMillis();
		LogicTuple tuple = new LogicTuple("msg", new Value("Hello world!"), new Value("time", new Value(now)));
		
		try {
			tid = new TucsonTupleCentreId("default@localhost"); //OCCHIOOOOOOOOOOO!
			
			cnt = Tucson.getContext(id, TucsonAgentLanguageManager.getCurLanguage());
			
			
			cnt.out(tid, tuple, (Long) null);
			System.out.println("Tuple inserted: " + tuple);

			LogicTuple template = new LogicTuple("msg", new Var("Msg"), new Var("Time"));
			LogicTuple msg = cnt.rd(tid, template, (Long) null);

			System.out.println("Tuple retrieved name: " + msg.getName());
			System.out.println("Msg argument: " + msg.getArg(0));
			System.out.println("Time argument: " + msg.getArg(1).getArg(0)); 
			System.out.println(TucsonLanguageManager.getConfLanguage());
			System.out.println(TucsonLanguageManager.getConfResolver());
			
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
//		catch (InvalidVarNameException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} 
			catch (InvalidTupleOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}

}
