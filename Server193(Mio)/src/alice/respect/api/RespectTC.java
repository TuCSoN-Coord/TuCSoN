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
package alice.respect.api;
import java.util.List;

import alice.logictuple.*;
import alice.respect.core.*;
import alice.tuplecentre.api.IId;
import alice.tuplecentre.core.OperationCompletionListener;
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
	// Creare il riferimento al container respect
	
	// Qui devo passare il riferimento all'Ontologia al tc
	public RespectTC(TupleCentreId tid,RespectTCContainer container, int qSize){
		// Qui devo passare un riferimento a se stesso
		vm=new RespectVM(tid,container,qSize);
		vm.setSpy(true);
		vmThread=new Thread(vm);
		vmThread.start();
		// Inizializzare il riferimento relativo al container respect
	}
	
	// Metodo per ottenere l'ontologia relativa al tc
	
	// Metodo per settare l'ontologia relativa al tc
	
	public IRespectOperation out(IId id, LogicTuple t, OperationCompletionListener l) throws OperationNotPossibleException {
		RespectOperation op=RespectOperation.makeOut(getProlog(), t,l);
		vm.doOperation(id,op);
		return op;
	}

	public IRespectOperation in(IId id, LogicTuple t, OperationCompletionListener l) throws OperationNotPossibleException {
		RespectOperation op=RespectOperation.makeIn(getProlog(), t,l);
		vm.doOperation(id,op);
		return op;
	}

	public IRespectOperation rd(IId id, LogicTuple t, OperationCompletionListener l) throws OperationNotPossibleException {
		RespectOperation op=RespectOperation.makeRd(getProlog(), t,l);
		vm.doOperation(id,op);
		return op;
	}

	public IRespectOperation inp(IId id, LogicTuple t, OperationCompletionListener l) throws OperationNotPossibleException {
		RespectOperation op=RespectOperation.makeInp(getProlog(), t,l);
		vm.doOperation(id,op);
		return op;
	}

	public IRespectOperation rdp(IId id, LogicTuple t, OperationCompletionListener l) throws OperationNotPossibleException {
		RespectOperation op=RespectOperation.makeRdp(getProlog(), t,l);
		vm.doOperation(id,op);
		return op;
	}

	public IRespectOperation out_s(IId id, LogicTuple t, OperationCompletionListener l) throws OperationNotPossibleException {
		RespectOperation op=RespectOperation.makeOut_s(getProlog(), t, l);
		vm.doOperation(id,op);
		return op;
	}
	
	public IRespectOperation in_s(IId id, LogicTuple t, OperationCompletionListener l) throws OperationNotPossibleException {
		RespectOperation op=RespectOperation.makeIn_s(getProlog(), t,l);
		vm.doOperation(id,op);
		return op;
	}
	
	public IRespectOperation rd_s(IId id, LogicTuple t, OperationCompletionListener l) throws OperationNotPossibleException {
		RespectOperation op=RespectOperation.makeRd_s(getProlog(), t,l);
		vm.doOperation(id,op);
		return op;
	}
	
	public IRespectOperation inp_s(IId id, LogicTuple t, OperationCompletionListener l) throws OperationNotPossibleException {
		RespectOperation op=RespectOperation.makeInp_s(getProlog(), t,l);
		vm.doOperation(id,op);
		return op;
	}
	
	public IRespectOperation rdp_s(IId id, LogicTuple t, OperationCompletionListener l) throws OperationNotPossibleException {
		RespectOperation op=RespectOperation.makeRdp_s(getProlog(), t,l);
		vm.doOperation(id,op);
		return op;
	}
	
	/*public IRespectOperation set(IId id, LogicTuple[] t) throws OperationNotPossibleException {
		
		return this.set(id, t,null);
	}*/

	public IRespectOperation out(IId id, LogicTuple t) throws OperationNotPossibleException {
		
		return this.out(id, t,null);
	}

	public IRespectOperation get(IId id) throws OperationNotPossibleException {
		
		return this.get(id,null);
	}
	
	public IRespectOperation set(IId id, List<LogicTuple> tupleList) throws OperationNotPossibleException {
		
		return this.set(id, tupleList, null);
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
	
	public void setSpec(IId id, RespectSpecification spec) throws InvalidSpecificationException {
		boolean accepted = vm.setReactionSpec(spec);
		if (!accepted){
			throw new InvalidSpecificationException();
		}
	}
	
	public RespectSpecification getSpec(IId id) {
		return (RespectSpecification)vm.getReactionSpec();
	}
	
	public IRespectOperation get(IId id, OperationCompletionListener l) throws OperationNotPossibleException {
		RespectOperation op=RespectOperation.makeGet(getProlog(), new LogicTuple("get"),l);
		vm.doOperation(id,op);
		return op;
	}
	
	public IRespectOperation set(IId id, List<LogicTuple> tupleList, OperationCompletionListener l) throws OperationNotPossibleException {
		RespectOperation op = RespectOperation.makeSet(getProlog(), tupleList, l);
		vm.doOperation(id,op);
		return op;
	}
	
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
	public IBlockingContext getBlockingContext(){
		return new BlockingContext(this);
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
	public INonBlockingContext getNonBlockingContext(){
		return new NonBlockingContext(this);
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
	public IBlockingSpecContext getBlockingSpecContext() {
		return new BlockingSpecContext(this);
	}
	
	public INonBlockingSpecContext getNonBlockingSpecContext() {
		return new NonBlockingSpecContext(this);
	}

	public RespectVM getRespectVM() {
		return vm;
	}
	
	public IEnvironmentContext getEnvironmentContext(){
	    return new EnviromentContext(vm.getRespectVMContext());
	}

	@Override
	public RespectVM getVM() {
		return vm;
	}
	
	private Prolog getProlog(){
		return vm.getRespectVMContext().getPrologCore();
	}
		
}