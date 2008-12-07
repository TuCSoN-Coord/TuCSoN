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
package alice.tucson.service;
import alice.respect.api.AgentId;
import alice.respect.api.TupleCentreId;
import alice.tucson.api.*;
import alice.tucson.api.exception.LanguageNotFoundException;
import alice.tucson.api.exception.OperationNotAllowedException;
import alice.tucson.api.exception.TucsonException;
import alice.tucson.api.exception.TucsonInvalidAgentIdException;
import alice.tucson.api.exception.TucsonInvalidTupleCentreIdException;
import alice.tucson.api.exception.UnreachableNodeException;
import alice.tucson.common.TucsonMsgReply;
import alice.tucson.common.TucsonMsgRequest;
import alice.tucson.common.TucsonProtocol;
import alice.tucson.common.TucsonProtocolTCP;
import alice.tucson.types.TucsonAgentId;
import alice.tucson.types.TucsonOpType;
import alice.tucson.types.TucsonTupleCentreId;
import alice.logictuple.*;


import java.util.*;
import java.io.*;

/**
 * Inter tuple centre context stub
 *
 */
public class InterTupleCentreContextStub implements InterTupleCentreContext{

	private TucsonTupleCentreId aid;
    private ContextDescription profile;

    private LinkedList<ContextEvent> events;
    private HashMap<String, ControllerSession> controllerSessions;
    
    private int opId;
	

	public InterTupleCentreContextStub(Object id, Object language) {
		//TucsonTCContainer.setAgentMode();
    	//TucsonTCContainer.useLanguage((String)language);
		if(id.getClass().getName().equals("alice.tucson.types.TucsonTupleCentreId")){
    		this.aid = (TucsonTupleCentreId) id;
    	} else {
    		try {
    			this.aid = new TucsonTupleCentreId(id);
			} catch (TucsonInvalidTupleCentreIdException e) {
				e.printStackTrace();
			}
    	}
    	
        profile = new ContextDescription();
        opId = 0;
        events = new LinkedList<ContextEvent>();        
        controllerSessions = new HashMap<String, ControllerSession>();

	}

	public synchronized TucsonOpId doOperation(Object tid, int type, Object t, String language) 
	throws OperationNotAllowedException, UnreachableNodeException, LanguageNotFoundException {
		TucsonTCContainer.useLanguage(language);
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
		//First phase: syntax check before going on remote node
		if (!tcid.checkSyntax() || !aid.checkSyntax()){
			throw new OperationNotAllowedException();
		}

		int nTry = 1;
		do {
			//Second phase: negotiation (method getSession)
			TucsonProtocol session = getSession(tcid, language);

			ObjectOutputStream outStream = session.getOutputStream();
			try {
				opId++;
				TucsonMsgRequest msg = new TucsonMsgRequest(opId,type,tcid.translateSyntax(session.getLanguageInfo()), (LogicTuple)t);
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

	private TucsonProtocol getSession(TucsonTupleCentreId tid, String language)
	throws OperationNotAllowedException, UnreachableNodeException, LanguageNotFoundException {
		String node = alice.util.Tools.removeApices(tid.toString());
		ControllerSession tc = controllerSessions.get(node);
		if (tc != null) {
			return tc.getSession();
		} else {
			String snode = tid.toString();

			//TODO da sistemare
			//profile.setProperty("node", snode );  //TODO controllare uso in remoto
			profile.setProperty("agent-identity",aid.toString());
			profile.setProperty("agent-role","user");

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
					ObjectInputStream din = new ObjectInputStream(dialog.getInputStream());
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

	
	class Controller extends Thread {

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





