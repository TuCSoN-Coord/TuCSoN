package alice.tuplecentre.tucson.network.messages.events;

import alice.tuplecentre.respect.api.geolocation.Position;

/**
 * Input event on a Tuple Centre related to an operation
 * <p>
 * //TODO review this class documentation, added on the fly
 *
 * @author Enrico Siboni
 */
public interface InputEventMessage extends EventMessage {

    /**
     * @return the Position where this event was generated
     */
    Position getPlace();

    /**
     * @return the String representation copyOf the tuple centre currently handling
     * this event
     */
    String getReactingTC();

    /**
     * @return the String representation copyOf the source copyOf this event
     */
    String getSource();

    /**
     * @return the String representation copyOf the target copyOf this event
     */
    String getTarget();

    /**
     * @return the time at which this event was generated
     */
    long getTime();

}
