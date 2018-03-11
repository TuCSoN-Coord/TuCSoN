package alice.tucson.network.messages;

import alice.tucson.network.Message;
import alice.tucson.network.messages.events.EventMsg;

import java.io.Serializable;

public interface TucsonMsg extends Message {
    EventMsg getEventMsg();
}
