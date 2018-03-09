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

package rbac;

import java.util.logging.Logger;

import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tuplecentre.tucson.network.exceptions.DialogInitializationException;
import alice.tuplecentre.tucson.service.TucsonNodeService;

/**
 * Example showcasing RBAC in TuCSoN. A TuCSoN node is configured with a set of
 * RBAC-related properties -- e.g. whether login is required and administrators'
 * credentials --, then 3 agents are started: an administrator agent, which
 * logins to change some RBAC properties, an authorised agent, which is allowed
 * to play some pre-defined role, and an unauthorised agent, which is allowed to
 * play solely the default role associated to the basic agent class--since login
 * is not required.
 * 
 * @author Stefano Mariani (mailto: s.mariani@unibo.it)
 *
 */
public final class RBACLauncher {

    private RBACLauncher() {
        /*
         * To prevent instantiation
         */
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
        TucsonNodeService tns = new TucsonNodeService(portno);
        /*
         * When login is not required, non-logged agents are allowed to
         * participate the TuCSoN-coordinated system anyway, but with restricted
         * access to coordination services--that is, only those enabled for the
         * default role associated to the basic agent class
         */
        tns.setLoginRequired(false);
        tns.setAdminUsername("admin");
        tns.setAdminPassword("psw");
        /*
         * The basic agent class is the class associated to non-logged agents by
         * RBAC-TuCSoN. Agents belonging to the basic agent class are restricted
         * to play the default role.
         */
        tns.setBasicAgentClass("basic");
        /*
         * To enhance security, requests for the list of all the roles playable
         * by a given agent may be forbidden.
         */
        tns.setListAllRolesAllowed(true);
        /*
         * To enhance security, inspection of the tuple centre '$ORG' -- the one
         * storing all the information regarding the RBAC configuration -- may
         * be forbidden.
         */
        tns.setInspectorsAuthorised(true);
        tns.install();
        try {
            while (!TucsonNodeService.isInstalled(portno, 1000)) {
                Thread.sleep(500);
            }
        } catch (DialogInitializationException | InterruptedException e) {
            e.printStackTrace();
        }
        Logger.getLogger("RBACLauncher").info(
                "TuCSoN Node installed on TCP port " + tns.getTCPPort());
        Logger.getLogger("RBACLauncher").info(
                "Launching administrator agent...");
        try {
            new AdminAgent("admin", "localhost", portno).go();
        } catch (TucsonInvalidAgentIdException e) {
            e.printStackTrace();
        }
        Logger.getLogger("RBACLauncher").info("Administrator agent launched");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Logger.getLogger("RBACLauncher").info(
                "Launching an authorised agent...");
        try {
            new AuthorisedAgent("authorised", "localhost", portno).go();
        } catch (TucsonInvalidAgentIdException e) {
            e.printStackTrace();
        }
        Logger.getLogger("RBACLauncher").info("Authorised agent launched");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } 
        Logger.getLogger("RBACLauncher").info(
                "Launching an unauthorised agent...");
        try {
            new UnauthorisedAgent("unauthorised", "localhost", portno).go();
        } catch (TucsonInvalidAgentIdException e) {
            e.printStackTrace();
        }
        Logger.getLogger("RBACLauncher").info("Unauthorised agent launched");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
