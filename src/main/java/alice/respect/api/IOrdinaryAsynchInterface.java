package alice.respect.api;

import alice.logictuple.exceptions.InvalidLogicTupleException;
import alice.respect.api.exceptions.OperationNotPossibleException;
import alice.tuplecentre.core.InputEvent;

/**
 * A ReSpecT Tuple Centre Interface to issue ReSpecT ordinary primitives using
 * an asynchronous semantics.
 * 
 * @author Alessandro Ricci
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 * @author (contributor) Michele Bombardi (mailto:
 *         michele.bombardi@studio.unibo.it)
 */
public interface IOrdinaryAsynchInterface {
    /**
     * 
     * @param ev
     *            the event to handle
     * @return the operation requested
     * @throws InvalidLogicTupleException
     *             if the tuple given as argument is not a valid Prolog tuple if
     *             the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException
     *             if the operation requested cannot be carried out if the
     *             operation requested cannot be carried out
     */
    IRespectOperation get(InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * 
     * @param ev
     *            the event to handle
     * @return the operation requested
     * @throws InvalidLogicTupleException
     *             if the tuple given as argument is not a valid Prolog tuple if
     *             the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException
     *             if the operation requested cannot be carried out if the
     *             operation requested cannot be carried out
     */
    IRespectOperation in(InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * 
     * @param ev
     *            the event to handle
     * @return the operation requested
     * @throws InvalidLogicTupleException
     *             if the tuple given as argument is not a valid Prolog tuple if
     *             the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException
     *             if the operation requested cannot be carried out if the
     *             operation requested cannot be carried out
     */
    IRespectOperation inAll(InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * 
     * @param ev
     *            the event to handle
     * @return the operation requested
     * @throws InvalidLogicTupleException
     *             if the tuple given as argument is not a valid Prolog tuple if
     *             the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException
     *             if the operation requested cannot be carried out if the
     *             operation requested cannot be carried out
     */
    IRespectOperation inp(InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * 
     * @param ev
     *            the event to handle
     * @return the operation requested
     * @throws InvalidLogicTupleException
     *             if the tuple given as argument is not a valid Prolog tuple if
     *             the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException
     *             if the operation requested cannot be carried out if the
     *             operation requested cannot be carried out
     */
    IRespectOperation no(InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * 
     * @param ev
     *            the event to handle
     * @return the operation requested
     * @throws InvalidLogicTupleException
     *             if the tuple given as argument is not a valid Prolog tuple if
     *             the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException
     *             if the operation requested cannot be carried out if the
     *             operation requested cannot be carried out
     */
    IRespectOperation noAll(InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * 
     * @param ev
     *            the event to handle
     * @return the operation requested
     * @throws InvalidLogicTupleException
     *             if the tuple given as argument is not a valid Prolog tuple if
     *             the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException
     *             if the operation requested cannot be carried out if the
     *             operation requested cannot be carried out
     */
    IRespectOperation nop(InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * 
     * @param ev
     *            the event to handle
     * @return the operation requested
     * @throws InvalidLogicTupleException
     *             if the tuple given as argument is not a valid Prolog tuple if
     *             the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException
     *             if the operation requested cannot be carried out if the
     *             operation requested cannot be carried out
     */
    IRespectOperation out(InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * 
     * @param ev
     *            the event to handle
     * @return the operation requested
     * @throws InvalidLogicTupleException
     *             if the tuple given as argument is not a valid Prolog tuple if
     *             the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException
     *             if the operation requested cannot be carried out if the
     *             operation requested cannot be carried out
     */
    IRespectOperation outAll(InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * 
     * @param ev
     *            the event to handle
     * @return the operation requested
     * @throws InvalidLogicTupleException
     *             if the tuple given as argument is not a valid Prolog tuple if
     *             the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException
     *             if the operation requested cannot be carried out if the
     *             operation requested cannot be carried out
     */
    IRespectOperation rd(InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * 
     * @param ev
     *            the event to handle
     * @return the operation requested
     * @throws InvalidLogicTupleException
     *             if the tuple given as argument is not a valid Prolog tuple if
     *             the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException
     *             if the operation requested cannot be carried out if the
     *             operation requested cannot be carried out
     */
    IRespectOperation rdAll(InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * 
     * @param ev
     *            the event to handle
     * @return the operation requested
     * @throws InvalidLogicTupleException
     *             if the tuple given as argument is not a valid Prolog tuple if
     *             the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException
     *             if the operation requested cannot be carried out if the
     *             operation requested cannot be carried out
     */
    IRespectOperation rdp(InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * 
     * @param ev
     *            the event to handle
     * @return the operation requested
     * @throws InvalidLogicTupleException
     *             if the tuple given as argument is not a valid Prolog tuple if
     *             the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException
     *             if the operation requested cannot be carried out if the
     *             operation requested cannot be carried out
     */
    IRespectOperation set(InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * 
     * @param ev
     *            the event to handle
     * @return the operation requested
     * @throws InvalidLogicTupleException
     *             if the tuple given as argument is not a valid Prolog tuple if
     *             the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException
     *             if the operation requested cannot be carried out if the
     *             operation requested cannot be carried out
     */
    IRespectOperation spawn(InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * 
     * @param ev
     *            the event to handle
     * @return the operation requested
     * @throws InvalidLogicTupleException
     *             if the tuple given as argument is not a valid Prolog tuple if
     *             the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException
     *             if the operation requested cannot be carried out if the
     *             operation requested cannot be carried out
     */
    IRespectOperation uin(InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * 
     * @param ev
     *            the event to handle
     * @return the operation requested
     * @throws InvalidLogicTupleException
     *             if the tuple given as argument is not a valid Prolog tuple if
     *             the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException
     *             if the operation requested cannot be carried out if the
     *             operation requested cannot be carried out
     */
    IRespectOperation uinp(InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * 
     * @param ev
     *            the event to handle
     * @return the operation requested
     * @throws InvalidLogicTupleException
     *             if the tuple given as argument is not a valid Prolog tuple if
     *             the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException
     *             if the operation requested cannot be carried out if the
     *             operation requested cannot be carried out
     */
    IRespectOperation uno(InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * 
     * @param ev
     *            the event to handle
     * @return the operation requested
     * @throws InvalidLogicTupleException
     *             if the tuple given as argument is not a valid Prolog tuple if
     *             the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException
     *             if the operation requested cannot be carried out if the
     *             operation requested cannot be carried out
     */
    IRespectOperation unop(InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * 
     * @param ev
     *            the event to handle
     * @return the operation requested
     * @throws InvalidLogicTupleException
     *             if the tuple given as argument is not a valid Prolog tuple if
     *             the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException
     *             if the operation requested cannot be carried out if the
     *             operation requested cannot be carried out
     */
    IRespectOperation urd(InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * 
     * @param ev
     *            the event to handle
     * @return the operation requested
     * @throws InvalidLogicTupleException
     *             if the tuple given as argument is not a valid Prolog tuple if
     *             the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException
     *             if the operation requested cannot be carried out if the
     *             operation requested cannot be carried out
     */
    IRespectOperation urdp(InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;
}
