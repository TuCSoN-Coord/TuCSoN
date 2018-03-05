package alice.tuplecentre.tucson.service;

import java.net.InetAddress;
import java.net.UnknownHostException;

import alice.logictuple.LogicTuple;
import alice.logictuple.TupleArgument;
import alice.logictuple.Value;
import alice.logictuple.Var;
import alice.logictuple.exceptions.InvalidTupleArgumentException;
import alice.logictuple.exceptions.InvalidVarNameException;
import alice.tuplecentre.api.TupleCentreIdentifier;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;
import alice.tuplecentre.respect.api.exceptions.OperationNotAllowedException;
import alice.tuplecentre.tucson.api.TucsonOperation;
import alice.tuplecentre.tucson.api.TucsonTupleCentreIdDefault;
import alice.tuplecentre.tucson.api.acc.AdminACC;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidTupleCentreIdException;
import alice.tuplecentre.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tuplecentre.tucson.api.exceptions.UnreachableNodeException;
import alice.tuplecentre.tucson.rbac.AuthorisedAgent;
import alice.tuplecentre.tucson.rbac.Permission;
import alice.tuplecentre.tucson.rbac.Policy;
import alice.tuplecentre.tucson.rbac.RBACStructure;
import alice.tuplecentre.tucson.rbac.Role;
import alice.tuplecentre.tucson.rbac.TucsonAuthorisedAgent;
import alice.tuplecentre.tucson.service.tools.TucsonACCTool;
import alice.tuplecentre.tucson.utilities.Utils;

/**
 * Class implementing the Administrator ACC.
 *
 * @author Emanuele Buccelli
 * @author (contributor) Stefano Mariani (mailto: s.mariani@unibo.it)
 *
 */
