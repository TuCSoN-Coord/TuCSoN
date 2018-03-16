package alice.tuplecentre.tucson.network.messages.events;

/**
 * Output Event on a TupleCentre related to an operation
 * <p>
 * //TODO review this class documentation, added on the fly
 *
 * @author Enrico Siboni
 */
public interface OutputEventMessage extends EventMessage {

    /**
     * @return the effect of the event
     */
    Object getTupleResult();

    /**
     * @return wether the event was allowed
     */
    boolean isAllowed();

    /**
     * @return wether the effect has been applied succesfully
     */
    boolean isResultSuccess();

    /**
     * @return wether the event has been handled succesfully
     */
    boolean isSuccess();
}
