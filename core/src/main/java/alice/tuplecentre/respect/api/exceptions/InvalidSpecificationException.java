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
 * Exception thrown when an invalid ReSpecT specification is created or used
 *
 * @author Alessandro Ricci
 */
public class InvalidSpecificationException extends RespectException {

    private static final long serialVersionUID = -7960971278428173271L;

    public InvalidSpecificationException() {
        super();
    }

    public InvalidSpecificationException(final String arg0) {
        super(arg0);
    }
}
