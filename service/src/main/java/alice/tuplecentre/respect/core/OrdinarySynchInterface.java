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
package alice.tuplecentre.respect.core;

import java.util.Iterator;
import java.util.List;

import alice.tuple.logic.LogicTuple;
import alice.tuple.logic.LogicTuples;
import alice.tuple.logic.TupleArgument;
import alice.tuple.logic.TupleArguments;
import alice.tuple.logic.exceptions.InvalidLogicTupleException;
import alice.tuplecentre.core.AbstractTupleCentreOperation;
import alice.tuplecentre.core.InputEvent;
import alice.tuplecentre.respect.api.IOrdinarySynchInterface;
import alice.tuplecentre.respect.api.IRespectTC;
import alice.tuplecentre.respect.api.RespectOperation;
import alice.tuplecentre.respect.api.exceptions.OperationNotPossibleException;
import alice.tuprolog.Struct;
import alice.tuprolog.Term;

/**
 * A Blocking Context wraps the access to a tuple centre virtual machine for a
 * specific thread of control, providing a blocking interface.
 *
 * @author Alessandro Ricci
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 * @author (contributor) Michele Bombardi (mailto:
 * michele.bombardi@studio.unibo.it)
 */
public class OrdinarySynchInterface extends RootInterface implements
        IOrdinarySynchInterface {
    private static Term list2tuple(final List<LogicTuple> list) {
        final Term[] termArray = new Term[list.size()];
        final Iterator<LogicTuple> it = list.iterator();
        int i = 0;
        while (it.hasNext()) {
            termArray[i] = it.next().toTerm();
            i++;
        }
        return new Struct(termArray);
    }

    /**
     * @param core the ReSpecT tuple centre this context refers to
     */
    public OrdinarySynchInterface(final IRespectTC core) {
        super(core);
    }

    @Override
    public List<LogicTuple> get(final InputEvent ev)
            throws OperationNotPossibleException {
        final RespectOperation op = this.getCore().get(ev);
        op.waitForOperationCompletion();
        return op.getLogicTupleListResult();
    }

    @Override
    public LogicTuple in(final InputEvent ev)
            throws InvalidLogicTupleException, OperationNotPossibleException {
        final AbstractTupleCentreOperation inOp = ev.getSimpleTCEvent();
        final LogicTuple t = (LogicTuple) inOp.getTemplateArgument();
        if (t == null) {
            throw new InvalidLogicTupleException();
        }
        final RespectOperation op = this.getCore().in(ev);
        op.waitForOperationCompletion();
        if (op.getLogicTupleResult() != null) {
            return this.unify(t, op.getLogicTupleResult());
        }
        return null;
    }

    @Override
    public List<LogicTuple> inAll(final InputEvent ev)
            throws InvalidLogicTupleException, OperationNotPossibleException {
        RespectOperation op;
        TupleArgument arg;
        final AbstractTupleCentreOperation inOp = ev.getSimpleTCEvent();
        final LogicTuple t = (LogicTuple) inOp.getTemplateArgument();
        //try {
        if (t == null) {
            throw new InvalidLogicTupleException();
        }
        op = this.getCore().inAll(ev);
        op.waitForOperationCompletion();
        if (",".equals(t.getName()) && t.getArity() == 2) {
            arg = ((LogicTuple) ev.getTuple()).getArg(1);
            this.unify(
                    LogicTuples.newInstance(TupleArguments.newInstance(arg.toTerm())),
                    LogicTuples.newInstance(OrdinarySynchInterface.list2tuple(op
                            .getLogicTupleListResult())));
            return op.getLogicTupleListResult();
        }
        // } catch (final InvalidLogicTupleOperationException e2) {
        //   throw new OperationNotPossibleException();
        //  }
        return op.getLogicTupleListResult();
    }

    @Override
    public LogicTuple inp(final InputEvent ev)
            throws InvalidLogicTupleException, OperationNotPossibleException {
        final AbstractTupleCentreOperation inOp = ev.getSimpleTCEvent();
        final LogicTuple t = (LogicTuple) inOp.getTemplateArgument();
        if (t == null) {
            throw new InvalidLogicTupleException();
        }
        final RespectOperation op = this.getCore().inp(ev);
        op.waitForOperationCompletion();
        return this.unify(t, op.getLogicTupleResult());
    }

    @Override
    public LogicTuple no(final InputEvent ev)
            throws InvalidLogicTupleException, OperationNotPossibleException {
        final AbstractTupleCentreOperation inOp = ev.getSimpleTCEvent();
        final LogicTuple t = (LogicTuple) inOp.getTemplateArgument();
        if (t == null) {
            throw new InvalidLogicTupleException();
        }
        final RespectOperation op = this.getCore().no(ev);
        op.waitForOperationCompletion();
        return this.unify(t, op.getLogicTupleResult());
    }

    @Override
    public List<LogicTuple> noAll(final InputEvent ev)
            throws InvalidLogicTupleException, OperationNotPossibleException {
        RespectOperation op;
        TupleArgument arg;
        final AbstractTupleCentreOperation inOp = ev.getSimpleTCEvent();
        final LogicTuple t = (LogicTuple) inOp.getTemplateArgument();
        //try {
        if (t == null) {
            throw new InvalidLogicTupleException();
        }
        op = this.getCore().noAll(ev);
        op.waitForOperationCompletion();
        if (",".equals(t.getName()) && t.getArity() == 2) {
            arg = t.getArg(1);
            this.unify(
                    LogicTuples.newInstance(TupleArguments.newInstance(arg.toTerm())),
                    LogicTuples.newInstance(OrdinarySynchInterface.list2tuple(op
                            .getLogicTupleListResult())));
            return op.getLogicTupleListResult();
        }
        // } catch (final InvalidLogicTupleOperationException e2) {
        //     throw new OperationNotPossibleException();
        //  }
        return op.getLogicTupleListResult();
    }

    @Override
    public LogicTuple nop(final InputEvent ev)
            throws InvalidLogicTupleException, OperationNotPossibleException {
        final AbstractTupleCentreOperation inOp = ev.getSimpleTCEvent();
        final LogicTuple t = (LogicTuple) inOp.getTemplateArgument();
        if (t == null) {
            throw new InvalidLogicTupleException();
        }
        final RespectOperation op = this.getCore().nop(ev);
        op.waitForOperationCompletion();
        return this.unify(t, op.getLogicTupleResult());
    }

    @Override
    public void out(final InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException {
        final LogicTuple t = (LogicTuple) ev.getTuple();
        if (t == null) {
            throw new InvalidLogicTupleException();
        }
        final RespectOperation op = this.getCore().out(ev);
        op.waitForOperationCompletion();
    }

    @Override
    public List<LogicTuple> outAll(final InputEvent ev)
            throws InvalidLogicTupleException, OperationNotPossibleException {
        final LogicTuple t = (LogicTuple) ev.getTuple();
        if (t == null) {
            throw new InvalidLogicTupleException();
        }
        final RespectOperation op = this.getCore().outAll(ev);
        op.waitForOperationCompletion();
        return op.getLogicTupleListResult();
    }

    @Override
    public LogicTuple rd(final InputEvent ev)
            throws InvalidLogicTupleException, OperationNotPossibleException {
        final AbstractTupleCentreOperation inOp = ev.getSimpleTCEvent();
        final LogicTuple t = (LogicTuple) inOp.getTemplateArgument();
        if (t == null) {
            throw new InvalidLogicTupleException();
        }
        final RespectOperation op = this.getCore().rd(ev);
        op.waitForOperationCompletion();
        return this.unify(t, op.getLogicTupleResult());
    }

    @Override
    public List<LogicTuple> rdAll(final InputEvent ev)
            throws InvalidLogicTupleException, OperationNotPossibleException {
        RespectOperation op;
        TupleArgument arg;
        final AbstractTupleCentreOperation inOp = ev.getSimpleTCEvent();
        final LogicTuple t = (LogicTuple) inOp.getTemplateArgument();
        // try {
        if (t == null) {
            throw new InvalidLogicTupleException();
        }
        op = this.getCore().rdAll(ev);
        op.waitForOperationCompletion();
        if (",".equals(t.getName()) && t.getArity() == 2) {
            arg = t.getArg(1);
            this.unify(
                    LogicTuples.newInstance(TupleArguments.newInstance(arg.toTerm())),
                    LogicTuples.newInstance(OrdinarySynchInterface.list2tuple(op
                            .getLogicTupleListResult())));
            return op.getLogicTupleListResult();
        }
        // } catch (final InvalidLogicTupleOperationException e2) {
        //  throw new OperationNotPossibleException();
        // }
        return op.getLogicTupleListResult();
    }

    @Override
    public LogicTuple rdp(final InputEvent ev)
            throws InvalidLogicTupleException, OperationNotPossibleException {
        final AbstractTupleCentreOperation inOp = ev.getSimpleTCEvent();
        final LogicTuple t = (LogicTuple) inOp.getTemplateArgument();
        if (t == null) {
            throw new InvalidLogicTupleException();
        }
        final RespectOperation op = this.getCore().rdp(ev);
        op.waitForOperationCompletion();
        return this.unify(t, op.getLogicTupleResult());
    }

    @Override
    public List<LogicTuple> set(final InputEvent ev)
            throws OperationNotPossibleException {
        final RespectOperation op = this.getCore().set(ev);
        op.waitForOperationCompletion();
        return op.getLogicTupleListResult();
    }

    @Override
    public LogicTuple spawn(final InputEvent ev)
            throws InvalidLogicTupleException, OperationNotPossibleException {
        final LogicTuple t = (LogicTuple) ev.getTuple();
        if (t == null) {
            throw new InvalidLogicTupleException();
        }
        final RespectOperation op = this.getCore().spawn(ev);
        op.waitForOperationCompletion();
        return t;
    }

    @Override
    public LogicTuple uin(final InputEvent ev)
            throws InvalidLogicTupleException, OperationNotPossibleException {
        final AbstractTupleCentreOperation inOp = ev.getSimpleTCEvent();
        final LogicTuple t = (LogicTuple) inOp.getTemplateArgument();
        if (t == null) {
            throw new InvalidLogicTupleException();
        }
        final RespectOperation op = this.getCore().uin(ev);
        op.waitForOperationCompletion();
        return this.unify(t, op.getLogicTupleResult());
    }

    @Override
    public LogicTuple uinp(final InputEvent ev)
            throws InvalidLogicTupleException, OperationNotPossibleException {
        final AbstractTupleCentreOperation inOp = ev.getSimpleTCEvent();
        final LogicTuple t = (LogicTuple) inOp.getTemplateArgument();
        if (t == null) {
            throw new InvalidLogicTupleException();
        }
        final RespectOperation op = this.getCore().uinp(ev);
        op.waitForOperationCompletion();
        final LogicTuple result = op.getLogicTupleResult();
        return this.unify(t, result);
    }

    @Override
    public LogicTuple uno(final InputEvent ev)
            throws InvalidLogicTupleException, OperationNotPossibleException {
        final AbstractTupleCentreOperation inOp = ev.getSimpleTCEvent();
        final LogicTuple t = (LogicTuple) inOp.getTemplateArgument();
        if (t == null) {
            throw new InvalidLogicTupleException();
        }
        final RespectOperation op = this.getCore().uno(ev);
        op.waitForOperationCompletion();
        return this.unify(t, op.getLogicTupleResult());
    }

    @Override
    public LogicTuple unop(final InputEvent ev)
            throws InvalidLogicTupleException, OperationNotPossibleException {
        final AbstractTupleCentreOperation inOp = ev.getSimpleTCEvent();
        final LogicTuple t = (LogicTuple) inOp.getTemplateArgument();
        if (t == null) {
            throw new InvalidLogicTupleException();
        }
        final RespectOperation op = this.getCore().unop(ev);
        op.waitForOperationCompletion();
        final LogicTuple result = op.getLogicTupleResult();
        return this.unify(t, result);
    }

    @Override
    public LogicTuple urd(final InputEvent ev)
            throws InvalidLogicTupleException, OperationNotPossibleException {
        final AbstractTupleCentreOperation inOp = ev.getSimpleTCEvent();
        final LogicTuple t = (LogicTuple) inOp.getTemplateArgument();
        if (t == null) {
            throw new InvalidLogicTupleException();
        }
        final RespectOperation op = this.getCore().urd(ev);
        op.waitForOperationCompletion();
        return this.unify(t, op.getLogicTupleResult());
    }

    @Override
    public LogicTuple urdp(final InputEvent ev)
            throws InvalidLogicTupleException, OperationNotPossibleException {
        final AbstractTupleCentreOperation inOp = ev.getSimpleTCEvent();
        final LogicTuple t = (LogicTuple) inOp.getTemplateArgument();
        if (t == null) {
            throw new InvalidLogicTupleException();
        }
        final RespectOperation op = this.getCore().urdp(ev);
        op.waitForOperationCompletion();
        final LogicTuple result = op.getLogicTupleResult();
        return this.unify(t, result);
    }
}
