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
import alice.tuplecentre.*;
import alice.logictuple.LogicTuple;

/**
 *
 * @version 1.0
 */
public final class Event_in_r extends ReactionEvent {

    private LogicTuple    tuple;

    public Event_in_r(long myid,AgentId aid,TupleCentreId id,LogicTuple tuple){
        super(myid,aid,id);
        this.tuple=tuple;
    }

    public String      getOperationName(){
        return "in_r";
    }

    public int         getOperationNumArguments(){
        return 1;
    }

    public boolean isToTupleCentre(){
        return false;
    }

    public boolean isFromTupleCentre(){
        return true;
    }

    public LogicTuple getTuple(){
        return tuple;
    }

}
