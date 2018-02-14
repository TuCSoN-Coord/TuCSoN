package alice.respect.core;

import alice.logictuple.exceptions.InvalidLogicTupleException;
import alice.respect.api.IOrdinaryAsynchInterface;
import alice.respect.api.IRespectOperation;
import alice.respect.api.IRespectTC;
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
public class OrdinaryAsynchInterface extends RootInterface implements
        IOrdinaryAsynchInterface {
    /**
     * 
     * @param core
     *            the ReSpecT tuple centre this context refers to
     */
    public OrdinaryAsynchInterface(final IRespectTC core) {
        super(core);
    }

    @Override
    public IRespectOperation get(final InputEvent ev)
            throws OperationNotPossibleException {
        return this.getCore().get(ev);
    }

    @Override
    public IRespectOperation in(final InputEvent ev)
            throws InvalidLogicTupleException, OperationNotPossibleException {
        final AbstractTupleCentreOperation op = ev.getSimpleTCEvent();
        if (op.getTemplateArgument() == null) {
            throw new InvalidLogicTupleException();
        }
        return this.getCore().in(ev);
    }

    @Override
    public IRespectOperation inAll(final InputEvent ev)
            throws InvalidLogicTupleException, OperationNotPossibleException {
        final AbstractTupleCentreOperation op = ev.getSimpleTCEvent();
        if (op.getTemplateArgument() == null) {
            throw new InvalidLogicTupleException();
        }
        return this.getCore().inAll(ev);
    }

    @Override
    public IRespectOperation inp(final InputEvent ev)
            throws InvalidLogicTupleException, OperationNotPossibleException {
        final AbstractTupleCentreOperation op = ev.getSimpleTCEvent();
        if (op.getTemplateArgument() == null) {
            throw new InvalidLogicTupleException();
        }
        return this.getCore().inp(ev);
    }

    @Override
    public IRespectOperation no(final InputEvent ev)
            throws InvalidLogicTupleException, OperationNotPossibleException {
        final AbstractTupleCentreOperation op = ev.getSimpleTCEvent();
        if (op.getTemplateArgument() == null) {
            throw new InvalidLogicTupleException();
        }
        return this.getCore().no(ev);
    }

    @Override
    public IRespectOperation noAll(final InputEvent ev)
            throws InvalidLogicTupleException, OperationNotPossibleException {
        final AbstractTupleCentreOperation op = ev.getSimpleTCEvent();
        if (op.getTemplateArgument() == null) {
            throw new InvalidLogicTupleException();
        }
        return this.getCore().noAll(ev);
    }

    @Override
    public IRespectOperation nop(final InputEvent ev)
            throws InvalidLogicTupleException, OperationNotPossibleException {
        final AbstractTupleCentreOperation op = ev.getSimpleTCEvent();
        if (op.getTemplateArgument() == null) {
            throw new InvalidLogicTupleException();
        }
        return this.getCore().nop(ev);
    }

    @Override
    public IRespectOperation out(final InputEvent ev)
            throws InvalidLogicTupleException, OperationNotPossibleException {
        if (ev.getTuple() == null) {
            throw new InvalidLogicTupleException();
        }
        return this.getCore().out(ev);
    }

    @Override
    public IRespectOperation outAll(final InputEvent ev)
            throws InvalidLogicTupleException, OperationNotPossibleException {
        if (ev.getTuple() == null) {
            throw new InvalidLogicTupleException();
        }
        return this.getCore().outAll(ev);
    }

    @Override
    public IRespectOperation rd(final InputEvent ev)
            throws InvalidLogicTupleException, OperationNotPossibleException {
        final AbstractTupleCentreOperation op = ev.getSimpleTCEvent();
        if (op.getTemplateArgument() == null) {
            throw new InvalidLogicTupleException();
        }
        return this.getCore().rd(ev);
    }

    @Override
    public IRespectOperation rdAll(final InputEvent ev)
            throws InvalidLogicTupleException, OperationNotPossibleException {
        final AbstractTupleCentreOperation op = ev.getSimpleTCEvent();
        if (op.getTemplateArgument() == null) {
            throw new InvalidLogicTupleException();
        }
        return this.getCore().rdAll(ev);
    }

    @Override
    public IRespectOperation rdp(final InputEvent ev)
            throws InvalidLogicTupleException, OperationNotPossibleException {
        final AbstractTupleCentreOperation op = ev.getSimpleTCEvent();
        if (op.getTemplateArgument() == null) {
            throw new InvalidLogicTupleException();
        }
        return this.getCore().rdp(ev);
    }

    @Override
    public IRespectOperation set(final InputEvent ev)
            throws InvalidLogicTupleException, OperationNotPossibleException {
        final AbstractTupleCentreOperation op = ev.getSimpleTCEvent();
        if (op.getTupleListArgument() == null) {
            throw new InvalidLogicTupleException();
        }
        return this.getCore().set(ev);
    }

    @Override
    public IRespectOperation spawn(final InputEvent ev)
            throws InvalidLogicTupleException, OperationNotPossibleException {
        if (ev.getTuple() == null) {
            throw new InvalidLogicTupleException();
        }
        return this.getCore().spawn(ev);
    }

    @Override
    public IRespectOperation uin(final InputEvent ev)
            throws InvalidLogicTupleException, OperationNotPossibleException {
        final AbstractTupleCentreOperation op = ev.getSimpleTCEvent();
        if (op.getTemplateArgument() == null) {
            throw new InvalidLogicTupleException();
        }
        return this.getCore().uin(ev);
    }

    @Override
    public IRespectOperation uinp(final InputEvent ev)
            throws InvalidLogicTupleException, OperationNotPossibleException {
        final AbstractTupleCentreOperation op = ev.getSimpleTCEvent();
        if (op.getTemplateArgument() == null) {
            throw new InvalidLogicTupleException();
        }
        return this.getCore().uinp(ev);
    }

    @Override
    public IRespectOperation uno(final InputEvent ev)
            throws InvalidLogicTupleException, OperationNotPossibleException {
        final AbstractTupleCentreOperation op = ev.getSimpleTCEvent();
        if (op.getTemplateArgument() == null) {
            throw new InvalidLogicTupleException();
        }
        return this.getCore().uno(ev);
    }

    @Override
    public IRespectOperation unop(final InputEvent ev)
            throws InvalidLogicTupleException, OperationNotPossibleException {
        final AbstractTupleCentreOperation op = ev.getSimpleTCEvent();
        if (op.getTemplateArgument() == null) {
            throw new InvalidLogicTupleException();
        }
        return this.getCore().unop(ev);
    }

    @Override
    public IRespectOperation urd(final InputEvent ev)
            throws InvalidLogicTupleException, OperationNotPossibleException {
        final AbstractTupleCentreOperation op = ev.getSimpleTCEvent();
        if (op.getTemplateArgument() == null) {
            throw new InvalidLogicTupleException();
        }
        return this.getCore().urd(ev);
    }

    @Override
    public IRespectOperation urdp(final InputEvent ev)
            throws InvalidLogicTupleException, OperationNotPossibleException {
        final AbstractTupleCentreOperation op = ev.getSimpleTCEvent();
        if (op.getTemplateArgument() == null) {
            throw new InvalidLogicTupleException();
        }
        return this.getCore().urdp(ev);
    }

//	@Override
//	public IRespectOperation getEnv(InputEvent ev)
//			throws InvalidLogicTupleException, OperationNotPossibleException {
//		final AbstractTupleCentreOperation op = ev.getSimpleTCEvent();
//        if (op.getTemplateArgument() == null) {
//            throw new InvalidLogicTupleException();
//        }
//        return this.getCore().getEnv(ev);
//	}
//
//	@Override
//	public IRespectOperation setEnv(InputEvent ev)
//			throws InvalidLogicTupleException, OperationNotPossibleException {
//		final AbstractTupleCentreOperation op = ev.getSimpleTCEvent();
//        if (op.getTemplateArgument() == null) {
//            throw new InvalidLogicTupleException();
//        }
//        return this.getCore().setEnv(ev);
//	}
}
