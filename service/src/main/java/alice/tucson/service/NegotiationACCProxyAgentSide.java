package alice.tucson.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import alice.tuple.logic.LogicTuple;
import alice.tuple.logic.LogicTupleDefault;
import alice.tuple.logic.TupleArgument;
import alice.tuple.logic.Value;
import alice.tuple.logic.Var;
import alice.tuple.logic.exceptions.InvalidVarNameException;
import alice.tucson.api.TucsonOperation;
import alice.tucson.api.acc.EnhancedACC;
import alice.tucson.api.acc.NegotiationACC;
import alice.tucson.api.TucsonTupleCentreId;
import alice.tucson.api.exceptions.*;
import alice.tucson.rbac.Policy;
import alice.tucson.rbac.Role;
import alice.tucson.rbac.TucsonPolicy;
import alice.tucson.rbac.TucsonRole;
import alice.tucson.service.tools.TucsonACCTool;
import alice.tuplecentre.api.TupleCentreId;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;

/**
 * Class implementing the negotiation ACC.
 *
 * @author Emanuele Buccelli
 * @author (contributor) Stefano Mariani (mailto: s.mariani@unibo.it)
 *
 */
public class NegotiationACCProxyAgentSide implements NegotiationACC {

    private static final int DEF_PORT = 20504;
    private static final String TC_AGENT = "negotAgent";
    private static final String TC_ORG = "'$ORG'";

    private static Policy chooseRole(final List<Policy> policies,
            final List<String> permissionsId) {
        Policy bestPolicy = null;
        int bestPermissionsArity = Integer.MAX_VALUE;
        for (final Policy pol : policies) {
            if (pol.hasPermissions(permissionsId)) {
                if (pol.getPermissions().size() < bestPermissionsArity) {
                    bestPermissionsArity = pol.getPermissions().size();
                    bestPolicy = pol;
                }
            }
        }
        return bestPolicy;
    }

    private final Object agentAid;
    private String agentClass;
    private final EnhancedACC internalACC;
    private final String node;
    private final int port;
    private final TupleCentreId tid;

    /**
     * Builds a Negotiation ACC given the associated agent ID or name
     *
     * @param aid
     *            the associated agent ID or name (String)
     * @throws TucsonInvalidAgentIdException
     *             if the given agent ID is NOT valid
     * @throws TucsonInvalidTupleCentreIdException
     *             if the given tuple centre ID is NOT valid
     */
    public NegotiationACCProxyAgentSide(final Object aid)
            throws TucsonInvalidAgentIdException,
            TucsonInvalidTupleCentreIdException {
        this(aid, "localhost", NegotiationACCProxyAgentSide.DEF_PORT);
    }

    /**
     * Builds a Negotiation ACC given the associated agent ID or name, the IP
     * address of the TuCSoN node the agent is willing to interact with, and the
     * TCP port also.
     *
     * @param aid
     *            the associated agent ID or name (String)
     * @param n
     *            the IP address of the target TuCSoN node
     * @param p
     *            the TCP port of the target TuCSoN node
     * @throws TucsonInvalidAgentIdException
     *             if the given agent ID is NOT valid
     * @throws TucsonInvalidTupleCentreIdException
     *             if the given tuple centre ID is NOT valid
     */
    public NegotiationACCProxyAgentSide(final Object aid, final String n,
            final int p) throws TucsonInvalidAgentIdException,
            TucsonInvalidTupleCentreIdException {
        this.internalACC = new ACCProxyAgentSide(
                NegotiationACCProxyAgentSide.TC_AGENT, n, p);
        this.node = n;
        this.port = p;
        this.agentAid = aid;
        this.tid = new TucsonTupleCentreId(NegotiationACCProxyAgentSide.TC_ORG,
                "'" + n + "'", "" + p);
        this.setBasicAgentClass();
    }

    // TODO: Lista dei ruoli!!!
    @Override
    public List<Role> getListOfPlayableRoles()
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException {
        TucsonOperation op = null;
        try {
            op = this.internalACC.inp(this.tid, new LogicTupleDefault(
                    "roles_list_request", new Value(this.agentClass), new Var(
                            "Result")), (Long) null);
        } catch (final InvalidVarNameException e) {
            /*
             * Cannot happen
             */
            e.printStackTrace();
        }
        final List<Role> roles = new ArrayList<Role>();
        if (op != null && op.isResultSuccess()) {
            final LogicTuple res = op.getLogicTupleResult();
            if (res.getArg(1).getName().equalsIgnoreCase("ok")) {
                final TupleArgument[] rolesList = res.getArg(1).getArg(0)
                        .toArray();
                for (final TupleArgument term : rolesList) {
                    final String roleName = term.getArg(0).toString();
                    final String roleDescription = term.getArg(1).toString();
                    final String policyName = term.getArg(2).toString();
                    final TupleArgument[] permissionsTuples = term.getArg(3)
                            .toArray();
                    final Policy newPolicy = TucsonPolicy.createPolicy(
                            policyName, permissionsTuples);
                    final Role newRole = new TucsonRole(roleName,
                            this.agentClass);
                    newRole.setDescription(roleDescription);
                    newRole.setPolicy(newPolicy);
                    roles.add(newRole);
                }
            }
        }
        return roles;
    }

