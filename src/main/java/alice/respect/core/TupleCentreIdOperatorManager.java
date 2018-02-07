/*
 * ReSpecT - Copyright (C) aliCE team at deis.unibo.it This library is free
 * software; you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package alice.respect.core;

/**
 * This class defines the operator manager used for parsing tuple centre
 * identifiers.
 *
 * @author Alessandro Ricci
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 */
public class TupleCentreIdOperatorManager extends
        alice.tuprolog.OperatorManager {

    private static final int IP_DELIMITER_PRIO = 548;
    private static final int NETID_DELIMITER_PRIO = 550;
    private static final int PORT_DELIMITER_PRIO = 549;
    private static final long serialVersionUID = 1319340757322429430L;

    /**
     *
     */
    public TupleCentreIdOperatorManager() {
        super();
        this.opNew("@", "xfx",
                TupleCentreIdOperatorManager.NETID_DELIMITER_PRIO);
        this.opNew(":", "xfx", TupleCentreIdOperatorManager.PORT_DELIMITER_PRIO);
        this.opNew(".", "xfx", TupleCentreIdOperatorManager.IP_DELIMITER_PRIO);
    }
}
