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
import alice.tucson.api.*;

import java.util.*;
import java.io.*;

import alice.tucson.common.*;

/**
 *  Implementation of a generic agent coordination context
 */
public class AgentContextStub implements TucsonContext {

	private static final int  TYPE_OUT = 0;
    private static final int  TYPE_IN = 1;
    private static final int  TYPE_RD = 2;
    private static final int  TYPE_INP = 3;
    private static final int  TYPE_RDP = 4;
    private static final int  TYPE_SETSPEC = 5;
    private static final int  TYPE_GETSPEC = 6;
    private static final int  TYPE_EXIT = 7;

    private TupleCentreId defaultTid = new TupleCentreId();
    private String localNode = "localhost";

    private AgentId aid;
    private ContextDescription profile;

    private LinkedList events;
    private HashMap controllerSessions;
    
    private int opId;
    
    public AgentContextStub(AgentId aid){
        profile = new ContextDescription();
        profile.setProperty("agent-identity",aid.toString());
        profile.setProperty("agent-role","user");
        opId = 0;
        events = new LinkedList();        
        controllerSessions = new HashMap();
    }

    
    public synchronized TucsonOpId out(TupleCentreId tid, LogicTuple t) throws OperationNotAllowedException, UnreachableNodeException {
        return doOperation(tid,TYPE_OUT,t);
    }

    public synchronized TucsonOpId in(TupleCentreId tid, LogicTuple t) throws OperationNotAllowedException, UnreachableNodeException {
        return doOperation(tid,TYPE_IN,t);
    }

    public synchronized TucsonOpId rd(TupleCentreId tid, LogicTuple t) throws OperationNotAllowedException, UnreachableNodeException {
        return doOperation(tid,TYPE_RD,t);
    }
    
    public synchronized TucsonOpId inp(TupleCentreId tid, LogicTuple t) throws OperationNotAllowedException, UnreachableNodeException {
        return doOperation(tid,TYPE_INP,t);
    }
 
    public synchronized TucsonOpId rdp(TupleCentreId tid, LogicTuple t) throws OperationNotAllowedException, UnreachableNodeException {
        return doOperation(tid,TYPE_RDP,t);
    }
    
    public synchronized TucsonOpId setSpec(TupleCentreId tid, String t) throws OperationNotAllowedException, UnreachableNodeException {
        try {
        		LogicTuple spec = new LogicTuple("spec",new Value(t));
        	    return doOperation(tid,TYPE_SETSPEC,spec);
        } catch (Exception ex){
        		ex.printStackTrace();
        		return null;
        }
    }
    
    public synchronized TucsonOpId getSpec(TupleCentreId tid) throws OperationNotAllowedException, UnreachableNodeException {
        LogicTuple spec = new LogicTuple("spec");
    		return doOperation(tid,TYPE_GETSPEC,spec);
    }
    
    public synchronized TucsonOpId doOperation(TupleCentreId tid, int type, LogicTuple t) throws OperationNotAllowedException, UnreachableNodeException {
        int nTry = 1;
        do {      		
        	   TucsonProtocol session = getSession(tid);
            DataOutputStream outStream = session.getOutputStream();
            try {
            		opId++;            		
            		TucsonMsgRequest msg = new TucsonMsgRequest(opId,type,tid,t);
                 TucsonMsgRequest.write(outStream,msg);
                 outStream.flush();
                // System.out.println("Request done: "+type+" "+t+" "+opId);
                return new TucsonOpId(opId);
            } catch (IOException ex) {
            		ex.printStackTrace();
            }
            nTry++;
        } while (nTry < 3);
        throw new UnreachableNodeException();
    }
    
