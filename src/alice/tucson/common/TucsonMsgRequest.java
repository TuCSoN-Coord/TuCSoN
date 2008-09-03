package alice.tucson.common;

import java.io.*;

import alice.tucson.api.*;
import alice.logictuple.*;

public class TucsonMsgRequest implements Serializable {

	private int id;
	private int type;
	private LogicTuple tuple;
	private TupleCentreId tid;
	
	protected TucsonMsgRequest(){
	}

	public TucsonMsgRequest(int id, int type, TupleCentreId tid, LogicTuple t){
		this.id = id;
		this.type = type;
		this.tid = tid;
		tuple = t;
	}

	public LogicTuple getTuple(){
		return tuple;
	}
	
	public int getId(){
		return id;
	}
	
	public TupleCentreId getTid(){
		return tid;
	}
	
	public int getType(){
		return type;
	}
		
    public static void write(DataOutputStream dout, TucsonMsgRequest msg) throws IOException{
        dout.writeInt(msg.getId());
        dout.writeInt(msg.getType());
        dout.writeUTF(msg.tid.getName());
        TupleSerialiser.writeTuple(dout,msg.getTuple());
    }

    public static TucsonMsgRequest read(DataInputStream din) throws Exception {
        		int id = din.readInt();
        		int type = din.readInt();
        		String name = din.readUTF();
        		LogicTuple t = TupleSerialiser.readTuple(din);
        		TucsonMsgRequest msg = new TucsonMsgRequest();
        		msg.id = id;
        		msg.type = type;
        		msg.tuple = t;
        		msg.tid = new TupleCentreId(name);
       		return msg;
    }    
}
