package alice.tuplecentre.respect.api;

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
     *            the input environment events to notify
     */
    void notifyInputEnvEvent(InputEvent ev);

    /**
     *
     * @param ev
     *            the input events to notify
     */
    void notifyInputEvent(InputEvent ev);
}
