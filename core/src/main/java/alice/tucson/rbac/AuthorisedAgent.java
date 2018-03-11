package alice.tucson.rbac;

import java.io.Serializable;

/**
 * Interface representing an agent authorised by TuCSoN to partecipate the
 * system, according to the installed RBAC (Role-Based Access Control) structure.
 *
 * @author Emanuele Buccelli
 * @author (contributor) Stefano Mariani (mailto: s.mariani@unibo.it)
 */
public interface AuthorisedAgent extends Serializable {

    /**
     * Gets the class associated to the authorised agent according to the
     * installed RBAC structure
     *
     * @return the agent class
     */
    String getAgentClass();

    /**
     * Gets the (encrypted) password used by the authorised agent
     *
     * @return the (encrypted) password
     */
    String getPassword();

    /**
     * Gets the user name used by the authorised agent
     *
     * @return the user name
     */
    String getUsername();

}
