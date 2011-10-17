/*
 * ReSpecT - Copyright (C) aliCE team at deis.unibo.it
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
package alice.respect.api;

import alice.respect.core.TupleCentreIdOperatorManager;
import alice.tuprolog.*;

/**
 * Tuple centre identifier for ReSpecT tuple centres
 * 
 * A tuple centre identifier must be a ground logic term.
 * 
 * @author aricci
 */
public class TupleCentreId implements alice.tuplecentre.api.TupleCentreId, java.io.Serializable  //lato SERVER191
{
	private static final long serialVersionUID = 1L;

	
	private static TupleCentreIdOperatorManager opManager = new TupleCentreIdOperatorManager();

	protected alice.tuprolog.Term id;

	protected TupleCentreId()
	{
	}

	/**
	 * Constructs a tuple centre identifier from a string, which must represent
	 * a well-formed ground logic term
	 * 
	 * @param name
	 *            is the textual representation of the identifier
	 * @throws InvalidTupleCentreIdException
	 *             if name is not a well-formed ground logic term
	 */
	public TupleCentreId(String name) throws InvalidTupleCentreIdException
	{
		if(name.indexOf("@")<0){
			name +="@localhost";
		}
		
		try
		{
			/*before*/
			//id = Term.parse(name, opManager);
			/*insert by Enrico Rossignoli*/
			id = Term.createTerm(name, opManager);
			/*end*/
		}
		catch (Exception ex)
		{
			throw new InvalidTupleCentreIdException();
		}
		if (!id.isGround())
		{
			throw new InvalidTupleCentreIdException();
		}
	}

	/**
	 * Constructs a tuple centre identifier, which must be a well-formed ground
	 * logic term
	 * 
	 * @param name
	 *            is the term representing the identifier
	 * @throws InvalidTupleCentreIdException
	 *             if name is not a well-formed ground logic term
	 */
	public TupleCentreId(Term name) throws InvalidTupleCentreIdException
	{
		id = name.getTerm();
		if (!id.isGround())
		{
			throw new InvalidTupleCentreIdException();
		}
	}

	/**
	 * Provides the logic term representation of the identifier
	 * 
	 * @return the term representing the identifier
	 */
	public Term toTerm()
	{
		return id;
	}

	public String toString()
	{
		return id.toString();
	}

	public boolean isAgent()
	{
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isTC()
	{
		// TODO Auto-generated method stub
		return true;
	}

	/**
	 * Gets the string representation of the tuple centre name
	 * 
	 * @return the ReSpecT node identifier
	 */
	public String getName()
	{
		/*before*/
		//if (id.isStruct())
		/*insert by Enrico Rossignoli*/
		if (id instanceof alice.tuprolog.Struct)
		/*end*/
		{
			Struct sid = (Struct) id;
			if (sid.getArity() == 2 && sid.getName().equals("@"))
			{
				return sid.getArg(0).getTerm().toString();
			}
		}
		return id.toString();
	}

	/**
	 * Gets localhost ReSpecT has no net infrastructure
	 * 
	 * @return the node identifier (localhost)
	 */
	public String getNode()
	{
		/*before*/
		//if (id.isStruct())
		/*insert by Enrico Rossignoli*/
		if (id instanceof alice.tuprolog.Struct)
		/*end*/
		{
			Struct sid = (Struct) id;
			if (sid.getArity() == 2 && sid.getName().equals("@"))
			{
				return sid.getArg(1).getTerm().toString();
			}
		}
		return "localhost";
	}

	/*
	 * public static void main(String[] args) throws
	 * InvalidTupleCentreIdException{
	 * 
	 * TupleCentreId id = new TupleCentreId("default@localhost");
	 * 
	 * System.out.println("tc name: "+id.getName());
	 *  }
	 */

	public boolean equals(Object o)
	{

		return this.id.equals(((TupleCentreId) o).id);

	}

	public static void main(String[] arg)
	{
		try
		{
			//System.out.println("allora" + new TupleCentreId("tc1@localhost").equals(new TupleCentreId("tc1@localhost")));
			//System.out.println(new TupleCentreId("tc1@localhost").getNode());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public boolean isEnv() {
		// TODO Auto-generated method stub
		return false;
	}
}
