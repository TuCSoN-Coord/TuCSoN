/*
 * User.java
 *
 * Copyright 2000-2001-2002  aliCE team at deis.unibo.it
 *
 * This software is the proprietary information of deis.unibo.it
 * Use is subject to license terms.
 *
 */
package alice.tucson.runtime;
import alice.tucson.api.*;
import alice.logictuple.*;


import java.util.*;

/**
 *
 *
 */
public class NodeManagementAgent extends Thread {

    private boolean isLogging = true;
        
    Node node;
    
    /**
     * for local access (valid only if
     * user is in the same java VM of daemon
     */
    private alice.tuplecentre.Event  evReceived;
    //
    private LocalTupleCentreContext config;
    
    public NodeManagementAgent(TucsonVM core, Node node){
        try {
            this.node=node;
            config = new LocalTupleCentreContext(new AgentId("node_management_agent"),core);
            start();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    //
    
    public void run(){
        log("agent booted.");
        //
        while (true){
            try {
                LogicTuple cmd=config.in(new LogicTuple("cmd",new Var("X")));
                log("received command "+cmd);
                execCmd(cmd.getArg(0));
            } catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }
    
    protected void execCmd(TupleArgument cmd) throws Exception {
        String name=cmd.getName();
        if (name.equals("destroy")){
            String tcName = cmd.getArg(0).getName();
            boolean result=node.destroyCore(tcName);
            if (result){
                log(tcName+" destroyed");
            } else {
                log("cannot destroy "+tcName);
            }
        }
    }
    
    
    protected void log(String s){
        if (isLogging){
            System.out.println("[NODE MANAGEMENT AGENT] "+s);
        }
    }   
}



