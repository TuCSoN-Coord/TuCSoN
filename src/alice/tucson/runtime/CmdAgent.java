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

import alice.logictuple.*;
import alice.tucson.api.*;
import alice.tuprolog.*;

/**
 * TuCSoN Agent that executes the command
 * provided as arguments when it is created
 *
 */
public class CmdAgent extends alice.tucson.api.Agent {

    private LogicTuple args;
    public CmdAgent(AgentId id,LogicTuple args) throws TucsonException {
        super(id);
        this.args=args;
    }

    private void log(String st){
        System.out.println("[COMMAND AGENT] "+st);
    }

    private Term[] toTermList(Struct list){
        Term[] tlist = new Term[list.listSize()];
        java.util.Iterator it = list.listIterator();
        for (int index=0; it.hasNext(); index++){
            tlist[index]=(Term)it.next();
        }
        return tlist;
    }

    protected void body(){
        try {
            if (args.getArity()==3){
                Struct cmd_args = (Struct) args.getArg(1).toTerm();
                String dir = alice.util.Tools.removeApices(args.getArg(2).toString());
                Term[] term_list = toTermList(cmd_args);
                String[] cmdArray = new String[term_list.length+1];
                cmdArray[0] = alice.util.Tools.removeApices(args.getArg(0).toString());
                log("The command to execute is "+cmdArray[0]);
                for (int i=0; i<term_list.length; i++){
                    cmdArray[i+1]=alice.util.Tools.removeApices(term_list[i].toString());
                    log("The argument "+i+" is "+cmdArray[i+1]);
                }
                log("The working directory is "+dir);
                Runtime.getRuntime().exec(cmdArray,null,new java.io.File(dir));
            } else if (args.getArity()==2){
                    Struct cmd_args = (Struct) args.getArg(1).toTerm();
                    Term[] term_list = toTermList(cmd_args);
                    String[] cmdArray = new String[term_list.length+1];
                    cmdArray[0] = alice.util.Tools.removeApices(args.getArg(0).toString());
                    log("The command to execute is "+cmdArray[0]);
                    for (int i=0; i<term_list.length; i++){
                        cmdArray[i+1]=alice.util.Tools.removeApices(term_list[i].toString());
                        log("The argument "+i+" is "+cmdArray[i+1]);
                    }
                    Runtime.getRuntime().exec(cmdArray);
            } else if (args.getArity()==1){
                String[] cmdArray = new String[1];
                cmdArray[0] = alice.util.Tools.removeApices(args.getArg(0).toString());
                log("The command to execute is "+cmdArray[0]);
                Runtime.getRuntime().exec(cmdArray);
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
}

