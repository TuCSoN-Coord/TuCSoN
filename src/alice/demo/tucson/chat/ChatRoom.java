/*
 * Chat TuCSoN Demo - Copyright (C) 2001 deis.unibo.it
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
package alice.demo.tucson.chat;

import alice.logictuple.*;
import alice.tucson.api.*;

import java.io.BufferedInputStream;

/**
 * The Chat Room setups the tuple centre,
 * with proper specification.
 *
 * @author  Alessandro Ricci
 *
 */
public class ChatRoom {

    public static void main (String args[]) throws Exception {
        if (args.length!=1){
            System.err.println("args: < IP address of the TuCSoN node where the chat service must be located >");
            System.exit(-1);
        }
        TupleCentreId tid=new TupleCentreId("chat_room @ '"+args[0]+"'");
        try {

            Tucson.enterDefaultContext().setSpec(tid,
                    alice.util.Tools.loadText(
                       new BufferedInputStream(ClassLoader.getSystemResourceAsStream("alice/demo/tucson/chat/chat_spec.rsp"))));
            System.out.println("ok.");
        } catch (Exception ex){
            ex.printStackTrace();
            System.exit(-1);
        }
    }
}

