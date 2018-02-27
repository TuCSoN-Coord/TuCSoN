package alice.tucson.introspection;

import java.io.Serializable;

import alice.logictuple.LogicTuple;

/**
 * Interface for Inspector Protocol
 * <p>
 * todo added this documentation on the fly, review it
 *
 * @author Enrico Siboni
 */
public interface InspectorProtocol extends Serializable {

    /**
     * @return the pendingQueryObservType
     */
    ObsType getPendingQueryObservType();

    /**
     * @return the reactionsObservType
     */
    ObsType getReactionsObservType();

    /**
     * @return the sepModeObservType
     */
    ObsType getStepModeObservType();

    /**
     * @return the tsetObservType
     */
    ObsType getTsetObservType();

    /**
     * @return the tsetFilter
     */
    LogicTuple getTsetFilter();

    /**
     * @return the wsetFilter
     */
    LogicTuple getWsetFilter();

    /**
     * @return the tracing
     */
    boolean isTracing();

    /**
     * @param obsType the pendingQueryObservType to set
     */
    void setPendingQueryObservType(final ObsType obsType);

    /**
     * @param obsType the reactionsObservType to set
     */
    void setReactionsObservType(final ObsType obsType);

    /**
     * @param obsType the stepModeObservType to set
     */
    void setStepModeObservType(final ObsType obsType);

    /**
     * @param obsType the tsetObservType to set
     */
    void setTsetObservType(final ObsType obsType);

    /**
     * @param trace the tracing to set
     */
    void setTracing(final boolean trace);

    /**
     * @param filter the tsetFilter to set
     */
    void setTsetFilter(final LogicTuple filter);

    /**
     * @param filter the wsetFilter to set
     */
    void setWsetFilter(final LogicTuple filter);

    /**
     * Enumeration containing types of Observation
     *
     * @author Enrico Siboni
     */
    enum ObsType {

        /**
         * don't observe
         */
        DISABLED,
        /**
         * observe continuously
         */
        PROACTIVE,
        /**
         * observe only when asked by inspector
         */
        REACTIVE,
        /**
         * observe step mode like an agent
         */
        STEP_MODE_AGENT,
        /**
         * observe step mode like the tuple space
         */
        STEP_MODE_TUPLE_SPACE
    }
}
