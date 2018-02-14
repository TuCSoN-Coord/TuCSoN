package alice.respect.api.geolocation;

import java.io.Serializable;
import alice.respect.api.place.AbstractPhysicalPlace;
import alice.respect.api.place.AbstractVirtualPlace;
import alice.respect.api.place.DnsPlace;
import alice.respect.api.place.IPlace;
import alice.respect.api.place.IpPlace;
import alice.respect.api.place.MapPlace;
import alice.respect.api.place.OrgPlace;
import alice.respect.api.place.PhPlace;
import alice.tuprolog.Term;

/**
 * 
 * @author Michele Bombardi (mailto: michele.bombardi@studio.unibo.it)
 * 
 */
public class Position implements Serializable {
    public static final String DNS = "dns";
    public static final String IP = "ip";
    public static final String MAP = "map";
    public static final String ORG = "org";
    // Types
    public static final String PH = "ph";
    private static final long serialVersionUID = -6258486416725772719L;
    private IPlace dnsPlace; // DNS
    // Virtual topology
    private IPlace ipPlace; // IP address
    private IPlace mapPlace; // address
    private IPlace orgPlace; // office
    // Physical topology
    private IPlace phPlace; // lat, lng

    /**
     * 
     */
    public Position() {
        this.phPlace = new PhPlace("coords(Lat,Lng)");
        this.mapPlace = new MapPlace("Address");
        this.orgPlace = new OrgPlace("Office");
        this.ipPlace = new IpPlace("Ip");
        this.dnsPlace = new DnsPlace("Dns");
    }

    /**
     * 
     * @return the DNS position
     */
    public IPlace getDnsPlace() {
        return this.dnsPlace;
    }

    /**
     * 
     * @return the IP position
     */
    public IPlace getIpPlace() {
        return this.ipPlace;
    }

    /**
     * 
     * @return the MAP position
     */
    public IPlace getMapPlace() {
        return this.mapPlace;
    }

    /**
     * 
     * @return the ORG position
     */
    public IPlace getOrgPlace() {
        return this.orgPlace;
    }

    /**
     * 
     * @return the PH position
     */
    public IPlace getPhPlace() {
        return this.phPlace;
    }

    /**
     * 
     * @param space
     *            the space whose correspondant position should be retrieved
     * @return the position in the specified space
     */
    public IPlace getPlace(final String space) {
        if (Position.PH.equals(space)) {
            return this.phPlace;
        } else if (Position.MAP.equals(space)) {
            return this.mapPlace;
        } else if (Position.ORG.equals(space)) {
            return this.orgPlace;
        } else if (Position.IP.equals(space)) {
            return this.ipPlace;
        } else if (Position.DNS.equals(space)) {
            return this.dnsPlace;
        }
        return null;
    }

    /**
     * 
     * @param space
     *            the space whose correspondant position should be retrieved
     * @return the position in the specified space
     */
    public IPlace getPlace(final Term space) {
        final String s = space.toString();
        if (Position.PH.equals(s)) {
            return this.phPlace;
        } else if (Position.MAP.equals(s)) {
            return this.mapPlace;
        } else if (Position.ORG.equals(s)) {
            return this.orgPlace;
        } else if (Position.IP.equals(s)) {
            return this.ipPlace;
        } else if (Position.DNS.equals(s)) {
            return this.dnsPlace;
        }
        return null;
    }

    /**
     * 
     * @param dns
     *            the String representation of the DNS place to set
     */
    public void setDnsPlace(final String dns) {
        this.dnsPlace = new DnsPlace(dns);
    }

    /**
     * 
     * @param ip
     *            the String representation of the IP place to set
     */
    public void setIpPlace(final String ip) {
        this.ipPlace = new IpPlace(ip);
    }

    /**
     * 
     * @param map
     *            the String representation of the MAP place to set
     */
    public void setMapPlace(final String map) {
        this.mapPlace = new MapPlace(map);
    }

    /**
     * 
     * @param org
     *            the String representation of the ORG place to set
     */
    public void setOrgPlace(final String org) {
        this.orgPlace = new OrgPlace(org);
    }

    /**
     * 
     * @param lat
     *            the latitude of the PH place to set
     * @param lng
     *            the longitude of the PH place to set
     */
    public void setPhPlace(final double lat, final double lng) {
        this.phPlace = new PhPlace("coords(" + lat + "," + lng + ")");
    }

    /**
     * 
     * @param latLng
     *            the String representation of the PH place to set
     */
    public void setPhPlace(final String latLng) {
        this.phPlace = new PhPlace(latLng);
    }

    /**
     * 
     * @param place
     *            the place to set
     */
    public void setPlace(final IPlace place) {
        if (place.isPhysical()) {
            final AbstractPhysicalPlace pp = (AbstractPhysicalPlace) place;
            if (pp.isPh()) {
                this.phPlace = pp;
            } else if (pp.isMap()) {
                this.mapPlace = pp;
            } else {
                this.orgPlace = pp;
            }
        } else {
            final AbstractVirtualPlace vp = (AbstractVirtualPlace) place;
            if (vp.isIp()) {
                this.ipPlace = vp;
            } else {
                this.dnsPlace = vp;
            }
        }
    }

    /**
     * 
     * @param space
     *            the String representation of the sort of space to consider
     * @param place
     *            the String representation of the place to set
     */
    public void setPlace(final String space, final String place) {
        if (Position.PH.equals(space)) {
            this.phPlace = new PhPlace(place);
        } else if (Position.MAP.equals(space)) {
            this.mapPlace = new MapPlace(place);
        } else if (Position.ORG.equals(space)) {
            this.orgPlace = new OrgPlace(place);
        } else if (Position.IP.equals(space)) {
            this.ipPlace = new IpPlace(place);
        } else if (Position.DNS.equals(space)) {
            this.dnsPlace = new DnsPlace(place);
        }
    }
}
