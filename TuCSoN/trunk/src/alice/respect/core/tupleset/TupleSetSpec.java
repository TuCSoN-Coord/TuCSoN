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
package alice.respect.core.tupleset;

import java.util.LinkedList;

import alice.logictuple.LogicTuple;
import alice.logictuple.TupleArgument;
import alice.logictuple.exceptions.InvalidLogicTupleException;
import alice.logictuple.exceptions.InvalidTupleOperationException;
import alice.respect.core.collection.DoubleKeyMVMap;

/**
 * Class representing a Tuple Set.
 * 
 * @author ste (mailto: s.mariani@unibo.it) on 22/lug/2013
 * 
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
        this.tuples = new DoubleKeyMVMap<String, String, LogicTuple>();
        this.tAdded = new LinkedList<LTEntry>();
        this.tRemoved = new LinkedList<LTEntry>();
        this.transaction = false;
    }

    @Override
    protected String getTupleKey1(final LogicTuple t)
            throws alice.logictuple.exceptions.InvalidLogicTupleException {
        try {
            final TupleArgument event = t.getArg(0);
            return event.getPredicateIndicator();
        } catch (final InvalidTupleOperationException e) {
            throw new alice.logictuple.exceptions.InvalidLogicTupleException();
        }
    }

    @Override
    protected String getTupleKey2(final LogicTuple t)
            throws InvalidLogicTupleException {
        try {
            final TupleArgument eventArg = t.getArg(0).getArg(0);

            if (eventArg.isNumber()) {
                return eventArg.toString();
            } else if (eventArg.isVar()) {
                return "VAR";
            } else {
                return eventArg.getPredicateIndicator();
            }

        } catch (final InvalidTupleOperationException e) {
            throw new alice.logictuple.exceptions.InvalidLogicTupleException();
        }
    }

}
