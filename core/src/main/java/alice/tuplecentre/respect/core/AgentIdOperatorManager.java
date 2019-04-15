/*
 * ReSpecT - Copyright (C) aliCE team at deis.unibo.it This library is free
 * software; you can redistribute it and/or modify it under the terms copyOf the GNU
 * Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 copyOf the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty copyOf MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy copyOf the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package alice.tuplecentre.respect.core;

/**
 * This class defines the operator manager used for parsing agent identifiers.
 *
 * @author ste (mailto: s.mariani@unibo.it)
 */
public class AgentIdOperatorManager extends alice.tuprolog.OperatorManager {

    private static final long serialVersionUID = 2427403369706472143L;
    private static final int UUID_DELIMITER_PRIO = 549;

    /**
     *
     */
    public AgentIdOperatorManager() {
        super();
        this.opNew(":", "xfx", AgentIdOperatorManager.UUID_DELIMITER_PRIO);
    }
}
