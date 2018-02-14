/*
 * ReSpecT - Copyright (C) aliCE team at deis.unibo.it This library is free
 * software; you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package alice.respect.core;

import java.util.List;

import alice.logictuple.LogicTuple;
import alice.logictuple.TupleArgument;
import alice.logictuple.exceptions.InvalidLogicTupleException;
import alice.logictuple.exceptions.InvalidLogicTupleOperationException;
import alice.respect.api.IRespectOperation;
import alice.respect.api.IRespectTC;
import alice.respect.api.ITimedContext;
import alice.respect.api.exceptions.OperationNotPossibleException;
import alice.respect.api.exceptions.OperationTimeOutException;
import alice.tuplecentre.core.AbstractTupleCentreOperation;
import alice.tuplecentre.core.InputEvent;

/**
 * 
 * A Timed Context wraps the access to a tuple centre virtual machine for a
 * specific thread of control, providing a timed interface.
 * 
 * @author Alessandro Ricci
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 * @author (contributor) Michele Bombardi (mailto:
 *         michele.bombardi@studio.unibo.it)
 */
public class TimedContext extends RootInterface implements ITimedContext {
    /**
     * 
     * @param core
     *            the ReSpecT tuple centres manager this interface refers to
     */
    public TimedContext(final IRespectTC core) {
        super(core);
    }

    @Override
    public List<LogicTuple> get(final InputEvent ev, final long ms)
            throws OperationNotPossibleException, OperationTimeOutException {
        final IRespectOperation op = this.getCore().get(ev);
        try {
            op.waitForOperationCompletion(ms);
        } catch (final alice.tuplecentre.api.exceptions.OperationTimeOutException ex) {
            throw new OperationTimeOutException(op);
        }
        return op.getLogicTupleListResult();
    }

    @Override
    public LogicTuple in(final InputEvent ev, final long ms)
            throws InvalidLogicTupleException, OperationNotPossibleException,
            OperationTimeOutException {
        final AbstractTupleCentreOperation inOp = ev.getSimpleTCEvent();
        final LogicTuple t = (LogicTuple) inOp.getTemplateArgument();
        if (t == null) {
            throw new InvalidLogicTupleException();
        }
        final IRespectOperation op = this.getCore().in(ev);
        try {
            op.waitForOperationCompletion(ms);
        } catch (final alice.tuplecentre.api.exceptions.OperationTimeOutException ex) {
            throw new OperationTimeOutException(op);
        }
        return this.unify(t, op.getLogicTupleResult());
    }

    @Override
    public LogicTuple inAll(final InputEvent ev, final long ms)
            throws InvalidLogicTupleException, OperationNotPossibleException,
            OperationTimeOutException {
        IRespectOperation op = null;
        TupleArgument arg = null;
        final AbstractTupleCentreOperation inOp = ev.getSimpleTCEvent();
        final LogicTuple t = (LogicTuple) inOp.getTemplateArgument();
        if (t == null) {
		    throw new InvalidLogicTupleException();
		}
		op = this.getCore().inAll(ev);
		try {
		    op.waitForOperationCompletion(ms);
		} catch (final alice.tuplecentre.api.exceptions.OperationTimeOutException ex) {
		    throw new OperationTimeOutException(op);
		}
		if (",".equals(t.getName()) && t.getArity() == 2) {
		    arg = t.getArg(1);
		    return this.unify(new LogicTuple(
		            new TupleArgument(arg.toTerm())), op
		            .getLogicTupleResult());
		}
        return op.getLogicTupleResult();
    }

    @Override
    public LogicTuple inp(final InputEvent ev, final long ms)
            throws InvalidLogicTupleException, OperationNotPossibleException,
            OperationTimeOutException {
        final AbstractTupleCentreOperation inOp = ev.getSimpleTCEvent();
        final LogicTuple t = (LogicTuple) inOp.getTemplateArgument();
        if (t == null) {
            throw new InvalidLogicTupleException();
        }
        final IRespectOperation op = this.getCore().inp(ev);
        try {
            op.waitForOperationCompletion(ms);
        } catch (final alice.tuplecentre.api.exceptions.OperationTimeOutException ex) {
            throw new OperationTimeOutException(op);
        }
        return this.unify(t, op.getLogicTupleResult());
    }

