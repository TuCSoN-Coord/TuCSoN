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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import alice.tuplecentre.core.TriggeredReaction;

/**
 * Triggered Reaction Set.
 *
 * @author Alessandro Ricci
 */
public class TRSet {

    private final List<TriggeredReaction> tAdded;
    private boolean transaction;
    private final List<TriggeredReaction> tRemoved;
    private final List<TriggeredReaction> trigs;

    /**
     *
     */
    public TRSet() {
        this.trigs = new LinkedList<TriggeredReaction>();
        this.tRemoved = new LinkedList<TriggeredReaction>();
        this.tAdded = new LinkedList<TriggeredReaction>();
        this.transaction = false;
    }

    /**
     * @param t the ReSpecT triggered reaction to add to the triggered
     *          reaction set
     */
    public void add(final TriggeredReaction t) {
        this.trigs.add(t);
        if (this.transaction) {
            this.tAdded.add(t);
        }
    }

    /**
     *
     */
    public void beginTransaction() {
        this.transaction = true;
        this.tAdded.clear();
        this.tRemoved.clear();
    }

    /**
     *
     */
    public void empty() {
        this.trigs.clear();
    }

    /**
     * @param commit wether the just terminated transaction has to be successfully
     *               commited or not
     */
    public void endTransaction(final boolean commit) {
        if (!commit) {
            Iterator<TriggeredReaction> it = this.tAdded.listIterator();
            while (it.hasNext()) {
                this.trigs.remove(it.next());
            }
            it = this.tRemoved.listIterator();
            while (it.hasNext()) {
                this.trigs.add(it.next());
            }
        }
        this.transaction = false;
        this.tAdded.clear();
        this.tRemoved.clear();
    }

    /**
     * @return the first triggered reaction of this list
     */
    public TriggeredReaction get() {
        return this.trigs.remove(0);
    }

    /**
     * @return a Java iterator through this triggered reactions list
     */
    public Iterator<? extends TriggeredReaction> getIterator() {
        return this.trigs.listIterator();
    }

    /**
     * @return wether this triggered reactions list is empty or not
     */
    public boolean isEmpty() {
        return this.trigs.isEmpty();
    }

    /**
     * @param t the triggered reaction to remove from this list
     */
    public void remove(final TriggeredReaction t) {
        this.trigs.remove(t);
        if (this.transaction) {
            this.tRemoved.add(t);
        }
    }

    /**
     * @return the Java array representation of this triggered reaction list
     */
    public TriggeredReaction[] toArray() {
        final int size = this.trigs.size();
        final TriggeredReaction[] trArray = new TriggeredReaction[size];
        for (int i = 0; i < size; i++) {
            trArray[i] = this.trigs.get(i);
        }
        return trArray;
    }
}
