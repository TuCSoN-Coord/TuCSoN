/*
 * Logic Tuple Communication Language - Copyright (C) 2001-2002  aliCE team at deis.unibo.it
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
package alice.logictuple;

//import alice.tuprolog.*;
import alice.tucson.service.Node;
import alice.tuprolog.Prolog;
import alice.tuprolog.Term;

/**
 * Class representing a logic tuple.
 * 
 * 
 * @see TupleArgument
 * @see alice.tuplecentre.api.Tuple
 * @see alice.tuplecentre.api.TupleTemplate
 * 
 * @author aricci
 * @version 1.0
 */
public class LogicTuple implements alice.tuplecentre.api.Tuple, alice.tuplecentre.api.TupleTemplate, java.io.Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** the information content of logic tuple */
	private TupleArgument info;

	/**
	 * Constructs a logic tuple providing the tuple name and argument list
	 * 
	 * @param name
	 *            the name of the tuple (the functor)
	 * @param list
	 *            the list of tuple argument
	 */
	public LogicTuple(String name, TupleArgument[] list)
	{
		info = new Value(name, list);
	}

	/**
	 * Constructs the logic tuple providing the tuple name, without arguments
	 * 
	 * @param name
	 *            the name of the tuple (the functor)
	 */
	public LogicTuple(String name)
	{
		info = new Value(name);
	}

	/**
	 * Constructs the logic tuple providing the tuple name and one argument
	 * 
	 * @param name
	 *            the name of the tuple (the functor)
	 * @param t1
	 *            the tuple argument
	 */
	public LogicTuple(String name, TupleArgument t1)
	{
		info = new Value(name, t1);
	}

	/**
	 * Constructs the logic tuple providing the tuple name and two arguments
	 * 
	 * @param name
	 *            the name of the tuple (the functor)
	 * @param t1
	 *            the first tuple argument
	 * @param t2
	 *            the second tuple argument
	 */
	public LogicTuple(String name, TupleArgument t1, TupleArgument t2)
	{
		info = new Value(name, t1, t2);
	}

	/**
	 * Constructs the logic tuple providing the tuple name and three arguments
	 * 
	 * @param name
	 *            the name of the tuple (the functor)
	 * @param t1
	 *            the first tuple argument
	 * @param t2
	 *            the second tuple argument
	 * @param t3
	 *            the third tuple argument
	 */
	public LogicTuple(String name, TupleArgument t1, TupleArgument t2, TupleArgument t3)
	{
		info = new Value(name, t1, t2, t3);
	}

	/**
	 * Constructs the logic tuple providing the tuple name and four arguments
	 * 
	 * @param name
	 *            the name of the tuple (the functor)
	 * @param t1
	 *            the first tuple argument
	 * @param t2
	 *            the second tuple argument
	 * @param t3
	 *            the third tuple argument
	 * @param t4
	 *            the fourth tuple argument
	 */
	public LogicTuple(String name, TupleArgument t1, TupleArgument t2, TupleArgument t3, TupleArgument t4)
	{
		info = new Value(name, t1, t2, t3, t4);
	}

	/**
	 * Constructs the logic tuple providing the tuple name and five arguments
	 * 
	 * @param name
	 *            the name of the tuple (the functor)
	 * @param t1
	 *            the first tuple argument
	 * @param t2
	 *            the second tuple argument
	 * @param t3
	 *            the third tuple argument
	 * @param t4
	 *            the fourth tuple argument
	 * @param t5
	 *            the fifth tuple argument
	 */
	public LogicTuple(String name, TupleArgument t1, TupleArgument t2, TupleArgument t3, TupleArgument t4, TupleArgument t5)
	{
		info = new Value(name, t1, t2, t3, t4, t5);
	}

	/**
	 * Constructs the logic tuple providing the tuple name and six arguments
	 * 
	 * @param name
	 *            the name of the tuple (the functor)
	 * @param t1
	 *            the first tuple argument
	 * @param t2
	 *            the second tuple argument
	 * @param t3
	 *            the third tuple argument
	 * @param t4
	 *            the fourth tuple argument
	 * @param t5
	 *            the fifth tuple argument
	 * @param t6
	 *            the sixth tuple argument
	 */
	public LogicTuple(String name, TupleArgument t1, TupleArgument t2, TupleArgument t3, TupleArgument t4, TupleArgument t5,
			TupleArgument t6)
	{
		info = new Value(name, t1, t2, t3, t4, t5, t6);
	}

	/**
	 * Constructs the logic tuple providing the tuple name and seven arguments
	 * 
	 * @param name
	 *            the name of the tuple (the functor)
	 * @param t1
	 *            the first tuple argument
	 * @param t2
	 *            the second tuple argument
	 * @param t3
	 *            the third tuple argument
	 * @param t4
	 *            the fourth tuple argument
	 * @param t5
	 *            the fifth tuple argument
	 * @param t6
	 *            the sixth tuple argument
	 * @param t7
	 *            the seventh tuple argument
	 */
	public LogicTuple(String name, TupleArgument t1, TupleArgument t2, TupleArgument t3, TupleArgument t4, TupleArgument t5,
			TupleArgument t6, TupleArgument t7)
	{
		info = new Value(name, t1, t2, t3, t4, t5, t6, t7);
	}

	/**
	 * Constructs the logic tuple from a tuple argument (free form of
	 * construction)
	 * 
	 * @param t
	 *            the tuple argument
	 */
	public LogicTuple(TupleArgument t)
	{
		info = t;
	}

	/**
	 * Constructs the logic tuple from a tuprolog term
	 * 
	 * @param t
	 *            the tuprolog term
	 */
	public LogicTuple(Term t)
	{
		info = new TupleArgument(t);
	}

	public LogicTuple()
	{
	}

	/**
	 * Gets the number of arguments of this argument supposed to be a structure
	 * 
	 * @return the number of arguments
	 * @throws InvalidTupleOperationException
	 *             if this argument is not a structure or an out of bounds index
	 *             error is issued
	 */
	public int getArity() throws InvalidTupleOperationException
	{
		return info.getArity();
	}

	/**
	 * Gets the name of the logic tuple
	 * 
	 * @return the name of the logic tuple
	 */
	public String getName() throws InvalidTupleOperationException
	{
		return info.getName();
	}

	/**
	 * Gets a argument inside the logic tuple
	 * 
	 * @param index
	 *            the position (index) of the argument
	 * @return the tuple argument if it exists, <code>null</code> otherwise
	 * @throws s
	 *             InvalidTupleOperationException for out of bounds error
	 */
	public TupleArgument getArg(int index) throws InvalidTupleOperationException
	{
		return info.getArg(index);
	}

	/**
	 * Gets an argument (typically a structured value) given its name
	 * 
	 * @param name
	 *            name of the argument
	 * @return the argument (a structured Value) or null if not presemt
	 */
	public TupleArgument getArg(String name)
	{
		return info.getArg(name);
	}

	/**
	 * Gets the argument linked to a variable inside the tuple.
	 * 
	 * Note that this method works only after that the logic tuple has been
	 * resolved, that is the tuple has been subject of matching or unification
	 * with an other tuple: typically this method is used with a tuple used in
	 * or retrieved by a coordination primitive.
	 * 
	 * @param varName
	 *            is the name of the variable
	 * @return the value linked to the variable, in the case tha the variable
	 *         exits inside the tuple, null otherwise
	 */
	public TupleArgument getVarValue(String varName)
	{
		return info.getVarValue(varName);
	}

	/**
	 * Gets the Term representation of the logic tuple
	 * 
	 * @return the logictuple as a term
	 */
	public Term toTerm()
	{
		return info.toTerm();
	}

	/**
	 * Gets the string representation of the logic tuple
	 * 
	 * @return the string representing the logic tuple
	 */
	public String toString()
	{
		try
		{
			return info.toString();
		}
		catch (Exception ex)
		{
			return null;
		}
	}

	/**
	 * Specifies if a logic tuple (as a tuple template) matches with a specified
	 * tuple, typically an other logic tuple
	 * 
	 * @param t
	 *            the matching tuple
	 * @return <code>true</code> if there is matching, <code>false</code>
	 *         otherwise
	 */
	public boolean match(alice.tuplecentre.api.Tuple t)
	{
		try
		{
			LogicTuple tu = (LogicTuple) t;
			Term term_a = info.toTerm();
			Term term_b = tu.info.toTerm();
			// System.out.println("BEFORE MATCH "+term_a+" "+term_b);
			boolean result = term_a.match(term_b);
			// System.out.println("AFTER MATCH "+term_a+" "+term_b);
			// System.out.println("result "+result);
			return result;
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return false;
		}
	}

	/**
	 * Tries to unify a logic tuple (as a tuple template) with a specified
	 * tuple, typically an other logic tuple
	 * 
	 * @param p the prolog engine
	 * @param t
	 *            the matching tuple
	 * @return <code>true</code> if the propagation was successfull,
	 *         <code>false</code> otherwise
	 */
	public boolean propagate(Prolog p, alice.tuplecentre.api.Tuple t)
	{
		try
		{
			LogicTuple tu = (LogicTuple) t;
			Term term_a = info.toTerm();
			Term term_b = tu.info.toTerm();
			/*before*/
			//return term_a.unify(term_b);	//NEW FOR TUPROLOG 2.?? con null,
			
			/*insert by Enrico Rossignoli*/
			return term_a.unify(p,term_b);
			/*end new insert*/
		}
		catch (Exception ex)
		{
			return false;
		}
	}

	/**
	 * Static service to get a Logic tuple from a textual representation
	 * 
	 * @param st
	 *            the text representing the tuple
	 * @return the logic tuple interpreted from the text
	 * @exception InvalidLogicTupleException
	 *                if the text does not represent a valid logic tuple
	 */
	public static LogicTuple parse(String st) throws InvalidLogicTupleException
	{
		try
		{
			/*Before*/
			//Term t = alice.tuprolog.Term.parse(st);
			
			/*insert by Enrico Rossignoli*/
			Term t = alice.tuprolog.Term.createTerm(st);
			/*end new insert*/
			
			return new LogicTuple(new TupleArgument(t));
		}
		catch (Exception ex)
		{
			throw new InvalidLogicTupleException();
		}
	}
}
