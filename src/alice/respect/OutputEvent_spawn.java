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
package alice.respect;
import alice.logictuple.LogicTuple;
import alice.tuprolog.Struct;
import alice.tuprolog.Term;
/**
 * The spawn output event is generated when spawn
 * ReSpecT primitive is used
 */
public class OutputEvent_spawn extends alice.tuplecentre.OutputEvent {

    private Term agentId;
    private Term agentType;
    private Struct args;

    public OutputEvent_spawn(AgentId aid,TupleCentreId tid,
            Term agentId, Term atype, Struct args){
        super(-1,aid,tid);
        this.agentId=agentId;
        agentType=atype;
        this.args=args;
    }

    public boolean isSuccess(){
        return true;
    }

    public boolean isFailure(){
        return false;
    }

    public String getOperationName(){
        return "spawn";
    }

    public int         getOperationNumArguments(){
        return 3;
    }

    public Term getSpawnedAgentId(){
        return agentId;
    }

    public Term getSpawnedAgentType(){
        return agentType;
    }

    public Struct getSpawnedAgentArguments(){
        return args;
    }

    public String toString(){
        return getOperationName()+"("+agentId+", "+agentType+","+args+")";
    }

}
