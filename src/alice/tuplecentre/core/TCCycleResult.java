package alice.tuplecentre.core;

import alice.tuplecentre.api.ITCCycleResult;
import alice.tuplecentre.api.Tuple;

public class TCCycleResult implements ITCCycleResult {

	private Tuple result;
	private long startTime, endTime;

	
	public TCCycleResult(){
		
		this.result = null;
		this.startTime = System.currentTimeMillis();
		
	}
	
	
	public void setResult(Tuple res) {
		this.result = res;
	}
	
	public Tuple getResult() {
		// TODO Auto-generated method stub
		return this.result;
	}

	public void setEndTime(long time) {
		// TODO Auto-generated method stub
		this.endTime = time;
	}
	
	public long getEndTime() {
		// TODO Auto-generated method stub
		return this.endTime;

	}

	public long getStartTime() {
		// TODO Auto-generated method stub
		return this.startTime;

	}


	public boolean isResultDefined() {
		// TODO Auto-generated method stub
		return this.result != null;
	}

}
