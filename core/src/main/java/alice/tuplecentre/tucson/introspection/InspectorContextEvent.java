package alice.tuplecentre.tucson.introspection;

import java.io.Serializable;
import java.util.List;

import alice.tuple.logic.LogicTuple;
import alice.tuplecentre.core.TriggeredReaction;

/**
 * Interface for an Inspector Context Event
 * <p>
 * // TODO review this doc, added on the fly
 *
 * @author Enrico Siboni
 */
public interface InspectorContextEvent extends Serializable {

    /**
     * @return the localTime
     */
    long getLocalTime();

    /**
     * @return verify if VM mode changed
     */
    boolean getModeChanged();

    /**
     * @return the reactionFailed
     */
    TriggeredReaction getReactionFailed();

    /**
     * @return the reactionOk
     */
    TriggeredReaction getReactionOk();

    /**
     * @return verify if step mode is active
     */
    boolean getStepMode();

    /**
     * @return the tuples
     */
    List<LogicTuple> getTuples();

    /**
     * @return the vmTime
     */
    long getVmTime();

    /**
     * @return the wnEvents
     */
    List<WSetEvent> getWnEvents();

    /**
     * @param lt the localTime to set
     */
    void setLocalTime(final long lt);

    /**
     * @param a set modeChanged
     */
    void setModeChanged(final boolean a);

    /**
     * @param rf the reactionFailed to set
     */
    void setReactionFailed(final TriggeredReaction rf);

    /**
     * @param ro the reactionOk to set
     */
    void setReactionOk(final TriggeredReaction ro);

    /**
     * @param a set step mode as active
     */
    void setStepMode(final boolean a);

    /**
     * @param t the tuples to set
     */
    void setTuples(final List<LogicTuple> t);

    /**
     * @param vmt the vmTime to set
     */
    void setVmTime(final long vmt);

    /**
     * @param wne the wnEvents to set
     */
    void setWnEvents(final List<WSetEvent> wne);
}
