package alice.tucson.service;

/**
 * Wrapper for a Tucson Identifier
 *
 * @param <I> the actual identifier Type
 * @author ste (mailto: s.mariani@unibo.it)
 */
public class TucsonIdWrapper<I> {

    private final I id;

    /**
     * @param i either an agent or tuple centre identifier
     */
    public TucsonIdWrapper(final I i) {
        this.id = i;
    }

    /**
     * @return the agent or tuple centre identifier
     */
    public I getId() {
        return this.id;
    }
}
