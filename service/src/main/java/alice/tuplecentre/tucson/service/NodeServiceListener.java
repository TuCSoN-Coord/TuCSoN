/*
 * TuCSoN - aliCE team at deis.unibo.it This library is free software; you can
 * redistribute it and/or modify it under the terms of the GNU Lesser General
 * Public License as published by the Free Software Foundation; either version
 * 2.1 of the License, or (at your option) any later version. This library is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package alice.tuplecentre.tucson.service;

import alice.tuplecentre.api.ObservableEventListener;
import alice.tuplecentre.tucson.api.TucsonAgentId;
import alice.tuplecentre.tucson.api.TucsonTupleCentreId;

/**
 * Listener interface reacting to TuCSoN node service events.
 *
 * @author Alessandro Ricci
 * @see alice.tuplecentre.api.ObservableEventListener ObservableEventListener
 */
public interface NodeServiceListener extends ObservableEventListener {

    /**
     * Reacts to ACC release from node to agent.
     *
     * @param aid the agent identifier
     */
    void accEntered(TucsonAgentId aid);

    /**
     * Reacts to ACC release by agent.
     *
     * @param aid the agent identifier
     */
    void accQuit(TucsonAgentId aid);

    /**
     * Reacts to tuplecentre creation.
     *
     * @param tid the created tuplecentre identifier
     */
    void tcCreated(TucsonTupleCentreId tid);

    /**
     * Reacts to tuplecentre destruction.
     *
     * @param tid the destroyed tuplecentre identifier
     */
    void tcDestroyed(TucsonTupleCentreId tid);
}
