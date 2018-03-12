package alice.tuplecentre.respect.api.place;

/**
 * @author Michele Bombardi (mailto: michele.bombardi@studio.unibo.it)
 */
public class MapPlace extends AbstractPhysicalPlace {
    /**
     *
     */
    private static final long serialVersionUID = -8393583389847401746L;

    /**
     * @param p The String representation of a MAP place
     */
    public MapPlace(final String p) {
        super("'" + p + "'");
    }

    @Override
    public boolean isMap() {
        return true;
    }

    @Override
    public boolean isOrg() {
        return false;
    }

    @Override
    public boolean isPh() {
        return false;
    }
}
