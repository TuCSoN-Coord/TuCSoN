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
package alice.tuplecentre.respect.core.tupleset;

import java.util.LinkedList;

import alice.tuple.logic.LogicTuple;
import alice.tuple.logic.TupleArgument;
import alice.tuplecentre.respect.core.collection.DoubleKeyMVMap;

/**
 * @author Saverio Cicora
 */
public class TupleSetCoord extends AbstractTupleSet {

    /**
     *
     */
    public TupleSetCoord() {
        super();
        this.tuples = new DoubleKeyMVMap<String, String, LogicTuple>();
        this.tAdded = new LinkedList<LTEntry>();
        this.tRemoved = new LinkedList<LTEntry>();
        this.transaction = false;
    }

    /**
     * Return the first level key. This key are based on functorn name and
     * arity.
     */
    @Override
    public String getTupleKey1(final LogicTuple t) {
        final TupleArgument ta = t.getVarValue(null);
        if (ta != null && ta.isStruct()) {
            return ta.getPredicateIndicator();
        }
        return t.getPredicateIndicator();
    }

    /**
     * Returns the second level key. This key is based on the first term of the
     * LogicTuple if exist, return an empty String otherwise. The variable are
     * stored whit a special key.
     */
    @Override
    public String getTupleKey2(final LogicTuple t) {
        TupleArgument tArg = t.getVarValue(null);
        // Check if the term as a value assigned to a variable
        if (tArg != null) {
            if (tArg.getArity() > 0) {
                tArg = tArg.getArg(0);
                if (tArg.isNumber()) {
                    // The number are treated as a special case and
                    // are indexed with their string representation
                    return tArg.toString();
                } else if (tArg.isVar()) {
                    return "VAR";
                } else {
                    return tArg.getPredicateIndicator();
                }
            }
            return "";
        } else if (t.getArity() > 0) {
            tArg = t.getArg(0);
            if (tArg.isNumber()) {
                return tArg.toString();
            } else if (tArg.isVar()) {
                return "VAR";
            } else {
                return tArg.getPredicateIndicator();
            }
        } else {
            return "";
        }
    }
}
