package alice.tuplecentre.respect.api.geolocation.service;

import alice.tuplecentre.api.IId;
import alice.tuprolog.Struct;
import alice.tuprolog.Term;

/**
 * 
 * @author Michele Bombardi (mailto: michele.bombardi@studio.unibo.it)
 * 
 */
public class GeoServiceId implements IId {
    private final Struct id;
    private final String name;

    /**
     * 
     * @param i
     *            the String representation of the Geolocation Service id
     */
    public GeoServiceId(final String i) {
        this.name = i;
        this.id = new Struct(i);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final GeoServiceId other = (GeoServiceId) obj;
        if (this.name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!this.name.equals(other.name)) {
            return false;
        }
        return true;
    }

    /**
     * 
     * @return the name given to the Geolocation Service
     */
    public String getName() {
        return this.name;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + (this.name == null ? 0 : this.name.hashCode());
        return result;
    }

    @Override
    public boolean isAgent() {
        return false;
    }

    @Override
    public boolean isEnv() {
        return false;
    }

    @Override
    public boolean isGeo() {
        return true;
    }

    @Override
    public boolean isTC() {
        return false;
    }

    /**
     * 
     * @return the tuProlog Term representation of this Geolocation Service id
     */
    public Term toTerm() {
        if ("@".equals(this.id.getName())) {
            return this.id.getArg(0).getTerm();
        }
        return this.id.getTerm();
    }
}
