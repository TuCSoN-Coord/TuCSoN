package alice.tucson.common;

import java.io.*;

import alice.tucson.types.TucsonTupleCentreId;
import alice.logictuple.*;

public class TucsonMsgRequest implements Serializable {

	private int id;
	private int type;
	private LogicTuple tuple;
	private TucsonTupleCentreId tid;
	
	protected TucsonMsgRequest(){
	}

	public TucsonMsgRequest(int id, int type, TucsonTupleCentreId tid, LogicTuple t){
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
	
	public TucsonTupleCentreId getTid(){
		return tid;
	}
	
	public int getType(){
		return type;
	}
		
    public static void write(ObjectOutputStream dout, TucsonMsgRequest msg) throws IOException{
        dout.writeInt(msg.getId());
        dout.writeInt(msg.getType());
        dout.writeObject(msg.tid);
        dout.writeObject(msg.getTuple());
    }

    public static TucsonMsgRequest read(ObjectInputStream din) throws Exception {
    	int id = din.readInt();
    	int type = din.readInt();
    	TucsonTupleCentreId tid = (TucsonTupleCentreId) din.readObject();
    	LogicTuple t = (LogicTuple) din.readObject();
    	TucsonMsgRequest msg = new TucsonMsgRequest();
    	msg.id = id;
    	msg.type = type;
    	msg.tuple = t;
    	msg.tid = tid;
    	return msg;
    }    
}