    @Override
    public LogicTuple no(final InputEvent ev, final long ms)
            throws InvalidLogicTupleException, OperationNotPossibleException,
            OperationTimeOutException {
        final AbstractTupleCentreOperation inOp = ev.getSimpleTCEvent();
        final LogicTuple t = (LogicTuple) inOp.getTemplateArgument();
        if (t == null) {
            throw new InvalidLogicTupleException();
        }
        final IRespectOperation op = this.getCore().no(ev);
        try {
            op.waitForOperationCompletion(ms);
        } catch (final alice.tuplecentre.api.exceptions.OperationTimeOutException ex) {
            throw new OperationTimeOutException(op);
        }
        return this.unify(t, op.getLogicTupleResult());
    }

    @Override
    public LogicTuple noAll(final InputEvent ev, final long ms)
            throws InvalidLogicTupleException, OperationNotPossibleException,
            OperationTimeOutException {
        IRespectOperation op = null;
        TupleArgument arg = null;
        final AbstractTupleCentreOperation inOp = ev.getSimpleTCEvent();
        final LogicTuple t = (LogicTuple) inOp.getTemplateArgument();
        if (t == null) {
		    throw new InvalidLogicTupleException();
		}
		op = this.getCore().noAll(ev);
		try {
		    op.waitForOperationCompletion(ms);
		} catch (final alice.tuplecentre.api.exceptions.OperationTimeOutException ex) {
		    throw new OperationTimeOutException(op);
		}
		if (",".equals(t.getName()) && t.getArity() == 2) {
		    arg = t.getArg(1);
		    return this.unify(new LogicTuple(
		            new TupleArgument(arg.toTerm())), op
		            .getLogicTupleResult());
		}
        return op.getLogicTupleResult();
    }

    @Override
    public LogicTuple nop(final InputEvent ev, final long ms)
            throws InvalidLogicTupleException, OperationNotPossibleException,
            OperationTimeOutException {
        final AbstractTupleCentreOperation inOp = ev.getSimpleTCEvent();
        final LogicTuple t = (LogicTuple) inOp.getTemplateArgument();
        if (t == null) {
            throw new InvalidLogicTupleException();
        }
        final IRespectOperation op = this.getCore().nop(ev);
        try {
            op.waitForOperationCompletion(ms);
        } catch (final alice.tuplecentre.api.exceptions.OperationTimeOutException ex) {
            throw new OperationTimeOutException(op);
        }
        return this.unify(t, op.getLogicTupleResult());
    }

    @Override
    public void out(final InputEvent ev, final long ms)
            throws InvalidLogicTupleException, OperationNotPossibleException,
            OperationTimeOutException {
        final AbstractTupleCentreOperation inOp = ev.getSimpleTCEvent();
        final LogicTuple t = (LogicTuple) inOp.getTupleArgument();
        if (t == null) {
            throw new InvalidLogicTupleException();
        }
        final IRespectOperation op = this.getCore().out(ev);
        try {
            op.waitForOperationCompletion(ms);
        } catch (final alice.tuplecentre.api.exceptions.OperationTimeOutException ex) {
            throw new OperationTimeOutException(op);
        }
    }

    @Override
    public void outAll(final InputEvent ev, final long ms)
            throws InvalidLogicTupleException, OperationNotPossibleException,
            OperationTimeOutException {
        final AbstractTupleCentreOperation inOp = ev.getSimpleTCEvent();
        final LogicTuple t = (LogicTuple) inOp.getTupleArgument();
        if (t == null) {
            throw new InvalidLogicTupleException();
        }
        final IRespectOperation op = this.getCore().outAll(ev);
        try {
            op.waitForOperationCompletion(ms);
        } catch (final alice.tuplecentre.api.exceptions.OperationTimeOutException ex) {
            throw new OperationTimeOutException(op);
        }
    }

