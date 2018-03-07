package alice.tuplecentre.tucson.network;

import java.io.Serializable;

import alice.tuplecentre.tucson.service.OutputEventMsg;

/**
 * 
 * @author Alessandro Ricci
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 * @author (contributor) Saverio Cicora
 * @author (contributor) Michele Bombardi (mailto:
 *         michele.bombardi@studio.unibo.it)
 * 
 */
public class TucsonMsgReply implements Serializable {
    /** serialVersionUID **/
    private static final long serialVersionUID = 1L;
    private OutputEventMsg outputEvent;

    /**
     * 
     * @param ev
     *            the event to transmit
     */
    public TucsonMsgReply(final OutputEventMsg ev) {
        this.outputEvent = ev;
    }

    /**
     * 
     */
    protected TucsonMsgReply() {
        /*
         * 
         */
    }

    /**
     * 
     * @return the OutputEvent this message veichles
     */
    public OutputEventMsg getOutputEvent() {
        return this.outputEvent;
    }

    @Override
    public String toString() {
        final StringBuffer s = new StringBuffer(87);
        final OutputEventMsg oEv = this.getOutputEvent();
        s.append("Identifier: ");
        s.append(oEv.getOpId());
        s.append("; Type: ");
        s.append(oEv.getOpType());
        s.append("; Tuple Requested: ");
        s.append(oEv.getTupleRequested());
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
