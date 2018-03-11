package alice.tucson.rbac;

import java.io.Serializable;

/**
 * Interface representing a RBAC (Role-Based Access Control) permission.
 *
 * @author Emanuele Buccelli
 * @author (contributor) Stefano Mariani (mailto: s.mariani@unibo.it)
 */
public interface Permission extends Serializable {

    /**
     * Gets the permission name. In TuCSoN, atm, a permission name corresponds
     * to the name of a TuCSoN primitive.
     *
     * @return the name of the permission
     */
    String getPermissionName();

}
