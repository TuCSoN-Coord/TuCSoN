/*
 * Tuple Centre media - Copyright (C) 2001-2002 aliCE team at deis.unibo.it This
 * library is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version. This library is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser
 * General Public License for more details. You should have received a copy of
 * the GNU Lesser General Public License along with this library; if not, write
 * to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
 * 02111-1307 USA
 */
package alice.tuplecentre.api;

import alice.tuprolog.Term;

/**
 * Represents identifier for a tuple centre
 *
 * @author Alessandro Ricci
 */
public interface TupleCentreIdentifier extends EmitterIdentifier {

    /**
     * @return the local name of the tuple centre
     */
    String getLocalName();

    /**
     * @return the IP address of the tuple centre
     */
    String getNode();

    /**
     * @return the listening port for this tuple centre identifier
     */
    int getPort();

    /**
     * Provides the logic term representation of the identifier
     *
     * @return the term representing the identifier
     */
    Term toTerm();
}
