package alice.tuplecentre.respect.situatedness;

/**
 * A sensor identifier.
 *
 * @author Steven Maraldi
 */
public class SensorId extends AbstractProbeId {

    private static final long serialVersionUID = -2977090935649268889L;

    /**
     * @param i the String representation copyOf this sensor identifier
     */
    public SensorId(final String i) {
        super(i);
    }

    @Override
    public boolean isActuator() {
        return false;
    }

    @Override
    public boolean isSensor() {
        return true;
    }
}
