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
 * Represents output event produced as answer to a inp input event
 *
 *
 * @version 1.0
 */
public class OutputEvent_inp extends OutputEvent {

    private Tuple tuple;
    private TupleTemplate template;

    public OutputEvent_inp(long myid,AgentId aid,TupleCentreId tid,Tuple t,TupleTemplate templ){
        super(myid,aid,tid);
        tuple=t;
        template=templ;
    }

    public boolean isSuccess(){
        return tuple!=null;
    }

    public boolean isFailure(){
        return tuple==null;
    }

    public Tuple getTuple(){
        return tuple;
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

}
