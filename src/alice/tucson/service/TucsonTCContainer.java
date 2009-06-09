package alice.tucson.service;

import java.util.List;

import alice.logictuple.LogicTuple;

import alice.tuplecentre.api.ITupleCentreOperation;

import alice.tuplecentre.core.OperationCompletionListener;

import alice.tucson.api.TucsonAgentId;
import alice.tucson.api.TucsonTupleCentreId;

import alice.tucson.service.TucsonOperation;

import alice.tucson.types.IContainerResolver;

public class TucsonTCContainer
{		
	public static final int RESPECTQUEUE = 10;
	
	public static boolean createTC(TucsonTupleCentreId id)
	{
		IContainerResolver resolver;
		LanguageInfo currLang = TucsonNodeLanguageManager.getCurLanguage();
		
		try
		{
			// Ottengo il resolver relativo al linguaggio corrente
			resolver = (IContainerResolver) TucsonNodeLanguageManager.createObject(currLang.getResolverComponentName(), new Object[0]);
			
			return resolver.tupleCentreCreator(id, RESPECTQUEUE);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}			
	}
	
	// mettere questo metodo nel resolver ...
	public static Object doManagementOperation(int type, TucsonAgentId aid, TucsonTupleCentreId tid, LogicTuple filter,
			OperationCompletionListener l) //throws OperationNotAllowedException, TucsonTupleException,
			//TucsonInvalidSpecificationException
	{
		/*ITupleCentreOperation res = null;
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
		if (type == TucsonOperation.get_TSet())
			return context.getTSet(filter);*/
		
		return null;
	}
	
	public static ITupleCentreOperation doNonBlockingOperation(int type, TucsonAgentId aid, TucsonTupleCentreId tid,
			LogicTuple t, OperationCompletionListener l) throws Exception
	{			
		IContainerResolver resolver;
		LanguageInfo currLang = TucsonNodeLanguageManager.getCurLanguage();
		
		log("Operazione non bloccante " + t.toString());
		
		try
		{
			// Ottengo il resolver relativo al linguaggio corrente
			resolver = (IContainerResolver) TucsonNodeLanguageManager.createObject(currLang.getResolverComponentName(), new Object[0]);
			
			return resolver.doNonBlockingOperation(type, aid, tid, t, l);
		}
		catch (Exception e)
		{
			throw new Exception();
		}
	}

	public static ITupleCentreOperation doNonBlockingSpecOperation(int type, TucsonAgentId aid, TucsonTupleCentreId tid,
			LogicTuple t, OperationCompletionListener l) throws Exception
	{
		IContainerResolver resolver;
		LanguageInfo currLang = TucsonNodeLanguageManager.getCurLanguage();
		
		try
		{
			// Ottengo il resolver relativo al linguaggio corrente
			resolver = (IContainerResolver) TucsonNodeLanguageManager.createObject(currLang.getResolverComponentName(), new Object[0]);
			
			return resolver.doNonBlockingSpecOperation(type, aid, tid, t, l);
		}
		catch (Exception e)
		{
			throw new Exception();
		}
	}

	public static Object doBlockingOperation(int type, TucsonAgentId aid, TucsonTupleCentreId tid, LogicTuple t)
			throws Exception
	{
		IContainerResolver resolver;
		LanguageInfo currLang = TucsonNodeLanguageManager.getCurLanguage();
		
		try
		{
			// Ottengo il resolver relativo al linguaggio corrente
			resolver = (IContainerResolver) TucsonNodeLanguageManager.createObject(currLang.getResolverComponentName(), new Object[0]);
			
			return resolver.doBlockingOperation(type, aid, tid, t);
		}
		catch (Exception e)
		{
			throw new Exception();
		}		
	}

	public static Object doBlockingSpecOperation(int type, TucsonAgentId aid, TucsonTupleCentreId tid, LogicTuple t)
			throws Exception
	{
		IContainerResolver resolver;
		LanguageInfo currLang = TucsonNodeLanguageManager.getCurLanguage();
		
		try
		{
			// Ottengo il resolver relativo al linguaggio corrente
			resolver = (IContainerResolver) TucsonNodeLanguageManager.createObject(currLang.getResolverComponentName(), new Object[0]);
			
			return resolver.doBlockingSpecOperation(type, aid, tid, t);
		}
		catch (Exception e)
		{
			throw new Exception();
		}
	}

	public static ITupleCentreOperation out(TucsonAgentId aid, TucsonTupleCentreId tid, LogicTuple t,
			OperationCompletionListener l) throws Exception
	{
		return doNonBlockingOperation(TucsonOperation.outCode(), aid, tid, t, l);
	}

	public static ITupleCentreOperation in(TucsonAgentId aid, TucsonTupleCentreId tid, LogicTuple t, OperationCompletionListener l)
			throws Exception
	{
		return doNonBlockingOperation(TucsonOperation.inCode(), aid, tid, t, l);
	}

	public static ITupleCentreOperation rd(TucsonAgentId aid, TucsonTupleCentreId tid, LogicTuple t, OperationCompletionListener l)
			throws Exception
	{
		return doNonBlockingOperation(TucsonOperation.rdCode(), aid, tid, t, l);
	}

