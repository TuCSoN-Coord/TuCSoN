/**
 * Created by Stefano Mariani on 05/mag/2015 (mailto: s.mariani@unibo.it)
 */

/*
 * Copyright 1999-2014 Alma Mater Studiorum - Universita' di Bologna
 *
 * This file is part of TuCSoN4JADE <http://tucson4jade.apice.unibo.it>.
 *
 *    TuCSoN4JADE is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU Lesser General Public License as published
 *    by the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    TuCSoN4JADE is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU Lesser General Public License for more details.
 *
 *    You should have received a copy of the GNU Lesser General Public License
 *    along with TuCSoN4JADE.  If not, see
 *    <https://www.gnu.org/licenses/lgpl.html>.
 *
 */

package alice.tucson.examples.rbac;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import alice.logictuple.LogicTuple;
import alice.logictuple.Value;
import alice.tucson.api.AbstractTucsonAgent;
import alice.tucson.api.EnhancedACC;
import alice.tucson.api.ITucsonOperation;
import alice.tucson.api.NegotiationACC;
import alice.tucson.api.TucsonMetaACC;
import alice.tucson.api.TucsonTupleCentreId;
import alice.tucson.api.exceptions.AgentNotAllowedException;
import alice.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tucson.api.exceptions.TucsonInvalidTupleCentreIdException;
import alice.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tucson.api.exceptions.UnreachableNodeException;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;
import alice.tuplecentre.core.AbstractTupleCentreOperation;

/**
 * An authorised agent. It is "known" by TuCSoN-RBAC, thanks to administrators
 * configuration, thus may login to play roles different from the default one.
 * 
 * @author Stefano Mariani (mailto: s.mariani@unibo.it)
 *
 */
public final class AuthorisedAgent extends AbstractTucsonAgent {

    /**
     * @param id
     *            the ID of this TuCSoN agent
     * @param netid
     *            the IP address of the TuCSoN node it is willing to interact
     *            with
     * @param p
     *            the TCP port number of the TuCSoN node it is willing to
     *            interact with
     * @throws TucsonInvalidAgentIdException
     *             if the given String does not represent a valid TuCSoN agent
     *             ID
     */
    public AuthorisedAgent(final String id, final String netid, final int p)
            throws TucsonInvalidAgentIdException {
        super(id, netid, p);
    }

    /*
     * (non-Javadoc)
     * @see
     * alice.tucson.api.AbstractTucsonAgent#operationCompleted(alice.tuplecentre
     * .core.AbstractTupleCentreOperation)
     */
    @Override
    public void operationCompleted(final AbstractTupleCentreOperation op) {
        /*
         * Not used atm
         */
    }

    /*
     * (non-Javadoc)
     * @see
     * alice.tucson.api.AbstractTucsonAgent#operationCompleted(alice.tucson.
     * api.ITucsonOperation)
     */
    @Override
    public void operationCompleted(final ITucsonOperation op) {
        /*
         * Not used atm
         */
    }

    /*
     * (non-Javadoc)
     * @see alice.tucson.api.AbstractTucsonAgent#main()
     */
    @Override
    protected void main() {
        Logger.getLogger("AuthorisedAgent").info(
                "Acquiring NegotiationACC from TuCSoN Node installed on TCP port "
                        + this.myport());
        /*
         * A negotiation ACC must be acquired by any software entity willing to
         * exploit TuCSoN coordination services, thus willing to acquire an ACC.
         */
        NegotiationACC negACC = TucsonMetaACC
                .getNegotiationContext("authorised");
        Logger.getLogger("AuthorisedAgent").info("NegotiationACC acquired");
        List<String> permissions = new ArrayList<String>();
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
            ITucsonOperation op = acc.out(new TucsonTupleCentreId("default",
                    this.myNode(), String.valueOf(this.myport())),
                    new LogicTuple("test", new Value("hello")), (Long) null);
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
     * @param args
     *            program arguments: args[0] is TuCSoN Node TCP port number.
     */
    public static void main(final String[] args) {
        int portno = 20504;
        if (args.length == 1) {
            portno = Integer.parseInt(args[0]);
        }
        try {
            new AuthorisedAgent("authorised", "localhost", portno).go();
        } catch (TucsonInvalidAgentIdException e) {
            e.printStackTrace();
        }
    }

}
