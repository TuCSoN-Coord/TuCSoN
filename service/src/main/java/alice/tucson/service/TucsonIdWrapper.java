package alice.tucson.service;

import alice.tuplecentre.api.Identifier;

/**
 *
 * @author ste (mailto: s.mariani@unibo.it)
 *
 * @param <I>
 *            the actual identifier Type
 */
public class TucsonIdWrapper<I> implements Identifier{

    private final I id;

    /**
     *
     * @param i
     *            either an agent or tuple centre identifier
     */
    public TucsonIdWrapper(final I i) {
        this.id = i;
    }

    /**
     *
     * @return the agent or tuple centre identifier
     */
    public I getId() {
        return this.id;
    }
}
