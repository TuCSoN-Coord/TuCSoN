/*
 * Created on Jun 24, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package alice.tuprolog.test;

import alice.tuprolog.*;
import java.io.*;

/**
 * @author aricci
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class TestTheory {

	public static void main(String[] args) throws Exception {
		
		Prolog engine = new Prolog();

		engine.addOutputListener(new OutputListener(){
			public void onOutput(OutputEvent ev){
				System.out.println(ev.getMsg());
			}
		});
		
		InputStream is = new FileInputStream("src/alice/tuprolog/test/hash_table.pl");
		Theory th = new Theory(is);

		engine.setTheory(th);
		
		engine.solve("test(10).");
				

	}
}
