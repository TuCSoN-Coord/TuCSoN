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
 * Represents output events of the tuple centre virtual machine
 *
 *
 * @author aricci
 */
public class OutputEvent extends Event {

    private InputEvent inputEvent;
    private boolean isLinking;
    
    public OutputEvent(InputEvent ev){
    	super(ev.getId(),ev.getOperation(),ev.getReactingTC(),ev.getTime());
        inputEvent=ev;
        isLinking = ev.isLinking();
    }
        
    public void setIsLinking(boolean flag){
    	
    	isLinking = flag;
    }

	public boolean isLinking(){
		
		return isLinking;
		
	}
    
    public InputEvent getInputEvent(){
        return inputEvent;
    }

	public boolean isInput(){
		return false;
	}

	public boolean isOutput(){
		return true;
	}

	public boolean isInternal(){
		return false;
	}
	
	

	
	public String toString(){
		return "output_event(agentId("+getId()+"),operation("+(this.getOperation().isResultDefined() ? getOperation().getResult(): getOperation())+"))";
	}
    
}
