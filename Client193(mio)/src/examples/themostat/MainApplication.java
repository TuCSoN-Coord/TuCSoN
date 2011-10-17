package examples.themostat;

import alice.tucson.api.*;

public class MainApplication
{
	public static void main(String[] args) throws Exception
	{		            
		TucsonAgentId aid1 = new TucsonAgentId("thermostat");
		TucsonAgentId aid2 = new TucsonAgentId("thermostatAgent");
		new Thermostat(aid1).spawn();
		new ThermostatAgent(aid2).spawn();
		
	}
}