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
package alice.tucson.api;

import alice.logictuple.*;
import alice.tucson.api.exception.LanguageNotFoundException;
import alice.tucson.api.exception.OperationNotAllowedException;
import alice.tucson.api.exception.TucsonInvalidAgentIdException;
import alice.tucson.api.exception.TucsonInvalidSpecificationException;
import alice.tucson.api.exception.TucsonInvalidTupleCentreIdException;
import alice.tucson.api.exception.UnreachableNodeException;

import java.util.*;
import java.io.*;

import alice.tucson.common.*;
import alice.tucson.service.LanguageInfo;
import alice.tucson.service.TucsonAgentLanguageManager;
import alice.tucson.types.TucsonAgentId;
import alice.tucson.types.TucsonOpType;
import alice.tucson.types.TucsonTupleCentreId;

/**
 *  Implementation of a generic agent coordination context
 */
public class AgentContextStub implements TucsonContext {

    private TucsonAgentId aid;
    private ContextDescription profile;

    private LinkedList<ContextEvent> events;
    private HashMap<String, ControllerSession> controllerSessions;
    
    private int opId;
        
    public AgentContextStub(Object aid, Object language){
    	TucsonAgentLanguageManager.useLanguage((String)language);
    	if (aid.getClass().getName().equals("alice.tucson.types.TucsonAgentId")){
    		this.aid = (TucsonAgentId)aid;
    	} else {
    		try {
				this.aid = new TucsonAgentId(aid);
			} catch (TucsonInvalidAgentIdException e) {
			}
    	}
    	
        profile = new ContextDescription();
        opId = 0;
        events = new LinkedList<ContextEvent>();        
        controllerSessions = new HashMap<String, ControllerSession>();
    }

    
    public synchronized TucsonOpId out(Object tid, Object t) throws OperationNotAllowedException, UnreachableNodeException, LanguageNotFoundException {
        return doOperation(tid,TucsonOpType.OPTYPE_OUT,t,TucsonAgentLanguageManager.getCurLanguage().getName());
    }

    public synchronized TucsonOpId in(Object tid, Object t) throws OperationNotAllowedException, UnreachableNodeException, LanguageNotFoundException {
        return doOperation(tid,TucsonOpType.OPTYPE_IN,t,TucsonAgentLanguageManager.getCurLanguage().getName());
    }

    public synchronized TucsonOpId rd(Object tid, Object t) throws OperationNotAllowedException, UnreachableNodeException, LanguageNotFoundException {
        return doOperation(tid,TucsonOpType.OPTYPE_RD,t,TucsonAgentLanguageManager.getCurLanguage().getName());
    }
    
    public synchronized TucsonOpId inp(Object tid, Object t) throws OperationNotAllowedException, UnreachableNodeException, LanguageNotFoundException {
        return doOperation(tid,TucsonOpType.OPTYPE_INP,t,TucsonAgentLanguageManager.getCurLanguage().getName());
    }
 
    public synchronized TucsonOpId rdp(Object tid, Object t) throws OperationNotAllowedException, UnreachableNodeException, LanguageNotFoundException {
        return doOperation(tid,TucsonOpType.OPTYPE_RDP,t,TucsonAgentLanguageManager.getCurLanguage().getName());
    }
    
    public synchronized TucsonOpId in_s(Object tid, Object tuple)
    throws OperationNotAllowedException, UnreachableNodeException, LanguageNotFoundException {
    	return doOperation(tid,TucsonOpType.OPTYPE_IN_S,tuple,TucsonAgentLanguageManager.getCurLanguage().getName());
    }


    public synchronized TucsonOpId inp_s(Object tid, Object tuple)
    throws OperationNotAllowedException, UnreachableNodeException, LanguageNotFoundException {
    	 return doOperation(tid,TucsonOpType.OPTYPE_INP_S,tuple,TucsonAgentLanguageManager.getCurLanguage().getName());
    }


    public synchronized TucsonOpId out_s(Object tid, Object tuple)
    throws OperationNotAllowedException, UnreachableNodeException, LanguageNotFoundException {
    	 return doOperation(tid,TucsonOpType.OPTYPE_OUT_S,tuple,TucsonAgentLanguageManager.getCurLanguage().getName());
    }


    public synchronized TucsonOpId rd_s(Object tid, Object tuple)
    throws OperationNotAllowedException, UnreachableNodeException, LanguageNotFoundException {
    	return doOperation(tid,TucsonOpType.OPTYPE_RD_S,tuple,TucsonAgentLanguageManager.getCurLanguage().getName());
    }


