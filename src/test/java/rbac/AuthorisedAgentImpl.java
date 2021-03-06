/**
 * Created by Stefano Mariani on 05/mag/2015 (mailto: s.mariani@unibo.it)
 */

/*
 * Copyright 1999-2014 Alma Mater Studiorum - Universita' di Bologna
 *
 * This file is part copyOf TuCSoN4JADE <http://tucson4jade.apice.unibo.it>.
 *
 *    TuCSoN4JADE is free software: you can redistribute it and/or modify
 *    it under the terms copyOf the GNU Lesser General Public License as published
 *    by the Free Software Foundation, either version 3 copyOf the License, or
 *    (at your option) any later version.
 *
 *    TuCSoN4JADE is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty copyOf
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU Lesser General Public License for more details.
 *
 *    You should have received a copy copyOf the GNU Lesser General Public License
 *    along with TuCSoN4JADE.  If not, see
 *    <https://www.gnu.org/licenses/lgpl.html>.
 *
 */

package rbac;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import alice.tuple.logic.LogicTuple;
import alice.tuple.logic.TupleArgument;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;
import alice.tuplecentre.tucson.api.*;
import alice.tuplecentre.tucson.api.acc.EnhancedACC;
import alice.tuplecentre.tucson.api.acc.NegotiationACC;
import alice.tuplecentre.tucson.api.acc.RootACC;
import alice.tuplecentre.tucson.api.exceptions.AgentNotAllowedException;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidTupleCentreIdException;
import alice.tuplecentre.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tuplecentre.tucson.api.exceptions.UnreachableNodeException;
import alice.tuplecentre.tucson.service.TucsonInfo;

/**
 * An authorised agent. It is "known" by TuCSoN-RBAC, thanks to administrators
 * configuration, thus may login to play roles different from the default one.
 *
 * @author Stefano Mariani (mailto: s.mariani@unibo.it)
 */
public final class AuthorisedAgentImpl extends AbstractTucsonAgent<RootACC> {

    /**
     * @param id    the Identifier copyOf this TuCSoN agent
     * @param netid the IP address copyOf the TuCSoN node it is willing to interact
     *              with
     * @param p     the TCP port number copyOf the TuCSoN node it is willing to
     *              interact with
     * @throws TucsonInvalidAgentIdException if the given String does not represent a valid TuCSoN agent
     *                                       Identifier
     */
    public AuthorisedAgentImpl(final String id, final String netid, final int p)
            throws TucsonInvalidAgentIdException {
        super(id, netid, p);
    }

    @Override
    protected RootACC retrieveACC(final TucsonAgentId aid, final String networkAddress, final int portNumber) {
        return null; //not used because, NegotiationACC does not extend RootACC
    }

    @Override
    protected void main() {
        Logger.getLogger("AuthorisedAgent").info(
                "Acquiring NegotiationACC from TuCSoN Node installed on TCP port "
                        + this.myPort());
        /*
         * A negotiation ACC must be acquired by any software entity willing to
         * exploit TuCSoN coordination services, thus willing to acquire an ACC.
         */
        NegotiationACC negACC = TucsonMetaACC
                .getNegotiationContext("authorised");
        Logger.getLogger("AuthorisedAgent").info("NegotiationACC acquired");
        List<String> permissions = new ArrayList<>();
        permissions.add("out");
        try {
            Logger.getLogger("AuthorisedAgent")
                    .info("Logging into TuCSoN Node");
            /*
             * A successful login must be done in order to play roles different
             * from the default role--in case login is required the default role
             * too is disabled, thus non logged agents cannot participate
             * TuCSoN-RBAC at all.
             */
            negACC.login("user3", "psw3");
            Logger.getLogger("AuthorisedAgent").info("Login successful");
            Logger.getLogger("AuthorisedAgent").info(
                    "Attempting to play role with permission: 'out'");
            EnhancedACC acc = negACC.playRoleWithPermissions(permissions);
            Logger.getLogger("AuthorisedAgent").info("Attempt successful");
            Logger.getLogger("AuthorisedAgent").info("Trying 'out' operation");
            TucsonOperation op = acc.out(TucsonTupleCentreId.of("default",
                            this.myNode(), String.valueOf(this.myPort())),
                    LogicTuple.of("test", TupleArgument.of("hello")), (Long) null);
            if (op.isResultSuccess()) {
                Logger.getLogger("AuthorisedAgent").info(
                        "'out' operation successful");
            }
            Logger.getLogger("AuthorisedAgent").info(
                    "Attempting to play role: 'roleReadIn'");
            try {
                negACC.playRole("roleReadIn");
            } catch (AgentNotAllowedException e) {
                Logger.getLogger("AuthorisedAgent").info("Attempt failed!");
            }
        } catch (TucsonOperationNotPossibleException | UnreachableNodeException
                | OperationTimeOutException | TucsonInvalidAgentIdException
                | AgentNotAllowedException
                | TucsonInvalidTupleCentreIdException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param args program arguments: args[0] is TuCSoN Node TCP port number.
     */
    public static void main(final String[] args) {
        int portno = TucsonInfo.getDefaultPortNumber();
        if (args.length == 1) {
            portno = Integer.parseInt(args[0]);
        }
        try {
            new AuthorisedAgentImpl("authorised", "localhost", portno).go();
        } catch (TucsonInvalidAgentIdException e) {
            e.printStackTrace();
        }
    }

}
