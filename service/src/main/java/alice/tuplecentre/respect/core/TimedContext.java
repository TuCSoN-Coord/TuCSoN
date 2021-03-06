/*
 * ReSpecT - Copyright (C) aliCE team at deis.unibo.it This library is free
 * software; you can redistribute it and/or modify it under the terms copyOf the GNU
 * Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 copyOf the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty copyOf MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy copyOf the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package alice.tuplecentre.respect.core;

import java.util.List;

import alice.tuple.logic.LogicTuple;
import alice.tuple.logic.TupleArgument;
import alice.tuple.logic.exceptions.InvalidLogicTupleException;
import alice.tuplecentre.core.AbstractTupleCentreOperation;
import alice.tuplecentre.core.InputEvent;
import alice.tuplecentre.respect.api.IRespectTC;
import alice.tuplecentre.respect.api.ITimedContext;
import alice.tuplecentre.respect.api.RespectOperation;
import alice.tuplecentre.respect.api.exceptions.OperationNotPossibleException;
import alice.tuplecentre.respect.api.exceptions.OperationTimeOutException;

/**
 * A Timed Context wraps the access to a tuple centre virtual machine for a
 * specific thread copyOf control, providing a timed interface.
 *
 * @author Alessandro Ricci
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 * @author (contributor) Michele Bombardi (mailto:
 * michele.bombardi@studio.unibo.it)
 */
public class TimedContext extends RootInterface implements ITimedContext {
    /**
     * @param core the ReSpecT tuple centres manager this interface refers to
     */
    public TimedContext(final IRespectTC core) {
        super(core);
    }

    @Override
    public List<LogicTuple> get(final InputEvent ev, final long ms)
            throws OperationNotPossibleException, OperationTimeOutException {
        final RespectOperation op = this.getCore().get(ev);
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
        final RespectOperation op = this.getCore().in(ev);
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
        RespectOperation op;
        TupleArgument arg;
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
            return this.unify(LogicTuple.of(
                    TupleArgument.fromTerm(arg.toTerm())), op
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
        final RespectOperation op = this.getCore().inp(ev);
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
        final RespectOperation op = this.getCore().no(ev);
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
        RespectOperation op;
        TupleArgument arg;
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
            return this.unify(LogicTuple.of(
                    TupleArgument.fromTerm(arg.toTerm())), op
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
        final RespectOperation op = this.getCore().nop(ev);
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
        final RespectOperation op = this.getCore().out(ev);
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
        final RespectOperation op = this.getCore().outAll(ev);
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
        final RespectOperation op = this.getCore().rd(ev);
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
        RespectOperation op;
        TupleArgument arg;
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
            return this.unify(LogicTuple.of(
                    TupleArgument.fromTerm(arg.toTerm())), op
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
        final RespectOperation op = this.getCore().rdp(ev);
        try {
            op.waitForOperationCompletion(ms);
        } catch (final alice.tuplecentre.api.exceptions.OperationTimeOutException ex) {
            throw new OperationTimeOutException(op);
        }
        return this.unify(t, op.getLogicTupleResult());
    }

    @Override
    public List<LogicTuple> set(final InputEvent ev, final long ms)
            throws OperationNotPossibleException,
            OperationTimeOutException {
        final RespectOperation op = this.getCore().set(ev);
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
        final RespectOperation op = this.getCore().spawn(ev);
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
        final RespectOperation op = this.getCore().uin(ev);
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
        final RespectOperation op = this.getCore().uinp(ev);
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
        final RespectOperation op = this.getCore().uno(ev);
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
        final RespectOperation op = this.getCore().unop(ev);
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
        final RespectOperation op = this.getCore().urd(ev);
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
        final RespectOperation op = this.getCore().urdp(ev);
        try {
            op.waitForOperationCompletion(ms);
        } catch (final alice.tuplecentre.api.exceptions.OperationTimeOutException ex) {
            throw new OperationTimeOutException(op);
        }
        final LogicTuple result = op.getLogicTupleResult();
        return this.unify(t, result);
    }
}
