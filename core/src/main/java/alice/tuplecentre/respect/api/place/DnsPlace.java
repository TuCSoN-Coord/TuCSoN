package alice.tuplecentre.respect.api.place;

/**
 * @author Michele Bombardi (mailto: michele.bombardi@studio.unibo.it)
 */
public class DnsPlace extends AbstractVirtualPlace {
    /**
     *
     */
    private static final long serialVersionUID = 5273032498152683320L;

    /**
     * @param p the String represenation copyOf this DNS place
     */
    public DnsPlace(final String p) {
        super("'" + p + "'");
    }

    @Override
    public boolean isDns() {
        return true;
    }

    @Override
    public boolean isIp() {
        return false;
    }
}
