/*
 * Tuple Centre  Framework - aliCE team at deis.unibo.it
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package alice.tuplecentre.api;

public interface ObservableEventListener {

	void in_requested(TupleCentreId tid, IId id, TupleTemplate t);
	void in_completed(TupleCentreId tid, IId id, Tuple t);
	void rd_requested(TupleCentreId tid, IId id, TupleTemplate t);
	void rd_completed(TupleCentreId tid, IId id, Tuple t);
	void out_requested(TupleCentreId tid, IId id, Tuple t);
	void inp_requested(TupleCentreId tid, IId id, TupleTemplate t);
	void inp_completed(TupleCentreId tid, IId id, Tuple t);
	void rdp_requested(TupleCentreId tid, IId id, TupleTemplate t);
	void rdp_completed(TupleCentreId tid, IId id, Tuple t);

	void setSpec_requested(TupleCentreId tid, IId id, String spec);
	void setSpec_completed(TupleCentreId tid, IId id);
	void getSpec_requested(TupleCentreId tid, IId id);
	void getSpec_completed(TupleCentreId tid, IId id, String spec);
}
