package alice.tuplecentre.respect.api;

import alice.logictuple.LogicTuple;
import alice.logictuple.exceptions.InvalidLogicTupleException;
import alice.tuplecentre.core.InputEvent;
import alice.tuplecentre.respect.api.exceptions.OperationNotPossibleException;
import alice.tuplecentre.respect.api.exceptions.OperationTimeOutException;

import java.util.List;

/**
 * Interface to a ReSpecT Tuple Centre with timing functionalities.
 * 
 * @author Alessandro Ricci
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 * @author (contributor) Michele Bombardi (mailto:
 *         michele.bombardi@studio.unibo.it)
 */
public interface ITimedContext {
    /**
     * 
     * @param ev
     *            the event to handle
     * @param ms
     *            the timeout for operation completion
     * @return the result of the operation
     * @throws OperationNotPossibleException
     *             if the operation requested cannot be carried out
     * @throws OperationTimeOutException
     *             if the given timeout expired prior to operation completion
     */
    List<LogicTuple> get(InputEvent ev, long ms)
            throws OperationNotPossibleException, OperationTimeOutException;

    /**
     * 
     * @param ev
     *            the event to handle
     * @param ms
     *            the timeout for operation completion
     * @return the result of the operation
     * @throws InvalidLogicTupleException
     *             if the given argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException
     *             if the operation requested cannot be carried out
     * @throws OperationTimeOutException
     *             if the given timeout expired prior to operation completion
     */
    LogicTuple in(InputEvent ev, long ms) throws InvalidLogicTupleException,
            OperationNotPossibleException, OperationTimeOutException;

    /**
     * 
     * @param ev
     *            the event to handle
     * @param ms
     *            the timeout for operation completion
     * @return the result of the operation
     * @throws InvalidLogicTupleException
     *             if the given argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException
     *             if the operation requested cannot be carried out
     * @throws OperationTimeOutException
     *             if the given timeout expired prior to operation completion
     */
    LogicTuple inAll(InputEvent ev, long ms) throws InvalidLogicTupleException,
            OperationNotPossibleException, OperationTimeOutException;

    /**
     * 
     * @param ev
     *            the event to handle
     * @param ms
     *            the timeout for operation completion
     * @return the result of the operation
     * @throws InvalidLogicTupleException
     *             if the given argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException
     *             if the operation requested cannot be carried out
     * @throws OperationTimeOutException
     *             if the given timeout expired prior to operation completion
     */
    LogicTuple inp(InputEvent ev, long ms) throws InvalidLogicTupleException,
            OperationNotPossibleException, OperationTimeOutException;

    /**
     * 
     * @param ev
     *            the event to handle
     * @param ms
     *            the timeout for operation completion
     * @return the result of the operation
     * @throws InvalidLogicTupleException
     *             if the given argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException
     *             if the operation requested cannot be carried out
     * @throws OperationTimeOutException
     *             if the given timeout expired prior to operation completion
     */
    LogicTuple no(InputEvent ev, long ms) throws InvalidLogicTupleException,
            OperationNotPossibleException, OperationTimeOutException;

    /**
     * 
     * @param ev
     *            the event to handle
     * @param ms
     *            the timeout for operation completion
     * @return the result of the operation
     * @throws InvalidLogicTupleException
     *             if the given argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException
     *             if the operation requested cannot be carried out
     * @throws OperationTimeOutException
     *             if the given timeout expired prior to operation completion
     */
    LogicTuple noAll(InputEvent ev, long ms) throws InvalidLogicTupleException,
            OperationNotPossibleException, OperationTimeOutException;

    /**
     * 
     * @param ev
     *            the event to handle
     * @param ms
     *            the timeout for operation completion
     * @return the result of the operation
     * @throws InvalidLogicTupleException
     *             if the given argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException
     *             if the operation requested cannot be carried out
     * @throws OperationTimeOutException
     *             if the given timeout expired prior to operation completion
     */
    LogicTuple nop(InputEvent ev, long ms) throws InvalidLogicTupleException,
            OperationNotPossibleException, OperationTimeOutException;

    /**
     * 
     * @param ev
     *            the event to handle
     * @param ms
     *            the timeout for operation completion
     * @throws InvalidLogicTupleException
     *             if the given argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException
     *             if the operation requested cannot be carried out
     * @throws OperationTimeOutException
     *             if the given timeout expired prior to operation completion
     */
    void out(InputEvent ev, long ms) throws InvalidLogicTupleException,
            OperationNotPossibleException, OperationTimeOutException;

    /**
     * 
     * @param ev
     *            the event to handle
     * @param ms
     *            the timeout for operation completion
     * @throws InvalidLogicTupleException
     *             if the given argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException
     *             if the operation requested cannot be carried out
     * @throws OperationTimeOutException
     *             if the given timeout expired prior to operation completion
     */
    void outAll(InputEvent ev, long ms) throws InvalidLogicTupleException,
            OperationNotPossibleException, OperationTimeOutException;

    /**
     * 
     * @param ev
     *            the event to handle
     * @param ms
     *            the timeout for operation completion
     * @return the result of the operation
     * @throws InvalidLogicTupleException
     *             if the given argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException
     *             if the operation requested cannot be carried out
     * @throws OperationTimeOutException
     *             if the given timeout expired prior to operation completion
     */
    LogicTuple rd(InputEvent ev, long ms) throws InvalidLogicTupleException,
            OperationNotPossibleException, OperationTimeOutException;

