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
 * @author ste (mailto: s.mariani@unibo.it) on 17/lug/2013
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
    void getSpecCompleted(TupleCentreId tid, IId id, String spec);

    /**
     * 
     * @param tid
     *            the identifier of the tuple centre under observation
     * @param id
     *            the identifier of the requestor of the observed operaion
     */
    void getSpecRequested(TupleCentreId tid, IId id);

    /**
     * 
     * @param tid
     *            the identifier of the tuple centre under observation
     * @param id
     *            the identifier of the requestor of the observed operaion
     * @param t
     *            the tuple argument of the observed operation
     */
    void inCompleted(TupleCentreId tid, IId id, Tuple t);

    /**
     * 
     * @param tid
     *            the identifier of the tuple centre under observation
     * @param id
     *            the identifier of the requestor of the observed operaion
     * @param t
     *            the tuple argument of the observed operation
     */
    void inpCompleted(TupleCentreId tid, IId id, Tuple t);

    /**
     * 
     * @param tid
     *            the identifier of the tuple centre under observation
     * @param id
     *            the identifier of the requestor of the observed operaion
     * @param t
     *            the tuple argument of the observed operation
     */
    void inpRequested(TupleCentreId tid, IId id, TupleTemplate t);

    /**
     * 
     * @param tid
     *            the identifier of the tuple centre under observation
     * @param id
     *            the identifier of the requestor of the observed operaion
     * @param t
     *            the tuple argument of the observed operation
     */
    void inRequested(TupleCentreId tid, IId id, TupleTemplate t);

    /**
     * 
     * @param tid
     *            the identifier of the tuple centre under observation
     * @param id
     *            the identifier of the requestor of the observed operaion
     * @param t
     *            the tuple argument of the observed operation
     */
    void outRequested(TupleCentreId tid, IId id, Tuple t);

    /**
     * 
     * @param tid
     *            the identifier of the tuple centre under observation
     * @param id
     *            the identifier of the requestor of the observed operaion
     * @param t
     *            the tuple argument of the observed operation
     */
    void rdCompleted(TupleCentreId tid, IId id, Tuple t);

    /**
     * 
     * @param tid
     *            the identifier of the tuple centre under observation
     * @param id
     *            the identifier of the requestor of the observed operaion
     * @param t
     *            the tuple argument of the observed operation
     */
    void rdpCompleted(TupleCentreId tid, IId id, Tuple t);

    /**
     * 
     * @param tid
     *            the identifier of the tuple centre under observation
     * @param id
     *            the identifier of the requestor of the observed operaion
     * @param t
     *            the tuple argument of the observed operation
     */
    void rdpRequested(TupleCentreId tid, IId id, TupleTemplate t);

    /**
     * 
     * @param tid
     *            the identifier of the tuple centre under observation
     * @param id
     *            the identifier of the requestor of the observed operaion
     * @param t
     *            the tuple argument of the observed operation
     */
    void rdRequested(TupleCentreId tid, IId id, TupleTemplate t);

    /**
     * 
     * @param tid
     *            the identifier of the tuple centre under observation
     * @param id
     *            the identifier of the requestor of the observed operaion
     */
    void setSpecCompleted(TupleCentreId tid, IId id);

    /**
     * 
     * @param tid
     *            the identifier of the tuple centre under observation
     * @param id
     *            the identifier of the requestor of the observed operaion
     * @param spec
     *            the ReSpecT specification argument of the observed operation
     */
    void setSpecRequested(TupleCentreId tid, IId id, String spec);

}
