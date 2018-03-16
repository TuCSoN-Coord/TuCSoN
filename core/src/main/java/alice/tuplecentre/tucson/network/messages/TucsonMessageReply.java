package alice.tuplecentre.tucson.network.messages;

import alice.tuplecentre.tucson.network.messages.events.OutputEventMessage;

/**
 * @author Alessandro Ricci
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 * @author (contributor) Saverio Cicora
 * @author (contributor) Michele Bombardi (mailto:
 * michele.bombardi@studio.unibo.it)
 */
public class TucsonMessageReply extends AbstractTucsonMessage {

    /**
     * serialVersionUID
     **/
    private static final long serialVersionUID = 1L;

    /**
     * @param ev the events to transmit
     */
    public TucsonMessageReply(final OutputEventMessage ev) {
        setEventMsg(ev);
    }

    public TucsonMessageReply() {
    }

    @Override
    public OutputEventMessage getEventMsg() {
        return (OutputEventMessage) super.getEventMsg();
    }

    @Override
    public String toString() {
        final StringBuilder s = new StringBuilder(87);
        final OutputEventMessage oEv = this.getEventMsg();
        s.append("ID: ");
        s.append(oEv.getOpId());
        s.append("; Type: ");
        s.append(oEv.getOpType());
        s.append("; Tuple Requested: ");
        s.append(oEv.getTuple());
        s.append("; Tuple Result: ");
        s.append(oEv.getTupleResult());
        s.append("; Allowed: ");
        s.append(oEv.isAllowed());
        s.append("; Success: ");
        s.append(oEv.isSuccess());
        s.append("; Result Success: ");
        s.append(oEv.isResultSuccess());
        return s.toString();
    }
}
