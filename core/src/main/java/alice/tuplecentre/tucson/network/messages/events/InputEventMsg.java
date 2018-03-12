package alice.tuplecentre.tucson.network.messages.events;

import alice.tuplecentre.respect.api.geolocation.Position;

/**
 * Input event on a Tuple Centre related to an operation
 * <p>
 * //TODO review this class documentation, added on the fly
 *
 * @author Enrico Siboni
 */
public interface InputEventMsg extends EventMessage {

    /**
     * @return the Position where this event was generated
     */
    Position getPlace();

    /**
     * @return the String representation of the tuple centre currently handling
     * this event
     */
    String getReactingTC();

    /**
     * @return the String representation of the source of this event
     */
    String getSource();

    /**
     * @return the String representation of the target of this event
     */
    String getTarget();

    /**
     * @return the time at which this event was generated
     */
    long getTime();

}
