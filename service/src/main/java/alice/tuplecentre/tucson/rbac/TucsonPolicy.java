package alice.tuplecentre.tucson.rbac;

import java.util.ArrayList;
import java.util.List;

import alice.logictuple.TupleArgument;

/**
 * Class implementing a RBAC policy.
 *
 * @author Emanuele Buccelli
 * @author (contributor) Stefano Mariani (mailto: s.mariani@unibo.it)
 *
 */
public class TucsonPolicy implements Policy {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * Builds a policy given its name and the set of permissions
     *
     * @param policyName
     *            the name of this policy
     * @param permissions
     *            the set of permissions
     * @return the policy just built
     */
    public static Policy createPolicy(final String policyName,
            final TupleArgument[] permissions) {
        final Policy policy = new TucsonPolicy(policyName);
        for (final TupleArgument p : permissions) {
            policy.addPermission(new TucsonPermission(p.getName()));
        }
        return policy;
    }

    private List<Permission> permissions;
    private String policyName;

    /**
     * Builds a policy given its name and the set of permissions
     *
     * @param polName
     *            the name of this policy
     * @param perms
     *            the set of permissions
     */
    public TucsonPolicy(final String polName, final List<Permission> perms) {
        this.policyName = polName;
        this.permissions = perms;
    }

    public TucsonPolicy(final String polName) {
        this(polName, new ArrayList<Permission>());
    }

    @Override
    public void addPermission(final Permission permission) {
        if (this.permissions == null) {
            this.permissions = new ArrayList<Permission>();
        }
        if (!this.permissions.contains(permission)) {
            this.permissions.add(permission);
        }
    }

    @Override
    public List<Permission> getPermissions() {
        return this.permissions;
    }

    @Override
    public String getPolicyName() {
        return this.policyName;
    }

    @Override
    public boolean hasPermissions(final List<String> permName) {
        for (final String perm : permName) {
            if (!this.hasPermission(perm)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void removePermission(final Permission permission) {
        if (this.permissions != null) {
            this.permissions.remove(permission);
        }
    }

    @Override
    public void setPermissions(final List<Permission> perms) {
        this.permissions = perms;
    }

    @Override
    public void setPolicyName(final String polName) {
        this.policyName = polName;
    }

    private boolean hasPermission(final String permName) {
        for (final Permission perm : this.permissions) {
            if (perm.getPermissionName().equalsIgnoreCase(permName)) {
                return true;
            }
        }
        return false;
    }
}
