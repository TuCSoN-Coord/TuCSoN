package alice.tucson.common;

import java.io.*;


import alice.logictuple.*;

public class TucsonMsgRequest implements Serializable
{
	/**
	 * modified by a.pedone
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private int type;
	private LogicTuple tuple;
	private String tid;		//new

	protected TucsonMsgRequest()
	{
	}

	public TucsonMsgRequest(long id, int type, String stcid, LogicTuple t)
	{
		this.id = id;
		this.type = type;
		this.tid = stcid;
		tuple = t;
	}

	public LogicTuple getTuple()
	{
		return tuple;
	}

	public long getId()
	{
		return id;
	}

	public String getTid()
	{
		return tid;
	}

	public int getType()
	{
		return type;
	}

	public static void write(ObjectOutputStream dout, TucsonMsgRequest msg) throws IOException
	{
		dout.writeLong(msg.getId());
		//System.out.println(msg.getId());  //ok
		dout.writeInt(msg.getType());
		//System.out.println(msg.getType());  //ok
		dout.writeObject(msg.getTid());
		//System.out.println(msg.getTid());  //ok
		dout.writeObject(msg.getTuple());
		//System.out.println(msg.getTuple());  //ok
	}

	public static TucsonMsgRequest read(ObjectInputStream din) throws Exception
	{
		long id = din.readLong();
		int type = din.readInt();
		String tid = (String) din.readObject();
		LogicTuple t = (LogicTuple) din.readObject();
		TucsonMsgRequest msg = new TucsonMsgRequest();
		msg.id = id;
		msg.type = type;
		msg.tuple = t;
		msg.tid = tid;
		return msg;
	}
}
