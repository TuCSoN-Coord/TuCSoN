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
import alice.tuprolog.*;

/**
 * Defines the Tuple Centre identifier for TuCSoN
 * logic tuple centres.
 *
 * Tuple Centre identifier is a logic ground term
 *
 * The id can include also the identifier of the TuCSoN
 * node where the tuple centre is located:
 * in this case the logic term is in the form
 *
 *  tupleCentreName @ tucsonNodeIdentifier
 *
 * The template is:  term @ term
 *
 * So in the case of Internet Address used as TuCSoN nodes
 * the node identifier must be enclosed in apices; for instance:
 * '137.204.191.189' or 'lia.deis.unibo.it'
 */
public class TupleCentreId extends alice.respect.TupleCentreId {

    private static TupleCentreIdOperatorManager opManager = new TupleCentreIdOperatorManager();

    /**
     * Constructs a default tuple centre identifier
     *
     * The name is default, the TuCSoN node is localhost
     */
    public TupleCentreId(){
        id = new  Struct("default");
    }

    /**
     * Constructs a tuple centre identifier from a string,
     * which must represent a well-formed ground logic term
     *
     * @param sid is the string representation of the tuple centre identifier;
     *        it must be the string representation of a valid logic term.
     *
     * @throws InvalidTupleCentreIdException if an the tuple centre identifier is not valid
     */
    public TupleCentreId(String sid) throws InvalidTupleCentreIdException {
        try{
            id=Term.parse(sid,opManager);
        } catch (Exception ex){
            throw new InvalidTupleCentreIdException();
        }
        if (!id.isGround()){
            throw new InvalidTupleCentreIdException();
        }
    }


    /**
     * Constructs tuple centre identifier
     *
     * @param name is the name of the tuple centre identifier
     * @throws InvalidTupleCentreIdException if the tuple centre identifier is not valid
     *
     */
    public TupleCentreId(Term name) throws InvalidTupleCentreIdException {
        id = name.getTerm();
        if (!id.isGround()){
            throw new InvalidTupleCentreIdException();
        }
    }

    /**
     * Gets the string representation of the tuple centre name
     *
     * @return the TuCSoN node identifier
     */
    public String getName(){
        if (id.isStruct()){
            Struct sid=(Struct)id;
            if (sid.getArity()==2 && sid.getName().equals("@")){
                return sid.getArg(0).getTerm().toString();
            }
        }
        return id.toString();
    }

    /**
     * Gets string representation of the TuCSoN node
     *
     * @return the TuCSoN node identifier
     */
    public String getNode(){
        if (id.isStruct()){
            Struct sid=(Struct)id;
            if (sid.getArity()==2 && sid.getName().equals("@")){
                return sid.getArg(1).getTerm().toString();
            }
        }
        return "localhost";
    }

    /**
     * Provides the logic term representation of the identifier
     *
     * @return the term representing the identifier
     */
    public Term toTerm(){
        return id;
    }

    public String toString(){
        return id.toString();
    }


}
