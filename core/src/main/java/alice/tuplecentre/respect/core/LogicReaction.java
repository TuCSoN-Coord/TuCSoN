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
package alice.tuplecentre.respect.core;

import java.util.AbstractMap;
import java.util.LinkedHashMap;

import alice.tuprolog.Struct;
import alice.tuprolog.Term;
import alice.tuprolog.Var;

/**
 * Defines the reaction type managed by ReSpect Reactor (logic reactions).
 *
 * @author Alessandro Ricci
 */
public class LogicReaction implements alice.tuplecentre.core.Reaction,
        java.io.Serializable {

    private static final long serialVersionUID = -930986977792219715L;
    private Struct reaction;

    /**
     *
     */
    public LogicReaction() {
        /*
         *
         */
    }

    /**
     *
     * @param t
     *            the Prolog struct representing this reaction
     */
    public LogicReaction(final Struct t) {
        final AbstractMap<Var, Var> v = new LinkedHashMap<Var, Var>();
        this.reaction = (Struct) t.copyGoal(v, 0);
    }

    /**
     *
     * @return the Prolog term representing the trigger event
     */
    public Term getReactionTerm() {
        return this.reaction.getArg(0);
    }

    /**
     *
     * @return the Prolog struct representing this reaction
     */
    public Struct getStructReaction() {
        return this.reaction;
    }

    @Override
    public String toString() {
        // this.reaction.resolveTerm();
        return this.reaction.toString();
    }
}
