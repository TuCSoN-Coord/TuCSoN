package alice.respect.api;

import java.util.Iterator;

public interface ITCRegistry {
	
	
	public IRespectTC getTC(TupleCentreId id) throws InstantiationNotPossibleException;
	
	public void addTC(IRespectTC tc);

}
