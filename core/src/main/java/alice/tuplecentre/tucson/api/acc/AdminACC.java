package alice.tuplecentre.tucson.api.acc;

import alice.tuplecentre.api.exceptions.OperationTimeOutException;
import alice.tuplecentre.respect.api.exceptions.OperationNotAllowedException;
import alice.tuplecentre.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tuplecentre.tucson.api.exceptions.UnreachableNodeException;
import alice.tuplecentre.tucson.rbac.*;

/**
 * Agent Coordination Context enabling system administrators to manage the RBAC
 * (Role-Based Access Control) structure installed in a TuCSoN node.
 *
 * @author Emanuele Buccelli
 * @author (contributor) Stefano Mariani (mailto: s.mariani@unibo.it)
 */
public interface AdminACC extends EnhancedACC {

    /**
     * Adds an agent to the set of authorised agents, that is, those already
     * recognised by TuCSoN according to RBAC (Role-Based Access Control), in the default TuCSoN node
     * (installed on {@code localhost:20504}).
     *
     * @param agent the authorised agent
     * @throws TucsonOperationNotPossibleException if the requested TuCSoN operation cannot be performed
     * @throws UnreachableNodeException            if the TuCSoN node target of this operation is not
     *                                             network-reachable
     * @throws OperationTimeOutException           if the operation timeout expired prior to operation
     *                                             completion
     */
    void add(final AuthorisedAgent agent) throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException;

    /**
     * Adds a permission, that is, a grant stating what is allowed to do in the
     * organisation, to the given RBAC policy already installed in default
     * TuCSoN node (installed on {@code localhost:20504}).
     *
     * @param permission the permission to add
     * @param policyName the name of the existing policy to extend
     * @throws TucsonOperationNotPossibleException if the requested TuCSoN operation cannot be performed
     * @throws UnreachableNodeException            if the TuCSoN node target of this operation is not
     *                                             network-reachable
     * @throws OperationTimeOutException           if the operation timeout expired prior to operation
     *                                             completion
     */
    void add(final Permission permission, final String policyName) //TODO oppure anche un oggetto Policy???
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException;

    /**
     * Adds a policy, that is, a set of permissions, to the RBAC (Role-Based Access Control) structure
     * installed in the default TuCSoN node.
     *
     * @param policy the policy to add
     * @throws TucsonOperationNotPossibleException if the requested TuCSoN operation cannot be performed
     * @throws UnreachableNodeException            if the TuCSoN node target of this operation is not
     *                                             network-reachable
     * @throws OperationTimeOutException           if the operation timeout expired prior to operation
     *                                             completion
     */
    void add(final Policy policy) throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException;

    /**
     * Adds an RBAC (Role-Based Access Control) structure, that is, the set of roles, policies, permissions,
     * and their relationships, to the default TuCSoN node (installed on
     * {@code localhost:20504}).
     *
     * @param rbac the RBAC structure to add
     * @throws TucsonOperationNotPossibleException if the requested TuCSoN operation cannot be performed
     * @throws UnreachableNodeException            if the TuCSoN node target of this operation is not
     *                                             network-reachable
     * @throws OperationTimeOutException           if the operation timeout expired prior to operation
     *                                             completion
     * @throws OperationNotAllowedException        if the requested TuCSoN operation is not allowed to the
     *                                             requesting agent
     */
    void install(final RBACStructure rbac)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException,
            OperationNotAllowedException;

    /**
     * Adds an RBAC (Role-Based Access Control) structure, that is, the set of roles, policies, permissions,
     * and their relationships, to the given TuCSoN node (only local nodes are
     * supported atm, that is, this installed on {@code localhost}).
     *
     * @param rbac    the RBAC structure to add
     * @param timeout the maximum waiting time for the operation to complete
     * @param node    the IP address where the target TuCSoN node is installed (only
     *                local nodes are supported atm, that is, on {@code localhost})
     * @param port    the TCP port where the target TuCSoN node is installed
     * @throws TucsonOperationNotPossibleException if the requested TuCSoN operation cannot be performed
     * @throws UnreachableNodeException            if the TuCSoN node target of this operation is not
     *                                             network-reachable
     * @throws OperationTimeOutException           if the operation timeout expired prior to operation
     *                                             completion
     * @throws OperationNotAllowedException        if the requested TuCSoN operation is not allowed to the
     *                                             requesting agent
     */
    void install(final RBACStructure rbac, final Long timeout, final String node, int port)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException,
            OperationNotAllowedException;

    /**
     * Adds a role, that is, a position in the organisation associated to an
     * RBAC policy, to the RBAC (Role-Based Access Control) structure installed in default TuCSoN node
     * (installed on {@code localhost:20504}).
     *
     * @param role the role to add
     * @throws TucsonOperationNotPossibleException if the requested TuCSoN operation cannot be performed
     * @throws UnreachableNodeException            if the TuCSoN node target of this operation is not
     *                                             network-reachable
     * @throws OperationTimeOutException           if the operation timeout expired prior to operation
     *                                             completion
     */
    void add(final Role role) throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException;