    /**
     * 
     * @param ev
     *            the event to handle
     * @param ms
     *            the timeout for operation completion
     * @return the result of the operation
     * @throws InvalidLogicTupleException
     *             if the given argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException
     *             if the operation requested cannot be carried out
     * @throws OperationTimeOutException
     *             if the given timeout expired prior to operation completion
     */
    LogicTuple rdAll(InputEvent ev, long ms) throws InvalidLogicTupleException,
            OperationNotPossibleException, OperationTimeOutException;

    /**
     * 
     * @param ev
     *            the event to handle
     * @param ms
     *            the timeout for operation completion
     * @return the result of the operation
     * @throws InvalidLogicTupleException
     *             if the given argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException
     *             if the operation requested cannot be carried out
     * @throws OperationTimeOutException
     *             if the given timeout expired prior to operation completion
     */
    LogicTuple rdp(InputEvent ev, long ms) throws InvalidLogicTupleException,
            OperationNotPossibleException, OperationTimeOutException;

    /**
     * 
     * @param ev
     *            the event to handle
     * @param ms
     *            the timeout for operation completion
     * @return the result of the operation
     * @throws InvalidLogicTupleException
     *             if the given argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException
     *             if the operation requested cannot be carried out
     * @throws OperationTimeOutException
     *             if the given timeout expired prior to operation completion
     */
    List<LogicTuple> set(InputEvent ev, long ms)
            throws InvalidLogicTupleException, OperationNotPossibleException,
            OperationTimeOutException;

    /**
     * 
     * @param ev
     *            the event to handle
     * @param ms
     *            the timeout for operation completion
     * @return the result of the operation
     * @throws InvalidLogicTupleException
     *             if the given argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException
     *             if the operation requested cannot be carried out
     * @throws OperationTimeOutException
     *             if the given timeout expired prior to operation completion
     */
    LogicTuple spawn(InputEvent ev, long ms) throws InvalidLogicTupleException,
            OperationNotPossibleException, OperationTimeOutException;

    /**
     * 
     * @param ev
     *            the event to handle
     * @param ms
     *            the timeout for operation completion
     * @return the result of the operation
     * @throws InvalidLogicTupleException
     *             if the given argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException
     *             if the operation requested cannot be carried out
     * @throws OperationTimeOutException
     *             if the given timeout expired prior to operation completion
     */
    LogicTuple uin(InputEvent ev, long ms) throws InvalidLogicTupleException,
            OperationNotPossibleException, OperationTimeOutException;

    /**
     * 
     * @param ev
     *            the event to handle
     * @param ms
     *            the timeout for operation completion
     * @return the result of the operation
     * @throws InvalidLogicTupleException
     *             if the given argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException
     *             if the operation requested cannot be carried out
     * @throws OperationTimeOutException
     *             if the given timeout expired prior to operation completion
     */
    LogicTuple uinp(InputEvent ev, long ms) throws InvalidLogicTupleException,
            OperationNotPossibleException, OperationTimeOutException;

    /**
     * 
     * @param ev
     *            the event to handle
     * @param ms
     *            the timeout for operation completion
     * @return the result of the operation
     * @throws InvalidLogicTupleException
     *             if the given argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException
     *             if the operation requested cannot be carried out
     * @throws OperationTimeOutException
     *             if the given timeout expired prior to operation completion
     */
    LogicTuple uno(InputEvent ev, long ms) throws InvalidLogicTupleException,
            OperationNotPossibleException, OperationTimeOutException;

    /**
     * 
     * @param ev
     *            the event to handle
     * @param ms
     *            the timeout for operation completion
     * @return the result of the operation
     * @throws InvalidLogicTupleException
     *             if the given argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException
     *             if the operation requested cannot be carried out
     * @throws OperationTimeOutException
     *             if the given timeout expired prior to operation completion
     */
    LogicTuple unop(InputEvent ev, long ms) throws InvalidLogicTupleException,
            OperationNotPossibleException, OperationTimeOutException;

    /**
     * 
     * @param ev
     *            the event to handle
     * @param ms
     *            the timeout for operation completion
     * @return the result of the operation
     * @throws InvalidLogicTupleException
     *             if the given argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException
     *             if the operation requested cannot be carried out
     * @throws OperationTimeOutException
     *             if the given timeout expired prior to operation completion
     */
    LogicTuple urd(InputEvent ev, long ms) throws InvalidLogicTupleException,
            OperationNotPossibleException, OperationTimeOutException;

    /**
     * 
     * @param ev
     *            the event to handle
     * @param ms
     *            the timeout for operation completion
     * @return the result of the operation
     * @throws InvalidLogicTupleException
     *             if the given argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException
     *             if the operation requested cannot be carried out
     * @throws OperationTimeOutException
     *             if the given timeout expired prior to operation completion
     */
    LogicTuple urdp(InputEvent ev, long ms) throws InvalidLogicTupleException,
            OperationNotPossibleException, OperationTimeOutException;
}
