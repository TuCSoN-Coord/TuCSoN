package alice.tucson.resolver;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

import alice.logictuple.InvalidLogicTupleException;
import alice.logictuple.InvalidTupleOperationException;
import alice.logictuple.LogicTuple;
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
import alice.respect.api.RespectSpecification;
import alice.respect.api.TupleCentreId;
import alice.respect.core.BlockingSpecContext;
import alice.respect.core.RespectTCContainer;

import alice.tucson.api.TucsonAgentId;
import alice.tucson.api.TucsonTupleCentreId;

import alice.tucson.api.exception.TucsonInstantiationNotPossibleException;
import alice.tucson.api.exception.TucsonInvalidAgentIdException;
import alice.tucson.api.exception.TucsonInvalidLogicTupleException;
import alice.tucson.api.exception.TucsonInvalidSpecificationException;
import alice.tucson.api.exception.TucsonInvalidTupleCentreIdException;
import alice.tucson.api.exception.TucsonOperationNotPossibleException;

import alice.tucson.service.LanguageInfo;
import alice.tucson.service.TucsonLanguageManager;
import alice.tucson.service.TucsonOperation;

import alice.tucson.types.IAgentIdResolver;
import alice.tucson.types.IContainerResolver;
import alice.tucson.types.IResolver;
import alice.tucson.types.ITCIdResolver;
import alice.tuplecentre.api.ITupleCentreOperation;
import alice.tuplecentre.api.InspectableEventListener;
import alice.tuplecentre.api.ObservableEventListener;
import alice.tuplecentre.core.OperationCompletionListener;

@SuppressWarnings("serial")
public class Respect22Resolver implements IContainerResolver, ITCIdResolver, IAgentIdResolver, IResolver, Serializable
{
	private static LanguageInfo lInfo;

	public Respect22Resolver() {}

	public LanguageInfo getInfo()
	{
		lInfo = new LanguageInfo(TucsonLanguageManager.RESPECT22);
		lInfo.setResolverComponentName(TucsonLanguageManager.RESOLVERPATH + "Respect22Resolver");
		return lInfo;
	}

	public boolean tupleCentreCreator(TucsonTupleCentreId id, int qSize) throws TucsonInstantiationNotPossibleException
	{			
		try
		{
			return (RespectTCContainer.getRespectTCContainer()).createRespectTC((TupleCentreId)id.getInternalTupleCentreId(), qSize);
		}
		catch (InstantiationNotPossibleException e)
		{
			throw new TucsonInstantiationNotPossibleException();
		}
	}
	
	public Object tupleCentreProvider(TucsonTupleCentreId id) throws TucsonInstantiationNotPossibleException
	{			
		try
		{
			return (RespectTCContainer.getRespectTCContainer()).getTC((TupleCentreId)id.getInternalTupleCentreId());
		}
		catch (InstantiationNotPossibleException e)
		{
			throw new TucsonInstantiationNotPossibleException();
		}
	}
	
	public Object doManagementOperation(int type, TucsonTupleCentreId tid, Object obj) 
			throws TucsonOperationNotPossibleException, TucsonInvalidLogicTupleException
	{		
		IManagementContext context = null;

		try
		{
			context = (RespectTCContainer.getRespectTCContainer()).getManagementContext((TupleCentreId) tid.getInternalTupleCentreId());
		}
		catch (OperationNotPossibleException e)
		{
			throw new TucsonOperationNotPossibleException();
		}

		if (type == TucsonOperation.abortOpCode())
			return context.abortOperation((Long)obj);
		if (type == TucsonOperation.set_specCode())
		{
			// andrˆ solo nel contesto di gestione e richiederˆ un BehaviourSpecification ...
			try
			{
				context.setSpec(new RespectSpecification(((LogicTuple) obj).getArg(0).getName()));
			}
			catch (InvalidSpecificationException e)
			{
				e.printStackTrace();
			}
			catch (InvalidTupleOperationException e)
			{
				e.printStackTrace();
			}
			return null;
		}
		if (type == TucsonOperation.get_specCode())
		{
			// andrˆ solo nel contesto di gestione e ritornerˆ un BehaviourSpecification ...
			return new LogicTuple(context.getSpec().toString());
		}
		if (type == TucsonOperation.getTRSetCode())
		{
			return context.getTRSet((LogicTuple) obj);
		}
		if (type == TucsonOperation.getTSetCode())
		{
			return context.getTSet((LogicTuple) obj);
		}
		if (type == TucsonOperation.getWSetCode())
		{
			return context.getWSet((LogicTuple) obj);
		}
		if (type == TucsonOperation.setWSetCode())
		{
			context.setWSet((List<LogicTuple>) obj);
		}
		if (type == TucsonOperation.goCmdCode())
		{
			try
			{
				context.goCommand();
			}
			catch (OperationNotPossibleException e)
			{
				e.printStackTrace();
			}
		}
		if (type == TucsonOperation.nextStepCode())
		{
			try
			{
				context.nextStepCommand();
			}
			catch (OperationNotPossibleException e)
			{
				e.printStackTrace();
			}
		}
		if (type == TucsonOperation.setMngModeCode())
		{
			context.setManagementMode((Boolean)obj);
		}
		if (type == TucsonOperation.setSpyCode())
		{
			context.setSpy((Boolean)obj);
		}
		if (type == TucsonOperation.stopCmdCode())
		{
			try
			{
				context.stopCommand();
			}
			catch (OperationNotPossibleException e)
			{
				e.printStackTrace();
			}
		}		
		if (type == TucsonOperation.addObsCode())
		{
			context.addObserver((ObservableEventListener)obj);
		}
		if (type == TucsonOperation.rmvObsCode())
		{
			context.removeObserver((ObservableEventListener)obj);
		}
		if (type == TucsonOperation.hasObsCode())
		{
			return context.hasObservers();
		}
		if (type == TucsonOperation.addInspCode())
		{
			context.addInspector((InspectableEventListener)obj);
		}
		if (type == TucsonOperation.rmvInspCode())
		{
			context.removeInspector((InspectableEventListener)obj);
		}
		if (type == TucsonOperation.hasInspCode())
		{
			return context.hasInspectors();
		}
		if (type == TucsonOperation.reset())
		{
			// da finire
			return context.hasInspectors();
		}
		
		return null;
	}
	
