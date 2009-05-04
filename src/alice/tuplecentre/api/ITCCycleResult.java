package alice.tuplecentre.api;


public interface ITCCycleResult {
	
	public void setResult(Tuple res);
	
	public Tuple getResult();

	public void setEndTime(long time);
	
	public long getEndTime();

	public long getStartTime(); 
	
	public boolean isResultDefined();

}
