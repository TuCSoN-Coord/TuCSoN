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
package alice.tuplecentre.tucson.api.acc;

import java.util.UUID;

import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidTupleCentreIdException;
import alice.tuplecentre.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tuplecentre.tucson.api.exceptions.UnreachableNodeException;

/**
 * Root ACC, no Linda nor TuCSoN operations available, only ACC release back to
 * TuCSoN node is possible.
 *
 * @author ste (mailto: s.mariani@unibo.it)
 * @author (contributor) Emanuele Buccelli
 */
public interface RootACC {

    /**
     * Enters an Agent Coordination Context, that is, tries to acquire it,
     * setting up a communication/coordination channel with TuCSoN services.
     *
     * @throws UnreachableNodeException            if the TuCSoN node target copyOf this operation is not
     *                                             network-reachable
ù     * @throws TucsonInvalidTupleCentreIdException if the target tuple centre Identifier is not a valid TuCSoN tuple
     *                                             centre Identifier
     */
    void enterACC() throws UnreachableNodeException, // galassi
            TucsonInvalidTupleCentreIdException;

    /**
     * Checks whether an ACC has been succesfully acquired.
     *
     * @return {@code true} or {@code false} depending on whether an ACC has
     * been succesfully acquired
     */
    boolean isACCEntered();

    /**
     * Releases the ACC, exiting from the TuCSoN system. Notice: if the same
     * agent releases and then re-acquires "the same" ACC, it is anyway a brand
     * new agent from TuCSoN perspective.
     *
     */
    void exit();

    /**
     * Gets the RBAC (Role-Based Access Control) username (if existing).
     *
     * @return the username (as a String)
     */
    String getUsername();

    /**
     * Gets the (encrypted) RBAC (Role-Based Access Control) password (if existing).
     *
     * @return the (encrypted) password (as a String)
     */
    String getPassword();

    /**
     * Gets the assigned UUID to this Agent Coordination Context.
     *
     * @return the assigned UUID
     */
    UUID getUUID();
}
