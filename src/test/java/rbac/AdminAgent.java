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

import java.util.logging.Logger;

import alice.tuplecentre.api.exceptions.OperationTimeOutException;
import alice.tuplecentre.respect.api.exceptions.OperationNotAllowedException;
import alice.tuplecentre.tucson.api.AbstractTucsonAgent;
import alice.tuplecentre.tucson.api.TucsonAgentId;
import alice.tuplecentre.tucson.api.TucsonMetaACC;
import alice.tuplecentre.tucson.api.acc.AdminACC;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tuplecentre.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tuplecentre.tucson.api.exceptions.UnreachableNodeException;
import alice.tuplecentre.tucson.rbac.AuthorisedAgent;
import alice.tuplecentre.tucson.rbac.Permission;
import alice.tuplecentre.tucson.rbac.Policy;
import alice.tuplecentre.tucson.rbac.Role;
import alice.tuplecentre.tucson.rbac.TucsonAuthorisedAgent;
import alice.tuplecentre.tucson.rbac.TucsonPermission;
import alice.tuplecentre.tucson.rbac.TucsonPolicy;
import alice.tuplecentre.tucson.rbac.TucsonRBACStructure;
import alice.tuplecentre.tucson.rbac.TucsonRole;
import alice.tuplecentre.tucson.service.TucsonInfo;

/**
 * The administrator agent, configuring the RBAC properties.
 * 
 * @author Stefano Mariani (mailto: s.mariani@unibo.it)
 *
 */
public final class AdminAgent extends AbstractTucsonAgent<AdminACC> {

    /**
     * @param id
     *            the Identifier copyOf this TuCSoN agent
     * @param netid
     *            the IP address copyOf the TuCSoN node it is willing to interact
     *            with
     * @param p
     *            the TCP port number copyOf the TuCSoN node it is willing to
     *            interact with
     * @throws TucsonInvalidAgentIdException
     *             if the given String does not represent a valid TuCSoN agent
     *             Identifier
     */
    public AdminAgent(final String id, final String netid, final int p)
            throws TucsonInvalidAgentIdException {
        super(id, netid, p);
    }

    @Override
    protected AdminACC retrieveACC(final TucsonAgentId aid, final String networkAddress, final int portNumber) {
        /*
         * An administrator ACC should be acquired in order to gain
         * administrative access to TuCSoN-RBAC.
         */

        Logger.getLogger("AdminAgent").info("AdminACC acquiring");
        return TucsonMetaACC.getAdminContext(aid, networkAddress, portNumber, "admin", "psw");
    }

    @Override
    protected void main() {
        Logger.getLogger("AdminAgent").info("AdminACC acquired");

        /*
         * Atm, TuCSoN RBAC permissions are simply the name copyOf a TuCSoN
         * primitive, indicating that an agent may invoke such operation.
         */
        Permission prd = new TucsonPermission("rd");
        Permission prdp = new TucsonPermission("rdp");
        Permission pin = new TucsonPermission("in");
        Permission pinp = new TucsonPermission("inp");
        Permission pout = new TucsonPermission("out");
        /*
         * TuCSoN RBAC policies are composed by permissions and associated to
         * role, indicating which TuCSoN operations the role allows.
         */
        Policy policyrd = new TucsonPolicy("policyrd");
        Policy policyrdrdp = new TucsonPolicy("policyrdrdp");
        Policy policyin = new TucsonPolicy("policyin");
        Policy policyout = new TucsonPolicy("policyout");
        policyrd.addPermission(prd);
        policyrdrdp.addPermission(prd);
        policyrdrdp.addPermission(prdp);
        policyin.addPermission(pin);
        policyin.addPermission(pinp);
        policyout.addPermission(pout);
        /*
         * TuCSoN RBAC roles associate policies to agent classes, indicating the
         * set copyOf permissions agents copyOf that class, and willing to play that
         * role, can be granted.
         */
        Role roleRead = new TucsonRole("roleRead");
        roleRead.setPolicy(policyrd);
        Role roleReadP = new TucsonRole("roleReadP");
        roleReadP.setPolicy(policyrdrdp);
        Role roleReadIn = new TucsonRole("roleReadIn");
        roleReadIn.setAgentClass("readClass");
        roleReadIn.setPolicy(policyin);
        Role roleWrite = new TucsonRole("roleWrite");
        roleWrite.setAgentClass("writeClass");
        roleWrite.setPolicy(policyout);
        /*
         * In order to be associated to an agent class different from the basic
         * one, thus to play roles different from the default one, agents must
         * log into TuCSoN-RBAC.
         */
        AuthorisedAgent agent1 = new TucsonAuthorisedAgent("readClass",
                "user12", "psw1");
        AuthorisedAgent agent2 = new TucsonAuthorisedAgent("readClass",
                "user12", "psw2");
        AuthorisedAgent agent3 = new TucsonAuthorisedAgent("writeClass",
                "user3", "psw3");
        TucsonRBACStructure rbac = new TucsonRBACStructure("acme-org");
        /*
         * Roles, policies and authorised agents should be explicitly set in the
         * RBAC configuration.
         */
        rbac.addRole(roleRead);
        rbac.addRole(roleReadP);
        rbac.addRole(roleReadIn);
        rbac.addRole(roleWrite);
        rbac.addPolicy(policyrd);
        rbac.addPolicy(policyrdrdp);
        rbac.addPolicy(policyin);
        rbac.addPolicy(policyout);
        rbac.addAuthorisedAgent(agent1);
        rbac.addAuthorisedAgent(agent2);
        rbac.addAuthorisedAgent(agent3);
        /*
         * Administrator agents may change TuCSoN node RBAC-related properties.
         */
        rbac.allowInspection(true);
        rbac.setBasicAgentClass("newBasicClass");
        rbac.requireLogin(false);
        Logger.getLogger("AdminAgent").info(
                "Acquiring AdminACC from TuCSoN Node installed on TCP port "
                        + this.myPort());

        try {
            Logger.getLogger("AdminAgent")
                    .info("Installing RBAC configuration");
            getACC().install(rbac);
            Logger.getLogger("AdminAgent").info("RBAC configuration installed");
            Logger.getLogger("AdminAgent").info("Removing policy 'policyrd'");
            getACC().removePolicy("policyrd");
            Logger.getLogger("AdminAgent").info("Policy 'policyrd' removed");
            Logger.getLogger("AdminAgent").info("Removing role 'roleRd'");
            getACC().removeRole("roleRead");
            Logger.getLogger("AdminAgent").info("Role 'roleRd' removed");
            Logger.getLogger("AdminAgent").info(
                    "Changing basic agent class to 'yetAnotherBasicClass'");
            getACC().setBasicAgentClass("yetAnotherBasicClass");
            Logger.getLogger("AdminAgent").info(
                    "Basic agent class changed to 'yetAnotherBasicClass'");
            Logger.getLogger("AdminAgent").info("Releasing AdminACC");
            getACC().exit();
            Logger.getLogger("AdminAgent").info("AdminACC released, bye!");
        } catch (TucsonOperationNotPossibleException | UnreachableNodeException
                | OperationTimeOutException | OperationNotAllowedException e) {
            e.printStackTrace();
        } 
    }

    /**
     * @param args
     *            program arguments: args[0] is TuCSoN Node TCP port number.
     */
    public static void main(final String[] args) {
        int portno = TucsonInfo.getDefaultPortNumber();
        if (args.length == 1) {
            portno = Integer.parseInt(args[0]);
        }
        try {
            new AdminAgent("admin", "localhost", portno).go();
        } catch (TucsonInvalidAgentIdException e) {
            e.printStackTrace();
        }
    }

}
