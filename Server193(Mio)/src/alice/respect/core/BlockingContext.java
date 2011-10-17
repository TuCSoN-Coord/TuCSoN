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
import java.util.List;

import alice.logictuple.*;
import alice.respect.api.AgentId;
import alice.respect.api.IBlockingContext;
import alice.respect.api.IRespectTC;
import alice.respect.api.IRespectOperation;
import alice.respect.api.OperationNotPossibleException;


/**
 *
 * A Blocking Context wraps the access to a tuple centre virtual machine
 * for a specific thread of control, providing a blocking interface.
 * 
 * @author aricci
 */


public class BlockingContext extends AbstractContext implements IBlockingContext {
    
    public BlockingContext(IRespectTC core){
    	//Before this.core= core;
    	/*insert by Enrico Rossignoli*/
        super(core);
    }
    
    public void out(AgentId id, LogicTuple t) throws InvalidLogicTupleException, OperationNotPossibleException {
        if (t==null){
            throw new InvalidLogicTupleException();
        }
        /*Before*/
        //IRespectOperation op = getCore().out(id,t);
        /*insert by Enrico Rossignoli*/
		IRespectOperation op = getCore().out(id,t);
		op.waitForOperationCompletion();
    }
    
    public LogicTuple in(AgentId id, LogicTuple t) throws InvalidLogicTupleException, OperationNotPossibleException {
        if (t==null){
            throw new InvalidLogicTupleException();
        }
        /*Before*/
        //IRespectOperation op = getCore().in(id,t);
        /*insert by Enrico Rossignoli*/
		IRespectOperation op = getCore().in(id,t);
		op.waitForOperationCompletion();
        return unify(t,op.getLogicTupleResult());
    }
    
	public LogicTuple rd(AgentId id, LogicTuple t) throws InvalidLogicTupleException, OperationNotPossibleException {
		if (t==null){
			throw new InvalidLogicTupleException();
		}
		/*Before*/
        //IRespectOperation op = getCore().rd(id,t);
        /*insert by Enrico Rossignoli*/
		IRespectOperation op = getCore().rd(id,t);
		op.waitForOperationCompletion();
		return unify(t,op.getLogicTupleResult());
	}

	public LogicTuple inp(AgentId id, LogicTuple t) throws InvalidLogicTupleException, OperationNotPossibleException {
		if (t==null){
			throw new InvalidLogicTupleException();
		}
		/*Before*/
        //IRespectOperation op = getCore().inp(id,t);
        /*insert by Enrico Rossignoli*/
		IRespectOperation op = getCore().inp(id,t);
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
		/*Before*/
        //IRespectOperation op = getCore().rdp(id,t);
        /*insert by Enrico Rossignoli*/
		IRespectOperation op = getCore().rdp(id,t);
		op.waitForOperationCompletion();
		LogicTuple result = op.getLogicTupleResult();
		if (result==null){
			return null;
		} else {
			return unify(t,op.getLogicTupleResult());
		}
	}
  	
	// Aggiunta la primitiva set
	public void set(AgentId aid, List<LogicTuple> tupleList) throws OperationNotPossibleException {
		/*Before*/
        //IRespectOperation op = getCore().set(aid, tupleList);
        /*insert by Enrico Rossignoli*/
		IRespectOperation op = getCore().set(aid, tupleList);
		op.waitForOperationCompletion();
		getCore().set(aid, tupleList);
	}
	
	public List<LogicTuple> get(AgentId aid) throws OperationNotPossibleException {
		
		/*Before*/
        //IRespectOperation op = getCore().get(aid);
        /*insert by Enrico Rossignoli*/
		IRespectOperation op = getCore().get(aid);
		op.waitForOperationCompletion();
		List<LogicTuple> res =  op.getTupleResultList();
		return res;
	}
	
	/*public RespectSpecification getSpec(AgentId aid) {
		return core.getSpec(aid);
	}*/

   
    
}

