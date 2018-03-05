package alice.tuplecentre.tucson.rbac;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class implementing a RBAC structure in TuCSoN.
 *
 * @author Emanuele Buccelli
 * @author (contributor) Stefano Mariani (mailto: s.mariani@unibo.it)
 *
 */
public class TucsonRBACStructure implements RBACStructure {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private final List<AuthorisedAgent> authorisedAgents;
    private String basicAgentClass;
    private boolean isInspectionAuthorised;
    private boolean isLoginRequired;
    private String orgName;
    private final Map<String, Policy> policies;
    private final Map<String, Role> roles;

    /**
     * Builds a empty RBAC structure.
     *
     * @param org
     *            the name of the organisation represented by this RBAC
     *            structure
     */
    public TucsonRBACStructure(final String org) {
        this.roles = new HashMap<String, Role>();
        this.policies = new HashMap<String, Policy>();
        this.authorisedAgents = new ArrayList<AuthorisedAgent>();
        this.orgName = org;
        this.isLoginRequired = false;
        this.isInspectionAuthorised = false;
        this.basicAgentClass = "basicAgentClass";
    }

    @Override
    public void addAuthorisedAgent(final AuthorisedAgent agent) {
        if (!this.authorisedAgents.contains(agent)) {
            this.authorisedAgents.add(agent);
        }
    }

    @Override
    public void addPolicy(final Policy policy) {
        if (policy != null && this.policies.get(policy.getPolicyName()) == null) {
            this.policies.put(policy.getPolicyName(), policy);
        }
        /*
         * TODO: log something
         */
    }

    @Override
    public void addRole(final Role role) {
        if (role != null && this.roles.get(role.getRoleName()) == null) {
            this.roles.put(role.getRoleName(), role);
        }
        /*
         * TODO: log something
         */
    }

    @Override
    public void allowInspection(final boolean auth) {
        this.isInspectionAuthorised = auth;
    }

    @Override
    public List<AuthorisedAgent> getAuthorisedAgents() {
        return this.authorisedAgents;
    }

    @Override
    public String getBasicAgentClass() {
        return this.basicAgentClass;
    }

    @Override
    public String getOrgName() {
        return this.orgName;
    }

    @Override
    public List<Policy> getPolicies() {
        return new ArrayList<Policy>(this.policies.values());
    }

    @Override
    public List<Role> getRoles() {
        return new ArrayList<Role>(this.roles.values());
    }

    @Override
    public boolean isInspectionAllowed() {
        return this.isInspectionAuthorised;
    }

    @Override
    public boolean isLoginRequired() {
        return this.isLoginRequired;
    }

    @Override
    public void removeAuthorisedAgent(final AuthorisedAgent agent) {
        if (this.authorisedAgents.contains(agent)) {
            this.authorisedAgents.remove(agent);
        }
    }

    @Override
    public void removePolicy(final Policy policy) {
        if (policy != null && this.policies.containsKey(policy.getPolicyName())) {
            this.policies.remove(policy.getPolicyName());
        }
    }

    @Override
    public void removePolicy(final String policyName) {
        if (this.policies.containsKey(policyName)) {
            this.policies.remove(policyName);
        }
    }

    @Override
    public void removeRole(final Role role) {
        if (role != null && this.roles.containsKey(role.getRoleName())) {
            this.roles.remove(role.getRoleName());
        }
    }

    @Override
    public void removeRole(final String roleName) {
        this.roles.remove(roleName);
    }

    @Override
    public void requireLogin(final boolean loginReq) {
        this.isLoginRequired = loginReq;
    }

    @Override
    public void setBasicAgentClass(final String agentClass) {
        this.basicAgentClass = agentClass;
    }

    @Override
    public void setOrgName(final String org) {
        this.orgName = org;
    }

}
