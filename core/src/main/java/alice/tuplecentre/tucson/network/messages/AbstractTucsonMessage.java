package alice.tuplecentre.tucson.network.messages;

import alice.tuplecentre.tucson.network.messages.events.EventMsg;

/**
 * @author Alessandro Ricci
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 * @author (contributor) Saverio Cicora
 * @author (contributor) Piscaglia Nicola
 */
public abstract class AbstractTucsonMessage implements TucsonMessage {

    private static final long serialVersionUID = 1L;

    protected EventMsg event;

    @Override
    public String toString() {
        return "Generic Tucson Message";
    }

    @Override
    public EventMsg getEventMsg() {
        return event;
    }
}
