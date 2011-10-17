package examples.service;

import alice.tucson.api.TucsonAgentId;

public class ServiceProvider {
	
	public static void main(String[] args) throws Exception {
		TucsonAgentId aid = new TucsonAgentId("provider");
		new ServiceProviderAgent(aid).spawn();
	}

}
