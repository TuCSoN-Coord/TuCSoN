package alice.tucson.service;

import java.util.List;

import alice.logictuple.LogicTuple;

import alice.respect.api.InstantiationNotPossibleException;
//import alice.respect.api.RespectTC;
//import alice.respect.api.TupleCentreId;
import alice.tuplecentre.api.ITupleCentreOperation;
import alice.tuplecentre.api.ObservableEventListener;

import alice.tuplecentre.core.OperationCompletionListener;

import alice.tucson.api.TucsonAgentId;
import alice.tucson.api.TucsonTupleCentreId;
import alice.tucson.api.exception.TucsonInvalidLogicTupleException;
import alice.tucson.api.exception.TucsonOperationNotPossibleException;

import alice.tucson.service.TucsonOperation;

import alice.tucson.types.IContainerResolver;

public class TucsonTCContainer
{		
	public static final int RESPECTQUEUE = 10;
	
	// Questo metodo deve essere utilizzato dall'org per creare centri di tuple con una specifica ontologia
	
	// Qui devo passare il riferimento all'Otologia relativa al tc
	public static boolean createTC(TucsonTupleCentreId id, int q)
	{
		IContainerResolver resolver;
		LanguageInfo currLang = TucsonNodeLanguageManager.getCurLanguage();
		
		try
		{
			// Ottengo il resolver relativo al linguaggio corrente
			resolver = (IContainerResolver) TucsonNodeLanguageManager.createObject(currLang.getResolverComponentName(), new Object[0]);
			
			// Qui devo passare il riferimento all'Otologia relativa al tc
			return resolver.tupleCentreCreator(id, q);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}			
	}
	
	public static Object getTC(TucsonTupleCentreId id) throws InstantiationNotPossibleException 
	{ 	
		IContainerResolver resolver;
		LanguageInfo currLang = TucsonNodeLanguageManager.getCurLanguage();
		
		try
		{
			// Ottengo il resolver relativo al linguaggio corrente
			resolver = (IContainerResolver) TucsonNodeLanguageManager.createObject(currLang.getResolverComponentName(), new Object[0]);
			
			return resolver.tupleCentreProvider(id);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}				
	 }	 
	
	// mettere questo metodo nel resolver ...
	public static Object doManagementOperation(int type, TucsonTupleCentreId tid, Object obj) throws TucsonOperationNotPossibleException, TucsonInvalidLogicTupleException
	{
		IContainerResolver resolver;
		LanguageInfo currLang = TucsonNodeLanguageManager.getCurLanguage();
		
		try
		{
			// Ottengo il resolver relativo al linguaggio corrente
			resolver = (IContainerResolver) TucsonNodeLanguageManager.createObject(currLang.getResolverComponentName(), new Object[0]);
			
			return resolver.doManagementOperation(type, tid, obj);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}				
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

	public static Object doBlockingOperation(int type, TucsonAgentId aid, TucsonTupleCentreId tid, Object o)
			throws Exception
	{
		IContainerResolver resolver;
		LanguageInfo currLang = TucsonNodeLanguageManager.getCurLanguage();
		
		try
		{
			// Ottengo il resolver relativo al linguaggio corrente
			resolver = (IContainerResolver) TucsonNodeLanguageManager.createObject(currLang.getResolverComponentName(), new Object[0]);
			
			return resolver.doBlockingOperation(type, aid, tid, o);
		}
		catch (Exception e)
		{
			e.printStackTrace();
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
		return (List<LogicTuple>) doBlockingOperation(TucsonOperation.get_Code(), aid, tid, null);
	}
	
	public static List<LogicTuple> set(TucsonAgentId aid, TucsonTupleCentreId tid, List<LogicTuple> tupleList) throws Exception
	{
		return (List<LogicTuple>) doBlockingOperation(TucsonOperation.set_Code(), aid, tid, null);
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

	/*public static synchronized void activateObservability(TucsonTupleCentreId tid, ObservableEventListener l)
	{
		log("Activate observability on tuple centre " + tid.toString());
	}

	public static synchronized void deactivateObservability(TucsonTupleCentreId tid)
	{
		log("Deactivate observability on tuple centre " + tid.toString());
	}*/

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
