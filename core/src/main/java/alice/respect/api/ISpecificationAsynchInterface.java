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
     *            the event to handle
     * @return the operation requested
     * @throws InvalidLogicTupleException
     *             if the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException
     *             if the operation requested cannot be carried out
     */
    IRespectOperation getS(InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * 
     * @param ev
     *            the event to handle
     * @return the operation requested
     * @throws InvalidLogicTupleException
     *             if the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException
     *             if the operation requested cannot be carried out
     */
    IRespectOperation inpS(InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * 
     * @param ev
     *            the event to handle
     * @return the operation requested
     * @throws InvalidLogicTupleException
     *             if the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException
     *             if the operation requested cannot be carried out
     */
    IRespectOperation inS(InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * 
     * @param ev
     *            the event to handle
     * @return the operation requested
     * @throws InvalidLogicTupleException
     *             if the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException
     *             if the operation requested cannot be carried out
     */
    IRespectOperation nopS(InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * 
     * @param ev
     *            the event to handle
     * @return the operation requested
     * @throws InvalidLogicTupleException
     *             if the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException
     *             if the operation requested cannot be carried out
     */
    IRespectOperation noS(InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * 
     * @param ev
     *            the event to handle
     * @return the operation requested
     * @throws InvalidLogicTupleException
     *             if the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException
     *             if the operation requested cannot be carried out
     */
    IRespectOperation outS(InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * 
     * @param ev
     *            the event to handle
     * @return the operation requested
     * @throws InvalidLogicTupleException
     *             if the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException
     *             if the operation requested cannot be carried out
     */
    IRespectOperation rdpS(InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * 
     * @param ev
     *            the event to handle
     * @return the operation requested
     * @throws InvalidLogicTupleException
     *             if the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException
     *             if the operation requested cannot be carried out
     */
    IRespectOperation rdS(InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * 
     * @param spec
     *            the ReSpecT specification given as argument
     * @param ev
     *            the event to handle
     * @return the operation requested
     * @throws InvalidLogicTupleException
     *             if the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException
     *             if the operation requested cannot be carried out
     */
    IRespectOperation setS(RespectSpecification spec, InputEvent ev)
            throws InvalidLogicTupleException, OperationNotPossibleException;
}
