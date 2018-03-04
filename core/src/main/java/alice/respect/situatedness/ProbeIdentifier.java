package alice.respect.situatedness;

import alice.respect.api.EnvironmentIdentifier;

/**
 * Interface for a "probe" (aka environmental resource) identifier.
 *
 * @author Enrico Siboni
 */
public interface ProbeIdentifier extends EnvironmentIdentifier {

    /**
     * Checks if the resource is an actuator.
     *
     * @return true if the resource is an actuatore one.
     */
    boolean isActuator();

    /**
     * Checks if the resource is a sensor.
     *
     * @return true if the resource is a sensor one.
     */
    boolean isSensor();
}