	public ITupleCentreOperation doNonBlockingOperation(int type, TucsonAgentId aid, TucsonTupleCentreId tid,
			LogicTuple t, OperationCompletionListener l) 
			throws TucsonOperationNotPossibleException, TucsonInvalidLogicTupleException
	{		
		ITupleCentreOperation res = null;
		INonBlockingContext context = null;

		try
		{
			context = (RespectTCContainer.getRespectTCContainer()).getNonBlockingContext((TupleCentreId) tid.getInternalTupleCentreId());			
			if (type == TucsonOperation.outCode())
			{
				System.out.println("[Respect22Resolver] operazione di out");
				return context.out((AgentId) aid.getInternalAgentId(), t, l);				
			}
			if (type == TucsonOperation.inCode())
				return context.in((AgentId) aid.getInternalAgentId(), t, l);
			if (type == TucsonOperation.inpCode())
				return context.inp((AgentId) aid.getInternalAgentId(), t, l);
			if (type == TucsonOperation.rdCode())
				return context.rd((AgentId) aid.getInternalAgentId(), t, l);
			if (type == TucsonOperation.rdpCode())
				return context.rdp((AgentId) aid.getInternalAgentId(), t, l);
		}
		catch (InvalidLogicTupleException e)
		{
			throw new TucsonInvalidLogicTupleException();
		}
		catch (OperationNotPossibleException e)
		{
			throw new TucsonOperationNotPossibleException();
		}
		return res;		
	}
	
	public ITupleCentreOperation doNonBlockingSpecOperation(int type, TucsonAgentId aid, TucsonTupleCentreId tid,
			LogicTuple t, OperationCompletionListener l) throws TucsonOperationNotPossibleException, TucsonInvalidLogicTupleException
	{		
		ITupleCentreOperation res = null;
		INonBlockingSpecContext context = null;

		try
		{
			context = (RespectTCContainer.getRespectTCContainer()).getNonBlockingSpecContext((TupleCentreId) tid.getInternalTupleCentreId());			
		
			if (type == TucsonOperation.out_sCode())
				return context.out_s((AgentId) aid.getInternalAgentId(), t, l);
			if (type == TucsonOperation.in_sCode())
				return context.in_s((AgentId) aid.getInternalAgentId(), t, l);
			if (type == TucsonOperation.inp_sCode())
				return context.inp_s((AgentId) aid.getInternalAgentId(), t, l);
			if (type == TucsonOperation.rd_sCode())
				return context.rd_s((AgentId) aid.getInternalAgentId(), t, l);
			if (type == TucsonOperation.rdp_sCode())
				return context.rdp_s((AgentId) aid.getInternalAgentId(), t, l);
		}
		catch (InvalidLogicTupleException e)
		{
			throw new TucsonInvalidLogicTupleException();
		}
		catch (OperationNotPossibleException e)
		{
			throw new TucsonOperationNotPossibleException();
		}

		return res;		
	}
	
