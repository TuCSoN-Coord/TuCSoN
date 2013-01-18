package alice.logictuple;

import alice.logictuple.exceptions.*;
import alice.tuprolog.Struct;
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
	public BioTuple(String name, TupleArgument[] list, long multiplicity) throws InvalidMultiplicityException
	{
		super(name, list);
		if(!(multiplicity>0))
			throw new InvalidMultiplicityException();
		mult = multiplicity;
	}
	
	/**
	 * Constructs a bio tuple providing the tuple name and the multiplicity, without argument
	 * 
	 * @param name
	 *            the name of the tuple (the functor)
	 * @param multiplicity
	 *            the multiplicity of the tuple           
	 * @throws InvalidMultiplicityException 
	 *            
	 */
	public BioTuple(String name, long multiplicity) throws InvalidMultiplicityException
	{
		super(name);
		if(!(multiplicity>0))
			throw new InvalidMultiplicityException();
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
	 * @throws InvalidMultiplicityException 
	 */
	public BioTuple(String name, TupleArgument t1, long multiplicity) throws InvalidMultiplicityException
	{
		super(name,t1);
		if(!(multiplicity>0))
			throw new InvalidMultiplicityException();
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
	 * @throws InvalidMultiplicityException 
	 */
	public BioTuple(String name, TupleArgument t1, TupleArgument t2, long multiplicity) throws InvalidMultiplicityException
	{
		super(name,t1,t2);
		if(!(multiplicity>0))
			throw new InvalidMultiplicityException();
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
	 * @throws InvalidMultiplicityException 
	 */
	public BioTuple(String name, TupleArgument t1, TupleArgument t2, TupleArgument t3, long multiplicity) throws InvalidMultiplicityException
	{
		super(name, t1, t2, t3);
		if(!(multiplicity>0))
			throw new InvalidMultiplicityException();
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
	 * @throws InvalidMultiplicityException 
	 */
	public BioTuple(String name, TupleArgument t1, TupleArgument t2, TupleArgument t3, TupleArgument t4, long multiplicity) throws InvalidMultiplicityException
	{
		super(name, t1, t2, t3, t4);
		if(!(multiplicity>0))
			throw new InvalidMultiplicityException();
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
	 * @throws InvalidMultiplicityException 
	 */
	public BioTuple(String name, TupleArgument t1, TupleArgument t2, TupleArgument t3, TupleArgument t4, TupleArgument t5, long multiplicity) throws InvalidMultiplicityException
	{
		super(name, t1, t2, t3, t4, t5);
		if(!(multiplicity>0))
			throw new InvalidMultiplicityException();
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
	 * @throws InvalidMultiplicityException 
	 */
	public BioTuple(String name, TupleArgument t1, TupleArgument t2, TupleArgument t3, TupleArgument t4, TupleArgument t5,
			TupleArgument t6, long multiplicity) throws InvalidMultiplicityException
	{
		super(name, t1, t2, t3, t4, t5, t6);
		if(!(multiplicity>0))
			throw new InvalidMultiplicityException();
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
	 * @throws InvalidMultiplicityException 
	 */
	public BioTuple(String name, TupleArgument t1, TupleArgument t2, TupleArgument t3, TupleArgument t4, TupleArgument t5,
			TupleArgument t6, TupleArgument t7, long multiplicity) throws InvalidMultiplicityException
	{
		super(name, t1, t2, t3, t4, t5, t6, t7);
		if(!(multiplicity>0))
			throw new InvalidMultiplicityException();
		mult = multiplicity;
	}

	/**
	 * Constructs the bio tuple from a tuple argument (free form of construction) and the multiplicity
	 * 
	 * @param t
	 *            the tuple argument
	 * @param multiplicity
	 *            the multiplicity of the tuple
	 * @throws InvalidMultiplicityException 
	 */
	public BioTuple(TupleArgument t, long multiplicity) throws InvalidMultiplicityException
	{
		super(t);
		if(multiplicity<=0)
			throw new InvalidMultiplicityException();
		mult = multiplicity;
	}
	
	/**
	 * Constructs the bio tuple from a tuprolog term and the multiplicity
	 * 
	 * @param t
	 *            the tuprolog term
	 * @param multiplicity
	 *            the multiplicity of the tuple
	 * @throws InvalidMultiplicityException 
	 */
	public BioTuple(Term t, long multiplicity) throws InvalidMultiplicityException
	{
		super(t);
		if(multiplicity<=0)
			throw new InvalidMultiplicityException();
		mult = multiplicity;
	}
	
	/**
	 * Constructs the bio tuple from a tuprolog term. Multiplicty auto-set to 1
	 * 
	 * @param t
	 *            the tuprolog term
	 * @throws InvalidMultiplicityException 
	 */
	public BioTuple(Term t)
	{
		super(t);
		mult = 1;
	}

	/**
	 * Empty constructor
	 */
	public BioTuple(){
	}

	//Added to allow to specify null value of multiplicity
	public BioTuple(TupleArgument t, Long multiplicity) throws InvalidMultiplicityException{
		super(t);
		if(multiplicity!=null){
			if(multiplicity<=0)
				throw new InvalidMultiplicityException();
			mult = multiplicity;
		}
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
	 * @throws InvalidMultiplicityException 
	 */
	public void setMultiplicity(long m) throws InvalidMultiplicityException{
		if(m<=0)
			throw new InvalidMultiplicityException();
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
	 * Gets the Term representation of the bio tuple
	 * 
	 * @return the bio tuple as a term
	 */
	public Term toTerm()
	{
		Term tuple = info.toTerm();
		Term multTerm = null;
		
		if(mult==0)
			multTerm = new alice.tuprolog.Var("X");
		else	
			multTerm = new alice.tuprolog.Long(mult);
		
		return new Struct("biotuple", tuple, multTerm);
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
	public static BioTuple parse(String st, long multiplicity) throws InvalidMultiplicityException
	{
		try
		{
			Term t = alice.tuprolog.Term.createTerm(st);
			return new BioTuple(new TupleArgument(t), new Long(multiplicity));
		}
		catch (InvalidMultiplicityException ex)
		{
			throw new InvalidMultiplicityException();
		}
	}
	
	public static BioTuple parse(String st) throws InvalidLogicTupleException
	{
		try
		{
			Term t = alice.tuprolog.Term.createTerm(st);
			return new BioTuple(new TupleArgument(t), null);
		}
		catch (Exception ex)
		{
			throw new InvalidLogicTupleException();
		}
	}
	
	//add for bio primitives
	public static BioTuple parseCLI(String tuple) {
		
		String s1 = tuple.substring(9);
		String s2 = s1.replace(')', ' ');
		String s3 = s2.trim();
		
		String[] elem = (s3.toString()).split(",");
		
		if(elem.length == 1){
			try {
				return BioTuple.parse(elem[0]);
			} catch (InvalidLogicTupleException e) {
				e.printStackTrace();
			}
		}else if(elem.length == 2){
			String name = elem[0];
			long mult = Long.parseLong(elem[1]);
			try {
				return BioTuple.parse(name,mult);
			} catch (InvalidMultiplicityException e) {
				e.printStackTrace();
			}
		}	
		return null;
	}
	
	public boolean isMultGround(){
		if(mult == 0)
			return false;
		else
			return true;		
	}
	
}
