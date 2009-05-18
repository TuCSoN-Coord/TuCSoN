package alice.tucson.service;

import alice.tuplecentre.api.Tuple;
import alice.tuplecentre.api.TupleTemplate;

import alice.tuplecentre.core.*;

import alice.logictuple.*;

import alice.tucson.api.*;


public class TucsonOperation extends TupleCentreOperation implements ITucsonOperation
{	
	private static final int OPTYPE_EXIT = 16;
	private static final int OPTYPE_GET_TSET = 17;
	private static final int OPTYPE_GET_WSET = 18;
	private boolean successed;
	private boolean allowed;
	
	public TucsonOperation(int type, Tuple t, OperationCompletionListener l)
	{
		super(type, t, l);
	}

	public TucsonOperation(int type, TupleTemplate t, OperationCompletionListener l)
	{
		super(type, t, l);
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

	public static int set_specCode()
	{
		return OPTYPE_SET_SPEC;
	}
	
	public static int get_specCode()
	{
		return OPTYPE_GET_SPEC;
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
	
	public static int get_TSet()
	{
		return OPTYPE_GET_TSET;
	}
	
	public static int get_WSet()
	{
		return OPTYPE_GET_WSET;
	}
	
	public static int exitCode()
	{
		return OPTYPE_EXIT;
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
}
