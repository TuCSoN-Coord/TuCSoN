package alice.tucson.service;

import java.util.ArrayList;
import java.util.HashMap;

import alice.logictuple.InvalidLogicTupleException;
import alice.logictuple.LogicTuple;
import alice.respect.api.AgentId;
import alice.respect.api.IBlockingContext;
import alice.respect.api.IBlockingSpecContext;
import alice.respect.api.INonBlockingContext;
import alice.respect.api.INonBlockingSpecContext;
import alice.respect.api.InstantiationNotPossibleException;
import alice.respect.api.InvalidAgentIdException;
import alice.respect.api.InvalidSpecificationException;
import alice.respect.api.OperationNotPossibleException;
import alice.respect.api.RespectSpecification;
import alice.respect.api.TupleCentreId;
import alice.tucson.api.exception.OperationNotAllowedException;
import alice.tucson.api.exception.TCInstantiationNotPossibleException;
import alice.tucson.api.exception.TucsonInvalidSpecificationException;
import alice.tucson.api.exception.TucsonTupleException;
import alice.tucson.types.TucsonAgentId;
import alice.tucson.types.TucsonOpType;
import alice.tucson.types.TucsonTupleCentreId;
import alice.tuplecentre.api.ITupleCentreOperation;
import alice.tuplecentre.core.OperationCompletionListener;

/**
 * @author lsemprini
 *
 */
public class TucsonTCContainer {
		
	public static void loadContainerLanguage(ArrayList<String> cont, HashMap<String, LanguageInfo> languages){
		//Add the container component (the language is not responsible of the component)
		for (int i= 0; i<cont.size();i++){
			LanguageInfo respectCont;
			try {
				//Calls  DynamicRespectTCContainer.getLanguage()
				respectCont = (LanguageInfo) TucsonLanguageManager.callStaticMethod(cont.get(i), "getLanguage", new Object[0]);
				respectCont.setContainerComponentName(cont.get(i));
				languages.put(respectCont.getName(), respectCont);
				log("Container "+respectCont.getName()+" loaded succesfully");
			} catch (Exception e) {
				log("Error: Unable to load "+cont.get(i));
			}		
		}

	}
	
