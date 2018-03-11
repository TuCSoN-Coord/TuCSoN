package alice.tuplecentre.respect.api.place;

/**
 * 
 * @author Michele Bombardi (mailto: michele.bombardi@studio.unibo.it)
 * 
 */
public class OrgPlace extends AbstractPhysicalPlace {
    /**
     * 
     */
    private static final long serialVersionUID = 2184976079074938533L;

    /**
     * 
     * @param p
     *            The String representation of an ORG place
     */
    public OrgPlace(final String p) {
        super("'" + p + "'");
    }

    @Override
    public boolean isMap() {
        return false;
    }

    @Override
    public boolean isOrg() {
        return true;
    }

    @Override
    public boolean isPh() {
        return false;
    }
}
