/*
 * ReSpecT Framework - aliCE team at deis.unibo.it
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package alice.respect.tools;
import alice.respect.api.*;
import alice.respect.core.*;

/**
 * The CLIAgent is a ReSpecT agent that enables   
 * access to a tuple centre through a GUI.
 * 
 */
public class Console extends Agent {

	IBlockingContext tc;
	String title;
	
	public Console(String title, String name, IBlockingContext tc) throws Exception {
		super(new AgentId(name));
		this.tc=tc;
		this.title = title;
	}

	public String getTitle(){
		
		return title;
	}
	
	protected void mainPlan(){ 
		GUIInterpreter form=new GUIInterpreter(getTitle(),getId(),tc);
		form.setSize(400,200);
    	form.setVisible(true);
    	
    }
	
	public static void main(String[] args) throws Exception{
		
		TupleCentreId id1 = new TupleCentreId("tc1@localhost");
		TupleCentreId id2 = new TupleCentreId("tc2@localhost");
		
		RespectTCContainer.createRespectTC(id1,10);
		RespectTCContainer.createRespectTC(id2,10);
		
		
		
		new Console("tc1","console_tc1",RespectTCContainer.getBlockingContext(new TupleCentreId("tc1@localhost"))).mainPlan();
		new Console("tc2", "console_tc2",RespectTCContainer.getBlockingContext(new TupleCentreId("tc2@localhost"))).mainPlan();
		
	}
}