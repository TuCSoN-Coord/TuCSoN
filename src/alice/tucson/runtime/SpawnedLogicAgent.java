/*
 * tuProlog - Copyright (C) 2001-2002  aliCE team at deis.unibo.it
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

import alice.tuprolog.*;
import alice.tucson.api.*;
import alice.logictuple.*;

import java.io.*;

/**
 * This class represents the Prolog agents
 * spawned by the spawn ReSpecT primitive
 *
 * The logic agent can access to TuCSoN by means of the
 * alice.tuprologx.lib.TucsonLibrary that is automatically
 * loaded in the engine used by the agent.
 *
 * The entry point for the logic theory execution
 * must be a clause body(Args), where args are the arguments
 * passed to the agent, in a structure whose functor is args.
 */
public class SpawnedLogicAgent extends alice.tucson.api.Agent {

    private Prolog core;
    private String theory;
    private Term   args;

    /**
     * Creates a logic agent
     *
     * @param id is the agent identifier
     * @param argList is the argument list that is passed to the agent
     * @param fileTheory is the theory that constitutes agent body
     * @throws TucsonException is thrown either if the theory is not
     *          not valid, or there are problems in building the Prolog
     *          engine.
     */
    public SpawnedLogicAgent(AgentId id, Struct argList, String fileTheory) throws TucsonException {
        super(id);
        this.theory=fileTheory;
        this.args = argList;
    }

    final protected void body(){
        try {
                core=new Prolog();
                alice.tucson.api.Tucson2PLibrary lib=
                        (alice.tucson.api.Tucson2PLibrary)core.loadLibrary("alice.tucson.api.Tucson2PLibrary");
                lib.setContext(getContext());
                OutputListener l=new OutputListener() {
                    public void onOutput(OutputEvent ev) {
                        System.out.print(ev.getMsg());
                }};
                core.addOutputListener(l);
                core.setTheory(new Theory(new FileInputStream(theory)));
            core.solve("body("+args.toString()+").");
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
}