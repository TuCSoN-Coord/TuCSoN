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
import alice.logictuple.exceptions.InvalidTupleOperationException;
import alice.respect.api.IRespectOperation;
import alice.respect.api.IRespectTC;
import alice.respect.api.ITimedContext;
import alice.respect.api.exceptions.OperationNotPossibleException;
import alice.respect.api.exceptions.OperationTimeOutException;
import alice.tuplecentre.api.IId;

/**
 * 
 * A Timed Context wraps the access to a tuple centre virtual machine for a
 * specific thread of control, providing a timed interface.
 * 
 * @author aricci
 */
public class TimedContext extends RootInterface implements ITimedContext {

    public TimedContext(final IRespectTC core) {
        super(core);
    }

    public List<LogicTuple> get(final IId aid, final long ms)
            throws OperationNotPossibleException, OperationTimeOutException {
        final IRespectOperation op = this.getCore().get(aid);
        try {
            op.waitForOperationCompletion(ms);
        } catch (final alice.tuplecentre.api.exceptions.OperationTimeOutException ex) {
            throw new OperationTimeOutException(op);
        }
        return op.getLogicTupleListResult();
    }

    public LogicTuple in(final IId id, final LogicTuple t, final long ms)
            throws InvalidLogicTupleException, OperationNotPossibleException,
            OperationTimeOutException {
        if (t == null) {
            throw new InvalidLogicTupleException();
        }
        final IRespectOperation op = this.getCore().in(id, t);
        try {
            op.waitForOperationCompletion(ms);
        } catch (final alice.tuplecentre.api.exceptions.OperationTimeOutException ex) {
            throw new OperationTimeOutException(op);
        }
        return this.unify(t, op.getLogicTupleResult());
    }

    public LogicTuple in_all(final IId aid, final LogicTuple t, final long ms)
            throws InvalidLogicTupleException, OperationNotPossibleException,
            OperationTimeOutException {
        IRespectOperation op = null;
        TupleArgument arg = null;
        try {
            if (t == null) {
                throw new InvalidLogicTupleException();
            } else if (t.getName().equals(",") && (t.getArity() == 2)) {
                op = this.getCore().in_all(aid, new LogicTuple(t.getArg(0)));
            } else {
                op = this.getCore().in_all(aid, t);
            }
            try {
                op.waitForOperationCompletion(ms);
            } catch (final alice.tuplecentre.api.exceptions.OperationTimeOutException ex) {
                throw new OperationTimeOutException(op);
            }
            if (t.getName().equals(",") && (t.getArity() == 2)) {
                arg = t.getArg(1);
                return this.unify(new LogicTuple(
                        new TupleArgument(arg.toTerm())), op
                        .getLogicTupleResult());
            }
        } catch (final InvalidTupleOperationException e2) {
            throw new OperationNotPossibleException();
        }
        return op.getLogicTupleResult();
    }

    public LogicTuple inp(final IId id, final LogicTuple t, final long ms)
            throws InvalidLogicTupleException, OperationNotPossibleException,
            OperationTimeOutException {
        if (t == null) {
            throw new InvalidLogicTupleException();
        }
        final IRespectOperation op = this.getCore().inp(id, t);
        try {
            op.waitForOperationCompletion(ms);
        } catch (final alice.tuplecentre.api.exceptions.OperationTimeOutException ex) {
            throw new OperationTimeOutException(op);
        }
        return this.unify(t, op.getLogicTupleResult());
    }

    public LogicTuple no(final IId id, final LogicTuple t, final long ms)
            throws InvalidLogicTupleException, OperationNotPossibleException,
            OperationTimeOutException {
        if (t == null) {
            throw new InvalidLogicTupleException();
        }
        final IRespectOperation op = this.getCore().no(id, t);
        try {
            op.waitForOperationCompletion(ms);
        } catch (final alice.tuplecentre.api.exceptions.OperationTimeOutException ex) {
            throw new OperationTimeOutException(op);
        }
        return this.unify(t, op.getLogicTupleResult());
    }

