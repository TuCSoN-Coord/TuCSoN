package alice.tucson.rbac;

import java.io.Serializable;
import java.util.List;

/**
 * Interface representing a RBAC (Role-Based Access Control) policy.
 * In TuCSoN, policies are a set of permissions.
 *
 * @author Emanuele Buccelli
 * @author (contributor) Stefano Mariani (mailto: s.mariani@unibo.it)
 */
public interface Policy extends Serializable {

    /**
     * Adds a permission to this policy.
     *
     * @param permission the permission to add
     */
    void addPermission(final Permission permission);

    /**
     * Gets the permissions associated to this policy
     *
     * @return the permissions of this policy
     */
    List<Permission> getPermissions();

    /**
     * Gets the name of this policy
     *
     * @return the name of this policy
     */
    String getPolicyName();

    /**
     * Checks whether this policy has ALL the given permissions
     *
     * @param permissions the set of permissions to check
     * @return {@code true} or {@code false} depending on wether ALL the
     * permissions were found
     */
    boolean hasPermissions(final List<String> permissions); //TODO sarebbe meglio passare una lista di Permission???

    /**
     * Removes the given permission from this policy
     *
     * @param permission the permission to remove
     */
    void removePermission(final Permission permission);

    /**
     * Replaces the permissions associated to this policy
     *
     * @param permissions the new set of permissions
     */
    void setPermissions(final List<Permission> permissions);

    /**
     * Replaces the name of this policy
     *
     * @param policyName the new name of this policy
     */
    void setPolicyName(final String policyName);
}
