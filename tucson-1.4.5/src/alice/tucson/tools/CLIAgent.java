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
package alice.tucson.tools;
import java.io.*;
import java.util.*;
import alice.tucson.api.*;
import alice.logictuple.*;
import java.lang.reflect.*;

/**
 * Command Line Interpreter
 */
public class CLIAgent {

    public static void main(String[] args) throws Exception{
        System.out.println("Booting TuCSoN Command Line Intepreter ...");
        if (alice.util.Tools.isOpt(args,"-?")){
            System.out.println("Argument Template: ");
            System.out.println("{-aid agent identifier} {-console} {-?}");
        } else {
            String st_aid = null;
            if (alice.util.Tools.isOpt(args,"-aid")){
                st_aid=alice.util.Tools.getOpt(args,"-aid");
            } else {
                try {
                    st_aid="cli_"+System.currentTimeMillis()+"@"+
                                    java.net.InetAddress.getLocalHost().getHostAddress();
                } catch (Exception ex){
                    st_aid="cli_"+System.currentTimeMillis();
                }
            }

            System.out.println("Agent Identifier: "+st_aid);
            TucsonContextSynch context = null;
            try {
                context=new TucsonContextSynch(Tucson.getContext(new AgentId(st_aid)));

                if (alice.util.Tools.isOpt(args,"-console")){
                    new Thread(new ConsoleInterpreter(context)).start();
                } else {
                    GUIInterpreter form=new GUIInterpreter(context);
                    form.setSize(400,200);
                    form.show ();
                }
            } catch (Exception ex){
                System.err.println("AGENT FAILED: "+ex);
            }
        }
    }
}