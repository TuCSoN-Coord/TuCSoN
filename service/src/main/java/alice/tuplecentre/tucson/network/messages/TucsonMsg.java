package alice.tuplecentre.tucson.network.messages;

import alice.tuplecentre.tucson.network.Message;
import alice.tuplecentre.tucson.network.messages.events.EventMsg;

public interface TucsonMsg extends Message {
    EventMsg getEventMsg();
}
