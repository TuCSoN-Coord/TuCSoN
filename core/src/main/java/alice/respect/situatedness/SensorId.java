package alice.respect.situatedness;

/**
 * A sensor identifier.
 *
 * @author Steven Maraldi
 *
 */
public class SensorId extends AbstractProbeId {

    private static final long serialVersionUID = -2977090935649268889L;

    /**
     *
     * @param i
     *            the String representation of this sensor identifier
     */
    public SensorId(final String i) {
        super(i);
    }

    @Override
    public boolean isActuator() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isSensor() {
        // TODO Auto-generated method stub
        return true;
    }
}
