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
package alice.tucson.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Properties;
import alice.tucson.rbac.Role;

/**
 * Agent Coordination Context Description class.
 *
 * It is meant to store information about the current ACC "session" held by a
 * TuCSoN Agent (such as its identity, role, etc.) toward the TuCSoN Node
 * Service. It is responsible to establish the connection between the user agent
 * proxy {@link alice.tucson.service.ACCProxyAgentSide agent} and the TuCSoN
 * Node Service proxy {@link alice.tucson.service.ACCProxyNodeSide node}. It
 * actually triggers the latter proxy creation by the ACC Provider
 * {@link alice.tucson.service.ACCProvider provider} spawned by the Tucson Node
 * Service {@link alice.tucson.service.TucsonNodeService TuCSoN}
 *
 * @see alice.tucson.service.ACCProxyAgentSide ACCProxyAgentSide
 * @see alice.tucson.service.ACCProxyNodeSide ACCProxyNodeSide
 * @see alice.tucson.service.ACCProvider ACCProvider
 * @see alice.tucson.service.TucsonNodeService TucsonNodeService
 *
 * @author Alessandro Ricci
 *
 */
public class ACCDescription implements Serializable {

    private static final long serialVersionUID = -8231854077657631541L;
    private final java.util.Properties properties;

    private HashMap<String, Role> roles;

    /**
     * Creates an ACCDescription as a Java Properties empty map.
     *
     * @see java.util.Properties Properties
     */
    public ACCDescription() {
        this.properties = new Properties();
        this.roles = new HashMap<String, Role>(); // galassi
    }

    /**
     * Creates an ACCDescription using the Java Properties instance passed.
     *
     * @param p
     *            Java Properties map to be used for initialization
     * @see java.util.Properties Properties
     */
    public ACCDescription(final Properties p) {
        this.properties = p;
        this.roles = new HashMap<String, Role>(); // galassi
    }

    public void addRole(final Role role) { // galassi
        if (!this.roles.containsValue(role)) {
            this.roles.put(role.getRoleName(), role);
        }
    }

    /**
     * Gets the named property from the Java Properties map
     *
     * @param name
     *            Named property to be retrieved
     * @return Value of the property retrieved
     * @see java.util.Properties Properties
     */
    public String getProperty(final String name) {
        return this.properties.getProperty(name);
    }

    public Role getRole(final Role role) { // galassi
        return this.roles.get(role.getRoleName());
    }

    public Role getRole(final String role) { // galassi
        return this.roles.get(role);
    }

    public HashMap<String, Role> getRoles() { // galassi
        return this.roles;
    }

    public void removeRole(final Role role) { // galassi
        this.removeRole(role.getRoleName());
    }

    public void removeRole(final String roleId) {
        this.roles.remove(roleId);
    }

    /**
     * Sets a new Java Property map entry using the Strings passed
     *
     * @param name
     *            Name of the property to store
     * @param value
     *            Value of the property to store
     * @see java.util.Properties Properties
     */
    public void setProperty(final String name, final String value) {
        this.properties.setProperty(name, value);
    }

    public void setRoles(final HashMap<String, Role> r) { // galassi
        this.roles = r;
    }
}