	public Object doBlockingOperation(int type, TucsonAgentId aid, TucsonTupleCentreId tid, Object o)
	throws TucsonOperationNotPossibleException, TucsonInvalidLogicTupleException
	{
		IBlockingContext context = null;
		try
		{
			context = (IBlockingContext) (RespectTCContainer.getRespectTCContainer()).getBlockingContext((TupleCentreId) tid.getInternalTupleCentreId());			
			
			if (type == TucsonOperation.outCode())
			{
				//System.out.println("OK");
				// La out restituisce un "null" --> la "out" di ReSpecT ritorna un void 
				context.out((AgentId) aid.getInternalAgentId(), (LogicTuple) o);
				return o;
			}
			if (type == TucsonOperation.inCode())
				return context.in((AgentId) aid.getInternalAgentId(), (LogicTuple) o);
			if (type == TucsonOperation.inpCode())
			{
				//System.out.println("OK" + context.inp((AgentId) aid.getInternalAgentId(), (LogicTuple) t));
				return context.inp((AgentId) aid.getInternalAgentId(), (LogicTuple) o);
			}
			if (type == TucsonOperation.rdCode())
				return context.rd((AgentId) aid.getInternalAgentId(), (LogicTuple) o);
			if (type == TucsonOperation.rdpCode())
				return context.rdp((AgentId) aid.getInternalAgentId(), (LogicTuple) o);
			if (type == TucsonOperation.get_Code())
				return context.get((AgentId) aid.getInternalAgentId());
			if (type == TucsonOperation.set_Code())
			{
				//System.out.println((List<LogicTuple>) o);
				context.set((AgentId) aid.getInternalAgentId(), (List<LogicTuple>) o);
				return o;
			}
						
		}
		catch (InvalidLogicTupleException e)
		{
			throw new TucsonInvalidLogicTupleException();
		}
		catch (OperationNotPossibleException e)
		{
			throw new TucsonOperationNotPossibleException();
		}		
		return null;
	}

	public Object doBlockingSpecOperation(int type, TucsonAgentId aid, TucsonTupleCentreId tid, LogicTuple t)
	throws TucsonOperationNotPossibleException, TucsonInvalidLogicTupleException, TucsonInvalidSpecificationException
	{
		LogicTuple res = null;		
		IBlockingSpecContext context = null;
		
		try
		{
			context = (IBlockingSpecContext) (RespectTCContainer.getRespectTCContainer()).getBlockingSpecContext((TupleCentreId) tid.getInternalTupleCentreId());

			if (type == TucsonOperation.out_sCode())
			{
				context.out_s((AgentId) aid.getInternalAgentId(), (LogicTuple) t);
				return res;
			}
			if (type == TucsonOperation.in_sCode())
				return context.in_s((AgentId) aid.getInternalAgentId(), (LogicTuple) t);
			if (type == TucsonOperation.inp_sCode())
				return context.inp_s((AgentId) aid.getInternalAgentId(), (LogicTuple) t);
			if (type == TucsonOperation.rdCode())
				return context.rd_s((AgentId) aid.getInternalAgentId(), (LogicTuple) t);
			if (type == TucsonOperation.rdp_sCode())
				return context.rdp_s((AgentId) aid.getInternalAgentId(), (LogicTuple) t);	
			if (type == TucsonOperation.set_specCode())
			{
				// andrˆ solo nel contesto di gestione e richiederˆ un BehaviourSpecification ...
				((BlockingSpecContext)context).setSpec((AgentId) aid.getInternalAgentId(), new RespectSpecification(((LogicTuple) t).getArg(0).getName()));
				return res;
			}
			if (type == TucsonOperation.get_specCode())
			{
				// andrˆ solo nel contesto di gestione e ritornerˆ un BehaviourSpecification ...
				res = new LogicTuple(((BlockingSpecContext)context).getSpec((AgentId) aid.getInternalAgentId()).toString());
				return res;
			}
		}
		catch (InvalidLogicTupleException e)
		{
			throw new TucsonInvalidLogicTupleException();
		}
		catch (OperationNotPossibleException e)
		{
			throw new TucsonOperationNotPossibleException();
		}
		catch (InvalidSpecificationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new TucsonInvalidSpecificationException();
		}
		catch (InvalidTupleOperationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new TucsonOperationNotPossibleException();
		}	
		
		return res;
	}
	
	public Object tupleCentreIdConstructor(Object id) throws TucsonInvalidTupleCentreIdException
	{
		if (id.getClass().getName().equals("alice.respect.api.TupleCentreId"))
		{
			return id;
		}
		else
		{
			try
			{
				return new TupleCentreId((String) id);
			}
			catch (InvalidTupleCentreIdException e)
			{
				throw new TucsonInvalidTupleCentreIdException();
			}
		}
	}

	public boolean tupleCentreIdCheckSyntax(Object id)
	{
		return true;
	}

	public String tupleCentreIdGetNode(Object id)
	{
		return ((TupleCentreId) id).getNode();
	}

	public String tupleCentreIdGetName(Object id)
	{
		return ((TupleCentreId) id).getName();
	}

	public String tupleCentreIdToString(Object id)
	{
		return ((TupleCentreId) id).toString();
	}

	public Object agentIdConstructor(Object id) throws TucsonInvalidAgentIdException
	{
		if (id.getClass().getName().equals("alice.respect.api.AgentId"))
		{
			return id;
		}
		else
		{
			try
			{
				return new AgentId((String) id);
			}
			catch (InvalidAgentIdException e)
			{
				throw new TucsonInvalidAgentIdException();
			}
		}
	}

	public boolean agentIdCheckSyntax(Object id)
	{
		return true;
	}

	public String agentIdToString(Object id)
	{
		return ((AgentId) id).toString();
	}
}
