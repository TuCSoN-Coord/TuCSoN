package alice.respect.core;

import alice.logictuple.LogicTuple;
import alice.logictuple.exceptions.InvalidLogicTupleException;
import alice.respect.api.IRespectOperation;
import alice.respect.api.IRespectTC;
import alice.respect.api.ISpecificationAsynchInterface;
import alice.respect.api.RespectSpecification;
import alice.respect.api.exceptions.OperationNotPossibleException;
import alice.tuplecentre.core.AbstractTupleCentreOperation;
import alice.tuplecentre.core.InputEvent;

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
    public IRespectOperation getS(final InputEvent ev)
            throws OperationNotPossibleException {
        return this.core.get(ev);
    }

    @Override
    public IRespectOperation inpS(final InputEvent ev)
            throws InvalidLogicTupleException, OperationNotPossibleException {
        final AbstractTupleCentreOperation op = ev.getSimpleTCEvent();
        if (op.getTemplateArgument() == null) {
            throw new InvalidLogicTupleException();
        }
        return this.core.inpS(ev);
    }

    @Override
    public IRespectOperation inS(final InputEvent ev)
            throws InvalidLogicTupleException, OperationNotPossibleException {
        final AbstractTupleCentreOperation op = ev.getSimpleTCEvent();
        if (op.getTemplateArgument() == null) {
            throw new InvalidLogicTupleException();
        }
        return this.core.inS(ev);
    }

    @Override
    public IRespectOperation nopS(final InputEvent ev)
            throws InvalidLogicTupleException, OperationNotPossibleException {
        final AbstractTupleCentreOperation op = ev.getSimpleTCEvent();
        if (op.getTemplateArgument() == null) {
            throw new InvalidLogicTupleException();
        }
        return this.core.nopS(ev);
    }

    @Override
    public IRespectOperation noS(final InputEvent ev)
            throws InvalidLogicTupleException, OperationNotPossibleException {
        final AbstractTupleCentreOperation op = ev.getSimpleTCEvent();
        if (op.getTemplateArgument() == null) {
            throw new InvalidLogicTupleException();
        }
        return this.core.noS(ev);
    }

    @Override
    public IRespectOperation outS(final InputEvent ev)
            throws InvalidLogicTupleException, OperationNotPossibleException {
        final LogicTuple t = (LogicTuple) ev.getTuple();
        if (t == null) {
            throw new InvalidLogicTupleException();
        }
        return this.core.outS(ev);
    }

    @Override
    public IRespectOperation rdpS(final InputEvent ev)
            throws InvalidLogicTupleException, OperationNotPossibleException {
        final AbstractTupleCentreOperation op = ev.getSimpleTCEvent();
        if (op.getTemplateArgument() == null) {
            throw new InvalidLogicTupleException();
        }
        return this.core.rdpS(ev);
    }

    @Override
    public IRespectOperation rdS(final InputEvent ev)
            throws InvalidLogicTupleException, OperationNotPossibleException {
        final AbstractTupleCentreOperation op = ev.getSimpleTCEvent();
        if (op.getTemplateArgument() == null) {
            throw new InvalidLogicTupleException();
        }
        return this.core.rdS(ev);
    }

    @Override
    public IRespectOperation setS(final RespectSpecification spec,
            final InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException {
        if (spec == null) {
            throw new InvalidLogicTupleException();
        }
        return this.core.setSasynch(ev, spec);
    }
}
