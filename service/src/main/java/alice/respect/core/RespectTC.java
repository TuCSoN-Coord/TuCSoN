/*
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

import java.util.Iterator;
import java.util.LinkedList;

import alice.logictuple.LogicTuple;
import alice.respect.api.IEnvironmentContext;
import alice.respect.api.ILinkContext;
import alice.respect.api.IManagementContext;
import alice.respect.api.IOrdinaryAsynchInterface;
import alice.respect.api.IOrdinarySynchInterface;
import alice.respect.api.RespectOperation;
import alice.respect.api.IRespectTC;
import alice.respect.api.ISpatialContext;
import alice.respect.api.ISpecificationAsynchInterface;
import alice.respect.api.ISpecificationSynchInterface;
import alice.respect.api.ITimedContext;
import alice.respect.api.RespectSpecification;
import alice.respect.api.TupleCentreId;
import alice.respect.api.exceptions.InvalidSpecificationException;
import alice.respect.api.exceptions.OperationNotPossibleException;
import alice.tuplecentre.api.ITCCycleResult;
import alice.tuplecentre.api.Tuple;
import alice.tuplecentre.core.InputEvent;
import alice.tuprolog.Prolog;

/**
 *
 * A ReSpecT tuple centre.
 *
 * @author Alessandro Ricci
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 * @author (contributor) Michele Bombardi (mailto:
 *         michele.bombardi@studio.unibo.it)
 */
public class RespectTC implements IRespectTC {
    private final RespectVM vm;
    private final Thread vmThread;

    /**
     *
     * @param tid
     *            the identifier of the tuple centre
     * @param container
     *            the ReSpecT wrapper this tuple centre refers to
     * @param qSize
     *            the maximum size of the input queue
     */
    public RespectTC(final TupleCentreId tid,
            final RespectTCContainer container, final int qSize) {
        this.vm = new RespectVM(tid, container, qSize, this);
        this.vmThread = new Thread(this.vm);
        this.vmThread.start();
    }

    @Override
    public RespectOperation get(final InputEvent ev)
            throws OperationNotPossibleException {
        this.vm.doOperation(ev);
        return (RespectOperation) ev.getSimpleTCEvent();
    }

    /**
     *
     * @return the environment context toward this tuple centre
     */
    public IEnvironmentContext getEnvironmentContext() {
        return new EnviromentContext(this.vm.getRespectVMContext());
    }

    @Override
    public TupleCentreId getId() {
        return this.vm.getId();
    }

    /**
     * Gets a interface for linking operations
     *
     * @return the linking context toward this tuple centre
     */
    public ILinkContext getLinkContext() {
        return new LinkContext(this.vm);
    }

    /**
     * Gets a context for tuple centre management.
     *
     * @return the management context toward this tuple centre
     */
    public IManagementContext getManagementContext() {
        return new ManagementContext(this.vm);
    }

    /**
     * Gets a context with no blocking functionalities
     *
     * @return the ordinary, asynchronous context toward this tuple centre
     */
    public IOrdinaryAsynchInterface getOrdinaryAsynchInterface() {
        return new OrdinaryAsynchInterface(this);
    }

    /**
     * Gets a context with blocking functionalities
     *
     * @return the ordinary, synchronous context toward this tuple centre
     */
    public IOrdinarySynchInterface getOrdinarySynchInterface() {
        return new OrdinarySynchInterface(this);
    }

    /**
     *
     * @return the tuProlog engine behind this tuple centre
     */
    public Prolog getProlog() {
        return this.vm.getRespectVMContext().getPrologCore();
    }

    @Override
    public RespectOperation getS(final InputEvent ev)
            throws OperationNotPossibleException {
        this.vm.doOperation(ev);
        return (RespectOperation) ev.getSimpleTCEvent();
    }

    /**
     * Gets a context with spatial functionalities.
     *
     * @return the spatial context toward this tuple centre
     */
    public ISpatialContext getSpatialContext() {
        return new SpatialContext(this.vm.getRespectVMContext());
    }

