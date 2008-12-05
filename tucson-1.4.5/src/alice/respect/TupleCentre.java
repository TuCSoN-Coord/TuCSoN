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
package alice.respect;
import alice.logictuple.*;
import java.util.*;

/**
 * This class represents a ReSpecT tuple centre.
 * 
 */
public class TupleCentre implements ITupleCentre {

    private RespectVM  core;
    private HashMap contexts=new HashMap();

	public TupleCentre(TupleCentreId id){
		core=new RespectVM(id);
		new Thread(core).start();
		while (!core.isReady()){
			try{
				Thread.sleep(10);
			}catch(Exception ex){
			}
		}
	}
	

	TupleCentre(TupleCentreId id, alice.tuplecentre.OutputEventListener l){
		core=new RespectVM(id,l);
		new Thread(core).start();
		while (!core.isReady()){
			try{
				Thread.sleep(10);
			}catch(Exception ex){
			}
		}
	}
	
	public TupleCentre(String name) throws InvalidTupleCentreIdException {
		this(new TupleCentreId(name));
	}


    public void out(LogicTuple t) throws InvalidLogicTupleException {
        LocalContext context=resolveContext();
        context.out(t);
    }

    public LogicTuple in(LogicTuple t) throws InvalidLogicTupleException {
        LocalContext context=resolveContext();
        return context.in(t);
    }

    public LogicTuple rd(LogicTuple t)  throws InvalidLogicTupleException {
        LocalContext context=resolveContext();
        return context.rd(t);
    }

    public LogicTuple inp(LogicTuple t)  throws InvalidLogicTupleException {
        LocalContext context=resolveContext();
        return context.inp(t);
    }

    public LogicTuple rdp(LogicTuple t) throws InvalidLogicTupleException  {
        LocalContext context=resolveContext();
        return context.rdp(t);
    }

    public void setSpec(String spec) throws InvalidSpecificationException {
        LocalContext context=resolveContext();
        context.setSpec(spec);
    }

    public String getSpec() {
        LocalContext context=resolveContext();
        return context.getSpec();
    }

	public TupleCentreId getId(){
		return (TupleCentreId)core.getId();
	}
	
	public RespectVM getCore(){
		return core;
	}
	
    private synchronized LocalContext resolveContext(){
        LocalContext context=(LocalContext)contexts.get(Thread.currentThread());
        if (context==null){
            String name="'"+Thread.currentThread().toString()+"'";
            try {
                context=new LocalContext(new AgentId(name),core);
                contexts.put(Thread.currentThread(),context);
            } catch (Exception ex){
            }
        }
        return context;
    }
        
}

