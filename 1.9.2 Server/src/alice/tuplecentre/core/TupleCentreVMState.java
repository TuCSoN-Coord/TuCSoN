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
 * Abstract base class for Tuple Centre VM states.
 *
 * @author  aricci
 */
public abstract class TupleCentreVMState {
    
    protected TupleCentreVMContext vm;
    
    public TupleCentreVMState(TupleCentreVMContext vm) {
        this.vm=vm;
    }
    
    public abstract TupleCentreVMState getNextState();
    
    public abstract void execute();
    
    public abstract void resolveLinks();
    
    public boolean isIdle(){
        return false;
    }
    
}