    /**
     * Removes an agent from the list of authorised agents installed in default
     * TuCSoN node (installed on {@code localhost:20504}).
     *
     * @param agentName the name of the agent to remove
     */
    void remove(final String agentName); //TODO sarebbe meglio passare un oggetto che identifica un agente (visto che li abbiamo)???

    /**
     * Removes a policy from the RBAC (Role-Based Access Control) structure installed in default TuCSoN node
     * (installed on {@code localhost:20504}).
     *
     * @param policyName the name of the policy to remove
     * @throws TucsonOperationNotPossibleException if the requested TuCSoN operation cannot be performed
     * @throws UnreachableNodeException            if the TuCSoN node target of this operation is not
     *                                             network-reachable
     * @throws OperationTimeOutException           if the operation timeout expired prior to operation
     *                                             completion
     */
    void removePolicy(final String policyName) //TODO passare un oggetto Policy???
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException;

    /**
     * Removes the RBAC (Role-Based Access Control) structure installed in default TuCSoN node (installed on
     * {@code localhost:20504}).
     *
     * @throws TucsonOperationNotPossibleException if the requested TuCSoN operation cannot be performed
     * @throws UnreachableNodeException            if the TuCSoN node target of this operation is not
     *                                             network-reachable
     * @throws OperationTimeOutException           if the operation timeout expired prior to operation
     *                                             completion
     * @throws OperationNotAllowedException        if the requested TuCSoN operation is not allowed to the
     *                                             requesting agent
     */
    void removeRBAC() throws OperationNotAllowedException,
            TucsonOperationNotPossibleException, UnreachableNodeException,
            OperationTimeOutException;

    /**
     * Removes the RBAC (Role-Based Access Control) structure installed in the given TuCSoN node (only local
     * nodes are supported atm, that is, this installed on {@code localhost}).
     *
     * @param timeout the maximum waiting time for the operation to complete
     * @param node    the IP address where the target TuCSoN node is installed (only
     *                local nodes are supported atm, that is, on {@code localhost})
     * @param port    the TCP port where the target TuCSoN node is installed
     * @throws OperationNotAllowedException        if the requested TuCSoN operation is not allowed to the
     *                                             requesting agent
     * @throws TucsonOperationNotPossibleException if the requested TuCSoN operation cannot be performed
     * @throws UnreachableNodeException            if the TuCSoN node target of this operation is not
     *                                             network-reachable
     * @throws OperationTimeOutException           if the operation timeout expired prior to operation
     *                                             completion
     */
    void removeRBAC(final Long timeout, final String node, final int port)
            throws OperationNotAllowedException,
            TucsonOperationNotPossibleException, UnreachableNodeException,
            OperationTimeOutException;

    /**
     * Removes a role from the RBAC (Role-Based Access Control) structure installed in default TuCSoN node
     * (installed on {@code localhost:20504}).
     *
     * @param roleName the name of the role to remove
     * @throws TucsonOperationNotPossibleException if the requested TuCSoN operation cannot be performed
     * @throws UnreachableNodeException            if the TuCSoN node target of this operation is not
     *                                             network-reachable
     * @throws OperationTimeOutException           if the operation timeout expired prior to operation
     *                                             completion
     */
    void removeRole(final String roleName) //TODO passare un oggetto Role???
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException;

    /**
     * Sets the basic agent class, that is, the cohort of agents representing
     * un-authenticated agents (if allowed by the RBAC (Role-Based Access Control) structure currently
     * installed) from the RBAC structure installed in default TuCSoN node
     * (installed on {@code localhost:20504}).
     *
     * @param newBasicAgentClass the new basic agent class
     * @throws TucsonOperationNotPossibleException if the requested TuCSoN operation cannot be performed
     * @throws UnreachableNodeException            if the TuCSoN node target of this operation is not
     *                                             network-reachable
     * @throws OperationTimeOutException           if the operation timeout expired prior to operation
     *                                             completion
     */
    void setBasicAgentClass(final String newBasicAgentClass)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException;

    /**
     * Sets the agent class associated to a role in the RBAC (Role-Based Access Control) structure installed
     * in default TuCSoN node (installed on {@code localhost:20504}).
     *
     * @param roleName   the name of the role whose class association should be set
     * @param agentClass the agent class to associate to the given role
     * @throws TucsonOperationNotPossibleException if the requested TuCSoN operation cannot be performed
     * @throws UnreachableNodeException            if the TuCSoN node target of this operation is not
     *                                             network-reachable
     * @throws OperationTimeOutException           if the operation timeout expired prior to operation
     *                                             completion
     */
    void setRoleAgentClass(final String roleName, final String agentClass) // TODO passare un oggetto Role???
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException;

    /**
     * Sets the association between a role and a policy in the RBAC structure
     * installed in default TuCSoN node (installed on {@code localhost:20504}).
     *
     * @param roleName   the name of the role
     * @param policyName the name of the policy
     * @throws TucsonOperationNotPossibleException if the requested TuCSoN operation cannot be performed
     * @throws UnreachableNodeException            if the TuCSoN node target of this operation is not
     *                                             network-reachable
     * @throws OperationTimeOutException           if the operation timeout expired prior to operation
     *                                             completion
     */
    void setRolePolicy(final String roleName, final String policyName) //TODO passare gli oggetti relativi non sarebbe meglio???
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException;
}