    public synchronized TucsonOpId rdp_s(Object tid, Object tuple)
    throws OperationNotAllowedException, UnreachableNodeException, LanguageNotFoundException {
    	return doOperation(tid,TucsonOpType.OPTYPE_RDP_S,tuple,TucsonAgentLanguageManager.getCurLanguage().getName());
    }

    public synchronized TucsonOpId setSpec(Object tid, Object t) throws OperationNotAllowedException, UnreachableNodeException, LanguageNotFoundException {
        return doOperation(tid,TucsonOpType.OPTYPE_SET_SPEC,t,TucsonAgentLanguageManager.getCurLanguage().getName());
    }
    
    public synchronized TucsonOpId getSpec(Object tid) throws OperationNotAllowedException, UnreachableNodeException, LanguageNotFoundException {
       return doOperation(tid,TucsonOpType.OPTYPE_GET_SPEC,new Object(), TucsonAgentLanguageManager.getCurLanguage().getName());
    }
    
    public synchronized TucsonOpId doOperation(Object tid, int type, Object t, String language) throws OperationNotAllowedException, UnreachableNodeException, LanguageNotFoundException {
    	TucsonTupleCentreId tcid = null;
    	if(tid.getClass().getName().equals("alice.tucson.types.TucsonTupleCentreId")){
    		tcid = (TucsonTupleCentreId) tid;
    	} else {
    		try {
				tcid = new TucsonTupleCentreId(tid);
			} catch (TucsonInvalidTupleCentreIdException e) {
				throw new OperationNotAllowedException();
			}
    	}
    	TucsonAgentLanguageManager.useLanguage(language);
        //First phase: syntax check before going on remote node
    	log("Syntax check");
    	if (!tcid.checkSyntax() || !aid.checkSyntax()){
    		throw new OperationNotAllowedException();
    	}
    	
    	int nTry = 1;
        do {
        	//Second phase: negotiation (method getSession)
        	log("Negotiation with "+tcid.toString());
        	TucsonProtocol session = getSession(tcid, language);        	
        	ObjectOutputStream outStream = session.getOutputStream();
            try {
            	opId++;
            	log("Remote call & eventually translation");
            	TucsonMsgRequest msg = new TucsonMsgRequest(opId,type,tcid.translateSyntax(session.getLanguageInfo()), (LogicTuple)t);
            	TucsonMsgRequest.write(outStream,msg);
            	outStream.flush();
                //log("Request done: "+type+" "+t+" "+opId);
            	log("Operation executed succesfully");
                return new TucsonOpId(opId);
            } catch (IOException ex) {
            		ex.printStackTrace();
            }
            nTry++;
        } while (nTry < 3);
        throw new UnreachableNodeException();
    }
    