    @Override
    public LogicTuple rd(final InputEvent ev, final long ms)
            throws InvalidLogicTupleException, OperationNotPossibleException,
            OperationTimeOutException {
        final AbstractTupleCentreOperation inOp = ev.getSimpleTCEvent();
        final LogicTuple t = (LogicTuple) inOp.getTemplateArgument();
        if (t == null) {
            throw new InvalidLogicTupleException();
        }
        final IRespectOperation op = this.getCore().rd(ev);
        try {
            op.waitForOperationCompletion(ms);
        } catch (final alice.tuplecentre.api.exceptions.OperationTimeOutException ex) {
            throw new OperationTimeOutException(op);
        }
        return this.unify(t, op.getLogicTupleResult());
    }

    @Override
    public LogicTuple rdAll(final InputEvent ev, final long ms)
            throws InvalidLogicTupleException, OperationNotPossibleException,
            OperationTimeOutException {
        IRespectOperation op = null;
        TupleArgument arg = null;
        final AbstractTupleCentreOperation inOp = ev.getSimpleTCEvent();
        final LogicTuple t = (LogicTuple) inOp.getTemplateArgument();
        if (t == null) {
		    throw new InvalidLogicTupleException();
		}
		op = this.getCore().rdAll(ev);
		try {
		    op.waitForOperationCompletion(ms);
		} catch (final alice.tuplecentre.api.exceptions.OperationTimeOutException ex) {
		    throw new OperationTimeOutException(op);
		}
		if (",".equals(t.getName()) && t.getArity() == 2) {
		    arg = t.getArg(1);
		    return this.unify(new LogicTuple(
		            new TupleArgument(arg.toTerm())), op
		            .getLogicTupleResult());
		}
        return op.getLogicTupleResult();
    }

    @Override
    public LogicTuple rdp(final InputEvent ev, final long ms)
            throws InvalidLogicTupleException, OperationNotPossibleException,
            OperationTimeOutException {
        final AbstractTupleCentreOperation inOp = ev.getSimpleTCEvent();
        final LogicTuple t = (LogicTuple) inOp.getTemplateArgument();
        if (t == null) {
            throw new InvalidLogicTupleException();
        }
        final IRespectOperation op = this.getCore().rdp(ev);
        try {
            op.waitForOperationCompletion(ms);
        } catch (final alice.tuplecentre.api.exceptions.OperationTimeOutException ex) {
            throw new OperationTimeOutException(op);
        }
        return this.unify(t, op.getLogicTupleResult());
    }

    @Override
    public List<LogicTuple> set(final InputEvent ev, final long ms)
            throws OperationNotPossibleException, InvalidLogicTupleException,
            OperationTimeOutException {
        final IRespectOperation op = this.getCore().set(ev);
        try {
            op.waitForOperationCompletion(ms);
        } catch (final alice.tuplecentre.api.exceptions.OperationTimeOutException ex) {
            throw new OperationTimeOutException(op);
        }
        return op.getLogicTupleListResult();
    }

    @Override
    public LogicTuple spawn(final InputEvent ev, final long ms)
            throws InvalidLogicTupleException, OperationNotPossibleException,
            OperationTimeOutException {
        final AbstractTupleCentreOperation inOp = ev.getSimpleTCEvent();
        final LogicTuple t = (LogicTuple) inOp.getTupleArgument();
        if (t == null) {
            throw new InvalidLogicTupleException();
        }
        final IRespectOperation op = this.getCore().spawn(ev);
        try {
            op.waitForOperationCompletion(ms);
        } catch (final alice.tuplecentre.api.exceptions.OperationTimeOutException ex) {
            throw new OperationTimeOutException(op);
        }
        return t;
    }

    @Override
    public LogicTuple uin(final InputEvent ev, final long ms)
            throws InvalidLogicTupleException, OperationNotPossibleException,
            OperationTimeOutException {
        final AbstractTupleCentreOperation inOp = ev.getSimpleTCEvent();
        final LogicTuple t = (LogicTuple) inOp.getTemplateArgument();
        if (t == null) {
            throw new InvalidLogicTupleException();
        }
        final IRespectOperation op = this.getCore().uin(ev);
        try {
            op.waitForOperationCompletion(ms);
        } catch (final alice.tuplecentre.api.exceptions.OperationTimeOutException ex) {
            throw new OperationTimeOutException(op);
        }
        return this.unify(t, op.getLogicTupleResult());
    }

