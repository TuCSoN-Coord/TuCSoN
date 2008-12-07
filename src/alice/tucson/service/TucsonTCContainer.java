package alice.tucson.service;

import java.util.HashMap;

import alice.logictuple.InvalidLogicTupleException;
import alice.logictuple.LogicTuple;
import alice.logictuple.TupleArgument;
import alice.respect.api.AgentId;
import alice.respect.api.IBlockingContext;
import alice.respect.api.IBlockingSpecContext;
import alice.respect.api.IManagementContext;
import alice.respect.api.INonBlockingContext;
import alice.respect.api.INonBlockingSpecContext;
import alice.respect.api.InstantiationNotPossibleException;
import alice.respect.api.InvalidAgentIdException;
import alice.respect.api.InvalidSpecificationException;
import alice.respect.api.InvalidTupleCentreIdException;
import alice.respect.api.OperationNotPossibleException;
import alice.respect.api.RespectFramework;
import alice.respect.api.RespectSpecification;
import alice.respect.api.TupleCentreId;
import alice.respect.core.RespectTCContainer;
import alice.tucson.api.exception.OperationNotAllowedException;
import alice.tucson.api.exception.TCInstantiationNotPossibleException;
import alice.tucson.api.exception.TucsonInvalidSpecificationException;
import alice.tucson.api.exception.TucsonInvalidTupleCentreIdException;
import alice.tucson.api.exception.TucsonTupleException;
import alice.tucson.types.ITucsonOperationCompletionListener;
import alice.tucson.types.TucsonAgentId;
import alice.tucson.types.TucsonOpType;
import alice.tucson.types.TucsonTuple;
import alice.tucson.types.TucsonTupleCentreId;
import alice.tuplecentre.api.IId;
import alice.tuplecentre.api.ITupleCentreOperation;
import alice.tuplecentre.core.OperationCompletionListener;
import alice.tuplecentre.core.TupleCentreOperation;

/**
 * @author lsemprini
 *
 */
public class TucsonTCContainer {
	
	private static LanguageInfo curLang;
	private static HashMap<String, LanguageInfo> languages;
	private static boolean isNode = true;
	private static boolean firstTime = true;
	
	public static void setNodeMode(){
		isNode = true;
	}
	
	public static void setAgentMode(){
		isNode = false;
	}
	
	//Entry point of the container
	public static void useLanguage(String lang){
		//Load all the available languages
		if (firstTime){
			loadLanguages();
		}
		
		//curLang = languages.get(lang);
		curLang = languages.get(TucsonOpType.DEFLANGUAGE);
		if (curLang == null){
			//TuCSoN 1.9.0 Default language
			curLang = languages.get(TucsonOpType.DEFLANGUAGE);
		}
		log("Language choose: "+curLang.getName());
		
		if (isNode){
			attachTucson();
		}
	}	
	
	private static void loadLanguages(){
		log("Loading available container...");
		languages = new HashMap<String, LanguageInfo>();		
		if (isNode){
			//TODO da fare dinamico
			//Add the container component (the language is not responsible of the component)
			LanguageInfo respect20 = new LanguageInfo("ReSpecT 2.0");
			log("Load language "+respect20.getName()+"...");
			respect20.setComponentName("RespectContainer24");
			languages.put(respect20.getName(), respect20);
			
			LanguageInfo respect22 = RespectTCContainer.getLanguage();
			log("Load language "+respect22.getName()+"...");
			respect22.setComponentName("RespectContainer");
			languages.put(RespectTCContainer.getLanguage().getName(), respect22);
			
			LanguageInfo respect24 = new LanguageInfo(TucsonOpType.RESPECT24);
			log("Load language "+respect24.getName()+"...");
			respect22.setComponentName("RespectContainer24");
			languages.put(respect24.getName(), respect24);
		} else {
			//TODO da fare dinamico
			//Load the component for agentModule interaction like AgentId, TupleCentreId, LogicTuple, ...
	        LanguageInfo lInfo1 = new LanguageInfo(TucsonOpType.DEFLANGUAGE);
	        log("Load language "+lInfo1.getName()+"...");
	        lInfo1.setComponentName("RespectComp");
			lInfo1.setCompatibleLanguage("ReSpecT 2.1");
			lInfo1.setCompatibleLanguage("ReSpecT 2.0");
			lInfo1.setTransLanguage(TucsonOpType.RESPECT24, "RespecT22to24");
	        languages.put(lInfo1.getName(), lInfo1);
	        LanguageInfo lInfo2 = new LanguageInfo(TucsonOpType.RESPECT24);
	        log("Load language "+lInfo2.getName()+"...");
			lInfo2.setCompatibleLanguage("ReSpecT 2.3");
			lInfo2.setComponentName("Respect24Comp");
			languages.put(lInfo2.getName(), lInfo2);		
		}
		firstTime = false;
	}
	