	public static ITupleCentreOperation inp(TucsonAgentId aid, TucsonTupleCentreId tid, LogicTuple t,
			OperationCompletionListener l) throws Exception
	{
		return doNonBlockingOperation(TucsonOperation.inpCode(), aid, tid, t, l);
	}

	public static ITupleCentreOperation rdp(TucsonAgentId aid, TucsonTupleCentreId tid, LogicTuple t,
			OperationCompletionListener l) throws Exception
	{
		return doNonBlockingOperation(TucsonOperation.rdpCode(), aid, tid, t, l);
	}

	public static ITupleCentreOperation out_s(TucsonAgentId aid, TucsonTupleCentreId tid, LogicTuple t,
			OperationCompletionListener l) throws Exception
	{
		return doNonBlockingOperation(TucsonOperation.out_sCode(), aid, tid, t, l);
	}

	public static ITupleCentreOperation in_s(TucsonAgentId aid, TucsonTupleCentreId tid, LogicTuple t,
			OperationCompletionListener l) throws Exception
	{
		return doNonBlockingOperation(TucsonOperation.in_sCode(), aid, tid, t, l);
	}

	public static ITupleCentreOperation rd_s(TucsonAgentId aid, TucsonTupleCentreId tid, LogicTuple t,
			OperationCompletionListener l) throws Exception
	{
		return doNonBlockingOperation(TucsonOperation.rd_sCode(), aid, tid, t, l);
	}

	public static ITupleCentreOperation rdp_s(TucsonAgentId aid, TucsonTupleCentreId tid, LogicTuple t,
			OperationCompletionListener l) throws Exception
	{
		return doNonBlockingOperation(TucsonOperation.rdp_sCode(), aid, tid, t, l);
	}

	public static ITupleCentreOperation inp_s(TucsonAgentId aid, TucsonTupleCentreId tid, LogicTuple t,
			OperationCompletionListener l) throws Exception
	{
		return doNonBlockingOperation(TucsonOperation.inp_sCode(), aid, tid, t, l);
	}

	public static void setSpec(TucsonAgentId aid, TucsonTupleCentreId tid, LogicTuple spec) throws Exception
	{
		doBlockingSpecOperation(TucsonOperation.set_specCode(), aid, tid, spec);
	}

	public static LogicTuple getSpec(TucsonAgentId aid, TucsonTupleCentreId tid) throws Exception
	{

		return (LogicTuple) doBlockingSpecOperation(TucsonOperation.get_specCode(), aid, tid, null);
	}

	public static List<LogicTuple> get(TucsonAgentId aid, TucsonTupleCentreId tid) throws Exception
	{
		return (List<LogicTuple>) doBlockingOperation(TucsonOperation.outCode(), aid, tid, null);
	}
	
	public static LogicTuple out(TucsonAgentId aid, TucsonTupleCentreId tid, LogicTuple t) throws Exception
	{
		return (LogicTuple) doBlockingOperation(TucsonOperation.outCode(), aid, tid, t);
	}

	public static LogicTuple in(TucsonAgentId aid, TucsonTupleCentreId tid, LogicTuple t) throws Exception
	{
		return (LogicTuple) doBlockingOperation(TucsonOperation.inCode(), aid, tid, t);
	}

	public static LogicTuple rd(TucsonAgentId aid, TucsonTupleCentreId tid, LogicTuple t) throws Exception
	{
		return (LogicTuple) doBlockingOperation(TucsonOperation.rdCode(), aid, tid, t);
	}

	public static LogicTuple inp(TucsonAgentId aid, TucsonTupleCentreId tid, LogicTuple t) throws Exception
	{
		return (LogicTuple) doBlockingOperation(TucsonOperation.inpCode(), aid, tid, t);
	}

	public static LogicTuple rdp(TucsonAgentId aid, TucsonTupleCentreId tid, LogicTuple t) throws Exception
	{
		return (LogicTuple) doBlockingOperation(TucsonOperation.rdpCode(), aid, tid, t);
	}

	public static LogicTuple out_s(TucsonAgentId aid, TucsonTupleCentreId tid, LogicTuple t) throws Exception
	{
		return (LogicTuple) doBlockingOperation(TucsonOperation.out_sCode(), aid, tid, t);
	}

	public static LogicTuple in_s(TucsonAgentId aid, TucsonTupleCentreId tid, LogicTuple t) throws Exception
	{
		return (LogicTuple) doBlockingOperation(TucsonOperation.in_sCode(), aid, tid, t);
	}

	public static LogicTuple rd_s(TucsonAgentId aid, TucsonTupleCentreId tid, LogicTuple t) throws Exception
	{
		return (LogicTuple) doBlockingOperation(TucsonOperation.rd_sCode(), aid, tid, t);
	}

	public static LogicTuple rdp_s(TucsonAgentId aid, TucsonTupleCentreId tid, LogicTuple t) throws Exception
	{
		return (LogicTuple) doBlockingOperation(TucsonOperation.rdp_sCode(), aid, tid, t);
	}

	public static LogicTuple inp_s(TucsonAgentId aid, TucsonTupleCentreId tid, LogicTuple t) throws Exception
	{
		return (LogicTuple) doBlockingOperation(TucsonOperation.inp_sCode(), aid, tid, t);
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
