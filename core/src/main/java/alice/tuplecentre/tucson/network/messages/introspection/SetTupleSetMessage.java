package alice.tuplecentre.tucson.network.messages.introspection;

import java.util.List;

import alice.tuple.Tuple;

/**
 * // TODO: 12/03/2018 add documentation
 *
 * @author Enrico Siboni
 */
public interface SetTupleSetMessage extends NodeMessage {

    /**
     * @return the tupleSet
     */
    List<Tuple> getTupleSet();

    /**
     * @param set the tupleSet to set
     */
    void setTupleSet(List<? extends Tuple> set);
}
