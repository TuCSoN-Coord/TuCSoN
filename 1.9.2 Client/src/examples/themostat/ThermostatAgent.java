package examples.themostat;

import alice.logictuple.*;

import alice.tucson.api.*;
import alice.tucson.api.exception.*;
import alice.tuplecentre.core.TupleCentreOperation;

public class ThermostatAgent extends TucsonAgent
{
	int tempCorr;
	TucsonAgentId id;
	TucsonContext cnt;
	TucsonTupleCentreId envTC;


	public ThermostatAgent(TucsonAgentId id) throws TucsonException
	{
		super(id);
		this.id=id;		
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
				tempCorr = sense();
				tempCorr = checkChangeTemperature(tempCorr);
				act(tempCorr);
				
				Thread.sleep(5000);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private int sense() throws Exception
	{		
		LogicTuple tc = cnt.in(envTC, new LogicTuple("temp", new Var("T")), (Long) null);
		log("sensing");
		
		int temCorr = ((int) tc.getArg(0).intValue());		
		log("current temperature = " + temCorr);
		
		return temCorr;
	}

	private int checkChangeTemperature(int TempCorr)
	{
		log("checking");
		
		if(TempCorr >= 22)
		{
			log("temperature too high: " + TempCorr);
			log("change the temperature...");				
			TempCorr--;
			log("new temperature: " + TempCorr);
		}
		else if(TempCorr <= 18)
		{
			log("temperature too low: " + TempCorr);
			log("change the temperature...");				
			TempCorr++;
			log("new temperature: " + TempCorr);
		}
		else
			log("temperature is perfect: " + TempCorr);	
		
		return TempCorr;
	}
	
	private void act(int newTemp) throws Exception
	{
		log("acting");		
		log("give the new temperature = " + newTemp);
		
		cnt.out(envTC, new LogicTuple("setTemp", new Value(newTemp)), (Long) null);
	}
	
	private void log(String l)
	{
		System.out.println("[ " + getTucsonAgentId() + " ] " + l);
	}

	public void operationCompleted(TupleCentreOperation op) {
		// TODO Auto-generated method stub
		
	}
}
