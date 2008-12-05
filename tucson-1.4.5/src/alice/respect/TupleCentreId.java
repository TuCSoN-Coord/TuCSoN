/*
 * ReSpecT Framework - aliCE team at deis.unibo.it
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
package alice.respect;
import alice.tuprolog.*;

/**
 * Tuple centre identifier for ReSpecT tuple centres
 *
 * A tuple centre identifier must be a ground logic term
 */
public class TupleCentreId implements alice.tuplecentre.TupleCentreId,java.io.Serializable{

    private static TupleCentreIdOperatorManager opManager = new TupleCentreIdOperatorManager();

    protected alice.tuprolog.Term id;

    protected TupleCentreId(){
    }

    /**
     * Constructs a tuple centre identifier from a string,
     * which must represent a well-formed ground logic term
     *
     * @param name is the textual representation of the identifier
     * @throws InvalidTupleCentreIdException if name is not a well-formed ground logic term
     */
    public TupleCentreId(String name) throws InvalidTupleCentreIdException {
        try{
            id=Term.parse(name,opManager);
        } catch (Exception ex){
            throw new InvalidTupleCentreIdException();
        }
        if (!id.isGround()){
            throw new InvalidTupleCentreIdException();
        }
    }


    /**
     * Constructs a tuple centre identifier,
     * which must be a well-formed ground logic term
     *
     * @param name is the term representing the identifier
     * @throws InvalidTupleCentreIdException if name is not a well-formed ground logic term
     */
    public TupleCentreId(Term name) throws InvalidTupleCentreIdException {
        id=name.getTerm();
        if (!id.isGround()){
            throw new InvalidTupleCentreIdException();
        }
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

