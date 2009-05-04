package alice.tucson.api;

import alice.logictuple.*;

public class ContextEvent
{
	private LogicTuple tuple;
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

	public String getSpec()
	{
		return spec;
	}
}
