package alice.tucson.api;

import java.util.List;

import alice.logictuple.*;

public class ContextEvent
{
	private LogicTuple tuple;
	private List<LogicTuple> tupleList;
	private boolean allowed;
	private TucsonOpId opId;
	private String spec;
	private boolean success;

	public ContextEvent(TucsonOpId opId, boolean allowed, boolean success)
	{
		this.opId = opId;
		this.allowed = allowed;
		this.success = success;
	}

	public ContextEvent(TucsonOpId opId, boolean allowed, boolean success, LogicTuple tuple)
	{
		this.opId = opId;
		this.allowed = allowed;
		this.success = success;
		this.tuple = tuple;
	}

	public ContextEvent(TucsonOpId opId, boolean allowed, boolean success, String spec)
	{
		this.opId = opId;
		this.allowed = allowed;
		this.success = success;
		this.spec = spec;
	}
	
	public ContextEvent(TucsonOpId opId, boolean allowed, boolean success, List<LogicTuple> tupleList)
	{
		this.opId = opId;
		this.allowed = allowed;
		this.success = success;
		this.tupleList = tupleList;
	}

	public boolean operationAllowed()
	{
		return allowed;
	}

	public boolean operationSucceeded()
	{
		return success;
	}

	public TucsonOpId getOpId()
	{
		return opId;
	}

	public LogicTuple getTuple()
	{
		return tuple;
	}
	
	public List<LogicTuple> getSetTuple()
	{
		return tupleList;
	}

	public String getSpec()
	{
		return spec;
	}
}
