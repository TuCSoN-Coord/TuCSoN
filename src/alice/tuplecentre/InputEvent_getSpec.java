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
 *
 * This class represents getSpec communication input events of the VM
 *
 *
 * @version 1.0
 */
public class InputEvent_getSpec extends PendingQueryEvent {

    public InputEvent_getSpec(long myid,AgentId aid,TupleCentreId tid){
        super(myid,aid,tid);
    }

    public String      getOperationName(){
        return "get_spec";
    }

    public int         getOperationNumArguments(){
        return 0;
    }

    public void listen_action(TupleCentreVM vm){
        vm.addPendingQueryEvent(this);
    }

    public OutputEvent speak_action(TupleCentreVM vm){
        String spec=vm.getReactionSpec();
        return new OutputEvent_getSpec(getId(), getAgentId(), getTupleCentreId(),spec);
    }

    public String toString(){
        return "get_spec  from agent "+getAgentId();
    }
}
