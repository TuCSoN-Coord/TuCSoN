package alice.tuplecentre.tucson.introspection4gui;

import alice.tuplecentre.api.Tuple;
import alice.tuplecentre.tucson.api.TucsonTupleCentreId;

public interface Inspector4GuiObserver {
	
	void onNewTupleCenter(TucsonTupleCentreId ttc);
	void onNewTuple(Tuple tuple, TucsonTupleCentreId ttc);
	void onRemovedTuple(Tuple tupleRemoved, TucsonTupleCentreId ttc);
	void onNewTrasfer(String tccSource, String tccDest, String tuple);
	
}
