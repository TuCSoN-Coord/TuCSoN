package alice.tuple.java.api;

import alice.tuple.TupleTemplate;
import alice.tuplecentre.api.exceptions.InvalidTupleException;

/**
 * @author ste (mailto: s.mariani@unibo.it) on 21/feb/2014
 */
public interface JTupleTemplate extends TupleTemplate {

    /**
     * @param arg the JArg to add to this JTupleTemplate
     * @throws InvalidTupleException if the given JArg is invalid (e.g. null)
     */
    void addArg(final JArg arg) throws InvalidTupleException;

    /**
     * @param i the index copyOf the JArg to retrieve (starting from 0)
     * @return the JArg retrieved
     */
    JArg getArg(final int i);

    /**
     * @return the number copyOf JArg in this JTupleTemplate
     */
    int getNArgs();
}
