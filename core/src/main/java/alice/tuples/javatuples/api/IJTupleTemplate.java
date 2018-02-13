/**
 * IJTupleTemplate.java
 */
package alice.tuples.javatuples.api;

import alice.tuplecentre.api.TupleTemplate;
import alice.tuplecentre.api.exceptions.InvalidTupleException;

/**
 * @author ste (mailto: s.mariani@unibo.it) on 21/feb/2014
 *
 */
public interface IJTupleTemplate extends TupleTemplate {

    /**
     *
     * @param arg
     *            the JArg to add to this JTupleTemplate
     * @throws InvalidTupleException
     *             if the given JArg is invalid (e.g. null)
     */
    void addArg(IJArg arg) throws InvalidTupleException;

    /**
     *
     * @param i
     *            the index of the JArg to retrieve (starting from 0)
     * @return the JArg retrieved
     *
     */
    IJArg getArg(int i);

    /**
     *
     * @return the number of JArg in this JTupleTemplate
     */
    int getNArgs();
}
