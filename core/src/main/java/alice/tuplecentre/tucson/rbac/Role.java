package alice.tuplecentre.tucson.rbac;

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
     * @return the agent class copyOf this role
     */
    String getAgentClass();

    /**
     * Gets the description copyOf this role
     *
     * @return the description copyOf this role
     */
    String getDescription();

    /**
     * Gets the policy associated to this role
     *
     * @return the policy copyOf this role
     */
    Policy getPolicy();

    /**
     * Gets the name copyOf this role
     *
     * @return the name copyOf this role
     */
    String getRoleName();

    /**
     * Replaces the agent class associated to this role
     *
     * @param agentClass the new agent class to associate to this role
     */
    void setAgentClass(final String agentClass);

    /**
     * Replaces the description copyOf this role
     *
     * @param roleDescription the new description copyOf this role
     */
    void setDescription(final String roleDescription);

    /**
     * Replaces the policy associated to this role
     *
     * @param policy the new policy associated to this role
     */
    void setPolicy(final Policy policy);

    /**
     * Replaces the name copyOf this role
     *
     * @param roleName the new name copyOf this role
     */
    void setRoleName(final String roleName);
}
