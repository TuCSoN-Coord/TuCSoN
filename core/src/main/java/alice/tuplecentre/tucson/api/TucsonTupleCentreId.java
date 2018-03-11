/*
 * TuCSoN coordination infrastructure - Copyright (C) 2001-2002 aliCE team at
 * deis.unibo.it This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of the License,
 * or (at your option) any later version. This library is distributed in the
 * hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU Lesser General Public License for more details. You should have
 * received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */
package alice.tuplecentre.tucson.api;

import java.io.Serializable;

import alice.tuplecentre.respect.api.TupleCentreId;
import alice.tuplecentre.respect.api.exceptions.InvalidTupleCentreIdException;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidTupleCentreIdException;
import alice.tuprolog.Term;

/**
 *
 * @author Alessandro Ricci
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 *
 */
public class TucsonTupleCentreId implements
        alice.tuplecentre.api.TupleCentreId, Serializable {

    private static final long serialVersionUID = -4503481713163088789L;
    private TupleCentreId tid;

    /**
     *
     * @param id
     *            the String representation of a valid TuCSoN tuple centre
     *            identifier
     * @throws TucsonInvalidTupleCentreIdException
     *             if the given String does not represent a valid TuCSoN
     *             identifier
     */
    public TucsonTupleCentreId(final String id)
            throws TucsonInvalidTupleCentreIdException {
        try {
            this.tid = new TupleCentreId(id);
        } catch (final InvalidTupleCentreIdException e) {
            throw new TucsonInvalidTupleCentreIdException();
        }
    }

    /**
     *
     * @param tcName
     *            the String representation of a valid tuple centre name
     * @param netid
     *            the String representation of a valid IP address
     * @param portno
     *            the String representation of a valid port number
     * @throws TucsonInvalidTupleCentreIdException
     *             if the combination of the given Strings does not represent a
     *             valid TuCSoN identifier
     */
    public TucsonTupleCentreId(final String tcName, final String netid,
            final String portno) throws TucsonInvalidTupleCentreIdException {
        try {
            this.tid = new TupleCentreId(tcName, netid, portno);
        } catch (final InvalidTupleCentreIdException e) {
            throw new TucsonInvalidTupleCentreIdException();
        }
    }

    /**
     *
     * @param id
     *            the String representation of a valid TuCSoN tuple centre
     *            identifier
     */
    public TucsonTupleCentreId(final TupleCentreId id) {
        this.tid = id;
    }

    /**
     *
     * @return the local tuple centre identifier
     */
    public TupleCentreId getInternalTupleCentreId() {
        return this.tid;
    }

    /**
     *
     * @return the local name of the tuple centre
     */
    public String getName() {
        return this.tid.getName();
    }

    /**
     *
     * @return the IP address of the tuple centre
     */
    public String getNode() {
        return this.tid.getNode();
    }

    /**
     *
     * @return the listening port of the tuple centre
     */
    public int getPort() {
        return this.tid.getPort();
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
        return false;
    }

    @Override
    public boolean isTC() {
        return true;
    }

    @Override
    public String toString() {
        return this.tid.toString();
    }

    /**
     *
     * @return the tuProlog Term representation of this tuple centre identifier
     */
    public Term toTerm() {
        return alice.tuprolog.Term.createTerm(this.tid.toString());
    }
}
