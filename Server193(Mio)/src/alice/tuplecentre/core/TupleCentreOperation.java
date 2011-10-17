/*
 * Tuple Centre media - Copyright (C) 2001-2002  aliCE team at deis.unibo.it
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
package alice.tuplecentre.core;

import java.util.List;

import alice.tuplecentre.api.ITupleCentreOperation;
import alice.tuplecentre.api.Tuple;
import alice.tuplecentre.api.TupleTemplate;
import alice.tuprolog.Prolog;

/**
 * This class represents an Operation on a tuple centre.
 * 
 * @author aricci
 * 
 */
public abstract class TupleCentreOperation implements ITupleCentreOperation
{
	protected static final int OPTYPE_IN = 1;
	protected static final int OPTYPE_RD = 2;
	protected static final int OPTYPE_OUT = 3;
	protected static final int OPTYPE_INP = 4;
	protected static final int OPTYPE_RDP = 5;
	protected static final int OPTYPE_NO = 6;
	protected static final int OPTYPE_GET = 7;
	protected static final int OPTYPE_SET = 8;

	protected static final int OPTYPE_IN_S = 11;
	protected static final int OPTYPE_RD_S = 12;
	protected static final int OPTYPE_OUT_S = 13;
	protected static final int OPTYPE_INP_S = 14;
	protected static final int OPTYPE_RDP_S = 15;

	public static final int OPTYPE_SET_SPEC = 16;
	public static final int OPTYPE_GET_SPEC = 17;
	
	// da 18 a 21 in RespectOperation
	
	public static final int OPTYPE_ABORT_OP = 22;
	public static final int OPTYPE_SET_MNG_MODE = 23;
	public static final int OPTYPE_STOP_CMD = 24;
	public static final int OPTYPE_GO_CMD = 25;
	public static final int OPTYPE_NEXT_STEP = 26;
	public static final int OPTYPE_SET_SPY = 27;
	public static final int OPTYPE_GET_TSET = 28;
	public static final int OPTYPE_GET_WSET = 29;	
	public static final int OPTYPE_GET_TRSET = 30;
	// 31 = OPTYPE_EXIT in TucsonOperation
	public static final int OPTYPE_ADD_OBS = 32;
	public static final int OPTYPE_RMV_OBS = 33;
	public static final int OPTYPE_HAS_OBS = 34;
	public static final int OPTYPE_ADD_INSP = 35;
	public static final int OPTYPE_RMV_INSP = 36;
	public static final int OPTYPE_HAS_INSP = 37;
	public static final int OPTYPE_SET_WSET = 38;
	public static final int RESET = 39;
	
	protected boolean operationCompleted;
	protected OperationCompletionListener listener;

	private Tuple tupleArgument;
	private List<Tuple> tupleListArgument;
	private TupleTemplate templateArgument;
	private TCCycleResult result;	
	
	private int type;

	// internal identifier of the operation
	private long id;
	// shared id counter
	private static long idCounter = 0;

	// used for possible synchronisation
	protected Object token;
	private Prolog p;

	private TupleCentreOperation(Prolog p, int type)
	{
		operationCompleted = false;
		this.result = new TCCycleResult();
		this.type = type;
		token = new Object();
		id = idCounter;
		idCounter++;
		this.p = p;
	}

	protected TupleCentreOperation(Prolog p, int type, Tuple t)
	{
		this(p, type);
		listener = null;
		this.tupleArgument = t;
	}

	protected TupleCentreOperation(Prolog p, int type, TupleTemplate t)
	{
		this(p, type);
		listener = null;
		this.templateArgument = t;
	}
	
	// Aggiunta per la set
	protected TupleCentreOperation(Prolog p, int type, List<Tuple> tupleList)
	{
		this(p, type);
		listener = null;
		this.tupleListArgument = tupleList;
	}

	protected TupleCentreOperation(Prolog p, int type, Tuple t, OperationCompletionListener l)
	{
		this(p, type, t);
		listener = l;
	}

	protected TupleCentreOperation(Prolog p, int type, TupleTemplate t, OperationCompletionListener l)
	{
		this(p, type, t);
		listener = l;
	}
	
