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
import java.util.*;

/**
 *
 * Tucson Virtual Machine Users Information
 *
 */
public class TucsonVMUsers {

    private String name;
    private Thread idThread;
    private TucsonVM vm;
    private Date creationDate;
    private LinkedList currentUsers;

    public TucsonVMUsers(String name, TucsonVM vm, Thread idThread){
        this.name=name;
        this.vm=vm;
        this.idThread=idThread;
        creationDate=new Date();
        currentUsers=new LinkedList();
    }

    public Thread getThread(){
        return idThread;
    }

    public TucsonVM getVirtualMachine(){
        return vm;
    }

    public Date getCreationDate(){
        return creationDate;
    }

    public void addUser(ContextSkeleton skel){
        synchronized(currentUsers){
            currentUsers.add(skel);
        }
    }

    public void removeUser(ContextSkeleton skel){
        synchronized(currentUsers){
            currentUsers.remove(skel);
        }
    }

    public void removeAllUsers(){
        synchronized(currentUsers){
            Iterator it=currentUsers.iterator();
            while (it.hasNext()){
                try {
                    ((ContextSkeleton)(it.next())).exit();
                    it.remove();
                } catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        }
    }
}

