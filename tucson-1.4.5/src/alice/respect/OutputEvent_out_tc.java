/*
 * ReSpecT Framework - aliCE team at deis.unibo.it
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


public class OutputEvent_out_tc extends alice.tuplecentre.OutputEvent {

    LogicTuple targetTupleCentreId;
    LogicTuple tuple;

    public OutputEvent_out_tc(AgentId aid,TupleCentreId tid,
            LogicTuple destTid,LogicTuple t){
        super(-1,aid,tid);
        targetTupleCentreId=destTid;
        tuple=t;
    }

    public boolean isSuccess(){
        return true;
    }

    public boolean isFailure(){
        return false;
    }

    public String getOperationName(){
        return "out_tc";
    }

    public int         getOperationNumArguments(){
        return 2;
    }

    public LogicTuple getTuple(){
        return tuple;
    }

    public LogicTuple getTargetTupleCentreId(){
        return targetTupleCentreId;
    }

    public String toString(){
        return getOperationName()+" "+tuple+" to "+targetTupleCentreId+ "  from agent "+getAgentId();
    }

}
