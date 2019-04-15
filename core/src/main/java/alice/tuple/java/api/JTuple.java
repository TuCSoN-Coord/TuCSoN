package alice.tuple.java.api;

import alice.tuple.Tuple;
import alice.tuplecentre.api.exceptions.InvalidTupleException;

/**
 * @author ste (mailto: s.mariani@unibo.it) on 21/feb/2014
 */
public interface JTuple extends Tuple {

    /**
     * @param arg the JVal to add to this JTuple
     * @throws InvalidTupleException if the given JVal is invalid (e.g. null)
     */
    void addArg(final JVal arg) throws InvalidTupleException;

    /**
     * @param i the index copyOf the JVal to retrieve (starting from 0)
     * @return the JVal retrieved
     */
    JVal getArg(final int i);

    /**
     * @return the number copyOf JVal in this JTuple
     */
    int getNArgs();
}
