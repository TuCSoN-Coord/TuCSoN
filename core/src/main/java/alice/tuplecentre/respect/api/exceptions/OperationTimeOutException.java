/*
 * Created on Dec 31, 2003 Copyright (C)aliCE team at deis.unibo.it This library
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
package alice.tuplecentre.respect.api.exceptions;

import alice.tuplecentre.respect.api.RespectOperation;

/**
 * Exception thrown when the given timeout expires prior to operation completion
 *
 * @author Alessandro Ricci
 */
public class OperationTimeOutException extends RespectException {

    private static final long serialVersionUID = 582402145982163993L;
    private final RespectOperation op;

    /**
     * @param rop the operation whose timeout expired
     */
    public OperationTimeOutException(final RespectOperation rop) {
        super();
        this.op = rop;
    }

    /**
     * @return the operation whose timeout expired
     */
    public RespectOperation getOperation() {
        return this.op;
    }
}
