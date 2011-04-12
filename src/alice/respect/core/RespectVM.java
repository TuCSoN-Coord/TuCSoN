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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import alice.respect.api.OperationNotPossibleException;
import alice.respect.api.TupleCentreId;
import alice.tuplecentre.core.BehaviourSpecification;
import alice.tuplecentre.api.IId;
import alice.tuplecentre.api.InspectableEventListener;
import alice.tuplecentre.api.ObservableEventListener;
import alice.tuplecentre.api.Tuple;
import alice.tuplecentre.api.TupleTemplate;
import alice.tuplecentre.core.*;
import alice.logictuple.*;


/**
 * RespecT Tuple Centre Virtual Machine.
 * 
 * Defines the core behaviour of a tuple centre virtual machine.
 *
 * The behaviour reflects the operational semantic
 * expressed in related tuple centre articles.
 *
 * @author aricci
 * @version 1.0
 */
public class RespectVM implements Runnable {
    
    private RespectVMContext context;
	private RespectTCContainer container;
    
	// semaphores
	private Object news;
	private Object idle;
	private TransducerManager tm;
	
	protected List<ObservableEventListener> observers;
	
	/** listener to VM inspectable events  */
    protected List<InspectableEventListener> inspectors;
    
    // Qui devo passare un riferimento all'oggetto di tipo RespectTC che lo ha creato
    public RespectVM(TupleCentreId tid, RespectTCContainer container, int qSize){
    	this.container = container;
    	// Qui devo passare anche il riferimento all'oggetto di tipo RespectTC che lo ha creato 
        context = new RespectVMContext(this,tid, qSize);
		news = new Object();
		idle = new Object();
		inspectors = new ArrayList<InspectableEventListener>();
		observers = new ArrayList<ObservableEventListener>();
    }
    
    public RespectVM(TupleCentreId tid, int qSize, TransducerManager tm){
        context = new RespectVMContext(this,tid, qSize,tm);
		news = new Object();
		idle = new Object();
		this.tm=tm;
		System.out.println("************************************************ TRANSDUCERMANAGER "+ this.tm.toString());
    }

/**

	 * Registers a new observer to observable events

	 *

	 * @param l the listener to be notified

	 */

	public void addObserver(ObservableEventListener l){

		observers.add(l);

	}
	
	/**

	 * Removes (if present) a listener to observable events

	 *

	 * @param l the listener to be removed

	 */

	public void removeObserver(ObservableEventListener l){

		observers.remove(l);

	}

	public boolean hasObservers(){

		return observers.size()>0;

	}

	public boolean hasInspectors(){

		return this.inspectors.size()>0;

	}
	
	public List<ObservableEventListener> getObservers(){

		return observers;

	}
	
	 /**

     * Registers a new listener to observable events

     *

     * @param l the listener to be notified

     */

    public void addInspector(InspectableEventListener l){

        inspectors.add(l);

    }

    /**

     * Removes (if present) a listener to observable events

     *

     * @param l the listener to be removed

     */
    public void removeInspector(InspectableEventListener l){

        inspectors.remove(l);

    }

    
	public void doOperation(IId id, RespectOperation op) throws OperationNotPossibleException {
		try {
			context.doOperation(id,op);
			synchronized (news){
				news.notifyAll();
			}
		} catch (Exception ex){
			//ex.printStackTrace();
			throw new OperationNotPossibleException();
		}
	}
		
	public void notifyNewInputEvent(){
		synchronized (news){
			news.notifyAll();
		}
	}
	
	public boolean abortOperation(long opId) {
		try {
			boolean res;
			synchronized (idle){
				res = context.removePendingQueryEvent(opId);
			}
			return res;
		} catch (Exception ex){
			//ex.printStackTrace();
			return false;
		}
	}

		
    public void run(){
        
        while (true) {            
        	try {
        		synchronized(idle){
            		// execute a basic VM (atomic) step
            		context.execute();
            		// wait for new stimuli
        		}
			} catch (Exception ex){
				context.notifyException(ex);
			}
            try {
            	if (hasInspectors())
            		notifyInspectableEvent(new InspectableEvent(this,InspectableEvent.TYPE_NEWSTATE));
                context.spy("Waiting for news...");
                
                if(!(context.pendingEvents() || context.pendingEnvEvents())){
	                synchronized(news){
	                	news.wait();
	                }
                }
            } catch (Exception ex){
                context.notifyException(ex);
            }
        }
    }

    /**

     * Notifies registered listeners of a new observable event

     *

     * @param e the observable event

     */

    public void notifyInspectableEvent(InspectableEvent e){

        Iterator it = inspectors.iterator();

        while (it.hasNext()){

            try {

                ((InspectableEventListener)it.next()).onInspectableEvent(e);

            } catch (Exception ex){

                ex.printStackTrace();

                it.remove();
            }
        }
    }        
        
