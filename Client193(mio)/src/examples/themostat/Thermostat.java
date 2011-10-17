package examples.themostat;

import alice.logictuple.*;

import alice.tucson.api.*;
import alice.tucson.api.exception.*;
import alice.tuplecentre.core.TupleCentreOperation;

public class Thermostat extends TucsonAgent
{
	int tempCorr;
	int tempToSet;
	TucsonAgentId id;
	TucsonContext cnt; 
	TucsonTupleCentreId envTC; 


	public Thermostat(TucsonAgentId id) throws TucsonException
	{
		super(id);
		this.id=id;
		this.tempToSet=25;
	}		

	protected void mainPlan()
	{
		try {
			cnt = Tucson.getContext(id, null);
		} catch (ContextNotAvailableException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			envTC = new TucsonTupleCentreId("thermostat");
		} catch (TucsonInvalidTupleCentreIdException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		log("run");
		
		try
		{			
			while (true)
			{
				write(tempToSet);
				
				tempToSet = read();
												
				Thread.sleep(3000);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private int read() throws Exception
	{		
		LogicTuple tc = cnt.in(envTC, new LogicTuple("setTemp", new Var("T")), (Long) null);
		
		int temCorr = ((int) tc.getArg(0).intValue());		
		log("temperature to set = " + temCorr);
		
		return temCorr;
	}

	private void write(int temp) throws Exception
	{
		log("set the current temperature = " + temp);
		
		cnt.out(envTC, new LogicTuple("temp", new Value(temp)), (Long) null);
	}
	
	private void log(String l)
	{
		System.out.println("[ " + getTucsonAgentId() + " ] " + l);
	}

	
	@Override
	public void operationCompleted(TupleCentreOperation op) {
		// TODO Auto-generated method stub
		
	}
}
