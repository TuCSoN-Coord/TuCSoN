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
package alice.tucson.service;

import alice.respect.OutputEvent_out_tc;
import alice.respect.OutputEvent_spawn;
import alice.tucson.api.*;
import alice.logictuple.*;
import alice.tuprolog.Term;
import alice.tuprolog.Struct;
import java.lang.ClassLoader;

/**
 * This class defines the behaviour of the TuCSoN Tuple Centre
 * virtual machine
 *
 */
public class TucsonVM extends alice.respect.RespectVM {

    /** context used for inter-tuple centre operations */
    private InterTupleCentreContext helper;
    private Node node;


    static Class[] signature = new Class[]{ AgentId.class, LogicTuple.class};

    public TucsonVM(TupleCentreId tid, Node node){
        super(tid);
        this.node=node;
        try {
            helper = new InterTupleCentreContextStub(node);
        } catch (Exception ex){
            ex.printStackTrace();
        }
        this.setSpy(false);
    }

    /**
     * Manages the output events that could not be processed by
     * basic ReSpecT core, in particular the out_tc output event
     */
    protected void   notifyUnknownOutputEvent(alice.tuplecentre.OutputEvent ev){
        if (ev instanceof alice.respect.OutputEvent_out_tc){
            OutputEvent_out_tc evout=(OutputEvent_out_tc)ev;
            TupleCentreId tid=null;
            alice.tuprolog.Struct stid=(alice.tuprolog.Struct)evout.getTargetTupleCentreId().toTerm();
            try {
                tid = new TupleCentreId(stid);
            } catch (Exception ex){
                return;
            }

            try {
                helper.out_tc((AgentId)evout.getAgentId(),tid,evout.getTuple());
            } catch (Exception ex){
                ex.printStackTrace();
            }
        } else if (ev instanceof alice.respect.OutputEvent_spawn){
            OutputEvent_spawn evspawn=(OutputEvent_spawn)ev;
            Term agentId = evspawn.getSpawnedAgentId();
            Term agentType = evspawn.getSpawnedAgentType();
            Struct agentArgs = evspawn.getSpawnedAgentArguments();

            try {
                AgentId aid = new AgentId(alice.util.Tools.removeApices(agentId.toString()));
                spawnAgent(aid,agentType,agentArgs);
            } catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    /**
     *
     * Spawns an agent
     *
     *  Actually, the basic TuCSoN infrastructure supports
     *  directly two types of agent: Java Agent and tuProlog logic agent.
     *  Java agents are characterised by the type java(ClassName),
     *  Prolog agents are characterised by the type prolog(TheoryFile).
     *
     * @param id is the agent identifier
     *
     * @param agentType is the agent type.
     *
     *
     *
     * @param argsList is the list of the boot arguments.
     * @throws SpawnAgentException if it is not possible to spawn an
     *         agent with those characteristics
     */
    protected void spawnAgent(AgentId id, Term agentType, Struct argsList) throws SpawnAgentException {

        if (agentType.isStruct()){
            Struct at = (Struct)agentType;
            if (at.getName().equals("java") && at.getArity()==1){
                /*
                * Support for Java agents
                */
                Term className = at.getArg(0);
                try {
                    /*
                     * prepare arguments:
                     * from a list to a structure,
                     * for easy access to Java Agents
                     */
                    int nargs=argsList.listSize();
                    Term[] arg_list = new Term[nargs];
                    java.util.Iterator it=argsList.listIterator();
                    int count = 0;
                    while (it.hasNext()){
                        arg_list[count++]=(Term)it.next();
                    }
                    LogicTuple argStruct = new LogicTuple(new Struct("args",arg_list));

                    String flatName= alice.util.Tools.removeApices(className.toString());
                    //System.out.println("class: "+flatName+" args: "+args+"  id: "+id);

                    ClassLoader cloader = new TucsonClassLoader();
                    Class cl=cloader.loadClass(flatName);

                    TucsonAgent agent =
                               (TucsonAgent)(cl.getConstructor(signature).newInstance(new Object[]{id,argStruct}));
                    agent.spawn();
                    cloader = null;
                } catch (Exception ex){
                    //ex.printStackTrace();
                    throw new SpawnAgentException(ex.toString());
                }
            } else if (at.getName().equals("prolog") && at.getArity()==1){
                    /*
                    * Support for Logic agents
                    */
                    Term theoryFileName = at.getArg(0);
                    try {
                        String fileName= alice.util.Tools.removeApices(theoryFileName.toString());
                        SpawnedLogicAgent agent = new SpawnedLogicAgent(id,argsList,fileName);
                        agent.spawn();
                    } catch (Exception ex){
                        throw new SpawnAgentException(ex.toString());
                    }
            } else {
                throw new SpawnAgentException("unknown agent type "+at);
            }
        }

    }
}

class TucsonClassLoader extends ClassLoader{
}