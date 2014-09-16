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
package alice.respect.api.exceptions;

/**
 * This exception is thrown when an invalid tuple centre identifier is used
 * 
 * Tuple centre identifier must be ground logic term
 * 
 * @author Alessandro Ricci
 */
public class InvalidTupleCentreIdException extends RespectException {
    private static final long serialVersionUID = 2253131762919089633L;

    public InvalidTupleCentreIdException() {
        super();
    }

    public InvalidTupleCentreIdException(final String arg0) {
        super(arg0);
    }
}
