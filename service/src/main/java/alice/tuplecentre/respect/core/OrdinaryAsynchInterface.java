package alice.tuplecentre.respect.core;

import alice.logictuple.exceptions.InvalidLogicTupleException;
import alice.tuplecentre.core.AbstractTupleCentreOperation;
import alice.tuplecentre.core.InputEvent;
import alice.tuplecentre.respect.api.IOrdinaryAsynchInterface;
import alice.tuplecentre.respect.api.IRespectTC;
import alice.tuplecentre.respect.api.RespectOperation;
import alice.tuplecentre.respect.api.exceptions.OperationNotPossibleException;

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
    public RespectOperation get(final InputEvent ev)
            throws OperationNotPossibleException {
        return this.getCore().get(ev);
    }

    @Override
    public RespectOperation in(final InputEvent ev)
            throws InvalidLogicTupleException, OperationNotPossibleException {
        final AbstractTupleCentreOperation op = ev.getSimpleTCEvent();
        if (op.getTemplateArgument() == null) {
            throw new InvalidLogicTupleException();
        }
        return this.getCore().in(ev);
    }

    @Override
    public RespectOperation inAll(final InputEvent ev)
            throws InvalidLogicTupleException, OperationNotPossibleException {
        final AbstractTupleCentreOperation op = ev.getSimpleTCEvent();
        if (op.getTemplateArgument() == null) {
            throw new InvalidLogicTupleException();
        }
        return this.getCore().inAll(ev);
    }

    @Override
    public RespectOperation inp(final InputEvent ev)
            throws InvalidLogicTupleException, OperationNotPossibleException {
        final AbstractTupleCentreOperation op = ev.getSimpleTCEvent();
        if (op.getTemplateArgument() == null) {
            throw new InvalidLogicTupleException();
        }
        return this.getCore().inp(ev);
    }

    @Override
    public RespectOperation no(final InputEvent ev)
            throws InvalidLogicTupleException, OperationNotPossibleException {
        final AbstractTupleCentreOperation op = ev.getSimpleTCEvent();
        if (op.getTemplateArgument() == null) {
            throw new InvalidLogicTupleException();
        }
        return this.getCore().no(ev);
    }

    @Override
    public RespectOperation noAll(final InputEvent ev)
            throws InvalidLogicTupleException, OperationNotPossibleException {
        final AbstractTupleCentreOperation op = ev.getSimpleTCEvent();
        if (op.getTemplateArgument() == null) {
            throw new InvalidLogicTupleException();
        }
        return this.getCore().noAll(ev);
    }

    @Override
    public RespectOperation nop(final InputEvent ev)
            throws InvalidLogicTupleException, OperationNotPossibleException {
        final AbstractTupleCentreOperation op = ev.getSimpleTCEvent();
        if (op.getTemplateArgument() == null) {
            throw new InvalidLogicTupleException();
        }
        return this.getCore().nop(ev);
    }

    @Override
    public RespectOperation out(final InputEvent ev)
            throws InvalidLogicTupleException, OperationNotPossibleException {
        if (ev.getTuple() == null) {
            throw new InvalidLogicTupleException();
        }
        return this.getCore().out(ev);
    }

    @Override
    public RespectOperation outAll(final InputEvent ev)
            throws InvalidLogicTupleException, OperationNotPossibleException {
        if (ev.getTuple() == null) {
            throw new InvalidLogicTupleException();
        }
        return this.getCore().outAll(ev);
    }

    @Override
    public RespectOperation rd(final InputEvent ev)
            throws InvalidLogicTupleException, OperationNotPossibleException {
        final AbstractTupleCentreOperation op = ev.getSimpleTCEvent();
        if (op.getTemplateArgument() == null) {
            throw new InvalidLogicTupleException();
        }
        return this.getCore().rd(ev);
    }

    @Override
    public RespectOperation rdAll(final InputEvent ev)
            throws InvalidLogicTupleException, OperationNotPossibleException {
        final AbstractTupleCentreOperation op = ev.getSimpleTCEvent();
        if (op.getTemplateArgument() == null) {
            throw new InvalidLogicTupleException();
        }
        return this.getCore().rdAll(ev);
    }

    @Override
    public RespectOperation rdp(final InputEvent ev)
            throws InvalidLogicTupleException, OperationNotPossibleException {
        final AbstractTupleCentreOperation op = ev.getSimpleTCEvent();
        if (op.getTemplateArgument() == null) {
            throw new InvalidLogicTupleException();
        }
        return this.getCore().rdp(ev);
    }

    @Override
    public RespectOperation set(final InputEvent ev)
            throws InvalidLogicTupleException, OperationNotPossibleException {
        final AbstractTupleCentreOperation op = ev.getSimpleTCEvent();
        if (op.getTupleListArgument() == null) {
            throw new InvalidLogicTupleException();
        }
        return this.getCore().set(ev);
    }

    @Override
    public RespectOperation spawn(final InputEvent ev)
            throws InvalidLogicTupleException, OperationNotPossibleException {
        if (ev.getTuple() == null) {
            throw new InvalidLogicTupleException();
        }
        return this.getCore().spawn(ev);
    }

    @Override
    public RespectOperation uin(final InputEvent ev)
            throws InvalidLogicTupleException, OperationNotPossibleException {
        final AbstractTupleCentreOperation op = ev.getSimpleTCEvent();
        if (op.getTemplateArgument() == null) {
            throw new InvalidLogicTupleException();
        }
        return this.getCore().uin(ev);
    }

    @Override
    public RespectOperation uinp(final InputEvent ev)
            throws InvalidLogicTupleException, OperationNotPossibleException {
        final AbstractTupleCentreOperation op = ev.getSimpleTCEvent();
        if (op.getTemplateArgument() == null) {
            throw new InvalidLogicTupleException();
        }
        return this.getCore().uinp(ev);
    }

    @Override
    public RespectOperation uno(final InputEvent ev)
            throws InvalidLogicTupleException, OperationNotPossibleException {
        final AbstractTupleCentreOperation op = ev.getSimpleTCEvent();
        if (op.getTemplateArgument() == null) {
            throw new InvalidLogicTupleException();
        }
        return this.getCore().uno(ev);
    }

    @Override
    public RespectOperation unop(final InputEvent ev)
            throws InvalidLogicTupleException, OperationNotPossibleException {
        final AbstractTupleCentreOperation op = ev.getSimpleTCEvent();
        if (op.getTemplateArgument() == null) {
            throw new InvalidLogicTupleException();
        }
        return this.getCore().unop(ev);
    }

    @Override
    public RespectOperation urd(final InputEvent ev)
            throws InvalidLogicTupleException, OperationNotPossibleException {
        final AbstractTupleCentreOperation op = ev.getSimpleTCEvent();
        if (op.getTemplateArgument() == null) {
            throw new InvalidLogicTupleException();
        }
        return this.getCore().urd(ev);
    }

    @Override
    public RespectOperation urdp(final InputEvent ev)
            throws InvalidLogicTupleException, OperationNotPossibleException {
        final AbstractTupleCentreOperation op = ev.getSimpleTCEvent();
        if (op.getTemplateArgument() == null) {
            throw new InvalidLogicTupleException();
        }
        return this.getCore().urdp(ev);
    }

//	@Override
//	public RespectOperation getEnv(InputEvent ev)
//			throws InvalidLogicTupleException, OperationNotPossibleException {
//		final AbstractTupleCentreOperation op = ev.getSimpleTCEvent();
//        if (op.getTemplateArgument() == null) {
//            throw new InvalidLogicTupleException();
//        }
//        return this.getCore().getEnv(ev);
//	}
//
//	@Override
//	public RespectOperation setEnv(InputEvent ev)
//			throws InvalidLogicTupleException, OperationNotPossibleException {
//		final AbstractTupleCentreOperation op = ev.getSimpleTCEvent();
//        if (op.getTemplateArgument() == null) {
//            throw new InvalidLogicTupleException();
//        }
//        return this.getCore().setEnv(ev);
//	}
}
