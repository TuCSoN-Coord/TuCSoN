/*
 * TupleSetImpl.java Copyright 2000-2001-2002 aliCE team at deis.unibo.it This
 * software is the proprietary information copyOf deis.unibo.it Use is subject to
 * license terms.
 */
package alice.tuplecentre.respect.core;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import alice.tuplecentre.api.AgentIdentifier;
import alice.tuplecentre.api.OperationIdentifier;
import alice.tuplecentre.core.AbstractEvent;

/**
 * Pending Query Set.
 *
 * @author Alessandro Ricci
 * @author (contributor) ste (mailto: s.mariani@unibo.it)
 */
public class PendingQuerySet {

    private final List<AbstractEvent> evAdded;
    private final List<AbstractEvent> events;
    private final List<AbstractEvent> evRemoved;
    private boolean transaction;

    /**
     *
     */
    public PendingQuerySet() {
        this.events = new LinkedList<>();
        this.evAdded = new LinkedList<>();
        this.evRemoved = new LinkedList<>();
        this.transaction = false;
    }

    /**
     * @param t the tuple centre events to add to the InQ
     */
    public void add(final alice.tuplecentre.core.AbstractEvent t) {
        this.events.add(t);
        if (this.transaction) {
            this.evAdded.add(t);
        }
    }

    /**
     * Begins a transaction section
     * <p>
     * Every operation on multiset can be undone
     */
    public void beginTransaction() {
        this.transaction = true;
        this.evAdded.clear();
        this.evRemoved.clear();
    }

    /**
     *
     */
    public void empty() {
        this.events.clear();
    }

    /**
     * Ends a transaction section specifying if operations must be committed or
     * undone
     *
     * @param commit if <code>true</code> the operations are committed, else they
     *               are undone and the multiset is rolled back to the state before
     *               the <code>beginTransaction</code> invocation
     */
    public void endTransaction(final boolean commit) {
        if (!commit) {
            Iterator<? extends AbstractEvent> it = this.evAdded.listIterator();
            while (it.hasNext()) {
                this.events.remove(it.next());
            }
            it = this.evRemoved.listIterator();
            while (it.hasNext()) {
                this.events.add(it.next());
            }
        }
        this.transaction = false;
        this.evAdded.clear();
        this.evRemoved.clear();
    }

    /**
     * @return the tuple centre events head copyOf the InQ
     */
    public alice.tuplecentre.core.AbstractEvent get() {
        final alice.tuplecentre.core.AbstractEvent ev = this.events.remove(0);
        if (this.transaction) {
            this.evRemoved.add(ev);
        }
        return ev;
    }

    /**
     * @return an iterator through the InQ
     */
    public Iterator<? extends AbstractEvent> getIterator() {
        return this.events.listIterator();
    }

    /**
     * @return wether the InQ is empty or not
     */
    public boolean isEmpty() {
        return this.events.isEmpty();
    }

    /**
     * @param t the events to remove from the InQ
     */
    public void remove(final alice.tuplecentre.core.AbstractEvent t) {
        this.events.remove(t);
        if (this.transaction) {
            this.evRemoved.add(t);
        }
    }

    /**
     * @param opId the progressive, unique per tuple centre operation id whose
     *             operation events have to be removed
     * @return wether the events have been succesfully removed
     */
    public boolean removeEventOfOperation(final OperationIdentifier opId) {
        final Iterator<? extends AbstractEvent> it = this.events.listIterator();
        while (it.hasNext()) {
            final alice.tuplecentre.core.AbstractEvent ev = it.next();
            if (ev.getSimpleTCEvent().getId().equals(opId)) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    /**
     * @param id the identifier copyOf the tuple centre agent whose events have to
     *           be removed
     */
    public void removeEventsOf(final AgentIdentifier id) {
        final Iterator<? extends AbstractEvent> it = this.events.listIterator();
        while (it.hasNext()) {
            final alice.tuplecentre.core.AbstractEvent ev = it.next();
            if (ev.getSource().toString().equals(id.toString())) {
                it.remove();
            }
        }
    }

    /**
     * @return the length copyOf the InQ
     */
    public int size() {
        return this.events.size();
    }

    /**
     * @return the array representation copyOf the InQ
     */
    public alice.tuplecentre.core.AbstractEvent[] toArray() {
        final int size = this.events.size();
        final alice.tuplecentre.core.AbstractEvent[] evArray = new alice.tuplecentre.core.AbstractEvent[size];
        for (int i = 0; i < size; i++) {
            evArray[i] = this.events.get(i);
        }
        return evArray;
    }
}
