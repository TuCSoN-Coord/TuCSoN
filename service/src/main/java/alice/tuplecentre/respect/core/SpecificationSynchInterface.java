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

import java.util.LinkedList;
import java.util.List;

import alice.tuple.logic.LogicTuple;
import alice.tuple.logic.exceptions.InvalidLogicTupleException;
import alice.tuplecentre.core.AbstractTupleCentreOperation;
import alice.tuplecentre.core.InputEvent;
import alice.tuplecentre.respect.api.IRespectTC;
import alice.tuplecentre.respect.api.ISpecificationSynchInterface;
import alice.tuplecentre.respect.api.RespectOperation;
import alice.tuplecentre.respect.api.RespectSpecification;
import alice.tuplecentre.respect.api.exceptions.InvalidSpecificationException;
import alice.tuplecentre.respect.api.exceptions.OperationNotPossibleException;

/**
 * A Blocking Context wraps the access to a tuple centre virtual machine for a
 * specific thread copyOf control, providing a blocking interface.
 *
 * @author Alessandro Ricci
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 * @author (contributor) Michele Bombardi (mailto:
 * michele.bombardi@studio.unibo.it)
 */
public class SpecificationSynchInterface extends RootInterface implements
        ISpecificationSynchInterface {
    /**
     * @param core the ReSpecT tuple centres manager this interface refers to
     */
    public SpecificationSynchInterface(final IRespectTC core) {
        super(core);
    }

    @Override
    public List<LogicTuple> getS(final InputEvent ev)
            throws OperationNotPossibleException {
        final RespectOperation op = this.getCore().getS(ev);
        op.waitForOperationCompletion();
        return op.getLogicTupleListResult();
    }

    @Override
    public LogicTuple inpS(final InputEvent ev)
            throws InvalidLogicTupleException, OperationNotPossibleException {
        final AbstractTupleCentreOperation inOp = ev.getSimpleTCEvent();
        final LogicTuple t = (LogicTuple) inOp.getTemplateArgument();
        if (t == null) {
            throw new InvalidLogicTupleException();
        }
        final RespectOperation op = this.getCore().inpS(ev);
        op.waitForOperationCompletion();
        return this.unify(t, op.getLogicTupleResult());
    }

    @Override
    public LogicTuple inS(final InputEvent ev)
            throws InvalidLogicTupleException, OperationNotPossibleException {
        final AbstractTupleCentreOperation inOp = ev.getSimpleTCEvent();
        final LogicTuple t = (LogicTuple) inOp.getTemplateArgument();
        if (t == null) {
            throw new InvalidLogicTupleException();
        }
        final RespectOperation op = this.getCore().inS(ev);
        op.waitForOperationCompletion();
        return this.unify(t, op.getLogicTupleResult());
    }

    @Override
    public LogicTuple nopS(final InputEvent ev)
            throws InvalidLogicTupleException, OperationNotPossibleException {
        final AbstractTupleCentreOperation inOp = ev.getSimpleTCEvent();
        final LogicTuple t = (LogicTuple) inOp.getTemplateArgument();
        if (t == null) {
            throw new InvalidLogicTupleException();
        }
        final RespectOperation op = this.getCore().nopS(ev);
        op.waitForOperationCompletion();
        return this.unify(t, op.getLogicTupleResult());
    }

    @Override
    public LogicTuple noS(final InputEvent ev)
            throws InvalidLogicTupleException, OperationNotPossibleException {
        final AbstractTupleCentreOperation inOp = ev.getSimpleTCEvent();
        final LogicTuple t = (LogicTuple) inOp.getTemplateArgument();
        if (t == null) {
            throw new InvalidLogicTupleException();
        }
        final RespectOperation op = this.getCore().noS(ev);
        op.waitForOperationCompletion();
        return this.unify(t, op.getLogicTupleResult());
    }

    @Override
    public void outS(final InputEvent ev) throws InvalidLogicTupleException,
            OperationNotPossibleException {
        final LogicTuple t = (LogicTuple) ev.getTuple();
        if (t == null) {
            throw new InvalidLogicTupleException();
        }
        final RespectOperation op = this.getCore().outS(ev);
        op.waitForOperationCompletion();
    }

    @Override
    public LogicTuple rdpS(final InputEvent ev)
            throws InvalidLogicTupleException, OperationNotPossibleException {
        final AbstractTupleCentreOperation inOp = ev.getSimpleTCEvent();
        final LogicTuple t = (LogicTuple) inOp.getTemplateArgument();
        if (t == null) {
            throw new InvalidLogicTupleException();
        }
        final RespectOperation op = this.getCore().rdpS(ev);
        op.waitForOperationCompletion();
        return this.unify(t, op.getLogicTupleResult());
    }

    @Override
    public LogicTuple rdS(final InputEvent ev)
            throws InvalidLogicTupleException, OperationNotPossibleException {
        final AbstractTupleCentreOperation inOp = ev.getSimpleTCEvent();
        final LogicTuple t = (LogicTuple) inOp.getTemplateArgument();
        if (t == null) {
            throw new InvalidLogicTupleException();
        }
        final RespectOperation op = this.getCore().rdS(ev);
        op.waitForOperationCompletion();
        return this.unify(t, op.getLogicTupleResult());
    }

    @Override
    public List<LogicTuple> setS(final LogicTuple t, final InputEvent ev)
            throws OperationNotPossibleException {
        final RespectOperation op = this.getCore().setS(t, ev);
        op.waitForOperationCompletion();
        return op.getLogicTupleListResult();
    }

    @Override
    public List<LogicTuple> setS(final RespectSpecification spec,
                                 final InputEvent ev) throws
            InvalidSpecificationException {
        final RespectOperation op = this.getCore().setSsynch(ev, spec);
        if ("'$TucsonNodeService-Agent'".equals(ev.getSource().toString())
                || ev.getSource().toString().startsWith("'$Inspector-'")) {
            return new LinkedList<>();
        }
        return op.getLogicTupleListResult();
    }
}
