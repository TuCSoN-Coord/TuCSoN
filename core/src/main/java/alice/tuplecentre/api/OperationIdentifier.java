package alice.tuplecentre.api;

/**
 * Interface for Operation Identifiers
 *
 * @author Enrico Siboni
 */
public interface OperationIdentifier extends Identifier {

    /**
     * @return the ReSpecT operation identifier
     */
    long getId();
}
