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

import alice.respect.api.AgentId;
import alice.respect.api.OperationNotPossibleException;
import alice.respect.api.TupleCentreId;
import alice.tuplecentre.api.BehaviourSpecification;
import alice.tuplecentre.api.IId;
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
	
	// semaphores
	private Object news;
	private Object idle;
    
    public RespectVM(TupleCentreId tid, int qSize){
        context = new RespectVMContext(this,tid, qSize);
		news = new Object();
		idle = new Object();
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
                context.spy("Waiting for news...");
                if(!this.context.pendingEvents()){
                	synchronized(news){
                		news.wait();
                	}
                }
            } catch (Exception ex){
                context.notifyException(ex);
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

	public LogicTuple[] getTRSet(LogicTuple filter){
		return context.getTRSet(filter);
	}

	public TupleCentreId getId(){
		return (TupleCentreId) context.getId();
	}
}



