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
import alice.logictuple.*;
import alice.tuplecentre.*;

/**
 *
 */
class LocalContext implements ITupleCentre, OutputEventListener {

    /** user id */
    private AgentId  id;

    /** tuple centre to access */
    private RespectVM core;

    /** event notified by the tuple centre */
    private OutputEvent  evReceived;

	private Object semaphore;
	
    public LocalContext(AgentId id_, RespectVM core_){
        core=core_;
        id=id_;
        semaphore = new Object();
    }

    public AgentId getAgentId(){
        return id;
    }
    
    public TupleCentreId getId(){
    	return (TupleCentreId)core.getId();
    }

    public synchronized void out(LogicTuple t) throws InvalidLogicTupleException {
        if (t==null){
            throw new InvalidLogicTupleException();
        }
        //spy(id+" out "+t);
        core.out(t,id,null);
    }

    public synchronized LogicTuple in(LogicTuple t) throws InvalidLogicTupleException {
        if (t==null){
            throw new InvalidLogicTupleException();
        }
        //spy(id+" in "+t);
		evReceived=null;
        core.in(t,id,this);
        //spy(id+" in processed "+t);
		while (evReceived==null){
			try {
				wait();
			} catch (Exception e){
			}
		}
        //spy(id+" in return "+evReceived.tuple);
        return unify(t,((OutputEvent_in)evReceived).getTuple());
    }

    public synchronized LogicTuple rd(LogicTuple t) throws InvalidLogicTupleException {
        if (t==null){
            throw new InvalidLogicTupleException();
        }
        //spy(id+" rd "+t);
		evReceived=null;
        core.rd(t,id,this);
        //spy(id+" rd processed "+t);
		while (evReceived==null){
			try {
				wait();
			} catch (Exception e){
			}
		}
        //spy(id+" rd return "+evReceived.tuple);
        return unify(t,((OutputEvent_rd)evReceived).getTuple());
    }

    public synchronized LogicTuple inp(LogicTuple t) throws InvalidLogicTupleException {
        if (t==null){
            throw new InvalidLogicTupleException();
        }
		evReceived=null;
        core.inp(t,id,this);
		while (evReceived==null){
	        try {
	            wait();
    	    } catch (Exception e){
	        }
        }
        if (evReceived.isSuccess())
            return unify(t,((OutputEvent_inp)evReceived).getTuple());
        else
            return null;
    }

    public synchronized LogicTuple rdp(LogicTuple t) throws InvalidLogicTupleException {
        if (t==null){
            throw new InvalidLogicTupleException();
        }
		evReceived=null;
        core.rdp(t,id,this);
		while (evReceived==null){
			try {
	            wait();
    	    } catch (Exception e){
        	}
		}
        if (evReceived.isSuccess())
            return unify(t,((OutputEvent_rdp)evReceived).getTuple());
        else
            return null;
    }

    public synchronized void setSpec(String spec) throws InvalidSpecificationException {
        if (spec==null){
            throw new InvalidSpecificationException();
        }
		evReceived=null;
        core.setSpec(spec,id,this);
        while (evReceived==null){
        	try {
	            wait();
    	    } catch (Exception e){
        	}
        }
        if (!evReceived.isSuccess()){
            throw new InvalidSpecificationException();
        }
    }

    public synchronized String getSpec(){
		evReceived=null;
        core.getSpec(id,this);
		while (evReceived==null){
			try {
				wait();
			} catch (Exception e){
			}
		}
        return ((OutputEvent_getSpec)evReceived).getSpec();
    }

    private LogicTuple unify(alice.tuplecentre.TupleTemplate template,alice.tuplecentre.Tuple tuple){
        boolean res=template.propagate(tuple);
        if (res){
            return (LogicTuple)template;
        } else {
            return null;
        }
    }

    public  synchronized void notify(OutputEvent ev) {
        evReceived=ev;
        try {
        	notify();
        } catch (Exception ex){
        	ex.printStackTrace();
        }
    }
    
    
    void spy(String st){
        System.out.println(st);
    }
    
    
}

