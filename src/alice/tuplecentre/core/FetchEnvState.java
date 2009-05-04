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

/**
 * This is the listening state of the TCVM
 *
 * @author  aricci
 */
public class FetchEnvState extends TupleCentreVMState {
    
    private TupleCentreVMState reactingState;
    private TupleCentreVMState speakingState;
    
    public FetchEnvState(TupleCentreVMContext vm) {
        super(vm);
    }
    
    public TupleCentreVMState getNextState(){
        if (vm.triggeredReaction()){
            return reactingState;
        } else {
            return speakingState;
        }
    }
    
    public void resolveLinks(){
        reactingState = vm.getState("ReactingState");
        speakingState = vm.getState("SpeakingState");
    }
        
    public void execute(){
		vm.fetchPendingEnvEvent();
        InputEvent ev=vm.getCurrentEvent();
        vm.fetchTriggeredReactions(ev);
    }
}
