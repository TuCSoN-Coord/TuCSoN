/*
 * ReSpecT - Copyright (C) aliCE team at deis.unibo.it This library is free
 * software; you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package alice.tuplecentre.respect.api;

import java.io.Serializable;

import alice.tuplecentre.api.TupleCentreIdentifier;
import alice.tuplecentre.respect.api.exceptions.InvalidTupleCentreIdException;
import alice.tuplecentre.respect.core.TupleCentreIdOperatorManager;
import alice.tuplecentre.tucson.service.TucsonInfo;
import alice.tuprolog.InvalidTermException;
import alice.tuprolog.Struct;
import alice.tuprolog.Term;

/**
 * Tuple centre identifier for ReSpecT tuple centres
 * <p>
 * A tuple centre identifier must be a ground logic term.
 *
 * @author Alessandro Ricci
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 */
public class TupleCentreId implements TupleCentreIdentifier, Serializable {

    private static final TupleCentreIdOperatorManager opManager = new TupleCentreIdOperatorManager();
    private static final long serialVersionUID = 1L;

    private Term id;

    /**
     * Constructs a tuple centre identifier from a string, which must represent
     * a well-formed ground logic term
     *
     * @param n is the textual representation of the identifier
     * @throws InvalidTupleCentreIdException if name is not a well-formed ground logic term
     */
    public TupleCentreId(final String n) throws InvalidTupleCentreIdException {
        String name = n;
        if (n.indexOf('@') < 0) {
            name = n.concat("@localhost");
        }
        this.id = Term.createTerm(name, TupleCentreId.opManager);
        if (!this.id.isGround()) {
            throw new InvalidTupleCentreIdException("String '" + n
                    + "' is not a well-formed ground logic term");
        }
    }

    /**
     * @param tcName   logical name of the tuple centre
     * @param hostName the netid of the device hosting the tuple centre
     * @param portName the listening port of the tuple centre
     * @throws InvalidTupleCentreIdException if the tuple centre id is not a valid Prolog term
     */
    public TupleCentreId(final String tcName, final String hostName,
                         final String portName) throws InvalidTupleCentreIdException {
        final String tc = tcName.trim();
        final String host = alice.util.Tools.removeApices(hostName.trim());
        final String port = alice.util.Tools.removeApices(portName.trim());
        try {
            this.id = Term.createTerm(tc + "@'" + host + "':'" + port + "'",
                    TupleCentreId.opManager);
        } catch (final InvalidTermException e) {
            throw new InvalidTupleCentreIdException(
                    "Cannot create a valid centre id from tcName: '" + tc
                            + "', hostName: '" + host + "' and port:'" + port
                            + "'");
        }
        if (!this.id.isGround()) {
            throw new InvalidTupleCentreIdException("Term '" + this.id
                    + "' is not a well-formed ground logic term");
        }
    }

    /**
     * Constructs a tuple centre identifier, which must be a well-formed ground
     * logic term
     *
     * @param name is the term representing the identifier
     * @throws InvalidTupleCentreIdException if name is not a well-formed ground logic term
     */
    public TupleCentreId(final Term name) throws InvalidTupleCentreIdException {
        this.id = name.getTerm();
        if (!this.id.isGround()) {
            throw new InvalidTupleCentreIdException("Term '" + name
                    + "' is not a well-formed ground logic term");
        }
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof TupleCentreId)) {
            return false;
        }
        final TupleCentreId other = (TupleCentreId) obj;
        if (this.id == null) {
            return other.id == null;
        } else return this.id.equals(other.id);
    }

    @Override
    public String getLocalName() {
        if (this.id instanceof alice.tuprolog.Struct) {
            final Struct sid = (Struct) this.id;
            if (sid.getArity() == 2 && "@".equals(sid.getName())) {
                return sid.getArg(0).getTerm().toString();
            }
        }
        return this.id.toString();
    }

    @Override
    public String getNode() {
        if (this.id instanceof alice.tuprolog.Struct) {
            final Struct sid = (Struct) this.id.getTerm();
            if (sid.getArity() == 2 && "@".equals(sid.getName())) {
                final Struct t = (Struct) sid.getArg(1).getTerm();
                if (!t.getArg(0).getTerm().isCompound()) {
                    return alice.util.Tools.removeApices(t.getArg(0).getTerm()
                            .toString());
                }
                final Struct tt = (Struct) t.getArg(0).getTerm();
                return alice.util.Tools.removeApices(tt.getArg(0).getTerm()
                        .toString())
                        + "."
                        + alice.util.Tools.removeApices(tt.getArg(1).getTerm()
                        .toString());
            }
        }
        return "localhost";
    }

    @Override
    public int getPort() {
        if (this.id instanceof alice.tuprolog.Struct) {
            final Struct sid = (Struct) this.id;
            if (sid.getArity() == 2 && "@".equals(sid.getName())) {
                final Struct t = (Struct) sid.getArg(1);
                return Integer.parseInt(alice.util.Tools.removeApices(t
                        .getArg(1).getTerm().toString()));
            }
        }
        return TucsonInfo.getDefaultPortNumber();
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (this.id == null ? 0 : this.id.hashCode());
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
        return false;
    }

    @Override
    public boolean isTC() {
        return true;
    }

    @Override
    public Term toTerm() {
        return id;
    }

    @Override
    public String toString() {
        return this.id.toString();
    }
}
