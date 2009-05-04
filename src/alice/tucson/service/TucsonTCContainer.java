package alice.tucson.service;

import java.util.ArrayList;
import java.util.HashMap;

import alice.logictuple.InvalidLogicTupleException;
import alice.logictuple.InvalidTupleOperationException;
import alice.logictuple.LogicTuple;

import alice.respect.api.AgentId;
import alice.respect.api.IBlockingContext;
import alice.respect.api.IBlockingSpecContext;
import alice.respect.api.INonBlockingContext;
import alice.respect.api.INonBlockingSpecContext;
import alice.respect.api.IManagementContext;
import alice.respect.api.InstantiationNotPossibleException;
import alice.respect.api.InvalidAgentIdException;
import alice.respect.api.InvalidSpecificationException;
import alice.respect.api.OperationNotPossibleException;
import alice.respect.api.RespectSpecification;
import alice.respect.api.TupleCentreId;

import alice.respect.core.RespectVM;

import alice.tucson.api.exception.OperationNotAllowedException;
import alice.tucson.api.exception.TCInstantiationNotPossibleException;
import alice.tucson.api.exception.TucsonInvalidSpecificationException;
import alice.tucson.api.exception.TucsonTupleException;

import alice.tucson.types.TucsonConstant;
import alice.tucson.types.TucsonAgentId;
import alice.tucson.types.TucsonTupleCentreId;

import alice.tucson.service.TucsonOperation;

import alice.tuplecentre.api.ITupleCentreOperation;
import alice.tuplecentre.core.OperationCompletionListener;

/**
 * @author lsemprini
 * 
 */
public class TucsonTCContainer
{		
	public static void loadContainerLanguage(ArrayList<String> cont, HashMap<String, LanguageInfo> languages)
	{
		// Add the container component (the language is not responsible of the
		// component)
		for (int i = 0; i < cont.size(); i++)
		{
			LanguageInfo respectCont;
			try
			{
				// Calls DynamicRespectTCContainer.getLanguage()
				respectCont = (LanguageInfo) TucsonLanguageManager.callStaticMethod(cont.get(i), "getLanguage", new Object[0]);
				respectCont.setContainerComponentName(cont.get(i));
				languages.put(respectCont.getName(), respectCont);
				log("Container " + respectCont.getName() + " loaded succesfully");
			}
			catch (Exception e)
			{
				log("Error: Unable to load " + cont.get(i));
			}
		}
	}

