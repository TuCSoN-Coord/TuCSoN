package alice.tuplecentre.tucson.network.messages.introspection;

/**
 * // TODO: 12/03/2018 add documentation
 *
 * @author Enrico Siboni
 */
public interface GetSnapshotMessage extends NodeMessage {

    /**
     * @return the what
     */
    SetType getWhat();

    /**
     * @param setType the what to set
     */
    void setWhat(final SetType setType);

    /**
     * Set types of wich to get a snapshot
     *
     * @author Enrico Siboni
     */
    public enum SetType {
        // TODO: 12/03/2018 due to external usages, maybe should be moved out this class
        TSET, WSET
    }
}
