package alice.tucson.network;

import java.io.Serializable;
import alice.tucson.service.InputEventMsg;

/**
 * 
 * @author Alessandro Ricci
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 * @author (contributor) Saverio Cicora
 * @author (contributor) Michele Bombardi (mailto:
 *         michele.bombardi@studio.unibo.it)
 * 
 */
public class TucsonMsgRequest implements Serializable {
    /** serialVersionUID **/
    private static final long serialVersionUID = 1L;
    private InputEventMsg inputEvent;

    /**
     * 
     * @param ev
     *            the event to transmit
     */
    public TucsonMsgRequest(final InputEventMsg ev) {
        this.inputEvent = ev;
    }

    /**
     * 
     */
    protected TucsonMsgRequest() {
        /*
         * 
         */
    }

    /**
     * 
     * @return The InputEvent this message veichles
     */
    public InputEventMsg getInputEventMsg() {
        return this.inputEvent;
    }

    @Override
    public String toString() {
        final StringBuffer s = new StringBuffer(45);
        final InputEventMsg iEv = this.getInputEventMsg();
        s.append("ID: ");
        s.append(iEv.getOpId());
        s.append("; Type: ");
        s.append(iEv.getOpType());
        s.append("; TID: ");
        s.append(iEv.getReactingTC());
        s.append("; Tuple: ");
        s.append(iEv.getTuple());
        s.append("; Time: ");
        s.append(iEv.getTime());
        s.append("; Place: ");
        s.append(iEv.getPlace());
        return s.toString();
    }
}
