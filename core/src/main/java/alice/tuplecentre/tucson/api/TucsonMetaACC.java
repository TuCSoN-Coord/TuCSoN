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

import alice.tuplecentre.tucson.api.acc.AdminACC;
import alice.tuplecentre.tucson.api.acc.EnhancedACC;
import alice.tuplecentre.tucson.api.acc.NegotiationACC;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidTupleCentreIdException;
import alice.tuplecentre.tucson.service.ACCProxyAgentSide;
import alice.tuplecentre.tucson.service.AdminACCProxyAgentSide;
import alice.tuplecentre.tucson.service.NegotiationACCProxyAgentSide;
import alice.tuplecentre.tucson.service.TucsonInfo;

/**
 * TuCSoN Meta Agent Coordination Context. It is exploited by TuCSoN agents to
 * obtain an ACC with which to interact with the TuCSoN node.
 *
 * @author Alessandro Ricci
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 * @author (contributor) Emanuele Buccelli
 *
 */
public final class TucsonMetaACC {

    // TODO: Controllo password
    /**
     * Acquires an Administrative ACC from the TuCSoN node. Valid username and
     * password are required.
     *
     * @param aid
     *            the Identifier of the agent willing to obtain adminstrative access
     * @param netid
     *            the IP address of the TuCSoN node to contact
     * @param portno
     *            the TCP port number of the TuCSoN node to contact
     * @param username
     *            the username of the administrative agent
     * @param password
     *            the (encrypted) password of the administrative agent
     * @return the Administrative ACC if given credentials are valid,
     *         {@code null} otherwise
     */
    public static AdminACC getAdminContext(final TucsonAgentId aid,
                                           final String netid, final int portno, final String username,
                                           final String password) {
        AdminACC acc = null;
        try {
            acc = new AdminACCProxyAgentSide(aid, netid, portno, username,
                    password);
        } catch (final TucsonInvalidAgentIdException e) {
            System.out.println("[TucsonMetaACC]: Given agent Identifier is NOT valid!");
            e.printStackTrace();
            return null;
        } catch (final TucsonInvalidTupleCentreIdException e) {
            System.err
                    .println("[TucsonMetaACC]: Given tuple centre Identifier is NOT valid!");
            e.printStackTrace();
            return null;
        }
        return acc;
    }

    /*
     * public static NegotiationACC getNegotiationContext(final String aid,
     * String netid, int portno, String username, String password){
     * NegotiationACC acc = null; try { acc = new
     * NegotiationACCProxyAgentSide(new TucsonAgentId(aid), netid, portno);
     * acc.login(username, password); }catch (TucsonInvalidAgentIdException |
     * NoSuchAlgorithmException | InvalidVarNameException |
     * TucsonInvalidTupleCentreIdException | TucsonOperationNotPossibleException
     * | UnreachableNodeException | OperationTimeOutException e) {
     * System.err.println("[Tucson-NegotiationACC]: " + e); e.printStackTrace();
     * return null; } return acc; }
     */

    /**
     * Gets the available most-comprehensive ACC from the TuCSoN Node Service
     * active on the default host ("localhost") on the default port (20504).
     *
     * @param aid
     *            Who demand for the ACC
     *
     * @return The DefaultACC (which is the most powerful at the moment)
     */
    public static EnhancedACC getContext(final TucsonAgentId aid) {
        return TucsonMetaACC.getContext(aid, "localhost",
                TucsonInfo.getDefaultPortNumber());
    }

    /**
     * Gets the available most-comprehensive ACC from the TuCSoN Node Service
     * active on the specified pair node:port where node is the ip address.
     *
     * @param aid
     *            Who demand for the ACC
     * @param netid
     *            The ip address of the target TuCSoN Node Service
     * @param portno
     *            The listening port of the target TuCSoN Node Service
     *
     * @return The DefaultACC (which is the most powerful at the moment)
     */
    public static EnhancedACC getContext(final TucsonAgentId aid,
            final String netid, final int portno) {
        EnhancedACC acc = null;
        try {
            acc = new ACCProxyAgentSide(aid.toString(), netid, portno);
            aid.assignUUID();
        } catch (final TucsonInvalidAgentIdException e) {
            // Cannot happen, aid it's already a valid TucsonAgentId
            e.printStackTrace();
            return null;
        }
        return acc;
    }

    /**
     * Gets the available most-comprehensive ACC from the TuCSoN Node Service
     * active on the default host ("localhost") on the default port (20504).
     *
     * @param aid
     *            Who demands for the ACC
     *
     * @return The DefaultACC (which is the most powerful at the moment)
     */
    public static NegotiationACC getNegotiationContext(final String aid) {
        return TucsonMetaACC.getNegotiationContext(aid, "localhost",
                TucsonInfo.getDefaultPortNumber());
    }

    /**
     * Acquires the Negotiation ACC necessary to interact with TuCSoN according
     * to RBAC policies. If no RBAC policies are installed, method
     * {@link NegotiationACC#playDefaultRole()} is available.
     *
     * @param aid
     *            the Identifier of the agent demanding for the ACC
     * @param netid
     *            the IP address of the TuCSoN node to contact
     * @param portno
     *            the TCP port number of the TuCSoN node to contact
     * @return the Negotiation ACC requested
     */
    public static NegotiationACC getNegotiationContext(final String aid,
            final String netid, final int portno) {
        NegotiationACC acc = null;
        try {
            acc = new NegotiationACCProxyAgentSide(new TucsonAgentIdDefault(aid),
                    netid, portno);
        } catch (final TucsonInvalidAgentIdException e) {
            System.out.println("[TucsonMetaACC]: Given agent Identifier is NOT valid!");
            e.printStackTrace();
            return null;
        } catch (final TucsonInvalidTupleCentreIdException e) {
            System.err
                    .println("[TucsonMetaACC]: Given tuple centre Identifier is NOT valid!");
            e.printStackTrace();
            return null;
        }
        return acc;
    }

    /**
     * Acquires the Negotiation ACC from the default TuCSoN node.
     *
     * @param aid
     *            the Identifier of the agent demanding for the ACC
     * @return the Negotiation ACC requested
     */
    public static NegotiationACC getNegotiationContext(final TucsonAgentId aid) {
        return getNegotiationContext(aid.toString());
    }

    /**
     * Acquires the Negotiation ACC from the TuCSoN node installed on the given
     * {@code netid:portno} IP:TCP address.
     *
     * @param aid
     *            the Identifier of the agent demanding for the ACC
     * @param netid
     *            the IP address of the TuCSoN node to contact
     * @param portno
     *            the TCP port number of the TuCSoN node to contact
     * @return the Negotiation ACC requested
     */
    public static NegotiationACC getNegotiationContext(final TucsonAgentId aid,
            final String netid, final int portno) {
        return getNegotiationContext(aid.toString(), netid,
                portno);
    }

    private TucsonMetaACC() {
        /*
         *
         */
    }
}
