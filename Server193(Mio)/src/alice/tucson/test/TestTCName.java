package alice.tucson.test;

import alice.tuplecentre.core.TupleCentreOperation;

import alice.logictuple.*;

import alice.tucson.api.*;

import alice.tucson.service.TucsonAgentLanguageManager;
import alice.tucson.service.TucsonLanguageManager;
import alice.tucson.service.TucsonOperation;
import alice.tucson.service.TucsonOperationCompletionListener;

public class TestTCName
{
	private TucsonOperationCompletionListener l = null;

	public TestTCName()
	{
		this.l = new Listener();
	}
	
	public TucsonOperationCompletionListener getListener()
	{
		return l;
	}
	
	public static void main(String[] args) throws Exception
	{
		// Viene utilizzato "ReSpecT 2.2"
		TucsonAgentLanguageManager.useLanguage(TucsonLanguageManager.DEFLANGUAGE);
		
		TucsonTupleCentreId tid = new TucsonTupleCentreId("'test-tc'");//before 'test-tc'

		TucsonContext cnt = Tucson.getContext(new TucsonAgentId("pippo"), TucsonAgentLanguageManager.getCurLanguage());

		// Chiamata bloccante
		//cnt.out(tid, new LogicTuple("done_out", new Value("p", new Value(1))), (Long) null);				
		
		// Chiamata non bloccante polling				
		//cnt.out(tid, new LogicTuple("done_out", new Value("p", new Value(1))), (TucsonOperationCompletionListener) null);
		
		//TucsonOperation op = (TucsonOperation) cnt.in(tid, new LogicTuple("done_out", new Value("p", new Value(1))), (TucsonOperationCompletionListener) null);
		
		//System.out.println("pippo agent is waiting for operation completion ...");
		
		//while(!op.isOperationCompleted())
		//{
		//Thread.sleep(1000);
		//}
		
		//System.out.println("Operation completed");
		
		//System.out.println("Operation result: " + op.getLogicTupleResult().toString());
		
		// Chiamata non bloccante con listener				
		cnt.out(tid, new LogicTuple("done_out", new Value("p", new Value(1))), (TucsonOperationCompletionListener) null);
		
		cnt.in(tid, new LogicTuple("done_out", new Value("p", new Value(1))), new TestTCName().getListener());				
		
//		while(true)
		{
			
		}
	}

	class Listener implements TucsonOperationCompletionListener
	{
		@Override
		public void operationCompleted(TupleCentreOperation op)
		{
			System.out.println("Operation result: " + ((TucsonOperation)op).getLogicTupleResult());		
		}
	}
}
