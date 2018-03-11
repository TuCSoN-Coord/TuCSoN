package alice.tuplecentre.tucson.network.messages.events;

import alice.tuple.logic.LogicTuple;
import alice.tuplecentre.api.OperationIdentifier;
import alice.tuplecentre.core.TupleCentreOpType;

/**
 * Output Event on a TupleCentre related to an operation
 * <p>
 * //TODO review this class documentation, added on the fly
 *
 * @author Enrico Siboni
 */
public interface OutputEventMsg extends EventMsg {

    /**
     * @return the id of the operation which caused the event
     */
    OperationIdentifier getOpId();

    /**
     * @return the type code of the operation which caused the event
     */
    TupleCentreOpType getOpType();

    /**
     * @return the logic tuple argument of the operation which caused the event
     */
    LogicTuple getTupleRequested();

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
