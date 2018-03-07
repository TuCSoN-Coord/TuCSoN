package alice.tuplecentre.respect.api;

import java.util.List;

import alice.logictuple.LogicTuple;
import alice.logictuple.exceptions.InvalidLogicTupleException;
import alice.tuplecentre.core.InputEvent;
import alice.tuplecentre.respect.api.exceptions.OperationNotPossibleException;

/**
 * A ReSpecT Tuple Centre Interface to issue ReSpecT ordinary primitives using a
 * synchronous semantics.
 *
 * @author Alessandro Ricci
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 * @author (contributor) Michele Bombardi (mailto:
 * michele.bombardi@studio.unibo.it)
 */
public interface IOrdinarySynchInterface {
    /**
     * @param ev the event to handle
     * @return the result of the operation
     * @throws OperationNotPossibleException if the operation requested cannot be carried out
     */
    List<LogicTuple> get(InputEvent ev) throws OperationNotPossibleException;

    /**
     * @param ev the event to handle
     * @return the result of the operation
     * @throws InvalidLogicTupleException    if the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException if the operation requested cannot be carried out
     */
    LogicTuple in(InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * @param ev the event to handle
     * @return the result of the operation
     * @throws InvalidLogicTupleException    if the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException if the operation requested cannot be carried out
     */
    List<LogicTuple> inAll(InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * @param ev the event to handle
     * @return the result of the operation
     * @throws InvalidLogicTupleException    if the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException if the operation requested cannot be carried out
     */
    LogicTuple inp(InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * @param ev the event to handle
     * @return the result of the operation
     * @throws InvalidLogicTupleException    if the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException if the operation requested cannot be carried out
     */
    LogicTuple no(InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * @param ev the event to handle
     * @return the result of the operation
     * @throws InvalidLogicTupleException    if the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException if the operation requested cannot be carried out
     */
    List<LogicTuple> noAll(InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * @param ev the event to handle
     * @return the result of the operation
     * @throws InvalidLogicTupleException    if the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException if the operation requested cannot be carried out
     */
    LogicTuple nop(InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * @param ev the event to handle
     * @throws InvalidLogicTupleException    if the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException if the operation requested cannot be carried out
     */
    void out(InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * @param ev the event to handle
     * @return the result of the operation
     * @throws InvalidLogicTupleException    if the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException if the operation requested cannot be carried out
     */
    List<LogicTuple> outAll(InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * @param ev the event to handle
     * @return the result of the operation
     * @throws InvalidLogicTupleException    if the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException if the operation requested cannot be carried out
     */
    LogicTuple rd(InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * @param ev the event to handle
     * @return the result of the operation
     * @throws InvalidLogicTupleException    if the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException if the operation requested cannot be carried out
     */
    List<LogicTuple> rdAll(InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * @param ev the event to handle
     * @return the result of the operation
     * @throws InvalidLogicTupleException    if the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException if the operation requested cannot be carried out
     */
    LogicTuple rdp(InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * @param ev the event to handle
     * @return the result of the operation
     * @throws InvalidLogicTupleException    if the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException if the operation requested cannot be carried out
     */
    List<LogicTuple> set(InputEvent ev) throws OperationNotPossibleException,
            InvalidLogicTupleException;

    /**
     * @param ev the event to handle
     * @return the result of the operation
     * @throws InvalidLogicTupleException    if the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException if the operation requested cannot be carried out
     */
    LogicTuple spawn(InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * @param ev the event to handle
     * @return the result of the operation
     * @throws InvalidLogicTupleException    if the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException if the operation requested cannot be carried out
     */
    LogicTuple uin(InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * @param ev the event to handle
     * @return the result of the operation
     * @throws InvalidLogicTupleException    if the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException if the operation requested cannot be carried out
     */
    LogicTuple uinp(InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * @param ev the event to handle
     * @return the result of the operation
     * @throws InvalidLogicTupleException    if the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException if the operation requested cannot be carried out
     */
    LogicTuple uno(InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * @param ev the event to handle
     * @return the result of the operation
     * @throws InvalidLogicTupleException    if the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException if the operation requested cannot be carried out
     */
    LogicTuple unop(InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * @param ev the event to handle
     * @return the result of the operation
     * @throws InvalidLogicTupleException    if the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException if the operation requested cannot be carried out
     */
    LogicTuple urd(InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * @param ev the event to handle
     * @return the result of the operation
     * @throws InvalidLogicTupleException    if the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException if the operation requested cannot be carried out
     */
    LogicTuple urdp(InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;
}
