/*
 * ReSpecT - Copyright (C) aliCE team at deis.unibo.it
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package alice.respect.core;

import java.util.Iterator;
import java.util.LinkedList;

import alice.logictuple.*;
import alice.logictuple.exceptions.InvalidLogicTupleException;
import alice.respect.api.IEnvironmentContext;
import alice.respect.api.ILinkContext;
import alice.respect.api.IManagementContext;
import alice.respect.api.IOrdinaryAsynchInterface;
import alice.respect.api.IOrdinarySynchInterface;
import alice.respect.api.IRespectOperation;
import alice.respect.api.IRespectTC;
import alice.respect.api.ISpecificationAsynchInterface;
import alice.respect.api.ISpecificationSynchInterface;
import alice.respect.api.ITimedContext;
import alice.respect.api.RespectSpecification;
import alice.respect.api.TupleCentreId;
import alice.respect.api.exceptions.InvalidSpecificationException;
import alice.respect.api.exceptions.OperationNotPossibleException;
import alice.tuplecentre.api.IId;
import alice.tuplecentre.api.Tuple;
import alice.tuplecentre.core.OperationCompletionListener;
import alice.tuplecentre.core.TCCycleResult.Outcome;
import alice.tuprolog.Prolog;

/**
 * 
 * A ReSpecT tuple centre.
 * 
 * @author aricci
 *
 */
public class RespectTC implements IRespectTC {
	
	private RespectVM vm;
	private Thread vmThread;
	
	public RespectTC(TupleCentreId tid,RespectTCContainer container, int qSize){
		vm = new RespectVM(tid, container, qSize, this);
		vmThread=new Thread(vm);
		vmThread.start();
	}
	
	@Override
	public IRespectOperation spawn(IId id, LogicTuple t,
			OperationCompletionListener l) throws OperationNotPossibleException {
		RespectOperation op = RespectOperation.makeSpawn(getProlog(), t, l);
		vm.doOperation(id, op);
		return op;
	}

	@Override
	public IRespectOperation spawn(IId id, LogicTuple t)
			throws OperationNotPossibleException {
		return this.spawn(id, t, null);
	}
	
	/**
	 * ORDINARY primitives ASYNCH semantics
	 */
	
	public IRespectOperation out(IId id, LogicTuple t, OperationCompletionListener l) throws OperationNotPossibleException {
		RespectOperation op = RespectOperation.makeOut(getProlog(), t, l);
		vm.doOperation(id, op);
		return op;
	}

	public IRespectOperation in(IId id, LogicTuple t, OperationCompletionListener l) throws OperationNotPossibleException {
		RespectOperation op = RespectOperation.makeIn(getProlog(),t,l);
		vm.doOperation(id,op);
		return op;
	}

	public IRespectOperation rd(IId id, LogicTuple t, OperationCompletionListener l) throws OperationNotPossibleException {
		RespectOperation op = RespectOperation.makeRd(getProlog(),t,l);
		vm.doOperation(id,op);
		return op;
	}

	public IRespectOperation inp(IId id, LogicTuple t, OperationCompletionListener l) throws OperationNotPossibleException {
		RespectOperation op = RespectOperation.makeInp(getProlog(),t, l);
		vm.doOperation(id, op);
		return op;
	}

	public IRespectOperation rdp(IId id, LogicTuple t, OperationCompletionListener l) throws OperationNotPossibleException {
		RespectOperation op = RespectOperation.makeRdp(getProlog(),t, l);
		vm.doOperation(id, op);
		return op;
	}
	
	public IRespectOperation no(IId id, LogicTuple t, OperationCompletionListener l) throws OperationNotPossibleException {
		RespectOperation op = RespectOperation.makeNo(getProlog(),t, l);
		vm.doOperation(id, op);
		return op;
	}
	
	public IRespectOperation nop(IId id, LogicTuple t, OperationCompletionListener l) throws OperationNotPossibleException {
		RespectOperation op = RespectOperation.makeNop(getProlog(),t, l);
		vm.doOperation(id, op);
		return op;
	}
	
	public IRespectOperation get(IId id, OperationCompletionListener l) throws OperationNotPossibleException {
		RespectOperation op = RespectOperation.makeGet(getProlog(),new LogicTuple("get"),l);
		vm.doOperation(id,op);
		return op;
	}
	