    public synchronized void exit() throws OperationNotAllowedException {
        TupleCentreId tid = new TupleCentreId();
    		Iterator it = controllerSessions.values().iterator();
        while (it.hasNext()) {
            try {
                ControllerSession cs = (ControllerSession) it.next();
                TucsonProtocol info = cs.getSession();
                Controller contr = cs.getController();
                contr.setStop();
                DataOutputStream outStream = info.getOutputStream();
                opId++;
                TucsonMsgRequest exit = new TucsonMsgRequest(opId,TYPE_EXIT,tid,new LogicTuple("exit"));
                TucsonMsgRequest.write(outStream,exit);
                outStream.flush();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    private ContextEvent findEvent(TucsonOpId id){
    		Iterator it = events.iterator();
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
    				long dt = System.currentTimeMillis() - startTime;
    				ContextEvent ev = findEvent(id);
    				while (ev==null && dt < timeout){
    					//log("waiting for "+id.getId());
    					events.wait(timeout-dt);
        				ev = findEvent(id);
        				dt = System.currentTimeMillis() - startTime;
    				}
    				return ev;
    			}
    		} catch (Exception ex){
    			return null;
    		}
    }

    public ContextEvent waitForCompletion(TucsonOpId id) {
		try {			
			synchronized (events){
				ContextEvent ev = findEvent(id);
				while (ev==null){
					//log("waiting for "+id.getId());
					events.wait();
					ev = findEvent(id);
				}
				return ev;
			}
		} catch (Exception ex){
			return null;
		}
    }
    
    
    private TucsonProtocol getSession(TupleCentreId tid)
            throws OperationNotAllowedException, UnreachableNodeException {
        String node = alice.util.Tools.removeApices(tid.getNode());
        ControllerSession tc = (ControllerSession)controllerSessions.get(node);
        if (tc != null) {
            return tc.getSession();
        } else {
			String snode = tid.getNode();
		    //profile.setProperty("node", snode );

            try {
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
                	   DataInputStream din = new DataInputStream(dialog.getInputStream());
                	   Controller contr = new Controller(din);
                	   ControllerSession cs = new ControllerSession(contr,dialog);
                	   controllerSessions.put(node, cs);
                	   contr.start();
                    return dialog;
                }
            } catch (Exception ex){
                throw new UnreachableNodeException();
            }
        }
        throw new OperationNotAllowedException();
    }

    private void postEvent(ContextEvent ev){
    		synchronized (events){
    			events.addLast(ev);
    			//log("notified ev "+ev.getTuple()+" - op id: "+ev.getOpId().getId());
    			events.notifyAll();
    		}
    }

    private void log(String msg){
    		System.out.println("[ASYNC STUB] "+msg);
    }
    
    class Controller extends Thread {
    	
    		private boolean stop;
    		private DataInputStream in;
    		
    		Controller(DataInputStream in){
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
    						if (type==TYPE_IN || 
    						    type==TYPE_RD ||
    						    type==TYPE_INP ||
    						    type==TYPE_RDP){
    							
    							boolean succeeded = msg.isSuccess();
    							if (succeeded){
    								LogicTuple tupleReq = msg.getTupleRequested();
    								LogicTuple tupleRes = msg.getTupleResult();
    								LogicTuple res = unify(tupleReq,tupleRes);
    								ev = new ContextEvent(new TucsonOpId(msg.getId()),ok,true,res);
    							} else {
    								ev = new ContextEvent(new TucsonOpId(msg.getId()),ok,false);
    							}
    						} else if (type==TYPE_GETSPEC) {
        						LogicTuple tupleRes = msg.getTupleResult();
    							String spec = tupleRes.getArg(0).getName();
    							ev = new ContextEvent(new TucsonOpId(msg.getId()),ok,msg.isSuccess(),spec);
    						} else if (type==TYPE_SETSPEC ||
    									type==TYPE_OUT){
    							ev = new ContextEvent(new TucsonOpId(msg.getId()),ok,msg.isSuccess());
    						} else if (type==TYPE_EXIT){
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
    		
    	     LogicTuple unify(alice.tuplecentre.TupleTemplate template, alice.tuplecentre.Tuple tuple) {
    	        boolean res = template.propagate(tuple);
    	        if (res) {
    	            return (LogicTuple) template;
    	        } else {
    	            return null;
    	        }
    	    }
    }
    
    class ControllerSession {
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