	// Aggiunta per la set
	protected TupleCentreOperation(Prolog p, int type, List<Tuple> tupleList, OperationCompletionListener l)
	{
		this(p, type, tupleList);
		listener = l;
	}

	public void setListener(OperationCompletionListener l)
	{
		this.listener = l;
	}
	
	public TupleTemplate getTemplateArgument()
	{
		return templateArgument;
	}

	public Tuple getTupleArgument()
	{
		return tupleArgument;
	}
	
	// Aggiunta per la set
	public List<Tuple> getTupleListArgument()
	{
		return tupleListArgument;
	}
	
	// Aggiunta per la get
	public List<Tuple> getResultList()
	{
		return result.getResultList();
	}
	
	public Tuple getResult()
	{
		return result.getResult();
	}

	public void setResult(Tuple t)
	{
		result.setResult(t);
		if (this.templateArgument != null)
			this.templateArgument.propagate(p, t);
		this.tupleArgument = t;
		//System.out.println("[RespectOperation.setResult]: "+this.tupleArgument);
		result.setEndTime(System.currentTimeMillis());
	}
	
	public void setResultList(List<Tuple> t)
	{
		result.setResultList(t);
	
		result.setEndTime(System.currentTimeMillis());
	}

	public boolean isIn()
	{
		return type == OPTYPE_IN;
	}

	public boolean isRd()
	{
		return type == OPTYPE_RD;
	}

	public boolean isOut()
	{
		return type == OPTYPE_OUT;
	}

	public boolean isInp()
	{
		return type == OPTYPE_INP;
	}

	public boolean isRdp()
	{
		return type == OPTYPE_RDP;
	}

	public boolean isSetSpec()
	{
		return type == OPTYPE_SET_SPEC;
	}

	public boolean isGetSpec()
	{
		return type == OPTYPE_GET_SPEC;
	}

	public boolean isIn_s()
	{
		return type == TupleCentreOperation.OPTYPE_IN_S;
	}

	public boolean isRd_s()
	{
		return type == TupleCentreOperation.OPTYPE_RD_S;
	}

	public boolean isOut_s()
	{
		return type == TupleCentreOperation.OPTYPE_OUT_S;
	}

	public boolean isInp_s()
	{
		return type == TupleCentreOperation.OPTYPE_INP_S;
	}
	
	public boolean isGet()
	{
		return type == TupleCentreOperation.OPTYPE_GET;
	}
	
	public boolean isSet()
	{
		return type == TupleCentreOperation.OPTYPE_SET;
	}

	public boolean isRdp_s()
	{
		return type == TupleCentreOperation.OPTYPE_RDP_S;
	}
	
	/**
	 * Tests if the operation is completed
	 * 
	 * @return true if the operation is completed
	 */
	public boolean isOperationCompleted()
	{
		return operationCompleted;
	}

	public int getType()
	{
		return this.type;
	}

	/**
	 * Tests if the result is defined
	 * 
	 * @return true if the result is defined
	 */
	public boolean isResultDefined()
	{

		if (isInp() || isRdp() || isInp_s() || isRdp_s() || isGet())
		{

			return this.isOperationCompleted();

		}

		return this.result.isResultDefined();
	}

	/**
	 * Changes the state of the operation to complete.
	 * 
	 */
	public void notifyCompletion()
	{
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

	/**
	 * Wait for operation completion
	 * 
	 * Current execution flow is blocked until the operation is completed
	 */
	public void waitForOperationCompletion()
	{
		try
		{
			synchronized (token)
			{
				while (!operationCompleted)
				{
					token.wait();
				}
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	/**
	 * Wait for operation completion, with time out
	 * 
	 * Current execution flow is blocked until the operation is completed or a
	 * maximum waiting time is elapsed
	 * 
	 * @param ms
	 *            maximum waiting time
	 * @throws OperationTimeOutException
	 */
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
					throw new OperationTimeOutException();
				}
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public void removeListener()
	{
		this.listener = null;
	}

	public void addListener(OperationCompletionListener listener)
	{
		this.listener = listener;
	}

	/**
	 * Get operation identifier
	 * 
	 * @return Operation identifier
	 */
	public long getId()
	{
		return id;
	}
}
