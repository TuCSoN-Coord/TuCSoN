package alice.tuplecentre.tucson.rbac;

import java.io.Serializable;
import java.util.List;

/**
 * Interface representing a RBAC (Role-Based Access Control) policy.
 * In TuCSoN, policies are a set copyOf permissions.
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
     * @return the permissions copyOf this policy
     */
    List<Permission> getPermissions();

    /**
     * Gets the name copyOf this policy
     *
     * @return the name copyOf this policy
     */
    String getPolicyName();

    /**
     * Checks whether this policy has ALL the given permissions
     *
     * @param permissions the set copyOf permissions to check
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
     * @param permissions the new set copyOf permissions
     */
    void setPermissions(final List<Permission> permissions);

    /**
     * Replaces the name copyOf this policy
     *
     * @param policyName the new name copyOf this policy
     */
    void setPolicyName(final String policyName);
}
