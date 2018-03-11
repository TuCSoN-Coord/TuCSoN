package alice.tuplecentre.tucson.rbac;

import alice.tuplecentre.tucson.utilities.Utils;

/**
 * Class implementing a RBAC role in TuCSoN.
 *
 * @author Emanuele Buccelli
 * @author (contributor) Stefano Mariani (mailto: s.mariani@unibo.it)
 */
public class TucsonRole implements Role {

    private static final long serialVersionUID = 1L;
    private String agentClass;
    private Policy policy;
    private String roleDescription;
    private String roleName;

    /**
     * Builds a RBAC role given its name
     *
     * @param rn the name of this role
     */
    public TucsonRole(final String rn) {
        this(rn, "substitute");
    }

    /**
     * Builds a RBAC role given its name and associated agent class
     *
     * @param rn the name of this role
     * @param ac the agent class
     */
    public TucsonRole(final String rn, final String ac) {
        this.setRoleName(rn);
        this.setAgentClass(ac);
    }

    @Override
    public String getAgentClass() {
        if (this.agentClass == null || this.agentClass.equalsIgnoreCase("")) {
            return "substitute";
        }
        return this.agentClass;
    }

    @Override
    public String getDescription() {
        if (this.roleDescription != null
                && !this.roleDescription.equalsIgnoreCase("")) {
            return this.roleDescription;
        }
        return "role_" + this.roleName;
    }

    @Override
    public Policy getPolicy() {
        return this.policy;
    }

    @Override
    public String getRoleName() {
        return this.roleName;
    }

    @Override
    public void setAgentClass(final String ac) {
        this.agentClass = Utils.decapitalize(ac);
    }

    @Override
    public void setDescription(final String rd) {
        this.roleDescription = rd;
    }

    @Override
    public void setPolicy(final Policy p) {
        this.policy = p;
    }

    @Override
    public void setRoleName(final String rn) {
        if (rn != null && !rn.equals("")) {
            this.roleName = Utils.decapitalize(rn);
        }
    }
}
