package alice.tucson.rbac;

/**
 * Class implementing a RBAC permission.
 *
 * @author Emanuele Buccelli
 * @author (contributor) Stefano Mariani (mailto: s.mariani@unibo.it)
 */
public class TucsonPermission implements Permission {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private final String permissionName;

    /**
     * Builds the permission representation.
     *
     * @param perm the name of the permission. In TuCSoN, atm, a permission name
     *             corresponds to the name of a TuCSoN primitive.
     */
    public TucsonPermission(final String perm) {
        this.permissionName = perm;
    }

    @Override
    public String getPermissionName() {
        return this.permissionName;
    }

}
