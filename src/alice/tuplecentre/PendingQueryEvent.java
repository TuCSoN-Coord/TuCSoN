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
package alice.tuplecentre;

/**
 * Represents pending query input events of the tuple centre virtual machine
 *
 *
 * @version 1.0
 */
public abstract class PendingQueryEvent extends InputEvent {

    public PendingQueryEvent(long myid,AgentId aid,TupleCentreId tid){
        super(myid,aid,tid);
    }

    public void listen_action(TupleCentreVM vm){
        vm.addPendingQueryEvent(this);
    }

    // added 020624 --------------------
    public boolean isPersistent(){
        return false;
    }
    // ---------------------------------

    /**
     * Specifies the effect of this pending query event on the virtual machine
     * during a speaking transition. If the pending query can not be satisfiable
     * a null OutputEvent is returned
     *
     * @param vm the tuple centre virtual machine
     */
    public abstract OutputEvent    speak_action(TupleCentreVM  vm);
}