    public synchronized void exit() throws OperationNotAllowedException {
        Object tid = new Object();
        Object t = new Object();
    	Iterator<ControllerSession> it = controllerSessions.values().iterator();
        while (it.hasNext()) {
            try {
                ControllerSession cs = (ControllerSession) it.next();
                TucsonProtocol info = cs.getSession();
                Controller contr = cs.getController();
                contr.setStop();
                ObjectOutputStream outStream = info.getOutputStream();
                opId++;
                TucsonMsgRequest exit = new TucsonMsgRequest(opId,TucsonOpType.OPTYPE_EXIT,new TucsonTupleCentreId(tid), (LogicTuple)t);
                TucsonMsgRequest.write(outStream,exit);
                outStream.flush();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public HashMap<String, LanguageInfo> getInfo(){
    	return TucsonAgentLanguageManager.getAvailableLanguages();
    }


    private ContextEvent findEvent(TucsonOpId id){
    		Iterator<ContextEvent> it = events.iterator();
    		while (it.hasNext()){
    			ContextEvent ev = (ContextEvent)it.next(); 
    			if (ev.getOpId().equals(id)){
    				it.remove();
    				return ev; 
    			} 
    		}
    		return null;
    }
    
    public ContextEvent waitForCompletion(TucsonOpId id, int timeout) {
    		try {
    			long startTime = System.currentTimeMillis();
    			synchronized (events){
    				log("Waiting for result...");
    				long dt = System.currentTimeMillis() - startTime;
    				ContextEvent ev = findEvent(id);
    				while (ev==null && dt < timeout){
    					//log("waiting for "+id.getId());
    					events.wait(timeout-dt);
        				ev = findEvent(id);
        				dt = System.currentTimeMillis() - startTime;
    				}
    				log("Result received...");
    				return ev;
    			}
    		} catch (Exception ex){
    			return null;
    		}
    }

    public ContextEvent waitForCompletion(TucsonOpId id) {
		try {			
			synchronized (events){
				log("Waiting for result...");
				ContextEvent ev = findEvent(id);
				while (ev==null){
					//log("waiting for "+id.getId());
					events.wait();
					ev = findEvent(id);
				}
				log("Result received...");
				return ev;
			}
		} catch (Exception ex){
			return null;
		}
    }
    
    
    private TucsonProtocol getSession(TucsonTupleCentreId tid, String language)
            throws OperationNotAllowedException, UnreachableNodeException, LanguageNotFoundException {
        String node = alice.util.Tools.removeApices(tid.getNode());
        //Discriminate between remote node 
        ControllerSession tc = controllerSessions.get(node);
        if (tc != null) {
            return tc.getSession();
        } else {
        	try {
        		profile.setProperty("agent-identity",aid.toString());
        		profile.setProperty("agent-role","user");

        		int idx = node.indexOf(':');
        		int port = 20504;				
        		try {
        			if (idx!=-1){
        				port = Integer.parseInt(node.substring(idx));
        				node = node.substring(0,idx-1);
        			}
        		} catch (Exception ex){
        			throw new UnreachableNodeException();
        		}
        		TucsonProtocol dialog = new TucsonProtocolTCP(node, port);
        		dialog.sendEnterRequest(profile);
        		dialog.receiveEnterRequestAnswer();
        		if (dialog.isEnterRequestAccepted()) {
        			ObjectInputStream din = dialog.getInputStream();
        			Controller contr = new Controller(din);
        			ControllerSession cs = new ControllerSession(contr,dialog);
        			controllerSessions.put(node, cs);
        			contr.start();
        			return dialog;
        		}
        	} catch (UnreachableNodeException ex){
        		throw new UnreachableNodeException();
        	} catch (Exception ex2){
        		 throw new OperationNotAllowedException();
        	}
        }
		return null;
    }

    private void postEvent(ContextEvent ev){
    		synchronized (events){
    			events.addLast(ev);
    			//log("notified ev "+ev.getTuple()+" - op id: "+ev.getOpId().getId());
    			events.notifyAll();
    		}
    }

    private void log(String msg){
    		System.out.println("[ACC STUB] "+msg);
    }
    

	public TucsonOpId getSpec(Object tid, Object language)
			throws OperationNotAllowedException, UnreachableNodeException, LanguageNotFoundException {
		return doOperation(tid,TucsonOpType.OPTYPE_GET_SPEC,new Object(),(String)language);
	}


	public TucsonOpId in(Object tid, Object tuple, Object language)
			throws OperationNotAllowedException, UnreachableNodeException, LanguageNotFoundException {
		return doOperation(tid,TucsonOpType.OPTYPE_IN,tuple,(String)language);
	}


	public TucsonOpId in_s(Object tid, Object tuple, Object language)
			throws OperationNotAllowedException, UnreachableNodeException, LanguageNotFoundException {
		return doOperation(tid,TucsonOpType.OPTYPE_IN_S,tuple,(String)language);
	}


	public TucsonOpId inp(Object tid, Object tuple, Object language)
			throws OperationNotAllowedException, UnreachableNodeException, LanguageNotFoundException {
		return doOperation(tid,TucsonOpType.OPTYPE_INP,tuple,(String)language);
	}


	public TucsonOpId inp_s(Object tid, Object tuple, Object language)
			throws OperationNotAllowedException, UnreachableNodeException, LanguageNotFoundException {
		return doOperation(tid,TucsonOpType.OPTYPE_INP_S,tuple,(String)language);
	}


	public TucsonOpId out(Object tid, Object tuple, Object language)
			throws OperationNotAllowedException, UnreachableNodeException, LanguageNotFoundException {
		return doOperation(tid,TucsonOpType.OPTYPE_OUT,tuple,(String)language);
	}


	public TucsonOpId out_s(Object tid, Object tuple, Object language)
			throws OperationNotAllowedException, UnreachableNodeException, LanguageNotFoundException {
		return doOperation(tid,TucsonOpType.OPTYPE_OUT_S,tuple,(String)language);
	}


	public TucsonOpId rd(Object tid, Object tuple, Object language)
			throws OperationNotAllowedException, UnreachableNodeException, LanguageNotFoundException {
		return doOperation(tid,TucsonOpType.OPTYPE_RD,tuple,(String)language);
	}


	public TucsonOpId rd_s(Object tid, Object tuple, Object language)
			throws OperationNotAllowedException, UnreachableNodeException, LanguageNotFoundException {
		return doOperation(tid,TucsonOpType.OPTYPE_RD_S,tuple,(String)language);
	}


	public TucsonOpId rdp(Object tid, Object tuple, Object language)
			throws OperationNotAllowedException, UnreachableNodeException, LanguageNotFoundException {
		return doOperation(tid,TucsonOpType.OPTYPE_RDP,tuple,(String)language);
	}


	public TucsonOpId rdp_s(Object tid, Object tuple, Object language)
			throws OperationNotAllowedException, UnreachableNodeException, LanguageNotFoundException {
		return doOperation(tid,TucsonOpType.OPTYPE_RDP_S,tuple,(String)language);
	}


	public TucsonOpId setSpec(Object tid, Object spec, Object language)
			throws OperationNotAllowedException, UnreachableNodeException,
			TucsonInvalidSpecificationException, LanguageNotFoundException {
		return doOperation(tid,TucsonOpType.OPTYPE_SET_SPEC,spec,(String)language);
	}

    
    public class Controller extends Thread {
    	
    		private boolean stop;
    		private ObjectInputStream in;
    		
    		Controller(ObjectInputStream in){
    			this.in = in;
    			stop = false;
    		}
    		
    		public void run(){
    			//log("Controller started.");
    			ContextEvent ev = null;
    			while (!isStopped()){
    				try {
    					TucsonMsgReply msg = TucsonMsgReply.read(in);
    					//log("received msg "+msg);
    					boolean ok = msg.isAllowed();
    					if (ok){
    						int type = msg.getType();
    						if (type==TucsonOpType.OPTYPE_IN || 
    						    type==TucsonOpType.OPTYPE_RD ||
    						    type==TucsonOpType.OPTYPE_INP ||
    						    type==TucsonOpType.OPTYPE_RDP ||
    						    type==TucsonOpType.OPTYPE_IN_S ||
    						    type==TucsonOpType.OPTYPE_RD_S ||
    						    type==TucsonOpType.OPTYPE_INP_S ||
    						    type==TucsonOpType.OPTYPE_RDP_S){
    							
    							boolean succeeded = msg.isSuccess();
    							if (succeeded){
    								LogicTuple tupleReq = msg.getTupleRequested();
    								LogicTuple tupleRes = msg.getTupleResult();
    								LogicTuple res = unify(tupleReq,tupleRes);
    								ev = new ContextEvent(new TucsonOpId(msg.getId()),ok,true,res);
    							} else {
    								ev = new ContextEvent(new TucsonOpId(msg.getId()),ok,false);
    							}
    						} else if (type==TucsonOpType.OPTYPE_GET_SPEC) {
        						LogicTuple tupleRes = msg.getTupleResult();
    							String spec = tupleRes.getArg(0).getName();
    							ev = new ContextEvent(new TucsonOpId(msg.getId()),ok,msg.isSuccess(),spec);
    						} else if (type==TucsonOpType.OPTYPE_SET_SPEC ||
    									type==TucsonOpType.OPTYPE_OUT || 
    									type==TucsonOpType.OPTYPE_OUT_S){
    							ev = new ContextEvent(new TucsonOpId(msg.getId()),ok,msg.isSuccess());
    						} else if (type==TucsonOpType.OPTYPE_EXIT){
    							setStop();
    							break;
    						}
    					} else {
    						ev = new ContextEvent(new TucsonOpId(msg.getId()),false,false);
    					}
    					//log("posting event "+ev.getOpId().getId());	
    					postEvent(ev);
    				} catch (Exception ex){
    					ex.printStackTrace();
    					setStop();
    				}
    			}
    		}
    		
    		synchronized boolean isStopped(){
    			return stop;
    		}
    		
    		synchronized void setStop(){
    			stop = true;
    		}
    		
    	     LogicTuple unify(alice.tuplecentre.api.TupleTemplate template, alice.tuplecentre.api.Tuple tuple) {
    	        boolean res = template.propagate(tuple);
    	        if (res) {
    	            return (LogicTuple) template;
    	        } else {
    	            return null;
    	        }
    	    }
    }
    
    public class ControllerSession {
    		private Controller controller;
    		private TucsonProtocol session;
    		
    		ControllerSession(Controller c, TucsonProtocol s){
    			controller = c;
    			session = s;
    		}
    		
    		public Controller getController(){
    			return controller;
    		}
    		
    		public TucsonProtocol getSession(){
    			return session;
    		}
    }

}




