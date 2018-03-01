package alice.tuplecentre.core;

import alice.tuplecentre.api.Identifier;

public abstract class AbstractOperationId implements Identifier {

    protected final long id;

    /**
     *
     * @param i
     *            the progressive, unique per tuple centre, operation identifier
     */
    protected AbstractOperationId(final long i) {
        this.id = i;
    }

    /**
     *
     * @return the ReSpecT operation identifier
     */
    public long getId() {
        return this.id;
    }

    @Override
    public String toString() {
        return String.valueOf(this.id);
    }
}
