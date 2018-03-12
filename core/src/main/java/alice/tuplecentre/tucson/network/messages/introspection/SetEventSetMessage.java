package alice.tuplecentre.tucson.network.messages.introspection;

import java.util.List;

import alice.tuple.Tuple;

/**
 * // TODO: 12/03/2018 add documentation
 *
 * @author Enrico Siboni
 */
public interface SetEventSetMessage extends NodeMessage {

    /**
     * @return the eventWnSet
     */
    List<Tuple> getEventWnSet();

    /**
     * @param set the eventWnSet to set
     */
    void setEventWnSet(List<? extends Tuple> set);
}
