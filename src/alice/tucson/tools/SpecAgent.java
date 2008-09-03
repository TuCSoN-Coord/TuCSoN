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
 * Agent for Setting Behaviour of Tuple Centres
 */
public class SpecAgent {

    public static void main(String[] args) throws Exception{
        System.out.println("Setting a new behaviour of a TuCSoN tuple centre.");
        if (alice.util.Tools.isOpt(args,"-?")){
            System.out.println("Argument Template: ");
            System.out.println("-tid tuple centre identifier -file behaviour specification file {-aid agent identifier} {-?}");
        } else {
            String st_aid = null;
            String st_tid = null;
            String st_file = null;

            if (alice.util.Tools.isOpt(args,"-aid")){
                st_aid=alice.util.Tools.getOpt(args,"-aid");
            } else {
                try {
                    st_aid="user('"+System.currentTimeMillis()+"','"+
                                    java.net.InetAddress.getLocalHost().getHostAddress()+"')";
                } catch (Exception ex){
                    st_aid="user('"+System.currentTimeMillis()+"+', localhost)";
                }
            }

            if (alice.util.Tools.isOpt(args,"-tid")){
                st_tid = alice.util.Tools.getOpt(args,"-tid");
            } else {
                System.err.println("args: -aid agent identifier -tid tuple centre identifier -file behaviour specification file");
                System.err.println("OPERATION FAILED: tuple centre identifier is required");
                System.exit(-1);
            }

            if (alice.util.Tools.isOpt(args,"-file")){
                st_file = alice.util.Tools.getOpt(args,"-file");
            } else {
                System.err.println("args: -aid agent identifier -tid tuple centre identifier -file behaviour specification file");
                System.err.println("OPERATION FAILED: ReSpecT file is required");
                System.exit(-1);
            }

            System.out.println("Agent        Identifier: "+st_aid);
            System.out.println("Tuple Centre Identifier: "+st_tid);
            System.out.println("ReSpecT file name:       "+st_file);
            try {
                TucsonContextSynch context = null;

                context=new TucsonContextSynch(Tucson.getContext(new AgentId(st_aid)));

                TupleCentreId tid = new TupleCentreId(st_tid);

                String spec = alice.util.Tools.loadText(st_file);

                context.setSpec(tid,spec);

                System.out.println("OPERATION DONE.");
            } catch (Exception ex){
                System.err.println("OPERATION FAILED: "+ex);
                System.exit(-1);
            }
        }
    }
}