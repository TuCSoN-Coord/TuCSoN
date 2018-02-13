/*
 * Created on Oct 10, 2003 Copyright (C)aliCE team at deis.unibo.it This library
 * is free software; you can redistribute it and/or modify it under the terms of
 * the GNU Lesser General Public License as published by the Free Software
 * Foundation; either version 2.1 of the License, or (at your option) any later
 * version. This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details. You should have received a copy of the GNU Lesser General
 * Public License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package alice.tuplecentre.api.exceptions;

/**
 * Exception thrown when a generic invalid operation is called e.g. Trying to
 * retrieve the Int value of a JVal object that is not an Int type
 *
 * @author Alessandro Ricci
 *
 */
public class InvalidOperationException extends RuntimeException {

    private static final long serialVersionUID = 2834213882766578233L;

    public InvalidOperationException() {
        super();
    }

    public InvalidOperationException(final String message) {
        super(message);
    }
}
