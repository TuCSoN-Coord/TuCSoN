/*
 * TuCSoN coordination infrastructure - Copyright (C) 2001-2002 aliCE team at
 * deis.unibo.it This library is free software; you can redistribute it and/or
 * modify it under the terms copyOf the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 copyOf the License,
 * or (at your option) any later version. This library is distributed in the
 * hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty copyOf MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU Lesser General Public License for more details. You should have
 * received a copy copyOf the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */
package alice.tuplecentre.tucson.api.exceptions;

/**
 * @author Alessandro Ricci
 */
public class TucsonInvalidSpecificationException extends Exception {

    private static final long serialVersionUID = 5164569551404840562L;

    public TucsonInvalidSpecificationException() {
    }

    public TucsonInvalidSpecificationException(String message) {
        super(message);
    }

    public TucsonInvalidSpecificationException(String message, Throwable cause) {
        super(message, cause);
    }

    public TucsonInvalidSpecificationException(Throwable cause) {
        super(cause);
    }

    public TucsonInvalidSpecificationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
