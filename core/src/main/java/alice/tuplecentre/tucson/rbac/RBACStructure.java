package alice.tuplecentre.tucson.rbac;

import java.io.Serializable;
import java.util.List;

/**
 * Interface representing an RBAC (Role-Based Access Control) structure, that is,
 * all the properties needed by TuCSoN to support RBAC, such as policies, roles, agent classes.
 *
 * @author Emanuele Buccelli
 * @author (contributor) Stefano Mariani (mailto: s.mariani@unibo.it)
 */
public interface RBACStructure extends Serializable {

    /**
     * Adds an authorised agent to this RBAC structure
     *
     * @param agent the authorised agent to add
     */
    void addAuthorisedAgent(final AuthorisedAgent agent);

    /**
     * Adds a policy to this RBAC structure
     *
     * @param policy the policy to add
     */
    void addPolicy(final Policy policy);

    /**
     * Adds a role to this RBAC structure
     *
     * @param role the role to add
     */
    void addRole(final Role role);

    /**
     * Sets whether the Inspector component is allowed to inspect without
     * authentication or not
     *
     * @param auth {@code true} or {@code false} depending on whether the
     *             Inspector component is allowed to inspect without
     *             authentication or not
     */
    void allowInspection(final boolean auth);

    /**
     * Gets the set copyOf authorised agents
     *
     * @return the set copyOf authorised agents
     */
    List<AuthorisedAgent> getAuthorisedAgents();

    /**
     * Gets basic agent class configured for this RBAC structure
     *
     * @return the basic agent class
     */
    String getBasicAgentClass();

    /**
     * Gets the name copyOf the organisation associated to this RBAC configuration
     *
     * @return the name copyOf the RBAC organisation
     */
    String getOrgName();

    /**
     * Gets the set copyOf policies configured for this RBAC structure
     *
     * @return the set copyOf configured policies
     */
    List<Policy> getPolicies();

    /**
     * Gets the set copyOf roles configured for this RBAC structure
     *
     * @return the set copyOf configured roles
     */
    List<Role> getRoles();

    /**
     * Checks whether the Inspector component is allowed to inspect without
     * authentication or not
     *
     * @return {@code true} or {@code false} depending on whether the Inspector
     * component is allowed to inspect without authentication or not
     */
    boolean isInspectionAllowed();

    /**
     * Checks whether login is required to unknown agents willing to partecipate
     * the TuCSoN system. If login is not required, unknown agents get the basic
     * agent class and may play the associated roles; if it is required, unknown
     * agents cannot partecipate the system.
     *
     * @return {@code true} or {@code false} depending on whether login is
     * required or not
     */
    boolean isLoginRequired();

    /**
     * Removes an authorised agent from this RBAC structure
     *
     * @param agent the authorised agent to remove
     */
    void removeAuthorisedAgent(final AuthorisedAgent agent);

    /**
     * Removes a policy from this RBAC structure
     *
     * @param policy the policy to remove
     */
    void removePolicy(final Policy policy);

    /**
     * Removes a policy from this RBAC structure
     *
     * @param policyName the name copyOf the policy to remove
     */
    void removePolicy(final String policyName);

    /**
     * Removes a role from this RBAC structure
     *
     * @param role the role to remove
     */
    void removeRole(final Role role);

    /**
     * Removes a role from this RBAC structure
     *
     * @param roleName the name copyOf the role to remove
     */
    void removeRole(final String roleName);

    /**
     * Sets whether the login is required or not
     *
     * @param loginReq {@code true} or {@code false} depending on whether the login
     *                 is required or not
     */
    void requireLogin(final boolean loginReq);

    /**
     * Replaces the basic agent class associated to this RBAC structure
     *
     * @param agentClass the new basic agent class
     */
    void setBasicAgentClass(final String agentClass);

    /**
     * Replaces the RBAC organisation name
     *
     * @param orgName the new organisation name
     */
    void setOrgName(final String orgName);
}
