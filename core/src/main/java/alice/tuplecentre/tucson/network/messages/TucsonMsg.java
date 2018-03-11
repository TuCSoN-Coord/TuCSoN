package alice.tuplecentre.tucson.network.messages;

import alice.tuplecentre.tucson.network.Message;
import alice.tuplecentre.tucson.network.messages.events.EventMsg;

import java.io.Serializable;

public interface TucsonMsg extends Message {
    EventMsg getEventMsg();
}
