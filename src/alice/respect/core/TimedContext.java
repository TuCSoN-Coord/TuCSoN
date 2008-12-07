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
import alice.logictuple.*;
import alice.respect.api.AgentId;
import alice.respect.api.IRespectTC;
import alice.respect.api.IRespectOperation;
import alice.respect.api.ITimedContext;
import alice.respect.api.OperationNotPossibleException;
import alice.respect.api.OperationTimeOutException;

/**
 *
 * A Timed Context wraps the access to a tuple centre virtual machine
 * for a specific thread of control, providing a timed interface.
 * 
 * @author aricci
 */
public class TimedContext implements ITimedContext  {
    
    private IRespectTC core;
    
    public TimedContext(IRespectTC core_){
        core=core_;
    }
    
    public void out(AgentId id, LogicTuple t,long ms) throws InvalidLogicTupleException, OperationNotPossibleException, OperationTimeOutException {
        if (t==null){
            throw new InvalidLogicTupleException();
        }
		IRespectOperation op = core.out(id,t);
		try {
			op.waitForOperationCompletion(ms);
		} catch (alice.tuplecentre.api.OperationTimeOutException  ex){
			throw new OperationTimeOutException(op);
		}
    }
    
    public LogicTuple in(AgentId id, LogicTuple t,long ms) throws InvalidLogicTupleException, OperationNotPossibleException, alice.respect.api.OperationTimeOutException {
        if (t==null){
            throw new InvalidLogicTupleException();
        }
		IRespectOperation op = core.in(id,t);
		try {
			op.waitForOperationCompletion(ms);
		} catch (Exception  ex){
			throw new alice.respect.api.OperationTimeOutException(op);
		}
		return unify(t,op.getLogicTupleResult());
    }
    
	public LogicTuple rd(AgentId id, LogicTuple t, long ms) throws InvalidLogicTupleException, OperationNotPossibleException, OperationTimeOutException {
		if (t==null){
			throw new InvalidLogicTupleException();
		}
		IRespectOperation op = core.rd(id,t);
		try {
			op.waitForOperationCompletion(ms);
		} catch (alice.tuplecentre.api.OperationTimeOutException ex){
			throw new OperationTimeOutException(op);
		}
		return unify(t,op.getLogicTupleResult());
	}

	public LogicTuple inp(AgentId id, LogicTuple t, long ms) throws InvalidLogicTupleException, OperationNotPossibleException, OperationTimeOutException {
		if (t==null){
			throw new InvalidLogicTupleException();
		}
		IRespectOperation op = core.inp(id,t);
		try {
			op.waitForOperationCompletion(ms);
		} catch (alice.tuplecentre.api.OperationTimeOutException  ex){
			throw new OperationTimeOutException(op);
		}
		LogicTuple result = op.getLogicTupleResult();
		if (result==null){
			return null;
		} else {
			return unify(t,op.getLogicTupleResult());
		}
	}
    
	public LogicTuple rdp(AgentId id, LogicTuple t,long ms) throws InvalidLogicTupleException, OperationNotPossibleException, OperationTimeOutException {
		if (t==null){
			throw new InvalidLogicTupleException();
		}
		IRespectOperation op = core.rdp(id,t);
		try {
			op.waitForOperationCompletion(ms);
		} catch (alice.tuplecentre.api.OperationTimeOutException  ex){
			throw new OperationTimeOutException(op);
		}
		LogicTuple result = op.getLogicTupleResult();
		if (result==null){
			return null;
		} else {
			return unify(t,op.getLogicTupleResult());
		}
	}
    
  	//
    
    private LogicTuple unify(alice.tuplecentre.api.TupleTemplate template,alice.tuplecentre.api.Tuple tuple){
        boolean res=template.propagate(tuple);
        if (res){
            return (LogicTuple)template;
        } else {
            return null;
        }
    }
}

