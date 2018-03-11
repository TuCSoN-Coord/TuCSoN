package alice.tuplecentre.tucson.network.messages;

import alice.tuplecentre.tucson.network.messages.events.EventMsg;

/**
 * // TODO: 11/03/2018 add docs
 */
public interface TucsonMsg extends Message {
    EventMsg getEventMsg();
}
