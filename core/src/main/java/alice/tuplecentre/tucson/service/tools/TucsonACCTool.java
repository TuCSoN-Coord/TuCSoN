package alice.tuplecentre.tucson.service.tools;

import java.lang.invoke.MethodHandles;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import alice.tuple.logic.LogicTuple;
import alice.tuple.logic.TupleArgument;
import alice.tuple.logic.exceptions.InvalidVarNameException;
import alice.tuplecentre.api.TupleCentreIdentifier;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;
import alice.tuplecentre.tucson.api.TucsonOperation;
import alice.tuplecentre.tucson.api.acc.EnhancedACC;
import alice.tuplecentre.tucson.api.exceptions.AgentNotAllowedException;
import alice.tuplecentre.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tuplecentre.tucson.api.exceptions.UnreachableNodeException;
import alice.tuplecentre.tucson.rbac.Policy;
import alice.tuplecentre.tucson.rbac.Role;
import alice.tuplecentre.tucson.rbac.TucsonPolicy;
import alice.tuplecentre.tucson.rbac.TucsonRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility methods to manage RBAC-related facilities.
 *
 * @author Emanuele Buccelli
 * @author (contributor) Stefano Mariani (mailto: s.mariani@unibo.it)
 */
public final class TucsonACCTool {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * Activates a coordination context for a given agent.
     *
     * @param agentAid   the Identifier copyOf the agent
     * @param agentUUID  the UUID assigned to the agent
     * @param agentClass the RBAC agent class copyOf the agent
     * @param tid        the tuple centre bookeeping activations
     * @param acc        the ACC used to perform the activation
     * @return {@code true} or {@code false} depending on whether activation is
     * successful or not
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean activateContext(final String agentAid,
                                          final UUID agentUUID, final String agentClass,
                                          final TupleCentreIdentifier tid, final EnhancedACC acc) {
        try {
            final LogicTuple template = LogicTuple.of("context_request",
                    TupleArgument.of(agentAid), TupleArgument.var("Result"), TupleArgument.of(
                            agentClass), TupleArgument.of(agentUUID.toString()));
            final TucsonOperation op = acc.inp(tid, template, (Long) null);
            if (op.isResultSuccess()) {
                final LogicTuple res = op.getLogicTupleResult();
                if (res != null
                        && res.getArg(1).getName().equalsIgnoreCase("ok")) {
                    return true;
                } else if (res != null
                        && res.getArg(1).getName().equalsIgnoreCase("failed")
                        && res.getArg(1).getArg(0).toString()
                        .equalsIgnoreCase("agent_already_present")) {
                    return true;
                }
            }
        } catch (final InvalidVarNameException | OperationTimeOutException | UnreachableNodeException | TucsonOperationNotPossibleException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return false;
    }

    /**
     * Activates a given role for the given agent.
     *
     * @param agentAid   the Identifier copyOf the agent
     * @param accUUID    the UUID assigned to the agent
     * @param agentClass the RBAC agent class copyOf the agent
     * @param roleName   the name copyOf the role to activate
     * @param tid        the tuple centre bookeeping activations
     * @param acc        the ACC used to perform the activation
     * @return the RBAC role activated
     * @throws AgentNotAllowedException if the agent is not allowed to activate the given role
     */
    public static Role activateRole(final String agentAid, final UUID accUUID,
                                    final String agentClass, final String roleName,
                                    final TupleCentreIdentifier tid, final EnhancedACC acc)
            throws AgentNotAllowedException {
        if (!TucsonACCTool.activateContext(agentAid, accUUID, agentClass, tid,
                acc)) {
            return null;
        }
        Role newRole = null;
        try {
            final LogicTuple template = LogicTuple.of(
                    "role_activation_request", TupleArgument.of(agentAid),
                    TupleArgument.of(accUUID.toString()), TupleArgument.of(roleName),
                    TupleArgument.var("Result"));
            final TucsonOperation op = acc.inp(tid, template, (Long) null);
            if (op.isResultSuccess()) {
                final LogicTuple res = op.getLogicTupleResult();
                if (res != null
                        && res.getArg(3).getName().equalsIgnoreCase("ok")) {
                    final String policyName = res.getArg(3).getArg(0)
                            .toString();
                    final TupleArgument[] permissionsList = res.getArg(3)
                            .getArg(1).toArray();
                    final Policy newPolicy = TucsonPolicy.createPolicy(
                            policyName, permissionsList);
                    newRole = new TucsonRole(roleName);
                    newRole.setPolicy(newPolicy);
                } else if (res != null
                        && res.getArg(3).getName().equalsIgnoreCase("failed")) {
                    throw new AgentNotAllowedException();
                }
            }
        } catch (final InvalidVarNameException | OperationTimeOutException | UnreachableNodeException | TucsonOperationNotPossibleException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return newRole;
    }

    /**
     * Activates a RBAC role given its policy for a given agent.
     *
     * @param agentAid   the Identifier copyOf the agent
     * @param accUUID    the UUID assigned to the agent
     * @param agentClass the RBAC agent class copyOf the agent
     * @param policy     the policy whose role should be activated
     * @param tid        the tuple centre bookeeping activations
     * @param acc        the ACC used to perform the activation
     * @return the RBAC role activated
     */
    public static Role activateRoleWithPolicy(final String agentAid,
                                              final UUID accUUID, final String agentClass, final Policy policy,
                                              final TupleCentreIdentifier tid, final EnhancedACC acc) {
        if (!TucsonACCTool.activateContext(agentAid, accUUID, agentClass, tid,
                acc)) {
            return null;
        }
        Role newRole = null;
        try {
            final LogicTuple rolePolicyTemplate = LogicTuple.of(
                    "policy_role_request", TupleArgument.of(policy.getPolicyName()),
                    TupleArgument.var("Result"));
            TucsonOperation op = acc.inp(tid, rolePolicyTemplate, (Long) null);
            if (op.isResultSuccess()) {
                LogicTuple res = op.getLogicTupleResult();
                final String roleName = res.getArg(1).toString();
                final LogicTuple template = LogicTuple.of(
                        "role_activation_request", TupleArgument.of(
                                agentAid), TupleArgument.of(
                                accUUID.toString()), TupleArgument.of(roleName),
                        TupleArgument.var("Result"));
                op = acc.inp(tid, template, (Long) null);
                if (op.isResultSuccess()) {
                    res = op.getLogicTupleResult();
                    if (res != null
                            && res.getArg(3).getName().equalsIgnoreCase("ok")) {
                        newRole = new TucsonRole(roleName);
                        newRole.setPolicy(policy);
                        newRole.setAgentClass(agentClass);
                    }
                }
            }
        } catch (final InvalidVarNameException | OperationTimeOutException | UnreachableNodeException | TucsonOperationNotPossibleException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return newRole;
    }

    /**
     * Encrypts the given String using standard Java security library and
     * cryptography algorithms, such as SHA-256.
     *
     * @param password the String to encrypt
     * @return the encrypted String
     */
    public static String encrypt(final String password) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (final NoSuchAlgorithmException e) {
            /*
             * Should not happen
             */
            LOGGER.error(e.getMessage(), e);
        }
        Objects.requireNonNull(md).update(password.getBytes());
        final byte[] byteData = md.digest();
        final StringBuilder sb = new StringBuilder();
        for (final byte element : byteData) {
            sb.append(Integer.toString((element & 0xff) + 0x100, 16).substring(
                    1));
        }
        return sb.toString();
    }

    /**
     * Gets the list copyOf policies available for the given RBAC agent class.
     *
     * @param agentClass the RBAC agent class whose associated policies should be
     *                   retrieved
     * @param tid        the tuple centre bookeeping associations
     * @param acc        the ACC used to perform the query
     * @return the list copyOf policies available for the given RBAC agent class
     */
    public static List<Policy> getPoliciesList(final String agentClass,
                                               final TupleCentreIdentifier tid, final EnhancedACC acc) {
        final List<Policy> policies = new ArrayList<>();
        try {
            final LogicTuple policyListTuple = LogicTuple.of(
                    "policies_list_request", TupleArgument.of(agentClass), TupleArgument.var(
                            "Result"));
            final TucsonOperation op = acc.inp(tid, policyListTuple,
                    (Long) null);
            if (op.isResultSuccess()) {
                final LogicTuple res = op.getLogicTupleResult();
                if (res.getArg(1).getName().equalsIgnoreCase("ok")) {
                    final TupleArgument[] policiesList = res.getArg(1)
                            .getArg(0).toArray();
                    for (final TupleArgument term : policiesList) {
                        final TupleArgument[] permissionsTuples = term
                                .getArg(1).toArray();
                        final Policy newPolicy = TucsonPolicy.createPolicy(term
                                .getArg(0).toString(), permissionsTuples);
                        policies.add(newPolicy);
                    }
                }
            }
        } catch (final InvalidVarNameException | OperationTimeOutException | UnreachableNodeException | TucsonOperationNotPossibleException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return policies;
    }

    private TucsonACCTool() {
        /*
         * To avoid instantiability
         */
    }

}