public class AdminACCProxyAgentSide extends ACCProxyAgentSide implements
        AdminACC {

    private static final int DEF_PORT = 20504;
    private boolean isAdminAuth;
    private final TupleCentreIdentifier tid;

    /**
     * Builds an Administrator ACC given the associated agent Identifier or name
     *
     * @param aid
     *            the associated agent Identifier or name (String)
     * @throws TucsonInvalidAgentIdException
     *             if the given agent Identifier is NOT valid
     * @throws TucsonInvalidTupleCentreIdException
     *             if the given tuple centre Identifier is NOT valid
     */
    public AdminACCProxyAgentSide(final Object aid)
            throws TucsonInvalidAgentIdException,
            TucsonInvalidTupleCentreIdException {
        this(aid, "localhost", AdminACCProxyAgentSide.DEF_PORT);
    }

    /**
     * Builds an Administrator ACC given the associated agent Identifier or name, the IP
     * address of the TuCSoN node the agent is willing to interact with, and the
     * TCP port also.
     *
     * @param aid
     *            the associated agent Identifier or name (String)
     * @param node
     *            the IP address of the target TuCSoN node
     * @param port
     *            the TCP port of the target TuCSoN node
     * @throws TucsonInvalidAgentIdException
     *             if the given agent Identifier is NOT valid
     * @throws TucsonInvalidTupleCentreIdException
     *             if the given tuple centre Identifier is NOT valid
     */
    public AdminACCProxyAgentSide(final Object aid, final String node,
            final int port) throws TucsonInvalidAgentIdException,
            TucsonInvalidTupleCentreIdException {
        this(aid, node, port, "", "");
    }

    /**
     * Builds an Administrator ACC given the associated agent Identifier or name, the IP
     * address of the TuCSoN node the agent is willing to interact with, the TCP
     * port also, as well as the agent username and (encrypted) password.
     *
     * @param aid
     *            the associated agent Identifier or name (String)
     * @param node
     *            the IP address of the target TuCSoN node
     * @param port
     *            the TCP port of the target TuCSoN node
     * @param uname
     *            the associated agent user name
     * @param psw
     *            the associated agent (encrypted) password
     * @throws TucsonInvalidAgentIdException
     *             if the given agent Identifier is NOT valid
     * @throws TucsonInvalidTupleCentreIdException
     *             if the given tuple centre Identifier is NOT valid
     */
    public AdminACCProxyAgentSide(final Object aid, final String node,
            final int port, final String uname, final String psw)
            throws TucsonInvalidAgentIdException,
            TucsonInvalidTupleCentreIdException {
        super(aid, node, port);
        this.username = uname;
        this.password = psw;
        this.isAdminAuth = false;
        this.tid = this.getTid(node, port);
        this.playAdminRole();
    }

    @Override
    public void add(final AuthorisedAgent agent)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException {
        this.addAuthorisedAgent(agent, (Long) null);
    }

    // TODO: Non funziona l'inserimento in lista
    @Override
    public void add(final Permission permission, final String policyName)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException {

        try {
            final LogicTuple setPermissionTuple = new LogicTuple(
                    "add_permission", new Value(policyName), new Value(
                            permission.getPermissionName()), new Var("Result"));
            final TucsonOperation op = this.inp(this.tid, setPermissionTuple,
                    (Long) null);
            if (op.isResultSuccess()) {
                final LogicTuple res = op.getLogicTupleResult();
                if (res.getArg(2).getName().equalsIgnoreCase("ok")) {
                    this.log("Permission " + permission.getPermissionName()
                            + " added to " + policyName);
                } else if (res.getArg(2).getName().equalsIgnoreCase("failed")) {
                    final String failReason = res.getArg(2).getArg(0)
                            .toString();
                    this.log("Permission " + permission.getPermissionName()
                            + " NOT added because " + failReason);
                }
            }
        } catch (final InvalidVarNameException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void add(final Policy policy)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException {
        this.addPolicy(policy, (Long) null);
    }

    @Override
    public void install(final RBACStructure rbac)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException,
            OperationNotAllowedException {
        this.install(rbac, null, this.node, this.port);
    }

    @Override
    public void install(final RBACStructure rbac, final Long timeout,
            final String n, final int p)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException,
            OperationNotAllowedException {

        if (rbac != null) {

            if (!this.isAdminAuth) {
                throw new OperationNotAllowedException();
            }
            this.installRBACSupport();
            final LogicTuple orgTuple = new LogicTuple("organisation_name",
                    new Value(rbac.getOrgName()));
            TucsonOperation op = this.out(this.tid, orgTuple, timeout);
            if (op.isResultSuccess()) {
                final LogicTuple res = op.getLogicTupleResult();
                this.log("Installing RBAC configuration for organisation: "
                        + res.getArg(0));
            } else {
                this.log("Cannot install RBAC for organisation: "
                        + rbac.getOrgName());
            }
            final LogicTuple basicAgClassTuple = new LogicTuple(
                    "set_basic_agent_class", new Value(
                            rbac.getBasicAgentClass()));
            op = this.inp(this.tid, basicAgClassTuple, timeout);
            for (final Role role : rbac.getRoles()) {
                this.addRole(role, timeout);
            }
            for (final Policy policy : rbac.getPolicies()) {
                this.addPolicy(policy, timeout);
            }
            for (final AuthorisedAgent authAgent : rbac.getAuthorisedAgents()) {
                this.addAuthorisedAgent(authAgent, timeout);
            }
            LogicTuple loginTuple = null;
            if (rbac.isLoginRequired()) {
                loginTuple = new LogicTuple("set_login", new Value("yes"));
            } else {
                loginTuple = new LogicTuple("set_login", new Value("no"));
            }
            op = this.inp(this.tid, loginTuple, timeout);
            LogicTuple inspectorsTuple = null;
            if (rbac.isInspectionAllowed()) {
                inspectorsTuple = new LogicTuple("authorise_inspection",
                        new Value("yes"));
            } else {
                inspectorsTuple = new LogicTuple("authorise_inspection",
                        new Value("no"));
            }
            op = this.inp(this.tid, inspectorsTuple, timeout);
            if (op.isResultSuccess()) {
                final LogicTuple res = op.getLogicTupleResult();
                this.log("Inspection allowed: " + res.getArg(0));
            } else {
                this.log("Error while trying to allow inspection!");
            }

        } else {
            this.log("Empty RBAC structure given, nothing done.");
        }

    }

    @Override
    public void add(final Role role)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException {
        this.addRole(role, (Long) null);
    }

    @Override
    public void remove(final String agentName) {
        // TODO
    }

    @Override
    public void removePolicy(final String policyName)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException {
        final Value policy = new Value(policyName);
        final LogicTuple removePolicyTuple = new LogicTuple("remove_policy",
                policy);
        this.inp(this.tid, removePolicyTuple, (Long) null);
        this.log("Removed policy: " + policy);
    }

    @Override
    public void removeRBAC() throws OperationNotAllowedException,
            TucsonOperationNotPossibleException, UnreachableNodeException,
            OperationTimeOutException {
        this.removeRBAC(null, this.node, this.port);
    }

    @Override
    public void removeRBAC(final Long l, final String n, final int p)
            throws OperationNotAllowedException,
            TucsonOperationNotPossibleException, UnreachableNodeException,
            OperationTimeOutException {
        if (!this.isAdminAuth) {
            throw new OperationNotAllowedException();
        }
        this.inp(this.tid, new LogicTuple("disinstall_rbac"), (Long) null);
    }

    @Override
    public void removeRole(final String roleName)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException {
        final Value role = new Value(Utils.decapitalize(roleName));
        final LogicTuple roleDestructionTuple = new LogicTuple("remove_role",
                role);
        this.inp(this.tid, roleDestructionTuple, (Long) null);
        this.log("Removed role: " + role);
    }

    @Override
    public void setBasicAgentClass(final String newBasicAgentClass)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException {
        final LogicTuple newClassTuple = new LogicTuple(
                "set_basic_agent_class", new Value(newBasicAgentClass));
        final TucsonOperation op = this.inp(this.tid, newClassTuple,
                (Long) null);
        if (op.isResultSuccess()) {
            this.log("Changed basic agent class to: " + newBasicAgentClass);
        } else {
            this.log("Error while changing basic agent class!");
        }
    }

    @Override
    public void setRoleAgentClass(final String roleName, final String agentClass)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException {

        LogicTuple setRoleClassTuple;
        try {
            setRoleClassTuple = new LogicTuple("set_role_class", new Value(
                    roleName), new Value(agentClass), new Var("Result"));
            final TucsonOperation op = this.inp(this.tid, setRoleClassTuple,
                    (Long) null);
            if (op.isResultSuccess()) {
                final LogicTuple res = op.getLogicTupleResult();
                if (res.getArg(2).getName().equalsIgnoreCase("ok")) {
                    this.log("Agent class of role " + roleName + " changed to "
                            + agentClass);
                } else if (res.getArg(2).getName().equalsIgnoreCase("failed")) {
                    final String failReason = res.getArg(2).getArg(0)
                            .toString();
                    this.log("Agent class of role " + roleName
                            + " NOT changed because " + failReason);
                }
            }
        } catch (final InvalidVarNameException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void setRolePolicy(final String roleName, final String policyName)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException {
        LogicTuple setPolicyTuple;
        try {
            setPolicyTuple = new LogicTuple("set_role_policy", new Value(
                    roleName), new Value(policyName), new Var("Result"));
            final TucsonOperation op = this.inp(this.tid, setPolicyTuple,
                    (Long) null);
            if (op.isResultSuccess()) {
                final LogicTuple res = op.getLogicTupleResult();
                if (res.getArg(2).getName().equalsIgnoreCase("ok")) {
                    this.log("Policy of role " + roleName + " changed to "
                            + policyName);
                } else if (res.getArg(2).getName().equalsIgnoreCase("failed")) {
                    final String failReason = res.getArg(2).getArg(0)
                            .toString();
                    this.log("Policy of role " + roleName
                            + " NOT changed because " + failReason);
                }
            }
        } catch (final InvalidVarNameException e) {
            e.printStackTrace();
        }
    }

    private void addAuthorisedAgent(final AuthorisedAgent agent, final Long l)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException {
        final LogicTuple authTuple = TucsonAuthorisedAgent.asLogicTuple(agent);
        final TucsonOperation op = this.out(this.tid, authTuple, l);
        if (op.isResultSuccess()) {
            final LogicTuple res = op.getLogicTupleResult();
            this.log("Authorising agent: " + res);
        } else {
            this.log("Error while authorising agent!");
        }
    }

    private void addPolicy(final Policy policy, final Long l)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException {

        String permissions = "[";
        for (final Permission perm : policy.getPermissions()) {
            permissions += perm.getPermissionName() + ",";
        }
        permissions = permissions.substring(0, permissions.length() - 1);
        permissions += "]";
        LogicTuple policyTuple = null;
        TucsonOperation op = null;
        try {
            policyTuple = new LogicTuple("policy", new Value(
                    policy.getPolicyName()), TupleArgument.parse(permissions));
            op = this.out(this.tid, policyTuple, l);
        } catch (final InvalidTupleArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (op != null && op.isResultSuccess()) {
            final LogicTuple res = op.getLogicTupleResult();
            this.log("Added policy: " + res);
        } else {
            this.log("Error while adding policy!");
        }

    }

    private void addRole(final Role role, final Long l)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException {

        final LogicTuple roleTuple = new LogicTuple("role", new Value(
                role.getRoleName()), new Value(role.getDescription()),
                new Value(role.getAgentClass()));

        final TucsonOperation op = this.out(this.tid, roleTuple, l);
        if (op.isResultSuccess()) {
            final LogicTuple res = op.getLogicTupleResult();
            this.log("Added role: " + res);
        } else {
            this.log("Error while adding role!");
        }
        this.addRolePolicy(role.getPolicy(), role.getRoleName(), l);

    }

    private void addRolePolicy(final Policy policy, final String roleName,
            final Long l) throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException {

        final LogicTuple policyTuple = new LogicTuple("role_policy", new Value(
                roleName), new Value(policy.getPolicyName()));
        final TucsonOperation op = this.out(this.tid, policyTuple, l);
        if (op.isResultSuccess()) {
            final LogicTuple res = op.getLogicTupleResult();
            this.log("Added role-policy association: " + res);
        } else {
            this.log("Error while adding role-policy association!");
        }

    }

    private TupleCentreIdentifier getTid(final String n, final int p)
            throws TucsonInvalidTupleCentreIdException {

        String tmpNode;
        int tmpPort;
        if (n != null && !n.equals("")) {
            tmpNode = n;
            tmpPort = p;
        } else {
            tmpNode = this.node;
            tmpPort = this.port;
        }
        
        if (!tmpNode.equals("localhost")) {
        	if (!tmpNode.equals("127.0.0.1")) {
       /* if (tmpNode.equalsIgnoreCase("localhost")
                || tmpNode.equalsIgnoreCase("127.0.0.1")
                || tmpNode.equalsIgnoreCase("'127.0.0.1'")) { */
	            InetAddress localhost;
	            try {
	                localhost = InetAddress.getLocalHost();
	                final String localNodeAddress = localhost.getHostAddress();
	                tmpNode = localNodeAddress;
	            } catch (final UnknownHostException e) {
	                return new TucsonTupleCentreIdDefault(TC_ORG, "'"
	                        + tmpNode + "'", "" + tmpPort);
	            } 
	        }
        }
        return new TucsonTupleCentreIdDefault(TC_ORG, "'" + tmpNode
                + "'", "" + tmpPort);

    }

    private void installRBACSupport()
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException,
            OperationNotAllowedException {

        if (!this.isAdminAuth) {
            throw new OperationNotAllowedException();
        }
        this.inp(this.tid, new LogicTuple("install_rbac"), (Long) null);
        // TODO: Serve admin autorizzato? Se non fosse autorizzato non potrebbe
        // farlo!
        final LogicTuple adminAuthorised = new LogicTuple("authorised_agent",
                new Value(this.aid.toString()));
        this.out(this.tid, adminAuthorised, (Long) null);

    }

    private void playAdminRole() {
        try {
            final LogicTuple template = new LogicTuple("admin_login_request",
                    new Value(this.getUsername() + ":"
                            + TucsonACCTool.encrypt(this.getPassword())),
                    new Var("Result"));
            final TucsonOperation op = this.inp(this.tid, template,
                    (Long) null);
            if (op.isResultSuccess()) {
                final LogicTuple res = op.getLogicTupleResult();
                if (res != null
                        && res.getArg(1).getName().equalsIgnoreCase("ok")) {
                    this.isAdminAuth = true;
                }
            }
        } catch (final InvalidVarNameException e) {
            e.printStackTrace();
        } catch (final TucsonOperationNotPossibleException e) {
            e.printStackTrace();
        } catch (final UnreachableNodeException e) {
            e.printStackTrace();
        } catch (final OperationTimeOutException e) {
            e.printStackTrace();
        }
    }

    /*
     * (non-Javadoc)
     * @see alice.tuplecentre.tucson.service.ACCProxyAgentSide#log(java.lang.String)
     */
    @Override
    protected void log(final String msg) {
        System.out.println("[AdminACCProxy]: " + msg);
    }

}
