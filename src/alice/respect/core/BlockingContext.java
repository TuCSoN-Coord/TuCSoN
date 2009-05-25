/*
 * ReSpecT - Copyright (C) aliCE team at deis.unibo.it
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
package alice.respect.core;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import alice.logictuple.*;
import alice.respect.api.AgentId;
import alice.respect.api.IBlockingContext;
import alice.respect.api.IRespectTC;
import alice.respect.api.IRespectOperation;
import alice.respect.api.InvalidSpecificationException;
import alice.respect.api.OperationNotPossibleException;
import alice.respect.api.RespectSpecification;
import alice.tuplecentre.api.Tuple;

/**
 *
 * A Blocking Context wraps the access to a tuple centre virtual machine
 * for a specific thread of control, providing a blocking interface.
 * 
 * @author aricci
 */
public class BlockingContext implements IBlockingContext {
    
    private IRespectTC core;
    
    public BlockingContext(IRespectTC core_){
        core=core_;
    }
    
    public void out(AgentId id, LogicTuple t) throws InvalidLogicTupleException, OperationNotPossibleException {
        if (t==null){
            throw new InvalidLogicTupleException();
        }
		IRespectOperation op = core.out(id,t);
		op.waitForOperationCompletion();
    }
    
    public LogicTuple in(AgentId id, LogicTuple t) throws InvalidLogicTupleException, OperationNotPossibleException {
        if (t==null){
            throw new InvalidLogicTupleException();
        }
		IRespectOperation op = core.in(id,t);
		op.waitForOperationCompletion();
        return unify(t,op.getLogicTupleResult());
    }
    
	public LogicTuple rd(AgentId id, LogicTuple t) throws InvalidLogicTupleException, OperationNotPossibleException {
		if (t==null){
			throw new InvalidLogicTupleException();
		}
		IRespectOperation op = core.rd(id,t);
		op.waitForOperationCompletion();
		return unify(t,op.getLogicTupleResult());
	}

	public LogicTuple inp(AgentId id, LogicTuple t) throws InvalidLogicTupleException, OperationNotPossibleException {
		if (t==null){
			throw new InvalidLogicTupleException();
		}
		IRespectOperation op = core.inp(id,t);
		op.waitForOperationCompletion();
		LogicTuple result = op.getLogicTupleResult();
		if (result==null){
			return null;
		} else {
			return unify(t,op.getLogicTupleResult());
		}
	}
    
	public LogicTuple rdp(AgentId id, LogicTuple t) throws InvalidLogicTupleException, OperationNotPossibleException {
		if (t==null){
			throw new InvalidLogicTupleException();
		}
		IRespectOperation op = core.rdp(id,t);
		op.waitForOperationCompletion();
		LogicTuple result = op.getLogicTupleResult();
		if (result==null){
			return null;
		} else {
			return unify(t,op.getLogicTupleResult());
		}
	}
  	
	/*public void set(AgentId aid, LogicTuple[] list) throws InvalidSpecificationException {
		core.set(aid,spec);
	}*/
	
	public List<LogicTuple> get(AgentId aid) throws OperationNotPossibleException {
		
		IRespectOperation op = core.get(aid);
		
		List<LogicTuple> res = new LinkedList<LogicTuple>();
		
		Iterator<Tuple> it = op.getLTupleResultList().iterator();
		
		while(it.hasNext()){
			res.add((LogicTuple)it.next());
		}
		return res;
	}
	
	public RespectSpecification getSpec(AgentId aid) {
		return core.getSpec(aid);
	}

	private LogicTuple unify(alice.tuplecentre.api.TupleTemplate template,alice.tuplecentre.api.Tuple tuple){
        boolean res=template.propagate(tuple);
        if (res){
            return (LogicTuple)template;
        } else {
            return null;
        }
    }

   
    
}

