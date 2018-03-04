package alice.tucson.introspection4gui;

import alice.tucson.api.TucsonTupleCentreIdDefault;
import alice.tuplecentre.api.Tuple;

public interface Inspector4GuiObserver {
	
	void onNewTupleCenter(TucsonTupleCentreIdDefault ttc);
	void onNewTuple(Tuple tuple, TucsonTupleCentreIdDefault ttc);
	void onRemovedTuple(Tuple tupleRemoved, TucsonTupleCentreIdDefault ttc);
	void onNewTrasfer(String tccSource, String tccDest, String tuple);
	
}
