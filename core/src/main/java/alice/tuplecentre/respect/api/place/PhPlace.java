package alice.tuplecentre.respect.api.place;

/**
 * @author Michele Bombardi (mailto: michele.bombardi@studio.unibo.it)
 */
public class PhPlace extends AbstractPhysicalPlace {
    /**
     *
     */
    private static final long serialVersionUID = 2033533804064753016L;

    /**
     * @param p The String representation of a PH place
     */
    public PhPlace(final String p) {
        super(p);
    }

    @Override
    public boolean isMap() {
        return false;
    }

    @Override
    public boolean isOrg() {
        return false;
    }

    @Override
    public boolean isPh() {
        return true;
    }
}
