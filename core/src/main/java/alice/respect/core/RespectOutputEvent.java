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

import alice.tuplecentre.core.InputEvent;
import alice.tuplecentre.core.OutputEvent;

/**
 * @author Alessandro Ricci
 */
public class RespectOutputEvent extends OutputEvent {

    private static final long serialVersionUID = -3368404448915090722L;
    private final InternalOperation operation;

    /**
     *
     * @param ev
     *            the input events which caused this output events
     * @param op
     *            the internal operation which triggered this output events
     */
    public RespectOutputEvent(final InputEvent ev, final InternalOperation op) {
        super(ev);
        this.operation = op;
    }

    /**
     *
     * @return the internal operation which triggered this output events
     */
    public InternalOperation getRespectOperation() {
        return this.operation;
    }

    @Override
    public String toString() {
        return "[ op: " + this.operation + ", ie: [ "
                + this.inputEvent.toString() + " ] ]";
    }
}
