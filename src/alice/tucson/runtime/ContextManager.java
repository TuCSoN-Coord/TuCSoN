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
import alice.tucson.api.util.TucsonProtocol;
import alice.logictuple.LogicTuple;
import alice.logictuple.Value;
import alice.logictuple.Var;
import alice.logictuple.TupleArgument;

import java.lang.reflect.Constructor;


/**
 *
 * Node class provides the basic daemon of a
 * TuCSoN infrastructure node.
 *
 */
public class ContextManager  {

    LocalTupleCentreContext configContext;
    Node               nodeAgent;

    public ContextManager(Node agent, TucsonVM contextTC){
        try {
            configContext = new LocalTupleCentreContext(new AgentId("context_manager"),contextTC);
        } catch (Exception ex){
        }
        nodeAgent=agent;
    }

    private void log(String st){
        System.out.println(st);
    }

    public boolean validateContext(ContextDescription descr){
        return true;
    }



    public ContextSkeleton    buildContext(TucsonProtocol dialog, ContextDescription profile) throws Exception {

        String contextName = profile.getProperty("context-name");
        if (contextName==null){
            throw new TucsonException("Context negotiation failed: context-name property not found");
        }

        //
        // retrieve context description
        //
        LogicTuple contextDescription = getContextDescription(profile);
        if (contextDescription==null){
            throw new TucsonException("Context negotiation failed: context "+contextName+" not available.");
        }

        String exceptionDescr;
        try {

            String agentProfile = profile.getProperty("agent-profile");
            if (agentProfile==null){
                throw new TucsonException("Context negotiation failed: agent-profile property not found");
            }

            String agentId = profile.getProperty("agent-identity");

            LogicTuple req=new LogicTuple("context_code",new Value(contextName), new Value(agentProfile),new Var());
            LogicTuple contextTuple = configContext.rdp(req);

            if (contextTuple!=null){
                TupleArgument codeDescr=contextTuple.getArg(2);
                if (codeDescr.getName().equals("skeleton_class")){
                    String className=codeDescr.getArg(0).getName();

                    Class cl=Class.forName(className);
                    Constructor m=cl.getConstructor(new Class[]
                        {TucsonProtocol.class, Node.class, ContextDescription.class, LogicTuple.class});
                    ContextSkeleton skel=(ContextSkeleton)m.newInstance(new Object[]
                        {dialog,nodeAgent,profile,contextDescription});
                    if (skel!=null){
                        return skel;
                    } else {
                        exceptionDescr="skeleton_class not found";
                    }
                } else {
                    exceptionDescr="skeleton_class property not found in context "+contextTuple;
                }
            } else {
                exceptionDescr="context not found "+contextName;
            }
        } catch (Exception ex){
            ex.printStackTrace();
            throw new TucsonException();
        }
        throw new TucsonException(exceptionDescr);
    }


    private LogicTuple getContextDescription(ContextDescription p) {
        try {

            String contextName = p.getProperty("context-name");
            String agentName = p.getProperty("agent-identity");
            if (contextName==null || agentName==null){
                return null;
            }

            String tcName = p.getProperty("tuple-centre");
            if (tcName==null){
                tcName="_";
            }
            TupleArgument tcArg=TupleArgument.parse(tcName);
            LogicTuple req=new LogicTuple("context",new Value(contextName),
                                                    new Value(agentName),
                                                    tcArg,
                                                    new Var("Descr"));
            //System.out.println("profile req: "+req);
            return configContext.inp(req);
        } catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }
}

