package alice.tucson.api;

import java.util.List;
import alice.tucson.api.exceptions.AgentNotAllowedException;
import alice.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tucson.api.exceptions.UnreachableNodeException;
import alice.tucson.rbac.Role;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;

/**
 * (Meta) Agent Coordination Context enabling agents to negotiate an actual ACC,
 * configured according to the installed RBAC structure (if any) so as to allow
 * playing only those roles admissible for the agent, as well as to (un)play
 * allowed roles.
 *
 * @author Emanuele Buccelli
 * @author (contributor) Stefano Mariani (mailto: s.mariani@unibo.it)
 *
 */
public interface NegotiationACC {

    /**
     * Request the list of the roles playable by the requesting agent, according
     * to RBAC configuration (as installed in the TuCSoN node who released this
     * ACC) and to the requesting agent class.
     *
     * @return the set of playable roles
     * @throws TucsonOperationNotPossibleException
     * @throws UnreachableNodeException
     * @throws OperationTimeOutException
     */
    List<Role> listPlayableRoles() throws TucsonOperationNotPossibleException,
    UnreachableNodeException, OperationTimeOutException;

    /**
     * Attempts to perform login, so as to receive the associated agent class,
     * according to RBAC configuration (as installed in the TuCSoN node who
     * released this ACC).
     *
     * @param username
     *            the username of the agent
     * @param password
     *            the password of the agent
     * @return {@code true} or {@code false} depending on login success/failure
     * @throws TucsonOperationNotPossibleException
     * @throws UnreachableNodeException
     * @throws OperationTimeOutException
     */
    boolean login(String username, String password)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException;

    /**
     * Requests to play the default role according to RBAC configuration (as
     * installed in the TuCSoN node who released this ACC) and to the requesting
     * agent class.
     *
     * @return the ACC configured so as to enable and constrain the default role
     * @throws TucsonOperationNotPossibleException
     * @throws UnreachableNodeException
     * @throws OperationTimeOutException
     * @throws TucsonInvalidAgentIdException
     */
    EnhancedACC playDefaultRole() throws TucsonOperationNotPossibleException,
    UnreachableNodeException, OperationTimeOutException,
    TucsonInvalidAgentIdException;

    /**
     * Requests to play the given role according to RBAC configuration (as
     * installed in the TuCSoN node who released this ACC) and to the requesting
     * agent class.
     *
     * @param roleName
     *            the name of the role to play
     * @return the ACC configured so as to enable and constrain the requested
     *         role, or nothing if the agent is not allowed to play such role
     * @throws TucsonOperationNotPossibleException
     * @throws UnreachableNodeException
     * @throws OperationTimeOutException
     * @throws TucsonInvalidAgentIdException
     * @throws AgentNotAllowedException
     */
    EnhancedACC playRole(String roleName)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException,
            TucsonInvalidAgentIdException, AgentNotAllowedException;

    /**
     * Requests to play the given role according to RBAC configuration (as
     * installed in the TuCSoN node who released this ACC) and to the requesting
     * agent class, waiting {@code timeout} milliseconds at most for operation
     * completion.
     *
     * @param roleName
     *            the name of the role to play
     * @param timeout
     *            the maximum waiting time in milliseconds
     * @return the ACC configured so as to enable and constrain the requested
     *         role, or nothing if the agent is not allowed to play such role or
     *         if the timeout expires
     * @throws TucsonOperationNotPossibleException
     * @throws UnreachableNodeException
     * @throws OperationTimeOutException
     * @throws TucsonInvalidAgentIdException
     * @throws AgentNotAllowedException
     */
    EnhancedACC playRole(String roleName, Long timeout)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException,
            TucsonInvalidAgentIdException, AgentNotAllowedException;

    /**
     * Requests to play a role given a set of desired permissions, according to
     * RBAC configuration (as installed in the TuCSoN node who released this
     * ACC) and to the requesting agent class.
     *
     * @param permNames
     *            the set of desired permission names
     * @return the ACC configured so as to enable and constrain AT LEAST what
     *         requested, or nothing if no role exists satisfying agent's
     *         request
     * @throws TucsonOperationNotPossibleException
     * @throws UnreachableNodeException
     * @throws OperationTimeOutException
     * @throws TucsonInvalidAgentIdException
     * @throws AgentNotAllowedException
     */
    EnhancedACC playRoleWithPermissions(List<String> permNames)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException,
            TucsonInvalidAgentIdException, AgentNotAllowedException;

    /**
     * Requests to play a role given a set of desired permissions, according to
     * RBAC configuration (as installed in the TuCSoN node who released this
     * ACC) and to the requesting agent class, waiting {@code timeout}
     * milliseconds at most for operation completion.
     *
     * @param permNames
     *            the set of desired permission names
     * @param timeout
     *            the maximum waiting time in milliseconds
     * @return the ACC configured so as to enable and constrain AT LEAST what
     *         requested, or nothing if no role exists satisfying agent's
     *         request or if the timeout expires
     * @throws TucsonOperationNotPossibleException
     * @throws UnreachableNodeException
     * @throws OperationTimeOutException
     * @throws TucsonInvalidAgentIdException
     * @throws AgentNotAllowedException
     */
    EnhancedACC playRoleWithPermissions(List<String> permNames, Long timeout)
            throws TucsonOperationNotPossibleException,
            UnreachableNodeException, OperationTimeOutException,
            TucsonInvalidAgentIdException, AgentNotAllowedException;

}