	public static void attachTucson()
	{
		try
		{
			// Calls DynamicRespectTCContainer.setTucson()
			TucsonLanguageManager.callStaticMethod(TucsonNodeLanguageManager.getCurLanguage().getContainerComponentName(),
					"setTucson", new Object[0]);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static boolean createTC(TucsonTupleCentreId id) throws TCInstantiationNotPossibleException
	{
		// Calls
		// DynamicRespectTCContainer.createRespectTC((TupleCentreId)id.getInternalTupleCentreId(),
		// TucsonOpType.RESPECTQUEUE)
		try
		{
			return (Boolean) TucsonLanguageManager.callStaticMethod(TucsonNodeLanguageManager.getCurLanguage()
					.getContainerComponentName(), "createRespectTC", new Object[] {
					(TupleCentreId) id.getInternalTupleCentreId(), TucsonConstant.RESPECTQUEUE });
		}
		catch (InstantiationNotPossibleException e)
		{
			throw new TCInstantiationNotPossibleException();
		}
		catch (Exception e1)
		{
			e1.printStackTrace();
		}

		return false;
	}

	// forse da cancellare ...
	public static RespectVM getVM(TucsonTupleCentreId id)
	{
		try
		{
			return (RespectVM) TucsonLanguageManager.callStaticMethod(TucsonNodeLanguageManager.getCurLanguage()
					.getContainerComponentName(), "getVM", new Object[] {
					(TupleCentreId) id.getInternalTupleCentreId()});
		}		
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}
	
	public static Object doManagementOperation(int type, TucsonAgentId aid, TucsonTupleCentreId tid,
			LogicTuple filter, OperationCompletionListener l) throws OperationNotAllowedException, TucsonTupleException,
			TucsonInvalidSpecificationException
	{
		ITupleCentreOperation res = null;
		IManagementContext context = null;
			
		try
		{
			context = (IManagementContext) TucsonLanguageManager.callStaticMethod(TucsonNodeLanguageManager.getCurLanguage()
					.getContainerComponentName(), "getManagementContext", new Object[] { (TupleCentreId) tid
					.getInternalTupleCentreId() });
		}
		catch (OperationNotPossibleException e)
		{
			throw new OperationNotAllowedException();
		}
		catch (Exception e1)
		{
			e1.printStackTrace();
		}
		
		log(aid + " " + " " + filter + " " + tid);
		if(type == TucsonOperation.get_TSet())
			return context.getTSet(filter);
			/*try
			{
				log(aid + " " + " " + t + " " + tid);
				if(type == TucsonOperation.outCode())
					return context.out((AgentId) aid.getInternalAgentId(), t, l);
				if(type == TucsonOperation.inCode())
					return context.in((AgentId) aid.getInternalAgentId(), t, l);
				if(type == TucsonOperation.inpCode())
					return context.inp((AgentId) aid.getInternalAgentId(), t, l);
				if(type == TucsonOperation.rdCode())
					return context.rd((AgentId) aid.getInternalAgentId(), t, l);
				if(type == TucsonOperation.rdpCode())
					return context.rdp((AgentId) aid.getInternalAgentId(), t, l);
			}
			catch (InvalidLogicTupleException e)
			{
				throw new TucsonTupleException();
			}
			catch (OperationNotPossibleException e)
			{
				throw new OperationNotAllowedException();
			}
		*/
		return res;
	}
	
	public static ITupleCentreOperation doNonBlockingOperation(int type, TucsonAgentId aid, TucsonTupleCentreId tid,
			LogicTuple t, OperationCompletionListener l) throws OperationNotAllowedException, TucsonTupleException,
			TucsonInvalidSpecificationException
	{
		ITupleCentreOperation res = null;
		INonBlockingContext context = null;
			
			try
			{
				// Calls
				// DynamicRespectTCContainer.getNonBlockingContext((TupleCentreId)
				// tid.getInternalTupleCentreId())
				context = (INonBlockingContext) TucsonLanguageManager.callStaticMethod(TucsonNodeLanguageManager.getCurLanguage()
						.getContainerComponentName(), "getNonBlockingContext", new Object[] { (TupleCentreId) tid
						.getInternalTupleCentreId() });
			}
			catch (OperationNotPossibleException e)
			{
				throw new OperationNotAllowedException();
			}
			catch (Exception e1)
			{
				e1.printStackTrace();
			}
			try
			{
				log(aid + " " + " " + t + " " + tid);
				if(type == TucsonOperation.outCode())
					return context.out((AgentId) aid.getInternalAgentId(), t, l);
				if(type == TucsonOperation.inCode())
					return context.in((AgentId) aid.getInternalAgentId(), t, l);
				if(type == TucsonOperation.inpCode())
					return context.inp((AgentId) aid.getInternalAgentId(), t, l);
				if(type == TucsonOperation.rdCode())
					return context.rd((AgentId) aid.getInternalAgentId(), t, l);
				if(type == TucsonOperation.rdpCode())
					return context.rdp((AgentId) aid.getInternalAgentId(), t, l);
			}
			catch (InvalidLogicTupleException e)
			{
				throw new TucsonTupleException();
			}
			catch (OperationNotPossibleException e)
			{
				throw new OperationNotAllowedException();
			}
		
		return res;
	}

	public static ITupleCentreOperation doNonBlockingSpecOperation(int type, TucsonAgentId aid, TucsonTupleCentreId tid,
			LogicTuple t, OperationCompletionListener l) throws OperationNotAllowedException, TucsonTupleException,
			TucsonInvalidSpecificationException
	{
		ITupleCentreOperation res = null;		
		INonBlockingSpecContext context = null;
		
		try
		{
			// Calls
			// DynamicRespectTCContainer.getNonBlockingSpecContext((TupleCentreId)
			// tid.getInternalTupleCentreId())
			context = (INonBlockingSpecContext) TucsonLanguageManager.callStaticMethod(TucsonNodeLanguageManager
					.getCurLanguage().getContainerComponentName(), "getNonBlockingSpecContext",
					new Object[] { (TupleCentreId) tid.getInternalTupleCentreId() });
		}
		catch (OperationNotPossibleException e)
		{
			throw new OperationNotAllowedException();
		}
		catch (Exception e1)
		{
			e1.printStackTrace();
		}
		
		try
		{
			if(type == TucsonOperation.out_sCode())
				return context.out_s((AgentId) aid.getInternalAgentId(), t, l);
			if(type == TucsonOperation.in_sCode())
				return context.in_s((AgentId) aid.getInternalAgentId(), t, l);
			if(type == TucsonOperation.inp_sCode())
				return context.inp_s((AgentId) aid.getInternalAgentId(), t, l);
			if(type == TucsonOperation.rd_sCode())
				return context.rd_s((AgentId) aid.getInternalAgentId(), t, l);
			if(type == TucsonOperation.rdp_sCode())
				return context.rdp_s((AgentId) aid.getInternalAgentId(), t, l);
		}
		catch (InvalidLogicTupleException e)
		{
			throw new TucsonTupleException();
		}
		catch (OperationNotPossibleException e)
		{
			throw new OperationNotAllowedException();
		}
			
		return res;
	}
	
	public static LogicTuple doBlockingOperation(int type, TucsonAgentId aid, TucsonTupleCentreId tid, LogicTuple t)
	throws OperationNotAllowedException, TucsonTupleException, TucsonInvalidSpecificationException, InvalidTupleOperationException
	{
		LogicTuple res = null;
		
		if (type == TucsonOperation.outCode() || type == TucsonOperation.inCode() || type == TucsonOperation.inpCode()
				|| type == TucsonOperation.rdCode() || type == TucsonOperation.rdpCode())
		{
			IBlockingContext context = null;
			try
			{
				// Calls
				// DynamicRespectTCContainer.getBlockingContext((TupleCentreId)
				// tid.getInternalTupleCentreId())
				context = (IBlockingContext) TucsonLanguageManager.callStaticMethod(TucsonNodeLanguageManager
						.getCurLanguage().getContainerComponentName(), "getBlockingContext",
						new Object[] { (TupleCentreId) tid.getInternalTupleCentreId() });
				
				//System.out.println(context);
			}
			catch (OperationNotPossibleException e)
			{
				throw new OperationNotAllowedException();
			}
			catch (Exception e1)
			{
				e1.printStackTrace();
			}
	
			try
			{
				if(type == TucsonOperation.outCode())
				{
					context.out((AgentId) aid.getInternalAgentId(), (LogicTuple) t);
					return res;
				}
				if(type == TucsonOperation.inCode())
					return context.in((AgentId) aid.getInternalAgentId(), (LogicTuple) t);
				if(type == TucsonOperation.inpCode())
					return context.inp((AgentId) aid.getInternalAgentId(), (LogicTuple) t);
				if(type == TucsonOperation.rdCode())
					return context.rd((AgentId) aid.getInternalAgentId(), (LogicTuple) t);
				if(type == TucsonOperation.rdpCode())
					return context.rdp((AgentId) aid.getInternalAgentId(), (LogicTuple) t);
			}
			catch (InvalidLogicTupleException e)
			{
				throw new TucsonTupleException();
			}
			catch (OperationNotPossibleException e)
			{
				throw new OperationNotAllowedException();
			}
		}
		else if (type == TucsonOperation.set_specCode() || type == TucsonOperation.get_specCode())
		{
			IBlockingContext context = null;
			try
			{
				// Calls
				// DynamicRespectTCContainer.getBlockingContext((TupleCentreId)
				// tid.getInternalTupleCentreId())
				context = (IBlockingContext) TucsonLanguageManager.callStaticMethod(TucsonNodeLanguageManager
						.getCurLanguage().getContainerComponentName(), "getBlockingContext",
						new Object[] { (TupleCentreId) tid.getInternalTupleCentreId() });
			}
			catch (OperationNotPossibleException e)
			{
				throw new OperationNotAllowedException();
			}
			catch (Exception e1)
			{
				e1.printStackTrace();
			}
			try
			{
				if (type == TucsonOperation.set_specCode())
				{					
					context.setSpec((AgentId) aid.getInternalAgentId(), new RespectSpecification(((LogicTuple)t).getArg(0).getName()));
					return res;
				}
				else
				{
					res = new LogicTuple(context.getSpec((AgentId) aid.getInternalAgentId()).toString());
					return res;
				}
			}
			catch (InvalidSpecificationException e)
			{
				throw new TucsonInvalidSpecificationException();
			}
			
		}
			
		return res;
	}

	public static LogicTuple doBlockingSpecOperation(int type, TucsonAgentId aid, TucsonTupleCentreId tid, LogicTuple t)
			throws OperationNotAllowedException, TucsonTupleException, TucsonInvalidSpecificationException
	{
		LogicTuple res = null;
		
		IBlockingSpecContext context = null;
		try
		{
			// Calls
			// DynamicRespectTCContainer.getBlockingSpecContext((TupleCentreId)
			// tid.getInternalTupleCentreId())
			context = (IBlockingSpecContext) TucsonLanguageManager.callStaticMethod(TucsonNodeLanguageManager
					.getCurLanguage().getContainerComponentName(), "getBlockingSpecContext",
					new Object[] { (TupleCentreId) tid.getInternalTupleCentreId() });
		}
		catch (OperationNotPossibleException e)
		{
			throw new OperationNotAllowedException();
		}
		catch (Exception e1)
		{
			e1.printStackTrace();
		}
		try
		{
			if(type == TucsonOperation.out_sCode())
			{
				context.out_s((AgentId) aid.getInternalAgentId(), (LogicTuple) t);
				return res;
			}
			if(type == TucsonOperation.in_sCode())
				return context.in_s((AgentId) aid.getInternalAgentId(), (LogicTuple) t);
			if(type == TucsonOperation.inp_sCode())
				return context.inp_s((AgentId) aid.getInternalAgentId(), (LogicTuple) t);
			if(type == TucsonOperation.rdCode())
				return context.rd_s((AgentId) aid.getInternalAgentId(), (LogicTuple) t);
			if(type == TucsonOperation.rdp_sCode())
				return context.rdp_s((AgentId) aid.getInternalAgentId(), (LogicTuple) t);				
		}
		catch (InvalidLogicTupleException e)
		{
			throw new TucsonTupleException();
		}
		catch (OperationNotPossibleException e)
		{
			throw new OperationNotAllowedException();
		}		
		
		return res;
	}
	
	public static ITupleCentreOperation out(TucsonAgentId aid, TucsonTupleCentreId tid, LogicTuple t,
			OperationCompletionListener l) throws OperationNotAllowedException, TucsonTupleException
	{
		ITupleCentreOperation res = null;
		try
		{
			res = doNonBlockingOperation(TucsonOperation.outCode(), aid, tid, t, l);
		}
		catch (TucsonInvalidSpecificationException e)
		{
		}
		return res;
	}
	
	public static ITupleCentreOperation in(TucsonAgentId aid, TucsonTupleCentreId tid, LogicTuple t, OperationCompletionListener l)
			throws OperationNotAllowedException, TucsonTupleException
	{
		ITupleCentreOperation res = null;
		try
		{
			res = doNonBlockingOperation(TucsonOperation.inCode(), aid, tid, t, l);
		}
		catch (TucsonInvalidSpecificationException e)
		{
		}
		return res;
	}

	public static ITupleCentreOperation rd(TucsonAgentId aid, TucsonTupleCentreId tid, LogicTuple t, OperationCompletionListener l)
			throws OperationNotAllowedException, TucsonTupleException
	{
		ITupleCentreOperation res = null;
		try
		{
			res = doNonBlockingOperation(TucsonOperation.rdCode(), aid, tid, t, l);
		}
		catch (TucsonInvalidSpecificationException e)
		{
		}
		return res;
	}

	public static ITupleCentreOperation inp(TucsonAgentId aid, TucsonTupleCentreId tid, LogicTuple t,
			OperationCompletionListener l) throws OperationNotAllowedException, TucsonTupleException
	{
		ITupleCentreOperation res = null;
		try
		{
			res = doNonBlockingOperation(TucsonOperation.inpCode(), aid, tid, t, l);
		}
		catch (TucsonInvalidSpecificationException e)
		{
		}
		return res;
	}

	public static ITupleCentreOperation rdp(TucsonAgentId aid, TucsonTupleCentreId tid, LogicTuple t,
			OperationCompletionListener l) throws OperationNotAllowedException, TucsonTupleException
	{
		ITupleCentreOperation res = null;
		try
		{
			res = doNonBlockingOperation(TucsonOperation.rdpCode(), aid, tid, t, l);
		}
		catch (TucsonInvalidSpecificationException e)
		{
		}
		return res;
	}

	public static ITupleCentreOperation out_s(TucsonAgentId aid, TucsonTupleCentreId tid, LogicTuple t,
			OperationCompletionListener l) throws OperationNotAllowedException, TucsonTupleException
	{
		ITupleCentreOperation res = null;
		try
		{
			res = doNonBlockingOperation(TucsonOperation.out_sCode(), aid, tid, t, l);
		}
		catch (TucsonInvalidSpecificationException e)
		{
		}
		return res;
	}

	public static ITupleCentreOperation in_s(TucsonAgentId aid, TucsonTupleCentreId tid, LogicTuple t,
			OperationCompletionListener l) throws OperationNotAllowedException, TucsonTupleException
	{
		ITupleCentreOperation res = null;
		try
		{
			res = doNonBlockingOperation(TucsonOperation.in_sCode(), aid, tid, t, l);
		}
		catch (TucsonInvalidSpecificationException e)
		{
		}
		return res;
	}

	public static ITupleCentreOperation rd_s(TucsonAgentId aid, TucsonTupleCentreId tid, LogicTuple t,
			OperationCompletionListener l) throws OperationNotAllowedException, TucsonTupleException
	{
		ITupleCentreOperation res = null;
		try
		{
			res = doNonBlockingOperation(TucsonOperation.rd_sCode(), aid, tid, t, l);
		}
		catch (TucsonInvalidSpecificationException e)
		{
		}
		return res;
	}

	public static ITupleCentreOperation rdp_s(TucsonAgentId aid, TucsonTupleCentreId tid, LogicTuple t,
			OperationCompletionListener l) throws OperationNotAllowedException, TucsonTupleException
	{
		ITupleCentreOperation res = null;
		try
		{
			res = doNonBlockingOperation(TucsonOperation.rdp_sCode(), aid, tid, t, l);
		}
		catch (TucsonInvalidSpecificationException e)
		{
		}
		return res;
	}

	public static ITupleCentreOperation inp_s(TucsonAgentId aid, TucsonTupleCentreId tid, LogicTuple t,
			OperationCompletionListener l) throws OperationNotAllowedException, TucsonTupleException
	{
		ITupleCentreOperation res = null;
		try
		{
			res = doNonBlockingOperation(TucsonOperation.inp_sCode(), aid, tid, t, l);
		}
		catch (TucsonInvalidSpecificationException e)
		{
		}
		return res;
	}

	public static void setSpec(TucsonAgentId aid, TucsonTupleCentreId tid, LogicTuple spec) throws OperationNotAllowedException,
			TucsonInvalidSpecificationException, InvalidTupleOperationException
	{
		try
		{
			doBlockingOperation(TucsonOperation.set_specCode(), aid, tid, spec);
		}
		catch (TucsonTupleException e)
		{
		}
	}

	public static LogicTuple getSpec(TucsonAgentId aid, TucsonTupleCentreId tid) throws OperationNotAllowedException, InvalidTupleOperationException
	{
		try
		{
			return doBlockingOperation(TucsonOperation.get_specCode(), aid, tid, null);
		}
		catch (TucsonTupleException e)
		{
		}
		catch (TucsonInvalidSpecificationException e)
		{
		}
		return null;
	}

	public static LogicTuple out(TucsonAgentId aid, TucsonTupleCentreId tid, LogicTuple t) throws OperationNotAllowedException,
			TucsonTupleException, InvalidTupleOperationException
	{
		try
		{
			return doBlockingOperation(TucsonOperation.outCode(), aid, tid, t);
		}
		catch (TucsonInvalidSpecificationException e)
		{
		}
		return null;
	}

	public static LogicTuple in(TucsonAgentId aid, TucsonTupleCentreId tid, LogicTuple t) throws OperationNotAllowedException,
			TucsonTupleException, InvalidTupleOperationException
	{
		try
		{
			return doBlockingOperation(TucsonOperation.inCode(), aid, tid, t);
		}
		catch (TucsonInvalidSpecificationException e)
		{
		}
		return null;
	}

	public static LogicTuple rd(TucsonAgentId aid, TucsonTupleCentreId tid, LogicTuple t) throws OperationNotAllowedException,
			TucsonTupleException, InvalidTupleOperationException
	{
		try
		{
			return doBlockingOperation(TucsonOperation.rdCode(), aid, tid, t);
		}
		catch (TucsonInvalidSpecificationException e)
		{
		}
		return null;
	}

	public static LogicTuple inp(TucsonAgentId aid, TucsonTupleCentreId tid, LogicTuple t) throws OperationNotAllowedException,
			TucsonTupleException, InvalidTupleOperationException
	{
		try
		{
			return doBlockingOperation(TucsonOperation.inpCode(), aid, tid, t);
		}
		catch (TucsonInvalidSpecificationException e)
		{
		}
		return null;
	}

	public static LogicTuple rdp(TucsonAgentId aid, TucsonTupleCentreId tid, LogicTuple t) throws OperationNotAllowedException,
			TucsonTupleException, InvalidTupleOperationException
	{
		try
		{
			return doBlockingOperation(TucsonOperation.rdpCode(), aid, tid, t);
		}
		catch (TucsonInvalidSpecificationException e)
		{
		}
		return null;
	}

	public static LogicTuple out_s(TucsonAgentId aid, TucsonTupleCentreId tid, LogicTuple t) throws OperationNotAllowedException,
			TucsonTupleException, InvalidTupleOperationException
	{
		try
		{
			return doBlockingOperation(TucsonOperation.out_sCode(), aid, tid, t);
		}
		catch (TucsonInvalidSpecificationException e)
		{
		}
		return null;
	}

	public static LogicTuple in_s(TucsonAgentId aid, TucsonTupleCentreId tid, LogicTuple t) throws OperationNotAllowedException,
			TucsonTupleException, InvalidTupleOperationException
	{
		try
		{
			return doBlockingOperation(TucsonOperation.in_sCode(), aid, tid, t);
		}
		catch (TucsonInvalidSpecificationException e)
		{
		}
		return null;
	}

	public static LogicTuple rd_s(TucsonAgentId aid, TucsonTupleCentreId tid, LogicTuple t) throws OperationNotAllowedException,
			TucsonTupleException, InvalidTupleOperationException
	{
		try
		{
			return doBlockingOperation(TucsonOperation.rd_sCode(), aid, tid, t);
		}
		catch (TucsonInvalidSpecificationException e)
		{
		}
		return null;
	}

	public static LogicTuple rdp_s(TucsonAgentId aid, TucsonTupleCentreId tid, LogicTuple t) throws OperationNotAllowedException,
			TucsonTupleException, InvalidTupleOperationException
	{
		try
		{
			return doBlockingOperation(TucsonOperation.rdp_sCode(), aid, tid, t);
		}
		catch (TucsonInvalidSpecificationException e)
		{
		}
		return null;
	}

	public static LogicTuple inp_s(TucsonAgentId aid, TucsonTupleCentreId tid, LogicTuple t) throws OperationNotAllowedException,
			TucsonTupleException, InvalidTupleOperationException
	{
		try
		{
			return doBlockingOperation(TucsonOperation.inp_sCode(), aid, tid, t);
		}
		catch (TucsonInvalidSpecificationException e)
		{
		}
		return null;
	}

	public static synchronized void activateObservability(TucsonTupleCentreId tid, ObservableEventListener l)
	{
		log("Activate observability on tuple centre " + tid.toString());
	}

	public static synchronized void deactivateObservability(TucsonTupleCentreId tid)
	{
		log("Deactivate observability on tuple centre " + tid.toString());
	}

	public static synchronized void enablePersistence(TucsonTupleCentreId tid, String path)
	{
		log("Enable presistence on tuple centre " + tid.toString());
	}

	public static synchronized void disablePersistence(TucsonTupleCentreId tid)
	{
		log("Disable presistence on tuple centre " + tid.toString());
	}

	public static void loadPersistentInformation(TucsonTupleCentreId tid, String fileName)
	{
	}

	public static synchronized void destroyTC(TucsonTupleCentreId tid)
	{
		log("Destroy tuple centre " + tid.toString());
	}

	private static void log(String m)
	{
		System.out.println("[TuCSoN TC CONTAINER] " + m);
	}

}
