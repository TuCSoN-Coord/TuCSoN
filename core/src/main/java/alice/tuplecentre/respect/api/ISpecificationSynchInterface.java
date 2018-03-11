package alice.tuplecentre.respect.api;

import java.util.List;

import alice.logictuple.LogicTuple;
import alice.logictuple.exceptions.InvalidLogicTupleException;
import alice.tuplecentre.core.InputEvent;
import alice.tuplecentre.respect.api.exceptions.InvalidSpecificationException;
import alice.tuplecentre.respect.api.exceptions.OperationNotPossibleException;

/**
 * A ReSpecT Tuple Centre Interface to issue ReSpecT specification primitives
 * using a synchronous semantics.
 * 
 * @author Alessandro Ricci
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 * @author (contributor) Michele Bombardi (mailto:
 *         michele.bombardi@studio.unibo.it)
 */
public interface ISpecificationSynchInterface {
    /**
     * 
     * @param ev
     *            the events to handle
     * @return the result of the operation
     * @throws OperationNotPossibleException
     *             if the operation requested cannot be carried out
     */
    List<LogicTuple> getS(final InputEvent ev)
            throws OperationNotPossibleException;

    /**
     * 
     * @param ev
     *            the events to handle
     * @return the result of the operation
     * @throws InvalidLogicTupleException
     *             if the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException
     *             if the operation requested cannot be carried out
     */
    LogicTuple inpS(InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * 
     * @param ev
     *            the events to handle
     * @return the result of the operation
     * @throws InvalidLogicTupleException
     *             if the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException
     *             if the operation requested cannot be carried out
     */
    LogicTuple inS(InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * 
     * @param ev
     *            the events to handle
     * @return the result of the operation
     * @throws InvalidLogicTupleException
     *             if the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException
     *             if the operation requested cannot be carried out
     */
    LogicTuple nopS(InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * 
     * @param ev
     *            the events to handle
     * @return the result of the operation
     * @throws InvalidLogicTupleException
     *             if the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException
     *             if the operation requested cannot be carried out
     */
    LogicTuple noS(InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * 
     * @param ev
     *            the events to handle
     * @throws InvalidLogicTupleException
     *             if the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException
     *             if the operation requested cannot be carried out
     */
    void outS(InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * 
     * @param ev
     *            the events to handle
     * @return the result of the operation
     * @throws InvalidLogicTupleException
     *             if the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException
     *             if the operation requested cannot be carried out
     */
    LogicTuple rdpS(InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * 
     * @param ev
     *            the events to handle
     * @return the result of the operation
     * @throws InvalidLogicTupleException
     *             if the tuple given as argument is not a valid Prolog tuple
     * @throws OperationNotPossibleException
     *             if the operation requested cannot be carried out
     */
    LogicTuple rdS(InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException;

    /**
     * @param t
     *            the logic tuple representing the ReSpecT specification to set
     * @param ev
     *            the events to handle
     * @return the result of the operation
     * @throws OperationNotPossibleException
     *             if the operation requested cannot be carried out
     */
    List<LogicTuple> setS(final LogicTuple t, final InputEvent ev)
            throws OperationNotPossibleException;

    /**
     * 
     * @param spec
     *            the String representation of the ReSpecT specification to set
     * @param ev
     *            the events to handle
     * @return the result of the operation
     * @throws OperationNotPossibleException
     *             if the operation requested cannot be carried out
     * @throws InvalidSpecificationException
     *             if the given ReSpecT specification is not valid
     */
    List<LogicTuple> setS(final RespectSpecification spec, final InputEvent ev)
            throws OperationNotPossibleException, InvalidSpecificationException;
}
