package alice.tuplecentre.tucson.service;

import java.io.Serializable;

import alice.logictuple.LogicTuple;
import alice.tuplecentre.core.TupleCentreOpType;
import alice.tuplecentre.respect.api.geolocation.Position;

/**
 * Input event on a Tuple Centre related to an operation
 *
 * //TODO review this class documentation, added on the fly
 *
 * @author Enrico Siboni
 */
public interface InputEventMsg extends Serializable {

    /**
     * @return the id of the operation which caused this event
     */
    long getOpId();

    /**
     * @return the type code of the operation which caused this event
     */
    TupleCentreOpType getOpType();

    /**
     * @return the Position where this event was generated
     */
    Position getPlace();

    /**
     * @return the String representation of the tuple centre currently handling
     * this event
     */
    String getReactingTC();

    /**
     * @return the String representation of the source of this event
     */
    String getSource();

    /**
     * @return the String representation of the target of this event
     */
    String getTarget();

    /**
     * @return the time at which this event was generated
     */
    long getTime();

    /**
     * @return the logic tuple argument of the operation which caused this event
     */
    LogicTuple getTuple();
}
