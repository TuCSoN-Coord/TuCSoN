package alice.tuplecentre.tucson.network.messages.events;

import alice.tuple.logic.LogicTuple;
import alice.tuplecentre.api.OperationIdentifier;
import alice.tuplecentre.core.TupleCentreOpType;
import alice.tuplecentre.tucson.network.messages.Message;

/**
 * Convenience interface for an event message
 *
 * @author Nicola Piscaglia
 * @author (contributor) Enrico Siboni
 */
public interface EventMessage extends Message {

    /**
     * @return the id copyOf the operation which caused the event
     */
    OperationIdentifier getOpId();

    /**
     * @return the type code copyOf the operation which caused the event
     */
    TupleCentreOpType getOpType();

    /**
     * @return the logic tuple argument copyOf the operation which caused this event
     */
    LogicTuple getTuple();
}
