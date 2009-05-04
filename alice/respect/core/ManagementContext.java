package alice.respect.core;

import alice.logictuple.LogicTuple;
import alice.respect.api.IManagementContext;
import alice.respect.api.InvalidSpecificationException;
import alice.respect.api.OperationNotPossibleException;
import alice.respect.api.RespectSpecification;

public class ManagementContext implements IManagementContext
{
	private RespectVM vm;
	private Thread vmThread;

	public ManagementContext(RespectVM vm, Thread th)
	{
		this.vm = vm;
		vmThread = th;
	}

	public void setSpec(RespectSpecification spec) throws InvalidSpecificationException
	{
		boolean accepted = vm.setReactionSpec(spec);
		if (!accepted)
		{
			throw new InvalidSpecificationException();
		}
	}

	public RespectSpecification getSpec()
	{
		return (RespectSpecification) vm.getReactionSpec();
	}

	public boolean abortOperation(long opId)
	{
		return vm.abortOperation(opId);
	}

	//

	public void setManagementMode(boolean activate)
	{
		vm.setManagementMode(activate);
	}

	public void stopCommand() throws OperationNotPossibleException
	{
		try
		{
			vm.stopCommand();
		}
		catch (Exception ex)
		{
			throw new OperationNotPossibleException();
		}
	}

	public void goCommand() throws OperationNotPossibleException
	{
		try
		{
			vm.goCommand();
		}
		catch (Exception ex)
		{
			throw new OperationNotPossibleException();
		}
	}

	public void nextStepCommand() throws OperationNotPossibleException
	{
		try
		{
			vm.nextStepCommand();
		}
		catch (Exception ex)
		{
			throw new OperationNotPossibleException();
		}
	}

	public void setSpy(boolean what)
	{
		vm.setSpy(what);
	}

	public LogicTuple[] getTSet(LogicTuple t)
	{
		return vm.getTSet(t);
	}

	public LogicTuple[] getWSet(LogicTuple t)
	{
		return vm.getWSet(t);
	}

	public LogicTuple[] getTRSet(LogicTuple t)
	{
		return vm.getTRSet(t);
	}

	//

}
