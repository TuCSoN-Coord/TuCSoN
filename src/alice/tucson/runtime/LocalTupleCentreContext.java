/*
 * TuCSoN coordination infrastructure - Copyright (C) 2001-2002  aliCE team at deis.unibo.it
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
package alice.tucson.runtime;
import alice.tucson.api.*;
import alice.logictuple.*;


import java.util.*;
import java.io.*;

/**
 *
 *
 */
public class LocalTupleCentreContext implements alice.tuplecentre.OutputEventListener  {

    private AgentId  id;
    
    private alice.tuplecentre.Event  evReceived;

    private TucsonVM metaTC;
    
    public LocalTupleCentreContext(AgentId aid, TucsonVM core){
        id=aid;
        metaTC=core;
    }


    public synchronized void out(LogicTuple t)
        throws TucsonException, InvalidLogicTupleException {
        
        if (t==null){
            throw new InvalidLogicTupleException();
        }
        
        metaTC.out(t,id,null);
    }
    
    
    public synchronized void outSync(LogicTuple t)
        throws TucsonException, InvalidLogicTupleException {
        
        if (t==null){
            throw new InvalidLogicTupleException();
        }
        metaTC.out(t,id,null);
    }
    
    public synchronized LogicTuple in(LogicTuple t)
        throws TucsonException, InvalidLogicTupleException {
        if (t==null){
            throw new InvalidLogicTupleException();
        }
        metaTC.in(t,id,this);
        try {
            wait();
        } catch (Exception e){
        }
        return unify(t,((alice.tuplecentre.OutputEvent_in)evReceived).getTuple());
    }
    
    public synchronized LogicTuple rd(LogicTuple t)
        throws TucsonException, InvalidLogicTupleException {
        if (t==null){
            throw new InvalidLogicTupleException();
        }
        metaTC.rd(t,id,this);
        try {
            wait();
        } catch (Exception e){
        }
        return unify(t,((alice.tuplecentre.OutputEvent_rd)evReceived).getTuple());
    }
    
    public synchronized LogicTuple inp(LogicTuple t)
        throws TucsonException, InvalidLogicTupleException {
        if (t==null){
            throw new InvalidLogicTupleException();
        }
        metaTC.inp(t,id,this);
        try {
            wait();
        } catch (Exception e){
        }
        if (evReceived.isSuccess()){
            return unify(t,((alice.tuplecentre.OutputEvent_inp)evReceived).getTuple());
        } else {
            return null;
        }
    }
    
    public synchronized LogicTuple rdp(LogicTuple t)
        throws TucsonException, InvalidLogicTupleException {
        
        if (t==null){
            throw new InvalidLogicTupleException();
        }
        metaTC.rdp(t,id,this);
        try {
            wait();
        } catch (Exception e){
        }
        if (evReceived.isSuccess()){
            return unify(t,((alice.tuplecentre.OutputEvent_rdp)evReceived).getTuple());
        } else {
            return null;
        }
    }
    
    public synchronized void setSpec(String spec)
        throws TucsonException, InvalidSpecificationException {
        if (spec==null){
            throw new InvalidSpecificationException();
        }
        metaTC.setSpec(spec,id,this);
        try {
            wait();
        } catch (Exception e){
        }
        if (!((alice.tuplecentre.OutputEvent_setSpec)evReceived).isSuccess()){
            throw new InvalidSpecificationException();
        }
    }
    
    public synchronized String getSpec()  throws TucsonException {
        metaTC.getSpec(id,this);
        try {
            wait();
        } catch (Exception e){
        }
        return ((alice.tuplecentre.OutputEvent_getSpec)evReceived).getSpec();
    }
    
    /**
     * for local user:  notification of a new
     * event from a tuple centre virtual machine
     */
    public synchronized void notify(alice.tuplecentre.OutputEvent ev) {
        evReceived=ev;
        notify();
    }
    
    /**
     * Helper service, which unifies a tuple template and a tuple
     *
     * @param template the tuple template
     * @param tuple the tuple to be unified with the template
     * @return the unified tuple
     */
    private LogicTuple unify(alice.tuplecentre.TupleTemplate template,alice.tuplecentre.Tuple tuple){
        boolean res=template.propagate(tuple);
        if (res){
            return (LogicTuple)template;
        } else {
            return null;
        }
    }
}