    public void notifyObservableEvent(Event ev){

    	int size = observers.size();
    	
    	InputEvent e = (InputEvent)ev;
    	
		if (ev.isInput()){
			
			if (e.getOperation().isIn()){

				for (int i=0; i<size; i++){

					observers.get(i).in_requested(this.getId(),ev.getSource(),((TupleTemplate)((RespectOperation)ev.getOperation()).getLogicTupleArgument()));

				}

			}else	if (e.getOperation().isInp()){

				for (int i=0; i<size; i++){

					observers.get(i).inp_requested(this.getId(),ev.getSource(),((TupleTemplate)((RespectOperation)ev.getOperation()).getLogicTupleArgument()));

				}

			} 
			else	if (e.getOperation().isRd()){

				for (int i=0; i<size; i++){

					observers.get(i).rd_requested(this.getId(),ev.getSource(),((TupleTemplate)((RespectOperation)ev.getOperation()).getLogicTupleArgument()));

				}

			}else	if (e.getOperation().isRdp()){

				for (int i=0; i<size; i++){

					observers.get(i).rdp_requested(this.getId(),ev.getSource(),((TupleTemplate)((RespectOperation)ev.getOperation()).getLogicTupleArgument()));

				}

			} else	if (e.getOperation().isOut()){

				for (int i=0; i<size; i++){

					observers.get(i).out_requested(this.getId(),ev.getSource(),((Tuple)((RespectOperation)ev.getOperation()).getLogicTupleArgument()));

				}

			} else	if (e.getOperation().isSetSpec()){

				for (int i=0; i<size; i++){

					observers.get(i).setSpec_requested(this.getId(),ev.getSource(),((Tuple)((RespectOperation)ev.getOperation()).getLogicTupleArgument()).toString());

				}

			} else	if (e.getOperation().isGetSpec()){

				for (int i=0; i<size; i++){

					observers.get(i).getSpec_requested(this.getId(),ev.getSource());

				}

			}

		} else {

			if (e.getOperation().isIn()){
				
				for (int i=0; i<size; i++){

					observers.get(i).in_completed(this.getId(),ev.getSource(),((Tuple)((RespectOperation)ev.getOperation()).getLogicTupleArgument()));

				}

			} else	if (e.getOperation().isInp()){

				for (int i=0; i<size; i++){

					observers.get(i).inp_completed(this.getId(),ev.getSource(),((Tuple)((RespectOperation)ev.getOperation()).getLogicTupleArgument()));

				}

			}else	if (e.getOperation().isRd()){

				for (int i=0; i<size; i++){

					observers.get(i).rd_completed(this.getId(),ev.getSource(),((Tuple)((RespectOperation)ev.getOperation()).getLogicTupleArgument()));

				}

			} else	if (e.getOperation().isRdp()){

				for (int i=0; i<size; i++){

					observers.get(i).rdp_completed(this.getId(),ev.getSource(),((Tuple)((RespectOperation)ev.getOperation()).getLogicTupleArgument()));

				}

			} else	if (e.getOperation().isSetSpec()){

				for (int i=0; i<size; i++){

					observers.get(i).setSpec_completed(this.getId(),ev.getSource());

				}

			} else	if (e.getOperation().isGetSpec()){

				for (int i=0; i<size; i++){

					observers.get(i).getSpec_completed(this.getId(),ev.getSource(),((Tuple)((RespectOperation)ev.getOperation()).getLogicTupleArgument()).toString());
				}

			}

		}

	}


    
	/*
	 * This operation can be executed only when the VM is an idle
	 * state, waiting for I/O
	 */
	
	public boolean setReactionSpec(BehaviourSpecification spec){
		synchronized(idle){
			// execute a basic VM (atomic) step
			context.removeReactionSpec();
			return context.setReactionSpec(spec);
		}
	}

	public BehaviourSpecification getReactionSpec(){
		synchronized(idle){
			// execute a basic VM (atomic) step
			return context.getReactionSpec();
		}
	}
	
	// 
	
	public void setManagementMode(boolean activate){
		context.setManagementMode(activate);
	}
	
	public void stopCommand() throws OperationNotPossibleException {
		try {
			context.stopCommand();
		} catch (Exception ex){
			throw new OperationNotPossibleException(); 
		}
	}

	public void goCommand()throws OperationNotPossibleException {
		try {
			context.goCommand();
			synchronized(news){
				news.notifyAll();
			}
		} catch (Exception ex){
			throw new OperationNotPossibleException(); 
		}
	}

	public void nextStepCommand()throws OperationNotPossibleException {
		try {
			context.nextStepCommand();
			synchronized(news){
				news.notifyAll();
			}
		} catch (Exception ex){
			throw new OperationNotPossibleException(); 
		}
	}
	
	public void setSpy(boolean spy){
		context.setSpy(spy);
	}

	//
	
	public LogicTuple[] getTSet(LogicTuple filter){
		return context.getTSet(filter);
	}

	public LogicTuple[] getWSet(LogicTuple filter){
		return context.getWSet(filter);
	}
	
	public void setWSet(List<LogicTuple> wSet)
	{
		context.setWSet(wSet);
	}

	public LogicTuple[] getTRSet(LogicTuple filter){
		return context.getTRSet(filter);
	}
	
	public void reset(){
		context.reset();
	}

	public TupleCentreId getId(){
		return (TupleCentreId) context.getId();
	}


	public RespectVMContext getRespectVMContext() {
		return context;
	}
	
	public RespectTCContainer getContainer() {
		return this.container;
	}
}



