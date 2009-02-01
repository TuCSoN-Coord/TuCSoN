package alice.tucson.common;

import java.io.*;


import alice.logictuple.*;

public class TucsonMsgReply implements Serializable {

	static public LogicTuple NULL = new LogicTuple("null");
	private int id;
	private int type;
	private LogicTuple tuple_requested;
	private LogicTuple tuple_result;
	private boolean allowed;
	private boolean success;
	
	protected TucsonMsgReply(){
	}

	public TucsonMsgReply(int id, int type, boolean allowed,boolean success){
		this.id = id;
		this.type = type;
		this.allowed = allowed;
		this.success = success;
		tuple_requested = tuple_result = NULL;
	}

	public TucsonMsgReply(int id, int type, boolean allowed, boolean success, LogicTuple req, LogicTuple res){
		this.id = id;
		this.type = type;
		this.success = success;
		this.allowed = allowed;
		tuple_requested = req;
		tuple_result = res;
	}

	public LogicTuple getTupleRequested(){
		return tuple_requested;
	}

	public LogicTuple getTupleResult(){
		return tuple_result;
	}
	
	public int getId(){
		return id;
	}
	
	public int getType(){
		return type;
	}
	
	public boolean isSuccess(){
		return success;
	}
	public boolean isAllowed(){
		return allowed;
	}
	
    public static void write(ObjectOutputStream dout, TucsonMsgReply msg) throws IOException{
        dout.writeInt(msg.getId());
        dout.writeInt(msg.getType());
        dout.writeBoolean(msg.isAllowed());
        dout.writeBoolean(msg.isSuccess());
        dout.writeObject(msg.getTupleRequested());
        dout.writeObject(msg.getTupleResult());
    }

    public static TucsonMsgReply read(ObjectInputStream din) throws Exception {
        		int id = din.readInt();
        		int type = din.readInt();
        		boolean bool = din.readBoolean();
        		boolean succ = din.readBoolean();
        		LogicTuple treq = (LogicTuple) din.readObject();
        		LogicTuple tres = (LogicTuple) din.readObject();
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