    /**
     *
     * @return the specification, asynchronous context toward this tuple centre
     */
    public ISpecificationAsynchInterface getSpecificationAsynchInterface() {
        return new SpecificationAsynchInterface(this);
    }

    /**
     * Gets a context with blocking specification functionalities
     *
     * @return the specification, synchronous context toward this tuple centre
     */
    public ISpecificationSynchInterface getSpecificationSynchInterface() {
        return new SpecificationSynchInterface(this);
    }

    /**
     * Gets a context with timing functionalities.
     *
     * @return the timed context toward this tuple centre
     */
    public ITimedContext getTimedContext() {
        return new TimedContext(this);
    }

    @Override
    public RespectVM getVM() {
        return this.vm;
    }

    /**
     *
     * @return the Java thread executing the ReSpecT VM managing this tuple
     *         centre
     */
    public Thread getVMThread() {
        return this.vmThread;
    }

    @Override
    public RespectOperation in(final InputEvent ev)
            throws OperationNotPossibleException {
        this.vm.doOperation(ev);
        return (RespectOperation) ev.getSimpleTCEvent();
    }

    @Override
    public RespectOperation inAll(final InputEvent ev)
            throws OperationNotPossibleException {
        this.vm.doOperation(ev);
        return (RespectOperation) ev.getSimpleTCEvent();
    }

    @Override
    public RespectOperation inp(final InputEvent ev)
            throws OperationNotPossibleException {
        this.vm.doOperation(ev);
        return (RespectOperation) ev.getSimpleTCEvent();
    }

    @Override
    public RespectOperation inpS(final InputEvent ev)
            throws OperationNotPossibleException {
        this.vm.doOperation(ev);
        return (RespectOperation) ev.getSimpleTCEvent();
    }

    @Override
    public RespectOperation inS(final InputEvent ev)
            throws OperationNotPossibleException {
        this.vm.doOperation(ev);
        return (RespectOperation) ev.getSimpleTCEvent();
    }

    @Override
    public RespectOperation no(final InputEvent ev)
            throws OperationNotPossibleException {
        this.vm.doOperation(ev);
        return (RespectOperation) ev.getSimpleTCEvent();
    }

    @Override
    public RespectOperation noAll(final InputEvent ev)
            throws OperationNotPossibleException {
        this.vm.doOperation(ev);
        return (RespectOperation) ev.getSimpleTCEvent();
    }

    @Override
    public RespectOperation nop(final InputEvent ev)
            throws OperationNotPossibleException {
        this.vm.doOperation(ev);
        return (RespectOperation) ev.getSimpleTCEvent();
    }

    @Override
    public RespectOperation nopS(final InputEvent ev)
            throws OperationNotPossibleException {
        this.vm.doOperation(ev);
        return (RespectOperation) ev.getSimpleTCEvent();
    }

    @Override
    public RespectOperation noS(final InputEvent ev)
            throws OperationNotPossibleException {
        this.vm.doOperation(ev);
        return (RespectOperation) ev.getSimpleTCEvent();
    }

    @Override
    public RespectOperation out(final InputEvent ev)
            throws OperationNotPossibleException {
        this.vm.doOperation(ev);
        return (RespectOperation) ev.getSimpleTCEvent();
    }

    @Override
    public RespectOperation outAll(final InputEvent ev)
            throws OperationNotPossibleException {
        this.vm.doOperation(ev);
        return (RespectOperation) ev.getSimpleTCEvent();
    }

    @Override
    public RespectOperation outS(final InputEvent ev)
            throws OperationNotPossibleException {
        this.vm.doOperation(ev);
        return (RespectOperation) ev.getSimpleTCEvent();
    }

    @Override
    public RespectOperation rd(final InputEvent ev)
            throws OperationNotPossibleException {
        this.vm.doOperation(ev);
        return (RespectOperation) ev.getSimpleTCEvent();
    }

    @Override
    public RespectOperation rdAll(final InputEvent ev)
            throws OperationNotPossibleException {
        this.vm.doOperation(ev);
        return (RespectOperation) ev.getSimpleTCEvent();
    }

