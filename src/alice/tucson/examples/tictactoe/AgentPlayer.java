/*
 * TicTacToe TuCSoN Demo - Copyright (C) 2001 deis.unibo.it
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
package alice.tucson.examples.tictactoe;

import alice.logictuple.*;
import alice.tucson.api.*;
import alice.tucson.api.exception.TucsonException;
import alice.tucson.service.TucsonAgentLanguageManager;
import alice.tucson.service.TucsonLanguageManager;
import alice.tuplecentre.core.TupleCentreOperation;

@SuppressWarnings("serial")
public class AgentPlayer extends Automaton
{
	// game tuple centre id
	TucsonTupleCentreId tid = new TucsonTupleCentreId("tictactoe");
	// game id
	TupleArgument id;
	// role of the player = {o,x}
	TupleArgument role;
	// to handle incoming request from UI
	UserAction userAction = new UserAction();
	// current user interface used to display information
	UserInterface userInterface;

	public AgentPlayer(String id, TucsonTupleCentreId tcid, String language) throws TucsonException
	{
		super(id, language);
		tid = tcid;
	}

	/*
	 * 
	 * agent states
	 */
	public void boot()
	{
		try
		{
			userInterface = new AgentGUI(this);
			// joining game; acquire:
			// - game ID
			// - role

			LogicTuple tuple = this.getContext().in(tid, new LogicTuple("join", new Var("Id"), new Var("Role")), (Long) null);
			// integer number identifying the game
			id = tuple.getArg(0);
			// role can be 'o' or 'x'
			role = tuple.getArg(1);
			tuple = refreshGameStatus();

			// status {yourTurn, opponentTurn, won, lost,stalemate}
			// indicates directly next state
			String status = tuple.getArg(3).toString();
			become(status);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public void yourTurn() throws Exception
	{
		// waiting for UI request
		synchronized (userAction)
		{
			userAction.wait();
		}
		if (!userAction.leaving)
		{
			if (userAction.askAdvice)
			{
				userAction.askAdvice = false;
				LogicTuple t = this.getContext().in(tid, new LogicTuple("suggestion", id, role, new Var("Advice")), (Long) null);
				userAction.nextMove = t.getArg(2);
			}
			LogicTuple tin = this.getContext().in(tid, new LogicTuple("valid", id, role, userAction.nextMove, new Var("Ok")), (Long) null);
			become("opponentTurn");
		}
		else
		{
			this.getContext().out(tid, new LogicTuple("leave", id, role), (Long) null);
			become("end");
		}
	}

	public void opponentTurn() throws Exception
	{
		LogicTuple tuple = refreshGameStatus();
		// status {yourTurn, opponentTurn, won, lost,stalemate}
		// indicates directly next state
		String status = tuple.getArg(3).toString();
		become(status);
	}

	// player has lost
	public void lost() throws Exception
	{
		become("stop");
	}

	public void stalemate() throws Exception
	{
		become("stop");
	}

	public void abandoned() throws Exception
	{
		become("stop");
	}

	// player has won
	public void won() throws Exception
	{
		become("stop");
	}

	public void stop() throws Exception
	{
		// wait for user leaving game
		synchronized (userAction)
		{
			userAction.wait();
		}
		this.getContext().out(tid, new LogicTuple("leave", id, role), (Long) null);
		become("end");
	}

	// helper

	/**
	 * getting info about current game state: view(+Id,+Role,-Grid,-Status)
	 */
	private LogicTuple refreshGameStatus() throws Exception
	{
		LogicTuple tuple = this.getContext().in(tid, new LogicTuple("view", id, role, new Var("Grid"), new Var("Status")), (Long) null);
		if (userInterface != null)
		{
			userInterface.updateInfo(new LogicTuple(tuple.getArg(3)), new LogicTuple(tuple.getArg(2)));
		}
		return tuple;
	}

	/*
	 * INPUT interface for the UI
	 * 
	 * this part of interface is used by actual user interface to interact with
	 * player
	 */

	/**
	 * notify agent of user new move
	 */
	public void onNewMove(int ix, int iy)
	{
		synchronized (userAction)
		{
			if (state.equals("yourTurn"))
			{
				userAction.nextMove = new Value("on", new Value(ix), new Value(iy));
				userAction.notify();
			}
		}
	}

	/**
	 * notfy agent of user game leaving
	 */
	public void onLeave()
	{
		synchronized (userAction)
		{
			userAction.leaving = true;
			userAction.notify();
		}
	}

	/**
	 * notify agent that user wants an advice
	 */
	public void onAskAdvice()
	{
		synchronized (userAction)
		{
			if (state.equals("yourTurn"))
			{
				userAction.askAdvice = true;
				userAction.notify();
			}
		}
	}

	public static void main(String args[]) throws Exception
	{
		if (args.length != 2)
		{
			System.err.println("args: <player name> <IP address of the TuCSoN Node where TicTacToe service is >");
		}
		else
		{
			TucsonAgentLanguageManager.useLanguage(TucsonLanguageManager.DEFLANGUAGE);
			new AgentPlayer(args[0], new TucsonTupleCentreId("tictactoe@'" + args[1] + "'"), TucsonAgentLanguageManager.getCurLanguage().getName()).spawn();
		}
	}

	@Override
	public void operationCompleted(TupleCentreOperation op)
	{
		// TODO Auto-generated method stub		
	}
}

class UserAction
{
	UserAction()
	{
		leaving = false;
		askAdvice = false;
	}

	boolean leaving;
	boolean askAdvice;
	TupleArgument nextMove;
}
