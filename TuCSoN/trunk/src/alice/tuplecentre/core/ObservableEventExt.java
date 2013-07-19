/*
 * ReSpecT Framework - aliCE team at deis.unibo.it This library is free
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
package alice.tuplecentre.core;

/**
 * 
 * @author ste (mailto: s.mariani@unibo.it) on 17/lug/2013
 * 
 */
public class ObservableEventExt extends InspectableEvent {

    /**
     * 
     */
    public static final int TYPE_REACTIONFAIL = 1001;
    /**
     * 
     */
    public static final int TYPE_REACTIONOK = 1000;
    private static final long serialVersionUID = -2903815349443988799L;

    /**
     * 
     * @param src
     *            the source of the event
     * @param code
     *            the type code of the event
     */
    public ObservableEventExt(final Object src, final int code) {
        super(src, code);
    }

}
