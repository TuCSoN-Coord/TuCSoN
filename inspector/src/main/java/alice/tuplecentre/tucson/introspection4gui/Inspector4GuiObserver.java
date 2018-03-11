package alice.tuplecentre.tucson.introspection4gui;

import alice.tuple.Tuple;
import alice.tuplecentre.tucson.api.TucsonTupleCentreId;

public interface Inspector4GuiObserver {

    void onNewTupleCenter(final TucsonTupleCentreId ttc);

    void onNewTuple(final Tuple tuple, final TucsonTupleCentreId ttc);

    void onRemovedTuple(final Tuple tupleRemoved, final TucsonTupleCentreId ttc);

    void onNewTrasfer(final String tccSource, final String tccDest, final String tuple);

}
