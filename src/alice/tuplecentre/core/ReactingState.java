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

import alice.tuplecentre.api.TupleTemplate;

/**
 * This is the reacting state of the TCVM
 *
 * @author  aricci
 */
public class ReactingState extends TupleCentreVMState {
    
    private TupleCentreVMState speakingState;
    private TupleCentreVMState fetchEnvState;
    
    public ReactingState(TupleCentreVMContext vm) {
        super(vm);
    }
    
    public TupleCentreVMState getNextState(){
        if (vm.triggeredReaction() || vm.time_triggeredReaction()){
            return this;
        }else if (vm.pendingEnvEvents()){
            return fetchEnvState;
        }
        return speakingState;
    }
    
    public void resolveLinks(){
    	speakingState = vm.getState("SpeakingState");
    	fetchEnvState = vm.getState("FetchEnvState");
    }
    
    public void execute(){
        TriggeredReaction tr = vm.removeTriggeredReaction();
        if (tr!=null){
            vm.evalReaction(tr);
        }else if(vm.time_triggeredReaction()){
        	
        	tr = vm.removeTimeTriggeredReaction();
        	if (tr!=null){
        		vm.evalReaction(tr);
        		vm.updateSpecAfterTimedReaction(tr);
        	}
        }
    }
}
