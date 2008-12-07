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

import alice.tuplecentre.core.*;

/**
 * Represents an internal event of the ReSpecT VM
 *  
 * @author aricci
 * @version 1.0
 */
public class InternalEvent extends alice.tuplecentre.core.Event {

    private InputEvent inputEvent;
    private InternalOperation internalOperation; 
    

	public InternalEvent(InputEvent ev, InternalOperation op){
		super(ev.getId(),ev.getOperation(),ev.getReactingTC(),ev.getTime());
		inputEvent=ev;
		internalOperation=op;
	}

	public boolean isInput(){
		return false;
	}

	public boolean isOutput(){
		return false;
	}
	
	public boolean isInternal(){
		return true;		
	}
	
	public InternalOperation getInternalOperation(){
		return internalOperation;
	}

}
