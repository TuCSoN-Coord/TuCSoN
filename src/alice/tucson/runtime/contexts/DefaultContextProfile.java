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
package alice.tucson.runtime.contexts;

import alice.logictuple.*;
import alice.tucson.api.*;

import alice.tuprolog.Term;
import alice.tuprolog.Struct;

/**
 *
 * @author  aricci
 * @version
 */
public class DefaultContextProfile {
        
    private LogicTuple[] allowedSet_out;
    private LogicTuple[] allowedSet_outSync;
    private LogicTuple[] allowedSet_in;
    private LogicTuple[] allowedSet_rd;
    private LogicTuple[] allowedSet_inp;
    private LogicTuple[] allowedSet_rdp;
    private LogicTuple[] forbiddenSet_out;
    private LogicTuple[] forbiddenSet_outSync;
    private LogicTuple[] forbiddenSet_in;
    private LogicTuple[] forbiddenSet_rd;
    private LogicTuple[] forbiddenSet_inp;
    private LogicTuple[] forbiddenSet_rdp;
    
    private boolean timeExtensionPresent = false;
    private long timeExtension;
    private long startTime;
    
    private boolean dateValidityPresent = false;
    private long  expiringDateMillis;
        

    public DefaultContextProfile(LogicTuple contextDescr) throws InvalidContextDescriptionException {
        try {
            TupleArgument descr=contextDescr.getArg(3);
            //
            Struct list=(Struct)descr.toTerm();
            java.util.Iterator it=list.listIterator();
            while (it.hasNext()){
                Struct property=(Struct)it.next();
                //System.out.println("evaluating property "+property);
                //
                // security constraints
                //
                if (property.getName().equals("allowed_out")){
                    allowedSet_out=parseList(property.getTerm(0));
                } else if (property.getName().equals("allowed_in")){
                    allowedSet_in=parseList(property.getTerm(0));
                } else if (property.getName().equals("allowed_rd")){
                    allowedSet_rd=parseList(property.getTerm(0));
                } else if (property.getName().equals("allowed_inp")){
                    allowedSet_inp=parseList(property.getTerm(0));
                } else if (property.getName().equals("allowed_rdp")){
                    allowedSet_rdp=parseList(property.getTerm(0));
                } else if (property.getName().equals("allowed_out_sync")){
                    allowedSet_outSync=parseList(property.getTerm(0));
                } else if (property.getName().equals("forbidden_out")){
                    forbiddenSet_out=parseList(property.getTerm(0));
                } else if (property.getName().equals("forbidden_in")){
                    forbiddenSet_in=parseList(property.getTerm(0));
                } else if (property.getName().equals("forbidden_rd")){
                    forbiddenSet_rd=parseList(property.getTerm(0));
                } else if (property.getName().equals("forbidden_inp")){
                    forbiddenSet_inp=parseList(property.getTerm(0));
                } else if (property.getName().equals("forbidden_rdp")){
                    forbiddenSet_rdp=parseList(property.getTerm(0));
                } else if (property.getName().equals("forbidden_out_sync")){
                    forbiddenSet_outSync=parseList(property.getTerm(0));
                } else
                    //
                    // time constraints
                    //
                    if (property.getName().equals("expire_date")){
                        expiringDateMillis=parseDateMillis(property);
                        dateValidityPresent=true;
                } else if (property.getName().equals("time_extension")){
                    timeExtension=((alice.tuprolog.Number)property.getTerm(0)).longValue();
                    if (timeExtension>0){
                        timeExtensionPresent=true;
                        startTime=System.currentTimeMillis();
                    }
                } else {
                    System.out.println("Generic-Context: Unknown context property: "+property);
                }
            }
        } catch (Exception ex){
            throw new InvalidContextDescriptionException();
        }
    }
    
    public boolean isTemporalConstraintValid(){
        if (timeExtensionPresent){
            return System.currentTimeMillis()-startTime<timeExtension;
        } else if (dateValidityPresent){
            return System.currentTimeMillis()<expiringDateMillis;
        } else {
            return true;
        }
    }
    
    public boolean isAllowed_out(LogicTuple t){
        if (allowedSet_out!=null){
            if (!checkPresence(t,allowedSet_out)){
                return false;
            }
        } else if (forbiddenSet_out!=null){
            if (checkPresence(t,forbiddenSet_out)){
                return false;
            }
        }
        return true;
    }
    
    public boolean isAllowed_out_sync(LogicTuple t){
        if (allowedSet_outSync!=null){
            if (!checkPresence(t,allowedSet_outSync)){
                return false;
            }
        } else if (forbiddenSet_outSync!=null){
            if (checkPresence(t,forbiddenSet_outSync)){
                return false;
            }
        }
        return true;
    }
    
    public boolean isAllowed_in(LogicTuple t){
        if (allowedSet_in!=null){
            if (!checkPresence(t,allowedSet_in)){
                return false;
            }
        } else if (forbiddenSet_in!=null){
            if (checkPresence(t,forbiddenSet_in)){
                return false;
            }
        }
        return true;
    }
    
    public boolean isAllowed_rd(LogicTuple t){
        if (allowedSet_rd!=null){
            if (!checkPresence(t,allowedSet_rd)){
                return false;
            }
        } else if (forbiddenSet_rd!=null){
            if (checkPresence(t,forbiddenSet_rd)){
                return false;
            }
        }
        return true;
    }
    
    public boolean isAllowed_inp(LogicTuple t){
        if (allowedSet_inp!=null){
            if (!checkPresence(t,allowedSet_inp)){
                return false;
            }
        } else if (forbiddenSet_inp!=null){
            if (checkPresence(t,forbiddenSet_inp)){
                return false;
            }
        }
        return true;
    }
    
    public boolean isAllowed_rdp(LogicTuple t){
        if (allowedSet_rdp!=null){
            if (!checkPresence(t,allowedSet_rdp)){
                return false;
            }
        } else if (forbiddenSet_rdp!=null){
            if (checkPresence(t,forbiddenSet_rdp)){
                return false;
            }
        }
        return true;
    }
         
    private long parseDateMillis(Struct t) throws Exception {
        int year=((alice.tuprolog.Number)t.getTerm(0)).intValue();
        int month=((alice.tuprolog.Number)t.getTerm(1)).intValue();
        int day=((alice.tuprolog.Number)t.getTerm(2)).intValue();
        int hour=((alice.tuprolog.Number)t.getTerm(3)).intValue();
        int min=((alice.tuprolog.Number)t.getTerm(4)).intValue();
        java.util.Date now=new java.util.Date();
        java.util.Calendar future=new java.util.GregorianCalendar();
        future.set(year,month,day,hour,min);
        long ms=future.get(java.util.Calendar.MILLISECOND);
        if (ms>now.getTime()){
            return ms;
        } else {
            throw new InvalidContextDescriptionException();
        }
    }
    
    private LogicTuple[] parseList(Term t) throws Exception {
        java.util.ArrayList array=new java.util.ArrayList();
        Struct st=(Struct)t;
        java.util.Iterator it=st.listIterator();
        while (it.hasNext()){
            Term tu=(Term)it.next();
            array.add(new LogicTuple(tu));
        }
        LogicTuple[] tuples=new LogicTuple[array.size()];
        for (int i=0; i<tuples.length; i++){
            tuples[i]=(LogicTuple)array.get(i);
        }
        return tuples;
    }
    
    
    private boolean checkPresence(LogicTuple t, LogicTuple[] set){
        for (int i=0; i<set.length; i++){
            if (t.match(set[i])){
                return true;
            }
        }
        return false;
    }
    
}