	public IRespectOperation set(IId id, LogicTuple tuple, OperationCompletionListener l) throws OperationNotPossibleException, InvalidLogicTupleException {
		RespectOperation op = RespectOperation.makeSet(getProlog(),tuple, l);
		vm.doOperation(id,op);
		return op;
	}
	
	public IRespectOperation out_all(IId id, LogicTuple t, OperationCompletionListener l) throws OperationNotPossibleException {
		RespectOperation op = RespectOperation.makeOutAll(getProlog(),t,l);
		vm.doOperation(id,op);
		return op;
	}
	
	public IRespectOperation in_all(IId id, LogicTuple t, OperationCompletionListener l) throws OperationNotPossibleException {
		RespectOperation op = RespectOperation.makeInAll(getProlog(),t,l);
		vm.doOperation(id,op);
		return op;
	}
	
	public IRespectOperation rd_all(IId id, LogicTuple t, OperationCompletionListener l) throws OperationNotPossibleException {
		RespectOperation op = RespectOperation.makeRdAll(getProlog(),t,l);
		vm.doOperation(id,op);
		return op;
	}
	
	public IRespectOperation no_all(IId id, LogicTuple t, OperationCompletionListener l) throws OperationNotPossibleException {
		RespectOperation op = RespectOperation.makeNoAll(getProlog(),t,l);
		vm.doOperation(id,op);
		return op;
	}
	
	public IRespectOperation urd(IId id, LogicTuple t, OperationCompletionListener l) throws OperationNotPossibleException {
		RespectOperation op = RespectOperation.makeUrd(getProlog(),t,l);
		vm.doOperation(id,op);
		return op;
	}
	
	public IRespectOperation uin(IId id, LogicTuple t, OperationCompletionListener l) throws OperationNotPossibleException {
		RespectOperation op = RespectOperation.makeUin(getProlog(),t,l);
		vm.doOperation(id,op);
		return op;
	}
	
	public IRespectOperation uno(IId id, LogicTuple t, OperationCompletionListener l) throws OperationNotPossibleException {
		RespectOperation op = RespectOperation.makeUno(getProlog(),t,l);
		vm.doOperation(id,op);
		return op;
	}
	
	public IRespectOperation urdp(IId id, LogicTuple t, OperationCompletionListener l) throws OperationNotPossibleException {
		RespectOperation op = RespectOperation.makeUrdp(getProlog(),t,l);
		vm.doOperation(id,op);
		return op;
	}
	
	public IRespectOperation uinp(IId id, LogicTuple t, OperationCompletionListener l) throws OperationNotPossibleException {
		RespectOperation op = RespectOperation.makeUinp(getProlog(),t,l);
		vm.doOperation(id,op);
		return op;
	}
	
	public IRespectOperation unop(IId id, LogicTuple t, OperationCompletionListener l) throws OperationNotPossibleException {
		RespectOperation op = RespectOperation.makeUnop(getProlog(),t,l);
		vm.doOperation(id,op);
		return op;
	}
	
	/**
	 * SPECIFICATION primitives ASYNCH semantics
	 */

	public IRespectOperation out_s(IId id, LogicTuple t, OperationCompletionListener l) throws OperationNotPossibleException {
		RespectOperation op = RespectOperation.makeOut_s(getProlog(),t, l);
		vm.doOperation(id,op);
		return op;
	}
	
	public IRespectOperation in_s(IId id, LogicTuple t, OperationCompletionListener l) throws OperationNotPossibleException {
		RespectOperation op = RespectOperation.makeIn_s(getProlog(),t,l);
		vm.doOperation(id,op);
		return op;
	}
	
	public IRespectOperation rd_s(IId id, LogicTuple t, OperationCompletionListener l) throws OperationNotPossibleException {
		RespectOperation op = RespectOperation.makeRd_s(getProlog(),t,l);
		vm.doOperation(id,op);
		return op;
	}
	
	public IRespectOperation inp_s(IId id, LogicTuple t, OperationCompletionListener l) throws OperationNotPossibleException {
		RespectOperation op = RespectOperation.makeInp_s(getProlog(),t,l);
		vm.doOperation(id,op);
		return op;
	}
	
