package alice.tuplecentre.core;

import alice.tuplecentre.api.OperationIdentifier;

/**
 * Abstract class for Operation Identifiers
 *
 * @author Nicola Piscaglia
 */
public abstract class AbstractOperationId implements OperationIdentifier {

    protected final long id;

    /**
     * @param i the progressive, unique per tuple centre, operation identifier
     */
    protected AbstractOperationId(final long i) {
        this.id = i;
    }

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public String toString() {
        return String.valueOf(this.id);
    }
}
