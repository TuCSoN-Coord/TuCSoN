package alice.respect.api;

import alice.logictuple.exceptions.InvalidLogicTupleException;
import alice.respect.api.exceptions.OperationNotPossibleException;
import alice.tuplecentre.core.InputEvent;

/**
 * A ReSpecT Tuple Centre Interface to issue ReSpecT specification primitives
 * using an asynchronous semantics.
 * 
 * @author Alessandro Ricci
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 * @author (contributor) Michele Bombardi (mailto:
 *         michele.bombardi@studio.unibo.it)
 */
public interface ISpecificationAsynchInterface {
    /**
     * 
     * @param ev
     *            the events to handle
     * @return the operation requested
     * @throws InvalidLogicTupleException
     *             if the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException
     *             if the operation requested cannot be carried out
     */
    RespectOperation getS(InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * 
     * @param ev
     *            the events to handle
     * @return the operation requested
     * @throws InvalidLogicTupleException
     *             if the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException
     *             if the operation requested cannot be carried out
     */
    RespectOperation inpS(InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * 
     * @param ev
     *            the events to handle
     * @return the operation requested
     * @throws InvalidLogicTupleException
     *             if the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException
     *             if the operation requested cannot be carried out
     */
    RespectOperation inS(InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * 
     * @param ev
     *            the events to handle
     * @return the operation requested
     * @throws InvalidLogicTupleException
     *             if the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException
     *             if the operation requested cannot be carried out
     */
    RespectOperation nopS(InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * 
     * @param ev
     *            the events to handle
     * @return the operation requested
     * @throws InvalidLogicTupleException
     *             if the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException
     *             if the operation requested cannot be carried out
     */
    RespectOperation noS(InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * 
     * @param ev
     *            the events to handle
     * @return the operation requested
     * @throws InvalidLogicTupleException
     *             if the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException
     *             if the operation requested cannot be carried out
     */
    RespectOperation outS(InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * 
     * @param ev
     *            the events to handle
     * @return the operation requested
     * @throws InvalidLogicTupleException
     *             if the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException
     *             if the operation requested cannot be carried out
     */
    RespectOperation rdpS(InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * 
     * @param ev
     *            the events to handle
     * @return the operation requested
     * @throws InvalidLogicTupleException
     *             if the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException
     *             if the operation requested cannot be carried out
     */
    RespectOperation rdS(InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * 
     * @param spec
     *            the ReSpecT specification given as argument
     * @param ev
     *            the events to handle
     * @return the operation requested
     * @throws InvalidLogicTupleException
     *             if the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException
     *             if the operation requested cannot be carried out
     */
    RespectOperation setS(RespectSpecification spec, InputEvent ev)
            throws InvalidLogicTupleException, OperationNotPossibleException;
}
