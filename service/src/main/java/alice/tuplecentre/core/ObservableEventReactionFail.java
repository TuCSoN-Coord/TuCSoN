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
 * @author Alessandro Ricci
 *
 */
public class ObservableEventReactionFail extends ObservableEventExt {

    private static final long serialVersionUID = 5378208360215769012L;
    private TriggeredReaction z;

    /**
     *
     * @param src
     *            the source of the events
     * @param tr
     *            the triggered reaction
     */
    public ObservableEventReactionFail(final Object src,
            final TriggeredReaction tr) {
        super(src, ObservableEventExt.TYPE_REACTIONFAIL);
        this.z = tr;
    }

    /**
     * @return the z
     */
    public TriggeredReaction getZ() {
        return this.z;
    }

    /**
     * @param tr
     *            the z to set
     */
    public void setZ(final TriggeredReaction tr) {
        this.z = tr;
    }
}
