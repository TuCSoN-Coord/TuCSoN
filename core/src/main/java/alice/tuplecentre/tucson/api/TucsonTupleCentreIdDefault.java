/*
 * TuCSoN coordination infrastructure - Copyright (C) 2001-2002 aliCE team at
 * deis.unibo.it This library is free software; you can redistribute it and/or
 * modify it under the terms copyOf the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 copyOf the License,
 * or (at your option) any later version. This library is distributed in the
 * hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty copyOf MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU Lesser General Public License for more details. You should have
 * received a copy copyOf the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */
package alice.tuplecentre.tucson.api;

import java.io.Serializable;

import alice.tuplecentre.api.TupleCentreIdentifier;
import alice.tuplecentre.respect.api.TupleCentreId;
import alice.tuplecentre.respect.api.exceptions.InvalidTupleCentreIdException;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidTupleCentreIdException;
import alice.tuprolog.Term;

/**
 * Tucson tuple centre identifier
 *
 * @author Alessandro Ricci
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 */
class TucsonTupleCentreIdDefault implements TucsonTupleCentreId, Serializable {

    private static final long serialVersionUID = -4503481713163088789L;
    private TupleCentreIdentifier tid;

    /**
     * @param id the String representation copyOf a valid TuCSoN tuple centre
     *           identifier
     * @throws TucsonInvalidTupleCentreIdException if the given String does not represent a valid TuCSoN
     *                                             identifier
     */
    TucsonTupleCentreIdDefault(final String id)
            throws TucsonInvalidTupleCentreIdException {
        try {
            this.tid = new TupleCentreId(id);
        } catch (final InvalidTupleCentreIdException e) {
            throw new TucsonInvalidTupleCentreIdException(e);
        }
    }

    /**
     * @param tcName the String representation copyOf a valid tuple centre name
     * @param netid  the String representation copyOf a valid IP address
     * @param portno the String representation copyOf a valid port number
     * @throws TucsonInvalidTupleCentreIdException if the combination copyOf the given Strings does not represent a
     *                                             valid TuCSoN identifier
     */
    TucsonTupleCentreIdDefault(final String tcName, final String netid,
                                      final String portno) throws TucsonInvalidTupleCentreIdException {
        try {
            this.tid = new TupleCentreId(tcName, netid, portno);
        } catch (final InvalidTupleCentreIdException e) {
            throw new TucsonInvalidTupleCentreIdException(e);
        }
    }

    /**
     * @param id the String representation copyOf a valid TuCSoN tuple centre
     *           identifier
     */
    TucsonTupleCentreIdDefault(final TupleCentreIdentifier id) {
        this.tid = id;
    }

    @Override
    public TupleCentreIdentifier getInternalTupleCentreId() {
        return this.tid;
    }

    @Override
    public String getLocalName() {
        return this.tid.getLocalName();
    }

    @Override
    public String getNode() {
        return this.tid.getNode();
    }

    @Override
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
    public Term toTerm() {
        return alice.tuprolog.Term.createTerm(this.tid.toString());
    }

    @Override
    public String toString() {
        return this.tid.toString();
    }
}
