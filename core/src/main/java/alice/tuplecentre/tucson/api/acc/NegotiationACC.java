package alice.tuplecentre.tucson.api.acc;

import java.util.List;

import alice.tuplecentre.api.exceptions.OperationTimeOutException;
import alice.tuplecentre.tucson.api.exceptions.AgentNotAllowedException;
import alice.tuplecentre.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tuplecentre.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tuplecentre.tucson.api.exceptions.UnreachableNodeException;
import alice.tuplecentre.tucson.rbac.Role;

/**
 * (Meta) Agent Coordination Context enabling agents to negotiate an actual ACC,
 * configured according to the installed RBAC (Role-Based Access Control) structure
 * (if any) so as to allow playing only those roles admissible for the agent,
 * as well as to (un)play allowed roles.
 *
 * @author Emanuele Buccelli
 * @author (contributor) Stefano Mariani (mailto: s.mariani@unibo.it)
 */
public interface NegotiationACC {

    /**
     * Request the list of the roles playable by the requesting agent, according
     * to RBAC configuration (as installed in the TuCSoN node who released this
     * ACC) and to the requesting agent class.
     *
     * @return the set of playable roles
     * @throws TucsonOperationNotPossibleException if the requested TuCSoN operation cannot be performed
     * @throws UnreachableNodeException            if the TuCSoN node target of this operation is not
     *                                             network-reachable
     * @throws OperationTimeOutException           if the operation timeout expired prior to operation
     *                                             completion
     */
    List<Role> getListOfPlayableRoles() throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException;

    /**
     * Attempts to perform login, so as to receive the associated agent class,
     * according to RBAC configuration (as installed in the TuCSoN node who
     * released this ACC).
     *
     * @param username the username of the agent
     * @param password the password of the agent
     * @return {@code true} or {@code false} depending on login success/failure
     * @throws TucsonOperationNotPossibleException if the requested TuCSoN operation cannot be performed
     * @throws UnreachableNodeException            if the TuCSoN node target of this operation is not
     *                                             network-reachable
     * @throws OperationTimeOutException           if the operation timeout expired prior to operation
     *                                             completion
     */
    boolean login(final String username, final String password)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException;

    /**
     * Requests to play the default role according to RBAC configuration (as
     * installed in the TuCSoN node who released this ACC) and to the requesting
     * agent class.
     *
     * @return the ACC configured so as to enable and constrain the default role
     * @throws TucsonOperationNotPossibleException if the requested TuCSoN operation cannot be performed
     * @throws UnreachableNodeException            if the TuCSoN node target of this operation is not
     *                                             network-reachable
     * @throws OperationTimeOutException           if the operation timeout expired prior to operation
     *                                             completion
     * @throws TucsonInvalidAgentIdException       if the requesting agent ID is not a valid TuCSoN agent ID
     */
    EnhancedACC playDefaultRole() throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException,
            TucsonInvalidAgentIdException;

    /**
     * Requests to play the given role according to RBAC configuration (as
     * installed in the TuCSoN node who released this ACC) and to the requesting
     * agent class.
     *
     * @param roleName the name of the role to play
     * @return the ACC configured so as to enable and constrain the requested
     * role, or nothing if the agent is not allowed to play such role
     * @throws TucsonOperationNotPossibleException if the requested TuCSoN operation cannot be performed
     * @throws UnreachableNodeException            if the TuCSoN node target of this operation is not
     *                                             network-reachable
     * @throws OperationTimeOutException           if the operation timeout expired prior to operation
     *                                             completion
     * @throws TucsonInvalidAgentIdException       if the requesting agent ID is not a valid TuCSoN agent ID
     * @throws AgentNotAllowedException            if the requesting agent is not allowed to request this TuCSoN
     *                                             operation
     */
    EnhancedACC playRole(final String roleName) //TODO non sarebbe meglio avere come parametro un oggetto Role???
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException,
            TucsonInvalidAgentIdException, AgentNotAllowedException;

    /**
     * Requests to play the given role according to RBAC configuration (as
     * installed in the TuCSoN node who released this ACC) and to the requesting
     * agent class, waiting {@code timeout} milliseconds at most for operation
     * completion.
     *
     * @param roleName the name of the role to play
     * @param timeout  the maximum waiting time in milliseconds
     * @return the ACC configured so as to enable and constrain the requested
     * role, or nothing if the agent is not allowed to play such role or
     * if the timeout expires
     * @throws TucsonOperationNotPossibleException if the requested TuCSoN operation cannot be performed
     * @throws UnreachableNodeException            if the TuCSoN node target of this operation is not
     *                                             network-reachable
     * @throws OperationTimeOutException           if the operation timeout expired prior to operation
     *                                             completion
     * @throws TucsonInvalidAgentIdException       if the requesting agent ID is not a valid TuCSoN agent ID
     * @throws AgentNotAllowedException            if the requesting agent is not allowed to request this TuCSoN
     *                                             operation
     */
    EnhancedACC playRole(final String roleName, final Long timeout) //TODO {come sopra}
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException,
            TucsonInvalidAgentIdException, AgentNotAllowedException;

    /**
     * Requests to play a role given a set of desired permissions, according to
     * RBAC configuration (as installed in the TuCSoN node who released this
     * ACC) and to the requesting agent class.
     *
     * @param permNames the set of desired permission names
     * @return the ACC configured so as to enable and constrain AT LEAST what
     * requested, or nothing if no role exists satisfying agent's
     * request
     * @throws TucsonOperationNotPossibleException if the requested TuCSoN operation cannot be performed
     * @throws UnreachableNodeException            if the TuCSoN node target of this operation is not
     *                                             network-reachable
     * @throws OperationTimeOutException           if the operation timeout expired prior to operation
     *                                             completion
     * @throws TucsonInvalidAgentIdException       if the requesting agent ID is not a valid TuCSoN agent ID
     * @throws AgentNotAllowedException            if the requesting agent is not allowed to request this TuCSoN
     *                                             operation
     */
    EnhancedACC playRoleWithPermissions(final List<String> permNames) //TODO meglio lista di Permisssion??
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException,
            TucsonInvalidAgentIdException, AgentNotAllowedException;

    /**
     * Requests to play a role given a set of desired permissions, according to
     * RBAC configuration (as installed in the TuCSoN node who released this
     * ACC) and to the requesting agent class, waiting {@code timeout}
     * milliseconds at most for operation completion.
     *
     * @param permNames the set of desired permission names
     * @param timeout   the maximum waiting time in milliseconds
     * @return the ACC configured so as to enable and constrain AT LEAST what
     * requested, or nothing if no role exists satisfying agent's
     * request or if the timeout expires
     * @throws TucsonOperationNotPossibleException if the requested TuCSoN operation cannot be performed
     * @throws UnreachableNodeException            if the TuCSoN node target of this operation is not
     *                                             network-reachable
     * @throws OperationTimeOutException           if the operation timeout expired prior to operation
     *                                             completion
     * @throws TucsonInvalidAgentIdException       if the requesting agent ID is not a valid TuCSoN agent ID
     * @throws AgentNotAllowedException            if the requesting agent is not allowed to request this TuCSoN
     *                                             operation
     */
    EnhancedACC playRoleWithPermissions(final List<String> permNames, final Long timeout) //TODO {come sopra}
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException,
            TucsonInvalidAgentIdException, AgentNotAllowedException;

}
