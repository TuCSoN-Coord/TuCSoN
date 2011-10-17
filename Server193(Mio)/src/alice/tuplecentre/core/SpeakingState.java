/*
 * Tuple Centre media - Copyright (C) 2001-2002  aliCE team at deis.unibo.it
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
package alice.tuplecentre.core;

import java.util.*;

import alice.respect.core.RespectVMContext;
import alice.tuplecentre.api.Tuple;
import alice.tuplecentre.api.TupleCentreId;

/**
 * This is the speaking state of the TCVM
 *
 * @author  aricci
 */
public class SpeakingState extends TupleCentreVMState {
    
    private TupleCentreVMState reactingState;
    private TupleCentreVMState idleState;
    private boolean noMoreSatisfiablePendingQuery;
    
    public SpeakingState(TupleCentreVMContext vm) {
        super(vm);
    }
    
    public TupleCentreVMState getNextState(){
        if (vm.triggeredReaction()){
            return reactingState;
        } else if (noMoreSatisfiablePendingQuery){
            return idleState;
        } else {
        	return this;  
        }
    }
    
    public void resolveLinks(){
        reactingState = vm.getState("ReactingState");
        idleState = vm.getState("IdleState");
    }
    
    public void execute(){
        Iterator it = vm.getPendingQuerySetIterator();
        InputEvent ev = null;
        OutputEvent out_ev = null;
		noMoreSatisfiablePendingQuery=true;
		
		boolean foundSatisfied = false;	  	 
		Tuple tuple = null;
		List<Tuple> tupleList= null;

        while (it.hasNext() && !foundSatisfied) {
            try {
            	
            	
            	
	            ev = (InputEvent) (it.next());
	            TupleCentreOperation op = ev.getOperation();
	            vm.spy("(1)I GOT THIS OPERATION TO SERVE: "+op);
	            if(op.isResultDefined() || ev.isLinking()){
	            	//System.out.println("(speaking) IS LINKING --> "+ev.isLinking());
	            	foundSatisfied = true;
	            }else{
	            
		            if (op.isOut()){
		            		tuple = op.getTupleArgument();
		                	vm.addTuple(tuple);
		                	foundSatisfied = true;
		            } else if (op.isIn()){
		                tuple=vm.removeMatchingTuple(op.getTemplateArgument());
						if (tuple!=null){
							//System.out.println("INNNNNN");
							foundSatisfied=true;
						}
		            } else if (op.isRd()){
						tuple=vm.readMatchingTuple(op.getTemplateArgument());
						if (tuple!=null){
							foundSatisfied=true;
						}
					} else if (op.isInp()){
						vm.spy("RISULTATO SO FAR: "+op.getResult()); 
						tuple=vm.removeMatchingTuple(op.getTemplateArgument());
						foundSatisfied=true;
					} else if (op.isRdp()){
						tuple=vm.readMatchingTuple(op.getTemplateArgument());
						foundSatisfied=true;
					} else if (op.isOut_s()){
						tuple = op.getTupleArgument();
						vm.addSpecTuple(tuple);
						foundSatisfied = true;
					} else if (op.isIn_s()){
						tuple = vm.removeMatchingSpecTuple(op.getTemplateArgument());
						if (tuple != null)
							foundSatisfied = true;
					} else if (op.isRd_s()){
						tuple=vm.readMatchingSpecTuple(op.getTemplateArgument());
						if (tuple!=null)
							foundSatisfied=true;
					} else if (op.isInp_s()){
						tuple=vm.removeMatchingSpecTuple(op.getTemplateArgument());
						foundSatisfied=true;
					} else if (op.isRdp_s()){
						tuple=vm.readMatchingSpecTuple(op.getTemplateArgument());
						foundSatisfied=true;
					}else if (op.isGet()){
						tupleList = new LinkedList<Tuple>();
						tupleList=vm.getAllTuples();
						foundSatisfied=true;
		            }else if (op.isSet()){
		            	tupleList = op.getTupleListArgument();
		            	vm.setAllTuples(tupleList);
		            	foundSatisfied=true;
					}					
					else throw new InvalidOperationException();
		            		            
		            if (((RespectVMContext)vm).getRespectVM().getObservers().size()>0){
						((RespectVMContext)vm).getRespectVM().notifyObservableEvent(ev);
					}
	            }
            } catch(InvalidOperationException ex){
            	vm.spy("ERROR: "+ev.getOperation().getType()+"");
            	vm.notifyException(ex);
            	it.remove();
            }                
        }

		if (foundSatisfied){
			TupleCentreOperation op = ev.getOperation();
            vm.spy("(2)I GOT THIS OPERATION TO SERVE: "+op);

			// for failed inp/rdp the tuple ref can be null
			
			if(!op.isResultDefined() && !ev.isLinking()){	
				if(op.isGet()){
					op.setResultList(tupleList);
				}else{
					vm.spy("STO SETTANDO IL RISULTATO ORA");
					op.setResult(tuple);
				}
			}
			// generates and notifies the event
			
			//op.setResult(tuple);
			out_ev = new OutputEvent(ev);
			//System.out.println("TUPLE: "+tuple+" res: "+out_ev.getOperation().getResult());
			
			//if(ev.isLinking()) out_ev.setIsLinking(true);
			if(!ev.isLinking()){
				//System.out.println("NON LINKING, SOURCE: "+out_ev.getSource());
				//out_ev.setSource(vm.getId());
				vm.notifyOutputEvent(out_ev);
				
				if (((RespectVMContext)vm).getRespectVM().getObservers().size()>0){
					((RespectVMContext)vm).getRespectVM().notifyObservableEvent(out_ev);
				}
			}
			
			
			
			
			if(ev.isLinking() && !op.isResultDefined()){
				out_ev.setTarget(ev.getTarget());
				//System.out.println("TARGET: "+ev.getTarget()+" SOURCE: "+ev.getSource()+" OPERATION: "+op);
				vm.linkOperation(out_ev);
			}
			
		//	System.out.println("$$$$$$$$$$$$$"+out_ev.getSource());
			// triggers possible reactions
			//out_ev.setReactingTC(vm.getId());
			vm.fetchTriggeredReactions(out_ev);
			
			noMoreSatisfiablePendingQuery=false;
			it.remove();
			
			
		}
        
    }
}
