package alice.logictuple;

import alice.logictuple.exceptions.InvalidLogicTupleException;
import alice.tuprolog.Term;

/**
 * Class representing a BioTuple as extension of LogicTuple, to add the multiplicity
 * 
 */

public class BioTuple extends LogicTuple{

	/** the multiplicity of the tuple */
	private long mult;
	
	/**
	 * Constructs a bio tuple providing the tuple name, argument list and the multiplicity
	 * 
	 * @param name
	 *            the name of the tuple (the functor)
	 * @param list
	 *            the list of tuple argument
	 *@param multiplicity
	 *            the multiplicity of the tuple              
	 */
	public BioTuple(String name, TupleArgument[] list, long multiplicity)
	{
		super(name, list);
		mult = multiplicity;
	}
	
	/**
	 * Constructs a bio tuple providing the tuple name and the multiplicity, without argument
	 * 
	 * @param name
	 *            the name of the tuple (the functor)
	 * @param multiplicity
	 *            the multiplicity of the tuple           
	 *            
	 */
	public BioTuple(String name, long multiplicity)
	{
		super(name);
		mult = multiplicity;
	}
	
	/**
	 * Constructs the bio tuple providing the tuple name, one argument and the multiplicity
	 * 
	 * @param name
	 *            the name of the tuple (the functor)           
	 * @param t1
	 *            the tuple argument
	 * @param multiplicity
	 *            the multiplicity of the tuple 
	 */
	public BioTuple(String name, TupleArgument t1, long multiplicity)
	{
		super(name,t1);
		mult = multiplicity;
	}

	/**
	 * Constructs the bio tuple providing the tuple name, two arguments and the multiplicity
	 * 
	 * @param name
	 *            the name of the tuple (the functor)
	 * @param t1
	 *            the first tuple argument
	 * @param t2
	 *            the second tuple argument
	 * @param multiplicity
	 *            the multiplicity of the tuple
	 */
	public BioTuple(String name, TupleArgument t1, TupleArgument t2, long multiplicity)
	{
		super(name,t1,t2);
		mult = multiplicity;
	}

	/**
	 * Constructs the bio tuple providing the tuple name, three arguments and the multiplicity
	 * 
	 * @param name
	 *            the name of the tuple (the functor)
	 * @param t1
	 *            the first tuple argument
	 * @param t2
	 *            the second tuple argument
	 * @param t3
	 *            the third tuple argument
	 * @param multiplicity
	 *            the multiplicity of the tuple           
	 */
	public BioTuple(String name, TupleArgument t1, TupleArgument t2, TupleArgument t3, long multiplicity)
	{
		super(name, t1, t2, t3);
		mult = multiplicity;
	}

	/**
	 * Constructs the bio tuple providing the tuple name, four arguments and the multiplicity
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
	 * @param multiplicity
	 *            the multiplicity of the tuple
	 */
	public BioTuple(String name, TupleArgument t1, TupleArgument t2, TupleArgument t3, TupleArgument t4, long multiplicity)
	{
		super(name, t1, t2, t3, t4);
		mult = multiplicity;
	}

	/**
	 * Constructs the bio tuple providing the tuple name, five arguments and the multiplicity
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
	 * @param multiplicity
	 *            the multiplicity of the tuple
	 */
	public BioTuple(String name, TupleArgument t1, TupleArgument t2, TupleArgument t3, TupleArgument t4, TupleArgument t5, long multiplicity)
	{
		super(name, t1, t2, t3, t4, t5);
		mult = multiplicity;
	}

	/**
	 * Constructs the bio tuple providing the tuple name, six arguments and the multiplicity
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
	 * @param multiplicity
	 *            the multiplicity of the tuple
	 */
	public BioTuple(String name, TupleArgument t1, TupleArgument t2, TupleArgument t3, TupleArgument t4, TupleArgument t5,
			TupleArgument t6, long multiplicity)
	{
		super(name, t1, t2, t3, t4, t5, t6);
		mult = multiplicity;
	}

	/**
	 * Constructs the bio tuple providing the tuple name, seven arguments and the multiplicity
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
	 * @param multiplicity
	 *            the multiplicity of the tuple
	 */
	public BioTuple(String name, TupleArgument t1, TupleArgument t2, TupleArgument t3, TupleArgument t4, TupleArgument t5,
			TupleArgument t6, TupleArgument t7, long multiplicity)
	{
		super(name, t1, t2, t3, t4, t5, t6, t7);
		mult = multiplicity;
	}

	/**
	 * Constructs the bio tuple from a tuple argument (free form of construction) and the multiplicity
	 * 
	 * @param t
	 *            the tuple argument
	 * @param multiplicity
	 *            the multiplicity of the tuple
	 */
	public BioTuple(TupleArgument t, long multiplicity)
	{
		super(t);
		mult = multiplicity;
	}
	
	/**
	 * Constructs the bio tuple from a tuprolog term and the multiplicity
	 * 
	 * @param t
	 *            the tuprolog term
	 * @param multiplicity
	 *            the multiplicity of the tuple
	 */
	public BioTuple(Term t, long multiplicity)
	{
		super(t);
		mult = multiplicity;
	}

	/**
	 * Empty constructor
	 */
	public BioTuple(){
	}
	
	/**
	 * Gets the multiplicity of the tuple 
	 * 
	 * @return the multiplicity of the tuple
	 */
	
	public long getMultiplicity(){
		return mult;
	}
	
	/**
	 * Sets the multiplicity of the tuple
	 * @param the multiplicity value
	 */
	public void setMultiplicity(long m){
		mult = m;
	}
	
	/**
	 * Gets the string representation of the bio tuple
	 * 
	 * @return the string representing the bio tuple
	 */
	public String toString()
	{
		try
		{
			String tupla = info.toString();
			return "biotuple("+tupla+","+mult+")";
		}
		catch (Exception ex)
		{
			return null;
		}
	}
	
	/**
	 * Static service to get a bio tuple from a textual representation and the multiplicity
	 * 
	 * @param st
	 *            the text representing the tuple
	 * @param multiplicity
	 *            the multiplicity of the tuple
	 * @return the bio tuple interpreted from the text
	 * @exception InvalidLogicTupleException
	 *                if the text does not represent a valid logic tuple
	 */
	public static LogicTuple parse(String st, int multiplicity) throws InvalidLogicTupleException
	{
		try
		{
			Term t = alice.tuprolog.Term.createTerm(st);
			return new BioTuple(new TupleArgument(t),multiplicity);
		}
		catch (Exception ex)
		{
			throw new InvalidLogicTupleException();
		}
	}
	
	
}