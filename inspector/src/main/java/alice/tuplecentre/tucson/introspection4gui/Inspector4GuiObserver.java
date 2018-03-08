package alice.tuplecentre.tucson.introspection4gui;

import alice.tuplecentre.api.Tuple;
import alice.tuplecentre.tucson.api.TucsonTupleCentreId;
import alice.tucson.api.TucsonTupleCentreId;
import alice.tuple.Tuple;

public interface Inspector4GuiObserver {
	
	void onNewTupleCenter(TucsonTupleCentreId ttc);
	void onNewTuple(Tuple tuple, TucsonTupleCentreId ttc);
	void onRemovedTuple(Tuple tupleRemoved, TucsonTupleCentreId ttc);
	void onNewTrasfer(String tccSource, String tccDest, String tuple);
	
}
