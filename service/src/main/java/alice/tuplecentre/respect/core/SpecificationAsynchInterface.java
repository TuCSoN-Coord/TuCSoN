package alice.tuplecentre.respect.core;

import alice.logictuple.LogicTuple;
import alice.logictuple.exceptions.InvalidLogicTupleException;
import alice.tuplecentre.core.AbstractTupleCentreOperation;
import alice.tuplecentre.core.InputEvent;
import alice.tuplecentre.respect.api.IRespectTC;
import alice.tuplecentre.respect.api.ISpecificationAsynchInterface;
import alice.tuplecentre.respect.api.RespectOperation;
import alice.tuplecentre.respect.api.RespectSpecification;
import alice.tuplecentre.respect.api.exceptions.OperationNotPossibleException;

/**
 * 
 * @author ste (mailto: s.mariani@unibo.it)
 * @author (contributor) Michele Bombardi (mailto:
 *         michele.bombardi@studio.unibo.it)
 * 
 */
public class SpecificationAsynchInterface implements
        ISpecificationAsynchInterface {
    private final IRespectTC core;

    /**
     * 
     * @param c
     *            the ReSpecT tuple centres manager this interface refers to
     */
    public SpecificationAsynchInterface(final IRespectTC c) {
        this.core = c;
    }

    @Override
    public RespectOperation getS(final InputEvent ev)
            throws OperationNotPossibleException {
        return this.core.get(ev);
    }

    @Override
    public RespectOperation inpS(final InputEvent ev)
            throws InvalidLogicTupleException, OperationNotPossibleException {
        final AbstractTupleCentreOperation op = ev.getSimpleTCEvent();
        if (op.getTemplateArgument() == null) {
            throw new InvalidLogicTupleException();
        }
        return this.core.inpS(ev);
    }

    @Override
    public RespectOperation inS(final InputEvent ev)
            throws InvalidLogicTupleException, OperationNotPossibleException {
        final AbstractTupleCentreOperation op = ev.getSimpleTCEvent();
        if (op.getTemplateArgument() == null) {
            throw new InvalidLogicTupleException();
        }
        return this.core.inS(ev);
    }

    @Override
    public RespectOperation nopS(final InputEvent ev)
            throws InvalidLogicTupleException, OperationNotPossibleException {
        final AbstractTupleCentreOperation op = ev.getSimpleTCEvent();
        if (op.getTemplateArgument() == null) {
            throw new InvalidLogicTupleException();
        }
        return this.core.nopS(ev);
    }

    @Override
    public RespectOperation noS(final InputEvent ev)
            throws InvalidLogicTupleException, OperationNotPossibleException {
        final AbstractTupleCentreOperation op = ev.getSimpleTCEvent();
        if (op.getTemplateArgument() == null) {
            throw new InvalidLogicTupleException();
        }
        return this.core.noS(ev);
    }

    @Override
    public RespectOperation outS(final InputEvent ev)
            throws InvalidLogicTupleException, OperationNotPossibleException {
        final LogicTuple t = (LogicTuple) ev.getTuple();
        if (t == null) {
            throw new InvalidLogicTupleException();
        }
        return this.core.outS(ev);
    }

    @Override
    public RespectOperation rdpS(final InputEvent ev)
            throws InvalidLogicTupleException, OperationNotPossibleException {
        final AbstractTupleCentreOperation op = ev.getSimpleTCEvent();
        if (op.getTemplateArgument() == null) {
            throw new InvalidLogicTupleException();
        }
        return this.core.rdpS(ev);
    }

    @Override
    public RespectOperation rdS(final InputEvent ev)
            throws InvalidLogicTupleException, OperationNotPossibleException {
        final AbstractTupleCentreOperation op = ev.getSimpleTCEvent();
        if (op.getTemplateArgument() == null) {
            throw new InvalidLogicTupleException();
        }
        return this.core.rdS(ev);
    }

    @Override
    public RespectOperation setS(final RespectSpecification spec,
                                 final InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException {
        if (spec == null) {
            throw new InvalidLogicTupleException();
        }
        return this.core.setSasynch(ev, spec);
    }
}
