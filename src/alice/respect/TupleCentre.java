/*
 * Centre.java
 *
 * Copyright 2000-2001-2002  aliCE team at deis.unibo.it
 *
 * This software is the proprietary information of deis.unibo.it
 * Use is subject to license terms.
 *
 */
package alice.respect;
import alice.logictuple.*;
import java.util.*;
import java.io.*;

import java.lang.reflect.*;

/**
 * Standalone running tuple centre vm
 */
public class TupleCentre  {

    private RespectVM  core;
    private HashMap contexts=new HashMap();

    public TupleCentre(TupleCentreId id){
        core=new RespectVM(id);
        new Thread(core).start();
        try{
            Thread.currentThread().sleep(500);
        }catch(Exception ex){
        }
    }

    public void out(LogicTuple t) throws InvalidLogicTupleException {
        LocalContext context=resolveContext();
        context.out(t);
    }

    public LogicTuple in(LogicTuple t) throws InvalidLogicTupleException {
        LocalContext context=resolveContext();
        return context.in(t);
    }

    public LogicTuple rd(LogicTuple t)  throws InvalidLogicTupleException {
        LocalContext context=resolveContext();
        return context.rd(t);
    }

    public LogicTuple inp(LogicTuple t)  throws InvalidLogicTupleException {
        LocalContext context=resolveContext();
        return context.inp(t);
    }

    public LogicTuple rdp(LogicTuple t) throws InvalidLogicTupleException  {
        LocalContext context=resolveContext();
        return context.rdp(t);
    }

    public void setSpec(String spec) throws InvalidSpecificationException {
        LocalContext context=resolveContext();
        context.setSpec(spec);
    }

    public String getSpec() {
        LocalContext context=resolveContext();
        return context.getSpec();
    }

    private synchronized LocalContext resolveContext(){
        LocalContext context=(LocalContext)contexts.get(Thread.currentThread());
        if (context==null){
            String name="'"+Thread.currentThread().toString()+"'";
            try {
                context=new LocalContext(new AgentId(name),core);
                contexts.put(Thread.currentThread(),context);
            } catch (Exception ex){
            }
        }
        return context;
    }
        
}