    public LogicTuple no_all(final IId aid, final LogicTuple t, final long ms)
            throws InvalidLogicTupleException, OperationNotPossibleException,
            OperationTimeOutException {
        IRespectOperation op = null;
        TupleArgument arg = null;
        try {
            if (t == null) {
                throw new InvalidLogicTupleException();
            } else if (t.getName().equals(",") && (t.getArity() == 2)) {
                op = this.getCore().no_all(aid, new LogicTuple(t.getArg(0)));
            } else {
                op = this.getCore().no_all(aid, t);
            }
            try {
                op.waitForOperationCompletion(ms);
            } catch (final alice.tuplecentre.api.exceptions.OperationTimeOutException ex) {
                throw new OperationTimeOutException(op);
            }
            if (t.getName().equals(",") && (t.getArity() == 2)) {
                arg = t.getArg(1);
                return this.unify(new LogicTuple(
                        new TupleArgument(arg.toTerm())), op
                        .getLogicTupleResult());
            }
        } catch (final InvalidTupleOperationException e2) {
            throw new OperationNotPossibleException();
        }
        return op.getLogicTupleResult();
    }

    public LogicTuple nop(final IId id, final LogicTuple t, final long ms)
            throws InvalidLogicTupleException, OperationNotPossibleException,
            OperationTimeOutException {
        if (t == null) {
            throw new InvalidLogicTupleException();
        }
        final IRespectOperation op = this.getCore().nop(id, t);
        try {
            op.waitForOperationCompletion(ms);
        } catch (final alice.tuplecentre.api.exceptions.OperationTimeOutException ex) {
            throw new OperationTimeOutException(op);
        }
        return this.unify(t, op.getLogicTupleResult());
    }

    public void out(final IId id, final LogicTuple t, final long ms)
            throws InvalidLogicTupleException, OperationNotPossibleException,
            OperationTimeOutException {
        if (t == null) {
            throw new InvalidLogicTupleException();
        }
        final IRespectOperation op = this.getCore().out(id, t);
        try {
            op.waitForOperationCompletion(ms);
        } catch (final alice.tuplecentre.api.exceptions.OperationTimeOutException ex) {
            throw new OperationTimeOutException(op);
        }
    }

    public void out_all(final IId id, final LogicTuple t, final long ms)
            throws InvalidLogicTupleException, OperationNotPossibleException,
            OperationTimeOutException {
        if (t == null) {
            throw new InvalidLogicTupleException();
        }
        final IRespectOperation op = this.getCore().out_all(id, t);
        try {
            op.waitForOperationCompletion(ms);
        } catch (final alice.tuplecentre.api.exceptions.OperationTimeOutException ex) {
            throw new OperationTimeOutException(op);
        }
    }

    public LogicTuple rd(final IId id, final LogicTuple t, final long ms)
            throws InvalidLogicTupleException, OperationNotPossibleException,
            OperationTimeOutException {
        if (t == null) {
            throw new InvalidLogicTupleException();
        }
        final IRespectOperation op = this.getCore().rd(id, t);
        try {
            op.waitForOperationCompletion(ms);
        } catch (final alice.tuplecentre.api.exceptions.OperationTimeOutException ex) {
            throw new OperationTimeOutException(op);
        }
        return this.unify(t, op.getLogicTupleResult());
    }

    public LogicTuple rd_all(final IId aid, final LogicTuple t, final long ms)
            throws InvalidLogicTupleException, OperationNotPossibleException,
            OperationTimeOutException {
        IRespectOperation op = null;
        TupleArgument arg = null;
        try {
            if (t == null) {
                throw new InvalidLogicTupleException();
            } else if (t.getName().equals(",") && (t.getArity() == 2)) {
                op = this.getCore().rd_all(aid, new LogicTuple(t.getArg(0)));
            } else {
                op = this.getCore().rd_all(aid, t);
            }
            try {
                op.waitForOperationCompletion(ms);
            } catch (final alice.tuplecentre.api.exceptions.OperationTimeOutException ex) {
                throw new OperationTimeOutException(op);
            }
            if (t.getName().equals(",") && (t.getArity() == 2)) {
                arg = t.getArg(1);
                return this.unify(new LogicTuple(
                        new TupleArgument(arg.toTerm())), op
                        .getLogicTupleResult());
            }
        } catch (final InvalidTupleOperationException e2) {
            throw new OperationNotPossibleException();
        }
        return op.getLogicTupleResult();
    }

    public LogicTuple rdp(final IId id, final LogicTuple t, final long ms)
            throws InvalidLogicTupleException, OperationNotPossibleException,
            OperationTimeOutException {
        if (t == null) {
            throw new InvalidLogicTupleException();
        }
        final IRespectOperation op = this.getCore().rdp(id, t);
        try {
            op.waitForOperationCompletion(ms);
        } catch (final alice.tuplecentre.api.exceptions.OperationTimeOutException ex) {
            throw new OperationTimeOutException(op);
        }
        return this.unify(t, op.getLogicTupleResult());
    }