	public static void attachTucson(){
		try {
			//Calls  DynamicRespectTCContainer.setTucson()
			TucsonLanguageManager.callStaticMethod(TucsonNodeLanguageManager.getCurLanguage().getContainerComponentName(), "setTucson", new Object[0]);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	public static boolean createTC(TucsonTupleCentreId id) throws TCInstantiationNotPossibleException{
		//Calls  DynamicRespectTCContainer.createRespectTC((TupleCentreId)id.getInternalTupleCentreId(), TucsonOpType.RESPECTQUEUE)
		try {
			return  (Boolean) TucsonLanguageManager.callStaticMethod(TucsonNodeLanguageManager.getCurLanguage().getContainerComponentName(), "createRespectTC", new Object[]{(TupleCentreId)id.getInternalTupleCentreId(), TucsonOpType.RESPECTQUEUE});
		} catch (InstantiationNotPossibleException e) {
			throw new TCInstantiationNotPossibleException();
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		return false;
	}
	
	public static ITupleCentreOperation doNonBlockingOperation(int type, TucsonAgentId aid, TucsonTupleCentreId tid, LogicTuple t, OperationCompletionListener l) throws OperationNotAllowedException, TucsonTupleException, TucsonInvalidSpecificationException{
		ITupleCentreOperation res = null;
		if (type==TucsonOpType.OPTYPE_OUT || type==TucsonOpType.OPTYPE_IN || type==TucsonOpType.OPTYPE_INP || type==TucsonOpType.OPTYPE_RD || type==TucsonOpType.OPTYPE_RDP){
			INonBlockingContext context = null;
			try {
				//Calls  DynamicRespectTCContainer.getNonBlockingContext((TupleCentreId) tid.getInternalTupleCentreId())
				context = (INonBlockingContext) TucsonLanguageManager.callStaticMethod(TucsonNodeLanguageManager.getCurLanguage().getContainerComponentName(), "getNonBlockingContext", new Object[]{(TupleCentreId) tid.getInternalTupleCentreId()});
			} catch (OperationNotPossibleException e) {
				throw new OperationNotAllowedException();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			try {
				switch (type){
				case TucsonOpType.OPTYPE_OUT: return context.out((AgentId)aid.getInternalAgentId(), t, l);
				case TucsonOpType.OPTYPE_IN: return context.in((AgentId)aid.getInternalAgentId(), t, l);
				case TucsonOpType.OPTYPE_INP: return context.inp((AgentId)aid.getInternalAgentId(), t, l);
				case TucsonOpType.OPTYPE_RD: return context.rd((AgentId)aid.getInternalAgentId(), t, l);
				case TucsonOpType.OPTYPE_RDP: return context.rdp((AgentId)aid.getInternalAgentId(), t, l);				
				}
			}  catch (InvalidLogicTupleException e) {
				throw new TucsonTupleException();
			} catch (OperationNotPossibleException e) {
				throw new OperationNotAllowedException();
			}
		} else if (type==TucsonOpType.OPTYPE_OUT_S || type==TucsonOpType.OPTYPE_IN_S || type==TucsonOpType.OPTYPE_INP_S || type==TucsonOpType.OPTYPE_RD_S || type==TucsonOpType.OPTYPE_RDP_S){
			INonBlockingSpecContext context = null;
			try {
				//Calls  DynamicRespectTCContainer.getNonBlockingSpecContext((TupleCentreId) tid.getInternalTupleCentreId())
				context = (INonBlockingSpecContext) TucsonLanguageManager.callStaticMethod(TucsonNodeLanguageManager.getCurLanguage().getContainerComponentName(), "getNonBlockingSpecContext", new Object[]{(TupleCentreId) tid.getInternalTupleCentreId()});
			} catch (OperationNotPossibleException e) {
				throw new OperationNotAllowedException();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			try {
				switch (type){
				case TucsonOpType.OPTYPE_OUT_S: return context.out_s((AgentId)aid.getInternalAgentId(), t, l);
				case TucsonOpType.OPTYPE_IN_S: return context.in_s((AgentId)aid.getInternalAgentId(), t, l);
				case TucsonOpType.OPTYPE_INP_S: return context.inp_s((AgentId)aid.getInternalAgentId(), t, l);
				case TucsonOpType.OPTYPE_RD_S: return context.rd_s((AgentId)aid.getInternalAgentId(), t, l);
				case TucsonOpType.OPTYPE_RDP_S: return context.rdp_s((AgentId)aid.getInternalAgentId(), t, l);				
				}
			}  catch (InvalidLogicTupleException e) {
				throw new TucsonTupleException();
			} catch (OperationNotPossibleException e) {
				throw new OperationNotAllowedException();
			}
		}
		return res;
	}
	
	public static LogicTuple doBlockingOperation(int type, TucsonAgentId aid, TucsonTupleCentreId tid, Object t) throws OperationNotAllowedException, TucsonTupleException, TucsonInvalidSpecificationException{
		LogicTuple res = null;
		if (TucsonNodeLanguageManager.getCurLanguage().getName().equals(TucsonOpType.DEFLANGUAGE)){
			if (type==TucsonOpType.OPTYPE_OUT || type==TucsonOpType.OPTYPE_IN || type==TucsonOpType.OPTYPE_INP || type==TucsonOpType.OPTYPE_RD || type==TucsonOpType.OPTYPE_RDP){
				IBlockingContext context = null;
				try {
					//Calls  DynamicRespectTCContainer.getBlockingContext((TupleCentreId) tid.getInternalTupleCentreId())
					context = (IBlockingContext) TucsonLanguageManager.callStaticMethod(TucsonNodeLanguageManager.getCurLanguage().getContainerComponentName(), "getBlockingContext", new Object[]{(TupleCentreId) tid.getInternalTupleCentreId()});
				} catch (OperationNotPossibleException e) {
					throw new OperationNotAllowedException();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				
				try {
					switch (type){
					case TucsonOpType.OPTYPE_OUT:{
						context.out((AgentId)aid.getInternalAgentId(), (LogicTuple)t);
						return res;
					}
					case TucsonOpType.OPTYPE_IN: return context.in((AgentId)aid.getInternalAgentId(), (LogicTuple)t);
					case TucsonOpType.OPTYPE_INP: return context.inp((AgentId)aid.getInternalAgentId(), (LogicTuple)t);
					case TucsonOpType.OPTYPE_RD: return context.rd((AgentId)aid.getInternalAgentId(), (LogicTuple)t);
					case TucsonOpType.OPTYPE_RDP: return context.rdp((AgentId)aid.getInternalAgentId(), (LogicTuple)t);				
					}
				}  catch (InvalidLogicTupleException e) {
					throw new TucsonTupleException();
				} catch (OperationNotPossibleException e) {
					throw new OperationNotAllowedException();
				}
			} else if (type==TucsonOpType.OPTYPE_OUT_S || type==TucsonOpType.OPTYPE_IN_S || type==TucsonOpType.OPTYPE_INP_S || type==TucsonOpType.OPTYPE_RD_S || type==TucsonOpType.OPTYPE_RDP_S){
				IBlockingSpecContext context = null;
				try {
					//Calls  DynamicRespectTCContainer.getBlockingSpecContext((TupleCentreId) tid.getInternalTupleCentreId())
					context = (IBlockingSpecContext) TucsonLanguageManager.callStaticMethod(TucsonNodeLanguageManager.getCurLanguage().getContainerComponentName(), "getBlockingSpecContext", new Object[]{(TupleCentreId) tid.getInternalTupleCentreId()});
				} catch (OperationNotPossibleException e) {
					throw new OperationNotAllowedException();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				try {				
					switch (type){
					case TucsonOpType.OPTYPE_OUT_S:{
						context.out_s((AgentId)aid.getInternalAgentId(), (LogicTuple)t);
						return res;
					}
					case TucsonOpType.OPTYPE_IN_S: return context.in_s((AgentId)aid.getInternalAgentId(), (LogicTuple)t);
					case TucsonOpType.OPTYPE_INP_S: return context.inp_s((AgentId)aid.getInternalAgentId(), (LogicTuple)t);
					case TucsonOpType.OPTYPE_RD_S: return context.rd_s((AgentId)aid.getInternalAgentId(), (LogicTuple)t);
					case TucsonOpType.OPTYPE_RDP_S: return context.rdp_s((AgentId)aid.getInternalAgentId(), (LogicTuple)t);				
					}
				}  catch (InvalidLogicTupleException e) {
					throw new TucsonTupleException();
				} catch (OperationNotPossibleException e) {
					throw new OperationNotAllowedException();
				}
			} else if (type==TucsonOpType.OPTYPE_SET_SPEC || type==TucsonOpType.OPTYPE_GET_SPEC){
				IBlockingContext context = null;
				try {
					//Calls  DynamicRespectTCContainer.getBlockingContext((TupleCentreId) tid.getInternalTupleCentreId())
					context = (IBlockingContext) TucsonLanguageManager.callStaticMethod(TucsonNodeLanguageManager.getCurLanguage().getContainerComponentName(), "getBlockingContext", new Object[]{(TupleCentreId) tid.getInternalTupleCentreId()});
				} catch (OperationNotPossibleException e) {
					throw new OperationNotAllowedException();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				try {
					if (type==TucsonOpType.OPTYPE_SET_SPEC){
						try {
							context.setSpec(new AgentId("prova"),new RespectSpecification((String)t));
							return res;
						} catch (InvalidAgentIdException e) {
							
						}
					} else {
						res = new LogicTuple(context.getSpec((AgentId)aid.getInternalAgentId()).toString());
						return res;
					}
				} catch (InvalidSpecificationException e) {
					throw new TucsonInvalidSpecificationException();
				}

			}
		}
		return res;
	}
	
	public static ITupleCentreOperation out(TucsonAgentId aid, TucsonTupleCentreId tid, LogicTuple t, OperationCompletionListener l) throws OperationNotAllowedException, TucsonTupleException{
		ITupleCentreOperation res = null;
		try {
			res = doNonBlockingOperation(TucsonOpType.OPTYPE_OUT, aid, tid, t, l);
		} catch (TucsonInvalidSpecificationException e) {
		}
		return res;
	}
	
	public static ITupleCentreOperation in(TucsonAgentId aid, TucsonTupleCentreId tid, LogicTuple t, OperationCompletionListener l) throws OperationNotAllowedException, TucsonTupleException{
		ITupleCentreOperation res = null;
		try {
			res = doNonBlockingOperation(TucsonOpType.OPTYPE_IN, aid, tid, t, l);
		} catch (TucsonInvalidSpecificationException e) {
		}
		return res;
	}
	
	public static ITupleCentreOperation rd(TucsonAgentId aid, TucsonTupleCentreId tid, LogicTuple t, OperationCompletionListener l) throws OperationNotAllowedException, TucsonTupleException{
		ITupleCentreOperation res = null;
		try {
			res = doNonBlockingOperation(TucsonOpType.OPTYPE_RD, aid, tid, t, l);
		} catch (TucsonInvalidSpecificationException e) {
		}
		return res;
	}
	
	public static ITupleCentreOperation inp(TucsonAgentId aid, TucsonTupleCentreId tid, LogicTuple t, OperationCompletionListener l) throws OperationNotAllowedException, TucsonTupleException{
		ITupleCentreOperation res = null;
		try {
			res = doNonBlockingOperation(TucsonOpType.OPTYPE_INP, aid, tid, t, l);
		} catch (TucsonInvalidSpecificationException e) {
		}
		return res;
	}
	
	public static ITupleCentreOperation rdp(TucsonAgentId aid, TucsonTupleCentreId tid, LogicTuple t, OperationCompletionListener l) throws OperationNotAllowedException, TucsonTupleException{
		ITupleCentreOperation res = null;
		try {
			res = doNonBlockingOperation(TucsonOpType.OPTYPE_RDP, aid, tid, t, l);
		} catch (TucsonInvalidSpecificationException e) {
		}
		return res;
	}
	
	public static ITupleCentreOperation out_s(TucsonAgentId aid, TucsonTupleCentreId tid, LogicTuple t, OperationCompletionListener l) throws OperationNotAllowedException, TucsonTupleException{
		ITupleCentreOperation res = null;
		try {
			res = doNonBlockingOperation(TucsonOpType.OPTYPE_OUT_S, aid, tid, t, l);
		} catch (TucsonInvalidSpecificationException e) {
		}
		return res;
	}
	
	public static ITupleCentreOperation in_s(TucsonAgentId aid, TucsonTupleCentreId tid, LogicTuple t, OperationCompletionListener l) throws OperationNotAllowedException, TucsonTupleException{
		ITupleCentreOperation res = null;
		try {
			res = doNonBlockingOperation(TucsonOpType.OPTYPE_IN_S, aid, tid, t, l);
		} catch (TucsonInvalidSpecificationException e) {
		}
		return res;
	}
	
	public static ITupleCentreOperation rd_s(TucsonAgentId aid, TucsonTupleCentreId tid, LogicTuple t, OperationCompletionListener l) throws OperationNotAllowedException, TucsonTupleException{
		ITupleCentreOperation res = null;
		try {
			res = doNonBlockingOperation(TucsonOpType.OPTYPE_RD_S, aid, tid, t, l);
		} catch (TucsonInvalidSpecificationException e) {
		}
		return res;
	}
	
	public static ITupleCentreOperation rdp_s(TucsonAgentId aid, TucsonTupleCentreId tid, LogicTuple t, OperationCompletionListener l) throws OperationNotAllowedException, TucsonTupleException{
		ITupleCentreOperation res = null;
		try {
			res = doNonBlockingOperation(TucsonOpType.OPTYPE_RDP_S, aid, tid, t, l);
		} catch (TucsonInvalidSpecificationException e) {
		}
		return res;
	}
	
	public static ITupleCentreOperation inp_s(TucsonAgentId aid, TucsonTupleCentreId tid, LogicTuple t, OperationCompletionListener l) throws OperationNotAllowedException, TucsonTupleException{
		ITupleCentreOperation res = null;
		try {
			res = doNonBlockingOperation(TucsonOpType.OPTYPE_INP_S, aid, tid, t, l);
		} catch (TucsonInvalidSpecificationException e) {
		}
		return res;
	}
	
	public static void setSpec(TucsonTupleCentreId tid, String spec) throws OperationNotAllowedException, TucsonInvalidSpecificationException{
		try {
			doBlockingOperation(TucsonOpType.OPTYPE_SET_SPEC, null, tid, spec);
		} catch (TucsonTupleException e) {
		}		
	}
	
	
	public static LogicTuple getSpec(TucsonTupleCentreId tid) throws OperationNotAllowedException{
		try {
			return doBlockingOperation(TucsonOpType.OPTYPE_GET_SPEC, null, tid, null);
		} catch (TucsonTupleException e) {
		} catch (TucsonInvalidSpecificationException e) {
		}
		return null;
	}
	
	public static LogicTuple out(TucsonAgentId aid, TucsonTupleCentreId tid, LogicTuple t) throws OperationNotAllowedException, TucsonTupleException{
		try {
			return doBlockingOperation(TucsonOpType.OPTYPE_OUT, aid, tid, t);
		} catch (TucsonInvalidSpecificationException e) {
		}
		return null;
	}
	
	public static LogicTuple in(TucsonAgentId aid, TucsonTupleCentreId tid, LogicTuple t) throws OperationNotAllowedException, TucsonTupleException{
		try {
			return doBlockingOperation(TucsonOpType.OPTYPE_IN, aid, tid, t);
		} catch (TucsonInvalidSpecificationException e) {
		}
		return null;
	}
	
	public static LogicTuple rd(TucsonAgentId aid, TucsonTupleCentreId tid, LogicTuple t) throws OperationNotAllowedException, TucsonTupleException{
		try {
			return doBlockingOperation(TucsonOpType.OPTYPE_RD, aid, tid, t);
		} catch (TucsonInvalidSpecificationException e) {
		}
		return null;
	}
	
	public static LogicTuple inp(TucsonAgentId aid, TucsonTupleCentreId tid, LogicTuple t) throws OperationNotAllowedException, TucsonTupleException{
		try {
			return doBlockingOperation(TucsonOpType.OPTYPE_INP, aid, tid, t);
		} catch (TucsonInvalidSpecificationException e) {
		}
		return null;
	}
	
	public static LogicTuple rdp(TucsonAgentId aid, TucsonTupleCentreId tid, LogicTuple t) throws OperationNotAllowedException, TucsonTupleException{
		try {
			return doBlockingOperation(TucsonOpType.OPTYPE_RDP, aid, tid, t);
		} catch (TucsonInvalidSpecificationException e) {
		}
		return null;
	}
	

	public static LogicTuple out_s(TucsonAgentId aid, TucsonTupleCentreId tid, LogicTuple t) throws OperationNotAllowedException, TucsonTupleException{
		try {
			return doBlockingOperation(TucsonOpType.OPTYPE_OUT_S, aid, tid, t);
		} catch (TucsonInvalidSpecificationException e) {
		}
		return null;
	}
	
	public static LogicTuple in_s(TucsonAgentId aid, TucsonTupleCentreId tid, LogicTuple t) throws OperationNotAllowedException, TucsonTupleException{
		try {
			return doBlockingOperation(TucsonOpType.OPTYPE_IN_S, aid, tid, t);
		} catch (TucsonInvalidSpecificationException e) {
		}
		return null;
	}
	
	public static LogicTuple rd_s(TucsonAgentId aid, TucsonTupleCentreId tid, LogicTuple t) throws OperationNotAllowedException, TucsonTupleException{
		try {
			return doBlockingOperation(TucsonOpType.OPTYPE_RD_S, aid, tid, t);
		} catch (TucsonInvalidSpecificationException e) {
		}
		return null;
	}
	
	public static LogicTuple rdp_s(TucsonAgentId aid, TucsonTupleCentreId tid, LogicTuple t) throws OperationNotAllowedException, TucsonTupleException{
		try {
			return doBlockingOperation(TucsonOpType.OPTYPE_RDP_S, aid, tid, t);
		} catch (TucsonInvalidSpecificationException e) {
		}
		return null;
	}
	
	public static LogicTuple inp_s(TucsonAgentId aid, TucsonTupleCentreId tid, LogicTuple t) throws OperationNotAllowedException, TucsonTupleException{
		try {
			return doBlockingOperation(TucsonOpType.OPTYPE_INP_S, aid, tid, t);
		} catch (TucsonInvalidSpecificationException e) {
		}
		return null;
	}
	
	public static synchronized void activateObservability(TucsonTupleCentreId tid, ObservableEventListener l){
		log("Activate observability on tuple centre "+tid.toString());
	}
	
	public static synchronized void deactivateObservability(TucsonTupleCentreId tid){
		log("Deactivate observability on tuple centre "+tid.toString());
	}
	
	public static synchronized void enablePersistence(TucsonTupleCentreId tid, String path) {
		log("Enable presistence on tuple centre "+tid.toString());
	}
	
	public static synchronized void disablePersistence(TucsonTupleCentreId tid) {
		log("Disable presistence on tuple centre "+tid.toString());
	}
	
	public static void loadPersistentInformation(TucsonTupleCentreId tid, String fileName) {
		
	}
	
	public static synchronized void destroyTC(TucsonTupleCentreId tid) {
		log("Destroy tuple centre "+tid.toString());
	}
	
	private static void log(String m) {
		System.out.println("[TuCSoN TC CONTAINER] " + m);
	}
	
		
}
