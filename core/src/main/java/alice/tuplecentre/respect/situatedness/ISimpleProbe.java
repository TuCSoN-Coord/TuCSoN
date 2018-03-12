package alice.tuplecentre.respect.situatedness;

/**
 * Interface for a generic probe. TuCSoN programmers must implement this
 * interface when coding their own environmental resource wrappers. More
 * specifically, they should implement all methods but the 'writeValue' for
 * sensors, all methods but the 'readValue' for actuators.
 *
 * @author Steven Maraldi
 */
public interface ISimpleProbe {

    /**
     * Get the probe's identifier
     *
     * @return probe's identifier
     */
    ProbeIdentifier getIdentifier();

    /**
     * Gets the transducer which the probe is communicating with.
     *
     * @return the transducer's identifier
     */
    TransducerId getTransducer();

    /**
     * Reads the probe's value. A read request should force an events from the
     * device
     *
     * @param key the parameter to read
     * @return true if success
     */
    boolean readValue(String key);

    /**
     * Sets the transducer which the probe will communicate with.
     *
     * @param tId transducer's identifier
     */
    void setTransducer(TransducerId tId);

    /**
     * Set the specific value of a parameter of the probe
     *
     * @param key   the parameter to set
     * @param value value to be set.
     * @return true if the operation has been successfully executed.
     */
    boolean writeValue(String key, int value);
}
