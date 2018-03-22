package alice.tuplecentre.tucson.service;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import alice.tuple.logic.LogicTuple;
import alice.tuple.logic.LogicTuples;
import alice.tuple.logic.TupleArgument;
import alice.tuple.logic.TupleArguments;
import alice.tuple.logic.exceptions.InvalidVarNameException;
import alice.tuplecentre.api.TupleCentreIdentifier;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;
import alice.tuplecentre.tucson.api.TucsonOperation;
import alice.tuplecentre.tucson.api.TucsonTupleCentreIdDefault;
import alice.tuplecentre.tucson.api.acc.EnhancedACC;
import alice.tuplecentre.tucson.api.acc.NegotiationACC;
import alice.tuplecentre.tucson.api.exceptions.AgentNotAllowedException;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidTupleCentreIdException;
import alice.tuplecentre.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tuplecentre.tucson.api.exceptions.UnreachableNodeException;
import alice.tuplecentre.tucson.rbac.Policy;
import alice.tuplecentre.tucson.rbac.Role;
import alice.tuplecentre.tucson.rbac.TucsonPolicy;
import alice.tuplecentre.tucson.rbac.TucsonRole;
import alice.tuplecentre.tucson.service.tools.TucsonACCTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class implementing the negotiation ACC.
 *
 * @author Emanuele Buccelli
 * @author (contributor) Stefano Mariani (mailto: s.mariani@unibo.it)
 */
public class NegotiationACCProxyAgentSide implements NegotiationACC {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

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
    private final TupleCentreIdentifier tid;

    /**
     * Builds a Negotiation ACC given the associated agent Identifier or name
     *
     * @param aid the associated agent Identifier or name (String)
     * @throws TucsonInvalidAgentIdException       if the given agent Identifier is NOT valid
     * @throws TucsonInvalidTupleCentreIdException if the given tuple centre Identifier is NOT valid
     */
    public NegotiationACCProxyAgentSide(final Object aid)
            throws TucsonInvalidAgentIdException,
            TucsonInvalidTupleCentreIdException {
        this(aid, "localhost", TucsonInfo.getDefaultPortNumber());
    }

    /**
     * Builds a Negotiation ACC given the associated agent Identifier or name, the IP
     * address of the TuCSoN node the agent is willing to interact with, and the
     * TCP port also.
     *
     * @param aid the associated agent Identifier or name (String)
     * @param n   the IP address of the target TuCSoN node
     * @param p   the TCP port of the target TuCSoN node
     * @throws TucsonInvalidAgentIdException       if the given agent Identifier is NOT valid
     * @throws TucsonInvalidTupleCentreIdException if the given tuple centre Identifier is NOT valid
     */
    public NegotiationACCProxyAgentSide(final Object aid, final String n,
                                        final int p) throws TucsonInvalidAgentIdException,
            TucsonInvalidTupleCentreIdException {
        this.internalACC = new ACCProxyAgentSide(
                NegotiationACCProxyAgentSide.TC_AGENT, n, p);
        this.node = n;
        this.port = p;
        this.agentAid = aid;
        this.tid = new TucsonTupleCentreIdDefault(NegotiationACCProxyAgentSide.TC_ORG,
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
            op = this.internalACC.inp(this.tid, LogicTuples.newInstance(
                    "roles_list_request", TupleArguments.newValueArgument(this.agentClass), TupleArguments.newVarArgument(
                            "Result")), (Long) null);
        } catch (final InvalidVarNameException e) {
            /*
             * Cannot happen
             */
            LOGGER.error(e.getMessage(), e);
        }
        final List<Role> roles = new ArrayList<>();
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
        LogicTuple loginTuple;
        TucsonOperation op = null;
        try {
            loginTuple = LogicTuples.newInstance("login_request", TupleArguments.newValueArgument(username
                    + ":" + TucsonACCTool.encrypt(password)), TupleArguments.newVarArgument("Result"));
            op = this.internalACC.inp(this.tid, loginTuple, (Long) null);
        } catch (final InvalidVarNameException e) {
            // TODO Auto-generated catch block
            LOGGER.error(e.getMessage(), e);
        }
        if (op != null && op.isResultSuccess()) {
            final TupleArgument reply = op.getLogicTupleResult().getArg(1);
            this.setAgentClass(reply.getArg(0).toString());
            return reply.getName().equals("ok");
        }
        return false;
    }

    @Override
    public EnhancedACC playDefaultRole()
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException,
            TucsonInvalidAgentIdException {
        if (this.isNotRBACInstalled(this.tid)) {
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

        if (this.isNotRBACInstalled(this.tid)) {
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

        if (this.isNotRBACInstalled(this.tid)) {
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

    private boolean isNotRBACInstalled(final TupleCentreIdentifier tcid)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException {
        LogicTuple rbacInstalled = null;
        try {
            rbacInstalled = LogicTuples.newInstance("is_rbac_installed", TupleArguments.newVarArgument("Result"));
        } catch (final InvalidVarNameException e) {
            LOGGER.error(e.getMessage(), e);
        }
        final TucsonOperation op = this.internalACC.rd(tcid, rbacInstalled,
                (Long) null);
        if (op.isResultSuccess()) {
            final LogicTuple res = op.getLogicTupleResult();
            return !res.getArg(0).toString().equals("yes");
        }
        return true;
    }

    private void setAgentClass(final String agClass) {
        this.agentClass = agClass;
    }

    private void setBasicAgentClass() {
        try {
            final LogicTuple baseClassTuple = LogicTuples.newInstance(
                    "get_basic_agent_class", TupleArguments.newVarArgument("Response"));
            final TucsonOperation op = this.internalACC.inp(this.tid,
                    baseClassTuple, (Long) null);
            if (op.isResultSuccess()) {
                final String baseClass = op.getLogicTupleResult().getArg(0)
                        .toString();
                this.setAgentClass(baseClass);
            }
        } catch (final InvalidVarNameException | OperationTimeOutException | UnreachableNodeException | TucsonOperationNotPossibleException e) {
            LOGGER.error(e.getMessage(), e);
        }

    }

    protected void log(final String msg) {
        LOGGER.info("[NegotiationACCProxy]: " + msg);
    }
}
