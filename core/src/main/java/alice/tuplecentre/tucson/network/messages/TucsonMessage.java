package alice.tuplecentre.tucson.network.messages;

import alice.tuplecentre.tucson.network.messages.events.EventMsg;

/**
 * // TODO: 11/03/2018 add docs
 *
 * @author Nicola Piscaglia
 */
public interface TucsonMessage extends Message {

    /**
     * @return the event message associated to this message
     */
    EventMsg getEventMsg();
}