    public List<LogicTuple> set(final IId aid, final LogicTuple tuple,
            final long ms) throws OperationNotPossibleException,
            InvalidLogicTupleException, OperationTimeOutException {
        final IRespectOperation op = this.getCore().set(aid, tuple);
        try {
            op.waitForOperationCompletion(ms);
        } catch (final alice.tuplecentre.api.exceptions.OperationTimeOutException ex) {
            throw new OperationTimeOutException(op);
        }
        return op.getLogicTupleListResult();
    }

    public LogicTuple spawn(final IId aid, final LogicTuple t, final long ms)
            throws InvalidLogicTupleException, OperationNotPossibleException,
            OperationTimeOutException {
        if (t == null) {
            throw new InvalidLogicTupleException();
        }
        final IRespectOperation op = this.getCore().spawn(aid, t);
        try {
            op.waitForOperationCompletion(ms);
        } catch (final alice.tuplecentre.api.exceptions.OperationTimeOutException ex) {
            throw new OperationTimeOutException(op);
        }
        return t;
    }

    public LogicTuple uin(final IId id, final LogicTuple t, final long ms)
            throws InvalidLogicTupleException, OperationNotPossibleException,
            OperationTimeOutException {
        if (t == null) {
            throw new InvalidLogicTupleException();
        }
        final IRespectOperation op = this.getCore().uin(id, t);
        try {
            op.waitForOperationCompletion(ms);
        } catch (final alice.tuplecentre.api.exceptions.OperationTimeOutException ex) {
            throw new OperationTimeOutException(op);
        }
        return this.unify(t, op.getLogicTupleResult());
    }

    public LogicTuple uinp(final IId id, final LogicTuple t, final long ms)
            throws InvalidLogicTupleException, OperationNotPossibleException,
            OperationTimeOutException {
        if (t == null) {
            throw new InvalidLogicTupleException();
        }
        final IRespectOperation op = this.getCore().uinp(id, t);
        try {
            op.waitForOperationCompletion(ms);
        } catch (final alice.tuplecentre.api.exceptions.OperationTimeOutException ex) {
            throw new OperationTimeOutException(op);
        }
        final LogicTuple result = op.getLogicTupleResult();
        return this.unify(t, result);
    }

    public LogicTuple uno(final IId id, final LogicTuple t, final long ms)
            throws InvalidLogicTupleException, OperationNotPossibleException,
            OperationTimeOutException {
        if (t == null) {
            throw new InvalidLogicTupleException();
        }
        final IRespectOperation op = this.getCore().uno(id, t);
        try {
            op.waitForOperationCompletion(ms);
        } catch (final alice.tuplecentre.api.exceptions.OperationTimeOutException ex) {
            throw new OperationTimeOutException(op);
        }
        return this.unify(t, op.getLogicTupleResult());
    }

    public LogicTuple unop(final IId id, final LogicTuple t, final long ms)
            throws InvalidLogicTupleException, OperationNotPossibleException,
            OperationTimeOutException {
        if (t == null) {
            throw new InvalidLogicTupleException();
        }
        final IRespectOperation op = this.getCore().unop(id, t);
        try {
            op.waitForOperationCompletion(ms);
        } catch (final alice.tuplecentre.api.exceptions.OperationTimeOutException ex) {
            throw new OperationTimeOutException(op);
        }
        final LogicTuple result = op.getLogicTupleResult();
        return this.unify(t, result);
    }

    public LogicTuple urd(final IId id, final LogicTuple t, final long ms)
            throws InvalidLogicTupleException, OperationNotPossibleException,
            OperationTimeOutException {
        if (t == null) {
            throw new InvalidLogicTupleException();
        }
        final IRespectOperation op = this.getCore().urd(id, t);
        try {
            op.waitForOperationCompletion(ms);
        } catch (final alice.tuplecentre.api.exceptions.OperationTimeOutException ex) {
            throw new OperationTimeOutException(op);
        }
        return this.unify(t, op.getLogicTupleResult());
    }

    public LogicTuple urdp(final IId id, final LogicTuple t, final long ms)
            throws InvalidLogicTupleException, OperationNotPossibleException,
            OperationTimeOutException {
        if (t == null) {
            throw new InvalidLogicTupleException();
        }
        final IRespectOperation op = this.getCore().urdp(id, t);
        try {
            op.waitForOperationCompletion(ms);
        } catch (final alice.tuplecentre.api.exceptions.OperationTimeOutException ex) {
            throw new OperationTimeOutException(op);
        }
        final LogicTuple result = op.getLogicTupleResult();
        return this.unify(t, result);
    }

}
