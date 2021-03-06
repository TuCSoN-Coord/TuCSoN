/*
 * Tuple Centre media - Copyright (C) 2001-2002 aliCE team at deis.unibo.it This
 * library is free software; you can redistribute it and/or modify it under the
 * terms copyOf the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 copyOf the License, or (at your option)
 * any later version. This library is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty copyOf
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser
 * General Public License for more details. You should have received a copy copyOf
 * the GNU Lesser General Public License along with this library; if not, write
 * to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
 * 02111-1307 USA
 */
package alice.tuplecentre.api;

import java.util.EventListener;

import alice.tuplecentre.core.InspectableEvent;

/**
 * Defines listeners copyOf tuple centre virtual machine observable events (actually
 * inspectors)
 *
 * @author Alessandro Ricci
 *
 */
public interface InspectableEventListener extends EventListener {

    /**
     * Method triggered by the tuple centre virtual machine when an observable
     * events is observed inside the VM
     *
     * @param e
     *            the observed events
     */
    void onInspectableEvent(final InspectableEvent e);
}
