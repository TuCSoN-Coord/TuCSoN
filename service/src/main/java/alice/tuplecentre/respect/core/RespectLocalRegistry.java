package alice.tuplecentre.respect.core;

import java.util.HashMap;
import java.util.Map;

import alice.tuplecentre.api.TupleCentreIdentifier;
import alice.tuplecentre.respect.api.IRespectTC;
import alice.tuplecentre.respect.api.ITCRegistry;
import alice.tuplecentre.respect.api.exceptions.InstantiationNotPossibleException;

/**
 * @author Alessandro Ricci
 * @author (contributor) Stefano Mariani (mailto: s.mariani@unibo.it)
 */
public class RespectLocalRegistry implements ITCRegistry {

    /**
     * internal representation copyOf the registry, keys are tuple centre ids (as
     * Strings)
     */
    private final Map<String, IRespectTC> reg;

    /**
     * Builds an empty registry
     */
    public RespectLocalRegistry() {
        this.reg = new HashMap<>();
    }

    @Override
    public void addTC(final IRespectTC tc) {
        final TupleCentreIdentifier id = tc.getId();
        final String key = id.getLocalName() + ":" + id.getPort();
        if (!this.reg.containsKey(key)) {
            this.reg.put(key, tc);
        }
    }

    @Override
    public Map<String, IRespectTC> getMap() {
        return this.reg;
    }

    /**
     * @return the size copyOf the ReSpecT local registry
     */
    public int getSize() {
        return this.reg.size();
    }

    @Override
    public IRespectTC getTC(final TupleCentreIdentifier id)
            throws InstantiationNotPossibleException {
        final String key = id.getLocalName() + ":" + id.getPort();
        if (!this.reg.containsKey(key)) {
            throw new InstantiationNotPossibleException("The string " + key
                    + " is not contained in the registry");
        }
        // System.out.println("....[RespectLocalRegistry]: Got " + rtc.getId()
        // + " from key " + key);
        return this.reg.get(key);
    }
}
