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


/**
 * A context for executing operations on a tuple centre.
 * 
 */
class Context implements ITupleCentre {

	private LocalContext ctx;
	
    public Context (AgentId id, RespectVM core){
		ctx = new LocalContext(id,core);        
    }


    public void out(LogicTuple t) throws InvalidLogicTupleException {
		ctx.out(t);
    }

    public LogicTuple in(LogicTuple t) throws InvalidLogicTupleException {
		return ctx.in(t);
    }

	public LogicTuple rd(LogicTuple t) throws InvalidLogicTupleException {
		return ctx.rd(t);
	}

	public LogicTuple inp(LogicTuple t) throws InvalidLogicTupleException {
		return ctx.inp(t);
	}

	public LogicTuple rdp(LogicTuple t) throws InvalidLogicTupleException {
		return ctx.rdp(t);
	}

    public void setSpec(String spec) throws InvalidSpecificationException {
        ctx.setSpec(spec);
    }

    public synchronized String getSpec(){
		return ctx.getSpec();
    }
    
    public TupleCentreId getId(){
    	return ctx.getId();
    }
}

