/*
 * TuCSoN coordination infrastructure - Copyright (C) 2001-2002  aliCE team at deis.unibo.it
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
package alice.tucson.api;

import java.lang.reflect.*;

import alice.tucson.api.exception.TucsonInvalidAgentIdException;
import alice.tucson.api.exception.TucsonOperationNotPossibleException;

/**
 * This class defines a state-based TuCSoN Agent.
 * 
 * States are defined by (better private) method of the class. The transition
 * from state to state is enacted by means of the <code>become</code> method.
 */
public abstract class Automaton extends TucsonAgent implements java.io.Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** method name representing state behaviour */
	protected String state = "boot";

	/** arguments value eventually associated to state transition */
	protected Object[] arguments = null;

	/** arguments class eventually associated to state transition */

	static protected Class[] argType;

	/**
	 * Constructs the automaton specifying its identifier
	 * 
	 * @param id
	 *            the automaton identifier
	 * @throws TucsonInvalidAgentIdException 
	 */
	public Automaton(String aid, String language) throws TucsonInvalidAgentIdException
	{
		super(aid, language);
	}

	/**
	 * Changes the state of the automaton
	 * 
	 * @param s
	 *            the state name, which must coincide with the name of a public
	 *            method of the object
	 */
	protected void become(String s)
	{
		if (!state.equals("end"))
		{
			state = s;
			arguments = null;
		}
	}

	/**
	 * Changes the state of the automaton, providing auciliary information
	 * 
	 * @param s
	 *            the state name, which must coincide with the name of a public
	 *            method of the object
	 * @param args
	 *            auxiliary information needed in the next state
	 */
	protected void become(String s, Object[] args)
	{
		if (!state.equals("end"))
		{
			state = s;
			arguments = args;
		}
	}

	/**
	 * Specifies the behaviour of the automaton in its boot state, which is
	 * reached when the automaton start its thread of control
	 */
	protected abstract void boot();

	/**
	 * Defines the automaton behaviour
	 */
	final protected void main()
	{
		try
		{
			argType = new Class[] { Class.forName("[Ljava.lang.Object;") };
		}
		catch (Exception ex)
		{
		}
		while (true)
		{
			try
			{
				if (!state.equals("end"))
				{
					if (arguments == null)
					{
						Method m = this.getClass().getDeclaredMethod(state, null);
						m.setAccessible(true);
						m.invoke(this, null);
					}
					else
					{
						Method m = this.getClass().getDeclaredMethod(state, argType);
						m.setAccessible(true);
						m.invoke(this, arguments);
					}
				}
				else
				{
					end();
					break;
				}
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
				error();
			}
		}
	}

	/**
	 * Specifies the behaviour of the automaton in its end state
	 * @throws TucsonOperationNotPossibleException 
	 */
	protected void end() throws TucsonOperationNotPossibleException
	{
		getContext().exit();
	}

	/**
	 * Specifies the behaviour of the automaton in its error state
	 */
	protected void error()
	{
		become("end");
	}
}
