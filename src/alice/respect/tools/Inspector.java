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
import alice.respect.*;

/**
 * The Inspector is a ReSpecT agent that makes it possible to inspect 
 * and debug a tuple centre, providing a graphical user 
 * interface.
 */
public class Inspector extends Agent{
	
	private InspectorCore core;
	private InspectorFrame frame;

	
	public Inspector(String name, TupleCentre tc) throws Exception {
		super(new AgentId(name));
		core = new InspectorCore(tc);
		frame = new InspectorFrame(core);
		core.addInspectorContextListener(frame);
	}
	
	protected void main(){
		frame.show();		
	}
	
	
	
	
}
