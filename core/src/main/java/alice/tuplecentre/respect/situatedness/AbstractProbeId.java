package alice.tuplecentre.respect.situatedness;

import alice.tuplecentre.respect.api.EnvironmentId;

/**
 * A "probe" (aka environmental resource) identifier. Being part of the MAS
 * environment, a probe can be either a sensor or an actuator.
 *
 * @author Steven Maraldi
 */
public abstract class AbstractProbeId extends EnvironmentId implements ProbeIdentifier {

    private static final long serialVersionUID = -7709792820397648780L;

    /**
     * Constructs a probe identifier
     *
     * @param i the resource's identifier
     */
    public AbstractProbeId(final String i) {
        super(i);
    }

}
