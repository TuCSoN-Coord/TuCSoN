/*
 * UserLocal.java
 *
 * Copyright 2000-2001-2002  aliCE team at deis.unibo.it
 *
 * This software is the proprietary information of deis.unibo.it
 * Use is subject to license terms.
 *
 */
package alice.respect;
import alice.logictuple.*;
import alice.tuplecentre.*;
import java.util.*;

/**
 *
 */
class LocalContext implements OutputEventListener {

    /** user id */
    private AgentId  id;

    /** tuple centre to access */
    private RespectVM core;

    /** event notified by the tuple centre */
    private OutputEvent  evReceived;

    public LocalContext(AgentId id_, RespectVM core_){
        core=core_;
        id=id_;
    }

    public AgentId getId(){
        return id;
    }

    public synchronized void out(LogicTuple t) throws InvalidLogicTupleException {
        if (t==null){
            throw new InvalidLogicTupleException();
        }
        //spy(id+" out "+t);
        core.out(t,id,null);
    }

    public synchronized LogicTuple in(LogicTuple t) throws InvalidLogicTupleException {
        if (t==null){
            throw new InvalidLogicTupleException();
        }
        //spy(id+" in "+t);
        core.in(t,id,this);
        //spy(id+" in processed "+t);
        try {
            wait();
        } catch (Exception e){
        }
        //spy(id+" in return "+evReceived.tuple);
        return unify(t,((OutputEvent_in)evReceived).getTuple());
    }

    public synchronized LogicTuple rd(LogicTuple t) throws InvalidLogicTupleException {
        if (t==null){
            throw new InvalidLogicTupleException();
        }
        //spy(id+" rd "+t);
        core.rd(t,id,this);
        //spy(id+" rd processed "+t);
        try {
            wait();
        } catch (Exception e){
        }
        //spy(id+" rd return "+evReceived.tuple);
        return unify(t,((OutputEvent_rd)evReceived).getTuple());
    }

    public synchronized LogicTuple inp(LogicTuple t) throws InvalidLogicTupleException {
        if (t==null){
            throw new InvalidLogicTupleException();
        }
        core.inp(t,id,this);
        try {
            wait();
        } catch (Exception e){
        }
        if (evReceived.isSuccess())
            return unify(t,((OutputEvent_inp)evReceived).getTuple());
        else
            return null;
    }

    public synchronized LogicTuple rdp(LogicTuple t) throws InvalidLogicTupleException {
        if (t==null){
            throw new InvalidLogicTupleException();
        }
        core.rdp(t,id,this);
        try {
            wait();
        } catch (Exception e){
        }
        if (evReceived.isSuccess())
            return unify(t,((OutputEvent_rdp)evReceived).getTuple());
        else
            return null;
    }

    public synchronized void setSpec(String spec) throws InvalidSpecificationException {
        if (spec==null){
            throw new InvalidSpecificationException();
        }
        core.setSpec(spec,id,this);
        try {
            wait();
        } catch (Exception e){
        }
        if (!evReceived.isSuccess()){
            throw new InvalidSpecificationException();
        }
    }

    public synchronized String getSpec(){
        core.getSpec(id,this);
        try {
            wait();
        } catch (Exception e){
        }
        return ((OutputEvent_getSpec)evReceived).getSpec();
    }

    private LogicTuple unify(alice.tuplecentre.TupleTemplate template,alice.tuplecentre.Tuple tuple){
        boolean res=template.propagate(tuple);
        if (res){
            return (LogicTuple)template;
        } else {
            return null;
        }
    }

    public synchronized void notify(OutputEvent ev) {
        evReceived=ev;
        notify();
    }
    
    
    void spy(String st){
        System.out.println(st);
    }
    
    
}