    @Override
    public RespectOperation rdp(final InputEvent ev)
            throws OperationNotPossibleException {
        this.vm.doOperation(ev);
        return (RespectOperation) ev.getSimpleTCEvent();
    }

    @Override
    public RespectOperation rdpS(final InputEvent ev)
            throws OperationNotPossibleException {
        this.vm.doOperation(ev);
        return (RespectOperation) ev.getSimpleTCEvent();
    }

    @Override
    public RespectOperation rdS(final InputEvent ev)
            throws OperationNotPossibleException {
        this.vm.doOperation(ev);
        return (RespectOperation) ev.getSimpleTCEvent();
    }

    @Override
    public RespectOperation set(final InputEvent ev)
            throws OperationNotPossibleException {
        this.vm.doOperation(ev);
        return (RespectOperation) ev.getSimpleTCEvent();
    }

    @Override
    public RespectOperation setS(final LogicTuple t, final InputEvent ev)
            throws OperationNotPossibleException {
        this.vm.doOperation(ev);
        return (RespectOperation) ev.getSimpleTCEvent();
    }

    @Override
    public RespectOperation setSasynch(final InputEvent ev,
                                       final RespectSpecification spec)
            throws OperationNotPossibleException {
        this.vm.doOperation(ev);
        return (RespectOperation) ev.getSimpleTCEvent();
    }

    @Override
    public RespectOperation setSsynch(final InputEvent ev,
                                      final RespectSpecification spec)
            throws InvalidSpecificationException {
        final RespectOperationDefault op = (RespectOperationDefault) ev.getSimpleTCEvent();
        final boolean accepted = this.vm.setReactionSpec(spec);
        if (!accepted) {
            throw new InvalidSpecificationException();
        }
        final Iterator<LogicTuple> rit = this.vm.getRespectVMContext()
                .getSpecTupleSetIterator();
        final LinkedList<Tuple> reactionList = new LinkedList<Tuple>();
        while (rit.hasNext()) {
            reactionList.add(rit.next());
        }
        op.setOpResult(ITCCycleResult.Outcome.SUCCESS);
        op.setTupleListResult(reactionList);
        return op;
    }

    @Override
    public RespectOperation spawn(final InputEvent ev)
            throws OperationNotPossibleException {
        this.vm.doOperation(ev);
        return (RespectOperation) ev.getSimpleTCEvent();
    }

    @Override
    public RespectOperation uin(final InputEvent ev)
            throws OperationNotPossibleException {
        this.vm.doOperation(ev);
        return (RespectOperation) ev.getSimpleTCEvent();
    }

    @Override
    public RespectOperation uinp(final InputEvent ev)
            throws OperationNotPossibleException {
        this.vm.doOperation(ev);
        return (RespectOperation) ev.getSimpleTCEvent();
    }

    @Override
    public RespectOperation uno(final InputEvent ev)
            throws OperationNotPossibleException {
        this.vm.doOperation(ev);
        return (RespectOperation) ev.getSimpleTCEvent();
    }

    @Override
    public RespectOperation unop(final InputEvent ev)
            throws OperationNotPossibleException {
        this.vm.doOperation(ev);
        return (RespectOperation) ev.getSimpleTCEvent();
    }

    @Override
    public RespectOperation urd(final InputEvent ev)
            throws OperationNotPossibleException {
        this.vm.doOperation(ev);
        return (RespectOperation) ev.getSimpleTCEvent();
    }

    @Override
    public RespectOperation urdp(final InputEvent ev)
            throws OperationNotPossibleException {
        this.vm.doOperation(ev);
        return (RespectOperation) ev.getSimpleTCEvent();
    }

//	@Override
//	public RespectOperation getEnv(InputEvent ev)
//			throws OperationNotPossibleException {
//		this.vm.doOperation(ev);
//        return (RespectOperation) ev.getSimpleTCEvent();
//	}
//
//	@Override
//	public RespectOperation setEnv(InputEvent ev)
//			throws OperationNotPossibleException {
//		this.vm.doOperation(ev);
//        return (RespectOperation) ev.getSimpleTCEvent();
//	}
}
