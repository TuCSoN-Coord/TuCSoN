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

import java.util.Map;

import alice.respect.core.RespectTCContainer;
import alice.respect.core.RespectVMContext;
import alice.tuplecentre.api.AgentId;
import alice.tuplecentre.api.IId;
import alice.tuplecentre.api.TupleCentreId;

/**
 * Represents input events of the tuple centre virtual machine
 *
 *
 * @author aricci
 */
public class InputEvent extends Event {

	private boolean isLinking;
    
    public InputEvent(IId aid, TupleCentreOperation op, TupleCentreId c,long time){
        super(aid,op,c,time);
        this.isLinking = false;
    }

    public InputEvent(IId aid, TupleCentreOperation op, TupleCentreId c,long time, Map<String, String> prop){
    	super(aid,op,c,time,prop);
        this.isLinking = false;
    }
    public String toString(){
    	return "input_event(agentId("+getId()+"),operation("+getOperation()+"))";
    }
    
	public boolean isInput(){
		return true;
	}

	public boolean isOutput(){
		return false;
	}

	public boolean isInternal(){
		return false;
	}
	
	public void setIsLinking(boolean flag){
    	
    	isLinking = flag;
    }

	public boolean isLinking(){
		
		return isLinking;
		
	}


}
