package alice.tuplecentre;

/**
 * @author aricci
 */
public class TrapEvent extends alice.tuplecentre.Event {
	
	private long time;
	private Tuple tuple;
	
	public TrapEvent(long time, Tuple tuple){
		super(-1,null,null);
		this.time=time;
		this.tuple=tuple;	
	}
	
	public long getTime(){
		return time;
	}
	
	public Tuple getTuple(){
		return tuple;
	}

	//
	
	public String      getOperationName(){
		return "trap";
	}

	public int         getOperationNumArguments(){
		return 1;
	}

	public boolean isToTupleCentre(){
		return false;
	}

	public boolean isFromTupleCentre(){
		return false;
	}
	
	public boolean isSuccess(){
		return false;
	}

	public  boolean isFailure(){
		return false;
	}

	public  boolean isException(){
		return true;
	}
	
}

