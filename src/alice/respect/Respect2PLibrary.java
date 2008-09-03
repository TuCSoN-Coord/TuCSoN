/*
 * ReSpecT Framework - aliCE team at deis.unibo.it
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
package alice.respect;
import  alice.tuprolog.*;
import alice.logictuple.*;

/**
 * TuProlog library to be loaded in tuProlog
 * engines to exploit ReSpecT framework.
 * 
 * The library provides the basic ReSpecT primitives 
 * as builtin predicates.
 * 
 */
public class Respect2PLibrary extends alice.tuprolog.Library {

	
    public Respect2PLibrary() throws Exception {
    	//System.out.println("ReSpecT2PLibrary loaded.");
    }

    public boolean out_2(Term tid, Term tuple){
        try {
        	ITupleCentre tc = RSPFramework.getTC(tid.toString());
            tc.out(new LogicTuple(tuple));
            return true;
        } catch(Exception x) {
            //x.printStackTrace();
            return false;
        }
    }

	public boolean in_2(Term tid, Term tuple){
		try {
			ITupleCentre tc = RSPFramework.getTC(tid.toString());
			tc.in(new LogicTuple(tuple));
			return true;
		} catch(Exception x) {
			//x.printStackTrace();
			return false;
		}
	}

	public boolean inp_2(Term tid, Term tuple){
		try {
			ITupleCentre tc = RSPFramework.getTC(tid.toString());
			LogicTuple t = tc.inp(new LogicTuple(tuple));
			if (t!=null){
				return true;
			} else {
				return false;
			}
		} catch(Exception x) {
			//x.printStackTrace();
			return false;
		}
	}
	
	public boolean rd_2(Term tid, Term tuple){
		try {
			ITupleCentre tc = RSPFramework.getTC(tid.toString());
			tc.rd(new LogicTuple(tuple));
			return true;
		} catch(Exception x) {
			//x.printStackTrace();
			return false;
		}
	}

	public boolean rdp_2(Term tid, Term tuple){
		try {
			ITupleCentre tc = RSPFramework.getTC(tid.toString());
			LogicTuple t = tc.rdp(new LogicTuple(tuple));
			if (t!=null){
				return true;
			} else {
				return false;
			}
		} catch(Exception x) {
			//x.printStackTrace();
			return false;
		}
	}
	
	public boolean set_spec_2(Term tid, Struct spec){
		try {
			ITupleCentre tc = RSPFramework.getTC(tid.toString());
			tc.setSpec(spec.getName());
			return true;
		} catch(Exception x) {
			//x.printStackTrace();
			return false;
		}
	}

	public boolean get_spec_2(Term tid, Term spec){
		try {
			ITupleCentre tc = RSPFramework.getTC(tid.toString());
			return unify(spec,new Struct(tc.getSpec()));
		} catch(Exception x) {
			//x.printStackTrace();
			return false;
		}
	}
	
	public String getTheory(){
		return
			":- op(  600, xfx,  '?'). \n"+
			"TC ? out(T) :- out(TC,T). \n"+
			"TC ? in(T)  :- in(TC,T). \n"+
			"TC ? inp(T) :- inp(TC,T). \n"+
			"TC ? rd(T)  :- rd(TC,T). \n"+
			"TC ? rdp(T) :- rdp(TC,T). \n"+
			"TC ? set_spec(T)  :- set_spec(TC,T). \n"+
			"TC ? get_spec(T) :- get_spec(TC,T). \n"+
			"out(T):-out(default,T). \n"+
			"in(T):-in(default,T). \n"+
			"rd(T):-rd(default,T). \n"+
			"inp(T):-inp(default,T). \n"+
			"rdp(T):-rdp(default,T). \n"+
			"set_spec(T):-set_spec(default,T). \n"+
			"get_spec(T):-get_spec(default,T). \n";
	}


 }