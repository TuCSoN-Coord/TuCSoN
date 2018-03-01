package alice.respect.situatedness;

import alice.respect.api.EnvironmentId;

/**
 * A "probe" (aka environmental resource) identifier. Being part of the MAS
 * environment, a probe can be either a sensor or an actuator.
 *
 * @author Steven Maraldi
 */
public abstract class AbstractProbeId extends EnvironmentId {

    private static final long serialVersionUID = -7709792820397648780L;

    /**
     * Constructs a probe identifier
     *
     * @param i
     *            the resource's identifier
     */
    public AbstractProbeId(final String i) {
        super(i);
    }

    /**
     * Checks if the resource is an actuator.
     *
     * @return true if the resource is an actuatore one.
     */
    public abstract boolean isActuator();

    /**
     * Checks if the resource is a sensor.
     *
     * @return true if the resource is a sensor one.
     */
    public abstract boolean isSensor();
}