    @Override
    public LogicTuple uinp(final InputEvent ev, final long ms)
            throws InvalidLogicTupleException, OperationNotPossibleException,
            OperationTimeOutException {
        final AbstractTupleCentreOperation inOp = ev.getSimpleTCEvent();
        final LogicTuple t = (LogicTuple) inOp.getTemplateArgument();
        if (t == null) {
            throw new InvalidLogicTupleException();
        }
        final IRespectOperation op = this.getCore().uinp(ev);
        try {
            op.waitForOperationCompletion(ms);
        } catch (final alice.tuplecentre.api.exceptions.OperationTimeOutException ex) {
            throw new OperationTimeOutException(op);
        }
        final LogicTuple result = op.getLogicTupleResult();
        return this.unify(t, result);
    }

    @Override
    public LogicTuple uno(final InputEvent ev, final long ms)
            throws InvalidLogicTupleException, OperationNotPossibleException,
            OperationTimeOutException {
        final AbstractTupleCentreOperation inOp = ev.getSimpleTCEvent();
        final LogicTuple t = (LogicTuple) inOp.getTemplateArgument();
        if (t == null) {
            throw new InvalidLogicTupleException();
        }
        final IRespectOperation op = this.getCore().uno(ev);
        try {
            op.waitForOperationCompletion(ms);
        } catch (final alice.tuplecentre.api.exceptions.OperationTimeOutException ex) {
            throw new OperationTimeOutException(op);
        }
        return this.unify(t, op.getLogicTupleResult());
    }

    @Override
    public LogicTuple unop(final InputEvent ev, final long ms)
            throws InvalidLogicTupleException, OperationNotPossibleException,
            OperationTimeOutException {
        final AbstractTupleCentreOperation inOp = ev.getSimpleTCEvent();
        final LogicTuple t = (LogicTuple) inOp.getTemplateArgument();
        if (t == null) {
            throw new InvalidLogicTupleException();
        }
        final IRespectOperation op = this.getCore().unop(ev);
        try {
            op.waitForOperationCompletion(ms);
        } catch (final alice.tuplecentre.api.exceptions.OperationTimeOutException ex) {
            throw new OperationTimeOutException(op);
        }
        final LogicTuple result = op.getLogicTupleResult();
        return this.unify(t, result);
    }

    @Override
    public LogicTuple urd(final InputEvent ev, final long ms)
            throws InvalidLogicTupleException, OperationNotPossibleException,
            OperationTimeOutException {
        final AbstractTupleCentreOperation inOp = ev.getSimpleTCEvent();
        final LogicTuple t = (LogicTuple) inOp.getTemplateArgument();
        if (t == null) {
            throw new InvalidLogicTupleException();
        }
        final IRespectOperation op = this.getCore().urd(ev);
        try {
            op.waitForOperationCompletion(ms);
        } catch (final alice.tuplecentre.api.exceptions.OperationTimeOutException ex) {
            throw new OperationTimeOutException(op);
        }
        return this.unify(t, op.getLogicTupleResult());
    }

    @Override
    public LogicTuple urdp(final InputEvent ev, final long ms)
            throws InvalidLogicTupleException, OperationNotPossibleException,
            OperationTimeOutException {
        final AbstractTupleCentreOperation inOp = ev.getSimpleTCEvent();
        final LogicTuple t = (LogicTuple) inOp.getTemplateArgument();
        if (t == null) {
            throw new InvalidLogicTupleException();
        }
        final IRespectOperation op = this.getCore().urdp(ev);
        try {
            op.waitForOperationCompletion(ms);
        } catch (final alice.tuplecentre.api.exceptions.OperationTimeOutException ex) {
            throw new OperationTimeOutException(op);
        }
        final LogicTuple result = op.getLogicTupleResult();
        return this.unify(t, result);
    }
}
