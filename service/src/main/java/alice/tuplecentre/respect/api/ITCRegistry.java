package alice.tuplecentre.respect.api;

import java.util.Map;

import alice.tuplecentre.respect.api.exceptions.InstantiationNotPossibleException;

/**
 *
 * @author Alessandro Ricci
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 *
 */
public interface ITCRegistry {

    /**
     *
     * @param tc
     *            the ReSpecT tuple centre to register
     */
    void addTC(IRespectTC tc);

    /**
     *
     * @return the register of the ReSpecT tuple centres
     */
    Map<String, ? extends IRespectTC> getMap();

    /**
     *
     * @param id
     *            the identifier of the tuple centre to retrieve
     * @return the tuple centre container
     * @throws InstantiationNotPossibleException
     *             if the tuple centre cannot be instantiated
     */
    IRespectTC getTC(TupleCentreId id) throws InstantiationNotPossibleException;
}
