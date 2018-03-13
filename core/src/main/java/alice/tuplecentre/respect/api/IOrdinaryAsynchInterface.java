package alice.tuplecentre.respect.api;

import alice.tuple.logic.exceptions.InvalidLogicTupleException;
import alice.tuplecentre.core.InputEvent;
import alice.tuplecentre.respect.api.exceptions.OperationNotPossibleException;

/**
 * A ReSpecT Tuple Centre Interface to issue ReSpecT ordinary primitives using
 * an asynchronous semantics.
 *
 * @author Alessandro Ricci
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 * @author (contributor) Michele Bombardi (mailto:
 * michele.bombardi@studio.unibo.it)
 */
public interface IOrdinaryAsynchInterface {
    /**
     * @param ev the events to handle
     * @return the operation requested
     * @throws InvalidLogicTupleException    if the tuple given as argument is not a valid Prolog tuple if
     *                                       the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException if the operation requested cannot be carried out if the
     *                                       operation requested cannot be carried out
     */
    RespectOperation get(InputEvent ev) throws
            OperationNotPossibleException;

    /**
     * @param ev the events to handle
     * @return the operation requested
     * @throws InvalidLogicTupleException    if the tuple given as argument is not a valid Prolog tuple if
     *                                       the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException if the operation requested cannot be carried out if the
     *                                       operation requested cannot be carried out
     */
    RespectOperation in(InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * @param ev the events to handle
     * @return the operation requested
     * @throws InvalidLogicTupleException    if the tuple given as argument is not a valid Prolog tuple if
     *                                       the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException if the operation requested cannot be carried out if the
     *                                       operation requested cannot be carried out
     */
    RespectOperation inAll(InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * @param ev the events to handle
     * @return the operation requested
     * @throws InvalidLogicTupleException    if the tuple given as argument is not a valid Prolog tuple if
     *                                       the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException if the operation requested cannot be carried out if the
     *                                       operation requested cannot be carried out
     */
    RespectOperation inp(InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * @param ev the events to handle
     * @return the operation requested
     * @throws InvalidLogicTupleException    if the tuple given as argument is not a valid Prolog tuple if
     *                                       the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException if the operation requested cannot be carried out if the
     *                                       operation requested cannot be carried out
     */
    RespectOperation no(InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * @param ev the events to handle
     * @return the operation requested
     * @throws InvalidLogicTupleException    if the tuple given as argument is not a valid Prolog tuple if
     *                                       the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException if the operation requested cannot be carried out if the
     *                                       operation requested cannot be carried out
     */
    RespectOperation noAll(InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * @param ev the events to handle
     * @return the operation requested
     * @throws InvalidLogicTupleException    if the tuple given as argument is not a valid Prolog tuple if
     *                                       the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException if the operation requested cannot be carried out if the
     *                                       operation requested cannot be carried out
     */
    RespectOperation nop(InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * @param ev the events to handle
     * @return the operation requested
     * @throws InvalidLogicTupleException    if the tuple given as argument is not a valid Prolog tuple if
     *                                       the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException if the operation requested cannot be carried out if the
     *                                       operation requested cannot be carried out
     */
    RespectOperation out(InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * @param ev the events to handle
     * @return the operation requested
     * @throws InvalidLogicTupleException    if the tuple given as argument is not a valid Prolog tuple if
     *                                       the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException if the operation requested cannot be carried out if the
     *                                       operation requested cannot be carried out
     */
    RespectOperation outAll(InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * @param ev the events to handle
     * @return the operation requested
     * @throws InvalidLogicTupleException    if the tuple given as argument is not a valid Prolog tuple if
     *                                       the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException if the operation requested cannot be carried out if the
     *                                       operation requested cannot be carried out
     */
    RespectOperation rd(InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * @param ev the events to handle
     * @return the operation requested
     * @throws InvalidLogicTupleException    if the tuple given as argument is not a valid Prolog tuple if
     *                                       the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException if the operation requested cannot be carried out if the
     *                                       operation requested cannot be carried out
     */
    RespectOperation rdAll(InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * @param ev the events to handle
     * @return the operation requested
     * @throws InvalidLogicTupleException    if the tuple given as argument is not a valid Prolog tuple if
     *                                       the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException if the operation requested cannot be carried out if the
     *                                       operation requested cannot be carried out
     */
    RespectOperation rdp(InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * @param ev the events to handle
     * @return the operation requested
     * @throws InvalidLogicTupleException    if the tuple given as argument is not a valid Prolog tuple if
     *                                       the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException if the operation requested cannot be carried out if the
     *                                       operation requested cannot be carried out
     */
    RespectOperation set(InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * @param ev the events to handle
     * @return the operation requested
     * @throws InvalidLogicTupleException    if the tuple given as argument is not a valid Prolog tuple if
     *                                       the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException if the operation requested cannot be carried out if the
     *                                       operation requested cannot be carried out
     */
    RespectOperation spawn(InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * @param ev the events to handle
     * @return the operation requested
     * @throws InvalidLogicTupleException    if the tuple given as argument is not a valid Prolog tuple if
     *                                       the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException if the operation requested cannot be carried out if the
     *                                       operation requested cannot be carried out
     */
    RespectOperation uin(InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * @param ev the events to handle
     * @return the operation requested
     * @throws InvalidLogicTupleException    if the tuple given as argument is not a valid Prolog tuple if
     *                                       the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException if the operation requested cannot be carried out if the
     *                                       operation requested cannot be carried out
     */
    RespectOperation uinp(InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * @param ev the events to handle
     * @return the operation requested
     * @throws InvalidLogicTupleException    if the tuple given as argument is not a valid Prolog tuple if
     *                                       the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException if the operation requested cannot be carried out if the
     *                                       operation requested cannot be carried out
     */
    RespectOperation uno(InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * @param ev the events to handle
     * @return the operation requested
     * @throws InvalidLogicTupleException    if the tuple given as argument is not a valid Prolog tuple if
     *                                       the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException if the operation requested cannot be carried out if the
     *                                       operation requested cannot be carried out
     */
    RespectOperation unop(InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * @param ev the events to handle
     * @return the operation requested
     * @throws InvalidLogicTupleException    if the tuple given as argument is not a valid Prolog tuple if
     *                                       the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException if the operation requested cannot be carried out if the
     *                                       operation requested cannot be carried out
     */
    RespectOperation urd(InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * @param ev the events to handle
     * @return the operation requested
     * @throws InvalidLogicTupleException    if the tuple given as argument is not a valid Prolog tuple if
     *                                       the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException if the operation requested cannot be carried out if the
     *                                       operation requested cannot be carried out
     */
    RespectOperation urdp(InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;
}
