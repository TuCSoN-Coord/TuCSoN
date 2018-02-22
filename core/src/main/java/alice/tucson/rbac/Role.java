package alice.tucson.rbac;

import java.io.Serializable;

/**
 * Interface representing a RBAC (Role-Based Access Control) role.
 *
 * @author Emanuele Buccelli
 * @author (contributor) Stefano Mariani (mailto: s.mariani@unibo.it)
 */
public interface Role extends Serializable {

    /**
     * Gets the agent class this role is associated to
     *
     * @return the agent class of this role
     */
    String getAgentClass();

    /**
     * Gets the description of this role
     *
     * @return the description of this role
     */
    String getDescription();

    /**
     * Gets the policy associated to this role
     *
     * @return the policy of this role
     */
    Policy getPolicy();

    /**
     * Gets the name of this role
     *
     * @return the name of this role
     */
    String getRoleName();

    /**
     * Replaces the agent class associated to this role
     *
     * @param agentClass the new agent class to associate to this role
     */
    void setAgentClass(final String agentClass);

    /**
     * Replaces the description of this role
     *
     * @param roleDescription the new description of this role
     */
    void setDescription(final String roleDescription);

    /**
     * Replaces the policy associated to this role
     *
     * @param policy the new policy associated to this role
     */
    void setPolicy(final Policy policy);

    /**
     * Replaces the name of this role
     *
     * @param roleName the new name of this role
     */
    void setRoleName(final String roleName);
}
