package alice.respect.api;

import alice.tuplecentre.core.InputEvent;

/**
 *
 * @author Unknown...
 *
 */
public interface IEnvironmentContext {

    /**
     *
     * @return the current time
     */
    long getCurrentTime();

    /**
     *
     * @param ev
     *            the input environment event to notify
     */
    void notifyInputEnvEvent(InputEvent ev);

    /**
     *
     * @param ev
     *            the input event to notify
     */
    void notifyInputEvent(InputEvent ev);
}
