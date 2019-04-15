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

import alice.tuplecentre.core.AbstractOperationId;

/**
 * Class that represent operation ID on TuCSoN tupleCentres
 *
 * @author ste (mailto: s.mariani@unibo.it)
 */
public class TucsonOpId extends AbstractOperationId {

    /**
     * @param i the Java long progressively, uniquely identifying TuCSoN
     *          operations
     */
    public TucsonOpId(final long i) {
        super(i);
    }

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof TucsonOpId)) {
            return false;
        }
        final TucsonOpId other = (TucsonOpId) obj;
        return this.id == other.id;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (this.id ^ this.id >>> 32);
        return result;
    }

}
