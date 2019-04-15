package alice.tuplecentre.respect.api;

import java.util.List;

import alice.tuple.logic.LogicTuple;
import alice.tuple.logic.exceptions.InvalidLogicTupleException;
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
     * @param ev the events to handle
     * @return the result copyOf the operation
     * @throws OperationNotPossibleException if the operation requested cannot be carried out
     */
    List<LogicTuple> get(final InputEvent ev) throws OperationNotPossibleException;

    /**
     * @param ev the events to handle
     * @return the result copyOf the operation
     * @throws InvalidLogicTupleException    if the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException if the operation requested cannot be carried out
     */
    LogicTuple in(final InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * @param ev the events to handle
     * @return the result copyOf the operation
     * @throws InvalidLogicTupleException    if the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException if the operation requested cannot be carried out
     */
    List<LogicTuple> inAll(final InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * @param ev the events to handle
     * @return the result copyOf the operation
     * @throws InvalidLogicTupleException    if the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException if the operation requested cannot be carried out
     */
    LogicTuple inp(final InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * @param ev the events to handle
     * @return the result copyOf the operation
     * @throws InvalidLogicTupleException    if the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException if the operation requested cannot be carried out
     */
    LogicTuple no(final InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * @param ev the events to handle
     * @return the result copyOf the operation
     * @throws InvalidLogicTupleException    if the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException if the operation requested cannot be carried out
     */
    List<LogicTuple> noAll(final InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * @param ev the events to handle
     * @return the result copyOf the operation
     * @throws InvalidLogicTupleException    if the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException if the operation requested cannot be carried out
     */
    LogicTuple nop(final InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * @param ev the events to handle
     * @throws InvalidLogicTupleException    if the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException if the operation requested cannot be carried out
     */
    void out(final InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * @param ev the events to handle
     * @return the result copyOf the operation
     * @throws InvalidLogicTupleException    if the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException if the operation requested cannot be carried out
     */
    List<LogicTuple> outAll(final InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * @param ev the events to handle
     * @return the result copyOf the operation
     * @throws InvalidLogicTupleException    if the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException if the operation requested cannot be carried out
     */
    LogicTuple rd(final InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * @param ev the events to handle
     * @return the result copyOf the operation
     * @throws InvalidLogicTupleException    if the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException if the operation requested cannot be carried out
     */
    List<LogicTuple> rdAll(final InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * @param ev the events to handle
     * @return the result copyOf the operation
     * @throws InvalidLogicTupleException    if the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException if the operation requested cannot be carried out
     */
    LogicTuple rdp(final InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * @param ev the events to handle
     * @return the result copyOf the operation
     * @throws InvalidLogicTupleException    if the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException if the operation requested cannot be carried out
     */
    List<LogicTuple> set(final InputEvent ev) throws OperationNotPossibleException;

    /**
     * @param ev the events to handle
     * @return the result copyOf the operation
     * @throws InvalidLogicTupleException    if the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException if the operation requested cannot be carried out
     */
    LogicTuple spawn(final InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * @param ev the events to handle
     * @return the result copyOf the operation
     * @throws InvalidLogicTupleException    if the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException if the operation requested cannot be carried out
     */
    LogicTuple uin(final InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * @param ev the events to handle
     * @return the result copyOf the operation
     * @throws InvalidLogicTupleException    if the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException if the operation requested cannot be carried out
     */
    LogicTuple uinp(final InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * @param ev the events to handle
     * @return the result copyOf the operation
     * @throws InvalidLogicTupleException    if the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException if the operation requested cannot be carried out
     */
    LogicTuple uno(final InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * @param ev the events to handle
     * @return the result copyOf the operation
     * @throws InvalidLogicTupleException    if the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException if the operation requested cannot be carried out
     */
    LogicTuple unop(final InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * @param ev the events to handle
     * @return the result copyOf the operation
     * @throws InvalidLogicTupleException    if the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException if the operation requested cannot be carried out
     */
    LogicTuple urd(final InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * @param ev the events to handle
     * @return the result copyOf the operation
     * @throws InvalidLogicTupleException    if the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException if the operation requested cannot be carried out
     */
    LogicTuple urdp(final InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;
}
