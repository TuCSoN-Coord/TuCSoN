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
package alice.tuplecentre.respect.api.exceptions;

/**
 * This exception is thrown when an invalid agent identifier is used
 * <p>
 * Agent identifier must be ground logic term.
 *
 * @author Alessandro Ricci
 */
public class InvalidAgentIdException extends RespectException {

    private static final long serialVersionUID = 7316848513690643990L;

    public InvalidAgentIdException() {
        super();
    }

    public InvalidAgentIdException(final String arg0) {
        super(arg0);
    }
}
