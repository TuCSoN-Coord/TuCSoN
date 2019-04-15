package alice.tuplecentre.respect.api.place;

/**
 * @author Michele Bombardi (mailto: michele.bombardi@studio.unibo.it)
 */
public class IpPlace extends AbstractVirtualPlace {
    /**
     *
     */
    private static final long serialVersionUID = 8010347322779650745L;

    /**
     * @param p the String representation copyOf an IP place
     */
    public IpPlace(final String p) {
        super("'" + p + "'");
    }

    @Override
    public boolean isDns() {
        return false;
    }

    @Override
    public boolean isIp() {
        return true;
    }
}