    @Override
    public boolean login(final String username, final String password)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException {
        LogicTuple loginTuple = null;
        TucsonOperation op = null;
        try {
            loginTuple = new LogicTupleDefault("login_request", new Value(username
                    + ":" + TucsonACCTool.encrypt(password)), new Var("Result"));
            op = this.internalACC.inp(this.tid, loginTuple, (Long) null);
        } catch (final InvalidVarNameException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (op != null && op.isResultSuccess()) {
            final TupleArgument reply = op.getLogicTupleResult().getArg(1);
            this.setAgentClass(reply.getArg(0).toString());
            if (reply.getName().equals("ok")) {
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public EnhancedACC playDefaultRole()
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException,
            TucsonInvalidAgentIdException {
        if (!this.isRBACInstalled(this.tid)) {
            return new ACCProxyAgentSide(this.agentAid, this.node, this.port);
        }
        return null;
    }

    @Override
    public EnhancedACC playRole(final String roleName)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException,
            TucsonInvalidAgentIdException, AgentNotAllowedException {
        return this.playRole(roleName, null);
    }

    @Override
    public EnhancedACC playRole(final String roleName, final Long timeout)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException,
            TucsonInvalidAgentIdException, AgentNotAllowedException {

        if (!this.isRBACInstalled(this.tid)) {
            return new ACCProxyAgentSide(this.agentAid, this.node, this.port);
        }
        final UUID agentUUID = UUID.randomUUID();
        final Role newRole = TucsonACCTool.activateRole(
                this.agentAid.toString(), agentUUID, this.getAgentClass(),
                roleName, this.tid, this.internalACC);
        if (newRole == null) {
            return null;
        }
        return new RBACACCProxyAgentSide(this.agentAid.toString(), this.node,
                this.port, newRole, agentUUID);

    }

    @Override
    public EnhancedACC playRoleWithPermissions(final List<String> permissionsId)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException,
            TucsonInvalidAgentIdException, AgentNotAllowedException {
        return this.playRoleWithPermissions(permissionsId, null);
    }

    @Override
    public synchronized EnhancedACC playRoleWithPermissions(
            final List<String> permissionsId, final Long l)
                    throws TucsonOperationNotPossibleException,
                    UnreachableNodeException, OperationTimeOutException,
                    TucsonInvalidAgentIdException, AgentNotAllowedException {

        if (!this.isRBACInstalled(this.tid)) {
            return new ACCProxyAgentSide(this.agentAid, this.node, this.port);
        }
        final List<Policy> policies = TucsonACCTool.getPoliciesList(
                this.agentClass, this.tid, this.internalACC);
        final Policy policy = NegotiationACCProxyAgentSide.chooseRole(policies,
                permissionsId);
        if (policy == null) {
            throw new AgentNotAllowedException();
        }
        final UUID agentUUID = UUID.randomUUID();
        final Role newRole = TucsonACCTool.activateRoleWithPolicy(
                this.agentAid.toString(), agentUUID, this.getAgentClass(),
                policy, this.tid, this.internalACC);
        if (newRole == null) {
            throw new AgentNotAllowedException();
        }
        return new RBACACCProxyAgentSide(this.agentAid, this.node, this.port,
                newRole, agentUUID);

    }

    private String getAgentClass() {
        return this.agentClass;
    }

    private boolean isRBACInstalled(final TupleCentreId tcid)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException {
        LogicTuple rbacInstalled = null;
        try {
            rbacInstalled = new LogicTupleDefault("is_rbac_installed", new Var("Result"));
        } catch (final InvalidVarNameException e) {
            e.printStackTrace();
        }
        final TucsonOperation op = this.internalACC.rd(tcid, rbacInstalled,
                (Long) null);
        if (op.isResultSuccess()) {
            final LogicTuple res = op.getLogicTupleResult();
            if (res.getArg(0).toString().equals("yes")) {
                return true;
            }
        }
        return false;
    }

    private void setAgentClass(final String agClass) {
        this.agentClass = agClass;
    }

    private void setBasicAgentClass() {
        try {
            final LogicTuple baseClassTuple = new LogicTupleDefault(
                    "get_basic_agent_class", new Var("Response"));
            final TucsonOperation op = this.internalACC.inp(this.tid,
                    baseClassTuple, (Long) null);
            if (op.isResultSuccess()) {
                final String baseClass = op.getLogicTupleResult().getArg(0)
                        .toString();
                this.setAgentClass(baseClass);
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

    protected void log(final String msg) {
        System.out.println("[NegotiationACCProxy]: " + msg);
    }
}
