package alice.tuplecentre.tucson.network.messages;

import alice.tuplecentre.tucson.network.messages.events.EventMessage;

/**
 * @author Alessandro Ricci
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 * @author (contributor) Saverio Cicora
 * @author (contributor) Piscaglia Nicola
 */
abstract class AbstractTucsonMessage implements TucsonMessage {

    private static final long serialVersionUID = 1L;

    private EventMessage event;

    @Override
    public String toString() {
        return "Generic Tucson Message";
    }

    @Override
    public EventMessage getEventMsg() {
        return event;
    }

    /**
     * @param event the EventMessage to set
     */
    void setEventMsg(final EventMessage event) {
        this.event = event;
    }
}
