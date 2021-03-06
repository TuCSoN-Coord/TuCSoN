package alice.tuplecentre.respect.api;

import alice.tuplecentre.core.InputEvent;
import alice.tuplecentre.respect.api.geolocation.Position;
import alice.tuplecentre.respect.api.place.IPlace;

/**
 * Interface to a ReSpecT Tuple Centre with spatial functionalities.
 *
 * @author Michele Bombardi (mailto: michele.bombardi@studio.unibo.it)
 */
public interface ISpatialContext {
    /**
     * Gets the current ReSpecT VM local time
     *
     * @return the vm local time
     */
    long getCurrentTime();

    /**
     * Gets the current ReSpecT VM position
     *
     * @return the vm position
     */
    Position getPosition();

    /**
     * Notifies a new input environment (spatial) events
     *
     * @param ev the events to handle
     */
    void notifyInputEnvEvent(InputEvent ev);

    /**
     * Sets a specified place in the ReSpecT vm position
     *
     * @param place the new place
     */
    void setPosition(IPlace place);
}
