package alice.respect.test;

import java.io.*;

public class Respect2P {

	public static void main(String[] args) throws Exception {
	
	    //System.out.println(new File(".").getAbsolutePath());

		InputStream is = new FileInputStream("src/alice/respect/test/manager.PL");
		alice.tuprolog.Agent agent = new alice.tuprolog.Agent(is,"go.");
		agent.spawn();
	    
		InputStream is1 = new FileInputStream("src/alice/respect/test/timed_consumer.PL");
		alice.tuprolog.Agent agent1 = new alice.tuprolog.Agent(is1,"go.");
		agent1.spawn();

		InputStream is2 = new FileInputStream("src/alice/respect/test/producer.PL");
		alice.tuprolog.Agent agent2 = new alice.tuprolog.Agent(is2,"produce(10).");
		agent2.spawn();

		System.out.println("agents spawn.");
		
	}
}
