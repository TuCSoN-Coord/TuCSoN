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
 * This class represents inp communication input events of the VM
 *
 *
 * @version 1.0
 */
public class InputEvent_inp extends PendingQueryEvent {

    private TupleTemplate    template;

    public InputEvent_inp(long myid,AgentId aid,TupleCentreId tid,TupleTemplate templ){
        super(myid,aid,tid);
        template=templ;
    }

    public String      getOperationName(){
        return "inp";
    }

    public int         getOperationNumArguments(){
        return 1;
    }

    public TupleTemplate       getTupleTemplate(){
        return template;
    }

    public OutputEvent  speak_action(TupleCentreVM vm){
        Tuple t=vm.removeMatchingTuple(template);
        return new OutputEvent_inp(getId(), getAgentId(), getTupleCentreId(), t,template);
    }

    public String toString(){
        return "inp("+template+")  from agent "+getAgentId();
    }
}
