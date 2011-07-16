package alice.tucson.common;

import java.io.*;
import java.util.List;

import alice.logictuple.*;

public class TucsonMsgReply implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//static public LogicTuple NULL = new LogicTuple("null");
	private long id;
	private int type;
	private LogicTuple tuple_requested;
	private Object tuple_result;
	private boolean allowed;
	private boolean success;

	protected TucsonMsgReply()
	{
	}

	public TucsonMsgReply(long id, int type, boolean allowed, boolean success)
	{
		this.id = id;
		this.type = type;
		this.allowed = allowed;
		this.success = success;
		tuple_requested = null;
		tuple_result = null;
	}

	public TucsonMsgReply(long id, int type, boolean allowed, boolean success, LogicTuple req, Object res)
	{
		this.id = id;
		this.type = type;
		this.success = success;
		this.allowed = allowed;
		tuple_requested = req;
		tuple_result = res;
	}
	
	public LogicTuple getTupleRequested()
	{
		return tuple_requested;
	}

	public Object getTupleResult()
	{
		return tuple_result;
	}
	
	public long getId()
	{
		return id;
	}

	public int getType()
	{
		return type;
	}

	public boolean isSuccess()
	{
		return success;
	}

	public boolean isAllowed()
	{
		return allowed;
	}

	public static void write(ObjectOutputStream dout, TucsonMsgReply msg) throws IOException
	{
		dout.writeLong(msg.getId());
		//System.out.println(msg.getId());
		dout.writeInt(msg.getType());
		//System.out.println(msg.getType());
		dout.writeBoolean(msg.isAllowed());
		//System.out.println(msg.isAllowed());
		dout.writeBoolean(msg.isSuccess());
		//System.out.println(msg.isSuccess());
		dout.writeObject(msg.getTupleRequested());
		//System.out.println(msg.getTupleRequested());
		dout.writeObject(msg.getTupleResult());
		//System.out.println(msg.getTupleResult());		
	}

	public static TucsonMsgReply read(ObjectInputStream din) throws Exception
	{
		long id = din.readLong();
		int type = din.readInt();
		boolean bool = din.readBoolean();
		boolean succ = din.readBoolean();
		LogicTuple treq = (LogicTuple) din.readObject();
		Object tres = din.readObject();
		TucsonMsgReply msg = new TucsonMsgReply();
		msg.id = id;
		msg.type = type;
		msg.allowed = bool;
		msg.success = succ;
		msg.tuple_requested = treq;
		msg.tuple_result = tres;
		return msg;
	}
}
