/*
 * TupleCentreId.java
 *
 * Copyright 2000-2001-2002  aliCE team at deis.unibo.it
 *
 * This software is the proprietary information of deis.unibo.it
 * Use is subject to license terms.
 *
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

