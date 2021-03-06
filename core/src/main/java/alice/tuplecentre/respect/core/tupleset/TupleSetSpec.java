/*
 * ReSpecT - Copyright (C) aliCE team at deis.unibo.it This library is free
 * software; you can redistribute it and/or modify it under the terms copyOf the GNU
 * Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 copyOf the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty copyOf MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy copyOf the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package alice.tuplecentre.respect.core.tupleset;

import java.util.LinkedList;

import alice.tuple.logic.LogicTuple;
import alice.tuple.logic.TupleArgument;
import alice.tuplecentre.respect.core.collection.DoubleKeyMVMap;

/**
 * Class representing a Tuple Set.
 *
 * @author Saverio Cicora
 */
public class TupleSetSpec extends AbstractTupleSet {

    // TODO CICORA: ha senso avere due reaction perfettamente uguali nella
    // lista?
    // due normali tuple devono rimanere distinte anche se sono identiche, ma
    // per le reaction potrebbe essere un problema.

    /**
     *
     */
    public TupleSetSpec() {
        super();
        this.tuples = new DoubleKeyMVMap<>();
        this.tAdded = new LinkedList<>();
        this.tRemoved = new LinkedList<>();
        this.transaction = false;
    }

    @Override
    public String getTupleKey1(final LogicTuple t) {
        final TupleArgument event = t.getArg(0);
        return event.getPredicateIndicator();
    }

    @Override
    public String getTupleKey2(final LogicTuple t) {
        final TupleArgument eventArg = t.getArg(0).getArg(0);
        if (eventArg.isNumber()) {
            return eventArg.toString();
        } else if (eventArg.isVar()) {
            return "VAR";
        } else {
            return eventArg.getPredicateIndicator();
        }
    }
}
