package alice.tucson.service.stubs;

import java.util.HashMap;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import alice.tuplecentre.core.TupleCentreOperation;

import alice.respect.api.*;

import alice.respect.core.RespectOperation;

import alice.tucson.api.ContextEvent;
import alice.tucson.api.TucsonOpId;

import alice.tucson.api.exception.LanguageNotFoundException;
import alice.tucson.api.exception.TucsonInvalidTupleCentreIdException;
import alice.tucson.api.exception.TucsonOperationNotPossibleException;
import alice.tucson.api.exception.UnreachableNodeException;

import alice.tucson.service.InterTupleCentreContext;
import alice.tucson.service.InterTupleCentreContextStub;
import alice.tucson.api.TucsonTupleCentreId;

public class RespectInterTupleCentreContextStub implements ILinkContext
{
	private final static int threadsNumber = 5;
	// It's a static list because the same ITCC Stub (from a well defined tuple
	// centre source) must be used to interact with different tuple centres
	// target
	private static HashMap<String, InterTupleCentreContext> helpList;
	private static ExecutorService exec;
	private alice.tuplecentre.api.TupleCentreId idTo;
	private String lang;

	public RespectInterTupleCentreContextStub(alice.tuplecentre.api.TupleCentreId id)
	{
		idTo = id;
		//lang = language;
		if (helpList == null)
		{
			helpList = new HashMap<String, InterTupleCentreContext>();
		}
		// Create the pool only the first time
		if (exec == null)
		{
			// Multiple threads due to blocking interaction with ITCC Stub
			exec = Executors.newFixedThreadPool(threadsNumber);
		}
	}

	public synchronized void doOperation(TupleCentreId id, TupleCentreOperation op) throws OperationNotPossibleException
	{
		log("Call ITCC Stub");
		Executor ex = new Executor(idTo, lang, id, op, helpList);
		exec.execute(ex);
	}

	private void log(String msg)
	{
		System.out.println("[ReSpecT ITCC STUB] " + msg);
	}

	class Executor extends Thread
	{

		private alice.tuplecentre.api.TupleCentreId toId;
		private String fromLanguage;
		private TupleCentreId fromId;
		private TupleCentreOperation op;
		private HashMap<String, InterTupleCentreContext> helpList;
		private InterTupleCentreContext helper;

		public Executor(alice.tuplecentre.api.TupleCentreId toId, String fromLanguage, TupleCentreId fromId,
				TupleCentreOperation op, HashMap<String, InterTupleCentreContext> helpList)
		{
			this.toId = toId;
			this.fromLanguage = fromLanguage;
			this.fromId = fromId;
			this.op = op;
			this.helpList = helpList;
		}

		public void run()
		{
			try
			{
				// helper = helpList.get(fromId.toString());
				helper = helpList.get(fromId.getNode());
				if (helper == null)
				{
					// One Tucson ITCC Stub for each remote node not tuple
					// centre
					helper = new InterTupleCentreContextStub(new TucsonTupleCentreId(fromId), fromLanguage);
					helpList.put(fromId.getNode(), helper);
				}
			}
			catch (TucsonInvalidTupleCentreIdException e1)
			{
				e1.printStackTrace();
			}
			try
			{
				TucsonOpId opId = helper.doOperation(toId, op.getType(), ((RespectOperation) op).getLogicTupleArgument(),
						fromLanguage);
				ContextEvent ev = helper.waitForCompletion(opId);
				op.setResult(ev.getTuple());
				// Notify ReSpecT of the completion of the call
				// TODO to activate only when the Vm is able to manage
				// op.notifyCompletion();
			}
			catch (TucsonOperationNotPossibleException e)
			{
				e.printStackTrace();
				// TODO gestire eccezioni
			}
			catch (UnreachableNodeException e)
			{
				e.printStackTrace();
				// TODO gestire eccezioni
			}
			catch (LanguageNotFoundException e)
			{
			}
		}

	}
}
