/*
 * Tuple Centre Framework - aliCE team at deis.unibo.it This library is free
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
package alice.tuplecentre.api;

/**
 *
 * @author Alessandro Ricci
 *
 */
public interface ObservableEventListener {

    /**
     *
     * @param tid
     *            the identifier of the tuple centre under observation
     * @param id
     *            the identifier of the requestor of the observed operaion
     * @param spec
     *            the ReSpecT specification argument of the observed operation
     */
    void getSpecCompleted(TupleCentreIdentifier tid, EmitterIdentifier id, String spec);

    /**
     *
     * @param tid
     *            the identifier of the tuple centre under observation
     * @param id
     *            the identifier of the requestor of the observed operaion
     */
    void getSpecRequested(TupleCentreIdentifier tid, EmitterIdentifier id);

    /**
     *
     * @param tid
     *            the identifier of the tuple centre under observation
     * @param id
     *            the identifier of the requestor of the observed operaion
     * @param t
     *            the tuple argument of the observed operation
     */
    void inCompleted(TupleCentreIdentifier tid, EmitterIdentifier id, Tuple t);

    /**
     *
     * @param tid
     *            the identifier of the tuple centre under observation
     * @param id
     *            the identifier of the requestor of the observed operaion
     * @param t
     *            the tuple argument of the observed operation
     */
    void inpCompleted(TupleCentreIdentifier tid, EmitterIdentifier id, Tuple t);

    /**
     *
     * @param tid
     *            the identifier of the tuple centre under observation
     * @param id
     *            the identifier of the requestor of the observed operaion
     * @param t
     *            the tuple argument of the observed operation
     */
    void inpRequested(TupleCentreIdentifier tid, EmitterIdentifier id, TupleTemplate t);

    /**
     *
     * @param tid
     *            the identifier of the tuple centre under observation
     * @param id
     *            the identifier of the requestor of the observed operaion
     * @param t
     *            the tuple argument of the observed operation
     */
    void inRequested(TupleCentreIdentifier tid, EmitterIdentifier id, TupleTemplate t);

    /**
     *
     * @param tid
     *            the identifier of the tuple centre under observation
     * @param id
     *            the identifier of the requestor of the observed operaion
     * @param t
     *            the tuple argument of the observed operation
     */
    void outRequested(TupleCentreIdentifier tid, EmitterIdentifier id, Tuple t);

    /**
     *
     * @param tid
     *            the identifier of the tuple centre under observation
     * @param id
     *            the identifier of the requestor of the observed operaion
     * @param t
     *            the tuple argument of the observed operation
     */
    void rdCompleted(TupleCentreIdentifier tid, EmitterIdentifier id, Tuple t);

    /**
     *
     * @param tid
     *            the identifier of the tuple centre under observation
     * @param id
     *            the identifier of the requestor of the observed operaion
     * @param t
     *            the tuple argument of the observed operation
     */
    void rdpCompleted(TupleCentreIdentifier tid, EmitterIdentifier id, Tuple t);

    /**
     *
     * @param tid
     *            the identifier of the tuple centre under observation
     * @param id
     *            the identifier of the requestor of the observed operaion
     * @param t
     *            the tuple argument of the observed operation
     */
    void rdpRequested(TupleCentreIdentifier tid, EmitterIdentifier id, TupleTemplate t);

    /**
     *
     * @param tid
     *            the identifier of the tuple centre under observation
     * @param id
     *            the identifier of the requestor of the observed operaion
     * @param t
     *            the tuple argument of the observed operation
     */
    void rdRequested(TupleCentreIdentifier tid, EmitterIdentifier id, TupleTemplate t);

    /**
     *
     * @param tid
     *            the identifier of the tuple centre under observation
     * @param id
     *            the identifier of the requestor of the observed operaion
     */
    void setSpecCompleted(TupleCentreIdentifier tid, EmitterIdentifier id);

    /**
     *
     * @param tid
     *            the identifier of the tuple centre under observation
     * @param id
     *            the identifier of the requestor of the observed operaion
     * @param spec
     *            the ReSpecT specification argument of the observed operation
     */
    void setSpecRequested(TupleCentreIdentifier tid, EmitterIdentifier id, String spec);
}
