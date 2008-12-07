package alice.respect.api;

import java.util.Iterator;

public interface ITCRegistry {
	
	
	public IRespectTC getTC(TupleCentreId id);
	
	public void addTC(IRespectTC tc);

}