	public IRespectOperation rdp_s(IId id, LogicTuple t, OperationCompletionListener l) throws OperationNotPossibleException {
		RespectOperation op = RespectOperation.makeRdp_s(getProlog(),t,l);
		vm.doOperation(id,op);
		return op;
	}
	
	public IRespectOperation no_s(IId id, LogicTuple t, OperationCompletionListener l) throws OperationNotPossibleException {
		RespectOperation op = RespectOperation.makeNo_s(getProlog(),t,l);
		vm.doOperation(id,op);
		return op;
	}
	
	public IRespectOperation nop_s(IId id, LogicTuple t, OperationCompletionListener l) throws OperationNotPossibleException {
		RespectOperation op = RespectOperation.makeNop_s(getProlog(),t,l);
		vm.doOperation(id,op);
		return op;
	}
	
	public IRespectOperation set_s(IId aid, RespectSpecification spec, OperationCompletionListener l) throws OperationNotPossibleException {
		RespectOperation op = RespectOperation.makeSet_s(getProlog(), spec, l);
		vm.doOperation(aid,op);
		return op;
	}
	
	public IRespectOperation set_s(IId aid, LogicTuple t, OperationCompletionListener l) throws OperationNotPossibleException {
		RespectOperation op = RespectOperation.makeSet_s(getProlog(), t, l);
		vm.doOperation(aid,op);
		return op;
	}

	public IRespectOperation get_s(IId aid, OperationCompletionListener l) throws OperationNotPossibleException {
		RespectOperation op = RespectOperation.makeGet_s(getProlog(),new LogicTuple("spec", new Var("S")), l);
		vm.doOperation(aid,op);
		return op;
	}
	
	/**
	 * ORDINARY primitives SYNCH semantics
	 */

	public IRespectOperation out(IId id, LogicTuple t) throws OperationNotPossibleException {
		return this.out(id, t,null);
	}
	
	public IRespectOperation in(IId id, LogicTuple t) throws OperationNotPossibleException {
		return this.in(id, t,null);
	}

	public IRespectOperation rd(IId id, LogicTuple t) throws OperationNotPossibleException {
		return this.rd(id, t,null);
	}

	public IRespectOperation inp(IId id, LogicTuple t) throws OperationNotPossibleException {
		return this.inp(id, t,null);
	}

	public IRespectOperation rdp(IId id, LogicTuple t) throws OperationNotPossibleException {
		return this.rdp(id, t,null);
	}
	
	public IRespectOperation no(IId id, LogicTuple t) throws OperationNotPossibleException {
		return this.no(id, t,null);
	}
	
	public IRespectOperation nop(IId id, LogicTuple t) throws OperationNotPossibleException {
		return this.nop(id, t,null);
	}
	
	public IRespectOperation get(IId id) throws OperationNotPossibleException {
		return this.get(id,null);
	}
	
	public IRespectOperation set(IId id, LogicTuple tuple) throws OperationNotPossibleException, InvalidLogicTupleException {
		return this.set(id, tuple, null);
	}
	
	public IRespectOperation out_all(IId id, LogicTuple t) throws OperationNotPossibleException {
		return this.out_all(id, t,null);
	}
	
	public IRespectOperation in_all(IId id, LogicTuple t) throws OperationNotPossibleException {
		return this.in_all(id, t,null);
	}
	
	public IRespectOperation rd_all(IId id, LogicTuple t) throws OperationNotPossibleException {
		return this.rd_all(id, t,null);
	}
	
	public IRespectOperation no_all(IId id, LogicTuple t) throws OperationNotPossibleException {
		return this.no_all(id, t,null);
	}
	
	public IRespectOperation urd(IId id, LogicTuple t) throws OperationNotPossibleException {
		return this.urd(id, t,null);
	}
	
	public IRespectOperation uin(IId id, LogicTuple t) throws OperationNotPossibleException {
		return this.uin(id, t,null);
	}
	
	public IRespectOperation uno(IId id, LogicTuple t) throws OperationNotPossibleException {
		return this.uno(id, t,null);
	}
	
	public IRespectOperation urdp(IId id, LogicTuple t) throws OperationNotPossibleException {
		return this.urdp(id, t,null);
	}
	
	public IRespectOperation uinp(IId id, LogicTuple t) throws OperationNotPossibleException {
		return this.uinp(id, t,null);
	}
	
