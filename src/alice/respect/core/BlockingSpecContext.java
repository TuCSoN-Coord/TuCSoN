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
import alice.respect.api.IBlockingSpecContext;
import alice.respect.api.IRespectTC;
import alice.respect.api.IRespectOperation;
import alice.respect.api.InvalidSpecificationException;
import alice.respect.api.OperationNotPossibleException;
import alice.respect.api.RespectSpecification;

/**
 * 
 * A Blocking Context wraps the access to a tuple centre virtual machine for a
 * specific thread of control, providing a blocking interface.
 * 
 * @author aricci
 */
public class BlockingSpecContext implements IBlockingSpecContext
{
	private IRespectTC core;

	public BlockingSpecContext(IRespectTC core_)
	{
		core = core_;
	}

	public void out_s(AgentId id, LogicTuple t) throws InvalidLogicTupleException, OperationNotPossibleException
	{
		if (t == null)
		{
			throw new InvalidLogicTupleException();
		}
		IRespectOperation op = core.out_s(id, t);
		op.waitForOperationCompletion();
	}

	public LogicTuple in_s(AgentId id, LogicTuple t) throws InvalidLogicTupleException, OperationNotPossibleException
	{
		if (t == null)
		{
			throw new InvalidLogicTupleException();
		}
		IRespectOperation op = core.in_s(id, t);
		op.waitForOperationCompletion();
		return unify(t, op.getLogicTupleResult());
	}

	public LogicTuple rd_s(AgentId id, LogicTuple t) throws InvalidLogicTupleException, OperationNotPossibleException
	{
		if (t == null)
		{
			throw new InvalidLogicTupleException();
		}
		IRespectOperation op = core.rd_s(id, t);
		op.waitForOperationCompletion();
		return unify(t, op.getLogicTupleResult());
	}

	public LogicTuple inp_s(AgentId id, LogicTuple t) throws InvalidLogicTupleException, OperationNotPossibleException
	{
		if (t == null)
		{
			throw new InvalidLogicTupleException();
		}
		IRespectOperation op = core.inp_s(id, t);
		op.waitForOperationCompletion();
		LogicTuple result = op.getLogicTupleResult();
		if (result == null)
		{
			return null;
		}
		else
		{
			return unify(t, op.getLogicTupleResult());
		}
	}

	public LogicTuple rdp_s(AgentId id, LogicTuple t) throws InvalidLogicTupleException, OperationNotPossibleException
	{
		if (t == null)
		{
			throw new InvalidLogicTupleException();
		}
		IRespectOperation op = core.rdp_s(id, t);
		op.waitForOperationCompletion();
		LogicTuple result = op.getLogicTupleResult();
		if (result == null)
		{
			return null;
		}
		else
		{
			return unify(t, op.getLogicTupleResult());
		}
	}

	private LogicTuple unify(alice.tuplecentre.api.TupleTemplate template, alice.tuplecentre.api.Tuple tuple)
	{
		boolean res = template.propagate(tuple);
		if (res)
		{
			return (LogicTuple) template;
		}
		else
		{
			return null;
		}
	}
}
