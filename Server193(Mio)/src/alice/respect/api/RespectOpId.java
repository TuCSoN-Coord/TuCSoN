package alice.respect.api;

public class RespectOpId {

	private long id;
	
    public RespectOpId(long id){
		this.id = id;
	}
	
	public long getId(){
		return id;
	}

	public boolean equals(Object opid){
		return id==((RespectOpId)opid).id;
	}
	
	public String toString(){
		return ""+id;
	}
}
