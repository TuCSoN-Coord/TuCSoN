package alice.tuplecentre.tucson.network.messages.events;

import alice.tuple.logic.LogicTuple;
import alice.tuplecentre.api.OperationIdentifier;
import alice.tuplecentre.core.TupleCentreOpType;

/**
 * Abstract class for event messages
 *
 * @author Enrico Siboni
 */
abstract class AbstractEventMessage implements EventMessage {

    private final TupleCentreOpType opType;
    private final LogicTuple tuple;
    private final OperationIdentifier opId;


    AbstractEventMessage(final TupleCentreOpType opType, final LogicTuple tuple, final OperationIdentifier operationIdentifier) {
        this.opType = opType;
        this.tuple = tuple;
        this.opId = operationIdentifier;
    }

    @Override
    public OperationIdentifier getOpId() {
        return this.opId;
    }

    @Override
    public TupleCentreOpType getOpType() {
        return this.opType;
    }

    @Override
    public LogicTuple getTuple() {
        return this.tuple;
    }
}