	public IRespectOperation unop(IId id, LogicTuple t) throws OperationNotPossibleException {
		return this.unop(id, t,null);
	}
	
	/**
	 * SPECIFICATION primitives SYNCH semantics
	 */
	
	public IRespectOperation out_s(IId id, LogicTuple t) throws OperationNotPossibleException {
		return this.out_s(id, t, null);
	}
	
	public IRespectOperation in_s(IId id, LogicTuple t) throws OperationNotPossibleException {
		return this.in_s(id, t,null);
	}
	
	public IRespectOperation rd_s(IId id, LogicTuple t) throws OperationNotPossibleException {
		return this.rd_s(id, t,null);
	}
	
	public IRespectOperation inp_s(IId id, LogicTuple t) throws OperationNotPossibleException {
		return this.inp_s(id, t,null);
	}

	public IRespectOperation rdp_s(IId id, LogicTuple t) throws OperationNotPossibleException {
		return this.rdp_s(id, t,null);
	}
	
	public IRespectOperation no_s(IId id, LogicTuple t) throws OperationNotPossibleException{
		return this.no_s(id, t, null);
	}
	
	public IRespectOperation nop_s(IId id, LogicTuple t) throws OperationNotPossibleException{
		return this.nop_s(id, t, null);
	}
	
	public IRespectOperation get_s(IId aid) throws OperationNotPossibleException {
		return this.get_s(aid, null);
	}
	
	public IRespectOperation set_s(IId aid, RespectSpecification spec) throws OperationNotPossibleException, InvalidSpecificationException {
		boolean accepted = vm.setReactionSpec(spec);
		if (!accepted){
			throw new InvalidSpecificationException();
		}else{
			RespectOperation op = RespectOperation.makeSet_s(getProlog(), null);
			Iterator<Tuple> rit = vm.getRespectVMContext().getSpecTupleSetIterator();
			LinkedList<Tuple> reactionList = new LinkedList<Tuple>();
			while(rit.hasNext())
				reactionList.add(rit.next());
			op.setOpResult(Outcome.SUCCESS);
			op.setTupleListResult(reactionList);
			return op;
		}
	}
	
	public IRespectOperation set_s(IId aid, LogicTuple t)
			throws OperationNotPossibleException {
		return this.set_s(aid, t, null);
	}
	
	

	/**
	 * Gets the tuple centre id
	 * 
	 * @return the id
	 */
	public TupleCentreId getId(){
		return vm.getId();
	}
	
	/**
	 * Gets a context for tuple centre management.
	 * 
	 * @return
	 */
	public IManagementContext getManagementContext(){
		return new ManagementContext(vm,vmThread);
	}
	
	/**
	 * Gets a context with blocking functionalities
	 * 
	 * @return
	 */
	public IOrdinarySynchInterface getOrdinarySynchInterface(){
		return new OrdinarySynchInterface(this);
	}
	
	/**
	 * Gets a interface for linking operations
	 * 
	 * @return
	 */
	public ILinkContext getLinkContext(){
		return new LinkContext(vm);
	}

	/**
	 * Gets a context with no blocking functionalities
	 * 
	 * @return
	 */
	public IOrdinaryAsynchInterface getOrdinaryAsynchInterface(){
		return new OrdinaryAsynchInterface(this);
	}
	
	/**
	 * Gets a context with timing functionalities.
	 * 
	 * @return
	 */
	public ITimedContext getTimedContext(){
		return new TimedContext(this);
	}

	/**
	 * Gets a context with blocking specification functionalities 
	 * 
	 * @return
	 */
	public ISpecificationSynchInterface getSpecificationSynchInterface() {
		return new SpecificationSynchInterface(this);
	}
	
	public ISpecificationAsynchInterface getSpecificationAsynchInterface() {
		return new SpecificationAsynchInterface(this);
	}

	public Thread getVMThread() {
		return vmThread;
	}
	
	public IEnvironmentContext getEnvironmentContext(){
	    return new EnviromentContext(vm.getRespectVMContext());
	}
	
	public RespectVM getVM(){
		return vm;
	}
	
	private Prolog getProlog(){
		return vm.getRespectVMContext().getPrologCore();
	}
	
}