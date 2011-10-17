package alice.tucson.test;

import alice.logictuple.*;

import alice.tucson.api.*;
import alice.tucson.api.exception.TucsonInvalidAgentIdException;

import alice.tucson.service.TucsonAgentLanguageManager;
import alice.tucson.service.TucsonLanguageManager;
import alice.tucson.service.TucsonOperation;
import alice.tucson.service.TucsonOperationCompletionListener;
import alice.tuplecentre.core.TupleCentreOperation;

class AgentA extends TucsonAgent
{
	AgentA(TucsonAgentId aid, String language) throws Exception
	{
		super(aid, language);
		
		// Definisco il TucsonContex per l'agente
		super.setContext(Tucson.getContext(super.getTucsonAgentId(), TucsonAgentLanguageManager.getCurLanguage()));
	}
	
	protected void main()
	{
		try
		{
			String spec = "reaction(out(p(X)),( in_r(p(X)),out_r(pp(X)))).\n";
			TucsonTupleCentreId tid = new TucsonTupleCentreId("agora");

			super.getContext().setSpec(tid, spec, (Long) null);
						
			super.getContext().getSpec(tid, (Long) null);
						
			super.getContext().out(tid, new LogicTuple("ready"), (TucsonOperationCompletionListener) null);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	@Override
	public void operationCompleted(TupleCentreOperation op)
	{
		// TODO Auto-generated method stub		
	}
}

class AgentB extends TucsonAgent
{
	AgentB(TucsonAgentId aid, String language) throws TucsonInvalidAgentIdException
	{
		super(aid, language);
	}

	protected void main()
	{
		try
		{
			TucsonTupleCentreId tid = new TucsonTupleCentreId("agora");

			TucsonOperation op = (TucsonOperation) (super.getContext().rd(tid, new LogicTuple("ready"), (TucsonOperationCompletionListener) null));
			
			op.waitForOperationCompletion();
								
			// Operazione completata
			System.out.println(op.isSuccessed());
			
			for (int i = 0; i < 100; i++)
			{
				op = (TucsonOperation) (super.getContext().out(tid, new LogicTuple("p", new Value(i)), (TucsonOperationCompletionListener) null));
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	@SuppressWarnings("unused")
	private void log(String msg)
	{
		System.out.println("[" + this.getTucsonAgentId() + "] " + msg);
	}

	@Override
	public void operationCompleted(TupleCentreOperation op)
	{
		// TODO Auto-generated method stub	
	}
}

class AgentC extends TucsonAgent
{
	AgentC(TucsonAgentId aid, String language) throws TucsonInvalidAgentIdException
	{
		super(aid, language);
	}

	protected void main()
	{
		try
		{
			TucsonTupleCentreId tid = new TucsonTupleCentreId("agora");

			TucsonOperation op = (TucsonOperation) (super.getContext().rd(tid, new LogicTuple("ready"), (TucsonOperationCompletionListener) null));
			
			op.waitForOperationCompletion();
			
			for (int i = 0; i < 100; i++)
			{
				op = (TucsonOperation) (super.getContext().in(tid, new LogicTuple("pp", new Var("X")), (TucsonOperationCompletionListener) null));
				
				op.waitForOperationCompletion(2000);
				
				if(!op.isOperationCompleted())
					return;
				
				if (op.isSuccessed())
					log(" " + op.getLogicTupleResult());
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	private void log(String msg)
	{
		System.out.println("[" + this.getTucsonAgentId() + "] " + msg);
	}

	@Override
	public void operationCompleted(TupleCentreOperation op)
	{
		// TODO Auto-generated method stub		
	}
}

public class Test
{
	public static void main(String[] args) throws Exception
	{
		// Viene utilizzato "ReSpecT 2.2"
		TucsonAgentLanguageManager.useLanguage(TucsonLanguageManager.DEFLANGUAGE);
		
		AgentA agentA = new AgentA(new TucsonAgentId("pippo"), TucsonLanguageManager.DEFLANGUAGE);		
		agentA.spawn();				
		
		AgentB agentB = new AgentB(new TucsonAgentId("pluto"), TucsonLanguageManager.DEFLANGUAGE);		
		agentB.spawn();
		
		AgentC agentC = new AgentC(new TucsonAgentId("paperino"), TucsonLanguageManager.DEFLANGUAGE);
		agentC.spawn();				
	}
}
