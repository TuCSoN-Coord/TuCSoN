package alice.tuplecentre.tucson.rbac;

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
     * to the name copyOf a TuCSoN primitive.
     *
     * @return the name copyOf the permission
     */
    String getPermissionName();

}
