package alice.tucson.service;

import java.util.LinkedList;
import java.util.List;

import alice.tuplecentre.api.Tuple;
import alice.tuplecentre.api.TupleTemplate;

import alice.tuplecentre.core.*;

import alice.logictuple.*;

import alice.tucson.api.*;


public class TucsonOperation extends TupleCentreOperation implements ITucsonOperation
{	
	private static final int OPTYPE_EXIT = 31;
	private boolean successed;
	private boolean allowed;
	private AgentContextStub context = null;
	
	public TucsonOperation(int type, Tuple t, OperationCompletionListener l, AgentContextStub context)
	{
		/*insert by Enrico Rossignoli*/
		super(null, type, t, l);
		this.context = context;
		successed = false;
	}

	public TucsonOperation(int type, TupleTemplate t, OperationCompletionListener l, AgentContextStub context)
	{
		/*insert by Enrico Rossignoli*/
		super(null, type, t, l);
		this.context = context;
		successed = false;
	}
	
	public static int outCode()
	{
		return OPTYPE_OUT;
	}
	
	public static int inCode()
	{
		return OPTYPE_IN;
	}
	
	public static int rdCode()
	{
		return OPTYPE_RD;
	}
	
	public static int inpCode()
	{
		return OPTYPE_INP;
	}
	
	public static int rdpCode()
	{
		return OPTYPE_RDP;
	}

	public static int set_Code()
	{
		return OPTYPE_SET;
	}
	
	public static int get_Code()
	{
		return OPTYPE_GET;
	}
	
	public static int out_sCode()
	{
		return OPTYPE_OUT_S;
	}
	
	public static int in_sCode()
	{
		return OPTYPE_IN_S;
	}
	
	public static int rd_sCode()
	{
		return OPTYPE_RD_S;
	}
	
	public static int inp_sCode()
	{
		return OPTYPE_INP_S;
	}
	
	public static int rdp_sCode()
	{
		return OPTYPE_RD_S;
	}
	
	public static int set_specCode()
	{
		return OPTYPE_SET_SPEC;
	}
	
	public static int get_specCode()
	{
		return OPTYPE_GET_SPEC;
	}	
	
	public static int exitCode()
	{
		return OPTYPE_EXIT;
	}
	
	public static int abortOpCode()
	{
		return OPTYPE_ABORT_OP;
	}
	
	public static int setMngModeCode()
	{
		return OPTYPE_SET_MNG_MODE;
	}
	
	public static int stopCmdCode()
	{
		return OPTYPE_STOP_CMD;
	}
	
	public static int goCmdCode()
	{
		return OPTYPE_GO_CMD;
	}
	
	public static int nextStepCode()
	{
		return OPTYPE_NEXT_STEP;
	}
	
	public static int setSpyCode()
	{
		return OPTYPE_SET_SPY;
	}
	
	public static int getTSetCode()
	{
		return OPTYPE_GET_TSET;
	}
	
	public static int getWSetCode()
	{
		return OPTYPE_GET_WSET;
	}
	
	public static int setWSetCode()
	{
		return OPTYPE_SET_WSET;
	}
	
	public static int getTRSetCode()
	{
		return OPTYPE_GET_TRSET;
	}
	
	public TucsonContext getTucsonContex()
	{
		return context;
	}
	
	public static int addObsCode()
	{
		return OPTYPE_ADD_OBS;
	}
	
	public static int rmvObsCode()
	{
		return OPTYPE_RMV_OBS;
	}
	
	public static int hasObsCode()
	{
		return OPTYPE_HAS_OBS;
	}
	
	public static int addInspCode()
	{
		return OPTYPE_ADD_INSP;
	}
	
	public static int rmvInspCode()
	{
		return OPTYPE_RMV_INSP;
	}
	
	public static int hasInspCode()
	{
		return OPTYPE_HAS_INSP;
	}
	
	public static int reset()
	{
		return RESET;
	}
	
	public OperationCompletionListener getListener()
	{
		return listener;
	}	
	
	/**
	 * Changes the state of the operation to complete.
	 * 
	 */
	public void notifyCompletion(boolean successed, boolean allowed)
	{				
		this.successed = successed;
		this.allowed = allowed;
		
		if (listener != null)
		{
			operationCompleted = true;
			listener.operationCompleted(this);
		}
		else
		{
			try
			{
				synchronized (token)
				{
					operationCompleted = true;
					token.notifyAll();
				}
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}
	}
	
	public LogicTuple getLogicTupleResult()
	{
		return (LogicTuple) getResult();
	}

	public LogicTuple getLogicTupleArgument()
	{
		if (isOut() | isOut_s() | isSetSpec())
		{
			return (LogicTuple) getTupleArgument();
		}
		else
		{
			return (LogicTuple) getTemplateArgument();
		}
	}
	
	// Necessario per la "get"
	public List<LogicTuple> getTupleResultList()
	{
		List<Tuple> tl = this.getResultList();
		List<LogicTuple> tll = new LinkedList<LogicTuple>();
		
		for(Tuple t : tl)
		{
			tll.add((LogicTuple) t);
		}
		
		return tll;
	}
	
	public void setTupleResultList(List<LogicTuple> tl)
	{
		List<Tuple> t = new LinkedList<Tuple>();
		
		for(LogicTuple logicTuple : tl)
		{
			t.add(logicTuple);
		}
		
		setResultList(t);
	}
	
	// da togliere ...
	public String getSpecResult() throws InvalidTupleOperationException
	{
		return getLogicTupleResult().getArg(0).getName();
	}
	
	public boolean isSuccessed()
	{
		return successed;
	}
	
	public boolean isAllowed()
	{
		return allowed;
	}
	
	public void waitForOperationCompletion(long ms) throws OperationTimeOutException
	{
		try
		{
			synchronized (token)
			{
				if (!operationCompleted)
				{
					token.wait(ms);
				}
				if (!operationCompleted)
				{
					this.context.addOperationExpired(getId());
					throw new OperationTimeOutException();
				}
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
}