	public static void attachTucson(){
		//TODO da fare dinamico
		if (curLang.getName().equals(TucsonOpType.DEFLANGUAGE)){
			//RespectTCContainer.setTucson();
		}
	}
	
	public static HashMap<String, LanguageInfo> getAvailableContainer(){
		loadLanguages();
		return languages; 
	}
	
	public static LanguageInfo getCurLanguage(){
		return curLang;
		
	}
	
	public static boolean createTC(TucsonTupleCentreId id) throws TCInstantiationNotPossibleException{
		if (curLang.getName().equals(TucsonOpType.DEFLANGUAGE)){
			try {
				return RespectTCContainer.createRespectTC((TupleCentreId)id.getInternalTupleCentreId(), 10);
			} catch (InstantiationNotPossibleException e) {
				throw new TCInstantiationNotPossibleException();
			}
		}
		return false;
	}
	
	public static ITupleCentreOperation doNonBlockingOperation(int type, TucsonAgentId aid, TucsonTupleCentreId tid, LogicTuple t, OperationCompletionListener l) throws OperationNotAllowedException, TucsonTupleException, TucsonInvalidSpecificationException{
		ITupleCentreOperation res = null;
		if (curLang.getName().equals(TucsonOpType.DEFLANGUAGE)){
			if (type==TucsonOpType.OPTYPE_OUT || type==TucsonOpType.OPTYPE_IN || type==TucsonOpType.OPTYPE_INP || type==TucsonOpType.OPTYPE_RD || type==TucsonOpType.OPTYPE_RDP){
				INonBlockingContext context;
				try {
					context = RespectTCContainer.getNonBlockingContext((TupleCentreId) tid.getInternalTupleCentreId());
				} catch (OperationNotPossibleException e) {
					throw new OperationNotAllowedException();
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
				INonBlockingSpecContext context;
				try {
					context = RespectTCContainer.getNonBlockingSpecContext((TupleCentreId) tid.getInternalTupleCentreId());
				} catch (OperationNotPossibleException e) {
					throw new OperationNotAllowedException();
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
		}
		return res;
	}
	
	public static LogicTuple doBlockingOperation(int type, TucsonAgentId aid, TucsonTupleCentreId tid, Object t) throws OperationNotAllowedException, TucsonTupleException, TucsonInvalidSpecificationException{
		LogicTuple res = null;
		if (curLang.getName().equals(TucsonOpType.DEFLANGUAGE)){
			if (type==TucsonOpType.OPTYPE_OUT || type==TucsonOpType.OPTYPE_IN || type==TucsonOpType.OPTYPE_INP || type==TucsonOpType.OPTYPE_RD || type==TucsonOpType.OPTYPE_RDP){
				IBlockingContext context;
				try {
					context = RespectTCContainer.getBlockingContext((TupleCentreId) tid.getInternalTupleCentreId());
				} catch (OperationNotPossibleException e) {
					throw new OperationNotAllowedException();
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
				IBlockingSpecContext context;
				try {
					context = RespectTCContainer.getBlockingSpecContext((TupleCentreId) tid.getInternalTupleCentreId());
				} catch (OperationNotPossibleException e) {
					throw new OperationNotAllowedException();
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
					log(res.toString());
				}  catch (InvalidLogicTupleException e) {
					throw new TucsonTupleException();
				} catch (OperationNotPossibleException e) {
					throw new OperationNotAllowedException();
				}
			} else if (type==TucsonOpType.OPTYPE_SET_SPEC || type==TucsonOpType.OPTYPE_GET_SPEC){
				IBlockingContext context;
				try {
					context = RespectTCContainer.getBlockingContext((TupleCentreId) tid.getInternalTupleCentreId());
				} catch (OperationNotPossibleException e) {
					throw new OperationNotAllowedException();
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
		if (isNode){
			System.out.println("[TuCSoN TC CONTAINER] " + m);
		} else {
			System.out.println("[TuCSoN AGENT MODULE RESOLVER] " + m);
		}
	}
	
		
}
